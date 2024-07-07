package fuzs.statuemenus.impl.client.gui.components;

import fuzs.puzzleslib.api.client.gui.v2.components.tooltip.TooltipComponentImpl;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.MenuTooltipPositioner;
import net.minecraft.network.chat.FormattedText;

import java.util.List;

public abstract class RotationsTooltip extends TooltipComponentImpl {
    private final boolean isLeft;

    public RotationsTooltip(AbstractWidget abstractWidget, boolean isLeft) {
        super(abstractWidget);
        this.isLeft = isLeft;
    }

    @Override
    public ClientTooltipPositioner createTooltipPositioner(AbstractWidget abstractWidget) {
        ClientTooltipPositioner tooltipPositioner = super.createTooltipPositioner(abstractWidget);
        if (tooltipPositioner instanceof MenuTooltipPositioner) {
            return (screenWidth, screenHeight, mouseX, mouseY, tooltipWidth, tooltipHeight) -> {
                if (this.isLeft) mouseX -= 24 + tooltipWidth;
                return DefaultTooltipPositioner.INSTANCE.positionTooltip(screenWidth,
                        screenHeight,
                        mouseX,
                        mouseY,
                        tooltipWidth,
                        tooltipHeight
                );
            };
        } else {
            return tooltipPositioner;
        }
    }

    @Override
    public abstract List<? extends FormattedText> getLinesForNextRenderPass();
}
