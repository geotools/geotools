/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005 Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.filter;

import org.opengis.annotation.XmlElement;

/**
 * Defines a filter that supports filtering on multi-valued attributes.
 * 
 * @author Niels Charlier, Curtin University of Technology
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/library/opengis/src/main/java/org/opengis/filter/MultiValuedFilter.java $
 */
public interface MultiValuedFilter extends Filter {
        
    /**
     * Enumerated type for MatchAction property (used by comparison and geometry operations):
     * When one or more of the operands evaluates to multiple values rather than a single value, which
     * action should be taken?
     * 
     * For example, in case of a binary comparison, if there are n values for the left operand 
     * and m values for the right operand, there are n * m possible combinations that can be compared,
     * 
     * ANY - if any of the possible combinations match, the result is true (aggregated OR)
     * ALL - only if all of the possible combinations match, the result is true (aggregated AND)
     * ONE - only if exactly one of the possible combinations match, the result is true (aggregated XOR)
     * 
     * @author Niels Charlier, Curtin University of Technology
     *
     */
    public enum MatchAction {ANY, ALL, ONE};
    

    /***
     * Flag Controlling MatchAction property
     * When one or more of the operands evaluates to multiple values rather than a single value, which
     * action should be taken? 
     * If there are n values for the left operand and m values for the right operand, there are 
     * n * m possible combinations that can be compared,
     * 
     * ANY - if any of the possible combinations match, the result is true (aggregated OR)
     * ALL - only if all of the possible combinations match, the result is true (aggregated AND)
     * ONE - only if exactly one of the possible combinations match, the result is true (aggregated XOR)
     * 
     * @return MatchAction flag
     * 
     */
    @XmlElement("matchAction")
    MatchAction getMatchAction();

}
