package org.geotools.geopkg;

/**
 * Raster entry in a geopackage.
 * <p>
 * This class corresponds to the "raster_columns" table.
 * </p>
 * @author Justin Deoliveira, OpenGeo
 */
public class RasterEntry extends Entry {

    public static enum Rectification {
        None(0), Geo(1), Ortho(2);

        int value;

        Rectification(int value) {
            this.value = value;
        }
        
        public int value() {
            return value;
        }

        public static Rectification valueOf(int value) {
            for (Rectification r : values()) {
                if (r.value == value) {
                    return r;
                }
            }
            return null;
        }
    }

    String rasterColumn;
    Rectification geoRectification;
    Double compressionQualityFactor;

    public RasterEntry() {
        setDataType(DataType.Raster);
    }

    public String getRasterColumn() {
        return rasterColumn;
    }
    
    public void setRasterColumn(String rasterColumn) {
        this.rasterColumn = rasterColumn;
    }

    public Rectification getGeoRectification() {
        return geoRectification;
    }

    public void setGeoRectification(Rectification geoRectification) {
        this.geoRectification = geoRectification;
    }

    public Double getCompressionQualityFactor() {
        return compressionQualityFactor;
    }

    public void setCompressionQualityFactor(Double compressionQualityFactor) {
        this.compressionQualityFactor = compressionQualityFactor;
    }

    void init(RasterEntry e) {
        super.init(e);
        setRasterColumn(e.getRasterColumn());
        setGeoRectification(e.getGeoRectification());
        setCompressionQualityFactor(e.getCompressionQualityFactor());
    }

    @Override
    RasterEntry copy() {
        RasterEntry e = new RasterEntry();
        e.init(this);
        return e;
    }
}
