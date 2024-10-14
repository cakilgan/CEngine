package io.github.cakilgan.editor.core;

public class MapCore {
    public String mapName;
    public int mapX,mapY;
    public int mapWidth,mapHeight;
    public int objWidth,objHeight;
    public void set(String mapName,
                    int mapX,
                    int mapY,
                    int mapWidth,
                    int mapHeight,
                    int objWidth,
                    int objHeight){
        this.mapName = mapName;
        this.mapX = mapX;
        this.mapY = mapY;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.objWidth = objWidth;
        this.objHeight = objHeight;
    }
}
