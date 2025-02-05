package fuzs.statuemenus.impl.client.gui.components;

import com.google.common.collect.Lists;
import fuzs.puzzleslib.api.client.gui.v2.components.tooltip.TooltipBuilder;
import fuzs.statuemenus.api.v1.client.gui.screens.ArmorStandPosesScreen;
import fuzs.statuemenus.api.v1.world.inventory.data.ArmorStandPose;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.MenuTooltipPositioner;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.StringUtil;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public final class TooltipFactories {

    private TooltipFactories() {
        // NO-OP
    }

    public static void applyPosesTooltip(AbstractWidget abstractWidget, int index) {
        TooltipBuilder.create()
                .setTooltipPositionerFactory(TooltipFactories::createPosesTooltipPositioner)
                .setLines(() -> getLinesForNextRenderPass(index))
                .build(abstractWidget);
    }

    private static List<? extends FormattedText> getLinesForNextRenderPass(int index) {
        Optional<ArmorStandPose> optional = ArmorStandPosesScreen.getPoseAt(index);
        if (optional.isPresent()) {
            String translationKey = optional.get().getTranslationKey();
            if (translationKey != null) {
                Component component = Component.translatable(translationKey);
                List<Component> lines = Lists.newArrayList(component);
                String source = optional.get().getSourceType().getDisplayName();
                if (!StringUtil.isNullOrEmpty(source)) {
                    lines.add(Component.translatable(ArmorStandPosesScreen.POSE_SOURCE_TRANSLATION_KEY, source)
                            .withStyle(ChatFormatting.GRAY));
                }
                return lines;
            }
        }

        return Collections.emptyList();
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
                    return DefaultTooltipPositioner.INSTANCE.positionTooltip(screenWidth, screenHeight, mouseX, mouseY, tooltipWidth, tooltipHeight);
                };
            } else {
                return clientTooltipPositioner;
            }
        };
    }
}
