package com.tallinomod.init;

import com.tallinomod.TallinoMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TallinoMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> TALLINOMOD_TAB =
        CREATIVE_TABS.register("tallinomod_tab", () ->
            CreativeModeTab.builder()
                .title(Component.translatable("itemGroup.tallinomod"))
                .icon(() -> ModItems.TALLINOSAURUS_SPAWN_EGG.get().getDefaultInstance())
                .displayItems((params, output) -> output.accept(ModItems.TALLINOSAURUS_SPAWN_EGG.get()))
                .build());
}
