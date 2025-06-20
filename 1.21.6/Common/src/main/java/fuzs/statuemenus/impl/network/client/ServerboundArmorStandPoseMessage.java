package fuzs.statuemenus.impl.network.client;

import fuzs.puzzleslib.api.network.v4.message.MessageListener;
import fuzs.puzzleslib.api.network.v4.message.play.ServerboundPlayMessage;
import fuzs.statuemenus.api.v1.world.inventory.ArmorStandMenu;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ServerboundArmorStandPoseMessage(CompoundTag tag) implements ServerboundPlayMessage {
    public static final StreamCodec<ByteBuf, ServerboundArmorStandPoseMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG,
            ServerboundArmorStandPoseMessage::tag,
            ServerboundArmorStandPoseMessage::new);

    @Override
    public MessageListener<Context> getListener() {
        return new MessageListener<Context>() {
            @Override
            public void accept(Context context) {
                if (context.player().containerMenu instanceof ArmorStandMenu menu &&
                        menu.stillValid(context.player())) {
                    menu.getArmorStand().readPose(ServerboundArmorStandPoseMessage.this.tag);
                }
            }
        };
    }
}
