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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.geotools.swing.locale.LocaleUtils;

/**
 * Displays an {@code Exception} to the user in a modal dialog. This class is not a Swing
 * component itself, rather it provides static {@code showDialog} methods to create and
 * display dialogs safely from any thread.
 * <p>
 * 
 * Example of use:
 * 
 * <pre><code>
 * try {
 * 
 *     // ...something awful happens in here...
 * 
 * } catch (SomeException ex) {
 *     JExceptionReporter.showDialog(ex, "Bummer, it failed again");
 * }
 * </code></pre>
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
public class JExceptionReporter {
    
    // Hidden constructor.
    private JExceptionReporter() {}

    
    /**
     * Displays an exception in a dialog where the title is the 
     * exception class name and the body of the dialog shows the 
     * exception message.
     * <p>
     * It is safe to call this method from any thread.
     * 
     * @param exception exception to display
     */
    public static void showDialog(Throwable exception) {
        showDialog(exception, null);
    }
    
    /**
     * Displays an exception in a dialog where the title is the 
     * exception class name and the body of the dialog shows the 
     * given message.
     * <p>
     * It is safe to call this method from any thread.
     * 
     * @param exception exception to display
     * @param message message to display; if {@code null} or empty the 
     *     message will be taken from the exception
     */
    public static void showDialog(final Throwable exception, final String message) {
        if (exception == null) {
            throw new IllegalArgumentException("exception must not be null");
        }

        if (SwingUtilities.isEventDispatchThread()) {
            doShowDialog(exception, message);

        } else {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    doShowDialog(exception, message);
                }
            });
        }
    }
    
    private static void doShowDialog(Throwable exception, String message) {
        String title = exception.getClass().getSimpleName();
        
        if (empty(message)) {
            message = exception.getLocalizedMessage();
            
            if (empty(message)) {
                message = LocaleUtils.getValue("Common", "UnspecifiedError");
            }
        }
        
        ReportingDialog dialog = new ReportingDialog(title, message);
        DialogUtils.showCentred(dialog);
    }
    
    private static boolean empty(String s) {
        return s == null || s.trim().length() == 0;
    }


    /**
     * The dialog used to display the {@code Exception}. It is package-private
     * rather than private to enable unit tests.
     */
    static class ReportingDialog extends AbstractSimpleDialog {
        private static final int DEFAULT_WIDTH = 400;
        private static final int DEFAULT_HEIGHT = 200;
        private static final int MARGIN = 5;
        
        private final String message;

        private ReportingDialog(String title, String message) {
            super(title);
            this.message = message;
            
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            initComponents();
        }

        @Override
        public JPanel createControlPanel() {
            JPanel panel = new JPanel();
            panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK),
                    BorderFactory.createEmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN)));
            
            String text = String.format("<html>%s</html>", message);
            int w = DEFAULT_WIDTH - 2 * MARGIN;
            Dimension dim = DialogUtils.getHtmlLabelTextExtent(text, w, true);
            
            JLabel label = new JLabel(text);
            label.setPreferredSize(dim);
            panel.add(label);
            
            panel.setPreferredSize(new Dimension(DEFAULT_WIDTH, dim.height + 4*MARGIN ));
            return panel;
        }

        @Override
        protected JPanel createButtonPanel() {
            JPanel panel = new JPanel();
            
            JButton button = new JButton("Close");
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onOK();
                }
            });
            
            panel.add(button);
            return panel;
        }

        @Override
        public void onOK() {
            closeDialog();
        }
        
    }
    
}
