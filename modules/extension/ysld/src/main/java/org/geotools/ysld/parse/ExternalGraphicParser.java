/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016 Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2016 Boundless Spatial
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
package org.geotools.ysld.parse;

import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.ResourceLocator;
import org.geotools.ysld.YamlMap;
import org.geotools.ysld.YamlObject;

/** Handles parsing a Ysld "external" property into a {@link ExternalGraphic} object. */
public abstract class ExternalGraphicParser extends YsldParseHandler {

    ExternalGraphic external;

    public ExternalGraphicParser(Factory factory) {
        super(factory);
        external = factory.style.createExternalGraphic((String) null, null);
    }

    protected abstract void externalGraphic(ExternalGraphic externalGraphic);

    @Override
    public void handle(YamlObject<?> obj, YamlParseContext context) {
        externalGraphic(external);
        YamlMap map = obj.map();
        if (map.has("url")) {
            String value = map.str("url");
            try {
                external.setLocation(
                        ((ResourceLocator) context.getDocHint("resourceLocator"))
                                .locateResource(value));
            } catch (IllegalArgumentException e) {
                external.setURI("file:" + value);
            }
        }
        external.setFormat(map.str("format"));
    }
}
