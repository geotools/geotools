package org.geotools.filter.function;

import static org.junit.Assert.*;

import static org.geotools.filter.capability.FunctionNameImpl.parameter;

import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.junit.Test;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Function;

public class FunctionExpressionImplTest {

    @Test
    public void testVariableArgCount() {
        FunctionName name = new FunctionNameImpl(
                "test",
                parameter("result", Double.class),
                parameter("double",Double.class,2,Integer.MAX_VALUE));

        FunctionExpressionImpl f = new FunctionExpressionImpl(name) {
            
        };
        
        assertEquals(-1, f.getArgCount());
    }
    
    @Test
    public void testHigherCardinality() {
        FunctionName name = new FunctionNameImpl(
                "test",
                parameter("result", Double.class),
                parameter("double",Double.class,2,2));

        FunctionExpressionImpl f = new FunctionExpressionImpl(name) {
            
        };
        
        assertEquals(2, f.getArgCount());
    }
    
    @Test
    public void testSimpleArguments() {
        FunctionName name = new FunctionNameImpl(
                "test",
                parameter("result", Double.class),
                parameter("one",Double.class),
                parameter("two",Double.class));

        FunctionExpressionImpl f = new FunctionExpressionImpl(name) {
            
        };
        
        assertEquals(2, f.getArgCount());
    }
}
