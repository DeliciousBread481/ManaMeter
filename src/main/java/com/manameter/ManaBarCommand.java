package com.manameter;

import com.manameter.config.ManaMeterConfig;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(
   modid = "manameter"
)
public class ManaBarCommand {
   @SubscribeEvent
   public static void onRegisterCommands(RegisterCommandsEvent event) {
      CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
      dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("manabar").then(Commands.literal("move").then(Commands.argument("x", IntegerArgumentType.integer(-500, 500)).then(Commands.argument("y", IntegerArgumentType.integer(-500, 500)).executes(ManaBarCommand::moveCommand))))).then(Commands.literal("size").then(Commands.argument("scale", DoubleArgumentType.doubleArg(0.1, (double)5.0F)).executes(ManaBarCommand::sizeCommand)))).then(Commands.literal("reset").executes(ManaBarCommand::resetCommand)));
   }

   private static int moveCommand(CommandContext<CommandSourceStack> context) {
      int x = IntegerArgumentType.getInteger(context, "x");
      int y = IntegerArgumentType.getInteger(context, "y");
      ManaMeterConfig.MANA_BAR_X_OFFSET.set(x);
      ManaMeterConfig.MANA_BAR_Y_OFFSET.set(y);
      ManaMeterConfig.SPEC.save();
      ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.literal("Mana bar moved to offset: " + x + ", " + y), false);
      return 1;
   }

   private static int sizeCommand(CommandContext<CommandSourceStack> context) {
      double scale = DoubleArgumentType.getDouble(context, "scale");
      ManaMeterConfig.TEXT_SCALE.set(scale);
      ManaMeterConfig.SPEC.save();
      ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.literal("Mana bar text scale set to: " + scale), false);
      return 1;
   }

   private static int resetCommand(CommandContext<CommandSourceStack> context) {
      ManaMeterConfig.MANA_BAR_X_OFFSET.set(-205);
      ManaMeterConfig.MANA_BAR_Y_OFFSET.set(10);
      ManaMeterConfig.SPEC.save();
      ((CommandSourceStack)context.getSource()).sendSuccess(() -> Component.literal("Mana bar position reset to defaults"), false);
      return 1;
   }
}
