package schuraytz.box2ddemo;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import javax.swing.JComponent;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;

public class Box2DComponent extends JComponent {

    private static final float BOX_TO_SCREEN = 5f;   //every 1 unit in Box2d is 5 in our world
    private static final float SCREEN_TO_BOX = 1f / BOX_TO_SCREEN;

    private final World world;
    private PolygonShape shape;
    private long time;
    private final int BOX_WIDTH = 200;
    private final int BOX_MARGIN = 10;
    private Body boxBody;
    private Body bodyA;
    private Body bodyB;

    private Vector2[] staticPoints = new Vector2[]{new Vector2(130, 140), new Vector2(130, 140),
                                new Vector2(170, 110), new Vector2(170, 100)};
    private Vector2[] dynamicPoints = new Vector2[]{new Vector2(100, 180), new Vector2(100, 170),
            new Vector2(130, 140), new Vector2(130, 150)};

    private PolygonShape polyShapeStatic;
    private PolygonShape polyShapeDynamic;
    private float desiredAngle;

    private RevoluteJoint joint;

    public Box2DComponent() {

        World.setVelocityThreshold(0);
        world = new World(new Vector2(0, 9.8f * SCREEN_TO_BOX), false);
        shape = new PolygonShape();
        createBodies();

        polyShapeStatic = new PolygonShape();
        polyShapeDynamic = new PolygonShape();
        setBodyShape(staticPoints, polyShapeStatic, bodyA);
        setBodyShape(dynamicPoints, polyShapeDynamic, bodyB);

        RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.initialize(bodyA, bodyB, bodyA.getWorldCenter());
        revoluteJointDef.collideConnected = false;
        revoluteJointDef.localAnchorA.set(1, 3);
        revoluteJointDef.localAnchorB.set(1, 1);
        revoluteJointDef.enableLimit = true;
        revoluteJointDef.lowerAngle = -30 * SCREEN_TO_BOX;
        revoluteJointDef.upperAngle = 30 * SCREEN_TO_BOX;

        joint = (RevoluteJoint) world.createJoint(revoluteJointDef);
        joint.enableMotor(true);
        joint.setMotorSpeed(10);
        joint.setMaxMotorTorque(25);

        createBodyBox();
        createAllWalls();


        this.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Vector2 force = new Vector2(200, 100);
                bodyB.applyForceToCenter(force, true);
                System.out.println("a: " + bodyA.getAngle() + " b: " + bodyB.getAngle());
                repaint();

            }
        });
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

    private void createBodies() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.StaticBody;
        bodyDef.position.set(50, 70);
        bodyA = world.createBody(bodyDef);

        bodyDef.type = BodyType.DynamicBody;
        bodyDef.position.set(80, 100);
        bodyB = world.createBody(bodyDef);
    }

    private void setBodyShape(Vector2[] points, PolygonShape polygonShape, Body body) {
        FixtureDef fixtureDef = new FixtureDef();
//        Vector2[] chainPoints = new Vector2[]{
//                new Vector2(xPoints[0], yPoints[0]), new Vector2(xPoints[1], yPoints[1]),
//                new Vector2(xPoints[2], yPoints[2]), new Vector2(xPoints[3], yPoints[3])};
        polygonShape.set(points);
        fixtureDef.shape = polygonShape;
        fixtureDef.density = 1 ;
        body.createFixture(fixtureDef);
    }

    private void createWall(int hx, int hy, int x, int y) {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.restitution = 1;
        shape.setAsBox(hx * SCREEN_TO_BOX, hy * SCREEN_TO_BOX, new Vector2(x * SCREEN_TO_BOX, y * SCREEN_TO_BOX), 0);
        boxBody.createFixture(fixtureDef);
    }

    @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);

            long currentTime = System.currentTimeMillis();
            world.step((currentTime - time) / 1000f, 6, 2); //amount of millis since last time called paintComponent
            time = currentTime;

            graphics.drawLine(1, BOX_WIDTH, (BOX_WIDTH - BOX_MARGIN), BOX_WIDTH);                        //bottom wall
            graphics.drawLine((BOX_WIDTH - BOX_MARGIN), BOX_MARGIN, (BOX_WIDTH - BOX_MARGIN), BOX_WIDTH);    //right wall
            graphics.drawLine(1, BOX_MARGIN, 1, BOX_WIDTH);                                         //left wall
            graphics.drawLine(1, BOX_MARGIN, (BOX_WIDTH - BOX_MARGIN), BOX_MARGIN);                     //top wall


//            graphics.drawPolygon(xPointsDynamic, yPointsDynamic, xPointsDynamic.length);
//            graphics.drawPolygon(xPointsStatic, yPointsStatic, xPointsStatic.length);
//            graphics.drawPolygon(polyShapeStatic);
//            graphics.drawPolygon(polyShapeStatic, , polyShapeStatic.getVertexCount());

            repaint();
        }

        public void drawBall(Graphics graphics, Body ball) {
            int radius = (int) (ball.getFixtureList().get(0).getShape().getRadius() * BOX_TO_SCREEN);
            graphics.fillOval((int) (ball.getPosition().x * BOX_TO_SCREEN - radius), (int) (ball.getPosition().y * BOX_TO_SCREEN - radius),
                    radius * 2, radius * 2);
        }
}
