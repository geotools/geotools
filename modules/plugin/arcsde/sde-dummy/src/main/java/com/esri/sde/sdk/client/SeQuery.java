package com.esri.sde.sdk.client;

import com.esri.sde.sdk.client.SeTable.SeTableStats;

public class SeQuery extends SeStreamOp {
	
	public static short SE_SPATIAL_FIRST = 0;

    public SeQuery(SeConnection c) throws SeException {}
	public SeQuery(SeConnection c, String[] s, SeSqlConstruct y) throws SeException {}
	
	public static /* GEOT-947 final*/ short SE_OPTIMIZE = 0;
	
	public void prepareQuery()throws SeException {}
	public void prepareQueryInfo(SeQueryInfo i)throws SeException {}
	public SeExtent calculateLayerExtent(SeQueryInfo i) { return null; }
	public void cancel(boolean b) {}
	public void setRowLocking(int i) {}
	public SeRow fetch() throws SeException{ return null; }
	public void setSpatialConstraints(short i, boolean b, SeFilter[] f)throws SeException {}
	public SeTableStats calculateTableStatistics(String s, int i, SeQueryInfo q, int j) { return null; }
	public void queryRasterTile(SeRasterConstraint c) throws SeException{}
	public void prepareSql(String s) {}

}
