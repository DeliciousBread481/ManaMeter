package com.manameter.command;

import com.manameter.config.ManaMeterConfig;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class ManaPositionCommand {
   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
      dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("manadisplay").then(Commands.literal("enable").executes((ctx) -> {
         ManaMeterConfig.ENABLE_CUSTOM_OVERLAY.set(true);
         ManaMeterConfig.SPEC.save();
         ((CommandSourceStack)ctx.getSource()).sendSuccess(() -> Component.literal("Numeric mana display enabled"), false);
         return 1;
      }))).then(Commands.literal("disable").executes((ctx) -> {
         ManaMeterConfig.ENABLE_CUSTOM_OVERLAY.set(false);
         ManaMeterConfig.SPEC.save();
         ((CommandSourceStack)ctx.getSource()).sendSuccess(() -> Component.literal("Numeric mana display disabled"), false);
         return 1;
      }))).then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("position").then(Commands.literal("center").executes(ManaPositionCommand::centerPosition))).then(Commands.literal("set").then(Commands.argument("x", IntegerArgumentType.integer(-1000, 1000)).then(Commands.argument("y", IntegerArgumentType.integer(0, 200)).executes(ManaPositionCommand::setPosition))))).executes(ManaPositionCommand::showPosition))).then(((LiteralArgumentBuilder)Commands.literal("scale").then(Commands.argument("scale", DoubleArgumentType.doubleArg(0.1, (double)5.0F)).executes(ManaPositionCommand::setScale))).executes(ManaPositionCommand::showScale))).then(((LiteralArgumentBuilder)Commands.literal("rotation").then(Commands.argument("degrees", DoubleArgumentType.doubleArg((double)-360.0F, (double)360.0F)).executes(ManaPositionCommand::setRotation))).executes(ManaPositionCommand::showRotation))).then(((LiteralArgumentBuilder)Commands.literal("color").then(Commands.argument("hex", StringArgumentType.string()).executes(ManaPositionCommand::setColor))).executes(ManaPositionCommand::showColor))).then(((LiteralArgumentBuilder)Commands.literal("transparency").then(Commands.argument("alpha", IntegerArgumentType.integer(0, 255)).executes(ManaPositionCommand::setTransparency))).executes(ManaPositionCommand::showTransparency))).then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("shadow").then(Commands.literal("on").executes(ManaPositionCommand::enableShadow))).then(Commands.literal("off").executes(ManaPositionCommand::disableShadow))).executes(ManaPositionCommand::showShadow))).then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("percent").then(Commands.literal("enable").executes(ManaPositionCommand::enablePercentMode))).then(Commands.literal("disable").executes(ManaPositionCommand::disablePercentMode))).then(Commands.literal("set").then(Commands.argument("fromLeft", DoubleArgumentType.doubleArg((double)0.0F, (double)1.0F)).then(Commands.argument("fromTop", DoubleArgumentType.doubleArg((double)0.0F, (double)1.0F)).executes(ManaPositionCommand::setPercentPosition))))).then(Commands.literal("anchor").then(Commands.argument("fromLeft", DoubleArgumentType.doubleArg((double)0.0F, (double)1.0F)).then(Commands.argument("fromTop", DoubleArgumentType.doubleArg((double)0.0F, (double)1.0F)).executes((context) -> {
         double left = DoubleArgumentType.getDouble(context, "fromLeft");
         double top = DoubleArgumentType.getDouble(context, "fromTop");
         ManaMeterConfig.PERCENT_FROM_LEFT.set(left);
         ManaMeterConfig.PERCENT_FROM_TOP.set(top);
         ManaMeterConfig.USE_PERCENTAGE_MODE.set(true);
         ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.literal(String.format("Mana display anchored to %.3f left, %.3f top\nThis position will adapt better to screen size changes.", left, top)), false);
         return 1;
      }))))).executes(ManaPositionCommand::showPercentSettings))).then(Commands.literal("reset").executes(ManaPositionCommand::resetAll))).executes(ManaPositionCommand::showStatus));
   }

   private static int centerPosition(CommandContext<CommandSourceStack> context) {
      ManaMeterConfig.X_OFFSET.set(0);
      ManaMeterConfig.Y_OFFSET.set(7);
      ManaMeterConfig.SPEC.save();
      ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.literal("Mana display centered at 15% screen position!"), true);
      return 1;
   }

   private static int setPosition(CommandContext<CommandSourceStack> context) {
      int x = IntegerArgumentType.getInteger(context, "x");
      int y = IntegerArgumentType.getInteger(context, "y");
      ManaMeterConfig.X_OFFSET.set(x);
      ManaMeterConfig.Y_OFFSET.set(y);
      ManaMeterConfig.SPEC.save();
      ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.literal("Position set to X: " + x + ", Y: " + y), true);
      return 1;
   }

   private static int showPosition(CommandContext<CommandSourceStack> context) {
      int x = (Integer)ManaMeterConfig.X_OFFSET.get();
      int y = (Integer)ManaMeterConfig.Y_OFFSET.get();
      ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.literal("Current position - X: " + x + ", Y: " + y), false);
      return 1;
   }

   private static int setScale(CommandContext<CommandSourceStack> context) {
      double scale = DoubleArgumentType.getDouble(context, "scale");
      ManaMeterConfig.TEXT_SCALE.set(scale);
      ManaMeterConfig.SPEC.save();
      ((CommandSourceStack)context.getSource()).sendSuccess(() -> {
         Object[] var10001 = new Object[]{scale};
         return Component.literal("Text scale set to " + String.format("%.2f", var10001));
      }, true);
      return 1;
   }

   private static int showScale(CommandContext<CommandSourceStack> context) {
      double scale = (Double)ManaMeterConfig.TEXT_SCALE.get();
      ((CommandSourceStack)context.getSource()).sendSuccess(() -> {
         Object[] var10001 = new Object[]{scale};
         return Component.literal("Current scale: " + String.format("%.2f", var10001));
      }, false);
      return 1;
   }

   private static int setRotation(CommandContext<CommandSourceStack> context) {
      double degrees = DoubleArgumentType.getDouble(context, "degrees");
      ManaMeterConfig.ROTATION.set(degrees);
      ManaMeterConfig.SPEC.save();
      ((CommandSourceStack)context.getSource()).sendSuccess(() -> {
         Object[] var10001 = new Object[]{degrees};
         return Component.literal("Rotation set to " + String.format("%.1f", var10001) + " degrees");
      }, true);
      return 1;
   }

   private static int showRotation(CommandContext<CommandSourceStack> context) {
      double rotation = (Double)ManaMeterConfig.ROTATION.get();
      ((CommandSourceStack)context.getSource()).sendSuccess(() -> {
         Object[] var10001 = new Object[]{rotation};
         return Component.literal("Current rotation: " + String.format("%.1f", var10001) + " degrees");
      }, false);
      return 1;
   }

   private static int setColor(CommandContext<CommandSourceStack> context) {
      String hex = StringArgumentType.getString(context, "hex");
      if (!hex.startsWith("#")) {
         hex = "#" + hex;
      }

      String finalHex = hex;

      try {
         Integer.parseInt(finalHex.substring(1), 16);
         ManaMeterConfig.TEXT_COLOR.set(finalHex);
         ManaMeterConfig.SPEC.save();
         ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.literal("Text color set to " + finalHex), true);
         return 1;
      } catch (NumberFormatException var4) {
         ((CommandSourceStack)context.getSource()).sendFailure(Component.literal("Invalid hex color format! Use format like #FFFFFF or FFFFFF"));
         return 0;
      }
   }

   private static int showColor(CommandContext<CommandSourceStack> context) {
      String color = (String)ManaMeterConfig.TEXT_COLOR.get();
      ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.literal("Current color: " + color), false);
      return 1;
   }

   private static int setTransparency(CommandContext<CommandSourceStack> context) {
      int alpha = IntegerArgumentType.getInteger(context, "alpha");
      ManaMeterConfig.TRANSPARENCY.set(alpha);
      ManaMeterConfig.SPEC.save();
      ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.literal("Transparency set to " + alpha + " (0=invisible, 255=solid)"), true);
      return 1;
   }

   private static int showTransparency(CommandContext<CommandSourceStack> context) {
      int transparency = (Integer)ManaMeterConfig.TRANSPARENCY.get();
      ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.literal("Current transparency: " + transparency + " (0=invisible, 255=solid)"), false);
      return 1;
   }

   private static int enableShadow(CommandContext<CommandSourceStack> context) {
      ManaMeterConfig.ENABLE_SHADOW.set(true);
      ManaMeterConfig.SPEC.save();
      ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.literal("Text shadow enabled"), true);
      return 1;
   }

   private static int disableShadow(CommandContext<CommandSourceStack> context) {
      ManaMeterConfig.ENABLE_SHADOW.set(false);
      ManaMeterConfig.SPEC.save();
      ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.literal("Text shadow disabled"), true);
      return 1;
   }

   private static int showShadow(CommandContext<CommandSourceStack> context) {
      boolean shadow = (Boolean)ManaMeterConfig.ENABLE_SHADOW.get();
      ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.literal("Text shadow: " + (shadow ? "enabled" : "disabled")), false);
      return 1;
   }

   private static int enablePercentMode(CommandContext<CommandSourceStack> context) {
      ManaMeterConfig.USE_PERCENTAGE_MODE.set(true);
      ManaMeterConfig.SPEC.save();
      ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.literal("Percentage positioning mode enabled"), true);
      return 1;
   }

   private static int disablePercentMode(CommandContext<CommandSourceStack> context) {
      ManaMeterConfig.USE_PERCENTAGE_MODE.set(false);
      ManaMeterConfig.SPEC.save();
      ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.literal("Percentage positioning mode disabled (using fixed 15% mode)"), true);
      return 1;
   }

   private static int setPercentPosition(CommandContext<CommandSourceStack> context) {
      double fromLeft = DoubleArgumentType.getDouble(context, "fromLeft");
      double fromTop = DoubleArgumentType.getDouble(context, "fromTop");
      ManaMeterConfig.USE_PERCENTAGE_MODE.set(true);
      ManaMeterConfig.PERCENT_FROM_LEFT.set(fromLeft);
      ManaMeterConfig.PERCENT_FROM_TOP.set(fromTop);
      ManaMeterConfig.SPEC.save();
      double percentRight = (double)1.0F - fromLeft;
      double percentBottom = (double)1.0F - fromTop;
      ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.literal(String.format("Position set to %.1f%% from left (%.1f%% from right), %.1f%% from top (%.1f%% from bottom)", fromLeft * (double)100.0F, percentRight * (double)100.0F, fromTop * (double)100.0F, percentBottom * (double)100.0F)), true);
      return 1;
   }

   private static int showPercentSettings(CommandContext<CommandSourceStack> context) {
      boolean usePercent = (Boolean)ManaMeterConfig.USE_PERCENTAGE_MODE.get();
      double fromLeft = (Double)ManaMeterConfig.PERCENT_FROM_LEFT.get();
      double fromTop = (Double)ManaMeterConfig.PERCENT_FROM_TOP.get();
      if (usePercent) {
         double fromRight = (double)1.0F - fromLeft;
         double fromBottom = (double)1.0F - fromTop;
         ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.literal(String.format("Percentage mode: ON\nPosition: %.1f%% from left, %.1f%% from top\n(%.1f%% from right, %.1f%% from bottom)", fromLeft * (double)100.0F, fromTop * (double)100.0F, fromRight * (double)100.0F, fromBottom * (double)100.0F)), false);
      } else {
         ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.literal("Percentage mode: OFF (using fixed 15% positioning)"), false);
      }

      return 1;
   }

   private static int resetAll(CommandContext<CommandSourceStack> context) {
      ManaMeterConfig.X_OFFSET.set(45);
      ManaMeterConfig.Y_OFFSET.set(-7);
      ManaMeterConfig.USE_PERCENTAGE_MODE.set(false);
      ManaMeterConfig.PERCENT_FROM_LEFT.set(0.15);
      ManaMeterConfig.PERCENT_FROM_TOP.set(0.85);
      ManaMeterConfig.TEXT_SCALE.set((double)1.5F);
      ManaMeterConfig.ROTATION.set((double)0.0F);
      ManaMeterConfig.TEXT_COLOR.set("#FFD700");
      ManaMeterConfig.TRANSPARENCY.set(255);
      ManaMeterConfig.ENABLE_SHADOW.set(true);
      ManaMeterConfig.SPEC.save();
      ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.literal("All mana display settings reset to default!"), true);
      return 1;
   }

   private static int showStatus(CommandContext<CommandSourceStack> context) {
      int x = (Integer)ManaMeterConfig.X_OFFSET.get();
      int y = (Integer)ManaMeterConfig.Y_OFFSET.get();
      boolean usePercent = (Boolean)ManaMeterConfig.USE_PERCENTAGE_MODE.get();
      double fromLeft = (Double)ManaMeterConfig.PERCENT_FROM_LEFT.get();
      double fromTop = (Double)ManaMeterConfig.PERCENT_FROM_TOP.get();
      double scale = (Double)ManaMeterConfig.TEXT_SCALE.get();
      double rotation = (Double)ManaMeterConfig.ROTATION.get();
      String color = (String)ManaMeterConfig.TEXT_COLOR.get();
      int transparency = (Integer)ManaMeterConfig.TRANSPARENCY.get();
      boolean shadow = (Boolean)ManaMeterConfig.ENABLE_SHADOW.get();
      String positionInfo;
      if (usePercent) {
         positionInfo = String.format("Percentage: %.1f%% left, %.1f%% top", fromLeft * (double)100.0F, fromTop * (double)100.0F);
      } else {
         positionInfo = "Fixed: 15% from edges";
      }

      ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.literal("Mana Display Settings:\nOffsets: X=" + x + ", Y=" + y + "\nPosition: " + positionInfo + "\nScale: " + String.format("%.2f", scale) + "\nRotation: " + String.format("%.1f", rotation) + "°\nColor: " + color + "\nTransparency: " + transparency + "\nShadow: " + (shadow ? "enabled" : "disabled")), false);
      return 1;
   }
}
