package io.github.cakilgan.cmath;

import org.joml.Vector2f;

public class CMTransform {
    public Vector2f pos;
    public Vector2f scale;
    public float rotation;
    public CMTransform(Vector2f pos, Vector2f scale){
        init(pos,scale);
    }
    public CMTransform(Vector2f scale){
        init(new Vector2f(),scale);
    }
    public CMTransform(Vector2f pos, Vector2f scale, float rotation){
        init(new Vector2f(),scale);
        this.rotation = rotation;
    }
    public CMTransform(){
        init(new Vector2f(),new Vector2f());
    }
    public void init(Vector2f pos,Vector2f scale){
        this.pos = pos;
        this.scale = scale;
    }
    public Vector2f getCenter(){
        return new Vector2f(pos.x+(scale.x/2),pos.y+(scale.y/2));
    }
    public CMTransform copy(){
        return new CMTransform(new Vector2f(this.pos),new Vector2f(this.scale));
    }
    public void copy(CMTransform rtr){
        rtr.pos.set(this.pos);
        rtr.scale.set(this.scale);
    }
    @Override
    public String toString() {
        return "TMTransform{" +
                "pos=" + pos +
                ", scale=" + scale +
                '}';
    }
    @Override
    public boolean equals(Object obj) {
        if (obj==null) return false;
        if (!(obj instanceof CMTransform)) return false;
        return ((CMTransform) obj).pos.equals(this.pos) && ((CMTransform) obj).scale.equals(this.scale);
    }
}
