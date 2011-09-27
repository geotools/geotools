/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.efeature.tests.unit.conditions;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.query.conditions.Condition;
import org.geotools.data.efeature.internal.EFeatureLogFormatter;
import org.geotools.data.efeature.tests.unit.AbstractEFeatureTest;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.filter.Filter;

/**
 * @author kengu - 14. juni 2011
 *
 *
 * @source $URL$
 */
public abstract class AbstractEAttributeFilterTest<E extends EObject> extends AbstractEFeatureTest {

    /**
     * Flag telling {@link Values#getValue(int)} to return all values
     */    
    public static final int ALL = Values.ALL;    
    
    
    // ----------------------------------------------------- 
    //  Test members
    // -----------------------------------------------------
    
    /**
     * Cached {@link Filter} instance
     */
    private Condition eCondition;
    
    /**
     * Cached {@link EObject} fixture instance
     */
    private E eFixture;

    // ----------------------------------------------------- 
    //  Constructors
    // -----------------------------------------------------
    
    public AbstractEAttributeFilterTest(String name) {
        super(name);
    }

    // -----------------------------------------------------
    // Test configuration methods
    // -----------------------------------------------------

    @Override
    @BeforeClass
    protected final void setUp() throws Exception {
        //
        // Notify test start
        //
        trace("===> Begin : " + getName() + "[" + getValueCount() + " values]"); //$NON-NLS-1$
        EFeatureLogFormatter.setMinimal();
        //
        // Create fixture
        //        
        eFixture = createFixture();
    }
    
    @Override
    protected void tearDown() throws Exception {
        try {
            
            //
            // Dispose fixture and condition
            //
            eFixture = null;
            eCondition = null;
            
        } finally {
            trace("===> End   : " + getName(), TIME_TOTAL); //$NON-NLS-1$
            EFeatureLogFormatter.setStandard();
        }    
    }

    

    // ----------------------------------------------------- 
    //  Tests
    // -----------------------------------------------------
    
    @Test
    public void testCondition() throws Exception {
        //
        // Initialized
        //
        int passed = 0;
        //
        // Get number of values and passes
        //
        int passes = getPassCount();
        int values = getValueCount();
        //
        // Sanity check
        //
        if(passes==0) fail("No passes defined for " + getOperationName());
        if(values==0) fail("No values defined for " + getOperationName());
        //
        // Loop over all values
        //
        for(int value=0; value<values; value++) {
            //
            // Is current value and operand of given operation?
            // 
            if(isOperand(value)) {
                //
                // Assert condition given number of passes
                //
                for(int pass=0; pass<passes; pass++) {
                    //
                    // Update fixture
                    //
                    updateFixture(eFixture, value, pass);
                    //
                    // Create condition
                    //        
                    eCondition = createCondition(value, pass);
                    //
                    // Assert condition
                    //
                    assertCondition(failure(value, pass), expect(pass));
                    //
                    // Increment "test passed" count
                    //
                    passed++;
                }
            }
        }
        //
        // Sanity check
        //
        if(passed==0) fail("No tests defined for " + getOperationName());
    }

    // ----------------------------------------------------- 
    //  Implementation methods
    // -----------------------------------------------------
    
    protected abstract E createFixture() throws Exception;
    protected abstract int getPassCount();
    protected abstract int getValueCount();
    protected abstract String getType(int value);
    protected abstract String getOperationName();
    protected abstract boolean isOperand(int value);
    protected abstract boolean expect(int pass);
    protected abstract Object getFilter(int value, int pass);
    protected abstract Object getTest(int value, int pass);
    protected abstract void updateFixture(E eFixture, int value, int pass) throws Exception;
    protected abstract Condition createCondition(int value, int pass) throws Exception;
    
    // ----------------------------------------------------- 
    //  Helper methods
    // -----------------------------------------------------
    
    protected void assertCondition(String message, boolean expected) {
        assertEquals(message, expected, eCondition.isSatisfied(eFixture));
    }
    
    protected final String failure(int value, int pass) {
        return "Condition [" 
                + getType(value) + "] "
                + toString(getTest(value, pass)) + " "
                + getOperationName() + " "
                + toString(getFilter(value, pass)) + " not satisfied";
    }
    
    protected static final String nameOf(int operation, Field[] operations) throws IllegalArgumentException {
        try {
            //
            // Compare values
            //
            for (Field it : operations) {
                //
                // Check value
                //
                if (Modifier.isStatic(it.getModifiers()) && int.class.isAssignableFrom(it.getType())) {
                    //
                    // Found?
                    //
                    if (it.getInt(null) == operation) {
                        //
                        // Found
                        //
                        return it.getName();
                    }
                }
            }
            //
            // Failure
            //
            fail("Operation " + operation + " not found");
            
        } catch (IllegalAccessException e) {
            //
            // Failure
            //
            fail(e.getMessage());            
        }
        //
        // Not found
        //
        return null;
    }

    protected static final int indexOf(int operation) {
        String s = Integer.toBinaryString(operation);
        int index = s.lastIndexOf("0");
        return index==-1 ? 0 : index;
    }

    protected static final String toString(Object value) {
        if(value instanceof Collection) {
            value = Arrays.toString(((Collection<?>)value).toArray());
        }
        return String.valueOf(value);
    }
        
    protected static final boolean isFlag(int pattern, int flag) {
        return (pattern & flag) == flag;
    }
    
    // ----------------------------------------------------- 
    //  Helper classes
    // -----------------------------------------------------
    
    protected final static class Tests {
        final String name;
        final AbstractEAttributeFilterTest.Pass[] passes;
        
        public Tests(String name, AbstractEAttributeFilterTest.Pass...passes) {
            this.passes = passes;
            this.name = name;
        }
    
        public Tests(String name, Tests...tests) {
            List<AbstractEAttributeFilterTest.Pass> passes = new ArrayList<AbstractEAttributeFilterTest.Pass>();
            for(Tests it : tests) {
                passes.addAll(Arrays.asList(it.passes));
            }
            this.passes = passes.toArray(new AbstractEAttributeFilterTest.Pass[]{});
            this.name = name;
        }        
    }

    protected final static class Pass {
        final int test;
        final int[] filter;
        final boolean success;
        
        public Pass(boolean success, int test, int... filter) {
            this.test = test;
            this.filter = filter;
            this.success = success;
        }
    }

    protected final static class Values {

        /**
         * Flag telling {@link getValue(int)} to return all values
         */    
        public static final int ALL = -1;
        
        
        final Class<?> type;
        final Object[] values;
        final int operations;
        
        public Values(int operations, Class<?> type, Object...values) {
            this.type = type;
            int count = values.length;
            this.values = new Object[count];
            if(count>0) System.arraycopy(values, 0, this.values, 0, count);
            this.operations = operations;
        }
        
        public Class<?> getType() {
            return type;
        }
        
        public Object getValue(int index) {
            //
            // Return all values or value at given index only ?
            //            
            return index < 0 ? Arrays.asList(values) : values[index];
        }
        
        public Object getFilter(Pass pass) {
            //
            // Get filter value indices
            //
            int[] filter = pass.filter;
            //
            // Has no filter value?
            //
            if(filter.length==0) return null; 
            //
            // Single value?
            //
            if(filter.length==1 || filter[0]<0) {                
                return getValue(filter[0]);
            } 
            //
            // Collect values from indices
            //
            List<Object> list = new ArrayList<Object>(filter.length); 
            for(int i : filter) {
                list.add(getValue(i));
            }
            //
            // Finished
            //
            return list;
        }
        
        public Object getTest(Pass pass) {
            return getValue(pass.test);
        }        
    
        public boolean isOperand(int operation) {
            return (operations & operation) == operation;
        }
                     
        @Override
        public String toString() {
            return type.getSimpleName() + " " + Arrays.toString(values);
        }                
                
    }

    
    
}
