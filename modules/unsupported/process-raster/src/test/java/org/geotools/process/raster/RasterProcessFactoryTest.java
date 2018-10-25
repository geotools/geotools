package org.geotools.process.raster;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.geotools.data.Parameter;
import org.geotools.feature.NameImpl;
import org.geotools.process.Processors;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.util.factory.FactoryIteratorProvider;
import org.geotools.util.factory.GeoTools;
import org.jaitools.numeric.Range;
import org.junit.Test;
import org.opengis.feature.type.Name;

public class RasterProcessFactoryTest {

    RasterProcessFactory factory = new RasterProcessFactory();

    @Test
    public void testLookup() {
        Set<Name> names = factory.getNames();
        assertFalse(names.isEmpty());
        assertTrue(names.contains(new NameImpl("ras", "AddCoverages")));
    }

    @Test
    public void testRangeLookup() {
        Map<String, Parameter<?>> params =
                factory.getParameterInfo(new NameImpl("ras", "RangeLookup"));
        Parameter<?> ranges = params.get("ranges");
        assertEquals("ranges", ranges.getName());
        assertEquals(0, ranges.getMinOccurs());
        assertEquals(Integer.MAX_VALUE, ranges.getMaxOccurs());
        assertEquals(Range.class, ranges.getType());
    }

    @Test
    public void testAddCustomProcess() {
        assertNull(Processors.createProcess(new NameImpl("ras", "Custom")));

        FactoryIteratorProvider p =
                new FactoryIteratorProvider() {
                    @Override
                    public <T> Iterator<T> iterator(Class<T> category) {
                        if (category == RasterProcess.class) {
                            return (Iterator<T>) Arrays.asList(new CustomProcess()).iterator();
                        }
                        return null;
                    }
                };
        GeoTools.addFactoryIteratorProvider(p);
        try {
            Processors.reset();
            assertNotNull(Processors.createProcess(new NameImpl("ras", "Custom")));
        } finally {
            GeoTools.removeFactoryIteratorProvider(p);
        }
    }

    @DescribeProcess(title = "Custom", description = "Custom mock process")
    public static class CustomProcess implements RasterProcess {
        @DescribeResult(name = "result", description = "The result")
        public void execute() {}
    }
}
