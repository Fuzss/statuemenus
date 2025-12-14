package fuzs.statuemenus.impl.network.client;

import fuzs.puzzleslib.api.network.v4.codec.ExtraStreamCodecs;
import fuzs.puzzleslib.api.network.v4.message.MessageListener;
import fuzs.puzzleslib.api.network.v4.message.play.ServerboundPlayMessage;
import fuzs.statuemenus.api.v1.helper.ScaleAttributeHelper;
import fuzs.statuemenus.api.v1.world.inventory.StatueMenu;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;

public record ServerboundStatuePropertyMessage(DataType dataType, float value) implements ServerboundPlayMessage {
    public static final StreamCodec<ByteBuf, ServerboundStatuePropertyMessage> STREAM_CODEC = StreamCodec.composite(
            ExtraStreamCodecs.fromEnum(DataType.class),
            ServerboundStatuePropertyMessage::dataType,
            ByteBufCodecs.FLOAT,
            ServerboundStatuePropertyMessage::value,
            ServerboundStatuePropertyMessage::new);

    @Override
    public MessageListener<Context> getListener() {
        return new MessageListener<Context>() {
            @Override
            public void accept(Context context) {
                if (context.player().containerMenu instanceof StatueMenu menu
                        && menu.stillValid(context.player())) {
                    LivingEntity livingEntity = menu.getEntity();
                    float value = ServerboundStatuePropertyMessage.this.value;
                    switch (ServerboundStatuePropertyMessage.this.dataType) {
                        case ROTATION -> {
                            livingEntity.setYRot(value);
                            // might not be necessary
                            livingEntity.setYBodyRot(value);
                        }
                        case SCALE -> {
                            ScaleAttributeHelper.setScale(livingEntity, value);
                        }
                    }
                }
            }
        };
    }

    public enum DataType {
        SCALE,
        ROTATION
    }
}
