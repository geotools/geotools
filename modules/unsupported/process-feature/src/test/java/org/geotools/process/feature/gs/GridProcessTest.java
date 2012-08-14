package org.geotools.process.feature.gs;

import static org.junit.Assert.*;

import java.util.Map;

import org.geotools.data.Parameter;
import org.geotools.feature.NameImpl;
import org.geotools.process.ProcessFactory;
import org.geotools.process.Processors;
import org.geotools.process.feature.gs.GridProcess.GridMode;
import org.junit.Test;

public class GridProcessTest {

    @Test
    public void testDescription() {
        NameImpl gridName = new NameImpl("gs", "Grid");
        ProcessFactory pf = Processors.createProcessFactory(gridName);
        assertNotNull(pf);
        Map<String, Parameter<?>> parameterInfo = pf.getParameterInfo(gridName);
        Parameter<?> modeParam = parameterInfo.get("mode");
        assertEquals(GridMode.class, modeParam.getType());
        assertEquals(GridMode.Rectangular, modeParam.getDefaultValue());
    }
}
