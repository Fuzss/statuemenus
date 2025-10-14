package fuzs.statuemenus.impl.data.client;

import fuzs.puzzleslib.api.client.data.v2.AbstractLanguageProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.statuemenus.api.v1.client.gui.screens.StatuePosesScreen;
import fuzs.statuemenus.api.v1.client.gui.screens.StatuePositionScreen;
import fuzs.statuemenus.api.v1.client.gui.screens.StatueRotationsScreen;
import fuzs.statuemenus.api.v1.client.gui.screens.StatueStyleScreen;
import fuzs.statuemenus.api.v1.helper.ArmorStandInteractHelper;
import fuzs.statuemenus.api.v1.world.inventory.data.*;
import fuzs.statuemenus.impl.world.inventory.StatuePoseImpl;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addTranslations(TranslationBuilder builder) {
        builder.add(ArmorStandInteractHelper.OPEN_SCREEN_TRANSLATION_KEY,
                "Use %s + %s with an empty hand to open configuration screen.");
        builder.add(StatuePoseImpl.SourceType.MINECRAFT.component, "By %s");
        builder.add(StatuePose.ATHENA.getTranslationKey(), "Athena");
        builder.add(StatuePose.BRANDISH.getTranslationKey(), "Brandish");
        builder.add(StatuePose.CANCAN.getTranslationKey(), "Cancan");
        builder.add(StatuePose.DEFAULT.getTranslationKey(), "Default");
        builder.add(StatuePose.ENTERTAIN.getTranslationKey(), "Entertain");
        builder.add(StatuePose.HERO.getTranslationKey(), "Hero");
        builder.add(StatuePose.HONOR.getTranslationKey(), "Honor");
        builder.add(StatuePose.RIPOSTE.getTranslationKey(), "Riposte");
        builder.add(StatuePose.SALUTE.getTranslationKey(), "Salute");
        builder.add(StatuePose.SOLEMN.getTranslationKey(), "Solemn");
        builder.add(StatuePose.ZOMBIE.getTranslationKey(), "Zombie");
        builder.add(StatuePose.WALKING.getTranslationKey(), "Walking");
        builder.add(StatuePose.RUNNING.getTranslationKey(), "Running");
        builder.add(StatuePose.POINTING.getTranslationKey(), "Pointing");
        builder.add(StatuePose.BLOCKING.getTranslationKey(), "Blocking");
        builder.add(StatuePose.LUNGEING.getTranslationKey(), "Lungeing");
        builder.add(StatuePose.WINNING.getTranslationKey(), "Winning");
        builder.add(StatuePose.SITTING.getTranslationKey(), "Sitting");
        builder.add(StatuePose.ARABESQUE.getTranslationKey(), "Arabesque");
        builder.add(StatuePose.CUPID.getTranslationKey(), "Cupid");
        builder.add(StatuePose.CONFIDENT.getTranslationKey(), "Confident");
        builder.add(StatuePose.DEATH.getTranslationKey(), "Death");
        builder.add(StatuePose.FACEPALM.getTranslationKey(), "Facepalm");
        builder.add(StatuePose.LAZING.getTranslationKey(), "Lazing");
        builder.add(StatuePose.CONFUSED.getTranslationKey(), "Confused");
        builder.add(StatuePose.FORMAL.getTranslationKey(), "Formal");
        builder.add(StatuePose.SAD.getTranslationKey(), "Sad");
        builder.add(StatuePose.JOYOUS.getTranslationKey(), "Joyous");
        builder.add(StatuePose.STARGAZING.getTranslationKey(), "Stargazing");
        builder.add(StatueScreenType.EQUIPMENT.getTranslationKey(), "Equipment");
        builder.add(StatueScreenType.ROTATIONS.getTranslationKey(), "Rotations");
        builder.add(StatueScreenType.STYLE.getTranslationKey(), "Style");
        builder.add(StatueScreenType.POSES.getTranslationKey(), "Poses");
        builder.add(StatueScreenType.POSITION.getTranslationKey(), "Position");
        builder.add(StatueStyleScreen.TEXT_BOX_HINT_TRANSLATION_KEY, "Custom Name");
        builder.add(StatueStyleScreen.TEXT_BOX_TOOLTIP_TRANSLATION_KEY, "Set a name to display above the statue.");
        builder.add(StatueStyleOption.SHOW_ARMS.getTranslationKey(), "Show Arms");
        builder.add(StatueStyleOption.SHOW_ARMS.getDescriptionKey(),
                "Shows the statue's arms, so it may hold items in either hand.");
        builder.add(StatueStyleOption.SMALL.getTranslationKey(), "Small");
        builder.add(StatueStyleOption.SMALL.getDescriptionKey(), "Makes the statue half it's size like a baby mob.");
        builder.add(StatueStyleOption.INVISIBLE.getTranslationKey(), "Invisible");
        builder.add(StatueStyleOption.INVISIBLE.getDescriptionKey(),
                "Makes the statue itself invisible, but still shows all equipped items.");
        builder.add(StatueStyleOption.NO_BASE_PLATE.getTranslationKey(), "No Base Plate");
        builder.add(StatueStyleOption.NO_BASE_PLATE.getDescriptionKey(),
                "Hide the stone base plate at the statue's feet.");
        builder.add(StatueStyleOption.SHOW_NAME.getTranslationKey(), "Show Name");
        builder.add(StatueStyleOption.SHOW_NAME.getDescriptionKey(), "Render the statue's name tag above it's head.");
        builder.add(StatueStyleOption.NO_GRAVITY.getTranslationKey(), "No Gravity");
        builder.add(StatueStyleOption.NO_GRAVITY.getDescriptionKey(),
                "Prevents the statue from falling down, so it may float freely.");
        builder.add(StatueStyleOption.INVULNERABLE.getTranslationKey(), "Invulnerable");
        builder.add(StatueStyleOption.INVULNERABLE.getDescriptionKey(), "The statue can neither be broken nor killed.");
        builder.add(StatueStyleOption.SEALED.getTranslationKey(), "Sealed");
        builder.add(StatueStyleOption.SEALED.getDescriptionKey(),
                "Disallows changing equipment and opening this menu in survival mode.");
        builder.add(StatuePositionScreen.SCALE_TRANSLATION_KEY, "Scale:");
        builder.add(StatuePositionScreen.ROTATION_TRANSLATION_KEY, "Rotation:");
        builder.add(StatuePositionScreen.POSITION_X_TRANSLATION_KEY, "X-Position:");
        builder.add(StatuePositionScreen.POSITION_Y_TRANSLATION_KEY, "Y-Position:");
        builder.add(StatuePositionScreen.POSITION_Z_TRANSLATION_KEY, "Z-Position:");
        builder.add(StatuePositionScreen.INCREMENT_TRANSLATION_KEY, "Increment by %s");
        builder.add(StatuePositionScreen.DECREMENT_TRANSLATION_KEY, "Decrement by %s");
        builder.add(StatuePositionScreen.DEGREES_TRANSLATION_KEY, "%s\u00B0");
        builder.add(StatuePositionScreen.MOVE_BY_TRANSLATION_KEY, "Move By:");
        builder.add(StatuePositionScreen.PIXELS_TRANSLATION_KEY, "%s Pixel(s)");
        builder.add(StatuePositionScreen.BLOCKS_TRANSLATION_KEY, "%s Block(s)");
        builder.add(StatuePositionScreen.CENTERED_TRANSLATION_KEY, "Align Centered");
        builder.add(StatuePositionScreen.CENTERED_DESCRIPTION_TRANSLATION_KEY,
                "Align an armor stand in the center of the block position it is placed on.");
        builder.add(StatuePositionScreen.CORNERED_TRANSLATION_KEY, "Align Cornered");
        builder.add(StatuePositionScreen.CORNERED_DESCRIPTION_TRANSLATION_KEY,
                "Align an armor stand at the corner of the block position it is placed on.");
        builder.add(StatuePositionScreen.ALIGNED_TRANSLATION_KEY, "Aligned!");
        builder.add(PosePartMutator.HEAD.getTranslationKey(), "Head");
        builder.add(PosePartMutator.BODY.getTranslationKey(), "Body");
        builder.add(PosePartMutator.LEFT_ARM.getTranslationKey(), "Left Arm");
        builder.add(PosePartMutator.RIGHT_ARM.getTranslationKey(), "Right Arm");
        builder.add(PosePartMutator.LEFT_LEG.getTranslationKey(), "Left Leg");
        builder.add(PosePartMutator.RIGHT_LEG.getTranslationKey(), "Right Leg");
        builder.add(PosePartMutator.AXIS_X_TRANSLATION_KEY, "X: %s");
        builder.add(PosePartMutator.AXIS_Y_TRANSLATION_KEY, "Y: %s");
        builder.add(PosePartMutator.AXIS_Z_TRANSLATION_KEY, "Z: %s");
        builder.add(StatueRotationsScreen.TIP_TRANSLATION_KEY + 1,
                "Hold any §dShift§r or §dAlt§r key to lock two-dimensional sliders to a single axis while dragging!");
        builder.add(StatueRotationsScreen.TIP_TRANSLATION_KEY + 2,
                "Use arrow keys to move sliders with greater precision than when dragging! Focus a slider first by clicking.");
        builder.add(StatueRotationsScreen.RESET_TRANSLATION_KEY, "Reset");
        builder.add(StatueRotationsScreen.RANDOMIZE_TRANSLATION_KEY, "Randomize");
        builder.add(StatueRotationsScreen.LIMITED_TRANSLATION_KEY, "Limited Rotations");
        builder.add(StatueRotationsScreen.UNLIMITED_TRANSLATION_KEY, "Unlimited Rotations");
        builder.add(StatueRotationsScreen.COPY_TRANSLATION_KEY, "Copy");
        builder.add(StatueRotationsScreen.PASTE_TRANSLATION_KEY, "Paste");
        builder.add(StatueRotationsScreen.MIRROR_TRANSLATION_KEY, "Mirror");
        builder.add(StatueAlignment.BLOCK.getTranslationKey(), "Align Block On Surface");
        builder.add(StatueAlignment.BLOCK.getDescriptionsKey(),
                "Align an armor stand placed on a surface so that a block held by it appears on the surface.");
        builder.add(StatueAlignment.FLOATING_ITEM.getTranslationKey(), "Align Item On Surface");
        builder.add(StatueAlignment.FLOATING_ITEM.getDescriptionsKey(),
                "Align an armor stand placed on a surface so that an item held by it appears upright on the surface.");
        builder.add(StatueAlignment.FLAT_ITEM.getTranslationKey(), "Align Item Flat On Surface");
        builder.add(StatueAlignment.FLAT_ITEM.getDescriptionsKey(),
                "Align an armor stand placed on a surface so that a non-tool item held by it appears flat on the surface.");
        builder.add(StatueAlignment.TOOL.getTranslationKey(), "Align Tool Flat On Surface");
        builder.add(StatueAlignment.TOOL.getDescriptionsKey(),
                "Align an armor stand placed on a surface so that a tool held by it appears flat on the surface.");
        builder.add(StatuePosesScreen.CREDITS_TRANSLATION_KEY,
                "Some content on this page originates from the Vanilla Tweaks \"Armor Statues\" data pack. Click this button to go to their website!");
    }
}
