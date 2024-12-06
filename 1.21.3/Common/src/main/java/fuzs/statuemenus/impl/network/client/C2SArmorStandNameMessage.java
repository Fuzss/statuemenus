package fuzs.statuemenus.impl.network.client;

import fuzs.puzzleslib.api.network.v2.WritableMessage;
import fuzs.statuemenus.api.v1.network.client.data.DataSyncHandler;
import fuzs.statuemenus.api.v1.world.inventory.ArmorStandMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public class C2SArmorStandNameMessage implements WritableMessage<C2SArmorStandNameMessage> {
    private final String name;

    public C2SArmorStandNameMessage(FriendlyByteBuf buf) {
        this.name = buf.readUtf();
    }

    public C2SArmorStandNameMessage(String name) {
        this.name = name;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(this.name);
    }

    @Override
    public MessageHandler<C2SArmorStandNameMessage> makeHandler() {
        return new MessageHandler<>() {

            @Override
            public void handle(C2SArmorStandNameMessage message, Player player, Object gameInstance) {
                if (player.containerMenu instanceof ArmorStandMenu menu && menu.stillValid(player)) {
                    DataSyncHandler.setCustomArmorStandName(menu.getArmorStand(), message.name);
                }
            }
        };
    }
}
