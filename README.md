# CEngine

This project contains a simple 2D game scene developed using the CEngine. CEngine is a custom game engine built with LWJGL (Lightweight Java Game Library) and OpenGL, providing optimized graphics and resource management.

  
## Features

- **Custom Game Engine**: Built from scratch, allowing full control over graphics and resource handling.
- **2D Rendering**: Efficient 2D sprite rendering using batching techniques.
- **Physics Engine**: Integration with JPhysics library for handling object physics.
- **Animation System**: Play character animations depending on actions like jumping, falling, and running. (has many problems)

## Requirements

- **Java**: Java 8 or higher
- **JavaFX** (for console)
- **LWJGL**: Version 3.x or higher
- **OpenGL**: Modern OpenGL context for rendering

## How To Clone and Test
1. Open the project as maven project on your IDE.
2. and run the Starter.java class for testing the engine. If you can open the test game and run it then there should be no problem.

## How to Start
1.create your main game class. you can implement HasLogger to let engine use your logger to log important things.
```java
public class MainGame implements CEComponent, HasLogger {
    @Override
    public void init() {
        
    }

    @Override
    public void loop() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public CLogger getLogger() {
        return CLoggerSystem.logger(this.getClass());
    }
}
```
2. add your component to engine before running and run.
```java
   public class Starter {
   public static void main(String[] args) {
   CEngine.setParentPublicComponent(new MainGame());
   CEngine.ENGINE.run();
   }
   }
```
3. create the scene class
```java
public class MainGameScene1 extends CEScene {
    @Override
    public void init() {
        super.init();
    }
    @Override
    public void update(double dt) {
        super.update(dt);
    }

    @Override
    public String getName() {
        return "Scene_1";
    }
}


```

4. set scene
```java
public class MainGame implements CEComponent, HasLogger {
    @Override
    public void init() {
        CEngine.SCENE.setScene(new MainGameScene1());
    }

    @Override
    public void loop() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public CLogger getLogger() {
        return CLoggerSystem.logger(this.getClass());
    }
}
```
5. see the example in the io.github.cakilgan.game package.
## Games
 ### SnakeGame
 ![img1.png](img1.png)
 ![img2.png](img2.png)
 - Use arrow keys to play.
 - Press enter the try again when you lost.
 - Shows the fps,move-ms,score
 - It stores high score in highscore.dat and loads from there when game is restarted.
## To-Do

- Add more game examples.
- Implement sound management.
- Improve physics performance.
- Improve Animation System and fix the delay issue.

