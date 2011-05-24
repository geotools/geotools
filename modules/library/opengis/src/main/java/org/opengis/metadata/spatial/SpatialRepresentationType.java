/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.metadata.spatial;

import java.util.List;
import java.util.ArrayList;
import org.opengis.util.CodeList;
import org.opengis.annotation.UML;
import org.opengis.annotation.Profile;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;
import static org.opengis.annotation.ComplianceLevel.*;


/**
 * Method used to represent geographic information in the dataset.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/metadata/spatial/SpatialRepresentationType.java $
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 2.0
 */
@Profile (level=CORE)
@UML(identifier="MD_SpatialRepresentationTypeCode", specification=ISO_19115)
public final class SpatialRepresentationType extends CodeList<SpatialRepresentationType> {
    /**
     * Serial number for compatibility with different versions.
     */
    private static final long serialVersionUID = 4790487150664264363L;

    /**
     * List of all enumerations of this type.
     * Must be declared before any enum declaration.
     */
    private static final List<SpatialRepresentationType> VALUES = new ArrayList<SpatialRepresentationType>(6);

    /**
     * Vector data is used to represent geographic data.
     */
    @UML(identifier="vector", obligation=CONDITIONAL, specification=ISO_19115)
    public static final SpatialRepresentationType VECTOR = new SpatialRepresentationType("VECTOR");

    /**
     * Grid data is used to represent geographic data.
     */
    @UML(identifier="grid", obligation=CONDITIONAL, specification=ISO_19115)
    public static final SpatialRepresentationType GRID = new SpatialRepresentationType("GRID");

    /**
     * Textual or tabular data is used to represent geographic data.
     */
    @UML(identifier="textTable", obligation=CONDITIONAL, specification=ISO_19115)
    public static final SpatialRepresentationType TEXT_TABLE = new SpatialRepresentationType("TEXT_TABLE");

    /**
     * Triangulated irregular network.
     */
    @UML(identifier="tin", obligation=CONDITIONAL, specification=ISO_19115)
    public static final SpatialRepresentationType TIN = new SpatialRepresentationType("TIN");

    /**
     * Three-dimensional view formed by the intersecting homologous rays of an
     * overlapping pair of images.
     */
    @UML(identifier="stereoModel", obligation=CONDITIONAL, specification=ISO_19115)
    public static final SpatialRepresentationType STEREO_MODEL = new SpatialRepresentationType("STEREO_MODEL");

    /**
     * Scene from a video recording.
     */
    @UML(identifier="video", obligation=CONDITIONAL, specification=ISO_19115)
    public static final SpatialRepresentationType VIDEO = new SpatialRepresentationType("VIDEO");

    /**
     * Constructs an enum with the given name. The new enum is
     * automatically added to the list returned by {@link #values}.
     *
     * @param name The enum name. This name must not be in use by an other enum of this type.
     */
    private SpatialRepresentationType(final String name) {
        super(name, VALUES);
    }

    /**
     * Returns the list of {@code SpatialRepresentationType}s.
     *
     * @return The list of codes declared in the current JVM.
     */
    public static SpatialRepresentationType[] values() {
        synchronized (VALUES) {
            return VALUES.toArray(new SpatialRepresentationType[VALUES.size()]);
        }
    }

    /**
     * Returns the list of enumerations of the same kind than this enum.
     */
    public SpatialRepresentationType[] family() {
        return values();
    }

    /**
     * Returns the spatial representation type that matches the given string, or returns a
     * new one if none match it.
     *
     * @param code The name of the code to fetch or to create.
     * @return A code matching the given name.
     */
    public static SpatialRepresentationType valueOf(String code) {
        return valueOf(SpatialRepresentationType.class, code);
    }
}
