package com.manameter;

import com.manameter.command.NumericManaCommand;
import com.manameter.config.ManaMeterConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod("manameter")
public class ManaMeter {
   public static final String MODID = "manameter";
   private static final Logger LOGGER = LoggerFactory.getLogger(ManaMeter.class);

   public ManaMeter() {
      FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
      FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
      ModLoadingContext.get().registerConfig(Type.CLIENT, ManaMeterConfig.SPEC, "manameter-client.toml");
      MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
      MinecraftForge.EVENT_BUS.register(this);
   }

   private void setup(FMLCommonSetupEvent event) {
      LOGGER.info("ManaMeter common setup");
   }

   private void doClientStuff(FMLClientSetupEvent event) {
      LOGGER.info("ManaMeter client setup");
      MinecraftForge.EVENT_BUS.register(new ManaDisplayHandler());
      MinecraftForge.EVENT_BUS.register(new CustomManaOverlay());
   }

   public void registerCommands(RegisterCommandsEvent event) {
      event.getDispatcher().register(NumericManaCommand.register());
   }
}
