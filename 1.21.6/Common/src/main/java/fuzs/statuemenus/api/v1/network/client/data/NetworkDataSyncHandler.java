package fuzs.statuemenus.api.v1.network.client.data;

import fuzs.puzzleslib.api.network.v4.MessageSender;
import fuzs.statuemenus.api.v1.world.inventory.ArmorStandHolder;
import fuzs.statuemenus.api.v1.world.inventory.data.ArmorStandPose;
import fuzs.statuemenus.api.v1.world.inventory.data.ArmorStandStyleOption;
import fuzs.statuemenus.impl.network.client.*;
import net.minecraft.nbt.CompoundTag;

public class NetworkDataSyncHandler implements DataSyncHandler {
    private final ArmorStandHolder holder;

    public NetworkDataSyncHandler(ArmorStandHolder holder) {
        this.holder = holder;
    }

    @Override
    public ArmorStandHolder getArmorStandHolder() {
        return this.holder;
    }

    @Override
    public void sendName(String name) {
        DataSyncHandler.setCustomArmorStandName(this.getArmorStand(), name);
        MessageSender.broadcast(new ServerboundArmorStandNameMessage(name));
    }

    @Override
    public void sendPose(ArmorStandPose pose, boolean finalize) {
        pose.applyToEntity(this.getArmorStand());
        CompoundTag compoundTag = new CompoundTag();
        pose.serializeAllPoses(compoundTag);
        MessageSender.broadcast(new ServerboundArmorStandPoseMessage(compoundTag));
    }

    @Override
    public void sendPosition(double posX, double posY, double posZ, boolean finalize) {
        MessageSender.broadcast(new ServerboundArmorStandPositionMessage(posX, posY, posZ));
    }

    @Override
    public void sendScale(float scale, boolean finalize) {
        MessageSender.broadcast(new ServerboundArmorStandPropertyMessage(ServerboundArmorStandPropertyMessage.DataType.SCALE,
                scale));
    }

    @Override
    public void sendRotation(float rotation, boolean finalize) {
        MessageSender.broadcast(new ServerboundArmorStandPropertyMessage(ServerboundArmorStandPropertyMessage.DataType.ROTATION,
                rotation));
    }

    @Override
    public void sendStyleOption(ArmorStandStyleOption styleOption, boolean value, boolean finalize) {
        styleOption.setOption(this.getArmorStand(), value);
        MessageSender.broadcast(new ServerboundArmorStandStyleMessage(styleOption, value));
    }
}
