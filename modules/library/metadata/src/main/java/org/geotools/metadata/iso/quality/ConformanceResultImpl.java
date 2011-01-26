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
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotools.metadata.iso.quality;

import org.opengis.metadata.citation.Citation;
import org.opengis.metadata.quality.ConformanceResult;
import org.opengis.util.InternationalString;


/**
 * Information about the outcome of evaluating the obtained value (or set of values) against
 * a specified acceptable conformance quality level.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Touraïvane
 *
 * @since 2.1
 */
public class ConformanceResultImpl extends ResultImpl implements ConformanceResult {

    /**
     * Serial number for compatibility with different versions.
     */
    private static final long serialVersionUID = 6429932577869033286L;

    /**
     * Citation of product specification or user requirement against which data is being evaluated.
     */
    private Citation specification;

    /**
     * Explanation of the meaning of conformance for this result.
     */
    private InternationalString explanation;

    /**
     * Indication of the conformance result.
     */
    private boolean pass;

    /**
     * Constructs an initially empty conformance result.
     */
    public ConformanceResultImpl() {
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @since 2.4
     */
    public ConformanceResultImpl(final ConformanceResult source) {
        super(source);
    }

    /**
     * Creates a conformance result initialized to the given values.
     */
    public ConformanceResultImpl(final Citation            specification,
                                 final InternationalString explanation,
                                 final boolean             pass)
    {
        setSpecification(specification);
        setExplanation  (explanation);
        setPass         (pass);
    }

    /**
     * Citation of product specification or user requirement against which data is being evaluated.
     */
    public Citation getSpecification() {
        return specification;
    }

    /**
     * Set the citation of product specification or user requirement against which data
     * is being evaluated.
     */
    public synchronized void setSpecification(final Citation newValue) {
        checkWritePermission();
        specification = newValue;
    }

    /**
     * Explanation of the meaning of conformance for this result.
     */
    public InternationalString getExplanation() {
        return explanation;
    }

    /**
     * Set the explanation of the meaning of conformance for this result.
     */
    public synchronized void setExplanation(final InternationalString newValue) {
        checkWritePermission();
        explanation = newValue;
    }

    /**
     * Indication of the conformance result.
     */
    public boolean pass() {
        return pass;
    }

    /**
     * Set the indication of the conformance result.
     */
    public synchronized void setPass(final boolean newValue) {
        checkWritePermission();
        pass = newValue;
    }
}
