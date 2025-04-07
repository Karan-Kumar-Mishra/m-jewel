/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package customcomponents;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

/**
 *
 * @author ansar
 */
public class CustomCursorExample extends JFrame{
    
    private void createAndSetCustomCursor() {
        int cursorSize = 16; // Size of the cursor
        BufferedImage cursorImage = new BufferedImage(cursorSize, cursorSize, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = cursorImage.createGraphics();
        g2d.setColor(Color.RED); // Set your desired color here
        g2d.fillOval(0, 0, cursorSize, cursorSize);
        g2d.dispose();

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Point hotspot = new Point(0, 0); // The cursor's hotspot (tip)
        Cursor customCursor = toolkit.createCustomCursor(cursorImage, hotspot, "Custom Cursor");

        setCursor(customCursor);
        setCursorForAllComponents(this, customCursor);
    }

    private void setCursorForAllComponents(Container container, Cursor cursor) {
        container.setCursor(cursor);
        for (Component component : container.getComponents()) {
            if (component instanceof Container) {
                setCursorForAllComponents((Container) component, cursor);
            } else {
                component.setCursor(cursor);
            }
        }
    }
    
}
