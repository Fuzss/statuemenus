package fuzs.statuemenus.api.v1.client.gui.components;

public interface UnboundedSliderButton {

    default boolean isDirty() {
        return false;
    }

    default void clearDirty() {
        // NO-OP
    }
}
