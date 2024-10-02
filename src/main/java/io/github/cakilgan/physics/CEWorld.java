package io.github.cakilgan.physics;

import io.github.cakilgan.cmath.CMath;
import io.github.cakilgan.engine.system.CESceneComponent;
import library.dynamics.World;
import library.math.Vectors2D;
import org.joml.Vector2f;

public class CEWorld implements CESceneComponent {
    World world;
    public World getWorld() {
        return world;
    }
    public CEWorld(Vector2f gravity){
        world = new World(CMath.toFloat(gravity));
    }
    public CEWorld(Vectors2D gravity){
        world = new World(gravity);
    }

    @Override
    public void init() {

    }

    @Override
    public void update(double dt) {
        world.step(dt*5f);
    }

    @Override
    public void dispose() {

    }
}
