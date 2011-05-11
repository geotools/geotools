/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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

import java.io.IOException;

import javax.swing.JOptionPane;

import org.geotools.data.DataStore;

public class TypeNameChooser {
    
    public static String showTypeNameChooser( DataStore dataStore ){
        if( dataStore == null ){
            return null; // could not connect
        }
        String typeNames[];
        try {
            typeNames = dataStore.getTypeNames();
        } catch (IOException e) {
            return null; // could not connect
        }
        if( typeNames.length == 0 ){
            return null; // could not connect
        }
        if (typeNames.length == 1) {
            return typeNames[0]; // no need to choose only one option
        } else {
            String typeName = (String) JOptionPane
                    .showInputDialog(null, "Please select a type name.", "Type Name",
                            JOptionPane.QUESTION_MESSAGE, null, typeNames, typeNames[0]);
            return typeName;
        }
    }
}
