package org.geotools.ysld.encode;

import org.geotools.styling.StyledLayerDescriptor;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.Writer;

/**
 * Encodes GeoTools style objects as Ysld.
 */
public class YsldEncoder {

    Writer out;

    public YsldEncoder(Writer out) {
        this.out = out;
    }

    public void encode(StyledLayerDescriptor sld) throws IOException {
        DumperOptions dumpOpts = new DumperOptions();
        dumpOpts.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        Yaml yaml = new Yaml(dumpOpts);
        yaml.dumpAll(new RootEncoder(sld), out);
    }
}
