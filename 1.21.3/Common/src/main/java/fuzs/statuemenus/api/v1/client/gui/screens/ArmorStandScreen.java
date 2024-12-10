package fuzs.statuemenus.api.v1.client.gui.screens;

import fuzs.statuemenus.api.v1.network.client.data.DataSyncHandler;
import fuzs.statuemenus.api.v1.world.inventory.ArmorStandHolder;
import fuzs.statuemenus.api.v1.world.inventory.ArmorStandMenu;
import fuzs.statuemenus.api.v1.world.inventory.data.ArmorStandScreenType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public interface ArmorStandScreen {

    ArmorStandHolder getHolder();

    ArmorStandScreenType getScreenType();

    <T extends Screen & MenuAccess<ArmorStandMenu> & ArmorStandScreen> T createScreenType(ArmorStandScreenType screenType);

    void setMouseX(int mouseX);

    void setMouseY(int mouseY);

    DataSyncHandler getDataSyncHandler();

    default void renderArmorStandInInventory(GuiGraphics guiGraphics, int posX, int posY, int scale, float mouseX, float mouseY) {
        ArmorStand armorStand = this.getHolder().getArmorStand();
        Runnable finalizeInInventoryRendering = this.getHolder()
                .getDataProvider()
                .setupInInventoryRendering(armorStand);
        renderEntityInInventoryFollowsMouse(guiGraphics, posX, posY, scale, mouseX, mouseY, armorStand);
        finalizeInInventoryRendering.run();
    }

    /**
     * The legacy implementation of the inventory entity renderer, does not support scissor, but helps with porting.
     */
    private static void renderEntityInInventoryFollowsMouse(GuiGraphics guiGraphics, int posX, int posY, int scale, float mouseX, float mouseY, LivingEntity livingEntity) {
        float f = (float) Math.atan(mouseX / 40.0F);
        float g = (float) Math.atan(mouseY / 40.0F);
        Quaternionf quaternionf = (new Quaternionf()).rotateZ(Mth.PI);
        Quaternionf quaternionf2 = (new Quaternionf()).rotateX(g * 20.0F * Mth.DEG_TO_RAD);
        quaternionf.mul(quaternionf2);
        float h = livingEntity.yBodyRot;
        float i = livingEntity.getYRot();
        float j = livingEntity.getXRot();
        float k = livingEntity.yHeadRotO;
        float l = livingEntity.yHeadRot;
        livingEntity.yBodyRot = 180.0F + f * 20.0F;
        livingEntity.setYRot(180.0F + f * 40.0F);
        livingEntity.setXRot(-g * 20.0F);
        livingEntity.yHeadRot = livingEntity.getYRot();
        livingEntity.yHeadRotO = livingEntity.getYRot();
        InventoryScreen.renderEntityInInventory(guiGraphics,
                posX,
                posY,
                scale / livingEntity.getScale(),
                new Vector3f(),
                quaternionf,
                quaternionf2,
                livingEntity);
        livingEntity.yBodyRot = h;
        livingEntity.setYRot(i);
        livingEntity.setXRot(j);
        livingEntity.yHeadRotO = k;
        livingEntity.yHeadRot = l;
    }
}
