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

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.geotools.gml.SubHandlerLinearRing;
import org.geotools.util.logging.Logging;

/**
 * Swing does not provide a wizard construct (boo hiss) so this is a quick dialog that can step us
 * through a series of pages.
 *
 * <p>This code is based on <a
 * href="http://java.sun.com/developer/technicalArticles/GUI/swing/wizard/">Creating Wizard Dialogs
 * with Java</a>.
 *
 * @author Jody, gdavis
 */
public class JWizard extends JDialog {

    static final Logger LOGGER = Logging.getLogger(SubHandlerLinearRing.class);

    private static final long serialVersionUID = 1L;

    /** Indicates that the 'Finish' button was pressed to close the dialog. */
    public static final int FINISH = 0;

    /**
     * Indicates that the 'Cancel' button was pressed to close the dialog, or the user pressed the
     * close box in the corner of the window.
     */
    public static final int CANCEL = 1;

    /** Indicates that the dialog closed due to an internal error. */
    public static final int ERROR = 2;

    Controller controller = new Controller();

    HashMap<String, JPage> model = new HashMap<String, JPage>();

    JPage current;

    private JPanel cardPanel;

    private CardLayout cardLayout;

    private JButton backButton;

    private JButton nextButton;

    private JButton finishButton;

    private JButton cancelButton;

    private int returnCode;

    public JWizard(String title) throws HeadlessException {
        super();
        setTitle(title);
        initComponents();
    }

    public JWizard(Dialog owner, String title) throws HeadlessException {
        super(owner, title, true, null);
        initComponents();
    }

    private void initComponents() {
        JPanel buttonPanel = new JPanel();
        Box buttonBox = new Box(BoxLayout.X_AXIS);

        cardPanel = new JPanel();
        cardPanel.setBorder(new EmptyBorder(new Insets(5, 10, 5, 10)));

        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);
        backButton = new JButton("Back");
        nextButton = new JButton("Next");
        finishButton = new JButton("Finish");
        cancelButton = new JButton("Cancel");

        backButton.addActionListener(controller);
        nextButton.addActionListener(controller);
        finishButton.addActionListener(controller);
        cancelButton.addActionListener(controller);

        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.add(new JSeparator(), BorderLayout.NORTH);

        buttonBox.setBorder(new EmptyBorder(new Insets(5, 10, 5, 10)));
        buttonBox.add(backButton);
        buttonBox.add(Box.createHorizontalStrut(10));
        buttonBox.add(nextButton);
        buttonBox.add(Box.createHorizontalStrut(30));

        buttonBox.add(finishButton);
        buttonBox.add(Box.createHorizontalStrut(10));
        buttonBox.add(cancelButton);

        buttonPanel.add(buttonBox, java.awt.BorderLayout.EAST);
        getContentPane().add(buttonPanel, java.awt.BorderLayout.SOUTH);
        getContentPane().add(new JScrollPane(cardPanel), java.awt.BorderLayout.CENTER);
    }

    public Boolean isCancelEnabled() {
        return cancelButton == null ? null : cancelButton.isEnabled();
    }

    public void setCancelEnabled(Boolean isEnabled) {
        Boolean oldValue = cancelButton.isEnabled();
        if (!isEnabled.equals(oldValue)) {
            firePropertyChange("isCancelEnabled", oldValue, isEnabled);
            cancelButton.setEnabled(isEnabled);
        }
    }

    public Boolean isNextEnabled() {
        return nextButton == null ? null : nextButton.isEnabled();
    }

    public void setNextEnabled(Boolean isEnabled) {
        Boolean oldValue = nextButton.isEnabled();
        if (!isEnabled.equals(oldValue)) {
            firePropertyChange("isNextEnabled", oldValue, isEnabled);
            nextButton.setEnabled(isEnabled);
        }
    }

    public void setFinishEnabled(Boolean isEnabled) {
        Boolean oldValue = finishButton.isEnabled();
        if (!isEnabled.equals(oldValue)) {
            firePropertyChange("isFinishEnabled", oldValue, isEnabled);
            finishButton.setEnabled(isEnabled);
        }
    }

    public Boolean isBackEnabled() {
        return backButton == null ? null : backButton.isEnabled();
    }

    public void setBackEnabled(Boolean isEnabled) {
        Boolean oldValue = backButton.isEnabled();
        if (!isEnabled.equals(oldValue)) {
            firePropertyChange("isBackEnabled", oldValue, isEnabled);
            backButton.setEnabled(isEnabled);
        }
    }

    /**
     * Closes the dialog and sets the return code to the integer parameter.
     *
     * @param code The return code.
     */
    protected void close(int code) {
        returnCode = code;
        for (Component card : cardPanel.getComponents()) {
            String id = card.getName();
            JPage page = model.get(id);
            if (page != null) {
                try {
                    page.dispose();
                } catch (Throwable t) {
                    LOGGER.info(id + " close: " + t);
                }
            }
        }
        dispose();
    }

    public void windowClosing(WindowEvent e) {
        returnCode = CANCEL;
    }

    /**
     * Retrieves the last return code set by the dialog.
     *
     * @return An integer that identifies how the dialog was closed. See the *_RETURN_CODE constants
     *     of this class for possible values.
     */
    public int getReturnCode() {
        return returnCode;
    }

    /**
     * Convenience method that displays a modal wizard dialog and blocks until the dialog has
     * completed.
     *
     * @return Indicates how the dialog was closed one of CANCEL, ERROR, FINISH
     */
    public int showModalDialog() {
        setModal(true);
        pack();
        setVisible(true);
        return returnCode;
    }

    /**
     * Called to display a page.
     *
     * <p>
     */
    public void setCurrentPanel(String id) {
        if (id == null) {
            close(ERROR);
        }
        JPage old = current;
        if (old != null) {
            old.preClosePanel();
        }
        if (!model.containsKey(id)) {
            close(ERROR);
        }
        JPage page = model.get(id);
        if (page == null) {
            close(ERROR);
            return;
        }
        current = page;

        JPanel panel = null;
        // see if panel is already created
        for (Component card : cardPanel.getComponents()) {
            if (id.equals(card.getName())) {
                panel = (JPanel) card;
                break;
            }
        }
        if (panel == null) {
            // lazy create panel
            panel = page.getPanel();
            if (panel == null) {
                close(ERROR); // card panel not provided
                return;
            }
            panel.setName(id);
            cardPanel.add(panel, id);
        }
        controller.syncButtonsToPage();
        page.preDisplayPanel();

        // Show the panel in the dialog.
        cardLayout.show(cardPanel, id);
        page.postDisplayPanel();
    }
    /**
     * Registers the page with this JWizard. The page is stored by its identifier (so other pages
     * can look it up). The page.setJWizard() method is also called so the page can refer to its
     * containing wizard at runtime.
     */
    public void registerWizardPanel(JPage page) {
        page.setJWizard(this);
        model.put(page.getPageIdentifier(), page);
        page.setJWizard(this);
        if (page.getPageIdentifier() == JPage.DEFAULT) {
            setCurrentPanel(page.getPageIdentifier());
        }
    }

    /**
     * The controller can be hooked up to your own fields or lists; it will call syncWizardButtons()
     * which will use validate to update the buttons in response to user input.
     *
     * @return Controller
     */
    public Controller getController() {
        return controller;
    }

    /** The controller listens to everything and updates the buttons */
    public class Controller
            implements ActionListener, KeyListener, DocumentListener, ListSelectionListener {
        public boolean listen = true;

        public void actionPerformed(ActionEvent e) {
            if (!listen) return;

            if (e.getSource() == cancelButton || e.getActionCommand().equals("Canel")) {
                cancelButtonPressed();
            } else if (e.getSource() == backButton || e.getActionCommand().equals("Back")) {
                backButtonPressed();
            } else if (e.getSource() == nextButton || e.getActionCommand().equals("Next")) {
                nextButtonPressed();
            } else if (e.getSource() == finishButton || e.getActionCommand().equals("Finish")) {
                finishButtonPressed();
            } else {
                syncButtonsToPage();
            }
        }

        private void cancelButtonPressed() {
            close(CANCEL);
        }

        private void finishButtonPressed() {
            // If it is a finishable panel, close down the dialog. Otherwise,
            // get the ID that the current panel identifies as the next panel,
            // and display it.
            if (current == null) {
                // we are lost - no page has been registered
                close(ERROR);
                return;
            }
            if (!current.isValid()) {
                syncButtonsToPage();
                return; // not valid so we cannot go on
            }
            current.preClosePanel();
            close(FINISH);
        }

        private void nextButtonPressed() {
            // get the ID that the current panel identifies as the next panel,
            // and display it.
            if (current == null) {
                // we are lost - no page has been registered
                close(ERROR);
                return;
            }
            if (!current.isValid()) {
                syncButtonsToPage();
                return; // not valid so we cannot go on
            }
            String nextId = current.getNextPageIdentifier();
            setCurrentPanel(nextId);
        }

        private void backButtonPressed() {
            if (current == null) {
                // we are lost - no page has been registered
                close(ERROR);
                return;
            }
            String backId = current.getBackPageIdentifier();
            setCurrentPanel(backId);
        }
        /**
         * Set listen to false to update a field without the controller passing on a notification.
         */
        public void setListen(boolean listen) {
            this.listen = listen;
        }

        public void syncButtonsToPage() {
            String backPageId = current.getBackPageIdentifier();
            String nextPageId = current.getNextPageIdentifier();
            boolean isValid = current.isValid();

            setBackEnabled(backPageId != null);
            if (nextPageId == null) {
                // next page has not been sorted out yet
                setNextEnabled(false);
                setFinishEnabled(false);
            } else if (nextPageId == JPage.FINISH) {
                setNextEnabled(false);
                setFinishEnabled(isValid);
            } else {
                setNextEnabled(isValid);
                setFinishEnabled(false);
            }
        }

        public void keyPressed(KeyEvent e) {
            // ignore
        }

        public void keyReleased(KeyEvent e) {
            syncButtonsToPage();
        }

        public void keyTyped(KeyEvent e) {
            syncButtonsToPage();
        }

        public void changedUpdate(DocumentEvent e) {
            syncButtonsToPage();
        }

        public void insertUpdate(DocumentEvent e) {
            syncButtonsToPage();
        }

        public void removeUpdate(DocumentEvent e) {
            syncButtonsToPage();
        }

        public void valueChanged(ListSelectionEvent e) {
            syncButtonsToPage();
        }
    }
}
