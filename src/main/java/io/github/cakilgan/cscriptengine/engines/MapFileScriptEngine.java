package io.github.cakilgan.cscriptengine.engines;

import io.github.cakilgan.cgraphics.c2d.render.sprite.C2DSprite;
import io.github.cakilgan.cgraphics.c2d.render.sprite.C2DSpriteSheet;
import io.github.cakilgan.clogger.util.Text;
import io.github.cakilgan.cresourcemanager.resources.FileResource;
import io.github.cakilgan.cresourcemanager.resources.file.TextureFileResource;
import io.github.cakilgan.cscriptengine.CakilganScriptEngine;
import io.github.cakilgan.cscriptengine.comp.Keyword;
import io.github.cakilgan.engine.CEngine;
import io.github.cakilgan.engine.map.C2DMap;
import io.github.cakilgan.physics.CPBody;
import library.geometry.Polygon;
import library.math.Vectors2D;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;

public class MapFileScriptEngine extends CakilganScriptEngine {
    int startPosition_x,startPosition_y;
    String mapName;

    int mapHeight,mapWidth,objectHeight,objectWidth;
    HashMap<C2DSprite,String> sprites;
    HashMap<Integer,List<String>> spriteCodes;
    HashMap<Integer,String> bodyCodes;
    HashMap<String,C2DSpriteSheet> spriteSheetHashMap;
    HashMap<Integer,Integer> zPos;
    HashMap<String,String> pointers;
    HashMap<String,String> bodyPointers;
    public C2DMap parse(FileResource fileResource){
        sprites = new HashMap<>();
        spriteCodes = new HashMap<>();
        spriteSheetHashMap = new HashMap<>();
        pointers = new HashMap<>();
        zPos = new HashMap<>();
        bodyPointers = new HashMap<>();
        bodyCodes = new HashMap<>();
        Keyword<String> constructor = new Keyword<>("constructor",String.class){
            @Override
            public void action(String value) {
                List<String> values = Text.convertDottedStringToList(value);
                startPosition_x = Integer.parseInt(values.get(0));
                startPosition_y = Integer.parseInt(values.get(1));
                mapName = values.get(2);
                super.action(value);
            }
        };
        Keyword<String> init = new Keyword<>("init",String.class){
            @Override
            public void action(String value) {
                List<String> values = Text.convertDottedStringToList(value);
                mapHeight = Integer.parseInt(values.get(0));
                mapWidth = Integer.parseInt(values.get(1));
                objectHeight = Integer.parseInt(values.get(2));
                objectWidth = Integer.parseInt(values.get(3));
                super.action(value);
            }
        };
        Keyword<String> addSpriteSheet = new Keyword<>("addSpriteSheet",String.class){
            @Override
            public void action(String value) {
                List<String> values = Text.convertDottedStringToList(value);
                for (String string : values) {
                    List<String> value2 = Text.makeList(string);
                    if (value2.get(0).equals("-fromManager")){
                        String name = value2.get(1);
                        String code = value2.get(2);
                        spriteSheetHashMap.put(name,CEngine.RESOURCE_MANAGER.getSpriteSheet(code));
                        for (int i = 0; i < spriteSheetHashMap.get(name).getSprites().size(); i++) {
                            sprites.put(spriteSheetHashMap.get(name).getSprites().get(i),code+"["+i+"]");
                        }
                    }else{
                        String code = value2.get(0);
                        String path = value2.get(1);
                        int xScale = Integer.parseInt(value2.get(2));
                        int yScale = Integer.parseInt(value2.get(3));
                        C2DSpriteSheet spriteSheet = C2DSpriteSheet.readFromTextureMetadata(new TextureFileResource(new FileResource(path),new Vector2i(xScale,yScale)));
                        spriteSheetHashMap.put(code,spriteSheet);
                        for (int i = 0; i < spriteSheet.getSprites().size(); i++) {
                            sprites.put(spriteSheet.getSprites().get(i),code+"["+i+"]");
                        }
                    }
                }
                super.action(value);
            }
        };
        Keyword<String> addSprite = new Keyword<>("addSprite",String.class){
            @Override
            public void action(String value) {
                List<String> values = Text.convertDottedStringToList(value);
                for (String string : values) {
                    List<String> value2 = Text.makeList(string);
                    sprites.put(CEngine.RESOURCE_MANAGER.getSprite(value2.get(0)),value2.get(1));
                }
                super.action(value);
            }
        };
        Keyword<String> setSprite = new Keyword<>("setSprite",String.class){
            @Override
            public void action(String value) {
                List<String> values = Text.convertDottedStringToList(value);
                for (String string : values) {
                    List<String> value2 = Text.makeList(string);
                    if (value2.size()==1){

                    }else{
                    List<String> strings = new ArrayList<>();
                    strings.add(value2.get(1));
                    strings.add(value2.get(2));
                    spriteCodes.put(Integer.valueOf(value2.get(0)),strings);
                    }
                }
                super.action(value);
            }
        };
        Keyword<String> setZPos = new Keyword<>("setZPos",String.class){
            @Override
            public void action(String value) {
                List<String> values = Text.convertDottedStringToList(value);
                for (String string : values) {
                    List<String> value2 = Text.makeList(string);
                    if (value2.get(0).equals("all")){
                        int count = Integer.parseInt(value2.get(1));
                        for (int i = 0; i < count; i++) {
                            zPos.put(i, Integer.valueOf(value2.get(2)));
                        }
                    }else{
                    zPos.put(Integer.valueOf(value2.get(0)), Integer.valueOf(value2.get(1)));
                    }
                }
                super.action(value);
            }
        };
        Keyword<String> addPointer = new Keyword<>("addPointer",String.class){
            @Override
            public void action(String value) {
                List<String> values = Text.convertDottedStringToList(value);
                for (String string : values) {
                    List<String> value2 = Text.makeList(string);
                    String pointer = value2.get(0);
                    String pointerVal = value2.get(1);
                    if (value2.size()>2){
                        String bodyType = value2.get(2);
                        bodyPointers.put(pointer,bodyType);
                    }
                    pointers.put(pointer,pointerVal);
                }
                super.action(value);
            }
        };
        Keyword<String> parseMap = new Keyword<>("parseMap",String.class){
            @Override
            public void action(String value) {
                List<String> values = Text.convertDottedStringToList(value);
                    int count=0;
                for (String string : values) {
                    List<String> lines = Text.makeList(string);
                    for (int i = 0; i < lines.size(); i++) {
                        for (int i1 = 0; i1 < Text.convertDottedStringToList(lines.get(i)).size(); i1++) {
                           String a =  Text.convertDottedStringToList(lines.get(i)).get(i1);
                           List<String> strings = new ArrayList<>();
                           strings.add("sprite");
                           strings.add(pointers.get(a));
                           spriteCodes.put(count,strings);
                           if (bodyPointers.get(a)!=null){
                               bodyCodes.put(count,bodyPointers.get(a));
                           }
                            count++;
                        }
                    }
                }
                super.action(value);
            }
        };


        addKeyword(constructor);
        addKeyword(init);
        addKeyword(addSprite);
        addKeyword(setSprite);
        addKeyword(addSpriteSheet);
        addKeyword(setZPos);
        addKeyword(addPointer);
        addKeyword(parseMap);
        parse(fileResource.type.getContext());
        final C2DMap[] rtrn = new C2DMap[1];
        rtrn[0] = new C2DMap(new Vector2i(startPosition_x,startPosition_y),mapName);
        rtrn[0].init(mapHeight,mapWidth,objectHeight,objectWidth);
        rtrn[0].compile();
        sprites.forEach(new BiConsumer<C2DSprite, String>() {
            @Override
            public void accept(C2DSprite sprite, String s) {
                rtrn[0].addSprite(sprite,s);
            }
        });
        spriteCodes.forEach(new BiConsumer<Integer, List<String>>() {
            @Override
            public void accept(Integer integer, List<String> strings) {
                rtrn[0].setSprite(integer,strings.get(0),strings.get(1));
            }
        });
        spriteSheetHashMap.forEach(new BiConsumer<String, C2DSpriteSheet>() {
            @Override
            public void accept(String s, C2DSpriteSheet c2DSpriteSheet) {
                rtrn[0].spriteSheets.put(s,c2DSpriteSheet);
            }
        });
        zPos.forEach(new BiConsumer<Integer, Integer>() {
            @Override
            public void accept(Integer obj, Integer integer) {
                rtrn[0].getMapObjects()[obj].getObject().getTransform().setZPos(integer);
            }
        });
        bodyCodes.forEach(new BiConsumer<Integer, String>() {
            @Override
            public void accept(Integer integer, String s) {
                if (s.equals("static")){
                    CPBody<Polygon> body = new CPBody<>(new Polygon(objectWidth/2f,objectHeight/2f),new Vectors2D(rtrn[0].getMapObjects()[integer].getObject().getTransform().getPos().x,rtrn[0].getMapObjects()[integer].getObject().getTransform().getPos().y));
                    body.getBody().setDensity(0f);
                    rtrn[0].addBody(integer,"body",body);
                } else if (s.equals("dynamic")) {
                    CPBody<Polygon> body = new CPBody<>(new Polygon(objectWidth/2f,objectHeight/2f),new Vectors2D(rtrn[0].getMapObjects()[integer].getObject().getTransform().getPos().x,rtrn[0].getMapObjects()[integer].getObject().getTransform().getPos().y));
                    rtrn[0].addBody(integer,"body",body);
                }
            }
        });

        return rtrn[0];
    }
}
