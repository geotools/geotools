package org.geotools.referencing.operation.transform;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.geotools.referencing.factory.gridshift.DataUtilities;
import org.geotools.referencing.factory.gridshift.NADCONGridShiftFactory;
import org.geotools.referencing.factory.gridshift.NADConGridShift;
import org.junit.Test;
import org.opengis.referencing.FactoryException;

public class NADCONGridShiftFactoryTest {

	@Test 
	public void testReleaseGrids() throws IOException, FactoryException {
		File gridShifts = new File("src/test/resources/org/geotools/referencing/factory/gridshift");
		File las = new File(gridShifts, "stpaul.las");
		File los = new File(gridShifts, "stpaul.los");
		
		File tlas = new File("./target/stpaul.las");
		File tlos = new File("./target/stpaul.los");
		copyFile(las, tlas);
		copyFile(los, tlos);
		
		NADCONGridShiftFactory factory = new NADCONGridShiftFactory();
		NADConGridShift shift = factory.loadGridShift(DataUtilities.fileToURL(tlas), DataUtilities.fileToURL(tlos));
		// minor checks on the grid
		assertNotNull(shift);
		
		// now the good part, try to delete the files, on windows this will fail
		// unless the sources were properly closed
		assertTrue(tlas.delete());
		assertTrue(tlos.delete());
	}

	private void copyFile(File src, File dst) throws IOException  {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(src);
			fos = new FileOutputStream(dst);
			byte[] buffer = new byte[4096];
			int read = 0;
			while((read = fis.read(buffer)) > 0) {
				fos.write(buffer, 0, read);
			}
		} finally {
			if(fis != null) {
				fis.close();
			}
			if(fos != null) {
				fos.close();
			}
		}
		
	}
}

