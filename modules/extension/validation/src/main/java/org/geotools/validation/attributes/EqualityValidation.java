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

import java.util.logging.Logger;

import org.geotools.validation.DefaultFeatureValidation;
import org.geotools.validation.ValidationResults;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;


/**
 * Tests to see if an attribute is equal to a provided value.
 * 
 * <p>
 * I can only see this test being useful if a Filter is also used. Online
 * research shows that this test is used in the wild, so we are adding it into
 * our system.
 * </p>
 *
 * @author Jody Garnett, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 *
 * @source $URL$
 * @version $Id$
 */
public class EqualityValidation extends DefaultFeatureValidation {
    /** The logger for the validation module. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(
            "org.geotools.validation");
    private String attributeName;

    /** Expected value that attribute are supposed to equal */
    private Object expected;

    /** Filter used to limit the number of Features we check */
    private Filter filter = Filter.INCLUDE;

    /**
     * No argument constructor, required by the Java Bean Specification.
     */
    public EqualityValidation() {
    }

    /**
     * The priority level used to schedule this Validation.
     *
     * @return PRORITY_SIMPLE
     *
     * @see org.geotools.validation.Validation#getPriority()
     */
    public int getPriority() {
        return PRIORITY_SIMPLE;
    }

    /**
     * Implementation of getTypeNames.
     *
     * @return Array of typeNames, or empty array for all, null for disabled
     *
     * @see org.geotools.validation.Validation#getTypeRefs()
     */
    public String[] getTypeRefs() {
        if (getTypeRef() == null) {
            return null;
        }

        if (getTypeRef().equals("*")) {
            return ALL;
        }

        return new String[] { getTypeRef(), };
    }

    /**
     * Validation test for feature.
     * 
     * <p>
     * Description of test ...
     * </p>
     *
     * @param feature The Feature to be validated
     * @param type The FeatureType of the feature
     * @param results The storage for error messages.
     *
     * @return <code>true</code> if the feature is a valid geometry.
     *
     * @see org.geotools.validation.FeatureValidation#validate
     */
    public boolean validate(SimpleFeature feature, SimpleFeatureType type,
        ValidationResults results) {
        if (!filter.evaluate(feature)) {
            return true;
        }

        Object actual = feature.getAttribute(attributeName);

        if (expected.equals(actual)) {
            return true;
        }

        results.error(feature, attributeName + " did not not equals "
            + expected);

        return false;
    }

    /**
     * Access attributeName property.
     *
     * @return Returns the attributeName.
     */
    public String getAttributeName() {
        return attributeName;
    }

    /**
     * Set attributeName to attributeName.
     *
     * @param attributeName The attributeName to set.
     */
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    /**
     * Access expected property.
     *
     * @return Returns the expected.
     */
    public Object getExpected() {
        return expected;
    }

    /**
     * Set expected to expected.
     *
     * @param expected The expected to set.
     */
    public void setExpected(Object expected) {
        this.expected = expected;
    }

    /**
     * Access filter property.
     *
     * @return Returns the filter.
     */
    public Filter getFilter() {
        return filter;
    }

    /**
     * Set filter to filter.
     *
     * @param filter The filter to set.
     */
    public void setFilter(Filter filter) {
        this.filter = filter;
    }
}
