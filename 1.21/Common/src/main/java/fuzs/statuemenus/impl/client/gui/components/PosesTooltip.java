package fuzs.statuemenus.impl.client.gui.components;

import com.google.common.collect.Lists;
import fuzs.puzzleslib.api.client.gui.v2.components.tooltip.TooltipComponentImpl;
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

public class PosesTooltip extends TooltipComponentImpl {
    private final int index;

    public PosesTooltip(AbstractWidget abstractWidget, int index) {
        super(abstractWidget);
        this.index = index;
    }

    @Override
    public List<? extends FormattedText> getLinesForNextRenderPass() {
        Optional<ArmorStandPose> optional = ArmorStandPosesScreen.getPoseAt(this.index);
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

    @Override
    public ClientTooltipPositioner createTooltipPositioner(AbstractWidget abstractWidget) {
        ClientTooltipPositioner tooltipPositioner = super.createTooltipPositioner(abstractWidget);
        if (tooltipPositioner instanceof MenuTooltipPositioner) {
            return DefaultTooltipPositioner.INSTANCE;
        } else {
            return tooltipPositioner;
        }
    }
}
