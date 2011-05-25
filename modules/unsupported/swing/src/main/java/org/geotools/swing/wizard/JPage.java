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

import java.util.Map;

import javax.swing.JPanel;

/**
 * Wizard page; will be created as needed.
 * @author Jody Garnett (LISAsoft)
 * @author Graham Davis (Refractions Research)
 *
 *
 * @source $URL$
 */
public abstract class JPage {
    /**
     * Used to indicate which page we should start with.
     */
    public static final String DEFAULT = "Default";

    /**
     * Used to indicate that we are done and the wizard should close
     */
    public static final String FINISH = "Finish";
    
    /**
     * Used to indicate that there is a next step to complete
     */
    public static final String NEXT = "Next";    

    /**
     * The page contents; created as needed.
     */
    private JPanel panel;
    
    /**
     * Identifier used to track this page in a work flow.
     */
    protected String pageIdentifier;
    
    protected String backPageIdentifier =  null;
    
    protected String nextPageIdentifier = FINISH;
    
    /**
     * Wizard hosting this process page; we will access wizard.model directly to look up our friends
     * for next and previous.
     */
    private JWizard wizard;
    
    /**
     * Create a default page.
     */
    public JPage() {
        this( DEFAULT );
    }

    /**
     * Create a page with the provided id.
     */
    public JPage(String id ) {
        pageIdentifier = id;
    }

    /**
     * Access the JPanel - init will be used to create the
     * panel the first time this method is called.
     * @return JPanel used for this wizard page
     */
    public final JPanel getPanel() {
        if( panel == null){
            return createPanel();
        }
        
        return panel;        
    }

    public String getPageIdentifier() {
        return pageIdentifier;
    }
    
    public void setPageIdentifier(String pageIdentifier) {
        this.pageIdentifier = pageIdentifier;
    }
    
    final void setJWizard( JWizard w ) {
        wizard = w;
    }

    public final JWizard getJWizard() {
        return wizard;
    }
    public Map<String, JPage> getModel() {
        return wizard.model;
    }

    /**
     * Identifier of the panel to use Next.
     * 
     * @return Return id of the next JProcessPage or null if next should be disabled. You can use
     *         FINISH to indicate the wizard is complete and may be closed.
     */
    public String getNextPageIdentifier() {
        return nextPageIdentifier;
    }
    
    public void setNextPageIdentifier(String nextPageIdentifier) {
        this.nextPageIdentifier = nextPageIdentifier;
    }
    
    /**
     * Identifier of the panel to use Back.
     * 
     * @return Return id of the next JProcessPage or null if next should be disabled.
     */
    public String getBackPageIdentifier() {
        return backPageIdentifier;
    }
    
    public void setBackPageIdentifier(String backPageIdentifier) {
        this.backPageIdentifier = backPageIdentifier;
    }    

    /**
     * Called to initialize the page for the first time.
     * <p>
     * Please note this method will normally only be called once; even if the
     * user clicks forwards and backwards to return to your page. It
     * is only called if getPanel() returns null.
     * <p>
     * Calling this method directly will erase any previously constructed
     * panel.
     */
    public JPanel createPanel(){
        panel = new JPanel();
        return panel;
    }
    /**
     * Called just before the panel is to be displayed.
     * <p>
     * This is a good time to populate your fields
     * with values; hook up any listeners and
     * gernally muck about.
     */
    public void preDisplayPanel() {
        // the default implementation does nothing
    }

    /**
     * Called just after the panel is displayed.
     * <p>
     * This is a good time to perform any animations or set the focus
     * into one of the fields etc.
     * </p>
     */
    public void postDisplayPanel() {
        // the default implementation does nothing
    }

    /**
     * Validation of page state; if the page isValid then the next or
     * finish button will be enabled.
     * <p>
     * If you need to call setNextPageIdentifier based on input
     * this is the time to do it.
     * </p>
     * @return true if the page contents are valid
     */
    public boolean isValid(){
        return true;
    }
    
    /**
     * Override this method to perform functionality just before the panel is to be hidden.
     */
    public void preClosePanel() {
    }

    /**
     * Called when the workflow is completed (either in Finish, Canel or Error)
     * giving the page a chance to clean up any resources it is using such
     * as a database connection.
     */
    public void dispose(){
    }

}
