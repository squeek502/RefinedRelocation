package com.dynious.refinedrelocation.tileentity;

import com.dynious.refinedrelocation.api.APIUtils;
import com.dynious.refinedrelocation.api.filter.IFilter;
import com.dynious.refinedrelocation.api.tileentity.ISortingInventory;
import com.dynious.refinedrelocation.api.tileentity.handlers.ISortingInventoryHandler;
import com.dynious.refinedrelocation.mods.BarrelFilter;
import cpw.mods.fml.common.network.PacketDispatcher;
import mcp.mobius.betterbarrels.common.blocks.TileEntityBarrel;
import mcp.mobius.betterbarrels.network.Packet0x01ContentUpdate;
import net.minecraft.item.ItemStack;

public class TileSortingBarrel extends TileEntityBarrel implements ISortingInventory
{
    public boolean isFirstRun = true;

    private BarrelFilter filter = new BarrelFilter(this);

    private ISortingInventoryHandler sortingInventoryHandler = APIUtils.createSortingInventoryHandler(this);

    @Override
    public void updateEntity()
    {
        if (isFirstRun)
        {
            sortingInventoryHandler.onTileAdded();
            isFirstRun = false;
        }
        super.updateEntity();
    }

    @Override
    public boolean canUpdate()
    {
        return isFirstRun || super.canUpdate();
    }

    @Override
    public final boolean putStackInSlot(ItemStack itemStack, int slotIndex)
    {
        super.setInventorySlotContents(slotIndex, itemStack);
        return true;
    }

    @Override
    public ItemStack putInInventory(ItemStack itemStack)
    {
        int added = getStorage().addStack(itemStack.copy());
        if (added != 0)
        {
            itemStack.stackSize -= added;
            PacketDispatcher.sendPacketToAllInDimension(Packet0x01ContentUpdate.create(this), worldObj.provider.dimensionId);
            if (itemStack.stackSize == 0)
                return null;
        }
        return itemStack;
    }

    @Override
    public void invalidate()
    {
        sortingInventoryHandler.onTileRemoved();
        super.invalidate();
    }

    @Override
    public void onChunkUnload()
    {
        sortingInventoryHandler.onTileRemoved();
        super.onChunkUnload();
    }

    @Override
    public IFilter getFilter()
    {
        return filter;
    }

    @Override
    public Priority getPriority()
    {
        return Priority.NORMAL_HIGH;
    }

    @Override
    public void setPriority(Priority priority)
    {
        //NOOP
    }

    @Override
    public ISortingInventoryHandler getHandler()
    {
        return sortingInventoryHandler;
    }
}