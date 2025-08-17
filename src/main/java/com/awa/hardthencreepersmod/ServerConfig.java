package com.awa.hardthencreepersmod;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;

import java.nio.file.Path;

public class ServerConfig {
    public static ForgeConfigSpec SERVER_CONFIG;

    public static final ForgeConfigSpec.BooleanValue VANILLA_RECIPES_ENABLED;
    public static final ForgeConfigSpec.DoubleValue COOK_TIME_FACTOR;
    public static final ForgeConfigSpec.ConfigValue<String> RECIPE_BLACKLIST;

    static {
        final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("Options that affect the added furnaces.").push("Furnace Settings");
        VANILLA_RECIPES_ENABLED = builder
                .comment("If enabled, the vanilla blasting, smelting, and smoking recipes are used by the brick furnaces.")
                .define("vanillaRecipesEnabled", true);
        COOK_TIME_FACTOR = builder
                .comment("Cook time factor of all added brick furnaces in relation to corresponding vanilla furnaces. (i. e. 0.5 - half the time, 1.0 same time, 2.0 twice the time)")
                .defineInRange("cookTimeFactor", 1.0, 0.0, 100.0);
        RECIPE_BLACKLIST = builder
                .comment("A comma separated list of all vanilla recipes that should not be used by the brick furnaces. Example: \"baked_potato,baked_potato_from_smoking,othermod:other_baked_food\"")
                .define("recipeBlacklist", "");
        builder.pop();

        SERVER_CONFIG = builder.build();
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();
        configData.load();
        spec.setConfig(configData);
    }

    public static boolean isRecipeNotBlacklisted(final ResourceLocation id) {
        String configValue = RECIPE_BLACKLIST.get().trim();
        if (!configValue.isEmpty()) {
            String[] ids = configValue.split(",");
            if (ids.length < 1) {
                return !(new ResourceLocation(configValue).equals(id));
            } else {
                for (String recipeId : ids) {
                    if (new ResourceLocation(recipeId.trim()).equals(id)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

}
