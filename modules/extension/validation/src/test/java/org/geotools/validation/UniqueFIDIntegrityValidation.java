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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;

/**
 * UniqueFIDIntegrityValidation purpose.
 *
 * <p>Description of UniqueFIDIntegrityValidation ...
 *
 * <p>Capabilities:
 *
 * <ul>
 * </ul>
 *
 * Example Use:
 *
 * <pre><code>
 * UniqueFIDIntegrityValidation x = new UniqueFIDIntegrityValidation(...);
 * </code></pre>
 *
 * @author bowens, Refractions Research, Inc.
 * @author $Author: sploreg $ (last modification)
 * @version $Id$
 */
public class UniqueFIDIntegrityValidation implements IntegrityValidation {

    private String name;
    private String description;
    private String[] typeNames;
    // private String uniqueID;

    /**
     * UniqueFIDIntegrityValidation constructor.
     *
     * <p>Description
     */
    public UniqueFIDIntegrityValidation() {}

    /**
     * UniqueFIDIntegrityValidation constructor.
     *
     * <p>Description
     */
    public UniqueFIDIntegrityValidation(
            String name, String description, String[] typeNames, String uniqueID) {
        this.name = name;
        this.description = description;
        this.typeNames = typeNames;
        // this.uniqueID = uniqueID;
    }

    /**
     * Override setName.
     *
     * <p>Description ...
     *
     * @see org.geotools.validation.Validation#setName(java.lang.String)
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Override getName.
     *
     * <p>Description ...
     *
     * @see org.geotools.validation.Validation#getName()
     */
    public String getName() {
        return name;
    }

    /**
     * Override setDescription.
     *
     * <p>Description ...
     *
     * @see org.geotools.validation.Validation#setDescription(java.lang.String)
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Override getDescription.
     *
     * <p>Description ...
     *
     * @see org.geotools.validation.Validation#getDescription()
     */
    public String getDescription() {
        return description;
    }

    /**
     * Override getPriority.
     *
     * <p>Description ...
     *
     * @see org.geotools.validation.Validation#getPriority()
     */
    public int getPriority() {
        return 10;
    }

    /**
     * Override setTypeNames.
     *
     * <p>Description ...
     *
     * @see org.geotools.validation.Validation#setTypeNames(java.lang.String[])
     */
    public void setTypeNames(String[] names) {
        this.typeNames = names;
    }

    /**
     * Override getTypeNames.
     *
     * <p>Description ...
     *
     * @see org.geotools.validation.Validation#getTypeNames()
     */
    public String[] getTypeRefs() {
        return typeNames;
    }

    /**
     * Override validate.
     *
     * <p>Description ...
     *
     * @see org.geotools.validation.IntegrityValidation#validate(java.util.Map,
     *     org.locationtech.jts.geom.Envelope, org.geotools.validation.ValidationResults)
     */
    public boolean validate(Map layers, ReferencedEnvelope envelope, ValidationResults results)
            throws Exception {

        HashMap FIDs = new HashMap();
        boolean result = true;
        Iterator it = layers.values().iterator();

        while (it.hasNext()) // for each layer
        {
            SimpleFeatureSource featureSource = (SimpleFeatureSource) it.next();
            SimpleFeatureIterator features = featureSource.getFeatures().features();
            try {

                while (features.hasNext()) // for each feature
                {
                    SimpleFeature feature = features.next();
                    String fid = feature.getID();
                    if (FIDs.containsKey(fid)) // if a FID like this one already exists
                    {
                        results.error(feature, "FID already exists.");
                        result = false;
                    } else FIDs.put(fid, fid);
                }
            } finally {
                features.close(); // this is an important line
            }
        }

        return result;
    }
}
