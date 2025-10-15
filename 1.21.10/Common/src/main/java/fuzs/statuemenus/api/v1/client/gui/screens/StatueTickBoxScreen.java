package fuzs.statuemenus.api.v1.client.gui.screens;

import fuzs.statuemenus.api.v1.network.client.data.DataSyncHandler;
import fuzs.statuemenus.api.v1.world.inventory.StatueHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public abstract class StatueTickBoxScreen<T> extends AbstractStatueScreen {
    protected EditBox name;
    private int inputUpdateTicks;

    public StatueTickBoxScreen(StatueHolder holder, Inventory inventory, Component component, DataSyncHandler dataSyncHandler) {
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
            if (!Objects.equals(name, this.getNameValue())) {
                this.syncNameChange(name);
            }

            this.inputUpdateTicks = -1;
        }
    }

    protected abstract void syncNameChange(String input);

    @Override
    protected void init() {
        super.init();
        this.name = new EditBox(this.font,
                this.leftPos + 16,
                this.topPos + 32,
                73,
                9,
                this.getHolder().getEntity().getType().getDescription());
        this.name.setTextColor(-1);
        this.name.setBordered(false);
        this.name.setMaxLength(this.getNameMaxLength());
        this.name.setValue(this.getNameValue());
        Component nameHint = this.getNameHint();
        if (nameHint != null) {
            this.name.setHint(nameHint);
        }

        this.name.setResponder((String input) -> {
            this.inputUpdateTicks = 20;
        });
        Component nameTooltip = this.getNameTooltip();
        if (nameTooltip != null) {
            this.name.setTooltip(Tooltip.create(nameTooltip));
        }

        this.addWidget(this.name);
        this.inputUpdateTicks = -1;
        List<T> values = this.getAllTickBoxValues();
        final int buttonStartY = (this.imageHeight - values.size() * 20 - (values.size() - 1) * 2) / 2;
        for (int i = 0; i < values.size(); i++) {
            this.addRenderableWidget(this.makeTickBoxWidget(this.holder.getEntity(), buttonStartY, i, values.get(i)));
        }
    }

    protected abstract int getNameMaxLength();

    protected abstract String getNameValue();

    protected abstract @Nullable Component getNameHint();

    protected abstract @Nullable Component getNameTooltip();

    protected abstract List<T> getAllTickBoxValues();

    protected abstract AbstractWidget makeTickBoxWidget(LivingEntity livingEntity, int buttonStartY, int index, T option);

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
    public boolean keyPressed(KeyEvent keyEvent) {
        if (keyEvent.isEscape() && this.shouldCloseOnEsc()) {
            this.onClose();
            return true;
        } else {
            return this.name.keyPressed(keyEvent) || this.name.canConsumeInput() || super.keyPressed(keyEvent);
        }
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        // name edit box background
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                getArmorStandWidgetsLocation(),
                this.leftPos + 14,
                this.topPos + 30,
                0,
                108,
                76,
                12,
                256,
                256);
        this.name.render(guiGraphics, mouseX, mouseY, partialTick);
    }
}
