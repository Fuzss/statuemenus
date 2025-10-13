package fuzs.statuemenus.api.v1.client.gui.screens;

import com.mojang.blaze3d.platform.InputConstants;
import fuzs.statuemenus.api.v1.network.client.data.DataSyncHandler;
import fuzs.statuemenus.api.v1.world.inventory.StatueHolder;
import fuzs.statuemenus.api.v1.world.inventory.StatueMenu;
import fuzs.statuemenus.api.v1.world.inventory.data.StatueScreenType;
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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

import java.util.List;

public class StatueEquipmentScreen extends AbstractContainerScreen<StatueMenu> implements StatueScreen {
    private final Inventory inventory;
    private final DataSyncHandler dataSyncHandler;
    private int mouseX;
    private int mouseY;

    public StatueEquipmentScreen(StatueHolder holder, Inventory inventory, Component component, DataSyncHandler dataSyncHandler) {
        super(holder.getMenu(), inventory, component);
        this.inventory = inventory;
        this.dataSyncHandler = dataSyncHandler;
        this.imageWidth = 210;
        this.imageHeight = 188;
    }

    @Override
    public StatueHolder getHolder() {
        return this.menu;
    }

    @Override
    public DataSyncHandler getDataSyncHandler() {
        return this.dataSyncHandler;
    }

    @Override
    public <T extends Screen & MenuAccess<StatueMenu> & StatueScreen> T createScreenType(StatueScreenType screenType) {
        T screen = StatueScreenFactory.createScreenType(screenType,
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
        this.addRenderableWidget(AbstractStatueScreen.makeCloseButton(this,
                this.leftPos,
                this.imageWidth,
                this.topPos));
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent mouseButtonEvent, boolean doubleClick) {
        if (mouseButtonEvent.button() == InputConstants.MOUSE_BUTTON_LEFT) {
            if (AbstractStatueScreen.handleTabClicked((int) mouseButtonEvent.x(),
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
            return AbstractStatueScreen.handleMouseScrolled((int) mouseX,
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
            AbstractStatueScreen.findHoveredTab(this.leftPos,
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
                AbstractStatueScreen.getArmorStandEquipmentLocation(),
                this.leftPos,
                this.topPos,
                0,
                0,
                this.imageWidth,
                this.imageHeight,
                256,
                256);
        for (int i = 0, j = 0; i < StatueMenu.SLOT_IDS.length; ++i) {
            EquipmentSlot equipmentSlot = StatueMenu.SLOT_IDS[i];
            if (equipmentSlot != null) {
                Slot slot = this.menu.slots.get(j++);
                if (slot.isActive() && this.isSlotRestricted(this.menu.getEntity(), equipmentSlot)) {
                    guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                            AbstractStatueScreen.getArmorStandEquipmentLocation(),
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

        AbstractStatueScreen.drawTabs(guiGraphics,
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

    protected boolean isSlotRestricted(LivingEntity livingEntity, EquipmentSlot equipmentSlot) {
        if (livingEntity instanceof ArmorStand armorStand) {
            return ArmorStandSlot.isSlotDisabled(armorStand, equipmentSlot, ArmorStandSlot.DISABLE_ALL_OFFSET)
                    || ArmorStandSlot.isSlotDisabled(armorStand, equipmentSlot, ArmorStandSlot.DISABLE_TAKING_OFFSET)
                    || ArmorStandSlot.isSlotDisabled(armorStand, equipmentSlot, ArmorStandSlot.DISABLE_PUTTING_OFFSET);
        } else {
            return false;
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        // NO-OP
    }

    @Override
    public boolean keyPressed(KeyEvent keyEvent) {
        if (this.menu.getCarried().isEmpty() && this.hoveredSlot == null) {
            List<StatueScreenType> tabs = this.dataSyncHandler.getScreenTypes();
            AbstractStatueScreen.handleHotbarKeyPressed(keyEvent, this, tabs);
        }

        return super.keyPressed(keyEvent);
    }

    @Override
    public StatueScreenType getScreenType() {
        return StatueScreenType.EQUIPMENT;
    }
}
