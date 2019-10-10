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
package org.geotools.validation.attributes;

import java.util.Map;
import org.geotools.validation.DefaultIntegrityValidation;
import org.geotools.validation.ValidationResults;
import org.locationtech.jts.geom.Envelope;

/**
 * Tests to that an attribute's value is unique across the entire FeatureType.
 *
 * <p>For a starting point you may want to look at UniqueFIDIntegrityValidation
 *
 * @author Jody Garnett, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 * @version $Id$
 */
public class UniquityValidation extends DefaultIntegrityValidation {
    /** No argument constructor, required by the Java Bean Specification. */
    public UniquityValidation() {}

    /**
     * The priority level used to schedule this Validation.
     *
     * @return PRORITY_SIMPLE
     * @see org.geotools.validation.Validation#getPriority()
     */
    public int getPriority() {
        return PRIORITY_SIMPLE;
    }

    /**
     * Check FeatureType for ...
     *
     * <p>Detailed description...
     *
     * @param layers Map of SimpleFeatureSource by "dataStoreID:typeName"
     * @param envelope The bounding box that encloses the unvalidated data
     * @param results Used to coallate results information
     * @return <code>true</code> if all the features pass this test.
     */
    public boolean validate(Map layers, Envelope envelope, ValidationResults results)
            throws Exception {
        return false;
    }
}
