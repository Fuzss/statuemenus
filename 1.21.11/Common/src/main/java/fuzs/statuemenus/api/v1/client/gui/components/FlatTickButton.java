package fuzs.statuemenus.api.v1.client.gui.components;

import fuzs.statuemenus.api.v1.client.gui.screens.AbstractStatueScreen;
import net.minecraft.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;

public class FlatTickButton extends FlatButton {
    private final int imageTextureX;
    private final int imageTextureY;
    private final Identifier imageTextureLocation;
    private long lastClickedTime;
    protected int lastClickedTicksDelay = 20;

    public FlatTickButton(int x, int y, int width, int height, int imageTextureX, int imageTextureY, Identifier imageTextureLocation, OnPress onPress) {
        super(x,
                y,
                width,
                height,
                0,
                184,
                AbstractStatueScreen.getArmorStandWidgetsLocation(),
                CommonComponents.EMPTY,
                onPress);
        this.imageTextureX = imageTextureX;
        this.imageTextureY = imageTextureY;
        this.imageTextureLocation = imageTextureLocation;
    }

    @Override
    protected int getYImage() {
        if (!this.active) {
            return 0;
        } else if (this.isHoveredOrFocused()) {
            return 2;
        } else {
            return 1;
        }
    }

    @Override
    public void onPress(InputWithModifiers inputWithModifiers) {
        super.onPress(inputWithModifiers);
        this.lastClickedTime = Util.getMillis();
    }

    protected boolean wasClicked() {
        return Util.getMillis() - this.lastClickedTime < this.lastClickedTicksDelay * 50L;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, Minecraft minecraft, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, minecraft, mouseX, mouseY);
        Identifier identifier = this.wasClicked() ? this.textureLocation : this.imageTextureLocation;
        final int i = this.getYImage();
        if (this.wasClicked()) {
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                    identifier,
                    this.getX() + this.width / 2 - 8,
                    this.getY() + this.height / 2 - 8,
                    176,
                    124 + i * 16,
                    16,
                    16,
                    256,
                    256,
                    ARGB.white(this.alpha));
        } else {
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                    identifier,
                    this.getX() + this.width / 2 - 8,
                    this.getY() + this.height / 2 - 8,
                    this.imageTextureX,
                    this.imageTextureY + i * 16,
                    16,
                    16,
                    256,
                    256,
                    ARGB.white(this.alpha));
        }
    }
}
