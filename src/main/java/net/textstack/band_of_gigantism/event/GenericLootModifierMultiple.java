package net.textstack.band_of_gigantism.event;

import com.google.common.base.Suppliers;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class GenericLootModifierMultiple  { //extends LootModifier
    /*public static final Supplier<Codec<GenericLootModifierMultiple>> CODEC = Suppliers.memoize(()
            -> RecordCodecBuilder.create(inst -> codecStart(inst).and(ForgeRegistries.ITEMS.getCodec()
            .fieldOf("item").forGetter(m -> m.item)).apply(inst, GenericLootModifierMultiple::new)));
    private final JsonArray additionArray;

    protected GenericLootModifierMultiple(LootItemCondition[] conditionsIn, JsonArray additionArray) {
        super(conditionsIn);
        this.additionArray = additionArray;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        for (JsonElement additionElement : additionArray) {
            JsonObject additionObject = additionElement.getAsJsonObject();
            Item addition = ForgeRegistries.ITEMS.getValue(new ResourceLocation(additionObject.get("item").getAsString()));
            float chance = additionObject.get("chance").getAsFloat();
            if (context.getRandom().nextFloat() < chance) {
                generatedLoot.add(new ItemStack(addition, 1));
            }
        }
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }*/
}