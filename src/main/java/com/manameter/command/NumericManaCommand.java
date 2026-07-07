package com.manameter.command;

import com.manameter.config.ManaMeterConfig;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class NumericManaCommand {
   public static LiteralArgumentBuilder<CommandSourceStack> register() {
      return (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("numeric_mana").then(Commands.literal("position").then(Commands.argument("x", IntegerArgumentType.integer(-500, 500)).then(Commands.argument("y", IntegerArgumentType.integer(-100, 100)).executes((context) -> {
         int x = IntegerArgumentType.getInteger(context, "x");
         int y = IntegerArgumentType.getInteger(context, "y");
         ManaMeterConfig.X_OFFSET.set(x);
         ManaMeterConfig.Y_OFFSET.set(y);
         ManaMeterConfig.SPEC.save();
         ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.literal(String.format("Mana display position set to (%d, %d) and saved to config", x, y)), false);
         return 1;
      }))))).then(Commands.literal("size").then(Commands.argument("scale", DoubleArgumentType.doubleArg(0.1, (double)5.0F)).executes((context) -> {
         double scale = DoubleArgumentType.getDouble(context, "scale");
         ManaMeterConfig.TEXT_SCALE.set(scale);
         ManaMeterConfig.SPEC.save();
         ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.literal(String.format("Mana display size set to %.2f and saved to config", scale)), false);
         return 1;
      })))).then(Commands.literal("color").then(Commands.argument("hex", StringArgumentType.string()).executes((context) -> {
         String hexInput = StringArgumentType.getString(context, "hex");

         try {
            String hex = hexInput;
            if (!hexInput.startsWith("#")) {
               hex = "#" + hexInput;
            }

            Integer.parseInt(hex.substring(1), 16);
            ManaMeterConfig.TEXT_COLOR.set(hex);
            int color = Integer.parseInt(hex.substring(1), 16);
            int r = color >> 16 & 255;
            int g = color >> 8 & 255;
            int b = color & 255;
            r = (int)((double)r * (double)0.5F);
            g = (int)((double)g * (double)0.5F);
            b = (int)((double)b * (double)0.5F);
            String shadowHex = String.format("#%02X%02X%02X", r, g, b);
            ManaMeterConfig.SHADOW_COLOR.set(shadowHex);
            ManaMeterConfig.SPEC.save();
            String finalHex = hex;
            ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.literal(String.format("Color set to %s (shadow: %s) and saved to config", finalHex, shadowHex)), false);
            return 1;
         } catch (NumberFormatException var9) {
            ((CommandSourceStack)context.getSource()).sendFailure(Component.literal("Invalid hex color. Use format: #RRGGBB or RRGGBB"));
            return 0;
         }
      })))).then(Commands.literal("rotation").then(Commands.argument("degrees", DoubleArgumentType.doubleArg((double)-360.0F, (double)360.0F)).executes((context) -> {
         double rotation = DoubleArgumentType.getDouble(context, "degrees");
         ManaMeterConfig.ROTATION.set(rotation);
         ManaMeterConfig.SPEC.save();
         ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.literal(String.format("Mana display rotation set to %.1f degrees and saved to config", rotation)), false);
         return 1;
      })))).then(Commands.literal("reset").executes((context) -> {
         ManaMeterConfig.X_OFFSET.set(22);
         ManaMeterConfig.Y_OFFSET.set(-10);
         ManaMeterConfig.TEXT_SCALE.set(0.7);
         ManaMeterConfig.TEXT_COLOR.set("#FFD100");
         ManaMeterConfig.ROTATION.set((double)0.0F);
         ManaMeterConfig.SPEC.save();
         ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.literal("All settings reset to defaults and saved to config"), false);
         return 1;
      }))).executes((context) -> {
         ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.literal(String.format("Numeric Mana Display Settings:\nPosition: (%d, %d)\nSize: %.2f\nColor: %s\nRotation: %.1f°", ManaMeterConfig.X_OFFSET.get(), ManaMeterConfig.Y_OFFSET.get(), ManaMeterConfig.TEXT_SCALE.get(), ManaMeterConfig.TEXT_COLOR.get(), ManaMeterConfig.ROTATION.get())), false);
         return 1;
      });
   }
}
