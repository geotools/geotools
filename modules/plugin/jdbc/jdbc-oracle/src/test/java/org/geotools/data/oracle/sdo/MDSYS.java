package org.geotools.data.oracle.sdo;

/**
 * Mimics Oracle MDSYS functions for building geometries. Useful for creating test objects.
 * 
 * @author Martin Davis
 * 
 */
public class MDSYS {

    protected static final int NULL = -1;

    public static SDO_GEOMETRY SDO_GEOMETRY(int gType, int srid, int ptType, int[] elemInfo,
            double[] ordinates) {
        return new SDO_GEOMETRY(gType, srid, elemInfo, ordinates);
    }

    public static SDO_GEOMETRY SDO_GEOMETRY(int gType, int srid, double[] ptType, int null1,
            int null2) {
        return new SDO_GEOMETRY(gType, srid, ptType);
    }

    public static double[] SDO_POINT_TYPE(double x, double y, double z) {
        if (z == NULL)
            z = Double.NaN;
        return new double[] { x, y, z };
    }

    public static int[] SDO_ELEM_INFO_ARRAY(int... i) {
        return i;
    }

    public static double[] SDO_ORDINATE_ARRAY(double... d) {
        return d;
    }

    public static class SDO_GEOMETRY {

        int gType;

        int srid;

        double[] ptType;

        int[] elemInfo;

        double[] ordinates;

        public SDO_GEOMETRY(int gType, int srid, int[] elemInfo, double[] ordinates) {
            this.gType = gType;
            this.srid = srid;
            this.elemInfo = elemInfo;
            this.ordinates = ordinates;
        }

        public SDO_GEOMETRY(int gType, int srid, double[] ptType) {
            this.gType = gType;
            this.srid = srid;
            this.ptType = ptType;
        }

    }
}
