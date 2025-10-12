package fuzs.statuemenus.api.v1.client.gui.screens;

import fuzs.statuemenus.api.v1.client.gui.components.TickBoxButton;
import fuzs.statuemenus.api.v1.network.client.data.DataSyncHandler;
import fuzs.statuemenus.api.v1.world.inventory.StatueHolder;
import fuzs.statuemenus.api.v1.world.inventory.data.StatueScreenType;
import fuzs.statuemenus.api.v1.world.inventory.data.StatueStyleOption;
import net.minecraft.Util;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;

public abstract class StatueStyleScreen<T extends LivingEntity> extends StatueTickBoxScreen<StatueStyleOption<? super T>> {
    public static final String TEXT_BOX_TRANSLATION_KEY = StatueScreenType.STYLE.id().toLanguageKey("screen", "name");

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
    protected String getNameDefaultValue() {
        return this.holder.getEntity().getName().getString();
    }

    @Override
    protected Component getNameComponent() {
        return Component.translatable(TEXT_BOX_TRANSLATION_KEY);
    }
}
