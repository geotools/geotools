/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2008, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.style;

import java.util.List;
import java.util.ArrayList;
import org.opengis.util.CodeList;

import org.opengis.annotation.XmlElement;


/**
 * Identifies the more general "type" of geometry that this style is meant to act upon.
 * In the current OGC SE specifications, this is an experimental element and
 * can take only one of the following values:
 * <p>
 * <ul>
 *   <li>{@code generic:point}</li>
 *   <li>{@code generic:line}</li>
 *   <li>{@code generic:polygon}</li>
 *   <li>{@code generic:text}</li>
 *   <li>{@code generic:raster}</li>
 *   <li>{@code generic:any}</li>
 * </ul>
 * <p>
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/style/SemanticType.java $
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding Implementation Specification 1.1.0</A>
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @since GeoAPI 2.2
 */
@XmlElement("SemanticTypeIdentifier")
public final class SemanticType extends CodeList<SemanticType> {
    /**
     * Serial number for compatibility with different versions.
     */
    private static final long serialVersionUID = -7328502367911363577L;

    /**
     * List of all enumerations of this type.
     * Must be declared before any enum declaration.
     */
    private static final List<SemanticType> VALUES = new ArrayList<SemanticType>(6);

    /**
     * Semantic identifies a point geometry.
     */
    @XmlElement("generic:point")
    public static final SemanticType POINT = new SemanticType("POINT");

    /**
     * Semantic identifies a line geometry.
     */
    @XmlElement("generic:line")
    public static final SemanticType LINE = new SemanticType("LINE");

    /**
     * Semantic identifies a polygon geometry.
     */
    @XmlElement("generic:polygon")
    public static final SemanticType POLYGON = new SemanticType("POLYGON");

    /**
     * Semantic identifies a text geometry.
     */
    @XmlElement("generic:text")
    public static final SemanticType TEXT = new SemanticType("TEXT");

    /**
     * Semantic identifies a raster geometry.
     */
    @XmlElement("generic:raster")
    public static final SemanticType RASTER = new SemanticType("RASTER");

    /**
     * Semantic identifies any geometry.
     */
    @XmlElement("generic:any")
    public static final SemanticType ANY = new SemanticType("ANY");

    /**
     * Constructs an enum with the given name. The new enum is
     * automatically added to the list returned by {@link #values}.
     *
     * @param name The enum name. This name must not be in use by an other enum of this type.
     */
    private SemanticType(final String name) {
        super(name, VALUES);
    }

    /**
     * Returns the list of {@code SemanticType}s.
     *
     * @return The list of codes declared in the current JVM.
     */
    public static SemanticType[] values() {
        synchronized (VALUES) {
            return VALUES.toArray(new SemanticType[VALUES.size()]);
        }
    }

    /**
     * Returns the list of enumerations of the same kind than this enum.
     */
    public SemanticType[] family() {
        return values();
    }

    /**
     * Returns the semantic type that matches the given string, or returns a
     * new one if none match it.
     *
     * @param code The name of the code to fetch or to create.
     * @return A code matching the given name.
     */
    public static SemanticType valueOf(String code) {
        return valueOf(SemanticType.class, code);
    }
}
