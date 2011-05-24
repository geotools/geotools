/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.metadata.distribution;

import java.util.Collection;
import javax.measure.unit.Unit;
import org.opengis.util.InternationalString;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Information about the media on which the resource can be distributed.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/metadata/distribution/Medium.java $
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 2.0
 */
@UML(identifier="MD_Medium", specification=ISO_19115)
public interface Medium {
    /**
     * Name of the medium on which the resource can be received.
     *
     * @return Name of the medium, or {@code null}.
     */
    @UML(identifier="name", obligation=OPTIONAL, specification=ISO_19115)
    MediumName getName();

    /**
     * Density at which the data is recorded.
     * The numbers should be greater than zero.
     *
     * @return Density at which the data is recorded, or {@code null}.
     */
    @UML(identifier="density", obligation=OPTIONAL, specification=ISO_19115)
    Collection<Double> getDensities();

    /**
     * Units of measure for the recording density.
     *
     * @return Units of measure for the recording density, or {@code null}.
     */
    @UML(identifier="densityUnits", obligation=CONDITIONAL, specification=ISO_19115)
    Unit<?> getDensityUnits();

    /**
     * Number of items in the media identified.
     * Returns {@code null} if unknown.
     *
     * @return Number of items in the media identified, or {@code null}.
     */
    @UML(identifier="volumes", obligation=OPTIONAL, specification=ISO_19115)
    Integer getVolumes();

    /**
     * Method used to write to the medium.
     *
     * @return Method used to write to the medium, or {@code null}.
     */
    @UML(identifier="mediumFormat", obligation=OPTIONAL, specification=ISO_19115)
    Collection<MediumFormat> getMediumFormats();

    /**
     * Description of other limitations or requirements for using the medium.
     *
     * @return Description of other limitations for using the medium, or {@code null}.
     */
    @UML(identifier="mediumNote", obligation=OPTIONAL, specification=ISO_19115)
    InternationalString getMediumNote();
}
