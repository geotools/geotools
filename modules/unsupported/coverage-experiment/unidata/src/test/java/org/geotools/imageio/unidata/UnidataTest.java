/*
 *    Geotools2 - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2002, Geotools Project Managment Committee (PMC)
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
 *
 */
package org.geotools.imageio.unidata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.geotools.coverage.grid.io.DimensionDescriptor;
import org.geotools.coverage.io.CoverageSource.AdditionalDomain;
import org.geotools.coverage.io.CoverageSource.DomainType;
import org.geotools.coverage.io.CoverageSourceDescriptor;
import org.geotools.coverage.io.catalog.CoverageSlice;
import org.geotools.coverage.io.catalog.CoverageSlicesCatalog;
import org.geotools.data.DataUtilities;
import org.geotools.data.Query;
import org.geotools.feature.NameImpl;
import org.geotools.imageio.unidata.reader.DummyUnidataImageReader;
import org.geotools.imageio.unidata.reader.DummyUnidataImageReaderSpi;
import org.geotools.test.TestData;
import org.junit.After;
import org.junit.Test;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;

/**
 * Testing Low Level Unidata reader infrastructure.
 * 
 * @author Simone Giannecchini, GeoSolutions SAS
 * @source $URL$
 */
public final class UnidataTest extends Assert {

    private final static Logger LOGGER = Logger.getLogger(UnidataTest.class.toString());

    @Test
    public void testImageReaderPolyphemunsComplex() throws Exception {
        File file=null;
        try{
            file = TestData.file(this, "polyphemus_20130301.nc");
        } catch (IOException e) {
            LOGGER.warning("Unable to find file polyphemus_20130301.nc");
            return;
        }
        if(!file.exists()){
            LOGGER.warning("Unable to find file polyphemus_20130301.nc");
            return;
        }
        final DummyUnidataImageReaderSpi unidataImageReaderSpi = new DummyUnidataImageReaderSpi();
        assertTrue(unidataImageReaderSpi.canDecodeInput(file));
        DummyUnidataImageReader reader = null;
        try {
            reader = (DummyUnidataImageReader) unidataImageReaderSpi.createReaderInstance();
            reader.setInput(file);
            int numImages = reader.getNumImages(true);
            assertEquals(1008, numImages);
            for (int i = 0; i < numImages; i++) {
                UnidataSlice2DIndex sliceIndex = reader.getSlice2DIndex(i);
                assertNotNull(sliceIndex);
                spitOutSliceInformation(i, sliceIndex);
            }

            // check coverage names
            final List<Name> names = reader.getCoveragesNames();
            assertNotNull(names);
            assertTrue(!names.isEmpty());
            assertTrue(3==names.size());
            assertTrue(names.contains(new NameImpl("NO2")));
            assertTrue(names.contains(new NameImpl("O3")));
            assertTrue(names.contains(new NameImpl("V")));
            
            // checking slice catalog
            final CoverageSlicesCatalog cs = reader.getCatalog();
            assertNotNull(cs);
            
            
            // get typenames
            final String[] typeNames = cs.getTypeNames();
            for(String typeName:typeNames){
                final List<CoverageSlice> granules = cs.getGranules(new Query(typeName, Filter.INCLUDE));
                assertNotNull(granules);
                assertFalse(granules.isEmpty());
                for(CoverageSlice slice:granules){
                    final SimpleFeature sf = slice.getOriginator();
                    if(TestData.isInteractiveTest()){
                        LOGGER.info(DataUtilities.encodeFeature(sf));
                    }
                    
                    // checks
                   for(Property p: sf.getProperties()){
                       assertNotNull("Property "+p.getName()+" had a null value!",p.getValue());
                   }
                }  
            }            
        } finally {
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {
                    // Does nothing
                }
            }
        }
    }
    
    @Test
    public void testImageReaderPolyphemusSimple() throws Exception {
        final File file = TestData.file(this, "O3-NO2.nc");
        final DummyUnidataImageReaderSpi unidataImageReaderSpi = new DummyUnidataImageReaderSpi();
        assertTrue(unidataImageReaderSpi.canDecodeInput(file));
        DummyUnidataImageReader reader = null;
        try {
            
            // checking low level
            reader = (DummyUnidataImageReader) unidataImageReaderSpi.createReaderInstance();
            reader.setInput(file);
            int numImages = reader.getNumImages(true);
            LOGGER.info("Found "+numImages+ " images.");
            for (int i = 0; i < numImages; i++) {
                UnidataSlice2DIndex sliceIndex = reader.getSlice2DIndex(i);
                assertNotNull(sliceIndex);
                spitOutSliceInformation(i, sliceIndex);
            }
            
            // checking slice catalog
            final CoverageSlicesCatalog cs = reader.getCatalog();
            assertNotNull(cs);
            
            // get typenames
            final String[] typeNames = cs.getTypeNames();
            for(String typeName:typeNames){
                final List<CoverageSlice> granules = cs.getGranules(new Query(typeName, Filter.INCLUDE));
                assertNotNull(granules);
                assertFalse(granules.isEmpty());
                for(CoverageSlice slice:granules){
                    final SimpleFeature sf = slice.getOriginator();
                    if(TestData.isInteractiveTest()){
                        LOGGER.info(DataUtilities.encodeFeature(sf));
                    }
                    
                    // checks
                   for(Property p: sf.getProperties()){
                       assertNotNull("Property "+p.getName()+" had a null value!",p.getValue());
                   }
                }  
            }
        } finally {
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {
                    // Does nothing
                }
            }
        }
    }

    /**
     * recursively delete indexes
     * @param file
     */
    private void removeIndexes(final File file){
        if(file.isFile()){
            final String absolutePath = file.getAbsolutePath().toLowerCase();
            if(absolutePath.endsWith(".idx")||absolutePath.endsWith(".db")){
                file.delete();
            }
        } else {
            final File[] files = file.listFiles();
            for(File f:files){
                removeIndexes(f);
            }
        }
    }
    
    private void cleanUp() throws FileNotFoundException, IOException {

        final File dir = TestData.file(this, ".");
        removeIndexes(dir);
    }

    @After
    public void tearDown() throws FileNotFoundException, IOException {
        if (TestData.isInteractiveTest()) {
            return;
        }
        cleanUp();
    }


    @Test
    public void testImageReaderIASI() throws Exception {
        File file=null;
        try{
            file = TestData.file(this, "IASI_C_EUMP_20121120062959_31590_eps_o_l2.nc");
        } catch (IOException e) {
            LOGGER.warning("Unable to find file IASI_C_EUMP_20121120062959_31590_eps_o_l2.nc");
            return;
        }
        if(!file.exists()){
            LOGGER.warning("Unable to find file IASI_C_EUMP_20121120062959_31590_eps_o_l2.nc");
            return;
        }        
        final DummyUnidataImageReaderSpi unidataImageReaderSpi = new DummyUnidataImageReaderSpi();
        assertTrue(unidataImageReaderSpi.canDecodeInput(file));
        DummyUnidataImageReader reader = null;
        try {
            reader = (DummyUnidataImageReader) unidataImageReaderSpi.createReaderInstance();
            reader.setInput(file);
            int numImages = reader.getNumImages(true);
            for (int i = 0; i < numImages; i++) {
                UnidataSlice2DIndex sliceIndex = reader.getSlice2DIndex(i);
                assertNotNull(sliceIndex);
                spitOutSliceInformation(i, sliceIndex);
            }
            
            // cloud_phase
            CoverageSourceDescriptor cd = reader.getCoverageDescriptor(new NameImpl("cloud_phase"));
            final List<AdditionalDomain> additionalDomains = cd.getAdditionalDomains();
            assertTrue(!additionalDomains.isEmpty());
            final AdditionalDomain ad=additionalDomains.get(0);
            assertTrue(ad.getType().equals(DomainType.NUMBER));
            assertEquals("cloud_phase",ad.getName());
            
           
        } finally {
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {
                    // Does nothing
                }
            }
        }
    }

    @Test
    public void testImageReaderGOME2() throws Exception {
        final File file = TestData.file(this, "20130101.METOPA.GOME2.NO2.DUMMY.nc");
        final DummyUnidataImageReaderSpi unidataImageReaderSpi = new DummyUnidataImageReaderSpi();
        assertTrue(unidataImageReaderSpi.canDecodeInput(file));
        DummyUnidataImageReader reader = null;
        try {
            
            // checking low level
            reader = (DummyUnidataImageReader) unidataImageReaderSpi.createReaderInstance();
            reader.setInput(file);
            int numImages = reader.getNumImages(true);
            assertEquals(1, numImages);
            LOGGER.info("Found "+numImages+ " images.");
            for (int i = 0; i < numImages; i++) {
                UnidataSlice2DIndex sliceIndex = reader.getSlice2DIndex(i);
                assertNotNull(sliceIndex);
                spitOutSliceInformation(i, sliceIndex);
            }
            
            // check coverage names
            final List<Name> names = reader.getCoveragesNames();
            assertNotNull(names);
            assertTrue(!names.isEmpty());
            assertTrue(1==names.size());
            assertEquals("NO2", names.get(0).toString());
            
            // checking slice catalog
            final CoverageSlicesCatalog cs = reader.getCatalog();
            assertNotNull(cs);
            
            
            // get typenames
            final String[] typeNames = cs.getTypeNames();
            for(String typeName:typeNames){
                final List<CoverageSlice> granules = cs.getGranules(new Query(typeName, Filter.INCLUDE));
                assertNotNull(granules);
                assertFalse(granules.isEmpty());
                for(CoverageSlice slice:granules){
                    final SimpleFeature sf = slice.getOriginator();
                    if(TestData.isInteractiveTest()){
                        LOGGER.info(DataUtilities.encodeFeature(sf));
                    }
                    
                    // checks
                   for(Property p: sf.getProperties()){
                       final String pName = p.getName().toString();
                    if(!pName.equalsIgnoreCase("time")&&!pName.equalsIgnoreCase("elevation")){
                        assertNotNull("Property "+p.getName()+" had a null value!",p.getValue());
                    } else {
                        assertNull("Property "+p.getName()+" did not have a null value!",p.getValue());
                    }
                   }
                }  
            }
        } finally {
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {
                    // Does nothing
                }
            }
        }
    }

    @Test
    public void testImageReaderAscat() throws Exception {
        File file=null;
        try{
            file = TestData.file(this, "ascatl1.nc");
        } catch (IOException e) {
            LOGGER.warning("Unable to find file ascatl1.nc");
            return;
        }
        if(!file.exists()){
            LOGGER.warning("Unable to find file ascatl1.nc");
            return;
        }        
        final DummyUnidataImageReaderSpi unidataImageReaderSpi = new DummyUnidataImageReaderSpi();
        assertTrue(unidataImageReaderSpi.canDecodeInput(file));
        DummyUnidataImageReader reader = null;
        try {
            reader = (DummyUnidataImageReader) unidataImageReaderSpi.createReaderInstance();
            reader.setInput(file);
            int numImages = reader.getNumImages(true);
            for (int i = 0; i < numImages; i++) {
                UnidataSlice2DIndex sliceIndex = reader.getSlice2DIndex(i);
                assertNotNull(sliceIndex);
                spitOutSliceInformation(i, sliceIndex);
            }
            
         // check coverage names
            final List<Name> names = reader.getCoveragesNames();
            assertNotNull(names);
            assertTrue(!names.isEmpty());
            assertTrue(2==names.size());
            assertEquals("cell_index", names.get(0).toString());
            assertEquals("f_land", names.get(1).toString());
            
            // checking slice catalog
            final CoverageSlicesCatalog cs = reader.getCatalog();
            assertNotNull(cs);
            
            
            // get typenames
            final String[] typeNames = cs.getTypeNames();
            for(String typeName:typeNames){
                final List<CoverageSlice> granules = cs.getGranules(new Query(typeName, Filter.INCLUDE));
                assertNotNull(granules);
                assertFalse(granules.isEmpty());
                for(CoverageSlice slice:granules){
                    final SimpleFeature sf = slice.getOriginator();
                    if(TestData.isInteractiveTest()){
                        LOGGER.info(DataUtilities.encodeFeature(sf));
                    }
                    
                    // checks
                   for(Property p: sf.getProperties()){
                       final String pName = p.getName().toString();
                    if(!pName.equalsIgnoreCase("time")&&!pName.equalsIgnoreCase("elevation")){
                        assertNotNull("Property "+p.getName()+" had a null value!",p.getValue());
                    } else {
                        assertNull("Property "+p.getName()+" did not have a null value!",p.getValue());
                    }
                   }
                }  
            }
            
           
        } finally {
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {
                    // Does nothing
                }
            }
        }
    }

    /**
     * @param i
     * @param sliceIndex
     */
    private void spitOutSliceInformation(int i, UnidataSlice2DIndex sliceIndex) {
        if(TestData.isInteractiveTest()){
            String variableName = sliceIndex.getVariableName();
            StringBuilder sb = new StringBuilder();
            sb.append("\n").append("\n").append("\n");
            sb.append("IMAGE: ").append(i).append("\n");
            sb.append(" Variable Name = ").append(variableName);
            sb.append(" ( Z = ");
            sb.append(sliceIndex.getZIndex());
            sb.append("; T = ");
            sb.append(sliceIndex.getTIndex());
            sb.append(")");
            LOGGER.info(sb.toString());
        }
    }

    @Test
    public void testImageReaderPolyphemunsComplex2() throws Exception {
        File file=null;
        try{
            file = TestData.file(this, "polyphemus_20130301.nc");
        } catch (IOException e) {
            LOGGER.warning("Unable to find file polyphemus_20130301.nc");
            return;
        }
        if(!file.exists()){
            LOGGER.warning("Unable to find file polyphemus_20130301.nc");
            return;
        }
        FileUtils.copyFile(file, new File(TestData.file(this,null),"polyphemus.nc"));
        file=TestData.file(this,"polyphemus.nc");
        final DummyUnidataImageReaderSpi unidataImageReaderSpi = new DummyUnidataImageReaderSpi();
        assertTrue(unidataImageReaderSpi.canDecodeInput(file));
        DummyUnidataImageReader reader = null;
        try {
            reader = (DummyUnidataImageReader) unidataImageReaderSpi.createReaderInstance();
            reader.setInput(file);
            int numImages = reader.getNumImages(true);
            assertEquals(1008, numImages);
            for (int i = 0; i < numImages; i++) {
                UnidataSlice2DIndex sliceIndex = reader.getSlice2DIndex(i);
                assertNotNull(sliceIndex);
                spitOutSliceInformation(i, sliceIndex);
            }
    
            // check dimensions
            CoverageSourceDescriptor cd = reader.getCoverageDescriptor(new NameImpl("NO2"));
            final List<AdditionalDomain> additionalDomains = cd.getAdditionalDomains();
            assertNull(additionalDomains);
            
            final List<DimensionDescriptor> dimensions = cd.getDimensionDescriptors();
            assertNotNull(dimensions);
            assertTrue(!dimensions.isEmpty());
            assertEquals("wrong dimensions",2, dimensions.size());
            DimensionDescriptor dim = dimensions.get(0);
            assertTrue(dim.getName().equals("TIME"));
            assertTrue(dim.getStartAttribute().equals("time"));
            dim = dimensions.get(1);
            assertTrue(dim.getName().equals("ELEVATION"));
            assertTrue(dim.getStartAttribute().equals("z"));
            
            
            // check coverage names
            final List<Name> names = reader.getCoveragesNames();
            assertNotNull(names);
            assertTrue(!names.isEmpty());
            assertTrue(3==names.size());
            assertTrue(names.contains(new NameImpl("NO2")));
            assertTrue(names.contains(new NameImpl("O3")));
            assertTrue(names.contains(new NameImpl("V")));
            
            // checking slice catalog
            final CoverageSlicesCatalog cs = reader.getCatalog();
            assertNotNull(cs);
            
            
            // get typenames
            final String[] typeNames = cs.getTypeNames();
            for(String typeName:typeNames){
                final List<CoverageSlice> granules = cs.getGranules(new Query(typeName, Filter.INCLUDE));
                assertNotNull(granules);
                assertFalse(granules.isEmpty());
                for(CoverageSlice slice:granules){
                    final SimpleFeature sf = slice.getOriginator();
                    if(TestData.isInteractiveTest()){
                        LOGGER.info(DataUtilities.encodeFeature(sf));
                    }
                    
                    // checks
                   for(Property p: sf.getProperties()){
                       assertNotNull("Property "+p.getName()+" had a null value!",p.getValue());
                   }
                }  
            }            
        } finally {
            
            // close reader
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {
                    // Does nothing
                }
            }
            

            // specific clean up
            FileUtils.deleteDirectory(TestData.file(this,".polyphemus"));
        }
    }
}
