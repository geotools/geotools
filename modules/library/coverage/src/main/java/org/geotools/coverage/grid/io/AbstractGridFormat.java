/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003 - 2016, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.Color;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageWriteParam;
import javax.media.jai.Interpolation;
import javax.media.jai.InterpolationNearest;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.footprint.FootprintBehavior;
import org.geotools.coverage.grid.io.imageio.GeoToolsWriteParams;
import org.geotools.parameter.DefaultParameterDescriptor;
import org.geotools.referencing.factory.epsg.CartesianAuthorityFactory;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.coverage.grid.GridCoverageWriter;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.util.ProgressListener;

/**
 * AbstractGridFormat is a convenience class so subclasses only need to populate a Map class and set
 * the read and write parameter fields.
 *
 * <p>For example the ArcGridFormat has the following method which sets up all the required
 * information: <code>private void setInfo(){ HashMap info=new
 * HashMap(); info.put("name", "ArcGrid"); info.put("description", "Arc Grid
 * Coverage Format"); info.put("vendor", "Geotools"); info.put("docURL",
 * "http://gdal.velocet.ca/projects/aigrid/index.html"); info.put("version",
 * "1.0");  mInfo=info;  readParameters=new GeneralParameterDescriptor[2];
 * readParameters[0]=ArcGridOperationParameter.getGRASSReadParam();
 * readParameters[0]=ArcGridOperationParameter.getCompressReadParam();
 * writeParameters=new GeneralParameterDescriptor[2];
 * writeParameters[0]=ArcGridOperationParameter.getGRASSWriteParam();
 * writeParameters[0]=ArcGridOperationParameter.getCompressWriteParam();
 * }</code>
 *
 * @author jeichar
 * @author Simone Giannecchini, GeoSolutions
 */
public abstract class AbstractGridFormat implements Format {

    /**
     * The Map object is used by the information methods(such as getName()) as a data source. The
     * keys in the Map object (for the associated method) are as follows: getName() key = "name"
     * value type=String getDescription() key = "description" value type=String getVendor() key =
     * "vendor" value type=String getDocURL() key = "docURL" value type=String getVersion() key =
     * "version" value type=String Naturally, any methods that are overridden need not have an entry
     * in the Map
     */
    protected Map<String, String> mInfo;

    /**
     * {@link ParameterValueGroup} that controls the reading process for a {@link
     * GridCoverageReader} through the {@link
     * GridCoverageReader#read(org.opengis.parameter.GeneralParameterValue[])} method.
     */
    protected ParameterValueGroup readParameters;

    /**
     * {@link ParameterValueGroup} that controls the writing process for a {@link
     * GridCoverageWriter} through the {@link
     * GridCoverageWriter#write(org.opengis.coverage.grid.GridCoverage,
     * org.opengis.parameter.GeneralParameterValue[])} method.
     */
    protected ParameterValueGroup writeParameters;

    /** Default {@link CoordinateReferenceSystem} used by all the plugins. */
    private static CoordinateReferenceSystem crs = CartesianAuthorityFactory.GENERIC_2D;

    /**
     * This {@link GeneralParameterValue} can be provided to the {@link GridCoverageReader}s through
     * the {@code GridCoverageReader#read(GeneralParameterValue[])} method in order to pick up the
     * best matching resolution level and (soon) the best matching area.
     */
    public static final DefaultParameterDescriptor<GridGeometry2D> READ_GRIDGEOMETRY2D =
            new DefaultParameterDescriptor<GridGeometry2D>(
                    "ReadGridGeometry2D", GridGeometry2D.class, null, null);

    /**
     * This {@link GeneralParameterValue} can be provided to the {@link GridCoverageReader}s through
     * the {@code GridCoverageReader#write(GeneralParameterValue[])} method in order to monitor a
     * writing process
     */
    public static final DefaultParameterDescriptor<ProgressListener> PROGRESS_LISTENER =
            new DefaultParameterDescriptor<ProgressListener>(
                    "Listener", ProgressListener.class, null, null);

    /**
     * This {@link GeneralParameterValue} can be provided to the {@link GridCoverageWriter}s through
     * the {@code GridCoverageWriter#write(org.opengis.coverage.grid.GridCoverage,
     * GeneralParameterValue[])} method in order to control the writing process in terms of
     * compression, tiling, etc.GridGeometry2D
     */
    public static final DefaultParameterDescriptor<GeoToolsWriteParams> GEOTOOLS_WRITE_PARAMS =
            new DefaultParameterDescriptor<GeoToolsWriteParams>(
                    "WriteParameters", GeoToolsWriteParams.class, null, null);

    /**
     * This {@code GeneralParameterValue} can be provided to the {@link GridCoverageReader}s through
     * the {@link GridCoverageReader#read(GeneralParameterValue[])} method in order to specify the
     * type of image read operation requested: using a JAI ImageRead operation (leveraging on
     * Deferred Execution Model, Tile Caching,...), or the direct {@code ImageReader}'s read
     * methods.
     */
    public static final DefaultParameterDescriptor<Boolean> USE_JAI_IMAGEREAD =
            new DefaultParameterDescriptor<Boolean>(
                    Hints.USE_JAI_IMAGEREAD.toString(),
                    Boolean.class,
                    new Boolean[] {Boolean.TRUE, Boolean.FALSE},
                    Boolean.TRUE);

    /**
     * This {@link GeneralParameterValue} can be provided to the {@link GridCoverageReader}s through
     * the {@code GridCoverageReader#read(GeneralParameterValue[])} method in order to specify the
     * policy a reader should adopt when choosing the right overview during a read operation.
     */
    public static final DefaultParameterDescriptor<OverviewPolicy> OVERVIEW_POLICY =
            new DefaultParameterDescriptor<OverviewPolicy>(
                    Hints.OVERVIEW_POLICY.toString(),
                    OverviewPolicy.class,
                    new OverviewPolicy[] {
                        OverviewPolicy.IGNORE,
                        OverviewPolicy.NEAREST,
                        OverviewPolicy.QUALITY,
                        OverviewPolicy.SPEED
                    },
                    OverviewPolicy.QUALITY);

    /**
     * This {@link GeneralParameterValue} can be provided to the {@link GridCoverageReader}s through
     * the {@code GridCoverageReader#read(GeneralParameterValue[])} method in order to specify the
     * policy a reader should adopt when setting read parameters when evaluating a needed
     * resolution.
     */
    public static final ParameterDescriptor<DecimationPolicy> DECIMATION_POLICY =
            new DefaultParameterDescriptor<DecimationPolicy>(
                    Hints.DECIMATION_POLICY.toString(),
                    DecimationPolicy.class,
                    new DecimationPolicy[] {DecimationPolicy.ALLOW, DecimationPolicy.DISALLOW},
                    DecimationPolicy.ALLOW);

    /** The {@code String} representing the parameter to customize tile sizes */
    private static final String SUGGESTED_TILESIZE = "SUGGESTED_TILE_SIZE";

    /**
     * This {@link GeneralParameterValue} can be provided to the {@link GridCoverageReader}s through
     * the {@code GridCoverageReader#read(GeneralParameterValue[])} method in order to specify the
     * suggested size of tiles to avoid long time reading occurring with JAI ImageRead on striped
     * images. (Images with tiles Nx1) Value should be a String in the form of "W,H" (without
     * quotes) where W is a number representing the suggested tileWidth and H is a number
     * representing the suggested tileHeight.
     */
    public static final DefaultParameterDescriptor<String> SUGGESTED_TILE_SIZE =
            new DefaultParameterDescriptor<String>(
                    SUGGESTED_TILESIZE, String.class, null, "512,512");

    /**
     * This {@link GeneralParameterValue} can be provided to the {@link GridCoverageReader}s through
     * the {@code GridCoverageReader#read(GeneralParameterValue[])} method to specify the band
     * indices of the input grid coverage that are going to be in the resulting coverage. The order
     * of the bands on the output coverage is the order of the indices in the parameter. Value
     * should be an integer array (int[]) containing the band indices in the desired order.
     * Duplicate or multiple appearances of the same band index are allowed.
     */
    public static final DefaultParameterDescriptor<int[]> BANDS =
            new DefaultParameterDescriptor<int[]>("Bands", int[].class, null, null);

    public static final String TILE_SIZE_SEPARATOR = ",";

    /** Control the transparency of the input coverages. */
    public static final ParameterDescriptor<Color> INPUT_TRANSPARENT_COLOR =
            new DefaultParameterDescriptor<Color>("InputTransparentColor", Color.class, null, null);

    /** Control the background color to be used where the input was transparent */
    public static final ParameterDescriptor<Color> BACKGROUND_COLOR =
            new DefaultParameterDescriptor<Color>("BackgroundColor", Color.class, null, null);

    /** Optional Time value for this mosaic. */
    public static final ParameterDescriptor<List> TIME =
            DefaultParameterDescriptor.create(
                    "TIME", "A list of time objects", List.class, null, false);

    /** Optional Elevation value for this mosaic. */
    public static final ParameterDescriptor<List> ELEVATION =
            DefaultParameterDescriptor.create(
                    "ELEVATION", "An elevation value", List.class, null, false);

    static final Interpolation DEFAULT_INTERPOLATION = new InterpolationNearest();

    /**
     * Control the interpolation to be used in the eventual image processing done while reading data
     */
    public static final ParameterDescriptor<Interpolation> INTERPOLATION =
            new DefaultParameterDescriptor<Interpolation>(
                    "Interpolation", Interpolation.class, null, DEFAULT_INTERPOLATION);

    /** Control the footprint management. */
    public static final ParameterDescriptor<String> FOOTPRINT_BEHAVIOR =
            new DefaultParameterDescriptor<String>(
                    "FootprintBehavior",
                    String.class,
                    FootprintBehavior.valuesAsStrings(),
                    FootprintBehavior.None.name());

    /** @see org.opengis.coverage.grid.Format#getName() */
    public String getName() {
        return mInfo.get("name");
    }

    /** @see org.opengis.coverage.grid.Format#getDescription() */
    public String getDescription() {
        return mInfo.get("description");
    }

    /** @see org.opengis.coverage.grid.Format#getVendor() */
    public String getVendor() {
        return mInfo.get("vendor");
    }

    /** @see org.opengis.coverage.grid.Format#getDocURL() */
    public String getDocURL() {
        return mInfo.get("docURL");
    }

    /** @see org.opengis.coverage.grid.Format#getVersion() */
    public String getVersion() {
        return mInfo.get("version");
    }

    /**
     * Gets a {@link GridCoverageReader} for this format able to create coverages out of the <code>
     * source</code> object.
     *
     * <p>In case this {@link Format} cannot reader the provided <code>source</code> object <code>
     * null</code> is returned.
     *
     * @param source The source object to parse.
     * @return A reader for this {@link Format} or null.
     */
    public abstract AbstractGridCoverage2DReader getReader(Object source);

    /**
     * Gets a {@link GridCoverageReader} for this format able to create coverages out of the <code>
     * source</code> object using the provided <code>hints</code>.
     *
     * <p>In case this {@link Format} cannot reader the provided <code>source</code> object <code>
     * null</code> is returned.
     *
     * @param source The source object to parse. *
     * @param hints The {@link Hints} to use when trying to instantiate this reader.
     * @return A reader for this {@link Format} or null.
     */
    public abstract AbstractGridCoverage2DReader getReader(Object source, Hints hints);

    /**
     * Retrieves a {@link GridCoverageWriter} suitable for writing to the provided <code>destination
     * </code> with this format.
     *
     * <p>In case no writers are available <code>null</code> is returned.
     *
     * @param destination The destinatin where to write.
     * @return A {@link GridCoverageWriter} suitable for writing to the provided <code>destination
     *     </code> with this format.
     */
    public abstract GridCoverageWriter getWriter(Object destination);

    /**
     * Tells me if this {@link Format} can read the provided <code>input</code>.
     *
     * @param input The input object to test for suitability.
     * @return True if this format can read this object, False otherwise.
     */
    public boolean accepts(Object source) {
        return accepts(source, GeoTools.getDefaultHints());
    }

    /**
     * Tells me if this {@link Format} can read the provided <code>input</code>.
     *
     * @param input The input object to test for suitability.
     * @param hints {@link Hints} to control the accepts internal machinery.
     * @return True if this format can read this object, False otherwise.
     */
    public abstract boolean accepts(Object source, Hints hints);

    /**
     * @see org.geotools.data.coverage.grid.Format#equals(org.geotools.data.coverage.grid.Format)
     */
    public boolean equals(Format f) {
        if (f.getClass() == getClass()) {
            return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.opengis.coverage.grid.Format#getReadParameters()
     */
    public ParameterValueGroup getReadParameters() {
        if (this.readParameters == null)
            throw new UnsupportedOperationException(
                    "This format does not support usage of read parameters.");
        return this.readParameters.clone();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.opengis.coverage.grid.Format#getWriteParameters()
     */
    public ParameterValueGroup getWriteParameters() {
        if (this.writeParameters == null)
            throw new UnsupportedOperationException(
                    "This format does not support usage of write parameters.");
        return this.writeParameters.clone();
    }

    /**
     * getDefaultCRS
     *
     * <p>This method provides the user with a default crs WGS84
     */
    public static CoordinateReferenceSystem getDefaultCRS() {
        return crs;
    }

    /**
     * Returns an instance of {@link ImageWriteParam} that can be used to control a subsequent
     * {@link GridCoverageWriter#write(org.opengis.coverage.grid.GridCoverage,
     * org.opengis.parameter.GeneralParameterValue[])};
     *
     * <p>Be careful with using the {@link ImageWriteParam} since their usage is still experimental.
     *
     * @return an instance of {@link ImageWriteParam}.
     */
    public abstract GeoToolsWriteParams getDefaultImageIOWriteParameters();

    /**
     * Call the accepts() method before asking for a writer to determine if the current object is
     * supported.
     *
     * @param destination the destination object to write a WorldImage to
     * @param hints {@link Hints} to control the internal machinery.
     * @return a new WorldImageWriter for the destination
     */
    public abstract GridCoverageWriter getWriter(Object destination, Hints hints);
}
