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

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

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

import org.geotools.util.logging.Logging;

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
 *
 * @source $URL$
 * @version $URL$
 */
public class JTextReporter {
    private static final Logger LOGGER = Logging.getLogger("org.geotools.swing");
    
    /**
     * Maximum permissable time for dialog creation.
     */
    private static final long DIALOG_CREATION_TIMEOUT = 1000;
    
    /**
     * Constant indicating that a text reporter should be displayed as a modal dialog.
     * Use with {@code showDialog} methods which take a {@code flags} argument.
     */
    public static final int FLAG_MODAL = 1;
    
    /**
     * Constant indicating that a text reporter should stay on top of other application windows.
     * Use with {@code showDialog} methods which take a {@code flags} argument.
     */
    public static final int FLAG_ALWAYS_ON_TOP = 1 << 1;
    
    /**
     * Constant indicating that a text reporter dialog should be resizable.
     * Use with {@code showDialog} methods which take a {@code flags} argument.
     */
    public static final int FLAG_RESIZABLE = 1 << 2;
    
    /**
     * Default flags argument for {@code showDialog} methods. 
     * Equivalent to {@code FLAG_ALWAYS_ON_TOP | FLAG_RESIZABLE}.
     */
    public static final int DEFAULT_FLAGS = FLAG_ALWAYS_ON_TOP | FLAG_RESIZABLE;
    
    /**
     * Default number of rows shown in the text display area's
     * preferred size
     */
    public static final int DEFAULT_TEXTAREA_ROWS = 20;

    /**
     * Default number of columns shown in the text display area's
     * preferred size
     */
    public static final int DEFAULT_TEXTAREA_COLS = 50;

    /**
     * System-dependent newline character(s).
     */
    public static String NEWLINE = System.getProperty("line.separator");
    
    /**
     * Default character to use for the {@linkplain Connection#appendSeparatorLine(int)} method. 
     */
    public static final char DEFAULT_SEPARATOR_CHAR = '-';

    
    /**
     * A connection to an active text reporter dialog providing methods to 
     * update the text displayed.
     */
    public static class Connection {

        private void fireEvent(EventType eventType) {
            for (TextReporterListener listener : listeners) {
                switch (eventType) {
                    case DIALOG_CLOSED:
                        listener.onReporterClosed();
                        break;
                        
                    case TEXT_UPDATED:
                        listener.onReporterUpdated();
                        break;
                }
            }
        }
        
        private static enum EventType {
            TEXT_UPDATED,
            DIALOG_CLOSED;
        }
        
        private final WeakReference<TextDialog> dialogRef;
        private final List<TextReporterListener> listeners;
        private final ReadWriteLock updateLock;

        /**
         * Private constructor.
         * 
         * @param dialog the dialog to connect to
         */
        private Connection(TextDialog dialog) {
            dialogRef = new WeakReference<TextDialog>(dialog);
            updateLock = new ReentrantReadWriteLock();
            listeners = new ArrayList<TextReporterListener>();
        }
        
        /**
         * Adds a listener.
         * 
         * @param listener the listener
         * @throws IllegalArgumentException if {@code listener} is {@code null}
         */
        public void addListener(TextReporterListener listener) {
            if (listener == null) {
                throw new IllegalArgumentException("listener must not be null");
            }
        
            updateLock.writeLock().lock();
            try {
                if (!listeners.contains(listener)) {
                    listeners.add(listener);
                }
            } finally {
                updateLock.writeLock().unlock();
            }
        }
        
        /**
         * Removes the listener if it is registered with this connection.
         * 
         * @param listener the listener
         * @throws IllegalArgumentException if {@code listener} is {@code null}
         */
        public void removeListener(TextReporterListener listener) {
            if (listener == null) {
                throw new IllegalArgumentException("listener must not be null");
            }
        
            updateLock.writeLock().lock();
            try {
                listeners.remove(listener);
                
            } finally {
                updateLock.writeLock().unlock();
            }
        }
        
        /**
         * Removes all currently registered listeners.
         */
        public void removeAllListeners() {
            updateLock.writeLock().lock();
            try {
                listeners.clear();
                
            } finally {
                updateLock.writeLock().unlock();
            }
        }

        public void append(String text) {
            append(text, 0);
        }

        public void append(final String text, final int indent) {
            updateLock.writeLock().lock();
            try {
                final TextDialog dialog = dialogRef.get();
                if (dialog == null) {
                    LOGGER.severe("Appending text to an expired JTextReporter connection");

                } else {
                    if (SwingUtilities.isEventDispatchThread()) {
                        dialog.append(text, indent);

                    } else {
                        doAppendOnEDT(dialog, text, indent);
                    }

                    fireEvent(EventType.TEXT_UPDATED);
                }
                
            }  finally {
                updateLock.writeLock().unlock();
            }
        }

        /**
         * Appends a line of repeated {@link #DEFAULT_SEPARATOR_CHAR}
         * followed by a newline.
         */
        public void appendSeparatorLine(int n) {
            appendSeparatorLine(n, DEFAULT_SEPARATOR_CHAR);
        }

        /**
         * Appends a line consisting of {@code n} copies of char {@code c}
         * followed by a newline.
         */
        public void appendSeparatorLine(int n, char c) {
            char[] carray = new char[n];
            Arrays.fill(carray, c);
            append(String.valueOf(carray));
            appendNewline();
        }
        
        /**
         * Appends a newline.
         */
        public void appendNewline() {
            append(NEWLINE);
        }

        private void doAppendOnEDT(final TextDialog dialog,
            final String text,
            final int indent) {

            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        dialog.append(text, indent);
                    }
                });

            } catch (InterruptedException ex) {
                LOGGER.severe("Interrupted while appending text");
            } catch (InvocationTargetException ex) {
                LOGGER.log(Level.SEVERE, "Unable to append text", ex);
            }
        }

        /**
         * Called by the associated TextDialog when it is closing
         */
        private void setDialogClosed() {
            updateLock.writeLock().lock();
            try {
                dialogRef.clear();
                fireEvent(EventType.DIALOG_CLOSED);
                
            } finally {
                updateLock.writeLock().unlock();
            }
        }
    }

    /**
     * Creates a displays a new text reporter dialog.
     * <p>
     * This method can be called safely from any thread.
     * 
     * @param title dialog title (may be {@code null} or empty
     * 
     * @return a {@linkplain Connection} via which the text displayed by the
     *     dialog can be updated
     */
    public static Connection showDialog(String title) {
        return showDialog(title, null);
    }
    
    /**
     * Creates a displays a new text reporter dialog.
     * <p>
     * This method can be called safely from any thread.
     * 
     * @param title dialog title (may be {@code null} or empty
     * @param initialText text to display initially (may be {@code null} or empty
     * 
     * @return a {@linkplain Connection} via which the text displayed by the
     *     dialog can be updated
     */
    public static Connection showDialog(String title, String initialText) {
        return showDialog(title, initialText, DEFAULT_FLAGS);
    }
    
    /**
     * Creates a displays a new text reporter dialog.
     * <p>
     * This method can be called safely from any thread.
     * 
     * @param title dialog title (may be {@code null} or empty
     * @param initialText text to display initially (may be {@code null} or empty
     * @param flags
     * 
     * @return a {@linkplain Connection} via which the text displayed by the
     *     dialog can be updated
     */
    public static Connection showDialog(String title, 
            String initialText,
            int flags) {
        return showDialog(title, initialText, flags, DEFAULT_TEXTAREA_ROWS, DEFAULT_TEXTAREA_COLS);
    }
    
    
    public static Connection showDialog(final String title, 
            final String initialText, 
            final int flags,
            final int textAreaRows, 
            final int textAreaCols) {
        
        final Connection[] conn = new Connection[1];
        
        if (SwingUtilities.isEventDispatchThread()) {
            conn[0] = doShowDialog(title, initialText, flags, textAreaRows, textAreaCols);
            
        } else {
            final CountDownLatch latch = new CountDownLatch(1);
            
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    conn[0] = doShowDialog(title, initialText, flags, textAreaRows, textAreaCols);
                    latch.countDown();
                }
            });
            
            try {
                latch.await(DIALOG_CREATION_TIMEOUT, TimeUnit.MILLISECONDS);
            } catch (InterruptedException ex) {
                LOGGER.severe("Thread interrupted while setting up text reporter");
                return null;
            }
        }
        
        return conn[0];
    }
    
    private static Connection doShowDialog(final String title, 
            final String initialText,
            final int flags,
            final int textAreaRows, 
            final int textAreaCols) {
        
        TextDialog dialog = new TextDialog(title, initialText, flags, textAreaRows, textAreaCols);
        Connection conn = new Connection(dialog);
        dialog.setConnection(conn);
        DialogUtils.showCentred(dialog);
        
        return conn;
    }

    
    static class TextDialog extends AbstractSimpleDialog {
        
        private final JTextArea textArea;
        
        /** Helper method for constructor. */
        private static boolean isFlagSet(int flags, int testFlag) {
            return (flags & testFlag) > 0;
        }
        
        /**
         * Private constructor.
         * @param title dialog title
         * @param initialText initial text to display
         * @param flags dialog state flags
         * @param textAreaRows number of text area rows
         * @param textAreaCols number of text area columns
         */
        private TextDialog(String title, 
                String initialText,
                int flags,
                int textAreaRows, 
                int textAreaCols) {
            
            super((JDialog) null, title, isFlagSet(flags, FLAG_MODAL), isFlagSet(flags, FLAG_RESIZABLE));
            setAlwaysOnTop(isFlagSet(flags, FLAG_ALWAYS_ON_TOP));
            
            textArea = new JTextArea(textAreaRows, textAreaCols);
            initComponents();
            
            if (initialText != null && initialText.length() > 0) {
                append(initialText);
            }
            
        }
        
        /**
         * Establishes a link between this dialog instance and a Connection instance.
         * 
         * @param conn the connection associated with this dialog
         */
        private void setConnection(final Connection conn) {
            /*
             * The dialog informs its Connection object when closing.
             * We override both windowClosing and windowClosed methods: the
             * former is called when the dialog is closed via the system button
             * and the latter when it is closed using the dialog Close button.
             */
            addWindowListener(new WindowAdapter() {
                private boolean flag = false;

                @Override
                public void windowClosing(WindowEvent e) {
                    if (!flag) {
                        conn.setDialogClosed();
                        flag = true;
                    }
                }

                @Override
                public void windowClosed(WindowEvent e) {
                    if (!flag) {
                        conn.setDialogClosed();
                        flag = true;
                    }
                }
            });
        }

        @Override
        public JPanel createControlPanel() {
            textArea.setEditable(false);
            textArea.setLineWrap(true);
            textArea.setAutoscrolls(true);
            JScrollPane scrollPane = new JScrollPane(textArea);

            JPanel panel = new JPanel(new MigLayout("wrap 1", "[grow]", "[grow][]"));
            panel.add(scrollPane, "grow");
            
            return panel;
        }

        @Override
        protected JPanel createButtonPanel() {
            JPanel panel = new JPanel(new MigLayout());

            JButton copyBtn = new JButton("Copy to clipboard");
            copyBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onCopyToClipboard();
                }
            });
            panel.add(copyBtn, "align center");
            
            JButton saveBtn = new JButton("Save...");
            saveBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onSave();
                }
            });
            panel.add(saveBtn);
            
            JButton clearBtn = new JButton("Clear");
            clearBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onClear();
                }
            });
            panel.add(clearBtn);
            
            JButton closeBtn = new JButton("Close");
            closeBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    closeDialog();
                }
            });
            panel.add(closeBtn);

            return panel;
        }
        
        @Override
        public void onOK() {
            closeDialog();
        }
        
        private void onCopyToClipboard() {
            if (textArea.getDocument().getLength() > 0) {
                StringSelection sel = new StringSelection(textArea.getText());
                Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
                clip.setContents(sel, sel);
            }
        }
        
        private void onSave() {
            int len = textArea.getDocument().getLength();
            if (len > 0) {
                Writer writer = null;
                try {
                    // allow the file chooser to be on top of this dialog
                    boolean alwaysOnTop = isAlwaysOnTop();
                    setAlwaysOnTop(false);
                    
                    File file = FileHelper.getFile();
                    
                    // restore normal setting
                    setAlwaysOnTop(alwaysOnTop);
                    
                    if (file != null) {
                        writer = new BufferedWriter(new FileWriter(file));
                        for (int line = 0; line < textArea.getLineCount(); line++) {
                            int start = textArea.getLineStartOffset(line);
                            int end = textArea.getLineEndOffset(line);
                            String lineText = textArea.getText(start, end - start);
                            if (lineText.endsWith("\n")) {
                                lineText = lineText.substring(0, lineText.length() - 1);
                            }
                            writer.write(lineText);
                            writer.write(NEWLINE);
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
         * Clears the current text.
         */
        private void onClear() {
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
        
        private void append(String text) {
            append(text, 0);
        }

        /**
         * Appends the given text to that displayed. No additional newlines
         * are added after the text.
         * 
         * @param text the text to append
         * @param indent indent width as number of spaces
         */
        void append(final String text, final int indent) {
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
        }
        
    }
    
    
    /**
     * Provides a file chooser dialog which remembers the previous directory.
     */
    static class FileHelper {
        /* current working directory - for multiple saves */
        private static File cwd;

        /**
         * Displays a file chooser dialog and returns the selected file.
         *
         * @return the selected file
         */
        static File getFile() {
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

            if (chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
                return null;
            }

            cwd = chooser.getCurrentDirectory();
            return chooser.getSelectedFile();
        }
        
    }

}
