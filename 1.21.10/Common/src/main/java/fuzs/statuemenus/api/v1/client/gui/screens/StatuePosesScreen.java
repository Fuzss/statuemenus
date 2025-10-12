package fuzs.statuemenus.api.v1.client.gui.screens;

import fuzs.puzzleslib.api.client.gui.v2.components.SpritelessImageButton;
import fuzs.statuemenus.api.v1.network.client.data.DataSyncHandler;
import fuzs.statuemenus.api.v1.world.entity.decoration.StatueEntity;
import fuzs.statuemenus.api.v1.world.inventory.StatueHolder;
import fuzs.statuemenus.api.v1.world.inventory.data.StatuePose;
import fuzs.statuemenus.api.v1.world.inventory.data.StatueScreenType;
import fuzs.statuemenus.impl.client.gui.components.TooltipFactories;
import fuzs.statuemenus.impl.world.inventory.StatuePoses;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import java.util.Optional;

public class StatuePosesScreen extends AbstractStatueScreen {
    private static int firstPoseIndex;
    private final AbstractWidget[] cycleButtons = new AbstractWidget[2];
    private final AbstractWidget[] poseButtons = new AbstractWidget[4];

    public StatuePosesScreen(StatueHolder holder, Inventory inventory, Component component, DataSyncHandler dataSyncHandler) {
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
        if (newFirstPoseIndex >= 0 && newFirstPoseIndex < StatuePoses.size()) {
            firstPoseIndex = newFirstPoseIndex;
            this.cycleButtons[0].active = newFirstPoseIndex - this.poseButtons.length >= 0;
            this.cycleButtons[1].active = newFirstPoseIndex + this.poseButtons.length < StatuePoses.size();
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
        StatueEntity statueEntity = this.holder.getStatueEntity();
        StatuePose originalPose = StatuePose.fromEntity(statueEntity);
        for (int i = 0; i < this.poseButtons.length; i++) {
            Optional<StatuePose> pose = getPoseAt(i);
            if (pose.isPresent()) {
                pose.get().applyToEntity(statueEntity);
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
                        lookY + (this.poseButtons[i].isHovered() ? (this.mouseY - lookY) * 4 : 0),
                        partialTick);
            }
        }

        originalPose.applyToEntity(statueEntity);
    }

    @Override
    protected boolean withCloseButton() {
        return false;
    }

    @Override
    public StatueScreenType getScreenType() {
        return StatueScreenType.POSES;
    }

    public static Optional<StatuePose> getPoseAt(int index) {
        return StatuePoses.get(index + firstPoseIndex);
    }
}
