/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */

package org.geotools.swing.locale;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * Searches for properties files in a resource directory within the gt-swing module
 * and records the {@code Locales} supported by each file. This is a helper for 
 * {@linkplain LocaleUtils}.
 * <p>
 * Normally, the {@linkplain #scan(String)} method will be responding to a call from 
 * outside this class's jar, either directly or indirectly. An example of an indirect
 * outside call is when an application calls a LocaleUtils method which in turn calls 
 * the {@code scan} method. In this case, the resource directory is searched by scanning
 * the relevant entries in the gt-swing jar.
 * <p>
 * For completeness, and to aid unit testing, calls from within the swing module are
 * also supported. In this case the resource directory is accessed as a local
 * {@linkplain File} object.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
public class PropertiesFileFinder {
    
    /**
     * Searches for properties files in the specified resource directory and returns
     * information about each file and the {@code Locales} that it supports.
     * 
     * @param resourceDir
     * @return
     * @throws IOException 
     */
    public List<PropertiesFileInfo> scan(String resourceDir) throws IOException {
        List<SingleFileInfo> infoList = new ArrayList<SingleFileInfo>();
        
        String path = getSelfPath();
        if (isJarPath(path)) {
            JarInputStream jarFile = getAsJarFile(path);
            JarEntry entry;
            while ((entry = jarFile.getNextJarEntry()) != null) {
                String name = entry.getName();
                if (name.startsWith(resourceDir) && name.endsWith("properties")) {
                    infoList.add(parseEntry(resourceDir.length(), name));
                }
            }
            jarFile.close();

        } else {  // must be running locally
            File localDir = getAsLocalDir(path);
            File[] children = localDir.listFiles();
            for (File child : children) {
                if (child != null && child.isFile()) {
                    String name = child.getName();
                    if (name.endsWith(".properties")) {
                        infoList.add(parseEntry(0, name));
                    }
                }
            }
        }

        return createReturnList( infoList );
    }

    /**
     * Gets the path to this class file. This will be a jar file path
     * if called from outside this module, or a local path if called 
     * from within.
     * 
     * @return path to this class
     */
    private String getSelfPath() {
        String className = getClass().getSimpleName() + ".class";
        URL url = getClass().getResource(className);
        return url.getPath();
    }
    
    private boolean isJarPath(String path) {
        return path.contains(".jar!");
    }
    
    private JarInputStream getAsJarFile(String path) throws IOException {
        boolean filePrefix = path.startsWith("file:");
        int pos = path.indexOf(".jar!");

        if (!filePrefix || pos <= 0) {
            throw new IllegalArgumentException("Not a valid jar path");
        }
        
        String trimmed = path.substring(5, pos + 4);
        File file = new File(trimmed);
        if (!file.exists()) {
            throw new IllegalArgumentException("File not found: " + file);
        }
        
        return new JarInputStream(new FileInputStream(file));
    }
    
    private File getAsLocalDir(String path) {
        int pos = path.lastIndexOf(File.separatorChar);
        File file = new File(path.substring(0, pos));
        if (!file.exists() || !file.isDirectory()) {
            throw new IllegalArgumentException("Invalid directory path: " + file);
        }
        return file;
    }

    private SingleFileInfo parseEntry(int prefixLength, String name) {
        name = name.substring(prefixLength, name.indexOf(".properties"));
        String[] parts = name.split("_");
        String baseName = parts[0];
        String language = "";
        String country = "";
        String variant = "";

        if (parts.length > 1) {
            language = parts[1];
        }
        if (parts.length > 2) {
            country = parts[2];
        }
        if (parts.length > 3) {
            variant = parts[3];
        }
        
        Locale locale;
        if (parts.length == 1) {
            locale = Locale.ROOT;
        } else {
            locale = new Locale(language, country, variant);
        }
        
        return new SingleFileInfo(baseName, locale);
    }

    private List<PropertiesFileInfo> createReturnList(List<SingleFileInfo> infoList) {
        List<PropertiesFileInfo> pfiList = new ArrayList<PropertiesFileInfo>();

        if (!infoList.isEmpty()) {
            Collections.sort(infoList, new Comparator<SingleFileInfo>() {
                @Override
                public int compare(SingleFileInfo o1, SingleFileInfo o2) {
                    return o1.name.compareTo(o2.name);
                }
            });

            String curName = infoList.get(0).name;
            List<Locale> locales = new ArrayList<Locale>();
            ListIterator<SingleFileInfo> iter = infoList.listIterator();
            while (iter.hasNext()) {
                SingleFileInfo sfi = iter.next();
                if (sfi.name.equals(curName)) {
                    locales.add(sfi.locale);
                } else {
                    pfiList.add(new PropertiesFileInfo(curName, locales));
                    curName = sfi.name;
                    locales.clear();
                    locales.add(sfi.locale);
                }
            }
            pfiList.add(new PropertiesFileInfo(curName, locales));
        }
        return pfiList;
    }

    private static class SingleFileInfo {
        String name;
        Locale locale;

        public SingleFileInfo(String name, Locale locale) {
            this.name = name;
            this.locale = locale;
        }
    }
}
