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

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.geotools.process.ProcessFactory;
import org.geotools.process.Processors;
import org.geotools.swing.wizard.*;
import org.geotools.process.literal.IntersectionFactory;


/**
 * This page is responsible for making a process selection widget
 * that moves onto to the selected process page.
 * 
 * @author gdavis
 */
public class ProcessSelectionPage extends JPage {
        Map<String, Object> input;
    JList processList;
    Set<ProcessFactory> processFactories;
    JTextField descLabel;
    ProcessFactory selectedFactory;
    final static String defaultDesc = "select a process to see its description";

    public ProcessSelectionPage() {
        this(null);
    }
    public ProcessSelectionPage( Map<String, Object> input ) {
        super("process selection");
        this.input = input;
        processFactories = Processors.getProcessFactories();
        selectedFactory = null;
    }

    public String getBackPageIdentifier() {
        return null;
    }
    public String getNextPageIdentifier() {
        if (selectedFactory == null) {
                return null;
        }
        ProcessParameterPage inputPage = new ProcessParameterPage(selectedFactory);
        this.getJProcessWizard().registerWizardPanel( inputPage );  
        inputPage.setJProcessWizard(this.getJProcessWizard());
        return inputPage.getIdentifier();
    }
    public void aboutToDisplayPanel() {
        page.removeAll();
        GridBagLayout gridBag = new GridBagLayout();
        page.setLayout(gridBag);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipadx = 5;
        c.ipady = 5;
        
        JLabel title = new JLabel("Process Selection");
        title.setFont(new Font("Arial", Font.BOLD, 14));
        c.gridx = 0;
        c.gridy = 0;
        gridBag.setConstraints(title, c);
        page.add(title);
        
        JLabel description = new JLabel("Select a Process and click 'Next'");
        description.setFont(new Font("Arial", Font.PLAIN, 11));
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.ipady = 20;
        gridBag.setConstraints(description, c);        
        page.add(description);
        
        JLabel label1 = new JLabel("Process:");
        label1.setFont(new Font("Arial", Font.BOLD, 12));
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.ipady = 5;
        gridBag.setConstraints(label1, c);
        page.add(label1);      
        
        JLabel label2 = new JLabel("Process Description:");
        label2.setFont(new Font("Arial", Font.BOLD, 12));
        c.gridx = 1;
        c.gridy = 2;
        gridBag.setConstraints(label2, c);
        page.add(label2);          
        
        String[] data = new String[processFactories.size()];
        createFactoryTitleArray(data);
        processList = new JList(data);
        processList.setFont(new Font("Arial", Font.PLAIN, 12));
        processList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  
        processList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                  Object selection = processList.getSelectedValue();
                  updateProcessDesc(selection);
                }
              }
        });
        c.gridx = 0;
        c.gridy = 3;
        gridBag.setConstraints(processList, c); 
        page.add(processList);
        
        descLabel = new JTextField(defaultDesc, 35);
        descLabel.setEditable(false);
        Border border = new LineBorder(this.getPage().getBackground(), 0);
        descLabel.setBorder(border);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        c.gridx = 1;
        c.gridy = 3;
        gridBag.setConstraints(descLabel, c);
        page.add(descLabel);           
        
        /*
        ParamWidget widget;
        widget = new JField( parameter );
        JComponent field = widget.doLayout();
        page.add(processList);
        fields.put( parameter.key, widget );
        */
    }
    
    /**
     * Populates an array of strings with the process factory titles based on 
     * the factory set
     * @param data the string array to populate
     */
    private void createFactoryTitleArray(String[] data) {
        Iterator<ProcessFactory> iterator = processFactories.iterator();
        int i = 0;
        while (iterator.hasNext() && i < data.length) {
                data[i] = iterator.next().getTitle().toString();
                i++;
        }
    }
    
    /**
     * Returns the first instance of a ProcssFactory in the factories set
     * that has a title matching the given title.
     * @param title
     * @return ProcessFactory instance
     */
    private ProcessFactory findProcessFactoryByTitle(String title) {
        Iterator<ProcessFactory> iterator = processFactories.iterator();
        while (iterator.hasNext()) {
                ProcessFactory fac = iterator.next();
                if (fac.getTitle().toString().equalsIgnoreCase(title)) {
                        return fac;
                }
                
        }
        return null;
    }
    
    /**
     * Update the process description based on the selected process
     * @param selection title of selected process
     */
        private void updateProcessDesc(Object selection) {
                if ( selection == null || 
                                (selectedFactory = findProcessFactoryByTitle(selection.toString())) == null ) {
                        descLabel.setText(defaultDesc);
                        selectedFactory = null;
                        updateNavButtons();
                        return;
                }                       
                descLabel.setText(selectedFactory.getDescription().toString());
                updateNavButtons();
        }
        
        /**
         * Update the wizard nav buttons based on what process factory is selected and
         * if the form is validated to move to the next page or not
         */
        private void updateNavButtons() {
                this.getJProcessWizard().syncWizardButtons();
                
        }     
}