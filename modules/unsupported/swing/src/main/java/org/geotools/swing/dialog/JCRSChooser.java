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

import java.awt.Dialog.ModalityType;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.SynchronousQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
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
 * CoordinateReferenceSystem crs = JCRSChooser.showDialog(
 *         null,  // parent component
 *         "Choose a projection");  // title
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
    private static final Logger LOGGER = Logging.getLogger("org.geotools.swing");
    
    /** Default authority name (EPSG). */
    public static final String DEFAULT_AUTHORITY = "EPSG";
    
    private JDialog dialog;
    private CRSListModel model;
    private CoordinateReferenceSystem crs;
    
    /**
     * Displays a dialog with a list of coordinate reference systems in the EPSG
     * database. This method can be called safely from any thread.
     *
     * @param parent the parent {@code Component}; may be {@code null}
     *
     * @param title dialog title; if {@code null} title will be "Choose Projection"
     *
     * @return a {@code CoordinateReferenceSystem} object or {@code null} if the user
     *         cancelled the dialog
     */
    public static CoordinateReferenceSystem showDialog(
            final Window parent, final String title) {
        
        return showDialog(parent, title, null);
    }
    
    public static CoordinateReferenceSystem showDialog(
            final Window parent, final String title, final String initialCode) {
        
        return showDialog(parent, title, initialCode, null);
    }

    public static CoordinateReferenceSystem showDialog(
            final Window parent, final String title, 
            final String initialCode, final String authority) {

        CoordinateReferenceSystem selected = null;
        
        if (SwingUtilities.isEventDispatchThread()) {
            selected = runDialog(parent, title, initialCode, authority);
            
        } else {
            final SynchronousQueue<CoordinateReferenceSystem> sq =
                    new SynchronousQueue<CoordinateReferenceSystem>();
            
            final Thread currentThread = Thread.currentThread();
            
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        CoordinateReferenceSystem crs = 
                                runDialog(parent, title, initialCode, authority);
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
     * Creates and displays the dialog. Both arguments may be {@code null}.
     * 
     * @param parent the parent window
     * @param title custom title
     * 
     * @return the selected reference system or {@code null} if the dialog
     *     is cancelled by the user
     */
    private static CoordinateReferenceSystem runDialog(
             Window parent, String title, String initialCode, String authority) {
        
        JCRSChooser chooser = new JCRSChooser(parent, title, initialCode, authority);
        DialogUtils.showCentred(chooser.dialog);
        return chooser.crs;
    }

    /**
     * Private constructor.
     * 
     * @param parent parent window (may be {@code null})
     * @param title custom title (may be {@code null})
     */
    private JCRSChooser(Window parent, String title, String initialCode, String authority) {
        if (!EventQueue.isDispatchThread()) {
            throw new IllegalStateException(
                    "Constructor should only be called on the event dispatch thread");
        }

        dialog = new JDialog();
        dialog.setModalityType(ModalityType.APPLICATION_MODAL);
        dialog.setResizable(false);
        
        dialog.setTitle( title == null ? "Choose Projection" : title );
        
        final int WIDTH = 400;
        final int MARGIN = 10;
        
        JPanel panel = new JPanel(new MigLayout(String.format("insets %d", MARGIN)));
        
        if (authority == null) {
            authority = DEFAULT_AUTHORITY;
        }
        model = new CRSListModel(authority);
        
        panel.add(new JLabel("Enter sub-string to filter list"), "wrap");
        
        final JTextField filterFld = new JTextField();
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
        
        panel.add(filterFld, String.format("w %d!, wrap", WIDTH));
        
        final JList listBox = new JList(model);
        listBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    selectCRS(listBox.getSelectedIndex());
                }
            }
        });
        
        JScrollPane listPane = new JScrollPane(listBox);
        
        listBox.setBorder(BorderFactory.createEtchedBorder());
        listBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        int initialIndex = model.findCode(initialCode);
        if (initialIndex >= 0) {
            listBox.setSelectedIndex(initialIndex);
            Point p = listBox.indexToLocation(initialIndex);
            JViewport port = listPane.getViewport();
            port.setViewPosition(p);
        }
        
        panel.add(listPane, String.format("w %d!, h %d!, wrap", WIDTH, WIDTH / 2));
        
        JButton okBtn = new JButton("OK");
        okBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (model.getSize() > 0) {
                    if (model.getSize() == 1) {
                        selectCRS(0);
                    } else {
                        selectCRS(listBox.getSelectedIndex());
                    }
                }
            }
        });
        panel.add(okBtn, "split 2");
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                crs = null;
                closeDialog();
            }
        });
        panel.add(cancelBtn);
        
        dialog.add(panel);
        dialog.pack();
    }

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

    private void closeDialog() {
        dialog.dispose();
    }

}
