package com.dynious.refinedrelocation.network;

import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.api.IFilterTile;
import com.dynious.refinedrelocation.gui.*;
import com.dynious.refinedrelocation.gui.container.ContainerAdvanced;
import com.dynious.refinedrelocation.gui.container.ContainerAdvancedFiltered;
import com.dynious.refinedrelocation.gui.container.ContainerFiltered;
import com.dynious.refinedrelocation.gui.container.ContainerFilteringChest;
import com.dynious.refinedrelocation.gui.container.ContainerFilteringHopper;
import com.dynious.refinedrelocation.lib.GuiIds;
import com.dynious.refinedrelocation.tileentity.*;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.ironchest.client.GUIChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler
{
    public GuiHandler()
    {
        NetworkRegistry.instance().registerGuiHandler(RefinedRelocation.instance, this);
    }

    @Override
    public Object getServerGuiElement(int GuiId, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        switch (GuiId)
        {
            //case GuiIds.BLOCK_EXTENDER:
            //case GuiIds.BUFFER:
            case GuiIds.FILTERED:
                return new ContainerFiltered((IFilterTile) tile);
            case GuiIds.ADVANCED_BLOCK_EXTENDER:
            case GuiIds.ADVANCED_BUFFER:
                return new ContainerAdvanced((IAdvancedTile) tile);
            case GuiIds.ADVANCED_FILTERED_BLOCK_EXTENDER:
                return new ContainerAdvancedFiltered((IAdvancedFilteredTile) tile);
            case GuiIds.FILTERING_CHEST:
                if (Loader.isModLoaded("IronChest") && tile != null && tile instanceof TileFilteringIronChest)
                {
                    return GuiFilteringIronChest.makeContainer(GUIChest.GUI.values()[((TileFilteringIronChest)tile).getType().ordinal()], player, (TileFilteringIronChest) tile);
                }
                return new ContainerFilteringChest(player, (TileFilteringChest) tile);
            case GuiIds.FILTERING_HOPPER:
                return new ContainerFilteringHopper(player.inventory, (IFilterTile) tile);
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int GuiId, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tile = world.getBlockTileEntity(x, y, z);

        switch (GuiId)
        {
            case GuiIds.ADVANCED_BLOCK_EXTENDER:
                if (tile != null && tile instanceof TileAdvancedBlockExtender)
                {
                    return new GuiAdvancedBlockExtender(player.inventory, (TileAdvancedBlockExtender) tile);
                }
                break;
            case GuiIds.FILTERED:
                if (tile != null && tile instanceof IFilterTile)
                {
                    return new GuiFiltered(player.inventory, (IFilterTile) tile);
                }
                break;
            case GuiIds.ADVANCED_FILTERED_BLOCK_EXTENDER:
                if (tile != null && tile instanceof TileAdvancedFilteredBlockExtender)
                {
                    return new GuiAdvancedFilteredBlockExtender(player.inventory, (TileAdvancedFilteredBlockExtender) tile);
                }
                break;
            case GuiIds.ADVANCED_BUFFER:
                if (tile != null && tile instanceof TileAdvancedBuffer)
                {
                    return new GuiAdvancedBuffer(player.inventory, (TileAdvancedBuffer) tile);
                }
                break;
            case GuiIds.FILTERING_CHEST:
                if (Loader.isModLoaded("IronChest") && tile != null && tile instanceof TileFilteringIronChest)
                {
                    return new GuiFilteringIronChest(GUIChest.GUI.values()[((TileFilteringIronChest)tile).getType().ordinal()], player, (TileFilteringIronChest) tile);
                }

                if (tile != null && tile instanceof TileFilteringChest)
                {
                    return new GuiFilteringChest(player, (TileFilteringChest) tile);
                }
                break;
            case GuiIds.FILTERING_HOPPER:
                if (tile != null && tile instanceof TileFilteringHopper)
                {
                    return new GuiFilteringHopper(player.inventory, (TileFilteringHopper) tile);
                }
                break;
        }

        return null;
    }

}