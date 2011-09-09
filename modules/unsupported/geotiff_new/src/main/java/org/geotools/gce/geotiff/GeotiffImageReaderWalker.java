package org.geotools.gce.geotiff;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;

import org.geotools.factory.Hints;
import org.geotools.gce.geotiff.ImageReaderSource.FileImageReaderSource;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;

public class GeotiffImageReaderWalker extends FileBasedImageReaderWalker {

    /** Logger. */
    private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(GeotiffImageReaderWalker.class);
    
    private ImageReader mainImageReader;
    private ImageReader externalOvrImageReader;
    private int mainFileNumImages;
    private int extOvrFileNumImages;

    private ImageInputStream mainImageInputStream;

    private ImageInputStream externalOvrImageInputStream;

    public GeotiffImageReaderWalker(URL sourceURL, Hints hints) throws IOException {
        super(sourceURL, hints, Utils.TIFFREADERFACTORY);

        
    }

    @Override
    protected TiffRasterManagerBuilder getRasterManagerBuilder() {
        return new TiffRasterManagerBuilder();
    }

    @Override
    protected ImageReader acquireReader(int i) throws IOException{
        if(mainImageReader==null)
            throw new IOException(Errors.format(ErrorKeys.DISPOSED_FACTORY));
        if(!hasExternalOverviews)
            return mainImageReader;
        if(i<mainFileNumImages)
            return mainImageReader;
        if(i<mainFileNumImages+extOvrFileNumImages)
            return externalOvrImageReader;
        throw new IOException(Errors.format(ErrorKeys.INDEX_OUT_OF_BOUNDS_$1));
    }

    @Override
    protected void releaseReader(int i)throws IOException {
//        do nothing
    }

    @Override
    protected void endWalk() throws IOException{
        dispose();
        
    }

    @Override
    protected void prepareElement(int i)throws IOException {
        // do nothing
    }

    @Override
    protected int getNumElemements()throws IOException {
        if(mainImageReader==null)
            return -1;
        return mainFileNumImages+extOvrFileNumImages;
    }

    @Override
    protected void startWalk() throws IOException {
        
        // check if we have ext ovr then open needed readers
        // MAIN FILE
        // create a stream
        mainImageInputStream=this.inStreamSPI.createInputStreamInstance(sourceFile,ImageIO.getUseCache(),ImageIO.getCacheDirectory());
        // create the main reader
        mainImageReader=this.readerSPI.createReaderInstance();
        mainImageReader.setInput(mainImageInputStream);
        
        // OVERVIEWS FILE
        // create a stream
        if(hasExternalOverviews){
            externalOvrImageInputStream=this.inStreamSPI.createInputStreamInstance(externalOverviewsFile,ImageIO.getUseCache(),ImageIO.getCacheDirectory());
            // create the main reader
            externalOvrImageReader=Utils.TIFFREADERFACTORY.createReaderInstance();
            externalOvrImageReader.setInput(externalOvrImageInputStream);
        }
        
        // now check the num image
        mainFileNumImages=mainImageReader.getNumImages(true);
        if(hasExternalOverviews){
            extOvrFileNumImages=externalOvrImageReader.getNumImages(true);
            
            // TODO if we have ext ovr we should somehow ignore additional pages that might bring overviews themselves
            }
            
        
    }

    private void dispose() {
        mainFileNumImages=-1;
        extOvrFileNumImages=-1;
        try {
            if(mainImageInputStream!=null)
                mainImageInputStream.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
        
        try {
            if(externalOvrImageInputStream!=null)
                externalOvrImageInputStream.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
        
        
        try {
            if(mainImageReader!=null)
                mainImageReader.dispose();
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            mainImageReader=null;
        }
        
        try {
            if(externalOvrImageReader!=null)
                externalOvrImageReader.dispose();
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            externalOvrImageReader=null;
        }
        
    }

    @Override
    protected IIOMetadata getStreamMetadata() throws IOException {
        if(mainImageReader==null)
            return null;
        return mainImageReader.getStreamMetadata();
    }

    @Override
    protected ImageReaderSource<?> getSource(int imageIndex) throws IOException {
        if(mainImageReader==null)
            throw new IOException(Errors.format(ErrorKeys.DISPOSED_FACTORY));
        if(!hasExternalOverviews)
            return super.getSource(imageIndex);
        if(imageIndex<mainFileNumImages)
            return super.getSource(imageIndex);
        if(imageIndex<mainFileNumImages+extOvrFileNumImages)
            return ImageReaderSource.wrapFile(imageIndex,externalOverviewsFile, super.inStreamSPI, Utils.TIFFREADERFACTORY);
        throw new IOException(Errors.format(ErrorKeys.INDEX_OUT_OF_BOUNDS_$1));
    }

}
