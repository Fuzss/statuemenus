package fuzs.statuemenus.impl.network.client;

import fuzs.puzzleslib.api.network.v3.ServerMessageListener;
import fuzs.puzzleslib.api.network.v3.ServerboundMessage;
import fuzs.statuemenus.api.v1.helper.ScaleAttributeHelper;
import fuzs.statuemenus.api.v1.world.inventory.ArmorStandMenu;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.decoration.ArmorStand;

public record ServerboundArmorStandPropertyMessage(Type type,
                                                   float value) implements ServerboundMessage<ServerboundArmorStandPropertyMessage> {

    @Override
    public ServerMessageListener<ServerboundArmorStandPropertyMessage> getHandler() {
        return new ServerMessageListener<>() {

            @Override
            public void handle(ServerboundArmorStandPropertyMessage message, MinecraftServer minecraftServer, ServerGamePacketListenerImpl serverGamePacketListener, ServerPlayer serverPlayer, ServerLevel serverLevel) {
                if (serverPlayer.containerMenu instanceof ArmorStandMenu menu && menu.stillValid(serverPlayer)) {
                    ArmorStand armorStand = menu.getArmorStand();
                    switch (message.type) {
                        case ROTATION -> {
                            armorStand.setYRot(message.value);
                            // not sure if this is necessary...
                            armorStand.setYBodyRot(message.value);
                        }
                        case SCALE -> {
                            ScaleAttributeHelper.setScale(armorStand, message.value);
                        }
                    }
                }
            }
        };
    }

    public enum Type {
        SCALE,
        ROTATION
    }
}
