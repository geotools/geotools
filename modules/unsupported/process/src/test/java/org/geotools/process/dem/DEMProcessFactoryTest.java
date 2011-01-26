package org.geotools.process.dem;

import java.util.Map;
import java.util.Set;

import org.geotools.data.Parameter;
import org.geotools.feature.NameImpl;
import org.geotools.process.Process;
import org.junit.Test;
import org.opengis.feature.type.Name;
import org.opengis.util.InternationalString;

import static org.junit.Assert.*;

/**
 * Test the DEMProcessFactory; mostly a test of the ability to use annotations to call methods
 * defined in DEMTools.
 * 
 * @author jody
 */
public class DEMProcessFactoryTest {
    @Test
    public void testProcessFactory() {
        DEMProcessFactory factory = new DEMProcessFactory();
        Set<Name> names = factory.getNames();
        assertNotNull(names);

        final Name NAME = new NameImpl("http://localhost/dem/", "slope");
        assertTrue(names.contains(NAME));

        InternationalString description = factory.getDescription(NAME);
        assertNotNull(description);
        assertNotNull(description.toString());

        Map<String, Parameter<?>> input = factory.getParameterInfo(NAME);
        assertNotNull(input);
        Parameter<?> param = input.get("DEM");
        assertNotNull(param);
        assertEquals("DEM", param.key);

        Map<String, Parameter<?>> result = factory.getResultInfo(NAME, null);
        assertNotNull(result);
        Parameter<?> param2 = result.get("slope");
        assertNotNull(param2);
        assertEquals("slope", param2.key);
        
        Process process = factory.create(NAME);
        assertNotNull( process );
    }

}
