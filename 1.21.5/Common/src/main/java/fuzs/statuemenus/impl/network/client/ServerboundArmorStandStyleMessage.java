package fuzs.statuemenus.impl.network.client;

import fuzs.puzzleslib.api.network.v4.message.MessageListener;
import fuzs.puzzleslib.api.network.v4.message.play.ServerboundPlayMessage;
import fuzs.statuemenus.api.v1.world.inventory.ArmorStandMenu;
import fuzs.statuemenus.api.v1.world.inventory.data.ArmorStandStyleOption;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record ServerboundArmorStandStyleMessage(ArmorStandStyleOption styleOption,
                                                boolean value) implements ServerboundPlayMessage {
    public static final StreamCodec<ByteBuf, ServerboundArmorStandStyleMessage> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC.map(ArmorStandStyleOption::get, ArmorStandStyleOption::getId),
            ServerboundArmorStandStyleMessage::styleOption,
            ByteBufCodecs.BOOL,
            ServerboundArmorStandStyleMessage::value,
            ServerboundArmorStandStyleMessage::new);

    @Override
    public MessageListener<Context> getListener() {
        return new MessageListener<Context>() {
            @Override
            public void accept(Context context) {
                if (context.player().containerMenu instanceof ArmorStandMenu menu &&
                        menu.stillValid(context.player())) {
                    ServerboundArmorStandStyleMessage.this.styleOption.setOption(menu.getArmorStand(),
                            ServerboundArmorStandStyleMessage.this.value);
                }
            }
        };
    }
}
