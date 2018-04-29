/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.sld.bindings;

import javax.xml.namespace.QName;
import org.geotools.styling.StyleFactory;

/**
 *
 *
 * <pre>
 *         <code>
 *    &lt;xsd:element name="Title" type="sld:InternationalStringType"/&gt;
 *          </code>
 *         </pre>
 *
 * @author "Mauro Bartolomeoli - mauro.bartolomeoli@geo-solutions.it"
 */
public class SLDTitleBinding extends SLDInternationalStringBinding {

    /** @param styleFactory */
    public SLDTitleBinding(StyleFactory styleFactory) {
        super(styleFactory);
    }

    @Override
    public QName getTarget() {
        return SLD.TITLE;
    }
}
