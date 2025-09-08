package com.a_day650.hardthencreepersmod.datagen;

import com.a_day650.hardthencreepersmod.HardThenCreepersMod;
import com.a_day650.hardthencreepersmod.block.ModBlocks;
import com.a_day650.hardthencreepersmod.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {
    private final String locale;

    public ModLanguageProvider(PackOutput output, String locale) {
        super(output, HardThenCreepersMod.MODID, locale);
        this.locale = locale;
    }

    @Override
    protected void addTranslations() {
        if ("en_us".equals(locale)) {
            addEnglishTranslations();
        } else if ("zh_cn".equals(locale)) {
            addChineseTranslations();
        }
    }

    private void addEnglishTranslations() {
        // 物品名称 - 英文
        addItem(ModItems.BARK, "bark");
        addItem(ModItems.SHARP_FLINT, "sharp flint");
        addItem(ModItems.FLINT_HATCHET, "flint hatchet");
        addItem(ModItems.PEBBLE, "pebble");
        addItem(ModItems.PLANT_FIBER, "plant_fiber");
        addItem(ModItems.CLOTH, "cloth");
        addItem(ModItems.RAW_BRICK, "raw brick");
        addItem(ModItems.WATER_BAG, "water bag");
        addItem(ModItems.FRIED_EGG_FOOD, "fried egg");
        addItem(ModItems.FLINT_SHEARS, "flint shears");
        addItem(ModItems.MYSTERIOUS_RAW_MEAT, "mysterious raw meat");
        addItem(ModItems.STONE_DUST, "stone dust");

        // 水袋相关文本
        add("item.hardthencreepersmod.water_bag.clean", "Clean Water");
        add("item.hardthencreepersmod.water_bag.dirty", "Dirty Water");
        add("item.hardthencreepersmod.water_bag.status", "%s: %d/%d");

        // 方块名称 - 英文
        addBlock(ModBlocks.CLAY_FURNACE, "clay furnace");
        addBlock(ModBlocks.WET_RAW_BRICK, "wet raw brick");
        addBlock(ModBlocks.SLEEPING_BAG, "sleeping bag");
        addBlock(ModBlocks.DESTROYED_ORE_BLOCK, "destroyed ore block");

        // 创造模式标签页
        add("item_group.hardthencreepersmod.htc_tab", "HardThenCreepers 's Items");

        // 工具提示
        add("tooltip.quality", "Craft Quality");
        add("tooltip.quality.poor", "Poor");
        add("tooltip.quality.rough", "Rough");
        add("tooltip.quality.average", "Average");
        add("tooltip.durability_penalty", "Dura Penalty: +%d/use");
        add("tooltip.unrepairable", "Unrepairable");
        add("tooltip.hardthencreepersmod.need_fuel", "need fuel!");
        add("tooltip.durability_varies", "???");
        add("tooltip.crafting_note", "???");

        // 消息
        add("message.tool_break", "Your %s broke due to poor craft!");
        add("message.tool_warning", "%s is about to break!");
        add("message.hardthencreepersmod.low_thirst", "Ugh... so thirsty... maybe I should find water...");

        // 死亡消息
        add("death.attack.hardthencreepersmod.thirst_damage", "%s died of thirst.");

        // 效果
        add("effect.hardthencreepersmod.thirst", "thirst");
        add("effect.hardthencreepersmod.guilt", "guilt");

        // 容器
        add("container.clay_furnace", "clay furnace");

        add("advancements.hardthencreepersmod.htc_mod_root.title","HardThenCreepers");
        add("advancements.hardthencreepersmod.htc_mod_root.description","Challenges and difficulties coexist.");

        add("advancements.hardthencreepersmod.craft_sleeping_bag.title","The ability to sleep");
        add("advancements.hardthencreepersmod.craft_sleeping_bag.description","Craft a sleeping bag");

        add("advancements.hardthencreepersmod.craft_flint_hatchet.title","Fragile item");
        add("advancements.hardthencreepersmod.craft_flint_hatchet.description","Craft a flint hatchet");

        add("advancements.hardthencreepersmod.craft_clay_furnace.title","The prototype of the stove");
        add("advancements.hardthencreepersmod.craft_clay_furnace.description","Craft a clay furnace");
    }

    private void addChineseTranslations() {
        // 物品名称 - 中文
        addItem(ModItems.BARK, "树皮");
        addItem(ModItems.SHARP_FLINT, "锋利的燧石");
        addItem(ModItems.FLINT_HATCHET, "燧石斧头");
        addItem(ModItems.PEBBLE, "小石子");
        addItem(ModItems.WATER_BAG, "水袋");
        addItem(ModItems.FRIED_EGG_FOOD, "煎蛋");
        addItem(ModItems.CLOTH, "布料");
        addItem(ModItems.RAW_BRICK, "生砖");
        addItem(ModItems.FLINT_SHEARS, "燧石剪刀");
        addItem(ModItems.PLANT_FIBER, "植物纤维");
        addItem(ModItems.STONE_DUST, "石粉");
        addItem(ModItems.MYSTERIOUS_RAW_MEAT, "迷之生肉");

        // 水袋相关文本
        add("item.hardthencreepersmod.water_bag.clean", "纯净水");
        add("item.hardthencreepersmod.water_bag.dirty", "脏水");
        add("item.hardthencreepersmod.water_bag.status", "%s：%d/%d");

        // 方块名称 - 中文
        addBlock(ModBlocks.CLAY_FURNACE, "瓦炉");
        addBlock(ModBlocks.WET_RAW_BRICK, "湿生砖");
        addBlock(ModBlocks.SLEEPING_BAG, "睡袋");
        addBlock(ModBlocks.DESTROYED_ORE_BLOCK, "破坏后的矿石");

        // 创造模式标签页
        add("item_group.hardthencreepersmod.htc_tab", "HardThenCreepers 的物品");

        // 工具提示
        add("tooltip.quality", "工艺质量");
        add("tooltip.quality.poor", "劣质");
        add("tooltip.quality.rough", "粗糙");
        add("tooltip.quality.average", "普通");
        add("tooltip.durability_penalty", "耐久惩罚: +%d/次");
        add("tooltip.unrepairable", "无法修复");
        add("tooltip.hardthencreepersmod.need_fuel", "需要燃料！");

        // 消息
        add("message.tool_break", "你的%s因工艺问题损坏！");
        add("message.tool_warning", "%s即将损坏！");
        add("message.hardthencreepersmod.low_thirst", "唔...好渴...也许我应该找点水喝...");

        // 死亡消息
        add("death.attack.hardthencreepersmod.thirst_damage", "%s 因口渴而死");

        // 效果
        add("effect.hardthencreepersmod.thirst", "口渴");
        add("effect.hardthencreepersmod.guilt", "罪恶");

        // 容器
        add("container.clay_furnace", "瓦炉");

        add("advancements.hardthencreepersmod.htc_mod_root.title","HardThenCreepers");
        add("advancements.hardthencreepersmod.htc_mod_root.description","挑战与困难并存");

        add("advancements.hardthencreepersmod.craft_sleeping_bag.title","睡觉的资本");
        add("advancements.hardthencreepersmod.craft_sleeping_bag.description","合成睡袋");

        add("advancements.hardthencreepersmod.craft_flint_hatchet.title","易碎物");
        add("advancements.hardthencreepersmod.craft_flint_hatchet.description","合成燧石斧头");

        add("advancements.hardthencreepersmod.craft_clay_furnace.title","炉灶雏形");
        add("advancements.hardthencreepersmod.craft_clay_furnace.description","合成瓦炉");
    }
}