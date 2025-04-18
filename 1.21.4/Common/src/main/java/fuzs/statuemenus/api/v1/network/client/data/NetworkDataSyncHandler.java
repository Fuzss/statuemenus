package fuzs.statuemenus.api.v1.network.client.data;

import fuzs.statuemenus.api.v1.world.inventory.ArmorStandHolder;
import fuzs.statuemenus.api.v1.world.inventory.data.ArmorStandPose;
import fuzs.statuemenus.api.v1.world.inventory.data.ArmorStandStyleOption;
import fuzs.statuemenus.impl.StatueMenus;
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
        StatueMenus.NETWORK.sendMessage(new C2SArmorStandNameMessage(name).toServerboundMessage());
    }

    @Override
    public void sendPose(ArmorStandPose pose, boolean finalize) {
        pose.applyToEntity(this.getArmorStand());
        CompoundTag compoundTag = new CompoundTag();
        pose.serializeAllPoses(compoundTag);
        StatueMenus.NETWORK.sendMessage(new C2SArmorStandPoseMessage(compoundTag).toServerboundMessage());
    }

    @Override
    public void sendPosition(double posX, double posY, double posZ, boolean finalize) {
        StatueMenus.NETWORK.sendMessage(new C2SArmorStandPositionMessage(posX, posY, posZ).toServerboundMessage());
    }

    @Override
    public void sendScale(float scale, boolean finalize) {
        StatueMenus.NETWORK.sendMessage(new ServerboundArmorStandPropertyMessage(ServerboundArmorStandPropertyMessage.Type.SCALE,
                scale));
    }

    @Override
    public void sendRotation(float rotation, boolean finalize) {
        StatueMenus.NETWORK.sendMessage(new ServerboundArmorStandPropertyMessage(ServerboundArmorStandPropertyMessage.Type.ROTATION,
                rotation));
    }

    @Override
    public void sendStyleOption(ArmorStandStyleOption styleOption, boolean value, boolean finalize) {
        styleOption.setOption(this.getArmorStand(), value);
        StatueMenus.NETWORK.sendMessage(new C2SArmorStandStyleMessage(styleOption, value).toServerboundMessage());
    }
}
