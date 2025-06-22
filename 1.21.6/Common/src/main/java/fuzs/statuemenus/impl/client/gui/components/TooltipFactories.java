package fuzs.statuemenus.impl.client.gui.components;

import fuzs.puzzleslib.api.client.gui.v2.tooltip.TooltipBuilder;
import fuzs.statuemenus.api.v1.client.gui.screens.LegacyArmorStandPosesScreen;
import fuzs.statuemenus.api.v1.world.inventory.data.ArmorStandPose;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.MenuTooltipPositioner;
import net.minecraft.network.chat.FormattedText;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public final class TooltipFactories {

    private TooltipFactories() {
        // NO-OP
    }

    public static void applyPosesTooltip(AbstractWidget abstractWidget, int index) {
        TooltipBuilder.create()
                .setTooltipPositionerFactory(TooltipFactories::createPosesTooltipPositioner)
                .setLines(() -> {
                    return LegacyArmorStandPosesScreen.getPoseAt(index)
                            .map(ArmorStandPose::getTooltipLines)
                            .orElse(Collections.emptyList());
                })
                .build(abstractWidget);
    }

    private static ClientTooltipPositioner createPosesTooltipPositioner(ClientTooltipPositioner clientTooltipPositioner, AbstractWidget abstractWidget) {
        if (clientTooltipPositioner instanceof MenuTooltipPositioner) {
            return DefaultTooltipPositioner.INSTANCE;
        } else {
            return clientTooltipPositioner;
        }
    }

    public static void applyRotationsTooltip(AbstractWidget abstractWidget, boolean isLeft, Supplier<List<? extends FormattedText>> supplier) {
        TooltipBuilder.create()
                .setTooltipPositionerFactory(createRotationsTooltipPositioner(isLeft))
                .setLines(supplier)
                .build(abstractWidget);
    }

    private static BiFunction<ClientTooltipPositioner, AbstractWidget, ClientTooltipPositioner> createRotationsTooltipPositioner(boolean isLeft) {
        return (ClientTooltipPositioner clientTooltipPositioner, AbstractWidget abstractWidget) -> {
            if (clientTooltipPositioner instanceof MenuTooltipPositioner) {
                return (screenWidth, screenHeight, mouseX, mouseY, tooltipWidth, tooltipHeight) -> {
                    if (isLeft) mouseX -= 24 + tooltipWidth;
                    return DefaultTooltipPositioner.INSTANCE.positionTooltip(screenWidth,
                            screenHeight,
                            mouseX,
                            mouseY,
                            tooltipWidth,
                            tooltipHeight);
                };
            } else {
                return clientTooltipPositioner;
            }
        };
    }
}
