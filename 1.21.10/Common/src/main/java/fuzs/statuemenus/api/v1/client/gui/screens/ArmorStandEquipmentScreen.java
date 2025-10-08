package fuzs.statuemenus.api.v1.client.gui.screens;

import com.mojang.blaze3d.platform.InputConstants;
import fuzs.statuemenus.api.v1.network.client.data.DataSyncHandler;
import fuzs.statuemenus.api.v1.world.inventory.ArmorStandHolder;
import fuzs.statuemenus.api.v1.world.inventory.ArmorStandMenu;
import fuzs.statuemenus.api.v1.world.inventory.data.ArmorStandScreenType;
import fuzs.statuemenus.impl.world.inventory.ArmorStandSlot;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class ArmorStandEquipmentScreen extends AbstractContainerScreen<ArmorStandMenu> implements ArmorStandScreen {
    private final Inventory inventory;
    private final DataSyncHandler dataSyncHandler;
    private int mouseX;
    private int mouseY;

    public ArmorStandEquipmentScreen(ArmorStandHolder holder, Inventory inventory, Component component, DataSyncHandler dataSyncHandler) {
        super((ArmorStandMenu) holder, inventory, component);
        this.inventory = inventory;
        this.dataSyncHandler = dataSyncHandler;
        this.imageWidth = 210;
        this.imageHeight = 188;
    }

    @Override
    public ArmorStandHolder getHolder() {
        return this.menu;
    }

    @Override
    public DataSyncHandler getDataSyncHandler() {
        return this.dataSyncHandler;
    }

    @Override
    public <T extends Screen & MenuAccess<ArmorStandMenu> & ArmorStandScreen> T createScreenType(ArmorStandScreenType screenType) {
        T screen = ArmorStandScreenFactory.createScreenType(screenType,
                this.menu,
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
    protected void containerTick() {
        this.dataSyncHandler.tick();
    }

    @Override
    protected void init() {
        super.init();
        this.addRenderableWidget(AbstractArmorStandScreen.makeCloseButton(this,
                this.leftPos,
                this.imageWidth,
                this.topPos));
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent mouseButtonEvent, boolean doubleClick) {
        if (mouseButtonEvent.button() == InputConstants.MOUSE_BUTTON_LEFT) {
            if (AbstractArmorStandScreen.handleTabClicked((int) mouseButtonEvent.x(),
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
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (super.mouseScrolled(mouseX, mouseY, scrollX, scrollY)) {
            return true;
        } else {
            return AbstractArmorStandScreen.handleMouseScrolled((int) mouseX,
                    (int) mouseY,
                    scrollY,
                    this.leftPos,
                    this.topPos,
                    this.imageHeight,
                    this,
                    this.dataSyncHandler.getScreenTypes());
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
        if (this.menu.getCarried().isEmpty()) {
            AbstractArmorStandScreen.findHoveredTab(this.leftPos,
                    this.topPos,
                    this.imageHeight,
                    mouseX,
                    mouseY,
                    this.dataSyncHandler.getScreenTypes()).ifPresent(hoveredTab -> {
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
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                AbstractArmorStandScreen.getArmorStandEquipmentLocation(),
                this.leftPos,
                this.topPos,
                0,
                0,
                this.imageWidth,
                this.imageHeight,
                256,
                256);
        for (int i = 0, j = 0; i < ArmorStandMenu.SLOT_IDS.length; ++i) {
            EquipmentSlot equipmentSlot = ArmorStandMenu.SLOT_IDS[i];
            if (equipmentSlot != null) {
                Slot slot = this.menu.slots.get(j++);
                if (slot.isActive() && isSlotRestricted(this.menu.getArmorStand(), equipmentSlot)) {
                    guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                            AbstractArmorStandScreen.getArmorStandEquipmentLocation(),
                            this.leftPos + slot.x - 1,
                            this.topPos + slot.y - 1,
                            210,
                            0,
                            18,
                            18,
                            256,
                            256);
                }
            }
        }
        AbstractArmorStandScreen.drawTabs(guiGraphics,
                this.leftPos,
                this.topPos,
                this.imageHeight,
                this,
                this.dataSyncHandler.getScreenTypes());
        int posX = this.leftPos + 81;
        int posY = this.topPos + 20;
        this.renderArmorStandInInventory(guiGraphics,
                posX,
                posY,
                posX + this.getInventoryEntityScissorWidth(true),
                posY + this.getInventoryEntityScissorHeight(true),
                this.getInventoryEntityScale(true),
                this.mouseX,
                this.mouseY,
                partialTick);
    }

    private static boolean isSlotRestricted(ArmorStand armorStand, EquipmentSlot equipmentSlot) {
        return ArmorStandSlot.isSlotDisabled(armorStand, equipmentSlot, 0) || ArmorStandSlot.isSlotDisabled(armorStand,
                equipmentSlot,
                ArmorStand.DISABLE_TAKING_OFFSET) || ArmorStandSlot.isSlotDisabled(armorStand,
                equipmentSlot,
                ArmorStand.DISABLE_PUTTING_OFFSET);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        // NO-OP
    }

    @Override
    public boolean keyPressed(KeyEvent keyEvent) {
        ArmorStandScreenType[] tabs = this.dataSyncHandler.getScreenTypes();
        if (this.menu.getCarried().isEmpty() && this.hoveredSlot == null) {
            AbstractArmorStandScreen.handleHotbarKeyPressed(keyEvent, this, tabs);
        }

        return super.keyPressed(keyEvent);
    }

    @Override
    public ArmorStandScreenType getScreenType() {
        return ArmorStandScreenType.EQUIPMENT;
    }
}
