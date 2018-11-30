/* (c) 2018 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
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
