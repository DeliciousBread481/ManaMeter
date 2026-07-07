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
      dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.m_82127_("manabar").then(Commands.m_82127_("move").then(Commands.m_82129_("x", IntegerArgumentType.integer(-500, 500)).then(Commands.m_82129_("y", IntegerArgumentType.integer(-500, 500)).executes(ManaBarCommand::moveCommand))))).then(Commands.m_82127_("size").then(Commands.m_82129_("scale", DoubleArgumentType.doubleArg(0.1, (double)5.0F)).executes(ManaBarCommand::sizeCommand)))).then(Commands.m_82127_("reset").executes(ManaBarCommand::resetCommand)));
   }

   private static int moveCommand(CommandContext<CommandSourceStack> context) {
      int x = IntegerArgumentType.getInteger(context, "x");
      int y = IntegerArgumentType.getInteger(context, "y");
      ManaMeterConfig.MANA_BAR_X_OFFSET.set(x);
      ManaMeterConfig.MANA_BAR_Y_OFFSET.set(y);
      ManaMeterConfig.SPEC.save();
      ((CommandSourceStack)context.getSource()).m_288197_(() -> Component.m_237113_("Mana bar moved to offset: " + x + ", " + y), false);
      return 1;
   }

   private static int sizeCommand(CommandContext<CommandSourceStack> context) {
      double scale = DoubleArgumentType.getDouble(context, "scale");
      ManaMeterConfig.TEXT_SCALE.set(scale);
      ManaMeterConfig.SPEC.save();
      ((CommandSourceStack)context.getSource()).m_288197_(() -> Component.m_237113_("Mana bar text scale set to: " + scale), false);
      return 1;
   }

   private static int resetCommand(CommandContext<CommandSourceStack> context) {
      ManaMeterConfig.MANA_BAR_X_OFFSET.set(-205);
      ManaMeterConfig.MANA_BAR_Y_OFFSET.set(10);
      ManaMeterConfig.SPEC.save();
      ((CommandSourceStack)context.getSource()).m_288197_(() -> Component.m_237113_("Mana bar position reset to defaults"), false);
      return 1;
   }
}
