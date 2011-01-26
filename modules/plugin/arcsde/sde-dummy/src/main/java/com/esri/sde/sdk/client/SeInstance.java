package com.esri.sde.sdk.client;

public class SeInstance {
	
	public SeInstance(String s, int i) throws SeException {}
	
	public String getServerName() { return null; }
	public SeInstanceStatus getStatus() { return null; }
	public SeInstanceConfiguration getConfiguration() { return null; }
	
	public class SeInstanceStatus {
		public SeRelease getSeRelease() { return null; }
		public boolean isAccepting() { return false; }
		public boolean isBlocking() { return false; }
	}
	public class SeInstanceConfiguration {
		public boolean getReadOnlyInstance() { return false; }
		public String getHomePath() { return null; }
		public String getLogPath() { return null; }
		public int getStreamPoolSize() { return 0; }
		public int getMaxConnections() { return 0; }
		public int getMaxLayers() { return 0; }
		public int getMaxStreams() { return 0; }
	}

}
