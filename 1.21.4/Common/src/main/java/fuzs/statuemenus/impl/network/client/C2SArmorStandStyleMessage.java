package fuzs.statuemenus.impl.network.client;

import fuzs.puzzleslib.api.network.v2.WritableMessage;
import fuzs.statuemenus.api.v1.world.inventory.ArmorStandMenu;
import fuzs.statuemenus.api.v1.world.inventory.data.ArmorStandStyleOption;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class C2SArmorStandStyleMessage implements WritableMessage<C2SArmorStandStyleMessage> {
    private final ResourceLocation styleOption;
    private final boolean value;

    public C2SArmorStandStyleMessage(FriendlyByteBuf buf) {
        this.styleOption = buf.readResourceLocation();
        this.value = buf.readBoolean();
    }

    public C2SArmorStandStyleMessage(ArmorStandStyleOption styleOption, boolean value) {
        this.styleOption = styleOption.getId();
        this.value = value;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.styleOption);
        buf.writeBoolean(this.value);
    }

    @Override
    public MessageHandler<C2SArmorStandStyleMessage> makeHandler() {
        return new MessageHandler<>() {

            @Override
            public void handle(C2SArmorStandStyleMessage message, Player player, Object gameInstance) {
                if (player.containerMenu instanceof ArmorStandMenu menu && menu.stillValid(player)) {
                    ArmorStandStyleOption.get(message.styleOption).setOption(menu.getArmorStand(), message.value);
                }
            }
        };
    }
}
