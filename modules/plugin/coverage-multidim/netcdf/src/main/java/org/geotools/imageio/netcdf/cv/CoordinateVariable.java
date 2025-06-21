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
 */
package org.geotools.imageio.netcdf.cv;

import java.io.IOException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.imageio.netcdf.cv.CoordinateHandlerSpi.CoordinateHandler;
import org.geotools.imageio.netcdf.utilities.NetCDFCRSUtilities;
import org.geotools.imageio.netcdf.utilities.NetCDFUtilities;
import org.geotools.util.Utilities;
import org.geotools.util.logging.Logging;
import ucar.ma2.Array;
import ucar.ma2.ArrayChar;
import ucar.ma2.ArrayChar.StringIterator;
import ucar.ma2.DataType;
import ucar.ma2.IndexIterator;
import ucar.nc2.Attribute;
import ucar.nc2.constants.AxisType;
import ucar.nc2.dataset.CoordinateAxis;
import ucar.nc2.dataset.CoordinateAxis1D;
import ucar.nc2.dataset.CoordinateAxis2D;

/**
 * @author Simone Giannecchini GeoSolutions SAS
 * @author Niels Charlier
 * @param <T>
 */
public abstract class CoordinateVariable<T> {

    protected interface AxisHelper<T> {
        int getSize();

        T get(Map<String, Integer> indexMap);

        List<T> getAll();

        T getMinimum();

        T getMaximum();
    }

    protected class CoordinateAxis1DNumericHelper implements AxisHelper<T> {
        private CoordinateAxis1D axis1D;

        public CoordinateAxis1DNumericHelper() {
            this.axis1D = (CoordinateAxis1D) coordinateAxis;
        }

        @Override
        public synchronized T get(Map<String, Integer> indexMap) {
            // Made it synchronized since axis1D values retrieval
            // does cached read on its underlying
            return convertValue(axis1D.getCoordValue(indexMap.get(coordinateAxis.getFullName())));
        }

        @Override
        public int getSize() {
            return axis1D.getShape(0);
        }

        @Override
        public synchronized T getMinimum() {
            // Made it synchronized since axis1D values retrieval
            // does cached read on its underlying
            return convertValue(axis1D.getMinValue());
        }

        @Override
        public synchronized T getMaximum() {
            // Made it synchronized since axis1D values retrieval
            // does cached read on its underlying
            return convertValue(axis1D.getMaxValue());
        }

        @Override
        public synchronized List<T> getAll() {
            // Made it synchronized since axis1D values retrieval
            // does cached read on its underlying
            return new AbstractList<>() {
                @Override
                public T get(int index) {
                    return convertValue(axis1D.getCoordValue(index));
                }

                @Override
                public int size() {
                    return axis1D.getShape(0);
                }
            };
        }
    }

    /** To use in case that (1) coordinate axis is not one-dimensional (2) coordinate axis is not numerical */
    protected class CoordinateAxisGeneralHelper implements AxisHelper<T> {
        private List<T> convertedData = new ArrayList<>();
        private SortedSet<T> orderedSet = new TreeSet<>();

        public CoordinateAxisGeneralHelper() {
            Array data;
            try {
                data = coordinateAxis.read();
            } catch (IOException ioe) {
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE, "Error reading coordinate values ", ioe);
                }
                throw new IllegalStateException(ioe);
            }

            if (data instanceof ArrayChar) {
                StringIterator it = ((ArrayChar) data).getStringIterator();
                while (it.hasNext()) {
                    String val = it.next();
                    if (val != null && !val.isEmpty()) {
                        T convertedVal = convertValue(val);
                        orderedSet.add(convertedVal);
                        convertedData.add(convertedVal);
                    } else {
                        convertedData.add(null);
                    }
                }
            } else {
                IndexIterator it = data.getIndexIterator();
                while (it.hasNext()) {
                    Object val = it.next();
                    if (!isMissing(val)) {
                        T convertedVal = convertValue(val);
                        orderedSet.add(convertedVal);
                        convertedData.add(convertedVal);
                    } else {
                        convertedData.add(null);
                    }
                }
            }
        }

        @Override
        @SuppressWarnings("deprecation") // no Alternative for Dimension.getFullName
        public synchronized T get(Map<String, Integer> indexMap) {
            int i = indexMap.get(coordinateAxis.getFullName());
            int j = coordinateAxis instanceof CoordinateAxis2D
                    ? indexMap.get(coordinateAxis.getDimension(0).getFullName())
                    : 0;
            // j will be zero for 1D axis
            return convertedData.get(
                    i + (j != 0 ? j * coordinateAxis.getDimension(1).getLength() : 0));
        }

        @Override
        public int getSize() {
            return orderedSet.size();
        }

        @Override
        public T getMinimum() {
            return orderedSet.first();
        }

        @Override
        public T getMaximum() {
            return orderedSet.last();
        }

        @Override
        public List<T> getAll() {
            return new ArrayList<>(orderedSet);
        }
    }

    private static final double KM_TO_M = 1000d;

    private static final Logger LOGGER = Logging.getLogger(CoordinateVariable.class);

    public static Class<?> suggestBinding(CoordinateAxis coordinateAxis) {
        Utilities.ensureNonNull("coordinateAxis", coordinateAxis);
        final AxisType axisType = coordinateAxis.getAxisType();

        switch (axisType) {
            case GeoX:
            case GeoY:
            case GeoZ:
            case Height:
            case Lat:
            case Lon:
            case Pressure:
            case Spectral:
                // numeric ?
                final DataType dataType = coordinateAxis.getDataType();
                // scale and offset are there?
                Attribute scaleFactor = coordinateAxis.findAttribute("scale_factor");
                Attribute offsetFactor = coordinateAxis.findAttribute("offset");
                if (scaleFactor != null || offsetFactor != null) {
                    return Double.class;
                }
                switch (dataType) {
                    case DOUBLE:
                        return Double.class;
                    case BYTE:
                        return Byte.class;
                    case FLOAT:
                        return Float.class;
                    case INT:
                        return Integer.class;
                    case LONG:
                        return Long.class;
                    case SHORT:
                        return Short.class;
                    default:
                        break;
                }
                break;
            case Time:
            case RunTime:
                // numeric
                LOGGER.log(Level.FINE, "Date mapping for axis:" + coordinateAxis.toString());
                return java.util.Date.class;
            default:
                break;
        }
        // unable to recognize this one
        LOGGER.log(Level.FINE, "Unable to find mapping for axis:" + coordinateAxis.toString());
        return null;
    }

    @SuppressWarnings("unchecked")
    public static CoordinateVariable<?> create(CoordinateAxis coordinateAxis) {
        Utilities.ensureNonNull("coordinateAxis", coordinateAxis);

        final AxisType axisType = coordinateAxis.getAxisType();
        if (coordinateAxis.isNumeric()) {

            // AxisType?
            switch (axisType) {
                case GeoX:
                case GeoY:
                case GeoZ:
                case Height:
                case Lat:
                case Lon:
                case Pressure:
                case Spectral:
                    return new NumericCoordinateVariable(suggestBinding(coordinateAxis), coordinateAxis);
                case RunTime:
                case Time:
                    return new TimeCoordinateVariable(coordinateAxis);
                default:
                    throw new IllegalArgumentException("Unsupported axis type: "
                            + axisType
                            + " for coordinate variable: "
                            + coordinateAxis.toStringDebug());
            }
        }
        if (NetCDFUtilities.isCheckCoordinatePlugins()) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Checking for registered coordinate plugins");
            }
            CoordinateHandler handler = CoordinateHandlerFinder.findHandler(coordinateAxis);
            if (handler != null) {
                return handler.createCoordinateVariable(coordinateAxis);
            }
        }
        // If the axis is not numeric and it isn't a parseable time, we can't process any further.
        throw new IllegalArgumentException(
                "Unable to process non numeric coordinate variable: " + coordinateAxis.toString());
    }

    protected final Class<T> binding;

    protected final CoordinateAxis coordinateAxis;

    private CoordinateReferenceSystem crs;

    private double conversionFactor = Double.NaN;

    private boolean convertAxis = false;

    private AxisHelper<T> axisHelper;

    /** */
    public CoordinateVariable(Class<T> binding, CoordinateAxis coordinateAxis) {
        Utilities.ensureNonNull("coordinateAxis", coordinateAxis);
        Utilities.ensureNonNull("binding", binding);
        this.binding = binding;
        this.coordinateAxis = coordinateAxis;
        this.conversionFactor = 1d;
        AxisType axisType = coordinateAxis.getAxisType();
        // Legacy: Special management for projected coordinates with unit = km
        if (NetCDFCRSUtilities.isConvertAxisKm()
                && (axisType == AxisType.GeoX || axisType == AxisType.GeoY)
                && coordinateAxis.getUnitsString().equalsIgnoreCase("km")) {
            conversionFactor = KM_TO_M;
            convertAxis = true;
        }
    }

    protected void init() {
        if (!coordinateAxis.isNumeric()
                || !(coordinateAxis instanceof CoordinateAxis1D)
                || coordinateAxis.hasMissing() && !AxisType.Time.equals(coordinateAxis.getAxisType())) {
            // Not sure time variable can have actual NoData values in the array.
            // Let's exclude it from GeneralHelper case.
            // We may revisit it if we find some data with FillValues in the array.
            axisHelper = new CoordinateAxisGeneralHelper();
        } else {
            axisHelper = new CoordinateAxis1DNumericHelper();
        }
    }

    protected boolean isMissing(Object val) {
        if (val instanceof Number) {
            return coordinateAxis.isMissing(((Number) val).doubleValue());
        } else {
            return val == null;
        }
    }

    public Class<T> getType() {
        return binding;
    }

    public String getUnit() {
        return coordinateAxis.getUnitsString();
    }

    public CoordinateAxis unwrap() {
        return coordinateAxis;
    }

    public AxisType getAxisType() {
        return coordinateAxis.getAxisType();
    }

    public String getName() {
        return coordinateAxis.getShortName();
    }

    public long getSize() throws IOException {
        return axisHelper.getSize();
    }

    public boolean isRegular() {
        return coordinateAxis instanceof CoordinateAxis1D && ((CoordinateAxis1D) coordinateAxis).isRegular();
    }

    public double getIncrement() {
        if (!(coordinateAxis instanceof CoordinateAxis1D)) {
            return Double.NaN;
        }
        return convertAxis
                ? ((CoordinateAxis1D) coordinateAxis).getIncrement() * conversionFactor
                : ((CoordinateAxis1D) coordinateAxis).getIncrement();
    }

    public double getStart() {
        if (!(coordinateAxis instanceof CoordinateAxis1D)) {
            return Double.NaN;
        }
        return convertAxis
                ? ((CoordinateAxis1D) coordinateAxis).getStart() * conversionFactor
                : ((CoordinateAxis1D) coordinateAxis).getStart();
    }

    public T getMinimum() throws IOException {
        return axisHelper.getMinimum();
    }

    public T getMaximum() throws IOException {
        return axisHelper.getMaximum();
    }

    public T read(Map<String, Integer> indexMap) throws IndexOutOfBoundsException {
        return axisHelper.get(indexMap);
    }

    public List<T> read() throws IndexOutOfBoundsException {
        return axisHelper.getAll();
    }

    public final CoordinateReferenceSystem getCoordinateReferenceSystem() {
        if (crs == null) {
            crs = buildCoordinateReferenceSystem();
        }
        return crs;
    }

    public abstract boolean isNumeric();

    protected abstract T convertValue(Object o);

    protected abstract CoordinateReferenceSystem buildCoordinateReferenceSystem();

    @Override
    public String toString() {
        try {
            return "CoordinateVariable [binding="
                    + binding
                    + ", coordinateAxis="
                    + coordinateAxis
                    + ", getType()="
                    + getType()
                    + ", getUnit()="
                    + getUnit()
                    + ", getAxisType()="
                    + getAxisType()
                    + ", getName()="
                    + getName()
                    + ", getSize()="
                    + getSize()
                    + ", isRegular()="
                    + isRegular()
                    + ", getIncrement()="
                    + getIncrement()
                    + ", getStart()="
                    + getStart()
                    + ", isNumeric()="
                    + isNumeric()
                    + ", getMinimum()="
                    + getMinimum()
                    + ", getMaximum()="
                    + getMaximum()
                    + "]";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
