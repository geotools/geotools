package org.geotools.gce.geotiff;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.imageio.IIOImage;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.event.IIOReadProgressListener;
import javax.imageio.event.IIOReadUpdateListener;
import javax.imageio.event.IIOReadWarningListener;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageInputStreamSpi;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;

import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.factory.GeoTools;
import org.geotools.factory.Hints;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;

public abstract class ImageReaderInspector extends AbstractList<RasterManager> implements List<RasterManager>  {
    private final static class ImageReaderAdapter extends ImageReader{
        
        @Override
        public String getFormatName() throws IOException {
            return adaptee.getFormatName();
        }

        @Override
        public ImageReaderSpi getOriginatingProvider() {
            return adaptee.getOriginatingProvider();
        }

        @Override
        public void setInput(Object input, boolean seekForwardOnly, boolean ignoreMetadata) {
            throw new UnsupportedOperationException(Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1,"Can't modify reader's state"));
        }

        @Override
        public void setInput(Object input, boolean seekForwardOnly) {
            throw new UnsupportedOperationException(Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1,"Can't modify reader's state"));
        }

        @Override
        public void setInput(Object input) {
            throw new UnsupportedOperationException(Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1,"Can't modify reader's state"));
        }

        @Override
        public Object getInput() {
            return adaptee.getInput();
        }

        @Override
        public boolean isSeekForwardOnly() {
            return adaptee.isSeekForwardOnly();
        }

        @Override
        public boolean isIgnoringMetadata() {
            return adaptee.isIgnoringMetadata();
        }

        @Override
        public int getMinIndex() {
            return adaptee.getMinIndex();
        }

        @Override
        public Locale[] getAvailableLocales() {
            return adaptee.getAvailableLocales();
        }

        @Override
        public void setLocale(Locale locale) {
            throw new UnsupportedOperationException(Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1,"Can't modify reader's state"));
        }

        @Override
        public Locale getLocale() {
            return adaptee.getLocale();
        }

        @Override
        public boolean isRandomAccessEasy(int imageIndex) throws IOException {
            return adaptee.isRandomAccessEasy(imageIndex);
        }

        @Override
        public float getAspectRatio(int imageIndex) throws IOException {
            return adaptee.getAspectRatio(imageIndex);
        }

        @Override
        public ImageTypeSpecifier getRawImageType(int imageIndex) throws IOException {
            return adaptee.getRawImageType(imageIndex);
        }

        @Override
        public ImageReadParam getDefaultReadParam() {
            return adaptee.getDefaultReadParam();
        }

        @Override
        public IIOMetadata getStreamMetadata(String formatName, Set<String> nodeNames)
                throws IOException {
            return adaptee.getStreamMetadata(formatName, nodeNames);
        }

        @Override
        public IIOMetadata getImageMetadata(int imageIndex, String formatName, Set<String> nodeNames)
                throws IOException {
            return adaptee.getImageMetadata(imageIndex, formatName, nodeNames);
        }

        @Override
        public BufferedImage read(int imageIndex) throws IOException {
            return adaptee.read(imageIndex);
        }

        @Override
        public IIOImage readAll(int imageIndex, ImageReadParam param) throws IOException {
            return adaptee.readAll(imageIndex, param);
        }

        @Override
        public Iterator<IIOImage> readAll(Iterator<? extends ImageReadParam> params)
                throws IOException {
            return adaptee.readAll(params);
        }

        @Override
        public boolean canReadRaster() {
            return adaptee.canReadRaster();
        }

        @Override
        public Raster readRaster(int imageIndex, ImageReadParam param) throws IOException {
            return adaptee.readRaster(imageIndex, param);
        }

        @Override
        public boolean isImageTiled(int imageIndex) throws IOException {
            return adaptee.isImageTiled(imageIndex);
        }

        @Override
        public int getTileWidth(int imageIndex) throws IOException {
            return adaptee.getTileWidth(imageIndex);
        }

        @Override
        public int getTileHeight(int imageIndex) throws IOException {
            return adaptee.getTileHeight(imageIndex);
        }

        @Override
        public int getTileGridXOffset(int imageIndex) throws IOException {
            return adaptee.getTileGridXOffset(imageIndex);
        }

        @Override
        public int getTileGridYOffset(int imageIndex) throws IOException {
            return adaptee.getTileGridYOffset(imageIndex);
        }

        @Override
        public BufferedImage readTile(int imageIndex, int tileX, int tileY) throws IOException {
            return adaptee.readTile(imageIndex, tileX, tileY);
        }

        @Override
        public Raster readTileRaster(int imageIndex, int tileX, int tileY) throws IOException {
            return adaptee.readTileRaster(imageIndex, tileX, tileY);
        }

        @Override
        public RenderedImage readAsRenderedImage(int imageIndex, ImageReadParam param)
                throws IOException {
            return adaptee.readAsRenderedImage(imageIndex, param);
        }

        @Override
        public boolean readerSupportsThumbnails() {
            return adaptee.readerSupportsThumbnails();
        }

        @Override
        public boolean hasThumbnails(int imageIndex) throws IOException {
            return adaptee.hasThumbnails(imageIndex);
        }

        @Override
        public int getNumThumbnails(int imageIndex) throws IOException {
            return adaptee.getNumThumbnails(imageIndex);
        }

        @Override
        public int getThumbnailWidth(int imageIndex, int thumbnailIndex) throws IOException {
            return adaptee.getThumbnailWidth(imageIndex, thumbnailIndex);
        }

        @Override
        public int getThumbnailHeight(int imageIndex, int thumbnailIndex) throws IOException {
            return adaptee.getThumbnailHeight(imageIndex, thumbnailIndex);
        }

        @Override
        public BufferedImage readThumbnail(int imageIndex, int thumbnailIndex) throws IOException {
            return adaptee.readThumbnail(imageIndex, thumbnailIndex);
        }

        @Override
        public synchronized void abort() {
            adaptee.abort();
        }

        @Override
        public void addIIOReadWarningListener(IIOReadWarningListener listener) {
            throw new UnsupportedOperationException(Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1,"Can't modify reader's state"));
        }

        @Override
        public void removeIIOReadWarningListener(IIOReadWarningListener listener) {
            throw new UnsupportedOperationException(Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1,"Can't modify reader's state"));
        }

        @Override
        public void removeAllIIOReadWarningListeners() {
            throw new UnsupportedOperationException(Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1,"Can't modify reader's state"));
        }

        @Override
        public void addIIOReadProgressListener(IIOReadProgressListener listener) {
            throw new UnsupportedOperationException(Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1,"Can't modify reader's state"));
        }

        @Override
        public void removeIIOReadProgressListener(IIOReadProgressListener listener) {
            throw new UnsupportedOperationException(Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1,"Can't modify reader's state"));
        }

        @Override
        public void removeAllIIOReadProgressListeners() {
            throw new UnsupportedOperationException(Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1,"Can't modify reader's state"));
        }

        @Override
        public void addIIOReadUpdateListener(IIOReadUpdateListener listener) {
            throw new UnsupportedOperationException(Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1,"Can't modify reader's state"));
        }

        @Override
        public void removeIIOReadUpdateListener(IIOReadUpdateListener listener) {
            throw new UnsupportedOperationException(Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1,"Can't modify reader's state"));
        }

        @Override
        public void removeAllIIOReadUpdateListeners() {
            throw new UnsupportedOperationException(Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1,"Can't modify reader's state"));
        }

        @Override
        public void reset() {
            throw new UnsupportedOperationException(Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1,"Can't modify reader's state"));
        }

        @Override
        public void dispose() {
            throw new UnsupportedOperationException(Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1,"Can't modify reader's state"));
        }

        @Override
        public int hashCode() {
            return adaptee.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return adaptee.equals(obj);
        }

        @Override
        public String toString() {
            return adaptee.toString();
        }

        private ImageReaderAdapter(final ImageReader reader) {
            super(reader.getOriginatingProvider());
            this.adaptee=reader;
        }

        private final ImageReader adaptee;

        @Override
        public int getNumImages(boolean allowSearch) throws IOException {
            return adaptee.getNumImages(allowSearch);
        }

        @Override
        public int getWidth(int imageIndex) throws IOException {
            return adaptee.getWidth(imageIndex);
        }

        @Override
        public int getHeight(int imageIndex) throws IOException {
            return adaptee.getHeight(imageIndex);
        }

        @Override
        public Iterator<ImageTypeSpecifier> getImageTypes(int imageIndex) throws IOException {
            return adaptee.getImageTypes(imageIndex);
        }

        @Override
        public IIOMetadata getStreamMetadata() throws IOException {
            return adaptee.getStreamMetadata();
        }

        @Override
        public IIOMetadata getImageMetadata(int imageIndex) throws IOException {
            return adaptee.getImageMetadata(imageIndex);
        }

        @Override
        public BufferedImage read(int imageIndex, ImageReadParam param) throws IOException {
            return adaptee.read(imageIndex, param);
        }
    }

    protected final Map<String, java.io.Serializable> sourceParams;
    
    /** Hints used by the {@link AbstractGridCoverage2DReader} subclasses. */
    private Hints hints = GeoTools.getDefaultHints();
    
    private List<RasterManager> elements;
    
    private ImageInputStreamSpi inStreamSPI;
    
    private ImageReaderSpi readerSPI;

    private int numImages;

    private int minIndex;
    
    public ImageReaderInspector(Map<String, Serializable> sourceParams, Hints hints,
            ImageInputStreamSpi inStreamSPI, ImageReaderSpi readerSPI) {
        this.sourceParams = sourceParams;
        this.hints = hints;
        this.inStreamSPI = inStreamSPI;
        this.readerSPI = readerSPI;
    }
    
    public ImageInputStreamSpi getInStreamSPI() {
        return inStreamSPI;
    }

    public ImageReaderSpi getReaderSPI() {
        return readerSPI;
    }


    @Override
    public RasterManager get(int index) {
        return elements.get(index);
    }

    @Override
    public int size() {
        return elements.size();
    }

    public Hints getHints() {
        return hints.clone();
    }
    

    protected void initialize() throws IOException {

        // instantiate a stream and a reader
        ImageInputStream inputStream= null;
        ImageReader reader =null;
        try{
            inputStream= createImageInputStream();
            reader = readerSPI.createReaderInstance();
            reader.setInput(inputStream);
            
            final RasterManagerBuilder rasterManagerBuilder=getRasterManagerBuilder();
            if(rasterManagerBuilder==null){
                throw new IOException("Unable to obtain a RasterManagerBuilder");
            }
            
            // get basic information
            this.numImages= reader.getNumImages(true);
            this.minIndex=reader.getMinIndex();
            
            // now parse the stream metadata
            if(rasterManagerBuilder.isParseStreamMetadata()){
                rasterManagerBuilder.parseStreamMetadata(reader.getStreamMetadata());
            }
            
            // now start parsing metadata plus other info for each image we have
            for(int i=0; i<this.numImages; i++){
                rasterManagerBuilder.parseImage(minIndex+i,reader);
            }
            
            this.elements=rasterManagerBuilder.create();
        } finally {
            if(inputStream!=null){
                try{
                    inputStream.close();
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
            if(reader!=null){
                try{
                    reader.dispose();
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }            
        }
    }

    protected abstract RasterManagerBuilder getRasterManagerBuilder();

    protected abstract ImageInputStream createImageInputStream() throws IOException;

}
