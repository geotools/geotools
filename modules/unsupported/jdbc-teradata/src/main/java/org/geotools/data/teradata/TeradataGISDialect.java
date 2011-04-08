/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.teradata;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.geotools.data.jdbc.FilterToSQL;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.jdbc.BasicSQLDialect;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.referencing.FactoryException;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class TeradataGISDialect extends BasicSQLDialect {
	private boolean mLooseBBOXEnabled;
	private String mLastSchemaName; 
	private String mLastTableName; 
	private Set<String> mIndexTables;
	
	// used for tessellate index
	private String key = "fid";
	private double u_xmin = -180;
	private double u_ymin = -90;
	private double u_xmax = 180;
	private double u_ymax = 90;
	private int g_nx = 1000;
	private int g_ny = 1000;
	private int levels = 1;
	private double scale = 0.01;
	private int shift = 0;
	

	protected TeradataGISDialect(JDBCDataStore dataStore) {
		super(dataStore);
	}

	public void setLooseBBOXEnabled(boolean looseBBOXEnabled) {
		this.mLooseBBOXEnabled = looseBBOXEnabled;
	}

	final static Map<String, Class<?>> TYPE_TO_CLASS = new HashMap<String, Class<?>>() {
		{
			put("GEOMETRY", Geometry.class);
			put("POINT", Point.class);
			put("LINESTRING", LineString.class);
			put("POLYGON", Polygon.class);

			put("MULTIPOINT", MultiPoint.class);
			put("MULTILINESTRING", MultiLineString.class);
			put("MULTIPOLYGON", MultiPolygon.class);
			put("GEOMETRYCOLLECTION", GeometryCollection.class);
			put("GEOSEQUENCE", Geometry.class);
		}
	};

	final static Map<Class<?>, String> CLASS_TO_TYPE = new HashMap<Class<?>, String>() {
		{
			put(Geometry.class, "GEOMETRY");
			put(Point.class, "POINT");
			put(LineString.class, "LINESTRING");
			put(Polygon.class, "POLYGON");
			put(MultiPoint.class, "MULTIPOINT");
			put(MultiLineString.class, "MULTILINESTRING");
			put(MultiPolygon.class, "MULTIPOLYGON");
			put(GeometryCollection.class, "GEOMETRYCOLLECTION");
		}
	};

	@Override
	public boolean includeTable(String schemaName, String tableName,
			Connection cx) throws SQLException {
		if (tableName.equalsIgnoreCase("geometry_columns")) {
			return false;
		} else if (tableName.toLowerCase().startsWith("spatial_ref_sys")) {
			return false;
		} else if (tableName.equalsIgnoreCase("geography_columns")) {
			return false;
		}

		// others?
		return dataStore.getDatabaseSchema() == null
				|| dataStore.getDatabaseSchema().equals(schemaName);
	}

	ThreadLocal<WKBAttributeIO> wkbReader = new ThreadLocal<WKBAttributeIO>();

	// WKBAttributeIO reader;
	@Override
	public Geometry decodeGeometryValue(GeometryDescriptor descriptor,
			ResultSet rs, String column, GeometryFactory factory, Connection cx)
			throws IOException, SQLException {
		WKBAttributeIO reader = getWkbReader(factory);

		Geometry g = (Geometry) reader.read(rs, column);
		return g;
	}

	private WKBAttributeIO getWkbReader(GeometryFactory factory) {
		WKBAttributeIO reader = wkbReader.get();
		if (reader == null) {
			GeometryFactory geometryFactory = factory == null ? dataStore
					.getGeometryFactory() : factory;
			reader = new WKBAttributeIO(geometryFactory);
			wkbReader.set(reader);
		}
		return reader;
	}

	@Override
	public void encodeGeometryColumn(GeometryDescriptor gatt, int srid,
			StringBuffer sql) {
		encodeColumnName(gatt.getLocalName(), sql);
		sql.append(".ST_AsBinary()");
	}

	@Override
	public void encodeGeometryEnvelope(String tableName, String geometryColumn,
			StringBuffer sql) {
		encodeColumnName(geometryColumn, sql);
		sql.append(".ST_Envelope().ST_AsBinary()");
	}

	@Override
	public List<ReferencedEnvelope> getOptimizedBounds(String schema,
			SimpleFeatureType featureType, Connection cx) throws SQLException,
			IOException {
		return null;
		/*
		 * String tableName = featureType.getTypeName();
		 * 
		 * Statement st = null; ResultSet rs = null;
		 * 
		 * List<ReferencedEnvelope> result = new
		 * ArrayList<ReferencedEnvelope>(); try { st = cx.createStatement();
		 * 
		 * for (AttributeDescriptor att : featureType.getAttributeDescriptors())
		 * { if (att instanceof GeometryDescriptor) { // use estimated extent
		 * (optimizer statistics) StringBuffer sql = new StringBuffer();
		 * sql.append("select SYSSPATIAL.AggGeomUnion(");
		 * encodeColumnName(att.getName().getLocalPart(), sql);
		 * sql.append(".ST_Envelope()) from "); if(schema != null) {
		 * encodeColumnName(tableName, sql); sql.append("."); }
		 * encodeColumnName(tableName, sql); rs =
		 * st.executeQuery(sql.toString());
		 * 
		 * if (rs.next()) { // decode the geometry Envelope env =
		 * decodeGeometryEnvelope(rs, 1, cx);
		 * 
		 * // reproject and merge if (!env.isNull()) { CoordinateReferenceSystem
		 * crs = ((GeometryDescriptor) att) .getCoordinateReferenceSystem();
		 * result.add(new ReferencedEnvelope(env, crs)); } } rs.close(); } } }
		 * catch(SQLException e) { LOGGER.log(Level.WARNING,
		 * "Failed to use ST_Estimated_Extent, falling back on envelope aggregation"
		 * , e); return null; } finally { dataStore.closeSafe(rs);
		 * dataStore.closeSafe(st); } return result;
		 */
	}

	@Override
	public Envelope decodeGeometryEnvelope(ResultSet rs, int column,
			Connection cx) throws SQLException, IOException {
		Geometry envelope = getWkbReader(null).read(rs, column);
		if (envelope != null) {
			return envelope.getEnvelopeInternal();
		} else {
			// empty one
			return new Envelope();
		}
		/*
		 * try {
		 * 
		 * if (envelope != null) return new
		 * WKTReader().read(envelope).getEnvelopeInternal(); else // empty one
		 * return new Envelope(); } catch (ParseException e) { throw
		 * (IOException) new IOException(
		 * "Error occurred parsing the bounds WKT").initCause(e); }
		 */
		/*
		 * byte[] bytes = rs.getBytes(column); if (bytes != null && bytes.length
		 * > 0) { WKBAttributeIO reader = getWkbReader(null); return
		 * reader.wkb2Geometry(bytes).getEnvelopeInternal(); } else // empty one
		 * return new Envelope();
		 */
	}

	@Override
	public Class<?> getMapping(ResultSet columnMetaData, Connection cx)
			throws SQLException {
		String typeName = columnMetaData.getString("TYPE_NAME");
		String gType = null;
		if ("SYSUDTLIB.ST_GEOMETRY".equalsIgnoreCase(typeName)) {
			gType = lookupGeometryType(columnMetaData, cx,
					"SYSSPATIAL.GEOMETRY_COLUMNS", "F_GEOMETRY_COLUMN");
		} else if ("SYSUDTLIB.ST_GEOGRAPHY".equalsIgnoreCase(typeName)) {
			gType = lookupGeometryType(columnMetaData, cx,
					"SYSSPATIAL.GEOGRAPHY_COLUMNS", "G_GEOGRAPHY_COLUMN");
		} else {
			return null;
		}

		// decode the type into
		if (gType == null) {
			// it's either a generic geography or geometry not registered in the
			// medatata tables
			return Geometry.class;
		} else {
			Class<?> geometryClass = TYPE_TO_CLASS.get(gType.toUpperCase());
			if (geometryClass == null) {
				geometryClass = Geometry.class;
			}

			return geometryClass;
		}
	}

	String lookupGeometryType(ResultSet columnMetaData, Connection cx,
			String gTableName, String gColumnName) throws SQLException {

		// grab the information we need to proceed
		String tableName = columnMetaData.getString("TABLE_NAME");
		String columnName = columnMetaData.getString("COLUMN_NAME");
		String schemaName = columnMetaData.getString("TABLE_SCHEM");

		// first attempt, try with the geometry metadata
		Statement statement = null;
		ResultSet result = null;

		try {
			String sqlStatement = "SELECT \"GEOM_TYPE\" FROM " + gTableName
					+ " WHERE " + "F_TABLE_SCHEMA = '" + schemaName + "' "
					+ "AND F_TABLE_NAME = '" + tableName + "' " + "AND "
					+ gColumnName + " = '" + columnName + "'";

			LOGGER.log(Level.FINE, "Geometry type check; {0} ", sqlStatement);
			statement = cx.createStatement();
			result = statement.executeQuery(sqlStatement);

			if (result.next()) {
				return result.getString(1);
			}
		} finally {
			dataStore.closeSafe(result);
			dataStore.closeSafe(statement);
		}

		return null;
	}

	@Override
	public Integer getGeometrySRID(String schemaName, String tableName,
			String columnName, Connection cx) throws SQLException {

		// first attempt, try with the geometry metadata
		Statement statement = null;
		ResultSet result = null;
		Integer srid = null;
		try {
			if (schemaName == null)
				schemaName = "public";

			String sqlStatement = "SELECT ref.AUTH_SRID FROM SYSSPATIAL.GEOMETRY_COLUMNS as col, SYSSPATIAL.spatial_ref_sys as ref WHERE "
					+ "col.F_TABLE_SCHEMA = '"
					+ schemaName
					+ "' "
					+ "AND col.F_TABLE_NAME = '"
					+ tableName
					+ "' "
					+ "AND col.F_GEOMETRY_COLUMN = '"
					+ columnName
					+ "' "
					+ "AND col.SRID = ref.SRID";

			LOGGER.log(Level.FINE, "Geometry srid check; {0} ", sqlStatement);
			statement = cx.createStatement();
			result = statement.executeQuery(sqlStatement);

			if (result.next()) {
				srid = result.getInt(1);
			} else {
				dataStore.closeSafe(result);
				sqlStatement = "SELECT ref.AUTH_SRID FROM SYSSPATIAL.GEOMETRY_COLUMNS as col, SYSSPATIAL.spatial_ref_sys as ref WHERE "
						+ "col.F_TABLE_NAME = '"
						+ tableName
						+ "' "
						+ "AND col.F_GEOMETRY_COLUMN = '"
						+ columnName
						+ "' "
						+ "AND col.SRID = ref.SRID";
				result = statement.executeQuery(sqlStatement);

				if (result.next()) {
					srid = result.getInt(1);
				}
			}
		} finally {
			dataStore.closeSafe(result);
			dataStore.closeSafe(statement);
		}

		return srid;
	}

	@Override
	public void registerClassToSqlMappings(Map<Class<?>, Integer> mappings) {
		super.registerClassToSqlMappings(mappings);

		// jdbc metadata for geom columns reports DATA_TYPE=1111=Types.OTHER
		mappings.put(Geometry.class, Types.OTHER);
	}

	@Override
	public void registerSqlTypeNameToClassMappings(
			Map<String, Class<?>> mappings) {
		super.registerSqlTypeNameToClassMappings(mappings);

		mappings.put("ST_Geometry", Geometry.class);
	}

	@Override
	public void registerSqlTypeToSqlTypeNameOverrides(
			Map<Integer, String> overrides) {
		overrides.put(Types.VARCHAR, "VARCHAR");
	}

	@Override
	public String getGeometryTypeName(Integer type) {
		return "ST_Geometry";
	}

	@Override
	public void encodePrimaryKey(String column, StringBuffer sql) {
		encodeColumnName(column, sql);
		sql.append(" PRIMARY KEY not null generated always as identity (start with 0) integer");
	}

	@Override
	public void encodeColumnType(String sqlTypeName, StringBuffer sql) {
		if (sqlTypeName.toUpperCase().startsWith("VARCHAR")) {
			sql.append(sqlTypeName);
			sql.append("casespecific");
		} else {
			sql.append(sqlTypeName);
		}
	}

	/**
	 * Creates GEOMETRY_COLUMN registrations and spatial indexes for all
	 * geometry columns
	 */
	@Override
	public void postCreateTable(String schemaName,
			SimpleFeatureType featureType, Connection cx) throws SQLException {
		schemaName = schemaName != null ? schemaName : "";
		String tableName = featureType.getName().getLocalPart();
		Statement st = null;
		try {
			st = cx.createStatement();

			// register all geometry columns in the database
			for (AttributeDescriptor att : featureType
					.getAttributeDescriptors()) {
				if (att instanceof GeometryDescriptor) {
					GeometryDescriptor gd = (GeometryDescriptor) att;
					int srid = -1;
					try {
						Integer result = CRS.lookupEpsgCode(gd
								.getCoordinateReferenceSystem(), true);
						if (result != null) {
							String sql = "select srid from SYSSPATIAL.spatial_ref_sys"
									+ " where AUTH_SRID = " + result;
							LOGGER.fine(sql);
							ResultSet resultSet = st.executeQuery(sql);
							resultSet.next();
							srid = resultSet.getInt("srid");
						}
					} catch (FactoryException e) {
						e.printStackTrace();
					}

					// grab the geometry type
					String geomType = CLASS_TO_TYPE.get(gd.getType()
							.getBinding());
					if (geomType == null)
						geomType = "GEOMETRY";

					String sql = MessageFormat.format("INSERT INTO SYSSPATIAL.GEOMETRY_COLUMNS "
							+ "(F_TABLE_CATALOG, F_TABLE_SCHEMA, F_TABLE_NAME, F_GEOMETRY_COLUMN, COORD_DIMENSION, SRID, GEOM_TYPE)" 
							+ " VALUES ('''', ''{0}'', ''{1}'', ''{2}'', 2, {3,number,0}, ''{4}'')",
							schemaName, tableName, gd.getLocalName(), srid, geomType);
					LOGGER.fine(sql);
					st.execute(sql);

					
					
		    		StringBuffer sb = new StringBuffer();
		    		if (schemaName != null) {
			    		encodeSchemaName(schemaName, sb);
			    		sb.append(".");
		    		}
		    		encodeTableName(tableName, sb);
		    		String encodedTableName = sb.toString();
		    		
		    		sb = new StringBuffer();
		    		if (schemaName != null) {
			    		encodeSchemaName(schemaName, sb);
			    		sb.append(".");
		    		}
		    		
		    		String indexTableName = tableName + "_" + gd.getLocalName() + "_idx"; 
		    		encodeTableName(indexTableName, sb);
		    		String encodedIdxTableName = sb.toString();
		    		
		    		
					// DROP IF EXISTS workaround
					try {
						sql = "DROP TABLE " + encodedIdxTableName;
						LOGGER.fine(sql);
						st.execute(sql);
					}
					catch (Exception e) {
						LOGGER.fine(e.getMessage());
					}
					
					// add the spatial index
					sql = "CREATE MULTISET TABLE " + encodedIdxTableName + 
							" (id INTEGER NOT NULL, cellid INTEGER NOT NULL) PRIMARY INDEX (cellid)";
					LOGGER.fine(sql);
					st.execute(sql);
					mIndexTables.add(indexTableName);

					sql = MessageFormat.format("CREATE TRIGGER \"{0}_{1}_mi\" AFTER INSERT ON {12}"
							+ "  REFERENCING NEW TABLE AS nt"
							+ "  FOR EACH STATEMENT"
							+ "  BEGIN ATOMIC"
							+ "  ("
							+ "    INSERT INTO {12} SELECT \"{2}\","
							+ "    sysspatial.tessellate_index("
							+ "      \"{1}\".ST_Envelope().ST_ExteriorRing().ST_PointN(1).ST_X(), "
							+ "      \"{1}\".ST_Envelope().ST_ExteriorRing().ST_PointN(1).ST_Y(), "
							+ "      \"{1}\".ST_Envelope().ST_ExteriorRing().ST_PointN(3).ST_X(), "
							+ "      \"{1}\".ST_Envelope().ST_ExteriorRing().ST_PointN(3).ST_Y(), "
							+ "      {3,number,0.0#}, {4,number,0.0#}, {5,number,0.0#}, {6,number,0.0#}, "
							+ "      {7,number,0}, {8,number,0}, {9,number,0}, {10,number,0.0#}, {11,number,0})"
							+ "    FROM nt;"
							+ "  ) "
							+ "END", tableName, gd.getLocalName(), key, u_xmin, u_ymin, u_xmax, u_ymax, g_nx, g_ny, levels, scale, shift,
							encodedTableName);
//					sql = MessageFormat.format("CREATE TRIGGER \"{0}_{1}_mi\" AFTER INSERT ON {12}"
//							+ "  REFERENCING NEW AS nt"
//							+ "  FOR EACH ROW"
//							+ "  BEGIN ATOMIC"
//							+ "  ("
//							+ "    INSERT INTO {12}"
//							+ "    VALUES (nt.\"{2}\","
//							+ "    sysspatial.tessellate_index("
//							+ "      nt.\"{1}\".ST_Envelope().ST_ExteriorRing().ST_PointN(1).ST_X(), "
//							+ "      nt.\"{1}\".ST_Envelope().ST_ExteriorRing().ST_PointN(1).ST_Y(), "
//							+ "      nt.\"{1}\".ST_Envelope().ST_ExteriorRing().ST_PointN(3).ST_X(), "
//							+ "      nt.\"{1}\".ST_Envelope().ST_ExteriorRing().ST_PointN(3).ST_Y(), "
//							+ "      {3,number,0.0#}, {4,number,0.0#}, {5,number,0.0#}, {6,number,0.0#}, "
//							+ "      {7,number,0}, {8,number,0}, {9,number,0}, {10,number,0.0#}, {11,number,0})"
//							+ "  );) "
//							+ "END", tableName, gd.getLocalName(), key, u_xmin, u_ymin, u_xmax, u_ymax, g_nx, g_ny, levels, scale, shift,
//							encodedTableName);
					LOGGER.fine(sql);
					st.execute(sql);

					sql = MessageFormat.format("CREATE TRIGGER \"{0}_{1}_mu\" AFTER UPDATE OF \"{1}\" ON {12}"
							+ "  REFERENCING NEW AS nt"
							+ "  FOR EACH STATEMENT"
							+ "  BEGIN ATOMIC"
							+ "  ("
							+ "    UPDATE {12} SET "
							+ "    cellid=sysspatial.tessellate_index("
							+ "      nt.\"{1}\".ST_Envelope().ST_ExteriorRing().ST_PointN(1).ST_X(), "
							+ "      nt.\"{1}\".ST_Envelope().ST_ExteriorRing().ST_PointN(1).ST_Y(), "
							+ "      nt.\"{1}\".ST_Envelope().ST_ExteriorRing().ST_PointN(3).ST_X(), "
							+ "      nt.\"{1}\".ST_Envelope().ST_ExteriorRing().ST_PointN(3).ST_Y(), "
							+ "      {3,number,0.0#}, {4,number,0.0#}, {5,number,0.0#}, {6,number,0.0#}, "
							+ "      {7,number,0}, {8,number,0}, {9,number,0}, {10,number,0.0#}, {11,number,0})"
							+ "    WHERE id=nt.\"{2}\";"
							+ "  ) "
							+ "END", tableName, gd.getLocalName(), key, u_xmin, u_ymin, u_xmax, u_ymax, g_nx, g_ny, levels, scale, shift,
							encodedTableName);
					
					sql = MessageFormat.format("CREATE TRIGGER \"{0}_{1}_md\" AFTER DELETE ON {2}"
							+ "  REFERENCING OLD TABLE AS ot"
							+ "  FOR EACH STATEMENT"
							+ "  BEGIN ATOMIC"
							+ "  ("
							+ "    DELETE FROM \"{0}_{1}_idx\" WHERE ID IN (SELECT \"{1}\" from ot);"
							+ "  )"
							+ "END", tableName, gd.getLocalName(), encodedTableName);
					LOGGER.fine(sql);
					st.execute(sql);
				}
				cx.commit();
			}
		} finally {
			dataStore.closeSafe(st);
		}
	}

	@Override
	public void encodeGeometryValue(Geometry value, int srid, StringBuffer sql)
			throws IOException {
		if (value == null) {
			sql.append("NULL");
		} else {
			sql.append("'" + value.toText() + "'");
		}
	}

	@Override
	public void encodeValue(Object value, Class type, StringBuffer sql) {
		if (byte[].class.equals(type)) {
			// escape the into bytea representation
			StringBuffer sb = new StringBuffer();
			byte[] input = (byte[]) value;
			for (int i = 0; i < input.length; i++) {
				byte b = input[i];
				if (b == 0) {
					sb.append("\\\\000");
				} else if (b == 39) {
					sb.append("\\'");
				} else if (b == 92) {
					sb.append("\\\\134'");
				} else if (b < 31 || b >= 127) {
					sb.append("\\\\");
					String octal = Integer.toOctalString(b);
					if (octal.length() == 1) {
						sb.append("00");
					} else if (octal.length() == 2) {
						sb.append("0");
					}
					sb.append(octal);
				} else {
					sb.append((char) b);
				}
			}
			super.encodeValue(sb.toString(), String.class, sql);
		} else {
			super.encodeValue(value, type, sql);
		}
	}

	@Override
	public FilterToSQL createFilterToSQL() {
		TeradataFilterToSQL sql = new TeradataFilterToSQL(this);
        sql.setLooseBBOXEnabled(mLooseBBOXEnabled);
		return sql;
	}

	@Override
	public boolean isLimitOffsetSupported() {
		return false;
	}

	@Override
	public Object getLastAutoGeneratedValue(String schemaName,
			String tableName, String columnName, Connection cx)
			throws SQLException {
		Statement stmt = cx.createStatement();
		try {
			StringBuffer sql = new StringBuffer("SELECT TOP 1 ");
			encodeColumnName(columnName, sql);
			sql.append(" FROM ");
			if (schemaName != null) {
				encodeColumnName(schemaName, sql);
				sql.append(".");
			}
			encodeColumnName(tableName, sql);
			sql.append(" ORDER BY ");
			encodeColumnName(columnName, sql);
			sql.append(" DESC");

			ResultSet resultSet = stmt.executeQuery(sql.toString());
			if (resultSet.next()) {
				return resultSet.getInt(1);
			} else {
				throw new IllegalStateException(
						"Unable to determine next value in autogenerated sequence.  SQL was: "
								+ sql);
			}
		} finally {
			stmt.close();
		}
	}

	@Override
	public boolean lookupGeneratedValuesPostInsert() {
		return true;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String pKey) {
		key = pKey;
	}

	public double getU_xmin() {
		return u_xmin;
	}

	public void setU_xmin(double uXmin) {
		u_xmin = uXmin;
	}

	public double getU_ymin() {
		return u_ymin;
	}

	public void setU_ymin(double uYmin) {
		u_ymin = uYmin;
	}

	public double getU_xmax() {
		return u_xmax;
	}

	public void setU_xmax(double uXmax) {
		u_xmax = uXmax;
	}

	public double getU_ymax() {
		return u_ymax;
	}

	public void setU_ymax(double uYmax) {
		u_ymax = uYmax;
	}

	public int getG_nx() {
		return g_nx;
	}

	public void setG_nx(int gNx) {
		g_nx = gNx;
	}

	public int getG_ny() {
		return g_ny;
	}

	public void setG_ny(int gNy) {
		g_ny = gNy;
	}

	public int getLevels() {
		return levels;
	}

	public void setLevels(int levels) {
		this.levels = levels;
	}

	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	public int getShift() {
		return shift;
	}

	public void setShift(int shift) {
		this.shift = shift;
	}
	
	public String getLastTableName() {
		return mLastTableName;
	}

	public String getLastSchemaName() {
		return mLastSchemaName;
	}
	
	@Override
	public void encodeSchemaName(String raw, StringBuffer sql) {
		super.encodeSchemaName(raw, sql);
		mLastSchemaName = raw;
	}
	
	@Override
	public void encodeTableName(String raw, StringBuffer sql) {
		super.encodeTableName(raw, sql);
		mLastTableName = raw;
	}

	public boolean indexTableExists(String table) {
		return mIndexTables.contains(table);
	}
	
    public void initializeConnection(Connection cx) throws SQLException {
    	if (mIndexTables == null) {
    		mIndexTables = new HashSet<String>();
    		ResultSet tables = cx.getMetaData().getTables("", "", "%_idx", new String[] {"TABLE"});
        	while (tables.next()) {
        		mIndexTables.add(tables.getString(3));
        	}
    	}
    }
}
