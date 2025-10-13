package fuzs.statuemenus.api.v1.client.gui.screens;

import fuzs.puzzleslib.api.client.gui.v2.components.SpritelessImageButton;
import fuzs.statuemenus.api.v1.client.gui.components.BoxedSliderButton;
import fuzs.statuemenus.api.v1.client.gui.components.FlatTickButton;
import fuzs.statuemenus.api.v1.client.gui.components.LiveSliderButton;
import fuzs.statuemenus.api.v1.client.gui.components.VerticalSliderButton;
import fuzs.statuemenus.api.v1.network.client.data.DataSyncHandler;
import fuzs.statuemenus.api.v1.world.inventory.StatueHolder;
import fuzs.statuemenus.api.v1.world.inventory.data.PosePartMutator;
import fuzs.statuemenus.api.v1.world.inventory.data.StatuePose;
import fuzs.statuemenus.api.v1.world.inventory.data.StatueScreenType;
import fuzs.statuemenus.impl.client.gui.components.TooltipFactories;
import net.minecraft.Util;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.core.Rotations;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class StatueRotationsScreen extends AbstractStatueScreen {
    public static final String TIP_TRANSLATION_KEY = StatueScreenType.ROTATIONS.id().toLanguageKey("screen", "tip");
    public static final String UNLIMITED_TRANSLATION_KEY = StatueScreenType.ROTATIONS.id()
            .toLanguageKey("screen", "unlimited");
    public static final String LIMITED_TRANSLATION_KEY = StatueScreenType.ROTATIONS.id()
            .toLanguageKey("screen", "limited");
    public static final String RESET_TRANSLATION_KEY = StatueScreenType.ROTATIONS.id().toLanguageKey("screen", "reset");
    public static final String RANDOMIZE_TRANSLATION_KEY = StatueScreenType.ROTATIONS.id()
            .toLanguageKey("screen", "randomize");
    public static final String PASTE_TRANSLATION_KEY = StatueScreenType.ROTATIONS.id().toLanguageKey("screen", "paste");
    public static final String COPY_TRANSLATION_KEY = StatueScreenType.ROTATIONS.id().toLanguageKey("screen", "copy");
    public static final String MIRROR_TRANSLATION_KEY = StatueScreenType.ROTATIONS.id()
            .toLanguageKey("screen", "mirror");

    private static boolean clampRotations = true;
    @Nullable
    private static StatuePose clipboard;
    private final AbstractWidget[] lockButtons = new AbstractWidget[2];
    private StatuePose currentPose;

    public StatueRotationsScreen(StatueHolder holder, Inventory inventory, Component component, DataSyncHandler dataSyncHandler) {
        super(holder, inventory, component, dataSyncHandler);
        this.inventoryEntityX = 80;
        this.inventoryEntityY = 58;
        this.smallInventoryEntity = true;
        this.currentPose = StatuePose.fromEntity(holder.getStatueEntity());
    }

    protected List<PosePartMutator> getPosePartMutators() {
        return PosePartMutator.TYPES;
    }

    protected StatuePose setupRandomPose(StatuePose statuePose) {
        return statuePose.withBodyPose(new Rotations(0.0F, 0.0F, 0.0F));
    }

    @Override
    protected void init() {
        super.init();
        this.lockButtons[0] = Util.make(this.addRenderableWidget(new SpritelessImageButton(this.leftPos + 83,
                this.topPos + 10,
                20,
                20,
                156,
                124,
                20,
                getArmorStandWidgetsLocation(),
                256,
                256,
                (Button button) -> {
                    clampRotations = true;
                    this.toggleLockButtons();
                    this.refreshLiveButtons();
                },
                CommonComponents.EMPTY)), (SpritelessImageButton widget) -> {
            widget.setTooltip(Tooltip.create(Component.translatable(UNLIMITED_TRANSLATION_KEY)));
        });
        this.lockButtons[1] = Util.make(this.addRenderableWidget(new SpritelessImageButton(this.leftPos + 83,
                this.topPos + 10,
                20,
                20,
                136,
                124,
                20,
                getArmorStandWidgetsLocation(),
                256,
                256,
                (Button button) -> {
                    clampRotations = false;
                    this.toggleLockButtons();
                    this.refreshLiveButtons();
                },
                CommonComponents.EMPTY)), (SpritelessImageButton widget) -> {
            widget.setTooltip(Tooltip.create(Component.translatable(LIMITED_TRANSLATION_KEY)));
        });
        Tooltip tooltip = this.getTipComponent();
        if (tooltip != null) {
            this.addRenderableWidget(new SpritelessImageButton(this.leftPos + 107,
                    this.topPos + 10,
                    20,
                    20,
                    136,
                    64,
                    20,
                    getArmorStandWidgetsLocation(),
                    256,
                    256,
                    Function.identity()::apply) {

                @Nullable
                @Override
                public ComponentPath nextFocusPath(FocusNavigationEvent event) {
                    return null;
                }

                @Override
                public boolean mouseClicked(MouseButtonEvent mouseButtonEvent, boolean doubleClick) {
                    return false;
                }
            }).setTooltip(tooltip);
        }
        this.addRenderableWidget(new FlatTickButton(this.leftPos + 83,
                this.topPos + 34,
                20,
                20,
                192,
                124,
                getArmorStandWidgetsLocation(),
                (Button button) -> {
                    StatuePose statuePose = StatuePose.randomize(this.getPosePartMutators(), clampRotations);
                    this.setCurrentPose(this.setupRandomPose(statuePose));
                })).setTooltip(Tooltip.create(Component.translatable(RANDOMIZE_TRANSLATION_KEY)));
        this.addRenderableWidget(new FlatTickButton(this.leftPos + 107,
                this.topPos + 34,
                20,
                20,
                240,
                124,
                getArmorStandWidgetsLocation(),
                (Button button) -> {
                    this.setCurrentPose(StatuePose.EMPTY);
                })).setTooltip(Tooltip.create(Component.translatable(RESET_TRANSLATION_KEY)));
        this.addRenderableWidget(new FlatTickButton(this.leftPos + 83,
                this.topPos + 134,
                44,
                20,
                179,
                0,
                getArmorStandWidgetsLocation(),
                (Button button) -> {
                    this.setCurrentPose(this.currentPose.mirror());
                })).setTooltip(Tooltip.create(Component.translatable(MIRROR_TRANSLATION_KEY)));
        AbstractWidget[] pasteButton = new AbstractWidget[1];
        this.addRenderableWidget(new FlatTickButton(this.leftPos + 83,
                this.topPos + 158,
                20,
                20,
                208,
                124,
                getArmorStandWidgetsLocation(),
                (Button button) -> {
                    clipboard = this.currentPose;
                    pasteButton[0].active = true;
                })).setTooltip(Tooltip.create(Component.translatable(COPY_TRANSLATION_KEY)));
        pasteButton[0] = Util.make(this.addRenderableWidget(new FlatTickButton(this.leftPos + 107,
                this.topPos + 158,
                20,
                20,
                224,
                124,
                getArmorStandWidgetsLocation(),
                (Button button) -> {
                    if (clipboard != null) this.setCurrentPose(clipboard);
                })), (FlatTickButton widget) -> {
            widget.setTooltip(Tooltip.create(Component.translatable(PASTE_TRANSLATION_KEY)));
            widget.active = clipboard != null;
        });
        List<PosePartMutator> posePartMutators = this.getPosePartMutators();
        for (int i = 0; i < posePartMutators.size(); i++) {
            PosePartMutator mutator = posePartMutators.get(i);
            boolean isLeft = i % 2 == 0;
            this.addRenderableWidget(new BoxedSliderButton(this.leftPos + 23 + i % 2 * 110,
                    this.topPos + 7 + i / 2 * 60,
                    () -> mutator.getNormalizedRotationsAtAxis(1, this.currentPose, clampRotations),
                    () -> mutator.getNormalizedRotationsAtAxis(0, this.currentPose, clampRotations)) {
                private boolean dirty;

                {
                    this.active = StatueRotationsScreen.this.isPosePartMutatorActive(mutator,
                            StatueRotationsScreen.this.holder.getEntity());
                    TooltipFactories.applyRotationsTooltip(this, isLeft, () -> {
                        List<Component> lines = new ArrayList<>();
                        lines.add(Component.translatable(mutator.getTranslationKey()));
                        lines.add(mutator.getAxisComponent(StatueRotationsScreen.this.currentPose, 0));
                        lines.add(mutator.getAxisComponent(StatueRotationsScreen.this.currentPose, 1));
                        return lines;
                    });
                }

                @Override
                protected void applyValue() {
                    this.dirty = true;
                    StatueRotationsScreen.this.currentPose = mutator.setRotationsAtAxis(1,
                            StatueRotationsScreen.this.currentPose,
                            this.horizontalValue,
                            clampRotations);
                    StatueRotationsScreen.this.currentPose = mutator.setRotationsAtAxis(0,
                            StatueRotationsScreen.this.currentPose,
                            this.verticalValue,
                            clampRotations);
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
                    if (this.isDirty()) {
                        this.dirty = false;
                        StatueRotationsScreen.this.setCurrentPose(StatueRotationsScreen.this.currentPose);
                    }
                }
            });
            this.addRenderableWidget(new VerticalSliderButton(this.leftPos + 6 + i % 2 * 183,
                    this.topPos + 7 + i / 2 * 60,
                    () -> mutator.getNormalizedRotationsAtAxis(2, this.currentPose, clampRotations)) {
                private boolean dirty;

                {
                    this.active = StatueRotationsScreen.this.isPosePartMutatorActive(mutator,
                            StatueRotationsScreen.this.holder.getEntity());
                    TooltipFactories.applyRotationsTooltip(this, isLeft, () -> {
                        List<Component> lines = new ArrayList<>();
                        lines.add(Component.translatable(mutator.getTranslationKey()));
                        lines.add(mutator.getAxisComponent(StatueRotationsScreen.this.currentPose, 2));
                        return lines;
                    });
                }

                @Override
                protected void applyValue() {
                    this.dirty = true;
                    StatueRotationsScreen.this.currentPose = mutator.setRotationsAtAxis(2,
                            StatueRotationsScreen.this.currentPose,
                            this.value,
                            clampRotations);
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
                    if (this.isDirty()) {
                        this.dirty = false;
                        StatueRotationsScreen.this.setCurrentPose(StatueRotationsScreen.this.currentPose);
                    }
                }
            });
            this.toggleLockButtons();
        }
    }

    @Nullable
    private Tooltip getTipComponent() {
        List<Component> components = new ArrayList<>();
        for (int i = 1; Language.getInstance().has(TIP_TRANSLATION_KEY + i); i++) {
            components.add(Component.translatable(TIP_TRANSLATION_KEY + i));
        }

        Collections.shuffle(components);
        return components.stream().findAny().map(Tooltip::create).orElse(null);
    }

    private void toggleLockButtons() {
        this.lockButtons[0].visible = !clampRotations;
        this.lockButtons[1].visible = clampRotations;
    }

    @Override
    protected StatuePose getPoseOverride() {
        return this.currentPose;
    }

    @Override
    protected boolean withCloseButton() {
        return false;
    }

    @Override
    public StatueScreenType getScreenType() {
        return StatueScreenType.ROTATIONS;
    }

    private void setCurrentPose(StatuePose pose) {
        this.dataSyncHandler.sendPose(pose);
        StatuePose lastSyncedPose = this.dataSyncHandler.getLastSyncedPose();
        this.currentPose = lastSyncedPose != null ? lastSyncedPose : pose;
        this.refreshLiveButtons();
    }

    private void refreshLiveButtons() {
        for (GuiEventListener child : this.children()) {
            if (child instanceof LiveSliderButton button) {
                button.refreshValues();
            }
        }
    }

    protected boolean isPosePartMutatorActive(PosePartMutator posePartMutator, LivingEntity livingEntity) {
        return true;
    }
}
