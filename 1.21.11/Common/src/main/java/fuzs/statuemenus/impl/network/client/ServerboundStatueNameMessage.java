package fuzs.statuemenus.impl.network.client;

import fuzs.puzzleslib.api.network.v4.message.MessageListener;
import fuzs.puzzleslib.api.network.v4.message.play.ServerboundPlayMessage;
import fuzs.statuemenus.api.v1.network.client.data.DataSyncHandler;
import fuzs.statuemenus.api.v1.world.inventory.StatueMenu;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ServerboundStatueNameMessage(String name) implements ServerboundPlayMessage {
    public static final StreamCodec<ByteBuf, ServerboundStatueNameMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            ServerboundStatueNameMessage::name,
            ServerboundStatueNameMessage::new);

    @Override
    public MessageListener<Context> getListener() {
        return new MessageListener<Context>() {
            @Override
            public void accept(Context context) {
                if (context.player().containerMenu instanceof StatueMenu menu &&
                        menu.stillValid(context.player())) {
                    DataSyncHandler.setCustomArmorStandName(menu.getEntity(),
                            ServerboundStatueNameMessage.this.name);
                }
            }
        };
    }
}
