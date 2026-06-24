package com.tallinomod.init;

import com.tallinomod.TallinoMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS, TallinoMod.MOD_ID);

    // Spawn egg: primary colour = dark green, secondary = lime
    public static final RegistryObject<Item> TALLINOSAURUS_SPAWN_EGG =
        ITEMS.register("tallinosaurus_spawn_egg", () ->
            new ForgeSpawnEggItem(ModEntities.TALLINOSAURUS, 0x2d6a2d, 0x6abf6a,
                new Item.Properties()));
}
