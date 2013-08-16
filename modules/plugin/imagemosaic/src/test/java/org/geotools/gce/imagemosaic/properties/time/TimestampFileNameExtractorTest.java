package org.geotools.gce.imagemosaic.properties.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.TimeZone;

import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;
import org.geotools.gce.imagemosaic.properties.PropertiesCollector;
import org.geotools.gce.imagemosaic.properties.PropertiesCollectorFinder;
import org.geotools.gce.imagemosaic.properties.PropertiesCollectorSPI;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class TimestampFileNameExtractorTest {

    private SimpleDateFormat df;
    private SimpleFeature feature;

    @Before
    public void setup() throws SchemaException {
        df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        SimpleFeatureType ft = DataUtilities.createType("test", "id:int,time:java.util.Date");
        feature = DataUtilities.createFeature(ft, "1|null");
    }
    
    @Test
    public void testParseIsoTimestamp() {
        PropertiesCollectorSPI spi = getTimestampSpi();
        PropertiesCollector collector = spi.create("regex=[0-9]{8}T[0-9]{6}", Arrays.asList("time"));
        File file = new File("polyphemus_20130301T000000.nc");
        collector.collect(file);
        collector.setProperties(feature);
        Date time = (Date) feature.getAttribute("time");
        assertNotNull(time);
        assertEquals("2013-03-01T00:00:00.000Z", df.format(time));

    }
    
    @Test
    public void testParseCustomTimestamp() {
        PropertiesCollectorSPI spi = getTimestampSpi();
        PropertiesCollector collector = spi.create("regex=[0-9]{14},format=yyyyMMddHHmmss", Arrays.asList("time"));
        File file = new File("polyphemus_20130301000000.nc");
        collector.collect(file);
        collector.setProperties(feature);
        Date time = (Date) feature.getAttribute("time");
        assertNotNull(time);
        assertEquals("2013-03-01T00:00:00.000Z", df.format(time));

    }

    private PropertiesCollectorSPI getTimestampSpi() {
        Set<PropertiesCollectorSPI> spis = PropertiesCollectorFinder.getPropertiesCollectorSPI();
        for (PropertiesCollectorSPI spi : spis) {
            if (spi.getName().equals(TimestampFileNameExtractorSPI.class.getSimpleName())) {
                return spi;
            }
        }

        return null;
    }
}
