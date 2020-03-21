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

import org.geotools.data.util.NumericConverterFactory;
import org.geotools.imageio.netcdf.utilities.NetCDFCRSUtilities;
import org.geotools.util.Converter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import ucar.nc2.Attribute;
import ucar.nc2.constants.AxisType;
import ucar.nc2.dataset.CoordinateAxis;

/**
 * @author Simone Giannecchini, GeoSolutions SAS
 * @author Niels Charlier
 *     <p>TODO caching
 */
class NumericCoordinateVariable<T extends Number> extends CoordinateVariable<T> {

    private double scaleFactor = Double.NaN;

    private double offsetFactor = Double.NaN;

    private Converter converter;

    private static final NumericConverterFactory CONVERTER_FACTORY = new NumericConverterFactory();

    /** */
    public NumericCoordinateVariable(Class<T> binding, CoordinateAxis coordinateAxis) {
        super(binding, coordinateAxis);
        // If the axis is not numeric, we can't process any further.
        if (!coordinateAxis.isNumeric()) {
            throw new IllegalArgumentException(
                    "Unable to process non numeric coordinate variable: "
                            + coordinateAxis.toString());
        }

        // scale and offset
        Attribute scaleFactor = coordinateAxis.findAttribute("scale_factor");
        if (scaleFactor != null) {
            this.scaleFactor = scaleFactor.getNumericValue().doubleValue();
        }
        Attribute offsetFactor = coordinateAxis.findAttribute("offset");
        if (offsetFactor != null) {
            this.offsetFactor = offsetFactor.getNumericValue().doubleValue();
        }

        // converter from double to binding
        this.converter = CONVERTER_FACTORY.createConverter(Double.class, this.binding, null);
        init();
    }

    @Override
    public boolean isNumeric() {
        return true;
    }

    @Override
    protected synchronized CoordinateReferenceSystem buildCoordinateReferenceSystem() {
        final AxisType axisType = coordinateAxis.getAxisType();
        switch (axisType) {
            case GeoZ:
            case Height:
            case Pressure:
                String axisName = getName();
                if (NetCDFCRSUtilities.VERTICAL_AXIS_NAMES.contains(axisName)) {
                    return NetCDFCRSUtilities.buildVerticalCrs(coordinateAxis);
                }
            default:
                return null;
        }
    }

    @Override
    protected T convertValue(Object o) {
        double val = ((Number) o).doubleValue();
        if (!Double.isNaN(scaleFactor)) {
            val *= scaleFactor;
        }
        if (!Double.isNaN(offsetFactor)) {
            val += offsetFactor;
        }
        try {
            return converter.convert(val, this.binding);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
