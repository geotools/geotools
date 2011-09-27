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
package org.geotools.swt.control;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.geotools.data.Parameter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;

/**
 * Text field for filling in a Geometry parameter using WKT.
 *
 *
 *
 * @source $URL$
 */
public class JGeometryField extends ParamField {
    private Text text;

    public JGeometryField( Composite parent, Parameter< ? > parameter ) {
        super(parent, parameter);
    }

    public Control doLayout() {
        text = new Text(parent, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
        text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        text.addModifyListener(new ModifyListener(){
            public void modifyText( ModifyEvent arg0 ) {
                validate();
            }
        });
        return text;
    }

    public Object getValue() {
        WKTReader reader = new WKTReader();
        String wkt = text.getText();
        if (wkt.length() == 0) {
            return null;
        }

        try {
            return reader.read(wkt);
        } catch (Throwable eek) {
            return null;
        }
    }

    /**
     * Determine the number of dimensions based on the CRS metadata.
     * 
     * @return Number of dimensions expected based on metadata, default of 2
     */
    int getD() {
        try {
            CoordinateReferenceSystem crs = (CoordinateReferenceSystem) parameter.metadata.get(Parameter.CRS);
            if (crs == null) {
                return 2;
            } else {
                return crs.getCoordinateSystem().getDimension();
            }
        } catch (Throwable t) {
            return 2;
        }
    }

    public void setValue( Object value ) {
        Geometry geom = (Geometry) value;

        WKTWriter writer = new WKTWriter(getD());
        String wkt = writer.write(geom);

        text.setText(wkt);
    }

    public boolean validate() {
        WKTReader reader = new WKTReader();
        String wkt = text.getText();
        if (wkt.length() == 0) {
            return true;
        }

        try {
            Geometry geom = reader.read(wkt);
            if (parameter.type.isInstance(geom)) {
                text.setToolTipText(null);
                text.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
                return true;
            } else {
                text.setToolTipText("Could not use " + geom.getClass() + " as " + parameter.type);
                text.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
                return false;
            }
        } catch (Throwable eek) {
            text.setToolTipText(eek.getLocalizedMessage());
            text.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
            return false;
        }
    }

}
