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
    public static void renderEntityInInventoryFollowsMouse(GuiGraphics guiGraphics, int x1, int y1, int x2, int y2, int scale, float yOffset, float mouseX, float mouseY, LivingEntity entity) {
        float f = (x1 + x2) / 2.0F;
        float g = (y1 + y2) / 2.0F;
        guiGraphics.enableScissor(x1, y1, x2, y2);
        float h = (float) Math.atan((f - mouseX) / 40.0F);
        float i = (float) Math.atan((g - mouseY) / 40.0F);
        Quaternionf quaternionf = new Quaternionf().rotateZ((float) Math.PI);
        Quaternionf quaternionf2 = new Quaternionf().rotateX(i * 20.0F * (float) (Math.PI / 180.0));
        quaternionf.mul(quaternionf2);
        float j = entity.yBodyRot;
        float k = entity.getYRot();
        float l = entity.getXRot();
        float m = entity.yHeadRotO;
        float n = entity.yHeadRot;
        entity.yBodyRot = 180.0F + h * 20.0F;
        entity.setYRot(180.0F + h * 40.0F);
        entity.setXRot(-i * 20.0F);
        entity.yHeadRot = entity.getYRot();
        entity.yHeadRotO = entity.getYRot();
        float o = entity.getScale();
        Vector3f vector3f = new Vector3f(0.0F, entity.getBbHeight() / 2.0F + yOffset * o, 0.0F);
        float p = scale / o;
        renderEntityInInventory(guiGraphics, x1, y1, x2, y2, p, vector3f, quaternionf, quaternionf2, entity);
        entity.yBodyRot = j;
        entity.setYRot(k);
        entity.setXRot(l);
        entity.yHeadRotO = m;
        entity.yHeadRot = n;
        guiGraphics.disableScissor();
    }

    /**
     * @see net.minecraft.client.gui.screens.inventory.InventoryScreen#renderEntityInInventory(GuiGraphics, int, int,
     *         int, int, float, Vector3f, Quaternionf, Quaternionf, LivingEntity)
     */
    public static <S extends EntityRenderState> void renderEntityInInventory(GuiGraphics guiGraphics, int i, int j, int k, int l, float f, Vector3f vector3f, Quaternionf quaternionf, @Nullable Quaternionf quaternionf2, LivingEntity livingEntity) {
        EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        EntityRenderer<? super LivingEntity, S> entityRenderer = (EntityRenderer<? super LivingEntity, S>) entityRenderDispatcher.getRenderer(
                livingEntity);
        S entityRenderState = entityRenderer.createRenderState();
        entityRenderer.extractRenderState(livingEntity, entityRenderState, 1.0F);
        entityRenderState.hitboxesRenderState = null;
        guiGraphics.submitEntityRenderState(entityRenderState, f, vector3f, quaternionf, quaternionf2, i, j, k, l);
    }
}
