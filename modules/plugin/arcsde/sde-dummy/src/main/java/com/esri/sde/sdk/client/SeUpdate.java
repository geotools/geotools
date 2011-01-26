package com.esri.sde.sdk.client;

public class SeUpdate extends SeStreamOp{
	
	public SeUpdate(SeConnection c) throws SeException {}
	
	public void toTable(String s, String[] y, String x) {}
	public void setWriteMode(boolean b) {}
	public SeRow getRowToSet() { return null; }

    public SeRow singleRow(SeObjectId seObjectId, String typeName, String[] rowColumnNames) throws SeException{
        return null;
    }
}
