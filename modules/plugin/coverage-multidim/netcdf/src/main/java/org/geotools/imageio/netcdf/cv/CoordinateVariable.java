/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2014, Open Source Geospatial Foundation (OSGeo)
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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.util.Utilities;
import org.geotools.util.logging.Logging;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import ucar.ma2.DataType;
import ucar.nc2.Attribute;
import ucar.nc2.constants.AxisType;
import ucar.nc2.dataset.CoordinateAxis1D;

/**
 * @author Simone Giannecchini GeoSolutions SAS
 * @param <T>
 * 
 */
public abstract class CoordinateVariable<T> {

    static class CoordinateAxisWrapper {
        CoordinateAxis1D axis1D;

        private AxisType axisType;

        private String unit;

        public AxisType getAxisType() {
            return axisType;
        }

        public String getUnit() {
            return unit;
        }

        public String getName() {
            return name;
        }

        public long getSize() {
            return size;
        }

        public boolean isRegular() {
            return isRegular;
        }

        public double getIncrement() {
            return increment;
        }

        public double getStart() {
            return start;
        }

        public String getFullName() {
            return fullName;
        }

        public String getPositive() {
            return positive;
        }

        private String name;

        private long size;

        private boolean isRegular;

        private double increment;

        private double start;

        private String fullName;

        private String positive;
        
        public CoordinateAxisWrapper(CoordinateAxis1D coordinateAxis) {
            this.axis1D = coordinateAxis;
            axisType = coordinateAxis.getAxisType();
            unit = coordinateAxis.getUnitsString();
            fullName = coordinateAxis.getFullName();
            name = coordinateAxis.getShortName();
            size = coordinateAxis.getSize();
            isRegular = coordinateAxis.isRegular();
            increment = coordinateAxis.getIncrement();
            start = coordinateAxis.getStart();
            positive = coordinateAxis.getPositive();
        }

        public int[] getShape() {
            return axis1D.getShape();
        }

        public synchronized Object getMinValue() {
            return axis1D.getMinValue();
        }

        public synchronized Object getMaxValue() {
            return axis1D.getMaxValue();
        }

        public synchronized double getCoordValue(int index) {
            return axis1D.getCoordValue(index);
        }

        public synchronized Attribute findAttribute(String name) {
            return axis1D.findAttribute(name);
        }
    }

    private static final double KM_TO_M = 1000d;

    private final static Logger LOGGER = Logging.getLogger(CoordinateVariable.class);

    public static Class<?> suggestBinding(CoordinateAxis1D coordinateAxis) {
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
            @SuppressWarnings("deprecation")
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

            }
            break;
        case Time:
        case RunTime:
            // numeric
            LOGGER.log(Level.FINE, "Date mapping for axis:" + coordinateAxis.toString());
            return java.util.Date.class;
        }
        // unable to recognize this one
        LOGGER.log(Level.FINE, "Unable to find mapping for axis:" + coordinateAxis.toString());
        return null;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static CoordinateVariable<?> create(CoordinateAxis1D coordinateAxis) {
        Utilities.ensureNonNull("coordinateAxis", coordinateAxis);
        // If the axis is not numeric, we can't process any further.
        if (!coordinateAxis.isNumeric()) {
            throw new IllegalArgumentException(
                    "Unable to process non numeric coordinate variable: "
                            + coordinateAxis.toString());
        }

        // INITIALIZATION

        // AxisType?
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
            return new NumericCoordinateVariable(suggestBinding(coordinateAxis), coordinateAxis);
        case RunTime:
        case Time:
            return new TimeCoordinateVariable(coordinateAxis);
        default:
            throw new IllegalArgumentException("Unsupported axis type: " + axisType
                    + " for coordinate variable: " + coordinateAxis.toStringDebug());
        }

    }

    protected final Class<T> binding;

    CoordinateAxisWrapper axis;

    private double conversionFactor = Double.NaN;

    private boolean convertAxis = false;

    /**
     * @param binding
     * @param coordinateAxis
     */
    public CoordinateVariable(Class<T> binding, CoordinateAxis1D coordinateAxis) {
        Utilities.ensureNonNull("coordinateAxis", coordinateAxis);
        Utilities.ensureNonNull("binding", binding);
        this.binding = binding;
        this.axis = new CoordinateAxisWrapper(coordinateAxis);
        this.conversionFactor = 1d;
        AxisType axisType = coordinateAxis.getAxisType();
        String unit = coordinateAxis.getUnitsString();
        // Special management for projected coordinates with unit = km
        if ((axisType == AxisType.GeoX || axisType == AxisType.GeoY) && 
                "km".equalsIgnoreCase(unit)) {
            conversionFactor = KM_TO_M;
            convertAxis = true;
        }
    }

    public Class<T> getType() {
        return binding;
    }

    public String getUnit() {
        return axis.unit;
    }

    public AxisType getAxisType() {
        return axis.axisType;
    }

    public String getName() {
        return axis.name;
    }

    public String getFullName() {
        return axis.fullName;
    }

    public long getSize() {
        return axis.size;
    }

    public boolean isRegular() {
        return axis.isRegular;
    }

    public double getIncrement() {
        return convertAxis ? axis.increment * conversionFactor : axis.increment;
    }

    public double getStart() {
        return convertAxis ? axis.start * conversionFactor : axis.start;
    }

    public String getPositive() {
        return axis.positive;
    }

    abstract public boolean isNumeric();

    abstract public T getMinimum() throws IOException;

    abstract public T getMaximum() throws IOException;

    abstract public T read(int index) throws IndexOutOfBoundsException;

    abstract public List<T> read() throws IndexOutOfBoundsException;

    @Override
    public String toString() {
        try {
            return "CoordinateVariable [binding=" + binding + ", coordinateAxis:"  
                    + " getType()=" + getType() + ", getUnit()=" + getUnit() + ", getAxisType()="
                    + getAxisType() + ", getName()=" + getName() + ", getSize()=" + getSize()
                    + ", isRegular()=" + isRegular() + ", getIncrement()=" + getIncrement()
                    + ", getStart()=" + getStart() + ", isNumeric()=" + isNumeric()
                    + ", getMinimum()=" + getMinimum() + ", getMaximum()=" + getMaximum() + "]";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return null;
    }
}
