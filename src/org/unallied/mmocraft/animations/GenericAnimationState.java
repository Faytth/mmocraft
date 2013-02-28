package org.unallied.mmocraft.animations;

import org.newdawn.slick.Animation;
import org.unallied.mmocraft.Living;
import org.unallied.mmocraft.client.SpriteHandler;
import org.unallied.mmocraft.client.SpriteSheetNode;

/**
 * A generic animation state used when creating animation states from animations.uap.
 * 
 * This is mainly used by monsters and NPCs.  It does not contain any logic, which
 * means it won't change animation or state unless explicitly told to do so.
 * @author Alexandria
 *
 */
public abstract class GenericAnimationState extends AnimationState {

    /**
     * 
     */
    private static final long serialVersionUID = 5856945673992136035L;

    /** 
     * The type of animation, such as idle, walk, or run.
     */
    private AnimationType animationType;
    
    /** The sprite ID for the sprite that will be rendered when displaying this animation. */
    private String spriteId;
    
    /**
     * Creates a new generic animation state, typically used for monsters and NPCs.
     * @param living The living creature that is performing the animation.
     * @param last The last animation state that this living creature was in.
     * @param animationType The type of animation that this living creature is now performing.
     * @param spriteId The id used to render this state's sprite.
     */
    public GenericAnimationState(Living living, AnimationState last, AnimationType animationType, 
            String spriteId) {
        super(living, last);
        
        animation = new Animation();
        animation.setAutoUpdate(false);
        animation.setLooping(true);
        SpriteSheetNode node = SpriteHandler.getInstance().getNode(spriteId);
        width = node.getWidth();
        height = node.getHeight();
        duration = node.getDuration();
        setAnimation(node.getSpriteSheet());
        animation.start();
        
        this.animationType = animationType;
        this.spriteId = spriteId;
        this.horizontalOffset = node.getHorizontalOffset();
        this.verticalOffset = node.getVerticalOffset();
    }

    @Override
    public short getId() {
        return animationType.getValue();
    }

    @Override
    public float moveDownMultiplier() {
        return 1f;
    }
}
