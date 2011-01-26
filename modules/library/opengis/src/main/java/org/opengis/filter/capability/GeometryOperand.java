/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005 Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.filter.capability;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.opengis.feature.type.Name;


/**
 * Enumeration of the different {@code GeometryOperand} types.
 * <p>
 * <pre>
 *  &lt;xsd:simpleType name="GeometryOperandType">
 *    &lt;xsd:restriction base="xsd:QName">
 *        &lt;xsd:enumeration value="gml:Envelope"/>
 *        &lt;xsd:enumeration value="gml:Point"/>
 *        &lt;xsd:enumeration value="gml:LineString"/>
 *        &lt;xsd:enumeration value="gml:Polygon"/>
 *        &lt;xsd:enumeration value="gml:ArcByCenterPoint"/>
 *        &lt;xsd:enumeration value="gml:CircleByCenterPoint"/>
 *        &lt;xsd:enumeration value="gml:Arc"/>
 *        &lt;xsd:enumeration value="gml:Circle"/>
 *        &lt;xsd:enumeration value="gml:ArcByBulge"/>
 *        &lt;xsd:enumeration value="gml:Bezier"/>
 *        &lt;xsd:enumeration value="gml:Clothoid"/>
 *        &lt;xsd:enumeration value="gml:CubicSpline"/>
 *        &lt;xsd:enumeration value="gml:Geodesic"/>
 *        &lt;xsd:enumeration value="gml:OffsetCurve"/>
 *        &lt;xsd:enumeration value="gml:Triangle"/>
 *        &lt;xsd:enumeration value="gml:PolyhedralSurface"/>
 *        &lt;xsd:enumeration value="gml:TriangulatedSurface"/>
 *        &lt;xsd:enumeration value="gml:Tin"/>
 *        &lt;xsd:enumeration value="gml:Solid"/>
 *     &lt;/xsd:restriction>
 *  &lt;/xsd:simpleType>
 *  </pre>
 * </p>
 *
 * @author Justin Deoliveira (The Open Planning Project)
 * @author Martin Desruisseaux (Geomatys)
 */
public final class GeometryOperand implements Name, Serializable {
    /**
     * For cross-version compatibility.
     */
    private static final long serialVersionUID = -9006169053542932716L;

    /**
     * The pool of operands created up to date.
     */
    private static final Map<GeometryOperand, GeometryOperand> POOL =
            new HashMap<GeometryOperand, GeometryOperand>();

    /** {@code "http://www.opengis.net/gml/Envelope"} */
    public static final GeometryOperand Envelope = new GeometryOperand("Envelope");

    /** {@code "http://www.opengis.net/gml/Point"} */
    public static final GeometryOperand Point = new GeometryOperand("Point");

    /** {@code "http://www.opengis.net/gml/LineString"} */
    public static final GeometryOperand LineString = new GeometryOperand("LineString");

    /** {@code "http://www.opengis.net/gml/Polygon"} */
    public static final GeometryOperand Polygon = new GeometryOperand("Polygon");

    /** {@code "http://www.opengis.net/gml/ArcByCenterPoint"} */
    public static final GeometryOperand ArcByCenterPoint = new GeometryOperand("ArcByCenterPoint");

    /** {@code "http://www.opengis.net/gml/CircleByCenterPoint"} */
    public static final GeometryOperand CircleByCenterPoint = new GeometryOperand("CircleByCenterPoint");

    /** {@code "http://www.opengis.net/gml/Arc"} */
    public static final GeometryOperand Arc = new GeometryOperand("Arc");

    /** {@code "http://www.opengis.net/gml/Circle"} */
    public static final GeometryOperand Circle = new GeometryOperand("Circle");

    /** {@code "http://www.opengis.net/gml/ArcByBulge"} */
    public static final GeometryOperand ArcByBulge = new GeometryOperand("ArcByBulge");

    /** {@code "http://www.opengis.net/gml/Bezier"} */
    public static final GeometryOperand Bezier = new GeometryOperand("Bezier");

    /** {@code "http://www.opengis.net/gml/Clothoid"} */
    public static final GeometryOperand Clothoid = new GeometryOperand("Clothoid");

    /** {@code "http://www.opengis.net/gml/CubicSpline"} */
    public static final GeometryOperand CubicSpline = new GeometryOperand("CubicSpline");

    /** {@code "http://www.opengis.net/gml/Geodesic"} */
    public static final GeometryOperand Geodesic = new GeometryOperand("Geodesic");

    /** {@code "http://www.opengis.net/gml/OffsetCurve"} */
    public static final GeometryOperand OffsetCurve = new GeometryOperand("OffsetCurve");

    /** {@code "http://www.opengis.net/gml/Triangle"} */
    public static final GeometryOperand Triangle = new GeometryOperand("Triangle");

    /** {@code "http://www.opengis.net/gml/PolyhedralSurface"} */
    public static final GeometryOperand PolyhedralSurface = new GeometryOperand("PolyhedralSurface");

    /** {@code "http://www.opengis.net/gml/TriangulatedSurface"} */
    public static final GeometryOperand TriangulatedSurface = new GeometryOperand("TriangulatedSurface");

    /** {@code "http://www.opengis.net/gml/Tin"} */
    public static final GeometryOperand Tin = new GeometryOperand("Tin");

    /** {@code "http://www.opengis.net/gml/Solid"} */
    public static final GeometryOperand Solid = new GeometryOperand("Solid");

    /**
     * The namespace URI.
     */
    private final String namespaceURI;

    /**
     * The name.
     */
    private final String name;

    /**
     * Creates an operand in the {@code "http://www.opengis.net/gml"} namespace.
     */
    private GeometryOperand(final String name) {
        this("http://www.opengis.net/gml", name);
    }

    /**
     * Creates an operand in the given namespace.
     */
    private GeometryOperand(final String namespaceURI, final String name) {
        this.namespaceURI = namespaceURI;
        this.name = name;
        POOL.put(this, this);
    }

    /**
     * Returns the geometry operand for the given name.
     *
     * @param  namespaceURI The namespace URI, or {@code null} for the default one.
     * @param  name The operand name.
     * @return The geometry operand, or {@code null} if none was found.
     */
    public static GeometryOperand get(String namespaceURI, String name) {
        if (namespaceURI == null || namespaceURI.trim().length() == 0) {
            namespaceURI = "http://www.opengis.net/gml";
        }
        name = name.trim();
        return POOL.get(new GeometryOperand(namespaceURI, name));
    }

    /**
     * Retrieve the Local name.
     */
    public String getLocalPart() {
        return name;
    }

    /**
     * Returns the name space, which is usually {@code "http://www.opengis.net/gml"}.
     */
    public String getNamespaceURI() {
        return namespaceURI;
    }

    /**
     * Convert this name to a complete URI.
     */
    public String getURI() {
        return namespaceURI + '/' + name;
    }

    /**
     * Returns {@code false} since this name has a {@linkplain #getNamespaceURI namespace}.
     */
    public boolean isGlobal() {
        return false;
    }

    public String getSeparator() {
    	return "#";
    }
    
    /**
     * Returns a hash code value for this operand.
     */
    @Override
    public int hashCode() {
        return namespaceURI.hashCode() + 37*name.hashCode();
    }

    /**
     * Compares this operand with the specified value for equality.
     */
    @Override
    public boolean equals(final Object other) {
        if (other != null && other instanceof Name) {
            final Name that = (Name) other;
            return namespaceURI.equals(that.getNamespaceURI()) && name.equals(that.getLocalPart());
        }
        return false;
    }

    /**
     * Returns a string representation of this operand.
     */
    @Override
    public String toString() {
        return getURI();
    }

    /**
     * Returns the canonical instance on deserialization.
     */
    private Object readResolve() throws ObjectStreamException {
        final GeometryOperand unique = POOL.get(this);
        return (unique != null) ? unique : this;
    }
}
