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

import java.util.ArrayList;
import org.opengis.feature.simple.SimpleFeature;

/**
 * RoadNetworkValidationResults purpose.
 *
 * <p>Description of RoadNetworkValidationResults ...
 *
 * <p>Capabilities:
 *
 * <ul>
 * </ul>
 *
 * Example Use:
 *
 * <pre><code>
 * RoadNetworkValidationResults x = new RoadNetworkValidationResults(...);
 * </code></pre>
 *
 * @author bowens, Refractions Research, Inc.
 * @author $Author: sploreg $ (last modification)
 * @version $Id$
 */
public class RoadNetworkValidationResults implements ValidationResults {

    ArrayList validationList; // list of validations that are to be performed
    ArrayList failedFeatures;
    ArrayList warningFeatures;
    ArrayList failureMessages;
    ArrayList warningMessages;

    /**
     * RoadNetworkValidationResults constructor.
     *
     * <p>Description
     */
    public RoadNetworkValidationResults() {
        validationList = new ArrayList();
        failedFeatures = new ArrayList();
        warningFeatures = new ArrayList();
        failureMessages = new ArrayList();
        warningMessages = new ArrayList();
    }

    /**
     * Override setValidation.
     *
     * <p>Description ...
     *
     * @see
     *     org.geotools.validation.ValidationResults#setValidation(org.geotools.validation.Validation)
     */
    public void setValidation(Validation validation) {
        validationList.add(validation);
    }

    /**
     * Override error.
     *
     * <p>Description ...
     *
     * @see org.geotools.validation.ValidationResults#error(org.geotools.feature.Feature,
     *     java.lang.String)
     */
    public void error(SimpleFeature feature, String message) {
        failedFeatures.add(feature);
        failureMessages.add(feature.getID() + ": " + message);
    }

    /**
     * Override warning.
     *
     * <p>Description ...
     *
     * @see org.geotools.validation.ValidationResults#warning(org.geotools.feature.Feature,
     *     java.lang.String)
     */
    public void warning(SimpleFeature feature, String message) {
        warningFeatures.add(feature);
        warningMessages.add(feature.getID() + ": " + message);
    }

    /**
     * getFailedMessages purpose.
     *
     * <p>Description ...
     */
    public String[] getFailedMessages() {
        String[] result = new String[failureMessages.size()];
        for (int i = 0; i < failureMessages.size(); i++) {
            result[i] = (String) failureMessages.get(i);
        }

        return result;
    }

    /**
     * getWarningMessages purpose.
     *
     * <p>Description ...
     */
    public String[] getWarningMessages() {
        String[] result = new String[warningMessages.size()];
        for (int i = 0; i < warningMessages.size(); i++) {
            result[i] = (String) warningMessages.get(i);
        }

        return result;
    }
}
