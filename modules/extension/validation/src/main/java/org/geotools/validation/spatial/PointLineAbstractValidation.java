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

import org.geotools.validation.DefaultIntegrityValidation;


/**
 * PointCoveredByLineValidation purpose.
 * 
 * <p>
 * Basic typeref functionality for a point-line validation.
 * </p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 * @source $URL$
 * @version $Id$
 */
public abstract class PointLineAbstractValidation
    extends DefaultIntegrityValidation {
    private String restrictedLineTypeRef;
    private String pointTypeRef;

    /**
     * PointCoveredByLineValidation constructor.
     * 
     * <p>
     * Super
     * </p>
     */
    public PointLineAbstractValidation() {
        super();
    }

    /**
     * Implementation of getTypeNames. Should be called by sub-classes is being
     * overwritten.
     *
     * @return Array of typeNames, or empty array for all, null for disabled
     *
     * @see org.geotools.validation.Validation#getTypeNames()
     */
    public String[] getTypeRefs() {
        if ((pointTypeRef == null) || (restrictedLineTypeRef == null)) {
            return null;
        }

        return new String[] { pointTypeRef, restrictedLineTypeRef };
    }

    /**
     * Access pointTypeRef property.
     *
     * @return Returns the pointTypeRef.
     */
    public final String getRestrictedLineTypeRef() {
        return restrictedLineTypeRef;
    }

    /**
     * Set pointTypeRef to pointTypeRef.
     *
     * @param lineTypeRef The pointTypeRef to set.
     */
    public final void setRestrictedLineTypeRef(String lineTypeRef) {
        this.restrictedLineTypeRef = lineTypeRef;
    }

    /**
     * Access restrictedLineTypeRef property.
     *
     * @return Returns the restrictedLineTypeRef.
     */
    public final String getPointTypeRef() {
        return pointTypeRef;
    }

    /**
     * Set restrictedLineTypeRef to restrictedLineTypeRef.
     *
     * @param polygonTypeRef The restrictedLineTypeRef to set.
     */
    public final void setPointTypeRef(String polygonTypeRef) {
        this.pointTypeRef = polygonTypeRef;
    }
}
