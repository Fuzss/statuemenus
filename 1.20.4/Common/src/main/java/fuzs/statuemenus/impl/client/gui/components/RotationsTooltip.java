package fuzs.statuemenus.impl.client.gui.components;

import fuzs.statuemenus.api.v1.client.gui.components.AbstractTooltip;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.MenuTooltipPositioner;

public abstract class RotationsTooltip extends AbstractTooltip {
    private final boolean isLeft;

    protected RotationsTooltip(boolean isLeft) {
        this.isLeft = isLeft;
    }

    @Override
    protected ClientTooltipPositioner createTooltipPositioner(boolean hovering, boolean focused, ScreenRectangle screenRectangle) {
        ClientTooltipPositioner tooltipPositioner = super.createTooltipPositioner(hovering, focused,
                screenRectangle
        );
        if (tooltipPositioner instanceof MenuTooltipPositioner) {
            return (screenWidth, screenHeight, mouseX, mouseY, tooltipWidth, tooltipHeight) -> {
                if (this.isLeft) mouseX -= 24 + tooltipWidth;
                return DefaultTooltipPositioner.INSTANCE.positionTooltip(screenWidth, screenHeight, mouseX, mouseY, tooltipWidth, tooltipHeight);
            };
        }
        return tooltipPositioner;
    }
}
