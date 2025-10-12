package fuzs.statuemenus.api.v1.client.gui.components;

import fuzs.statuemenus.api.v1.client.gui.screens.AbstractStatueScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.Tickable;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;

public class TickButton extends FlatButton implements Tickable {
    private final Component title;
    private final Component clickedTitle;
    private int lastClickedTicks;
    protected int lastClickedTicksDelay = 30;

    public TickButton(int x, int y, int width, int height, Component title, Component clickedTitle, OnPress onPress) {
        super(x, y, width, height, 0, 184, AbstractStatueScreen.getArmorStandWidgetsLocation(), title, onPress);
        this.title = title;
        this.clickedTitle = clickedTitle;
    }

    @Override
    public void onPress(InputWithModifiers inputWithModifiers) {
        super.onPress(inputWithModifiers);
        this.lastClickedTicks = this.lastClickedTicksDelay;
        this.setMessage(this.clickedTitle);
    }

    @Override
    public void tick() {
        if (this.lastClickedTicks > 0) this.lastClickedTicks--;
        if (this.lastClickedTicks == 0) {
            this.setMessage(this.title);
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, Minecraft minecraft, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, minecraft, mouseX, mouseY);
        if (this.lastClickedTicks > 0) {
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
        }
    }

    @Override
    protected int getMessageXOffset() {
        if (this.lastClickedTicks > 0) {
            return (16 + 4) / 2;
        }
        return super.getMessageXOffset();
    }
}
