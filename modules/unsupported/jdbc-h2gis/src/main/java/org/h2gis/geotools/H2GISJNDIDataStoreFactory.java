/*
 * h2gis-geotools is an extension to the geotools library to connect H2GIS a
 * spatial library that brings spatial support to the H2 Java database. *
 *
 * Copyright (C) 2017 LAB-STICC CNRS UMR 6285
 *
 * h2gis-geotools is free software;
 * you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation;
 * version 3.0 of the License.
 *
 * h2gis-geotools is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details <http://www.gnu.org/licenses/>.
 *
 *
 * For more information, please consult: <http://www.h2gis.org/>
 * or contact directly: info_at_h2gis.org
 */
package org.h2gis.geotools;

import static org.h2gis.geotools.H2GISDataStoreFactory.ASSOCIATIONS;
import static org.h2gis.geotools.H2GISDataStoreFactory.AUTO_SERVER;
import static org.h2gis.geotools.H2GISDataStoreFactory.ENCODE_FUNCTIONS;
import static org.h2gis.geotools.H2GISDataStoreFactory.ESTIMATED_EXTENTS;
import static org.h2gis.geotools.H2GISDataStoreFactory.PREPARED_STATEMENTS;

import java.util.Map;
import org.geotools.jdbc.JDBCJNDIDataStoreFactory;

/** @author Erwan Bocher, CNRS, 2020 */
public class H2GISJNDIDataStoreFactory extends JDBCJNDIDataStoreFactory {

    public H2GISJNDIDataStoreFactory() {
        super(new H2GISDataStoreFactory());
    }

    @Override
    protected void setupParameters(Map<String, Object> parameters) {
        super.setupParameters(parameters);
        parameters.put(ASSOCIATIONS.key, ASSOCIATIONS);
        parameters.put(AUTO_SERVER.key, AUTO_SERVER);
        parameters.put(ESTIMATED_EXTENTS.key, ESTIMATED_EXTENTS);
        parameters.put(PREPARED_STATEMENTS.key, PREPARED_STATEMENTS);
        parameters.put(ENCODE_FUNCTIONS.key, ENCODE_FUNCTIONS);
    }
}
