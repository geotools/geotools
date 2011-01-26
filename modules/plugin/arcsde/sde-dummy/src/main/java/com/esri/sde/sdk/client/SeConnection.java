package com.esri.sde.sdk.client;

import java.util.List;
import java.util.Vector;

public class SeConnection {

    public static int SE_TRYLOCK_POLICY = 0;
    public static /* GEOT-947 final*/ int SE_UNPROTECTED_POLICY = 0;
    public static int SE_ONE_THREAD_POLICY = 1;
	
	public SeConnection(String a, int i, String b, String c, String d) throws SeException{
	    throw new UnsupportedOperationException("this is the dummy api");
	}
	
	public SeConnection(String a, String i, String b, String c, String d) throws SeException{
	    throw new UnsupportedOperationException("this is the dummy api");
	}
	public String getDatabaseName() throws SeException { return null; }
	public String getUser() throws SeException { return null; }
	public java.util.Vector getLayers() throws SeException { return null; }
	public java.util.Vector getRasterColumns() throws SeException { return null; }
	public SeRelease getRelease() { return null; }
	public boolean isClosed() { return false; }
	
	public void close() throws SeException {}
	public void commitTransaction() throws SeException {}
	public void rollbackTransaction() throws SeException {}
	
	public void setConcurrency(int i)throws SeException {}
	public int setTransactionAutoCommit(int i) throws SeException { return -1;}
	public void startTransaction() throws SeException{}

    public SeDBMSInfo getDBMSInfo() throws SeException{
        return null;
    }

    public String getSdeDbaName() throws SeException{
        return null;
    }

    public long getTimeSinceLastRT() {
        return 0;
    }

    public void testServer(long testServerRoundtripIntervalSeconds) throws SeException{
    }

    public SeVersion[] getVersionList(String where) throws SeException{
        return null;
    }

    public Vector getTables(int se_select_privilege) throws SeException{
        return null;
    }

}
