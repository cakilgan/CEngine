package io.github.cakilgan.physics;

import io.github.cakilgan.cmath.CMath;
import io.github.cakilgan.engine.system.ecs.comp.core.CEOComponent;
import library.dynamics.Body;
import library.geometry.Circle;
import library.geometry.Polygon;
import library.geometry.Shapes;
import library.math.Vectors2D;
import org.joml.Vector2f;

public class CPBody<T extends Shapes> extends CEOComponent {
    Body body;
    Vectors2D pos;
    T shape;

    boolean syncWithObject = true;
    public CPBody(T shape, Vectors2D pos){
        this.shape = shape;
        this.pos = pos;
        body = new Body(shape,pos.x,pos.y);
    }
    public CPBody(T shape, Vectors2D pos,boolean syncWithObject){
        this.shape = shape;
        this.pos = pos;
        body = new Body(shape,pos.x,pos.y);
        this.syncWithObject = syncWithObject;
    }
    public void setSyncWithObject(boolean syncWithObject) {
        this.syncWithObject = syncWithObject;
    }

    public void set(float width,float height){
        Polygon polygon = new Polygon(width/2f,height/2f);
        body.shape = polygon;
    }
    public T getShape() {
        return shape;
    }
    public Body getBody() {
        return body;
    }
    public Vectors2D getPos() {
        return pos;
    }

    public void addToWorld(CEWorld world){
        world.world.addBody(body);
    }

    @Override
    public void init() {
        addToWorld(getParent().getParent().getScene().getWorld());
        super.init();
    }

    @Override
    public void update(double dt) {
        if (syncWithObject) {
            pos = body.position;
            getParent().getTransform().getPos().set(CMath.toFloat(pos));
            getParent().getTransform().setRotation((float) Math.toDegrees(body.orientation));
        }
        super.update(dt);
    }
}
