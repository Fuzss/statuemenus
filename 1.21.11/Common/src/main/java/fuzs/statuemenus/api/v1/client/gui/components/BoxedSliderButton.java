package fuzs.statuemenus.api.v1.client.gui.components;

import fuzs.puzzleslib.api.util.v1.CommonHelper;
import fuzs.statuemenus.api.v1.client.gui.screens.AbstractStatueScreen;
import fuzs.statuemenus.impl.world.inventory.StatuePoses;
import net.minecraft.client.InputType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;

import java.util.function.DoubleSupplier;

public abstract class BoxedSliderButton extends AbstractWidget implements UnboundedSliderButton, LiveSliderButton {
    static final double VALUE_KEY_INTERVAL = 0.035;
    private static final int SLIDER_SIZE = 13;

    private final DoubleSupplier currentHorizontalValue;
    private final DoubleSupplier currentVerticalValue;
    protected double horizontalValue;
    protected double verticalValue;
    private boolean canChangeValue;

    public BoxedSliderButton(int x, int y, DoubleSupplier currentHorizontalValue, DoubleSupplier currentVerticalValue) {
        super(x, y, 54, 54, CommonComponents.EMPTY);
        this.currentHorizontalValue = currentHorizontalValue;
        this.currentVerticalValue = currentVerticalValue;
        this.refreshValues();
    }

    @Override
    public void refreshValues() {
        this.horizontalValue = this.currentHorizontalValue.getAsDouble();
        this.verticalValue = this.currentVerticalValue.getAsDouble();
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
        } else {
            return 1;
        }
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        final int sliderX = (int) (this.horizontalValue * (double) (this.width - SLIDER_SIZE - 2));
        final int sliderY = (int) (this.verticalValue * (double) (this.height - SLIDER_SIZE - 2));
        if (!this.active || !this.isHoveredOrFocused()
                || !this.horizontalValueLocked() && !this.verticalValueLocked()) {
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                    AbstractStatueScreen.getArmorStandWidgetsLocation(),
                    this.getX(),
                    this.getY(),
                    0,
                    120,
                    this.width,
                    this.height,
                    256,
                    256,
                    ARGB.white(this.alpha));
        } else if (this.horizontalValueLocked() && this.verticalValueLocked()) {
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                    AbstractStatueScreen.getArmorStandWidgetsLocation(),
                    this.getX() + sliderX,
                    this.getY() + sliderY,
                    164,
                    0,
                    SLIDER_SIZE + 2,
                    SLIDER_SIZE + 2,
                    256,
                    256,
                    ARGB.white(this.alpha));
        } else if (this.horizontalValueLocked()) {
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                    AbstractStatueScreen.getArmorStandWidgetsLocation(),
                    this.getX() + sliderX,
                    this.getY(),
                    54,
                    120,
                    SLIDER_SIZE + 2,
                    this.height,
                    256,
                    256,
                    ARGB.white(this.alpha));
        } else {
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                    AbstractStatueScreen.getArmorStandWidgetsLocation(),
                    this.getX(),
                    this.getY() + sliderY,
                    136,
                    49,
                    this.width,
                    SLIDER_SIZE + 2,
                    256,
                    256,
                    ARGB.white(this.alpha));
        }
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                AbstractStatueScreen.getArmorStandWidgetsLocation(),
                this.getX() + 1 + sliderX,
                this.getY() + 1 + sliderY,
                151,
                this.getYImage() * SLIDER_SIZE,
                SLIDER_SIZE,
                SLIDER_SIZE,
                256,
                256,
                ARGB.white(this.alpha));
    }

    @Override
    public void onClick(MouseButtonEvent mouseButtonEvent, boolean doubleClick) {
        this.setValueFromMouse(mouseButtonEvent);
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
    public boolean keyPressed(KeyEvent keyEvent) {
        if (keyEvent.isSelection()) {
            this.canChangeValue = !this.canChangeValue;
            return true;
        } else {
            if (this.canChangeValue) {
                return this.onKeyPressed(keyEvent);
            } else {
                return false;
            }
        }
    }

    private boolean onKeyPressed(KeyEvent keyEvent) {
        if (this.active && this.visible) {
            if (keyEvent.isLeft()) {
                this.setHorizontalValue(this.horizontalValue - VALUE_KEY_INTERVAL, false);
                return true;
            } else if (keyEvent.isRight()) {
                this.setHorizontalValue(this.horizontalValue + VALUE_KEY_INTERVAL, false);
                return true;
            } else if (keyEvent.isUp()) {
                this.setVerticalValue(this.verticalValue - VALUE_KEY_INTERVAL, false);
                return true;
            } else if (keyEvent.isDown()) {
                this.setVerticalValue(this.verticalValue + VALUE_KEY_INTERVAL, false);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    protected void onDrag(MouseButtonEvent mouseButtonEvent, double dragX, double dragY) {
        this.setValueFromMouse(mouseButtonEvent);
    }

    private void setValueFromMouse(MouseButtonEvent mouseButtonEvent) {
        this.setHorizontalValue(
                (mouseButtonEvent.x() - (double) (this.getX() + 8)) / (double) (this.width - SLIDER_SIZE - 2), true);
        this.setVerticalValue(
                (mouseButtonEvent.y() - (double) (this.getY() + 8)) / (double) (this.height - SLIDER_SIZE - 2), true);
    }

    private void setHorizontalValue(double horizontalValue, boolean snapValue) {
        double oldHorizontalValue = this.horizontalValue;
        if (!this.horizontalValueLocked()) {
            this.horizontalValue = Mth.clamp(horizontalValue, 0.0, 1.0);
            if (snapValue) {
                this.horizontalValue = StatuePoses.snapValue(this.horizontalValue,
                        StatuePoses.DEGREES_SNAP_INTERVAL);
            }
        }
        if (oldHorizontalValue != this.horizontalValue) {
            this.applyValue();
        }
    }

    private void setVerticalValue(double verticalValue, boolean snapValue) {
        double oldVerticalValue = this.verticalValue;
        if (!this.verticalValueLocked()) {
            this.verticalValue = Mth.clamp(verticalValue, 0.0, 1.0);
            if (snapValue) {
                this.verticalValue = StatuePoses.snapValue(this.verticalValue,
                        StatuePoses.DEGREES_SNAP_INTERVAL);
            }
        }
        if (oldVerticalValue != this.verticalValue) {
            this.applyValue();
        }
    }

    protected boolean verticalValueLocked() {
        return CommonHelper.hasShiftDown();
    }

    protected boolean horizontalValueLocked() {
        return CommonHelper.hasAltDown();
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
        // NO-OP
    }

    @Override
    public void onRelease(MouseButtonEvent mouseButtonEvent) {
        super.playDownSound(Minecraft.getInstance().getSoundManager());
    }

    protected abstract void applyValue();
}
