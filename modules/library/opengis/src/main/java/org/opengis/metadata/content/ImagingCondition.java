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

import java.util.List;
import java.util.ArrayList;

import org.opengis.util.CodeList;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Code which indicates conditions which may affect the image.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/metadata/content/ImagingCondition.java $
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 2.0
 */
@UML(identifier="MD_ImagingConditionCode", specification=ISO_19115)
public final class ImagingCondition extends CodeList<ImagingCondition> {
    /**
     * Serial number for compatibility with different versions.
     */
    private static final long serialVersionUID = -1948380148063658761L;

    /**
     * List of all enumerations of this type.
     * Must be declared before any enum declaration.
     */
    private static final List<ImagingCondition> VALUES = new ArrayList<ImagingCondition>(11);

    /**
     * Portion of the image is blurred.
     */
    @UML(identifier="blurredImage", obligation=CONDITIONAL, specification=ISO_19115)
    public static final ImagingCondition BLURRED_IMAGE = new ImagingCondition("BLURRED_IMAGE");

    /**
     * Portion of the image is partially obscured by cloud cover
     */
    @UML(identifier="cloud", obligation=CONDITIONAL, specification=ISO_19115)
    public static final ImagingCondition CLOUD = new ImagingCondition("CLOUD");

    /**
     * Acute angle between the plane of the ecliptic (the plane of the Earth's orbit) and
     * the plane of the celestial equator.
     */
    @UML(identifier="degradingObliquity", obligation=CONDITIONAL, specification=ISO_19115)
    public static final ImagingCondition DEGRADING_OBLIQUITY = new ImagingCondition("DEGRADING_OBLIQUITY");

    /**
     * Portion of the image is partially obscured by fog.
     */
    @UML(identifier="fog", obligation=CONDITIONAL, specification=ISO_19115)
    public static final ImagingCondition FOG = new ImagingCondition("FOG");

    /**
     * Portion of the image is partially obscured by heavy smoke or dust.
     */
    @UML(identifier="heavySmokeOrDust", obligation=CONDITIONAL, specification=ISO_19115)
    public static final ImagingCondition HEAVY_SMOKE_OR_DUST = new ImagingCondition("HEAVY_SMOKE_OR_DUST");

    /**
     * Image was taken at night.
     */
    @UML(identifier="night", obligation=CONDITIONAL, specification=ISO_19115)
    public static final ImagingCondition NIGHT = new ImagingCondition("NIGHT");

    /**
     * Image was taken during rainfall.
     */
    @UML(identifier="rain", obligation=CONDITIONAL, specification=ISO_19115)
    public static final ImagingCondition RAIN = new ImagingCondition("RAIN");

    /**
     * Image was taken during semi-dark conditions or twilight conditions.
     */
    @UML(identifier="semiDarkness", obligation=CONDITIONAL, specification=ISO_19115)
    public static final ImagingCondition SEMI_DARKNESS = new ImagingCondition("SEMI_DARKNESS");

    /**
     * Portion of the image is obscured by shadow.
     */
    @UML(identifier="shadow", obligation=CONDITIONAL, specification=ISO_19115)
    public static final ImagingCondition SHADOW = new ImagingCondition("SHADOW");

    /**
     * Portion of the image is obscured by snow.
     */
    @UML(identifier="snow", obligation=CONDITIONAL, specification=ISO_19115)
    public static final ImagingCondition SNOW = new ImagingCondition("SNOW");

    /**
     * The absence of collection data of a given point or area caused by the relative
     * location of topographic features which obstruct the collection path between the
     * collector(s) and the subject(s) of interest.
     */
    @UML(identifier="terrainMasking", obligation=CONDITIONAL, specification=ISO_19115)
    public static final ImagingCondition TERRAIN_MASKING = new ImagingCondition("TERRAIN_MASKING");

    /**
     * Constructs an enum with the given name. The new enum is
     * automatically added to the list returned by {@link #values}.
     *
     * @param name The enum name. This name must not be in use by an other enum of this type.
     */
    private ImagingCondition(final String name) {
        super(name, VALUES);
    }

    /**
     * Returns the list of {@code ImagingCondition}s.
     *
     * @return The list of codes declared in the current JVM.
     */
    public static ImagingCondition[] values() {
        synchronized (VALUES) {
            return VALUES.toArray(new ImagingCondition[VALUES.size()]);
        }
    }

    /**
     * Returns the list of enumerations of the same kind than this enum.
     */
    public ImagingCondition[] family() {
        return values();
    }

    /**
     * Returns the imaging condition that matches the given string, or returns a
     * new one if none match it.
     *
     * @param code The name of the code to fetch or to create.
     * @return A code matching the given name.
     */
    public static ImagingCondition valueOf(String code) {
        return valueOf(ImagingCondition.class, code);
    }
}
