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
package org.geotools.validation;

/**
 * Used to define a type of validation test that is performed on Features.
 *
 * <p>Each ValidationPlugIn is very specific in nature: it performs one test extermly well. This
 * simplifies design decisions, documenation configuration and use.
 *
 * <p>The PlugIn is also required to supply some metadata to aid in its deployment, scripting,
 * logging and execution and error recovery:
 *
 * <ul>
 *   <li>name: user's name of validation test
 *   <li>description: user's description of validation test
 *   <li>priority: used to schedule validation test
 *   <li>typeNames: used to connect validaiton test to transaction opperation
 * </ul>
 *
 * @author bowens, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 * @version $Id$
 */
public interface Validation {
    /**
     * A constant for getPriority() used for Trivial validation checks.
     *
     * <p>This is a Priority Hint to Validation Processor based on computational expense.
     *
     * <p>Trivial is used for thing that don't require any real work - like checking for any O(1)
     * opperations such as checking null values.
     */
    public static final int PRIORITY_TRIVIAL = 100;

    /**
     * A constant for getPriority() used for simple validation checks.
     *
     * <p>This is a Priority Hint to Validation Processor based on computational expense.
     *
     * <p>Used for simple attribute or geometry validations, basically anything that is Order(N)
     * like a Bounds check.
     */
    public static final int PRIORITY_SIMPLE = 200;

    /**
     * A constant for getPriority() used for complex validation checks.
     *
     * <p>This is a Priority Hint to Validation Processor based on computational expense.
     *
     * <p>Used for complex validations, basically anything that is has a Chance of being worse then
     * O(N).
     *
     * <p>Any integrity tests between two FeatureTypes should be an example of a PRIORITY_COMPLEX.
     */
    public static final int PRIORITY_COMPLEX = 1000;

    /**
     * A constant for getPriority() used for involved validation checks.
     *
     * <p>This is a Priority Hint to Validation Processor based on computational expense.
     *
     * <p>Used for involved validations, basically anything that is has a Chance of being worse then
     * O(N^2).
     *
     * <p>Any integrity tests involving network code, were we have to build the network and then
     * walk it, is an example of PRIORITY_INVOLVED.
     */
    public static final int PRIORITY_INVOLVED = 10000;

    /**
     * Empty String array for use with getTypeNames().
     *
     * <p>Used to denote that all FeatureTypes should be tested. (If this does not Pan out we may
     * have to define the wild-card character "" or support Regex based typeName matching.
     */
    public static final String[] ALL = null; // test all featureTypes

    /**
     * Sets the name of the validation.
     *
     * @param name the name of the validation.
     */
    void setName(String name);

    /**
     * User's name for the validation.
     *
     * @return the name of the validation.
     */
    String getName();

    /**
     * Sets the description of the validation.
     *
     * @param description of the validation
     */
    void setDescription(String description);

    /**
     * User's Description of this Validation.
     *
     * @return the description of the validation.
     */
    String getDescription();

    /**
     * Priority used in scheduling this Validation test.
     *
     * @return The priority (time cost) of the validation test
     */
    int getPriority();

    /**
     * Identify the FeatureTypes that this validation test is run against.
     *
     * <p>If this list is empty the ValidationProcess will run the test against all FeatureTypes.
     * The ValidationProcess expects these names to be in the format dataStoreId:typeName.
     *
     * @return FeatureType names that this validation test is run against, empty for all, null to
     *     disable
     */
    String[] getTypeRefs();
}
