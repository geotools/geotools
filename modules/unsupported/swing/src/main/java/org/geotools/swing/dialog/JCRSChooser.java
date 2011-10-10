/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2011, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.SynchronousQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


import net.miginfocom.swing.MigLayout;
import org.geotools.referencing.CRS;
import org.geotools.util.logging.Logging;

import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * This class has a single static method that shows a dialog to prompt
 * the user to choose a coordinate reference system. 
 * <p>
 * Example of use:
 * <pre><code>
 * CoordinateReferenceSystem crs = JCRSChooser.showDialog();
 * if (crs != null) {
 *     // use the CRS...
 * }
 * </code></pre>
 *
 * @author Michael Bedward
 * @since 2.6
 *
 * @source $URL$
 * @version $Id$
 */
public class JCRSChooser {
    private static final Logger LOGGER = Logging.getLogger("org.geotools.swing");
    
    /** Default authority name (EPSG). */
    public static final String DEFAULT_AUTHORITY = "EPSG";
    
    private CRSDialog dialog;
    private CoordinateReferenceSystem crs;
    
    /**
     * Constructor is hidden.
     */
    private JCRSChooser() {}
    
    /**
     * Displays a dialog with a list of coordinate reference systems in the EPSG
     * database. 
     * <p>
     * This method can be called safely from any thread.
     *
     * @return a {@code CoordinateReferenceSystem} object or {@code null} if the user
     *         cancelled the dialog
     */
    public static CoordinateReferenceSystem showDialog() {
        return showDialog(null);
    }
    
    /**
     * Displays a dialog with a list of coordinate reference systems in the EPSG
     * database. 
     * <p>
     * This method can be called safely from any thread.
     *
     * @param title optional non-default title
     *
     * @return a {@code CoordinateReferenceSystem} object or {@code null} if the user
     *         cancelled the dialog
     */
    public static CoordinateReferenceSystem showDialog(final String title) {
        return showDialog(title, null);
    }
    
    /**
     * Displays a dialog with a list of coordinate reference systems in the EPSG
     * database and with the specified initial code highlighted.
     * <p>
     * This method can be called safely from any thread.
     *
     * @param title optional non-default title
     * @param initialCode optional initial EPSG code
     *
     * @return a {@code CoordinateReferenceSystem} object or {@code null} if the user
     *         cancelled the dialog
     */
    public static CoordinateReferenceSystem showDialog(final String title, 
            final String initialCode) {
        
        return showDialog(title, initialCode, null);
    }

    /**
     * Displays a dialog with a list of coordinate reference systems provided by
     * the given authority (e.g. "EPSG"), and with the specified initial code
     * highlighted.
     * <p>
     * This method can be called safely from any thread.
     *
     * @param title optional non-default title
     * @param initialCode an optional initial code in appropriate form for the authority
     * @param authority optional non-default authority (defaults to "EPSG")
     *
     * @return a {@code CoordinateReferenceSystem} object or {@code null} if the user
     *         cancelled the dialog
     */
    public static CoordinateReferenceSystem showDialog(final String title, 
            final String initialCode, 
            final String authority) {

        CoordinateReferenceSystem selected = null;
        
        if (SwingUtilities.isEventDispatchThread()) {
            selected = doShow(title, initialCode, authority);
            
        } else {
            final SynchronousQueue<CoordinateReferenceSystem> sq =
                    new SynchronousQueue<CoordinateReferenceSystem>();
            
            final Thread currentThread = Thread.currentThread();
            
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        CoordinateReferenceSystem crs = 
                                doShow(title, initialCode, authority);
                        if (crs == null) {
                            currentThread.interrupt();
                        } else {
                            sq.put(crs);
                        }
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
            
            try {
                selected = sq.take();
            } catch (InterruptedException ex) {
                // dialog was cancelled
            }
        }
        
        return selected;
    }
    
    /**
     * Creates and displays the modal dialog.
     * 
     * @param title optional non-default title
     * @param initialCode an optional initial code in appropriate form for the authority
     * @param authority optional non-default authority (defaults to "EPSG")
     * 
     * @return the selected coordinate reference system or {@code null} if the dialog
     *     is cancelled by the user
     */
    private static CoordinateReferenceSystem doShow(String title, 
            String initialCode, String authority) {
        
        CRSDialog dialog = new CRSDialog(title, initialCode, authority);
        DialogUtils.showCentred(dialog);
        
        CoordinateReferenceSystem crs = dialog.getCoordinateReferenceSystem();
        dialog.dispose();
        
        return crs;
    }

    /**
     * A modal dialog which displays a list of projections for the user to choose from.
     * <p>
     * This class is package-private, rather than private, for unit testing
     * purposes.
     */
    static class CRSDialog extends AbstractSimpleDialog {
        private static final int CONTROL_WIDTH = 400;

        private final String authority;
        private final String initialCode;
        
        private CRSListModel model;
        private JList listBox;
        private CoordinateReferenceSystem crs;

        /**
         * Creates the dialog.
         * 
         * @param title optional non-default title
         * @param initialCode an optional initial code in appropriate form for the authority
         * @param authority optional non-default authority (defaults to "EPSG")
         */
        public CRSDialog(String title, String initialCode, String authority) {
            super(DialogUtils.getString(title, "Choose projection"));
            this.authority = DialogUtils.getString(authority, DEFAULT_AUTHORITY);
            this.initialCode = initialCode;
            
            initComponents();
        }

        @Override
        public JPanel createControlPanel() {
            JPanel panel = new JPanel(new MigLayout("", "[left]"));
            
            model = new CRSListModel(authority);
            
            panel.add(new JLabel("Enter sub-string to filter list"), "growx, wrap");
        
            final JTextField filterFld = new JTextField();
            filterFld.setPreferredSize(new Dimension(CONTROL_WIDTH, 20));
            filterFld.getDocument().addDocumentListener(new DocumentListener() {

                @Override
                public void insertUpdate(DocumentEvent e) {
                    model.setFilter(filterFld.getText());
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    model.setFilter(filterFld.getText());
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    model.setFilter(filterFld.getText());
                }
            });
            
            panel.add(filterFld, "wrap");

            listBox = new JList(model);
            listBox.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        selectCRS(listBox.getSelectedIndex());
                    }
                }
            });

            JScrollPane listPane = new JScrollPane(listBox);
            listPane.setPreferredSize(new Dimension(CONTROL_WIDTH, 300));

            listBox.setBorder(BorderFactory.createEtchedBorder());
            listBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            int initialIndex = model.findCode(initialCode);
            if (initialIndex >= 0) {
                listBox.setSelectedIndex(initialIndex);
                Point p = listBox.indexToLocation(initialIndex);
                JViewport port = listPane.getViewport();
                port.setViewPosition(p);
            }

            panel.add(listPane, "gaptop 10, wrap");
            
            return panel;
        }

        /**
         * Records the selected coordinate reference system, if one exists,
         * and hides the dialog.
         */
        @Override
        public void onOK() {
            if (model.getSize() > 0) {
                if (model.getSize() == 1) {
                    selectCRS(0);
                } else {
                    int index = listBox.getSelectedIndex();
                    if (index >= 0) {
                        selectCRS(index);
                    }
                }
            }
            setVisible(false);
        }

        /**
         * Sets the selection coordinate reference system to {@code null}
         * and hides the dialog.
         */
        @Override
        public void onCancel() {
            crs = null;
            setVisible(false);
        }

        /**
         * Helper method for the list box and {@linkplain #onOK()} method
         * which records the selected coordinate reference system.
         * 
         * @param index selected item index in the list box
         */
        private void selectCRS(int index) {
            String code = model.getCodeAt(index);
            try {
                crs = CRS.decode(DEFAULT_AUTHORITY + ":" + code, true);

            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE,
                        "Failed to get coordinate reference system for code {0}",
                        code);

            } finally {
                closeDialog();
            }
        }

        /**
         * Gets the selected coordinate reference system.
         * 
         * @return selected coordinate reference system (may be {@code null}).
         */
        CoordinateReferenceSystem getCoordinateReferenceSystem() {
            return crs;
        }
    }

}
