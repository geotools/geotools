package org.geotools.data;

/**
 *
 * @author Gertjan
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Triangle;

public enum GeometryType {

    // Make sure there is no duplicate class or extension use
    UNSUPPORTED(Class.class, "null"),
    ALL(Geometry.class, ""),
    POINT(Point.class, "_p"),
    LINE(LineString.class, "_l"),
    POLYGON(Polygon.class, "_v"),
    MULTIPOINT(MultiPoint.class, "_mp"),
    MULTILINE(MultiLineString.class, "_ml"),
    MULTIPOLYGON(MultiPolygon.class, "_mv"),
    COORDINATE(Coordinate.class, "_c"),
    TRIANGLE(Triangle.class, "_t");
    private Class geomId;
    private String extension;

    // Enum custom constructor
    private GeometryType(Class geomId, String extension) {
        this.geomId = geomId;
        this.extension = extension;
    }

    // Get geometryClass of the GeometryType
    public Class getGeometryClass() {
        return geomId;
    }

    // Get String extension of the GeometryType
    public String getExtension() {
        return extension;
    }

    // Set extension of the GeometryType
    public static void setExtension(GeometryType type, String extension) {
        type.extension = extension;
    }

    public static String[] getTypeNames(String filename, GeometryType... types) {
        ArrayList extensionList;

        if (types.length == 0) {
            extensionList = new ArrayList(Arrays.asList(GeometryType.values()));
            extensionList.remove(GeometryType.ALL);
            extensionList.remove(GeometryType.UNSUPPORTED);
        } else {
            extensionList = new ArrayList(Arrays.asList(types));
        }

        String[] typeNames = new String[extensionList.size()];
        for (int i = 0; i < typeNames.length; i++) {
            typeNames[i] = filename + ((GeometryType) extensionList.get(i)).getExtension();
        }

        return typeNames;
    }

    public static Map getTypeNamesMap(String filename, GeometryType... types) {
        ArrayList extensionList;

        if (types.length == 0) {
            extensionList = new ArrayList(Arrays.asList(GeometryType.values()));
            extensionList.remove(GeometryType.ALL);
            extensionList.remove(GeometryType.UNSUPPORTED);
        } else {
            extensionList = new ArrayList(Arrays.asList(types));
        }

        Map typenameMap = new HashMap();
        for (int i = 0; i < extensionList.size(); i++) {
            GeometryType geometryType = (GeometryType) extensionList.get(i);
            String name = filename + geometryType.getExtension();
            typenameMap.put(name, geometryType);
        }

        return typenameMap;
    }

    // Return Geometry Class of given String extension
    public static Class getClassByExtension(String extension) {
        for (GeometryType geomType : GeometryType.values()) {
            if (geomType.getExtension().equals(extension)) {
                return geomType.getGeometryClass();
            }
        }
        return null;
    }

    // Return String extension of given Geometry Class
    public static String getExtensionByClass(Geometry typeClass) {
        for (GeometryType geomType : GeometryType.values()) {
            if (geomType.getGeometryClass().equals(typeClass.getClass())) {
                return geomType.getExtension();
            }
        }
        return null;
    }

    // Get Geometry Type of given Geometry Class
    public static GeometryType getTypeByClass(Geometry typeClass) {
        for (GeometryType geomType : GeometryType.values()) {
            if (geomType.getGeometryClass().equals(typeClass.getClass())) {
                return geomType;
            }
        }
        return null;
    }

    // Get Geometry Type of given Geometry extension
    public static GeometryType getTypeByExtension(String typeExtension) {
        for (GeometryType geomType : GeometryType.values()) {
            if (geomType.getExtension().equals(typeExtension)) {
                return geomType;
            }
        }
        return null;
    }

    public static String stripExtensionFromTypename(String typename, GeometryType type) {
        String ext = type.getExtension();
        if (typename.endsWith(ext)) {
            typename = typename.substring(0, typename.length() - ext.length());
        }

        return typename;
    }
};
