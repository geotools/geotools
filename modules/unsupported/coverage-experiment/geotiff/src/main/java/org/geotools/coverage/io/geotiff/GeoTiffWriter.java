package org.geotools.coverage.io.geotiff;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.List;

import javax.imageio.IIOException;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageOutputStream;

import org.geotools.coverage.Category;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverageWriter;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.imageio.GeoToolsWriteParams;
import org.geotools.coverage.grid.io.imageio.geotiff.CRS2GeoTiffMetadataAdapter;
import org.geotools.coverage.grid.io.imageio.geotiff.GeoTiffConstants;
import org.geotools.coverage.grid.io.imageio.geotiff.GeoTiffException;
import org.geotools.coverage.grid.io.imageio.geotiff.GeoTiffIIOMetadataEncoder;
import org.geotools.factory.Hints;
import org.geotools.parameter.Parameter;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.resources.i18n.VocabularyKeys;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Parent;
import org.jdom.input.DOMBuilder;
import org.jdom.output.DOMOutputter;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.crs.ProjectedCRS;
import org.opengis.referencing.operation.TransformException;

import com.sun.media.imageioimpl.plugins.tiff.TIFFImageMetadata;
import com.sun.media.imageioimpl.plugins.tiff.TIFFImageWriterSpi;

/**
 * @author Simone Giannecchini
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/plugin/geotiff/src/org/geotools/gce/geotiff/GeoTiffWriter.java $
 */
final class GeoTiffWriter extends AbstractGridCoverageWriter {

    /** factory for getting tiff writers. */
    private final static TIFFImageWriterSpi tiffWriterFactory = new TIFFImageWriterSpi();

    /**
     * Constructor for a {@link GeoTiffWriter}.
     * 
     * @param destination
     * @throws IOException
     */
    public GeoTiffWriter(Object destination) throws IOException {
        this(destination, null);

    }

    /**
     * Constructor for a {@link GeoTiffWriter}.
     * 
     * @param destination
     * @param hints
     * @throws IOException
     */
    public GeoTiffWriter(Object destination, Hints hints) throws IOException {

        this.destination = destination;
        if (destination instanceof File)
            this.outStream = ImageIO.createImageOutputStream(destination);
        else if (destination instanceof URL) {
            final URL dest = (URL) destination;
            if (dest.getProtocol().equalsIgnoreCase("file")) {
                final File destFile = new File(URLDecoder.decode(
                        dest.getFile(), "UTF-8"));
                this.outStream = ImageIO.createImageOutputStream(destFile);
            }

        } else if (destination instanceof OutputStream) {

            this.outStream = ImageIO
                    .createImageOutputStream((OutputStream) destination);

        } else if (destination instanceof ImageOutputStream)
            this.outStream = (ImageOutputStream) destination;
        else
            throw new IllegalArgumentException(
                    "The provided destination canno be used!");
        // //
        //
        // managing hints
        //
        // //
        if (this.hints == null)
            this.hints = new Hints();
        if (hints != null) {
            // prevent the use from reordering axes
            hints.remove(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER);
            this.hints.add(hints);
            this.hints.add(new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER,
                    Boolean.TRUE));

        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.opengis.coverage.grid.GridCoverageWriter#write(org.opengis.coverage.grid.GridCoverage,
     *      org.opengis.parameter.GeneralParameterValue[])
     */
    public void write(final GridCoverage gc,
            final GeneralParameterValue[] params)
            throws IllegalArgumentException, IOException,
            IndexOutOfBoundsException {

        GeoToolsWriteParams gtParams = null;
        if (params != null) {
            // /////////////////////////////////////////////////////////////////////
            //
            // Checking params
            //
            // /////////////////////////////////////////////////////////////////////
            if (params != null) {
                Parameter param;
                final int length = params.length;
                for (int i = 0; i < length; i++) {
                    param = (Parameter) params[i];
                    if (param.getDescriptor().getName().getCode().equals(
                            AbstractGridFormat.GEOTOOLS_WRITE_PARAMS.getName()
                                    .toString())) {
                        gtParams = (GeoToolsWriteParams) param.getValue();
                    }
                }
            }
        }
        if (gtParams == null)
            gtParams = new GeoTiffWriteParams();

        // /////////////////////////////////////////////////////////////////////
        //
        // getting the coordinate reference system
        //
        // /////////////////////////////////////////////////////////////////////
        final GridGeometry2D gg = (GridGeometry2D) gc.getGridGeometry();
        GridEnvelope2D range = gg.getGridRange2D();
        final Rectangle sourceRegion = gtParams.getSourceRegion();
        if (sourceRegion != null)
            range = new GridEnvelope2D(sourceRegion);
        final AffineTransform tr = (AffineTransform) gg.getGridToCRS2D();
        final CoordinateReferenceSystem crs = gg
                .getCoordinateReferenceSystem2D();

        // //
        // no data management
        // //
        double inNoData = getCandidateNoData(gc);

        // /////////////////////////////////////////////////////////////////////
        //
        // we handle just projected and geographic crs
        //
        // /////////////////////////////////////////////////////////////////////
        if (crs instanceof ProjectedCRS || crs instanceof GeographicCRS) {

            // creating geotiff metadata
            final CRS2GeoTiffMetadataAdapter adapter = (CRS2GeoTiffMetadataAdapter) CRS2GeoTiffMetadataAdapter
                    .get(crs);
            final GeoTiffIIOMetadataEncoder metadata = adapter
                    .parseCoordinateReferenceSystem();

            if (!Double.isNaN(inNoData)) {
                metadata.setNoData(inNoData);
            }

            // setting georeferencing
            setGeoReference(crs, metadata, tr, range);

            // writing ALWAYS the geophysics vew of the data
            writeImage(((GridCoverage2D) gc).geophysics(true)
                    .getRenderedImage(), this.outStream, metadata, gtParams);

        } else
            throw new GeoTiffException(
                    null,
                    "The supplied grid coverage uses an unsupported crs! You are allowed to use only projected and geographic coordinate reference systems",
                    null);
    }

    /**
     * This method is used to set the tie point and the scale parameters for the
     * GeoTiff file we are writing or the ModelTransformation in case a more
     * general {@link AffineTransform} is needed to represent the raster space
     * to model space transform.
     * 
     * <p>
     * This method works regardles of the nature fo the crs without making any
     * assumptions on the order or the direction of the axes, but checking them
     * from the supplied CRS.
     * 
     * @see {@link http://lists.maptools.org/pipermail/geotiff/2006-January/000213.html}
     * @see {@http://lists.maptools.org/pipermail/geotiff/2006-January/000212.html}
     * @param crs
     *                The {@link CoordinateReferenceSystem} of the
     *                {@link GridCoverage2D} to encode.
     * @param metadata
     *                where to set the georeferencing information.
     * @param range
     *                that describes the raster space for this geotiff.
     * @param rasterToModel
     *                describes the {@link AffineTransform} between raster space
     *                and model space.
     * 
     * @throws IndexOutOfBoundsException
     * @throws IOException
     * @throws TransformException
     */
    private void setGeoReference(final CoordinateReferenceSystem crs,
            final GeoTiffIIOMetadataEncoder metadata,
            final AffineTransform rasterToModel, GridEnvelope2D range)
            throws IndexOutOfBoundsException, IOException {

        // /////////////////////////////////////////////////////////////////////
        //
        // We have to set an affine transformation which is going to be 2D
        // since we support baseline GeoTiff.
        //
        // /////////////////////////////////////////////////////////////////////
        final AffineTransform modifiedRasterToModel;
        int minx = range.x, miny = range.y;
        if (minx != 0 || miny != 0) {
            // //
            //
            // Preconcatenate a transform to have raster space beginning at
            // (0,0)
            //
            // //
            modifiedRasterToModel = new AffineTransform(rasterToModel);
            modifiedRasterToModel.concatenate(AffineTransform
                    .getTranslateInstance(minx, miny));
        } else
            modifiedRasterToModel = rasterToModel;

        // /////////////////////////////////////////////////////////////////////
        //
        // Setting raster type to pixel centre since the ogc specifications
        // require so.
        //
        // /////////////////////////////////////////////////////////////////////
        metadata.addGeoShortParam(GeoTiffConstants.GTRasterTypeGeoKey,
                GeoTiffConstants.RasterPixelIsPoint);

        // /////////////////////////////////////////////////////////////////////
        //
        // AXES DIRECTION
        //
        // we need to understand how the axes of this gridcoverage are
        // specified, trying to understand the direction of the first axis in
        // order to correctly use transformations.
        //
        // Note that here wew assume that in case of a Flip the flip is on the Y
        // axis.
        //
        // /////////////////////////////////////////////////////////////////////
        boolean lonFirst = XAffineTransform.getSwapXY(modifiedRasterToModel) != -1;

        // /////////////////////////////////////////////////////////////////////
        //
        // ROTATION
        //
        // If fthere is not rotation or shearing or flipping we have a simple
        // scale and translate hence we can simply set the tie points.
        //
        // /////////////////////////////////////////////////////////////////////
        double rotation = XAffineTransform.getRotation(modifiedRasterToModel);

        // /////////////////////////////////////////////////////////////////////
        //
        // Deciding how to save the georef with respect to the CRS.
        //
        // /////////////////////////////////////////////////////////////////////
        // tie points
        if (!(Double.isInfinite(rotation) || Double.isNaN(rotation) || Math
                .abs(rotation) > 1E-6)) {
            final double tiePointLongitude = (lonFirst) ? modifiedRasterToModel
                    .getTranslateX() : modifiedRasterToModel.getTranslateY();
            final double tiePointLatitude = (lonFirst) ? modifiedRasterToModel
                    .getTranslateY() : modifiedRasterToModel.getTranslateX();
            metadata.setModelTiePoint(0, 0, 0, tiePointLongitude,
                    tiePointLatitude, 0);
            // scale
            final double scaleModelToRasterLongitude = (lonFirst) ? Math
                    .abs(modifiedRasterToModel.getScaleX()) : Math
                    .abs(modifiedRasterToModel.getShearY());
            final double scaleModelToRasterLatitude = (lonFirst) ? Math
                    .abs(modifiedRasterToModel.getScaleY()) : Math
                    .abs(modifiedRasterToModel.getShearX());
            metadata.setModelPixelScale(scaleModelToRasterLongitude,
                    scaleModelToRasterLatitude, 0);
            // Alternative code, not yet enabled in order to avoid breaking
            // code.
            // The following code is insensitive to axis order and rotations in
            // the 'coord' space (not in the 'grid' space, otherwise we would
            // not take the inverse of the matrix).
            /*
             * final AffineTransform coordToGrid = gridToCoord.createInverse();
             * final double scaleModelToRasterLongitude = 1 /
             * XAffineTransform.getScaleX0(coordToGrid); final double
             * scaleModelToRasterLatitude = 1 /
             * XAffineTransform.getScaleY0(coordToGrid);
             */
        } else {

            metadata.setModelTransformation(modifiedRasterToModel);

        }
    }

    /**
     * Writes the provided rendered image to the provided image output stream
     * using the supplied geotiff metadata.
     * 
     * @param gtParams
     */
    private boolean writeImage(final RenderedImage image,
            final ImageOutputStream outputStream,
            final GeoTiffIIOMetadataEncoder geoTIFFMetadata,
            GeoToolsWriteParams gtParams) throws IOException {
        if (image == null || outputStream == null) {
            throw new IllegalArgumentException("some parameters are null");
        }
        final ImageWriteParam params = gtParams.getAdaptee();
        // /////////////////////////////////////////////////////////////////////
        //
        // GETTING READER AND METADATA
        //
        // /////////////////////////////////////////////////////////////////////
        final ImageWriter writer = tiffWriterFactory.createWriterInstance();
        final IIOMetadata metadata = createGeoTiffIIOMetadata(writer,
                ImageTypeSpecifier.createFromRenderedImage(image),
                geoTIFFMetadata, params);

        // /////////////////////////////////////////////////////////////////////
        //
        // IMAGEWRITE
        //
        // /////////////////////////////////////////////////////////////////////
        writer.setOutput(outputStream);
        writer.write(writer.getDefaultStreamMetadata(params), new IIOImage(
                image, null, metadata), params);

        // /////////////////////////////////////////////////////////////////////
        //
        // release resources
        //
        // /////////////////////////////////////////////////////////////////////
        outputStream.flush();
        if (!(destination instanceof ImageOutputStream))
            outputStream.close();
        writer.dispose();

        return true;
    }

    /**
     * Creates image metadata which complies to the GeoTIFFWritingUtilities
     * specification for the given image writer, image type and
     * GeoTIFFWritingUtilities metadata.
     * 
     * @param writer
     *                the image writer, must not be null
     * @param type
     *                the image type, must not be null
     * @param geoTIFFMetadata
     *                the GeoTIFFWritingUtilities metadata, must not be null
     * @param params
     * @return the image metadata, never null
     * @throws IIOException
     *                 if the metadata cannot be created
     */
    public final static IIOMetadata createGeoTiffIIOMetadata(
            ImageWriter writer, ImageTypeSpecifier type,
            GeoTiffIIOMetadataEncoder geoTIFFMetadata, ImageWriteParam params)
            throws IIOException {
        IIOMetadata imageMetadata = writer
                .getDefaultImageMetadata(type, params);
        imageMetadata = writer
                .convertImageMetadata(imageMetadata, type, params);
        org.w3c.dom.Element w3cElement = (org.w3c.dom.Element) imageMetadata
                .getAsTree(GeoTiffConstants.GEOTIFF_IIO_METADATA_FORMAT_NAME);
        final Element element = new DOMBuilder().build(w3cElement);

        geoTIFFMetadata.assignTo(element);

        final Parent parent = element.getParent();
        parent.removeContent(element);

        final Document document = new Document(element);

        try {
            final org.w3c.dom.Document w3cDoc = new DOMOutputter()
                    .output(document);
            final IIOMetadata iioMetadata = new TIFFImageMetadata(
                    TIFFImageMetadata.parseIFD(w3cDoc.getDocumentElement()
                            .getFirstChild()));
            imageMetadata = iioMetadata;
        } catch (JDOMException e) {
            throw new IIOException(
                    "Failed to set GeoTiffIIOMetadataEncoder specific tags.", e);
        } catch (IIOInvalidTreeException e) {
            throw new IIOException(
                    "Failed to set GeoTiffIIOMetadataEncoder specific tags.", e);
        }

        return imageMetadata;
    }

    public Format getFormat() {
        return null;
    }

    static double getCandidateNoData(GridCoverage gc) {
        // no data management
        final GridSampleDimension sd = (GridSampleDimension) gc
                .getSampleDimension(0);
        final List<Category> categories = sd.getCategories();
        double inNoData = Double.NaN;
        if (categories != null) {
            final Iterator<Category> it = categories.iterator();
            Category candidate;
            final String noDataName = Vocabulary.format(VocabularyKeys.NODATA);
            while (it.hasNext()) {
                candidate = (Category) it.next();
                final String name = candidate.getName().toString();
                if (name.equalsIgnoreCase("No Data")
                        || name.equalsIgnoreCase(noDataName)) {
                    inNoData = candidate.getRange().getMaximum();
                }
            }
        }
        return inNoData;
    }

}
