package schuraytz.box2ddemo;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.awt.Graphics2D;
import java.util.Iterator;

public class Box2DRenderer {

    private World world;        //don't need bodies b/c the bodies come from the world itself

    public Box2DRenderer(World world) {
        this.world = world;
    }

    private void render(Graphics2D graphics) {

        Array<Body> bodies = new Array<>();
        world.getBodies(bodies);            //the world is populating the bodies array

        for (Body body : bodies) {
            Vector2 position = body.getPosition();                      // so we know where to draw it
            Shape shape = body.getFixtureList().get(0).getShape();      //each fixture has a shape
            Shape.Type type = shape.getType();                          //circle, edge, polygon
            float angle = body.getAngle();                              //angle of rotation
            float radius = shape.getRadius();

            switch (type) {
                case Circle: {
                    graphics.fillOval((int) (position.x - radius), (int) (position.y - radius), (int) (radius * 2), (int) (radius * 2));
                }
            }
        }

    }

}
