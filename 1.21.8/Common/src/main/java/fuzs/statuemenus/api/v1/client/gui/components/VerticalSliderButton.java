package fuzs.statuemenus.api.v1.client.gui.components;

import com.mojang.blaze3d.platform.InputConstants;
import fuzs.statuemenus.api.v1.client.gui.screens.AbstractArmorStandScreen;
import fuzs.statuemenus.impl.world.inventory.ArmorStandPoses;
import net.minecraft.client.InputType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.CommonInputs;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;

import java.util.function.DoubleSupplier;

public abstract class VerticalSliderButton extends AbstractWidget implements UnboundedSliderButton, LiveSliderButton {
    private final int sliderSize = 13;
    private final DoubleSupplier currentValue;
    protected double value;
    private boolean canChangeValue;

    public VerticalSliderButton(int x, int y, DoubleSupplier currentValue) {
        super(x, y, 15, 54, CommonComponents.EMPTY);
        this.currentValue = currentValue;
        this.refreshValues();
    }

    @Override
    public void refreshValues() {
        this.value = this.currentValue.getAsDouble();
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        if (this.active) {
            if (this.isFocused()) {
                narrationElementOutput.add(NarratedElementType.USAGE,
                        Component.translatable("narration.slider.usage.focused"));
            } else {
                narrationElementOutput.add(NarratedElementType.USAGE,
                        Component.translatable("narration.slider.usage.hovered"));
            }
        }
    }

    protected int getYImage() {
        if (!this.active) {
            return 0;
        } else if (this.isHovered || this.canChangeValue) {
            return 2;
        }
        return 1;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                AbstractArmorStandScreen.getArmorStandWidgetsLocation(),
                this.getX(),
                this.getY(),
                54,
                120,
                this.width,
                this.height,
                256,
                256,
                ARGB.white(this.alpha));
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                AbstractArmorStandScreen.getArmorStandWidgetsLocation(),
                this.getX() + 1,
                this.getY() + 1 + (int) (this.value * (double) (this.height - this.sliderSize - 2)),
                151,
                this.getYImage() * this.sliderSize,
                this.sliderSize,
                this.sliderSize,
                256,
                256,
                ARGB.white(this.alpha));
        ;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.setValueFromMouse(mouseY);
    }

    @Override
    public void setFocused(boolean focused) {
        super.setFocused(focused);
        if (!focused) {
            this.canChangeValue = false;
        } else {
            InputType inputType = Minecraft.getInstance().getLastInputType();
            if (inputType == InputType.MOUSE || inputType == InputType.KEYBOARD_TAB) {
                this.canChangeValue = true;
            }

        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (CommonInputs.selected(keyCode)) {
            this.canChangeValue = !this.canChangeValue;
            return true;
        } else {
            if (this.canChangeValue) {
                return this.keyPressed(keyCode);
            } else {
                return false;
            }
        }
    }

    private boolean keyPressed(int keyCode) {
        if (this.active && this.visible) {
            switch (keyCode) {
                case InputConstants.KEY_UP -> {
                    this.setValue(this.value - BoxedSliderButton.VALUE_KEY_INTERVAL, false);
                    return true;
                }
                case InputConstants.KEY_DOWN -> {
                    this.setValue(this.value + BoxedSliderButton.VALUE_KEY_INTERVAL, false);
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double dragX, double dragY) {
        this.setValueFromMouse(mouseY);
    }

    private void setValueFromMouse(double mouseY) {
        this.setValue((mouseY - (double) (this.getY() + 8)) / (double) (this.height - this.sliderSize - 2));
    }

    private void setValue(double value) {
        this.setValue(value, true);
    }

    private void setValue(double value, boolean snapValue) {
        double oldValue = this.value;
        this.value = Mth.clamp(value, 0.0, 1.0);
        if (snapValue) {
            this.value = ArmorStandPoses.snapValue(this.value, ArmorStandPoses.DEGREES_SNAP_INTERVAL);
        }
        if (oldValue != this.value) {
            this.applyValue();
        }
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
        // NO-OP
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        super.playDownSound(Minecraft.getInstance().getSoundManager());
    }

    protected abstract void applyValue();
}
