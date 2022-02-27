package net.textstack.band_of_gigantism.misc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.mojang.serialization.JsonOps;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.textstack.band_of_gigantism.BandOfGigantism;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

@Mod.EventBusSubscriber(modid = BandOfGigantism.MODID)
public class StatListener extends JsonReloadListener {

    private static StatListener currentInstance = null;
    private static StatListener reloadingInstance = null;

    private static final Logger LOGGER = LogManager.getLogger("StatListener");
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    private ItemStats itemStats;
    private MarkStats markStats;
    private ObliteratedStats obliteratedStats;

    public StatListener(boolean server) {
        super(GSON, "bog_stats");
        if (!server || currentInstance == null) {
            currentInstance = this;
        } else {
            reloadingInstance = this;
        }
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> map, @NotNull IResourceManager resourceManagerIn, @NotNull IProfiler profilerIn) {
        Function<String, JsonElement> getter = file -> map.entrySet().stream()
                .filter(e -> e.getKey().getNamespace().equals(BandOfGigantism.MODID) && e.getKey().getPath().equals(file))
                .map(Map.Entry::getValue)
                .findAny().orElse(JsonNull.INSTANCE);

        this.itemStats = ItemStats.CODEC.parse(JsonOps.INSTANCE, getter.apply(ItemStats.FILE))
                .getOrThrow(false,prefix("itemStats: "));
        this.markStats = MarkStats.CODEC.parse(JsonOps.INSTANCE, getter.apply(MarkStats.FILE))
                .getOrThrow(false,prefix("markStats: "));
        this.obliteratedStats = ObliteratedStats.CODEC.parse(JsonOps.INSTANCE, getter.apply(ObliteratedStats.FILE))
                .getOrThrow(false,prefix("obliteratedStats: "));
        LOGGER.debug("finished parsing bog config");

        if (this==reloadingInstance) {
            currentInstance = this;
            reloadingInstance = null;
        }
    }

    public static StatListener getInstance() {
        if (currentInstance == null) {
            throw new RuntimeException("(bog) fucking idiot you did the thing too early!!!");
        }
        return currentInstance;
    }

    private static Consumer<String> prefix(String pre) {
        return s -> LOGGER.error(pre+s);
    }

    public static ItemStats getItemStats() {
        return getInstance().itemStats;
    }

    public static MarkStats getMarkStats() {
        return getInstance().markStats;
    }

    public static ObliteratedStats getObliteratedStats() {
        return getInstance().obliteratedStats;
    }

    public static void setClientInstance(ItemStats itemStats, MarkStats markStats, ObliteratedStats obliteratedStats) {
        new StatListener(false);
        currentInstance.itemStats = itemStats;
        currentInstance.markStats = markStats;
        currentInstance.obliteratedStats = obliteratedStats;
        BandOfGigantism.LOGGER.debug("Loaded BOG stats clientside");
    }

    @SubscribeEvent
    public static void addListener(AddReloadListenerEvent event) {
        event.addListener(new StatListener(true));
    }
}
