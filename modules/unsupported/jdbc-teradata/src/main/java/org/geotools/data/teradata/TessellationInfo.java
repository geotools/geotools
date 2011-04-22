package org.geotools.data.teradata;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Holds teradata tessellation parameters.
 * 
 * @author Justin Deoliveira, OpenGeo
 *
 */
public class TessellationInfo {

    /** 
     * user data key
     */
    public static String KEY = TessellationInfo.class.getName();
    
    /** bounds of universe */
    Envelope uBounds;
    
    /** index dimensions */
    int nx, ny;
    
    /** levels */
    int levels;
    
    /** scale */
    double scale;
    
    /** shift */
    int shift;
    
    /** table info */
    String schemaName;
    String tableName;
    String columName;
    
    /** spatial index */
    String indexTableName;
    
    public Envelope getUBounds() {
        return uBounds;
    }
    
    public void setUBounds(Envelope uBounds) {
        this.uBounds = uBounds;
    }

    public int getNx() {
        return nx;
    }

    public void setNx(int nx) {
        this.nx = nx;
    }
    
    public void setNy(int ny) {
        this.ny = ny;
    }
    
    public int getNy() {
        return ny;
    }
    
    public int getLevels() {
        return levels;
    }
    
    public void setLevels(int levels) {
        this.levels = levels;
    }
    
    public double getScale() {
        return scale;
    }
    
    public void setScale(double scale) {
        this.scale = scale;
    }
    
    public int getShift() {
        return shift;
    }
    
    public void setShift(int shift) {
        this.shift = shift;
    }
    
    public String getSchemaName() {
        return schemaName;
    }
    
    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }
    
    public String getTableName() {
        return tableName;
    }
    
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    
    public String getColumName() {
        return columName;
    }
    
    public void setColumName(String columName) {
        this.columName = columName;
    }
    
    public String getIndexTableName() {
        return indexTableName;
    }
    
    public void setIndexTableName(String indexTableName) {
        this.indexTableName = indexTableName;
    }
    
    
}
