package fuzs.statuemenus.api.v1.client.gui.screens;

import fuzs.statuemenus.api.v1.network.client.data.DataSyncHandler;
import fuzs.statuemenus.api.v1.network.client.data.NetworkDataSyncHandler;
import fuzs.statuemenus.api.v1.world.inventory.StatueHolder;
import fuzs.statuemenus.api.v1.world.inventory.StatueMenu;
import fuzs.statuemenus.api.v1.world.inventory.data.StatueScreenType;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@FunctionalInterface
public interface StatueScreenFactory<T extends Screen & MenuAccess<StatueMenu> & StatueScreen> {
    Map<StatueScreenType, StatueScreenFactory<?>> FACTORIES = new HashMap<>();

    static <T extends Screen & MenuAccess<StatueMenu> & StatueScreen> T createScreenType(StatueScreenType screenType, StatueHolder holder, Inventory inventory, Component component, DataSyncHandler dataSyncHandler) {
        AbstractStatueScreen.lastScreenType = screenType;
        StatueScreenFactory<?> factory = FACTORIES.get(screenType);
        if (factory == null) {
            throw new IllegalStateException("No screen factory registered for armor stand screen type %s".formatted(
                    screenType));
        }
        T screen = (T) factory.create(holder, inventory, component, dataSyncHandler);
        if (screen.getScreenType() != screenType) {
            throw new IllegalStateException("Armor stand screen type mismatch: %s and %s".formatted(screen.getScreenType(),
                    screenType));
        }
        return screen;
    }

    static <T extends Screen & MenuAccess<StatueMenu> & StatueScreen> T createLastScreenType(StatueMenu menu, Inventory inventory, Component component) {
        return createLastScreenType(menu, inventory, component, new NetworkDataSyncHandler(menu));
    }

    static <T extends Screen & MenuAccess<StatueMenu> & StatueScreen> T createLastScreenType(StatueHolder holder, Inventory inventory, Component component, DataSyncHandler dataSyncHandler) {
        Collection<StatueScreenType> screenTypes = dataSyncHandler.getScreenTypes();
        Optional<StatueScreenType> lastScreenType = Optional.ofNullable(AbstractStatueScreen.lastScreenType)
                .filter(screenTypes::contains)
                .filter(dataSyncHandler::supportsScreenType);
        StatueScreenType screenType = lastScreenType.orElse(dataSyncHandler.getArmorStandHolder()
                .getStatueEntity()
                .getDefaultScreenType());
        return createScreenType(screenType, holder, inventory, component, dataSyncHandler);
    }

    static void register(StatueScreenType screenType, StatueScreenFactory<?> factory) {
        if (FACTORIES.putIfAbsent(screenType, factory) != null) {
            throw new IllegalStateException(
                    "Attempted to register duplicate screen factory for armor stand screen type %s".formatted(screenType));
        }
    }

    T create(StatueHolder holder, Inventory inventory, Component component, DataSyncHandler dataSyncHandler);
}
