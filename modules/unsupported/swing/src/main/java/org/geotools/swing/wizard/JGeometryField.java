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
package org.geotools.swing.wizard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.geotools.data.Parameter;
import org.geotools.swing.wizard.JWizard.Controller;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;

/**
 * Text field for filling in a Geometry parameter using WKT.
 *
 * @source $URL$
 */
public class JGeometryField extends ParamField {
    private JTextArea text;

    public JGeometryField(Parameter<?> parameter) {
        super(parameter);
    }

    public JComponent doLayout() {
        text = new JTextArea(40, 3);
        text.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                validate();
            }
        });
        text.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(text, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setPreferredSize(new Dimension(400, 80));
        return scroll;
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
            CoordinateReferenceSystem crs = (CoordinateReferenceSystem) parameter.metadata
                    .get(Parameter.CRS);
            if (crs == null) {
                return 2;
            } else {
                return crs.getCoordinateSystem().getDimension();
            }
        } catch (Throwable t){
            return 2;
        }
    }

    public void setValue(Object value) {
        Geometry geom = (Geometry) value;

        WKTWriter writer = new WKTWriter(getD());
        String wkt = writer.write(geom);

        text.setText(wkt);
    }

    public void addListener(Controller controller) {
        text.addKeyListener(controller);
    }

    public void removeListener(Controller controller) {
        text.addKeyListener(controller);
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
                text.setForeground(Color.BLACK);
                return true;
            } else {
                text.setToolTipText("Could not use " + geom.getClass() + " as " + parameter.type);
                text.setForeground(Color.RED);
                return false;
            }
        } catch (Throwable eek) {
            text.setToolTipText(eek.getLocalizedMessage());
            text.setForeground(Color.RED);
            return false;
        }
    }

}
