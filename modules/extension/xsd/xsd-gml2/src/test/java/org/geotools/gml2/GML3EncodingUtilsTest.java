package org.geotools.gml2;

import static org.junit.Assert.assertEquals;

import org.geotools.gml2.bindings.GML2EncodingUtils;
import org.geotools.referencing.CRS;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;
import org.junit.Test;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class GML3EncodingUtilsTest {

    @Test
    public void testForceSrs() throws NoSuchAuthorityCodeException, FactoryException {
        System.setProperty(GeoTools.FORCE_SRS_STYLE, "true");
        Hints.putSystemDefault(Hints.FORCE_SRS_STYLE, true);

        CoordinateReferenceSystem crs = CRS.decode("epsg:5730");

        assertEquals(
                "http://www.opengis.net/def/crs/EPSG/0/5730",
                GML2EncodingUtils.toURI(crs, SrsSyntax.OGC_HTTP_URI));

        System.clearProperty(GeoTools.FORCE_SRS_STYLE);
        Hints.removeSystemDefault(Hints.FORCE_SRS_STYLE);
    }
}
