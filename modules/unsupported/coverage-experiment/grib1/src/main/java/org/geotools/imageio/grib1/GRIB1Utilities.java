/*
 *    JImageIO-extension - OpenSource Java Image translation Library
 *    http://www.geo-solutions.it/
 *    (C) 2007, GeoSolutions
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    either version 3 of the License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.imageio.grib1;

import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.io.IOException;

import javax.media.jai.RasterFactory;

import org.geotools.imageio.netcdf.NetCDFUtilities;

import ucar.ma2.Array;
import ucar.nc2.Variable;

/**
 * Set of utility methods and constants
 * 
 * @author Daniele Romagnoli, GeoSolutions
 */
public class GRIB1Utilities {

	private final static ColorModel INT_COLOR_MODEL = buildColorModel(DataBuffer.TYPE_INT);

	private final static ColorModel SHORT_COLOR_MODEL = buildColorModel(DataBuffer.TYPE_USHORT);

	private final static ColorModel FLOAT_COLOR_MODEL = buildColorModel(DataBuffer.TYPE_FLOAT);

	private final static ColorModel DOUBLE_COLOR_MODEL = buildColorModel(DataBuffer.TYPE_DOUBLE);

	public static ColorModel getColorModel(final int type) {
		switch (type) {
		case DataBuffer.TYPE_INT:
			return INT_COLOR_MODEL;

		case DataBuffer.TYPE_USHORT:
			return SHORT_COLOR_MODEL;

		case DataBuffer.TYPE_FLOAT:
			return FLOAT_COLOR_MODEL;

		case DataBuffer.TYPE_DOUBLE:
			return DOUBLE_COLOR_MODEL;

		default:
			return buildColorModel(type);
		}
	}

	private static ColorModel buildColorModel(final int type) {
		return RasterFactory.createComponentColorModel(type, // dataType
				ColorSpace.getInstance(ColorSpace.CS_GRAY), // color space
				false, // has alpha
				false, // is alphaPremultiplied
				Transparency.OPAQUE); // transparency
	}

	final static String GRIB_PARAM_PREFIX = "GRIB_param_";

	final static String GRIB_PARAM_NUMBER = "GRIB_param_number";

	final static String GRIB_TABLE_ID = "GRIB_table_id";

	final static String GRIB_PARAM_NAME = "GRIB_param_name";

	final static String GRIB_PRODUCT_DEFINITION_TYPE = "GRIB_product_definition_type";

	final static String GRIB_PARAM_CENTER_ID = "GRIB_center_id";

	final static String GRIB_LEVEL_TYPE = "GRIB_level_type";

	final static String GRIB_PARAM_UNIT = NetCDFUtilities.UNITS;

	final static String BOUNDS = "bounds";

	final static int UNDEFINED_NUMBER = Integer.MIN_VALUE;

	public final static String VALUES_SEPARATOR = " ";

	private GRIB1Utilities() {

	}

	static final boolean isVerticalLevelSymbolic(final int levelType) {
		switch (levelType) {
		case 0:
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
		case 9:
		case 102:
		case 200:
		case 201:
		case 204:
		case 206:
		case 207:
		case 209:
		case 210:
		case 211:
		case 212:
		case 213:
		case 214:
		case 215:
		case 220:
		case 222:
		case 223:
		case 224:
		case 232:
		case 233:
		case 234:
		case 235:
		case 236:
		case 237:
		case 238:
		case 239:
		case 240:
		case 242:
		case 243:
		case 244:
		case 245:
		case 246:
		case 247:
		case 248:
		case 249:
		case 251:
		case 252:
		case 253:
		case 254:
			return true;

		default:
			return false;
		}
	}

	public static String getValuesAsString(Variable variable, int[] indexes) {
		final int dataType = NetCDFUtilities.getRawDataType(variable);
		final StringBuilder sb = new StringBuilder("");
		try {
			final int size = indexes.length;
			final Array values = variable.read();
			for (int i = 0; i < size; i++) {

				switch (dataType) {
				// TODO: ADD MORE.
				case DataBuffer.TYPE_SHORT:
					final short val1s = values.getShort(indexes[i]);
					sb.append(Short.toString(val1s));
					break;
				case DataBuffer.TYPE_INT:
					final int val1 = values.getInt(indexes[i]);
					sb.append(Integer.toString(val1));
					break;
				case DataBuffer.TYPE_FLOAT:
					final float val1f = values.getFloat(indexes[i]);
					sb.append(Float.toString(val1f));
					break;
				case DataBuffer.TYPE_DOUBLE:
					final double val1d = values.getDouble(indexes[i]);
					sb.append(Double.toString(val1d));
					break;
				}
				if (size > 1 && i != size - 1)
					sb.append(VALUES_SEPARATOR);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			// TODO LOG ME
		}
		return sb.toString();
	}
}
