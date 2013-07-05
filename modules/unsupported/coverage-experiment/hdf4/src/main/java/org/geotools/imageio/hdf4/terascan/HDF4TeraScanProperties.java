/*
 *    ImageI/O-Ext - OpenSource Java Image translation Library
 *    http://www.geo-solutions.it/
 *    http://java.net/projects/imageio-ext/
 *    (C) 2007 - 2009, GeoSolutions
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
package org.geotools.imageio.hdf4.terascan;

import java.util.List;

import org.geotools.imageio.hdf4.HDF4Products;
import org.geotools.imageio.hdf4.HDF4Products.HDF4Product;

import ucar.nc2.Variable;

public class HDF4TeraScanProperties {

    // TODO: Leverages on a properties file
    public static class TeraScanProducts extends HDF4Products {
        public TeraScanProducts() {
            super(2);
            int i = 0;
            HDF4Product mcsst = new HDF4Product("mcsst", 1);
            add(i++, mcsst);

            // TODO: check the exact product name. The TSS paper states
            // lowclouds while the sample data contains a lowcloud product
            HDF4Product lowcloud = new HDF4Product("lowcloud", 1);
            add(i++, lowcloud);
        }
    }

    private HDF4TeraScanProperties() {

    }

    /**
     * Reduces the product's list by removing not interesting ones. As an
     * instance the dataset containing l2_flags will be not presented.
     * 
     * @param products
     *                The originating <code>String</code> array containing the
     *                list of products to be checked.
     * @return A <code>String</code> array containing a refined list of
     *         products
     */
    static String[] refineProductList(final List<Variable> variables) {
    	if (variables!=null && !variables.isEmpty()){
	    	final int inputProducts = variables.size();
	        final String[] products = new String[inputProducts];
	        int j = 0;
	        final boolean[] accepted = new boolean[inputProducts];
	
	        for (int i = 0; i < inputProducts; i++) {
	            final String productName = variables.get(i).getName();
	            products[i] = productName;
	            if (HDF4TeraScanProperties.terascanProducts.get(productName) != null) {
	                accepted[i] = true;
	                j++;
	            } else
	                accepted[i] = false;
	        }
	        if (j == inputProducts)
	            return products;
	        final String[] returnedProductsList = new String[j];
	        j = 0;
	        for (int i = 0; i < inputProducts; i++) {
	            if (accepted[i])
	                returnedProductsList[j++] = products[i];
	        }
	        return returnedProductsList;
    	}
    	return null;
    }

    public final static HDF4TeraScanProperties.TeraScanProducts terascanProducts = new HDF4TeraScanProperties.TeraScanProducts();

    public static class DatasetAttribs {
        public final static String VALID_RANGE = "valid_range";
        public final static String LONG_NAME = "long_name";
        public final static String FILL_VALUE = "_FillValue";
        public final static String SCALE_FACTOR = "scale_factor";
        public final static String SCALE_FACTOR_ERR = "scale_factor_err";
        public final static String ADD_OFFSET = "add_offset";
        public final static String ADD_OFFSET_ERR = "add_offset_err";
        public final static String CALIBRATED_NT = "calibrated_nt";
        public final static String UNITS = "units";
        public final static String UNSIGNED = "_Unsigned";
        
        private DatasetAttribs() {

        }

    }

    public static class ProjAttribs {
        public final static String PROJECT_TO_IMAGE_AFFINE = "proj_to_image_affine";
        public final static String PROJECT_ORIGIN_LATITUDE = "proj_origin_latitude";
        public final static String PROJECT_ORIGIN_LONGITUDE = "proj_origin_longitude";
        public final static String EARTH_FLATTENING = "earth_flattening";
        public final static String EQUATORIAL_RADIUS = "equatorial_radius";
        public final static String STANDARD_PARALLEL_1 = "std_parallel_1";
        public final static String STANDARD_PARALLEL_2 = "std_parallel_2";
        public final static String DATUM_NAME = "datum_name";
        public final static String PROJECTION_NAME = "projection_name";

        private ProjAttribs() {

        }
    }
    
    public static class TemporalAttribs {
        public final static String PASS_START_DATE = "pass_start_date";
        public final static String PASS_START_TIME = "pass_start_time";
        public final static String DATA_START_DATE = "data_start_date";
        public final static String DATA_START_TIME = "data_start_time";
        public final static String DATA_END_DATE = "data_end_date";
        public final static String DATA_END_TIME = "data_end_time";
        
        private TemporalAttribs() {

        }
    }

    public final static String LOWER_LEFT_LONGITUDE = "lower_left_longitude";
    public final static String LOWER_LEFT_LATITUDE = "lower_left_latitude";
    public final static String UPPER_RIGHT_LONGITUDE = "upper_right_longitude";
    public final static String UPPER_RIGHT_LATITUDE = "upper_right_latitude";

}
