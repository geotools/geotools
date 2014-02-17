package org.geotools.geopkg;

import org.geotools.geometry.jts.Geometries;

/**
 * Feature entry in a geopackage.
 * <p>
 * This class corresponds to the "geometry_columns" table.
 * </p>
 * @author Justin Deoliveira, OpenGeo
 */
public class FeatureEntry extends Entry {

    Geometries geometryType;
    boolean z = false;
    boolean m = false;
    String geometryColumn;

    public FeatureEntry() {
        setDataType(DataType.Feature);
    }

    public String getGeometryColumn() {
        return geometryColumn;
    }

    public void setGeometryColumn(String geometryColumn) {
        this.geometryColumn = geometryColumn;
    }

    public Geometries getGeometryType() {
        return geometryType;
    }

    public void setGeometryType(Geometries geometryType) {
        this.geometryType = geometryType;
    }

    void init(FeatureEntry e) {
        super.init(e);
        setGeometryColumn(e.getGeometryColumn());
        setGeometryType(e.getGeometryType());
        setZ(e.isZ());
        setM(e.isM());
    }
    

    public boolean isZ() {
        return z;
    }

    public void setZ(boolean z) {
        this.z = z;
    }

    public boolean isM() {
        return m;
    }

    public void setM(boolean m) {
        this.m = m;
    }

    @Override
    FeatureEntry copy() {
        FeatureEntry e = new FeatureEntry();
        e.init(this);
        return e;
    }
}
