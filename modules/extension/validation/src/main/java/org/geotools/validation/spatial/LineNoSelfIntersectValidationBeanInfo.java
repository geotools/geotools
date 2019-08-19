/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.validation.spatial;

import java.beans.PropertyDescriptor;
import org.geotools.validation.DefaultFeatureValidationBeanInfo;

/**
 * LineAbstractValidationBeanInfopurpose.
 *
 * <p>Description of LineAbstractValidationBeanInfo...
 *
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 * @version $Id$
 */
public class LineNoSelfIntersectValidationBeanInfo extends DefaultFeatureValidationBeanInfo {
    /**
     * LineAbstractValidationBeanInfoconstructor.
     *
     * <p>Description
     */
    public LineNoSelfIntersectValidationBeanInfo() {
        super();
    }

    /**
     * Implementation of getPropertyDescriptors.
     *
     * @see java.beans.BeanInfo#getPropertyDescriptors()
     */
    public PropertyDescriptor[] getPropertyDescriptors() {
        return super.getPropertyDescriptors();
    }
}
