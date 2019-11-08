package schuraytz.box2ddemo;

import javax.swing.*;
import java.awt.BorderLayout;

public class Box2DFrame extends JFrame {

    public Box2DFrame() {

        setSize(800, 800);
        setTitle("Box2DDemo");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());
        add(new Box2DComponent(), BorderLayout.CENTER);

    }

    public static void main(String[] args) {
        new Box2DFrame().setVisible(true);
    }
}
