package fuzs.statuemenus.impl.network.client;

import fuzs.puzzleslib.api.network.v4.codec.ExtraStreamCodecs;
import fuzs.puzzleslib.api.network.v4.message.MessageListener;
import fuzs.puzzleslib.api.network.v4.message.play.ServerboundPlayMessage;
import fuzs.statuemenus.api.v1.helper.ScaleAttributeHelper;
import fuzs.statuemenus.api.v1.world.inventory.ArmorStandMenu;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.decoration.ArmorStand;

public record ServerboundArmorStandPropertyMessage(DataType dataType, float value) implements ServerboundPlayMessage {
    public static final StreamCodec<ByteBuf, ServerboundArmorStandPropertyMessage> STREAM_CODEC = StreamCodec.composite(
            ExtraStreamCodecs.fromEnum(DataType.class),
            ServerboundArmorStandPropertyMessage::dataType,
            ByteBufCodecs.FLOAT,
            ServerboundArmorStandPropertyMessage::value,
            ServerboundArmorStandPropertyMessage::new);

    @Override
    public MessageListener<Context> getListener() {
        return new MessageListener<Context>() {
            @Override
            public void accept(Context context) {
                if (context.player().containerMenu instanceof ArmorStandMenu menu &&
                        menu.stillValid(context.player())) {
                    ArmorStand armorStand = menu.getArmorStand();
                    float value = ServerboundArmorStandPropertyMessage.this.value;
                    switch (ServerboundArmorStandPropertyMessage.this.dataType) {
                        case ROTATION -> {
                            armorStand.setYRot(value);
                            // might not be necessary
                            armorStand.setYBodyRot(value);
                        }
                        case SCALE -> {
                            ScaleAttributeHelper.setScale(armorStand, value);
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
