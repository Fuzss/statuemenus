package fuzs.statuemenus.api.v1.client.gui.screens;

import fuzs.puzzleslib.api.client.gui.v2.components.SpritelessImageButton;
import fuzs.statuemenus.api.v1.network.client.data.DataSyncHandler;
import fuzs.statuemenus.api.v1.world.inventory.ArmorStandHolder;
import fuzs.statuemenus.api.v1.world.inventory.data.ArmorStandPose;
import fuzs.statuemenus.api.v1.world.inventory.data.ArmorStandScreenType;
import fuzs.statuemenus.impl.StatueMenus;
import fuzs.statuemenus.impl.client.gui.components.PosesTooltip;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Inventory;

import java.util.Optional;

public class ArmorStandPosesScreen extends AbstractArmorStandScreen {
    public static final String POSE_SOURCE_TRANSLATION_KEY = StatueMenus.MOD_ID + ".screen.pose.by";
    private static final int POSES_PER_PAGE = 4;

    private static int firstPoseIndex;

    private final AbstractWidget[] cycleButtons = new AbstractWidget[2];
    private final AbstractWidget[] poseButtons = new AbstractWidget[POSES_PER_PAGE];

    public ArmorStandPosesScreen(ArmorStandHolder holder, Inventory inventory, Component component, DataSyncHandler dataSyncHandler) {
        super(holder, inventory, component, dataSyncHandler);
        this.inventoryEntityX = 5;
        this.inventoryEntityY = 40;
    }

    @Override
    protected void init() {
        super.init();
        this.cycleButtons[0] = this.addRenderableWidget(new SpritelessImageButton(this.leftPos + 17, this.topPos + 153, 20, 20, 156, 64, getArmorStandWidgetsLocation(), button -> {
            this.toggleCycleButtons(-POSES_PER_PAGE);
        }));
        this.cycleButtons[1] = this.addRenderableWidget(new SpritelessImageButton(this.leftPos + 49, this.topPos + 153, 20, 20, 176, 64, getArmorStandWidgetsLocation(), button -> {
            this.toggleCycleButtons(POSES_PER_PAGE);
        }));
        for (int i = 0; i < this.poseButtons.length; i++) {
            final int index = i;
            this.poseButtons[i] = this.addRenderableWidget(new SpritelessImageButton(this.leftPos + 83 + i % 2 * 62, this.topPos + 9 + i / 2 * 88, 60, 82, 76, 0, 82, getArmorStandWidgetsLocation(), 256, 256, button -> {
                getPoseAt(index).ifPresent(this.dataSyncHandler::sendPose);
            }));
            new PosesTooltip(this.poseButtons[i], index);
        }
        this.toggleCycleButtons(0);
        this.addVanillaTweaksCreditsButton();
    }

    private void toggleCycleButtons(int increment) {
        int newFirstPoseIndex = firstPoseIndex + increment;
        if (newFirstPoseIndex >= 0 && newFirstPoseIndex < ArmorStandPose.valuesLength()) {
            firstPoseIndex = newFirstPoseIndex;
            this.cycleButtons[0].active = newFirstPoseIndex - POSES_PER_PAGE >= 0;
            this.cycleButtons[1].active = newFirstPoseIndex + POSES_PER_PAGE < ArmorStandPose.valuesLength();
            for (int i = 0; i < this.poseButtons.length; i++) {
                this.poseButtons[i].visible = getPoseAt(i).isPresent();
            }
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (super.mouseScrolled(mouseX, mouseY, scrollX, scrollY)) {
            return true;
        } else if (mouseX >= this.leftPos && mouseX < this.leftPos + this.imageWidth && mouseY >= this.topPos && mouseY < this.topPos + this.imageHeight) {
            scrollY = Math.signum(scrollY);
            if (scrollY != 0.0) {
                this.toggleCycleButtons((int) (-1.0 * scrollY * POSES_PER_PAGE));
                return true;
            }
        }
        return false;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, partialTick, mouseX, mouseY);
        ArmorStand armorStand = this.holder.getArmorStand();
        ArmorStandPose entityPose = ArmorStandPose.fromEntity(armorStand);
        for (int i = 0; i < POSES_PER_PAGE; i++) {
            Optional<ArmorStandPose> pose = getPoseAt(i);
            if (pose.isPresent()) {
                pose.get().applyToEntity(armorStand);
                this.renderArmorStandInInventory(guiGraphics, this.leftPos + 112 + i % 2 * 62, this.topPos + 79 + i / 2 * 88, 30, this.leftPos + 112 + i % 2 * 62 - 10 - this.mouseX, this.topPos + 79 + i / 2 * 88 - 44 - this.mouseY);
            }
        }
        entityPose.applyToEntity(armorStand);
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
        index += firstPoseIndex;
        if (index >= ArmorStandPose.valuesLength()) return Optional.empty();
        return Optional.of(ArmorStandPose.values()[index]);
    }
}
