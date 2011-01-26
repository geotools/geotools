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

package org.geotools.swing.data;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.DataUtilities;
import org.geotools.data.Parameter;
import org.geotools.swing.wizard.JWizard;

/**
 * Wizard prompting the user to enter or review connection parameters.
 * <p>
 * Example of use (from the GeoTools example project):
 * <pre><code>
        List<Parameter<?>> list = new ArrayList<Parameter<?>>();
        list.add(new Parameter<File>("image", File.class, "Image",
                "GeoTiff or World+Image to display as basemap",
                new KVP( Parameter.EXT, "tif", Parameter.EXT, "jpg")));
        list.add(new Parameter<File>("shape", File.class, "Shapefile",
                "Shapefile contents to display", new KVP(Parameter.EXT, "shp")));

        JParameterListWizard wizard = new JParameterListWizard("Image Lab",
                "Fill in the following layers", list);
        int finish = wizard.showModalDialog();

        if (finish != JWizard.FINISH) {
            System.exit(0);
        }
        File imageFile = (File) wizard.getConnectionParameters().get("image");
        File shapeFile = (File) wizard.getConnectionParameters().get("shape");
 * </pre></code>
 *
 * @source $URL$
 */
public class JParameterListWizard extends JWizard {
    private static final long serialVersionUID = -3961250234483352643L;

    /**
     * Initial page of user focused options
     */
    private JParameterListPage userPage;

    /**
     * Optional page2 used for advanced options
     */
    private JParameterListPage advancedPage;

    /**
     * Connection parameters; shared with pages for editing
     */
    protected Map<String, Object> connectionParameters;

    /**
     * Constructor.
     *
     * @param title title for the dialog
     * @param description brief description to be displayed on the page
     * @param contents a {@code List} of {@code Parameter} objects defining the data being requested
     * @param connectionParams an optional {@code Map} of initial parameter values
     */
    public JParameterListWizard(String title, String description, List<Parameter<?>> contents,
            Map<String, Object> connectionParams) {
        super(title);
        this.connectionParameters = connectionParams == null ? new HashMap<String, Object>() : connectionParams;
        fillInDefaults(contents, this.connectionParameters);

        List<Parameter<?>> userContents = contentsForLevel(contents, "user");

        userPage = new JParameterListPage(title, description, userContents, connectionParameters);
        userPage.setPageIdentifier("userPage");
        registerWizardPanel(userPage);

        List<Parameter<?>> advancedContents = contentsForLevel(contents, "advanced");

        if (advancedContents.size() > 0) {
            advancedPage = new JParameterListPage(title, description, advancedContents,
                    connectionParameters);
            advancedPage.setPageIdentifier("advancedPage");
            advancedPage.setBackPageIdentifier("userPage");
            registerWizardPanel(advancedPage);

            // link from page 1
            userPage.setNextPageIdentifier("advancedPage");
        }
        setCurrentPanel("userPage");
    }

    /**
     * Constructor.
     *
     * @param title title for the dialog
     * @param description brief description to be displayed on the page
     * @param contents a {@code List} of {@code Parameter} objects defining the data being requested
     */
    public JParameterListWizard(String title, String description, List<Parameter<?>> contents) {
        this( title, description, contents, new HashMap<String,Object>() );
    }

    /**
     * Method used to fill in any required "programming" level defaults such as dbtype.
     * 
     * @param contents
     * @param connectionParams a {@code Map} of initial parameter values
     */
    private void fillInDefaults(List<Parameter<?>> contents, Map<String, Object> connectionParams) {
        if (connectionParams == null)
            return;

        for (Parameter<?> param : contents) {
            if (param.required && "program".equals(param.getLevel())) {
                if (!connectionParams.containsKey(param.key)) {
                    connectionParams.put(param.key, param.sample);
                }
            }
        }
    }

    List<Parameter<?>> contentsForLevel(List<Parameter<?>> contents, String level) {
        List<Parameter<?>> list = new ArrayList<Parameter<?>>();
        if (level == null) {
            level = "user";
        }
        if (contents != null) {
            for (Parameter<?> param : contents) {
                if (level != null) {
                    String check = param.metadata == null ? "user" : (String) param.metadata
                            .get(Parameter.LEVEL);
                    if (check == null) {
                        check = "user";
                    }
                    if (level.equals(check)) {
                        // we are good this is the one we want
                        list.add(param);
                    }
                }
            }
        }
        return list;
    }

    private int countParamsAtLevel(List<Parameter<?>> contents, String level) {
        if (contents == null)
            return 0;
        int count = 0;
        if (level == null) {
            return contents.size();
        }
        for (Parameter<?> param : contents) {
            String check = param.getLevel();
            if (level.equals(check)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Retrieve the connection parameters entered
     *
     * @return the {@code Map} of connection parameters
     */
    public Map<String, Object> getConnectionParameters() {
        return connectionParameters;
    }

    /**
     * Helper method that returns the "url" element of the connection
     * parameters as a File, if present. Equivalent to:
     * <pre><code>
     *     URL url = (URL) myWizard.getConnectionParameters().get("url");
     *     File file = DataUtilities.urlToFile(url);
     * </code></pre>
     *
     * @return url parameter as a File, or null if not applicable
     */
    public File getFile() {
        URL url = (URL) connectionParameters.get("url");
        return DataUtilities.urlToFile(url);
    }

}
