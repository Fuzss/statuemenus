package fuzs.statuemenus.impl.network.client;

import fuzs.puzzleslib.api.network.v2.WritableMessage;
import fuzs.statuemenus.api.v1.world.inventory.ArmorStandMenu;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public class C2SArmorStandPoseMessage implements WritableMessage<C2SArmorStandPoseMessage> {
    private final CompoundTag tag;

    public C2SArmorStandPoseMessage(FriendlyByteBuf buf) {
        this.tag = buf.readNbt();
    }

    public C2SArmorStandPoseMessage(CompoundTag tag) {
        this.tag = tag;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeNbt(this.tag);
    }

    @Override
    public MessageHandler<C2SArmorStandPoseMessage> makeHandler() {
        return new MessageHandler<>() {

            @Override
            public void handle(C2SArmorStandPoseMessage message, Player player, Object gameInstance) {
                if (player.containerMenu instanceof ArmorStandMenu menu && menu.stillValid(player)) {
                    menu.getArmorStand().readPose(message.tag);
                }
            }
        };
    }
}
