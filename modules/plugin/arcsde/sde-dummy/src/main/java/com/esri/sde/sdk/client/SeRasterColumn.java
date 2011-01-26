package com.esri.sde.sdk.client;

public class SeRasterColumn {
    
    public SeRasterColumn(SeConnection conn, SeObjectId id) throws SeException {}
    public SeRasterColumn(SeConnection conn) throws SeException {}
    
    public SeCoordinateReference getCoordRef() { return null; }
    public String getName() { return null; }
    public String getQualifiedTableName() { return null; }
    
    public void setTableName(String name) {}
    public void setDescription(String desc) {}
    public void setRasterColumnName(String rColName) {}
    public void setCoordRef(SeCoordinateReference coordref) {}
    public void setConfigurationKeyword(String s) {}
    
    public void create() {}
    public String getTableName() {
        return null;
    }
    public SeObjectId getID() throws SeException{
        // TODO Auto-generated method stub
        return null;
    }

}
