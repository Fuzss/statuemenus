package fuzs.statuemenus.api.v1.client.gui.components;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;

public class FlatButton extends Button {
    private final int textureX;
    private final int textureY;
    protected final ResourceLocation textureLocation;

    public FlatButton(int x, int y, int width, int height, int textureX, int textureY, ResourceLocation textureLocation, Component component, OnPress onPress) {
        super(x, y, width, height, component, onPress, Button.DEFAULT_NARRATION);
        this.textureX = textureX;
        this.textureY = textureY;
        this.textureLocation = textureLocation;
    }

    protected int getYImage() {
        return !this.active || this.isHoveredOrFocused() ? 2 : 1;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        int yImage = this.getYImage();
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                this.textureLocation,
                this.getX(),
                this.getY(),
                this.textureX,
                this.textureY + yImage * 20,
                this.width / 2,
                this.height,
                256,
                256,
                ARGB.white(this.alpha));
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                this.textureLocation,
                this.getX() + this.width / 2,
                this.getY(),
                this.textureX + 200 - this.width / 2,
                this.textureY + yImage * 20,
                this.width / 2,
                this.height,
                256,
                256,
                ARGB.white(this.alpha));
        this.renderBg(guiGraphics, minecraft, mouseX, mouseY);
        int textColor = this.active && this.isHoveredOrFocused() ? ChatFormatting.YELLOW.getColor() : 0x404040;
        drawCenteredStringWithShadow(guiGraphics,
                minecraft.font,
                this.getMessage(),
                this.getX() + this.width / 2 + this.getMessageXOffset(),
                this.getY() + (this.height - 8) / 2,
                ARGB.color(this.alpha, textColor),
                false);
    }

    protected void renderBg(GuiGraphics guiGraphics, Minecraft minecraft, int mouseX, int mouseY) {
        // NO-OP
    }

    protected int getMessageXOffset() {
        return 0;
    }

    /**
     * @see GuiGraphics#drawCenteredString(Font, Component, int, int, int)
     */
    public static void drawCenteredStringWithShadow(GuiGraphics guiGraphics, Font font, Component component, int x, int y, int color, boolean dropShadow) {
        guiGraphics.drawString(font, component, x - font.width(component) / 2, y, color, dropShadow);
    }
}
