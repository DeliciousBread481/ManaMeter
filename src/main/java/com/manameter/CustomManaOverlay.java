package com.manameter;

import com.hollingsworth.arsnouveau.api.client.IDisplayMana;
import com.hollingsworth.arsnouveau.api.item.ICasterTool;
import com.hollingsworth.arsnouveau.api.mana.IManaCap;
import com.hollingsworth.arsnouveau.api.spell.ISpellCaster;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.util.CasterUtil;
import com.hollingsworth.arsnouveau.api.util.ManaUtil;
import com.hollingsworth.arsnouveau.setup.registry.CapabilityRegistry;
import com.manameter.config.ManaMeterConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(
   modid = "manameter",
   bus = Bus.FORGE,
   value = Dist.CLIENT
)
public class CustomManaOverlay {
   @SubscribeEvent
   public static void onRenderGuiOverlay(RenderGuiOverlayEvent.Pre event) {
      if (event.getOverlay().id().getPath().equals("manabar") || event.getOverlay().id().toString().contains("mana")) {
         Minecraft mc = Minecraft.getInstance();
         Player player = mc.player;
         if (player == null) {
            return;
         }

         if (!shouldDisplayManaBar(player)) {
            return;
         }

         IManaCap mana = CapabilityRegistry.getMana(player).orElse(null);
         if (mana == null) {
            return;
         }

         int maxMana = mana.getMaxMana();
         int currentMana = (int)mana.getCurrentMana();
         int spellCost = getSpellCost(player);
         event.setCanceled(true);
         renderEnhancedManaBar(event.getGuiGraphics(), mc, currentMana, maxMana, spellCost);
      }

   }

   private static void renderEnhancedManaBar(GuiGraphics guiGraphics, Minecraft mc, int currentMana, int maxMana, int spellCost) {
      int screenWidth = mc.getWindow().getGuiScaledWidth();
      int screenHeight = mc.getWindow().getGuiScaledHeight();
      int offsetLeft = screenWidth / 2 - 91 + 81 + (Integer)ManaMeterConfig.MANA_BAR_X_OFFSET.get();
      int yOffset = screenHeight - 39 + (Integer)ManaMeterConfig.MANA_BAR_Y_OFFSET.get();
      int barWidth = 108;
      int barHeight = 18;
      int remainingMana = Math.max(0, currentMana - spellCost);
      int mainFillWidth;
      int costFillWidth;
      if (spellCost > 0) {
         mainFillWidth = maxMana > 0 ? remainingMana * 96 / maxMana : 0;
         costFillWidth = maxMana > 0 ? Math.min(spellCost, currentMana) * 96 / maxMana + 1 : 0;
      } else {
         mainFillWidth = maxMana > 0 ? currentMana * 96 / maxMana : 0;
         costFillWidth = 0;
      }

      float time = (float)(System.currentTimeMillis() % 2000L) / 2000.0F;
      float costAlpha = 0.5F + 0.4F * (float)Math.sin((double)time * Math.PI * (double)2.0F);
      RenderSystem.setShaderTexture(0, new ResourceLocation("ars_nouveau", "textures/gui/manabar_gui_border.png"));
      guiGraphics.blit(new ResourceLocation("ars_nouveau", "textures/gui/manabar_gui_border.png"), offsetLeft, yOffset, 0.0F, 0.0F, barWidth, barHeight, 256, 256);
      if (mainFillWidth > 0) {
         RenderSystem.setShaderTexture(0, new ResourceLocation("ars_nouveau", "textures/gui/manabar_gui_mana.png"));
         int manaOffset = (int)(((float)mc.level.getGameTime() + mc.getFrameTime()) / 3.0F % 33.0F) * 6;
         guiGraphics.blit(new ResourceLocation("ars_nouveau", "textures/gui/manabar_gui_mana.png"), offsetLeft + 9, yOffset + 9, 0.0F, (float)manaOffset, mainFillWidth, 6, 256, 256);
      }

      if (costFillWidth > 0) {
         RenderSystem.enableBlend();
         RenderSystem.setShaderColor(1.0F, 0.6F, 0.2F, costAlpha);
         int costStartX = offsetLeft + 9 + mainFillWidth;
         int costWidth = Math.min(costFillWidth, 96 - mainFillWidth + 1);
         RenderSystem.setShaderTexture(0, new ResourceLocation("ars_nouveau", "textures/gui/manabar_gui_mana.png"));
         int manaOffset = (int)(((float)mc.level.getGameTime() + mc.getFrameTime()) / 3.0F % 33.0F) * 6;
         guiGraphics.blit(new ResourceLocation("ars_nouveau", "textures/gui/manabar_gui_mana.png"), costStartX, yOffset + 9, 0.0F, (float)manaOffset, costWidth, 6, 256, 256);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         RenderSystem.disableBlend();
      }

      RenderSystem.setShaderTexture(0, new ResourceLocation("ars_nouveau", "textures/gui/manabar_gui_border.png"));
      guiGraphics.blit(new ResourceLocation("ars_nouveau", "textures/gui/manabar_gui_border.png"), offsetLeft, yOffset + 1, 0.0F, 18.0F, barWidth, 20, 256, 256);
      String manaText = currentMana + "/" + maxMana;
      double scale = (Double)ManaMeterConfig.TEXT_SCALE.get();
      guiGraphics.pose().pushPose();
      guiGraphics.pose().scale((float)scale, (float)scale, 1.0F);
      int scaledTextX = (int)((double)(offsetLeft + (Integer)ManaMeterConfig.X_OFFSET.get() + barWidth / 2) / scale - (double)(mc.font.width(manaText) / 2));
      int scaledTextY = (int)((double)(yOffset + (Integer)ManaMeterConfig.Y_OFFSET.get() + 6) / scale);
      int textColor = parseColor((String)ManaMeterConfig.TEXT_COLOR.get());
      int shadowColor = parseColor((String)ManaMeterConfig.SHADOW_COLOR.get());
      guiGraphics.drawString(mc.font, manaText, scaledTextX + 1, scaledTextY + 1, shadowColor, false);
      guiGraphics.drawString(mc.font, manaText, scaledTextX, scaledTextY, textColor, false);
      guiGraphics.pose().popPose();
   }

   private static int parseColor(String hex) {
      try {
         if (hex.startsWith("#")) {
            hex = hex.substring(1);
         }

         return Integer.parseInt(hex, 16) | -16777216;
      } catch (NumberFormatException var2) {
         return -10496;
      }
   }

   private static int getSpellCost(Player player) {
      ItemStack mainHand = player.getItemInHand(InteractionHand.MAIN_HAND);
      ItemStack offHand = player.getItemInHand(InteractionHand.OFF_HAND);
      int cost = getItemSpellCost(mainHand, player);
      return cost > 0 ? cost : getItemSpellCost(offHand, player);
   }

   private static int getItemSpellCost(ItemStack stack, Player player) {
      if (stack.isEmpty()) {
         return 0;
      } else {
         if (stack.getItem() instanceof ICasterTool) {
            ISpellCaster caster = CasterUtil.getCaster(stack);
            Spell spell = caster.getSpell();
            if (!spell.isEmpty()) {
               int cost = spell.getCost() - ManaUtil.getPlayerDiscounts(player, spell, stack);
               return Math.max(0, cost);
            }
         }

         return 0;
      }
   }

   private static boolean shouldDisplayManaBar(Player player) {
      ItemStack mainHand = player.getMainHandItem();
      ItemStack offHand = player.getOffhandItem();
      Item mainItem = mainHand.getItem();
      if (mainItem instanceof IDisplayMana displayMana) {
         if (displayMana.shouldDisplay(mainHand)) {
            return true;
         }
      }

      Item offItem = offHand.getItem();
      if (offItem instanceof IDisplayMana displayMana) {
         if (displayMana.shouldDisplay(offHand)) {
            return true;
         }
      }

      double maxMana = (double)ManaUtil.getMaxMana(player);
      double currentMana = ManaUtil.getCurrentMana(player);
      return maxMana > currentMana;
   }
}
