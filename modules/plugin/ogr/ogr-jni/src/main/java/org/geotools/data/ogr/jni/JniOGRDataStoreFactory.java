package org.geotools.data.ogr.jni;

import java.util.Vector;

import org.geotools.data.ogr.OGR;
import org.geotools.data.ogr.OGRDataStoreFactory;

public class JniOGRDataStoreFactory extends OGRDataStoreFactory {

    @Override
    protected OGR createOGR() {
        return new JniOGR();
    }
    
    @Override
    protected boolean doIsAvailable() throws Throwable {
    // TODO: ogrjni instead ?
      if (jniIsAlreadyLoaded("gdaljni")) {
      	return true;
      }
      System.loadLibrary("gdaljni");
      return true;
    }

    private static boolean jniIsAlreadyLoaded(String libName){
    	// this returns only the basename (under linux, without the libname.so.x.y.z suffix)
    	String foundLib = System.mapLibraryName(libName);
    	try {
    		java.lang.reflect.Field LIBRARIES = null;
    		LIBRARIES = ClassLoader.class.getDeclaredField("loadedLibraryNames");
    		LIBRARIES.setAccessible(true);
    		// The following vector will contain the full path to the loaded library
    		// (under linux with the libname.so[.x.y.z] suffix, not the path to the symlink)
    		final Vector<String>  libs = (Vector<String>) LIBRARIES.get(ClassLoader.getSystemClassLoader());
    		for (String l : libs){
    			if (l.contains(foundLib))
    				return true;
    		}
    		return false;
    	} catch (Throwable e) {
    		return false;
    	}
    }
}
