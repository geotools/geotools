/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling;

import org.geotools.api.style.StyleVisitor;
import org.geotools.api.util.InternationalString;
import org.geotools.util.SimpleInternationalString;

public class Description implements org.geotools.api.style.Description {
    private InternationalString title;

    private InternationalString description;

    public Description() {
        title = null;
        description = null;
    }

    public Description(String title, String description) {
        this(new SimpleInternationalString(title), new SimpleInternationalString(description));
    }

    public Description(InternationalString title, InternationalString description) {
        this.title = title;
        this.description = description;
    }

    /** Copy constructor. */
    public Description(org.geotools.api.style.Description description) {
        this(description.getTitle(), description.getAbstract());
    }

    @Override
    public InternationalString getTitle() {
        return title;
    }

    public void setTitle(InternationalString title) {
        this.title = title;
    }

    public void setTitle(String title) {
        this.title = title != null ? new SimpleInternationalString(title) : null;
    }

    @Override
    public InternationalString getAbstract() {
        return description;
    }

    public void setAbstract(InternationalString description) {
        this.description = description;
    }

    public void setAbstract(String description) {
        this.description = description != null ? new SimpleInternationalString(description) : null;
    }

    @Override
    public Object accept(org.geotools.api.style.StyleVisitor visitor, Object extraData) {
        return null;
    }

    @Override
    public void accept(StyleVisitor visitor) {
        // nothing to do
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Description other = (Description) obj;
        if (description == null) {
            if (other.description != null) return false;
        } else if (!description.equals(other.description)) return false;
        if (title == null) {
            if (other.title != null) return false;
        } else if (!title.equals(other.title)) return false;
        return true;
    }

    /**
     * Check the provided description return it as a DescriptionImpl
     *
     * @return DescriptionImpl from the provided description
     */
    static Description cast(org.geotools.api.style.Description description) {
        if (description == null) {
            return null;
        } else if (description instanceof Description) {
            return (Description) description;
        } else {
            Description copy = new Description();
            copy.setTitle(description.getTitle());
            copy.setAbstract(description.getAbstract());
            return copy;
        }
    }
}
