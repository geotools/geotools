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
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.geotools.data.DataAccessFactory.Param;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataUtilities;
import org.geotools.data.FileDataStoreFinder;

/**
 * Data store wizard.
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 */
public class JDataStoreWizard extends Wizard {

    private Map<String, Object> connectionParameters;
    private DataStoreFactorySpi format;
    private JDataChoosePage dataChoosePage;
    private JDataStorePage dataStorePage;

    public JDataStoreWizard( String extension ) {
        this(extension == null ? null : FileDataStoreFinder.getDataStoreFactory(extension));
    }

    public JDataStoreWizard( DataStoreFactorySpi format ) {
        this(format, new HashMap<String, Object>());
    }

    public JDataStoreWizard( DataStoreFactorySpi format, Map<String, Object> params ) {
        setWindowTitle(format == null ? "Connect" : format == null ? "" : format.getDisplayName());

        if (params == null) {
            connectionParameters = new HashMap<String, Object>();
        } else {
            connectionParameters = params;
        }
        if (format == null) {
            dataChoosePage = new JDataChoosePage();
        }

        fillInDefaults(format, params);

        this.format = format;
        dataStorePage = new JDataStorePage(format, connectionParameters);
        dataStorePage.setLevel("user");

        if (countParamsAtLevel(format, "advanced") != 0) {
            dataStorePage = new JDataStorePage(format, connectionParameters);
            dataStorePage.setLevel("advanced");
        }
    }

    public void addPages() {
        super.addPages();
        if (dataChoosePage != null)
            addPage(dataChoosePage);
        addPage(dataStorePage);
    }

    public boolean performFinish() {
        return true;
    }

    private void fillInDefaults( DataStoreFactorySpi format, Map<String, Object> params ) {
        if (format == null)
            return;
        for( Param param : format.getParametersInfo() ) {
            if (param.required && "program".equals(param.getLevel())) {
                if (!params.containsKey(param.key)) {
                    params.put(param.key, param.sample);
                }
            }
        }
    }

    private int countParamsAtLevel( DataStoreFactorySpi format, String level ) {
        if (format == null)
            return 0;
        int count = 0;
        Param[] parametersInfo = format.getParametersInfo();
        if (level == null) {
            return parametersInfo.length;
        }
        for( Param param : parametersInfo ) {
            String check = param.getLevel();
            if (level.equals(check)) {
                count++;
            }
        }
        return count;
    }

    public Map<String, Object> getConnectionParameters() {
        return connectionParameters;
    }

    /**
     * Helper method to check if for "url" parameter.
     * 
     * @return url parameters as a File, or null if not applicable
     */
    public File getFile() {
        URL url = (URL) connectionParameters.get("url");
        return DataUtilities.urlToFile(url);
    }

    /**
     * Setter for the format.
     * 
     * @param format the format to set.
     */
    public void setFormat( DataStoreFactorySpi format ) {
        if (this.format == format) {
            return;
        }
        if (connectionParameters.isEmpty()) {
            fillInDefaults(format, connectionParameters);
        }
        if (dataStorePage != null) {
            dataStorePage.setFormat(format);
        }
    }

    public static void main( String[] args ) {
        Display display = new Display();
        Shell shell = new Shell(display);
        JDataStoreWizard wizard = new JDataStoreWizard(".shp");
        WizardDialog dialog = new WizardDialog(shell, wizard);
        dialog.create();
        dialog.open();
    }

}
