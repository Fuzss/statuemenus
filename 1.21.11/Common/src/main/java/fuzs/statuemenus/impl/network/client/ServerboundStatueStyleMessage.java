package fuzs.statuemenus.impl.network.client;

import fuzs.puzzleslib.api.network.v4.message.MessageListener;
import fuzs.puzzleslib.api.network.v4.message.play.ServerboundPlayMessage;
import fuzs.statuemenus.api.v1.world.inventory.StatueMenu;
import fuzs.statuemenus.api.v1.world.inventory.data.StatueStyleOption;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;

public record ServerboundStatueStyleMessage(StatueStyleOption<?> styleOption,
                                            boolean value) implements ServerboundPlayMessage {
    public static final StreamCodec<ByteBuf, ServerboundStatueStyleMessage> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC.map(StatueStyleOption::get, StatueStyleOption::getName),
            ServerboundStatueStyleMessage::styleOption,
            ByteBufCodecs.BOOL,
            ServerboundStatueStyleMessage::value,
            ServerboundStatueStyleMessage::new);

    @Override
    public MessageListener<Context> getListener() {
        return new MessageListener<Context>() {
            @Override
            public void accept(Context context) {
                if (context.player().containerMenu instanceof StatueMenu menu
                        && menu.stillValid(context.player())) {
                    ((StatueStyleOption<LivingEntity>) ServerboundStatueStyleMessage.this.styleOption).setOption(
                            menu.getEntity(),
                            ServerboundStatueStyleMessage.this.value);
                }
            }
        };
    }
}
