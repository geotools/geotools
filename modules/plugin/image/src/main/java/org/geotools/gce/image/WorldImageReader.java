/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.geotools.gce.image;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.renderable.ParameterBlock;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import org.geotools.api.coverage.grid.Format;
import org.geotools.api.data.DataSourceException;
import org.geotools.api.data.FileGroupProvider.FileGroup;
import org.geotools.api.geometry.Bounds;
import org.geotools.api.geometry.MismatchedDimensionException;
import org.geotools.api.parameter.GeneralParameterValue;
import org.geotools.api.parameter.ParameterValue;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.datum.PixelInCell;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.data.PrjFileReader;
import org.geotools.data.WorldFileReader;
import org.geotools.geometry.GeneralBounds;
import org.geotools.image.io.ImageIOExt;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.builder.GridToEnvelopeMapper;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.util.factory.Hints;

/**
 * Reads a GridCoverage from a given source. WorldImage sources only support one GridCoverage so hasMoreGridCoverages()
 * will return true until the only GridCoverage is read. No metadata is currently supported, so all related methods
 * return null. In the early future we will start (hopefully supporting them).
 *
 * @author simone giannecchini
 * @author alessio fabiani
 * @author rgould
 */
public final class WorldImageReader extends AbstractGridCoverage2DReader implements GridCoverage2DReader {

    /** Logger. */
    private Logger LOGGER = org.geotools.util.logging.Logging.getLogger(WorldImageReader.class);

    private boolean wmsRequest;

    private boolean metaFile;

    private String parentPath;

    private String extension;

    private ImageReaderSpi readerSPI;

    /**
     * Class constructor. Construct a new ImageWorldReader to read a GridCoverage from the source object. The source
     * must point to the raster file itself, not the world file. If the source is a Java URL it checks if it is ponting
     * to a file and if so it converts the url into a file.
     *
     * @param input The source of a GridCoverage, can be a File, a URL or an input stream.
     */
    public WorldImageReader(Object input) throws DataSourceException {
        this(input, null);
    }

    /**
     * Class constructor. Construct a new ImageWorldReader to read a GridCoverage from the source object. The source
     * must point to the raster file itself, not the world file. If the source is a Java URL it checks if it is ponting
     * to a file and if so it converts the url into a file.
     *
     * @param input The source of a GridCoverage, can be a File, a URL or an input stream.
     */
    public WorldImageReader(Object input, final Hints hints) throws DataSourceException {
        // /////////////////////////////////////////////////////////////////////
        //
        // Checking input
        //
        // /////////////////////////////////////////////////////////////////////
        if (input == null) {

            final IOException ex = new IOException("WorldImage:No source set to read this coverage.");
            LOGGER.logp(
                    Level.SEVERE, WorldImageReader.class.toString(), "WorldImageReader", ex.getLocalizedMessage(), ex);
            throw new DataSourceException(ex);
        }
        this.source = input;

        // //
        //
        // managing hints
        //
        // //
        if (this.hints == null) this.hints = new Hints();
        if (hints != null) {
            this.hints.add(hints);
        }

        // GridCoverageFactory initialization
        if (this.hints.containsKey(Hints.GRID_COVERAGE_FACTORY)) {
            final Object factory = this.hints.get(Hints.GRID_COVERAGE_FACTORY);
            if (factory != null && factory instanceof GridCoverageFactory) {
                this.coverageFactory = (GridCoverageFactory) factory;
            }
        }
        if (this.coverageFactory == null) {
            this.coverageFactory = CoverageFactoryFinder.getGridCoverageFactory(this.hints);
        }

        coverageName = "image_coverage";
        try {
            boolean closeMe = true;

            // /////////////////////////////////////////////////////////////////////
            //
            // Source management
            //
            // /////////////////////////////////////////////////////////////////////
            if (input instanceof URL) {
                // URL that point to a file
                final URL sourceURL = (URL) input;
                if (sourceURL.getProtocol().compareToIgnoreCase("file") == 0) {
                    String auth = sourceURL.getAuthority();
                    String path = sourceURL.getPath();
                    if (auth != null && !auth.equals("")) {
                        path = "//" + auth + path;
                    }
                    this.source = input = new File(URLDecoder.decode(path, "UTF-8"));
                } else if (sourceURL.getProtocol().equalsIgnoreCase("http")) {
                    // // getting a stream to the reader
                    // this.source = sourceURL.openStream();

                    // /////////////////////////////////////////////////////////////////////
                    //
                    // WMS Request? I want to be able to handle that case too
                    //
                    // /////////////////////////////////////////////////////////////////////
                    wmsRequest = WMSRequest(input);
                }
            }

            // //
            //
            // Name, path, etc...
            //
            // //
            if (input instanceof File) {
                final File sourceFile = (File) input;
                final String filename = sourceFile.getName();
                final int i = filename.lastIndexOf('.');
                final int length = filename.length();
                if (i > 0 && i < length - 1) {
                    extension = filename.substring(i + 1).toLowerCase();
                }
                this.parentPath = sourceFile.getParent();
                this.coverageName = filename;
                final int dotIndex = coverageName.lastIndexOf(".");
                coverageName = dotIndex == -1 ? coverageName : coverageName.substring(0, dotIndex);
            } else if (input instanceof URL) input = ((URL) input).openStream();
            // //
            //
            // Get a stream in order to read from it for getting the basic
            // information for this coverfage
            //
            // //
            if (input instanceof ImageInputStream) {
                closeMe = false;
            } else {
                inStreamSPI = ImageIOExt.getImageInputStreamSPI(source);
                if (inStreamSPI == null) throw new DataSourceException("No input stream for the provided source");
                inStream = inStreamSPI.createInputStreamInstance(
                        this.source, ImageIO.getUseCache(), ImageIO.getCacheDirectory());
            }

            if (inStream == null) throw new IllegalArgumentException("No input stream for the provided source");

            // /////////////////////////////////////////////////////////////////////
            //
            // CRS
            //
            // /////////////////////////////////////////////////////////////////////
            if (!wmsRequest) {
                final Object tempCRS = this.hints.get(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM);
                if (tempCRS != null) {
                    this.crs = (CoordinateReferenceSystem) tempCRS;
                    LOGGER.log(Level.WARNING, "Using forced coordinate reference system ");
                } else readCRS();
            }

            // /////////////////////////////////////////////////////////////////////
            //
            // Informations about multiple levels and such
            //
            // /////////////////////////////////////////////////////////////////////
            getHRInfo();

            // release the stream
            if (closeMe) inStream.close();
        } catch (IOException | TransformException e) {
            LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
            throw new DataSourceException(e);
        }
    }

    /** Gets the relevant information for the underlying raster. */
    private void getHRInfo() throws IOException, TransformException {

        // //
        //
        // Get a reader for this format
        // TODO optimize this using image file extension when possible
        //
        // //
        final Iterator<ImageReader> it = ImageIO.getImageReaders(inStream);
        if (!it.hasNext()) throw new DataSourceException("No reader avalaible for this source");
        final ImageReader reader = it.next();
        readerSPI = reader.getOriginatingProvider();
        reader.setInput(inStream);

        //
        // parse and set layout
        //
        setLayout(reader);

        // //
        //
        // get the dimension of the hr image and build the model as well as
        // computing the resolution
        // //
        numOverviews = wmsRequest ? 0 : reader.getNumImages(true) - 1;
        int hrWidth = reader.getWidth(0);
        int hrHeight = reader.getHeight(0);
        final Rectangle actualDim = new Rectangle(0, 0, hrWidth, hrHeight);
        originalGridRange = new GridEnvelope2D(actualDim);

        // /////////////////////////////////////////////////////////////////////
        //
        // Envelope, coverage name and other resolution information
        //
        // /////////////////////////////////////////////////////////////////////
        if (source instanceof File) {
            prepareWorldImageGridToWorldTransform();

            // //
            //
            // In case we read from a real world file we have toget the envelope
            //
            // //
            if (!metaFile) {
                final AffineTransform tempTransform = new AffineTransform((AffineTransform) raster2Model);
                tempTransform.translate(-0.5, -0.5);

                originalEnvelope =
                        CRS.transform(ProjectiveTransform.create(tempTransform), new GeneralBounds(actualDim));
                originalEnvelope.setCoordinateReferenceSystem(crs);
                highestRes = new double[2];
                highestRes[0] = XAffineTransform.getScaleX0(tempTransform);
                highestRes[1] = XAffineTransform.getScaleY0(tempTransform);
            } else {
                // ///
                //
                // setting the higher resolution available for this coverage
                //
                // ///
                highestRes = getResolution(originalEnvelope, actualDim, crs);
                final GridToEnvelopeMapper mapper = new GridToEnvelopeMapper(originalGridRange, originalEnvelope);
                mapper.setPixelAnchor(PixelInCell.CELL_CENTER);
                this.raster2Model = mapper.createTransform();
            }
        }
        // //
        //
        // get information for the overviews in case ony exists
        //
        // //
        if (numOverviews >= 1) {
            overViewResolutions = new double[numOverviews][2];
            for (int i = 0; i < numOverviews; i++) {
                overViewResolutions[i][0] = highestRes[0] * this.originalGridRange.getSpan(0) / reader.getWidth(i + 1);
                overViewResolutions[i][1] = highestRes[1] * this.originalGridRange.getSpan(1) / reader.getHeight(i + 1);
            }
        } else overViewResolutions = null;
    }

    /**
     * Returns the format that this Reader accepts.
     *
     * @return a new WorldImageFormat class
     */
    @Override
    public Format getFormat() {
        return new WorldImageFormat();
    }

    /**
     * Reads an image from a source stream. Loads an image from a source stream, then loads the values from the world
     * file and constructs a new GridCoverage from this information. When reading from a remote stream we do not look
     * for a world fiel but we suppose those information comes from a different way (xml, gml, pigeon?)
     *
     * @param params WorldImageReader supports no parameters, it just ignores them.
     * @return a new GridCoverage read from the source.
     */
    @Override
    public GridCoverage2D read(GeneralParameterValue... params) throws IllegalArgumentException, IOException {

        // /////////////////////////////////////////////////////////////////////
        //
        // do we have parameters to use for reading from the specified source
        //
        // /////////////////////////////////////////////////////////////////////
        GeneralBounds requestedEnvelope = null;
        Rectangle dim = null;
        OverviewPolicy overviewPolicy = null;
        // /////////////////////////////////////////////////////////////////////
        //
        // Checking params
        //
        // /////////////////////////////////////////////////////////////////////
        if (params != null) {
            for (GeneralParameterValue generalParameterValue : params) {
                final ParameterValue param = (ParameterValue) generalParameterValue;
                final String name = param.getDescriptor().getName().getCode();
                if (name.equals(AbstractGridFormat.READ_GRIDGEOMETRY2D.getName().toString())) {
                    final GridGeometry2D gg = (GridGeometry2D) param.getValue();
                    requestedEnvelope = new GeneralBounds((Bounds) gg.getEnvelope2D());
                    dim = gg.getGridRange2D().getBounds();
                    continue;
                }
                if (name.equals(AbstractGridFormat.OVERVIEW_POLICY.getName().toString())) {
                    overviewPolicy = (OverviewPolicy) param.getValue();
                    continue;
                }
            }
        }
        // /////////////////////////////////////////////////////////////////////
        //
        // set params
        //
        // /////////////////////////////////////////////////////////////////////
        Integer imageChoice = Integer.valueOf(0);
        final ImageReadParam readP = new ImageReadParam();
        if (!wmsRequest) {
            try {
                imageChoice = setReadParams(overviewPolicy, readP, requestedEnvelope, dim);
            } catch (TransformException e) {
                throw new DataSourceException(e);
            }
        }
        // /////////////////////////////////////////////////////////////////////
        //
        // Reading the source layer
        //
        // /////////////////////////////////////////////////////////////////////
        // final ImageReader reader = readerSPI.createReaderInstance();
        // final ImageInputStream inStream = wmsRequest ? ImageIO
        // .createImageInputStream(((URL) source).openStream()) : ImageIO
        // .createImageInputStream(source);
        //
        final Hints newHints = hints.clone();
        // if (!wmsRequest) {
        // reader.setInput(inStream);
        // if (!reader.isImageTiled(imageChoice.intValue())) {
        // final Dimension tileSize = ImageUtilities
        // .toTileSize(new Dimension(reader.getWidth(imageChoice
        // .intValue()), reader.getHeight(imageChoice
        // .intValue())));
        // final ImageLayout layout = new ImageLayout();
        // layout.setTileGridXOffset(0);
        // layout.setTileGridYOffset(0);
        // layout.setTileHeight(tileSize.height);
        // layout.setTileWidth(tileSize.width);
        // newHints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout));
        // }
        // }
        // inStream.close();
        final ParameterBlock pbjRead = new ParameterBlock();
        pbjRead.add(
                inStreamSPI != null
                        ? inStreamSPI.createInputStreamInstance(
                                source, ImageIO.getUseCache(), ImageIO.getCacheDirectory())
                        : ImageIO.createImageInputStream(source));
        // pbjRead.add(wmsRequest ? ImageIO
        // .createImageInputStream(((URL) source).openStream()) : ImageIO
        // .createImageInputStream(source));
        pbjRead.add(imageChoice);
        pbjRead.add(Boolean.FALSE);
        pbjRead.add(Boolean.FALSE);
        pbjRead.add(Boolean.FALSE);
        pbjRead.add(null);
        pbjRead.add(null);
        pbjRead.add(readP);
        pbjRead.add(readerSPI.createReaderInstance());
        final RenderedOp coverageRaster = JAI.create("ImageRead", pbjRead, newHints);

        // /////////////////////////////////////////////////////////////////////
        //
        // BUILDING COVERAGE
        //
        // /////////////////////////////////////////////////////////////////////
        AffineTransform rasterToModel = getRescaledRasterToModel(coverageRaster);
        return createImageCoverage(coverageRaster, ProjectiveTransform.create(rasterToModel));
    }

    /**
     * This method is used to check if we are connecting directly to a WMS with a getmap request. In such a case we skip
     * reading all the parameters we can read from this http string.
     *
     * @return true if we are dealing with a WMS request, false otherwise.
     */
    private boolean WMSRequest(Object input) {
        // TODO do we need the requested envelope?
        if (input instanceof URL && ((URL) input).getProtocol().equalsIgnoreCase("http")) {
            try {
                // getting the query
                final String query =
                        java.net.URLDecoder.decode(((URL) input).getQuery().intern(), "UTF-8");

                // should we proceed? Let's look for a getmap WMS request
                if (query.intern().indexOf("GetMap") == -1) {
                    return false;
                }

                // tokenizer on $
                final String[] pairs = query.split("&");

                // parse each pair
                String[] kvp = null;
                for (String pair : pairs) {
                    // splitting the pairs
                    kvp = pair.split("=");

                    // checking the fields
                    // BBOX
                    if (kvp[0].equalsIgnoreCase("BBOX")) {
                        // splitting fields
                        kvp = kvp[1].split(",");
                        originalEnvelope = new GeneralBounds(
                                new double[] {Double.parseDouble(kvp[0]), Double.parseDouble(kvp[1])},
                                new double[] {Double.parseDouble(kvp[2]), Double.parseDouble(kvp[3])});
                    }

                    // SRS
                    if (kvp[0].equalsIgnoreCase("SRS")) {
                        crs = CRS.decode(kvp[1], true);
                    }

                    // layers
                    if (kvp[0].equalsIgnoreCase("layers")) {
                        this.coverageName = kvp[1].replaceAll(",", "_");
                    }
                }

            } catch (IndexOutOfBoundsException | MismatchedDimensionException | IOException | FactoryException e) {
                // TODO how to handle this?
                return false;
            }

            return true;
        }

        return false;
    }

    /**
     * This method is responsible for reading the CRS whhther a projection file is provided. If no projection file is
     * provided the second choice is the CRS supplied via the crs paramter. If even this one is not avalaible we default
     * to EPSG:4326.
     */
    private void readCRS() throws IOException {

        // check to see if there is a projection file
        if (source instanceof File || source instanceof URL && ((URL) source).getProtocol() == "file") {
            // getting name for the prj file
            final String sourceAsString;

            if (source instanceof File) {
                sourceAsString = ((File) source).getAbsolutePath();
            } else {
                String auth = ((URL) source).getAuthority();
                String path = ((URL) source).getPath();
                if (auth != null && !auth.equals("")) {
                    sourceAsString = "//" + auth + path;
                } else {
                    sourceAsString = path;
                }
            }

            final int index = sourceAsString.lastIndexOf(".");
            final StringBuffer base = new StringBuffer(sourceAsString.substring(0, index)).append(".prj");

            // does it exist?
            final File prjFile = new File(base.toString());
            if (prjFile.exists()) {
                // it exists then we have top read it

                try (FileChannel channel = new FileInputStream(prjFile).getChannel();
                        PrjFileReader projReader = new PrjFileReader(channel)) {
                    crs = projReader.getCoordinateReferenceSystem();
                } catch (FactoryException | IOException e) {
                    // warn about the error but proceed, it is not fatal
                    // we have at least the default crs to use
                    LOGGER.log(Level.INFO, e.getLocalizedMessage(), e);
                }
            }
        }
        if (crs == null) {
            crs = AbstractGridFormat.getDefaultCRS();
            LOGGER.fine("Unable to find crs, continuing with default CRS");
        }
    }

    /**
     * This method is in charge for reading the metadata file and for creating a valid envelope (whether possible);
     *
     * <p>TODO it would be great to having a centralized management for the world file
     */
    private void prepareWorldImageGridToWorldTransform() throws IOException {

        // getting name and extension
        final String base = parentPath != null
                ? new StringBuffer(this.parentPath)
                        .append(File.separator)
                        .append(coverageName)
                        .toString()
                : coverageName;

        // We can now construct the baseURL from this string.
        File file2Parse = new File(new StringBuffer(base).append(".wld").toString());

        if (file2Parse.exists()) {
            final WorldFileReader reader = new WorldFileReader(file2Parse);
            raster2Model = reader.getTransform();
        } else {
            // looking for another extension
            final Set<String> ext = WorldImageFormat.getWorldExtension(extension);
            final Iterator<String> it = ext.iterator();
            if (!it.hasNext()) throw new DataSourceException("Unable to parse extension " + extension);
            do {
                file2Parse = new File(new StringBuffer(base).append(it.next()).toString());
            } while (!file2Parse.exists() && it.hasNext());

            if (file2Parse.exists()) {
                // parse world file
                final WorldFileReader reader = new WorldFileReader(file2Parse);
                raster2Model = reader.getTransform();
                metaFile = false;
            } else {
                // looking for a meta file
                file2Parse = new File(new StringBuffer(base).append(".meta").toString());

                if (file2Parse.exists()) {
                    parseMetaFile(file2Parse);
                    metaFile = true;
                } else {
                    LOGGER.warning("Could not find a world transform file for "
                            + coverageName
                            + ", assuming the identity transform");
                    raster2Model = ProjectiveTransform.create(new AffineTransform());
                }
            }
        }
    }

    /**
     * This method is responsible for parsing a META file which is nothing more than another format of a WorldFile used
     * by the GIDB database.
     *
     * @task move me to a separate implementation
     */
    private void parseMetaFile(File file2Parse) throws NumberFormatException, IOException {
        double xMin = 0.0;
        double yMax = 0.0;
        double xMax = 0.0;
        double yMin = 0.0;

        // getting a buffered reader
        try (BufferedReader in = new BufferedReader(new FileReader(file2Parse, StandardCharsets.UTF_8))) {

            // parsing the lines
            String str = null;
            int index = 0;
            double value = 0;

            while ((str = in.readLine()) != null) {
                switch (index) {
                    case 1:
                        value = Double.parseDouble(
                                str.substring("Origin Longitude = ".intern().length()));
                        xMin = value;

                        break;

                    case 2:
                        value = Double.parseDouble(
                                str.substring("Origin Latitude = ".intern().length()));
                        yMin = value;

                        break;

                    case 3:
                        value = Double.parseDouble(
                                str.substring("Corner Longitude = ".intern().length()));
                        xMax = value;

                        break;

                    case 4:
                        value = Double.parseDouble(
                                str.substring("Corner Latitude = ".intern().length()));
                        yMax = value;

                        break;

                    default:
                        break;
                }

                index++;
            }
        }

        // building up envelope of this coverage
        originalEnvelope = new GeneralBounds(new double[] {xMin, yMin}, new double[] {xMax, yMax});
        originalEnvelope.setCoordinateReferenceSystem(crs);
    }

    /**
     * Number of coverages for this reader is 1
     *
     * @return the number of coverages for this reader.
     */
    @Override
    public int getGridCoverageCount() {
        return 1;
    }

    /**
     * Returns the file extension of the image.
     *
     * @since 2.7
     */
    public String getExtension() {
        return extension;
    }

    @Override
    protected List<FileGroup> getFiles() {
        File file = getSourceAsFile();
        if (file == null) {
            return null;
        }

        List<File> files = new ArrayList<>();
        List<String> extensions = new ArrayList<>();
        extensions.add(".prj");
        Set<String> worldExtensions = WorldImageFormat.getWorldExtension(getExtension());
        extensions.addAll(worldExtensions);
        String[] siblingExtensions = extensions.toArray(new String[extensions.size()]);
        addAllSiblings(file, files, siblingExtensions);
        return Collections.singletonList(new FileGroup(file, files, null));
    }
}
