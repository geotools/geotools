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
package org.geotools.swing.process;

import java.awt.Dialog;
import org.geotools.swing.wizard.*;
import java.awt.HeadlessException;

public class JProcessWizard extends JWizard {
    private static final long serialVersionUID = -5885825548881784615L;    
    public JProcessWizard( String title ) throws HeadlessException {
        super(title);
        initPages();
    }

    public JProcessWizard( Dialog owner, String title){
        super( owner, title );
        initPages();
        
    }
    private void initPages() {
        // hey eclisia implement this by looking at ProcessFinder :-D
        // registerWizardPanel( new JProcessPage() );
        
        ProcessSelectionPage selectionPage = new ProcessSelectionPage();        
        registerWizardPanel( selectionPage );
        selectionPage.setJProcessWizard(this);
        setCurrentPanel( selectionPage.getIdentifier() );
    }

    public static void main( String args[] ){
        JProcessWizard wizard = new JProcessWizard("Test Input Parameter UI");
        int result = wizard.showModalDialog();
        System.out.println("finished " + result );
    }
}