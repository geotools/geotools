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
package org.geotools.ysld.encode;

import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.ysld.UomMapper;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.IOException;
import java.io.Writer;

/**
 * Encodes GeoTools style objects as Ysld.
 */
public class YsldEncoder {

    Writer out;
    UomMapper uomMapper;

    public YsldEncoder(Writer out, UomMapper uomMapper) {
        this.out = out;
        this.uomMapper = uomMapper;
    }

    public void encode(StyledLayerDescriptor sld) throws IOException {
        DumperOptions dumpOpts = new DumperOptions();
        dumpOpts.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        Yaml yaml = new Yaml(new Constructor(), new YsldRepresenter(uomMapper), dumpOpts);
        yaml.dumpAll(new RootEncoder(sld), out);
    }
}
