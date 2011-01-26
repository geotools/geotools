/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import javax.imageio.ImageReader;

import ucar.nc2.dataset.AxisType;
import ucar.nc2.dataset.CoordinateAxis;
import ucar.nc2.dataset.CoordinateSystem;
import ucar.nc2.dataset.VariableEnhanced;

import org.geotools.image.io.GeographicImageReadParam;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;


/**
 * Default parameters for {@link NetcdfImageReader}.
 *
 * @since 2.4
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 *
 * @deprecated Having <cite>band dimension</cite> in this class is a problem because it make
 *             difficult to implement {@link NetcdfImageReader#getNumBands} in a reliable way.
 *             The information contained in this class need to move in some interface or in
 *             a {@code FileImageReaderND} superclass (common to NetCDF and HDF readers). The
 *             {@link AxisType} enumeration needs to be replaced by something neutral from GeoAPI.
 */
public class NetcdfReadParam extends GeographicImageReadParam {
    /**
     * The default source band to read from the NetCDF file. Also the default indice for
     * any additional dimension after the one assigned to bands. We use the same default
     * for consistency, because the third dimension (typically <var>z</var>) could be
     * selected either by {@link #setSourceBands} or by {@link #setSliceIndice}; the
     * relevant method depends on {@link NetcdfImageReader#getBandDimension} value.
     */
    static final int DEFAULT_INDICE = 0;

    /**
     * The default source bands to read from the NetCDF file.
     * Also the default destination bands in the buffered image.
     */
    private static final int[] DEFAULT_BANDS = new int[] {DEFAULT_INDICE};

    /**
     * The types of the dimension to use as image bands. If more than one type is specified,
     * only the first dimension with a suitable type will be assigned to bands.
     */
    private Set<AxisType> bandDimensionTypes;

    /**
     * For <var>n</var>-dimensional images, the indices to use for dimensions above 2.
     * Will be created only when first needed.
     */
    private Map<AxisType,Integer> sliceIndices;

    /**
     * Creates a new, initially empty, set of parameters.
     *
     * @param reader The reader for which this parameter block is created
     */
    public NetcdfReadParam(final ImageReader reader) {
        super(reader);
        setSourceBands     (DEFAULT_BANDS);
        setDestinationBands(DEFAULT_BANDS);
    }

    /**
     * Returns {@code true} if there is some possibility that {@link #getBandDimension}
     * returns a positive value.
     */
    final boolean isBandDimensionSet() {
        return (bandDimensionTypes != null) && !bandDimensionTypes.isEmpty() &&
                NetcdfReadParam.class.equals(getClass());
        // The last check is because the user could have overriden getBandDimension.
    }

    /**
     * Returns the dimension to assign to bands for the specified variable. The default
     * implementation returns the last dimension corresponding to one of the types specified
     * to {@link #setBandDimensionTypes}. Users can override this method if the bands should
     * be assigned from a dimension computed differently.
     * <p>
     * <b>Example:</b> For a NetCDF variable having dimensions in the
     * (<var>t</var>,<var>z</var>,<var>y</var>,<var>x</var>) order (as in CF convention), if the
     * {@linkplain AxisType#Height height} and {@linkplain AxisType#Pressure pressure} types have
     * been {@linkplain #setBandDimensionTypes assigned} to bands, then this method will returns
     * the index of the <var>z</var> dimension, i.e. {@code 1}.
     *
     * @param variable The variable for which we want to determine the dimension to assign to bands.
     * @return The dimension assigned to bands, or {@code -1} if none.
     */
    protected int getBandDimension(final VariableEnhanced variable) {
        if (bandDimensionTypes != null) {
            final List<CoordinateSystem> sys = variable.getCoordinateSystems();
            if (sys != null) {
                final int count = sys.size();
                for (int i=0; i<count; i++) {
                    final CoordinateSystem cs = sys.get(i);
                    final List<CoordinateAxis> axes = cs.getCoordinateAxes();
                    if (axes != null) {
                        for (int j=axes.size(); --j>=0;) { // Must be reverse order; see javadoc
                            final CoordinateAxis axis = axes.get(j);
                            if (axis != null && bandDimensionTypes.contains(axis.getAxisType())) {
                                return j;
                            }
                        }
                    }
                }
            }
        }
        return -1;
    }

    /**
     * Returns the dimension assigned to bands. This method returns the values given
     * to the last call to {@link #setBandDimensionTypes}, or {@code null} if none.
     */
    public AxisType[] getBandDimensionTypes() {
        if (bandDimensionTypes == null) {
            return null;
        }
        return bandDimensionTypes.toArray(new AxisType[bandDimensionTypes.size()]);
    }

    /**
     * Assigns the dimension of the specified types to bands. For example in a NetCDF variable
     * having (<var>t</var>,<var>y</var>,<var>x</var>) dimensions, it may be useful to treat
     * the <var>t</var> dimension as bands. After invoking this method with the
     * {@linkplain AxisType#Time time} value, users can select a time through the standard
     * {@link #setSourceBands} API.
     * <p>
     * More than one type may be specified if they should be considered as synonymous. For example
     * in order to assign the <var>z</var> dimension to bands, it may be necessary to specify both
     * the {@linkplain AxisType#Height height} and {@linkplain AxisType#Pressure pressure} types.
     *
     * @param type The types of dimension to assign to bands.
     */
    public void setBandDimensionTypes(final AxisType... types) {
        if (types != null && types.length != 0) {
            if (bandDimensionTypes == null) {
                bandDimensionTypes = new HashSet<AxisType>();
            } else {
                bandDimensionTypes.clear();
            }
            for (int i=0; i<types.length; i++) {
                bandDimensionTypes.add(types[i]);
            }
        } else {
            bandDimensionTypes = null;
        }
    }

    /**
     * Returns the indice to set at the dimension of the specified axis. This is relevant only
     * for <var>n</var>-dimensional data set where <var>n</var>&gt;2. This method returns the
     * last value set by {@link #setSliceIndice}.
     *
     * @param  dimension The axis type (typically {@linkplain AxisType#Height height} or
     *                   {@linkplain AxisType#Time time}).
     * @return The indice to set at the dimension of the specified axis (0 by default).
     */
    public int getSliceIndice(final AxisType axis) {
        if (sliceIndices != null) {
            final Integer indice = sliceIndices.get(axis);
            if (indice != null) {
                return indice.intValue();
            }
        }
        return DEFAULT_INDICE;
    }

    /**
     * Sets the indice for the dimension of the specified axis. This is relevant only for
     * <var>n</var>-dimensional data set where <var>n</var>&gt;2. For example in 4-D data
     * set with (<var>x</var>,<var>y</var>,<var>z</var>,<var>t</var>) axis, those indices
     * may be used by image readers for <var>z</var> and <var>t</var> dimensions.
     * <p>
     * The default value is 0 for all cases. This means that for the above-cited 4-D data set,
     * only the image at the first time (<var>t</var>=0) and first altitude (<var>z</var>=0)
     * is selected.
     *
     * @param  dimension The axis type (typically {@linkplain AxisType#Height height} or
     *                   {@linkplain AxisType#Time time}).
     * @param indice The indice as a positive value.
     */
    public void setSliceIndice(final AxisType dimension, final int indice) {
        if (indice < 0) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$2, "indice", indice));
        }
        if (indice != DEFAULT_INDICE) {
            if (sliceIndices == null) {
                sliceIndices = new HashMap<AxisType,Integer>();
            }
            sliceIndices.put(dimension, indice);
        } else if (sliceIndices != null) {
            sliceIndices.remove(dimension);
            if (sliceIndices.isEmpty()) {
                sliceIndices = null; // hasNonNullIndices() wants that.
            }
        }
    }

    /**
     * Returns {@code true} if this set of parameters contains a least one indice
     * defined to a value different than 0.
     */
    final boolean hasNonDefaultIndices() {
        return sliceIndices != null;
    }
}
