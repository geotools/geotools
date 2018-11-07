package org.geotools.wcs.v1_1;

import static org.junit.Assert.*;

import net.opengis.wcs11.DescribeCoverageType;
import org.geotools.xsd.Parser;
import org.junit.Test;

public class DescribeCoverageTest {

    Parser parser = new Parser(new WCSConfiguration());

    @Test
    public void testParseDescribeCoverage() throws Exception {
        String capRequestPath = "requestDescribeCoverage.xml";
        DescribeCoverageType dc =
                (DescribeCoverageType) parser.parse(getClass().getResourceAsStream(capRequestPath));
        assertEquals("WCS", dc.getService());
        assertEquals("1.1.1", dc.getVersion());
    }
}
