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
import java.util.Set;
import org.geotools.util.Utilities;
import org.opengis.metadata.extent.Extent;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.temporal.TemporalReferenceSystem;
import org.opengis.util.GenericName;
import org.opengis.util.InternationalString;

/**
 *
 * @author Mehdi Sidhoum (Geomatys)
 *
 *
 * @source $URL$
 */
public class DefaultTemporalReferenceSystem implements TemporalReferenceSystem {

    /**
     * This is a name that uniquely identifies the temporal reference system.
     */
    private ReferenceIdentifier name;
    private Extent domainOfValidity;
    private Extent validArea;
    private InternationalString scope;
    private Collection<GenericName> alias;
    private Set<ReferenceIdentifier> identifiers;
    private InternationalString remarks;

    /**
     * Creates a new instance of TemporalReferenceSystem by passing a ReferenceIdentifier name and a domain of validity.
     * @param name
     * @param domainOfValidity
     */
    public DefaultTemporalReferenceSystem(ReferenceIdentifier name, Extent domainOfValidity) {
        this.name = name;
        this.domainOfValidity = domainOfValidity;
    }

    public ReferenceIdentifier getName() {
        return name;
    }

    public Extent getDomainOfValidity() {
        return domainOfValidity;
    }

    /**
     * This method is deprecated, please use getDomainOfValidity() method.
     * @return
     */
    @Deprecated
    public Extent getValidArea() {
        return validArea;
    }

    public InternationalString getScope() {
        return scope;
    }

    public Collection<GenericName> getAlias() {
        return alias;
    }

    public Set<ReferenceIdentifier> getIdentifiers() {
        return identifiers;
    }

    public InternationalString getRemarks() {
        return remarks;
    }

    public String toWKT() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * This is a name that uniquely identifies the temporal reference system.
     */
    public void setName(ReferenceIdentifier name) {
        this.name = name;
    }

    public void setDomainOfValidity(Extent domainOfValidity) {
        this.domainOfValidity = domainOfValidity;
    }

    public void setValidArea(Extent validArea) {
        this.validArea = validArea;
    }

    public void setScope(InternationalString scope) {
        this.scope = scope;
    }

    @Override
    public boolean equals(final Object object) {
        if (object instanceof DefaultTemporalReferenceSystem) {
            final DefaultTemporalReferenceSystem that = (DefaultTemporalReferenceSystem) object;

            return Utilities.equals(this.alias, that.alias) &&
                    Utilities.equals(this.domainOfValidity, that.domainOfValidity) &&
                    Utilities.equals(this.identifiers, that.identifiers) &&
                    Utilities.equals(this.name, that.name) &&
                    Utilities.equals(this.scope, that.scope) &&
                    Utilities.equals(this.validArea, that.validArea) &&
                    Utilities.equals(this.remarks, that.remarks);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (this.alias != null ? this.alias.hashCode() : 0);
        hash = 37 * hash + (this.domainOfValidity != null ? this.domainOfValidity.hashCode() : 0);
        hash = 37 * hash + (this.identifiers != null ? this.identifiers.hashCode() : 0);
        hash = 37 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 37 * hash + (this.remarks != null ? this.remarks.hashCode() : 0);
        hash = 37 * hash + (this.scope != null ? this.scope.hashCode() : 0);
        hash = 37 * hash + (this.validArea != null ? this.validArea.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("TemporalReferenceSystem:").append('\n');
        if (name != null) {
            s.append("name:").append(name).append('\n');
        }
        if (domainOfValidity != null) {
            s.append("domainOfValidity:").append(domainOfValidity).append('\n');
        }
        return s.toString();
    }
}
