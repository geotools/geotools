package org.geotools.process.vector;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import org.geotools.api.feature.type.Name;
import org.geotools.feature.NameImpl;
import org.geotools.process.Processors;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.util.factory.FactoryIteratorProvider;
import org.geotools.util.factory.GeoTools;
import org.junit.Test;

public class VectorProcessFactoryTest {

    VectorProcessFactory factory = new VectorProcessFactory();

    @Test
    public void testLookup() {
        Set<Name> names = factory.getNames();
        assertFalse(names.isEmpty());
        assertTrue(names.contains(new NameImpl("vec", "Aggregate")));
    }

    @Test
    public void testAddCustomProcess() {
        assertNull(Processors.createProcess(new NameImpl("vec", "Custom")));

        FactoryIteratorProvider p = new FactoryIteratorProvider() {
            @Override
            public <T> Iterator<T> iterator(Class<T> category) {
                if (category == VectorProcess.class) {
                    @SuppressWarnings("unchecked")
                    T t = (T) new CustomProcess();
                    return Arrays.asList(t).iterator();
                }
                return null;
            }
        };
        GeoTools.addFactoryIteratorProvider(p);
        try {
            Processors.reset();
            assertNotNull(Processors.createProcess(new NameImpl("vec", "Custom")));
        } finally {
            GeoTools.removeFactoryIteratorProvider(p);
        }
    }

    @DescribeProcess(title = "Custom", description = "Custom mock process")
    public static class CustomProcess implements VectorProcess {
        @DescribeResult(name = "result", description = "The result")
        public void execute() {}
    }
}
