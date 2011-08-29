package org.geotools.gce.geotiff;

import it.geosolutions.imageio.plugins.tiff.TIFFField;
import it.geosolutions.imageioimpl.plugins.tiff.TIFFIFD;
import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageMetadata;
import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReader;

import java.io.IOException;
import java.util.List;

import javax.imageio.ImageReader;

import org.geotools.coverage.grid.io.imageio.IIOMetadataDumper;

public class TiffRasterManagerBuilder extends RasterManagerBuilder<TIFFImageReader> {


    @Override
    public void parseImage(int index, TIFFImageReader reader) throws IOException {
        
        // get metadata and check if this is an overview or not
        final TIFFImageMetadata metadata = (TIFFImageMetadata) reader.getImageMetadata(index);
        final TIFFIFD IFD = metadata.getRootIFD();
        
        //
        // Overviews or full resolution?
        //
        boolean fullResolution=true;
        boolean multipage=false;
        final int newSubfileType;
        TIFFField tifField=null;
        if((tifField=IFD.getTIFFField(254))!=null)
            newSubfileType=tifField.getAsInt(0);
        else
            newSubfileType=0;// default is single independent image
        fullResolution=(newSubfileType&0x1)!=1?true:false;
        multipage=((newSubfileType>>1)&0x2)!=1?true:false;
        
        //
        // Page number 
        //
        final int pageNumber;
        if((tifField=IFD.getTIFFField(254))!=null)
            pageNumber=tifField.getAsInt(0);
        else
            pageNumber=-1;// default is single independent image
        
        
//        System.out.println(new IIOMetadataDumper(metadata,metadata.getNativeMetadataFormatName()).getMetadata());

    }

    /**
     * We do not need to parse the stream metadata for the tiff {@link ImageReader}.
     * 
     */
    @Override
    public boolean isParseStreamMetadata() {
        return false;
    }

    @Override
    public List<RasterManager> create() {
        // TODO Auto-generated method stub
        return super.create();
    }

}
