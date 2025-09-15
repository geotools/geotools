/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.temporal.reference;

import java.util.Collection;
import org.geotools.api.metadata.extent.Extent;
import org.geotools.api.referencing.ReferenceIdentifier;
import org.geotools.api.temporal.OrdinalEra;
import org.geotools.api.temporal.OrdinalReferenceSystem;
import org.geotools.util.Utilities;

/** @author Mehdi Sidhoum (Geomatys) */
public class DefaultOrdinalReferenceSystem extends DefaultTemporalReferenceSystem implements OrdinalReferenceSystem {

    /** An ordinal temporal reference system consists of a set of ordinal eras. */
    private Collection<OrdinalEra> ordinalEraSequence;

    public DefaultOrdinalReferenceSystem(
            ReferenceIdentifier name, Extent domainOfValidity, Collection<OrdinalEra> ordinalEraSequence) {
        super(name, domainOfValidity);
        this.ordinalEraSequence = ordinalEraSequence;
    }

    @Override
    public Collection<OrdinalEra> getOrdinalEraSequence() {
        return ordinalEraSequence;
    }

    @Override
    public String toWKT() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof DefaultOrdinalReferenceSystem that && super.equals(object)) {

            return Utilities.equals(this.ordinalEraSequence, that.ordinalEraSequence);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 37 * hash + (this.ordinalEraSequence != null ? this.ordinalEraSequence.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("OrdinalReferenceSystem:").append('\n');
        if (ordinalEraSequence != null) {
            s.append("ordinalEraSequence:").append(ordinalEraSequence).append('\n');
        }
        return super.toString().concat("\n").concat(s.toString());
    }
}
