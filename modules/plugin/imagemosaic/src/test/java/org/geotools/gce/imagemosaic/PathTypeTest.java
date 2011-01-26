/**
 * 
 */
package org.geotools.gce.imagemosaic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import org.geotools.test.TestData;
import org.junit.Assert;
import org.junit.Test;

/**
 * Testing {@link PathType} class.
 * 
 * @author Simone Giannecchini, GeoSolutions SAS
 * @author Stefan Alfons Krueger (alfonx), Wikisquare.de : Support for jar:file:foo.jar/bar.properties like URLs
 *
 * @source $URL$
 */
public class PathTypeTest extends Assert {

	
	public PathTypeTest() {
	}
	
	@Test
	public void relative() throws FileNotFoundException, IOException{
		
		//get some test data
		final File testFile= TestData.file(this, "/rgb/global_mosaic_0.pgw");
		assertTrue(testFile.exists());
		
		// test it as a relative path to the test-data directory
		final URL temp=PathType.RELATIVE.resolvePath(TestData.url(this, ".").toExternalForm(), "rgb/global_mosaic_0.pgw");
		assertNotNull(temp);
		temp.openStream().close();
		
		// test error checks
		final URL temp1=PathType.RELATIVE.resolvePath(TestData.url(this, ".").toExternalForm(), "rgb/global_mosaic_0.pg");
		assertNull(temp1);
		try {
			temp1.openStream().close();
			fail("The relative URL "+temp1+" is not supposed to exist!");
		} catch (Exception e) {
		}
		
	}
	
	@Test
	public void absolute() throws FileNotFoundException, IOException{
		//get some test data
		final URL testFile= TestData.url(this, "/rgb/global_mosaic_0.pgw");
		testFile.openStream().close();
		
		// test it as a relative path to the test-data directory
		final URL temp=PathType.ABSOLUTE.resolvePath(TestData.url(this, ".").toExternalForm(), testFile.toExternalForm());
		assertNotNull(temp);
		temp.openStream().close();

		
		final URL temp1=PathType.ABSOLUTE.resolvePath(null, testFile.toExternalForm());
		assertNotNull(temp1);
		temp1.openStream().close();
		
		// test error checks using relative call
		final URL temp2=PathType.ABSOLUTE.resolvePath(TestData.url(this, ".").toExternalForm(), "rgb/global_mosaic_0.pg");
		assertNull(temp2);
		try {
			temp2.openStream().close();
			fail("The relative URL "+temp2+" is not supposed to exist!");
		} catch (Exception e) {
		}
		
		
	}

}
