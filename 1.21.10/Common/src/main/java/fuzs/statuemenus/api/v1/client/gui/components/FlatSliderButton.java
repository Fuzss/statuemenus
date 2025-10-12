package fuzs.statuemenus.api.v1.client.gui.components;

import fuzs.puzzleslib.api.client.gui.v2.GuiGraphicsHelper;
import fuzs.statuemenus.impl.world.inventory.StatuePoses;
import net.minecraft.client.InputType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;

public abstract class FlatSliderButton extends AbstractSliderButton implements UnboundedSliderButton {
    private final int textureX;
    private final int textureY;
    protected final ResourceLocation textureLocation;
    private boolean canChangeValue;
    public double snapInterval = -1.0;

    public FlatSliderButton(int x, int y, int width, int height, int textureX, int textureY, ResourceLocation textureLocation, Component component, double value) {
        super(x, y, width, height, component, value);
        this.textureX = textureX;
        this.textureY = textureY;
        this.textureLocation = textureLocation;
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
        Minecraft minecraft = Minecraft.getInstance();
        GuiGraphicsHelper.blitNineSliced(guiGraphics,
                RenderPipelines.GUI_TEXTURED,
                this.textureLocation,
                this.getX(),
                this.getY(),
                this.getWidth(),
                this.getHeight(),
                20,
                4,
                20,
                4,
                200,
                20,
                this.textureX,
                this.textureY,
                256,
                256,
                ARGB.white(this.alpha));
        GuiGraphicsHelper.blitNineSliced(guiGraphics,
                RenderPipelines.GUI_TEXTURED,
                this.textureLocation,
                this.getX() + (int) (this.value * (double) (this.width - 8)),
                this.getY(),
                8,
                20,
                20,
                4,
                20,
                4,
                200,
                20,
                this.textureX,
                this.textureY + (this.getYImage() == 2 ? 40 : 20),
                256,
                256,
                ARGB.white(this.alpha));
        int textColor = this.active ? -1 : 0XA0A0A0;
        this.renderScrollingString(guiGraphics, minecraft.font, 2, ARGB.color(this.alpha, textColor));
    }

    @Override
    public void onClick(MouseButtonEvent mouseButtonEvent, boolean doubleClick) {
        this.setValueFromMouse(mouseButtonEvent.x());
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
        boolean isLeft = keyEvent.isLeft();
        boolean isRight = keyEvent.isRight();
        if (isLeft || isRight) {
            float valueDirection = isLeft ? -1.0F : 1.0F;
            this.setValue(this.value + (double) (valueDirection / (float) (this.width - 8)), true);
        }

        return false;
    }

    private void setValueFromMouse(double mouseX) {
        this.setValue((mouseX - (double) (this.getX() + 4)) / (double) (this.width - 8), true);
    }

    private void setValue(double value, boolean snapValue) {
        double oldValue = this.value;
        this.value = Mth.clamp(value, 0.0, 1.0);
        if (snapValue) {
            this.value = StatuePoses.snapValue(this.value, this.snapInterval);
        }
        if (oldValue != this.value) {
            this.applyValue();
        }
        this.updateMessage();
    }

    @Override
    protected void onDrag(MouseButtonEvent mouseButtonEvent, double dragX, double dragY) {
        this.setValueFromMouse(mouseButtonEvent.x());
    }
}
