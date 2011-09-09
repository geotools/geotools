package org.geotools.gce.geotiff;

import java.io.IOException;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageInputStreamSpi;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;

import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.factory.GeoTools;
import org.geotools.factory.Hints;

public abstract class ImageReaderWalker extends AbstractList<RasterManager> implements List<RasterManager>  {
   
    protected final Map<String, java.io.Serializable> sourceParams;
    
    /** Hints used by the {@link AbstractGridCoverage2DReader} subclasses. */
    private Hints hints = GeoTools.getDefaultHints();
    
    private List<RasterManager> elements;
    
    protected final  ImageInputStreamSpi inStreamSPI;
    
    protected final  ImageReaderSpi readerSPI;
    
    public ImageReaderWalker(Map<String, Serializable> sourceParams, Hints hints,
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
        if(elements==null)
            return 0;
        return elements.size();
    }

    public Hints getHints() {
        return hints.clone();
    }
    

    protected void initialize() throws IOException {

        // get a raster manager builder
        final RasterManagerBuilder rasterManagerBuilder=getRasterManagerBuilder();
        if(rasterManagerBuilder==null){
            throw new IOException("Unable to obtain a RasterManagerBuilder");
        }
           
        //
        // Tell underlying implementation that we are starting to process
        //
        startWalk();
        
        //
        // Stream metadata
        //
        // now parse the stream metadata
        if(rasterManagerBuilder.needsStreamMetadata()){
            rasterManagerBuilder.parseStreamMetadata(getStreamMetadata());
        }        
        
        
        //
        // Main Loop on each image
        //
        // now start parsing metadata plus other info for each image we have
        final int numSteps=getNumElemements();
        for(int i=0; i<numSteps; i++){
            prepareElement(i);
            try{
                rasterManagerBuilder.addElement(i,acquireReader(i),getSource(i));
            } finally {
                releaseReader(i);
            }
        }
        
        //
        // Release Resources
        //
        endWalk();
        
        //
        // Create raster managers
        //
        this.elements=rasterManagerBuilder.create();
 
    }

    protected abstract ImageReaderSource<?> getSource(int i)throws IOException;
    
    protected abstract ImageReader acquireReader(int i)throws IOException;
    
    protected abstract void releaseReader(int i)throws IOException;

    protected abstract void endWalk()throws IOException ;

    protected abstract void prepareElement(int i)throws IOException;

    protected abstract int getNumElemements()throws IOException;

    protected abstract void startWalk() throws IOException;
    
    protected abstract IIOMetadata getStreamMetadata() throws IOException;

    protected abstract RasterManagerBuilder getRasterManagerBuilder();

    protected abstract ImageInputStream createImageInputStream() throws IOException;

}
