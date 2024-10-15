package io.github.cakilgan.game.scene.flappyBirdGame;

import io.github.cakilgan.cgraphics.c2d.render.C2DDebugDraw;
import io.github.cakilgan.cgraphics.c2d.render.anim.C2DAnimation;
import io.github.cakilgan.cgraphics.c2d.render.font.C2DFont;
import io.github.cakilgan.cgraphics.c2d.render.font.C2DFontRenderer;
import io.github.cakilgan.cgraphics.c2d.render.sprite.C2DSprite;
import io.github.cakilgan.cmath.CMath;
import io.github.cakilgan.core.CakilganCore;
import io.github.cakilgan.core.io.FileHelper;
import io.github.cakilgan.engine.CEngine;
import io.github.cakilgan.engine.system.ecs.comp.CEOTransform;
import io.github.cakilgan.engine.system.ecs.core.CEObject;
import io.github.cakilgan.engine.system.ecs.util.CEObjectID;
import io.github.cakilgan.engine.window.scene.CEScene;
import io.github.cakilgan.physics.CPBody;
import library.collision.Arbiter;
import library.geometry.Circle;
import library.geometry.Polygon;
import library.math.Vectors2D;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4d;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FlappyBirdGameScene extends CEScene {
    C2DFontRenderer scoreFontRenderer,gameOverFontRenderer;
    CEObjectID backgroundObjectID = new CEObjectID("backgroundObject"),
               groundObjectID = new CEObjectID("groundObject"),
               characterObjectID = new CEObjectID("characterObject");
    List<CEObjectID> pipes;
    C2DAnimation charAnim;
    public int getHighScore(){
        int rtrn = 0; // Başlangıçta yüksek skor 0 olarak ayarlandı
        for (int score : scores) {
            rtrn = Math.max(rtrn, score); // Her skoru rtrn ile karşılaştırarak en yükseğini bul
        }
        return rtrn;
    }
    public List<Integer> scores = new ArrayList<>();
    @Override
    public void init() {
        if (new File("highscore.dat").exists()){
            File file = new File("highscore.dat");
            FileHelper helper = CakilganCore.createHelper(file);
            try {
                helper.analyzeAndSetupTheFile();
                for (String string : helper.readLines()) {
                    if (string.contains("flappyBirdGame:")){
                        scores.add(Integer.valueOf(string.substring(string.indexOf(":")+2)));
                        break;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        getDebugRenderer().setShouldRender(false);
        getDebugRenderer().setShouldDebugRenderJPhysics(false);
        CEObject scoreFont = new CEObject(new CEObjectID("scoreFont"));
        scoreFont.addTransform(new CEOTransform(new Vector2f(-960,1080-64),new Vector2f("1000".length()*64,64),0f));
        scoreFont.addComponent("debug",new C2DDebugDraw());
        addObject(scoreFont);
        C2DFont font = C2DFont.DEFAULT_FONT_2.copy();
        font.set(-7f,0f);
        scoreFontRenderer = new C2DFontRenderer(font,new Vector2f(64,64));
        scoreFontRenderer.setZpos(555f);
        scoreFontRenderer.setParent(scoreFont);
        scoreFontRenderer.setText("0");
        scoreFontRenderer.init();

        CEObject gameOverFont = new CEObject(new CEObjectID("gameOverFont"));
        gameOverFont.addTransform(new CEOTransform(new Vector2f(-960,1080-64),new Vector2f("Game Over".length()*64,128),0f));
        gameOverFont.addComponent("debug",new C2DDebugDraw());
        addObject(gameOverFont);
        C2DFont font2 = C2DFont.DEFAULT_FONT_2.copy();
        font2.set(-7f,0f);
        gameOverFontRenderer = new C2DFontRenderer(font2,new Vector2f(64,64));
        gameOverFontRenderer.setZpos(555f);
        gameOverFontRenderer.setParent(gameOverFont);
        gameOverFontRenderer.setText("0");
        gameOverFontRenderer.init();


        pipes = new ArrayList<>();
        CEObject backgroundObject = new CEObject(backgroundObjectID);
        //566+56 = 622 1080
        backgroundObject.addTransform(new CEOTransform(new Vector2f(-960,540),new Vector2f(288*2+56,1080),0f));
        backgroundObject.addComponent("debug",new C2DDebugDraw().setColorcode(new Vector3f(1,0,1)));
        C2DSprite b1 = CEngine.RESOURCE_MANAGER.getSprite("b1").copyWithoutTransform();
        b1.setZPos(-10f);
        b1.setScale(new Vector2f(576+56,1080));
        b1.setDontSyncZpos(true);
        b1.getColor().set(new Vector4f(1,1,1,1));
        backgroundObject.addComponent("sprite",b1);

        CEObject groundObject = new CEObject(groundObjectID);
        groundObject.addTransform(new CEOTransform(new Vector2f(-960,55),new Vector2f(576+56,112),0));
        groundObject.addComponent("debug",new C2DDebugDraw());
        C2DSprite ground = CEngine.RESOURCE_MANAGER.getSprite("ground").copyWithoutTransform();
        ground.setZPos(-8f);
        ground.setScale(new Vector2f(576+56,112));
        ground.setDontSyncZpos(true);
        ground.getColor().set(new Vector4f(1,1,1,1));
        CPBody<Polygon> body = new CPBody<>(new Polygon((576+56)/2f,112/2f),new Vectors2D(
                groundObject.getTransform().getPos().x,
                groundObject.getTransform().getPos().y
        ));
        body.getBody().restitution = 0f;
        body.getBody().setDensity(0f);
        groundObject.addComponent("body",body);
        groundObject.addComponent("sprite",ground);

        CEngine.RESOURCE_MANAGER.getSpriteSheet("character").resize(new Vector2f(64,54));
        charAnim = new C2DAnimation("sprite",CEngine.RESOURCE_MANAGER.getSpriteSheet("character").getSprites().toArray(new C2DSprite[0]));
        charAnim.addAnim("fly",new int[]{0,1,2});
        charAnim.setMs(0.7f);
        charAnim.setZPosAll(1000f);

        CEObject character = new CEObject(characterObjectID);
        character.addTransform(new CEOTransform(new Vector2f(-770,540),new Vector2f(64,54),0f));
        character.addComponent("debug",new C2DDebugDraw());
        character.addComponent("sprite",CEngine.RESOURCE_MANAGER.getSpriteSheet("character").getSprites().get(0));
        character.addComponent("anim",charAnim);
        CPBody<Polygon> charBody = new CPBody<>(new Polygon(64/2f,54/2f),new Vectors2D(
                character.getTransform().getPos().x,
                character.getTransform().getPos().y
        ));
        //charBody.getBody().setDensity(0f);
        character.addComponent("body",charBody);


        CEObject pipe1 = new CEObject(new CEObjectID("pipe1"));
        pipes.add(pipe1.getID());
        pipe1.addTransform(new CEOTransform(new Vector2f(-(960),55+160),new Vector2f(104,320),0f));
        C2DSprite p1Sprite = CEngine.RESOURCE_MANAGER.getSprite("pipe").copyWithoutTransform();
        p1Sprite.setScale(new Vector2f(104,320));
        p1Sprite.setZPos(-9f);
        p1Sprite.setDontSyncZpos(true);
        pipe1.addComponent("sprite",p1Sprite);
        CPBody<Polygon> p1Body = new CPBody<>(new Polygon(52f,160f),new Vectors2D(-960,55+160));
        p1Body.getBody().setDensity(0);
        pipe1.addComponent("body",p1Body);



        CEObject pipe2 = new CEObject(new CEObjectID("pipe1"));
        pipes.add(pipe2.getID());
        pipe2.addTransform(new CEOTransform(new Vector2f(-960,1080-(160)),new Vector2f(104,320),180f));
        C2DSprite p2Sprite = CEngine.RESOURCE_MANAGER.getSprite("pipe").copyWithoutTransform();
        p2Sprite.setScale(new Vector2f(104,320));
        p2Sprite.setZPos(-9f);
        p2Sprite.setDontSyncZpos(true);
        p2Sprite.setSyncRotationWithObject(true);
        pipe2.addComponent("sprite",p2Sprite);
        CPBody<Polygon> p2Body = new CPBody<>(new Polygon(52f,160f),new Vectors2D(-960,1080-160));
        p2Body.getBody().setDensity(0);
        pipe2.addComponent("body",p2Body);


        addObject(pipe1);
        addObject(pipe2);

        addObject(backgroundObject);
        addObject(groundObject);
        addObject(character);

        super.init();
    }


    Vectors2D lastPos;
    float lastAngle;
    boolean gameOver = false;
    boolean birdIsNotDead = false;
    int score = 0;
    float time = 0;
    @Override
    public void update(double dt) {
        if (!gameOver){
            getObject(pipes.get(0)).getBody("body").getBody().orientation = 0f;
            getObject(pipes.get(1)).getBody("body").getBody().orientation = Math.toRadians(180f);

            scoreFontRenderer.setText(score+"");
            getObject(characterObjectID).getBody("body").getBody().velocity.x = 0f;
            time -= (float) dt;
            if (time>0){
                charAnim.setMs(0.2f);
                charAnim.play("fly", (float) dt);
            }

            for (Arbiter contact : getWorld().getWorld().contacts) {
                if (contact.getA().equals(getObject(characterObjectID).getBody("body").getBody())){
                    for (CEObjectID pipe : pipes) {
                        if (contact.getB().equals(getObject(pipe).getBody("body").getBody())){
                            gameOver = true;
                        }
                    }
                    if (contact.getB().equals(getObject(groundObjectID).getBody("body").getBody())){
                        gameOver = true;
                    }
                    lastPos = getObject(characterObjectID).getBody("body").getBody().position;
                    lastAngle = (float) getObject(characterObjectID).getBody("body").getBody().orientation;
                }
                if (contact.getB().equals(getObject(characterObjectID).getBody("body").getBody())){
                    for (CEObjectID pipe : pipes) {
                        if (contact.getA().equals(getObject(pipe).getBody("body").getBody())){
                            gameOver = true;
                        }
                    }
                    if (contact.getA().equals(getObject(groundObjectID).getBody("body").getBody())){
                        gameOver = true;
                    }
                    lastPos = getObject(characterObjectID).getBody("body").getBody().position;
                    lastAngle = (float) getObject(characterObjectID).getBody("body").getBody().orientation;
                }
            }
            if (CEngine.KEYBOARD.isKeyJustPressed(GLFW.GLFW_KEY_SPACE)){
                time = 3f;
                getObject(characterObjectID).getBody("body").getBody().velocity.add(new Vectors2D(0,76f));
            }

                // Sabit boşluk değeri

            float PIPE_GAP = 200f;

            Random random = new Random();

            for (CEObjectID pipe : pipes) {
                // Eğer borular ekranın dışına çıktıysa, yeniden konumlandır
                if (getObject(pipe).getTransform().getPos().x<(-960-311-52)
                        ||getObject(pipe).getTransform().getPos().x> (-960+311+52)) {
                    // Boruların X pozisyonunu tekrar sağ tarafta başlat
                    float newXPosition = -960 - 311 - 52;

                    // Üst boru için rastgele yükseklik belirle
                    float upperPipeHeight = random.nextFloat(300, 680);
                    float lowerPipeHeight = 1080 - upperPipeHeight - PIPE_GAP;

                    // Üst boruyu güncelle
                    getWorld().getWorld().removeBody(getObject(pipes.get(0)).getBody("body").getBody());
                    getObject(pipes.get(0)).removeComponent("body");
                    CPBody<Polygon> newUpperBody = new CPBody<>(new Polygon(52, upperPipeHeight / 2f),
                            new Vectors2D(newXPosition, upperPipeHeight / 2f));
                    newUpperBody.getBody().setDensity(0f);
                    getWorld().getWorld().addBody(newUpperBody.getBody());
                    getObject(pipes.get(0)).addComponent("body", newUpperBody);
                    getObject(pipes.get(0)).getSprite("sprite").setScale(new Vector2f(104, upperPipeHeight));

                    // Alt boruyu güncelle
                    getWorld().getWorld().removeBody(getObject(pipes.get(1)).getBody("body").getBody());
                    getObject(pipes.get(1)).removeComponent("body");
                    CPBody<Polygon> newLowerBody = new CPBody<>(new Polygon(52, lowerPipeHeight / 2f),
                            new Vectors2D(newXPosition, 1080 - (lowerPipeHeight / 2f)));
                    newLowerBody.getBody().setDensity(0f);
                    getWorld().getWorld().addBody(newLowerBody.getBody());
                    getObject(pipes.get(1)).addComponent("body", newLowerBody);
                    getObject(pipes.get(1)).getSprite("sprite").setScale(new Vector2f(104, lowerPipeHeight));

                    // Boruların yeniden doğması, kuşun ölüm kontrolünü sıfırla
                    birdIsNotDead = true;

                } else {
                    // Boruları ekranda hareket ettir (sola doğru)
                    getObject(pipes.get(0)).getBody("body").getBody().position.add(new Vectors2D(3f, 0f));
                    getObject(pipes.get(1)).getBody("body").getBody().position.add(new Vectors2D(3f, 0f));
                }
            }

            if (birdIsNotDead){
                score++;
                birdIsNotDead = false;
            }
            getObject(characterObjectID).getBody("body").getBody().orientation = Math.toRadians(Math.max(-30, Math.min(30, -getObject(characterObjectID).getBody("body").getBody().velocity.y * 5)));

            scoreFontRenderer.setColorize(new Vector3f(1,1,1));
            getObject(new CEObjectID("scoreFont")).getTransform().getPos().set(new Vector2f(-960-70,1080-64));
            gameOverFontRenderer.setText("");
        }else{
            gameOverFontRenderer.setText("Game Over  [Enter]");
            scoreFontRenderer.setText(score+":"+getHighScore());
            scoreFontRenderer.setColorize(new Vector3f(1,0,0));
            getObject(new CEObjectID("scoreFont")).getTransform().getPos().set(new Vector2f(-960-90,1080-64-90));
            getObject(characterObjectID).getBody("body").getBody().velocity.set(new Vectors2D());
            getObject(characterObjectID).getBody("body").getBody().orientation = lastAngle;
            getObject(characterObjectID).getBody("body").getBody().position.set(lastPos);

            if (CEngine.KEYBOARD.isKeyJustPressed(GLFW.GLFW_KEY_ENTER)){
                scores.add(score);
                getObject(characterObjectID).getBody("body").getBody().position.set(new Vectors2D(-770,540));
                getObject(pipes.get(0)).getBody("body").getBody().position.x =-960-311-52;
                getObject(pipes.get(1)).getBody("body").getBody().position.x =-960-311-52;
                score = 0;
                gameOver = false;
            }
        }
        gameOverFontRenderer.update(dt);
        scoreFontRenderer.update(dt);
        super.update(dt);
    }


    @Override
    public void dispose() {
        int highScore = getHighScore();
        FileHelper helper =  CakilganCore.createHelper(new File("highscore.dat"));
        try {
            int val = 0;
            helper.analyzeAndSetupTheFile();
            for (String string : helper.readLines()) {
                if (string.startsWith("snakeGame:")){
                    String sub = string.substring(string.indexOf(":")+2);
                    val = Integer.parseInt(sub);
                }
            }
            helper.resetNotAppend();
            helper.writeln("snakeGame: "+val);
            helper.writeln("flappyBirdGame: "+highScore);
            helper.exitAndSave();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        super.dispose();
    }

    @Override
    public String getName() {
        return "Flappy Bird Scene 1";
    }
}
