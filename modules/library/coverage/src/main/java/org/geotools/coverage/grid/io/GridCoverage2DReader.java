/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.grid.io;

import it.geosolutions.imageio.maskband.DatasetLayout;
import java.awt.image.ColorModel;
import java.awt.image.SampleModel;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Set;
import javax.media.jai.ImageLayout;
import org.geotools.api.coverage.grid.Format;
import org.geotools.api.coverage.grid.GridCoverageReader;
import org.geotools.api.coverage.grid.GridEnvelope;
import org.geotools.api.data.ResourceInfo;
import org.geotools.api.data.ServiceInfo;
import org.geotools.api.parameter.GeneralParameterValue;
import org.geotools.api.parameter.ParameterDescriptor;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.datum.PixelInCell;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.geometry.GeneralBounds;

/**
 * Provides access to named GridCoverage2D (along with any context information) from a persistent store.
 *
 * <p>Data access method take a <code>coverageName</code> supplied by {@link GridCoverageReader#getGridCoverageNames()}
 * allowing each data GridCoverage2D to be accessed independently.
 *
 * <p>In addition static keywords for use with {@link GridCoverageReader#getMetadataNames(String coverageName)} have
 * been provided.
 *
 * @author Daniele Romagnoli, GeoSolutions SAS
 * @author Andrea Aime, GeoSolutions SAS
 * @author Simone Giannecchini, GeoSolutions SAS
 */
public interface GridCoverage2DReader extends GridCoverageReader {

    /** The time domain (comma separated list of values) */
    public static final String TIME_DOMAIN = "TIME_DOMAIN";

    /** Time domain resolution (when using min/max/resolution) */
    public static final String TIME_DOMAIN_RESOLUTION = "TIME_DOMAIN_RESOLUTION";

    /** If the time domain is available (or if a min/max/resolution approach has been chosen) */
    public static final String HAS_TIME_DOMAIN = "HAS_TIME_DOMAIN";

    /** The time domain max value */
    public static final String TIME_DOMAIN_MAXIMUM = "TIME_DOMAIN_MAXIMUM";

    /** The time domain min value */
    public static final String TIME_DOMAIN_MINIMUM = "TIME_DOMAIN_MINIMUM";

    /** Whether the elevation is expressed as a full domain or min/max/resolution (true if domain list available) */
    public static final String HAS_ELEVATION_DOMAIN = "HAS_ELEVATION_DOMAIN";

    /** Elevation domain (comma separated list of values) */
    public static final String ELEVATION_DOMAIN = "ELEVATION_DOMAIN";

    /** Elevation domain maximum value */
    public static final String ELEVATION_DOMAIN_MAXIMUM = "ELEVATION_DOMAIN_MAXIMUM";

    /** Elevation domain minimum value */
    public static final String ELEVATION_DOMAIN_MINIMUM = "ELEVATION_DOMAIN_MINIMUM";

    /** Elevation domain resolution */
    public static final String ELEVATION_DOMAIN_RESOLUTION = "ELEVATION_DOMAIN_RESOLUTION";

    /**
     * If a coverage has this property is means it been read straight out of a file without any sub-setting, it means
     * the coverage represents the full contents of the file. This can be used for different types of optimizations,
     * such as avoiding reading and re-encoding the original file when the original file would do.
     */
    public static final String FILE_SOURCE_PROPERTY = "OriginalFileSource";

    /**
     * Name of a {@link GridCoverage2D} property that is a {@link URL} for the original source of the coverage. This
     * {@link URL} might be used to obtain further information on the coverage that is not otherwise available through
     * the {@link GridCoverage2D} API.
     */
    public static String SOURCE_URL_PROPERTY = "SourceUrl";

    /**
     * This property is present, and evaluates to "true", if the reader can do reprojection on its own (that is, it is
     * not backed by actual data, but by a remote service that can perform reprojection for us).
     */
    public static final String REPROJECTING_READER = "ReprojectingReader";

    /**
     * This property is present, and evaluates to "true", if the reader internally has multiple CRS elements, even if it
     * advertises a single CRS in output (e.g., it won't perform a general reprojection for us, but generate and output
     * in its declared CRS)
     */
    public static final String MULTICRS_READER = "MultiCRSReader";

    /**
     * When the above MULTICRS_READER property is present and evaluates to "true", this property may contain the list of
     * internal EPSG Codes of the multiple CRS, if the reader implements it.
     */
    public static final String MULTICRS_EPSGCODES = "MultiCRSEPSGCodes";

    /**
     * The name of the property containing the eventual GDAL {@link it.geosolutions.imageio.pam.PAMDataset} for the
     * coverage at hand.
     */
    public static final String PAM_DATASET = "PamDataset";

    /**
     * Return the original {@link GeneralBounds} for the default coverage served by the underlying store.
     *
     * @return the original {@link GeneralBounds} for the default coverage served by the underlying store.
     */
    GeneralBounds getOriginalEnvelope();

    /**
     * Return the original {@link GeneralBounds} for the specified coverageName.
     *
     * @param coverageName the name of the coverage to work on.
     * @return the original {@link GeneralBounds} for the specified coverageName.
     * @throws NullPointerException if the specified coverageName is <code>null</code>
     * @throws IllegalArgumentException if the specified coverageName does not exist
     */
    GeneralBounds getOriginalEnvelope(String coverageName);

    /**
     * Retrieves the {@link CoordinateReferenceSystem} associated to the default coverage for this
     * {@link GridCoverage2DReader}.
     *
     * <p>
     *
     * @return the {@link CoordinateReferenceSystem} mapped to the specified coverageName, or {@code null} if the
     *     provided coverageName does not map to a real coverage.
     * @throws NullPointerException if the specified coverageName is <code>null</code>
     */
    CoordinateReferenceSystem getCoordinateReferenceSystem();

    /**
     * Retrieves the {@link CoordinateReferenceSystem} associated to this {@link GridCoverage2DReader} for the specified
     * coverageName.
     *
     * <p>
     *
     * @return the {@link CoordinateReferenceSystem} mapped to the specified coverageName
     * @throws NullPointerException if the specified coverageName is <code>null</code>
     * @throws IllegalArgumentException if the specified coverageName does not exist
     */
    CoordinateReferenceSystem getCoordinateReferenceSystem(String coverageName);

    /**
     * Retrieves the {@link GridEnvelope} associated to the default coverage for this {@link GridCoverage2DReader}.
     *
     * <p>The {@link GridEnvelope} describes the raster area (in pixels) covered by the coverage.
     *
     * <p>
     *
     * @return the {@link CoordinateReferenceSystem} mapped to the default coverageName
     */
    GridEnvelope getOriginalGridRange();

    /**
     * Retrieves the {@link GridEnvelope} associated to the specified coverageName for this
     * {@link GridCoverage2DReader}.
     *
     * <p>The {@link GridEnvelope} describes the raster area (in pixels) covered by the coverage.
     *
     * @param coverageName the name of the coverage to work with
     * @return the {@link GridEnvelope} mapped to the specified coverageName
     * @throws NullPointerException if the specified coverageName is <code>null</code>
     * @throws IllegalArgumentException if the specified coverageName does not exist
     */
    GridEnvelope getOriginalGridRange(String coverageName);

    /**
     * Retrieves the {@link MathTransform} associated to the default coverage for this {@link GridCoverage2DReader}.
     *
     * <p>
     *
     * @return the {@link CoordinateReferenceSystem} mapped to the default coverageName
     */
    MathTransform getOriginalGridToWorld(PixelInCell pixInCell);

    /**
     * Retrieves the {@link MathTransform} associated to the requested coverageName for this
     * {@link GridCoverage2DReader}.
     *
     * @param coverageName the name of the coverage to work with
     * @return the {@link MathTransform} mapped to the specified coverageName
     * @throws NullPointerException if the specified coverageName is <code>null</code>
     * @throws IllegalArgumentException if the specified coverageName does not exist
     */
    MathTransform getOriginalGridToWorld(String coverageName, PixelInCell pixInCell);

    /**
     * Created a {@link GridCoverage2D} out of this {@link GridCoverage2DReader} for the default coverage.
     *
     * @param parameters an array of {@link GeneralParameterValue} that uses a subset of the available read params for
     *     this {@link GridCoverage2DReader} as specified by the {@link Format}
     * @return a {@link GridCoverage2D} for the underlying default coverage for this {@link GridCoverage2DReader} or
     *     <code>null</code> in case no {@link GridCoverage2D} can be read for the provided parameters.
     * @throws IOException in case an error happen during read time.
     */
    @Override
    GridCoverage2D read(GeneralParameterValue... parameters) throws IOException;

    /**
     * Retrieves the {@link GridEnvelope} associated to the specified coverageName for this
     * {@link GridCoverage2DReader}.
     *
     * @param coverageName the name of the coverage to work with
     * @param parameters an array of {@link GeneralParameterValue} that uses a subset of the available read params for
     *     this {@link GridCoverage2DReader} as specified by the {@link Format}
     * @return a {@link GridCoverage2D} for the underlying default coverage for this {@link GridCoverage2DReader} or
     *     <code>null</code> in case no {@link GridCoverage2D} can be read for the provided parameters.
     * @throws NullPointerException if the specified coverageName is <code>null</code>
     * @throws IllegalArgumentException if the specified coverageName does not exist
     */
    @Override
    GridCoverage2D read(String coverageName, GeneralParameterValue... parameters) throws IOException;

    /**
     * Return the {@link Set} of dynamic read parameters supported by this {@link GridCoverage2DReader} for the default
     * coverage.
     *
     * @return the {@link Set} of dynamic read parameters supported by this {@link GridCoverage2DReader}.
     * @throws IOException in case an error occurs while creating the {@link Set} of dynamic parameters.
     */
    Set<ParameterDescriptor<List>> getDynamicParameters() throws IOException;

    /**
     * Return the {@link Set} of dynamic read parameters supported by this {@link GridCoverage2DReader} for the
     * specified coverage.
     *
     * @param coverageName the name of the coverage to work with
     * @return the {@link Set} of dynamic read parameters supported by this {@link GridCoverage2DReader}.
     * @throws IOException in case an error occurs while creating the {@link Set} of dynamic parameters.
     * @throws NullPointerException if the specified coverageName is <code>null</code>
     * @throws IllegalArgumentException if the specified coverageName does not exist
     */
    Set<ParameterDescriptor<List>> getDynamicParameters(String coverageName) throws IOException;

    /**
     * Return the resolution of the overview which would be picked out for the provided requested resolution using the
     * provided {@link OverviewPolicy}. This method works on the default coverage for this {@link GridCoverage2DReader}.
     *
     * @param policy the {@link OverviewPolicy} to use during evaluation.
     * @param requestedResolution the requested resolution
     * @return an array of 2 double with the resolution of the selected overview.
     * @throws IOException in case an error occurs.
     */
    double[] getReadingResolutions(OverviewPolicy policy, double[] requestedResolution) throws IOException;

    /**
     * Return the resolution of the overview which would be picked out for the provided requested resolution using the
     * provided {@link OverviewPolicy}. This method works on the specified coverage for this
     * {@link GridCoverage2DReader}.
     *
     * @param coverageName the name of the coverage to work on.
     * @param policy the {@link OverviewPolicy} to use during evaluation.
     * @param requestedResolution the requested resolution
     * @return an array of 2 double with the resolution of the selected overview.
     * @throws IOException in case an error occurs.
     * @throws NullPointerException if the specified coverageName is <code>null</code>
     * @throws IllegalArgumentException if the specified coverageName does not exist
     */
    double[] getReadingResolutions(String coverageName, OverviewPolicy policy, double[] requestedResolution)
            throws IOException;

    /**
     * Returns the {@link DatasetLayout} for the coverage.
     *
     * @return a {@link DatasetLayout} object containing info about Overview number and Image masks.
     */
    DatasetLayout getDatasetLayout();

    /**
     * Returns the {@link DatasetLayout} for the specified coverage.
     *
     * @param coverageName the name of the coverage for which we do want to get the {@link DatasetLayout}
     * @return a {@link DatasetLayout} object containing info about Overview number and Image masks.
     */
    DatasetLayout getDatasetLayout(String coverageName);

    /**
     * Retrieve the {@link ImageLayout} for the default coverage.
     *
     * <p>Throw an {@link IllegalArgumentException} in case the name is wrong and/or no such a coverage exists.
     *
     * @return an {@link ImageLayout} that is useful for actually knowing the {@link ColorModel}, the
     *     {@link SampleModel} as well as the tile grid for the default coverage.
     */
    ImageLayout getImageLayout() throws IOException;

    /**
     * Retrieve the {@link ImageLayout} for the specified coverage.
     *
     * <p>Throw an {@link IllegalArgumentException} in case the name is wrong and/or no such a coverage exists.
     *
     * @param coverageName the name of the coverage for which we want to know the {@link GridEnvelope}.
     * @return an {@link ImageLayout} that is useful for actually knowing the {@link ColorModel}, the
     *     {@link SampleModel} as well as the tile grid for a certain coverage.
     * @throws NullPointerException if the specified coverageName is <code>null</code>
     * @throws IllegalArgumentException if the specified coverageName does not exist
     */
    ImageLayout getImageLayout(String coverageName) throws IOException;

    /**
     * Retrieve the resolution levels for the default coverage.
     *
     * <p>Throw an {@link IllegalArgumentException} in case the name is wrong and/or no such a coverage exists.
     *
     * @return the resolution levels for the default coverage.
     * @throws NullPointerException if the specified coverageName is <code>null</code>
     * @throws IllegalArgumentException if the specified coverageName does not exist
     */
    double[][] getResolutionLevels() throws IOException;

    /**
     * Retrieve the resolution levels for the specified coverage.
     *
     * <p>Throw an {@link IllegalArgumentException} in case the name is wrong and/or no such a coverage exists.
     *
     * @param coverageName the name of the coverage for which we want to know the resolution levels.
     * @return the resolution levels for the specified coverage.
     * @throws NullPointerException if the specified coverageName is <code>null</code>
     * @throws IllegalArgumentException if the specified coverageName does not exist
     */
    double[][] getResolutionLevels(String coverageName) throws IOException;

    /**
     * Information about the store.
     *
     * @return ServiceInfo describing the whole store.
     */
    ServiceInfo getInfo();

    /**
     * Information about a specific resource.
     *
     * @return ResourceInfo describing a specific coverage.
     */
    ResourceInfo getInfo(String coverageName);
}
