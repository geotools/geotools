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

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.media.jai.PlanarImage;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.simple.SimpleFeature;

import com.sun.media.jai.codec.ByteArraySeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.SeekableStream;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKBWriter;

/**
 * Utility class for importing tiles and georeferencing info into the database.
 * 
 * 
 * Supported methods:
 * 
 * Importing images having the georeferencing information in a world file
 * Importing images having the georeferencing information in a csv file
 * Importing images having the georeferencing information in a shape file
 * 
 * @author mcr
 * 
 *
 *
 * @source $URL$
 */
public class Import extends AbstractCmd {
	class ImageFilter extends Object implements FileFilter {
		public ImageFilter(String extension) {
			this.extension = extension;
		}

		String extension;

		public boolean accept(File file) {
			int index = file.getName().lastIndexOf(".");
			if (index == -1)
				return false;
			String ext = file.getName().substring(index + 1);
			return extension.equalsIgnoreCase(ext);

		}

	}

	public enum ImportTyp {
		SHAPE, CSV, DIR
	};

	private final static int DefaultCommitCount = 100;

	private final static String UsageInfo = "Importing from a shapefile or csv file\n"
			+ "\n Importing all levels \n\n"
			+ "-config URLOrFile -spatialTNPrefix spatialTablePrefix -tileTNPrefix tileTablePrefix [-commitCount commitCount] -shape shapeURLOrFile -shapeKeyField shapeKeyField\n"
			+ "Importing from a csv file\n"
			+ "-config URLOrFile -spatialTNPrefix spatialTablePrefix -tileTNPrefix tileTablePrefix [-commitCount commitCount] -csv csvURLOrFile -csvDelim csvDelim\n"
			+ "Importing using world wfiles\n"
			+ "-config URLOrFile -spatialTNPrefix spatialTablePrefix -tileTNPrefix tileTablePrefix [-commitCount commitCount] -dir directory -ext extension"
			+ "\n\n Importing one level \n\n"
			+ "-config URLOrFile -spatialTN spatialTableName -tileTN tileTableName [-commitCount commitCount] -shape shapeURLOrFile -shapeKeyField shapeKeyField\n"
			+ "Importing from a csv file\n"
			+ "-config URLOrFile -spatialTN spatialTableName -tileTN tileTableName [-commitCount commitCount] -csv csvURLOrFile -csvDelim csvDelim\n"
			+ "Importing using world wfiles\n"
			+ "-config URLOrFile -spatialTN spatialTableName -tileTN tileTableName [-commitCount commitCount] -dir directory -ext extension"
			+ "\n\n" + "The default for commitCount is 100\n";

	private final static String NotSupported = "Imporft not supported for type ";
	
	private Config config;

	private ImportTyp typ;

	private String spatialTableName;

	private String tileTableName;

	private Connection con;

	private String oracleInsertSpatial;

	private PreparedStatement sqlInsertImage;

	private PreparedStatement sqlInsertSpatial;

	private URL shapeFileUrl;

	private URL csvFileURL;

	private URL directoryURL;

	private String extension;

	private String csvDelimiter;

	private BufferedReader csvReader;

	private String keyInShapeFile;

	private Geometry currentGeom = null;

	private String currentLocation = null;

	private int currentPos;

	private int total;

	private int srs;

	private int commitCount = DefaultCommitCount;

	private SimpleFeatureIterator featureIterator;

	private SimpleFeatureCollection featureColl;

	private File imageFiles[];

	private Logger logger;

	Import(Config config, ImportParam param, int commitCount, Connection con)
			throws IOException, SQLException {
		this.config = config;
		this.commitCount = commitCount;
		this.spatialTableName = param.getSpatialTableName();
		this.typ = param.getTyp();
		this.tileTableName = param.getTileTableName();

		if (typ == ImportTyp.SHAPE) {
			this.shapeFileUrl = param.getSourceURL();
			this.keyInShapeFile = param.getSourceParam();
		} else if (typ == ImportTyp.CSV) {
			this.csvFileURL = param.getSourceURL();
			this.csvDelimiter = param.getSourceParam();
		}
		if (typ == ImportTyp.DIR) {
			this.directoryURL = param.getSourceURL();
			this.extension = param.getSourceParam();
		}

		this.logger = Logger.getLogger("Import " + "<" + spatialTableName
				+ "><" + tileTableName + ">");
		this.con = con;

		calculateSRS();

		if (isJoined() == false) {
			sqlInsertImage = con.prepareStatement("insert into  "
					+ tileTableName + " ( "
					+ config.getBlobAttributeNameInTileTable() + ","
					+ config.getKeyAttributeNameInTileTable()
					+ " ) values (?,?)");
		}

		prepareSpatialInsert();
	}

	private void terminate() throws SQLException {
		try {
			if (sqlInsertImage != null) {
				sqlInsertImage.close();
			}

			if (sqlInsertSpatial != null) {
				sqlInsertSpatial.close();
			}
		} catch (SQLException e) {
			logError(e, null);
			throw e;
		}
	}

	public static void start(String[] args) {
		Config config = null;
		String spatialTableName = null;
		String tileTableName = null;
		String spatialTablePrefix = null;
		String tileTablePrefix = null;

		int commitCount = DefaultCommitCount;
		URL csvUrl = null;
		URL shapeUrl = null;
		String csvDelim = null;
		String shapeKeyField = null;
		String extension = null, dir = null;

		for (int i = 0; i < args.length; i++) {
			if (args[i].equals(CONFIGPARAM)) {
				try {
					config = Config.readFrom(getURLFromString(args[i + 1]));
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(1);
				}
		                if (config !=null && (config.getSpatialExtension()==SpatialExtension.GEORASTER ||
		                        config.getSpatialExtension()==SpatialExtension.CUSTOM)) {
		                    System.out.println(NotSupported+config.getSpatialExtension().toString());
		                    System.exit(1);
		                }

				i++;
			} else if (args[i].equals("-spatialTN")) {
				spatialTableName = args[i + 1];
				i++;
			} else if (args[i].equals("-tileTN")) {
				tileTableName = args[i + 1];
				i++;
			} else if (args[i].equals(SPATIALTNPREFIXPARAM)) {
				spatialTablePrefix = args[i + 1];
				i++;
			} else if (args[i].equals(TILETNPREFIXPARAM)) {
				tileTablePrefix = args[i + 1];
				i++;
			} else if (args[i].equals("-commitCount")) {
				commitCount = new Integer(args[i + 1]);
				i++;
			} else if (args[i].equals("-shape")) {
				shapeUrl = getURLFromString(args[i + 1]);
				if (shapeUrl == null) {
					System.out.println("Cannot open " + args[i + 1]);
					System.exit(1);
				}
				i++;
			} else if (args[i].equals("-csv")) {
				csvUrl = getURLFromString(args[i + 1]);
				if (csvUrl == null) {
					System.out.println("Cannot open " + args[i + 1]);
					System.exit(1);
				}
				i++;
			} else if (args[i].equals("-csvDelim")) {
				csvDelim = args[i + 1];
				i++;
			} else if (args[i].equals("-shapeKeyField")) {
				shapeKeyField = args[i + 1];
				i++;
			} else if (args[i].equals("-dir")) {
				dir = args[i + 1];
				File f = new File(dir);
				if (f.exists() == false) {
					System.out.println("Cannot open " + args[i + 1]);
					System.exit(1);
				}
				i++;
			} else if (args[i].equals("-ext")) {
				extension = args[i + 1];
				i++;
			} else {
				System.out.println("Unkwnown option: " + args[i]);
				System.exit(1);
			}
		}

		if (config == null) { // check valid config
			System.out.println(UsageInfo);
			System.exit(1);
		}
		

		// check if prefix or level import
		boolean isLevelImport = false, isPrefixImport = false;

		if ((spatialTableName != null) && (tileTableName != null))
			isLevelImport = true;

		if ((spatialTablePrefix != null) && (tileTablePrefix != null))
			isPrefixImport = true;

		if ((isLevelImport && isPrefixImport)
				|| (isLevelImport == false && isPrefixImport == false)) {
			System.out.println(UsageInfo);
			System.exit(1);
		}

		ImportTyp typ = null;
		int countNull = 0;
		if (shapeUrl == null)
			countNull++;
		else
			typ = ImportTyp.SHAPE;

		if (csvUrl == null)
			countNull++;
		else
			typ = ImportTyp.CSV;

		if (dir == null)
			countNull++;
		else
			typ = ImportTyp.DIR;

		if (countNull != 2) { // select exaclty one image source
			System.out.println(UsageInfo);
			System.exit(1);
		}

		if (((shapeUrl != null) && (shapeKeyField == null))
				|| ((csvUrl != null) && (csvDelim == null))
				|| ((dir != null) && (extension == null))) {
			System.out.println(UsageInfo);
			System.exit(1);
		}

		Connection con = null;

		try {
			Class.forName(config.getDriverClassName());
			con = DriverManager.getConnection(config.getJdbcUrl(), config
					.getUsername(), config.getPassword());
			if (con.getAutoCommit()) // no autocommit 
			    con.setAutoCommit(false);
			List<ImportParam> importParamList = new ArrayList<ImportParam>();

			if (isLevelImport) {
				if (typ == ImportTyp.SHAPE)
					importParamList.add(new ImportParam(spatialTableName,
							tileTableName, shapeUrl, shapeKeyField,
							ImportTyp.SHAPE));
				if (typ == ImportTyp.CSV)
					importParamList.add(new ImportParam(spatialTableName,
							tileTableName, csvUrl, csvDelim, ImportTyp.CSV));
				if (typ == ImportTyp.DIR)
					importParamList.add(new ImportParam(spatialTableName,
							tileTableName, new File(dir).toURI().toURL(),
							extension, ImportTyp.DIR));
			}
			if (isPrefixImport) {
				if (typ == ImportTyp.SHAPE)
					fillImportParamList(spatialTablePrefix, tileTablePrefix,
							shapeUrl, shapeKeyField, ImportTyp.SHAPE,
							importParamList);
				if (typ == ImportTyp.CSV)
					fillImportParamList(spatialTablePrefix, tileTablePrefix,
							csvUrl, csvDelim, ImportTyp.CSV, importParamList);
				if (typ == ImportTyp.DIR)
					fillImportParamList(spatialTablePrefix, tileTablePrefix,
							new File(dir).toURI().toURL(), extension,
							ImportTyp.DIR, importParamList);
			}
			for (ImportParam param : importParamList) {
				Import imp = new Import(config, param, commitCount, con);
				imp.fillSpatialTable();
				con.commit();
			}
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.exit(0);
	}

	public static void fillImportParamList(String spatialTablePrefix,
			String tileTablePrefix, URL sourceUrl, String sourceParam,
			Import.ImportTyp typ, List<ImportParam> importParamList) {
		// base image
		ImportParam param = new ImportParam(
				getTableName(spatialTablePrefix, 0), getTableName(
						tileTablePrefix, 0), sourceUrl, sourceParam, typ);
		importParamList.add(param);

		int level = 1;
		String path = sourceUrl.getPath();
		while (true) { // find pyramids
			String pyramidPath = null;
			int index = path.lastIndexOf('/');
			if (typ == ImportTyp.CSV || typ == ImportTyp.SHAPE) {
				if (index == -1)
					pyramidPath = level + "/" + path;
				else {
					pyramidPath = path.substring(0, index + 1) + level + "/"
							+ path.substring(index + 1, path.length());
				}
				URL pyramidUrl = null;
				try {
					pyramidUrl = new URL(sourceUrl.getProtocol(), sourceUrl
							.getHost(), sourceUrl.getPort(), pyramidPath);
					pyramidUrl.openStream().close();
				} catch (Exception e) {
					return; // no pyramid found
				}
				param = new ImportParam(
						getTableName(spatialTablePrefix, level), getTableName(
								tileTablePrefix, level), pyramidUrl,
						sourceParam, typ);
				importParamList.add(param);

			}
			if (typ == ImportTyp.DIR) {
				pyramidPath = path;
				if (pyramidPath.endsWith("/") == false)
					pyramidPath += "/";
				pyramidPath += level;
				File directory = new File(pyramidPath);
				if (directory.exists()) {
					URL directoryURL = null;
					try {
						directoryURL = directory.toURI().toURL();
					} catch (MalformedURLException e) {
						// should not happen
					}
					param = new ImportParam(getTableName(spatialTablePrefix,
							level), getTableName(tileTablePrefix, level),
							directoryURL, sourceParam, typ);
					importParamList.add(param);

				} else { // no further pyramid
					return;
				}
			}
			level++;
		}

	}

	private void prepareSpatialInsert() throws SQLException {
		if (config.getSpatialExtension() == SpatialExtension.UNIVERSAL) {
			if (isJoined()) {
				sqlInsertSpatial = con.prepareStatement("INSERT INTO "
						+ spatialTableName + " ("
						+ config.getKeyAttributeNameInSpatialTable() + ","
						+ config.getTileMinXAttribute() + ","
						+ config.getTileMinYAttribute() + ","
						+ config.getTileMaxXAttribute() + ","
						+ config.getTileMaxYAttribute() + ","
						+ config.getBlobAttributeNameInTileTable()
						+ ") VALUES  (?,?,?,?,?,?)");
			} else {
				sqlInsertSpatial = con.prepareStatement("INSERT INTO "
						+ spatialTableName + " ("
						+ config.getKeyAttributeNameInSpatialTable() + ","
						+ config.getTileMinXAttribute() + ","
						+ config.getTileMinYAttribute() + ","
						+ config.getTileMaxXAttribute() + ","
						+ config.getTileMaxYAttribute()
						+ ") VALUES  (?,?,?,?,?)");
			}

			return;
		}

		String geometryParam = null;

		if (config.getSpatialExtension() == SpatialExtension.DB2) {
			geometryParam = "db2gse.st_tomultipolygon(db2gse.st_geometry(cast (? as BLOB(4096)),"
					+ srs + "))";
		} else if (config.getSpatialExtension() == SpatialExtension.POSTGIS) {
			geometryParam = "geomfromwkb(?," + srs + ")";
		} else if (config.getSpatialExtension() == SpatialExtension.MYSQL) {
			geometryParam = "geomfromwkb(?)";
		} else if (config.getSpatialExtension() == SpatialExtension.ORACLE) {
			geometryParam = "{0}"; // special handling for oracle
		}

		String statement = null;

		if (isJoined()) {
			statement = "INSERT INTO " + spatialTableName + " ("
					+ config.getKeyAttributeNameInSpatialTable() + ","
					+ config.getGeomAttributeNameInSpatialTable() + ","
					+ config.getBlobAttributeNameInTileTable()
					+ ") VALUES  (?," + geometryParam + ",?)";
		} else {
			statement = "INSERT INTO " + spatialTableName + " ("
					+ config.getKeyAttributeNameInSpatialTable() + ","
					+ config.getGeomAttributeNameInSpatialTable()
					+ ") VALUES  (?," + geometryParam + ")";
		}

		if (config.getSpatialExtension() == SpatialExtension.ORACLE) {
			oracleInsertSpatial = statement;
		} else {
			sqlInsertSpatial = con.prepareStatement(statement);
		}
	}

	private void truncateTables() throws SQLException {
		String truncateTable = "truncate table {0}";

		if (config.getSpatialExtension() == SpatialExtension.DB2) {
			truncateTable = "alter table {0} activate not logged initially with empty table";
		}

		logInfo("Truncating table : " + spatialTableName);
		con.prepareStatement(
				MessageFormat.format(truncateTable,
						new Object[] { spatialTableName })).execute();

		if (isJoined() == false) {
			logInfo("Truncating table : " + tileTableName);
			con.prepareStatement(
					MessageFormat.format(truncateTable,
							new Object[] { tileTableName })).execute();
		}
	}

	private void calculateSRS() throws SQLException, IOException {
		String schema = getSchema();
		String srsSelect = null;

		if (config.getSpatialExtension() == SpatialExtension.POSTGIS) {
			srsSelect = JDBCAccessPostGis.SRSSelect;
		} else if (config.getSpatialExtension() == SpatialExtension.DB2) {
			if (schema == null) {
				srsSelect = JDBCAccessDB2.SRSSelectCurrentSchema;
			} else {
				srsSelect = JDBCAccessDB2.SRSSelect;
			}
		} else if (config.getSpatialExtension() == SpatialExtension.ORACLE) {
			if (schema == null) {
				srsSelect = JDBCAccessOracle.SRSSelectCurrentSchema;
			} else {
				srsSelect = JDBCAccessOracle.SRSSelect;
			}
		} else {
			srs = 0;

			return;
		}

		PreparedStatement ps = con.prepareStatement(srsSelect);

		if (schema != null) {
			ps.setString(1, schema);
			ps.setString(2, spatialTableName);
			ps.setString(3, config.getGeomAttributeNameInSpatialTable());
		} else {
			ps.setString(1, spatialTableName);
			ps.setString(2, config.getGeomAttributeNameInSpatialTable());
		}

		ResultSet resultSet = ps.executeQuery();

		if (resultSet.next()) {
			srs = resultSet.getInt(1);
		} else {
			String msg = srsSelect + " has no result for "
					+ ((schema != null) ? (schema + ",") : "")
					+ spatialTableName + ","
					+ config.getGeomAttributeNameInSpatialTable();
			throw new IOException(msg);
		}

		resultSet.close();
		ps.close();
	}

	private boolean isJoined() {
		return spatialTableName.equals(tileTableName);
	}

	private void insertImage(URL imageUrl, byte[] imageBytes)
			throws SQLException, IOException {
		try {
			sqlInsertImage.setBytes(1, imageBytes);

			String path = imageUrl.getPath();
			String location = new File(path).getName();
			sqlInsertImage.setString(2, location);
			sqlExecute(sqlInsertImage);
		} catch (SQLException ex) {
			logError(ex, null);
			throw ex;
		}
	}

	private void insertMasterRecord() throws Exception {
		deleteExistingMasterRecord();
		String statmentString = "INSERT INTO " + config.getMasterTable() + "("
				+ config.getCoverageNameAttribute() + ","
				+ config.getTileTableNameAtribute() + ","
				+ config.getSpatialTableNameAtribute() + ") VALUES (?,?,?)";
		PreparedStatement ps = con.prepareStatement(statmentString);
		ps.setString(1, config.getCoverageName());
		ps.setString(2, tileTableName);
		ps.setString(3, spatialTableName);
		ps.execute();
		ps.close();
	}

	private void deleteExistingMasterRecord() throws Exception {
		int count = 0;
		String statmentString = "SELECT count(*) from "
				+ config.getMasterTable() + " where "
				+ config.getCoverageNameAttribute() + " = ? " + " and "
				+ config.getTileTableNameAtribute() + " = ? " + " and "
				+ config.getSpatialTableNameAtribute() + " = ? ";
		PreparedStatement ps = con.prepareStatement(statmentString);
		ps.setString(1, config.getCoverageName());
		ps.setString(2, tileTableName);
		ps.setString(3, spatialTableName);
		ResultSet rs = ps.executeQuery();
		if (rs.next())
			count = rs.getInt(1);
		rs.close();
		ps.close();

		if (count == 0)
			return; // no existing master Record

		statmentString = "DELETE FROM " + config.getMasterTable() + " where "
				+ config.getCoverageNameAttribute() + " = ? " + " and "
				+ config.getTileTableNameAtribute() + " = ? " + " and "
				+ config.getSpatialTableNameAtribute() + " = ? ";
		ps = con.prepareStatement(statmentString);
		ps.setString(1, config.getCoverageName());
		ps.setString(2, tileTableName);
		ps.setString(3, spatialTableName);
		ps.execute();
		ps.close();
	}

	private byte[] getImageBytes(URL url) throws IOException {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			InputStream in = url.openStream();
			int len;
			byte[] buff = new byte[4096];

			while ((len = in.read(buff)) > 0)
				out.write(buff, 0, len);

			out.close();
			in.close();

			return out.toByteArray();
		} catch (IOException ex) {
			logError(ex, "Error reading image");
			throw ex;
		}
	}

	private SimpleFeatureIterator getFeatureIterator()
			throws IOException {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("url", shapeFileUrl);

			DataStore shapefile = DataStoreFinder.getDataStore(map);

			int index = shapeFileUrl.getPath().lastIndexOf('/');
			int startIndex = (index == -1) ? 0 : (index + 1);
			index = shapeFileUrl.getPath().lastIndexOf('.');

			int endIndex = (index == -1) ? shapeFileUrl.getPath().length()
					: index;
			String layerName = shapeFileUrl.getPath().substring(startIndex,
					endIndex);
			SimpleFeatureSource contents = shapefile
					.getFeatureSource(layerName);
			featureColl = contents.getFeatures();
			total = featureColl.size();
			logTotalInfo();

			return featureColl.features();
		} catch (IOException ex) {
			logError(ex, "Cannot open shape file");
			throw ex;
		}
	}

	private URL calculateImageUrl() {

		if (typ == ImportTyp.DIR) {
			try {
				return imageFiles[currentPos - 1].toURI().toURL();
			} catch (MalformedURLException e1) {
			}
		}

		URL startUrl = null;
		if (typ == ImportTyp.SHAPE)
			startUrl = shapeFileUrl;
		if (typ == ImportTyp.CSV)
			startUrl = csvFileURL;

		String path = startUrl.getPath();
		int index = path.lastIndexOf('/');
		String imagePath = null;

		if (index == -1) {
			imagePath = currentLocation;
		} else {
			imagePath = path.substring(0, index + 1) + currentLocation;
		}

		try {
			return new URL(startUrl.getProtocol(), startUrl.getHost(), startUrl
					.getPort(), imagePath);
		} catch (MalformedURLException e) {
			logError(e, e.getMessage());

			return null;
		}
	}

	private BufferedImage readImage2(byte[] imageBytes) throws IOException {

		SeekableStream stream = new ByteArraySeekableStream(imageBytes);
		String decoderName = null;

		for (String dn : ImageCodec.getDecoderNames(stream)) {
			decoderName = dn;
			break;
		}

		ImageDecoder decoder = ImageCodec.createImageDecoder(decoderName,
				stream, null);
		PlanarImage img = PlanarImage.wrapRenderedImage(decoder
				.decodeAsRenderedImage());
		return img.getAsBufferedImage();
	}

	void fillSpatialTable() throws Exception {
		truncateTables();

		int insertCount = 0;

		while (next()) {
			URL imageUrl = calculateImageUrl();
			byte[] imageBytes = getImageBytes(imageUrl);
			if (typ == ImportTyp.DIR) {

				BufferedImage image = null;
				try {
					image = ImageIO.read(new ByteArrayInputStream(imageBytes));
				} catch (IOException e) {
					image = readImage2(imageBytes);
				}
				if (image == null)
					image = readImage2(imageBytes);

				currentGeom = getGeomFromWorldFile(imageFiles[currentPos - 1],
						image.getWidth(), image.getHeight());
			}

			if (config.getSpatialExtension() == SpatialExtension.UNIVERSAL) {
				sqlInsertSpatial.setString(1, currentLocation);

				Envelope env = currentGeom.getEnvelopeInternal();
				sqlInsertSpatial.setDouble(2, env.getMinX());
				sqlInsertSpatial.setDouble(3, env.getMinY());
				sqlInsertSpatial.setDouble(4, env.getMaxX());
				sqlInsertSpatial.setDouble(5, env.getMaxY());

				if (isJoined()) {
					sqlInsertSpatial.setBytes(6, imageBytes);
				} else {
					insertImage(imageUrl, imageBytes);
				}

				sqlExecute(sqlInsertSpatial);
			} else if ((config.getSpatialExtension() == SpatialExtension.DB2)
					|| (config.getSpatialExtension() == SpatialExtension.MYSQL)
					|| (config.getSpatialExtension() == SpatialExtension.POSTGIS)) {
				sqlInsertSpatial.setString(1, currentLocation);

				WKBWriter w = new WKBWriter();
				byte[] wkb = w.write(currentGeom);
				sqlInsertSpatial.setBytes(2, wkb);

				if (isJoined()) {
					sqlInsertSpatial.setBytes(3, imageBytes);
				} else {
					insertImage(imageUrl, imageBytes);
				}

				sqlExecute(sqlInsertSpatial);
			} else if (config.getSpatialExtension() == SpatialExtension.ORACLE) {
				String stmt = MessageFormat.format(oracleInsertSpatial,
						getOracleToGemoetryClause(currentGeom));
				sqlInsertSpatial = con.prepareStatement(stmt);
				sqlInsertSpatial.setString(1, currentLocation);

				if (isJoined()) {
					sqlInsertSpatial.setBytes(2, imageBytes);
				} else {
					insertImage(imageUrl, imageBytes);
				}

				sqlInsertSpatial.execute();
				sqlInsertSpatial.close();

				// sqlExecute(sqlInsertSpatial);
			}

			insertCount++;
			logInfo("Inserted tile " + currentLocation + " : " + insertCount
					+ "/" + total);

			if ((insertCount % commitCount) == 0) {
				sqlCommit();
			}
		}

		insertMasterRecord();
		sqlCommit();
		terminate();
		logInfo("FINISHED");
	}

	private boolean next() throws IOException {
		try {
			if (typ == ImportTyp.SHAPE) {
				if (currentLocation == null) {
					featureIterator = getFeatureIterator();
				}

				if (featureIterator.hasNext() == false) {
					featureColl.close(featureIterator);

					return false;
				}

				SimpleFeature feature = featureIterator.next();
				currentGeom = (Geometry) feature.getDefaultGeometry();
				currentPos++;
				currentLocation = (String) feature.getAttribute(keyInShapeFile);

				return true;
			} else if (typ == ImportTyp.CSV) {
				if (currentLocation == null) {
					csvReader = new BufferedReader(new InputStreamReader(
							csvFileURL.openStream()));

					while (csvReader.readLine() != null)
						total++;

					logTotalInfo();
					csvReader.close();
					csvReader = new BufferedReader(new InputStreamReader(
							csvFileURL.openStream()));
				}

				String line = csvReader.readLine();

				if (line == null) {
					csvReader.close();

					return false;
				}

				StringTokenizer tok = new StringTokenizer(line, csvDelimiter);
				currentLocation = tok.nextToken();

				Double minx = new Double(tok.nextToken());
				Double maxx = new Double(tok.nextToken());
				Double miny = new Double(tok.nextToken());
				Double maxy = new Double(tok.nextToken());

				currentPos++;

				GeometryFactory factory = new GeometryFactory();
				Coordinate[] coords = new Coordinate[] {
						new Coordinate(minx, miny), new Coordinate(minx, maxy),
						new Coordinate(maxx, maxy), new Coordinate(maxx, miny),
						new Coordinate(minx, miny) };
				Polygon poly = factory.createPolygon(factory
						.createLinearRing(coords), new LinearRing[0]);
				currentGeom = factory
						.createMultiPolygon(new Polygon[] { poly });

				return true;
			} else if (typ == ImportTyp.DIR) {
				if (currentLocation == null) {
					File dir = DataUtilities.urlToFile(directoryURL);
					imageFiles = dir.listFiles(new ImageFilter(extension));
					if (imageFiles == null) {
						logInfo("No files found in: " + dir.getPath()
								+ " with extension " + extension);
						System.exit(1);
					}
					total = imageFiles.length;
					logTotalInfo();

				}
				if (currentPos == total)
					return false;
				File imageFile = imageFiles[currentPos];
				currentLocation = imageFile.getName();
				currentGeom = null; // cant calculate at this point
				currentPos++;
				return true;
			} else {
				return false;
			}
		} catch (IOException ex) {
			logError(ex, null);
			throw ex;
		}
	}

	private Geometry getGeomFromWorldFile(File imageFile, int width, int height)
			throws IOException {
		StringBuffer buff = new StringBuffer(imageFile.getAbsolutePath());

		// try to find world file, e.g for *.png *.pgw
		char charToRemove = buff.charAt(buff.length() - 2);
		buff = buff.deleteCharAt(buff.length() - 2);
		if (Character.isUpperCase(charToRemove))
			buff.append("W");
		else
			buff.append("w");

		File f = new File(buff.toString());

		if (f.exists() == false) { // check if worldfile has "wld" extension
			f = null;
			int index = buff.lastIndexOf(".");
			if (index != -1) {
				buff.delete(index + 1, buff.length());
				buff.append("wld");
				f = new File(buff.toString());
			}
		}

		if (f.exists() == false)
			throw new IOException("Cannot find world file for "
					+ imageFile.getAbsolutePath());

		BufferedReader in = new BufferedReader(new FileReader(f));
		Double resx = new Double(in.readLine());
		in.readLine(); // skip rotate x
		in.readLine(); // skip rotaty y
		Double resy = new Double(in.readLine());
		Double ulx = new Double(in.readLine());
		Double uly = new Double(in.readLine());

		in.close();

		double minx = ulx;
		double maxx = ulx + width * resx;
		if (resy > 0)
			resy *= -1;

		double miny = uly + height * resy;
		double maxy = uly;
		GeometryFactory factory = new GeometryFactory();
		Coordinate[] coords = new Coordinate[] { new Coordinate(minx, miny),
				new Coordinate(minx, maxy), new Coordinate(maxx, maxy),
				new Coordinate(maxx, miny), new Coordinate(minx, miny) };
		Polygon poly = factory.createPolygon(factory.createLinearRing(coords),
				new LinearRing[0]);
		return factory.createMultiPolygon(new Polygon[] { poly });

	}

	private void logError(Exception e, String msg) {
		logger.log(Level.SEVERE, msg, e);
	}

	private void logInfo(String msg) {
		if (logger.isLoggable(Level.INFO))
			logger.log(Level.INFO, msg);
	}

	private void sqlExecute(PreparedStatement ps) throws SQLException {
		ps.addBatch();
	}

	private void sqlCommit() throws SQLException {
		if (sqlInsertImage != null) {
			sqlInsertImage.executeBatch();
		}

		if (config.getSpatialExtension() != SpatialExtension.ORACLE) {
			sqlInsertSpatial.executeBatch();
		}

		con.commit();
	}

	private String getSchema() {
		int index = spatialTableName.indexOf('.');

		if (index == -1) {
			if (config.getSpatialExtension() == SpatialExtension.POSTGIS) {
				return "public";
			} else {
				return null;
			}
		}

		return spatialTableName.substring(0, index);
	}

	private void logTotalInfo() {
		logInfo("Number of tiles to import: " + total);
	}

	private String getOracleToGemoetryClause(Geometry g) {
		// return "SDO_UTIL.FROM_WKTGEOMETRY('"+g.toText()+"')";
		String pattern = "sdo_geometry (2007, "
				+ srs
				+ ", null, sdo_elem_info_array (1,1003,1),"
				+ "sdo_ordinate_array ({0},{1}, {2},{3}, {4},{5}, {6},{7}, {8},{9}))";
		Envelope env = g.getEnvelopeInternal();
		NumberFormat f = NumberFormat.getNumberInstance(Locale.ENGLISH);
		f.setGroupingUsed(false);

		Object[] points = { f.format(env.getMinX()), f.format(env.getMinY()),
				f.format(env.getMaxX()), f.format(env.getMinY()),
				f.format(env.getMaxX()), f.format(env.getMaxY()),
				f.format(env.getMinX()), f.format(env.getMaxY()),
				f.format(env.getMinX()), f.format(env.getMinY()) };

		return MessageFormat.format(pattern, points);
	}
}
