# Marodi Game Library
## Intro
Marodi functions as more of an engine than a framework. 
While using it, update/draw methods are written by the game dev, 
and then called by the library when needed. 
Marodi as well handles physics, collision, and camera scrolling.
### Starting to code with the library
To utilize the library, one must create a _Game_ object 
by creating a subclass and creating an instance of that.
Below is an example of a _Game_ subclass.
Creating sprites is as well practically necessary for any game. 
Below also an example of how to make a basic _Player_ sprite class.
```
package org.game;

import marodi.control.Game;

class App extends Game {

    Player player = new Player();

    App() {
        getCurrentWorld().add(player);
    }
}
```
```
package org.game;

import marodi.component.Sprite;
import marodi.control.Game;
import marodi.control.KeyHandler;

import java.awt.*;

class Player extends Sprite {

    @Override
    public void start(Game game) {
        // sets resistance to 98% per second
        setResistance(0.98f, game);
        // sets position
        x = 500;
        y = 500;
    }

    @Override
    public void draw(Game game) {
        // draws 50px wide white square at position of sprite
        game.camera.drawRect(50,50, new Color(255, 255, 255), this);
    }

    @Override
    public void update(Game game) {
        float acc = 1500; // acc/sec, not considering resistance
        if (game.keyHandler.isPressed(KeyHandler.OF_UP))
            velocityY += acc * game.getFrameProportion();
        if (game.keyHandler.isPressed(KeyHandler.OF_DOWN))
            velocityY -= acc * game.getFrameProportion();
        if (game.keyHandler.isPressed(KeyHandler.OF_LEFT))
            velocityX -= acc * game.getFrameProportion();
        if (game.keyHandler.isPressed(KeyHandler.OF_RIGHT))
            velocityX += acc * game.getFrameProportion();
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
Well, How does this work? First off, the _start_ method sets the position to 
(500, 500) and sets the resistance of
the sprites to 0.98 to slow it down by 98% every second. The _draw_ method calls
a method from the camera object to draw a 50x50 white rectangle at the position
of the sprite. The _update_ method first checks if specific sets of keys are
being pressed. Lastly, it determines how to accelerate by multiplying the
acceleration by the time the most recent frame took.