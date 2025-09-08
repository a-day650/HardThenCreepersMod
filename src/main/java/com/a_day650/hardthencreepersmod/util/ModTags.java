package com.a_day650.hardthencreepersmod.util;

import com.a_day650.hardthencreepersmod.HardThenCreepersMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> MINEABLE_SLOW = tagBlock("mineable_slow");

        private static TagKey<Block> tagBlock(String name) {
            return BlockTags.create(new ResourceLocation(HardThenCreepersMod.MODID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> POOR_CRAFTING = tagItem("poor_crafting");

        private static TagKey<Item> tagItem(String name) {
            return ItemTags.create(new ResourceLocation(HardThenCreepersMod.MODID, name));
        }
    }
}