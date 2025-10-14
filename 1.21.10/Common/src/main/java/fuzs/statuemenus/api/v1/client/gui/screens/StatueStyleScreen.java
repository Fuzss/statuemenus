package fuzs.statuemenus.api.v1.client.gui.screens;

import fuzs.statuemenus.api.v1.client.gui.components.TickBoxButton;
import fuzs.statuemenus.api.v1.network.client.data.DataSyncHandler;
import fuzs.statuemenus.api.v1.world.inventory.StatueHolder;
import fuzs.statuemenus.api.v1.world.inventory.data.StatueScreenType;
import fuzs.statuemenus.api.v1.world.inventory.data.StatueStyleOption;
import net.minecraft.Util;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class StatueStyleScreen<T extends LivingEntity> extends StatueTickBoxScreen<StatueStyleOption<? super T>> {
    public static final Component TEXT_BOX_HINT_TRANSLATION_KEY = Component.translatable(StatueScreenType.STYLE.id()
            .toLanguageKey("screen", "hint")).withStyle(EditBox.SEARCH_HINT_STYLE);
    public static final Component TEXT_BOX_TOOLTIP_TRANSLATION_KEY = Component.translatable(StatueScreenType.STYLE.id()
            .toLanguageKey("screen", "tooltip"));

    public StatueStyleScreen(StatueHolder holder, Inventory inventory, Component component, DataSyncHandler dataSyncHandler) {
        super(holder, inventory, component, dataSyncHandler);
    }

    protected abstract List<StatueStyleOption<? super T>> getStyleOptions();

    @Override
    protected List<StatueStyleOption<? super T>> getAllTickBoxValues() {
        return this.getStyleOptions()
                .stream()
                .filter((StatueStyleOption<? super T> option) -> option.mayEdit(this.minecraft.player))
                .toList();
    }

    @Override
    protected AbstractWidget makeTickBoxWidget(LivingEntity livingEntity, int buttonStartY, int index, StatueStyleOption<? super T> option) {
        return Util.make(new TickBoxButton(this.leftPos + 96,
                this.topPos + buttonStartY + index * 22,
                6,
                76,
                Component.translatable(option.getTranslationKey()),
                () -> option.getOption((T) livingEntity),
                (Button button) -> {
                    this.dataSyncHandler.sendStyleOption(option, !option.getOption((T) livingEntity));
                }), (TickBoxButton button) -> {
            button.setTooltip(Tooltip.create(Component.translatable(option.getDescriptionKey())));
        });
    }

    @Override
    protected void syncNameChange(String input) {
        this.dataSyncHandler.sendName(input);
    }

    @Override
    protected int getNameMaxLength() {
        return 50;
    }

    @Override
    protected @Nullable String getNameValue() {
        if (this.holder.getEntity().hasCustomName()) {
            return this.holder.getEntity().getCustomName().getString();
        } else {
            return null;
        }
    }

    @Override
    protected Component getNameHint() {
        return TEXT_BOX_HINT_TRANSLATION_KEY;
    }

    @Override
    protected Component getNameTooltip() {
        return TEXT_BOX_TOOLTIP_TRANSLATION_KEY;
    }
}
