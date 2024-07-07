package fuzs.statuemenus.impl.client.gui.components;

import com.google.common.collect.Lists;
import fuzs.statuemenus.api.v1.client.gui.components.AbstractTooltip;
import fuzs.statuemenus.api.v1.client.gui.screens.ArmorStandPosesScreen;
import fuzs.statuemenus.api.v1.world.inventory.data.ArmorStandPose;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.MenuTooltipPositioner;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class PosesTooltip extends AbstractTooltip {
    private final int index;

    public PosesTooltip(int index) {
        this.index = index;
    }

    @Nullable
    protected List<Component> getLinesForNextRenderPass() {
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
        return null;
    }

    @Override
    protected ClientTooltipPositioner createTooltipPositioner(boolean hovering, boolean focused, ScreenRectangle screenRectangle) {
        ClientTooltipPositioner tooltipPositioner = super.createTooltipPositioner(hovering, focused, screenRectangle);
        return tooltipPositioner instanceof MenuTooltipPositioner ?
                DefaultTooltipPositioner.INSTANCE :
                tooltipPositioner;
    }
}
