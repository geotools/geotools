/*
 *    GeoTools - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2005-2006, GeoTools Project Managment Committee (PMC)
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
package org.geotools.data.edigeo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class EdigeoParser {
	
    public BufferedReader reader = null; 
    public String line = ""; // Current line buffer
    public int lineNumber = 0;  // not sure to be useful?
    
    /**
     * to comment....
     *
     */
    public EdigeoParser(File file) throws FileNotFoundException {  
    	super();
        reader = new BufferedReader(new FileReader(file));
    }
    
    /**
     * Stores the next non-null line from file buffer in the line buffer
     *
     * @return True if a non-null string was read, false if EOF or error
     */
    public boolean readLine() {
        String buffer = "";

        do {
            try {
                buffer = reader.readLine();
               
                if (buffer == null) {
                    return readLine(""); //EOF
                }
            } catch (IOException e) {
                return readLine("");
            }

            lineNumber++;
            buffer = buffer.trim();
        } while (buffer.length() == 0);

        return readLine(buffer);
    }

    /**
     * "Reads" a line from the given line, and initializes the token.
     *
     * @param line
     *
     * @return true if could read a non empty line (i.e. line != "")
     */
    public boolean readLine(String line) {

        if (line == null) {
            this.line = "";
        } else {
            this.line = ltrim(line);
        }

        return (!line.equals(""));
    }

    // Can't use String.trim() when Delimiter is \t
    // TODO use stringBuffer and a better algorithm
    public static String ltrim(String untrimmed) {
        while ((untrimmed.length() > 0) && (untrimmed.charAt(0) == ' ')) {
            untrimmed = untrimmed.substring(1);
        }

        return untrimmed;
    }
    
    /**
     * Gets value of the specified descriptor
     * 
     * @param target Descriptor
     * @return String
     */
    public String getValue(String target) {
        int index = line.indexOf(target);
        int nbchar = Integer.parseInt(line.substring(index + 5, index + 7));
        if (index + nbchar + 8 > line.length()) {
            return "";
        }
        String value = line.substring(index + 8, index + nbchar + 8);
        return value;
    }
    
    
    /**
     * Closes the associated reader.
     */
    public void close() {
        try {
            reader.close();
            reader = null ;
        } catch (IOException e) {
        }
    }
    
}
