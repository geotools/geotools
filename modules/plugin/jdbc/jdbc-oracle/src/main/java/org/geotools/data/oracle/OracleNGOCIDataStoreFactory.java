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
package org.geotools.data.oracle;

import java.io.IOException;
import java.util.Map;

/**
 * Creates an Oracle datastore based on a thick OCI client connection, instead of the traditional
 * (thin) jdbc connection. The thin JDBC connection was designed for clients requiring no more
 * classes than just ojdbc14.jar. The OCI JDBC drivers are based on the Oracle client software and
 * rely mostly on the C/C++ based OCI (Oracle Call Interface) runtime.
 * <p>
 * Looking over the internet it's not clear whether the OCI setup is faster than thin driver,
 * different benchmarks report different results, but for sure OCI allows to expose a wider set of
 * configurations and in particular it's recognized as the best way to connect to an Oracle cluster.
 * <p>
 * Instead of the instance, host, port requirements of the normal oracle factory this driver just
 * requires the 'alias', which refers to values defined by the Oracle Net Configuration assistant
 * and stored in $ORACLE_HOME/NETWORK/ADMIN/tnsnames.ora. We have also had luck on the same computer
 * with just leaving 'alias' as an empty string, and it seems to have a reasonable default behavior.
 * 
 * @author Chris Holmes, TOPP
 * @author Bernard de Terwangne, star.be
 * @author Andrea Aime - OpenGeo
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/plugin/jdbc/jdbc-oracle/src/main/java/org/geotools/data/oracle/OracleNGOCIDataStoreFactory.java $
 *         http://svn.osgeo.org/geotools/branches/2.6.x/modules/unsupported/oracle-spatial/src/
 *         main/java/org/geotools/data/oracle/OracleOCIDataStoreFactory.java $
 */
public class OracleNGOCIDataStoreFactory extends OracleNGDataStoreFactory {
    /** The alias parameter used to specify the database to connect to */
    public static final Param ALIAS = new Param("alias", String.class,
            "The alias to the oracle server, as defined in the tnsnames.ora file", true);

    @Override
    public String getDisplayName() {
        return "Oracle NG (OCI)";
    }

    public String getDescription() {
        return "Oracle Database (OCI)";
    }

    @Override
    protected String getJDBCUrl(Map params) throws IOException {
        String alias = (String) ALIAS.lookUp(params);
        return "jdbc:oracle:oci:@" + alias;
    }

    @Override
    protected void setupParameters(Map parameters) {
        // a full override is needed to make sure we get a good looking param order
        
        parameters.put(DBTYPE.key, new Param(DBTYPE.key, DBTYPE.type, DBTYPE.description,
                DBTYPE.required, getDatabaseID()));
        parameters.put(SCHEMA.key, SCHEMA);
        
        parameters.put(ALIAS.key, ALIAS);
        
        parameters.put(USER.key, USER);
        parameters.put(PASSWD.key, PASSWD);
        parameters.put(NAMESPACE.key, NAMESPACE);
        parameters.put(MAXCONN.key, MAXCONN);
        parameters.put(MINCONN.key, MINCONN);
        parameters.put(FETCHSIZE.key, FETCHSIZE);
        parameters.put(MAXWAIT.key, MAXWAIT);
        if (getValidationQuery() != null)
            parameters.put(VALIDATECONN.key, VALIDATECONN);
        parameters.put(PK_METADATA_TABLE.key, PK_METADATA_TABLE);
        
        parameters.put(LOOSEBBOX.key, LOOSEBBOX);
        parameters.put(MAX_OPEN_PREPARED_STATEMENTS.key, MAX_OPEN_PREPARED_STATEMENTS);
    }
}
