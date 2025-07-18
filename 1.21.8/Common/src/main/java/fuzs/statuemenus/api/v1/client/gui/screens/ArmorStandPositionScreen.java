package fuzs.statuemenus.api.v1.client.gui.screens;

import fuzs.puzzleslib.api.client.gui.v2.components.SpritelessImageButton;
import fuzs.puzzleslib.api.client.gui.v2.tooltip.TooltipBuilder;
import fuzs.statuemenus.api.v1.client.gui.components.NewTextureButton;
import fuzs.statuemenus.api.v1.client.gui.components.NewTextureSliderButton;
import fuzs.statuemenus.api.v1.helper.ScaleAttributeHelper;
import fuzs.statuemenus.api.v1.network.client.data.DataSyncHandler;
import fuzs.statuemenus.api.v1.world.inventory.ArmorStandHolder;
import fuzs.statuemenus.api.v1.world.inventory.data.ArmorStandScreenType;
import fuzs.statuemenus.impl.StatueMenus;
import fuzs.statuemenus.impl.world.inventory.ArmorStandPoses;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

public class ArmorStandPositionScreen extends ArmorStandButtonsScreen {
    public static final String SCALE_TRANSLATION_KEY = StatueMenus.MOD_ID + ".screen.position.scale";
    public static final String ROTATION_TRANSLATION_KEY = StatueMenus.MOD_ID + ".screen.position.rotation";
    public static final String POSITION_X_TRANSLATION_KEY = StatueMenus.MOD_ID + ".screen.position.x";
    public static final String POSITION_Y_TRANSLATION_KEY = StatueMenus.MOD_ID + ".screen.position.y";
    public static final String POSITION_Z_TRANSLATION_KEY = StatueMenus.MOD_ID + ".screen.position.z";
    public static final String INCREMENT_TRANSLATION_KEY = StatueMenus.MOD_ID + ".screen.position.increment";
    public static final String DECREMENT_TRANSLATION_KEY = StatueMenus.MOD_ID + ".screen.position.decrement";
    public static final String PIXELS_TRANSLATION_KEY = StatueMenus.MOD_ID + ".screen.position.pixels";
    public static final String BLOCKS_TRANSLATION_KEY = StatueMenus.MOD_ID + ".screen.position.blocks";
    public static final String DEGREES_TRANSLATION_KEY = StatueMenus.MOD_ID + ".screen.position.degrees";
    public static final String MOVE_BY_TRANSLATION_KEY = StatueMenus.MOD_ID + ".screen.position.moveBy";
    public static final String CENTERED_TRANSLATION_KEY = StatueMenus.MOD_ID + ".screen.centered";
    public static final String CENTERED_DESCRIPTION_TRANSLATION_KEY =
            StatueMenus.MOD_ID + ".screen.centered.description";
    public static final String CORNERED_TRANSLATION_KEY = StatueMenus.MOD_ID + ".screen.cornered";
    public static final String CORNERED_DESCRIPTION_TRANSLATION_KEY =
            StatueMenus.MOD_ID + ".screen.cornered.description";
    public static final String ALIGNED_TRANSLATION_KEY = StatueMenus.MOD_ID + ".screen.aligned";
    protected static final ArmorStandWidgetFactory<ArmorStandPositionScreen> SCALE_WIDGET_FACTORY = (ArmorStandPositionScreen screen, ArmorStand armorStand) -> {
        return screen.new ScaleWidget(Component.translatable(SCALE_TRANSLATION_KEY),
                armorStand::getScale,
                screen.dataSyncHandler::sendScale);
    };
    protected static final ArmorStandWidgetFactory<ArmorStandPositionScreen> ROTATION_WIDGET_FACTORY = (ArmorStandPositionScreen screen, ArmorStand armorStand) -> {
        return screen.new RotationWidget(Component.translatable(ROTATION_TRANSLATION_KEY),
                armorStand::getYRot,
                screen.dataSyncHandler::sendRotation);
    };
    protected static final ArmorStandWidgetFactory<ArmorStandPositionScreen> POSITION_INCREMENT_WIDGET_FACTORY = (ArmorStandPositionScreen screen, ArmorStand armorStand) -> {
        return screen.new PositionIncrementWidget();
    };
    protected static final ArmorStandWidgetFactory<ArmorStandPositionScreen> POSITION_X_WIDGET_FACTORY = (ArmorStandPositionScreen screen, ArmorStand armorStand) -> {
        return screen.new PositionComponentWidget(POSITION_X_TRANSLATION_KEY, armorStand::getX, (double x) -> {
            screen.dataSyncHandler.sendPosition(x, armorStand.getY(), armorStand.getZ());
        });
    };
    protected static final ArmorStandWidgetFactory<ArmorStandPositionScreen> POSITION_Y_WIDGET_FACTORY = (ArmorStandPositionScreen screen, ArmorStand armorStand) -> {
        return screen.new PositionComponentWidget(POSITION_Y_TRANSLATION_KEY, armorStand::getY, (double y) -> {
            screen.dataSyncHandler.sendPosition(armorStand.getX(), y, armorStand.getZ());
        });
    };
    protected static final ArmorStandWidgetFactory<ArmorStandPositionScreen> POSITION_Z_WIDGET_FACTORY = (ArmorStandPositionScreen screen, ArmorStand armorStand) -> {
        return screen.new PositionComponentWidget(POSITION_Z_TRANSLATION_KEY, armorStand::getZ, (double z) -> {
            screen.dataSyncHandler.sendPosition(armorStand.getX(), armorStand.getY(), z);
        });
    };
    private static final DecimalFormat BLOCK_INCREMENT_FORMAT = Util.make(new DecimalFormat("#.####"),
            (DecimalFormat decimalFormat) -> {
                decimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ROOT));
            });
    private static final double[] INCREMENTS = {0.0625, 0.25, 0.5, 1.0};

    private static double currentIncrement = INCREMENTS[0];

    public ArmorStandPositionScreen(ArmorStandHolder holder, Inventory inventory, Component component, DataSyncHandler dataSyncHandler) {
        super(holder, inventory, component, dataSyncHandler);
    }

    @Override
    protected List<ArmorStandWidget> buildWidgets(ArmorStand armorStand) {
        // only move server-side to prevent rubber banding
        return buildWidgets(this,
                armorStand,
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
    public ArmorStandScreenType getScreenType() {
        return ArmorStandScreenType.POSITION;
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
            super(title, currentValue, newValue, -1.0);
        }

        @Override
        protected double getCurrentValue() {
            double value = Mth.inverseLerp(this.currentValue.getAsDouble(),
                    ScaleAttributeHelper.MIN_SCALE,
                    ScaleAttributeHelper.MAX_SCALE);
            return (Math.log10(value + LOGARITHMIC_SCALE_POW) + LOGARITHMIC_SCALE) / LOGARITHMIC_SCALE;
        }

        @Override
        protected void setNewValue(double newValue) {
            this.newValue.accept(getScaledValue(newValue));
        }

        @Override
        protected Component getTooltipComponent(double mouseValue) {
            mouseValue = getScaledValue(mouseValue);
            mouseValue = (int) (mouseValue * 100.0F) / 100.0F;
            mouseValue = Mth.clamp(mouseValue, ScaleAttributeHelper.MIN_SCALE, ScaleAttributeHelper.MAX_SCALE);
            return Component.literal(ArmorStandPoses.ROTATION_FORMAT.format(mouseValue));
        }

        @Override
        protected void applyClientValue(double newValue) {
            ScaleAttributeHelper.setScale(ArmorStandPositionScreen.this.getHolder().getArmorStand(),
                    getScaledValue(newValue));
        }

        public static float getScaledValue(double value) {
            value = Math.pow(10.0, value * LOGARITHMIC_SCALE - LOGARITHMIC_SCALE) - LOGARITHMIC_SCALE_POW;
            return (float) Mth.lerp(value, ScaleAttributeHelper.MIN_SCALE, ScaleAttributeHelper.MAX_SCALE);
        }
    }

    protected class RotationWidget extends AbstractArmorStandWidget {
        protected final DoubleSupplier currentValue;
        protected final Consumer<Float> newValue;
        private final double snapInterval;
        @Nullable
        private Runnable reset;

        public RotationWidget(Component title, DoubleSupplier currentValue, Consumer<Float> newValue) {
            this(title, currentValue, newValue, ArmorStandPoses.DEGREES_SNAP_INTERVAL);
        }

        public RotationWidget(Component title, DoubleSupplier currentValue, Consumer<Float> newValue, double snapInterval) {
            super(title);
            this.currentValue = currentValue;
            this.newValue = newValue;
            this.snapInterval = snapInterval;
        }

        protected double getCurrentValue() {
            return fromWrappedDegrees(this.currentValue.getAsDouble());
        }

        protected void setNewValue(double newValue) {
            this.newValue.accept(toWrappedDegrees(newValue));
        }

        protected Component getTooltipComponent(double mouseValue) {
            return Component.translatable(DEGREES_TRANSLATION_KEY,
                    ArmorStandPoses.ROTATION_FORMAT.format(toWrappedDegrees(mouseValue)));
        }

        protected static double fromWrappedDegrees(double value) {
            return (Mth.wrapDegrees(value) + 180.0) / 360.0;
        }

        protected static float toWrappedDegrees(double value) {
            return (float) Mth.wrapDegrees(value * 360.0 - 180.0);
        }

        protected void applyClientValue(double newValue) {
            // NO-OP
        }

        @Override
        public void reset() {
            if (this.reset != null) this.reset.run();
        }

        @Override
        public void init(int posX, int posY) {
            super.init(posX, posY);
            var sliderButton = ArmorStandPositionScreen.this.addRenderableWidget(new NewTextureSliderButton(posX + 76,
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
                    double mouseValue = ArmorStandPoses.snapValue((mouseX - this.getX()) / (double) this.getWidth(),
                            this.snapInterval);
                    this.setTooltip(Tooltip.create(RotationWidget.this.getTooltipComponent(mouseValue)));
                }

                @Override
                protected void updateMessage() {
                    // NO-OP
                }

                public void reset() {
                    this.value = RotationWidget.this.getCurrentValue();
                }

                @Override
                protected void applyValue() {
                    this.dirty = true;
                    RotationWidget.this.applyClientValue(this.value);
                }

                @Override
                public void onRelease(double mouseX, double mouseY) {
                    super.onRelease(mouseX, mouseY);
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
            });
            sliderButton.snapInterval = this.snapInterval;
            this.reset = sliderButton::reset;
            this.children.add(sliderButton);
            this.children.add(ArmorStandPositionScreen.this.addRenderableWidget(new SpritelessImageButton(posX + 174,
                    posY + 1,
                    20,
                    20,
                    236,
                    64,
                    getArmorStandWidgetsLocation(),
                    (Button button) -> {
                        ArmorStandPositionScreen.this.setActiveWidget(this);
                    })));
        }
    }

    private class PositionIncrementWidget extends AbstractArmorStandWidget {

        public PositionIncrementWidget() {
            super(Component.translatable(MOVE_BY_TRANSLATION_KEY));
        }

        @Override
        public void init(int posX, int posY) {
            super.init(posX, posY);
            for (int i = 0; i < INCREMENTS.length; i++) {
                double increment = INCREMENTS[i];
                AbstractWidget widget = ArmorStandPositionScreen.this.addRenderableWidget(new NewTextureButton(
                        posX + 76 + i * 24 + (i > 1 ? 1 : 0),
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
                this.children.add(widget);
                if (increment == currentIncrement) {
                    widget.active = false;
                }
            }
            this.children.add(ArmorStandPositionScreen.this.addRenderableWidget(new SpritelessImageButton(posX + 174,
                    posY + 1,
                    20,
                    20,
                    236,
                    64,
                    getArmorStandWidgetsLocation(),
                    (Button button) -> {
                        ArmorStandPositionScreen.this.setActiveWidget(this);
                    })));
        }

        private void setActiveIncrement(AbstractWidget source, double increment) {
            currentIncrement = increment;
            for (AbstractWidget widget : this.children) {
                widget.active = widget != source;
            }
        }

        @Override
        public boolean alwaysVisible(@Nullable ArmorStandWidget activeWidget) {
            return !(activeWidget instanceof RotationWidget);
        }
    }

    private class PositionComponentWidget extends AbstractArmorStandWidget {
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
            if (this.ticks > 0) this.ticks--;
            if (this.ticks == 0 && this.editBox != null) {
                this.ticks = 10;
                this.editBox.setValue(BLOCK_INCREMENT_FORMAT.format(this.getPositionValue()));
            }
        }

        @Override
        public void init(int posX, int posY) {
            super.init(posX, posY);
            this.editBox = new EditBox(ArmorStandPositionScreen.this.font,
                    posX + 77,
                    posY,
                    66,
                    22,
                    EntityType.ARMOR_STAND.getDescription());
            this.editBox.setMaxLength(50);
            this.editBox.setEditable(false);
            this.editBox.setTextColorUneditable(0XFFE0E0E0);
            this.editBox.setValue(BLOCK_INCREMENT_FORMAT.format(this.getPositionValue()));
            this.addChildren(this.editBox);
            AbstractWidget incrementButton = this.addChildren(ArmorStandPositionScreen.this.addRenderableWidget(new SpritelessImageButton(
                    posX + 149,
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
                    })));
            TooltipBuilder.create()
                    .setLines(() -> Collections.singletonList(Component.translatable(INCREMENT_TRANSLATION_KEY,
                            getPixelIncrementComponent(currentIncrement))))
                    .build(incrementButton);
            AbstractWidget decrementButton = this.addChildren(ArmorStandPositionScreen.this.addRenderableWidget(new SpritelessImageButton(
                    posX + 149,
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
                    })));
            TooltipBuilder.create()
                    .setLines(() -> Collections.singletonList(Component.translatable(DECREMENT_TRANSLATION_KEY,
                            getPixelIncrementComponent(currentIncrement))))
                    .build(decrementButton);
            this.addChildren(ArmorStandPositionScreen.this.addRenderableWidget(new SpritelessImageButton(posX + 174,
                    posY + 1,
                    20,
                    20,
                    236,
                    64,
                    getArmorStandWidgetsLocation(),
                    (Button button) -> {
                        ArmorStandPositionScreen.this.setActiveWidget(this);
                    })));
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
        public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            super.render(guiGraphics, mouseX, mouseY, partialTick);
            this.editBox.render(guiGraphics, mouseX, mouseY, partialTick);
        }

        @Override
        public boolean alwaysVisible(@Nullable ArmorStandWidget activeWidget) {
            return activeWidget instanceof PositionIncrementWidget || super.alwaysVisible(activeWidget);
        }
    }
}
