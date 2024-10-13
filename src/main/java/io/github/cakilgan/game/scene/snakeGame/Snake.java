package io.github.cakilgan.game.scene.snakeGame;

import io.github.cakilgan.cgraphics.c2d.render.C2DDebugDraw;
import io.github.cakilgan.cgraphics.c2d.render.sprite.C2DSprite;
import io.github.cakilgan.engine.CEngine;
import io.github.cakilgan.engine.system.ecs.util.CEObjectID;
import io.github.cakilgan.engine.window.scene.CEScene;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Snake {
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    List<SnakeBody> bodies;
    Vector2f mapScale,snakeScale;
    int score;

    public void setScore(int score) {
        this.score = score;
    }
    public int getScore() {
        return score;
    }
    public int addScore(int add){
        score+=add;
        return score;
    }

    public Snake(String id){
        bodies = new ArrayList<>();
        this.id = id;
    }
    public void addBody(SnakeBody body){
        bodies.add(body);
    }

    public Direction getDirection() {
        return direction;
    }

    public CEObjectID getID(int i){
        return bodies.get(i).object.getID();
    }
    public List<SnakeBody> getBodies() {
        return bodies;
    }

    public void setupObjects(CEScene scene){
        for (SnakeBody body : bodies) {
            scene.addObject(body.object);
        }
    }
    public void debugDrawAll(){
        for (SnakeBody body : bodies) {
            body.object.addComponent("debugDraw",new C2DDebugDraw());
        }
    }
    public void updateSprites(CEScene scene, C2DSprite newSprite) {
        List<C2DSprite> sprites = new ArrayList<>();
        for (int i = 1; i < bodies.size(); i++) {
            C2DSprite sr = scene.getObject(getID(i)).getSprite("sprite").copyWithoutTransform();
            sr.setScale(new Vector2f(48,48));
            sprites.add(sr);
        }
        scene.getObject(getID(1)).getSprite("sprite").set(newSprite);
        for (int i = 2; i < bodies.size(); i++) {
            scene.getObject(getID(i)).getSprite("sprite").set(sprites.get(i-2));
        }
        switch (bodies.get(bodies.size()-1).object.getSprite("sprite").getDirection()){
            case "up"-> {
                C2DSprite sprite = CEngine.RESOURCE_MANAGER.getSpriteSheet("snakeAtlas").getSprites().get(11).copyWithoutTransform();
                sprite.setScale(new Vector2f(48,48));
                sprite.setZPos(10f);
                sprite.setDirection("up");
                bodies.get(bodies.size()-1).object.getSprite("sprite").set(sprite);
            }
            case "down"-> {
                C2DSprite sprite = CEngine.RESOURCE_MANAGER.getSpriteSheet("snakeAtlas").getSprites().get(12).copyWithoutTransform();
                sprite.setZPos(10f);
                sprite.setScale(new Vector2f(48,48));
                sprite.setDirection("down");
                bodies.get(bodies.size()-1).object.getSprite("sprite").set(sprite);
            }
            case "right"->{
                C2DSprite sprite = CEngine.RESOURCE_MANAGER.getSpriteSheet("snakeAtlas").getSprites().get(13).copyWithoutTransform();
                sprite.setScale(new Vector2f(48,48));
                sprite.setZPos(10f);
                sprite.setDirection("right");
                bodies.get(bodies.size()-1).object.getSprite("sprite").set(sprite);
            }
            case "left"->{
                C2DSprite sprite = CEngine.RESOURCE_MANAGER.getSpriteSheet("snakeAtlas").getSprites().get(14).copyWithoutTransform();
                sprite.setScale(new Vector2f(48,48));
                sprite.setZPos(10f);
                sprite.setDirection("left");
                bodies.get(bodies.size()-1).object.getSprite("sprite").set(sprite);
            }
        }
    }

    public void updatePosition(Vector2f add){
        List<Vector2f> beforePos = new ArrayList<>();
        for (SnakeBody body : bodies) {
            beforePos.add(new Vector2f(body.pos()));
        }
        bodies.get(0).pos().add(add);
        for (int i = 1; i < bodies.size(); i++) {
            bodies.get(i).pos().set(beforePos.get(i - 1));
        }
    }
    float ms = 0.35f;
    public void setMs(float ms) {
        this.ms = ms;
    }

    public float getMs() {
        return ms;
    }

    public void optimizeScores(){
        if (scores.size()>20){
            int highScore = getHighScore();
            scores = new ArrayList<>();
            scores.add(highScore);
        }
    }
    int onePlay = 0;
    float wait = ms;
    public void update(CEScene scene,float dt){
        if (!isGameOver){
            optimizeScores();
            if (isEatingItself()){
                gameOver(scene);
            }
            checkIsOutOfTheMap(scene);
            if (shouldUpdate){
                setupObjects(scene);
            }
            if (directionC){
                wait = 0f;
                directionC = false;
            }
            if (wait<=0){
                checkDirection();
                updatePosition(calcAdd());
                updateSprites(scene,calcSprites(scene));
                wait = ms;
            }else{
                checkDirection();
                wait -=dt;
            }
        }else{
            for (int i = 0; i < onePlay; i++) {
                if (isEatingItself()){
                    gameOver(scene);
                }
                checkIsOutOfTheMap(scene);
                if (shouldUpdate){
                    setupObjects(scene);
                }
                if (directionC){
                    wait = 0f;
                    directionC = false;
                }
                if (wait<=0){
                    checkDirection();
                    updatePosition(calcAdd());
                    updateSprites(scene,calcSprites(scene));
                    wait = ms;
                }else{
                    checkDirection();
                    wait -=dt;
                }
            }
            onePlay = 0;
        }
    }
    boolean directionC;
    public void checkDirection(){
        if (CEngine.KEYBOARD.isKeyJustPressed(GLFW.GLFW_KEY_RIGHT)){
            if (direction!= Direction.LEFT){
                direction = Direction.RIGHT;
            }
            directionC = true;
        }
        if (CEngine.KEYBOARD.isKeyJustPressed(GLFW.GLFW_KEY_LEFT)){
            if (direction!= Direction.RIGHT){
                direction = Direction.LEFT;
            }
            directionC = true;
        }
        if (CEngine.KEYBOARD.isKeyJustPressed(GLFW.GLFW_KEY_UP)){
            if (direction!= Direction.DOWN){
                direction = Direction.UP;
            }
            directionC = true;
        }
        if (CEngine.KEYBOARD.isKeyJustPressed(GLFW.GLFW_KEY_DOWN)){
            if (direction!= Direction.UP){
                direction = Direction.DOWN;
            }
            directionC = true;
        }
    }

    public boolean isGameOver() {
        return isGameOver;
    }
    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

    public enum  Direction{
        UP,DOWN,RIGHT,LEFT;
    }
    Direction direction = Direction.RIGHT;
    public void setDirection(Direction direction) {
        this.direction = direction;
    }
    public Vector2f calcAdd(){
        Vector2f add = null;
        switch (direction){
            case RIGHT -> {
                add = new Vector2f(-48f,0f);
            }
            case LEFT -> {
                add = new Vector2f(48f,0f);
            }
            case DOWN -> {
                add = new Vector2f(0f,-48f);
            }
            case UP ->  {
                add = new Vector2f(0f,48f);
            }
        }
        return add;
    }
    public C2DSprite calcSprites(CEScene scene){
        C2DSprite up_down = CEngine.RESOURCE_MANAGER.getSpriteSheet("snakeAtlas").getSprites().get(8).copyWithoutTransform();
        C2DSprite right_left = CEngine.RESOURCE_MANAGER.getSpriteSheet("snakeAtlas").getSprites().get(9).copyWithoutTransform();
        switch (direction){
            case UP -> {
                up_down.setScale(new Vector2f(48,48));
                up_down.setZPos(10f);
                up_down.setDirection("up");
                return up_down;
            }
            case LEFT -> {
                right_left.setScale(new Vector2f(48,48));
                right_left.setZPos(10f);
                right_left.setDirection("left");
                return right_left;
            }
            case DOWN -> {
                up_down.setScale(new Vector2f(48,48));
                up_down.setZPos(10f);
                up_down.setDirection("down");
                return up_down;
            }
            case RIGHT -> {
                right_left.setScale(new Vector2f(48,48));
                right_left.setZPos(10f);
                right_left.setDirection("right");
                return right_left;
            }
            default -> {
                return null;
            }
        }
    }
    public boolean isEatingItself() {
        Vector2f headPos = bodies.get(0).pos(); // Yılanın başı
        for (int i = 1; i < bodies.size(); i++) {
            if (headPos.equals(bodies.get(i).pos())) {
                return true; // Kafanın herhangi bir gövde parçasına çarpması
            }
        }
        return false;
    }

    float count = 0f;
    boolean shouldUpdate;
    public void addBodyOnRun(SnakeBody body,CEScene scene){
        //body.object.addComponent("debug",new C2DDebugDraw());
        C2DSprite sprite = CEngine.RESOURCE_MANAGER.getSpriteSheet("snakeAtlas").getSprites().get(8).copyWithoutTransform();
        sprite.setPosition(new Vector2f(-10000));
        sprite.setScale(new Vector2f(48,48));
        sprite.setZPos(10f+count);
        sprite.setDirection(direction.toString().toLowerCase(Locale.ROOT));
        scene.getRenderer().add(sprite);
        body.object.addComponent("sprite",sprite);
        body.object.setParent(scene.objectSystem());
        bodies.add(body);
        shouldUpdate  =true;
        count+=1f;
    }
    public void removeBodyOnRun(CEScene scene){
        Snake snake = this;
        scene.getObject(snake.getID(snake.getBodies().size()-1)).getSprite("sprite").setScale(new Vector2f(0,0));
        scene.objectSystem().getObjects().remove(snake.getID(snake.getBodies().size()-1));
        snake.getBodies().remove(snake.getBodies().get(snake.getBodies().size()-1));
    }
    public void checkIsOutOfTheMap(CEScene scene){
        Snake snake = this;
        if (snake.getBodies().get(0).pos().x>480||
                snake.getBodies().get(0).pos().x<-480||
                snake.getBodies().get(0).pos().y>480||
                snake.getBodies().get(0).pos().y<-480){
            snake.gameOver(scene);
            if (snake.getBodies().size()<=2){
                snake.getBodies().get(0).pos().set(new Vector2f(24,-24));
            }else{
                snake.getBodies().get(0).pos().set(new Vector2f(480-24,-480+24));
            }
        }
    }
    public int getHighScore(){
        int rtrn = 0; // Başlangıçta yüksek skor 0 olarak ayarlandı
        for (int score : scores) {
            rtrn = Math.max(rtrn, score); // Her skoru rtrn ile karşılaştırarak en yükseğini bul
        }
        return rtrn;
    }
    public List<Integer> scores = new ArrayList<>();
    boolean isGameOver;
    public void gameOver(CEScene scene){
        isGameOver = true;
        onePlay = 20;
        int newScore = score;
        if (bodies.size()<=2){
            scores.add(newScore);
            setScore(0);
            return;
        }
      do {
          removeBodyOnRun(scene);
      }while (bodies.size()>2);
      scores.add(score);
      setScore(0);
    }
}
