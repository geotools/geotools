package org.geotools.wcs.v2_0;

import static org.junit.Assert.assertEquals;

import java.util.List;
import net.opengis.wcs20.DescribeEOCoverageSetType;
import net.opengis.wcs20.DimensionTrimType;
import net.opengis.wcs20.Section;
import org.eclipse.emf.common.util.EList;
import org.geotools.xsd.Parser;
import org.junit.Test;

public class DescribeEOCoverageSetTest {

    Parser parser = new Parser(new WCSEOConfiguration());

    @Test
    public void testParseDescribeCoverage() throws Exception {
        String capRequestPath = "requestDescribeEOCoverageSet.xml";
        DescribeEOCoverageSetType dcs =
                (DescribeEOCoverageSetType)
                        parser.parse(getClass().getResourceAsStream(capRequestPath));
        assertEquals("WCS", dcs.getService());
        assertEquals("2.0.0", dcs.getVersion());

        List<String> ids = dcs.getEoId();
        assertEquals(1, ids.size());
        assertEquals("someDatasetSeries", ids.get(0));

        assertEquals(100, dcs.getCount());

        EList<Section> sections = dcs.getSections().getSection();
        assertEquals(2, sections.size());
        assertEquals(Section.COVERAGEDESCRIPTIONS, sections.get(0));
        assertEquals(Section.DATASETSERIESDESCRIPTIONS, sections.get(1));

        EList<DimensionTrimType> trims = dcs.getDimensionTrim();
        assertEquals(3, trims.size());
        DimensionTrimType lonTrim = trims.get(0);
        assertEquals("Long", lonTrim.getDimension());
        assertEquals("16", lonTrim.getTrimLow());
        assertEquals("18", lonTrim.getTrimHigh());
        DimensionTrimType latTrim = trims.get(1);
        assertEquals("Lat", latTrim.getDimension());
        assertEquals("46", latTrim.getTrimLow());
        assertEquals("48", latTrim.getTrimHigh());
        DimensionTrimType timeTrim = trims.get(2);
        assertEquals("phenomenonTime", timeTrim.getDimension());
        assertEquals("2011-01-18T22:21:52Z", timeTrim.getTrimLow());
        assertEquals("2011-01-18T22:22:52Z", timeTrim.getTrimHigh());
    }
}
