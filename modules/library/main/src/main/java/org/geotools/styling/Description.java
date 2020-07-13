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

import org.opengis.util.InternationalString;

public interface Description extends org.opengis.style.Description {

    /**
     * Human readable title.
     *
     * <p>
     *
     * @return the human readable title.
     */
    InternationalString getTitle();

    void setTitle(InternationalString title);

    /** Define title using the current locale. */
    void setTitle(String title);

    /** Human readable description. */
    InternationalString getAbstract();

    void setAbstract(InternationalString description);

    /**
     * Define description in the current locale.
     *
     * @param description Abstract providing summary of contents
     */
    void setAbstract(String description);

    /**
     * calls the visit method of a StyleVisitor
     *
     * @param visitor the style visitor
     */
    void accept(StyleVisitor visitor);
}
