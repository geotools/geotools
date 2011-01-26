package org.geotools.swing.data;

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
