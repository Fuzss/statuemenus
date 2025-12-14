package fuzs.statuemenus.api.v1.client.gui.components;

import fuzs.statuemenus.api.v1.client.gui.screens.AbstractStatueScreen;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;

public class TickButton extends FlatButton {
    private final Component title;
    private final Component clickedTitle;
    private long lastClickedTime;
    protected int lastClickedTicksDelay = 30;

    public TickButton(int x, int y, int width, int height, Component title, Component clickedTitle, OnPress onPress) {
        super(x, y, width, height, 0, 184, AbstractStatueScreen.getArmorStandWidgetsLocation(), title, onPress);
        this.title = title;
        this.clickedTitle = clickedTitle;
    }

    @Override
    public void onPress(InputWithModifiers inputWithModifiers) {
        super.onPress(inputWithModifiers);
        this.lastClickedTime = Util.getMillis();
        this.setMessage(this.clickedTitle);
    }

    protected boolean wasClicked() {
        return Util.getMillis() - this.lastClickedTime < this.lastClickedTicksDelay * 50L;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, Minecraft minecraft, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, minecraft, mouseX, mouseY);
        if (this.wasClicked()) {
            this.setMessage(this.clickedTitle);
            final int i = this.getYImage();
            int titleWidth = minecraft.font.width(this.clickedTitle);
            final int startX = (this.width - titleWidth - (titleWidth > 0 ? 4 : 0) - 16) / 2;
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                    this.textureLocation,
                    this.getX() + startX,
                    this.getY() + 2,
                    196,
                    16 + i * 16,
                    16,
                    16,
                    256,
                    256,
                    ARGB.white(this.alpha));
        } else {
            this.setMessage(this.title);
        }
    }

    @Override
    protected int getMessageXOffset() {
        if (this.wasClicked()) {
            return (16 + 4) / 2;
        } else {
            return super.getMessageXOffset();
        }
    }
}
