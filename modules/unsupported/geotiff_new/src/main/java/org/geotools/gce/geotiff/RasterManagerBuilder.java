package org.geotools.gce.geotiff;

import java.io.IOException;
import java.util.List;

import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;


public class RasterManagerBuilder<T extends ImageReader> {


    public void parseStreamMetadata(IIOMetadata streamMetadata)throws IOException {
    }

    public void parseImage(int i, T reader) throws IOException {
        
    }

    public boolean isParseStreamMetadata() {
        return false;
    }

    public List<RasterManager> create() {
        return null;
    }

}
    