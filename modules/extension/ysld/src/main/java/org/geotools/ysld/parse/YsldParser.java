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

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geotools.styling.ResourceLocator;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.ysld.UomMapper;

/**
 * Parses a Yaml/Ysld stream into GeoTools style objects by returning a {@link
 * StyledLayerDescriptor} from the {@link #parse()} method.
 */
public class YsldParser extends YamlParser {

    List<ZoomContextFinder> zCtxtFinders = Collections.emptyList();

    UomMapper uomMapper = new UomMapper();

    ResourceLocator locator =
            new ResourceLocator() {

                @Override
                public URL locateResource(String uri) {
                    try {
                        return new URL(uri);
                    } catch (MalformedURLException e) {
                        throw new IllegalArgumentException(
                                String.format("'%s' is not a valid URI", uri), e);
                    }
                }
            };

    public YsldParser(InputStream ysld) throws IOException {
        super(ysld);
    }

    public YsldParser(Reader reader) throws IOException {
        super(reader);
    }

    public void setZoomContextFinders(List<ZoomContextFinder> zCtxtFinders) {
        this.zCtxtFinders = zCtxtFinders;
    }

    public void setResourceLocator(ResourceLocator locator) {
        this.locator = locator;
    }

    public void setUomMapper(UomMapper uomMapper) {
        this.uomMapper = uomMapper;
    }

    /**
     * Parse the yaml provided to this instance into a {@link StyledLayerDescriptor} and return the
     * result.
     */
    public StyledLayerDescriptor parse() throws IOException {

        // Hand off to the base class to parse the yaml, and provide a Ysld parser handler that will
        // transform the resulting
        // YamlObject into GeoTools-style objects.

        Map<String, Object> hints = new HashMap();
        hints.put("resourceLocator", locator);
        hints.put(UomMapper.KEY, uomMapper);
        return super.parse(new RootParser(zCtxtFinders), hints).sld();
    }
}
