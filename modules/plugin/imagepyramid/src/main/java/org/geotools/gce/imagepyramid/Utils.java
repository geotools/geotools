/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagepyramid;

import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.geotools.data.DataUtilities;
import org.geotools.factory.Hints;
import org.geotools.gce.imagemosaic.ImageMosaicFormat;
import org.geotools.gce.imagemosaic.ImageMosaicReader;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.resources.coverage.CoverageUtilities;
import org.geotools.util.logging.Logging;
import org.opengis.referencing.datum.PixelInCell;

/**
 * Code to build a pyramid from a gdal_retile output
 * 
 * @author Andrea Aime - GeoSolutions SAS
 * @author Simone Giannecchini, GeoSolutions SAS
 *
 */
class Utils {

    static final Logger LOGGER = Logging.getLogger(Utils.class);
    
    static URL checkSource(Object source) {
    	return checkSource(source, null);
    }

    static URL checkSource(Object source, Hints hints) {
        URL sourceURL = null;
        File sourceFile = null;

        //
        // Check source
        //
        // if it is a URL or a String let's try to see if we can get a file to
        // check if we have to build the index
        if (source instanceof File) {
            sourceFile = (File) source;
            sourceURL = DataUtilities.fileToURL(sourceFile);
        } else if (source instanceof URL) {
            sourceURL = (URL) source;
            if (sourceURL.getProtocol().equals("file")) {
                sourceFile = DataUtilities.urlToFile(sourceURL);
            }
        } else if (source instanceof String) {
            // is it a File?
            final String tempSource = (String) source;
            File tempFile = new File(tempSource);
            if (!tempFile.exists()) {
                // is it a URL
                try {
                    sourceURL = new URL(tempSource);
                    source = DataUtilities.urlToFile(sourceURL);
                } catch (MalformedURLException e) {
                    sourceURL = null;
                    source = null;
                }
            } else {
                sourceURL = DataUtilities.fileToURL(tempFile);
                sourceFile = tempFile;
            }
        } else {
            // we really don't know how to convert the thing... give up
            if(LOGGER.isLoggable(Level.WARNING)){
                LOGGER.warning("we really don't know how to convert the thing:"+source!=null?source.toString():"null");
            }
            return null;
        }

        // logging
        if(LOGGER.isLoggable(Level.INFO)){
            if(sourceFile!=null){
                final String message = fileStatus(sourceFile);
                LOGGER.info(message);
            }
        }

        //
        // Handle cases where the pyramid descriptor file already exists
        //
        // can't do anything with it
        if(sourceFile == null || !sourceFile.exists())
            return sourceURL;
        
        // if it's already a file we don't need to adjust it, will try to open as is
        if(!sourceFile.isDirectory())
            return sourceURL;
        
        // it's a directory, let's see if it already has a pyramid description file inside
        File directory = sourceFile;
        sourceFile = new File(directory, directory.getName() + ".properties");
        // logging
        if(LOGGER.isLoggable(Level.INFO)){
            if(sourceFile!=null){
                final String message = fileStatus(sourceFile);
                LOGGER.info(message);
            }
        }        
        if(sourceFile.exists())
            return DataUtilities.fileToURL(sourceFile);
        

        //
        // Try to build the sub-folders mosaics
        //
        // if the structure of the directories is gdal_retile like, move the root files in their
        // own sub directory
        File zeroLevelDirectory = new File(directory, "0");
        IOFileFilter directoryFilter = FileFilterUtils.directoryFileFilter();
        File[] numericDirectories = directory.listFiles(new NumericDirectoryFilter());
        File[] directories = directory.listFiles((FileFilter) directoryFilter);
        
        // do we have at least one sub-directory?
        if(directories.length == 0){
            if(LOGGER.isLoggable(Level.INFO)){
                LOGGER.info("I was unable to determine a structure similar to the GDAL Retile one!!");
            }
            return null;
        }
        
        // check the gdal case and move files if necessary
        if(!zeroLevelDirectory.exists() && numericDirectories.length == directories.length) {
            LOGGER.log(Level.INFO, "Detected gdal_retile file structure, " +
            		"moving root files to the '0' subdirectory");
            if(zeroLevelDirectory.mkdir()) {
                if(LOGGER.isLoggable(Level.FINE)){
                    LOGGER.fine("Created '0' subidr, now moving files");
                }                   
                FileFilter zeroLevelsFiles = FileFilterUtils.makeSVNAware(FileFilterUtils.makeCVSAware(FileFilterUtils.notFileFilter(directoryFilter)));
                for (File f : directory.listFiles(zeroLevelsFiles)) {
                    if(LOGGER.isLoggable(Level.FINE)){
                        LOGGER.fine("Moving file"+f.getAbsolutePath());
                    }  
                    if(LOGGER.isLoggable(Level.FINEST)){
                        LOGGER.finest(fileStatus(f));
                    }                      
                    if(!f.renameTo(new File(zeroLevelDirectory, f.getName())))
                        LOGGER.log(Level.WARNING, "Could not move " + f.getAbsolutePath() + 
                                " to " + zeroLevelDirectory+ " check the permission inside the source directory "+f.getParent()+ " and target directory "+zeroLevelDirectory);
                }
                directories = directory.listFiles((FileFilter) directoryFilter);
            } else {
                if(LOGGER.isLoggable(Level.INFO)){
                    LOGGER.info("I was unable to create the 0 directory. check the file permission in the parent directory:"+sourceFile.getParent());
                }
                return null;
            }
        }
        
        // scan each subdirectory and try to build a mosaic in it, accumulate the resulting mosaics
        List<MosaicInfo> mosaics = new ArrayList<MosaicInfo>();
        ImageMosaicFormat mosaicFactory = new ImageMosaicFormat();
        for (File subdir : directories) {
            if(mosaicFactory.accepts(subdir, hints)) {
                if(LOGGER.isLoggable(Level.FINE)){
                    LOGGER.fine("Trying to build mosaic for the directory:"+subdir.getAbsolutePath());
                }                
                mosaics.add(new MosaicInfo(subdir, mosaicFactory.getReader(subdir, hints)));
            } else {
                if(LOGGER.isLoggable(Level.INFO)){
                    LOGGER.info("Unable to build mosaic for the directory:"+subdir.getAbsolutePath());
                }
            }
        }
        
        // do we have at least one level?
        if(mosaics.size() == 0)
            return null;
     
        // sort the mosaics by resolution and check they are actually in ascending resolution order
        // for both X and Y resolutions
        Collections.sort(mosaics);
        for(int i = 1; i < mosaics.size(); i++) {
            double[] resprev = mosaics.get(i - 1).getResolutions();
            double[] res = mosaics.get(i).getResolutions();
            if(resprev[1] > res[1]) {
                LOGGER.log(Level.INFO, "Invalid mosaic, y resolution in " 
                        + mosaics.get(i - 1).getPath() + " is greater than the one in " 
                        + mosaics.get(i).getPath() + " whilst x resolutions " +
                        		"have the opposite relationship");
                return null;
            }
        }
        
        //
        // We have everything we need, build the final pyramid descriptor info
        //        
        // build the property file
        Properties properties = new Properties();
        properties.put("Name", directory.getName());
        properties.put("LevelsNum", String.valueOf(mosaics.size()));
        StringBuilder sbDirNames = new StringBuilder();
        StringBuilder sbLevels = new StringBuilder();
        for(MosaicInfo mi : mosaics) {
            sbDirNames.append(mi.getName()).append(" ");
            double[] resolutions = mi.getResolutions();
            sbLevels.append(resolutions[0]).append(",").append(resolutions[1]).append(" ");
        }
        properties.put("LevelsDirs", sbDirNames.toString());
        properties.put("Levels", sbLevels.toString());
        GeneralEnvelope envelope = mosaics.get(0).getEnvelope();
        properties.put("Envelope2D", envelope.getMinimum(0) + "," + envelope.getMinimum(1) + " " +
                envelope.getMaximum(0) + "," + envelope.getMaximum(1));
        OutputStream os = null;
        try {
            os = new FileOutputStream(sourceFile); 
            properties.store(os, "Automatically generated");
        } catch(IOException e) {
            LOGGER.log(Level.INFO, "We could not generate the pyramid propert file " + 
                    sourceFile.getPath(), e);
            return null;
        } finally {
            if(os != null)
                IOUtils.closeQuietly(os);
        }
        
        // build the .prj file if possible
        if(envelope.getCoordinateReferenceSystem() != null) {
            File prjFile = new File(directory, directory.getName() + ".prj");
            PrintWriter pw = null;
            try {
                pw = new PrintWriter(new FileOutputStream(prjFile));
                pw.print(envelope.getCoordinateReferenceSystem().toString());
            } catch(IOException e) {
                LOGGER.log(Level.INFO, "We could not write out the projection file " + 
                        prjFile.getPath(), e);
                return null;
            } finally {
                pw.close();
            }
        }
        
        return DataUtilities.fileToURL(sourceFile);
    }

    /**
     * Prepares a message with the status of the provided file.
     * @param sourceFile The {@link File} to provided the status message for
     * @return a status message for the provided {@link File} or a {@link NullPointerException} in case the {@link File}is <code>null</code>.
     */
    private static String fileStatus(File sourceFile) {
        if(sourceFile==null){
            throw new NullPointerException("Provided null input to fileStatus method");
        }
        final StringBuilder builder = new StringBuilder();
        builder.append("Checking file:").append(FilenameUtils.getFullPath(sourceFile.getAbsolutePath())).append("\n");
        builder.append("exists").append(sourceFile.exists()).append("\n");
        builder.append("isFile").append(sourceFile.isFile()).append("\n");
        builder.append("canRead:").append(sourceFile.canRead()).append("\n");
        builder.append("canWrite").append(sourceFile.canWrite()).append("\n");     
        builder.append("isHidden:").append(sourceFile.isHidden()).append("\n");
        builder.append("lastModified").append(sourceFile.lastModified()).append("\n");
        
        return builder.toString();
    }
    
    
    /**
     * Stores informations about a mosaic 
     */
    static class MosaicInfo implements Comparable<MosaicInfo>{
        @Override
        public String toString() {
            return "MosaicInfo [directory=" + directory + ", resolutions="
                    + Arrays.toString(resolutions) + "]";
        }

        File directory;
        ImageMosaicReader reader;
        double[] resolutions;
        
        MosaicInfo(File directory, ImageMosaicReader reader) {
            this.directory = directory;
            this.reader = reader;
            this.resolutions = CoverageUtilities.getResolution((AffineTransform) reader.getOriginalGridToWorld(PixelInCell.CELL_CORNER));
        }
        
        double[] getResolutions() {
            return resolutions;
        }
        
        String getPath() {
            return directory.getPath();
        }
        
        String getName() {
            return directory.getName();
        }
        
        GeneralEnvelope getEnvelope() {
            return reader.getOriginalEnvelope();
        }
        
        public int compareTo(MosaicInfo other) {
            // we make an easy comparison against the x resolution, we'll do a sanity
            // check about the y resolution later
            return resolutions[0] > other.resolutions[0] ? 1 : -1;
        }
    }
    
    /**
     * A file filter that only returns directories whose name is an integer number
     * @author Andrea Aime - OpenGeo
     */
    static class NumericDirectoryFilter implements FileFilter {

        public boolean accept(File pathname) {
            if(!pathname.isDirectory())
                return false;
            try {
                Integer.parseInt(pathname.getName());
                return true;
            } catch(NumberFormatException e) {
                return false;
            }
        }
    }
     
}
