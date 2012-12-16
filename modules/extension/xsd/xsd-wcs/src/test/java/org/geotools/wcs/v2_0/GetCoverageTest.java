package org.geotools.wcs.v2_0;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import javax.media.jai.InterpolationTable;

import net.opengis.wcs20.DimensionSliceType;
import net.opengis.wcs20.DimensionSubsetType;
import net.opengis.wcs20.DimensionTrimType;
import net.opengis.wcs20.ExtensionItemType;
import net.opengis.wcs20.GetCoverageType;
import net.opengis.wcs20.InterpolationAxesType;
import net.opengis.wcs20.InterpolationAxisType;
import net.opengis.wcs20.InterpolationMethodType;
import net.opengis.wcs20.InterpolationType;
import net.opengis.wcs20.RangeItemType;
import net.opengis.wcs20.RangeSubsetType;
import net.opengis.wcs20.ScaleAxisByFactorType;
import net.opengis.wcs20.ScaleAxisType;
import net.opengis.wcs20.ScaleByFactorType;
import net.opengis.wcs20.ScaleToExtentType;
import net.opengis.wcs20.ScaleToSizeType;
import net.opengis.wcs20.ScalingType;
import net.opengis.wcs20.TargetAxisExtentType;
import net.opengis.wcs20.TargetAxisSizeType;

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
    
    @Test
    public void testParseGetCoverageGeotiff() throws Exception {
        String capRequestPath = "requestGetCoverageGeotiff.xml";
        GetCoverageType gc = (GetCoverageType) parser.parse(getClass()
                .getResourceAsStream(capRequestPath));
        
        Map<String, Object> extensions = getExtensionsMap(gc);
        
        // check values
        assertEquals("JPEG", extensions.get("http://www.opengis.net/wcs/geotiff/1.0:compression"));
        assertEquals("75", extensions.get("http://www.opengis.net/wcs/geotiff/1.0:jpeg_quality"));
        assertEquals("None", extensions.get("http://www.opengis.net/wcs/geotiff/1.0:predictor"));
        assertEquals("pixel", extensions.get("http://www.opengis.net/wcs/geotiff/1.0:interleave"));
        assertEquals("true", extensions.get("http://www.opengis.net/wcs/geotiff/1.0:tiling"));
        assertEquals("256", extensions.get("http://www.opengis.net/wcs/geotiff/1.0:tileheight"));
        assertEquals("256", extensions.get("http://www.opengis.net/wcs/geotiff/1.0:tilewidth"));
    }

    private Map<String, Object> getExtensionsMap(GetCoverageType gc) {
        // collect extensions
        Map<String, Object> extensions = new HashMap<String, Object>();
        for (ExtensionItemType item : gc.getExtension().getContents()) {
            Object value = item.getSimpleContent() != null ? item.getSimpleContent() : item.getObjectContent();
            extensions.put(item.getNamespace() + ":" + item.getName(), value);
        }
        return extensions;
    }
    
    @Test
    public void testParseGetCoverageRangeSubset() throws Exception {
        String capRequestPath = "requestGetCoverageRangeSubsetting.xml";
        GetCoverageType gc = (GetCoverageType) parser.parse(getClass()
                .getResourceAsStream(capRequestPath));
        
        Map<String, Object> extensions = getExtensionsMap(gc);
        assertEquals(1, extensions.size());
        
        RangeSubsetType rangeSubset = (RangeSubsetType) extensions.get("http://www.opengis.net/wcs/range-subsetting/1.0:rangeSubset");
        
        // check values
        assertEquals(2, rangeSubset.getRangeItems().size());
        RangeItemType first = rangeSubset.getRangeItems().get(0);
        assertEquals("band1", first.getRangeComponent());
        RangeItemType second = rangeSubset.getRangeItems().get(1);
        assertEquals("band3", second.getRangeInterval().getStartComponent());
        assertEquals("band5", second.getRangeInterval().getEndComponent());
    }
    
    @Test
    public void testParseGetCoverageScaleByFactor() throws Exception {
        String capRequestPath = "requestGetCoverageScaleByFactor.xml";
        GetCoverageType gc = (GetCoverageType) parser.parse(getClass()
                .getResourceAsStream(capRequestPath));
        
        Map<String, Object> extensions = getExtensionsMap(gc);
        assertEquals(1, extensions.size());
        
        ScalingType scaling = (ScalingType) extensions.get("http://www.opengis.net/WCS_service-extension_scaling/1.0:Scaling");
        
        assertNull(scaling.getScaleAxesByFactor());
        assertNull(scaling.getScaleToSize());
        assertNull(scaling.getScaleToExtent());
        
        ScaleByFactorType scaleByFactor = scaling.getScaleByFactor();
        assertEquals(2.5, scaleByFactor.getScaleFactor(), 1e-9d);
    }
    
    @Test
    public void testParseGetCoverageScaleAxesByFactor() throws Exception {
        String capRequestPath = "requestGetCoverageScaleAxesByFactor.xml";
        GetCoverageType gc = (GetCoverageType) parser.parse(getClass()
                .getResourceAsStream(capRequestPath));
        
        Map<String, Object> extensions = getExtensionsMap(gc);
        assertEquals(1, extensions.size());
        
        ScalingType scaling = (ScalingType) extensions.get("http://www.opengis.net/WCS_service-extension_scaling/1.0:Scaling");
        
        assertNull(scaling.getScaleByFactor());
        assertNull(scaling.getScaleToSize());
        assertNull(scaling.getScaleToExtent());
        
        ScaleAxisByFactorType sa = scaling.getScaleAxesByFactor();
        assertEquals(3, sa.getScaleAxis().size());
        ScaleAxisType sa1 = sa.getScaleAxis().get(0);
        assertEquals("http://www.opengis.net/def/axis/OGC/1/i", sa1.getAxis());
        assertEquals(3.5, sa1.getScaleFactor(), 1e-9);
        ScaleAxisType sa2 = sa.getScaleAxis().get(1);
        assertEquals("http://www.opengis.net/def/axis/OGC/1/j", sa2.getAxis());
        assertEquals(3.5, sa2.getScaleFactor(), 1e-9);
        ScaleAxisType sa3 = sa.getScaleAxis().get(2);
        assertEquals("http://www.opengis.net/def/axis/OGC/1/k", sa3.getAxis());
        assertEquals(2.0, sa3.getScaleFactor(), 1e-9);
    }
    
    @Test
    public void testParseGetCoverageScaleToSize() throws Exception {
        String capRequestPath = "requestGetCoverageScaleToSize.xml";
        GetCoverageType gc = (GetCoverageType) parser.parse(getClass()
                .getResourceAsStream(capRequestPath));
        
        Map<String, Object> extensions = getExtensionsMap(gc);
        assertEquals(1, extensions.size());
        
        ScalingType scaling = (ScalingType) extensions.get("http://www.opengis.net/WCS_service-extension_scaling/1.0:Scaling");
        
        assertNull(scaling.getScaleByFactor());
        assertNull(scaling.getScaleAxesByFactor());
        assertNull(scaling.getScaleToExtent());
        
        ScaleToSizeType sa = scaling.getScaleToSize();
        assertEquals(3, sa.getTargetAxisSize().size());
        TargetAxisSizeType sa1 = sa.getTargetAxisSize().get(0);
        assertEquals("http://www.opengis.net/def/axis/OGC/1/i", sa1.getAxis());
        assertEquals(1000.0, sa1.getTargetSize(), 1e-9);
        TargetAxisSizeType sa2 = sa.getTargetAxisSize().get(1);
        assertEquals("http://www.opengis.net/def/axis/OGC/1/j", sa2.getAxis());
        assertEquals(1000.0, sa2.getTargetSize(), 1e-9);
        TargetAxisSizeType sa3 = sa.getTargetAxisSize().get(2);
        assertEquals("http://www.opengis.net/def/axis/OGC/1/k", sa3.getAxis());
        assertEquals(10.0, sa3.getTargetSize(), 1e-9);
    }
    
    @Test
    public void testParseGetCoverageScaleToExtend() throws Exception {
        String capRequestPath = "requestGetCoverageScaleToExtent.xml";
        GetCoverageType gc = (GetCoverageType) parser.parse(getClass()
                .getResourceAsStream(capRequestPath));
        
        Map<String, Object> extensions = getExtensionsMap(gc);
        assertEquals(1, extensions.size());
        
        ScalingType scaling = (ScalingType) extensions.get("http://www.opengis.net/WCS_service-extension_scaling/1.0:Scaling");
        
        assertNull(scaling.getScaleByFactor());
        assertNull(scaling.getScaleAxesByFactor());
        assertNull(scaling.getScaleToSize());
        
        ScaleToExtentType se = scaling.getScaleToExtent();
        assertEquals(2, se.getTargetAxisExtent().size());
        TargetAxisExtentType sa1 = se.getTargetAxisExtent().get(0);
        assertEquals("http://www.opengis.net/def/axis/OGC/1/i", sa1.getAxis());
        assertEquals(10.0, sa1.getLow(), 1e-9);
        assertEquals(20.0, sa1.getHigh(), 1e-9);
        TargetAxisExtentType sa2 = se.getTargetAxisExtent().get(1);
        assertEquals("http://www.opengis.net/def/axis/OGC/1/j", sa2.getAxis());
        assertEquals(20.0, sa2.getLow(), 1e-9);
        assertEquals(30.0, sa2.getHigh(), 1e-9);
    }
    
    @Test
    public void testParseGetCoverageInterpolationLinear() throws Exception {
        String capRequestPath = "requestGetCoverageInterpolationLinear.xml";
        GetCoverageType gc = (GetCoverageType) parser.parse(getClass()
                .getResourceAsStream(capRequestPath));
        
        Map<String, Object> extensions = getExtensionsMap(gc);
        assertEquals(1, extensions.size());
        
        InterpolationType interpolation = (InterpolationType) extensions.get("http://www.opengis.net/WCS_service-extension_interpolation/1.0:Interpolation");
        
        assertNull(interpolation.getInterpolationAxes());
        
        InterpolationMethodType method = interpolation.getInterpolationMethod();
        assertEquals("http://www.opengis.net/def/interpolation/OGC/1/linear", method.getInterpolationMethod());
    }
    
    @Test
    public void testParseGetCoverageInterpolationMixed() throws Exception {
        String capRequestPath = "requestGetCoverageInterpolationMixed.xml";
        GetCoverageType gc = (GetCoverageType) parser.parse(getClass()
                .getResourceAsStream(capRequestPath));
        
        Map<String, Object> extensions = getExtensionsMap(gc);
        assertEquals(1, extensions.size());
        
        InterpolationType interpolation = (InterpolationType) extensions.get("http://www.opengis.net/WCS_service-extension_interpolation/1.0:Interpolation");
        
        assertNull(interpolation.getInterpolationMethod());
        
        EList<InterpolationAxisType> axes = interpolation.getInterpolationAxes().getInterpolationAxis();
        assertEquals(3, axes.size());
        assertEquals("http://www.opengis.net/def/axis/OGC/1/latitude", axes.get(0).getAxis());
        assertEquals("http://www.opengis.net/def/interpolation/OGC/1/quadratic", axes.get(0).getInterpolationMethod());
        assertEquals("http://www.opengis.net/def/axis/OGC/1/longitude", axes.get(1).getAxis());
        assertEquals("http://www.opengis.net/def/interpolation/OGC/1/quadratic", axes.get(1).getInterpolationMethod());
        assertEquals("http://www.opengis.net/def/axis/OGC/1/time", axes.get(2).getAxis());
        assertEquals("http://www.opengis.net/def/interpolation/OGC/1/nearest-neighbor", axes.get(2).getInterpolationMethod());
    }
    
    @Test
    public void testParseGetCoverageCRS() throws Exception {
        String capRequestPath = "requestGetCoverageCRS.xml";
        GetCoverageType gc = (GetCoverageType) parser.parse(getClass()
                .getResourceAsStream(capRequestPath));
        
        Map<String, Object> extensions = getExtensionsMap(gc);
        assertEquals(2, extensions.size());
        
        assertEquals("http://www.opengis.net/def/crs/EPSG/0/4326", extensions.get("http://www.opengis.net/wcs/service-extension/crs/1.0:subsettingCrs"));
        assertEquals("http://www.opengis.net/def/crs/EPSG/0/32632", extensions.get("http://www.opengis.net/wcs/service-extension/crs/1.0:outputCrs"));
    }

}
