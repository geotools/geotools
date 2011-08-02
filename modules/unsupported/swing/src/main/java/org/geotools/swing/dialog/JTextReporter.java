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

import java.awt.Color;
import java.awt.EventQueue;
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
import java.util.Arrays;
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
 * A simple dialog to display text with the option of saving it to file. Remains
 * on top of other application windows but does not block them.
 * <p>
 * Example of use:
 * <pre><code>
 * // This code can be run safely on or off the event dispatch thread
 * JTextReporter reporter = JTextReporter.create(
 *         "Important message", 
 *         -1, -1,  // for default number of rows and columns
 *         "The gt-swing module is particularly useful");
 *
 * DialogUtils.showCentred( reporter );
 * </code></pre>
 *
 * @author Michael Bedward
 * @since 2.6
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
    
    /**
     * Default character to use for the {@linkplain #separator()} method. 
     */
    public static final char DEFAULT_SEPARATOR_CHAR = '-';

    private int rows;
    private int cols;

    private JTextArea textArea;

    /* current working directory - for multiple saves */
    private File cwd;

    /* system-independent line separator */
    private static String lineSep = System.getProperty("line.separator");

    private List<TextReporterListener> listeners;

    /**
     * Creates a new text reporter. It is better to use this method than
     * construct JTextReporter instances directly because it can be called
     * safely from any thread.
     *
     * @param title dialog title
     *
     * @return new text reporter
     */
    public static JTextReporter create(String title) {
        return create(title, DEFAULT_ROWS, DEFAULT_COLS, null);
    }

    /**
     * Creates a new text reporter. It is better to use this method than
     * construct JTextReporter instances directly because it can be called
     * safely from any thread.
     *
     * @param title dialog title
     * @param rows number of text area rows
     * @param cols number of text area cols
     *
     * @return new text reporter
     */
    public static JTextReporter create(String title, int rows, int cols) {
        return create(title, rows, cols, null);
    }

    /**
     * Creates a new text reporter. It is better to use this method than
     * construct JTextReporter instances directly because it can be called
     * safely from any thread.
     *
     * @param title dialog title
     * @param rows number of text area rows
     * @param cols number of text area cols
     * @param text initial text (may be {@code null})
     *
     * @return new text reporter
     */
    public static JTextReporter create(final String title,
            final int rows, final int cols, final String text) {

        final JTextReporter[] reporter = new JTextReporter[1];

        if (SwingUtilities.isEventDispatchThread()) {
            reporter[0] = doCreate(title, rows, cols, text);
        } else {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        reporter[0] = doCreate(lineSep, rows, cols, text);
                    }
                });
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        return reporter[0];
    }

    /**
     * Helper for static {@code create} methods. This should only be called
     * from the event dispatch thread.
     *
     * @param title dialog title
     * @param rows number of text area rows
     * @param cols number of text area cols
     * @param text initial text (may be {@code null})
     *
     * @return new text reporter
     */
    private static JTextReporter doCreate(String title, int rows, int cols, String text) {
        JTextReporter reporter = new JTextReporter(title, rows, cols);
        if (text != null) {
            reporter.append(text);
        }
        return reporter;
    }

    /**
     * Creates a new text reporter. Client code should generally use
     * {@linkplain #create(String)} instead which is safe to call from any
     * thread.
     *
     * @param title dialog title
     */
    public JTextReporter(String title) {
        this(title, -1, -1);
    }

    /**
     * Creates a new text reporter. Client code should generally use
     * {@linkplain #create(String, int, int)} instead which is safe to call from any
     * thread.
     *
     * @param title dialog title
     * @param rows number of text area rows
     * @param cols number of text area cols
     */
    public JTextReporter(String title, int rows, int cols) {
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
     * Adds an event listener. Does nothing if the listener is already
     * registered.
     *
     * @param listener the listener
     * @throws IllegalArgumentException if {@code listener} ir {@code null}
     */
    public void addListener(TextReporterListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener must not be null");
        }

        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * Appends the given text to that displayed. No additional newlines
     * are added after the text.
     * 
     * @param text the text to append
     */
    public synchronized void append(final String text) {
        append(text, 0);
    }
    
    /**
     * Appends the given text to that displayed. No additional newlines
     * are added after the text.
     * 
     * @param text the text to append
     * @param indent indent width as number of spaces
     */
    public synchronized void append(final String text, final int indent) {
        if (EventQueue.isDispatchThread()) {
            doAppend(text, indent);

        } else {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        doAppend(text, indent);
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
     * Creates the interface.
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
            @Override
            public void actionPerformed(ActionEvent e) {
                saveReport();
            }
        });
        btnPanel.add(saveBtn);

        JButton clearBtn = new JButton("Clear");
        clearBtn.addActionListener(new ActionListener() {
            @Override
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
     * Helper method for {@linkplain #append(String)}.
     *
     * @param text the text to be appended
     * @param indent indent width as number of spaces
     */
    private void doAppend(final String text, final int indent) {
        int startLine = textArea.getLineCount();

        String appendText;
        if (indent > 0) {
            char[] c = new char[indent];
            Arrays.fill(c, ' ');
            String pad = String.valueOf(c);
            appendText = pad + text.replaceAll("\\n", "\n" + pad);
        } else {
            appendText = text;
        }
        
        textArea.append(appendText);
        textArea.setCaretPosition(textArea.getDocument().getLength());

        for (TextReporterListener listener : listeners) {
            listener.onReporterUpdated(startLine);
        }
    }

    /**
     * Clears the text area.
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

    /**
     * Saves text to file.
     */
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

    /**
     * Displays a file chooser dialog and returns the selected file.
     *
     * @return the selected file
     */
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

    /**
     * Appends a line of repeated {@link #DEFAULT_SEPARATOR_CHAR}
     * followed by a newline.
     */
    public void separator() {
        separator(DEFAULT_SEPARATOR_CHAR);
    }
    
    /**
     * Appends a line of repeated character {@code c}
     * followed by a newline.
     */
    public void separator(char c) {
        int n = textArea.getColumns() - 1;
        char[] carray = new char[n];
        Arrays.fill(carray, c);
        append(String.valueOf(carray));
        append("\n");
    }

}
