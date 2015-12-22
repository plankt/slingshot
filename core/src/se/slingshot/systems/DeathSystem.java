package se.slingshot.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.listener.Handler;
import se.slingshot.components.BodyComponent;
import se.slingshot.components.CollisionComponent;
import se.slingshot.components.RenderComponent;

/**
 * DESC
 *
 * @author Marc
 * @since 2015-12
 */
public class DeathSystem extends EntitySystem {
    // ECS
    private ImmutableArray<Entity> entities;
    private ComponentMapper<BodyComponent> bodyMapper = ComponentMapper.getFor(BodyComponent.class);
    private ComponentMapper<CollisionComponent> collisionMapper = ComponentMapper.getFor(CollisionComponent.class);
    private ComponentMapper<RenderComponent> renderMapper = ComponentMapper.getFor(RenderComponent.class);

    // Death
    /** EventBus is used to pass messages between systems conveniently */
    private final MBassador<CollisionComponent> eventBus;

    /**
     * @param eventBus EventBus is used to pass messages between systems conveniently
     */
    public DeathSystem(MBassador<CollisionComponent> eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(RenderComponent.class, BodyComponent.class).get());

        /** Receive collisions */
        eventBus.subscribe(this);
    }

    @Override
    public void update(float deltaTime) {
    }

    /**
     * Called when a collision occurs with the help of eventBus
     * @param collision Collision component
     */
    @Handler
    public void handle(CollisionComponent collision){
        Entity entity = collision.entity;
        RenderComponent render = renderMapper.get(entity);
        render.visible = false;
    }
}