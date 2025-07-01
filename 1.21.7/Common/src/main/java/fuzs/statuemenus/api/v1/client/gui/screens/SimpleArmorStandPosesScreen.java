package fuzs.statuemenus.api.v1.client.gui.screens;

import fuzs.puzzleslib.api.client.gui.v2.components.SpritelessImageButton;
import fuzs.puzzleslib.api.client.gui.v2.tooltip.TooltipBuilder;
import fuzs.statuemenus.api.v1.client.gui.components.NewTextureTickButton;
import fuzs.statuemenus.api.v1.network.client.data.DataSyncHandler;
import fuzs.statuemenus.api.v1.world.inventory.ArmorStandHolder;
import fuzs.statuemenus.api.v1.world.inventory.data.ArmorStandPose;
import fuzs.statuemenus.api.v1.world.inventory.data.ArmorStandScreenType;
import fuzs.statuemenus.impl.world.inventory.ArmorStandPoseImpl;
import fuzs.statuemenus.impl.world.inventory.ArmorStandPoses;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@Deprecated
public class SimpleArmorStandPosesScreen extends AbstractArmorStandScreen {
    private static int poseIndex;
    private final AbstractWidget[] cycleButtons = new AbstractWidget[2];
    private AbstractWidget applyPoseButton;

    public SimpleArmorStandPosesScreen(ArmorStandHolder holder, Inventory inventory, Component component, DataSyncHandler dataSyncHandler) {
        super(holder, inventory, component, dataSyncHandler);
        this.inventoryEntityX = (this.imageWidth - this.getInventoryEntityBackgroundWidth()) / 2;
        this.inventoryEntityY = (this.imageHeight - this.getInventoryEntityBackgroundHeight() - 8 - 20) / 2 + 8;
    }

    @Override
    protected void init() {
        super.init();
        int widgetY = this.topPos + this.inventoryEntityY + this.getInventoryEntityBackgroundHeight() + 8;
        this.cycleButtons[0] = this.addRenderableWidget(new SpritelessImageButton(
                this.leftPos + this.imageWidth / 2 - 35,
                widgetY,
                20,
                20,
                156,
                64,
                getArmorStandWidgetsLocation(),
                (Button button) -> {
                    this.toggleCycleButtons(-1);
                }));
        this.cycleButtons[1] = this.addRenderableWidget(new SpritelessImageButton(
                this.leftPos + this.imageWidth / 2 + 15,
                widgetY,
                20,
                20,
                176,
                64,
                getArmorStandWidgetsLocation(),
                (Button button) -> {
                    this.toggleCycleButtons(1);
                }));
        this.applyPoseButton = this.addRenderableWidget(new NewTextureTickButton(
                this.leftPos + this.imageWidth / 2 - 10,
                widgetY,
                20,
                20,
                176,
                124,
                getArmorStandWidgetsLocation(),
                (Button button) -> {
                    ArmorStandPoses.get(poseIndex).ifPresent(this.dataSyncHandler::sendPose);
                    this.toggleCycleButtons(0);
                }));
        this.toggleCycleButtons(0);
        this.addVanillaTweaksCreditsButton();
    }

    private void toggleCycleButtons(int increment) {
        int newPoseIndex = poseIndex + increment;
        if (newPoseIndex >= 0 && newPoseIndex < ArmorStandPoses.size()) {
            poseIndex = newPoseIndex;
            this.cycleButtons[0].active = newPoseIndex > 0;
            this.cycleButtons[1].active = newPoseIndex < ArmorStandPoses.size() - 1;
            this.applyPoseButton.active = !Objects.equals(ArmorStandPoseImpl.fromEntity(this.holder.getArmorStand()),
                    ArmorStandPoses.get(poseIndex).orElse(null));
            ArmorStandPoses.get(poseIndex).ifPresent((ArmorStandPose armorStandPose) -> {
                TooltipBuilder.create(armorStandPose.getTooltipLines()).build(this.applyPoseButton);
            });
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (super.mouseScrolled(mouseX, mouseY, scrollX, scrollY)) {
            return true;
        } else if (mouseX >= this.leftPos && mouseX < this.leftPos + this.imageWidth && mouseY >= this.topPos
                && mouseY < this.topPos + this.imageHeight) {
            scrollY = -Math.signum(scrollY);
            if (scrollY != 0.0) {
                this.toggleCycleButtons((int) scrollY);
                return true;
            }
        }
        return false;
    }

    @Override
    protected @Nullable ArmorStandPose getPoseOverride() {
        return ArmorStandPoses.get(poseIndex).orElse(null);
    }

    @Override
    public ArmorStandScreenType getScreenType() {
        return ArmorStandScreenType.POSES;
    }
}
