package org.geotools.geometry.jts;

import static org.junit.Assert.*;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

import org.junit.Test;

public class TransformedShapeTest {

    @Test
    public void testBounds() {
        GeneralPath gp = new GeneralPath();
        gp.moveTo(0f, 0f);
        gp.lineTo(1, 0.3);
        gp.lineTo(0f, 0.6f);
        gp.closePath();
        
        AffineTransform at = AffineTransform.getScaleInstance(1.5, 1.5);
        TransformedShape ts = new TransformedShape(gp, at);

        assertEquals(new Rectangle(0, 0, 2, 1), ts.getBounds());
    }
}
