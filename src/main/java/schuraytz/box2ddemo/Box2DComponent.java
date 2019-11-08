package schuraytz.box2ddemo;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import javax.swing.JComponent;
import java.awt.*;

public class Box2DComponent extends JComponent {

    private long time;

    private final World world;
    private Body ball;
    private Body ground;
 // private Body box;

    public Box2DComponent() {
        World.setVelocityThreshold(0);
        world = new World(new Vector2(0, 9.8f), false);

        createGround();
        createBall();
      //createBox();

        time = System.currentTimeMillis();

    }

    private void createGround() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.StaticBody;
        bodyDef.position.set(-100, 200f);
        ground = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(200f, 1f);       // a physics simulation shouldn't necessarily depend on the dimensions of the screen
        fixtureDef.shape = shape;
        fixtureDef.restitution = 1;     //makes it bounce off of the ground
        ground.createFixture(fixtureDef);
    }

    private void createBall() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        //next 2 lines cause ball to move in an arc and fall below ground level after first ground hit
//        bodyDef.angularVelocity = 40;
//        bodyDef.angle = (float) Math.toRadians(30);
        ball = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape circle = new CircleShape();
        circle.setRadius(20);
        fixtureDef.shape = circle;
        ball.createFixture(fixtureDef);

        ball.applyForceToCenter(200, -30, true);
    }
/*    private void createBox() {
        BodyDef boxDef = new BodyDef();
        boxDef.type = BodyType.DynamicBody;
        box = world.createBody(boxDef);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(5, 5);
        fixtureDef.shape = shape;
        box.createFixture(fixtureDef);
    }*/

    @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);

            long currentTime = System.currentTimeMillis();
            world.step((currentTime - time) / 1000f, 6, 2); //amount of millis since last time called paintComponent
            time = currentTime;

            // graphics.fillRect((int) box.getPosition().x, (int) box.getPosition().y, 5, 5);    //x, y is location of our body
            graphics.fillOval((int) ball.getPosition().x, (int) ball.getPosition().y, 20, 20);    //x, y is location of our body
            graphics.fillRect((int) ground.getPosition().x, (int) ground.getPosition().y, 200, 1);
            repaint();
        }
}
