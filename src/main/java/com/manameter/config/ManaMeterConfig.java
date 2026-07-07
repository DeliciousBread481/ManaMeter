package com.manameter.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ManaMeterConfig {
   public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
   public static final ForgeConfigSpec SPEC;
   public static final ForgeConfigSpec.BooleanValue ENABLE_CUSTOM_OVERLAY;
   public static final ForgeConfigSpec.BooleanValue SHOW_PERCENTAGE;
   public static final ForgeConfigSpec.BooleanValue SHOW_MANA_BAR;
   public static final ForgeConfigSpec.IntValue X_OFFSET;
   public static final ForgeConfigSpec.IntValue Y_OFFSET;
   public static final ForgeConfigSpec.IntValue MANA_BAR_X_OFFSET;
   public static final ForgeConfigSpec.IntValue MANA_BAR_Y_OFFSET;
   public static final ForgeConfigSpec.DoubleValue PERCENT_FROM_LEFT;
   public static final ForgeConfigSpec.DoubleValue PERCENT_FROM_TOP;
   public static final ForgeConfigSpec.BooleanValue USE_PERCENTAGE_MODE;
   public static final ForgeConfigSpec.BooleanValue ADAPTIVE_POSITIONING;
   public static final ForgeConfigSpec.DoubleValue TEXT_SCALE;
   public static final ForgeConfigSpec.DoubleValue ROTATION;
   public static final ForgeConfigSpec.IntValue TRANSPARENCY;
   public static final ForgeConfigSpec.BooleanValue ENABLE_SHADOW;
   public static final ForgeConfigSpec.ConfigValue<String> MANA_COLOR;
   public static final ForgeConfigSpec.ConfigValue<String> TEXT_COLOR;
   public static final ForgeConfigSpec.ConfigValue<String> SHADOW_COLOR;
   public static final ForgeConfigSpec.ConfigValue<String> DISPLAY_FORMAT;

   static {
      BUILDER.push("Display Options");
      ENABLE_CUSTOM_OVERLAY = BUILDER.comment("Enable custom mana overlay (disable to use Ars Nouveau's debug numbers)").define("enableCustomOverlay", true);
      SHOW_PERCENTAGE = BUILDER.comment("Show mana percentage").define("showPercentage", true);
      SHOW_MANA_BAR = BUILDER.comment("Show visual mana bar").define("showManaBar", false);
      BUILDER.pop();
      BUILDER.push("Position");
      X_OFFSET = BUILDER.comment("X offset in pixels from mana bar center (negative = left, positive = right)").defineInRange("xOffset", 45, -500, 500);
      Y_OFFSET = BUILDER.comment("Y offset in pixels from mana bar text level (negative = up, positive = down)").defineInRange("yOffset", -7, -100, 100);
      MANA_BAR_X_OFFSET = BUILDER.comment("Mana bar X offset in pixels").defineInRange("manaBarXOffset", -205, -500, 500);
      MANA_BAR_Y_OFFSET = BUILDER.comment("Mana bar Y offset in pixels").defineInRange("manaBarYOffset", 10, -500, 500);
      USE_PERCENTAGE_MODE = BUILDER.comment("Use percentage positioning instead of fixed positioning").define("usePercentageMode", true);
      ADAPTIVE_POSITIONING = BUILDER.comment("Enable adaptive positioning that adjusts for different screen sizes and GUI scales").define("adaptivePositioning", true);
      PERCENT_FROM_LEFT = BUILDER.comment("Percentage from left edge (0.0 to 1.0)").defineInRange("percentFromLeft", 0.18, (double)0.0F, (double)1.0F);
      PERCENT_FROM_TOP = BUILDER.comment("Percentage from top edge (0.0 to 1.0)").defineInRange("percentFromTop", (double)0.875F, (double)0.0F, (double)1.0F);
      BUILDER.pop();
      BUILDER.push("Appearance");
      TEXT_SCALE = BUILDER.comment("Scale multiplier for text size").defineInRange("textScale", (double)1.0F, 0.1, (double)5.0F);
      ROTATION = BUILDER.comment("Text rotation in degrees").defineInRange("rotation", (double)0.0F, (double)-360.0F, (double)360.0F);
      TRANSPARENCY = BUILDER.comment("Text transparency (0-255, where 255 is fully opaque)").defineInRange("transparency", 255, 0, 255);
      ENABLE_SHADOW = BUILDER.comment("Enable text shadow for better visibility").define("enableShadow", true);
      BUILDER.pop();
      BUILDER.push("Colors");
      MANA_COLOR = BUILDER.comment("Color code for mana numbers (hex format: #RRGGBB)").define("manaColor", "#55FFFF");
      TEXT_COLOR = BUILDER.comment("Color code for text (hex format: #RRGGBB)").define("textColor", "#FFD100");
      SHADOW_COLOR = BUILDER.comment("Color code for shadow text (hex format: #RRGGBB)").define("shadowColor", "#7F5F00");
      BUILDER.pop();
      BUILDER.push("Format");
      DISPLAY_FORMAT = BUILDER.comment("Display format (%c = current, %m = max)").define("displayFormat", "%c/%m");
      BUILDER.pop();
      SPEC = BUILDER.build();
   }
}
