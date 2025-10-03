package fuzs.statuemenus.impl.network.client;

import fuzs.puzzleslib.api.network.v4.message.MessageListener;
import fuzs.puzzleslib.api.network.v4.message.play.ServerboundPlayMessage;
import fuzs.statuemenus.api.v1.world.inventory.ArmorStandMenu;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Rotations;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.decoration.ArmorStand;

public record ServerboundArmorStandPoseMessage(ArmorStand.ArmorStandPose pose) implements ServerboundPlayMessage {
    public static final StreamCodec<ByteBuf, ArmorStand.ArmorStandPose> POSE_STREAM_CODEC = StreamCodec.composite(
            Rotations.STREAM_CODEC,
            ArmorStand.ArmorStandPose::head,
            Rotations.STREAM_CODEC,
            ArmorStand.ArmorStandPose::body,
            Rotations.STREAM_CODEC,
            ArmorStand.ArmorStandPose::leftArm,
            Rotations.STREAM_CODEC,
            ArmorStand.ArmorStandPose::rightArm,
            Rotations.STREAM_CODEC,
            ArmorStand.ArmorStandPose::leftLeg,
            Rotations.STREAM_CODEC,
            ArmorStand.ArmorStandPose::rightLeg,
            ArmorStand.ArmorStandPose::new);
    public static final StreamCodec<ByteBuf, ServerboundArmorStandPoseMessage> STREAM_CODEC = StreamCodec.composite(
            POSE_STREAM_CODEC,
            ServerboundArmorStandPoseMessage::pose,
            ServerboundArmorStandPoseMessage::new);

    @Override
    public MessageListener<Context> getListener() {
        return new MessageListener<Context>() {
            @Override
            public void accept(Context context) {
                if (context.player().containerMenu instanceof ArmorStandMenu menu
                        && menu.stillValid(context.player())) {
                    menu.getArmorStand().setArmorStandPose(ServerboundArmorStandPoseMessage.this.pose);
                }
            }
        };
    }
}
