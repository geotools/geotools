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
import java.util.Date;
import org.geotools.util.Utilities;
import org.opengis.temporal.OrdinalEra;
import org.opengis.util.InternationalString;

/**
 *
 * @author Mehdi Sidhoum (Geomatys)
 *
 * @source $URL$
 */
public class DefaultOrdinalEra implements OrdinalEra {

    /**
     * This is a string that identifies the ordinal era within the TM_OrdinalReferenceSystem.
     */
    private InternationalString name;
    /**
     * This is the temporal position at which the ordinal era began, if it is known.
     */
    private Date beginning;
    /**
     * This is the temporal position at which the ordinal era ended.
     */
    private Date end;
    private Collection<OrdinalEra> composition;
    private DefaultOrdinalEra group;

    public DefaultOrdinalEra(InternationalString name, Date beginning, Date end) {
        if (! beginning.before(end))
            throw new IllegalArgumentException("The beginning date of the OrdinalEra must be less than (i.e. earlier than) the end date of this OrdinalEra.");
        this.name = name;
        this.beginning = beginning;
        this.end = end;
    }

    public DefaultOrdinalEra(InternationalString name, Date beginning, Date end, Collection<OrdinalEra> composition) {
        this.name = name;
        this.beginning = beginning;
        this.end = end;

        for (OrdinalEra ordinalEra : composition) {
            ((DefaultOrdinalEra) ordinalEra).setGroup(this);
        }
    }

    public InternationalString getName() {
        return name;
    }

    public Date getBeginning() {
        return beginning;
    }

    public Date getEnd() {
        return end;
    }

    public Collection<OrdinalEra> getComposition() {
        return composition;
    }

    public void setName(InternationalString name) {
        this.name = name;
    }

    public void setBeginning(Date beginning) {
        this.beginning = beginning;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public DefaultOrdinalEra getGroup() {
        return group;
    }

    public void setGroup(DefaultOrdinalEra group) {
        this.group = group;
    }

    @Override
    public boolean equals(final Object object) {
        if (object instanceof DefaultOrdinalEra) {
            final DefaultOrdinalEra that = (DefaultOrdinalEra) object;

            return Utilities.equals(this.beginning, that.beginning) &&
                    Utilities.equals(this.end, that.end) &&
                    Utilities.equals(this.composition, that.composition) &&
                    Utilities.equals(this.group, that.group) &&
                    Utilities.equals(this.name, that.name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (this.beginning != null ? this.beginning.hashCode() : 0);
        hash = 37 * hash + (this.end != null ? this.end.hashCode() : 0);
        hash = 37 * hash + (this.composition != null ? this.composition.hashCode() : 0);
        hash = 37 * hash + (this.group != null ? this.group.hashCode() : 0);
        hash = 37 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("OrdinalEra:").append('\n');
        if (name != null) {
            s.append("name:").append(name).append('\n');
        }
        if (beginning != null) {
            s.append("beginning:").append(beginning).append('\n');
        }
        if (end != null) {
            s.append("end:").append(end).append('\n');
        }
        if (composition != null) {
            s.append("composition:").append(composition).append('\n');
        }
        if (group != null) {
            s.append("group:").append(group).append('\n');
        }
        return s.toString();
    }
}
