package org.geotools.filter.spatial;

import static org.junit.Assert.assertEquals;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.referencing.CRS;
import org.junit.Test;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.spatial.BBOX;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;

public class DefaultCRSFilterVisitorTest {

    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();

    @Test
    public void force3DCRS() throws Exception {
        CoordinateReferenceSystem crs = CRS.decode("EPSG:4939", true);
        CoordinateReferenceSystem hcrs = CRS.getHorizontalCRS(crs);
        BBOX bbox = ff.bbox("the_geom", -180, -90, 180, 90, null);
        DefaultCRSFilterVisitor visitor = new DefaultCRSFilterVisitor(ff, crs);
        BBOX filtered = (BBOX) bbox.accept(visitor, null);
        Literal box = (Literal) filtered.getExpression2();
        Geometry g = (Geometry) box.evaluate(null);
        assertEquals(hcrs, g.getUserData());
    }
}
