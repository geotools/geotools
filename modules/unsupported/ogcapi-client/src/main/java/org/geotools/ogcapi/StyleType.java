/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.ogcapi;

import java.io.IOException;
import java.net.URL;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.xml.styling.SLDParser;

public class StyleType {
    String identifier;
    String title;
    Style sld;
    static StyleFactory factory = CommonFactoryFinder.getStyleFactory();

    private Style loadStyleFromSLD(URL url) throws IOException {

        SLDParser stylereader = new SLDParser(factory, url);
        Style[] style = stylereader.readXML();

        return style[0];
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Style getSld() {
        return sld;
    }

    public void setSld(Style sld) {
        this.sld = sld;
    }

    public void setSld(URL url) throws IOException {
        this.sld = loadStyleFromSLD(url);
    }

    @Override
    public String toString() {
        return "StyleType [identifier=" + identifier + ", title=" + title + "]";
    }
}
