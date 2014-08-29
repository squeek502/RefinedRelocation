package com.dynious.refinedrelocation.tileentity;

import appeng.api.AEApi;
import appeng.api.config.*;
import appeng.api.networking.*;
import appeng.api.networking.events.MENetworkCellArrayUpdate;
import appeng.api.networking.events.MENetworkChannelsChanged;
import appeng.api.networking.events.MENetworkEventSubscribe;
import appeng.api.networking.events.MENetworkPowerStatusChange;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.networking.security.MachineSource;
import appeng.api.networking.storage.IStorageGrid;
import appeng.api.storage.*;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IItemList;
import appeng.api.util.AECableType;
import appeng.api.util.AEColor;
import appeng.api.util.DimensionalCoord;
import com.dynious.refinedrelocation.api.tileentity.IInventoryChangeListener;
import com.dynious.refinedrelocation.api.tileentity.grid.LocalizedStack;
import com.dynious.refinedrelocation.block.ModBlocks;
import com.dynious.refinedrelocation.helper.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.*;

public class TileMESortingInterface extends TileSortingConnector implements ICellContainer, IGridBlock, IInventoryChangeListener
{
    private IGridNode node = null;
    private IMEInventoryHandler<IAEItemStack> handler;
    private BaseActionSource mySrc = new MachineSource(this);
    private NBTTagCompound data;
    private boolean isReady = false;
    private boolean wasActive = false;
    private boolean shouldUpdateStorage = true;
    private IItemList<IAEItemStack> cachedItemList = AEApi.instance().storage().createItemList();

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        if (!worldObj.isRemote)
        {
            if (!isReady)
            {
                isReady = true;
                getGridNode(null).getGrid().postEvent(new MENetworkCellArrayUpdate());
            }
            if (shouldUpdateStorage)
            {
                shouldUpdateStorage = false;
                updateStorage();
            }
        }
    }

    private void updateStorage()
    {
        System.out.println("Update!");
        List<LocalizedStack> currentStacks = getHandler().getGrid().getItemsInGrid();
        IItemList<IAEItemStack> tempItemList = AEApi.instance().storage().createItemList();

        for (IAEItemStack stack : cachedItemList)
        {
            System.out.println("Cache: " + stack.getItemStack());
            tempItemList.add(stack.copy());
        }

        for (Iterator<IAEItemStack> iterator = tempItemList.iterator(); iterator.hasNext(); )
        {
            IAEItemStack stack = iterator.next();

            for (Iterator<LocalizedStack> iterator1 = currentStacks.iterator(); iterator1.hasNext(); )
            {
                LocalizedStack newStack = iterator1.next();

                if (stack.equals(newStack.STACK))
                {
                    iterator1.remove();

                    stack.setStackSize(stack.getStackSize() - newStack.STACK.stackSize);

                    if (stack.getStackSize() == 0)
                    {
                        iterator.remove();
                        break;
                    }
                }
            }
        }

        IStorageGrid storage = getGridNode(null).getGrid().getCache(IStorageGrid.class);

        for (IAEItemStack stack : tempItemList)
        {
            System.out.println("TempList: " + stack.getItemStack());
            stack.setStackSize(-stack.getStackSize());
            if (stack.getStackSize() < 0)
                removeStackFromCache(stack);
            else
                cachedItemList.add(stack);
            storage.postAlterationOfStoredItems(StorageChannel.ITEMS, stack, mySrc);
        }

        for (LocalizedStack stack : currentStacks)
        {
            System.out.println("New: " + stack.STACK);
            IAEItemStack aeStack = AEApi.instance().storage().createItemStack(stack.STACK);
            cachedItemList.add(aeStack);
            storage.postAlterationOfStoredItems(StorageChannel.ITEMS, aeStack, mySrc);
        }
    }

    private void removeStackFromCache(IAEItemStack stack)
    {
        for (IAEItemStack stack1: cachedItemList)
        {
            if (stack1.equals(stack))
            {
                long amount = Math.min(stack1.getStackSize(), -stack.getStackSize());
                stack1.setStackSize(stack1.getStackSize() - amount);
                stack.setStackSize(stack.getStackSize() + amount);
                if (stack.getStackSize() == 0)
                    return;
            }
        }
    }

    public IMEInventoryHandler<IAEItemStack> getInternalHandler()
    {
        if (handler == null)
        {
            handler = new SortingInventoryHandler(this);
        }
        return handler;
    }

    public IAEItemStack injectItems(IAEItemStack aeItemStack, Actionable actionable, BaseActionSource baseActionSource)
    {
        ItemStack stack = aeItemStack.getItemStack();
        ItemStack returnedStack = null;
        int amount;
        while(aeItemStack.getStackSize() > 0 && returnedStack == null)
        {
            amount = (int) Math.min(64, aeItemStack.getStackSize());
            stack.stackSize = amount;
            returnedStack = getHandler().getGrid().filterStackToGroup(stack, this, 0, actionable != Actionable.MODULATE);
            if (returnedStack != null)
            {
                amount -= returnedStack.stackSize;
            }
            aeItemStack.setStackSize(aeItemStack.getStackSize() - amount);
        }
        if (aeItemStack.getStackSize() == 0)
            return null;

        return aeItemStack;
    }

    public IAEItemStack extractItems(IAEItemStack aeItemStack, Actionable actionable, BaseActionSource baseActionSource)
    {
        ItemStack stackToExtract = aeItemStack.getItemStack();
        int extracted = 0;
        for (LocalizedStack stack : getHandler().getGrid().getItemsInGrid())
        {
            if (ItemStackHelper.areItemStacksEqual(stack.STACK, stackToExtract))
            {
                int amount = Math.min(stack.STACK.stackSize, stackToExtract.stackSize - extracted);
                extracted += amount;
                if (actionable == Actionable.MODULATE)
                {
                    //Post update
                    IAEItemStack aeStack = AEApi.instance().storage().createItemStack(stack.STACK);
                    aeStack.setStackSize(-amount);
                    removeStackFromCache(aeStack);
                    IStorageGrid storage = getGridNode(null).getGrid().getCache(IStorageGrid.class);
                    storage.postAlterationOfStoredItems(StorageChannel.ITEMS, aeStack, mySrc);

                    stack.STACK.stackSize -= amount;
                    if (stack.STACK.stackSize == 0)
                    {
                        stack.INVENTORY.setInventorySlotContents(stack.SLOT, null);
                    }
                }
                if (extracted >= stackToExtract.stackSize)
                {
                    return aeItemStack;
                }
            }
        }
        aeItemStack.setStackSize(extracted);
        return aeItemStack;
    }

    public IItemList<IAEItemStack> getAvailableItems(IItemList<IAEItemStack> aeItemStacks)
    {
        for (IAEItemStack cachedStack : cachedItemList)
        {
            aeItemStacks.add(cachedStack);
        }
        return aeItemStacks;
    }

    public StorageChannel getChannel()
    {
        return StorageChannel.ITEMS;
    }

    @Override
    public IGridNode getGridNode(ForgeDirection direction)
    {
        if (this.node == null && !worldObj.isRemote && isReady)
        {
            this.node = AEApi.instance().createGridNode(this);
            if (data != null)
                node.loadFromNBT("node", this.data);
            this.node.updateState();
        }
        return this.node;
    }

    @Override
    public AECableType getCableConnectionType(ForgeDirection direction)
    {
        return AECableType.COVERED;
    }

    @Override
    public void securityBreak()
    {
        worldObj.func_147480_a(xCoord, yCoord, zCoord, true);
    }

    @Override
    public double getIdlePowerUsage()
    {
        return 0;
    }

    @Override
    public EnumSet<GridFlags> getFlags()
    {
        return EnumSet.of(GridFlags.REQUIRE_CHANNEL);
    }

    @Override
    public boolean isWorldAccessable()
    {
        return true;
    }

    @Override
    public DimensionalCoord getLocation()
    {
        return new DimensionalCoord(this);
    }

    @Override
    public AEColor getGridColor()
    {
        return AEColor.Transparent;
    }

    @Override
    public void onGridNotification(GridNotification gridNotification)
    {
    }

    @Override
    public void setNetworkStatus(IGrid iGrid, int i)
    {

    }

    @Override
    public EnumSet<ForgeDirection> getConnectableSides()
    {
        return EnumSet.allOf(ForgeDirection.class);
    }

    @Override
    public IGridHost getMachine()
    {
        return this;
    }

    @Override
    public void gridChanged()
    {

    }

    @Override
    public ItemStack getMachineRepresentation()
    {
        return new ItemStack(ModBlocks.sortingConnector, 1, 3);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        data = compound;
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        node.saveToNBT("node", compound);
    }

    @Override
    public void invalidate()
    {
        isReady = false;
        if (node != null)
        {
            node.destroy();
            node = null;
        }
        super.invalidate();
    }

    @Override
    public void onChunkUnload()
    {
        isReady = false;
        if (node != null)
        {
            node.destroy();
            node = null;
        }
        super.onChunkUnload();
    }

    @Override
    public List<IMEInventoryHandler> getCellArray(StorageChannel storageChannel)
    {
        if (storageChannel == StorageChannel.ITEMS)
        {
            return Arrays.asList((IMEInventoryHandler) getInternalHandler());
        }
        return Collections.emptyList();
    }

    @Override
    public int getPriority()
    {
        return 0;
    }

    @Override
    public void blinkCell(int i)
    {

    }

    @Override
    public IGridNode getActionableNode()
    {
        return node;
    }

    @Override
    public void onInventoryChanged()
    {
        shouldUpdateStorage = true;
    }

    @MENetworkEventSubscribe
    public void powerRender(MENetworkPowerStatusChange c)
    {
        updateStatus();
    }

    @MENetworkEventSubscribe
    public void updateChannels(MENetworkChannelsChanged chann)
    {
        updateStatus();
    }

    private void updateStatus()
    {
        if (node.isActive() != wasActive)
        {
            wasActive = node.isActive();
            getGridNode(null).getGrid().postEvent(new MENetworkCellArrayUpdate());
        }
    }

    private static class SortingInventoryHandler implements IMEInventoryHandler<IAEItemStack>
    {
        private TileMESortingInterface tile;

        private SortingInventoryHandler(TileMESortingInterface tile)
        {
            this.tile = tile;
        }

        @Override
        public AccessRestriction getAccess()
        {
            return AccessRestriction.READ_WRITE;
        }

        @Override
        public boolean isPrioritized(IAEItemStack iaeStack)
        {
            return false;
        }

        @Override
        public boolean canAccept(IAEItemStack iaeStack)
        {
            return true;
        }

        @Override
        public int getPriority()
        {
            return tile.getPriority();
        }

        @Override
        public int getSlot()
        {
            return 0;
        }

        @Override
        public IAEItemStack injectItems(IAEItemStack iaeStack, Actionable actionable, BaseActionSource baseActionSource)
        {
            return tile.injectItems(iaeStack, actionable, baseActionSource);
        }

        @Override
        public IAEItemStack extractItems(IAEItemStack iaeStack, Actionable actionable, BaseActionSource baseActionSource)
        {
            return tile.extractItems(iaeStack, actionable, baseActionSource);
        }

        @Override
        public IItemList<IAEItemStack> getAvailableItems(IItemList<IAEItemStack> iItemList)
        {
            return tile.getAvailableItems(iItemList);
        }

        @Override
        public StorageChannel getChannel()
        {
            return tile.getChannel();
        }
    }
}