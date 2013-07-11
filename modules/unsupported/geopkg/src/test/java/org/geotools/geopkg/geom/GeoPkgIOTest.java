package org.geotools.geopkg.geom;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.geotools.geometry.jts.GeometryBuilder;
import org.junit.Test;

import com.vividsolutions.jts.geom.Geometry;

public class GeoPkgIOTest {

    @Test
    public void testReadWrite() throws IOException {
        Geometry g1 = new GeometryBuilder().point(0,0).buffer(10);
        byte[] bytes = new GeoPkgGeomWriter().write(g1);

        Geometry g2 = new GeoPkgGeomReader().read(bytes);
        assertTrue(g1.equals(g2));
    }
}
