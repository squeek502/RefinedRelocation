package com.dynious.refinedrelocation.item;

import com.dynious.refinedrelocation.lib.ItemIds;
import com.dynious.refinedrelocation.lib.Names;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ModItems
{
    public static ItemLinker linker;
    public static ItemSortingUpgrade sortingUpgrade;

    public static void init()
    {
        linker = new ItemLinker(ItemIds.LINKER);
        sortingUpgrade = new ItemSortingUpgrade(ItemIds.SORTING_UPGRADE);

        GameRegistry.registerItem(linker, Names.linker);
        GameRegistry.registerItem(sortingUpgrade, Names.sortingUpgrade);

        GameRegistry.addShapedRecipe(new ItemStack(linker), "iri", "rer", "iri", 'i', Item.ingotIron, 'r', Item.redstone, 'e', Item.enderPearl);
        GameRegistry.addShapedRecipe(new ItemStack(sortingUpgrade), "g g", " p ", "g g", 'g', Item.ingotGold, 'p', Block.thinGlass);
    }
}
