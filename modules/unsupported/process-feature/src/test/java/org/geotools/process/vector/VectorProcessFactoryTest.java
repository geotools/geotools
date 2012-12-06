package org.geotools.process.vector;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

import org.geotools.factory.FactoryIteratorProvider;
import org.geotools.factory.GeoTools;
import org.geotools.feature.NameImpl;
import org.geotools.process.Processors;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.junit.Test;
import org.opengis.feature.type.Name;

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
                    return (Iterator<T>) Arrays.asList(new CustomProcess()).iterator();
                }
                return null;
            }
        };
        GeoTools.addFactoryIteratorProvider(p);
        try {
            Processors.reset();
            assertNotNull(Processors.createProcess(new NameImpl("vec", "Custom")));
        }
        finally {
            GeoTools.removeFactoryIteratorProvider(p);
        }
    }
    
    @DescribeProcess(title = "Custom", description = "Custom mock process")
    public static class CustomProcess implements VectorProcess {
        @DescribeResult(name = "result", description = "The result")
        public void execute() {
        }
    }
}
