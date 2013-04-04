package org.geotools.data.sqlserver.reader;

/**
 * @author Anders Bakkevold, Bouvet
 *
 * @source $URL$
 */
public enum Type {

    POINT (1),
    LINESTRING(2),
    POLYGON(3),
    MULTIPOINT(4),
    MULTILINESTRING(5),
    MULTIPOLYGON(6),
    GEOMETRYCOLLECTION(7),
    CIRCULARSTRING(8),
    COMPOUNDCURVE(9),
    CURVEPOLYGON(10),
    FULLGLOBE(11);

    private int value;

    private Type(int value) {
        this.value = value;
    }

    public static Type findType(int value) {
        for (Type type : Type.values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new IllegalArgumentException();
    }
}
