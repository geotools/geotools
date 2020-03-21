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

import java.io.IOException;
import java.io.Writer;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.ysld.UomMapper;
import org.geotools.ysld.YamlUtil;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

/** Encodes GeoTools style objects as Ysld. */
public class YsldEncoder {

    Writer out;

    UomMapper uomMapper;

    /**
     * Create a YSLD Encoder.
     *
     * @param out Writer which the encoded YSLD output will be written to.
     * @param uomMapper An instance of {@link UomMapper}, used to map UOM.
     */
    public YsldEncoder(Writer out, UomMapper uomMapper) {
        this.out = out;
        this.uomMapper = uomMapper;
    }

    /** Encode the passed {@link StyledLayerDescriptor} as YSLD. */
    public void encode(StyledLayerDescriptor sld) throws IOException {
        DumperOptions dumpOpts = new DumperOptions();
        dumpOpts.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        Yaml yaml = YamlUtil.getSafeYaml(new YsldRepresenter(uomMapper), dumpOpts);
        yaml.dumpAll(new RootEncoder(sld), out);
    }
}
