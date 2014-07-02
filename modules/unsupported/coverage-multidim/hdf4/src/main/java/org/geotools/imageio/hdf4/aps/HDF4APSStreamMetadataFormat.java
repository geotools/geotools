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

import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadataFormatImpl;

class HDF4APSStreamMetadataFormat extends IIOMetadataFormatImpl {

    public HDF4APSStreamMetadataFormat() {
        super(HDF4APSStreamMetadata.nativeMetadataFormatName,
                IIOMetadataFormatImpl.CHILD_POLICY_ALL);
        addElement("StandardHDFAPSProperties",
                HDF4APSStreamMetadata.nativeMetadataFormatName, CHILD_POLICY_ALL);

        addElement("FileAttributes", "StandardHDFAPSProperties", CHILD_POLICY_EMPTY);
        addAttribute("FileAttributes", HDF4APSProperties.STD_FA_FILE, DATATYPE_STRING, false, null);
        addAttribute("FileAttributes", HDF4APSProperties.STD_FA_FILECLASSIFICATION, DATATYPE_STRING, true, null);
        addAttribute("FileAttributes", HDF4APSProperties.STD_FA_FILESTATUS, DATATYPE_STRING, false, null);
        addAttribute("FileAttributes", HDF4APSProperties.STD_FA_FILETITLE, DATATYPE_STRING, false, null);
        addAttribute("FileAttributes", HDF4APSProperties.STD_FA_FILEVERSION, DATATYPE_STRING, false, null);
        addAttribute("FileAttributes", HDF4APSProperties.STD_FA_CREATEAGENCY, DATATYPE_STRING, false, null);
        addAttribute("FileAttributes", HDF4APSProperties.STD_FA_CREATESOFTWARE, DATATYPE_STRING, false, null);
        addAttribute("FileAttributes", HDF4APSProperties.STD_FA_CREATEPLATFORM, DATATYPE_STRING, false, null);
        addAttribute("FileAttributes", HDF4APSProperties.STD_FA_CREATETIME, DATATYPE_STRING, false, null);
        addAttribute("FileAttributes", HDF4APSProperties.STD_FA_CREATEUSER, DATATYPE_STRING, false, null);

        addElement("TimeAttributes", "StandardHDFAPSProperties", CHILD_POLICY_EMPTY);
        addAttribute("TimeAttributes", HDF4APSProperties.STD_TA_TIMESTART, DATATYPE_STRING, true, null);
        addAttribute("TimeAttributes", HDF4APSProperties.STD_TA_TIMESTARTYEAR, DATATYPE_INTEGER, true, null);
        addAttribute("TimeAttributes", HDF4APSProperties.STD_TA_TIMESTARTDAY, DATATYPE_INTEGER, true, null);
        addAttribute("TimeAttributes", HDF4APSProperties.STD_TA_TIMESTARTTIME, DATATYPE_INTEGER, true, null);
        addAttribute("TimeAttributes", HDF4APSProperties.STD_TA_TIMEEND, DATATYPE_STRING, true, null);
        addAttribute("TimeAttributes", HDF4APSProperties.STD_TA_TIMEENDYEAR, DATATYPE_INTEGER, true, null);
        addAttribute("TimeAttributes", HDF4APSProperties.STD_TA_TIMEENDDAY, DATATYPE_INTEGER, true, null);
        addAttribute("TimeAttributes", HDF4APSProperties.STD_TA_TIMEENDTIME, DATATYPE_INTEGER, true, null);
        addAttribute("TimeAttributes", HDF4APSProperties.STD_TA_TIMEDAYNIGHT, DATATYPE_STRING, true, null);

        addElement("SensorAttributes", "StandardHDFAPSProperties", CHILD_POLICY_EMPTY);
        addAttribute("SensorAttributes", HDF4APSProperties.STD_SA_SENSOR, DATATYPE_STRING, true, null);
        addAttribute("SensorAttributes", HDF4APSProperties.STD_SA_SENSORPLATFORM, DATATYPE_INTEGER, true, null);
        addAttribute("SensorAttributes", HDF4APSProperties.STD_SA_SENSORAGENCY, DATATYPE_INTEGER, true, null);
        addAttribute("SensorAttributes", HDF4APSProperties.STD_SA_SENSORTYPE, DATATYPE_INTEGER, true, null);
        addAttribute("SensorAttributes", HDF4APSProperties.STD_SA_SENSORSPECTRUM, DATATYPE_STRING, true, null);
        addAttribute("SensorAttributes", HDF4APSProperties.STD_SA_SENSORNUMBEROFBANDS, DATATYPE_INTEGER, true, null);
        addAttribute("SensorAttributes", HDF4APSProperties.STD_SA_SENSORBANDUNITS, DATATYPE_INTEGER, true, null);
        addAttribute("SensorAttributes", HDF4APSProperties.STD_SA_SENSORBANDS, DATATYPE_INTEGER, true, null);
        addAttribute("SensorAttributes", HDF4APSProperties.STD_SA_SENSORBANDWIDTHS, DATATYPE_STRING, true, null);
        addAttribute("SensorAttributes", HDF4APSProperties.STD_SA_SENSORNOMINALALTITUDEINKM, DATATYPE_STRING, true, null);
        addAttribute("SensorAttributes", HDF4APSProperties.STD_SA_SENSORSCANWIDTHINKM, DATATYPE_STRING, true, null);
        addAttribute("SensorAttributes", HDF4APSProperties.STD_SA_SENSORRESOLUTIONINKM, DATATYPE_STRING, true, null);
        addAttribute("SensorAttributes", HDF4APSProperties.STD_SA_SENSORPLATFORMTYPE, DATATYPE_STRING, true, null);

        addElement("FileProductsAttributes", HDF4APSStreamMetadata.nativeMetadataFormatName, CHILD_POLICY_SOME);
        addElement("InputParameterAttributes", "FileProductsAttributes", CHILD_POLICY_EMPTY);
        addAttribute("InputParameterAttributes", HDF4APSProperties.PFA_IPA_INPUTCALIBRATIONFILE, DATATYPE_STRING, false, null);
        addAttribute("InputParameterAttributes", HDF4APSProperties.PFA_IPA_INPUTPARAMETER, DATATYPE_STRING, false, null);
        addAttribute("InputParameterAttributes", HDF4APSProperties.PFA_IPA_INPUTMASKSINT, DATATYPE_STRING, false, null);
        addAttribute("InputParameterAttributes", HDF4APSProperties.PFA_IPA_INPUTMASKS, DATATYPE_STRING, false, null);
        addAttribute("InputParameterAttributes", HDF4APSProperties.PFA_IPA_PRODLIST, DATATYPE_STRING, false, null);
        addAttribute("InputParameterAttributes", HDF4APSProperties.PFA_IPA_PROCESSINGVERSION, DATATYPE_STRING, false, null);

        addElement("NavigationAttributes", "FileProductsAttributes", CHILD_POLICY_EMPTY);
        addAttribute("NavigationAttributes", HDF4APSProperties.PFA_NA_NAVTYPE, DATATYPE_STRING, false, null);
        addAttribute("NavigationAttributes", HDF4APSProperties.PFA_NA_MAPPROJECTIONSYSTEM, DATATYPE_STRING, false, null);
        addAttribute("NavigationAttributes", HDF4APSProperties.PFA_NA_MAPPROJECTION, DATATYPE_STRING, false, null);
        addAttribute("NavigationAttributes", HDF4APSProperties.PFA_NA_MAPPEDUPPERLEFT, DATATYPE_STRING, false, null);
        addAttribute("NavigationAttributes", HDF4APSProperties.PFA_NA_MAPPEDUPPERRIGHT, DATATYPE_STRING, false, null);
        addAttribute("NavigationAttributes", HDF4APSProperties.PFA_NA_MAPPEDLOWERLEFT, DATATYPE_STRING, false, null);
        addAttribute("NavigationAttributes", HDF4APSProperties.PFA_NA_MAPPEDLOWERRIGHT, DATATYPE_STRING, false, null);

        addElement("InputGeographicalCoverageAttributes", "FileProductsAttributes", CHILD_POLICY_EMPTY);
        addAttribute("InputGeographicalCoverageAttributes", HDF4APSProperties.PFA_IGCA_LOCALEUPPERLEFT, DATATYPE_STRING, false, null);
        addAttribute("InputGeographicalCoverageAttributes", HDF4APSProperties.PFA_IGCA_LOCALEUPPERRIGHT, DATATYPE_STRING, false, null);
        addAttribute("InputGeographicalCoverageAttributes", HDF4APSProperties.PFA_IGCA_LOCALELOWERLEFT, DATATYPE_STRING, false, null);
        addAttribute("InputGeographicalCoverageAttributes", HDF4APSProperties.PFA_IGCA_LOCALELOWERRIGHT, DATATYPE_STRING, false, null);
        addAttribute("InputGeographicalCoverageAttributes", HDF4APSProperties.PFA_IGCA_LOCALENWCORNER, DATATYPE_STRING, false, null);
        addAttribute("InputGeographicalCoverageAttributes", HDF4APSProperties.PFA_IGCA_LOCALENECORNER, DATATYPE_STRING, false, null);
        addAttribute("InputGeographicalCoverageAttributes", HDF4APSProperties.PFA_IGCA_LOCALESWCORNER, DATATYPE_STRING, false, null);
        addAttribute("InputGeographicalCoverageAttributes", HDF4APSProperties.PFA_IGCA_LOCALESECORNER, DATATYPE_STRING, false, null);

//        addElement("Projection", HDF4APSStreamMetadata.nativeMetadataFormatName,
//                CHILD_POLICY_EMPTY);
//        addAttribute("Projection", "Name", DATATYPE_STRING, true, null);
//        addAttribute("Projection", "FullName", DATATYPE_STRING, true, null);
//        addAttribute("Projection", "Code", DATATYPE_STRING, true, null);
//        addAttribute("Projection", "Projection", DATATYPE_STRING, true, null);
//        addAttribute("Projection", "Zone", DATATYPE_STRING, true, null);
//        addAttribute("Projection", "Datum", DATATYPE_INTEGER, true, null);
//        addAttribute("Projection", "param0", DATATYPE_FLOAT, true, null);
//        addAttribute("Projection", "param1", DATATYPE_FLOAT, true, null);
//        addAttribute("Projection", "param2", DATATYPE_FLOAT, true, null);
//        addAttribute("Projection", "param3", DATATYPE_FLOAT, true, null);
//        addAttribute("Projection", "param4", DATATYPE_FLOAT, true, null);
//        addAttribute("Projection", "param5", DATATYPE_FLOAT, true, null);
//        addAttribute("Projection", "param6", DATATYPE_FLOAT, true, null);
//        addAttribute("Projection", "param7", DATATYPE_FLOAT, true, null);
//        addAttribute("Projection", "param8", DATATYPE_FLOAT, true, null);
//        addAttribute("Projection", "param9", DATATYPE_FLOAT, true, null);
//        addAttribute("Projection", "param10", DATATYPE_FLOAT, true, null);
//        addAttribute("Projection", "param11", DATATYPE_FLOAT, true, null);
//        addAttribute("Projection", "param12", DATATYPE_FLOAT, true, null);
//        addAttribute("Projection", "param13", DATATYPE_FLOAT, true, null);
//        addAttribute("Projection", "param14", DATATYPE_FLOAT, true, null);
//        addAttribute("Projection", "Width", DATATYPE_FLOAT, true, null);
//        addAttribute("Projection", "Height", DATATYPE_FLOAT, true, null);
//        addAttribute("Projection", "Longitude_1", DATATYPE_FLOAT, true, null);
//        addAttribute("Projection", "Latitude_1", DATATYPE_FLOAT, true, null);
//        addAttribute("Projection", "Pixel_1", DATATYPE_FLOAT, true, null);
//        addAttribute("Projection", "Line_1", DATATYPE_FLOAT, true, null);
//        addAttribute("Projection", "Longitude_2", DATATYPE_FLOAT, true, null);
//        addAttribute("Projection", "Latitude_2", DATATYPE_FLOAT, true, null);
//        addAttribute("Projection", "Pixel_2", DATATYPE_FLOAT, true, null);
//        addAttribute("Projection", "Line_2", DATATYPE_FLOAT, true, null);
//        addAttribute("Projection", "Delta", DATATYPE_FLOAT, true, null);
//        addAttribute("Projection", "Aspect", DATATYPE_FLOAT, true, null);

//        addElement("Products", HDF4APSStreamMetadata.nativeMetadataFormatName,
//                CHILD_POLICY_SEQUENCE);
//        addElement("Product", "Products", CHILD_POLICY_ALL);

    }

    public boolean canNodeAppear(String elementName,ImageTypeSpecifier imageType) {
        // @todo @task TODO
        return true;
    }
}
