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

    private static final float BOX_TO_SCREEN = 5f;   //every 1 unit in Box2d is 5 in our world
    private static final float SCREEN_TO_BOX = 1f / BOX_TO_SCREEN;

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
        world = new World(new Vector2(0, 9.8f * SCREEN_TO_BOX), false);
        shape = new PolygonShape();

        createBodyBox();
        createAllWalls();
        ball1 = createBall(10);
        ball2 = createBall(20);
        ball3 = createBall(30);
        time = System.currentTimeMillis();
    }

    private void createAllWalls() {
        int tempNum = 1;
        createWall(BOX_WIDTH, tempNum, tempNum, tempNum);     //top wall
        createWall(tempNum, BOX_WIDTH, tempNum, tempNum);     //left wall
        createWall(BOX_MARGIN, BOX_WIDTH, BOX_WIDTH, tempNum);  //right wall
        createWall(BOX_WIDTH, tempNum, tempNum, BOX_WIDTH);   //bottom wall
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
        shape.setAsBox(hx * SCREEN_TO_BOX, hy * SCREEN_TO_BOX, new Vector2(x * SCREEN_TO_BOX, y * SCREEN_TO_BOX), 0);
        boxBody.createFixture(fixtureDef);
    }

    private Body createBall(int radius) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.position.set(radius * SCREEN_TO_BOX, BOX_MARGIN * SCREEN_TO_BOX);
        Body ball = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape circle = new CircleShape();
        circle.setRadius(radius * SCREEN_TO_BOX);
        fixtureDef.shape = circle;
        fixtureDef.restitution = 1;
        ball.createFixture(fixtureDef);
        ball.applyForceToCenter(radius*BOX_WIDTH * SCREEN_TO_BOX, radius*BOX_WIDTH * SCREEN_TO_BOX, true);
        return ball;
    }

    @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);

            long currentTime = System.currentTimeMillis();
            world.step((currentTime - time) / 1000f, 6, 2); //amount of millis since last time called paintComponent
            time = currentTime;

            graphics.drawLine(1, BOX_WIDTH, (BOX_WIDTH - BOX_MARGIN), BOX_WIDTH);                  //bottom wall
            graphics.drawLine((BOX_WIDTH - BOX_MARGIN), BOX_MARGIN, (BOX_WIDTH - BOX_MARGIN), BOX_WIDTH);   //right wall
            graphics.drawLine(1, BOX_MARGIN, 1, BOX_WIDTH);                               //left wall
            graphics.drawLine(1, BOX_MARGIN, (BOX_WIDTH - BOX_MARGIN), BOX_MARGIN);                //top wall
            drawBall(graphics, ball1);
            drawBall(graphics, ball2);
            drawBall(graphics, ball3);

            repaint();
        }

        public void drawBall(Graphics graphics, Body ball) {
            int radius = (int) (ball.getFixtureList().get(0).getShape().getRadius() * BOX_TO_SCREEN);
            graphics.fillOval((int) (ball.getPosition().x * BOX_TO_SCREEN - radius), (int) (ball.getPosition().y * BOX_TO_SCREEN - radius),
                    radius * 2, radius * 2);
        }
}
