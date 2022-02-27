package net.textstack.band_of_gigantism.network;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.FMLHandshakeHandler;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.textstack.band_of_gigantism.BandOfGigantism;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collections;
import java.util.Objects;
import java.util.function.IntSupplier;

public final class Network {
    private static final ResourceLocation NAME = new ResourceLocation(BandOfGigantism.MODID,"network");

    public static SimpleChannel channel;

    public static void init() {
        channel = NetworkRegistry.ChannelBuilder.named(NAME)
                .clientAcceptedVersions(s -> Objects.equals(s, "2"))
                .serverAcceptedVersions(s -> Objects.equals(s, "2"))
                .networkProtocolVersion(() -> "2")
                .simpleChannel();

        channel.messageBuilder(BOGItemsPacket.class, 1, NetworkDirection.LOGIN_TO_CLIENT)
                .decoder(BOGItemsPacket::decode)
                .encoder(BOGItemsPacket::encode)
                .consumer(BOGItemsPacket::handle)
                .buildLoginPacketList(isLocal -> isLocal ? Collections.emptyList() :
                        Collections.singletonList(Pair.of(BOGItemsPacket.class.getName(), new BOGItemsPacket()))
                )
                .loginIndex(BOGItemsPacket::getLoginIdx, BOGItemsPacket::setLoginIdx)
                .add();

        channel.messageBuilder(SimpleReply.class, 2, NetworkDirection.LOGIN_TO_SERVER)
                .decoder(pb -> new SimpleReply())
                .encoder((r, pb) -> {})
                .consumer(FMLHandshakeHandler.indexFirst((handler, pkt, ctx) -> ctx.get().setPacketHandled(true)))
                .loginIndex(SimpleReply::getIdx, SimpleReply::setIdx)
                .add();
    }

    public static class SimpleReply implements IntSupplier {
        private int idx;

        @Override
        public int getAsInt() {
            return getIdx();
        }

        public int getIdx() {
            return idx;
        }

        public void setIdx(int idx) {
            this.idx = idx;
        }
    }
}
