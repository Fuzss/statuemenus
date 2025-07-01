package fuzs.statuemenus.impl.network.client;

import fuzs.puzzleslib.api.network.v4.message.MessageListener;
import fuzs.puzzleslib.api.network.v4.message.play.ServerboundPlayMessage;
import fuzs.statuemenus.api.v1.network.client.data.DataSyncHandler;
import fuzs.statuemenus.api.v1.world.inventory.ArmorStandMenu;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ServerboundArmorStandNameMessage(String name) implements ServerboundPlayMessage {
    public static final StreamCodec<ByteBuf, ServerboundArmorStandNameMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            ServerboundArmorStandNameMessage::name,
            ServerboundArmorStandNameMessage::new);

    @Override
    public MessageListener<Context> getListener() {
        return new MessageListener<Context>() {
            @Override
            public void accept(Context context) {
                if (context.player().containerMenu instanceof ArmorStandMenu menu &&
                        menu.stillValid(context.player())) {
                    DataSyncHandler.setCustomArmorStandName(menu.getArmorStand(),
                            ServerboundArmorStandNameMessage.this.name);
                }
            }
        };
    }
}
