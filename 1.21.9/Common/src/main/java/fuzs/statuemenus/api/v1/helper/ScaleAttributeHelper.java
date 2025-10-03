package fuzs.statuemenus.api.v1.helper;

import fuzs.statuemenus.impl.StatueMenus;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public final class ScaleAttributeHelper {
    public static final float DEFAULT_SCALE = (float) Attributes.SCALE.value().getDefaultValue();
    public static final float MIN_SCALE = (float) ((RangedAttribute) Attributes.SCALE.value()).getMinValue();
    public static final float MAX_SCALE = (float) ((RangedAttribute) Attributes.SCALE.value()).getMaxValue();
    public static final ResourceLocation SCALE_BONUS_ID = StatueMenus.id("scale_bonus");

    private ScaleAttributeHelper() {
        // NO-OP
    }

    public static void resetScale(LivingEntity livingEntity) {
        setScale(livingEntity, DEFAULT_SCALE);
    }

    public static void setScale(LivingEntity livingEntity, float scaleValue) {
        AttributeInstance attribute = livingEntity.getAttribute(Attributes.SCALE);
        if (scaleValue == DEFAULT_SCALE) {
            attribute.removeModifier(SCALE_BONUS_ID);
        } else {
            attribute.addOrReplacePermanentModifier(new AttributeModifier(SCALE_BONUS_ID,
                    scaleValue - DEFAULT_SCALE,
                    AttributeModifier.Operation.ADD_VALUE));
        }
    }
}
