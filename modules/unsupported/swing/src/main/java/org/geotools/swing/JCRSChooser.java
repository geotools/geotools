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
 */

package org.geotools.swing;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * This class has a single static method that shows a dialog to prompt
 * the user to choose a coordinate reference system. The options listed
 * are those supported by the EPSG database.
 * <p>
 * Example of use:
 * <pre><code>
 * CoordinateReferenceSystem crs = JCRSChooser.showDialog(
 *         null,  // parent component
 *         "Choose a projection",  // title
 *         "Select the CRS to re-project your data",  // message
 *         "EPSG:4326");  // initial selection
 *
 * if (crs != null) {
 *     // use the CRS...
 * }
 * </code></pre>
 *
 * @author Michael Bedward
 * @since 2.6
 * @source $URL$
 * @version $Id$
 */
public class JCRSChooser {

    /**
     * Displays a dialog with a list of coordinate reference systems in the EPSG
     * database.
     *
     * @param parent the parent {@code Component}; may be {@code null}
     *
     * @param title dialog title; if {@code null} title will be "Choose Projection"
     *
     * @param message dialog message; {@code null} for no message
     *
     * @param initialSelection the initial selection for the dialog to display
     *        (e.g. "EPSG:4326")
     *
     * @return a {@code CoordinateReferenceSystem} object or {@code null} if the user
     *         cancelled the dialog or a problem occurred
     */
    public static CoordinateReferenceSystem showDialog(
            Component parent, String title, String message, String initialSelection) {

        CoordinateReferenceSystem crs = null;

        if (title == null) {
            title = "Choose Projection";
        }

        try {
            CRSAuthorityFactory authorityFactory = ReferencingFactoryFinder.getCRSAuthorityFactory("EPSG", null);
            Set<String> codes = authorityFactory.getAuthorityCodes(CoordinateReferenceSystem.class);
            List<String> desc = new ArrayList<String>();

            for (String code : codes) {
                desc.add(code + ": " + authorityFactory.getDescriptionText("EPSG:" + code).toString());
            }
            String selected = (String) JOptionPane.showInputDialog(parent, message,
                    title, JOptionPane.QUESTION_MESSAGE, null,
                    desc.toArray(), initialSelection);

            if (selected == null) {
                return null;
            }

            String selectedCode = selected.substring(0, selected.indexOf(':'));
            crs = CRS.decode("EPSG:" + selectedCode, true);
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        return crs;
    }

}
