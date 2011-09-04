/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.postgis;

import org.geotools.jdbc.JDBCUDTTestSetup;

public class PostgisUDTTestSetup extends JDBCUDTTestSetup {

    public PostgisUDTTestSetup() {
        this(new PostGISTestSetup());
    }
    
    public PostgisUDTTestSetup(PostGISTestSetup setup) {
        super(setup);
    }

    @Override
    protected void createUdtTable() throws Exception {
        run("CREATE DOMAIN foo AS text CHECK (VALUE ~ '\\\\d{2}\\\\D{2}');");
        run("CREATE DOMAIN foo2 AS integer CONSTRAINT posint_check CHECK ((VALUE >= 0));");
        run("CREATE DOMAIN foo3 AS real CONSTRAINT posreal_check CHECK ((VALUE >= (0)::real));");
        run("CREATE DOMAIN foo4 AS bigint CONSTRAINT posbigint_check CHECK ((VALUE >= (0)::bigint));");
        run("CREATE DOMAIN foo5 AS boolean CONSTRAINT weirdboolean_check CHECK ((VALUE));");
        run("CREATE DOMAIN foo6 AS smallint CONSTRAINT posmallint_check CHECK ((VALUE >= (0)::smallint));");
        run("CREATE DOMAIN foo7 AS float4 CONSTRAINT posfloat4_check CHECK ((VALUE >= (0)::float4));");
        run("CREATE DOMAIN foo8 AS int4 CONSTRAINT posint4_check CHECK ((VALUE >= (0)::int4));");
        run("CREATE DOMAIN foo9 AS time CONSTRAINT customtime_check CHECK ((true));");
        run("CREATE DOMAIN foo10 AS timetz CONSTRAINT customtimetz_check CHECK ((true));");
        run("CREATE DOMAIN foo11 AS timestamp CONSTRAINT customtimestamp_check CHECK ((true));");
        run("CREATE DOMAIN foo12 AS timestamptz CONSTRAINT customtimestamptz_check CHECK ((true));");
        run("CREATE TABLE \"udt\" (id integer PRIMARY KEY, ut foo, ut2 foo2, ut3 foo3, ut4 foo4, ut5 foo5, ut6 foo6, ut7 foo7, ut8 foo8, ut9 foo9, ut10 foo10, ut11 foo11, ut12 foo12);");
        run("INSERT INTO \"udt\" VALUES (0, '12ab', 6, 6.6, 85748957, true, 3, 3.3, 2, '14:30'::time, '15:30'::time, '2004-10-31 16:30'::timestamp, '2004-10-30 17:30'::timestamp);");

    }

    @Override
    protected void dropUdtTable() throws Exception {
        runSafe("DROP TABLE \"udt\"");
        runSafe("DROP DOMAIN foo");
        runSafe("DROP DOMAIN foo2");
        runSafe("DROP DOMAIN foo3");
        runSafe("DROP DOMAIN foo4");
        runSafe("DROP DOMAIN foo5");
        runSafe("DROP DOMAIN foo6");
        runSafe("DROP DOMAIN foo7");
        runSafe("DROP DOMAIN foo8");
        runSafe("DROP DOMAIN foo9");
        runSafe("DROP DOMAIN foo10");
        runSafe("DROP DOMAIN foo11");
        runSafe("DROP DOMAIN foo12");
    }

}
