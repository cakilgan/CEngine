package io.github.cakilgan.cgraphics.c2d.render.anim;

import io.github.cakilgan.cgraphics.c2d.render.sprite.C2DSprite;
import io.github.cakilgan.engine.system.ecs.comp.core.CEOComponent;

import java.util.HashMap;

public class C2DAnimation extends CEOComponent {
    C2DSprite main;
    C2DSprite[] animSprites;
    String spriteComponentCode;
    public C2DAnimation(String spriteComponentCode,C2DSprite[] animSprites){
        this.spriteComponentCode = spriteComponentCode;
        this.animSprites = animSprites;
        this.anims = new HashMap<>();
    }
    public C2DAnimation(C2DSprite main,C2DSprite[] animSprites){
        this.animSprites =animSprites;
        this.main = main;
        this.anims = new HashMap<>();
    }
    public C2DAnimation(C2DSprite[] animSprites,int index){
        this.animSprites =animSprites;
        this.main = animSprites[index];
        this.anims = new HashMap<>();
    }
    @Override
    public void init() {
        super.init();
    }

    HashMap<String,int[]> anims;
    public void addAnim(String code,int[] indexes){
        anims.put(code,indexes);
    }
    String playcode="";
    public void setPlaycode(String playcode) {
        this.playcode = playcode;
    }
    public void setAnim(String code){
        indexes = anims.get(code);
    }
    boolean reset;

    public void setReset(boolean reset) {
        this.reset = reset;
    }

    public void play(String code, float dt){
        if (!playcode.equals(code)) {
            // Yeni bir animasyon set ediliyorsa gecikmeyi sıfırla
            setPlaycode(code);
            spriteNum = 0; // İlk kareye dön
            last_ms = ms;  // Animasyonu başlatmak için gecikmeyi sıfırla
        }

        indexes = anims.get(playcode);
        spriteCount = indexes.length;
        if (reset){
            dt = 0f;
            reset =false;
        }

        last_ms -= dt;
        if (last_ms <= 0) {
            last_ms = ms;
            spriteNum++;
            if (spriteNum >= spriteCount) {
                spriteNum = 0;
            }
            if (disable_first_frame_in_loop && spriteNum == 0) {
                return;
            }
            getParent().getSprite(spriteComponentCode).set(animSprites[indexes[spriteNum]]);
        }
    }


    int[] indexes;
    @Override
    public void update(double dt) {
        super.update(dt);
    }
    public void setMs(float ms) {
        this.ms = ms;
    }

    boolean disable_first_frame_in_loop;
    public void setDisable_first_frame_in_loop(boolean disable_first_frame_in_loop) {
        this.disable_first_frame_in_loop = disable_first_frame_in_loop;
    }

    public void setZPos(int index ,float zpos){
        animSprites[index].setZPos(zpos);
    }

    public void setZPosAll(float zpos){
        for (C2DSprite animSprite : animSprites) {
            animSprite.setDontSyncZpos(true);
            animSprite.setZPos(zpos);
        }
    }
    public C2DSprite[] sprites(){
        return animSprites;
    }
    float ms;
    float last_ms=0.0f;
    int spriteCount = 0;
    int spriteNum = 0;
    @Override
    public void dispose() {
        super.dispose();
    }
}
