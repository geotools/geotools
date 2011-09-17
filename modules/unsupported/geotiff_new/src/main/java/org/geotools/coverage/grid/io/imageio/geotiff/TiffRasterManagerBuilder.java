package org.geotools.coverage.grid.io.imageio.geotiff;

import it.geosolutions.imageio.plugins.tiff.TIFFField;
import it.geosolutions.imageioimpl.plugins.tiff.TIFFIFD;
import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageMetadata;
import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReader;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;

import org.geotools.coverage.grid.RasterLayout;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.imageio.ImageReaderSource;
import org.geotools.factory.Hints;
import org.geotools.gce.geotiff.GeoTiffUtils;
import org.geotools.gce.geotiff.RasterDescriptor;
import org.geotools.gce.geotiff.RasterManager;
import org.geotools.gce.geotiff.RasterManagerBuilder;
import org.geotools.gce.geotiff.RasterSlice;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;


public class TiffRasterManagerBuilder extends RasterManagerBuilder<TIFFImageReader> {

    /** Logger. */
    private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(TiffRasterManagerBuilder.class);
    
    private List<RasterDescriptor> descriptors = new ArrayList<RasterDescriptor>();
    
    private List<RasterSlice> slices= new ArrayList<RasterSlice>();
    
    public TiffRasterManagerBuilder() {
        super(new Hints());
    }
    
    public TiffRasterManagerBuilder(Hints hints) {
        super(hints);
    }


    @Override
    public void addElement(int index, TIFFImageReader reader, ImageReaderSource<?> source) throws IOException {
    
        final RasterSlice slice = new RasterSlice();
        slice.source=source;
        
        // get metadata and check if this is an overview or not
        final TIFFImageMetadata metadata = (TIFFImageMetadata) reader.getImageMetadata(index);
        final TIFFIFD IFD = metadata.getRootIFD();
        
        //
        // Overviews or full resolution?
        //
        boolean fullResolution=true;
//        boolean multipage=false;
        final int newSubfileType;
        TIFFField tifField=null;
        if((tifField=IFD.getTIFFField(254))!=null)
            newSubfileType=tifField.getAsInt(0);
        else
            newSubfileType=0;// default is single independent image
        fullResolution=(newSubfileType&0x1)!=1?true:false;
//        multipage=((newSubfileType>>1)&0x2)!=1?true:false;
        slice.overview=!fullResolution;
        if(!slice.overview&& !slices.isEmpty())
            createRasterDescriptor();
//        //
//        // Page number 
//        //
//        final int pageNumber;
//        if((tifField=IFD.getTIFFField(297))!=null){
//            pageNumber=tifField.getAsInt(0);
//            slice.pageIndex=pageNumber;
//        }
//        else
//            pageNumber=-1;// default is single independent image

        //
        // Raster dimensions
        //
        final int hrWidth = reader.getWidth(index);
        final int hrHeight = reader.getHeight(index);
        final int hrTileW = reader.getTileWidth(index);
        final int hrTileH = reader.getTileHeight(index);
        final RasterLayout rasterLayout = new RasterLayout(
                0,
                0,
                hrWidth,
                hrHeight,
                reader.getTileGridXOffset(index),
                reader.getTileGridXOffset(index),
                hrTileW,
                hrTileH);
        slice.rasterDimensions=rasterLayout;
        
        //
        // get sample image
        //
        final ImageReadParam readParam = reader.getDefaultReadParam();
        readParam.setSourceRegion(new Rectangle(0, 0, 2, 2));
        final BufferedImage sampleImage = reader.read(index, readParam);
        final ImageTypeSpecifier imageType = new ImageTypeSpecifier(sampleImage);
        slice.imageType=imageType;
        
        double noDataValue;
        CoordinateReferenceSystem crs=null;
        AffineTransform raster2Model=null;
        ReferencedEnvelope bbox=null;
        if(fullResolution){
            
            ////
            //
            // THIS IS A FULL RESOLUTION PAGE
            //
            ////
            
            // 
            // Now load geotiff metadata
            //
            final GeoTiffIIOMetadataDecoder decoder = new GeoTiffIIOMetadataDecoder(metadata);
            
            //
            // NO DATA
            //  
            if (decoder.hasNoData())
                noDataValue = decoder.getNoData();
            
            // //
            //
            // CRS INFO
            //
            // //
            GeoTiffMetadata2CRSAdapter gtcs = null;
            final Object tempCRS = this.hints.get(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM);
            if (tempCRS != null) {
                crs = (CoordinateReferenceSystem) tempCRS;
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, "Using forced coordinate reference system");
            } else {

                // metadata decoder
                gtcs=new GeoTiffMetadata2CRSAdapter(hints);
                // check metadata first
                if (decoder.hasGeoKey()&& gtcs != null)
                    try {
                        crs = gtcs.createCoordinateSystem(decoder);
                    } catch (FactoryException e) {
                       throw new IOException(e);
                    }

                if (crs == null)
                    crs = GeoTiffUtils.getCRS(source.getSource());
            }

            if (crs == null){
                if(LOGGER.isLoggable(Level.WARNING)){
                    LOGGER.warning("Coordinate Reference System is not available");
                }
                crs = AbstractGridFormat.getDefaultCRS();
            }
            
            if (gtcs != null&& metadata!=null&& (decoder.hasModelTrasformation()||(decoder.hasPixelScales()&&decoder.hasTiePoints()))) {
                // TODO I hate all thiese casts
                raster2Model = (AffineTransform) GeoTiffMetadata2CRSAdapter.getRasterToModel(decoder);
            } else {
                // TODO I hate all thiese casts                
                raster2Model = (AffineTransform) GeoTiffUtils.parseWorldFile(source);
            }
    
            if (raster2Model == null) {
                // TODO test this
                raster2Model=AffineTransform.getScaleInstance(0, 0);
            }
            slice.gridToWorld=raster2Model;
            
            final AffineTransform tempTransform = new AffineTransform(raster2Model);
            tempTransform.translate(-0.5, -0.5);
            try {
                GeneralEnvelope bbox_ = CRS.transform(ProjectiveTransform
                        .create(tempTransform), new GeneralEnvelope(rasterLayout.getBounds()));
                bbox_.setCoordinateReferenceSystem(crs);
                bbox= new ReferencedEnvelope(bbox_);
            } catch (TransformException e) {
                new IOException(e);
            }
            slice.envelope=bbox;
            
        } 
        slices.add(slice);
        
        
//        System.out.println(new IIOMetadataDumper(metadata,metadata.getNativeMetadataFormatName()).getMetadata());

    }

    private void createRasterDescriptor() {
        descriptors.add(new RasterDescriptor(slices));
        slices.clear();
        
    }

    /**
     * We do not need to parse the stream metadata for the tiff {@link ImageReader}.
     * 
     */
    @Override
    public boolean needsStreamMetadata() {
        return false;
    }

    @Override
    public List<RasterManager> create() {
        // do one last internal create
        if(!slices.isEmpty())
            this.descriptors.add(new RasterDescriptor(slices));
//        return new ArrayList<RasterManager>(descriptors);ù
        return null;
    }

    @Override
    public void parseStreamMetadata(IIOMetadata streamMetadata) throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void dispose() {
        if(descriptors!=null)
            descriptors.clear();
        descriptors=null;
        
        if(slices!=null)
            slices.clear();
        slices=null;
        
    }

}
