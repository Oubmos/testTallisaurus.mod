package com.tallinomod;

import com.tallinomod.entity.TallinosaurusEntity;
import com.tallinomod.init.ModCreativeTabs;
import com.tallinomod.init.ModEntities;
import com.tallinomod.init.ModItems;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(TallinoMod.MOD_ID)
public class TallinoMod {

    public static final String MOD_ID = "tallinomod";

    public TallinoMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ModEntities.ENTITY_TYPES.register(bus);
        ModItems.ITEMS.register(bus);
        ModCreativeTabs.CREATIVE_TABS.register(bus);

        bus.addListener(this::onAttributeCreate);
        bus.addListener(this::onSpawnPlacement);
    }

    private void onAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(ModEntities.TALLINOSAURUS.get(), TallinosaurusEntity.createAttributes().build());
    }

    private void onSpawnPlacement(SpawnPlacementRegisterEvent event) {
        event.register(
            ModEntities.TALLINOSAURUS.get(),
            SpawnPlacements.Type.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            Animal::checkAnimalSpawnRules,
            SpawnPlacementRegisterEvent.Operation.OR
        );
    }
}
