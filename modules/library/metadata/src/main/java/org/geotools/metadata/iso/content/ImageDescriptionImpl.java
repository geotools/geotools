/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotools.metadata.iso.content;

import org.opengis.metadata.Identifier;
import org.opengis.metadata.content.ImageDescription;
import org.opengis.metadata.content.ImagingCondition;


/**
 * Information about an image's suitability for use.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Touraïvane
 *
 * @since 2.1
 */
public class ImageDescriptionImpl extends CoverageDescriptionImpl implements ImageDescription {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -6168624828802439062L;

    /**
     * Illumination elevation measured in degrees clockwise from the target plane at
     * intersection of the optical line of sight with the Earths surface. For images from a
     * scanning device, refer to the centre pixel of the image.
     */
    private Double illuminationElevationAngle;

    /**
     * Illumination azimuth measured in degrees clockwise from true north at the time the
     * image is taken. For images from a scanning device, refer to the centre pixel of the image.
     */
    private Double illuminationAzimuthAngle;

    /**
     * Conditions affected the image.
     */
    private ImagingCondition imagingCondition;

    /**
     * Specifies the image quality.
     */
    private Identifier imageQualityCode;

    /**
     * Area of the dataset obscured by clouds, expressed as a percentage of the spatial extent.
     */
    private Double cloudCoverPercentage;

    /**
     * Image distributors code that identifies the level of radiometric and geometric
     * processing that has been applied.
     */
    private Identifier processingLevelCode;

    /**
     * Count of the number the number of lossy compression cycles performed on the image.
     * {@code null} if the information is not provided.
     */
    private Integer compressionGenerationQuantity;

    /**
     * Indication of whether or not triangulation has been performed upon the image.
     * {@code null} if the information is not provided.
     */
    private Boolean triangulationIndicator;

    /**
     * Indication of whether or not the radiometric calibration information for generating the
     * radiometrically calibrated standard data product is available.
     */
    private Boolean radiometricCalibrationDataAvailable;

    /**
     * Indication of whether or not constants are available which allow for camera calibration
     * corrections.
     */
    private Boolean cameraCalibrationInformationAvailable;

    /**
     * Indication of whether or not Calibration Reseau information is available.
     */
    private Boolean filmDistortionInformationAvailable;

    /**
     * Indication of whether or not lens aberration correction information is available.
     */
    private Boolean lensDistortionInformationAvailable;

    /**
     * Constructs an initially empty image description.
     */
    public ImageDescriptionImpl() {
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @since 2.4
     */
    public ImageDescriptionImpl(final ImageDescription source) {
        super(source);
    }

    /**
     * Returns the illumination elevation measured in degrees clockwise from the target plane at
     * intersection of the optical line of sight with the Earths surface. For images from a
     * scanning device, refer to the centre pixel of the image.
     */
    public Double getIlluminationElevationAngle() {
        return illuminationElevationAngle;
    }

    /**
     * Set the illumination elevation measured in degrees clockwise from the target plane at
     * intersection of the optical line of sight with the Earths surface. For images from a
     * scanning device, refer to the centre pixel of the image.
     */
    public synchronized void setIlluminationElevationAngle(final Double newValue) {
        checkWritePermission();
        illuminationElevationAngle = newValue;
    }

    /**
     * Returns the illumination azimuth measured in degrees clockwise from true north at the time
     * the image is taken. For images from a scanning device, refer to the centre pixel of the
     * image.
     */
    public Double getIlluminationAzimuthAngle() {
        return illuminationAzimuthAngle;
    }

    /**
     * Set the illumination azimuth measured in degrees clockwise from true north at the time the
     * image is taken. For images from a scanning device, refer to the centre pixel of the image.
     */
    public synchronized void setIlluminationAzimuthAngle(final Double newValue) {
        checkWritePermission();
        illuminationAzimuthAngle = newValue;
    }

    /**
     * Returns the conditions affected the image.
     */
    public ImagingCondition getImagingCondition() {
        return imagingCondition;
    }

    /**
     * Set the conditions affected the image.
     */
    public synchronized void setImagingCondition(final ImagingCondition newValue) {
        checkWritePermission();
        imagingCondition = newValue;
    }

    /**
     * Returns the specifies the image quality.
     */
    public Identifier getImageQualityCode() {
        return imageQualityCode;
    }

    /**
     * Set the specifies the image quality.
     */
    public synchronized void setImageQualityCode(final Identifier newValue) {
        checkWritePermission();
        imageQualityCode = newValue;
    }

    /**
     * Returns the area of the dataset obscured by clouds, expressed as a percentage of the spatial
     * extent.
     */
    public Double getCloudCoverPercentage() {
        return cloudCoverPercentage;
    }

    /**
     * Set the area of the dataset obscured by clouds, expressed as a percentage of the spatial
     * extent.
     */
    public synchronized void setCloudCoverPercentage(final Double newValue) {
        checkWritePermission();
        cloudCoverPercentage = newValue;
    }

    /**
     * Returns the image distributors code that identifies the level of radiometric and geometric
     * processing that has been applied.
     */
    public Identifier getProcessingLevelCode() {
        return processingLevelCode;
    }

    /**
     * Set the image distributors code that identifies the level of radiometric and geometric
     * processing that has been applied.
     */
    public synchronized void setProcessingLevelCode(final Identifier newValue) {
        checkWritePermission();
        processingLevelCode = newValue;
    }

    /**
     * Returns the count of the number the number of lossy compression cycles performed on the
     * image. Returns {@code null} if the information is not provided.
     */
    public Integer getCompressionGenerationQuantity() {
        return compressionGenerationQuantity;
    }

    /**
     * Set the count of the number the number of lossy compression cycles performed on the image.
     */
    public synchronized void setCompressionGenerationQuantity(final Integer newValue) {
        checkWritePermission();
        compressionGenerationQuantity = newValue;
    }

    /**
     * Returns the indication of whether or not triangulation has been performed upon the image.
     * Returns {@code null} if the information is not provided.
     */
    public Boolean getTriangulationIndicator() {
        return triangulationIndicator;
    }

    /**
     * Set the indication of whether or not triangulation has been performed upon the image.
     */
    public synchronized void setTriangulationIndicator(final Boolean newValue) {
        checkWritePermission();
        triangulationIndicator = newValue;
    }

    /**
     * Returns theiIndication of whether or not the radiometric calibration information for
     * generating the radiometrically calibrated standard data product is available.
     */
    public Boolean isRadiometricCalibrationDataAvailable() {
        return radiometricCalibrationDataAvailable;
    }

    /**
     * Set the indication of whether or not the radiometric calibration information for generating
     * the radiometrically calibrated standard data product is available.
     */
    public synchronized void setRadiometricCalibrationDataAvailable(final Boolean newValue) {
        checkWritePermission();
        radiometricCalibrationDataAvailable = newValue;
    }

    /**
     * Returns the indication of whether or not constants are available which allow for camera
     * calibration corrections.
     */
    public Boolean isCameraCalibrationInformationAvailable() {
        return cameraCalibrationInformationAvailable;
    }

    /**
     * Set the indication of whether or not constants are available which allow for camera
     * calibration corrections.
     */
    public synchronized void setCameraCalibrationInformationAvailable(final Boolean newValue) {
        checkWritePermission();
        cameraCalibrationInformationAvailable = newValue;
    }

    /**
     * Returns the indication of whether or not Calibration Reseau information is available.
     */
    public Boolean isFilmDistortionInformationAvailable() {
        return filmDistortionInformationAvailable;
    }

    /**
     * Set the indication of whether or not Calibration Reseau information is available.
     */
    public synchronized void setFilmDistortionInformationAvailable(final Boolean newValue) {
        checkWritePermission();
        filmDistortionInformationAvailable = newValue;
    }

    /**
     * Returns the indication of whether or not lens aberration correction information is available.
     */
    public Boolean isLensDistortionInformationAvailable() {
        return lensDistortionInformationAvailable;
    }

    /**
     * Set the indication of whether or not lens aberration correction information is available.
     */
    public synchronized void setLensDistortionInformationAvailable(final Boolean newValue) {
        checkWritePermission();
        lensDistortionInformationAvailable = newValue;
    }
}
