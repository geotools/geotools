/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2010, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageReadParam;
import javax.media.jai.BorderExtenderConstant;
import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.media.jai.iterator.RectIter;
import javax.media.jai.iterator.RectIterFactory;

import org.geotools.coverage.Category;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.gce.grassraster.core.GrassBinaryRasterReadHandler;
import org.geotools.gce.grassraster.core.color.JGrassColorTable;
import org.geotools.gce.grassraster.format.GrassCoverageFormat;
import org.geotools.gce.grassraster.metadata.GrassBinaryImageMetadata;
import org.geotools.gce.grassraster.spi.GrassBinaryImageReaderSpi;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.builder.GridToEnvelopeMapper;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.util.ProgressListener;

/**
 * Coverage Reader class for reading GRASS raster maps.
 * <p>
 * The class reads a GRASS raster map from a GRASS workspace (see package
 * documentation for further info). The reading is really done via Imageio
 * extended classes.
 * </p>
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 * @since 3.0
 * @see GrassBinaryImageReader
 * @see GrassBinaryRasterReadHandler
 */
public class GrassCoverageReader extends AbstractGridCoverage2DReader implements GridCoverageReader {
    private GrassBinaryImageReader imageReader = null;

    private String name;

    private PixelInCell cellAnchor = PixelInCell.CELL_CENTER;

    private Interpolation interpolation = Interpolation.getInstance(Interpolation.INTERP_NEAREST);

    private boolean useSubSamplingAsRequestedColsRows = false;

    private boolean castDoubleToFloating = false;

    private ProgressListener monitor = new DummyProgressListener();

    private JGrassMapEnvironment jgMapEnvironment;

    private double[] range;

    private String colorRulesString;

    private String categoriesString;

    /**
     * Sets the input source to use to the given {@link File file object}.
     * <p>
     * The input source must be set before any of the query or read methods are
     * used.
     * </p>
     * 
     * @param input
     *            the {@link File} to use for future decoding.
     */
    public GrassCoverageReader( Object input ) {
        if (input instanceof File) {
            File file = (File) input;
            imageReader = new GrassBinaryImageReader(new GrassBinaryImageReaderSpi());
            imageReader.setInput(input);
            jgMapEnvironment = new JGrassMapEnvironment(file);
            name = file.getName();
        } else {
            throw new IllegalArgumentException("Illegal input argument!");
        }
    }

    /**
     * Constructor for the {@link GrassCoverageReader}.
     * 
     * @param cellAnchor
     *            the object defining whether to assume the pixel value to be
     *            read in the grid's cell corner or center.
     * @param interpolation
     *            the type of interpolation to be used in the case some scaling
     *            or padding has to be done.
     * @param useSubSamplingAsColsRows
     *            a flag that gives the possibility to bypass the imageio
     *            subsampling mechanism. With GRASS maps this is often more
     *            performant in some boundary situations. In the case this flag
     *            is set to true, the subsampling values will be handled as the
     *            requested columns and rows.
     * @param castDoubleToFloating
     *            a flag that gives the possibility to force the reading of a
     *            map as a floating point map. This is necessary right now
     *            because of a imageio bug:
     *            https://jai-imageio-core.dev.java.net
     *            /issues/show_bug.cgi?id=180.
     * @param monitor
     *            a {@link IHMProgressMonitor monitor} for logging purposes.
     *            This can be null, in which case a dummy one will be used.
     */
    public void setParams( PixelInCell cellAnchor, Interpolation interpolation,
            boolean useSubSamplingAsColsRows, boolean castDoubleToFloating, ProgressListener monitor ) {
        this.useSubSamplingAsRequestedColsRows = useSubSamplingAsColsRows;
        this.castDoubleToFloating = castDoubleToFloating;
        if (monitor != null)
            this.monitor = monitor;
        if (cellAnchor != null)
            this.cellAnchor = cellAnchor;
        if (interpolation != null)
            this.interpolation = interpolation;
    }

    /**
     * Performs the reading of the coverage.
     * <p>
     * This method read the grass file with a special image I/O class. If the
     * image support the tiling read the data with a special operation which are
     * written for this case. The step are:
     * <li>set the region in the world</li>
     * <li>set the region to read in the JAI coordinate</li>
     * <li>read the data directly with driver or, if isTiling is true, with the
     * operation.</li>
     * <li>verify if the image cover whole the region, if not fill the rows and
     * columns with padding (with the Border operation)</li>
     * <li>scale the image to return an image with the number of columns and
     * rows equal to the requestedRegion</li>
     *<li>set the coverage (with the transformation from the JAI coordinate to
     * the real world coordinate.</li>
     * <p>
     * 
     * @param region
     *            the {@link JGrassRegion region}to read. 
     *            If null, the map is read in its
     *            original boundary and resolution.
     * @return the {@link GridCoverage2D read coverage}.
     * @throws IOException
     */
    public GridCoverage2D readRaster( JGrassRegion region ) throws IOException {
        /*
         * retrieve original map region and crs
         */
        HashMap<String, String> metaDataTable = ((GrassBinaryImageMetadata) imageReader
                .getImageMetadata(0)).toHashMap();
        double fileNorth = Double.parseDouble(metaDataTable.get(GrassBinaryImageMetadata.NORTH));
        double fileSouth = Double.parseDouble(metaDataTable.get(GrassBinaryImageMetadata.SOUTH));
        double fileEast = Double.parseDouble(metaDataTable.get(GrassBinaryImageMetadata.EAST));
        double fileWest = Double.parseDouble(metaDataTable.get(GrassBinaryImageMetadata.WEST));
        int fileRows = Integer.parseInt(metaDataTable.get(GrassBinaryImageMetadata.NROWS));
        int fileCols = Integer.parseInt(metaDataTable.get(GrassBinaryImageMetadata.NCOLS));

        CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
        try {
            crs = CRS.parseWKT(metaDataTable.get(GrassBinaryImageMetadata.CRS));
        } catch (FactoryException e) {
            throw new IOException(e.getLocalizedMessage());
        }
        // where to put the region to read and if use subSampling
        ImageReadParam imageReadParam = new ImageReadParam();

        /*
         * the envelope that was requested, i.e. what has to be given back in
         * terms of bounds and resolution.
         */
        Envelope requestedRegionEnvelope = null;
        /*
         * the read region, i.e. the requested region without the parts east and
         * south of the file region. (since they would produce negative origin)
         */
        Rectangle sourceRegion = null;

        int requestedRows = 0;
        int requestedCols = 0;
        double requestedWest = -1;
        double requestedEast = -1;
        double requestedSouth = -1;
        double requestedNorth = -1;
        double requestedXres = -1;
        double requestedYres = -1;

        int subSamplingX = 1;
        int subSamplingY = 1;
        // These variable are difference,in N-E coordinate, between the
        // requested region and the
        // image region.
        double xDeltaW = 0;
        double xDeltaE = 0;
        double yDeltaN = 0;
        double yDeltaS = 0;
        // this is a difference used to compute the exact region in the world in
        // order to transform
        // the SR.
        double tmpDxW = 0.0;
        double tmpDyS = 0.0;
        double tmpDyN = 0.0;
        double tmpDxE = 0.0;
        int xPaddingSx = 0;
        int yPaddingTop = 0;

        if (region != null) {
            /*
             * the user requested a particular read region. and that is exactly
             * the region we have to give back.
             */
            requestedRows = region.getRows();
            requestedCols = region.getCols();
            requestedWest = region.getWest();
            requestedEast = region.getEast();
            requestedSouth = region.getSouth();
            requestedNorth = region.getNorth();
            requestedXres = region.getWEResolution();
            requestedYres = region.getNSResolution();
            /*
             * define the raster space region
             */
            double scaleX = fileCols / (fileEast - fileWest);
            double scaleY = fileRows / (fileNorth - fileSouth);
            double EPS = 0.0;// 1E-6;

            // awt space seen in the world view (north is ymax)
            int xmin = (int) Math.floor((requestedWest - fileWest) * scaleX + EPS);
            int xmax = (int) Math.ceil((requestedEast - fileWest) * scaleX - EPS);
            int ymin = (int) Math.floor((fileNorth - requestedNorth) * scaleY + EPS);
            int ymax = (int) Math.ceil((fileNorth - requestedSouth) * scaleY - EPS);

            /*
             * clip away region that is west and south of the image bounds. This
             * is important because the imageio source region can't be < 0.
             * Later the clipped parts will be resolved by translation of the
             * image.
             */
            if (xmin < 0) {
                xPaddingSx = xmin;
                xmin = 0;
            }
            if (ymin < 0) {
                yPaddingTop = ymin;
                ymin = 0;
            }

            /*
             * the pixel space region that will be extracted. Since the origin
             * is always 0,0, we can continue to see this in world view.
             */
            sourceRegion = new Rectangle(xmin, ymin, (xmax - xmin), ymax - ymin);
            requestedRegionEnvelope = new Envelope2D(crs, requestedWest, requestedSouth,
                    requestedEast - requestedWest, requestedNorth - requestedSouth);

            /*
             * the real world deltas
             */
            xDeltaW = requestedWest - fileWest;
            yDeltaS = requestedSouth - fileSouth;
            xDeltaE = requestedEast - fileEast;
            yDeltaN = requestedNorth - fileNorth;
            // yDelta = requestedNorth - north;

            /*
             * the real world envelope covering the read map part.
             */
            tmpDxW = xDeltaW > 0.0 ? 0.0 : xDeltaW;
            tmpDyS = yDeltaS > 0.0 ? 0.0 : yDeltaS;
            tmpDyN = yDeltaN < 0.0 ? 0.0 : yDeltaN;
            tmpDxE = xDeltaE < 0.0 ? 0.0 : xDeltaE;
            // set the region to the requestedRegion, this value is passed to
            // the coverage to
            // transform the JAI space into the real space.

            /*
             * define the subsampling values. This done starting from the
             * original's image resolution.
             */
            if (!useSubSamplingAsRequestedColsRows) {
                /*
                 * in this case we respect the original subsampling contract.
                 */
                JGrassRegion tmpRegion = new JGrassRegion(region);
                tmpRegion.setWEResolution((fileEast - fileWest) / (double) fileCols);
                tmpRegion.setNSResolution((fileNorth - fileSouth) / (double) fileRows);
                subSamplingX = (int) Math.floor((double) tmpRegion.getCols()
                        / (double) requestedCols);
                subSamplingY = (int) Math.floor((double) tmpRegion.getRows()
                        / (double) requestedRows);
                if (subSamplingX == 0)
                    subSamplingX = 1;
                if (subSamplingY == 0)
                    subSamplingY = 1;
                if (subSamplingX != subSamplingY) {
                    if (subSamplingX < subSamplingY) {
                        subSamplingY = subSamplingX;
                    } else {
                        subSamplingX = subSamplingY;
                    }
                }
            } else {
                /*
                 * in this case the subsampling values are interpreted as
                 * columns and row numbers to be used to calculate the
                 * resolution from the given boundaries.
                 */

                double sourceCols = (requestedEast - requestedWest)
                        / (1 + (xPaddingSx / (xmax - xmin))) / requestedXres;
                double sourceRows = (requestedNorth - requestedSouth)
                        / (1 + (yPaddingTop / (ymax - ymin))) / requestedYres;
                /*
                 * the padding has to be removed since inside the reader
                 * the padding is ignored and non present in the sourceRegion that
                 * is passed.
                 */
                sourceCols = sourceCols + xPaddingSx;
                sourceRows = sourceRows + yPaddingTop;
                subSamplingX = (int) Math.round(sourceCols);
                subSamplingY = (int) Math.round(sourceRows);

                if (subSamplingX < 1) {
                    subSamplingX = 1;
                }
                if (subSamplingY < 1) {
                    subSamplingY = 1;
                }
            }

        } else {
            /*
             * if no region has been requested, the source and requested region
             * are the same, i.e. the whole raster is read and passed.
             */
            requestedRows = fileRows;
            requestedCols = fileCols;
            requestedWest = fileWest;
            requestedEast = fileEast;
            requestedSouth = fileSouth;
            requestedNorth = fileNorth;
            double scaleX = fileCols / (fileEast - fileWest);
            double scaleY = fileRows / (fileNorth - fileSouth);
            double EPS = 1E-6;
            int xmin = (int) Math.floor((requestedWest - fileWest) * scaleX + EPS);
            int xmax = (int) Math.ceil((requestedEast - fileWest) * scaleX - EPS);
            int ymin = (int) Math.floor((fileNorth - requestedNorth) * scaleY + EPS);
            int ymax = (int) Math.ceil((fileNorth - requestedSouth) * scaleY - EPS);
            sourceRegion = new Rectangle(xmin, ymin, (xmax - xmin), ymax - ymin);
            requestedRegionEnvelope = new Envelope2D(crs, requestedWest, requestedSouth,
                    requestedEast - requestedWest, requestedNorth - requestedSouth);

            /*
             * define the subsampling values. This done starting from the
             * original's image resolution.
             */
            if (!useSubSamplingAsRequestedColsRows) {
                /*
                 * in this case we respect the original subsampling contract.
                 */
                subSamplingX = 1;
                subSamplingY = 1;
            } else {
                subSamplingX = fileCols;
                subSamplingY = fileRows;

            }
        }
        if (sourceRegion.getWidth() <= 0 || sourceRegion.getHeight() <= 0) {
            return null;
        }

        // now we have enough info to create the ImageReadParam
        imageReadParam.setSourceRegion(sourceRegion);
        imageReadParam.setSourceSubsampling(subSamplingX, subSamplingY, 0, 0);
        RenderedImage finalImage = null;

        BufferedImage image = imageReader.read(0, imageReadParam,
                useSubSamplingAsRequestedColsRows, castDoubleToFloating, monitor);
        imageReader.dispose();
        
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        if (requestedSouth < fileSouth || requestedWest < fileWest || requestedEast > fileEast
                || requestedNorth > fileNorth) {
            double totalHeigth = (requestedNorth - tmpDyN) - (requestedSouth - tmpDyS);

            int xPaddingDx = 0;
            if (xDeltaE > 0) {
                xPaddingDx = (int) Math.round(xDeltaE * (double) imageWidth
                        / ((requestedEast - tmpDxE) - (requestedWest - tmpDxW)));
            }
            int yPaddingBottom = 0;
            if (yDeltaS < 0) {
                yPaddingBottom = (int) Math.round(yDeltaS * (double) imageHeight / (totalHeigth));
            }
            RenderedImage translatedImage = setPadding(xPaddingSx, xPaddingDx, yPaddingTop,
                    yPaddingBottom, image);

            if (requestedRows != imageHeight || requestedCols != imageWidth) {
                finalImage = scaleJAIImage(requestedCols, requestedRows, translatedImage);;
            } else {
                finalImage = translatedImage;
            }
        } else if (requestedRows != imageHeight || requestedCols != imageWidth) {
            finalImage = scaleJAIImage(requestedCols, requestedRows, image);
        } else {
            finalImage = image;
        }

        if (finalImage instanceof RenderedOp) {
            RenderedOp rOp = (RenderedOp) finalImage;
            finalImage = rOp.getAsBufferedImage();
        }

        // set the minimum and the maximum default value in order to obtain a
        // color table.
        double min = JGrassMapEnvironment.defaultMapMin;
        double max = JGrassMapEnvironment.defaultMapMax;

        File rangeFile = jgMapEnvironment.getCELLMISC_RANGE();
        // if the file exists, read the range.
        if (rangeFile.exists()) {
            InputStream is = new FileInputStream(rangeFile);
            byte[] numbers = new byte[16];
            int testread = is.read(numbers);
            is.close();
            if (testread == 16) {
                ByteBuffer rangeBuffer = ByteBuffer.wrap(numbers);
                min = rangeBuffer.getDouble();
                max = rangeBuffer.getDouble();
            }
        }
        range = new double[]{min, max};
        // range = imageReader.getRasterReader().getRange();

        /*
         * create the categories from the color rules
         */
        GridSampleDimension band = createGridSampleDimension(metaDataTable, range);
        band = band.geophysics(true);

        // create a relationship between the real region in the world and the
        // jai space. N.B. the
        // image dimension is only the real dimension which can be extract to a
        // file.
        GridToEnvelopeMapper g2eMapper = new GridToEnvelopeMapper();
        g2eMapper.setEnvelope(requestedRegionEnvelope);
        g2eMapper.setGridRange(new GridEnvelope2D(0, 0, requestedCols, requestedRows));

        g2eMapper.setPixelAnchor(cellAnchor);
        MathTransform gridToEnvelopeTransform = g2eMapper.createTransform();
        GridCoverageFactory factory = CoverageFactoryFinder.getGridCoverageFactory(null);

        GridCoverage2D coverage2D = factory.create(name, finalImage, crs, gridToEnvelopeTransform,
                new GridSampleDimension[]{band}, null, null);
        return coverage2D;

    }

    private RenderedImage setPadding( int xPaddingSx, int xPaddingDx, int yPaddingTop,
            int yPaddingBottom, RenderedImage image ) {
        ParameterBlock block = new ParameterBlock();
        block.addSource(image);
        block.add(Math.abs(xPaddingSx));
        block.add(Math.abs(0));
        block.add(Math.abs(yPaddingTop));
        block.add(Math.abs(0));
        block.add(new BorderExtenderConstant(new double[]{Double.NaN}));
        RenderedOp paddedImage = JAI.create("Border", block);

        block = new ParameterBlock();
        block.addSource(paddedImage);
        block.add((float) -xPaddingSx);
        block.add((float) -yPaddingTop);
        return JAI.create("translate", block);

    }

    private RenderedImage scaleJAIImage( int requestedCols, int requestedRows,
            RenderedImage translatedImage ) {

        ParameterBlock block = new ParameterBlock();
        block.addSource(translatedImage);
        block.add((float) requestedCols / (float) translatedImage.getWidth());
        block.add((float) requestedRows / (float) translatedImage.getHeight());
        // this is the translation, we have set to 0, an alternative is to put
        // the value
        // of the
        // above operation but the result is different because this operation
        // use a
        // special
        // formula.
        block.add(0F);
        block.add(0F);
        block.add(interpolation);
        return JAI.create("scale", block);

    }

    @SuppressWarnings("unused")
    private void printImage( GridCoverage2D coverage2D ) {
        RenderedImage renderedImage = coverage2D.getRenderedImage();
        System.out.println("Image dims: " + renderedImage.getWidth() + "/" //$NON-NLS-1$ //$NON-NLS-2$
                + renderedImage.getHeight());
        RectIter iter = RectIterFactory.create(renderedImage, new Rectangle(0, 0, renderedImage
                .getWidth(), renderedImage.getHeight()));

        for( int i = 0; i < renderedImage.getHeight(); i++ ) {
            for( int j = 0; j < renderedImage.getWidth(); j++ ) {
                System.out.print(iter.getSampleDouble() + " \t"); //$NON-NLS-1$
                iter.nextPixel();
            }
            System.out.println();
        }
    }

    private GridSampleDimension createGridSampleDimension( HashMap<String, String> metaDataTable,
            double[] range ) throws IOException {
        colorRulesString = metaDataTable.get(GrassBinaryImageMetadata.COLOR_RULES_DESCRIPTOR);
        categoriesString = metaDataTable.get(GrassBinaryImageMetadata.CATEGORIES_DESCRIPTOR);
        String[] colorRulesSplit;
        if (colorRulesString.length() > 3 && !colorRulesString.matches(".*Infinty.*|.*NaN.*")) { //$NON-NLS-1$
            colorRulesSplit = colorRulesString.split(GrassBinaryImageMetadata.RULESSPLIT);
        } else {
            List<String> defColorTable = JGrassColorTable.createDefaultColorTable(range, 255);
            colorRulesSplit = (String[]) defColorTable.toArray(new String[defColorTable.size()]);
            // make it also persistent
            File colrFile = jgMapEnvironment.getCOLR();
            JGrassUtilities.makeColorRulesPersistent(colrFile, defColorTable, range, 255);
        }

        int rulesNum = colorRulesSplit.length;

        int COLORNUM = 60000;

        if (colorRulesSplit != null) {
            if (colorRulesSplit.length > COLORNUM) {
                COLORNUM = colorRulesSplit.length + 1;
            }
            if (COLORNUM > 65500) {
                COLORNUM = 65500;
            }

            List<Category> catsList = new ArrayList<Category>();

            double[][] values = new double[rulesNum][2];
            Color[][] colors = new Color[rulesNum][2];
            for( int i = 0; i < rulesNum; i++ ) {
                String colorRule = colorRulesSplit[i];
                JGrassColorTable.parseColorRule(colorRule, values[i], colors[i]);

                if (values[i][0] == values[i][1]) {
                    colors[i][1] = colors[i][0];
                }
                // System.out.println("Processing colorrule: " + colorRule);
            }

            Category noData = new Category("novalue", new Color(Color.WHITE.getRed(), Color.WHITE //$NON-NLS-1$
                    .getGreen(), Color.WHITE.getBlue(), 0), 0);
            catsList.add(noData);

            double a = (values[values.length - 1][1] - values[0][0]) / (double) (COLORNUM - 1);
            double pmin = 1.0;
            double scale = a;
            if (scale == 0) {
                scale = 1;
            }
            double offSet = values[0][0] - scale * pmin;

            int previousUpper = -Integer.MAX_VALUE;
            for( int i = 0; i < rulesNum; i++ ) {
                StringBuilder sB = new StringBuilder();
                sB.append(name);
                sB.append("_"); //$NON-NLS-1$
                sB.append(i);

                double tmpLower = values[i][0];
                double tmpUpper = values[i][1];
                int lower = (int) ((tmpLower - values[0][0]) / scale + pmin);
                int upper = (int) ((tmpUpper - values[0][0]) / scale + pmin);
                if (lower <= previousUpper) {
                    lower = previousUpper + 1;
                }
                if (lower >= upper) {
                    upper = lower + 1;
                }
                previousUpper = upper;

                Category dataCategory = new Category(sB.toString(), colors[i], lower, upper, scale,
                        offSet);

                catsList.add(dataCategory);
            }

            Category[] array = (Category[]) catsList.toArray(new Category[catsList.size()]);
            return new GridSampleDimension(name, array, null);
        } else {
            return new GridSampleDimension(name, new Category[]{}, null);
        }
    }

    // @SuppressWarnings("nls")
    // public static void main( String[] args ) {
    // try {
    // String mapPath = "/home/daniele/Jgrassworkspace/testLettura/test/cell/testa";
    // // ";
    // // String mapPath =
    // //
    // "/home/moovida/rcpdevelopment/WORKSPACES/eclipseGanimede/jai_tests/spearfish/PERMANENT/cell/elevation.dem"
    // // ;
    //
    // /*
    // * test1 - read the whole data
    // */
    // Interpolation interpolation = Interpolation.getInstance(Interpolation.INTERP_NEAREST);
    // GrassCoverageReader tmp = new GrassCoverageReader(PixelInCell.CELL_CENTER,
    // interpolation, true, false, new PrintStreamProgressMonitor(System.out,
    // System.out));
    // tmp.setInput(new File(mapPath));
    //
    // // JGrassRegion readRegion = JGrassRegion
    // // .getActiveRegionFromMapset("/home/daniele/Jgrassworkspace/testLettura/test");
    // // JGrassRegion readRegion = new JGrassRegion(1640650.0, 1640950.0,
    // // 5139780.0, 5140020.0, 30.0, 30.0);
    // JGrassRegion readRegion = new JGrassRegion(1640650.0, 1640950.0, 5139780.0, 5140020.0,
    // 60.0, 60.0);
    //
    // GrassCoverageReadParam gcReadParam = new GrassCoverageReadParam(readRegion);
    //
    // GridCoverage2D coverage2D = tmp.read(gcReadParam);
    // Raster fileImage = coverage2D.getRenderedImage().getData();
    // for( int j = fileImage.getMinX(); j < fileImage.getMinX() + fileImage.getHeight(); j++ ) {
    // for( int i = fileImage.getMinY(); i < fileImage.getMinY() + fileImage.getWidth(); i++ ) {
    // System.out.print(fileImage.getSampleDouble(i, j, 0) + "\t");
    // }
    // System.out.println();
    // }
    //
    // if (true) {
    // System.exit(0);
    // }
    //
    // Point2D point = new Point2D.Double(608940.0, 4914330.0);
    // double[] buffer = new double[1];
    // System.out.println(coverage2D.evaluate(point, buffer)[0]);
    //
    // interpolation = Interpolation.getInstance(Interpolation.INTERP_BILINEAR);
    // GridCoverage2D interpolated = (GridCoverage2D) Operations.DEFAULT.interpolate(
    // coverage2D, interpolation);
    // System.out.println(interpolated.evaluate(point, buffer)[0]);
    //
    // GridCoverage2D integerView = coverage2D.view(ViewType.RENDERED);
    // System.out.println(integerView.evaluate(point, buffer)[0]);
    //
    // RenderedImage renderedImage = integerView.getRenderedImage();
    //            OutputStream oStream = new FileOutputStream(new File("/Users/moovida/Desktop/test.png")); //$NON-NLS-1$
    //            ImageIO.write(renderedImage, "png", oStream); //$NON-NLS-1$
    //
    // // GridCoverage2D geographic = (GridCoverage2D)
    // // Operations.DEFAULT.resample(coverage2D,
    // // DefaultGeographicCRS.WGS84);
    // // // GridGeometry2D g2d = new GridGeometry2D()
    // // // with GridGeometry I can control the target grid
    // //
    // // ImageIO.write(geographic.view(ViewType.RENDERED).getRenderedImage(),
    // // "png", new File(
    // // "/home/moovida/Desktop/test2.png"));
    // //
    // /*
    // * test2 - read the map at lower resolution
    // */
    // tmp = new GrassCoverageReader(null, null, false, false, new PrintStreamProgressMonitor(
    // System.out, System.out));
    // tmp.setInput(new File(mapPath));
    //
    // readRegion = new JGrassRegion(589980.0, 609000.0, 4913690.0, 4928030.0, 478, 200);
    // gcReadParam = new GrassCoverageReadParam(readRegion);
    //
    // coverage2D = tmp.read(gcReadParam);
    // integerView = coverage2D.view(ViewType.RENDERED);
    // renderedImage = integerView.getRenderedImage();
    //
    //            oStream = new FileOutputStream(new File("/Users/moovida/Desktop/test1.png")); //$NON-NLS-1$
    // ImageIO.write(renderedImage, "png", oStream);
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }

    /**
     * Getter for the legend string.
     * 
     * @return the legendstring.
     */
    public String getLegendString() {
        return colorRulesString;
    }

    /**
     * Getter for the categories string.
     * 
     * @return the categories string.
     */
    public String getCategoriesString() {
        return categoriesString;
    }

    /**
     * Gets the range.
     * 
     * <b>Note that the range is available only if the raster was read once.</b>
     * 
     * @return the range non considering novalues.
     */
    public double[] getRange() {
        return range;
    }

    @Override
    public GridCoverage2D read( GeneralParameterValue[] params ) throws IllegalArgumentException,
            IOException {

        GeneralEnvelope requestedEnvelope = null;
        Rectangle dim = null;
        // OverviewPolicy overviewPolicy = null;
        if (params != null) {
            for( int i = 0; i < params.length; i++ ) {
                final ParameterValue< ? > param = (ParameterValue< ? >) params[i];
                final String name = param.getDescriptor().getName().getCode();
                if (name.equals(AbstractGridFormat.READ_GRIDGEOMETRY2D.getName().toString())) {
                    final GridGeometry2D gg = (GridGeometry2D) param.getValue();
                    requestedEnvelope = new GeneralEnvelope((Envelope) gg.getEnvelope2D());
                    dim = gg.getGridRange2D().getBounds();
                    continue;
                }
                // if (name.equals(AbstractGridFormat.OVERVIEW_POLICY.getName().toString())) {
                // overviewPolicy = (OverviewPolicy) param.getValue();
                // continue;
                // }
            }
        }
        if (requestedEnvelope != null && dim != null) {
            DirectPosition lowerCorner = requestedEnvelope.getLowerCorner();
            double[] westSouth = lowerCorner.getCoordinate();
            DirectPosition upperCorner = requestedEnvelope.getUpperCorner();
            double[] eastNorth = upperCorner.getCoordinate();
            JGrassRegion region = new JGrassRegion(westSouth[0], eastNorth[0], westSouth[1],
                    eastNorth[1], dim.height, dim.width);
            return readRaster(region);
        }

        return readRaster((JGrassRegion) null);
    }

    public Format getFormat() {
        return new GrassCoverageFormat();
    }
}
