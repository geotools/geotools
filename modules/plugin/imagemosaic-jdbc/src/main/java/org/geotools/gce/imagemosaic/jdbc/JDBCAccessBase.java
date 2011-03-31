/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.jdbc;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.media.jai.PlanarImage;
import javax.sql.DataSource;

import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.data.jdbc.datasource.DataSourceFinder;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.util.logging.Logging;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.sun.media.jai.codec.ByteArraySeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.SeekableStream;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

/**
 * This class is an abstract Base Class for implementing the JDBCAccess
 * interface.
 * 
 * JDBCAccess classes for different spatial extensions extend this base class.
 * 
 * 
 * @author Christian Mueller
 * @since 2.5
 */
abstract class JDBCAccessBase implements JDBCAccess {
	/** Logger. */
	protected final static Logger LOGGER = Logging
			.getLogger(JDBCAccessBase.class.getPackage().getName());

	private List<ImageLevelInfo> levelInfos = new ArrayList<ImageLevelInfo>();

	protected Config config;

	protected DataSource dataSource = null;

	/**
	 * Constructor
	 * 
	 * @param config
	 *            the Config object
	 * @throws IOException
	 */
	JDBCAccessBase(Config config) throws IOException {
		super();
		this.config = config;
		this.dataSource = DataSourceFinder.getDataSource(config
				.getDataSourceParams());
	}

	/**
	 * if the table name include a dot, the first part is assumed to be the sql
	 * schema name.
	 * 
	 * @param tn
	 *            the sql table name
	 * @return the schema name or null
	 */
	protected String getSchemaFromSpatialTable(String tn) {
		int index = tn.indexOf('.');

		if (index == -1) {
			return null;
		}

		return tn.substring(0, index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.gce.imagemosaic.jdbc.JDBCAccess#initialize()
	 */
	public void initialize() throws IOException {
		Connection con = null;

		try {
			con = dataSource.getConnection();

			if (con.getAutoCommit()) {
				con.setAutoCommit(false);
			}

			initFromDB(config.getCoverageName(), con);
			calculateExtentsFromDB(config.getCoverageName(), con);
			calculateResolutionsFromDB(config.getCoverageName(), con);
			// con.commit();
			con.close();

			for (ImageLevelInfo levelInfo : levelInfos) {
				if (LOGGER.isLoggable(Level.INFO))
					LOGGER.info(levelInfo.infoString());
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);

			try {
				// con.rollback();
				con.close();
			} catch (SQLException e1) {
			}

			LOGGER.severe(e.getMessage());
			throw new IOException(e.getMessage());
		}

		if (levelInfos.isEmpty()) {
			String msg = "No level available for " + config.getCoverageName();
			LOGGER.severe(msg);
			throw new IOException(msg);
		}

		// sort levelinfos
		SortedSet<ImageLevelInfo> sortColl = new TreeSet<ImageLevelInfo>();
		sortColl.addAll(levelInfos);
		levelInfos = new ArrayList<ImageLevelInfo>();
		levelInfos.addAll(sortColl);
	}

	/**
	 * Step 1 of the bootstrapping process. Read meta table and build the
	 * ImageLevelInfo objects
	 * 
	 * @param coverageName
	 *            the coverage name stored in the sql meta table
	 * @param con
	 *            jdbc connection
	 * @throws SQLException
	 * @throws IOException
	 */
	protected void initFromDB(String coverageName, Connection con)
			throws SQLException, IOException {
		PreparedStatement s = null;
		ResultSet res = null;

		try {
			String stmt = config.getSqlSelectCoverageStatement();
			// TODO, investigate, setString for oracle does not work
			stmt = stmt.replace("?", "'" + coverageName + "'");
			s = con.prepareStatement(stmt);
			// s.setString(1,coverageName);
			res = s.executeQuery();

			while (res.next()) {
				ImageLevelInfo imageLevelInfo = new ImageLevelInfo();
				imageLevelInfo.setCoverageName(coverageName);
				imageLevelInfo.setSpatialTableName(res.getString(config
						.getSpatialTableNameAtribute()));
				imageLevelInfo.setTileTableName((res.getString(config
						.getTileTableNameAtribute())));

				// check cardinalities
				if (config.getVerifyCardinality().booleanValue()) {
					imageLevelInfo.setCountFeature(new Integer(getRowCount(
							imageLevelInfo.getSpatialTableName(), con)));

					if (imageLevelInfo.getSpatialTableName().equals(
							imageLevelInfo.getTileTableName())) {
						imageLevelInfo.setCountTiles(imageLevelInfo
								.getCountFeature());
					} else {
						imageLevelInfo.setCountTiles(new Integer(getRowCount(
								imageLevelInfo.getTileTableName(), con)));
					}

					if (imageLevelInfo.getCountFeature().intValue() == 0) {
						LOGGER.severe("Table "
								+ imageLevelInfo.getSpatialTableName()
								+ " has no entries");
					} else if (imageLevelInfo.getCountTiles().intValue() == 0) {
						LOGGER.severe("Table "
								+ imageLevelInfo.getTileTableName()
								+ " has no entries");
					} else if (imageLevelInfo.getCountFeature().intValue() != imageLevelInfo
							.getCountTiles().intValue()) {
						if (LOGGER.isLoggable(Level.WARNING))
							LOGGER.log(Level.WARNING,
									"Consistency warning: number of features: "
											+ imageLevelInfo.getCountFeature()
											+ " number tiles: "
											+ imageLevelInfo.getCountTiles());
					} else {
						if (LOGGER.isLoggable(Level.FINE))
							LOGGER.fine("Number of features: "
									+ imageLevelInfo.getCountFeature()
									+ " number tiles: "
									+ imageLevelInfo.getCountTiles());
					}
				}

				imageLevelInfo.setExtentMaxX(new Double(res.getDouble(config
						.getMaxXAttribute())));

				if (res.wasNull()) {
					imageLevelInfo.setExtentMaxX(null);
				}

				imageLevelInfo.setExtentMaxY(new Double(res.getDouble(config
						.getMaxYAttribute())));

				if (res.wasNull()) {
					imageLevelInfo.setExtentMaxY(null);
				}

				imageLevelInfo.setExtentMinX(new Double(res.getDouble(config
						.getMinXAttribute())));

				if (res.wasNull()) {
					imageLevelInfo.setExtentMinX(null);
				}

				imageLevelInfo.setExtentMinY(new Double(res.getDouble(config
						.getMinYAttribute())));

				if (res.wasNull()) {
					imageLevelInfo.setExtentMinY(null);
				}

				imageLevelInfo.setResX(new Double(res.getDouble(config
						.getResXAttribute())));

				if (res.wasNull()) {
					imageLevelInfo.setResX(null);
				}

				imageLevelInfo.setResY(new Double(res.getDouble(config
						.getResYAttribute())));

				if (res.wasNull()) {
					imageLevelInfo.setResY(null);
				}

				if (config.getVerifyCardinality().booleanValue()) {
					if ((imageLevelInfo.getCountFeature().intValue() > 0)
							&& (imageLevelInfo.getCountTiles().intValue() > 0)) {
						levelInfos.add(imageLevelInfo);
					}
				} else {
					levelInfos.add(imageLevelInfo);
				}

				imageLevelInfo.setSrsId(getSRSID(imageLevelInfo, con));
				imageLevelInfo.setCrs(getCRS(imageLevelInfo, con));
			}
		} catch (SQLException e) {
			throw (e);
		} catch (IOException e1) {
			throw (e1);
		} finally {
			if (res != null) {
				res.close();
			}

			if (s != null) {
				s.close();
			}
		}
	}

	/**
	 * Get the Coordinate Reference System from the database for this
	 * ImageLevelInfo
	 * 
	 * @param li
	 *            ImageLevelInfo object
	 * @param con
	 *            JDBC Connection
	 * @return CoordinateReferenceSystem or null
	 * @throws IOException
	 */
	protected abstract CoordinateReferenceSystem getCRS(ImageLevelInfo li,
			Connection con) throws IOException;

	/**
	 * Get the Spatial Reference System identifier from the database for this
	 * ImageLevelInfo
	 * 
	 * @param li
	 *            ImageLevelInfo object
	 * @param con
	 *            JDBC Connection
	 * @return Ineger or null
	 * @throws IOException
	 */

	protected Integer getSRSID(ImageLevelInfo li, Connection con)
			throws IOException {
		return null;
	}

	/**
	 * @param li
	 *            ImageLevelInfo object
	 * @return sql select statement for querying the extent for li
	 */
	protected abstract String getExtentSelectStatment(ImageLevelInfo li);

	/**
	 * @param li
	 *            ImageLevelInfo object
	 * @param con
	 *            JDBC Connection
	 * @return Envelope for the extent for li
	 * @throws SQLException
	 * @throws IOException
	 */
	protected Envelope getExtent(ImageLevelInfo li, Connection con)
			throws SQLException, IOException {
		String statementString = getExtentSelectStatment(li);
		Envelope extent = null;
		PreparedStatement s = con.prepareStatement(statementString);
		ResultSet r = s.executeQuery();

		if (r.next()) {
			extent = new Envelope(
					new Coordinate(r.getDouble(1), r.getDouble(2)),
					new Coordinate(r.getDouble(3), r.getDouble(4)));
		}

		r.close();
		s.close();

		return extent;
	}

	/**
	 * Step 2 of the bootstrapping process.
	 * 
	 * Calculating the the extent for each image level (original + pyramids).
	 * This calculation is only done if the extent info in the master table is
	 * SQL NULL. After calculation the meta table is updated with the result to
	 * avoid this operation in the future.
	 * 
	 * @param coverageName
	 *            The coverage name in the sql meta table
	 * @param con
	 *            JDBC connection
	 * @throws SQLException
	 * @throws IOException
	 */
	void calculateExtentsFromDB(String coverageName, Connection con)
			throws SQLException, IOException {
		PreparedStatement stmt = con.prepareStatement(config
				.getSqlUpdateMosaicStatement());

		List<ImageLevelInfo> toBeRemoved = new ArrayList<ImageLevelInfo>();

		for (ImageLevelInfo li : levelInfos) {
			if (li.getCoverageName().equals(coverageName) == false) {
				continue;
			}

			if (li.calculateExtentsNeeded() == false) {
				continue;
			}

			Date start = new Date();
			if (LOGGER.isLoggable(Level.INFO))
				LOGGER.info("Calculate extent for " + li.toString());

			Envelope env = getExtent(li, con);

			if (env == null) {
				if (LOGGER.isLoggable(Level.WARNING))
					LOGGER.log(Level.WARNING, "No extent, removing this level");
				toBeRemoved.add(li);

				continue;
			}

			li.setExtentMaxX(new Double(env.getMaxX()));
			li.setExtentMaxY(new Double(env.getMaxY()));
			li.setExtentMinX(new Double(env.getMinX()));
			li.setExtentMinY(new Double(env.getMinY()));

			stmt.setDouble(1, li.getExtentMaxX().doubleValue());
			stmt.setDouble(2, li.getExtentMaxY().doubleValue());
			stmt.setDouble(3, li.getExtentMinX().doubleValue());
			stmt.setDouble(4, li.getExtentMinY().doubleValue());
			stmt.setString(5, li.getCoverageName());
			stmt.setString(6, li.getTileTableName());
			stmt.setString(7, li.getSpatialTableName());
			stmt.execute();

			long msecs = (new Date()).getTime() - start.getTime();

			if (LOGGER.isLoggable(Level.INFO))
				LOGGER.info("Calculate extent for " + li.toString()
						+ " finished in " + msecs + " ms ");
		}

		levelInfos.removeAll(toBeRemoved);

		if (stmt != null) {
			stmt.close();
		}
	}

	/**
	 * 
	 * Step 3 of the bootstrapping process.
	 * 
	 * Calculating the the resolution for each image level (original +
	 * pyramids). This calculation is only done if the resultion info in the
	 * master table is SQL NULL. After calculation the meta table is updated
	 * with the result to avoid this operation in the future.
	 * 
	 * @param coverageName
	 *            The coverage name in the sql meta table
	 * @param con
	 *            JDBC Connection
	 * @throws SQLException
	 * @throws IOException
	 */
	void calculateResolutionsFromDB(String coverageName, Connection con)
			throws SQLException, IOException {
		PreparedStatement stmt = null;

		stmt = con.prepareStatement(config.getSqlUpdateResStatement());

		List<ImageLevelInfo> toBeRemoved = new ArrayList<ImageLevelInfo>();

		for (ImageLevelInfo li : levelInfos) {
			if (li.getCoverageName().equals(coverageName) == false) {
				continue;
			}

			if (li.calculateResolutionNeeded() == false) {
				continue;
			}

			Date start = new Date();
			if (LOGGER.isLoggable(Level.INFO))
				LOGGER.info("Calculate resolutions for " + li.toString());

			double[] resolutions = getPixelResolution(li, con);

			if (resolutions == null) {
				if (LOGGER.isLoggable(Level.WARNING))
					LOGGER.log(Level.WARNING, "No image found, removing "
							+ li.toString());
				toBeRemoved.add(li);

				continue;
			}

			li.setResX(resolutions[0]);
			li.setResY(resolutions[1]);
			if (LOGGER.isLoggable(Level.INFO))
				LOGGER.info("ResX: " + li.getResX() + " ResY: " + li.getResY());

			// li.setColorModel(loadedImage.getColorModel());
			stmt.setDouble(1, li.getResX().doubleValue());
			stmt.setDouble(2, li.getResY().doubleValue());
			stmt.setString(3, li.getCoverageName());
			stmt.setString(4, li.getTileTableName());
			stmt.setString(5, li.getSpatialTableName());
			stmt.execute();

			long msecs = (new Date()).getTime() - start.getTime();

			if (LOGGER.isLoggable(Level.INFO))
				LOGGER.info("Calculate resolutions for " + li.toString()
						+ " finished in " + msecs + " ms ");
		}

		levelInfos.removeAll(toBeRemoved);

		if (stmt != null) {
			stmt.close();
		}
	}

	/**
	 * Sql statement to query the needed tiles for a request
	 * 
	 * @param levelInfo
	 *            ImageLevelInfo object
	 * @return
	 */
	protected abstract String getGridSelectStatement(ImageLevelInfo levelInfo);

	/**
	 * Set envelope as sql parameters into the grid select statement.
	 * 
	 * @param s
	 *            the grid select statement
	 * @param envelope
	 *            the requested envelope
	 * @param li
	 *            ImageLevelInfo object
	 * @throws SQLException
	 */
	protected abstract void setGridSelectParams(PreparedStatement s,
			GeneralEnvelope envelope, ImageLevelInfo li) throws SQLException;

	Envelope getBounds(int level) throws IOException {
		ImageLevelInfo li = levelInfos.get(level);

		return li.getEnvelope();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.gce.imagemosaic.jdbc.JDBCAccess#startTileDecoders(java.awt.Rectangle,
	 *      org.geotools.geometry.GeneralEnvelope,
	 *      org.geotools.gce.imagemosaic.jdbc.ImageLevelInfo,
	 *      java.util.concurrent.LinkedBlockingQueue)
	 */
	public void startTileDecoders(Rectangle pixelDimension,
			GeneralEnvelope requestEnvelope, ImageLevelInfo levelInfo,
			LinkedBlockingQueue<TileQueueElement> tileQueue,
			GridCoverageFactory coverageFactory) throws IOException {
		Date start = new Date();
		Connection con = null;
		List<ImageDecoderThread> threads = new ArrayList<ImageDecoderThread>();
		ExecutorService pool =  getExecutorServivicePool ();
		

		String statementString = getGridSelectStatement(levelInfo);

		try {
			con = dataSource.getConnection();

			PreparedStatement s = con.prepareStatement(statementString);
			setGridSelectParams(s, requestEnvelope, levelInfo);

			ResultSet r = s.executeQuery();

			while (r.next()) {
				byte[] tileBytes = getTileBytes(r);
				Envelope env = getEnvelopeFromResultSet(r);
				String location = r.getString(config
						.getKeyAttributeNameInSpatialTable());

				Rectangle2D tmp = new Rectangle2D.Double(env.getMinX(), env
						.getMinY(), env.getWidth(), env.getHeight());
				GeneralEnvelope tileGeneralEnvelope = new GeneralEnvelope(tmp);
				tileGeneralEnvelope
						.setCoordinateReferenceSystem(requestEnvelope
								.getCoordinateReferenceSystem());

				ImageDecoderThread thread = new ImageDecoderThread(tileBytes,
						location, tileGeneralEnvelope, pixelDimension,
						requestEnvelope, levelInfo, tileQueue, config);
//				thread.start();
				threads.add(thread);
				pool.execute(thread);

			}

			;
			r.close();
			s.close();

			// if (con.getAutoCommit() == false) {
			// con.commit();
			// }

			con.close();
		} catch (SQLException e) {
			try {
				// if (con.getAutoCommit() == false) {
				// con.rollback();
				// }

				con.close();
			} catch (SQLException e1) {
			}

			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			throw new IOException(e.getMessage());
		}

		if (LOGGER.isLoggable(Level.INFO))
			LOGGER
					.info("Getting " + threads.size() + " Tiles needs "
							+ ((new Date()).getTime() - start.getTime())
							+ " millisecs");

		// wait for all threads dto finish and write end marker
		pool.shutdown();
		try {
		    pool.awaitTermination(3600, TimeUnit.SECONDS); // wait for one hour 
                  } catch (InterruptedException e) {
                           throw new RuntimeException(e.getLocalizedMessage());
                }
		
//		for (AbstractThread thread : threads) {
//			try {
//				thread.join();
//			} catch (InterruptedException e) {
//				throw new RuntimeException(e.getLocalizedMessage());
//			}
//		}

		tileQueue.add(TileQueueElement.ENDELEMENT);

		if (LOGGER.isLoggable(Level.INFO))
			LOGGER
					.info("Getting and decoding  " + threads.size()
							+ " Tiles needs "
							+ ((new Date()).getTime() - start.getTime())
							+ " millisecs");
	}

	/**
	 * @param resultSet
	 *            Sql Result Set
	 * @return byte array containing image bytes from curren sql cursor location
	 * @throws SQLException
	 */
	protected byte[] getTileBytes(ResultSet resultSet) throws SQLException {
		byte[] buffer = new byte[16384];

		InputStream in = resultSet.getBinaryStream(config
				.getBlobAttributeNameInTileTable());

		if (in == null) {
			return null;
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {
			while (in.read(buffer) > 0)
				out.write(buffer);
		} catch (IOException e) {
		}

		return out.toByteArray();

		// tileBytes=resultSet.getBytes(columnindex);
	}

	/**
	 * @param tableName
	 *            sql table name
	 * @param con
	 *            JDBC Connection
	 * @return return number of rows in the table
	 * @throws SQLException
	 */
	private int getRowCount(String tableName, Connection con)
			throws SQLException {
		PreparedStatement s = con.prepareStatement("select count(*) from "
				+ tableName);
		ResultSet res = s.executeQuery();
		res.next();

		int count = res.getInt(1);
		res.close();
		s.close();

		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.gce.imagemosaic.jdbc.JDBCAccess#getLevelInfo(int)
	 */
	public ImageLevelInfo getLevelInfo(int level) {
		return levelInfos.get(level);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geotools.gce.imagemosaic.jdbc.JDBCAccess#getNumOverviews()
	 */
	public int getNumOverviews() {
		return levelInfos.size() - 1;
	}

	/**
	 * @param env
	 *            GeneralEnvelope
	 * @return Polygon object with the same boundary as env
	 */
	protected Polygon polyFromEnvelope(GeneralEnvelope env) {
		GeometryFactory factory = new GeometryFactory();

		Coordinate[] coords = new Coordinate[] {
				new Coordinate(env.getMinimum(0), env.getMinimum(1)),
				new Coordinate(env.getMinimum(0), env.getMaximum(1)),
				new Coordinate(env.getMaximum(0), env.getMaximum(1)),
				new Coordinate(env.getMaximum(0), env.getMinimum(1)),
				new Coordinate(env.getMinimum(0), env.getMinimum(1)) };

		return factory.createPolygon(factory.createLinearRing(coords),
				new LinearRing[0]);
	}

	/**
	 * 
	 * @param li
	 *            ImageLevelInfo object
	 * @return the tile name of a random chosen tile for li
	 */
	protected abstract String getRandomTileStatement(ImageLevelInfo li);

	protected Envelope getEnvelopeFromResultSet(ResultSet r)
			throws SQLException {
		Envelope result = new Envelope(new Coordinate(r.getDouble(2), r
				.getDouble(3)), new Coordinate(r.getDouble(4), r.getDouble(5)));

		return result;
	}

	/**
	 * @param li
	 *            ImageLevelInfo object
	 * @param con
	 *            JDBC Connection
	 * @return the resolution for li, based on a random chosen tile
	 * @throws SQLException
	 * @throws IOException
	 */
	protected double[] getPixelResolution(ImageLevelInfo li, Connection con)
			throws SQLException, IOException {
		double[] result = null;
		String statementString = getRandomTileStatement(li);
		PreparedStatement s = con.prepareStatement(statementString);
		ResultSet r = s.executeQuery();

		while (r.next()) {
			byte[] tileBytes = getTileBytes(r);

			if (tileBytes == null) {
				continue;
			}

			BufferedImage buffImage = null;
			li.setCanImageIOReadFromInputStream(true);
			try {
				buffImage = ImageIO.read(new ByteArrayInputStream(tileBytes));
			} catch (IOException e) {
			}

			if (buffImage == null) {
				if (LOGGER.isLoggable(Level.WARNING)) {
					LOGGER
							.warning("Image IO cannot read from ByteInputStream,use less efficient jai methods");
				}
				li.setCanImageIOReadFromInputStream(false);
				SeekableStream stream = new ByteArraySeekableStream(tileBytes);
				String decoderName = null;

				for (String dn : ImageCodec.getDecoderNames(stream)) {
					decoderName = dn;
					break;
				}

				ImageDecoder decoder = ImageCodec.createImageDecoder(
						decoderName, stream, null);
				PlanarImage img = PlanarImage.wrapRenderedImage(decoder
						.decodeAsRenderedImage());
				buffImage = img.getAsBufferedImage();
			}

			Envelope env = getEnvelopeFromResultSet(r);

			result = new double[] { env.getWidth() / buffImage.getWidth(),
					env.getHeight() / buffImage.getHeight() };

			break;
		}

		r.close();
		s.close();

		return result;
	}
	
       public ExecutorService getExecutorServivicePool () {
           int availableProcessors = Runtime.getRuntime().availableProcessors();
           LOGGER.info("Using "+ availableProcessors + " CPU(s)");
           return Executors.newFixedThreadPool(availableProcessors);
       }
}
