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
package org.geotools.ysld;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.representer.Representer;

/** Factory for Yaml instances. */
public class YamlUtil {

    public static Yaml getSafeYaml() {
        return getSafeYaml(new DumperOptions());
    }

    public static Yaml getSafeYaml(DumperOptions dumperOptions) {
        return getSafeYaml(new Representer(), dumperOptions);
    }

    public static Yaml getSafeYaml(Representer representer, DumperOptions dumperOptions) {
        return new Yaml(new SafeConstructor(), representer, dumperOptions);
    }
}
