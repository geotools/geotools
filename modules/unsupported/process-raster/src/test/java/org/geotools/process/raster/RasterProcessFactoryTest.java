package org.geotools.process.raster;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import it.geosolutions.jaiext.range.Range;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.geotools.api.data.Parameter;
import org.geotools.api.feature.type.Name;
import org.geotools.feature.NameImpl;
import org.geotools.process.Processors;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.util.factory.FactoryIteratorProvider;
import org.geotools.util.factory.GeoTools;
import org.junit.Test;

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
        Map<String, Parameter<?>> params = factory.getParameterInfo(new NameImpl("ras", "RangeLookup"));
        Parameter<?> ranges = params.get("ranges");
        assertEquals("ranges", ranges.getName());
        assertEquals(0, ranges.getMinOccurs());
        assertEquals(Integer.MAX_VALUE, ranges.getMaxOccurs());
        assertEquals(Range.class, ranges.getType());
    }

    @Test
    public void testAddCustomProcess() {
        assertNull(Processors.createProcess(new NameImpl("ras", "Custom")));

        FactoryIteratorProvider p = new FactoryIteratorProvider() {
            @Override
            public <T> Iterator<T> iterator(Class<T> category) {
                if (category == RasterProcess.class) {
                    @SuppressWarnings("unchecked")
                    T customProcess = (T) new CustomProcess();
                    return Arrays.asList(customProcess).iterator();
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
