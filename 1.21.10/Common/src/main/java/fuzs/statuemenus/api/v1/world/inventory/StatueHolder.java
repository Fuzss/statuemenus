package fuzs.statuemenus.api.v1.world.inventory;

import fuzs.statuemenus.api.v1.world.entity.decoration.ArmorStandStatue;
import fuzs.statuemenus.api.v1.world.entity.decoration.StatueEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;

public interface StatueHolder {

    LivingEntity getEntity();

    StatueMenu getMenu();

    default StatueEntity getStatueEntity() {
        if (this.getEntity() instanceof StatueEntity statueEntity) {
            return statueEntity;
        } else if (this.getEntity() instanceof ArmorStand armorStand) {
            return new ArmorStandStatue(armorStand);
        } else {
            throw new IllegalStateException("Illegal statue entity: " + this.getEntity().getType());
        }
    }
}
