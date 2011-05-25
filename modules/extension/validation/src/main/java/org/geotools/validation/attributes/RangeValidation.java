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

import org.geotools.validation.DefaultFeatureValidation;
import org.geotools.validation.ValidationResults;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;


/**
 * RangeFeatureValidation validates that a number is within a given range.
 * 
 * <p>
 * RangeFeatureValidation is a quick and simple class the checks that the given
 * number resides within a given range.
 * </p>
 * 
 * <p>
 * Capabilities:
 * 
 * <ul>
 * <li>
 * Default max value is Integer.MAX_VALUE;
 * </li>
 * <li>
 * Default min value is Integer.MIN_VALUE;
 * </li>
 * <li>
 * If only one boundary of the range is set, only that boundary is checked.
 * </li>
 * <li>
 * The value of the integer is contained in the field specified by path.
 * </li>
 * </ul>
 * 
 * Example Use:
 * <pre><code>
 * RangeFeatureValidation x = new RangeFeatureValidation();
 * 
 * x.setMin(3);
 * x.setMax(5);
 * x.setName("id");
 * 
 * boolean result = x.validate(feature, featureType, results);
 * </code></pre>
 * </p>
 *
 * @author rgould, Refractions Research, Inc.
 * @author $Author: cholmesny $ (last modification)
 *
 * @source $URL$
 * @version $Id$
 */
public class RangeValidation extends DefaultFeatureValidation {
    private int max = Integer.MAX_VALUE;
    private int min = Integer.MIN_VALUE;
    /** XPath expression used to specify attribute */
	private String attribute;
    /**
     * RangeFeatureValidation constructor.
     * 
     * <p>
     * Description
     * </p>
     */
    public RangeValidation() {
        super();
    }

    /**
     * Override validate.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param feature
     * @param type
     * @param results
     *
     *
     * @see org.geotools.validation.FeatureValidation#validate(org.geotools.feature.Feature,
     *      org.geotools.feature.FeatureType,
     *      org.geotools.validation.ValidationResults)
     */
    public boolean validate(SimpleFeature feature, SimpleFeatureType type,
        ValidationResults results) {
        Object obj = feature.getAttribute(attribute);

        if (obj == null) {
            return true; // user can separately issue a nullzero test
        }

        if (obj instanceof Number) {
            Number number = (Number) obj;

            if (number.intValue() < min) {
                results.error(feature, attribute + " is less than " + min);
                return false;
            }

            if (number.intValue() > max) {
                results.error(feature, attribute + " is greater than " + max);
                return false;
            }
        }
        return true;
    }

    /**
     * Override getPriority.
     * 
     * <p>
     * Description ...
     * </p>
     *
     *
     * @see org.geotools.validation.Validation#getPriority()
     */
    public int getPriority() {
        return 0;
    }

    /**
     * getMax purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     */
    public int getMax() {
        return max;
    }

    /**
     * getMin purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     */
    public int getMin() {
        return min;
    }

    /**
     * getPath purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @task REVISIT: This wasn't compiling for me, as its parent sets
     * this as final.  If needed for some reason then fix it.  But it
     * looks like it should inherit ok... ch
     */
    //public String getName() {
    //    return name;
    //}

    /**
     * setMax purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param i
     */
    public void setMax(int i) {
        max = i;
    }

    /**
     * setMin purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param i
     */
    public void setMin(int i) {
        min = i;
    }

    /**
     * setPath purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param string
     * see note on getName - ch.
     */
    //public void setName(String string) {
    //    name = string;
    //}
	/**
	 * XPATH expression used to locate attribute
	 * @return xpath
	 */
	public String getAttribute() {
		return attribute;
	}

	/**
	 * XPATH expression used to locate attribute
	 * @param xpath
	 */
	public void setAttribute(String xpath) {
		attribute = xpath;
	}

}
