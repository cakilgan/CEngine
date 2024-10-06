package io.github.cakilgan.game.scene;

import io.github.cakilgan.cgraphics.c2d.render.sprite.C2DSpriteSheet;
import io.github.cakilgan.cgraphics.c2d.render.font.C2DFontRenderer;
import io.github.cakilgan.cscriptengine.engines.MapFileScriptEngine;
import io.github.cakilgan.cgraphics.c2d.render.anim.C2DAnimation;
import io.github.cakilgan.cgraphics.c2d.render.sprite.C2DSprite;
import io.github.cakilgan.engine.system.ecs.comp.CEOCameraLock;
import io.github.cakilgan.engine.system.ecs.comp.CEOTransform;
import io.github.cakilgan.cgraphics.c2d.render.C2DDebugDraw;
import io.github.cakilgan.cgraphics.c2d.render.font.C2DFont;
import io.github.cakilgan.engine.system.ecs.util.CEObjectID;
import io.github.cakilgan.cgraphics.c2d.render.C2DTexture;
import io.github.cakilgan.engine.system.ecs.core.CEObject;
import io.github.cakilgan.engine.map.comp.C2DMapObject;
import io.github.cakilgan.engine.window.scene.CEScene;
import io.github.cakilgan.engine.map.C2DMap;
import io.github.cakilgan.physics.CPBody;
import io.github.cakilgan.engine.CEngine;
import library.geometry.Polygon;
import library.math.Vectors2D;
import library.dynamics.Body;
import org.lwjgl.glfw.GLFW;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;


public class MainGameScene1 extends CEScene {
    enum Facing{RIGHT,LEFT}
    C2DFontRenderer fontRenderer;
    CEObjectID backGroundID = new CEObjectID("backGround");
    CEObjectID fpsObjectID = new CEObjectID("fpsObject");
    CEObjectID characterID = new CEObjectID("character");
    CEObjectID groundID = new CEObjectID("ground");
    CEObjectID enemyID = new CEObjectID("enemy");
    CEObjectID shopID = new CEObjectID("shop");
    Facing facing = Facing.RIGHT;
    C2DAnimation animation;
    C2DMap map;
    @Override
    public void init() {

        C2DFont font = new C2DFont(
                new C2DTexture(CEngine.RESOURCE_MANAGER.textures().getFile("default_font_2.png"),new Vector2i(816,306)),
                51,
                51,
                16,
                6,
                0
                ," !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~");
        font.set(0,0);
        fontRenderer = new C2DFontRenderer(font,new Vector2f(32,32));

        CEObject fpsObject = new CEObject(fpsObjectID);
        fpsObject.addTransform(new CEOTransform(new Vector2f(500,500),new Vector2f(32*50,32),0f));
        //fpsObject.addComponent("debug",new C2DDebugDraw());
        fpsObject.getTransform().setZPos(50f);
        fpsObject.addComponent("camLock",new CEOCameraLock(new Vector2f(-32*25,1048)));

        fontRenderer.setParent(fpsObject);
        fontRenderer.init();

        getCamera().getPosition().set(new Vector3f(2400,-1200,getCamera().getPosition().z));
        getDebugRenderer().setShouldDebugRenderJPhysics(false);

        C2DSprite b1 = CEngine.RESOURCE_MANAGER.getSprite("b1").copyWithoutTransform();
        b1.setDontSyncZpos(true);
        b1.setScale(new Vector2f(4800,2400));
        b1.setZPos(0f);

        C2DSprite b2 = CEngine.RESOURCE_MANAGER.getSprite("b2").copyWithoutTransform();
        b2.setDontSyncZpos(true);
        b2.setScale(new Vector2f(4800,2400));
        b2.setZPos(1f);

        C2DSprite b3 = CEngine.RESOURCE_MANAGER.getSprite("b3").copyWithoutTransform();
        b3.setDontSyncZpos(true);
        b3.setScale(new Vector2f(4800,2400));
        b3.setZPos(2f);

        // making a layered background using zPos
        CEObject backGround = new CEObject(backGroundID);
        backGround.addTransform(new CEOTransform(new Vector2f(0,0),new Vector2f(4800,2400),0f));
        //backGround.addComponent("debugDraw",new C2DDebugDraw());
        backGround.addComponent("layer1",b1);
        backGround.addComponent("layer2",b2);
        backGround.addComponent("layer3",b3);

        map = new MapFileScriptEngine().parse(CEngine.RESOURCE_MANAGER.getDirectoryResource("gameRes").getDir("data").getDir("map").getFile("map.c2dmap"));
        map.objectSetup(this);
        //map.setDebugDrawAll();

        CEngine.RESOURCE_MANAGER.getSpriteSheet("shop").resize(new Vector2f(500,500));

        C2DAnimation shopAnim = new C2DAnimation("sprite",CEngine.RESOURCE_MANAGER.getSpriteSheet("shop").getSprites().toArray(new C2DSprite[0]));
        shopAnim.setZPosAll(3f);
        shopAnim.setMs(0.25f);
        shopAnim.addAnim("main",new int[]{0,1,2,3,4,5});

        CEObject shop = new CEObject(shopID);
        shop.addTransform(new CEOTransform(new Vector2f(0,-1200+250+96),new Vector2f(500,500),0f));
        //shop.addComponent("debug",new C2DDebugDraw().setColorcode(new Vector3f(0.52f,1,1)));
        shop.addComponent("sprite",shopAnim.sprites()[0]);
        shop.addComponent("anim",shopAnim);

        C2DSpriteSheet normal = CEngine.RESOURCE_MANAGER.getSpriteSheet("character");
        normal.resize(new Vector2f(176,200));
        normal.repos(new Vector2f(0,40f));

        C2DSpriteSheet mirrored = C2DSpriteSheet.mirror(normal,true,false);
        mirrored.resize(new Vector2f(176,200));
        mirrored.repos(new Vector2f(0,40f));

        C2DSprite[] sprites = new C2DSprite[normal.getSprites().size()+mirrored.getSprites().size()];
        System.arraycopy(normal.getSprites().toArray(new C2DSprite[0]),0,sprites,0,normal.getSprites().size());
        System.arraycopy(mirrored.getSprites().toArray(new C2DSprite[0]),0,sprites,normal.getSprites().size(),mirrored.getSprites().size());

        animation = new C2DAnimation("sprite",sprites);
        animation.setZPosAll(5f);
        animation.setMs(0.8f);

        animation.addAnim("idle_r",new int[]{0,1,2,3,4,5});

        animation.addAnim("run_r",new int[]{16,17,18,19,20,21,22,23});
        animation.addAnim("idle_l",new int[]{56,57,58,59,60,61});
        animation.addAnim("run_l",new int[]{72,73,74,75,76,77,78,79});

        animation.addAnim("jump_up",new int[]{25,26,27,28,29,30});
        animation.addAnim("jump_down",new int[]{31,32,33,34,35,36,37,38});

        animation.addAnim("attack_r",new int[]{8,9,10,11,12,13});
        animation.addAnim("attack_l",new int[]{64,65,66,67,68,69});

        CPBody<Polygon> characterBody = new CPBody<>(new Polygon(48f,60f),new Vectors2D(map.getMapObjects()[1200].getPos().x-12*96,map.getMapObjects()[1200].getPos().y+12+48+48+600));
        characterBody.getBody().restitution = 0f;

        CEObject character = new CEObject(characterID);
        character.addComponent("anim",animation);
        character.addTransform(new CEOTransform(new Vector2f(),new Vector2f(96,120),0f));
        character.addComponent("sprite",CEngine.RESOURCE_MANAGER.getSpriteSheet("character").getSprites().get(0));
        //character.addComponent("debugDraw",new C2DDebugDraw().setColorcode(new Vector3f(1,1,0)));
        character.addComponent("body",characterBody);

        CEObject enemy = new CEObject(enemyID);
        enemy.addTransform(new CEOTransform(new Vector2f(-500,-1200+96+60),new Vector2f(96,120),0f));
        enemy.addComponent("debug",new C2DDebugDraw());
        C2DSprite sprite = CEngine.RESOURCE_MANAGER.getSpriteSheet("character").getSprites().get(0).copyWithoutTransform();
        sprite.setDontSyncZpos(true);
        sprite.setZPos(20f);
        sprite.setCompPos(new Vector2f(0,40f));
        sprite.setScale(new Vector2f(176,200));
        enemy.addComponent("sprite",sprite);

        addObject(fpsObject);
        addObject(shop);
        addObject(character);
        addObject(enemy);
        addObject(backGround);

        CPBody<Polygon> body = new CPBody<>(new Polygon(50*48f,48f),new Vectors2D(0f,-1152f));
        body.getBody().setDensity(0f);
        CEObject ground = new CEObject(groundID);
        ground.addTransform(new CEOTransform(new Vector2f(0,0f),new Vector2f(0f),0f));
        ground.addComponent("body",body);

        addObject(ground);
        super.init();
    }


    float attackCount = 0.55f;
    boolean isAttacking = false;
    @Override
    public void update(double dt) {
        getObject(characterID).getBody("body").getBody().orientation = 0f;
        getObject(characterID).getBody("body").getBody().dynamicFriction = 0f;
        getObject(characterID).getBody("body").getBody().staticFriction = 0f;
        getObject(characterID).getBody("body").getBody().velocity.x = 0f;
        getObject(characterID).getBody("body").getBody().invI = 0f;
        getObject(characterID).getBody("body").getBody().I = 0f;
        map.update(this);
        boolean isGrounded = false;
        Body characterBody = getObject(characterID).getBody("body").getBody();
        if (characterBody.velocity.y<0.1f&&characterBody.velocity.y>0f){
            characterBody.velocity.y = 0f;
        }
        float collisionTolerance = 0.1f;
        if (characterBody.velocity.y == 0f || Math.abs(characterBody.velocity.y) < collisionTolerance) {
            isGrounded = true;
        } else {
            isGrounded = false;
        }
        if (CEngine.KEYBOARD.isKeyJustPressed(GLFW.GLFW_KEY_H)){
            if (isGrounded){
                attackCount = 0.6f;
                isAttacking = true;
            }
        }
        if (CEngine.KEYBOARD.isKeyHolding(GLFW.GLFW_KEY_RIGHT)){
            if (isGrounded){
                animation.setMs(0.1f);
                animation.play("run_r", (float) dt);
            }
            getObject(characterID).getBody("body").getBody().position.add(new Vectors2D(-5f,0f));
            facing = Facing.RIGHT;
        }
        else if (CEngine.KEYBOARD.isKeyHolding(GLFW.GLFW_KEY_LEFT)) {
            if (isGrounded){
                animation.setMs(0.1f);
                animation.play("run_l", (float) dt);
            }
            getObject(characterID).getBody("body").getBody().position.add(new Vectors2D(5f,0f));
            facing = Facing.LEFT;
        } else{
            if (isGrounded&&!isAttacking){
                if (facing == Facing.RIGHT){
                    animation.setMs(0.1f);
                    animation.play("idle_r", (float) dt);
                }
                if (facing == Facing.LEFT){
                    animation.setMs(0.1f);
                    animation.play("idle_l", (float) dt);
                }
            } else if (isGrounded && isAttacking) {
                attackCount-= (float) dt;
                if (facing ==Facing.RIGHT){
                animation.play("attack_r", (float) dt);
                }else{
                    animation.play("attack_l", (float) dt);
                }
            }
        }
        if (attackCount<=0){
            isAttacking = false;
        }
        if (!isGrounded && characterBody.velocity.y > 0.2f) {
            if (facing == Facing.RIGHT){
            getObject(characterID).getSprite("sprite").set(animation.sprites()[31]);
            }else{
                getObject(characterID).getSprite("sprite").set(animation.sprites()[87]);
            }
        } else if (!isGrounded && characterBody.velocity.y < -1f) {
            if (facing == Facing.RIGHT){
                getObject(characterID).getSprite("sprite").set(animation.sprites()[32]);
            }else{
                getObject(characterID).getSprite("sprite").set(animation.sprites()[88]);
            }
        }
        if (CEngine.KEYBOARD.isKeyJustPressed(GLFW.GLFW_KEY_SPACE)){
            if (isGrounded){
                getObject(characterID).getBody("body").getBody().velocity.add(new Vectors2D(0,60f));
            }
        }
        fontRenderer.setText("FPS: "+(int)CEngine.TIME.getFps()+" obj: "+getDebugRenderer().getLines().size()+" c: "+map.getMapObjects().length+" velocity: "+ characterBody.velocity.y);
        if (CEngine.KEYBOARD.isKeyJustPressed(GLFW.GLFW_KEY_ENTER)){
            if (hasDebug){
                for (C2DMapObject mapObject : map.getMapObjects()) {
                    mapObject.getObject().removeComponent("draw");
                }
                hasDebug = false;
            }else{
                for (C2DMapObject mapObject : map.getMapObjects()) {
                    mapObject.getObject().addComponent("draw",new C2DDebugDraw());
                }
                hasDebug = true;
            }
        }
        //getCamera().smoothFollow(new Vector2f(getObject(characterID).getTransform().getPos().x,-696));
        ((C2DAnimation)getObject(shopID).getComponent("anim")).play("main", (float) dt);
        fontRenderer.update(dt);
        super.update(dt);
    }
    boolean hasDebug = false;

    @Override
    public String getName() {
        return "Scene_1";
    }
}

