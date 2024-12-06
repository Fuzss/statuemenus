package fuzs.statuemenus.api.v1.client.gui.components;

import fuzs.statuemenus.api.v1.client.gui.screens.AbstractArmorStandScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.Tickable;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;

public class NewTextureTickButton extends NewTextureButton implements Tickable {
    private final int imageTextureX;
    private final int imageTextureY;
    private final ResourceLocation imageTextureLocation;
    private int lastClickedTicks;
    protected int lastClickedTicksDelay = 20;

    public NewTextureTickButton(int x, int y, int width, int height, int imageTextureX, int imageTextureY, ResourceLocation imageTextureLocation, OnPress onPress) {
        super(x,
                y,
                width,
                height,
                0,
                184,
                AbstractArmorStandScreen.getArmorStandWidgetsLocation(),
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
        }
        return 1;
    }

    @Override
    public void onPress() {
        super.onPress();
        this.lastClickedTicks = this.lastClickedTicksDelay;
    }

    @Override
    public void tick() {
        if (this.lastClickedTicks > 0) this.lastClickedTicks--;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, Minecraft minecraft, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, minecraft, mouseX, mouseY);
        ResourceLocation resourceLocation =
                this.lastClickedTicks > 0 ? this.textureLocation : this.imageTextureLocation;
        final int i = this.getYImage();
        if (this.lastClickedTicks > 0) {
            guiGraphics.blit(RenderType::guiTextured,
                    resourceLocation,
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
            guiGraphics.blit(RenderType::guiTextured,
                    resourceLocation,
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
