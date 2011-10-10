/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.swing.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.geotools.factory.GeoTools;

/**
 * An 'About' dialog which displays information about the host environment,
 * software licenses pertaining to GeoTools (not implemented yet) and, if provided, 
 * summary details about your application.
 * <p>
 * Environment information is obtained from {@linkplain GeoTools#getEnvironmentInfo()}
 * and consists of:
 * <ul>
 * <li>GeoTools version</li>
 * <li>Java version</li>
 * <li>Host operating system and version</li>
 * </ul>
 * 
 * The GeoTools jar listing is obtained from {@linkplain GeoTools#getGeoToolsJarInfo()}
 * and consists of GeoTools jars (of the active version) on the application's class path.
 * <p>
 * 
 * To have the dialog display details of your own application, you pass them as a String
 * to the dialog constructor as in this example:
 * <pre><code>
 * final String appInfo = String.format(
 *           "GeoFoo: Map your foos in real time %nVersion 0.0.1");
 *
 * SwingUtilities.invokeLater(new Runnable() {
 *     &#64Override
 *     public void run() {
 *         AboutDialog dialog = new AboutDialog("About", appInfo);
 *         DialogUtils.showCentred(dialog);
 *     }
 * });
 * </code></pre>
 * 
 * When no application details are provided the 'Application' category will not be
 * shown in the dialog's category list.
 * 
 * @author Michael Bedward
 * @since 2.7
 * @source $URL$
 * @version $Id$
 */
public class AboutDialog extends AbstractSimpleDialog {
    
    private static final int DEFAULT_HEIGHT = 400;
    private static final int LIST_WIDTH = 150;
    private static final int TEXT_AREA_WIDTH = 450;
    
    private final String applicationInfo;
    private final boolean hasApplicationInfo;

    /*
     * Categories of information displayed by the dialog.
     */
    public static enum Category {
        APPLICATION("Application"),
        ENVIRONMENT("Environment"),
        LICENCES("Licences"),
        JARS("GeoTools jars"),
        ALL("All");
        
        private final String name;
        private Category(String name) {
            this.name = name;
        }
        
        public static Category getByIndex(int index, boolean hasApplicationInfo) {
            if (!hasApplicationInfo) {
                index++;
            }
            return Category.values()[index];
        }
        
        @Override
        public String toString() {
            return name;
        }
    }
    
    /*
     * Model for the dialog list control which displays categories.
     */
    private class CategoryListModel extends AbstractListModel {
        @Override
        public int getSize() {
            return Category.values().length - (hasApplicationInfo ? 0 : 1);
        }

        @Override
        public Object getElementAt(int index) {
            return Category.getByIndex(index, hasApplicationInfo).toString();
        }
    }

    private JList categoryList;
    private JTextArea textArea;
    
    /**
     * Creates a new dialog to display environment information but no application
     * details.
     */
    public AboutDialog() {
        this("About");
    }

    /**
     * Creates a new dialog to display environment information but no application
     * details.
     * 
     * @param title dialog title
     */
    public AboutDialog(String title) {
        this(title, null);
    }
    
    /**
     * Creates a new dialog to display environment information together with
     * application details.
     * 
     * @param title dialog title
     * @param applicationInfo the application information to display
     */
    public AboutDialog(String title, String applicationInfo) {
        super((JFrame) null, title, true, true);
        
        if (applicationInfo != null && applicationInfo.trim().length() > 0) {
            this.applicationInfo = applicationInfo;
            this.hasApplicationInfo = true;
        } else {
            this.applicationInfo = null;
            this.hasApplicationInfo = false;
        }
        
        initComponents();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public JPanel createControlPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        categoryList = new JList(new CategoryListModel());
        categoryList.setPreferredSize(new Dimension(LIST_WIDTH, DEFAULT_HEIGHT));
        categoryList.setBorder(BorderFactory.createTitledBorder("Categories"));
        
        categoryList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                showInfo(categoryList.getSelectedIndex());
            }
        });
        
        panel.add(categoryList, BorderLayout.WEST);
        
        textArea = new JTextArea();
        textArea.setPreferredSize(new Dimension(TEXT_AREA_WIDTH, DEFAULT_HEIGHT));
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        panel.add(textArea, BorderLayout.CENTER);

        categoryList.setSelectedIndex(0);
        return panel;
    }

    /**
     * Closes the dialog, disposing it.
     */
    @Override
    public void onOK() {
        closeDialog();
    }
    
    /**
     * Creates the button panel with 'Done' and 'Copy to clipboard' buttons.
     * 
     * @return the button panel
     */
    @Override
    protected JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        JButton copyBtn = new JButton("Copy to clipboard");
        copyBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copyToClipboard();
            }
        });
        panel.add(copyBtn);

        JButton okBtn = new JButton("Done");
        okBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });
        panel.add(okBtn);

        return panel;
    }
    
    /**
     * Called when a category has been selected in the list control.
     * 
     * @param index category index
     */
    private void showInfo(int index) {
        switch (Category.getByIndex(index, hasApplicationInfo)) {
            case APPLICATION:
                showApplicationInfo();
                break;
                
            case ENVIRONMENT:
                showEnvironmentInfo();
                break;
                
            case JARS:
                showJarInfo();
                break;
                
            case LICENCES:
                showLicenceInfo();
                break;
                
            case ALL:
                showAllInfo();
                break;
                
            default:
                throw new IndexOutOfBoundsException("Bad index value: " + index);
        }
    }

    /**
     * Displays application information. This is only called on the event 
     * dispatch thread.
     */
    private void showApplicationInfo() {
        textArea.setText(applicationInfo);
    }
    
    /**
     * Displays environment information. This is only called on the event 
     * dispatch thread.
     */
    private void showEnvironmentInfo() {
        textArea.setText(GeoTools.getEnvironmentInfo());
    }
    
    /**
     * Displays licence information. This is only called on the event 
     * dispatch thread.
     */
    private void showLicenceInfo() {
        textArea.setText("This is the licence info");
    }
    
    /**
     * Displays GeoTools jars on the classpath. This is only called on the event 
     * dispatch thread.
     */
    private void showJarInfo() {
        textArea.setText(GeoTools.getGeoToolsJarInfo());
    }
    
    /**
     * Displays all information combined. This is only called on the event 
     * dispatch thread.
     */
    private void showAllInfo() {
        final String newline = String.format("%n");
        final StringBuilder sb = new StringBuilder();
        
        if (hasApplicationInfo) {
            sb.append(applicationInfo).append(newline).append(newline);
        }
        
        sb.append(GeoTools.getEnvironmentInfo()).append(newline).append(newline);
        sb.append(GeoTools.getGeoToolsJarInfo()).append(newline).append(newline);
        sb.append("This is the licence info");
        
        textArea.setText(sb.toString());
    }
    
    /**
     * Copies the current contents of the dialog text area to the system clipboard.
     */
    private void copyToClipboard() {
        StringSelection sel = new StringSelection(textArea.getText());
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        clip.setContents(sel, sel);
    }

}
