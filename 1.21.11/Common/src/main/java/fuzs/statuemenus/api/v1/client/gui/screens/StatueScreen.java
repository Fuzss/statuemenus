package fuzs.statuemenus.api.v1.client.gui.screens;

import fuzs.statuemenus.api.v1.network.client.data.DataSyncHandler;
import fuzs.statuemenus.api.v1.world.inventory.StatueHolder;
import fuzs.statuemenus.api.v1.world.inventory.StatueMenu;
import fuzs.statuemenus.api.v1.world.inventory.data.StatueScreenType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.entity.LivingEntity;

public interface StatueScreen {

    StatueHolder getHolder();

    StatueScreenType getScreenType();

    <T extends Screen & MenuAccess<StatueMenu> & StatueScreen> T createScreenType(StatueScreenType screenType);

    void setMouseX(int mouseX);

    void setMouseY(int mouseY);

    DataSyncHandler getDataSyncHandler();

    default int getInventoryEntityScissorWidth(boolean smallInventoryEntity) {
        return smallInventoryEntity ? 48 : 74;
    }

    default int getInventoryEntityScissorHeight(boolean smallInventoryEntity) {
        return smallInventoryEntity ? 70 : 106;
    }

    default int getInventoryEntityScale(boolean smallInventoryEntity) {
        return smallInventoryEntity ? 30 : 45;
    }

    default void renderArmorStandInInventory(GuiGraphics guiGraphics, int x1, int y1, int x2, int y2, int scale, float mouseX, float mouseY, float partialTick) {
        LivingEntity livingEntity = this.getHolder().getEntity();
        Runnable finalizeInInventoryRendering = this.getHolder()
                .getStatueEntity()
                .setupInInventoryRendering(livingEntity);
        InventoryScreen.renderEntityInInventoryFollowsMouse(guiGraphics,
                x1,
                y1,
                x2,
                y2,
                scale,
                0.0625F,
                mouseX,
                mouseY,
                livingEntity);
        finalizeInInventoryRendering.run();
    }
}
