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
package org.geotools.imageio.hdf4.aps;

import org.geotools.imageio.hdf4.HDF4Products;
import org.geotools.imageio.hdf4.HDF4Products.HDF4Product;

public class HDF4APSProperties {

	public static class APSProducts extends HDF4Products {
        public APSProducts() {
        	super(14);
            int index = 0;
            HDF4Product sst = new HDF4Product("sst", 1);
            add(index++, sst);
            
            HDF4Product sst4 = new HDF4Product("sst4", 1);
            add(index++, sst4);

            HDF4Product chl_oc3 = new HDF4Product("chl_oc3", 1);
            add(index++, chl_oc3);
            
            HDF4Product chl_oc3m = new HDF4Product("chl_oc3m", 1);
            add(index++, chl_oc3m);

            HDF4Product k_490 = new HDF4Product("K_490", 1);
            add(index++, k_490);
            
            HDF4Product albedo_ch1 = new HDF4Product("albedo_ch1", 1);
            add(index++, albedo_ch1);

            HDF4Product albedo_ch2 = new HDF4Product("albedo_ch2", 1);
            add(index++, albedo_ch2);

            HDF4Product albedo_ch3 = new HDF4Product("albedo_ch3", 1);
            add(index++, albedo_ch3);

            HDF4Product btemp_ch4 = new HDF4Product("btemp_ch4", 1);
            add(index++, btemp_ch4);

            HDF4Product btemp_ch5 = new HDF4Product("btemp_ch5", 1);
            add(index++, btemp_ch5);

            HDF4Product k_PAR = new HDF4Product("K_PAR", 1);
            add(index++, k_PAR);

            HDF4Product c_660 = new HDF4Product("c_660", 1);
            add(index++, c_660);
            
            HDF4Product salinity = new HDF4Product("salinity", 1);
            add(index++, salinity);
            
            HDF4Product true_color = new HDF4Product("true_color", 3);
            add(index++, true_color);
       	
            // TODO: Add more APS supported products
        }
    }

    private HDF4APSProperties() {

    }

    public final static HDF4APSProperties.APSProducts apsProducts = new HDF4APSProperties.APSProducts();

    /**
     * ------------------------- 
     * Standard: File Attributes 
     * -------------------------
     */

    /** The name of the product */
    public final static String STD_FA_FILE = "file";

    /** Always set to UNCLASSIFIED */
    public final static String STD_FA_FILECLASSIFICATION = "fileClassification";

    /** Either EXPERIMENTAL or OPERATIONAL */
    public final static String STD_FA_FILESTATUS = "fileStatus";

    /** One of NRL Level-3 / NRL Level-3 Mosaic / NRL Level-4 */
    public final static String STD_FA_FILETITLE = "fileTitle";

    /** The version of APS Data format */
    public final static String STD_FA_FILEVERSION = "fileVersion";

    /** The agency which created the file */
    public final static String STD_FA_CREATEAGENCY = "createAgency";

    /** The version of the software which created the file */
    public final static String STD_FA_CREATESOFTWARE = "createSoftware";

    /** The hardware/software platform the file was created on */
    public final static String STD_FA_CREATEPLATFORM = "createPlatform";

    /** The date and time when the file was created */
    public final static String STD_FA_CREATETIME = "createTime";

    /** The name of the user that created this file */
    public final static String STD_FA_CREATEUSER = "createUser";

    public final static String[] STD_FA_ATTRIB = new String[] { STD_FA_FILE,
            STD_FA_FILECLASSIFICATION, STD_FA_FILESTATUS, STD_FA_FILETITLE,
            STD_FA_FILEVERSION, STD_FA_CREATEAGENCY, STD_FA_CREATESOFTWARE,
            STD_FA_CREATEPLATFORM, STD_FA_CREATETIME, STD_FA_CREATEUSER };

    /**
     * ------------------------- 
     * Standard: Time Attributes 
     * -------------------------
     */

    /** UTC start time as an ASCII string */
    public final static String STD_TA_TIMESTART = "timeStart";

    /** UTC year of data start, e.g. 2007 */
    public final static String STD_TA_TIMESTARTYEAR = "timeStartYear";

    /** UTC day-of-year of data start (1-366) */
    public final static String STD_TA_TIMESTARTDAY = "timeStartDay";

    /** UTC milliseconds-of-day of data start (1-86400000) */
    public final static String STD_TA_TIMESTARTTIME = "timeStartTime";

    /** UTC end time as an ASCII string */
    public final static String STD_TA_TIMEEND = "timeEnd";

    /** UTC year of data end, e.g. 2007 */
    public final static String STD_TA_TIMEENDYEAR = "timeEndYear";

    /** UTC day-of-year of data end (1-366) */
    public final static String STD_TA_TIMEENDDAY = "timeEndDay";

    /** UTC milliseconds-of-day of data end (1-86400000) */
    public final static String STD_TA_TIMEENDTIME = "timeEndTime";

    /**
     * Flag indicating if data collected during day or night. May be one of Day,
     * Night, Day/Night
     */
    public final static String STD_TA_TIMEDAYNIGHT = "timeDayNight";

    public final static String[] STD_TA_ATTRIB = new String[] {
            STD_TA_TIMESTART, STD_TA_TIMESTARTYEAR, STD_TA_TIMESTARTDAY,
            STD_TA_TIMESTARTTIME, STD_TA_TIMEEND, STD_TA_TIMEENDYEAR,
            STD_TA_TIMEENDDAY, STD_TA_TIMEENDTIME, STD_TA_TIMEDAYNIGHT };

    /**
     * --------------------------- 
     * Standard: Sensor Attributes
     * ---------------------------
     */

    /** AVHRR/3, SeaWiFS, MODIS */
    public final static String STD_SA_SENSOR = "sensor";

    /** Platform carrying sensor, like Orbview-2, NOAA-12, MODIS-AQUA */
    public final static String STD_SA_SENSORPLATFORM = "sensorPlatform";

    /** Agency/Owner of Sensor */
    public final static String STD_SA_SENSORAGENCY = "sensorAgency";

    /** Type of sensor: scanner, pushbroom, whiskbroom */
    public final static String STD_SA_SENSORTYPE = "sensorType";

    /** Description of spectrum: visible, near-IR, thermal */
    public final static String STD_SA_SENSORSPECTRUM = "sensorSpectrum";

    /** Number of Bands */
    public final static String STD_SA_SENSORNUMBEROFBANDS = "sensorNumberOfBands";

    /** Units of wavelengths, like nm */
    public final static String STD_SA_SENSORBANDUNITS = "sensorBandUnits";

    /** Center wavelengths */
    public final static String STD_SA_SENSORBANDS = "sensorBands";

    /** Nominal width of bands */
    public final static String STD_SA_SENSORBANDWIDTHS = "sensorBandWidths";

    /** Nominal Altitude of sensor */
    public final static String STD_SA_SENSORNOMINALALTITUDEINKM = "sensorNominalAltitudeInKM";

    /** Distance on earth of Field of View in kilometers */
    public final static String STD_SA_SENSORSCANWIDTHINKM = "sensorScanWidthInKM";

    /** Distance on earth of a single pixel in kilometers */
    public final static String STD_SA_SENSORRESOLUTIONINKM = "sensorResolutionInKM";

    /** Type of platform */
    public final static String STD_SA_SENSORPLATFORMTYPE = "sensorPlatformType";

    public final static String[] STD_SA_ATTRIB = new String[] { STD_SA_SENSOR,
            STD_SA_SENSORPLATFORM, STD_SA_SENSORAGENCY, STD_SA_SENSORTYPE,
            STD_SA_SENSORSPECTRUM, STD_SA_SENSORNUMBEROFBANDS,
            STD_SA_SENSORBANDUNITS, STD_SA_SENSORBANDS,
            STD_SA_SENSORBANDWIDTHS, STD_SA_SENSORNOMINALALTITUDEINKM,
            STD_SA_SENSORSCANWIDTHINKM, STD_SA_SENSORRESOLUTIONINKM,
            STD_SA_SENSORPLATFORMTYPE };

    /**
     * ----------------------------------------- 
     * Product file: Input Parameters Attributes 
     * -----------------------------------------
     */

    /** Name of the calibration file used. SeaWiFS/MOS specific. */
    public final static String PFA_IPA_INPUTCALIBRATIONFILE = "inputCalibrationFile";

    /** A string indicating the options used during the processing of the file */
    public final static String PFA_IPA_INPUTPARAMETER = "inputParameter";

    /** The mask defined as an integer */
    public final static String PFA_IPA_INPUTMASKSINT = "inputMasksInt";

    /**
     * A comma separated list of flags that were used as masks during
     * processing.
     */
    public final static String PFA_IPA_INPUTMASKS = "inputMasks";

    /** A comma separated list of products stored in this file. */
    public final static String PFA_IPA_PRODLIST = "prodList";

    /** Version of processing */
    public final static String PFA_IPA_PROCESSINGVERSION = "processingVersion";

    public final static String[] PFA_IPA_ATTRIB = new String[] {
            PFA_IPA_INPUTCALIBRATIONFILE, PFA_IPA_INPUTPARAMETER,
            PFA_IPA_INPUTMASKSINT, PFA_IPA_INPUTMASKS, PFA_IPA_PRODLIST,
            PFA_IPA_PROCESSINGVERSION };

    /**
     * -----------------------------------
     * Product file: Navigation Attributes
     * -----------------------------------
     */

    /** Navigation type of data. Always set to 'mapped' */
    public final static String PFA_NA_NAVTYPE = "navType";

    /** Map projection system used. Always set to NRL(USGS) */
    public final static String PFA_NA_MAPPROJECTIONSYSTEM = "mapProjectionSystem";

    /**
     * Name of the SDS included in the file that contains the map projection
     * parameter values.
     */
    public final static String PFA_NA_MAPPROJECTION = "mapProjection";

    /** Latitude and longitude of upper left (1,1) point of each product. */
    public final static String PFA_NA_MAPPEDUPPERLEFT = "mapUpperLeft";

    /** Latitude and longitude of upper right (1,n) point of each product. */
    public final static String PFA_NA_MAPPEDUPPERRIGHT = "mapUpperRight";

    /** Latitude and longitude of lower left (m,1) point of each product. */
    public final static String PFA_NA_MAPPEDLOWERLEFT = "mapLowerLeft";

    /** Latitude and longitude of lower right (m,n) point of each product. */
    public final static String PFA_NA_MAPPEDLOWERRIGHT = "mapLowerRight";

    public final static String[] PFA_NA_ATTRIB = new String[] { PFA_NA_NAVTYPE,
            PFA_NA_MAPPROJECTIONSYSTEM, PFA_NA_MAPPROJECTION,
            PFA_NA_MAPPEDUPPERLEFT, PFA_NA_MAPPEDUPPERRIGHT,
            PFA_NA_MAPPEDLOWERLEFT, PFA_NA_MAPPEDLOWERRIGHT };

    /**
     * ----------------------------------------------------
     * Product file: Input Geographical Coverage Attributes
     * ----------------------------------------------------
     */

    /**
     * latitude and longitude of upper left (1,1) point of original input data.
     */
    public final static String PFA_IGCA_LOCALEUPPERLEFT = "localeUpperLeft";

    /**
     * latitude and longitude of upper right (1,n) point of original input data.
     */
    public final static String PFA_IGCA_LOCALEUPPERRIGHT = "localeUpperRight";

    /**
     * latitude and longitude of lower left (m,1) point of original input data.
     */
    public final static String PFA_IGCA_LOCALELOWERLEFT = "localeLowerLeft";

    /**
     * latitude and longitude of lower right (m,n) point of original input data.
     */
    public final static String PFA_IGCA_LOCALELOWERRIGHT = "localeLowerRight";

    /** latitude and longitude of NorthWestern point of original input data. */
    public final static String PFA_IGCA_LOCALENWCORNER = "localeNWCorner";

    /** latitude and longitude of NorthEastern point of original input data. */
    public final static String PFA_IGCA_LOCALENECORNER = "localeNECorner";

    /** latitude and longitude of SouthWestern point of original input data. */
    public final static String PFA_IGCA_LOCALESWCORNER = "localeSWCorner";

    /** latitude and longitude of SouthEastern point of original input data. */
    public final static String PFA_IGCA_LOCALESECORNER = "localeSECorner";

    /**  */
    public final static String[] PFA_IGCA_ATTRIB = new String[] {
            PFA_IGCA_LOCALEUPPERLEFT, PFA_IGCA_LOCALEUPPERRIGHT,
            PFA_IGCA_LOCALELOWERLEFT, PFA_IGCA_LOCALELOWERRIGHT,
            PFA_IGCA_LOCALENWCORNER, PFA_IGCA_LOCALENECORNER,
            PFA_IGCA_LOCALESWCORNER, PFA_IGCA_LOCALESECORNER };

    /**
     * --------------------------
     * Product Dataset Attributes
     * --------------------------
     */

    /**
     * This string contains the version of the software which created the
     * product.
     */
    public final static String PDSA_CREATESOFTWARE = "createSoftware";

    /** This string contains the date and time when the product was created */
    public final static String PDSA_CREATETIME = "createTime";

    /**
     * This string contains a triple describing the cpu-machine-os which created
     * the scientific data set
     */
    public final static String PDSA_CREATEPLATFORM = "createPlatform";

    /** This is a description of the product. */
    public final static String PDSA_PRODUCTNAME = "productName";

    /** This is a notation about the algorithm, usually a paper reference. */
    public final static String PDSA_PRODUCTALGORITHM = "productAlgorithm";

    /** This is a description of the units of the product. */
    public final static String PDSA_PRODUCTUNITS = "productUnits";

    /**
     * This is a version number of the product used to indicate changes in the
     * algorithm.
     */
    public final static String PDSA_PRODUCTVERSION = "productVersion";

    /**
     * This is a type of product. For example, 'chl_oc4v4' and 'chl_oc3m' would
     * both set this to 'chl'.
     */
    public final static String PDSA_PRODUCTTYPE = "productType";

    /**
     * This is a space delimited string of additional units available for this
     * product. For example, an sst product may set this string to "Kelvin
     * Fahrenheit"
     */
    // The test file in the test-data folder has "otherUnits" as attribute
    // instead of "additionalUnits"
    public final static String PDSA_ADDITIONALUNITS_1 = "additionalUnits";

    public final static String PDSA_ADDITIONALUNITS_2 = "otherUnits";

    /**
     * This new SDS attribute will give an indication of the status this
     * product.
     */
    public final static String PDSA_PRODUCTSTATUS = "productStatus";

    /** This is a suggested range of valid data. */
    public final static String PDSA_VALIDRANGE = "validRange";

    /**
     * This is the geophysical value which will represent invalid data for the
     * given product.
     */
    public final static String PDSA_INVALID = "invalid";

    // The test file in the test-data folder has "badData" as attribute
    // instead of "invalid"
    public final static String PDSA_BADDATA = "badData";

    /** The type of scaling of the product. Currently, always Linear */
    public final static String PDSA_PRODUCTSCALING = "productScaling";

    /** The slope for product scaling. */
    public final static String PDSA_SCALINGSLOPE = "scalingSlope";

    /** The intercept for product scaling. */
    public final static String PDSA_SCALINGINTERCEPT = "scalingIntercept";

    /**
     * This is a suggested function to apply to convert the data in the SDS into
     * an image. A value of 1 indicates linear scaling; a value of 2 indicates
     * log10 scaling.
     */
    public final static String PDSA_BROWSEFUNC = "browseFunc";

    /**
     * This is a suggested display range when converting the data in the SDS
     * into an image. This may or may not be the same as validRange because in
     * some cases (e.g. rrs_412), the data has been known to fall outside the
     * range, but we wish to display the invalid data. This attribute is used by
     * the APS program imgBrowse when creating quick-look browse images of
     * different products.
     */
    public final static String PDSA_BROWSERANGES = "browseRanges";

    /**
     * Product dataset attributes Array
     */
    public final static String[] PDSA_ATTRIB = new String[] {
            PDSA_CREATESOFTWARE, PDSA_CREATETIME, PDSA_CREATEPLATFORM,
            PDSA_PRODUCTNAME, PDSA_PRODUCTALGORITHM, PDSA_PRODUCTUNITS,
            PDSA_PRODUCTVERSION, PDSA_PRODUCTTYPE, PDSA_ADDITIONALUNITS_1,
            PDSA_ADDITIONALUNITS_2, PDSA_PRODUCTSTATUS, PDSA_VALIDRANGE,
            PDSA_INVALID, PDSA_BADDATA, PDSA_PRODUCTSCALING, PDSA_SCALINGSLOPE,
            PDSA_SCALINGINTERCEPT, PDSA_BROWSEFUNC, PDSA_BROWSERANGES };

	public static final String PRODLIST = "prodList";

    /**
     * Build an ISO8601 formatted time from an input <code>String</code>
     * representing time in the form: "DAY_OF_THE_WEEK MONTH_NAME MONTH_DAY TIME
     * YEAR"
     * 
     * @param time
     *                the input time
     * @return a properly ISO8601 formatted time string.
     */
    public final static String buildISO8601Time(String time) {
        final String times[] = time.split(" ");
        final String iso8601Time = new StringBuffer(times[4]).append("-")
                .append(getMonthNumber(times[1])).append("-").append(times[2])
                .append("T").append(times[3]).append("Z").toString();
        return iso8601Time;
    }

    /**
     * Trivial method simply returning the month number given its abbreviated
     * name.
     * 
     * @param monthName
     *                the input month name.
     * @return the month number expressed as 2 digits in the range 01-12.
     */
    public final static String getMonthNumber(String monthName) {
        // TODO check this.
        if (monthName.equalsIgnoreCase("JAN"))
            return "01";
        else if (monthName.equalsIgnoreCase("FEB"))
            return "02";
        else if (monthName.equalsIgnoreCase("MAR"))
            return "03";
        else if (monthName.equalsIgnoreCase("APR"))
            return "04";
        else if (monthName.equalsIgnoreCase("MAY"))
            return "05";
        else if (monthName.equalsIgnoreCase("JUN"))
            return "06";
        else if (monthName.equalsIgnoreCase("JUL"))
            return "07";
        else if (monthName.equalsIgnoreCase("AUG"))
            return "08";
        else if (monthName.equalsIgnoreCase("SEP"))
            return "09";
        else if (monthName.equalsIgnoreCase("OCT"))
            return "10";
        else if (monthName.equalsIgnoreCase("NOV"))
            return "11";
        else if (monthName.equalsIgnoreCase("DEC"))
            return "12";
        else
            throw new IllegalArgumentException("Unsupported month name"
                    + monthName);
    }
    
    /**
     * Reduces the product's list by removing not interesting ones. As an
     * instance the dataset containing l2_flags will not be presented.
     * 
     * @param products
     *                The originating <code>String</code> array containing the
     *                list of products to be checked.
     * @return A <code>String</code> array containing a refined list of
     *         products
     * @todo remove products constraints when going to WMS EO specs.
     */
    static String[] refineProductList(String[] products) {
        final int inputProducts = products.length;
        int j = 0;
        final boolean[] accepted = new boolean[inputProducts];

        for (int i = 0; i < inputProducts; i++)
            if (isAcceptedItem(products[i])) {
                accepted[i] = true;
                j++;
            } else
                accepted[i] = false;
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
    
    /**
     * Check if the specified product is an accepted one
     */
    private static boolean isAcceptedItem(String productName) {
        if (productName.endsWith("_flags"))
        	return false;
        if (HDF4APSProperties.apsProducts.get(productName) != null)
            return true;
        return false;
    }
}
