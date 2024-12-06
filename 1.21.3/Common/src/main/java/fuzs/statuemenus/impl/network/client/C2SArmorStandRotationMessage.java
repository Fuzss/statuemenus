package fuzs.statuemenus.impl.network.client;

import fuzs.puzzleslib.api.network.v2.WritableMessage;
import fuzs.statuemenus.api.v1.world.inventory.ArmorStandMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;

public class C2SArmorStandRotationMessage implements WritableMessage<C2SArmorStandRotationMessage> {
    private final float rotation;

    public C2SArmorStandRotationMessage(FriendlyByteBuf buf) {
        this.rotation = buf.readFloat();
    }

    public C2SArmorStandRotationMessage(float rotation) {
        this.rotation = rotation;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeFloat(this.rotation);
    }

    @Override
    public MessageHandler<C2SArmorStandRotationMessage> makeHandler() {
        return new MessageHandler<>() {

            @Override
            public void handle(C2SArmorStandRotationMessage message, Player player, Object gameInstance) {
                if (player.containerMenu instanceof ArmorStandMenu menu && menu.stillValid(player)) {
                    ArmorStand armorStand = menu.getArmorStand();
                    armorStand.setYRot(message.rotation);
                    // not sure if this is necessary...
                    armorStand.setYBodyRot(message.rotation);
                }
            }
        };
    }
}
