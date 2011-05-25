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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.geotools.data.Parameter;

/**
 * Widget for double values
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 * @author gdavis
 *
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/unsupported/swt/src/main/java/org/geotools/swt/control/JDoubleField.java $
 */
public class JDoubleField extends ParamField {

    private Text text;

    public JDoubleField( Composite parent, Parameter< ? > parameter ) {
        super(parent, parameter);
    }

    public Control doLayout() {
        text = new Text(parent, SWT.SINGLE | SWT.LEAD | SWT.BORDER);
        text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        return text;
    }

    public Object getValue() {
        String val = text.getText();
        if (val == null || val.equals("")) {
            return new Double(0);
        }
        try {
            return new Double(val);
        } catch (NumberFormatException e) {
            return new Double(0);
        }
    }

    public void setValue( Object value ) {
        text.setText(((Double) value).toString());
    }

    public boolean validate() {
        String val = text.getText();
        try {
            Double d = Double.parseDouble(val);
            return d != null;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
