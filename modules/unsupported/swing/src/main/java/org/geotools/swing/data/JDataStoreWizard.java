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
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataUtilities;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.DataAccessFactory.Param;
import org.geotools.swing.wizard.JWizard;

/**
 * Wizard prompting the user to enter or review connection parameters.
 * <p>
 * GeoTools DataStores that work with files are asked to provide a FileDataStoreFactorySpi
 * documenting what file extensions they support; and any additional parameters that may be
 * interesting etc.
 *
 *
 * @source $URL$
 */
public class JDataStoreWizard extends JWizard {
    private static final long serialVersionUID = -3788708439279424698L;

    DataStoreFactorySpi format;

    /**
     * Optional page used to choose a format if needed.
     */
    private JDataChoosePage page0;

    /**
     * Initial page of user focused options
     */
    private JDataStorePage page1;

    /**
     * Optinoal page2 used for advanced options
     */
    private JDataStorePage page2;

    /**
     * Connection parameters; shared with pages for editing
     */
    protected Map<String, Object> connectionParameters;

    /**
     * Open up a wizard up with an initial page to
     * choose a datastore factory.
     */
    public JDataStoreWizard(){
        this(null,  new HashMap<String, Object>());
    }
    /**
     * Quick transition from JFileDataStoreChooser; allowing applications to migrate to connection
     * parameters.
     * 
     * @param extension
     *            Extension used to look up FileDataStoreFactory
     */
    public JDataStoreWizard(String extension) {
        this(extension == null ? null : FileDataStoreFinder.getDataStoreFactory(extension));
    }

    /**
     * Set up the wizard with a "default" set of parameters.
     * 
     * @param format
     */
    public JDataStoreWizard(DataStoreFactorySpi format) {
        this(format, new HashMap<String, Object>());
    }

    @SuppressWarnings("unchecked")
    public JDataStoreWizard(DataStoreFactorySpi format, Map params) {        
        super(format == null ? "Connect" : format == null ? "" : format.getDisplayName());
        
        if (params == null) {
            connectionParameters = new HashMap<String, Object>();
        }
        else {
            connectionParameters = params;            
        }
        if( format == null ){
            page0 = new JDataChoosePage();
            page0.setPageIdentifier("page0");
            page0.setNextPageIdentifier("page1");
            registerWizardPanel( page0 );
        }
        
        fillInDefaults(format, params);

        this.format = format;
        page1 = new JDataStorePage(format, connectionParameters);
        page1.setLevel("user");
        page1.setPageIdentifier("page1");
        if( page0 != null ){
            page1.setBackPageIdentifier("page0");
        }
        registerWizardPanel(page1);
        
        if (countParamsAtLevel(format, "advanced") != 0) {
            page2 = new JDataStorePage(format, connectionParameters);
            page2.setPageIdentifier("page2");
            page2.setBackPageIdentifier("page1");
            page2.setLevel("advanced");
            registerWizardPanel(page2);

            // link from page 1
            page1.setNextPageIdentifier("page2");
        }
        if( page0 != null ){
            setCurrentPanel("page0");
        }
        else {
            setCurrentPanel("page1");
        }
    }

    /**
     * Method used to fill in any required "programming" level defaults such as dbtype.
     * 
     * @param format2
     * @param params
     */
    private void fillInDefaults(DataStoreFactorySpi format, Map<String, Object> params) {
        if (format == null)
            return;
        for (Param param : format.getParametersInfo()) {
            if (param.required && "program".equals(param.getLevel())) {
                if (!params.containsKey(param.key)) {
                    params.put(param.key, param.sample);
                }
            }
        }
    }

    private int countParamsAtLevel(DataStoreFactorySpi format, String level) {
        if (format == null)
            return 0;
        int count = 0;
        Param[] parametersInfo = format.getParametersInfo();
        if (level == null) {
            return parametersInfo.length;
        }
        for (Param param : parametersInfo) {
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
    public File getFile(){
        URL url = (URL) connectionParameters.get("url");
        return DataUtilities.urlToFile(url);
    }
    /**
     * @param format2
     */
    public void setFormat(DataStoreFactorySpi format) {
        if( this.format == format ){
            return;
        }
        if( connectionParameters.isEmpty() ){
            fillInDefaults(format, connectionParameters);
        }
        if( page1 != null ){
            page1.setFormat( format );
        }
        if( page2 != null ){
            page2.setFormat( format );
        }
    }

}
