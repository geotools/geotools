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
import org.geotools.api.style.TraversingStyleVisitor;
import org.geotools.api.util.InternationalString;
import org.geotools.util.SimpleInternationalString;

public class DescriptionImpl implements org.geotools.api.style.Description {
    private InternationalString title;

    private InternationalString description;

    public DescriptionImpl() {
        title = null;
        description = null;
    }

    public DescriptionImpl(String title, String description) {
        this(new SimpleInternationalString(title), new SimpleInternationalString(description));
    }

    public DescriptionImpl(InternationalString title, InternationalString description) {
        this.title = title;
        this.description = description;
    }

    /** Copy constructor. */
    public DescriptionImpl(org.geotools.api.style.Description description) {
        this(description.getTitle(), description.getAbstract());
    }

    @Override
    public InternationalString getTitle() {
        return title;
    }

    @Override
    public void setTitle(InternationalString title) {
        this.title = title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title != null ? new SimpleInternationalString(title) : null;
    }

    @Override
    public InternationalString getAbstract() {
        return description;
    }

    @Override
    public void setAbstract(InternationalString description) {
        this.description = description;
    }

    @Override
    public void setAbstract(String description) {
        this.description = description != null ? new SimpleInternationalString(description) : null;
    }

    @Override
    public Object accept(TraversingStyleVisitor visitor, Object extraData) {
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
        result = prime * result + (description == null ? 0 : description.hashCode());
        result = prime * result + (title == null ? 0 : title.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        DescriptionImpl other = (DescriptionImpl) obj;
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
    static DescriptionImpl cast(org.geotools.api.style.Description description) {
        if (description == null) {
            return null;
        } else if (description instanceof DescriptionImpl) {
            return (DescriptionImpl) description;
        } else {
            DescriptionImpl copy = new DescriptionImpl();
            copy.setTitle(description.getTitle());
            copy.setAbstract(description.getAbstract());
            return copy;
        }
    }
}
