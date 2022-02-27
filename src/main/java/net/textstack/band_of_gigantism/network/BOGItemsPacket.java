package net.textstack.band_of_gigantism.network;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;
import net.textstack.band_of_gigantism.BandOfGigantism;
import net.textstack.band_of_gigantism.misc.ItemStats;
import net.textstack.band_of_gigantism.misc.MarkStats;
import net.textstack.band_of_gigantism.misc.ObliteratedStats;
import net.textstack.band_of_gigantism.misc.StatListener;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class BOGItemsPacket implements IntSupplier {
    private ItemStats itemStats;
    private MarkStats markStats;
    private ObliteratedStats obliteratedStats;
    
    public BOGItemsPacket(ItemStats itemStats, MarkStats markStats, ObliteratedStats obliteratedStats) {
        this.itemStats = itemStats;
        this.markStats = markStats;
        this.obliteratedStats = obliteratedStats;
    }
    
    public BOGItemsPacket() {
        this.itemStats = StatListener.getItemStats();
        this.markStats = StatListener.getMarkStats();
        this.obliteratedStats = StatListener.getObliteratedStats();
    }
    
    public void encode(PacketBuffer buffer) {
        try {
            buffer.func_240629_a_(ItemStats.CODEC, itemStats);
            buffer.func_240629_a_(MarkStats.CODEC, markStats);
            buffer.func_240629_a_(ObliteratedStats.CODEC, obliteratedStats);
        } catch (IOException a) {
            BandOfGigantism.LOGGER.error(a);
        }
    }

    public static BOGItemsPacket decode(PacketBuffer buffer) {
        ItemStats itemStats = null;
        MarkStats markStats = null;
        ObliteratedStats obliteratedStats = null;
        try {
            itemStats = buffer.func_240628_a_(ItemStats.CODEC);
            markStats = buffer.func_240628_a_(MarkStats.CODEC);
            obliteratedStats = buffer.func_240628_a_(ObliteratedStats.CODEC);
        } catch (IOException a) {
            BandOfGigantism.LOGGER.error(a);
        }
        return new BOGItemsPacket(itemStats, markStats, obliteratedStats);
    }

    public static void handle(BOGItemsPacket packet, Supplier<NetworkEvent.Context> ctx) {
        CountDownLatch block = new CountDownLatch(1);
        AtomicBoolean successful = new AtomicBoolean();
        ctx.get().enqueueWork(() -> {
           StatListener.setClientInstance(packet.itemStats, packet.markStats, packet.obliteratedStats);
           successful.set(Stream.of(
                   packet.itemStats,
                   packet.markStats,
                   packet.obliteratedStats
           ).allMatch(Objects::nonNull));
           block.countDown();
        });
        try {
            block.await();
        } catch (InterruptedException a) {
            Thread.interrupted();
        }
        ctx.get().setPacketHandled(true);
        if (successful.get()) {
            BandOfGigantism.LOGGER.debug("Received BOG stats clientside");
            Network.channel.reply(new Network.SimpleReply(), ctx.get());
        } else {
            BandOfGigantism.LOGGER.error("Failed to receive BOG stats clientside");
            ctx.get().getNetworkManager().closeChannel(new StringTextComponent("Didn't get BOG config data, closing connection"));
        }
    }

    //from FMLHandshakeMessages#LoginIndexMessage; not sure if this is doing anything important?
    private int loginIdx;

    public void setLoginIdx(int idx) {
        this.loginIdx = idx;
    }

    public int getLoginIdx() {
        return this.loginIdx;
    }

    @Override
    public int getAsInt() {
        return getLoginIdx();
    }
}
