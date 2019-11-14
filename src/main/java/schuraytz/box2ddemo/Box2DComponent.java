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

    private final World world;
    private PolygonShape shape;
    private Body ball1;
    private Body ball2;
    private Body ball3;
    private long time;
    private final int BOX_WIDTH = 200;
    private final int BOX_MARGIN = 10;
    private Body boxBody;

    public Box2DComponent() {
        World.setVelocityThreshold(0);
        world = new World(new Vector2(0, 9.8f), false);
        shape = new PolygonShape();

        createBodyBox();
        createAllWalls();
        ball1 = createBall(10);
        ball2 = createBall(20);
        ball3 = createBall(30);
        time = System.currentTimeMillis();
    }

    private void createAllWalls() {
        createWall(BOX_WIDTH, 0, 0, 0);     //top wall
        createWall(0, BOX_WIDTH, 0, 0);     //left wall
        createWall(BOX_MARGIN, BOX_WIDTH, BOX_WIDTH, 0);  //right wall
        createWall(BOX_WIDTH, 0, 0, BOX_WIDTH);   //bottom wall
    }

    private void createBodyBox() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.StaticBody;
        boxBody = world.createBody(bodyDef);
    }

    private void createWall(int hx, int hy, int x, int y) {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.restitution = 1;
        shape.setAsBox(hx, hy, new Vector2(x, y), 0);
        boxBody.createFixture(fixtureDef);
    }

    private Body createBall(int radius) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.position.set(radius, BOX_MARGIN);
        Body ball = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape circle = new CircleShape();
        circle.setRadius(radius);
        fixtureDef.shape = circle;
        fixtureDef.restitution = radius;
        ball.createFixture(fixtureDef);
        ball.applyForceToCenter(radius*BOX_WIDTH, radius*BOX_WIDTH, true);
        return ball;
    }

    @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);

            long currentTime = System.currentTimeMillis();
            world.step((currentTime - time) / 1000f, 6, 2); //amount of millis since last time called paintComponent
            time = currentTime;

            graphics.drawLine(BOX_MARGIN, BOX_WIDTH, BOX_WIDTH - BOX_MARGIN, BOX_WIDTH);    //bottom wall
            graphics.drawLine(BOX_WIDTH - BOX_MARGIN, BOX_MARGIN, BOX_WIDTH - BOX_MARGIN, BOX_WIDTH);         //right wall
            graphics.drawLine(BOX_MARGIN, BOX_MARGIN, BOX_MARGIN, BOX_WIDTH);                               //left wall
            graphics.drawLine(BOX_MARGIN, BOX_MARGIN, BOX_WIDTH - BOX_MARGIN, BOX_MARGIN);                               //top wall
            drawBall(graphics, ball1, 10);
            drawBall(graphics, ball2, 20);
            drawBall(graphics, ball3, 30);

            repaint();
        }

        public void drawBall(Graphics graphics, Body ball, int radius) {
            graphics.fillOval((int) ball.getPosition().x, (int) ball.getPosition().y,
                    radius, radius);
        }
}
