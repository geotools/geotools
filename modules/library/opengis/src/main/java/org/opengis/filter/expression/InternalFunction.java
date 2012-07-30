/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005 Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */

package org.opengis.filter.expression;

import org.opengis.filter.FilterFactory;

/**
 * Special {@link Function} type indicating that that the function is to be executed exclusively at
 * run-time, and does not participate in the SPI (Service Provider Interface) lookup mechanism (i.e.
 * cannot be created using a {@link FilterFactory}).
 * <p>
 * This is a (non OGC Filter compatible) extension point to the Filter API to allow for anonymous
 * inner classes to be used in filters.
 * <p>
 * The additional {@link #duplicate(Expression...)} method allows for implementations to return a
 * new instance of the function for the given set of arguments.
 * <p>
 * Usage example:
 * 
 * <pre>
 *  <code>
 *  InternalFunction function = new InternalFunction(){
 *      public String getName(){ return "MyFunction";}
 *      public FunctionName getFunctionName(){ return new FunctionNameImpl(getName(), 0);}
 *      public List<Expression> getParameters(){ return Collections.emptyList(); }
 *      public Literal getFallbackValue() { return null; }
 *      
 *      public Object evaluate(Object object){
 *        return determineRuntimeFunctionValue(object);
 *      }
 *      public InternalFunction duplicate(Expression... parameters){
 *        return this;
 *      }
 *  }
 *  
 *  FilterFactory ff = ..
 *  Filter filter = ff.equals(ff.literal(Boolean.TRUE), function);
 * </code>
 * </pre>
 * 
 * @since 9.0
 */
public interface InternalFunction extends Function {

    /**
     * Factory method to return a new instance of its own kind that works on the given parameters.
     * <p>
     * This is so because InternalFunctions do not participate on the standard SPI lookup mechanism
     * and hence they can't be created by {@link FilterFactory}, yet on occasion a new copy might be
     * needed, such as for duplicating filter visitors.
     * <p>
     * Note however implementations are free to return {@code this} if the actual function instance
     * does not work with {@link Expression} as parameters.
     * 
     * @param parameters the parameters the returned InternalFunction works on
     * @return a new instance of the same kind of InternalFunction that works on the given
     *         parameters
     */
    public InternalFunction duplicate(Expression... parameters);

}
