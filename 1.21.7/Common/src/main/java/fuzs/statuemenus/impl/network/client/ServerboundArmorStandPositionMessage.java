package fuzs.statuemenus.impl.network.client;

import fuzs.puzzleslib.api.network.v4.message.MessageListener;
import fuzs.puzzleslib.api.network.v4.message.play.ServerboundPlayMessage;
import fuzs.statuemenus.api.v1.world.inventory.ArmorStandMenu;
import fuzs.statuemenus.impl.StatueMenus;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.decoration.ArmorStand;

public record ServerboundArmorStandPositionMessage(double posX,
                                                   double posY,
                                                   double posZ) implements ServerboundPlayMessage {
    public static final StreamCodec<ByteBuf, ServerboundArmorStandPositionMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE,
            ServerboundArmorStandPositionMessage::posX,
            ByteBufCodecs.DOUBLE,
            ServerboundArmorStandPositionMessage::posY,
            ByteBufCodecs.DOUBLE,
            ServerboundArmorStandPositionMessage::posZ,
            ServerboundArmorStandPositionMessage::new);

    @Override
    public MessageListener<Context> getListener() {
        return new MessageListener<Context>() {
            @Override
            public void accept(Context context) {
                if (context.player().containerMenu instanceof ArmorStandMenu menu &&
                        menu.stillValid(context.player())) {
                    if (!this.tryMoveArmorStandTo(menu.getArmorStand(),
                            ServerboundArmorStandPositionMessage.this.posX,
                            ServerboundArmorStandPositionMessage.this.posY,
                            ServerboundArmorStandPositionMessage.this.posZ)) {
                        StatueMenus.LOGGER.warn("Player {} attempted to move armor stand further than allowed",
                                context.player());
                    }
                }
            }

            private boolean tryMoveArmorStandTo(ArmorStand armorStand, double posX, double posY, double posZ) {
                if (armorStand.distanceToSqr(posX, posY, posZ) < 64.0) {
                    armorStand.snapTo(posX, posY, posZ);
                    return true;
                } else {
                    return false;
                }
            }
        };
    }
}
