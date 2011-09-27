package org.geotools.gce;

import java.io.IOException;
import java.util.List;

import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;

import org.geotools.coverage.grid.io.imageio.ImageReaderSource;
import org.geotools.factory.Hints;
import org.geotools.util.Utilities;


/**
 * 
 *
 * @source $URL$
 */
public abstract class RasterManagerBuilder<T extends ImageReader> {

    protected final Hints hints;

    public abstract void parseStreamMetadata(IIOMetadata streamMetadata)throws IOException ;
    
    public RasterManagerBuilder(Hints hints) {
        Utilities.ensureNonNull("hints", hints);
        this.hints = hints;
    }

    /**
     * Uses the provided {@link ImageReader} to get relevant information about this element.
     * This usually means, getting basic info from the reader itself as well as geting the 
     * {@link IIOMetadata} and parsing it.
     * 
     * <p>
     * Notice that we should <strong>NOT</strong> take ownership of this provided reader.
     * It is duty of the caller to close it properly.
     * 
     * @param i element position in the {@link ImageReader} indexese range.
     * @param reader
     * @throws IOException
     */
    public abstract void addElement(int i, T reader, ImageReaderSource<?> source) throws IOException;
    
    public boolean needsStreamMetadata() {
        return false;
    }

    public abstract List<RasterManager> create() ;

    public abstract void dispose() ;

}
    
