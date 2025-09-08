package com.a_day650.hardthencreepersmod.datagen;

import com.a_day650.hardthencreepersmod.HardThenCreepersMod;
import com.a_day650.hardthencreepersmod.block.ModBlocks;
import com.a_day650.hardthencreepersmod.item.ModItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;

import java.util.function.Consumer;

public class ModAdvancementProvider implements ForgeAdvancementProvider.AdvancementGenerator {
    @Override
    public void generate(HolderLookup.Provider registries, Consumer<Advancement> consumer, ExistingFileHelper fileHelper) {
        Advancement htc_mod_root = Advancement.Builder.advancement()
                .display(ModItems.FLINT_HATCHET.get(),
                        Component.translatable("advancements.hardthencreepersmod.htc_mod_root.title"),
                        Component.translatable("advancements.hardthencreepersmod.htc_mod_root.description"),
                        new ResourceLocation(HardThenCreepersMod.MODID, "textures/block/stone.png"),
                        FrameType.TASK,
                        true,    // show_toast
                        false,   // announce_to_chat
                        false)   // hidden
                .addCriterion("htc_mod_root", PlayerTrigger.TriggerInstance.tick()) // 使用PlayerTrigger的tick方法
                .save(consumer, new ResourceLocation(HardThenCreepersMod.MODID, "htc_mod_root"), fileHelper);

        Advancement craftSleepingBag = Advancement.Builder.advancement()
                .parent(htc_mod_root)
                .display(ModBlocks.SLEEPING_BAG.get(),
                        Component.translatable("advancements.hardthencreepersmod.craft_sleeping_bag.title"),
                        Component.translatable("advancements.hardthencreepersmod.craft_sleeping_bag.description"),
                        null,
                        FrameType.TASK,
                        true,
                        true,
                        false)
                .addCriterion("craft_sleeping_bag", RecipeCraftedTrigger.TriggerInstance.craftedItem(new ResourceLocation(HardThenCreepersMod.MODID, "sleeping_bag")))
                .save(consumer, new ResourceLocation(HardThenCreepersMod.MODID, "craft_sleeping_bag"), fileHelper);
        // 制作燧石斧头
        Advancement craftFlintHatchet = Advancement.Builder.advancement()
                .parent(htc_mod_root)
                .display(ModItems.FLINT_HATCHET.get(),
                        Component.translatable("advancements.hardthencreepersmod.craft_flint_hatchet.title"),
                        Component.translatable("advancements.hardthencreepersmod.craft_flint_hatchet.description"),
                        null,
                        FrameType.TASK,
                        true,
                        true,
                        false)
                .addCriterion("craft_flint_hatchet", RecipeCraftedTrigger.TriggerInstance.craftedItem(new ResourceLocation(HardThenCreepersMod.MODID, "flint_hatchet")))
                .save(consumer, new ResourceLocation(HardThenCreepersMod.MODID, "craft_flint_hatchet"), fileHelper);

        Advancement craftClayFurnace = Advancement.Builder.advancement()
                .parent(craftFlintHatchet)
                .display(ModBlocks.CLAY_FURNACE.get(),
                        Component.translatable("advancements.hardthencreepersmod.craft_clay_furnace.title"),
                        Component.translatable("advancements.hardthencreepersmod.craft_clay_furnace.description"),
                        null,
                        FrameType.TASK,
                        true,
                        true,
                        false)
                .addCriterion("craft_clay_furnace", RecipeCraftedTrigger.TriggerInstance.craftedItem(new ResourceLocation(HardThenCreepersMod.MODID, "clay_furnace")))
                .save(consumer, new ResourceLocation(HardThenCreepersMod.MODID, "craft_clay_furnace"), fileHelper);

    }
}