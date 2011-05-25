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

package org.geotools.swing;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.BadLocationException;

import net.miginfocom.swing.MigLayout;

/**
 * A dialog to display text reports to the user and, if requested,
 * save them to file.
 *
 * @author Michael Bedward
 * @since 2.6
 *
 * @source $URL$
 * @version $URL$
 */
public class JTextReporter extends JDialog {

    /**
     * Default number of rows shown in the text display area's
     * preferred size
     */
    public static final int DEFAULT_ROWS = 20;

    /**
     * Default number of columns shown in the text display area's
     * preferred size
     */
    public static final int DEFAULT_COLS = 50;

    private int rows;
    private int cols;

    private JTextArea textArea;

    /* current working directory - for multiple saves */
    private File cwd;

    /* system-independent line separator */
    private static String lineSep = System.getProperty("line.separator");

    private List<TextReporterListener> listeners;

    /**
     * Creates a new JTextReporter with the following default options:
     * <ul>
     * <li> Remains on top of other application windows
     * <li> Is not modal
     * <li> Will be disposed of when closed
     * </ul>
     *
     * @param title title for the dialog (may be {@code null})
     *
     * @throws java.awt.HeadlessException
     */
    public JTextReporter(String title) throws HeadlessException {
        this(title, -1, -1);
    }

    /**
     * Creates a new JTextReporter with the following default options:
     * <ul>
     * <li> Remains on top of other application windows
     * <li> Is not modal
     * <li> Will be disposed of when closed
     * </ul>
     *
     * @param title title for the dialog (may be {@code null})
     * @param rows number of text rows displayed without scrolling
     *        (if zero or negative, the default is used)
     * @param cols number of text columns displayed without scrolling
     *        (if zero or negative the default is used)
     *
     * @throws java.awt.HeadlessException
     */
    public JTextReporter(String title, int rows, int cols) throws HeadlessException {
        setTitle(title);
        this.rows = (rows >= 0 ? rows : DEFAULT_ROWS);
        this.cols = (cols >= 0 ? cols : DEFAULT_COLS);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        setModal(false);

        initComponents();

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(WindowEvent e) {
                for (TextReporterListener listener : listeners) {
                    listener.onReporterClosed(e);
                }
            }
        });

        listeners = new ArrayList<TextReporterListener>();
    }

    /**
     * Register an object that wishes to lisen to events published by this
     * report frame
     *
     * @param listener the listening object
     *
     * @return true if successfully registered; false otherwise (listener
     * already registered)
     *
     * @see TextReporterListener
     */
    public boolean addListener(TextReporterListener listener) {
        return listeners.add(listener);
    }

    /**
     * Append text to the report being displayed. No additional line
     * feeds are added after the text.
     * <p>
     * If called from other than the AWT event dispatch thread
     * this method puts the append task onto the dispatch thread
     * and waits for its completion.
     * 
     * @param text the text to be appended to the report
     */
    public synchronized void append(final String text) {
        if (EventQueue.isDispatchThread()) {
            doAppend(text);

        } else {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {

                    public void run() {
                        doAppend(text);
                    }
                });

            } catch (InterruptedException intEx) {
                return;

            } catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
        }
    }

    /**
     * Create and layout the components
     */
    private void initComponents() {
        textArea = new JTextArea(rows, cols);
        textArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setAutoscrolls(true);
        JScrollPane scrollPane = new JScrollPane(textArea);

        MigLayout layout = new MigLayout("wrap 1", "[grow]", "[grow][]");
        JPanel panel = new JPanel(layout);
        //Dimension size = textArea.getPreferredScrollableViewportSize();
        //System.out.println("preferred size" + size);
        //panel.add(scrollPane, String.format("grow, width %d, height %d", size.width, size.height));
        panel.add(scrollPane, "grow");
        
        JPanel btnPanel = new JPanel();

        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveReport();
            }
        });
        btnPanel.add(saveBtn);

        JButton clearBtn = new JButton("Clear");
        clearBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                clearReport();
            }
        });
        btnPanel.add(clearBtn);

        panel.add(btnPanel);
        getContentPane().add(panel);
        pack();
    }

    /**
     * Append text to the report being displayed in the text area.
     *
     * @param text the text to be appended
     */
    private void doAppend(final String text) {
        int startLine = textArea.getLineCount();

        textArea.append(text);
        textArea.setCaretPosition(textArea.getDocument().getLength());

        for (TextReporterListener listener : listeners) {
            listener.onReporterUpdated(startLine);
        }
    }

    /**
     * Clear the report currently displayed
     */
    private void clearReport() {
        int len = textArea.getDocument().getLength();
        if (len > 0) {
            try {
                textArea.getDocument().remove(0, len);
            } catch (BadLocationException ex) {
                // this shouldn't happen
                throw new IllegalStateException(ex);
            }
        }
    }

    private void saveReport() {
        int len = textArea.getDocument().getLength();
        if (len > 0) {
            Writer writer = null;
            try {
                File file = getFile();
                if (file != null) {
                    writer = new BufferedWriter( new FileWriter(file) );
                    for (int line = 0; line < textArea.getLineCount(); line++) {
                        int start = textArea.getLineStartOffset(line);
                        int end = textArea.getLineEndOffset(line);
                        String lineText = textArea.getText(start, end - start);
                        if (lineText.endsWith("\n")) {
                            lineText = lineText.substring(0, lineText.length()-1);
                        }
                        writer.write(lineText);
                        writer.write(lineSep);
                    }
                }

            } catch (IOException ex) {
                throw new IllegalStateException(ex);

            } catch (BadLocationException ex) {
                // this should never happen
                throw new IllegalStateException("Internal error getting report to save");
                
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException ex) {
                        // having a bad day
                    }
                }
            }
        }
    }

    private File getFile() {
        JFileChooser chooser = new JFileChooser(cwd);
        chooser.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                return true;
            }

            @Override
            public String getDescription() {
                return "All files";
            }
        });

        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return null;
        }

        cwd = chooser.getCurrentDirectory();
        return chooser.getSelectedFile();
    }

}
