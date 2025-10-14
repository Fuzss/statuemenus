package fuzs.statuemenus.api.v1.client.gui.screens;

import fuzs.statuemenus.api.v1.client.gui.components.TickButton;
import fuzs.statuemenus.api.v1.network.client.data.DataSyncHandler;
import fuzs.statuemenus.api.v1.world.inventory.StatueHolder;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public abstract class StatueButtonsScreen extends StatueWidgetsScreen {

    public StatueButtonsScreen(StatueHolder holder, Inventory inventory, Component component, DataSyncHandler dataSyncHandler) {
        super(holder, inventory, component, dataSyncHandler);
    }

    protected class SingleButtonWidget extends ArmorStandWidget {
        private final Component title;
        private final Component description;
        private final Component clickedTitle;
        private final Button.OnPress onPress;

        public SingleButtonWidget(Component title, Component description, Component clickedTitle, Button.OnPress onPress) {
            this.title = title;
            this.description = description;
            this.clickedTitle = clickedTitle;
            this.onPress = onPress;
        }

        @Override
        protected boolean shouldTick() {
            return true;
        }

        @Override
        public void init(int posX, int posY) {
            super.init(posX, posY);
            this.addRenderableWidget(new TickButton(posX,
                    posY + 1,
                    194,
                    20,
                    this.title,
                    this.clickedTitle,
                    this.onPress)).setTooltip(Tooltip.create(this.description));
        }
    }

    protected class DoubleButtonWidget extends ArmorStandWidget {
        private final Component titleLeft;
        private final Component titleRight;
        private final Component descriptionLeft;
        private final Component descriptionRight;
        private final Component clickedTitleLeft;
        private final Component clickedTitleRight;
        private final Button.OnPress onPressLeft;
        private final Button.OnPress onPressRight;

        public DoubleButtonWidget(Component titleLeft, Component titleRight, Component descriptionLeft, Component descriptionRight, Component clickedTitle, Button.OnPress onPressLeft, Button.OnPress onPressRight) {
            this(titleLeft,
                    titleRight,
                    descriptionLeft,
                    descriptionRight,
                    clickedTitle,
                    clickedTitle,
                    onPressLeft,
                    onPressRight);
        }

        public DoubleButtonWidget(Component titleLeft, Component titleRight, Component descriptionLeft, Component descriptionRight, Component clickedTitleLeft, Component clickedTitleRight, Button.OnPress onPressLeft, Button.OnPress onPressRight) {
            this.titleLeft = titleLeft;
            this.titleRight = titleRight;
            this.descriptionLeft = descriptionLeft;
            this.descriptionRight = descriptionRight;
            this.clickedTitleLeft = clickedTitleLeft;
            this.clickedTitleRight = clickedTitleRight;
            this.onPressLeft = onPressLeft;
            this.onPressRight = onPressRight;
        }

        @Override
        protected boolean shouldTick() {
            return true;
        }

        @Override
        public void init(int posX, int posY) {
            super.init(posX, posY);
            this.addRenderableWidget(new TickButton(posX,
                    posY + 1,
                    94,
                    20,
                    this.titleLeft,
                    this.clickedTitleLeft,
                    this.onPressLeft)).setTooltip(Tooltip.create(this.descriptionLeft));
            this.addRenderableWidget(new TickButton(posX + 100,
                    posY + 1,
                    94,
                    20,
                    this.titleRight,
                    this.clickedTitleRight,
                    this.onPressRight)).setTooltip(Tooltip.create(this.descriptionRight));
        }
    }
}
