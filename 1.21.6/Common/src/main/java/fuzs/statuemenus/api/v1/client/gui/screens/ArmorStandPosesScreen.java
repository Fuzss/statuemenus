package fuzs.statuemenus.api.v1.client.gui.screens;

import fuzs.puzzleslib.api.client.gui.v2.components.SpritelessImageButton;
import fuzs.statuemenus.api.v1.network.client.data.DataSyncHandler;
import fuzs.statuemenus.api.v1.world.inventory.ArmorStandHolder;
import fuzs.statuemenus.api.v1.world.inventory.data.ArmorStandPose;
import fuzs.statuemenus.api.v1.world.inventory.data.ArmorStandScreenType;
import fuzs.statuemenus.impl.client.gui.components.TooltipFactories;
import fuzs.statuemenus.impl.client.util.ArmorStandPosesScreenHelper;
import fuzs.statuemenus.impl.world.inventory.ArmorStandPoses;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Inventory;

import java.util.Optional;

public class ArmorStandPosesScreen extends AbstractArmorStandScreen {
    private static int firstPoseIndex;
    private final AbstractWidget[] cycleButtons = new AbstractWidget[2];
    private final AbstractWidget[] poseButtons = new AbstractWidget[4];

    public ArmorStandPosesScreen(ArmorStandHolder holder, Inventory inventory, Component component, DataSyncHandler dataSyncHandler) {
        super(holder, inventory, component, dataSyncHandler);
        this.inventoryEntityX = 5;
        this.inventoryEntityY = 40;
    }

    @Override
    protected void init() {
        super.init();
        this.cycleButtons[0] = this.addRenderableWidget(new SpritelessImageButton(this.leftPos + 17,
                this.topPos + 153,
                20,
                20,
                156,
                64,
                getArmorStandWidgetsLocation(),
                (Button button) -> {
                    this.toggleCycleButtons(-this.poseButtons.length);
                }));
        this.cycleButtons[1] = this.addRenderableWidget(new SpritelessImageButton(this.leftPos + 49,
                this.topPos + 153,
                20,
                20,
                176,
                64,
                getArmorStandWidgetsLocation(),
                (Button button) -> {
                    this.toggleCycleButtons(this.poseButtons.length);
                }));
        for (int i = 0; i < this.poseButtons.length; i++) {
            final int index = i;
            this.poseButtons[i] = this.addRenderableWidget(new SpritelessImageButton(this.leftPos + 83 + i % 2 * 62,
                    this.topPos + 9 + i / 2 * 88,
                    60,
                    82,
                    76,
                    0,
                    82,
                    getArmorStandWidgetsLocation(),
                    256,
                    256,
                    (Button button) -> {
                        getPoseAt(index).ifPresent(this.dataSyncHandler::sendPose);
                    }));
            TooltipFactories.applyPosesTooltip(this.poseButtons[i], index);
        }
        this.toggleCycleButtons(0);
        this.addVanillaTweaksCreditsButton();
    }

    private void toggleCycleButtons(int increment) {
        int newFirstPoseIndex = firstPoseIndex + increment;
        if (newFirstPoseIndex >= 0 && newFirstPoseIndex < ArmorStandPoses.size()) {
            firstPoseIndex = newFirstPoseIndex;
            this.cycleButtons[0].active = newFirstPoseIndex - this.poseButtons.length >= 0;
            this.cycleButtons[1].active = newFirstPoseIndex + this.poseButtons.length < ArmorStandPoses.size();
            for (int i = 0; i < this.poseButtons.length; i++) {
                this.poseButtons[i].visible = getPoseAt(i).isPresent();
                this.poseButtons[i].setFocused(false);
            }
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (super.mouseScrolled(mouseX, mouseY, scrollX, scrollY)) {
            return true;
        } else if (mouseX >= this.leftPos && mouseX < this.leftPos + this.imageWidth && mouseY >= this.topPos
                && mouseY < this.topPos + this.imageHeight) {
            scrollY = Math.signum(scrollY);
            if (scrollY != 0.0) {
                this.toggleCycleButtons((int) (-1.0 * scrollY * this.poseButtons.length));
                return true;
            }
        }
        return false;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        ArmorStand armorStand = this.holder.getArmorStand();
        ArmorStandPose originalPose = ArmorStandPose.fromEntity(armorStand);
        for (int i = 0; i < this.poseButtons.length; i++) {
            Optional<ArmorStandPose> pose = getPoseAt(i);
            if (pose.isPresent()) {
                pose.get().applyToEntity(armorStand);
                int posX = this.leftPos + 89 + i % 2 * 62;
                int posY = this.topPos + 15 + i / 2 * 88;
                int lookX = posX + this.getInventoryEntityScissorWidth(true) / 2;
                int lookY = posY + this.getInventoryEntityScissorHeight(true) / 2;
                this.renderArmorStandInInventory(guiGraphics,
                        posX,
                        posY,
                        posX + this.getInventoryEntityScissorWidth(true),
                        posY + this.getInventoryEntityScissorHeight(true),
                        this.getInventoryEntityScale(true),
                        lookX + (this.poseButtons[i].isHovered() ? (this.mouseX - lookX) * 4 : 0),
                        lookY + (this.poseButtons[i].isHovered() ? (this.mouseY - lookY) * 4 : 0));
            }
        }
        originalPose.applyToEntity(armorStand);
    }

    @Override
    public void renderEntityInInventoryFollowsMouse(GuiGraphics guiGraphics, int x1, int y1, int x2, int y2, int scale, float yOffset, float mouseX, float mouseY, LivingEntity livingEntity) {
        ArmorStandPosesScreenHelper.renderEntityInInventoryFollowsMouse(guiGraphics,
                x1,
                y1,
                x2,
                y2,
                scale,
                yOffset,
                mouseX,
                mouseY,
                livingEntity);
    }

    @Override
    protected boolean withCloseButton() {
        return false;
    }

    @Override
    public ArmorStandScreenType getScreenType() {
        return ArmorStandScreenType.POSES;
    }

    public static Optional<ArmorStandPose> getPoseAt(int index) {
        return ArmorStandPoses.get(index + firstPoseIndex);
    }
}
