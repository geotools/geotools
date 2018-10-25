package org.geotools.referencing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.geotools.util.factory.Hints;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

@RunWith(Parameterized.class)
public class EsriLookupTest {

    @BeforeClass
    public static void setup() {
        System.setProperty("org.geotools.referencing.forceXY", "true");
        Hints.putSystemDefault(Hints.COMPARISON_TOLERANCE, 1e-8);
        CRS.getAuthorityFactory(true);
    }

    @AfterClass
    public static void teardown() {
        System.clearProperty("org.geotools.referencing.forceXY");
        Hints.removeSystemDefault(Hints.COMPARISON_TOLERANCE);
        CRS.reset("all");
    }

    @Parameters(name = "{0}")
    public static Collection<Object[]> data() throws IOException {

        java.util.Properties props = new java.util.Properties();
        try (InputStream is = EsriLookupTest.class.getResourceAsStream("esri_tests.properties")) {
            props.load(is);
        }

        List<Object[]> result = new ArrayList<>();
        for (Map.Entry entry : props.entrySet()) {
            Integer code = Integer.valueOf((String) entry.getKey());
            String wkt = (String) entry.getValue();
            result.add(new Object[] {code, wkt});
        }
        Collections.sort(result, (a, b) -> ((Integer) a[0]).compareTo((Integer) b[0]));

        return result;
    }

    private String wkt;
    private Integer expectedCode;

    public EsriLookupTest(Integer code, String wkt) {
        this.expectedCode = code;
        this.wkt = wkt;
    }

    @Test
    public void testCodeLookup() throws FactoryException {
        CoordinateReferenceSystem crs = CRS.parseWKT(wkt);
        Integer actualCode = CRS.lookupEpsgCode(crs, false);
        assertNotNull(
                "Could not find code for "
                        + expectedCode
                        + "\nsource wkt:\n"
                        + wkt
                        + "\nparsed wkt:\n"
                        + crs.toWKT(),
                actualCode);
        assertEquals(expectedCode, actualCode);
    }
}
