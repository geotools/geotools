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
package org.geotools.gce.imagemosaic;

import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.media.jai.widget.ScrollingImagePanel;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import junit.framework.JUnit4TestAdapter;
import junit.textui.TestRunner;

import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.postgis.PostgisNGDataStoreFactory;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.visitor.MaxVisitor;
import org.geotools.feature.visitor.MinVisitor;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.referencing.CRS;
import org.geotools.util.NullProgressListener;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * Testing {@link ImageMosaicReader}.
 * 
 * @author Simone Giannecchini, GeoSolutions
 * @author Stefan Alfons Krueger (alfonx), Wikisquare.de 
 * @since 2.3
 * 
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/plugin/imagemosaic/src/test/java/org/geotools/gce/imagemosaic/DataStoreTimeTest.java $
 */
@SuppressWarnings("deprecation")
public class DataStoreTimeTest{

	public static junit.framework.Test suite() { 
	    return new JUnit4TestAdapter(DataStoreTimeTest.class); 
	}

	/**
	 * Tests the {@link ImageMosaicReader} with default parameters for the
	 * various input params.
	 * 
	 * @throws IOException
	 * @throws MismatchedDimensionException
	 * @throws FactoryException 
	 * @throws InterruptedException 
	 * @throws CQLException 
	 */
	@Test
	@Ignore
	@SuppressWarnings("unused")
	public void timeTest() throws IOException,
			MismatchedDimensionException, FactoryException, InterruptedException, CQLException {
		
		final PrecisionModel precMod = new PrecisionModel(PrecisionModel.FLOATING);
		final GeometryFactory geomFactory = new GeometryFactory(precMod);
		final SimpleDateFormat iso801= new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
				
		final SimpleFeatureTypeBuilder featureBuilder = new SimpleFeatureTypeBuilder();
		featureBuilder.setName("time_mosaic");
		featureBuilder.setNamespaceURI("http://www.geo-solutions.it/");
		featureBuilder.add("location", String.class);
		final CoordinateReferenceSystem actualCRS=CRS.decode("EPSG:4326",true);
		featureBuilder.add("the_geom", Polygon.class,actualCRS);
		featureBuilder.add("ingestion", Date.class);
		featureBuilder.setDefaultGeometry("the_geom");
		
		final SimpleFeatureType schema = featureBuilder.buildFeatureType();
		System.out.println(DataUtilities.spec(schema));
		
		
		
		// get the params
		final PostgisNGDataStoreFactory spi= new PostgisNGDataStoreFactory();
		final Map<String, Serializable> params = new HashMap<String, Serializable>();	
		params.put(PostgisNGDataStoreFactory.PORT.key,5432);
		params.put(PostgisNGDataStoreFactory.HOST.key,"localhost");
		params.put(PostgisNGDataStoreFactory.SCHEMA.key,"public");
		params.put(PostgisNGDataStoreFactory.DATABASE.key,"test");
		params.put(PostgisNGDataStoreFactory.LOOSEBBOX.key,true);
		params.put(PostgisNGDataStoreFactory.USER.key,"itt");
		params.put(PostgisNGDataStoreFactory.PASSWD.key,"itttti");

		// create schema
		final JDBCDataStore datastore = spi.createDataStore(params);
		try{
			datastore.getSchema(schema.getTypeName());
		}catch (Exception e) 
		{
			datastore.createSchema(schema);
			final ReferencedEnvelope envelope=new ReferencedEnvelope(-180,180,-90,90,actualCRS);
			
			// insert features
			final FeatureWriter<SimpleFeatureType, SimpleFeature> fw = datastore.getFeatureWriterAppend(datastore.getTypeNames()[0], Transaction.AUTO_COMMIT);
			final List<Date> days= new ArrayList<Date>();
			for (int i=0;i < 10;i++){
				
				// create feature
				final SimpleFeature feature = fw.next();
				feature.setAttribute("location", "xxx");
				feature.setAttribute("the_geom", geomFactory.toGeometry(envelope));
				final Date day = new Date(109,11,i+1);
				days.add(day);
				feature.setAttribute("ingestion",day);
				fw.write();
				
				
			}
			fw.close();			
		}
		
		
		
		// now read it back with filtering
		
		
		// equality 
//		final Filter temporal=FACTORY.equal(FACTORY.property("ingestion"), FACTORY.literal(days.get(0)),true);
		
		// max and min
		DefaultQuery query= new DefaultQuery(datastore.getTypeNames()[0]);//, temporal);
		final MaxVisitor max = new MaxVisitor("ingestion");
		datastore.getFeatureSource(datastore.getTypeNames()[0]).accepts(query,max, new NullProgressListener());
		System.out.println("max "+max.getResult().toString());
		
		final MinVisitor min = new MinVisitor("ingestion");
		datastore.getFeatureSource(datastore.getTypeNames()[0]).accepts(query,min, new NullProgressListener());
		System.out.println("min "+min.getResult().toString());
		
		
		// nearest neighbor
		// Max of before
		final Filter before = CQL.toFilter("ingestion BEFORE 2009-12-05T05:00:00Z");
		query= new DefaultQuery(datastore.getTypeNames()[0],before);
		datastore.getFeatureSource(datastore.getTypeNames()[0]).accepts(query,max, new NullProgressListener());
		// did we get anything?
		if(max.getResult().getValue()==null)
			System.out.println("We got no result for max before");
		else
			System.out.println("max before "+max.getResult().toString());		
		
		final Filter after = CQL.toFilter("ingestion AFTER 2009-12-01T05:00:00Z");
		query= new DefaultQuery(datastore.getTypeNames()[0],after);
		datastore.getFeatureSource(datastore.getTypeNames()[0]).accepts(query,min, new NullProgressListener());
		if(min.getResult().getValue()==null)
			System.out.println("We got no result for min after");
		else
			System.out.println("min after "+min.getResult().toString());			
		final FeatureReader<SimpleFeatureType, SimpleFeature> reader = datastore.getFeatureReader(query, Transaction.AUTO_COMMIT);
		while(reader.hasNext())
		{
			final SimpleFeature elem = reader.next();
			System.out.println(elem.toString());
		}
		reader.close();
		
		
			

	}
	
	/**
	 * Shows the provided {@link RenderedImage} ina {@link JFrame} using the
	 * provided <code>title</code> as the frame's title.
	 * 
	 * @param image
	 *            to show.
	 * @param title
	 *            to use.
	 */
	static void show(RenderedImage image, String title) {
		final JFrame jf = new JFrame(title);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.getContentPane().add(new ScrollingImagePanel(image, 800, 800));
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				jf.pack();
				jf.setVisible(true);

			}
		});

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestRunner.run(DataStoreTimeTest.suite());

	}


}
