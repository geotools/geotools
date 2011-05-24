/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2007 Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.feature.type;

import java.util.List;

import org.opengis.filter.Filter;

/**
 * The type of operations to be invoked on an attribute.
 * <p>
 * Invoking an operation on an attribute is used to calculate a derived quantity
 * or update attribute state. OperationType is used to define the required
 * parameters and expected result for an Operation.
 *
 * @author Jody Garnett, Refractions Research, Inc.
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/feature/type/OperationType.java $
 */
 public interface OperationType extends PropertyType {

    /**
     * Access to super type information.
     * <p>
     * The super type of an operation provides additional
     * restrictions and description for this operation.
     * </p>
     * @return super type
     */
     OperationType getSuper();

    /**
     * Indicate that this OperationType may not be used directly.
     * <p>
     * This indicates that a sub type will need to actually define the operation
     * meaning here. As an example a graph system could have an Edge that would
     * have "related" operation returning that was abstract, and a sub type road
     * would define "related" based on touches, or "contains" or "common vertex".
     * </p>
     */
     boolean isAbstract();

    /**
     * AttributeType this operation type can function against.
     */
     AttributeType getTarget();

     /**
      * Indicates the expected result type, may be <code>null</code>.
      *
      * @return expected result type, may be <code>null</code>
      */
     AttributeType getResult();

    /**
     * We need the following AttributeTypes as parameters.
     * <p>
     * Note we do not need AttributeDescriptors here as parameters
     * are ordered, so name is not needed.
     * </p>
     * @return indicates paramters required for operation
     */
    List<AttributeType> getParameters();

    /**
     * List of restrictions used to limit the allowable returned value.
     *
     * @return Restrictions on valid return values
     */
     List<Filter> getRestrictions();
}
