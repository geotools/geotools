package org.geotools.renderer.lite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.geotools.data.DataUtilities;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.StyleBuilder;
import org.junit.Test;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;

public class GraphicsAwareDpiRescaleStyleVisitorTest {

    @Test
    public void testResizeMark() {
        StyleBuilder sb = new StyleBuilder();
        PointSymbolizer ps = sb.createPointSymbolizer(sb.createGraphic(null, sb.createMark("square"), null));
        GraphicsAwareDpiRescaleStyleVisitor visitor = new GraphicsAwareDpiRescaleStyleVisitor(2);
        ps.accept(visitor);
        PointSymbolizer resized = (PointSymbolizer) visitor.getCopy();
        Expression size = resized.getGraphic().getSize();
        assertTrue(size instanceof Literal);
        assertEquals(32, size.evaluate(null, Integer.class), 0d);
    }
    
    @Test
    public void testResizeExternalGraphic() throws IOException {
        StyleBuilder sb = new StyleBuilder();
        File imageFile = new File("./src/test/resources/org/geotools/renderer/lite/test-data/draw.png").getCanonicalFile();
        assertTrue(imageFile.exists());
        String fileUrl = DataUtilities.fileToURL(imageFile).toExternalForm();
        PointSymbolizer ps = sb.createPointSymbolizer(sb.createGraphic(null, null, sb.createExternalGraphic(fileUrl, "image/png")));
        GraphicsAwareDpiRescaleStyleVisitor visitor = new GraphicsAwareDpiRescaleStyleVisitor(2);
        ps.accept(visitor);
        PointSymbolizer resized = (PointSymbolizer) visitor.getCopy();
        Expression size = resized.getGraphic().getSize();
        assertTrue(size instanceof Literal);
        // original image height was 22
        assertEquals(44, size.evaluate(null, Integer.class), 0d);
    }
}
