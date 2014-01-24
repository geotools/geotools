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
    String name;
    String title;
    String description;
    String mimeType;
    String constraint;

    public RasterEntry() {
        setDataType(DataType.Raster);
    }

    public String getRasterColumn() {
        return rasterColumn;
    }
    
    public void setRasterColumn(String rasterColumn) {
        this.rasterColumn = rasterColumn;
    }
   
    void init(RasterEntry e) {
        super.init(e);
        setRasterColumn(e.getRasterColumn());
        setName(e.getName());
        setTitle(e.getTitle());
        setDescription(e.getDescription());
        setMimeType(e.getMimeType());
        setConstraint(e.getConstraint());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getConstraint() {
        return constraint;
    }

    public void setConstraint(String constraint) {
        this.constraint = constraint;
    }

    @Override
    RasterEntry copy() {
        RasterEntry e = new RasterEntry();
        e.init(this);
        return e;
    }
}
