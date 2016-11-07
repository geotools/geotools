/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geopkg;

import java.io.IOException;
import java.io.Writer;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

import org.geotools.jdbc.PreparedFilterToSQL;
import org.geotools.jdbc.PreparedStatementSQLDialect;
import org.opengis.filter.expression.Literal;

import com.vividsolutions.jts.geom.Geometry;

/**
 * @author ian
 *
 */
public class GeoPkgFilterToSQL extends PreparedFilterToSQL {



    /**
     * @param dialect
     */
    public GeoPkgFilterToSQL(PreparedStatementSQLDialect dialect) {
        super(dialect);
        // TODO Auto-generated constructor stub
    }


    /**
     * @param out
     */
    public GeoPkgFilterToSQL(Writer out) {
        super(out);
        // TODO Auto-generated constructor stub
    }

    
    @Override
    public Object visit(Literal expression, Object context) throws RuntimeException {
        if(!isPrepareEnabled())
            return super.visit(expression, context);
        
        // evaluate the literal and store it for later
        Object literalValue = evaluateLiteral( expression, (context instanceof Class ? (Class) context : null) );
        literalValues.add(literalValue);
        SRIDs.add(currentSRID);
        dimensions.add(currentDimension);
        
        Class clazz = null;
        if(context instanceof Class)
            clazz = (Class) context;
        else if(literalValue != null)
            clazz = literalValue.getClass();
        literalTypes.add( clazz );
        
        try {
            if ( literalValue == null || dialect == null ) {
                out.write( "?" );
            }
            else {
                StringBuffer sb = new StringBuffer();
                if (Geometry.class.isAssignableFrom(literalValue.getClass())) {
                    int srid = currentSRID != null ? currentSRID : -1;
                    int dimension = currentDimension != null ? currentDimension : -1;
                    dialect.prepareGeometryValue((Geometry) literalValue, dimension, srid,
                            Geometry.class, sb);
                } else if (Time.class.isAssignableFrom(literalValue.getClass())) {
                    sb.append("time(?,'localtime')");
                } else if (Timestamp.class.isAssignableFrom(literalValue.getClass())) {
                    sb.append("datetime(?,'localtime')");
                } else if (java.sql.Date.class.isAssignableFrom(literalValue.getClass())) {
                    sb.append("date(?,'localtime')");
                } else if (encodingFunction) {
                    dialect.prepareFunctionArgument(clazz, sb);
                } else {
                    sb.append("?");
                }
                out.write(sb.toString());
            }
        } 
        catch (IOException e) {
            throw new RuntimeException( e );
        }
        
        return context;
    
    }

    
    
}
