package com.dynious.refinedrelocation.multiblock;

import com.dynious.refinedrelocation.until.Vector2;
import com.dynious.refinedrelocation.until.Vector3;
import net.minecraft.tileentity.TileEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class TileMultiBlockBase extends TileEntity implements IMultiBlockLeader
{
    private int checkCount = 0;
    private boolean isFormed = false;
    protected Map<Integer, Integer> typeIds = new HashMap<Integer, Integer>();

    @Override
    public void updateEntity()
    {
        if (!worldObj.isRemote && shouldAutoCheckFormation())
        {
            if (checkCount <= 0)
            {
                checkCount = 200;
                checkMultiBlock();
            }
            checkCount--;
        }
    }

    private void checkMultiBlock()
    {
        IMultiBlock multiBlock = MultiBlockRegistry.getMultiBlock(getMultiBlockIdentifier());
        Vector3 leaderPos = multiBlock.getRelativeLeaderPos();

        for (int x = 0; x < multiBlock.getMultiBlockMap().getSizeX(); x++)
        {
            for (int y = 0; y < multiBlock.getMultiBlockMap().getSizeY(); y++)
            {
                for (int z = 0; z < multiBlock.getMultiBlockMap().getSizeZ(); z++)
                {
                    Vector2 blockInfo = multiBlock.getMultiBlockMap().getBlockAtPos(x, y, z);

                    if (blockInfo != null)
                    {
                        if (blockInfo.getX() < 0)
                        {
                            List<Vector2> list = multiBlock.getOptionalBlockList(blockInfo.getX());
                            boolean foundBlock = false;
                            for (int i = 0; i < list.size(); i++)
                            {
                                if (checkFormation(list.get(i), xCoord + x - leaderPos.getX(), yCoord + y - leaderPos.getY(), zCoord + z - leaderPos.getZ()))
                                {
                                    foundBlock = true;
                                    typeIds.put(-blockInfo.getX(), i);
                                }
                            }
                            if (!foundBlock)
                            {
                                if (isFormed)
                                {
                                    isFormed = false;
                                    onFormationChange();
                                }
                                return;
                            }
                        }
                        else
                        {
                            if (!checkFormation(blockInfo, xCoord + x - leaderPos.getX(), yCoord + y - leaderPos.getY(), zCoord + z - leaderPos.getZ()))
                            {
                                //UnInit
                                if (isFormed)
                                {
                                    isFormed = false;
                                    onFormationChange();
                                }
                                return;
                            }
                        }
                    }
                }
            }
        }
        //Init
        if (!isFormed)
        {
            isFormed = true;
            onFormationChange();
        }
    }

    private boolean checkFormation(Vector2 blockInfo, int x, int y, int z)
    {
        if (blockInfo.getX() == 0)
        {
            if (!worldObj.isAirBlock(x, y, z))
            {
                return false;
            }
        }
        else if (blockInfo.getY() == -1)
        {
            int id = worldObj.getBlockId(x, y, z);
            if (id != blockInfo.getX())
            {
                return false;
            }
        }
        else
        {
            int id = worldObj.getBlockId(x, y, z);
            int meta = worldObj.getBlockMetadata(x, y, z);
            if (id != blockInfo.getX() || meta != blockInfo.getY())
            {
                return false;
            }
        }
        return true;
    }

    public boolean shouldAutoCheckFormation()
    {
        return true;
    }

    public boolean isFormed(boolean forceRecheck)
    {
        if (forceRecheck)
        {
            forceCheck();
        }
        return isFormed;
    }

    protected void onFormationChange()
    {

    }

    public void forceCheck()
    {
        checkMultiBlock();
    }
}
