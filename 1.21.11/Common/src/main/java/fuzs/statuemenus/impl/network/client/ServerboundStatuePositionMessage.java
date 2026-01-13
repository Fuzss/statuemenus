package fuzs.statuemenus.impl.network.client;

import fuzs.puzzleslib.api.network.v4.message.MessageListener;
import fuzs.puzzleslib.api.network.v4.message.play.ServerboundPlayMessage;
import fuzs.statuemenus.api.v1.world.inventory.StatueMenu;
import fuzs.statuemenus.impl.StatueMenus;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ServerboundStatuePositionMessage(double posX,
                                               double posY,
                                               double posZ) implements ServerboundPlayMessage {
    public static final StreamCodec<ByteBuf, ServerboundStatuePositionMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE,
            ServerboundStatuePositionMessage::posX,
            ByteBufCodecs.DOUBLE,
            ServerboundStatuePositionMessage::posY,
            ByteBufCodecs.DOUBLE,
            ServerboundStatuePositionMessage::posZ,
            ServerboundStatuePositionMessage::new);

    @Override
    public MessageListener<Context> getListener() {
        return new MessageListener<Context>() {
            @Override
            public void accept(Context context) {
                if (context.player().containerMenu instanceof StatueMenu menu && menu.stillValid(context.player())) {
                    if (context.player().isWithinEntityInteractionRange(menu.getEntity(), 3.0)) {
                        menu.getEntity()
                                .snapTo(ServerboundStatuePositionMessage.this.posX,
                                        ServerboundStatuePositionMessage.this.posY,
                                        ServerboundStatuePositionMessage.this.posZ);
                    } else {
                        StatueMenus.LOGGER.warn("Player {} attempted to move armor stand further than allowed",
                                context.player());
                    }
                }
            }
        };
    }
}
