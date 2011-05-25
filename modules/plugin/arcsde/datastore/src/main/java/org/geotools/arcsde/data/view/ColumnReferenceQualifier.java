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
 *
 */
package org.geotools.arcsde.data.view;

import java.util.Map;

import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.ColumnIndex;
import net.sf.jsqlparser.statement.select.ColumnReference;
import net.sf.jsqlparser.statement.select.ColumnReferenceVisitor;

import org.geotools.arcsde.session.ISession;

/**
 * Qualifies a column reference (aliased) the ArcSDE "table.user." prefix as required by the ArcSDE
 * java api to not get confused when using joined tables.
 * 
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 *
 * @source $URL$
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/arcsde/datastore/src/main/java
 *         /org/geotools/arcsde/data/view/ColumnReferenceQualifier.java $
 * @since 2.3.x
 */
public class ColumnReferenceQualifier implements ColumnReferenceVisitor {
    private ColumnReference qualifiedReference;

    private ISession session;

    private Map<String, Object> tableAliases;

    /**
     * Creates a new ColumnReferenceQualifier object.
     * 
     * @param session
     */
    private ColumnReferenceQualifier(ISession session, Map<String, Object> tableAliases) {
        this.session = session;
        this.tableAliases = tableAliases;
    }

    public static ColumnReference qualify(ISession session, Map<String, Object> tableAliases,
            ColumnReference colRef) {
        if (colRef == null) {
            return null;
        }

        ColumnReferenceQualifier qualifier = new ColumnReferenceQualifier(session, tableAliases);
        colRef.accept(qualifier);

        return qualifier.qualifiedReference;
    }

    public void visit(ColumnIndex columnIndex) {
        qualifiedReference = columnIndex;
    }

    public void visit(Column column) {
        this.qualifiedReference = ColumnQualifier.qualify(session, tableAliases, column);
    }
}
