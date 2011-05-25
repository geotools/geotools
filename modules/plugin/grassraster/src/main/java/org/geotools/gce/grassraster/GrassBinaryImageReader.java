/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2011, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.gce.grassraster;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.zip.DataFormatException;

import javax.imageio.IIOException;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import javax.media.jai.PlanarImage;

import org.geotools.gce.grassraster.core.GrassBinaryRasterReadHandler;
import org.geotools.gce.grassraster.metadata.GrassBinaryImageMetadata;
import org.geotools.gce.grassraster.spi.GrassBinaryImageReaderSpi;
import org.opengis.util.ProgressListener;

/**
 * ImageIO reader for the grass binary raster format.
 * <p>
 * The reader extends imageio's {@link ImageReader} to support the reading of GRASS raster data.
 * </p>
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 * @since 3.0
 * @see ImageReader
 * @see GrassBinaryRasterReadHandler
 * @see GrassBinaryImageMetadata
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/plugin/grassraster/src/main/java/org/geotools/gce/grassraster/GrassBinaryImageReader.java $
 */
public class GrassBinaryImageReader extends ImageReader {

    /**
     * This method set the input only if the input object is a File.
     */
    @Override
    public void setInput( Object input, boolean seekForwardOnly, boolean ignoreMetadata ) {
        if (input != null) {
            boolean found = false;
            if (originatingProvider != null) {
                Class<?>[] classes = originatingProvider.getInputTypes();
                for( int i = 0; i < classes.length; i++ ) {
                    if (classes[i].isInstance(input)) {
                        found = true;
                        break;
                    }
                }
            } else {
                if (input instanceof ImageInputStream) {
                    found = true;
                }
            }
            if (!found) {
                throw new IllegalArgumentException("Incorrect input type!");
            }

            this.seekForwardOnly = seekForwardOnly;
            this.ignoreMetadata = ignoreMetadata;
            this.minIndex = 0;
        }
        if (input instanceof File) {

            this.input = input;

        }

    }

    /**
     * The {@linkplain GrassBinaryImageMetadata metadata} associated to this reader.
     */
    private GrassBinaryImageMetadata metadata = null;

    /**
     * The {@linkplain GrassBinaryRasterReadHandler} that takes care of all the input/output needed
     * to read and write grass raster files.
     */
    private GrassBinaryRasterReadHandler rasterHandler = null;

    /**
     * the flag defining if there are some listeners attached to this reader.
     */
    private boolean hasListeners;

    /**
     * the {@link ColorModel} to be used for the read raster.
     */
    public ColorModel ccmdl = null;

    /**
     * the {@link SampleModel} associated to this reader.
     */
    private SampleModel csm = null;

    /**
     * the {@link ImageTypeSpecifier} associated to this reader.
     */
    private ImageTypeSpecifier imageType;

    /**
     * the hashmap holding reference of all the available images.
     */
    private HashMap<Integer, BufferedImage> imagesMap = new HashMap<Integer, BufferedImage>();


    private boolean useSubSamplingAsRequestedRowcols = false;
    private boolean castDoubleToFloating = false;

    /**
     * A progress monitor, set to a dummy one, in the case it is not set by the user.
     */
    private ProgressListener monitor = new DummyProgressListener();
    
    public void setUseSubSamplingAsRequestedRowcols( boolean useSubSamplingAsRequestedRowcols ) {
        this.useSubSamplingAsRequestedRowcols = useSubSamplingAsRequestedRowcols;
    }

    public void setCastDoubleToFloating( boolean castDoubleToFloating ) {
        this.castDoubleToFloating = castDoubleToFloating;
    }

    /**
     * constructs an {@link ImageReader} able to read grass raster maps.
     * 
     * @param originatingProvider the service provider interface for the reader.
     */
    public GrassBinaryImageReader( GrassBinaryImageReaderSpi originatingProvider ) {
        super(originatingProvider);
    }

    /**
     * ensures that metadata are read before any data are accessed.
     * <p>
     * Opens the Grass raster file header and checks for consistency as well as type and
     * compression. Also memorizes the rows addresses in the file.
     * </p>
     * <p>
     * This method has to be called before any data access, in order to already have the native
     * raster data metadata available.
     * </p>
     * 
     * @return <code>true</code> if everything is consistent
     * @throws IIOException
     */
    private void ensureOpen() throws IOException {
        if (rasterHandler == null) {
            rasterHandler = new GrassBinaryRasterReadHandler((File) input);
            rasterHandler.parseHeaderAndAccessoryFiles();
        }
    }

    public int getHeight( final int imageIndex ) throws IOException {
        // BufferedImage bufferedImage = imagesMap.get(imageIndex);
        // if (bufferedImage != null) {
        // return bufferedImage.getHeight();
        // }
        ensureOpen();
        if (rasterHandler != null) {
            return rasterHandler.getRasterMapHeight();
        }
        return -1;
    }

    public int getWidth( final int imageIndex ) throws IOException {
        ensureOpen();
        if (rasterHandler != null) {
            return rasterHandler.getRasterMapWidth();
        }
        return -1;
    }

    public int getNumImages( final boolean allowSearch ) throws IOException {
        return imagesMap.size();
    }

    public synchronized Iterator<ImageTypeSpecifier> getImageTypes( final int imageIndex )
            throws IOException {
        ensureOpen();
        csm = rasterHandler.getSampleModel();
        ccmdl = PlanarImage.createColorModel(csm);
        final List<ImageTypeSpecifier> l = new ArrayList<ImageTypeSpecifier>();

        if (imageType == null) {
            imageType = new ImageTypeSpecifier(ccmdl, csm);
            l.add(imageType);
        }
        return l.iterator();
    }

    public IIOMetadata getStreamMetadata() throws IOException {
        // grass raster data do not support stream metadata.
        return null;
    }

    public IIOMetadata getImageMetadata( final int imageIndex ) throws IOException {
        ensureOpen();
        if (metadata == null)
            metadata = new GrassBinaryImageMetadata(this.rasterHandler);
        return metadata;
    }
    
//    public PlanarImage read( final int imageIndex, ImageReadParam param,
//            boolean useSubSamplingAsRequestedRowcols, boolean castDoubleToFloating,
//            IProgressMonitorJGrass monitor, int tileSize ) throws IOException {
//        this.useSubSamplingAsRequestedRowcols = useSubSamplingAsRequestedRowcols;
//        this.castDoubleToFloating = castDoubleToFloating;
//        this.monitor = monitor;
//
//        ImageLayout layout = new ImageLayout();
//        layout.setTileWidth(tileSize);
//        layout.setTileHeight(tileSize);
//        RenderingHints hints = new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout);
//        ParameterBlock pbj = new ParameterBlock();
//        pbj.add(this.getInput());// the input, it can be file, ImageInputStream....
//        pbj.add(0);// the image index
//        pbj.add(true);// read metadata
//        pbj.add(false);// verify and validate data
//        pbj.add(null);// eventListener
//        pbj.add(null);// Locale
//        pbj.add(null);// ImageReadParam
//        pbj.add(this);// ImageRead
//
//        RenderedOp image = JAI.create("ImageRead", pbj, hints);
//
//        return image;
//    }
    
    public void setMonitor( ProgressListener monitor ) {
        this.monitor = monitor;
    }

    /**
     * Performs the read method adding the possibility to override subsampling.
     * 
     * @param imageIndex same as {@link GrassBinaryImageReader#read(int, ImageReadParam)}
     * @param param same as {@link GrassBinaryImageReader#read(int, ImageReadParam)}
     * @param useSubSamplingAsRequestedRowcols a flag that gives the possibility to bypass the
     *        imageio subsampling mechanism. With GRASS maps this is often more performant in some
     *        boundary situations. In the case this flag is set to true, the subsampling values will
     *        be handled as the requested columns and rows.
     * @param castDoubleToFloating a flag that gives the possibility to force the reading of a map
     *        as a floating point map. This is necessary right now because of a imageio bug:
     *        https://jai-imageio-core.dev.java.net/issues/show_bug.cgi?id=180
     * @return same as {@link GrassBinaryImageReader#read(int, ImageReadParam)}
     * @throws IOException same as {@link GrassBinaryImageReader#read(int, ImageReadParam)}
     */
    public BufferedImage read( final int imageIndex, ImageReadParam param,
            boolean useSubSamplingAsRequestedRowcols, boolean castDoubleToFloating,
            ProgressListener monitor ) throws IOException {
        this.useSubSamplingAsRequestedRowcols = useSubSamplingAsRequestedRowcols;
        this.castDoubleToFloating = castDoubleToFloating;
        this.monitor = monitor;

        return read(imageIndex, param);
    }

    public BufferedImage read( final int imageIndex, ImageReadParam param ) throws IOException {
        ensureOpen();

        if (hasListeners) {
            clearAbortRequest();
            processImageStarted(0);
        }

        Raster readRaster = readRaster(imageIndex, param);

        csm = rasterHandler.getSampleModel();
        ccmdl = PlanarImage.createColorModel(csm);
        imageType = new ImageTypeSpecifier(ccmdl, csm);

        final BufferedImage bi = new BufferedImage(ccmdl, (WritableRaster) readRaster, false, null);

        if (hasListeners) {
            if (rasterHandler.isAborting())
                processReadAborted();
            else
                processImageComplete();
        }
        return bi;
    }

    public BufferedImage read( final int imageIndex ) throws IOException {
        ensureOpen();
        return read(imageIndex, null);
    }

    public Raster readRaster( final int imageIndex, ImageReadParam param ) throws IOException {
        ensureOpen();
        try {
            return rasterHandler.readRaster(param, useSubSamplingAsRequestedRowcols,
                    castDoubleToFloating, monitor);
        } catch (DataFormatException e) {
            throw new IOException(e.getLocalizedMessage());
        }
    }

    /**
     * A simple method which returns the proper {@link GrassBinaryRasterReadHandler} used to perform
     * reading operations
     * 
     * @return Returns the rasterReader.
     */
    public GrassBinaryRasterReadHandler getRasterReader() {
        return rasterHandler;
    }

    /**
     * Cleans this {@link GrassBinaryImageReader} up.
     */
    public void dispose() {
        if (rasterHandler != null) {
            try {
                rasterHandler.close();
            } catch (IOException e) {
                throw new RuntimeException(e.getCause());
            }
            rasterHandler = null;
        }
        super.dispose();
    }

    /**
     * Resets this {@link GrassBinaryImageReader}.
     */
    public void reset() {
        dispose();
        super.setInput(null, false, false);
        rasterHandler = null;
        csm = null;
        imageType = null;
        metadata = null;
    }

    /**
     * Request to abort any current read operation.
     */
    public synchronized void abort() {
        // super.abort();
        if (rasterHandler != null) {
            rasterHandler.abort();
            rasterHandler = null;
        }
    }

    /**
     * Checks if a request to abort the current read operation has been made.
     */
    protected synchronized boolean abortRequested() {
        return rasterHandler.isAborting();
    }



}
