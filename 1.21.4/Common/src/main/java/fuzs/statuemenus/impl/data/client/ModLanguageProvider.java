package fuzs.statuemenus.impl.data.client;

import fuzs.puzzleslib.api.client.data.v2.AbstractLanguageProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.statuemenus.api.v1.client.gui.screens.*;
import fuzs.statuemenus.api.v1.helper.ArmorStandInteractHelper;
import fuzs.statuemenus.api.v1.world.inventory.data.*;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addTranslations(TranslationBuilder builder) {
        builder.add(ArmorStandInteractHelper.OPEN_SCREEN_TRANSLATION_KEY,
                "Use %s + %s with an empty hand to open configuration screen.");
        builder.add(ArmorStandPosesScreen.POSE_SOURCE_TRANSLATION_KEY, "By %s");
        builder.add(ArmorStandPose.ATHENA.getTranslationKey(), "Athena");
        builder.add(ArmorStandPose.BRANDISH.getTranslationKey(), "Brandish");
        builder.add(ArmorStandPose.CANCAN.getTranslationKey(), "Cancan");
        builder.add(ArmorStandPose.DEFAULT.getTranslationKey(), "Default");
        builder.add(ArmorStandPose.ENTERTAIN.getTranslationKey(), "Entertain");
        builder.add(ArmorStandPose.HERO.getTranslationKey(), "Hero");
        builder.add(ArmorStandPose.HONOR.getTranslationKey(), "Honor");
        builder.add(ArmorStandPose.RIPOSTE.getTranslationKey(), "Riposte");
        builder.add(ArmorStandPose.SALUTE.getTranslationKey(), "Salute");
        builder.add(ArmorStandPose.SOLEMN.getTranslationKey(), "Solemn");
        builder.add(ArmorStandPose.ZOMBIE.getTranslationKey(), "Zombie");
        builder.add(ArmorStandPose.WALKING.getTranslationKey(), "Walking");
        builder.add(ArmorStandPose.RUNNING.getTranslationKey(), "Running");
        builder.add(ArmorStandPose.POINTING.getTranslationKey(), "Pointing");
        builder.add(ArmorStandPose.BLOCKING.getTranslationKey(), "Blocking");
        builder.add(ArmorStandPose.LUNGEING.getTranslationKey(), "Lungeing");
        builder.add(ArmorStandPose.WINNING.getTranslationKey(), "Winning");
        builder.add(ArmorStandPose.SITTING.getTranslationKey(), "Sitting");
        builder.add(ArmorStandPose.ARABESQUE.getTranslationKey(), "Arabesque");
        builder.add(ArmorStandPose.CUPID.getTranslationKey(), "Cupid");
        builder.add(ArmorStandPose.CONFIDENT.getTranslationKey(), "Confident");
        builder.add(ArmorStandPose.DEATH.getTranslationKey(), "Death");
        builder.add(ArmorStandPose.FACEPALM.getTranslationKey(), "Facepalm");
        builder.add(ArmorStandPose.LAZING.getTranslationKey(), "Lazing");
        builder.add(ArmorStandPose.CONFUSED.getTranslationKey(), "Confused");
        builder.add(ArmorStandPose.FORMAL.getTranslationKey(), "Formal");
        builder.add(ArmorStandPose.SAD.getTranslationKey(), "Sad");
        builder.add(ArmorStandPose.JOYOUS.getTranslationKey(), "Joyous");
        builder.add(ArmorStandPose.STARGAZING.getTranslationKey(), "Stargazing");
        builder.add(ArmorStandScreenType.EQUIPMENT.getTranslationKey(), "Equipment");
        builder.add(ArmorStandScreenType.ROTATIONS.getTranslationKey(), "Rotations");
        builder.add(ArmorStandScreenType.STYLE.getTranslationKey(), "Style");
        builder.add(ArmorStandScreenType.POSES.getTranslationKey(), "Poses");
        builder.add(ArmorStandScreenType.POSITION.getTranslationKey(), "Position");
        builder.add(ArmorStandStyleScreen.TEXT_BOX_TRANSLATION_KEY,
                "Set a name to display above the entity if enabled.");
        builder.add(ArmorStandStyleOptions.SHOW_ARMS.getTranslationKey(), "Show Arms");
        builder.add(ArmorStandStyleOptions.SHOW_ARMS.getDescriptionKey(),
                "Shows the statue's arms, so it may hold items in both hands.");
        builder.add(ArmorStandStyleOptions.SMALL.getTranslationKey(), "Small");
        builder.add(ArmorStandStyleOptions.SMALL.getDescriptionKey(),
                "Makes the statue half it's size like a baby mob.");
        builder.add(ArmorStandStyleOptions.INVISIBLE.getTranslationKey(), "Invisible");
        builder.add(ArmorStandStyleOptions.INVISIBLE.getDescriptionKey(),
                "Makes the statue itself invisible, but still shows all equipped items.");
        builder.add(ArmorStandStyleOptions.NO_BASE_PLATE.getTranslationKey(), "No Base Plate");
        builder.add(ArmorStandStyleOptions.NO_BASE_PLATE.getDescriptionKey(),
                "Hide the stone base plate at the statue's feet.");
        builder.add(ArmorStandStyleOptions.SHOW_NAME.getTranslationKey(), "Show Name");
        builder.add(ArmorStandStyleOptions.SHOW_NAME.getDescriptionKey(),
                "Render the statue's name tag above it's head.");
        builder.add(ArmorStandStyleOptions.NO_GRAVITY.getTranslationKey(), "No Gravity");
        builder.add(ArmorStandStyleOptions.NO_GRAVITY.getDescriptionKey(),
                "Prevents the statue from falling down, so it may float freely.");
        builder.add(ArmorStandStyleOptions.SEALED.getTranslationKey(), "Sealed");
        builder.add(ArmorStandStyleOptions.SEALED.getDescriptionKey(),
                "The statue can no longer be broken, equipment cannot be changed. Disallows opening this menu in survival mode.");
        builder.add(ArmorStandPositionScreen.SCALE_TRANSLATION_KEY, "Scale:");
        builder.add(ArmorStandPositionScreen.ROTATION_TRANSLATION_KEY, "Rotation:");
        builder.add(ArmorStandPositionScreen.POSITION_X_TRANSLATION_KEY, "X-Position:");
        builder.add(ArmorStandPositionScreen.POSITION_Y_TRANSLATION_KEY, "Y-Position:");
        builder.add(ArmorStandPositionScreen.POSITION_Z_TRANSLATION_KEY, "Z-Position:");
        builder.add(ArmorStandPositionScreen.INCREMENT_TRANSLATION_KEY, "Increment by %s");
        builder.add(ArmorStandPositionScreen.DECREMENT_TRANSLATION_KEY, "Decrement by %s");
        builder.add(ArmorStandPositionScreen.DEGREES_TRANSLATION_KEY, "%s\u00B0");
        builder.add(ArmorStandPositionScreen.MOVE_BY_TRANSLATION_KEY, "Move By:");
        builder.add(ArmorStandPositionScreen.PIXELS_TRANSLATION_KEY, "%s Pixel(s)");
        builder.add(ArmorStandPositionScreen.BLOCKS_TRANSLATION_KEY, "%s Block(s)");
        builder.add(ArmorStandPositionScreen.CENTERED_TRANSLATION_KEY, "Align Centered");
        builder.add(ArmorStandPositionScreen.CENTERED_DESCRIPTION_TRANSLATION_KEY,
                "Align an armor stand in the center of the block position it is placed on.");
        builder.add(ArmorStandPositionScreen.CORNERED_TRANSLATION_KEY, "Align Cornered");
        builder.add(ArmorStandPositionScreen.CORNERED_DESCRIPTION_TRANSLATION_KEY,
                "Align an armor stand at the corner of the block position it is placed on.");
        builder.add(ArmorStandPositionScreen.ALIGNED_TRANSLATION_KEY, "Aligned!");
        builder.add(PosePartMutator.HEAD.getTranslationKey(), "Head");
        builder.add(PosePartMutator.BODY.getTranslationKey(), "Body");
        builder.add(PosePartMutator.LEFT_ARM.getTranslationKey(), "Left Arm");
        builder.add(PosePartMutator.RIGHT_ARM.getTranslationKey(), "Right Arm");
        builder.add(PosePartMutator.LEFT_LEG.getTranslationKey(), "Left Leg");
        builder.add(PosePartMutator.RIGHT_LEG.getTranslationKey(), "Right Leg");
        builder.add(PosePartMutator.AXIS_X_TRANSLATION_KEY, "X: %s");
        builder.add(PosePartMutator.AXIS_Y_TRANSLATION_KEY, "Y: %s");
        builder.add(PosePartMutator.AXIS_Z_TRANSLATION_KEY, "Z: %s");
        builder.add(ArmorStandRotationsScreen.TIP_TRANSLATION_KEY + 1,
                "Hold any [§dShift§r] or [§dAlt§r] key to lock two-dimensional sliders to a single axis while dragging!");
        builder.add(ArmorStandRotationsScreen.TIP_TRANSLATION_KEY + 2,
                "Use arrow keys to move sliders with greater precision than when dragging! Focus a slider first by clicking.");
        builder.add(ArmorStandRotationsScreen.RESET_TRANSLATION_KEY, "Reset");
        builder.add(ArmorStandRotationsScreen.RANDOMIZE_TRANSLATION_KEY, "Randomize");
        builder.add(ArmorStandRotationsScreen.LIMITED_TRANSLATION_KEY, "Limited Rotations");
        builder.add(ArmorStandRotationsScreen.UNLIMITED_TRANSLATION_KEY, "Unlimited Rotations");
        builder.add(ArmorStandRotationsScreen.COPY_TRANSLATION_KEY, "Copy");
        builder.add(ArmorStandRotationsScreen.PASTE_TRANSLATION_KEY, "Paste");
        builder.add(ArmorStandRotationsScreen.MIRROR_TRANSLATION_KEY, "Mirror");
        builder.add(ArmorStandAlignment.BLOCK.getTranslationKey(), "Align Block On Surface");
        builder.add(ArmorStandAlignment.BLOCK.getDescriptionsKey(),
                "Align an armor stand placed on a surface so that a block held by it appears on the surface.");
        builder.add(ArmorStandAlignment.FLOATING_ITEM.getTranslationKey(), "Align Item On Surface");
        builder.add(ArmorStandAlignment.FLOATING_ITEM.getDescriptionsKey(),
                "Align an armor stand placed on a surface so that an item held by it appears upright on the surface.");
        builder.add(ArmorStandAlignment.FLAT_ITEM.getTranslationKey(), "Align Item Flat On Surface");
        builder.add(ArmorStandAlignment.FLAT_ITEM.getDescriptionsKey(),
                "Align an armor stand placed on a surface so that a non-tool item held by it appears flat on the surface.");
        builder.add(ArmorStandAlignment.TOOL.getTranslationKey(), "Align Tool Flat On Surface");
        builder.add(ArmorStandAlignment.TOOL.getDescriptionsKey(),
                "Align an armor stand placed on a surface so that a tool held by it appears flat on the surface.");
        builder.add(AbstractArmorStandScreen.CREDITS_TRANSLATION_KEY,
                "Some content on this page originates from the Vanilla Tweaks \"Armor Statues\" data pack. Click this button to go to their website!");
    }
}
