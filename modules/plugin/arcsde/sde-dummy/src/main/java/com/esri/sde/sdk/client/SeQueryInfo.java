package com.esri.sde.sdk.client;

/**
 * 
 *
 * @source $URL$
 */
public class SeQueryInfo {

	public void setConstruct(SeSqlConstruct s) throws SeException{}
	public void setColumns(String[] s) {}
	public void setByClause(String s) {}
	public String getByClause() { return null; }
	public SeSqlConstruct getConstruct() throws SeException { return null; }
    public String[] getColumns() { return null; }

}
