package com.esri.sde.sdk.client;

import java.io.ByteArrayInputStream;
import java.util.Calendar;

import com.esri.sde.sdk.geom.GeometryFactory;

public class SeRow {
	
	public static /* GEOT-947 final*/ int SE_IS_NULL_VALUE = 0;
	public static /* GEOT-947 final*/ int SE_IS_REPEATED_FEATURE = 1;
	public static /* GEOT-947 final*/ int SE_IS_ALREADY_FETCHED = 2;
	public static /* GEOT-947 final*/ int SE_IS_NOT_NULL_VALUE = 3;
	
	public void reset() throws SeException{};
	public SeColumnDefinition[] getColumns() { return null; }
	public Object getObject(int i) throws SeException { return null; }
	public SeColumnDefinition getColumnDef(int i) throws SeException{ return null; }
	public void setInteger(int i, Integer b) {}
	public void setShort(int i, Short s) {}
	public void setFloat(int i, Float f) {}
	public void setDouble(int i, Double d) {}
	public void setString(int i, String s) {}
	public void setTime(int i, Calendar c) {}
	public void setShape(int i, SeShape s) {}
	public void setRaster(int i, SeRasterAttr a) {}
	public SeRasterAttr getRaster(int i) { return null; }
	public SeRasterTile getRasterTile() throws SeException { return null; }
	public short getNumColumns() { return -1; }
	public int getIndicator(int i) { return -1; }
    public Integer getInteger(int i) throws SeException {return null;}
    public void setLong(int index, Long value) {}
    public SeShape getShape(int i) {return null;}
    public void setNString(int index, String convertedValue) {}
    public Object getGeometry(GeometryFactory seGeomFac, int i)throws SeException {
        return null;
    }
    public ByteArrayInputStream getBlob(int i) throws SeException{
        // TODO Auto-generated method stub
        return null;
    }
    public String getString(int i) throws SeException{
        // TODO Auto-generated method stub
        return null;
    }
    public ByteArrayInputStream getClob(int i) {return null;}
    public void setClob(int i, ByteArrayInputStream clobVal) {}
    public ByteArrayInputStream getNClob(int i) {return null;}
    public void setNClob(int i, ByteArrayInputStream clobVal) {}
}
