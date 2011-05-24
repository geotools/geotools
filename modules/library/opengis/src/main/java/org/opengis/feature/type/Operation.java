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

import java.lang.reflect.InvocationTargetException;

import org.opengis.feature.Attribute;

/**
 * An implementation of an operation that may be invoked on an Attribute.
 *
 * @author Jody Garnett, Refractions Research
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/feature/type/Operation.java $
 */
public interface Operation extends PropertyDescriptor {

    /**
     * Operations are not part of the structure.
     * @return 0 in order to not trip up
     */
    int getMaxOccurs();

    /**
     * Operations are not part of the structure.
     * @return 0 in order to not trip up
     */
    int getMinOccurs();

    /** Indicates the OpperationType of this attribute */
    OperationType getType();

    /**
     * Indicates if invoke may be called.
     * <p>
     * In order allow for faithful description of a software system we will need
     * construct models dynamically at runtime, possibly when no implementation
     * of this Operation is available. As an example when working with features
     * in a web application some operations may only be available
     * when being executed on a remote web processing service.
     * </p>
     *
     * @return true if invoke may be called.
     */
    boolean isImplemented();

    /**
     * Invoke this operation on an attribute using the provided parameters.
     * <p>
     * The state of the attribute may be used and / or updated during the
     * execution of the operation.
     * </p>
     * <p>
     * Please check to ensure that isImplemented returns <code>true</code>
     * before calling invoke.
     *
     * @param target
     *            Attribute this operation is being applied to, the state of this
     *            attribute may be changed by this operation.
     * @param params parameters used by the operation
     * @return the result of the operation
     * @throws InvoationTargetException
     *             if an error occurred while processing
     */
    Object invoke(Attribute target, Object params[])
            throws InvocationTargetException;

}
