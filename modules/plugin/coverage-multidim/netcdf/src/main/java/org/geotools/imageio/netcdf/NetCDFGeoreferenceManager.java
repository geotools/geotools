/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.coverage.io.netcdf.crs.NetCDFProjection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.imageio.netcdf.cv.CoordinateVariable;
import org.geotools.imageio.netcdf.utilities.NetCDFCRSUtilities;
import org.geotools.imageio.netcdf.utilities.NetCDFUtilities;
import org.geotools.util.logging.Logging;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import ucar.nc2.constants.AxisType;
import ucar.nc2.dataset.CoordinateAxis;
import ucar.nc2.dataset.CoordinateAxis1D;
import ucar.nc2.dataset.NetcdfDataset;

/**
 * Store information about the underlying NetCDF georeferencing.
 */
class NetCDFGeoreferenceManager {

    private final static Logger LOGGER = Logging.getLogger(NetCDFGeoreferenceManager.class
            .toString());

    /**
     * Set it to {@code true} in case the dataset contains multiple 2D coordinates definitions. 
     * Used to quickly access the bbox in case there is only a single one.
     */
    private boolean hasMultiple2Dcoords;

    /** Mapping containing the relation between a dimension and the related coordinate variable */
    private Map<String, String> dimensionsMapping;

    /** Map containing coordinates being wrapped by variables */
    private Map<String, CoordinateVariable<?>> coordinatesVariables;

    /**
     * BoundingBoxes available for the underlying dataset. 
     * Most common case is that all the dataset has a single boundingbox/grid/mapping. 
     * This will be signaled by the {@link #hasMultiple2Dcoords} flag equal to {@code false}. 
     * In that case, the map will only contain a single bbox which mapped to the "DEFAULT" name.
     * Other cases still need to be implemented.
     */
    private Map<String, ReferencedEnvelope> boundingBoxes = new HashMap<String, ReferencedEnvelope>();

    final static String DEFAULT = "Default";

    /** Boolean indicating if the input file needs flipping or not */
    private boolean needsFlipping = false;

    /** The underlying NetCDF dataset */
    private NetcdfDataset dataset;

    /** Flags telling whether the dataset is lonLat to avoid projections checks */
    private boolean isLonLat;

    public boolean isNeedsFlipping() {
        return needsFlipping;
    }

    public void setNeedsFlipping(boolean needsFlipping) {
        this.needsFlipping = needsFlipping;
    }

    public boolean isHasMultiple2Dcoords() {
        return hasMultiple2Dcoords;
    }

    public boolean isLonLat() {
        return isLonLat;
    }

    public void setHasMultiple2Dcoords(boolean hasMultiple2Dcoords) {
        this.hasMultiple2Dcoords = hasMultiple2Dcoords;
    }

    public CoordinateVariable<?> getCoordinateVariable(String name) {
        return coordinatesVariables.get(name);
    }

    public Collection<CoordinateVariable<?>> getCoordinatesVariables() {
        return coordinatesVariables.values();
    }

    public void addBoundingBox(String mapName, ReferencedEnvelope boundingBox) {
        boundingBoxes.put(mapName, boundingBox);
    }

    public void dispose() {
        if (coordinatesVariables != null) {
            coordinatesVariables.clear();
        }
        if (boundingBoxes != null) {
            boundingBoxes.clear();
        }
    }

    public ReferencedEnvelope getBoundingBox(String shortName) {
        if (!hasMultiple2Dcoords) {
            return boundingBoxes.get(DEFAULT);
        }
        throw new UnsupportedOperationException(
                "Multiple 2D georeferencing within same datasets still need to be supported");
    }

    public CoordinateReferenceSystem getCoordinateReferenceSystem(String shortName) {
        if (!hasMultiple2Dcoords) {
            return boundingBoxes.get(DEFAULT).getCoordinateReferenceSystem();
        }
        throw new UnsupportedOperationException(
                "Multiple 2D georeferencing within same datasets still need to be supported");
    }

    public Collection<CoordinateVariable<?>> getCoordinatesVariables(String shortName) {
        if (!hasMultiple2Dcoords) {
            return coordinatesVariables.values();
        }
        throw new UnsupportedOperationException(
                "Multiple 2D georeferencing within same datasets still need to be supported");
    }

    /** 
     * Main constructor to setup the NetCDF Georeferencing based on the available
     * information stored within the NetCDF dataset. 
     * */
    public NetCDFGeoreferenceManager(NetcdfDataset dataset) {
        this.dataset = dataset;
        extractCoordinatesVariable();
        try {
            extractBBOX();
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        } catch (FactoryException fe) {
            throw new RuntimeException(fe);
        }
    }

    /**
     * Parse the CoordinateAxes of the dataset and setup proper {@link CoordinateVariable} instances
     * on top of it.
     */
    private void extractCoordinatesVariable() {
        // get the coordinate variables
        Map<String, CoordinateVariable<?>> coordinates = new HashMap<String, CoordinateVariable<?>>();
        for (CoordinateAxis axis : dataset.getCoordinateAxes()) {
            if (axis instanceof CoordinateAxis1D && axis.getAxisType() != null) {
                coordinates.put(axis.getFullName(), CoordinateVariable.create((CoordinateAxis1D)axis));
            } else {
                // Workaround for Unsupported Axes
                Set<String> unsupported = NetCDFUtilities.getUnsupportedDimensions();
                if (axis instanceof CoordinateAxis1D && unsupported.contains(axis.getFullName())) {
                    axis.setAxisType(AxisType.GeoZ);
                    coordinates.put(axis.getFullName(),
                            CoordinateVariable.create((CoordinateAxis1D) axis));
                // Workaround for files that have a time dimension, but in a format that could not be parsed
                } else if ("time".equals(axis.getFullName())) {
                    LOGGER.warning("Detected unparseable unit string in time axis: '"
                            + axis.getUnitsString() + "'.");
                    axis.setAxisType(AxisType.Time);
                    coordinates.put(axis.getFullName(),
                            CoordinateVariable.create((CoordinateAxis1D) axis));
                } else {
                    LOGGER.warning("Unsupported axis: " + axis + " in input: " + dataset.getLocation()
                            + " has been found");
                }
            }
        }
        coordinatesVariables = coordinates;
        initMapping(dataset.getCoordinateAxes());
    }

    /**
     * Extract the bbox information
     * 
     * @throws IOException 
     * @throws FactoryException 
     */
    private void extractBBOX() throws IOException, FactoryException {
        double [] xLon = new double[2];
        double [] yLat = new double[2];
        byte set = 0;
        isLonLat = false;
        for (CoordinateVariable<?> cv : getCoordinatesVariables()) {
            if (cv.isNumeric()) {

                // is it lat or lon (or geoY or geoX)?
                AxisType type = cv.getAxisType();
                switch (type) {
                case GeoY: case Lat:
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
                default:
                    break;
                }
            }
            if (set == 2) {
                break;
            }
        }
        // create the envelope
        if (set != 2) {
            throw new IllegalStateException("Unable to create envelope for this dataset");
        }
        CoordinateReferenceSystem crs = NetCDFCRSUtilities.WGS84;
        if (!isHasMultiple2Dcoords()) {

            // Looks for Projection definition
            if (!isLonLat) {
                // First, looks for a global crs (it may have been defined 
                // as a NetCDF output write operation) to be used as fallback
                CoordinateReferenceSystem defaultCrs = NetCDFProjection.lookForDatasetCRS(dataset);

                // Then, look for a per variable CRS 
                CoordinateReferenceSystem localCrs = NetCDFProjection.lookForVariableCRS(dataset, defaultCrs);
                if (localCrs != null) {
                    // lookup for a custom EPSG if any
                    crs = NetCDFProjection.lookupForCustomEpsg(localCrs);
                }
            }
            ReferencedEnvelope boundingBox = new ReferencedEnvelope(xLon[0], xLon[1], yLat[0], yLat[1], crs);
            addBoundingBox(NetCDFGeoreferenceManager.DEFAULT, boundingBox);
        } else {
            // TODO: Support multiple Grids definition within the same file which
            // aren't currently supported
            throw new UnsupportedOperationException(
                    "Multiple 2D georeferencing within same datasets still need to be supported");
        }
    }

    /**
     * Get the bounding box coordinates from the provided coordinateVariable.
     * The resulting coordinates will be stored in the provided array.
     * @param cv
     * @param coordinate
     * @throws IOException
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

    /**
     * Parse the coordinateAxes and retrieve the associated coordinateVariable
     * to be used for the dimension mapping.
     *  
     * @param coordinateAxes
     */
    private void initMapping(List<CoordinateAxis> coordinateAxes) {
        // check other dimensions
        int coordinates2D = 0;
        Map<String, String> dimensionsMap = new HashMap<String, String>();
        for (CoordinateAxis axis : coordinateAxes) {
            // get from coordinate vars
            final CoordinateVariable<?> cv = getCoordinateVariable(axis.getFullName());
            if (cv != null) {
                final String name = cv.getName();
                AxisType axisType = cv.getAxisType();
                switch (axisType) {
                case GeoX:
                case GeoY:
                case Lat:
                case Lon:
                    // TODO: Add support for multiple different bboxes within the same file
                    coordinates2D++;
                    continue;
                case Height:
                case Pressure:
                case RadialElevation:
                case RadialDistance:
                case GeoZ:
                    if (NetCDFCRSUtilities.VERTICAL_AXIS_NAMES.contains(name)
                            && !dimensionsMap.containsKey(NetCDFUtilities.ELEVATION_DIM)) {
                        // Main elevation dimension
                        dimensionsMap.put(NetCDFUtilities.ELEVATION_DIM, name);
                    } else {
                        // additional elevation dimension
                        dimensionsMap.put(name.toUpperCase(), name);
                    }
                    break;
                case Time:
                    if (!dimensionsMap.containsKey(NetCDFUtilities.TIME_DIM)) {
                        // Main time dimension
                        dimensionsMap.put(NetCDFUtilities.TIME_DIM, name);
                    } else {
                        // additional time dimension
                        dimensionsMap.put(name.toUpperCase(), name);
                    }
                    break;
                default:
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine("The specified axis type isn't currently supported: " 
                    + axisType + "\nskipping it");
                    }
                    break;
                }
            }else {
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.severe("Null coordinate variable: '" + axis.getFullName() + "' while processing input: " + dataset.getLocation());
                }
            }
        }
        if (coordinates2D > 2) {
            setHasMultiple2Dcoords(true);
        }

        dimensionsMapping = dimensionsMap;
    }

    /**
     * Return the whole dimension to coordinateVariable mapping
     */
    public Map<String, String> getDimensions() {
        return dimensionsMapping;
    }

    /** 
     * Return the dimension name associated to the specified coordinateVariable.
     */
    public String getDimension(String coordinateVariableName) {
        return dimensionsMapping.get(coordinateVariableName);
    }
}
