package net.textstack.band_of_gigantism.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.textstack.band_of_gigantism.BandOfGigantism;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class BogDamageTypeTagData extends TagsProvider<DamageType> {
    public BogDamageTypeTagData(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
        super(output, Registries.DAMAGE_TYPE, registries, BandOfGigantism.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        this.tag(DamageTypeTags.BYPASSES_ARMOR).add(
                BogDamageTypes.BOG_OBLITERATED,
                BogDamageTypes.BOG_OBLITERATED_INVULN,
                BogDamageTypes.BOG_FADED,
                BogDamageTypes.BOG_FORGOTTEN,
                BogDamageTypes.BOG_PURIFIED,
                BogDamageTypes.BOG_UNKNOWN,
                BogDamageTypes.BOG_DESCENDED,
                BogDamageTypes.BOG_JUDGED,
                BogDamageTypes.BOG_MIRA
        );
        this.tag(DamageTypeTags.BYPASSES_INVULNERABILITY).add(
                BogDamageTypes.BOG_OBLITERATED_INVULN
        );
    }
}
