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

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.jface.wizard.Wizard;
import org.geotools.data.Parameter;
import org.geotools.util.URLs;

/**
 * The parameter list wizard.
 *
 * @author Andrea Antonello (www.hydrologis.com)
 */
public class JParameterListWizard extends Wizard {

    private Map<String, Object> connectionParameters;

    private JParameterListPage userPage;
    private JParameterListPage advancedPage;

    /**
     * Constructor.
     *
     * @param title title for the dialog
     * @param description brief description to be displayed on the page
     * @param contents a {@code List} of {@code Parameter} objects defining the data being requested
     */
    public JParameterListWizard(String title, String description, List<Parameter<?>> contents) {
        this(title, description, contents, new HashMap<String, Object>());
    }

    public JParameterListWizard(
            String title,
            String description,
            List<Parameter<?>> contents,
            Map<String, Object> connectionParams) {

        this.connectionParameters =
                connectionParams == null ? new HashMap<String, Object>() : connectionParams;
        fillInDefaults(contents, this.connectionParameters);

        List<Parameter<?>> userContents = contentsForLevel(contents, "user");

        userPage = new JParameterListPage(title, description, userContents, connectionParameters);

        List<Parameter<?>> advancedContents = contentsForLevel(contents, "advanced");

        if (advancedContents.size() > 0) {
            advancedPage =
                    new JParameterListPage(
                            title, description, advancedContents, connectionParameters);
        }
    }

    public void addPages() {
        super.addPages();
        if (userPage != null) addPage(userPage);
        if (advancedPage != null) addPage(advancedPage);
    }

    public boolean performFinish() {
        return false;
    }

    /**
     * Method used to fill in any required "programming" level defaults such as dbtype.
     *
     * @param connectionParams a {@code Map} of initial parameter values
     */
    private void fillInDefaults(List<Parameter<?>> contents, Map<String, Object> connectionParams) {
        if (connectionParams == null) return;

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
                    String check =
                            param.metadata == null
                                    ? "user"
                                    : (String) param.metadata.get(Parameter.LEVEL);
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

    /**
     * Retrieve the connection parameters entered
     *
     * @return the {@code Map} of connection parameters
     */
    public Map<String, Object> getConnectionParameters() {
        return connectionParameters;
    }

    /**
     * Helper method that returns the "url" element of the connection parameters as a File, if
     * present. Equivalent to:
     *
     * <pre><code>
     *     URL url = (URL) myWizard.getConnectionParameters().get("url");
     *     File file = URLs.urlToFile(url);
     * </code></pre>
     *
     * @return url parameter as a File, or null if not applicable
     */
    public File getFile() {
        URL url = (URL) connectionParameters.get("url");
        return URLs.urlToFile(url);
    }
}
