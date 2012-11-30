package org.geotools.wcs.v2_0;

import static org.junit.Assert.*;

import java.util.List;

import net.opengis.wcs20.DescribeCoverageType;

import org.geotools.xml.Parser;
import org.junit.Test;

public class DescribeCoverageTest {

    Parser parser = new Parser(new WCSConfiguration());

    @Test
    public void testParseDescribeCoverage() throws Exception {
        String capRequestPath = "requestDescribeCoverage.xml";
        DescribeCoverageType dc = (DescribeCoverageType) parser.parse(getClass()
                .getResourceAsStream(capRequestPath));
        assertEquals("WCS", dc.getService());
        assertEquals("2.0.1", dc.getVersion());

        List<String> ids = dc.getCoverageId();
        assertEquals(2, ids.size());
        assertEquals("C0001", ids.get(0));
        assertEquals("C0002", ids.get(1));
    }

}
