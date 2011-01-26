/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.image.io.netcdf;

import java.util.Set;
import java.util.List;
import java.util.Locale;
import java.util.HashSet;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.awt.image.DataBuffer;
import java.net.URL;
import java.net.URI;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import javax.imageio.IIOException;
import javax.imageio.ImageReader;
import javax.imageio.ImageReadParam;
import javax.imageio.metadata.IIOMetadata;

import ucar.ma2.Array;
import ucar.ma2.Range;
import ucar.ma2.DataType;
import ucar.ma2.IndexIterator;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.dataset.AxisType;
import ucar.nc2.dataset.CoordinateAxis;
import ucar.nc2.dataset.CoordinateSystem;
import ucar.nc2.dataset.CoordSysBuilder;
import ucar.nc2.dataset.NetcdfDataset;
import ucar.nc2.dataset.VariableDS;
import ucar.nc2.dataset.VariableEnhanced;
import ucar.nc2.dods.DODSNetcdfFile;
import ucar.nc2.util.CancelTask;
import ucar.nc2.Dimension;
import ucar.nc2.Variable;
import ucar.nc2.VariableIF;

import org.geotools.image.io.FileImageReader;
import org.geotools.image.io.SampleConverter;
import org.geotools.image.io.metadata.GeographicMetadata;
import org.geotools.math.Statistics;
import org.geotools.resources.XArray;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;


/**
 * Base implementation for NetCDF image reader. Pixels are assumed organized according the COARDS
 * convention (a precursor of <A HREF="http://www.cfconventions.org/">CF Metadata conventions</A>),
 * i.e. in (<var>t</var>,<var>z</var>,<var>y</var>,<var>x</var>) order, where <var>x</var> varies
 * faster. The image is created from the two last dimensions (<var>x</var>,<var>y</var>).
 * Additional dimensions (if any) are handled as below in the default implementation:
 * <p>
 * <ul>
 *   <li>The third dimension (<var>z</var> in the above sequence) is assigned to bands. Users
 *       can change this behavior by invoking the {@link NetcdfReadParam#setBandDimensionTypes}
 *       method.</li>
 *   <li>Additional dimensions like <var>t</var> are ignored; only the first slice is selected.
 *       Users can change this behavior by invoking the {@link NetcdfReadParam#setSliceIndice}
 *       method.</li>
 * </ul>
 * <p>
 * <b>Example:</b><br>
 * Assuming that:
 * <ul>
 *   <li>None of the above methods has been invoked</li>
 *   <li>Axis are (<var>t</var>,<var>z</var>,<var>y</var>,<var>x</var>)</li>
 * </ul>
 * Then the users can select the <var>z</var> value using {@link ImageReadParam#setSourceBands}.
 * If no band is selected, then the default selection is the first band (0) only. Note that this
 * is different than the usual Image I/O default, which is all bands.
 * <p>
 * <b>Connection to DODS servers</b>
 * This image reader accepts {@link File} and {@link URL} inputs. In the later case, if and only
 * if the URL uses the DODS protocol (as in "{@code dods://opendap.aviso.oceanobs.com/}"), then
 * this image reader tries to connect to the DODS remote server. Otherwise the URL content is
 * copied in a temporary file.
 *
 * @since 2.4
 * @source $URL$
 * @version $Id$
 * @author Antoine Hnawia
 * @author Martin Desruisseaux
 */
public class NetcdfImageReader extends FileImageReader implements CancelTask {
    /**
     * The URL protocol for connections to a DODS server. Any URL with this protocol will be
     * open using {@link DODSNetcdfFile} instead of the ordinary {@link NetcdfDataset}. The
     * later works only for local {@linkplain File files}, while the former connects to a
     * remote server.
     */
    private static final String DODS_PROTOCOL = "dods";

    /**
     * The dimension <strong>relative to the rank</strong> in {@link #variable} to use as image
     * width. The actual dimension is {@code variable.getRank() - X_DIMENSION}. Is hard-coded
     * because the loop in the {@code read} method expects this order.
     */
    private static final int X_DIMENSION = 1;

    /**
     * The dimension <strong>relative to the rank</strong> in {@link #variable} to use as image
     * height. The actual dimension is {@code variable.getRank() - Y_DIMENSION}. Is hard-coded
     * because the loop in the {@code read} method expects this order.
     */
    private static final int Y_DIMENSION = 2;

    /**
     * The default dimension <strong>relative to the rank</strong> in {@link #variable} to use
     * as image bands. The actual dimension is {@code variable.getRank() - Z_DIMENSION}.
     * <p>
     * At the difference of {@link #X_DIMENSION} and {@link #Y_DIMENSION}, this dimension doesn't
     * need to be hard-coded. User can invoke {@link NetcdfReadParam#setBandDimensionTypes} in
     * order to provide a different value.
     */
    private static final int Z_DIMENSION = 3;

    /**
     * The data type to accept in images. Used for automatic detection of which variables
     * to assign to images.
     */
    private static final Set<DataType> VALID_TYPES = new HashSet<DataType>(12);
    static {
        VALID_TYPES.add(DataType.BOOLEAN);
        VALID_TYPES.add(DataType.BYTE);
        VALID_TYPES.add(DataType.SHORT);
        VALID_TYPES.add(DataType.INT);
        VALID_TYPES.add(DataType.LONG);
        VALID_TYPES.add(DataType.FLOAT);
        VALID_TYPES.add(DataType.DOUBLE);
    }

    /**
     * The NetCDF dataset, or {@code null} if not yet open. The NetCDF file is open by
     * {@link #ensureOpen} when first needed.
     */
    private NetcdfDataset dataset;

    /**
     * The name of the {@linkplain Variable variables} to be read in a NetCDF file.
     * The first name is assigned to image index 0, the second name to image index 1,
     * <cite>etc.</cite>.
     */
    private String[] variableNames;

    /**
     * The image index of the current {@linkplain #variable variable}.
     */
    private int variableIndex;

    /**
     * The data from the NetCDF file. The value for this field is set by {@link #prepareVariable}
     * when first needed. This is typically (but not necessarly) an instance of {@link VariableDS}.
     */
    protected Variable variable;

    /**
     * The last error from the NetCDF library.
     */
    private String lastError;

    /**
     * {@code true} if {@link CoordSysBuilder#addCoordinateSystems} has been invoked
     * for current file.
     */
    private boolean metadataLoaded;

    /**
     * The stream metadata. Will be created only when first needed.
     */
    private IIOMetadata streamMetadata;

    /**
     * The current image metadata. Will be created only when first needed.
     */
    private IIOMetadata imageMetadata;

    /**
     * Constructs a new NetCDF reader.
     *
     * @param spi The service provider.
     */
    public NetcdfImageReader(final Spi spi) {
        super(spi);
    }

    /**
     * Returns the {@linkplain #input input} as an URL to a DODS dataset, or {@code null} if
     * none. If this method returns a non-null value, then the input should be open using a
     * {@link DODSNetcdfFile}. Otherwise it should be open using an ordinary {@link NetcdfDataset}.
     * <p>
     * Note that we returns the URL as a String, not as a {@link URL} object, in order to avoid
     * an "unknown protocol" exception.
     */
    private String getInputDODS() {
        String protocol = null;
        if (input instanceof URL) {
            final URL url = (URL) input;
            protocol = url.getProtocol();
        } else if (input instanceof URI) {
            final URI url = (URI) input;
            protocol = url.getScheme();
        } else if (input instanceof String) {
            final String url = (String) input;
            final int s = url.indexOf(':');
            if (s > 0) {
                protocol = url.substring(0, s);
            }
        }
        if (protocol == null || !protocol.equalsIgnoreCase(DODS_PROTOCOL)) {
            return null;
        }
        return input.toString();
    }

    /**
     * Returns the names of the variables to be read. The first name is assigned to image
     * index 0, the second name to image index 1, <cite>etc.</cite>. In other words a call
     * to <code>{@linkplain #read(int) read}(imageIndex)</code> will read the variable names
     * {@code variables[imageIndex]} where {@code variables} is the value returned by this
     * method.
     * <p>
     * The sequence of variable to be read can be changed by a call to {@link #setVariables}.
     *
     * @return The name of the variables to be read.
     * @throws IOException if the NetCDF file can not be read.
     */
    public String[] getVariables() throws IOException {
        if (variableNames == null) {
            ensureFileOpen();
        }
        return variableNames.clone();
    }

    /**
     * Sets the name of the {@linkplain Variable variables} to be read in a NetCDF file.
     * The first name is assigned to image index 0, the second name to image index 1,
     * <cite>etc.</cite>.
     * <p>
     * If {@code variableNames} is set to {@code null} (which is the default), then the
     * variables will be inferred from the content of the NetCDF file.
     *
     * @param variableNames The set of variables to be assigned to image index.
     */
    public void setVariables(final String[] variableNames) {
        this.variableNames = (variableNames != null) ? variableNames.clone() : null;
    }

    /**
     * Returns the number of images available from the current input source.
     *
     * @throws IllegalStateException if the input source has not been set.
     * @throws IOException if an error occurs reading the information from the input source.
     */
    @Override
    public int getNumImages(final boolean allowSearch) throws IllegalStateException, IOException {
        ensureFileOpen();
        // TODO: consider returning the actual number of images in the file.
        return variableNames.length;
    }

    /**
     * Returns the number of bands available for the specified image.
     *
     * @param  imageIndex  The image index.
     * @return The number of bands available.
     * @throws IOException if an error occurs reading the information from the input source.
     */
    @Override
    public int getNumBands(final int imageIndex) throws IOException {
        prepareVariable(imageIndex);
        final int rank = variable.getRank();
        final int bandDimension = rank - Z_DIMENSION;
        if (bandDimension >= 0 && bandDimension < rank) {
            return variable.getDimension(bandDimension).getLength();
        }
        return super.getNumBands(imageIndex);
    }

    /**
     * Returns the number of dimension of the image at the given index.
     *
     * @param  imageIndex The image index.
     * @return The number of dimension for the image at the given index.
     * @throws IOException if an error occurs reading the information from the input source.
     *
     * @since 2.5
     */
    @Override
    public int getDimension(final int imageIndex) throws IOException {
        prepareVariable(imageIndex);
        return variable.getRank();
    }

    /**
     * Convenience method returning the first (and only) sample converter in the specified
     * array, or a default converter if the specified array contains a null element.
     */
    private static SampleConverter first(final SampleConverter[] converters) {
        SampleConverter converter = converters[0];
        if (converter == null) {
            converter = SampleConverter.IDENTITY;
        }
        return converter;
    }

    /**
     * Returns statistics about the sample values in the specified image. This is for informative
     * purpose only and may be used when the {@linkplain #getImageMetadata metadata} do not provides
     * useful information about valid minimum and maximum values. Note that this method requires a
     * full scan of image data and may be slow.
     *
     * @param  imageIndex The index of the image to analyze.
     * @return Statistics on the sample values in the given image.
     * @throws IOException if an I/O error occured while reading the sample values.
     */
    public Statistics getStatistics(final int imageIndex) throws IOException {
        final double[] fillValues;
        final GeographicMetadata metadata = getGeographicMetadata(imageIndex);
        if (metadata != null && metadata.getNumBands() >= 1) {
            fillValues = metadata.getBand(0).getNoDataValues();
            // TODO: What should we do with other bands? For now we assume that
            //       every bands have the same fill values.
        } else {
            fillValues = null;
        }
        final Array      array = variable.read();
        final IndexIterator it = array.getIndexIterator();
        final Statistics stats = new Statistics();
        if (fillValues == null || fillValues.length == 0) {
            while (it.hasNext()) {
                stats.add(it.getDoubleNext());
            }
        } else if (fillValues.length == 1) {
            final double fillValue = fillValues[0];
            while (it.hasNext()) {
                final double value = it.getDoubleNext();
                if (value != fillValue) {
                    stats.add(value);
                }
            }
        } else {
scan:       while (it.hasNext()) {
                final double value = it.getDoubleNext();
                if (fillValues != null) {
                    for (int i=0; i<fillValues.length; i++) {
                        if (fillValues[i] == value) {
                            continue scan;
                        }
                    }
                }
                stats.add(value);
            }
        }
        return stats;
    }

    /**
     * Returns the image width.
     *
     * @throws IOException If an error occured while reading the NetCDF file.
     */
    public int getWidth(final int imageIndex) throws IOException {
        prepareVariable(imageIndex);
        return variable.getDimension(variable.getRank() - X_DIMENSION).getLength();
    }

    /**
     * Returns the image height.
     *
     * @throws IOException If an error occured while reading the NetCDF file.
     */
    public int getHeight(final int imageIndex) throws IOException {
        prepareVariable(imageIndex);
        return variable.getDimension(variable.getRank() - Y_DIMENSION).getLength();
    }

    /**
     * Ensures that metadata are loaded.
     */
    private void ensureMetadataLoaded() throws IOException {
        if (!metadataLoaded) {
            CoordSysBuilder.addCoordinateSystems(dataset, this);
            metadataLoaded = true;
        }
    }

    /**
     * Returns the metadata associated with the input source as a whole.
     *
     * @throws IOException If an error occured while reading the NetCDF file.
     */
    @Override
    public IIOMetadata getStreamMetadata() throws IOException {
        if (streamMetadata == null && !ignoreMetadata) {
            ensureFileOpen();
            ensureMetadataLoaded();
            streamMetadata = createMetadata(dataset);
        }
        return streamMetadata;
    }

    /**
     * Returns the metadata associated with the image at the specified index.
     *
     * @throws IOException If an error occured while reading the NetCDF file.
     */
    @Override
    public IIOMetadata getImageMetadata(final int imageIndex) throws IOException {
        if (imageMetadata == null && !ignoreMetadata) {
            prepareVariable(imageIndex);
            if (variable instanceof VariableDS) {
                ensureMetadataLoaded();
                imageMetadata = createMetadata((VariableDS) variable);
            }
        }
        return imageMetadata;
    }

    /**
     * Creates metadata for the specified NetCDF file. This method is invoked automatically
     * by {@link #getStreamMetadata} when first needed. The default implementation returns an
     * instance of {@link NetcdfMetadata}. Subclasses can override this method in order to create
     * a more specific set of metadata.
     *
     * @param  file The NetCDF dataset.
     * @return The metadata for the given dataset.
     * @throws IOException If an error occured while reading the NetCDF file.
     */
    protected IIOMetadata createMetadata(final NetcdfDataset file) throws IOException {
        return new NetcdfMetadata(this, file);
    }

    /**
     * Creates metadata for the specified NetCDF variable. This method is invoked automatically
     * by {@link #getImageMetadata} when first needed. The default implementation returns an
     * instance of {@link NetcdfMetadata}. Subclasses can override this method in order to create
     * a more specific set of metadata.
     *
     * @param  variable The NetCDF variable.
     * @return The metadata for the given variable.
     * @throws IOException If an error occured while reading the NetCDF file.
     */
    protected IIOMetadata createMetadata(final VariableDS variable) throws IOException {
        return new NetcdfMetadata(this, variable);
    }

    /**
     * Returns the data type which most closely represents the "raw" internal data of the image.
     *
     * @param  imageIndex The index of the image to be queried.
     * @return The data type, or {@link DataBuffer#TYPE_UNDEFINED} if unknown.
     * @throws IOException If an error occured while reading the NetCDF file.
     */
    @Override
    protected int getRawDataType(final int imageIndex) throws IOException {
        prepareVariable(imageIndex);
        return VariableMetadata.getRawDataType(variable);
    }

    /**
     * Returns the slice to select for the specified axis type.
     *
     * @return The axis type.
     * @return The indice as a value from 0 inclusive to {@link Dimension#getLength} exclusive.
     * @throws IOException If an error occured while reading the NetCDF file.
     */
    private int getSliceIndice(final ImageReadParam param, final int dimension) throws IOException {
        if (param instanceof NetcdfReadParam) {
            final NetcdfReadParam p = (NetcdfReadParam) param;
            if (p.hasNonDefaultIndices()) {
                final AxisType type = getAxisType(dimension);
                if (type != null) {
                    return p.getSliceIndice(type);
                }
            }
        }
        return NetcdfReadParam.DEFAULT_INDICE;
    }

    /**
     * Returns the axis type at the specified dimension. The {@link #prepareVariable} method
     * must be invoked prior this method (this is not verified).
     * <p>
     * This method is not public because it duplicates (in a different form) the informations
     * already provided in image metadata. We use it when we want only this specific information
     * without the the rest of metadata. In many cases this method will not be invoked at all,
     * thus avoiding the need to load metadata.
     *
     * @param  dimension The dimension.
     * @return The axis type, or {@code null} if unknown.
     * @throws IOException If an error occured while reading the NetCDF file.
     */
    private AxisType getAxisType(final int dimension) throws IOException {
        if (variable instanceof VariableDS) {
            ensureMetadataLoaded();
            final List sys = ((VariableDS) variable).getCoordinateSystems();
            if (sys != null) {
                final int count = sys.size();
                for (int i=0; i<count; i++) {
                    final CoordinateSystem cs = (CoordinateSystem) sys.get(i);
                    final List axes = cs.getCoordinateAxes();
                    if (axes != null && axes.size() > dimension) {
                        final CoordinateAxis axis = (CoordinateAxis) axes.get(dimension);
                        if (axis != null) {
                            return axis.getAxisType();
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Returns {@code true} if the specified variable is a dimension of an other variable.
     * Such dimensions will be excluded from the list returned by {@link #getVariables}.
     *
     * @param  candidate The variable to test.
     * @param  variables The list of variables.
     * @return {@code true} if the specified variable is a dimension of an other variable.
     */
    private static boolean isAxis(final VariableIF candidate, final List variables) {
        final String name = candidate.getName();
        final int size = variables.size();
        for (int i=0; i<size; i++) {
            final VariableIF var = (VariableIF) variables.get(i);
            if (var != candidate) {
                Dimension dim;
                for (int d=0; (dim=var.getDimension(d)) != null; d++) {
                    if (dim.getName().equals(name)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Ensures that the NetCDF file is open, but do not load any variable yet.
     * The {@linkplain #variable} will be read by {@link #prepareVariable} only.
     */
    private void ensureFileOpen() throws IOException {
        if (dataset == null) {
            /*
             * Clears the 'abort' flag here (instead of in 'read' method only) because
             * we pass this ImageReader instance to the NetCDF DataSet as a CancelTask.
             */
            lastError = null;
            clearAbortRequest();
            final String dodsURL = getInputDODS();
            if (dodsURL != null) {
                if (variableNames == null) {
                    final int s = dodsURL.indexOf('?');
                    if (s >= 0) {
                        variableNames = new String[] {
                            dodsURL.substring(s + 1)
                        };
                    }
                }
                dataset = new NetcdfDataset(new DODSNetcdfFile(dodsURL, this), false);
            } else {
                final File inputFile = getInputFile();
                dataset = NetcdfDataset.openDataset(inputFile.getPath(), false, this);
                if (dataset == null) {
                    throw new FileNotFoundException(Errors.format(
                            ErrorKeys.FILE_DOES_NOT_EXIST_$1, inputFile));
                }
            }
            if (variableNames == null) {
                /*
                 * Gets a list of every variables found in the NetcdfDataset and copies the names
                 * in a filtered list which exclude every variable that are dimension of an other
                 * variable. For example "longitude" may be a variable found in the NetcdfDataset,
                 * but is declared only because it is needed as a dimension for the "temperature"
                 * variable. The "longitude" variable is usually not of direct interest to the user
                 * (the interresting variable is "temperature"), so we exclude it.
                 */
                final List variables = dataset.getVariables();
                final String[] filtered = new String[variables.size()];
                int count = 0;
                for (int i=0; i<filtered.length; i++) {
                    final VariableIF candidate = (VariableIF) variables.get(i);
                    /*
                     * - Images require at least 2 dimensions. They may have more dimensions,
                     *   in which case a slice will be taken later.
                     *
                     * - Excludes characters, strings and structures, which can not be easily
                     *   mapped to an image type. In addition, 2-dimensional character arrays
                     *   are often used for annotations and we don't wan't to confuse them
                     *   with images.
                     *
                     * - Excludes axis. They are often already excluded by the first condition
                     *   because axis are usually 1-dimensional, but some are 2-dimensional,
                     *   e.g. a localization grid.
                     */
                    if (candidate.getRank()>=2 && VALID_TYPES.contains(candidate.getDataType()) &&
                            !isAxis(candidate, variables))
                    {
                        filtered[count++] = candidate.getName();
                    }
                }
                variableNames = XArray.resize(filtered, count);
            }
        }
    }

    /**
     * Ensures that data are loaded in the NetCDF {@linkplain #variable}. If data are already
     * loaded, then this method do nothing.
     * <p>
     * This method is invoked automatically before any operation that require the NetCDF
     * variable, including (but not limited to):
     * <ul>
     *   <li>{@link #getWidth}</li>
     *   <li>{@link #getHeight}</li>
     *   <li>{@link #getStatistics}</li>
     *   <li>{@link #getImageMetadata}</li>
     *   <li>{@link #getRawDataType}</li>
     *   <li>{@link #read(int,ImageReadParam)}</li>
     * </ul>
     *
     * @param  imageIndex The image index.
     * @return {@code true} if the {@linkplain #variable} changed as a result of this call,
     *         or {@code false} if the current value is already appropriate.
     * @throws IndexOutOfBoundsException if the specified index is outside the expected range.
     * @throws IllegalStateException If {@link #input} is not set.
     * @throws IOException If an error occured while reading the NetCDF file.
     */
    protected boolean prepareVariable(final int imageIndex) throws IOException {
        checkImageIndex(imageIndex);
        if (variable == null || variableIndex != imageIndex) {
            ensureFileOpen();
            final String name = variableNames[imageIndex];
            final Variable candidate = findVariable(name);
            final int rank = candidate.getRank();
            if (rank < Math.max(X_DIMENSION, Y_DIMENSION)) {
                throw new IIOException(Errors.format(ErrorKeys.NOT_TWO_DIMENSIONAL_$1, rank));
            }
            variable      = candidate;
            variableIndex = imageIndex;
            imageMetadata = null;
            return true;
        }
        return false;
    }

    /**
     * Returns the variable of the given name. This method is similar to
     * {@link NetcdfDataset#findVariable(String)} except that the search
     * is case-insensitive and an exception is thrown if no variable has
     * been found for the given name.
     *
     * @param  name The name of the variable to search.
     * @return The variable for the given name.
     * @throws IIOException if no variable has been found for the given name.
     * @throws IOException If an error occured while reading the NetCDF file.
     */
    protected Variable findVariable(final String name) throws IOException {
        ensureFileOpen();
        /*
         * First tries a case-sensitive search. Case matter since the same letter in different
         * case may represent different variables. For example "t" and "T" are typically "time"
         * and "temperature" respectively.
         */
        Variable candidate = dataset.findVariable(name);
        if (candidate != null) {
            return candidate;
        }
        /*
         * We tried a case-sensitive search without success. Now tries a case-insensitive search
         * before to report a failure.
         */
        final List<Variable> variables = dataset.getVariables();
        if (variables != null) {
            for (final Variable variable : variables) {
                if (variable!=null && name.equalsIgnoreCase(variable.getName())) {
                    return variable;
                }
            }
        }
        throw new IIOException(Errors.format(
                ErrorKeys.VARIABLE_NOT_FOUND_IN_FILE_$2, name, dataset.getLocation()));
    }

    /**
     * Returns parameters initialized with default values appropriate for this format.
     *
     * @return Parameters which may be used to control the decoding process using a set
     *         of default settings.
     */
    @Override
    public ImageReadParam getDefaultReadParam() {
        return new NetcdfReadParam(this);
    }

    /**
     * Creates an image from the specified parameters.
     *
     * @throws IOException If an error occured while reading the NetCDF file.
     */
    public BufferedImage read(final int imageIndex, final ImageReadParam param) throws IOException {
        clearAbortRequest();
        prepareVariable(imageIndex);
        /*
         * Fetchs the parameters that are not already processed by utility
         * methods like 'getDestination' or 'computeRegions' (invoked below).
         */
        final int strideX, strideY;
        final int[] srcBands, dstBands;
        if (param != null) {
            strideX  = param.getSourceXSubsampling();
            strideY  = param.getSourceYSubsampling();
            srcBands = param.getSourceBands();
            dstBands = param.getDestinationBands();
        } else {
            strideX  = 1;
            strideY  = 1;
            srcBands = null;
            dstBands = null;
        }
        final int rank = variable.getRank();
        int bandDimension = rank - Z_DIMENSION;
        if (false && param instanceof NetcdfReadParam) {
            final NetcdfReadParam p = (NetcdfReadParam) param;
            if (p.isBandDimensionSet() && variable instanceof VariableEnhanced) {
                ensureMetadataLoaded(); // Build the CoordinateSystems
                bandDimension = p.getBandDimension((VariableEnhanced) variable);
                final int relative = rank - bandDimension;
                if (relative < 0 || relative == X_DIMENSION || relative == Y_DIMENSION) {
                    throw new IllegalArgumentException(Errors.format(ErrorKeys.BAD_PARAMETER_$2,
                            "bandDimension", bandDimension));
                }
            }
        }
        /*
         * Gets the destination image of appropriate size. We create it now
         * since it is a convenient way to get the number of destination bands.
         */
        final int width  = variable.getDimension(rank - X_DIMENSION).getLength();
        final int height = variable.getDimension(rank - Y_DIMENSION).getLength();
        final SampleConverter[] converters = new SampleConverter[1];
        final BufferedImage  image  = getDestination(imageIndex, param, width, height, converters);
        final WritableRaster raster = image.getRaster();
        final SampleConverter converter = first(converters);
        /*
         * Checks the band setting. If the NetCDF file is at least 3D, the
         * data along the 'z' dimension are considered as different bands.
         */
        final boolean hasBands = (bandDimension >= 0 && bandDimension < rank);
        final int  numSrcBands = hasBands ? variable.getDimension(bandDimension).getLength() : 1;
        final int  numDstBands = raster.getNumBands();
        if (param != null) {
            // Do not test when 'param == null' since our default 'srcBands'
            // value is not the same than the one documented in Image I/O.
            checkReadParamBandSettings(param, numSrcBands, numDstBands);
        }
        /*
         * Computes the source region (in the NetCDF file) and the destination region
         * (in the buffered image). Copies those informations into UCAR Range structure.
         */
        final Rectangle  srcRegion = new Rectangle();
        final Rectangle destRegion = new Rectangle();
        computeRegions(param, width, height, image, srcRegion, destRegion);
        flipVertically(param, height, srcRegion);
        final Range[] ranges = new Range[rank];
        for (int i=0; i<ranges.length; i++) {
            final int first, length, stride;
            switch (rank - i) {
                case X_DIMENSION: {
                    first  = srcRegion.x;
                    length = srcRegion.width;
                    stride = strideX;
                    break;
                }
                case Y_DIMENSION: {
                    first  = srcRegion.y;
                    length = srcRegion.height;
                    stride = strideY;
                    break;
                }
                default: {
                    if (i == bandDimension) {
                        first = NetcdfReadParam.DEFAULT_INDICE;
                    } else {
                        first = getSliceIndice(param, i);
                    }
                    length = 1;
                    stride = 1;
                    break;
                }
            }
            try {
                ranges[i] = new Range(first, first+length-1, stride);
            } catch (InvalidRangeException e) {
                throw netcdfFailure(e);
            }
        }
        final List<Range> sections = Range.toList(ranges);
        /*
         * Reads the requested sub-region only.
         */
        processImageStarted(imageIndex);
        final float toPercent = 100f / numDstBands;
        final int type = raster.getSampleModel().getDataType();
        final int xmin = destRegion.x;
        final int ymin = destRegion.y;
        final int xmax = destRegion.width  + xmin;
        final int ymax = destRegion.height + ymin;
        for (int zi=0; zi<numDstBands; zi++) {
            final int srcBand = (srcBands == null) ? zi : srcBands[zi];
            final int dstBand = (dstBands == null) ? zi : dstBands[zi];
            final Array array;
            try {
                if (hasBands) {
                    ranges[bandDimension] = new Range(srcBand, srcBand, 1);
                    // No need to update 'sections' since it wraps directly the 'ranges' array.
                }
                array = variable.read(sections);
            } catch (InvalidRangeException e) {
                throw netcdfFailure(e);
            }
            final IndexIterator it = array.getIndexIterator();
            for (int y=ymax; --y>=ymin;) {
                for (int x=xmin; x<xmax; x++) {
                    switch (type) {
                        case DataBuffer.TYPE_DOUBLE: {
                            raster.setSample(x, y, dstBand, converter.convert(it.getDoubleNext()));
                            break;
                        }
                        case DataBuffer.TYPE_FLOAT: {
                            raster.setSample(x, y, dstBand, converter.convert(it.getFloatNext()));
                            break;
                        }
                        default: {
                            raster.setSample(x, y, dstBand, converter.convert(it.getIntNext()));
                            break;
                        }
                    }
                }
            }
            /*
             * Checks for abort requests after reading. It would be a waste of a potentially
             * good image (maybe the abort request occured after we just finished the reading)
             * if we didn't implemented the 'isCancel()' method. But because of the later, which
             * is checked by the NetCDF library, we can't assume that the image is complete.
             */
            if (abortRequested()) {
                processReadAborted();
                return image;
            }
            /*
             * Reports progress here, not in the deeper loop, because the costly part is the
             * call to 'variable.read(...)' which can't report progress.  The loop that copy
             * pixel values is fast, so reporting progress there would be pointless.
             */
            processImageProgress(zi * toPercent);
        }
        if (lastError != null) {
            throw new IIOException(lastError);
        }
        processImageComplete();
        return image;
    }

    /**
     * Wraps a generic exception into a {@link IIOException}.
     */
    private IIOException netcdfFailure(final Exception e) throws IOException {
        return new IIOException(Errors.format(ErrorKeys.CANT_READ_$1, dataset.getLocation()), e);
    }

    /**
     * Invoked by the NetCDF library during read operation in order to check if the task has
     * been canceled. Users should not invoke this method directly.
     *
     * @return {@code true} if abort has been requested.
     */
    public boolean isCancel() {
        return abortRequested();
    }

    /**
     * Invoked by the NetCDF library when an error occured during the read operation.
     * Users should not invoke this method directly.
     *
     * @param message An error message to report.
     */
    public void setError(final String message) {
        lastError = message;
    }

    /**
     * Closes the NetCDF file.
     *
     * @throws IOException If an error occured while accessing the NetCDF file.
     */
    @Override
    protected void close() throws IOException {
        metadataLoaded = false;
        streamMetadata = null;
        imageMetadata  = null;
        lastError      = null;
        variable       = null;
        if (dataset != null) {
            dataset.close();
            dataset = null;
        }
        super.close();
    }



    /**
     * The service provider for {@link NetcdfImageReader}.
     *
     * @version $Id$
     * @author Antoine Hnawia
     * @author Martin Desruisseaux
     */
    public static class Spi extends FileImageReader.Spi {
        /**
         * List of legal names for NetCDF readers.
         */
        private static final String[] NAMES = new String[] {"netcdf", "NetCDF"};

        /**
         * The mime types for the default {@link NetcdfImageReader} configuration.
         */
        private static final String[] MIME_TYPES = new String[] {"image/x-netcdf"};

        /**
         * Default list of file's extensions.
         */
        private static final String[] SUFFIXES = new String[] {"nc", "NC"};

        /**
         * Constructs a default {@code NetcdfImageReader.Spi}. This constructor
         * provides the following defaults in addition to the defaults defined
         * in the super-class constructor:
         *
         * <ul>
         *   <li>{@link #names}           = {@code "NetCDF"}</li>
         *   <li>{@link #MIMETypes}       = {@code "image/x-netcdf"}</li>
         *   <li>{@link #pluginClassName} = {@code "org.geotools.image.io.netcdf.NetcdfImageReader"}</li>
         *   <li>{@link #vendorName}      = {@code "Geotools"}</li>
         *   <li>{@link #suffixes}        = {{@code "nc"}, {@code "NC"}}</li>
         * </ul>
         *
         * For efficienty reasons, the above fields are initialized to shared arrays. Subclasses
         * can assign new arrays, but should not modify the default array content.
         */
        public Spi() {
            names           = NAMES;
            MIMETypes       = MIME_TYPES;
            suffixes        = SUFFIXES;
            pluginClassName = "org.geotools.image.io.netcdf.NetcdfImageReader";
            vendorName      = "Geotools";
            version         = "2.4";
        }

        /**
         * Returns a description for this provider.
         *
         * @todo Localize
         */
        public String getDescription(final Locale locale) {
            return "NetCDF image decoder";
        }

        /**
         * Checks if the specified input seems to be a readeable NetCDF file.
         * This method is only for indication purpose. Current implementation
         * conservatively returns {@code false}.
         *
         * @throws IOException If an error occured while reading the NetCDF file.
         * @todo Implements a more advanced check.
         */
        public boolean canDecodeInput(final Object source) throws IOException {
            return false;
        }

        /**
         * Constructs a NetCDF image reader.
         *
         * @throws IOException If an error occured while reading the NetCDF file.
         */
        public ImageReader createReaderInstance(final Object extension) throws IOException {
            return new NetcdfImageReader(this);
        }
    }
}
