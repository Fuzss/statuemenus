package fuzs.statuemenus.api.v1.client.gui.screens;

import com.mojang.blaze3d.platform.InputConstants;
import fuzs.statuemenus.api.v1.network.client.data.DataSyncHandler;
import fuzs.statuemenus.api.v1.world.inventory.ArmorStandHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Inventory;

public abstract class ArmorStandTickBoxScreen<T> extends AbstractArmorStandScreen {
    protected EditBox name;
    private int inputUpdateTicks;

    public ArmorStandTickBoxScreen(ArmorStandHolder holder, Inventory inventory, Component component, DataSyncHandler dataSyncHandler) {
        super(holder, inventory, component, dataSyncHandler);
        this.inventoryEntityX = 14;
        this.inventoryEntityY = 50;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.inputUpdateTicks > 0) {
            this.inputUpdateTicks--;
        }
        this.testNameInputChanged(true);
    }

    private void testNameInputChanged(boolean testEquality) {
        if (this.inputUpdateTicks == 0 || !testEquality && this.inputUpdateTicks != -1) {
            String name = this.name.getValue().trim();
            if (!name.equals(this.getNameDefaultValue())) {
                this.syncNameChange(name);
            }
            this.inputUpdateTicks = -1;
        }
    }

    protected abstract void syncNameChange(String input);

    @Override
    protected void init() {
        super.init();
        this.name = new EditBox(this.font, this.leftPos + 16, this.topPos + 32, 73, 9, EntityType.ARMOR_STAND.getDescription());
        this.name.setTextColor(0xFFFFFF);
        this.name.setBordered(false);
        this.name.setMaxLength(this.getNameMaxLength());
        this.name.setValue(this.getNameDefaultValue());
        this.name.setResponder(input -> this.inputUpdateTicks = 20);
        this.name.setTooltip(Tooltip.create(this.getNameComponent()));
        this.addWidget(this.name);
        this.inputUpdateTicks = -1;
        T[] values = this.getAllTickBoxValues();
        final int buttonStartY = (this.imageHeight - values.length * 20 - (values.length - 1) * 2) / 2;
        for (int i = 0; i < values.length; i++) {
            this.addRenderableWidget(this.makeTickBoxWidget(this.holder.getArmorStand(), buttonStartY, i, values[i]));
        }
    }

    protected abstract int getNameMaxLength();

    protected abstract String getNameDefaultValue();

    protected abstract T[] getAllTickBoxValues();

    protected abstract AbstractWidget makeTickBoxWidget(ArmorStand armorStand, int buttonStartY, int index, T option);

    protected abstract Component getNameComponent();

    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        this.testNameInputChanged(false);
        String s = this.name.getValue();
        this.init(minecraft, width, height);
        this.name.setValue(s);
    }

    @Override
    public void removed() {
        this.testNameInputChanged(false);
        super.removed();
    }

    @Override
    public void onClose() {
        this.testNameInputChanged(false);
        super.onClose();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == InputConstants.KEY_ESCAPE && this.shouldCloseOnEsc()) {
            this.onClose();
            return true;
        }
        return this.name.keyPressed(keyCode, scanCode, modifiers) || this.name.canConsumeInput() || super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, partialTick, mouseX, mouseY);
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        // name edit box background
        guiGraphics.blit(getArmorStandWidgetsLocation(), this.leftPos + 14, this.topPos + 30, 0, 108, 76, 12);
        this.name.render(guiGraphics, mouseX, mouseY, partialTick);
    }
}
