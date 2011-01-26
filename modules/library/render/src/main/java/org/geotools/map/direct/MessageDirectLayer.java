package org.geotools.map.direct;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DirectLayer;
import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;
import org.geotools.util.Converters;

/**
 * This is a simple watermark used to place a message (such as copyright information) onto a map.
 * 
 * @author Jody
 */
public class MessageDirectLayer extends DirectLayer {

    /**
     * Message to be displayed. While it is perfectly alright to just use the getUserData() map we
     * are making use of an field here in order to verify the ability to fire events correctly when
     * the message is changed.
     */
    private String message;

    /**
     * Key used to store optional color in getUserData()
     */
    private static final String COLOR = "color";

    /**
     * Key used to store x position in getUserData().
     */
    public static final String X = "x";

    /**
     * Key used to store y position in getUserData()
     */
    public static final String Y = "y";

    public MessageDirectLayer(String message) {
        this.message = message;
    }

    @Override
    public void draw(Graphics2D graphics, MapContent map, MapViewport viewport) {
        if (viewport == null) {
            viewport = map.getViewport(); // use the map viewport if one has not been provided

        }
        if (viewport == null || viewport.getScreenArea() == null) {
            return; // renderer is not set up for use yet

        }
        if (message == null || message.length() == 0) {
            return; // no message to draw
        }
        Rectangle2D screen = rectangle2D(viewport.getScreenArea());
        Double dx = Converters.convert(getUserData().get(X), Double.class);
        Double dy = Converters.convert(getUserData().get(Y), Double.class);
        if (dx == null) {
            dx = screen.getWidth();
        }
        if (dy == null) {
            dy = screen.getHeight();
        }
        FontMetrics fm = graphics.getFontMetrics();
        Rectangle2D text = fm.getStringBounds(message, graphics);
        double x = position( screen.getX(), screen.getWidth(), dx, text.getWidth() );
        double y = position( screen.getY(),screen.getHeight(), dy, text.getHeight() );
     
        Color color = Converters.convert( getUserData().get(COLOR), Color.class );
        Color previousColor = graphics.getColor();
        try {
            if( color != null ){
                graphics.setColor(color);
            }
            graphics.drawString( message, (float) x, (float) y );
        }
        finally {
            if( color != null ){
                graphics.setColor( previousColor );
            }
        }

    }

    static double position(double x, double width, double dx, double span) {
        double ratio = dx / width;

        if (ratio < 0.3) {
            // left alignment
            return x + dx;
        } else if (ratio > 0.7) {
            // right alignment
            return x+dx-span;
        }
        else {
            // center alignment
            return x + dx - span/2.0;
        }
    }

    static Rectangle2D rectangle2D(Rectangle rectangle) {
        if (rectangle == null) {
            return null;
        }
        return new Rectangle2D.Double(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    /**
     * Does not contribute a bounding box to the map.
     * 
     * @return null
     */
    @Override
    public ReferencedEnvelope getBounds() {
        return null;
    }

}
