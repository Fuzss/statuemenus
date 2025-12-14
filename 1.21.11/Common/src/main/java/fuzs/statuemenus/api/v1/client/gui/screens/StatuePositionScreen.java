package fuzs.statuemenus.api.v1.client.gui.screens;

import fuzs.puzzleslib.api.client.gui.v2.components.SpritelessImageButton;
import fuzs.puzzleslib.api.client.gui.v2.tooltip.TooltipBuilder;
import fuzs.puzzleslib.api.util.v1.CommonHelper;
import fuzs.statuemenus.api.v1.client.gui.components.FlatButton;
import fuzs.statuemenus.api.v1.client.gui.components.FlatSliderButton;
import fuzs.statuemenus.api.v1.client.gui.components.FlatTickButton;
import fuzs.statuemenus.api.v1.helper.ScaleAttributeHelper;
import fuzs.statuemenus.api.v1.network.client.data.DataSyncHandler;
import fuzs.statuemenus.api.v1.world.inventory.StatueHolder;
import fuzs.statuemenus.api.v1.world.inventory.data.StatueScreenType;
import fuzs.statuemenus.impl.world.inventory.StatuePoses;
import net.minecraft.util.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import org.jspecify.annotations.Nullable;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.OptionalDouble;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

public class StatuePositionScreen extends StatueButtonsScreen {
    public static final String SCALE_TRANSLATION_KEY = StatueScreenType.POSITION.id().toLanguageKey("screen", "scale");
    public static final String ROTATION_TRANSLATION_KEY = StatueScreenType.POSITION.id()
            .toLanguageKey("screen", "rotation");
    public static final String POSITION_X_TRANSLATION_KEY = StatueScreenType.POSITION.id().toLanguageKey("screen", "x");
    public static final String POSITION_Y_TRANSLATION_KEY = StatueScreenType.POSITION.id().toLanguageKey("screen", "y");
    public static final String POSITION_Z_TRANSLATION_KEY = StatueScreenType.POSITION.id().toLanguageKey("screen", "z");
    public static final String INCREMENT_TRANSLATION_KEY = StatueScreenType.POSITION.id()
            .toLanguageKey("screen", "increment");
    public static final String DECREMENT_TRANSLATION_KEY = StatueScreenType.POSITION.id()
            .toLanguageKey("screen", "decrement");
    public static final String PIXELS_TRANSLATION_KEY = StatueScreenType.POSITION.id()
            .toLanguageKey("screen", "pixels");
    public static final String BLOCKS_TRANSLATION_KEY = StatueScreenType.POSITION.id()
            .toLanguageKey("screen", "blocks");
    public static final String DEGREES_TRANSLATION_KEY = StatueScreenType.POSITION.id()
            .toLanguageKey("screen", "degrees");
    public static final String MOVE_BY_TRANSLATION_KEY = StatueScreenType.POSITION.id()
            .toLanguageKey("screen", "move_by");
    public static final String CENTERED_TRANSLATION_KEY = StatueScreenType.POSITION.id()
            .toLanguageKey("screen", "centered");
    public static final String CENTERED_DESCRIPTION_TRANSLATION_KEY = StatueScreenType.POSITION.id()
            .toLanguageKey("screen", "centered.description");
    public static final String CORNERED_TRANSLATION_KEY = StatueScreenType.POSITION.id()
            .toLanguageKey("screen", "cornered");
    public static final String CORNERED_DESCRIPTION_TRANSLATION_KEY = StatueScreenType.POSITION.id()
            .toLanguageKey("screen", "cornered.description");
    public static final String ALIGNED_TRANSLATION_KEY = StatueScreenType.POSITION.id()
            .toLanguageKey("screen", "aligned");
    protected static final ArmorStandWidgetFactory<StatuePositionScreen> SCALE_WIDGET_FACTORY = (StatuePositionScreen screen, LivingEntity livingEntity) -> {
        return screen.new ScaleWidget(Component.translatable(SCALE_TRANSLATION_KEY),
                livingEntity::getScale,
                screen.dataSyncHandler::sendScale) {
            @Override
            protected OptionalDouble getDefaultValue() {
                return OptionalDouble.of(fromLogarithmicValue(ScaleAttributeHelper.DEFAULT_SCALE));
            }
        };
    };
    protected static final ArmorStandWidgetFactory<StatuePositionScreen> ROTATION_WIDGET_FACTORY = (StatuePositionScreen screen, LivingEntity livingEntity) -> {
        return screen.new RotationWidget(Component.translatable(ROTATION_TRANSLATION_KEY),
                livingEntity::getYRot,
                screen.dataSyncHandler::sendRotation);
    };
    protected static final ArmorStandWidgetFactory<StatuePositionScreen> POSITION_INCREMENT_WIDGET_FACTORY = (StatuePositionScreen screen, LivingEntity livingEntity) -> {
        return screen.new PositionControlsWidget();
    };
    protected static final ArmorStandWidgetFactory<StatuePositionScreen> POSITION_X_WIDGET_FACTORY = (StatuePositionScreen screen, LivingEntity livingEntity) -> {
        return screen.new PositionComponentWidget(POSITION_X_TRANSLATION_KEY, livingEntity::getX, (double x) -> {
            screen.dataSyncHandler.sendPosition(x, livingEntity.getY(), livingEntity.getZ());
        });
    };
    protected static final ArmorStandWidgetFactory<StatuePositionScreen> POSITION_Y_WIDGET_FACTORY = (StatuePositionScreen screen, LivingEntity livingEntity) -> {
        return screen.new PositionComponentWidget(POSITION_Y_TRANSLATION_KEY, livingEntity::getY, (double y) -> {
            screen.dataSyncHandler.sendPosition(livingEntity.getX(), y, livingEntity.getZ());
        });
    };
    protected static final ArmorStandWidgetFactory<StatuePositionScreen> POSITION_Z_WIDGET_FACTORY = (StatuePositionScreen screen, LivingEntity livingEntity) -> {
        return screen.new PositionComponentWidget(POSITION_Z_TRANSLATION_KEY, livingEntity::getZ, (double z) -> {
            screen.dataSyncHandler.sendPosition(livingEntity.getX(), livingEntity.getY(), z);
        });
    };
    private static final DecimalFormat BLOCK_INCREMENT_FORMAT = Util.make(new DecimalFormat("#.####"),
            (DecimalFormat decimalFormat) -> {
                decimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ROOT));
            });
    private static final double[] INCREMENTS = {0.0625, 0.25, 0.5, 1.0};

    private static double currentIncrement = INCREMENTS[0];

    public StatuePositionScreen(StatueHolder holder, Inventory inventory, Component component, DataSyncHandler dataSyncHandler) {
        super(holder, inventory, component, dataSyncHandler);
    }

    @Override
    protected List<ArmorStandWidget> buildWidgets(LivingEntity livingEntity) {
        return buildWidgets(this,
                livingEntity,
                List.of(SCALE_WIDGET_FACTORY,
                        ROTATION_WIDGET_FACTORY,
                        POSITION_INCREMENT_WIDGET_FACTORY,
                        POSITION_X_WIDGET_FACTORY,
                        POSITION_Y_WIDGET_FACTORY,
                        POSITION_Z_WIDGET_FACTORY));
    }

    @Override
    protected boolean withCloseButton() {
        return false;
    }

    @Override
    public StatueScreenType getScreenType() {
        return StatueScreenType.POSITION;
    }

    private static Component getPixelIncrementComponent(double increment) {
        return Component.translatable(PIXELS_TRANSLATION_KEY, getBlockPixelIncrement(increment));
    }

    private static Component getBlockIncrementComponent(double increment) {
        return Component.translatable(BLOCKS_TRANSLATION_KEY, BLOCK_INCREMENT_FORMAT.format(increment));
    }

    private static int getBlockPixelIncrement(double increment) {
        return (int) Math.round(increment * 16.0);
    }

    protected class ScaleWidget extends RotationWidget {
        static final double LOGARITHMIC_SCALE = 2.0;
        static final double LOGARITHMIC_SCALE_POW = Math.pow(10.0, -LOGARITHMIC_SCALE);

        public ScaleWidget(Component title, DoubleSupplier currentValue, Consumer<Float> newValue) {
            super(title, currentValue, newValue, FlatSliderButton.NO_SNAP_INTERVAL);
        }

        @Override
        protected double getCurrentValue() {
            return fromLogarithmicValue(this.valueGetter.getAsDouble());
        }

        @Override
        protected void setNewValue(double newValue) {
            this.valueSetter.accept(toLogarithmicValue(newValue));
        }

        @Override
        protected Component getTooltipComponent(double mouseValue) {
            mouseValue = toLogarithmicValue(mouseValue);
            mouseValue = (int) (mouseValue * 100.0F) / 100.0F;
            mouseValue = Mth.clamp(mouseValue, ScaleAttributeHelper.MIN_SCALE, ScaleAttributeHelper.MAX_SCALE);
            return Component.literal(StatuePoses.ROTATION_FORMAT.format(mouseValue));
        }

        @Override
        protected void applyClientValue(double newValue) {
            ScaleAttributeHelper.setScale(StatuePositionScreen.this.getHolder().getEntity(), toLogarithmicValue(newValue));
        }

        public static double fromLogarithmicValue(double value) {
            value = Mth.inverseLerp(value,
                    ScaleAttributeHelper.MIN_SCALE,
                    ScaleAttributeHelper.MAX_SCALE);
            return (Math.log10(value + LOGARITHMIC_SCALE_POW) + LOGARITHMIC_SCALE) / LOGARITHMIC_SCALE;
        }

        public static float toLogarithmicValue(double value) {
            value = Math.pow(10.0, value * LOGARITHMIC_SCALE - LOGARITHMIC_SCALE) - LOGARITHMIC_SCALE_POW;
            return (float) Mth.lerp(value, ScaleAttributeHelper.MIN_SCALE, ScaleAttributeHelper.MAX_SCALE);
        }
    }

    protected class RotationWidget extends ArmorStandWidget {
        protected final DoubleSupplier valueGetter;
        protected final Consumer<Float> valueSetter;
        private final double snapInterval;
        @Nullable protected FlatSliderButton sliderButton;
        @Nullable protected Button resetButton;

        public RotationWidget(Component title, DoubleSupplier valueGetter, Consumer<Float> valueSetter) {
            this(title, valueGetter, valueSetter, StatuePoses.DEGREES_SNAP_INTERVAL);
        }

        public RotationWidget(Component title, DoubleSupplier valueGetter, Consumer<Float> valueSetter, double snapInterval) {
            super(title);
            this.valueGetter = valueGetter;
            this.valueSetter = valueSetter;
            this.snapInterval = snapInterval;
        }

        protected double getCurrentValue() {
            return fromWrappedDegrees(this.valueGetter.getAsDouble());
        }

        protected void setNewValue(double newValue) {
            this.valueSetter.accept(toWrappedDegrees(newValue));
        }

        protected OptionalDouble getDefaultValue() {
            return OptionalDouble.empty();
        }

        protected Component getTooltipComponent(double mouseValue) {
            return Component.translatable(DEGREES_TRANSLATION_KEY,
                    StatuePoses.ROTATION_FORMAT.format(toWrappedDegrees(mouseValue)));
        }

        public static double fromWrappedDegrees(double value) {
            return (Mth.wrapDegrees(value) + 180.0) / 360.0;
        }

        public static float toWrappedDegrees(double value) {
            return (float) Mth.wrapDegrees(value * 360.0 - 180.0);
        }

        protected void applyClientValue(double newValue) {
            // NO-OP
        }

        @Override
        public void reset() {
            if (this.sliderButton != null) {
                this.sliderButton.setSliderValue(this.getCurrentValue());
            }
        }

        @Override
        public void tick() {
            super.tick();
            this.setupButtonVisibility();
        }

        @Override
        public void setVisible(boolean visible) {
            super.setVisible(visible);
            if (visible) {
                this.setupButtonVisibility();
            }
        }

        private void setupButtonVisibility() {
            if (this.toggleButton != null && this.resetButton != null) {
                this.resetButton.visible = CommonHelper.hasAltDown();
                this.toggleButton.visible = !this.resetButton.visible;
            }
        }

        @Override
        public void init(int posX, int posY) {
            super.init(posX, posY);
            this.sliderButton = new FlatSliderButton(posX + 76,
                    posY + 1,
                    90,
                    20,
                    0,
                    184,
                    getArmorStandWidgetsLocation(),
                    CommonComponents.EMPTY,
                    this.getCurrentValue()) {
                private boolean dirty;

                @Override
                public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                    super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
                    double mouseValue = StatuePoses.snapValue((mouseX - this.getX()) / (double) this.getWidth(),
                            this.snapInterval);
                    this.setTooltip(Tooltip.create(RotationWidget.this.getTooltipComponent(mouseValue)));
                }

                @Override
                protected void updateMessage() {
                    // NO-OP
                }

                @Override
                protected void applyValue() {
                    this.dirty = true;
                    RotationWidget.this.applyClientValue(this.value);
                }

                @Override
                public void onRelease(MouseButtonEvent mouseButtonEvent) {
                    super.onRelease(mouseButtonEvent);
                    this.clearDirty();
                }

                @Override
                public boolean isDirty() {
                    return this.dirty;
                }

                @Override
                public void clearDirty() {
                    // we use #onRelease instead of directly applying in #applyValue as the armor stand will otherwise glitch out visually since the server constantly sends outdated values
                    if (this.isDirty()) {
                        this.dirty = false;
                        RotationWidget.this.setNewValue(this.value);
                    }
                }
            };
            this.sliderButton.snapInterval = this.snapInterval;
            this.addRenderableWidget(this.sliderButton);
            if (this.getDefaultValue().isPresent()) {
                this.resetButton = this.addRenderableWidget(new FlatTickButton(posX + 174,
                        posY + 1,
                        20,
                        20,
                        240,
                        124,
                        getArmorStandWidgetsLocation(),
                        (Button button) -> {
                            this.getDefaultValue().ifPresent((double value) -> {
                                this.setNewValue(value);
                                this.applyClientValue(value);
                                this.reset();
                            });
                        }));
                this.resetButton.setTooltip(Tooltip.create(Component.translatable(StatueRotationsScreen.RESET_TRANSLATION_KEY)));
                this.setupButtonVisibility();
            }
        }

        @Override
        protected boolean supportsToggleButton() {
            return true;
        }
    }

    private class PositionControlsWidget extends ArmorStandWidget {

        public PositionControlsWidget() {
            super(Component.translatable(MOVE_BY_TRANSLATION_KEY));
        }

        @Override
        public void init(int posX, int posY) {
            super.init(posX, posY);
            for (int i = 0; i < INCREMENTS.length; i++) {
                double increment = INCREMENTS[i];
                AbstractWidget widget = this.addRenderableWidget(new FlatButton(posX + 76 + i * 24 + (i > 1 ? 1 : 0),
                        posY + 1,
                        20,
                        20,
                        0,
                        184,
                        getArmorStandWidgetsLocation(),
                        Component.literal(String.valueOf(getBlockPixelIncrement(increment))),
                        (Button button) -> {
                            this.setActiveIncrement(button, increment);
                        }));
                TooltipBuilder.create(getPixelIncrementComponent(increment), getBlockIncrementComponent(increment))
                        .build(widget);
                if (increment == currentIncrement) {
                    widget.active = false;
                }
            }
        }

        @Override
        protected boolean supportsToggleButton() {
            return true;
        }

        private void setActiveIncrement(AbstractWidget source, double increment) {
            currentIncrement = increment;
            for (GuiEventListener widget : this.children()) {
                if (widget instanceof AbstractWidget abstractWidget) {
                    abstractWidget.active = widget != source;
                }
            }
        }

        @Override
        public boolean alwaysVisible(@Nullable ArmorStandWidget activeWidget) {
            return !(activeWidget instanceof RotationWidget);
        }
    }

    private class PositionComponentWidget extends ArmorStandWidget {
        private final DoubleSupplier currentValue;
        private final DoubleConsumer newValue;

        private EditBox editBox;
        private int ticks;

        public PositionComponentWidget(String translationKey, DoubleSupplier currentValue, DoubleConsumer newValue) {
            super(Component.translatable(translationKey));
            this.currentValue = currentValue;
            this.newValue = newValue;
        }

        @Override
        public void tick() {
            super.tick();
            // armor stand position might change externally, so we update occasionally
            if (--this.ticks <= 0 && this.editBox != null) {
                this.ticks = 10;
                this.editBox.setValue(BLOCK_INCREMENT_FORMAT.format(this.getPositionValue()));
            }
        }

        @Override
        public void init(int posX, int posY) {
            super.init(posX, posY);
            this.editBox = new EditBox(StatuePositionScreen.this.font,
                    posX + 77,
                    posY,
                    66,
                    22,
                    StatuePositionScreen.this.getHolder().getEntity().getType().getDescription());
            this.editBox.setMaxLength(50);
            this.editBox.setEditable(false);
            this.editBox.setTextColorUneditable(0XFFE0E0E0);
            this.editBox.setValue(BLOCK_INCREMENT_FORMAT.format(this.getPositionValue()));
            this.addRenderableWidget(this.editBox);
            AbstractWidget incrementButton = this.addRenderableWidget(new SpritelessImageButton(posX + 149,
                    posY + 1,
                    20,
                    10,
                    196,
                    64,
                    20,
                    getArmorStandWidgetsLocation(),
                    256,
                    256,
                    (Button button) -> {
                        this.setPositionValue(this.getPositionValue() + currentIncrement);
                    }));
            TooltipBuilder.create()
                    .setLines(() -> Collections.singletonList(Component.translatable(INCREMENT_TRANSLATION_KEY,
                            getPixelIncrementComponent(currentIncrement))))
                    .build(incrementButton);
            AbstractWidget decrementButton = this.addRenderableWidget(new SpritelessImageButton(posX + 149,
                    posY + 11,
                    20,
                    10,
                    216,
                    74,
                    20,
                    getArmorStandWidgetsLocation(),
                    256,
                    256,
                    (Button button) -> {
                        this.setPositionValue(this.getPositionValue() - currentIncrement);
                    }));
            TooltipBuilder.create()
                    .setLines(() -> Collections.singletonList(Component.translatable(DECREMENT_TRANSLATION_KEY,
                            getPixelIncrementComponent(currentIncrement))))
                    .build(decrementButton);
        }

        @Override
        protected boolean supportsToggleButton() {
            return true;
        }

        private double getPositionValue() {
            return roundWithPrecision(this.currentValue.getAsDouble(), 16.0, 4);
        }

        private static double roundWithPrecision(double toRound, double roundingPrecision, int decimalPlaces) {
            toRound = Math.round(toRound * roundingPrecision) / roundingPrecision;
            final double power = Math.pow(10, decimalPlaces);
            return Math.round(toRound * power) / power;
        }

        private void setPositionValue(double newValue) {
            this.ticks = 20;
            newValue = Math.round(newValue * 16.0) / 16.0;
            if (this.getPositionValue() != newValue) {
                this.editBox.setValue(BLOCK_INCREMENT_FORMAT.format(newValue));
                this.newValue.accept(newValue);
            }
        }

        @Override
        public boolean alwaysVisible(@Nullable ArmorStandWidget activeWidget) {
            return activeWidget instanceof PositionControlsWidget || super.alwaysVisible(activeWidget);
        }
    }
}
