package com.dynious.refinedrelocation.tileentity;

import com.dynious.refinedrelocation.api.SortingMemberHandler;
import com.dynious.refinedrelocation.api.ISortingMember;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

public class TileSortingConnector extends TileEntity implements ISortingMember, IDisguisable
{
    private SortingMemberHandler sortingMemberHandler = new SortingMemberHandler(this);
    private boolean isFirstTick = true;

    public Block blockDisguisedAs = null;
    public int blockDisguisedMetadata = 0;

    @Override
    public boolean canDisguise()
    {
        return true;
    }
    
    @Override
    public boolean canDisguiseAs(Block block, int metadata)
    {
        return block.isOpaqueCube();
    }

    @Override
    public Block getDisguise()
    {
        return blockDisguisedAs;
    }

    @Override
    public int getDisguiseMeta()
    {
        return blockDisguisedMetadata;
    }

    @Override
    public void setDisguise(Block block, int metadata)
    {
        blockDisguisedAs = block;
        blockDisguisedMetadata = metadata;
        if (worldObj != null)
            worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }

    @Override
    public void clearDisguise()
    {
        setDisguise(null, 0);
    }

    @Override
    public void updateEntity()
    {
        if (isFirstTick)
        {
            sortingMemberHandler.onTileAdded();
            isFirstTick = false;
        }
        super.updateEntity();
    }

    @Override
    public SortingMemberHandler getSortingMemberHandler()
    {
        return sortingMemberHandler;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        int disguiseBlockId = compound.getInteger("disguisedId");
        if (disguiseBlockId != 0)
        {
            int disguisedMeta = compound.getInteger("disguisedMeta");
            setDisguise(Block.blocksList[disguiseBlockId], disguisedMeta);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        if (blockDisguisedAs != null)
        {
            compound.setInteger("disguisedId", blockDisguisedAs.blockID);
            compound.setInteger("disguisedMeta", blockDisguisedMetadata);
        }
    }

    @Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
    {
        int disguiseBlockId = pkt.data.getInteger("disguisedId");
        if (disguiseBlockId != 0)
        {
            int disguisedMeta = pkt.data.getInteger("disguisedMeta");
            setDisguise(Block.blocksList[disguiseBlockId], disguisedMeta);
        }
    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound compound = new NBTTagCompound();
        if (blockDisguisedAs != null)
        {
            compound.setInteger("disguisedId", blockDisguisedAs.blockID);
            compound.setInteger("disguisedMeta", blockDisguisedMetadata);
        }
        return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, compound);
    }
}
