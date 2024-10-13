package io.github.cakilgan.game.scene.snakeGame;

import org.joml.Vector2f;

public class SnakeHead extends SnakeBody{
    public SnakeHead(String id, Vector2f scale) {
        super(id, scale);
    }
    public SnakeHead(String id,Vector2f scale,Vector2f pos){
        super(id,scale,pos);
    }
}
