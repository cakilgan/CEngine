package io.github.cakilgan.cgraphics.c2d.render.anim;

import io.github.cakilgan.cgraphics.c2d.render.sprite.C2DSprite;
import io.github.cakilgan.engine.system.ecs.comp.core.CEOComponent;
import io.github.cakilgan.engine.system.ecs.core.CEObject;

import java.util.HashMap;

public class C2DInstantAnimation extends CEOComponent {
    private CEObject object; // Animasyonun ait olduğu nesne
    private C2DSprite[] sprites; // Animasyon kareleri
    private HashMap<String, int[]> animations; // Animasyonlar ve kare dizileri
    private String currentAnimation; // Oynatılan animasyon kodu
    private int currentFrame; // Geçerli kare indeksi
    private float animationTime; // Zamanlayıcı
    private float frameDuration; // Bir çerçevenin ekranda kalacağı süre

    public C2DInstantAnimation(float frameDuration) {
        this.frameDuration = frameDuration;
        this.animations = new HashMap<>();
        this.currentAnimation = "";
        this.currentFrame = 0;
        this.animationTime = 0f;
    }

    @Override
    public void init() {
        this.object = getParent();
        super.init();
    }

    public void setObject(CEObject object) {
        this.object = object;
    }

    public void setSprites(C2DSprite[] sprites) {
        this.sprites = sprites;
    }

    public String getCurrentAnimation() {
        return currentAnimation;
    }

    // Yeni bir animasyon ekle
    public void addAnimation(String animationCode, int[] frames) {
        animations.put(animationCode, frames);
    }

    // Animasyonu güncelle (her karede çağrılır)
    @Override
    public void update(double dt) {
        if (currentAnimation.isEmpty() || sprites == null || sprites.length == 0) {
            return; // Animasyon yoksa veya sprite dizisi boşsa güncellemeyi durdur
        }

        // Zamanlayıcıyı güncelle
        animationTime += (float) dt;

        // Animasyon süresini kontrol et ve kareyi değiştir
        if (animationTime >= frameDuration) {
            animationTime -= frameDuration;
            currentFrame++;
            // Çerçeve sayısını aşarsa, başa döner
            if (currentFrame >= animations.get(currentAnimation).length) {
                currentFrame = 0; // Animasyon tekrar başa dönsün
            }

            // Yeni çerçeveyi ayarla
            int[] currentAnimFrames = animations.get(currentAnimation);
            object.getSprite("sprite").set(sprites[currentAnimFrames[currentFrame]]);
        }
    }

    // İki animasyon arasında anında geçiş yapar
    public void switchAnimation(String newAnimationCode) {
        if (!newAnimationCode.equals(currentAnimation)) {
            currentAnimation = newAnimationCode;
            currentFrame = 0; // İlk kareye git
            animationTime = 0; // Zamanlayıcıyı sıfırla

            // İlk çerçeveyi hemen ayarla
            int[] currentAnimFrames = animations.get(currentAnimation);
            object.getSprite("sprite").set(sprites[currentAnimFrames[currentFrame]]);
        }
    }

    // Geçerli animasyon karesini getir
    public int getCurrentFrame() {
        return animations.get(currentAnimation)[currentFrame];
    }

    // Kare süresini değiştirme metodu (örneğin aniden değiştirme istenirse)
    public void setFrameDuration(float duration) {
        this.frameDuration = duration;
    }
}
