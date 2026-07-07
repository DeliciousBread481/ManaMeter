package com.manameter;

import com.hollingsworth.arsnouveau.api.ArsNouveauAPI;
import com.manameter.config.ManaMeterConfig;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(
   modid = "manameter",
   bus = Bus.FORGE
)
public class ManaDisplayHandler {
   @SubscribeEvent
   public static void onClientTick(TickEvent.ClientTickEvent event) {
      if (event.phase == Phase.START) {
         boolean useCustomOverlay = (Boolean)ManaMeterConfig.ENABLE_CUSTOM_OVERLAY.get();
         ArsNouveauAPI.ENABLE_DEBUG_NUMBERS = !useCustomOverlay;
      }

   }
}
