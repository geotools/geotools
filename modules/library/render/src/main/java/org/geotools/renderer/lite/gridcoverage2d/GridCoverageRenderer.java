/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.lite.gridcoverage2d;

// J2SE dependencies
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImagingOpException;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.media.jai.BorderExtender;
import javax.media.jai.ImageLayout;
import javax.media.jai.Interpolation;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;

import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.TypeMap;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.ViewType;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.ImageWorker;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.builder.GridToEnvelopeMapper;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.geotools.styling.RasterSymbolizer;
import org.jaitools.imageutils.ImageLayout2;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.metadata.spatial.PixelOrientation;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Envelope;

/**
 * A helper class for rendering  {@link GridCoverage}  objects. 
 * @author  Simone Giannecchini, GeoSolutions SAS
 * @author  Andrea Aime, GeoSolutions SAS
 * @author  Alessio Fabiani, GeoSolutions SAS
 *
 *
 * @source $URL$
 * @version  $Id$
 *
 */
@SuppressWarnings("deprecation")
public final class GridCoverageRenderer { 
  
    /** Logger. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(GridCoverageRenderer.class);
    
    /** IDENTITY */
    private static final AffineTransform IDENTITY = AffineTransform2D.getTranslateInstance(0, 0);

    /**
     * This variable is use for testing purposes in order to force this
     * {@link GridCoverageRenderer} to dump images at various steps on the disk.
     */
    private static boolean DEBUG = Boolean
            .getBoolean("org.geotools.renderer.lite.gridcoverage2d.debug");

    private static String DUMP_DIRECTORY;
    static {
        if (DEBUG) {
            final File tempDir = new File(System.getProperty("user.home"),"gt-renderer");
            if (!tempDir.exists() ) {
                if(!tempDir.mkdir())
                System.out
                        .println("Unable to create debug dir, exiting application!!!");
                DEBUG=false;
                DUMP_DIRECTORY = null;
            } else
               {
                        DUMP_DIRECTORY = tempDir.getAbsolutePath();
                         System.out.println("Rendering debug dir "+DUMP_DIRECTORY);
               }
        }

    }

    /** The Display (User defined) CRS * */
    private final CoordinateReferenceSystem destinationCRS;

    /** Area we want to draw. */
    private final GeneralEnvelope destinationEnvelope;

    /** Size of the area we want to draw in pixels. */
    private final Rectangle destinationSize;

    private final AffineTransform finalGridToWorld;

    private final AffineTransform finalWorldToGrid;

    private Hints hints = new Hints();

    private Interpolation interpolation= new InterpolationNearest();
    
    /** {@link GridCoverageFactory} to be used within this {@link GridCoverageRenderer} instance.*/
    private final GridCoverageFactory gridCoverageFactory;


    /**
     * Creates a new {@link GridCoverageRenderer} object.
     * 
     * @param destinationCRS
     *                the CRS of the {@link GridCoverage2D} to render.
     * @param envelope
     *                delineating the area to be rendered.
     * @param screenSize
     *                at which we want to rendere the source
     *                {@link GridCoverage2D}.
     * @param worldToScreen if not <code>null</code> and if it contains a rotation,
     * this Affine Tranform is used directly to convert from world coordinates
     * to screen coordinates. Otherwise, a standard {@link GridToEnvelopeMapper}
     * is used to calculate the affine transform.
     * 
     * @throws TransformException
     * @throws NoninvertibleTransformException
     */
    public GridCoverageRenderer(
            final CoordinateReferenceSystem destinationCRS,
            final Envelope envelope, 
            Rectangle screenSize, 
            AffineTransform worldToScreen)
            throws TransformException, NoninvertibleTransformException {

        this(destinationCRS, envelope, screenSize, worldToScreen, null);

    }
    


    /**
     * Creates a new {@link GridCoverageRenderer} object.
     * 
     * @param destinationCRS
     *                the CRS of the {@link GridCoverage2D} to render.
     * @param envelope
     *                delineating the area to be rendered.
     * @param screenSize
     *                at which we want to rendere the source
     *                {@link GridCoverage2D}.
     * @param worldToScreen if not <code>null</code> and if it contains a rotation,
     * this Affine Tranform is used directly to convert from world coordinates
     * to screen coordinates. Otherwise, a standard {@link GridToEnvelopeMapper}
     * is used to calculate the affine transform.
     * 
     * @param newHints
     *                {@link RenderingHints} to control this rendering process.
     * @throws TransformException
     * @throws NoninvertibleTransformException
     */
    public GridCoverageRenderer(
            final CoordinateReferenceSystem destinationCRS,
            final Envelope envelope, 
            final Rectangle screenSize,
            final AffineTransform worldToScreen, 
            final RenderingHints newHints) throws TransformException,
            NoninvertibleTransformException {

        // ///////////////////////////////////////////////////////////////////
        //
        // Initialize this renderer
        //
        // ///////////////////////////////////////////////////////////////////
        this.destinationSize = screenSize;
        this.destinationCRS = destinationCRS;
        if (this.destinationCRS == null){
            throw new TransformException(Errors.format(ErrorKeys.CANT_SEPARATE_CRS_$1, this.destinationCRS));
        }
        destinationEnvelope = new GeneralEnvelope(new ReferencedEnvelope(envelope, this.destinationCRS));
        // ///////////////////////////////////////////////////////////////////
        //
        // FINAL DRAWING DIMENSIONS AND RESOLUTION
        // I am here getting the final drawing dimensions (on the device) and
        // the resolution for this rendererbut in the CRS of the source coverage
        // since I am going to compare this info with the same info for the
        // source coverage.
        //
        // ///////////////////////////////////////////////////////////////////
        
        // PHUSTAD : The gridToEnvelopeMapper does not handle rotated views. 
        // 
        if (worldToScreen != null && XAffineTransform.getRotation(worldToScreen) != 0.0) { 
            finalWorldToGrid = new AffineTransform(worldToScreen);
            finalGridToWorld = finalWorldToGrid.createInverse();
        } else {
            final GridToEnvelopeMapper gridToEnvelopeMapper = new GridToEnvelopeMapper();
            gridToEnvelopeMapper.setPixelAnchor(PixelInCell.CELL_CORNER);
            gridToEnvelopeMapper.setGridRange(new GridEnvelope2D(destinationSize));       
            gridToEnvelopeMapper.setEnvelope(destinationEnvelope);            
            finalGridToWorld = new AffineTransform(gridToEnvelopeMapper.createAffineTransform());
            finalWorldToGrid = finalGridToWorld.createInverse();
        }

        //
        // HINTS Management
        //
        if (newHints != null){
            this.hints.add(newHints);
        }
        // factory as needed
        this.gridCoverageFactory = CoverageFactoryFinder.getGridCoverageFactory(this.hints);
        
        // Interpolation
        if(hints.containsKey(JAI.KEY_INTERPOLATION)){
            interpolation = (Interpolation) newHints.get(JAI.KEY_INTERPOLATION);
        }else{
            hints.add(new RenderingHints(JAI.KEY_INTERPOLATION,interpolation));
        }
        if (LOGGER.isLoggable(Level.FINE)){
            LOGGER.fine("Rendering using interpolation "+interpolation);        
        }
        setInterpolationHints();
        
        // Tile Size
        if(hints.containsKey(JAI.KEY_IMAGE_LAYOUT)){
            final ImageLayout layout= (ImageLayout) hints.get(JAI.KEY_IMAGE_LAYOUT);
//            // only tiles are valid at this stage?? TODO
            layout.unsetImageBounds();
            layout.unsetValid(ImageLayout.COLOR_MODEL_MASK&ImageLayout.SAMPLE_MODEL_MASK);
//            if(layout.isValid(ImageLayout.TILE_HEIGHT_MASK)&&layout.isValid(ImageLayout.TILE_WIDTH_MASK)){
//                tileDimension=new Dimension();
//                tileDimension.setSize(layout.getTileWidth(null), layout.getTileHeight(null));
//            }
        }
        // this prevents users from overriding lenient hint
        this.hints.put(Hints.LENIENT_DATUM_SHIFT, Boolean.TRUE);
        this.hints.put(Hints.COVERAGE_PROCESSING_VIEW, ViewType.SAME);
        
        //SG add hints for the border extender
        this.hints.add(new RenderingHints(JAI.KEY_BORDER_EXTENDER,BorderExtender.createInstance(BorderExtender.BORDER_COPY)));
    }



    /**
     * 
     */
    private void setInterpolationHints() {
        if(interpolation instanceof InterpolationNearest){
            this.hints.add(new RenderingHints(JAI.KEY_REPLACE_INDEX_COLOR_MODEL, Boolean.FALSE));
            this.hints.add(new RenderingHints(JAI.KEY_TRANSFORM_ON_COLORMAP, Boolean.TRUE));
        } else {
            this.hints.add(new RenderingHints(JAI.KEY_REPLACE_INDEX_COLOR_MODEL, Boolean.TRUE));
            this.hints.add(new RenderingHints(JAI.KEY_TRANSFORM_ON_COLORMAP, Boolean.FALSE));
        }
    }

    /**
     * Write the provided {@link RenderedImage} in the debug directory with the provided file name.
     * 
     * @param raster
     *            the {@link RenderedImage} that we have to write.
     * @param fileName
     *            a {@link String} indicating where we should write it.
     */
    static void writeRenderedImage(final RenderedImage raster, final String fileName) {
        if (DUMP_DIRECTORY == null)
            throw new NullPointerException(
                    "Unable to write the provided coverage in the debug directory");
        if (DEBUG == false)
            throw new IllegalStateException(
                    "Unable to write the provided coverage since we are not in debug mode");
        try {
            ImageIO.write(raster, "tiff", new File(DUMP_DIRECTORY, fileName + ".tiff"));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }

    /**
     * Turns the coverage into a rendered image applying the necessary transformations and the
     * symbolizer
     * 
     * Builds a (RenderedImage, AffineTransform) pair that can be used for rendering onto a
     * {@link Graphics2D} or as the basis to build a final image. Will return null if there is
     * nothing to render.
     * 
     * @param gridCoverage
     * @param symbolizer
     * @return The transformed image, or null if the coverage does not lie within the rendering
     *         bounds
     * @throws Exception
     */
    public RenderedImage renderImage(
            final GridCoverage2D gridCoverage,
            final RasterSymbolizer symbolizer,
            final double[] bkgValues )throws Exception {

        // Initial checks
        GridCoverageRendererUtilities.ensureNotNull(gridCoverage, "gridCoverage");    	
        if (LOGGER.isLoggable(Level.FINE)){
            LOGGER.fine("Drawing coverage "+gridCoverage.toString());
        }

        // //
        //
        // REPROJECTION NEEDED?
        //
        // //
        boolean doReprojection =false; 
        final CoordinateReferenceSystem coverageCRS = gridCoverage.getCoordinateReferenceSystem2D();
        if(!CRS.equalsIgnoreMetadata(coverageCRS, destinationCRS)){
            final MathTransform transform = CRS.findMathTransform(coverageCRS, destinationCRS, true);
            doReprojection = !transform.isIdentity();
            if (LOGGER.isLoggable(Level.FINE)){
                LOGGER.fine("Reproject needed for rendering provided coverage");
            }    
        }

        // /////////////////////////////////////////////////////////////////////
        //
        // CROP
        //
        // /////////////////////////////////////////////////////////////////////
        final GridCoverage2D preReprojection=crop(gridCoverage, destinationEnvelope,doReprojection);
        if (preReprojection == null) {
            // nothing to render, the AOI does not overlap
            if (LOGGER.isLoggable(Level.FINE)){
                LOGGER.fine("Skipping current coverage because crop failed");
            }
            return null;
        }
        
        // /////////////////////////////////////////////////////////////////////
        //
        // REPROJECTION if needed
        //
        // /////////////////////////////////////////////////////////////////////
        final GridCoverage2D afterReprojection=reproject(preReprojection, doReprojection,bkgValues);
        
        // ///////////////////////////////////////////////////////////////////
        //
        // FINAL AFFINE
        //
        // ///////////////////////////////////////////////////////////////////
        final GridCoverage2D preSymbolizer= affine(afterReprojection,bkgValues);
        
       
//        final ParameterValueGroup param = (ParameterValueGroup) GridCoverageRendererUtilities.RESAMPLING_PARAMS.clone();
//        param.parameter("source").setValue(gridCoverage);
//        param.parameter("GridGeometry").setValue(new GridGeometry2D(new GridEnvelope2D(destinationSize), destinationEnvelope));
//        param.parameter("InterpolationType").setValue(interpolation);
//        final GridCoverage2D preSymbolizer =(GridCoverage2D) GridCoverageRendererUtilities.RESAMPLE_FACTORY.doOperation(param, hints);
//  
        // ///////////////////////////////////////////////////////////////////
        //
        // RASTERSYMBOLIZER
        //
        // ///////////////////////////////////////////////////////////////////
        final GridCoverage2D symbolizerGC;
        if(symbolizer!=null){
                if (LOGGER.isLoggable(Level.FINE)){
                    LOGGER.fine("Applying Raster Symbolizer ");
                }            
        	final RasterSymbolizerHelper rsp = new RasterSymbolizerHelper (preSymbolizer,this.hints);
        	rsp.visit(symbolizer);
        	symbolizerGC = (GridCoverage2D) rsp.getOutput();
    	} else {
            symbolizerGC = preSymbolizer;
        }
        if (DEBUG) {
            writeRenderedImage( symbolizerGC.geophysics(false).getRenderedImage(),"postSymbolizer");
        }

        
        // //
        //
        // FINAL Result
        // 
        // //
        return symbolizerGC.getRenderedImage();
    }            	

    /**
     * @param preResample
     * @param doReprojection 
     * @param bkgValues 
     * @return
     * @throws FactoryException 
     */
    private GridCoverage2D reproject(GridCoverage2D preResample, boolean doReprojection, double[] bkgValues) throws FactoryException {
        GridCoverage2D afterReprojection=null;
        try{
            if (doReprojection) {
                afterReprojection = GridCoverageRendererUtilities.reproject(
                        preResample, 
                        destinationCRS,
                        interpolation, 
                        destinationEnvelope,
                        bkgValues,
                        hints);
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.fine("Reprojecting to crs "+ destinationCRS.toWKT());
            } else{
                afterReprojection = preResample;
            }
            return afterReprojection;
        }finally{
            if (DEBUG) {
                if(afterReprojection!=null){
                    writeRenderedImage(
                            afterReprojection.geophysics(false).getRenderedImage(),
                            "afterReprojection");
                }
            }
        }
    }



    /**
     * @param destinationEnvelope 
     * @param gridCoverage
     * @return
     */
    private GridCoverage2D crop(
            final GridCoverage2D inputCoverage, 
            final GeneralEnvelope destinationEnvelope,
            final boolean doReprojection) {
        
        // //
        //
        // CREATING THE CROP ENVELOPE
        //
        // //  
        GridCoverage2D outputCoverage=inputCoverage;
        final GeneralEnvelope coverageEnvelope = (GeneralEnvelope) inputCoverage.getEnvelope();       
        final CoordinateReferenceSystem coverageCRS = inputCoverage.getCoordinateReferenceSystem2D();

        try{
            GeneralEnvelope renderingEnvelopeInCoverageCRS=null; 
            if(doReprojection){
                renderingEnvelopeInCoverageCRS = GridCoverageRendererUtilities
                        .reprojectEnvelopeWithWGS84Pivot(destinationEnvelope, coverageCRS);
            }else{
                // NO REPROJECTION
                renderingEnvelopeInCoverageCRS = new GeneralEnvelope(destinationEnvelope);
            }
            final GeneralEnvelope cropEnvelope = new GeneralEnvelope(renderingEnvelopeInCoverageCRS);
            cropEnvelope.intersect(coverageEnvelope);
            if (cropEnvelope.isEmpty()||cropEnvelope.isNull()) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info("The destination envelope does not intersect the envelope of the source coverage.");
                }
                return null;
            }   
            
            ////
            //
            // Cropping for real
            //
            /////
            outputCoverage = GridCoverageRendererUtilities.crop(inputCoverage, cropEnvelope, hints);
        }catch (Throwable t) {
                ////
                //
                // If it happens that the crop fails we try to proceed since the crop does only an optimization. Things might
                // work out anyway.
                //
                ////{
            if (LOGGER.isLoggable(Level.FINE)){
                LOGGER.log(Level.FINE,"Crop Failed for reason: "+t.getLocalizedMessage(),t);
            }
            outputCoverage=inputCoverage;
        }

        if (DEBUG) {
            writeRenderedImage(
                    outputCoverage.geophysics(false).getRenderedImage(),
                    "crop");
        }
        return outputCoverage;
        
    }



    /**
     * @param bkgValues 
     * @param preResample
     * @return
     */
    private GridCoverage2D affine(GridCoverage2D input, double[] bkgValues) {
     // NOTICE that at this stage the image we get should be 8 bits, either RGB, RGBA, Gray, GrayA
        // either multiband or indexed. It could also be 16 bits indexed!!!!
        final RenderedImage finalImage = input.getRenderedImage();
        final GridGeometry2D preSymbolizerGridGeometry = (input.getGridGeometry());
        // I need to translate half of a pixel since in wms the envelope
        // map to the corners of the raster space not to the center of the
        // pixels.
        final MathTransform2D finalGCTransform=preSymbolizerGridGeometry.getGridToCRS2D(PixelOrientation.UPPER_LEFT);
        if (!(finalGCTransform instanceof AffineTransform)) {
            throw new UnsupportedOperationException(
                    "Non-affine transformations not yet implemented"); // TODO
        }
        final AffineTransform finalGCgridToWorld = new AffineTransform((AffineTransform) finalGCTransform);


        // //
        //
        // I am going to concatenate the final world to grid transform for the
        // screen area with the grid to world transform of the input coverage.
        //
        // This way i right away position the coverage at the right place in the
        // area of interest for the device.
        //
        // //
        final AffineTransform finalRasterTransformation = (AffineTransform) finalWorldToGrid.clone();
        finalRasterTransformation.concatenate(finalGCgridToWorld);
       
        //paranoiac check to avoid that JAI freaks out when computing its internal layouT on images that are too small
        Rectangle2D finalLayout= GridCoverageRendererUtilities.layoutHelper(
                        finalImage, 
                        (float)finalRasterTransformation.getScaleX(), 
                        (float)finalRasterTransformation.getScaleY(), 
                        (float)finalRasterTransformation.getTranslateX(), 
                        (float)finalRasterTransformation.getTranslateY(), 
                        interpolation);
        if(finalLayout.isEmpty()){
                if(LOGGER.isLoggable(java.util.logging.Level.FINE))
                        LOGGER.fine("Unable to create a granuleDescriptor "+this.toString()+ " due to jai scale bug");
                return null;
        }
//
//        // final transformation
//        final ImageLayout2 layout = new ImageLayout2(finalImage);
//        if(tileDimension!=null){
//            layout.unsetImageBounds();
//            layout.unsetValid(ImageLayout.COLOR_MODEL_MASK&ImageLayout.SAMPLE_MODEL_MASK);
//            layout.setTileGridXOffset(0).setTileGridYOffset(0).setTileHeight(tileDimension.height).setTileWidth(tileDimension.width);            
//        }
//        final RenderingHints localHints = this.hints.clone(); 
//        localHints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout));
//        
//        // === interpolation management
//        // others, make sure we don't force the colormodel if it is indexed with layout as it might lead to NO color expansion        
//        if (!(interpolation instanceof InterpolationNearest)&& finalImage.getColorModel() instanceof IndexColorModel){
//            layout.unsetValid(ImageLayout2.COLOR_MODEL_MASK).unsetValid(ImageLayout2.SAMPLE_MODEL_MASK);
//        }

        RenderedImage im=null;
        try {
            ImageWorker iw = new ImageWorker(finalImage);
            iw.setRenderingHints(hints);
            iw.affine(finalRasterTransformation, interpolation, bkgValues);
            im = iw.getRenderedImage();
        } finally {
                if(DEBUG){
                    writeRenderedImage(im, "postAffine");
                }
        }        
        // recreate gridCoverage
        int numBands = im.getSampleModel().getNumBands();
        GridSampleDimension[] sd = new GridSampleDimension[numBands];
        for(int i=0;i<numBands;i++) {
            sd[i]= new GridSampleDimension(TypeMap.getColorInterpretation(im.getColorModel(), i).name());
        }
        
        // create a new grid coverage but preserve as much input as possible
       return this.gridCoverageFactory.create(
               input.getName(), 
                im,
                new GridGeometry2D(
                        new GridEnvelope2D(PlanarImage.wrapRenderedImage(im).getBounds()), 
                        input.getEnvelope()), 
                sd, 
                new GridCoverage[] { input },
                input.getProperties());    
    }



    /**
     * Turns the coverage into a rendered image applying the necessary transformations and the
     * symbolizer
     * 
     * @param gridCoverage
     * @param symbolizer
     * @return The transformed image, or null if the coverage does not lie within the rendering
     *         bounds
     * @throws FactoryException
     * @throws TransformException
     * @throws NoninvertibleTransformException
     * @Deprecated
     */
    public RenderedImage renderImage(
            final GridCoverage2D gridCoverage,
            final RasterSymbolizer symbolizer, 
            final Interpolation interpolation, 
            final Color background,
            final int tileSizeX, 
            final int tileSizeY
            ) throws FactoryException, TransformException, NoninvertibleTransformException {

        final Hints oldHints= this.hints.clone();
        
        ////
        //
        // TILING
        //
        //// 
        if(tileSizeX>0&&tileSizeY>0){
            // Tile Size
            final ImageLayout layout= new ImageLayout2();
            layout.setTileGridXOffset(0).setTileGridYOffset(0).setTileHeight(tileSizeY).setTileWidth(tileSizeX);
            hints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT,layout));            
        }

        
        ////
        //
        // INTERPOLATION
        //
        ////
        if(interpolation!=null){
            if (LOGGER.isLoggable(Level.FINE)){
                LOGGER.fine("Rendering using interpolation "+interpolation);        
            }
            this.interpolation=interpolation;
            hints.add(new RenderingHints(JAI.KEY_INTERPOLATION,this.interpolation));
            if (LOGGER.isLoggable(Level.FINE)){
                LOGGER.fine("Rendering using interpolation "+interpolation);        
            }
            setInterpolationHints();
        }
 
        try{
            return renderImage(gridCoverage, symbolizer,GridCoverageRendererUtilities.colorToArray(background));
        } catch (Exception e) {
            throw new FactoryException(e);
        }finally{
            this.hints=oldHints;
        }
     
        
//        // Build the final image and the associated world to grid transformation
//        final GCpair couple = prepareFinalImage(gridCoverage, symbolizer);
//        if (couple == null){
//            return null;
//        }
//        // NOTICE that at this stage the image we get should be 8 bits, either RGB, RGBA, Gray, GrayA
//        // either multiband or indexed. It could also be 16 bits indexed!!!!
//        
//        final RenderedImage finalImage = couple.getGridCoverage().getRenderedImage();
//        final AffineTransform finalRaster2Model = couple.getTransform();
//        
//        //paranoiac check to avoid that JAI freaks out when computing its internal layouT on images that are too small
//        Rectangle2D finalLayout= GridCoverageRendererUtilities.layoutHelper(
//                        finalImage, 
//                        (float)finalRaster2Model.getScaleX(), 
//                        (float)finalRaster2Model.getScaleY(), 
//                        (float)finalRaster2Model.getTranslateX(), 
//                        (float)finalRaster2Model.getTranslateY(), 
//                        interpolation);
//        if(finalLayout.isEmpty()){
//                if(LOGGER.isLoggable(java.util.logging.Level.FINE))
//                        LOGGER.fine("Unable to create a granuleDescriptor "+this.toString()+ " due to jai scale bug");
//                return null;
//        }
//
//        // final transformation
//        final ImageLayout2 layout = new ImageLayout2(finalImage);
//        layout.setTileGridXOffset(0).setTileGridYOffset(0).setTileHeight(tileSizeY).setTileWidth(tileSizeX);
//        final RenderingHints localHints = this.hints.clone(); 
//        localHints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout));
//        
//        // === interpolation management
//        if (interpolation instanceof InterpolationNearest) {
//            // nearest 
//            localHints.add(new RenderingHints(JAI.KEY_REPLACE_INDEX_COLOR_MODEL, Boolean.FALSE));
//            localHints.add(new RenderingHints(JAI.KEY_TRANSFORM_ON_COLORMAP, Boolean.TRUE));
//        } else {
//            // others, make sure we don't force the colormodel if it is indexed with layout as it might lead to NO color expansion 
//            if(finalImage.getColorModel() instanceof IndexColorModel){
//                layout.unsetValid(ImageLayout2.COLOR_MODEL_MASK).unsetValid(ImageLayout2.SAMPLE_MODEL_MASK);
//            }
//        }
//        //SG add hints for the border extender
//        localHints.add(new RenderingHints(JAI.KEY_BORDER_EXTENDER,BorderExtender.createInstance(BorderExtender.BORDER_COPY)));
//    	RenderedImage im=null;
//    	try {
//    	    ImageWorker iw = new ImageWorker(finalImage);
//    	    iw.setRenderingHints(localHints);
//    	    iw.affine(finalRaster2Model, interpolation, null);
//    	    im = iw.getRenderedImage();
//    	} finally {
//    		if(DEBUG){
//    		    writeRenderedImage(im, "postAffine");
//    		}
//    	}
//    	
//    	return im;

    }
    
    /**
     * Paint this grid coverage. The caller must ensure that
     * <code>graphics</code> has an affine transform mapping "real world"
     * coordinates in the coordinate system given by {@link
     * #getCoordinateSystem}.
     * 
     * @param graphics
     *                the {@link Graphics2D} context in which to paint.
     * @param metaBufferedEnvelope
     * @throws Exception
     * @throws UnsupportedOperationException
     *                 if the transformation from grid to coordinate system in
     *                 the GridCoverage is not an AffineTransform
     */
    public void paint(
            final Graphics2D graphics,
            final GridCoverage2D gridCoverage, 
            final RasterSymbolizer symbolizer)
            throws Exception {
        paint(graphics, gridCoverage, symbolizer,null);
    }
    
    /**
     * Paint this grid coverage. The caller must ensure that
     * <code>graphics</code> has an affine transform mapping "real world"
     * coordinates in the coordinate system given by {@link
     * #getCoordinateSystem}.
     * 
     * @param graphics
     *                the {@link Graphics2D} context in which to paint.
     * @param metaBufferedEnvelope
     * @throws Exception
     * @throws UnsupportedOperationException
     *                 if the transformation from grid to coordinate system in
     *                 the GridCoverage is not an AffineTransform
     */
    public void paint(
            final Graphics2D graphics,
            final GridCoverage2D gridCoverage, 
            final RasterSymbolizer symbolizer,
            final double[] bkgValues)
            throws Exception {

        //
        // Initial checks
        //
        if(graphics==null){
            throw new NullPointerException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1,"graphics"));
        }
        if(gridCoverage==null){
            throw new NullPointerException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1,"gridCoverage"));
        }
        
        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.fine(new StringBuilder("Drawing coverage ").append(gridCoverage.toString()).toString());

        final RenderingHints oldHints = graphics.getRenderingHints();
        graphics.setRenderingHints(this.hints);
        
        
        // Build the final image and the transformation
        RenderedImage finalImage = renderImage(gridCoverage, symbolizer,bkgValues);

        try {
            //debug
            if (DEBUG) {
                writeRenderedImage(finalImage,"final");
            }        
            
            // force solid alpha, the transparency has already been
            // dealt with in the image preparation, and we have to make
            // sure previous vector rendering code did not leave a non solid alpha
            graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));

            // //
            // Drawing the Image
            // //
            graphics.drawRenderedImage(finalImage, GridCoverageRenderer.IDENTITY);
            
        } catch (Throwable t) {
            try {
                //log the error
                if(LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE,t.getLocalizedMessage(),t);              
                
                // /////////////////////////////////////////////////////////////
                // this is a workaround for a bug in Java2D, we need to convert
                // the image to component color model to make it work just fine.
                // /////////////////////////////////////////////////////////////
                if(t instanceof IllegalArgumentException){
                    if (DEBUG) {
                        writeRenderedImage(finalImage,"preWORKAROUND1");
                    }
                    final RenderedImage image = new ImageWorker(finalImage).forceComponentColorModel(true).getRenderedImage();
                    
                    if (DEBUG) {
                        writeRenderedImage(image,"WORKAROUND1");
                        
                    }
                    graphics.drawRenderedImage(image, GridCoverageRenderer.IDENTITY);
                
                }
                else if(t instanceof ImagingOpException)
                // /////////////////////////////////////////////////////////////
                // this is a workaround for a bug in Java2D
                // (see bug 4723021
                // http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4723021).
                //
                // AffineTransformOp.filter throws a
                // java.awt.image.ImagingOpException: Unable to tranform src
                // image when a PixelInterleavedSampleModel is used.
                //
                // CUSTOMER WORKAROUND :
                // draw the BufferedImage into a buffered image of type ARGB
                // then perform the affine transform. THIS OPERATION WASTES
                // RESOURCES BY PERFORMING AN ALLOCATION OF MEMORY AND A COPY ON
                // LARGE IMAGES.
                // /////////////////////////////////////////////////////////////
                {
                    BufferedImage buf = 
                        finalImage.getColorModel().hasAlpha()?
                                new BufferedImage(finalImage.getWidth(), finalImage.getHeight(), BufferedImage.TYPE_4BYTE_ABGR):
                                new BufferedImage(finalImage.getWidth(), finalImage.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
                                if (DEBUG) {
                                    writeRenderedImage(buf,"preWORKAROUND2");
                                }
                    final Graphics2D g = (Graphics2D) buf.getGraphics();
                    final int translationX=finalImage.getMinX(),translationY=finalImage.getMinY();
                    g.drawRenderedImage(finalImage, AffineTransform.getTranslateInstance(-translationX, -translationY));
                    g.dispose();
                    if (DEBUG) {
                         writeRenderedImage(buf,"WORKAROUND2");
                    }
                    GridCoverageRenderer.IDENTITY.concatenate(AffineTransform.getTranslateInstance(translationX, translationY));
                    graphics.drawImage(buf, GridCoverageRenderer.IDENTITY, null);
                    //release
                    buf.flush();
                    buf=null;                   
                }
                else 
                    //log the error
                    if(LOGGER.isLoggable(Level.FINE))
                        LOGGER.fine("Unable to renderer this raster, no workaround found");


            } catch (Throwable t1) {
                // if the workaround fails again, there is really nothing to do
                // :-(
                LOGGER.log(Level.WARNING, t1.getLocalizedMessage(), t1);
            }
        }

        // ///////////////////////////////////////////////////////////////////
        //
        // Restore old hints
        //
        // ///////////////////////////////////////////////////////////////////
        graphics.setRenderingHints(oldHints);

    }

}
