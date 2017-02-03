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

import org.geotools.util.logging.Logging;
import org.geotools.ysld.YamlObject;

import java.util.logging.Logger;

/**
 * Extends {@link YamlParseHandler} to handle parsing YSLD into GeoTools-style objects. The {@link RootParser} subclass is the "entrypoint" for
 * parsing a {@link YamlObject} into GeoTools-style objects.
 * 
 */
public abstract class YsldParseHandler extends YamlParseHandler {

    protected static Logger LOG = Logging.getLogger(YsldParser.class);

    protected Factory factory;

    protected YsldParseHandler(Factory factory) {
        this.factory = factory;
    }
}
