/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.parameter;

import org.opengis.util.InternationalString;

/**
 * General parameter interface. 
 *
 * @author Justin Deoliveira, OpenGeo
 *
 * @param <T>
 *
 * @source $URL$
 */
public interface Parameter<T> {

    /**
     * Name of the parameter.
     */
    String getName();
    
    /**
     * Title of the parameter.
     */
    InternationalString getTitle();
    
    /**
     * Description of the parameter.
     */
    InternationalString getDescription();
    
    /**
     * Type/class of the parameter.
     */
    Class<T> getType();
    
    /**
     * Flag indicating if the parameter is required or not.
     */
    Boolean isRequired();
    
    /**
     * The minimum number of occurrences of the parameter.
     * @return minimum number of occurrences, or -1 if no minimum needed
     */
    int getMinOccurs();
    
    /**
     * The maximum number of occurrences of the parameter.
     * @return maximum number of occurences, or -1 for unbound
     */
    int getMaxOccurs();
    
    /**
     * A default value for the parameter. 
     */
    T getDefaultValue();
}
