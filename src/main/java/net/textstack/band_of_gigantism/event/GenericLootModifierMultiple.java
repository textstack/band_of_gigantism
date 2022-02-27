package net.textstack.band_of_gigantism.event;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.List;

public class GenericLootModifierMultiple extends LootModifier {
    private final JsonArray additionArray;

    protected GenericLootModifierMultiple(ILootCondition[] conditionsIn, JsonArray additionArray) {
        super(conditionsIn);
        this.additionArray = additionArray;
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        for (JsonElement additionElement : additionArray) {
            JsonObject additionObject = additionElement.getAsJsonObject();
            Item addition = ForgeRegistries.ITEMS.getValue(new ResourceLocation(additionObject.get("item").getAsString()));
            float chance = additionObject.get("chance").getAsFloat();
            if(context.getRandom().nextFloat() < chance) {
                generatedLoot.add(new ItemStack(addition, 1));
            }
        }
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<GenericLootModifierMultiple> {

        @Override
        public GenericLootModifierMultiple read(ResourceLocation name, JsonObject object, ILootCondition[] conditionsIn) {
            JsonArray additionArray = JSONUtils.getJsonArray(object,"addition");
            return new GenericLootModifierMultiple(conditionsIn, additionArray);
        }

        @Override
        public JsonObject write(GenericLootModifierMultiple instance) {
            JsonObject json = makeConditions(instance.conditions);
            for (JsonElement additionElement : instance.additionArray) {
                json.addProperty("addition", additionElement.getAsString());
            }
            return json;
        }
    }
}
