package fuzs.statuemenus.api.v1.client.gui.screens;

import com.google.common.collect.Lists;
import fuzs.statuemenus.api.v1.client.gui.components.NewTextureButton;
import fuzs.statuemenus.api.v1.network.client.data.DataSyncHandler;
import fuzs.statuemenus.api.v1.world.inventory.ArmorStandHolder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.renderer.texture.Tickable;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;

public abstract class ArmorStandWidgetsScreen extends AbstractArmorStandScreen {
    protected static final int WIDGET_HEIGHT = 22;

    protected final List<ArmorStandWidget> widgets;
    @Nullable
    private ArmorStandWidgetsScreen.ArmorStandWidget activeWidget;

    public ArmorStandWidgetsScreen(ArmorStandHolder holder, Inventory inventory, Component component, DataSyncHandler dataSyncHandler) {
        super(holder, inventory, component, dataSyncHandler);
        this.widgets = this.buildWidgets(holder.getArmorStand());
    }

    protected abstract List<ArmorStandWidget> buildWidgets(ArmorStand armorStand);

    protected static <T extends ArmorStandWidgetsScreen> List<ArmorStandWidget> buildWidgets(T screen, ArmorStand armorStand, List<ArmorStandWidgetFactory<? super T>> widgetFactories) {
        return widgetFactories.stream().map(factory -> factory.apply(screen, armorStand)).toList();
    }

    private Collection<ArmorStandWidget> getActivePositionComponentWidgets() {
        if (this.activeWidget != null) {
            List<ArmorStandWidget> activeWidgets = Lists.newArrayList(this.activeWidget);
            for (ArmorStandWidget widget : this.widgets) {
                if (widget.alwaysVisible(this.activeWidget)) activeWidgets.add(widget);
            }
            return activeWidgets;
        }
        return this.widgets;
    }

    protected void setActiveWidget(ArmorStandWidget widget) {
        if (this.activeWidget == widget) {
            this.toggleMenuRendering(false);
            this.activeWidget = null;
        } else {
            this.activeWidget = widget;
            this.toggleMenuRendering(true);
        }
    }

    @Override
    protected boolean renderInventoryEntity() {
        return false;
    }

    @Override
    protected boolean disableMenuRendering() {
        return this.activeWidget != null;
    }

    @Override
    protected void toggleMenuRendering(boolean disableMenuRendering) {
        super.toggleMenuRendering(disableMenuRendering);
        for (ArmorStandWidget widget : this.widgets) {
            widget.setVisible(!disableMenuRendering || widget.alwaysVisible(this.activeWidget));
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.getActivePositionComponentWidgets().forEach(ArmorStandWidget::tick);
    }

    @Override
    protected void init() {
        super.init();
        int fullWidgetsHeight =
                this.widgets.size() * WIDGET_HEIGHT + (this.widgets.size() - 1) * this.getWidgetRenderOffset();
        int startY = (this.imageHeight - fullWidgetsHeight) / 2;
        for (int i = 0; i < this.widgets.size(); i++) {
            this.widgets.get(i)
                    .init(this.leftPos + 8,
                            this.topPos + startY + this.getWidgetTopOffset() + i * (WIDGET_HEIGHT
                                    + this.getWidgetRenderOffset()));
        }
    }

    protected int getWidgetRenderOffset() {
        return 7;
    }

    protected int getWidgetTopOffset() {
        return this.withCloseButton() ? 7 : 0;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        for (ArmorStandWidget widget : this.getActivePositionComponentWidgets()) {
            widget.render(guiGraphics, mouseX, mouseY, partialTick);
        }
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @FunctionalInterface
    protected interface ArmorStandWidgetFactory<T extends ArmorStandWidgetsScreen> extends BiFunction<T, ArmorStand, ArmorStandWidget> {

    }

    protected interface ArmorStandWidget {

        void tick();

        void reset();

        void init(int posX, int posY);

        void setVisible(boolean visible);

        void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick);

        boolean alwaysVisible(@Nullable ArmorStandWidgetsScreen.ArmorStandWidget activeWidget);
    }

    protected abstract class AbstractArmorStandWidget implements ArmorStandWidget {
        protected final Component title;
        protected int posX;
        protected int posY;
        protected List<AbstractWidget> children;

        protected AbstractArmorStandWidget() {
            this(CommonComponents.EMPTY);
        }

        protected AbstractArmorStandWidget(Component title) {
            this.title = title;
        }

        @Override
        public void tick() {
            if (this.shouldTick()) {
                for (AbstractWidget widget : this.children) {
                    if (widget instanceof Tickable tickButton) {
                        tickButton.tick();
                    }
                }
            }
        }

        protected boolean shouldTick() {
            return false;
        }

        @Override
        public void reset() {
            // NO-OP
        }

        @Override
        public void init(int posX, int posY) {
            this.posX = posX;
            this.posY = posY;
            this.children = Lists.newArrayList();
        }

        @Override
        public final void setVisible(boolean visible) {
            for (AbstractWidget widget : this.children) {
                widget.visible = visible;
            }
        }

        protected <T extends AbstractWidget> T addChildren(T widget) {
            this.children.add(widget);
            return widget;
        }

        @Override
        public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            if (ArmorStandWidgetsScreen.this.disableMenuRendering()) {
                NewTextureButton.drawCenteredStringWithShadow(guiGraphics,
                        ArmorStandWidgetsScreen.this.font,
                        this.title,
                        this.posX + 36,
                        this.posY + 6,
                        -1,
                        true);
            } else {
                NewTextureButton.drawCenteredStringWithShadow(guiGraphics,
                        ArmorStandWidgetsScreen.this.font,
                        this.title,
                        this.posX + 36,
                        this.posY + 6,
                        0xFF404040,
                        false);
            }
        }

        @Override
        public boolean alwaysVisible(@Nullable ArmorStandWidgetsScreen.ArmorStandWidget activeWidget) {
            return activeWidget == this;
        }
    }
}
