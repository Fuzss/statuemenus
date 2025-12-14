package fuzs.statuemenus.api.v1.network.client.data;

import fuzs.puzzleslib.api.network.v4.MessageSender;
import fuzs.statuemenus.api.v1.world.inventory.StatueHolder;
import fuzs.statuemenus.api.v1.world.inventory.data.StatuePose;
import fuzs.statuemenus.api.v1.world.inventory.data.StatueStyleOption;
import fuzs.statuemenus.impl.network.client.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;

public class NetworkDataSyncHandler implements DataSyncHandler {
    private final StatueHolder holder;

    public NetworkDataSyncHandler(StatueHolder holder) {
        this.holder = holder;
    }

    @Override
    public StatueHolder getArmorStandHolder() {
        return this.holder;
    }

    @Override
    public void sendName(String name) {
        DataSyncHandler.setCustomArmorStandName(this.getEntity(), name);
        MessageSender.broadcast(new ServerboundStatueNameMessage(name));
    }

    @Override
    public void sendPose(StatuePose pose, boolean finalize) {
        pose.applyToEntity(this.getArmorStandHolder().getStatueEntity());
        CompoundTag compoundTag = new CompoundTag();
        pose.serializeAllPoses(compoundTag);
        ArmorStand.ArmorStandPose armorStandPose = ArmorStand.ArmorStandPose.CODEC.parse(NbtOps.INSTANCE, compoundTag)
                .getPartialOrThrow();
        MessageSender.broadcast(new ServerboundStatuePoseMessage(armorStandPose));
    }

    @Override
    public void sendPosition(double posX, double posY, double posZ, boolean finalize) {
        MessageSender.broadcast(new ServerboundStatuePositionMessage(posX, posY, posZ));
    }

    @Override
    public void sendScale(float scale, boolean finalize) {
        MessageSender.broadcast(new ServerboundStatuePropertyMessage(ServerboundStatuePropertyMessage.DataType.SCALE,
                scale));
    }

    @Override
    public void sendRotation(float rotation, boolean finalize) {
        MessageSender.broadcast(new ServerboundStatuePropertyMessage(ServerboundStatuePropertyMessage.DataType.ROTATION,
                rotation));
    }

    @Override
    public void sendStyleOption(StatueStyleOption<?> styleOption, boolean value, boolean finalize) {
        ((StatueStyleOption<LivingEntity>) styleOption).setOption(this.getEntity(), value);
        MessageSender.broadcast(new ServerboundStatueStyleMessage(styleOption, value));
    }
}
