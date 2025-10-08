package fuzs.statuemenus.impl.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ArmorStandPosesScreenHelper {

    /**
     * @see net.minecraft.client.gui.screens.inventory.InventoryScreen#renderEntityInInventoryFollowsMouse(GuiGraphics,
     *         int, int, int, int, int, float, float, float, LivingEntity)
     */
    public static void renderEntityInInventoryFollowsMouse(GuiGraphics guiGraphics, int x1, int y1, int x2, int y2, int scale, float yOffset, float mouseX, float mouseY, LivingEntity livingEntity, float partialTick) {
        float f = (x1 + x2) / 2.0F;
        float g = (y1 + y2) / 2.0F;
        guiGraphics.enableScissor(x1, y1, x2, y2);
        float h = (float) Math.atan((f - mouseX) / 40.0F);
        float i = (float) Math.atan((g - mouseY) / 40.0F);
        Quaternionf quaternionf = new Quaternionf().rotateZ((float) Math.PI);
        Quaternionf quaternionf2 = new Quaternionf().rotateX(i * 20.0F * (float) (Math.PI / 180.0));
        quaternionf.mul(quaternionf2);
        float j = livingEntity.yBodyRot;
        float k = livingEntity.getYRot();
        float l = livingEntity.getXRot();
        float m = livingEntity.yHeadRotO;
        float n = livingEntity.yHeadRot;
        livingEntity.yBodyRot = 180.0F + h * 20.0F;
        livingEntity.setYRot(180.0F + h * 40.0F);
        livingEntity.setXRot(-i * 20.0F);
        livingEntity.yHeadRot = livingEntity.getYRot();
        livingEntity.yHeadRotO = livingEntity.getYRot();
        float o = livingEntity.getScale();
        Vector3f vector3f = new Vector3f(0.0F, livingEntity.getBbHeight() / 2.0F + yOffset * o, 0.0F);
        float p = scale / o;
        renderEntityInInventory(guiGraphics,
                x1,
                y1,
                x2,
                y2,
                p,
                vector3f,
                quaternionf,
                quaternionf2,
                livingEntity,
                partialTick);
        livingEntity.yBodyRot = j;
        livingEntity.setYRot(k);
        livingEntity.setXRot(l);
        livingEntity.yHeadRotO = m;
        livingEntity.yHeadRot = n;
        guiGraphics.disableScissor();
    }

    /**
     * @see net.minecraft.client.gui.screens.inventory.InventoryScreen#renderEntityInInventory(GuiGraphics, int, int,
     *         int, int, float, Vector3f, Quaternionf, Quaternionf, LivingEntity)
     */
    public static <S extends EntityRenderState> void renderEntityInInventory(GuiGraphics guiGraphics, int x1, int y1, int x2, int y2, float scale, Vector3f translation, Quaternionf rotation, @Nullable Quaternionf overrideCameraAngle, LivingEntity livingEntity, float partialTick) {
        EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        EntityRenderer<? super LivingEntity, S> entityRenderer = (EntityRenderer<? super LivingEntity, S>) entityRenderDispatcher.getRenderer(
                livingEntity);
        S entityRenderState = entityRenderer.createRenderState();
        entityRenderer.extractRenderState(livingEntity, entityRenderState, partialTick);
        entityRenderState.hitboxesRenderState = null;
        guiGraphics.submitEntityRenderState(entityRenderState,
                scale,
                translation,
                rotation,
                overrideCameraAngle,
                x1,
                y1,
                x2,
                y2);
    }
}
