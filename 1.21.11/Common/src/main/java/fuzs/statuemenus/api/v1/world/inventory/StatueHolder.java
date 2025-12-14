package fuzs.statuemenus.api.v1.world.inventory;

import fuzs.statuemenus.api.v1.world.entity.decoration.StatueEntity;
import net.minecraft.world.entity.LivingEntity;

public interface StatueHolder {

    LivingEntity getEntity();

    StatueEntity getStatueEntity();

    StatueMenu getMenu();

    static StatueHolder simple(LivingEntity livingEntity, StatueEntity statueEntity) {
        return new StatueHolder() {
            @Override
            public LivingEntity getEntity() {
                return livingEntity;
            }

            @Override
            public StatueEntity getStatueEntity() {
                return statueEntity;
            }

            @Override
            public StatueMenu getMenu() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
