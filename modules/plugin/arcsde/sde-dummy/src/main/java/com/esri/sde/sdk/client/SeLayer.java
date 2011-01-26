package com.esri.sde.sdk.client;

public class SeLayer {
	
	public static /* GEOT-947 final*/ int SE_NIL_TYPE_MASK = 0;
	public static /* GEOT-947 final*/ int SE_LINE_TYPE_MASK = 1;
	public static /* GEOT-947 final*/ int SE_POINT_TYPE_MASK = 2;
	public static /* GEOT-947 final*/ int SE_AREA_TYPE_MASK = 3;
	public static /* GEOT-947 final*/ int SE_MULTIPART_TYPE_MASK = 4;
	public static /* GEOT-947 final*/ int SE_SHAPE_ATTRIBUTE_FID = 5;
	public static /* GEOT-947 final*/ int SE_SIMPLE_LINE_TYPE_MASK = 6;
	public static /* GEOT-947 final*/ int TYPE_NIL = 7;
	public static /* GEOT-947 final*/ int TYPE_MULTI_MASK = 8;
	public static /* GEOT-947 final*/ int TYPE_LINE = 9;
	public static /* GEOT-947 final*/ int TYPE_POINT = 10;
	public static /* GEOT-947 final*/ int TYPE_POLYGON = 11;
	public static /* GEOT-947 final*/ int TYPE_SIMPLE_LINE = 12;
	public static /* GEOT-947 final*/ int TYPE_MULTI_SIMPLE_LINE = 13;
	public static /* GEOT-947 final*/ int TYPE_MULTI_LINE = 14;
	public static /* GEOT-947 final*/ int TYPE_MULTI_POLYGON = 15;
	public static /* GEOT-947 final*/ int TYPE_MULTI_POINT = 16;
	
	public SeLayer(SeConnection c, String s, String y) throws SeException {}
	public SeLayer(SeConnection c) {}
	
	public String getName() { return null; }
	public int getShapeTypes() { return 0; }
	public void setTableName(String s) {}
	public void setSpatialColumnName(String s) {}
	public void setShapeTypes(int i) {}
	// public void setGridSizes(int i, int j, int k) {} NOT IN 9.1 or 9.2
	public void setDescription(String s) {}
	public void setExtent(SeExtent s) {}
	public void setCoordRef(SeCoordinateReference s) {}
	public void create(int i, int j) {}
	public String getQualifiedName() throws SeException { return null; }
	public String getSpatialColumn() { return null; }
	public SeCoordinateReference getCoordRef() { return null; }
	public SeExtent getExtent() { return null; }
	public String getShapeAttributeName(int i) throws SeException { return null; }
	public void setGridSizes(double a, double b, double c){}
	public void delete()throws SeException{}
    public void setCreationKeyword(String s) {}
}
