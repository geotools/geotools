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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

/**
 * An abstract base class for a simple, non-resizable, modal dialog with
 * OK and Cancel buttons. Sub-classes implement the {@linkplain #createControlPanel()}
 * and {@linkplain #onOK()} methods.
 *
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
public abstract class AbstractModalDialog extends JDialog {
    private boolean initialized = false;

    /**
     * Creates a new dialog with a {@code null} parent.
     *
     * @param title dialog title
     */
    public AbstractModalDialog(String title) {
        setTitle(title);
    }

    /**
     * Creates a new dialog.
     *
     * @param parent parent frame
     * @param title dialog title
     */
    public AbstractModalDialog(JFrame parent, String title) {
        super(parent, title);
    }

    /**
     * Creates a new dialog.
     *
     * @param parent parent dialog
     * @param title dialog title
     */
    public AbstractModalDialog(JDialog parent, String title) {
        super(parent, title);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setVisible(boolean b) {
        if (b && !initialized) {
            throw new IllegalStateException(
                    "Sub-class did not call initComponents() before showing dialog");
        }

        super.setVisible(b);
    }

    /**
     * Creates dialog components. This must be called explicitly by the
     * sub-class. We do this to give sub-classes the change to initialize
     * fields which can be used within {@linkplain #createControlPanel()}
     * (called as part of this method).
     */
    protected void initComponents() {
        JPanel panel = new JPanel(new BorderLayout());

        panel.add(createControlPanel(), BorderLayout.CENTER);
        panel.add(createButtonPanel(), BorderLayout.SOUTH);

        add(panel);
        pack();

        setResizable(false);
        setModalityType(ModalityType.APPLICATION_MODAL);
        initialized = true;
    }

    /**
     * Implemented by the sub-class to provide a panel with controls.
     *
     * @return panel with controls
     */
    public abstract JPanel createControlPanel();

    /**
     * Implemented by the sub-class to respond to the OK button.
     */
    public abstract void onOK();

    /**
     * Called when the cancel button is pressed. The base implementation
     * simply closes (disposes) the dialog.
     */
    public void onCancel() {
        closeDialog();
    }

    /**
     * Close the dialog using a call to {@linkplain JDialog#dispose()}.
     */
    public void closeDialog() {
        dispose();
    }

    /**
     * Creates teh OK and Cancel buttons on a panel.
     *
     * @return the button panel
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new MigLayout());

        JButton okBtn = new JButton("OK");
        okBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });
        panel.add(okBtn, "align center");

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });
        panel.add(cancelBtn);

        return panel;
    }
}
