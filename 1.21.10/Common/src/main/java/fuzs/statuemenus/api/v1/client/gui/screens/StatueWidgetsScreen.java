package fuzs.statuemenus.api.v1.client.gui.screens;

import fuzs.puzzleslib.api.client.gui.v2.components.SpritelessImageButton;
import fuzs.statuemenus.api.v1.client.gui.components.FlatButton;
import fuzs.statuemenus.api.v1.network.client.data.DataSyncHandler;
import fuzs.statuemenus.api.v1.world.inventory.StatueHolder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.renderer.texture.Tickable;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;

public abstract class StatueWidgetsScreen extends AbstractStatueScreen {
    protected static final int WIDGET_HEIGHT = 22;

    protected final List<ArmorStandWidget> widgets;
    @Nullable
    private StatueWidgetsScreen.ArmorStandWidget activeWidget;

    public StatueWidgetsScreen(StatueHolder holder, Inventory inventory, Component component, DataSyncHandler dataSyncHandler) {
        super(holder, inventory, component, dataSyncHandler);
        this.widgets = this.buildWidgets(holder.getEntity());
    }

    protected abstract List<ArmorStandWidget> buildWidgets(LivingEntity livingEntity);

    protected static <T extends StatueWidgetsScreen> List<ArmorStandWidget> buildWidgets(T screen, LivingEntity livingEntity, List<ArmorStandWidgetFactory<? super T>> widgetFactories) {
        return widgetFactories.stream().map(factory -> factory.apply(screen, livingEntity)).toList();
    }

    private Collection<ArmorStandWidget> getActivePositionComponentWidgets() {
        if (this.activeWidget != null) {
            List<ArmorStandWidget> activeWidgets = new ArrayList<>(Arrays.asList(this.activeWidget));
            for (ArmorStandWidget widget : this.widgets) {
                if (widget.alwaysVisible(this.activeWidget)) {
                    activeWidgets.add(widget);
                }
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
    protected interface ArmorStandWidgetFactory<T extends StatueWidgetsScreen> extends BiFunction<T, LivingEntity, ArmorStandWidget> {

    }

    protected abstract class ArmorStandWidget extends AbstractContainerEventHandler implements Renderable {
        private final List<GuiEventListener> children = new ArrayList<>();
        protected final Component title;
        protected int posX;
        protected int posY;
        protected Button toggleButton;

        protected ArmorStandWidget() {
            this(CommonComponents.EMPTY);
        }

        protected ArmorStandWidget(Component title) {
            this.title = title;
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return this.children;
        }

        public void tick() {
            if (this.shouldTick()) {
                for (GuiEventListener widget : this.children()) {
                    if (widget instanceof Tickable tickButton) {
                        tickButton.tick();
                    }
                }
            }
        }

        protected boolean shouldTick() {
            return false;
        }

        public void reset() {
            // NO-OP
        }

        public void init(int posX, int posY) {
            this.children().clear();
            this.posX = posX;
            this.posY = posY;
            this.toggleButton = new SpritelessImageButton(posX + 174,
                    posY + 1,
                    20,
                    20,
                    236,
                    64,
                    getArmorStandWidgetsLocation(),
                    (Button button) -> {
                        StatueWidgetsScreen.this.setActiveWidget(this);
                    });
        }

        public final void setVisible(boolean visible) {
            for (GuiEventListener widget : this.children()) {
                if (widget instanceof AbstractWidget abstractWidget) {
                    abstractWidget.visible = visible;
                }
            }
        }

        protected <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(T widget) {
            this.children.add(widget);
            StatueWidgetsScreen.this.addRenderableWidget(widget);
            return widget;
        }

        @Override
        public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            if (StatueWidgetsScreen.this.disableMenuRendering()) {
                FlatButton.drawCenteredStringWithShadow(guiGraphics,
                        StatueWidgetsScreen.this.font,
                        this.title,
                        this.posX + 36,
                        this.posY + 6,
                        -1,
                        true);
            } else {
                FlatButton.drawCenteredStringWithShadow(guiGraphics,
                        StatueWidgetsScreen.this.font,
                        this.title,
                        this.posX + 36,
                        this.posY + 6,
                        0xFF404040,
                        false);
            }
        }

        public boolean alwaysVisible(@Nullable StatueWidgetsScreen.ArmorStandWidget activeWidget) {
            return activeWidget == this;
        }
    }
}
