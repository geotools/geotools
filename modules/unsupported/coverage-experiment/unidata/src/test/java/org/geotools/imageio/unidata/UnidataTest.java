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
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.Assert;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.geotools.coverage.io.CoverageSource.AdditionalDomain;
import org.geotools.coverage.io.CoverageSource.DomainType;
import org.geotools.coverage.io.CoverageSourceDescriptor;
import org.geotools.feature.NameImpl;
import org.geotools.imageio.unidata.reader.DummyUnidataImageReader;
import org.geotools.imageio.unidata.reader.DummyUnidataImageReaderSpi;
import org.geotools.test.TestData;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 
 *
 * @source $URL$
 */
public final class UnidataTest extends Assert {

    private final static Logger LOGGER = Logger.getLogger(UnidataTest.class.toString());

    
    @Test
    public void testImageReaderGOME2() throws Exception {
        final File file = TestData.file(this, "O3-NO2.nc");
        final DummyUnidataImageReaderSpi unidataImageReaderSpi = new DummyUnidataImageReaderSpi();
        assertTrue(unidataImageReaderSpi.canDecodeInput(file));
        DummyUnidataImageReader reader = null;
        try {
            reader = (DummyUnidataImageReader) unidataImageReaderSpi.createReaderInstance();
            reader.setInput(file);
            int numImages = reader.getNumImages(true);
            for (int i = 0; i < numImages; i++) {
                UnidataSlice2DIndex sliceIndex = reader.getSlice2DIndex(i);
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
        } catch (Throwable t) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, t.getLocalizedMessage(), t);
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

    
    private void cleanUp() throws FileNotFoundException, IOException {
        if (TestData.isInteractiveTest()) {
            return;
        }
        final File dir = TestData.file(this, ".");
        File[] files = dir.listFiles((FilenameFilter) FileFilterUtils.notFileFilter(FileFilterUtils
                .or(FileFilterUtils.or(FileFilterUtils.suffixFileFilter(".nc")))));
        for (File file : files) {
            file.delete();
        }
    }

    @After
    public void tearDown() throws FileNotFoundException, IOException {
        cleanUp();
    }


    @Test
    @Ignore
    public void testImageReaderIASI() throws Exception {
        final File file = TestData.file(this, "IASI_C_EUMP_20121120062959_31590_eps_o_l2.nc");
        final DummyUnidataImageReaderSpi unidataImageReaderSpi = new DummyUnidataImageReaderSpi();
        assertTrue(unidataImageReaderSpi.canDecodeInput(file));
        DummyUnidataImageReader reader = null;
        try {
            reader = (DummyUnidataImageReader) unidataImageReaderSpi.createReaderInstance();
            reader.setInput(file);
            int numImages = reader.getNumImages(true);
            for (int i = 0; i < numImages; i++) {
                UnidataSlice2DIndex sliceIndex = reader.getSlice2DIndex(i);
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
            
            // cloud_phase
            CoverageSourceDescriptor cd = reader.getCoverageDescriptor(new NameImpl("cloud_phase"));
            final List<AdditionalDomain> additionalDomains = cd.getAdditionalDomains();
            assertTrue(!additionalDomains.isEmpty());
            final AdditionalDomain ad=additionalDomains.get(0);
            assertTrue(ad.getType().equals(DomainType.NUMBER));
            assertEquals("cloud_phase",ad.getName());
            
            
        } catch (Throwable t) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, t.getLocalizedMessage(), t);
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
}
