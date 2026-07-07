package com.manameter.mixins;

import com.hollingsworth.arsnouveau.api.mana.IManaCap;
import com.hollingsworth.arsnouveau.client.gui.GuiManaHUD;
import com.hollingsworth.arsnouveau.setup.config.Config;
import com.hollingsworth.arsnouveau.setup.registry.CapabilityRegistry;
import com.manameter.config.ManaMeterConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({GuiManaHUD.class})
public class ManaHUDMixin {
   @Inject(
      method = {"renderOverlay"},
      at = {@At("TAIL")},
      remap = false
   )
   private static void injectCustomManaText(ForgeGui gui, GuiGraphics guiGraphics, float pt, int width, int height, CallbackInfo ci) {
      if ((Boolean)ManaMeterConfig.ENABLE_CUSTOM_OVERLAY.get()) {
         Minecraft mc = Minecraft.getInstance();
         if (mc.player != null) {
            IManaCap mana = CapabilityRegistry.getMana(mc.player).orElse(null);
            if (mana != null) {
               int maxMana = mana.getMaxMana();
               if (maxMana != 0) {
                  int currentMana = (int)mana.getCurrentMana();
                  String manaText = currentMana + "/" + maxMana;
                  int offsetLeft = 10 + (Integer)Config.MANABAR_X_OFFSET.get();
                  int yOffset = height - 5 + (Integer)Config.MANABAR_Y_OFFSET.get();
                  double scale = (Double)ManaMeterConfig.TEXT_SCALE.get();
                  double rotation = (Double)ManaMeterConfig.ROTATION.get();
                  boolean enableShadow = (Boolean)ManaMeterConfig.ENABLE_SHADOW.get();
                  int textWidth = mc.font.width(manaText);
                  int maxWidth = mc.font.width(maxMana + "  /  " + maxMana);
                  int baseX = offsetLeft + 54;
                  int baseY = yOffset - 10;
                  int x = baseX + (Integer)ManaMeterConfig.X_OFFSET.get();
                  int y = baseY + (Integer)ManaMeterConfig.Y_OFFSET.get();
                  int textColor = -12032;
                  int shadowColor = -7650029;
                  guiGraphics.pose().pushPose();
                  if (scale != (double)1.0F || rotation != (double)0.0F) {
                     PoseStack var10000 = guiGraphics.pose();
                     float var10001 = (float)(x + textWidth / 2);
                     Objects.requireNonNull(mc.font);
                     var10000.translate(var10001, (float)(y + 9 / 2), 0.0F);
                     if (scale != (double)1.0F) {
                        guiGraphics.pose().scale((float)scale, (float)scale, 1.0F);
                     }

                     if (rotation != (double)0.0F) {
                        guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees((float)rotation));
                     }

                     var10000 = guiGraphics.pose();
                     var10001 = (float)(-textWidth / 2);
                     Objects.requireNonNull(mc.font);
                     var10000.translate(var10001, (float)(-9 / 2), 0.0F);
                     x = 0;
                     y = 0;
                  }

                  if (enableShadow) {
                     guiGraphics.drawString(mc.font, manaText, x + 1, y + 1, shadowColor, false);
                  }

                  guiGraphics.drawString(mc.font, manaText, x, y, textColor, false);
                  guiGraphics.pose().popPose();
               }
            }
         }
      }
   }
}
