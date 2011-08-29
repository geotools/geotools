package org.geotools.gce.geotiff;

import java.io.IOException;
import java.net.URL;

import org.geotools.factory.Hints;

public class GeotiffImageReaderInspector extends FileBasedImageReaderInspector {

    public GeotiffImageReaderInspector(URL sourceURL, Hints hints) throws IOException {
        super(sourceURL, hints, Utils.TIFFREADERFACTORY);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected TiffRasterManagerBuilder getRasterManagerBuilder() {
        return new TiffRasterManagerBuilder();
    }

}
