package io.github.cakilgan.game.scene.snakeGame;

import io.github.cakilgan.cgraphics.c2d.render.sprite.C2DSpriteSheet;
import io.github.cakilgan.cgraphics.c2d.render.font.C2DFontRenderer;
import io.github.cakilgan.cscriptengine.engines.MapFileScriptEngine;
import io.github.cakilgan.cgraphics.c2d.render.anim.C2DAnimation;
import io.github.cakilgan.cgraphics.c2d.render.sprite.C2DSprite;
import io.github.cakilgan.engine.system.ecs.comp.CEOCameraLock;
import io.github.cakilgan.engine.system.ecs.comp.CEOTransform;
import io.github.cakilgan.cgraphics.c2d.render.font.C2DFont;
import io.github.cakilgan.engine.system.ecs.util.CEObjectID;
import io.github.cakilgan.engine.system.ecs.core.CEObject;
import io.github.cakilgan.cgraphics.c2d.render.C2DTexture;
import io.github.cakilgan.engine.window.scene.CEScene;
import io.github.cakilgan.core.io.FileHelper;
import io.github.cakilgan.core.CakilganCore;
import io.github.cakilgan.engine.map.C2DMap;
import io.github.cakilgan.engine.CEngine;
import org.lwjgl.glfw.GLFW;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import java.io.IOException;
import java.util.Random;
import java.io.File;

import static io.github.cakilgan.game.scene.snakeGame.Snake.Direction.*;

public class SnakeGameScene extends CEScene {
    C2DMap map;
    C2DFontRenderer fontRenderer;
    Snake snake;
    C2DAnimation animation;
    CEObjectID fpsObjectID = new CEObjectID("fpsObject");
    CEObjectID mapObjectID = new CEObjectID("mapObject");
    CEObjectID food = new CEObjectID("food");
    @Override
    public void init() {
        getCamera().setCanMoveWithWASD(false);
        getCamera().getPosition().set(new Vector3f(960,-540,getCamera().getPosition().z));
        //map setup
        map = new MapFileScriptEngine().parse(CEngine.RESOURCE_MANAGER.getDirectoryResource("gameRes").getDir("data").getDir("map").getFile("snakeGame.c2dmap"));
        map.objectSetup(this);
        //map.setDebugDrawAll();
        // ;
        //fps font setup.
        C2DFont font = new C2DFont(
                new C2DTexture(CEngine.RESOURCE_MANAGER.textures().getFile("default_font_2.png"),new Vector2i(816,306)),
                51,
                51,
                16,
                6,
                0
                ," !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~");
        font.set(-16f,0);
        fontRenderer = new C2DFontRenderer(font,new Vector2f(32f,32f));
        CEObject fpsObject= new CEObject(fpsObjectID);
        fpsObject.addTransform(new CEOTransform(new Vector2f(0,0),new Vector2f(16f*120,32),0));
        // debugDraw
        // fpsObject.addComponent("debug",new C2DDebugDraw().setColorcode(new Vector3f(1,0.5f,0)));
        fpsObject.addComponent("camLock",new CEOCameraLock(new Vector2f(-16f*120f/2f,1080-20)));
        fontRenderer.setParent(fpsObject);
        fontRenderer.init();
        addObject(fpsObject);
        // ;


        CEObject mapObject = new CEObject(mapObjectID);
        mapObject.addTransform(new CEOTransform(new Vector2f(),new Vector2f(960,960),0f));
        //mapObject.addComponent("debugDraw",new C2DDebugDraw().setColorcode(new Vector3f(1,0.4f,0)));

        C2DSpriteSheet spriteSheet = CEngine.RESOURCE_MANAGER.getSpriteSheet("snakeAtlas");
        spriteSheet.resize(new Vector2f(48,48));
        spriteSheet.repos(new Vector2f(0,1));
        animation = new C2DAnimation("sprite",spriteSheet.getSprites().toArray(new C2DSprite[0]));
        animation.setZPosAll(525f);
        animation.setMs(0.2f);
        animation.addAnim("up",new int[]{0,1,0,1});
        animation.addAnim("down",new int[]{2,3,2,3});
        animation.addAnim("left",new int[]{4,5,4,5});
        animation.addAnim("right",new int[]{6,7,6,7});

        snake = new Snake("snake1");
        snake.addBody(new SnakeHead("head",new Vector2f(48f,48f),new Vector2f(480-24,-480+24)));
        snake.addBody(new SnakeBody("bod",new Vector2f(48f,48f)));
        snake.setupObjects(this);
        snake.setMs(0.65f);
        if (new File("highscore.dat").exists()){
            File file = new File("highscore.dat");
            FileHelper helper = CakilganCore.createHelper(file);
            try {
                String highscore = helper.readWord();
                snake.scores.add(Integer.valueOf(highscore));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        C2DSprite sprite = CEngine.RESOURCE_MANAGER.getSpriteSheet("snakeAtlas").getSprites().get(9).copyWithoutTransform();
        sprite.setScale(new Vector2f(48,48));
        sprite.setDontSyncZpos(true);
        sprite.setZPos(156f);
        getObject(snake.getID(0)).addComponent("sprite",CEngine.RESOURCE_MANAGER.getSpriteSheet("snakeAtlas").getSprites().get(10));
        getObject(snake.getID(1)).addComponent("sprite",sprite);
        getObject(snake.getID(0)).addComponent("anim",animation);

        addObject(mapObject);

        CEObject food = new CEObject(this.food);
        food.addTransform(new CEOTransform(new Vector2f(480-24-48*4,-480+24),new Vector2f(48f,48f),0f));
        C2DSprite foodSprite = CEngine.RESOURCE_MANAGER.getSpriteSheet("snakeAtlas").getSprites().get(15).copyWithoutTransform();
        foodSprite.setScale(new Vector2f(48,48));
        foodSprite.setZPos(1000f);
        foodSprite.setDontSyncZpos(true);
        food.addComponent("sprite",foodSprite);
        //food.addComponent("debug",new C2DDebugDraw());
        addObject(food);
        super.init();
    }






    @Override
    public void update(double dt) {

        snake.update(this,(float) dt);

        //snake.setDirection(direction);
        if (snake.getDirection()== UP){
            animation.play("up", (float) dt);
        }
        if (snake.getDirection()== Snake.Direction.DOWN){
            animation.play("down", (float) dt);
        }
        if (snake.getDirection()== Snake.Direction.RIGHT){
            animation.play("right", (float) dt);
        }
        if (snake.getDirection()== Snake.Direction.LEFT){
            animation.play("left", (float) dt);
        }
        if (CEngine.KEYBOARD.isKeyJustPressed(GLFW.GLFW_KEY_RIGHT)||CEngine.KEYBOARD.isKeyJustPressed(GLFW.GLFW_KEY_LEFT)||CEngine.KEYBOARD.isKeyJustPressed(GLFW.GLFW_KEY_DOWN)||CEngine.KEYBOARD.isKeyJustPressed(GLFW.GLFW_KEY_UP)){
            animation.setReset(true);
        }
        if (snake.getBodies().get(0).pos().equals(getObject(food).getTransform().getPos())) {
            // Yemeği yediyse, yeni bir yılan gövde parçası ekle
            snake.addScore(10);
            snake.addBodyOnRun(new SnakeBody("body" + snake.getBodies().size() + 1, new Vector2f(48f, 48f),new Vector2f(-10000)), this);

            Vector2f pos;
            boolean isOverlapping;

            // Yeni pozisyonu seçmek için döngü
            do {
                // Haritadan rastgele bir konum seç
                pos = new Vector2f(map.getMapObjects()[new Random().nextInt(map.getMapObjects().length)].getPos());

                // Yılanın gövdesiyle çakışmadığını kontrol et
                isOverlapping = false;
                for (SnakeBody body : snake.getBodies()) {
                    if (body.pos().equals(pos)) {
                        isOverlapping = true;
                        break;
                    }
                }
            } while (isOverlapping); // Yılanın gövdesiyle çakışmayan bir pozisyon bulana kadar devam et

            // Yemeğin pozisyonunu yeni seçilen konuma ayarla
            getObject(food).getTransform().getPos().set(pos);
            if (snake.getMs()>=0.13f){
            snake.setMs((float) (snake.getMs()-0.04d));
            }
        }

        if (CEngine.KEYBOARD.isKeyJustPressed(GLFW.GLFW_KEY_ENTER)){
            if (snake.isGameOver()){
            snake.setGameOver(false);
            }
        }
                //updating font renderer with fps value
        if (snake.isGameOver()){
            fontRenderer.setText("FPS:["+(int)CEngine.TIME.getFps()+"] gameOver=? "+snake.isGameOver()+" "+" Ms: "+snake.getMs()+" snake: "+snake.getId()+" score: "+snake.getScore()+" HighScore: "+snake.getHighScore()+" Try Again ?[Press Enter]");
        }else{
        fontRenderer.setText("FPS:["+(int)CEngine.TIME.getFps()+"] gameOver=? "+snake.isGameOver()+" "+" Ms: "+snake.getMs()+" snake: "+snake.getId()+" score: "+snake.getScore()+" HighScore: "+snake.getHighScore());
        }
        fontRenderer.update(dt);
        super.update(dt);
    }

    @Override
    public void dispose() {
        int highScore = snake.getHighScore();
       FileHelper helper =  CakilganCore.createHelper(new File("highscore.dat"));
        try {
            helper.resetNotAppend();
            helper.write(highScore+"");
        helper.exitAndSave();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        super.dispose();
    }

    @Override
    public String getName() {
        return "Snake Game Main Scene";
    }
}
