package fuzs.statuemenus.api.v1.client.gui.components;

import fuzs.puzzleslib.api.client.gui.v2.components.ScreenTooltipFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public abstract class AbstractTooltip extends Tooltip {
    @Nullable
    private List<Component> lines;
    @Nullable
    private List<FormattedCharSequence> cachedTooltip;

    public AbstractTooltip() {
        super(CommonComponents.EMPTY, null);
    }

    @Override
    public List<FormattedCharSequence> toCharSequence(Minecraft minecraft) {
        if (this.cachedTooltip == null) {
            Objects.requireNonNull(this.lines, "lines is null");
            this.cachedTooltip = this.lines.stream().flatMap(ScreenTooltipFactory::splitTooltipLines).toList();
        }
        return this.cachedTooltip;
    }

    @Override
    public void refreshTooltipForNextRenderPass(boolean hovering, boolean focused, ScreenRectangle screenRectangle) {
        List<Component> lines = this.getLinesForNextRenderPass();
        if (!Objects.equals(lines, this.lines)) {
            this.lines = lines;
            this.cachedTooltip = null;
        }
        if (this.lines != null) {
            super.refreshTooltipForNextRenderPass(hovering, focused, screenRectangle);
        }
    }

    @Nullable
    protected abstract List<Component> getLinesForNextRenderPass();
}
