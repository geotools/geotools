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

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for generating ddl scripts for a specific spatial extension.
 * The generated scripts should be used as a base for db setup. Manual
 * verification for performance issues is necessary.
 * 
 * 
 * 
 * @author mcr
 * 
 */
class DDLGenerator extends AbstractCmd {
	private final static int DefaultPyramids = 0;

	private final static String FN_CREATEMETA = "createmeta.sql";

	private final static String FN_DROPMETA = "dropmeta.sql";

	private final static String UsageInfo = "Generating DDL scripts\n"
			+ "-config URLOrFile -spatialTNPrefix spatialTNPrefix [-tileTNPrefix tileTNPrefix]\n"
			+ "  [-pyramids pyramids] -statementDelim statementDelim [-srs srs ] -targetDir directory";
	
	private final static String NotSuppord = "DDL generation not supported for type ";

	private Config config;

	private String spatialTNPrefix;

	private String tileTNPrefix;

	private int pyramids = DefaultPyramids;

	private String statementDelim;

	private Logger logger;

	private DBDialect dbDialect;

	private String srs;

	private String targetDir;

	/**
	 * Constructor
	 * 
	 * @param config
	 *            the config object
	 * @param spatialTNPrefix
	 *            the prefix for spatial table names
	 * @param tileTNPrefix
	 *            the prefix for tile table names
	 * @param pyramids
	 *            number of pyramids
	 * @param statementDelim
	 *            statement delimiter used in the scripts
	 * @param srs
	 *            name of spatial reference system
	 * @param targetDir
	 *            the target directory for the scripts
	 */
	DDLGenerator(Config config, String spatialTNPrefix, String tileTNPrefix,
			int pyramids, String statementDelim, String srs, String targetDir) {
		this.config = config;
		this.pyramids = pyramids;
		this.spatialTNPrefix = spatialTNPrefix;
		this.tileTNPrefix = tileTNPrefix;
		this.statementDelim = statementDelim;
		this.srs = srs;

		this.logger = Logger.getLogger(this.getClass().getName());
		this.dbDialect = DBDialect.getDBDialect(config);
		if (targetDir.endsWith(File.separator))
			this.targetDir = targetDir;
		else
			this.targetDir = targetDir + File.separator;
	}

	/**
	 * Entry point, called from Toolbox class
	 * 
	 * @param args
	 */
	public static void start(String[] args) {
		Config config = null;
		String spatialTNPrefix = null;
		String tileTNPrefix = null;
		String statementDelim = null;
		String srs = null;
		String targetDir = null;
		int pyramids = DefaultPyramids;

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
		                    System.out.println(NotSuppord+config.getSpatialExtension());
		                    System.exit(1);
		                }
				i++;
			} else if (args[i].equals(SPATIALTNPREFIXPARAM)) {
				spatialTNPrefix = args[i + 1];
				i++;
			} else if (args[i].equals(TILETNPREFIXPARAM)) {
				tileTNPrefix = args[i + 1];
				i++;
			} else if (args[i].equals("-statementDelim")) {
				statementDelim = args[i + 1];
				i++;
			} else if (args[i].equals("-srs")) {
				srs = args[i + 1];
				i++;

			} else if (args[i].equals("-pyramids")) {
				pyramids = new Integer(args[i + 1]);
				i++;
			} else if (args[i].equals("-targetDir")) {
				targetDir = args[i + 1];
				i++;
			} else {
				System.out.println("Unkwnown option: " + args[i]);
				System.exit(1);
			}
		}


		
		if ((config == null) || (spatialTNPrefix == null)
				|| (statementDelim == null)) {
			System.out.println(UsageInfo);
			System.exit(1);
		}

		if (targetDir == null) {
			System.out.println("Must specify -targetDir ");
			System.exit(1);
		}

		if (needsSpatialRegistry(config) && (srs == null)) {
			System.out.println("Must specify -srs ");
			System.exit(1);
		}

		DDLGenerator gen = new DDLGenerator(config, spatialTNPrefix,
				tileTNPrefix, pyramids, statementDelim, srs, targetDir);

		try {
			gen.generate();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		System.exit(0);
	}

	void generate() throws Exception {
		writeCreateMeta();
		writeDropMeta();

		String fn = targetDir + "add_" + config.getCoverageName() + ".sql";
		PrintWriter w = new PrintWriter(fn);
		w.println();
		writeFillMeta(w);
		w.println();
		writeCreateTables(w);
		w.println();
		writeRegister(w);
		w.println();
		writeCreateIndexes(w);

		w.close();
		if (logger.isLoggable(Level.INFO))
			logger.info(fn + " generated");

		fn = targetDir + "remove_" + config.getCoverageName() + ".sql";
		w = new PrintWriter(fn);
		w.println();
		writeDropIndexes(w);
		w.println();
		writeUnRegister(w);
		w.println();
		writeDropTables(w);
		w.println();
		writeDeleteMeta(w);
		w.close();
		if (logger.isLoggable(Level.INFO))
			logger.info(fn + " generated");

	}

	void writeFillMeta(PrintWriter w) throws IOException {

		String statmentString = "INSERT INTO " + config.getMasterTable() + "("
				+ config.getCoverageNameAttribute() + ","
				+ config.getTileTableNameAtribute() + ","
				+ config.getSpatialTableNameAtribute()
				+ ") VALUES ('%s','%s','%s')";

		for (int i = 0; i <= pyramids; i++) {
			String stn = getTableName(spatialTNPrefix, i);
			String ttn = (tileTNPrefix == null) ? stn : getTableName(
					tileTNPrefix, i);
			Object[] args = new Object[] { config.getCoverageName(), ttn, stn };
			w.printf(statmentString, args);
			w.println(statementDelim);
		}

	}

	void writeDeleteMeta(PrintWriter w) throws IOException {

		String statmentString = "DELETE FROM " + config.getMasterTable()
				+ " WHERE " + config.getCoverageNameAttribute()
				+ " = '%s' AND " + config.getTileTableNameAtribute()
				+ " = '%s' AND " + config.getSpatialTableNameAtribute()
				+ " = '%s'  ";

		for (int i = 0; i <= pyramids; i++) {
			String stn = getTableName(spatialTNPrefix, i);
			String ttn = (tileTNPrefix == null) ? stn : getTableName(
					tileTNPrefix, i);
			Object[] args = new Object[] { config.getCoverageName(), ttn, stn };
			w.printf(statmentString, args);
			w.println(statementDelim);
		}

	}

	void writeCreateMeta() throws Exception {
		PrintWriter w = new PrintWriter(targetDir + FN_CREATEMETA);
		w.print(dbDialect.getCreateMasterStatement());
		w.println(statementDelim);
		w.close();
		if (logger.isLoggable(Level.INFO))
			logger.info(FN_CREATEMETA + " generated");
	}

	void writeCreateTables(PrintWriter w) throws Exception {

		for (int i = 0; i <= pyramids; i++) {
			if (tileTNPrefix == null) {
				w.print(dbDialect
						.getCreateSpatialTableStatementJoined(getTableName(
								spatialTNPrefix, i)));
				w.println(statementDelim);
			} else {
				w.print(dbDialect.getCreateSpatialTableStatement(getTableName(
						spatialTNPrefix, i)));
				w.println(statementDelim);
				w.print(dbDialect.getCreateTileTableStatement(getTableName(
						tileTNPrefix, i)));
				w.println(statementDelim);
			}
		}

	}

	void writeCreateIndexes(PrintWriter w) throws Exception {

		for (int i = 0; i <= pyramids; i++) {
			w.print(dbDialect.getCreateIndexStatement(getTableName(
					spatialTNPrefix, i)));
			w.println(statementDelim);
		}

	}

	void writeDropMeta() throws IOException {
		PrintWriter w = new PrintWriter(targetDir + FN_DROPMETA);
		w.print(dbDialect.getDropTableStatement(config.getMasterTable()));
		w.println(statementDelim);
		w.close();
		if (logger.isLoggable(Level.INFO))
			logger.info(FN_DROPMETA + " generated");
	}

	void writeDropTables(PrintWriter w) throws IOException {

		for (int i = 0; i <= pyramids; i++) {
			w.print(dbDialect.getDropTableStatement(getTableName(
					spatialTNPrefix, i)));
			w.println(statementDelim);
		}

		if (tileTNPrefix != null) {
			w.println();

			for (int i = 0; i <= pyramids; i++) {
				w.print(dbDialect.getDropTableStatement(getTableName(
						tileTNPrefix, i)));
				w.println(statementDelim);
			}
		}

	}

	void writeDropIndexes(PrintWriter w) throws IOException {

		for (int i = 0; i <= pyramids; i++) {
			w.print(dbDialect.getDropIndexStatment(getTableName(
					spatialTNPrefix, i)));
			w.println(statementDelim);
		}

	}

	static boolean needsSpatialRegistry(Config config) {
		SpatialExtension type = config.getSpatialExtension();

		if ((type == SpatialExtension.DB2)
				|| (type == SpatialExtension.POSTGIS)
				|| (type == SpatialExtension.ORACLE)) {
			return true;
		}

		return false;
	}

	void writeRegister(PrintWriter w) throws IOException {
		if (needsSpatialRegistry(config) == false) {
			return;
		}

		for (int i = 0; i <= pyramids; i++) {
			w.print(dbDialect.getRegisterSpatialStatement(getTableName(
					spatialTNPrefix, i), srs));
			w.println(statementDelim);
		}

	}

	void writeUnRegister(PrintWriter w) throws IOException {
		if (needsSpatialRegistry(config) == false) {
			return;
		}

		for (int i = 0; i <= pyramids; i++) {
			w.print(dbDialect.getUnregisterSpatialStatement(getTableName(
					spatialTNPrefix, i)));
			w.println(statementDelim);
		}

	}
}
