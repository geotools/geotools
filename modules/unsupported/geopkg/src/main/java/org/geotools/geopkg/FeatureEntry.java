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
    Integer coordDimension;
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

    public Integer getCoordDimension() {
        return coordDimension;
    }

    public void setCoordDimension(Integer coordDimension) {
        this.coordDimension = coordDimension;
    }

    void init(FeatureEntry e) {
        super.init(e);
        setGeometryColumn(e.getGeometryColumn());
        setGeometryType(e.getGeometryType());
        setCoordDimension(e.getCoordDimension());
    }

    @Override
    FeatureEntry copy() {
        FeatureEntry e = new FeatureEntry();
        e.init(this);
        return e;
    }
}
