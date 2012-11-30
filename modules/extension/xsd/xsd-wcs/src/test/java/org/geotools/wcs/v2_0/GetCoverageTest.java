package org.geotools.wcs.v2_0;

import static org.junit.Assert.*;
import net.opengis.wcs20.DimensionSliceType;
import net.opengis.wcs20.DimensionSubsetType;
import net.opengis.wcs20.DimensionTrimType;
import net.opengis.wcs20.GetCoverageType;

import org.eclipse.emf.common.util.EList;
import org.geotools.xml.Parser;
import org.junit.Test;

public class GetCoverageTest {

    Parser parser = new Parser(new WCSConfiguration());

    @Test
    public void testParseGetCoverageSlicing() throws Exception {
        String capRequestPath = "requestGetCoverageSlicing.xml";
        GetCoverageType gc = (GetCoverageType) parser.parse(getClass()
                .getResourceAsStream(capRequestPath));
        assertEquals("WCS", gc.getService());
        assertEquals("2.0.1", gc.getVersion());

        // coverage id
        assertEquals("C0001", gc.getCoverageId());
        
        // slicing
        EList<DimensionSubsetType> dss = gc.getDimensionSubset();
        assertEquals(2, dss.size());
        DimensionSliceType ds1 = (DimensionSliceType) dss.get(0);
        assertEquals("Long", ds1.getDimension());
        assertEquals("1", ds1.getSlicePoint());
        DimensionSliceType ds2 = (DimensionSliceType) dss.get(1);
        assertEquals("Lat", ds2.getDimension());
        assertEquals("1", ds2.getSlicePoint());
        
        // format
        assertEquals("application/gml+xml", gc.getFormat());
    }
    
    @Test
    public void testParseGetCoverageTrimming() throws Exception {
        String capRequestPath = "requestGetCoverageTrimming.xml";
        GetCoverageType gc = (GetCoverageType) parser.parse(getClass()
                .getResourceAsStream(capRequestPath));
        assertEquals("WCS", gc.getService());
        assertEquals("2.0.1", gc.getVersion());

        // coverage id
        assertEquals("C0001", gc.getCoverageId());
        
        // trimming
        EList<DimensionSubsetType> dss = gc.getDimensionSubset();
        assertEquals(2, dss.size());
        DimensionTrimType dt1 = (DimensionTrimType) dss.get(0);
        assertEquals("Long", dt1.getDimension());
        assertEquals("1", dt1.getTrimLow());
        assertEquals("2", dt1.getTrimHigh());
        DimensionTrimType dt2 = (DimensionTrimType) dss.get(1);
        assertEquals("Lat", dt2.getDimension());
        assertEquals("1", dt2.getTrimLow());
        assertEquals("2", dt2.getTrimHigh());

        // format
        assertEquals("application/gml+xml", gc.getFormat());
        assertEquals("multipart/related", gc.getMediaType());
    }
    
    @Test
    public void testParseGetCoverageTrimmingSlicing() throws Exception {
        String capRequestPath = "requestGetCoverageTrimmingSlicing.xml";
        GetCoverageType gc = (GetCoverageType) parser.parse(getClass()
                .getResourceAsStream(capRequestPath));
        assertEquals("WCS", gc.getService());
        assertEquals("2.0.1", gc.getVersion());

        // coverage id
        assertEquals("C0001", gc.getCoverageId());
        
        // trimming
        EList<DimensionSubsetType> dss = gc.getDimensionSubset();
        assertEquals(2, dss.size());
        DimensionTrimType dt = (DimensionTrimType) dss.get(0);
        assertEquals("Long", dt.getDimension());
        assertEquals("20", dt.getTrimLow());
        assertEquals("29", dt.getTrimHigh());
        DimensionSliceType ds = (DimensionSliceType) dss.get(1);
        assertEquals("Lat", ds.getDimension());
        assertEquals("1", ds.getSlicePoint());

        // format
        assertEquals("application/gml+xml", gc.getFormat());
        assertEquals("multipart/related", gc.getMediaType());
    }


}
