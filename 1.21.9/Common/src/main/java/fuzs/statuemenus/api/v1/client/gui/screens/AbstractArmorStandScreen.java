package fuzs.statuemenus.api.v1.client.gui.screens;

import com.mojang.blaze3d.platform.InputConstants;
import fuzs.puzzleslib.api.client.gui.v2.components.SpritelessImageButton;
import fuzs.statuemenus.api.v1.client.gui.components.UnboundedSliderButton;
import fuzs.statuemenus.api.v1.network.client.data.DataSyncHandler;
import fuzs.statuemenus.api.v1.world.inventory.ArmorStandHolder;
import fuzs.statuemenus.api.v1.world.inventory.ArmorStandMenu;
import fuzs.statuemenus.api.v1.world.inventory.data.ArmorStandPose;
import fuzs.statuemenus.api.v1.world.inventory.data.ArmorStandScreenType;
import fuzs.statuemenus.impl.StatueMenus;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.Tickable;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class AbstractArmorStandScreen extends Screen implements MenuAccess<ArmorStandMenu>, ArmorStandScreen {
    public static final String VANILLA_TWEAKS_HOMEPAGE = "https://vanillatweaks.net/";
    public static final String CREDITS_TRANSLATION_KEY = StatueMenus.MOD_ID + ".screen.credits";
    private static final ResourceLocation ARMOR_STAND_BACKGROUND_LOCATION = StatueMenus.id(
            "textures/gui/container/statue/background.png");
    private static final ResourceLocation ARMOR_STAND_WIDGETS_LOCATION = StatueMenus.id(
            "textures/gui/container/statue/widgets.png");
    private static final ResourceLocation ARMOR_STAND_EQUIPMENT_LOCATION = StatueMenus.id(
            "textures/gui/container/statue/equipment.png");

    @Nullable
    static ArmorStandScreenType lastScreenType;
    protected final int imageWidth = 210;
    protected final int imageHeight = 188;
    protected final ArmorStandHolder holder;
    private final Inventory inventory;
    protected final DataSyncHandler dataSyncHandler;
    protected int leftPos;
    protected int topPos;
    protected int inventoryEntityX;
    protected int inventoryEntityY;
    protected boolean smallInventoryEntity;
    protected int mouseX;
    protected int mouseY;
    @Nullable
    private AbstractWidget closeButton;

    public AbstractArmorStandScreen(ArmorStandHolder holder, Inventory inventory, Component component, DataSyncHandler dataSyncHandler) {
        super(component);
        this.holder = holder;
        this.inventory = inventory;
        this.dataSyncHandler = dataSyncHandler;
    }

    public static ResourceLocation getArmorStandBackgroundLocation() {
        return ARMOR_STAND_BACKGROUND_LOCATION;
    }

    public static ResourceLocation getArmorStandWidgetsLocation() {
        return ARMOR_STAND_WIDGETS_LOCATION;
    }

    public static ResourceLocation getArmorStandEquipmentLocation() {
        return ARMOR_STAND_EQUIPMENT_LOCATION;
    }

    @Override
    public ArmorStandHolder getHolder() {
        return this.holder;
    }

    @Override
    public DataSyncHandler getDataSyncHandler() {
        return this.dataSyncHandler;
    }

    @Override
    public <T extends Screen & MenuAccess<ArmorStandMenu> & ArmorStandScreen> T createScreenType(ArmorStandScreenType screenType) {
        T screen = ArmorStandScreenFactory.createScreenType(screenType,
                this.holder,
                this.inventory,
                this.title,
                this.dataSyncHandler);
        screen.setMouseX(this.mouseX);
        screen.setMouseY(this.mouseY);
        return screen;
    }

    @Override
    public void setMouseX(int mouseX) {
        this.mouseX = mouseX;
    }

    @Override
    public void setMouseY(int mouseY) {
        this.mouseY = mouseY;
    }

    @Override
    public void tick() {
        this.dataSyncHandler.tick();
        for (GuiEventListener child : this.children()) {
            if (child instanceof Tickable button) {
                button.tick();
            }
        }
    }

    @Override
    protected void init() {
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
        if (this.withCloseButton()) {
            this.closeButton = this.addRenderableWidget(makeCloseButton(this,
                    this.leftPos,
                    this.imageWidth,
                    this.topPos));
        }
    }

    public static AbstractButton makeCloseButton(Screen screen, int leftPos, int imageWidth, int topPos) {
        return new SpritelessImageButton(leftPos + imageWidth - 15 - 8,
                topPos + 8,
                15,
                15,
                136,
                0,
                getArmorStandWidgetsLocation(),
                (Button button) -> {
                    screen.onClose();
                });
    }

    protected boolean withCloseButton() {
        return true;
    }

    protected boolean renderInventoryEntity() {
        return true;
    }

    protected boolean disableMenuRendering() {
        return false;
    }

    protected void toggleMenuRendering(boolean disableMenuRendering) {
        if (this.closeButton != null) {
            this.closeButton.visible = !disableMenuRendering;
        }
    }

    protected void addVanillaTweaksCreditsButton() {
        this.addRenderableWidget(new SpritelessImageButton(this.leftPos + 6,
                this.topPos + 6,
                20,
                20,
                136,
                64,
                20,
                getArmorStandWidgetsLocation(),
                256,
                256,
                (Button button) -> {
                    this.minecraft.setScreen(new ConfirmLinkScreen((boolean bl) -> {
                        if (bl) Util.getPlatform().openUri(VANILLA_TWEAKS_HOMEPAGE);
                        this.minecraft.setScreen(this);
                    }, VANILLA_TWEAKS_HOMEPAGE, true));
                },
                CommonComponents.EMPTY)).setTooltip(Tooltip.create(Component.translatable(CREDITS_TRANSLATION_KEY)));
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent mouseButtonEvent, boolean doubleClick) {
        if (mouseButtonEvent.button() == InputConstants.MOUSE_BUTTON_LEFT) {
            if (!this.disableMenuRendering() && handleTabClicked((int) mouseButtonEvent.x(),
                    (int) mouseButtonEvent.y(),
                    this.leftPos,
                    this.topPos,
                    this.imageHeight,
                    this,
                    this.dataSyncHandler.getScreenTypes())) {
                return true;
            }
        }

        return super.mouseClicked(mouseButtonEvent, doubleClick);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        if (!this.disableMenuRendering()) {
            if (this.renderInventoryEntity()) {
                int posX = this.leftPos + this.inventoryEntityX + 1;
                int posY = this.topPos + this.inventoryEntityY + 1;
                this.renderArmorStandInInventory(guiGraphics,
                        posX,
                        posY,
                        posX + this.getInventoryEntityScissorWidth(this.smallInventoryEntity),
                        posY + this.getInventoryEntityScissorHeight(this.smallInventoryEntity),
                        this.getInventoryEntityScale(this.smallInventoryEntity),
                        posX + this.getInventoryEntityScissorWidth(this.smallInventoryEntity) / 2,
                        posY + this.getInventoryEntityScissorHeight(this.smallInventoryEntity) / 2,
                        partialTick);
            }
            findHoveredTab(this.leftPos,
                    this.topPos,
                    this.imageHeight,
                    mouseX,
                    mouseY,
                    this.dataSyncHandler.getScreenTypes()).ifPresent((ArmorStandScreenType hoveredTab) -> {
                guiGraphics.setTooltipForNextFrame(this.font,
                        Component.translatable(hoveredTab.getTranslationKey()),
                        mouseX,
                        mouseY);
            });
        }
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    @Override
    public void renderArmorStandInInventory(GuiGraphics guiGraphics, int x1, int y1, int x2, int y2, int scale, float mouseX, float mouseY, float partialTick) {
        ArmorStand armorStand = this.holder.getArmorStand();
        ArmorStandPose pose = this.getPoseOverride();
        ArmorStandPose originalPose;
        if (pose != null) {
            originalPose = ArmorStandPose.fromEntity(armorStand);
            pose.applyToEntity(armorStand);
        } else {
            originalPose = null;
        }
        ArmorStandScreen.super.renderArmorStandInInventory(guiGraphics,
                x1,
                y1,
                x2,
                y2,
                scale,
                mouseX,
                mouseY,
                partialTick);
        if (originalPose != null) {
            originalPose.applyToEntity(armorStand);
        }
    }

    protected @Nullable ArmorStandPose getPoseOverride() {
        return null;
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (!this.disableMenuRendering()) {
            this.renderTransparentBackground(guiGraphics);
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                    getArmorStandBackgroundLocation(),
                    this.leftPos,
                    this.topPos,
                    0,
                    0,
                    this.imageWidth,
                    this.imageHeight,
                    256,
                    256);
            drawTabs(guiGraphics,
                    this.leftPos,
                    this.topPos,
                    this.imageHeight,
                    this,
                    this.dataSyncHandler.getScreenTypes());
            if (this.renderInventoryEntity()) {
                guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                        getArmorStandWidgetsLocation(),
                        this.leftPos + this.inventoryEntityX,
                        this.topPos + this.inventoryEntityY,
                        this.smallInventoryEntity ? 200 : 0,
                        this.smallInventoryEntity ? 184 : 0,
                        this.getInventoryEntityBackgroundWidth(),
                        this.getInventoryEntityBackgroundHeight(),
                        256,
                        256);
            }
        }
    }

    protected int getInventoryEntityBackgroundWidth() {
        return this.getInventoryEntityScissorWidth(this.smallInventoryEntity) + 2;
    }

    protected int getInventoryEntityBackgroundHeight() {
        return this.getInventoryEntityScissorHeight(this.smallInventoryEntity) + 2;
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent mouseButtonEvent) {
        // make sure value is sent to server when mouse is released outside of slider widget, but when the slider value has been changed
        boolean mouseReleased = false;
        for (GuiEventListener child : this.children()) {
            if (child instanceof UnboundedSliderButton sliderButton) {
                if (sliderButton.isDirty()) {
                    mouseReleased |= child.mouseReleased(mouseButtonEvent);
                }
            }
        }

        return mouseReleased || super.mouseReleased(mouseButtonEvent);
    }

    @Override
    public boolean keyPressed(KeyEvent keyEvent) {
        if (super.keyPressed(keyEvent)) {
            return true;
        } else if (this.minecraft.options.keyInventory.matches(keyEvent)) {
            this.onClose();
            return true;
        } else {
            return handleHotbarKeyPressed(keyEvent, this, this.dataSyncHandler.getScreenTypes());
        }
    }

    public static <T extends Screen & ArmorStandScreen> boolean handleHotbarKeyPressed(KeyEvent keyEvent, T screen, ArmorStandScreenType[] tabs) {
        for (int i = 0; i < Math.min(tabs.length, 9); ++i) {
            if (screen.minecraft.options.keyHotbarSlots[i].matches(keyEvent)) {
                if (openTabScreen(screen, tabs[i], true)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (super.mouseScrolled(mouseX, mouseY, scrollX, scrollY)) {
            return true;
        } else {
            return handleMouseScrolled((int) mouseX,
                    (int) mouseY,
                    scrollY,
                    this.leftPos,
                    this.topPos,
                    this.imageHeight,
                    this,
                    this.dataSyncHandler.getScreenTypes());
        }
    }

    public static <T extends Screen & ArmorStandScreen> boolean handleMouseScrolled(int mouseX, int mouseY, double delta, int leftPos, int topPos, int imageHeight, T screen, ArmorStandScreenType[] tabs) {
        delta = Math.signum(delta);
        if (delta != 0.0) {
            Optional<ArmorStandScreenType> optional = findHoveredTab(leftPos,
                    topPos,
                    imageHeight,
                    mouseX,
                    mouseY,
                    tabs);
            if (optional.isPresent()) {
                ArmorStandScreenType screenType = cycleTabs(screen.getScreenType(), tabs, delta > 0.0);
                return openTabScreen(screen, screenType, false);
            }
        }
        return false;
    }

    public static <T extends Screen & ArmorStandScreen> boolean handleTabClicked(int mouseX, int mouseY, int leftPos, int topPos, int imageHeight, T screen, ArmorStandScreenType[] tabs) {
        Optional<ArmorStandScreenType> hoveredTab = findHoveredTab(leftPos, topPos, imageHeight, mouseX, mouseY, tabs);
        return hoveredTab.filter((ArmorStandScreenType type) -> openTabScreen(screen, type, true)).isPresent();
    }

    private static <T extends Screen & ArmorStandScreen> boolean openTabScreen(T screen, ArmorStandScreenType screenType, boolean clickSound) {
        if (screenType != screen.getScreenType()) {
            if (clickSound) {
                SimpleSoundInstance sound = SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F);
                screen.minecraft.getSoundManager().play(sound);
            }
            screen.minecraft.setScreen(screen.createScreenType(screenType));
            return true;
        } else {
            return false;
        }
    }

    private static ArmorStandScreenType cycleTabs(ArmorStandScreenType currentScreenType, ArmorStandScreenType[] screenTypes, boolean backwards) {
        int index = ArrayUtils.indexOf(screenTypes, currentScreenType);
        return screenTypes[((backwards ? --index : ++index) % screenTypes.length + screenTypes.length)
                % screenTypes.length];
    }

    public static <T extends Screen & ArmorStandScreen> void drawTabs(GuiGraphics guiGraphics, int leftPos, int topPos, int imageHeight, T screen, ArmorStandScreenType[] tabs) {
        int tabsStartY = getTabsStartY(imageHeight, tabs.length);
        for (int i = 0; i < tabs.length; i++) {
            ArmorStandScreenType tabType = tabs[i];
            int tabX = leftPos - 32;
            int tabY = topPos + tabsStartY + 27 * i;
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                    getArmorStandBackgroundLocation(),
                    tabX,
                    tabY,
                    tabY <= topPos ? 36 : tabY >= topPos + imageHeight - 36 ? 72 : 0,
                    188 + (tabType == screen.getScreenType() ? 0 : 26),
                    36,
                    26,
                    256,
                    256);
            guiGraphics.renderItem(tabType.icon(), tabX + 10, tabY + 5);
        }
    }

    public static Optional<ArmorStandScreenType> findHoveredTab(int leftPos, int topPos, int imageHeight, int mouseX, int mouseY, ArmorStandScreenType[] tabs) {
        int tabsStartY = getTabsStartY(imageHeight, tabs.length);
        for (int i = 0; i < tabs.length; i++) {
            int tabX = leftPos - 32;
            int tabY = topPos + tabsStartY + 27 * i;
            if (mouseX > tabX && mouseX <= tabX + 32 && mouseY > tabY && mouseY <= tabY + 26) {
                return Optional.of(tabs[i]);
            }
        }
        return Optional.empty();
    }

    private static int getTabsStartY(int imageHeight, int tabsCount) {
        int tabsHeight = tabsCount * 26 + (tabsCount - 1);
        return (imageHeight - tabsHeight) / 2;
    }

    @Override
    public ArmorStandMenu getMenu() {
        if (this.holder instanceof ArmorStandMenu menu) {
            return menu;
        } else {
            // prevent ClassCastException in case some mod like OwoLib calls this
            Inventory inventory1 = this.minecraft.player.getInventory();
            ArmorStand armorStand = this.getHolder().getArmorStand();
            return new ArmorStandMenu(null, 0, inventory1, armorStand, this.getHolder().getDataProvider());
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void removed() {
        for (GuiEventListener child : this.children()) {
            if (child instanceof UnboundedSliderButton sliderButton) {
                sliderButton.clearDirty();
            }
        }
    }

    @Override
    public void onClose() {
        if (this.holder instanceof AbstractContainerMenu) {
            this.minecraft.player.closeContainer();
        }
        super.onClose();
    }
}
