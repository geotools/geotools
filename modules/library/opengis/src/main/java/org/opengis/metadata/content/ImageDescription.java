/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.metadata.content;

import org.opengis.metadata.Identifier;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Information about an image's suitability for use.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author  Martin Desruisseaux (IRD)
 * @author  Cory Horner (Refractions Research)
 * @since   GeoAPI 2.0
 */
@UML(identifier="MD_ImageDescription", specification=ISO_19115)
public interface ImageDescription extends CoverageDescription {
    /**
     * Illumination elevation measured in degrees clockwise from the target plane at
     * intersection of the optical line of sight with the Earth's surface. For images from a
     * scanning device, refer to the centre pixel of the image.
     *
     * @return A value between -90&deg; and +90&deg;, or {@code null} if unspecified.
     */
    @UML(identifier="illuminationElevationAngle", obligation=OPTIONAL, specification=ISO_19115)
    Double getIlluminationElevationAngle();

    /**
     * Illumination azimuth measured in degrees clockwise from true north at the time the
     * image is taken. For images from a scanning device, refer to the centre pixel of the image.
     *
     * @return A value between 0&deg; and 360&deg;, or {@code null} if unspecified.
     */
    @UML(identifier="illuminationAzimuthAngle", obligation=OPTIONAL, specification=ISO_19115)
    Double getIlluminationAzimuthAngle();

    /**
     * Conditions affected the image.
     *
     * @return Conditions affected the image, or {@code null} if unknown.
     */
    @UML(identifier="imagingCondition", obligation=OPTIONAL, specification=ISO_19115)
    ImagingCondition getImagingCondition();

    /**
     * Specifies the image quality.
     *
     * @return The image quality, or {@code null} if unknown.
     */
    @UML(identifier="imageQualityCode", obligation=OPTIONAL, specification=ISO_19115)
    Identifier getImageQualityCode();

    /**
     * Area of the dataset obscured by clouds, expressed as a percentage of the spatial extent.
     *
     * @return A value between 0 and 100, or {@code null} if unknown.
     */
    @UML(identifier="cloudCoverPercentage", obligation=OPTIONAL, specification=ISO_19115)
    Double getCloudCoverPercentage();

    /**
     * Image distributor's code that identifies the level of radiometric and geometric
     * processing that has been applied.
     *
     * @return The level of radiometric and geometric processing that has been applied,
     *         or {@code null} if unknown.
     */
    @UML(identifier="processingLevelCode", obligation=OPTIONAL, specification=ISO_19115)
    Identifier getProcessingLevelCode();

    /**
     * Count of the number the number of lossy compression cycles performed on the image.
     * Returns {@code null} if the information is not provided.
     *
     * @return The number the number of lossy compression cycles performed on the image,
     *         or {@code null} if unknown.
     */
    @UML(identifier="compressionGenerationQuantity", obligation=OPTIONAL, specification=ISO_19115)
    Integer getCompressionGenerationQuantity();

    /**
     * Indication of whether or not triangulation has been performed upon the image.
     * Returns {@code null} if the information is not provided.
     *
     * @return Whether or not triangulation has been performed upon the image,
     *         or {@code null} if unknown.
     */
    @UML(identifier="triangulationIndicator", obligation=OPTIONAL, specification=ISO_19115)
    Boolean getTriangulationIndicator();

    /**
     * Indication of whether or not the radiometric calibration information for generating the
     * radiometrically calibrated standard data product is available.
     *
     * @return Whether or not the radiometric calibration information is available,
     *         or {@code null} if unknown.
     */
    @UML(identifier="radiometricCalibrationDataAvailability", obligation=OPTIONAL, specification=ISO_19115)
    Boolean isRadiometricCalibrationDataAvailable();

    /**
     * Indication of whether or not constants are available which allow for camera calibration
     * corrections.
     *
     * @return Whether or not constants are available for camera calibration corrections,
     *         or {@code null} if unknown.
     */
    @UML(identifier="cameraCalibrationInformationAvailability", obligation=OPTIONAL, specification=ISO_19115)
    Boolean isCameraCalibrationInformationAvailable();

    /**
     * Indication of whether or not Calibration Reseau information is available.
     *
     * @return Whether or not Calibration Reseau information is available,
     *         or {@code null} if unknown.
     */
    @UML(identifier="filmDistortionInformationAvailability", obligation=OPTIONAL, specification=ISO_19115)
    Boolean isFilmDistortionInformationAvailable();

    /**
     * Indication of whether or not lens aberration correction information is available.
     *
     * @return Whether or not lens aberration correction information is available,
     *         or {@code null} if unknown.
     */
    @UML(identifier="lensDistortionInformationAvailability", obligation=OPTIONAL, specification=ISO_19115)
    Boolean isLensDistortionInformationAvailable();
}
