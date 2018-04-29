package org.geotools.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.List;
import org.geotools.referencing.crs.DefaultProjectedCRS;
import org.geotools.referencing.operation.builder.MappedPosition;
import org.junit.Test;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

/**
 * Test For MapInfoFileReader
 *
 * @author Niels Charlier, Scitus Development
 */
public class MapInfoFileReaderTest {

    @Test
    public void testReaderFile() throws Exception {
        URL tabFile = getClass().getResource("test-data/london.tab");
        MapInfoFileReader reader = new MapInfoFileReader(tabFile);

        List<MappedPosition> controlPoints = reader.getControlPoints();

        assertEquals(3, controlPoints.size());
        assertEquals(0, controlPoints.get(0).getSource().getOrdinate(0), 0);
        assertEquals(0, controlPoints.get(0).getSource().getOrdinate(1), 0);
        assertEquals(1, controlPoints.get(1).getSource().getOrdinate(0), 0);
        assertEquals(0, controlPoints.get(1).getSource().getOrdinate(1), 0);
        assertEquals(0, controlPoints.get(2).getSource().getOrdinate(0), 0);
        assertEquals(1, controlPoints.get(2).getSource().getOrdinate(1), 0);
        assertEquals(297055, controlPoints.get(0).getTarget().getOrdinate(0), 0);
        assertEquals(5717803, controlPoints.get(0).getTarget().getOrdinate(1), 0);
        assertEquals(297065, controlPoints.get(1).getTarget().getOrdinate(0), 0);
        assertEquals(5717803, controlPoints.get(1).getTarget().getOrdinate(1), 0);
        assertEquals(297055, controlPoints.get(2).getTarget().getOrdinate(0), 0);
        assertEquals(5717793, controlPoints.get(2).getTarget().getOrdinate(1), 0);

        MathTransform transform = reader.getTransform();

        assertNotNull(transform);

        CoordinateReferenceSystem crs = reader.getCRS();

        assertTrue(crs instanceof DefaultProjectedCRS);
        DefaultProjectedCRS geocrs = (DefaultProjectedCRS) crs;
        assertEquals("WGS_1984", geocrs.getDatum().getName().toString());
        assertEquals(
                "Transverse_Mercator",
                geocrs.getConversionFromBase().getMethod().getName().toString());
    }
}
