# Marodi Game Library
## Intro
Marodi functions as more of an engine than a framework. 
While using it, update/draw methods are written by the game dev, 
and then called by the library when needed. 
Marodi as well handles physics, collision, and camera scrolling.
### Starting to code with the library
To utilize the library one must create a _Game_ object, 
by either creating an instance of the 
_Game_ class directly or creating a subclass and creating an instance of that. 
Below is an example of a _Game_ subclass.
Creating sprites is as well practically necessary for any game. 
Below also an example of how to make a basic _Player_ sprite class.
```
package org.game;

import control.Game;

class App extends Game {

    Player player = new Player();

    App() {
        getCurrentWorld().getVector2D().add(player);
    }
}
```
```
package org.game;

import component.sprite.Sprite;
import control.Game;
import java.awt.*;
import static java.awt.event.KeyEvent.*;

class Player extends Sprite {

    @Override
    public void start(Game game) {
        setResistance(0.1, game);
    }

    @Override
    public void draw(Game game) {
        game.camera.drawRect(50,50, new Color(255, 255, 255), this);
    }

    @Override
    public void update(Game game) {
        double acc = 100;
        if (game.keyHandler.isPressed(VK_UP))
            velocityY += acc * game.getFrameTime();
        if (game.keyHandler.isPressed(VK_DOWN))
            velocityY -= acc * game.getFrameTime();
        if (game.keyHandler.isPressed(VK_LEFT))
            velocityX -= acc * game.getFrameTime();
        if (game.keyHandler.isPressed(VK_RIGHT))
            velocityX += acc * game.getFrameTime();
    }
}
```
Now to launch the game, we must create an instance of the _App_ class 
and call its _launch()_ method.
```
package org.game;

public class Main {
    public static void main(String[] args) {
        App game = new App();
        game.launch();
    }
}

```