/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015 - 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.imageio.netcdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.coverage.io.netcdf.crs.NetCDFProjection;
import org.geotools.coverage.io.netcdf.crs.ProjectionBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.imageio.netcdf.cv.CoordinateVariable;
import org.geotools.imageio.netcdf.utilities.NetCDFCRSUtilities;
import org.geotools.imageio.netcdf.utilities.NetCDFUtilities;
import org.geotools.util.Utilities;
import org.geotools.util.logging.Logging;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import ucar.nc2.Attribute;
import ucar.nc2.Variable;
import ucar.nc2.constants.AxisType;
import ucar.nc2.dataset.CoordinateAxis;
import ucar.nc2.dataset.NetcdfDataset;

/**
 * Store information about the underlying NetCDF georeferencing, such as Coordinate Variables (i.e.
 * Latitude, Longitude, Time variables, with values), Coordinate Reference Systems (i.e.
 * GridMappings), NetCDF dimensions to NetCDF coordinates relations.
 */
class NetCDFGeoreferenceManager {

    /** Base class for BBOX initialization and retrieval. */
    class BBoxGetter {

        /**
         * BoundingBoxes available for the underlying dataset. Most common case is that the whole
         * dataset has a single boundingbox/grid-mapping, resulting into a map made by a single
         * element. In that case, the only one envelope will be referred through the "DEFAULT" key
         */
        protected Map<String, ReferencedEnvelope> boundingBoxes;

        public BBoxGetter() throws FactoryException, IOException {
            boundingBoxes = new HashMap<String, ReferencedEnvelope>();
            init();
        }

        protected void init() throws FactoryException, IOException {
            double[] xLon = new double[2];
            double[] yLat = new double[2];
            byte set = 0;
            isLonLat = false;
            Map<String, Object> crsProperties = new HashMap<String, Object>();
            String axisUnit = null;
            // Scan over coordinateVariables
            for (CoordinateVariable<?> cv : getCoordinatesVariables()) {
                if (cv.isNumeric()) {

                    // is it lat or lon (or geoY or geoX)?
                    AxisType type = cv.getAxisType();
                    switch (type) {
                        case GeoY:
                        case Lat:
                            getCoordinate(cv, yLat);
                            if (yLat[1] > yLat[0]) {
                                setNeedsFlipping(true);
                            } else {
                                setNeedsFlipping(false);
                            }
                            set++;
                            break;
                        case GeoX:
                        case Lon:
                            getCoordinate(cv, xLon);
                            set++;
                            break;
                        default:
                            break;
                    }
                    switch (type) {
                        case Lat:
                        case Lon:
                            isLonLat = true;
                            break;
                        case GeoY:
                        case GeoX:
                            axisUnit = cv.getUnit();
                            break;
                        default:
                            break;
                    }
                }
                if (set == 2) {
                    break;
                }
            }

            if (set != 2) {
                throw new IllegalStateException("Unable to create envelope for this dataset");
            }

            // The previous code was able to automatically convert km coordinates to meter.
            // Let's check if we still want this automatically conversion occur or we
            // want the actual axisUnit to be used.
            if (!NetCDFCRSUtilities.isConvertAxisKm() && axisUnit != null) {
                // We assume that unit will be the same for Lon/GeoX
                crsProperties.put(ProjectionBuilder.AXIS_UNIT, axisUnit);
            }
            // create the envelope
            CoordinateReferenceSystem crs = NetCDFCRSUtilities.WGS84;
            // Looks for Projection definition
            if (!isLonLat) {
                // First, looks for a global crs (it may have been defined
                // as a NetCDF output write operation) to be used as fallback
                CoordinateReferenceSystem defaultCrs = NetCDFProjection.lookForDatasetCRS(dataset);

                // Then, look for a per variable CRS
                CoordinateReferenceSystem localCrs =
                        NetCDFProjection.lookForVariableCRS(dataset, defaultCrs, crsProperties);
                if (localCrs != null) {
                    // lookup for a custom EPSG if any
                    crs = NetCDFProjection.lookupForCustomEpsg(localCrs);
                }
            }
            ReferencedEnvelope boundingBox =
                    new ReferencedEnvelope(xLon[0], xLon[1], yLat[0], yLat[1], crs);
            boundingBoxes.put(NetCDFGeoreferenceManager.DEFAULT, boundingBox);
        }

        public void dispose() {
            if (boundingBoxes != null) {
                boundingBoxes.clear();
            }
        }

        public ReferencedEnvelope getBBox(String bboxName) {
            return boundingBoxes.get(bboxName);
        }
    }

    /**
     * BBoxGetter Implementation for multiple bounding boxes management. Use it for NetCDF datasets
     * defining multiple bounding boxes.
     */
    class MultipleBBoxGetter extends BBoxGetter {

        /**
         * Class delegated to retrieve and compute the coordinates from the available coordinate
         * variables.
         */
        class CoordinatesManager {

            // Map for longitude/easting coordinates
            private Map<String, double[]> xLonCoords = new HashMap<String, double[]>();

            // Map for latitude/northing coordinates
            private Map<String, double[]> yLatCoords = new HashMap<String, double[]>();

            /** Set latitude/northing coordinates */
            public void setYLat(CoordinateVariable<?> cv) throws IOException {
                double[] yLat = new double[2];
                getCoordinate(cv, yLat);
                if (yLat[1] > yLat[0]) {
                    setNeedsFlipping(true);
                } else {
                    setNeedsFlipping(false);
                }
                yLatCoords.put(cv.getName(), yLat);
            }

            /** Set longitude/easting coordinates */
            public void setXlon(CoordinateVariable<?> cv) throws IOException {
                double[] xLon = new double[2];
                getCoordinate(cv, xLon);
                xLonCoords.put(cv.getName(), xLon);
            }

            /**
             * Compute a {@link ReferencedEnvelope} instance, from the specified coordinate names
             */
            public ReferencedEnvelope computeEnvelope(
                    String coordinates, CoordinateReferenceSystem crs) {
                Utilities.ensureNonNull("coordinates", coordinates);
                String coords[] = coordinates.split(" ");
                double xLon[] = null;
                double yLat[] = null;
                // Get the previously computed coordinates
                for (String coord : coords) {
                    if (xLonCoords.containsKey(coord)) {
                        xLon = xLonCoords.get(coord);
                    } else if (yLatCoords.containsKey(coord)) {
                        yLat = yLatCoords.get(coord);
                    }
                }

                if (xLon == null || yLat == null) {
                    throw new IllegalArgumentException(
                            "Unable to initialize the boundingBox due to missing coordinates ");
                }
                return new ReferencedEnvelope(xLon[0], xLon[1], yLat[0], yLat[1], crs);
            }

            /**
             * Get the coordinates available for this variable. They are retrieved from the
             * {@linkplain NetCDFUtilities#COORDINATES} coordinates attribute. If missing, they will
             * be retrieved from the dimension names
             */
            public String getCoordinateNames(Variable var) {
                String coordinates = null;
                Attribute coordinatesAttribute = var.findAttribute(NetCDFUtilities.COORDINATES);
                boolean hasXLon = false;
                boolean hasYLat = false;
                if (coordinatesAttribute != null) {
                    // Look for coordinates attribute first
                    coordinates = coordinatesAttribute.getStringValue();
                } else if (!(var instanceof CoordinateAxis)) {
                    // fallback on dimensions to coordinates mapping
                    String dimensions = var.getDimensionsString();
                    if (dimensions != null && !dimensions.isEmpty()) {
                        coordinates = dimensions;
                    }
                }
                if (coordinates != null) {
                    for (String coord : coordinates.split(" ")) {
                        if (xLonCoords.containsKey(coord)) {
                            hasXLon = true;
                        } else if (yLatCoords.containsKey(coord)) {
                            hasYLat = true;
                        }
                    }
                }
                return (hasXLon && hasYLat) ? coordinates : null;
            }

            public void dispose() {
                // release resources.
                if (xLonCoords != null) {
                    xLonCoords.clear();
                    xLonCoords = null;
                }
                if (yLatCoords != null) {
                    yLatCoords.clear();
                    yLatCoords = null;
                }
            }
        }

        public MultipleBBoxGetter() throws FactoryException, IOException {
            super();
        }

        @Override
        protected void init() throws FactoryException, IOException {

            isLonLat = false;
            CoordinatesManager manager = new CoordinatesManager();
            for (CoordinateVariable<?> cv : getCoordinatesVariables()) {
                if (cv.isNumeric()) {
                    // is it lat or lon (or geoY or geoX)?
                    AxisType type = cv.getAxisType();
                    switch (type) {
                        case GeoY:
                        case Lat:
                            manager.setYLat(cv);
                            break;
                        case GeoX:
                        case Lon:
                            manager.setXlon(cv);
                            break;
                        default:
                            break;
                    }
                }
            }
            // create the envelope
            CoordinateReferenceSystem crs = NetCDFCRSUtilities.WGS84;

            // Looking for all coordinates pairs
            List<Variable> variables = dataset.getVariables();
            for (Variable var : variables) {
                Attribute gridMappingAttribute = var.findAttribute(NetCDFUtilities.GRID_MAPPING);
                String coordinates = manager.getCoordinateNames(var);
                if (coordinates != null && !boundingBoxes.containsKey(coordinates)) {
                    // Computing the bbox
                    crs = lookForCrs(crs, gridMappingAttribute, var);
                    ReferencedEnvelope boundingBox = manager.computeEnvelope(coordinates, crs);
                    boundingBoxes.put(coordinates, boundingBox);
                }
            }
            manager.dispose();
        }

        @Override
        public ReferencedEnvelope getBBox(String shortName) {
            // find auxiliary coordinateVariable
            String coordinates = getCoordinatesForVariable(shortName);
            if (coordinates != null) {
                return boundingBoxes.get(coordinates);
            }
            throw new IllegalArgumentException(
                    "Unable to find an envelope for the provided variable: " + shortName);
        }

        /** Look for a Coordinate Reference System */
        private CoordinateReferenceSystem lookForCrs(
                CoordinateReferenceSystem crs, Attribute gridMappingAttribute, Variable var)
                throws FactoryException {
            if (gridMappingAttribute != null) {
                String gridMappingName = gridMappingAttribute.getStringValue();
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("The variable refers to gridMapping: " + gridMappingName);
                }
                Variable mapping = dataset.findVariable(null, gridMappingName);
                if (mapping != null) {
                    CoordinateReferenceSystem localCrs = NetCDFProjection.parseProjection(mapping);
                    if (localCrs != null) {
                        // lookup for a custom EPSG if any
                        crs = NetCDFProjection.lookupForCustomEpsg(localCrs);
                    }
                } else if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.warning(
                            "The specified variable "
                                    + var.getFullName()
                                    + " declares a gridMapping = "
                                    + gridMappingName
                                    + " but that mapping doesn't exist.");
                }
            }
            return crs;
        }
    }

    private static final Logger LOGGER = Logging.getLogger(NetCDFGeoreferenceManager.class);

    /**
     * Set it to {@code true} in case the dataset has a single bbox. Set it to {@code false} in case
     * the dataset has multiple 2D coordinates definitions and bboxes. Used to quickly access the
     * bbox in case there is only a single one.
     */
    private boolean hasSingleBBox = true;

    /** The underlying BoundingBox getter instance */
    private BBoxGetter bbox;

    /**
     * Class mapping the NetCDF dimensions to related coordinate variables/axis names. Typical
     * example is a "time" coordinate variable containing the time instants for the "time"
     * dimensions.
     */
    class DimensionMapper {
        /**
         * Mapping containing the relation between a dimension and the related coordinate variable
         */
        private Map<String, String> dimensions;

        /** Return the whole dimension to coordinates mapping */
        public Map<String, String> getDimensions() {
            return dimensions;
        }

        /** Return the dimension names handled by this mapper */
        public Set<String> getDimensionNames() {
            return dimensions.keySet();
        }

        /** Return the dimension name associated to the specified coordinateVariable. */
        public String getDimension(String coordinateVariableName) {
            return dimensions.get(coordinateVariableName);
        }

        /** Mapper parsing the coordinateVariables. */
        public DimensionMapper(Map<String, CoordinateVariable<?>> coordinates) {
            // check other dimensions
            int coordinates2Dx = 0;
            int coordinates2Dy = 0;

            dimensions = new HashMap<String, String>();
            Set<String> coordinateKeys = new TreeSet<String>(coordinates.keySet());
            for (String key : coordinateKeys) {
                // get from coordinate vars
                final CoordinateVariable<?> cv = getCoordinateVariable(key);
                if (cv != null) {
                    final String name = cv.getName();
                    AxisType axisType = cv.getAxisType();
                    switch (axisType) {
                        case GeoX:
                        case Lon:
                            coordinates2Dx++;
                            continue;
                        case GeoY:
                        case Lat:
                            coordinates2Dy++;
                            continue;
                        case Height:
                        case Pressure:
                        case RadialElevation:
                        case RadialDistance:
                        case GeoZ:
                            if (NetCDFCRSUtilities.VERTICAL_AXIS_NAMES.contains(name)
                                    && !dimensions.containsKey(NetCDFUtilities.ELEVATION_DIM)) {
                                // Main elevation dimension
                                dimensions.put(NetCDFUtilities.ELEVATION_DIM, name);
                            } else {
                                // additional elevation dimension
                                dimensions.put(name.toUpperCase(), name);
                            }
                            break;
                        case Time:
                            if (!dimensions.containsKey(NetCDFUtilities.TIME_DIM)) {
                                // Main time dimension
                                dimensions.put(NetCDFUtilities.TIME_DIM, name);
                            } else {
                                // additional time dimension
                                dimensions.put(name.toUpperCase(), name);
                            }
                            break;
                        default:
                            // additional dimension
                            dimensions.put(name.toUpperCase(), name);
                            break;
                    }
                } else {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine(
                                "Null coordinate variable: '"
                                        + key
                                        + "' while processing input: "
                                        + dataset.getLocation());
                    }
                }
            }
            if (coordinates2Dx + coordinates2Dy > 2) {
                if (coordinates2Dx != coordinates2Dy) {
                    throw new IllegalArgumentException(
                            "number of x/lon coordinates must match number of y/lat coordinates");
                }
                // More than 2D coordinates have been found, as an instance lon1, lat1, lon2, lat2
                // Report that by unsetting the singleBbox flag.
                setHasSingleBBox(false);
            }
        }

        /** Update the dimensions mapping */
        public void remap(String dimension, String name) {
            dimensions.put(dimension, name);
        }
    }

    /** Map containing coordinates being wrapped by variables */
    private Map<String, CoordinateVariable<?>> coordinatesVariables;

    static final String DEFAULT = "Default";

    /** Flag reporting if the input file needs flipping or not */
    private boolean needsFlipping = false;

    /** The underlying NetCDF dataset */
    private NetcdfDataset dataset;

    /** The DimensionMapper instance */
    private DimensionMapper dimensionMapper;

    /** Flag reporting if the dataset is lonLat to avoid projections checks */
    private boolean isLonLat;

    public boolean isNeedsFlipping() {
        return needsFlipping;
    }

    public void setNeedsFlipping(boolean needsFlipping) {
        this.needsFlipping = needsFlipping;
    }

    public boolean isLonLat() {
        return isLonLat;
    }

    public boolean isHasSingleBBox() {
        return hasSingleBBox;
    }

    public void setHasSingleBBox(boolean hasSingleBBox) {
        this.hasSingleBBox = hasSingleBBox;
    }

    public CoordinateVariable<?> getCoordinateVariable(String name) {
        return coordinatesVariables.get(name);
    }

    public Collection<CoordinateVariable<?>> getCoordinatesVariables() {
        return coordinatesVariables.values();
    }

    public void dispose() {
        if (coordinatesVariables != null) {
            coordinatesVariables.clear();
        }
        bbox.dispose();
    }

    public ReferencedEnvelope getBoundingBox(String shortName) {
        return bbox.getBBox(hasSingleBBox ? DEFAULT : shortName);
    }

    public CoordinateReferenceSystem getCoordinateReferenceSystem(String shortName) {
        ReferencedEnvelope envelope = getBoundingBox(shortName);
        if (envelope != null) {
            return envelope.getCoordinateReferenceSystem();
        }
        throw new IllegalArgumentException(
                "Unable to find a CRS for the provided variable: " + shortName);
    }

    private String getCoordinatesForVariable(String shortName) {
        Variable var = dataset.findVariable(null, shortName);
        if (var != null) {
            // Getting the coordinates attribute
            Attribute attribute = var.findAttribute(NetCDFUtilities.COORDINATES);
            if (attribute != null) {
                return attribute.getStringValue();
            } else {
                return var.getDimensionsString();
            }
        }
        return null;
    }

    public Collection<CoordinateVariable<?>> getCoordinatesVariables(String shortName) {
        if (hasSingleBBox) {
            return coordinatesVariables.values();
        } else {
            String coordinates = getCoordinatesForVariable(shortName);
            String coords[] = coordinates.split(" ");
            List<CoordinateVariable<?>> coordVar = new ArrayList<CoordinateVariable<?>>();
            for (String coord : coords) {
                coordVar.add(coordinatesVariables.get(coord));
            }
            return coordVar;
        }
    }

    public DimensionMapper getDimensionMapper() {
        return dimensionMapper;
    }

    /**
     * Main constructor to setup the NetCDF Georeferencing based on the available information stored
     * within the NetCDF dataset.
     */
    public NetCDFGeoreferenceManager(NetcdfDataset dataset) {
        this.dataset = dataset;
        initCoordinates();
        try {
            initBBox();
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        } catch (FactoryException fe) {
            throw new RuntimeException(fe);
        }
    }

    /**
     * Parse the CoordinateAxes of the dataset and setup proper {@link CoordinateVariable} instances
     * on top of it and proper mapping between NetCDF dimensions and related coordinate variables.
     */
    private void initCoordinates() {
        // get the coordinate variables
        Map<String, CoordinateVariable<?>> coordinates =
                new HashMap<String, CoordinateVariable<?>>();
        Set<String> unsupported = NetCDFUtilities.getUnsupportedDimensions();
        Set<String> ignored = NetCDFUtilities.getIgnoredDimensions();
        for (CoordinateAxis axis : dataset.getCoordinateAxes()) {
            final int axisDimensions = axis.getDimensions().size();
            if (axisDimensions > 0 && axisDimensions < 3) {
                String axisName = axis.getFullName();
                if (axis.getAxisType() != null) {
                    coordinates.put(axisName, CoordinateVariable.create(axis));
                } else {
                    // Workaround for Unsupported Axes
                    if (unsupported.contains(axisName)) {
                        axis.setAxisType(AxisType.GeoZ);
                        coordinates.put(axisName, CoordinateVariable.create(axis));
                        // Workaround for files that have a time dimension, but in a format that
                        // could not be parsed
                    } else if ("time".equals(axisName)) {
                        if (LOGGER.isLoggable(Level.FINE)) {
                            LOGGER.fine(
                                    "Detected unparseable unit string in time axis: '"
                                            + axis.getUnitsString()
                                            + "'.");
                        }
                        axis.setAxisType(AxisType.Time);
                        coordinates.put(axisName, CoordinateVariable.create(axis));
                    } else if (!ignored.contains(axisName)) {
                        LOGGER.warning(
                                "Unsupported axis: "
                                        + axis
                                        + " in input: "
                                        + dataset.getLocation()
                                        + " has been found");
                    }
                }
            }
        }
        coordinatesVariables = coordinates;
        dimensionMapper = new DimensionMapper(coordinates);
    }

    /** Initialize the bbox getter */
    private void initBBox() throws IOException, FactoryException {
        bbox = hasSingleBBox ? new BBoxGetter() : new MultipleBBoxGetter();
    }

    /**
     * Get the bounding box coordinates from the provided coordinateVariable. The resulting
     * coordinates will be stored in the provided array. The convention is that the stored
     * coordinates represent the center of the cell so we apply a half pixel offset to go to the
     * corner.
     */
    private void getCoordinate(CoordinateVariable<?> cv, double[] coordinate) throws IOException {
        if (cv.isRegular()) {
            coordinate[0] = cv.getStart() - (cv.getIncrement() / 2d);
            coordinate[1] = coordinate[0] + cv.getIncrement() * (cv.getSize());
        } else {
            double min = ((Number) cv.getMinimum()).doubleValue();
            double max = ((Number) cv.getMaximum()).doubleValue();
            double incr = (max - min) / (cv.getSize() - 1);
            coordinate[0] = min - (incr / 2d);
            coordinate[1] = max + (incr / 2d);
        }
    }
}
