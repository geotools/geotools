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
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import org.geotools.data.Parameter;
import org.geotools.swing.wizard.JWizard.Controller;
import org.geotools.util.Converters;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Field that uses the converter API to hack away at a text representation of the provided value.
 *
 * @source $URL$
 */
public class JField extends ParamField {
    private JTextComponent text;
    private boolean single = true;

    public JField( Parameter< ? > parameter ) {
        super(parameter);
    }

    public void setSingleLine( boolean single ){
        this.single = single;
    }
    
    public JComponent doLayout() {
        if( parameter.metadata != null &&
                parameter.metadata.get(Parameter.IS_PASSWORD) == Boolean.TRUE ){
            text = new JPasswordField(32);
        }
        else if( single ){
            text = new JTextField(32);            
        }
        else {
            text = new JTextArea(40, 2);
            ((JTextArea)text).setWrapStyleWord(true);
        }
        text.addKeyListener(new KeyAdapter(){
            public void keyReleased( KeyEvent e ) {
                validate();
            }
        });
        if( text instanceof JTextArea ){
            JScrollPane scroll = new JScrollPane(text, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scroll.setPreferredSize(new Dimension(400, 80));
            return scroll;
        }
        return text;
    }

    public Object getValue() {
        String txt = text.getText();
        if (txt.length() == 0) {
            return null;
        }
        Object value = Converters.convert(txt, parameter.type);
        return value;
    }

    /**
     * Determine the number of dimensions based on the CRS metadata.
     * 
     * @return Number of dimensions expected based on metadata, default of 2
     */
    int getD() {
        CoordinateReferenceSystem crs = (CoordinateReferenceSystem) parameter.metadata
                .get(Parameter.CRS);
        if (crs == null) {
            return 2;
        } else {
            return crs.getCoordinateSystem().getDimension();
        }
    }

    public void setValue( Object value ) {
        String txt = (String) Converters.convert(value, String.class);
        text.setText(txt);
    }
    
    public void addListener( Controller controller ){
        text.addKeyListener( controller );
    }
    
    @Override
    public void removeListener(Controller controller) {
        text.addKeyListener( controller );
    }

    public boolean validate() {
        String txt = text.getText();
        if (txt.length() == 0) {
            return true;
        }
        Object value = Converters.convert(txt, parameter.type);
        if (value == null) {
            text.setToolTipText( "Could not create "+parameter.type );
            text.setForeground(Color.RED);
            return false;
        } else {
            text.setToolTipText(null);
            text.setForeground(Color.BLACK);
            return true;
        }
    }

}
