package io.github.cakilgan.cmath;

import library.math.Vectors2D;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class CMath {
    public static Vector2d to(Vectors2D from){
        return new Vector2d(from.x,from.y);
    }
    public static Vector2f toFloat(Vectors2D from){
        return new Vector2f((float) from.x, (float) from.y);
    }
    public static Vectors2D to(Vector2d from){
        return new Vectors2D(from.x,from.y);
    }
    public static Vectors2D to(Vector2f to){
        return new Vectors2D(to.x,to.y);
    }
    public static Vectors2D toFloat(Vector2f from){
        return new Vectors2D(from.x,from.y);
    }
    public static float lerp(float start, float end, float amt) {
        return (1 - amt) * start + amt * end;
    }
    public static float dist(Vector2f pos1, Vector2f pos2) {
        return (float) Math.hypot(pos1.x - pos2.x, pos1.y - pos2.y);
    }
    public static float dist(Vector2f pos1, Vector3f pos2) {
        return dist(pos1,new Vector2f(pos2.x,pos2.y));
    }
}
