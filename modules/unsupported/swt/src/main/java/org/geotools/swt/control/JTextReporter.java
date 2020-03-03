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

package org.geotools.swt.control;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.geotools.swt.utils.Utils;

/**
 * A dialog to display text reports to the user and, if requested, save them to file.
 *
 * @author Michael Bedward
 * @since 2.6
 * @version $URL$
 */
public class JTextReporter extends Dialog {

    /** Default number of rows shown in the text display area's preferred size */
    private Text textArea;

    private final String title;

    /**
     * Creates a new JTextReporter with the following default options:
     *
     * <ul>
     *   <li>Remains on top of other application windows
     *   <li>Is not modal
     *   <li>Will be disposed of when closed
     * </ul>
     *
     * @param title title for the dialog (may be {@code null})
     * @param rows number of text rows displayed without scrolling (if zero or negative, the default
     *     is used)
     * @param cols number of text columns displayed without scrolling (if zero or negative the
     *     default is used)
     */
    public JTextReporter(Shell parent, String title) {
        super(parent);
        this.title = title;

        setShellStyle(SWT.CLOSE | SWT.MODELESS | SWT.BORDER | SWT.TITLE);
        setBlockOnOpen(false);
    }

    @Override
    protected void configureShell(Shell newShell) {
        newShell.setText(title);
        newShell.setSize(250, 400);

        super.configureShell(newShell);
    }

    /**
     * Append text to the report being displayed. No additional line feeds are added after the text.
     *
     * <p>If called from other than the AWT event dispatch thread this method puts the append task
     * onto the dispatch thread and waits for its completion.
     *
     * @param text the text to be appended to the report
     */
    public synchronized void append(final String text) {
        Runnable runner =
                new Runnable() {
                    public void run() {
                        StringBuilder sb = new StringBuilder();
                        sb.append(textArea.getText());
                        sb.append("\n");
                        sb.append(text);
                        textArea.setText(sb.toString());
                        textArea.setSelection(textArea.getCharCount());
                    }
                };
        Utils.runGuiRunnableSafe(runner, true);
    }

    protected Control createDialogArea(Composite parent) {
        parent.setLayout(new GridLayout(1, false));

        textArea = new Text(parent, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
        textArea.setEditable(false);
        GridData textAreaGD = new GridData(SWT.FILL, SWT.FILL, true, true);
        textArea.setLayoutData(textAreaGD);

        Composite buttonComposite = new Composite(parent, SWT.NONE);
        buttonComposite.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));
        buttonComposite.setLayout(new GridLayout(2, true));

        Button saveButton = new Button(buttonComposite, SWT.PUSH);
        saveButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        saveButton.setText("Save");
        saveButton.addSelectionListener(
                new SelectionAdapter() {
                    public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                        saveReport();
                    };
                });

        Button clearButton = new Button(buttonComposite, SWT.PUSH);
        clearButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        clearButton.setText("Clear");
        clearButton.addSelectionListener(
                new SelectionAdapter() {
                    public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                        clearReport();
                    };
                });

        return parent;
    }

    @Override
    protected Button createButton(Composite parent, int id, String label, boolean defaultButton) {
        return null;
    }

    /** Clear the report currently displayed */
    private void clearReport() {
        textArea.setText("");
    }

    private void saveReport() {
        Writer writer = null;
        try {
            File file = getFile();
            if (file != null) {
                writer = new BufferedWriter(new FileWriter(file));
                writer.write(textArea.getText());
            }
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
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

    private File getFile() {
        FileDialog fileDialog = new FileDialog(textArea.getShell(), SWT.SAVE);
        String path = fileDialog.open();
        if (path == null || path.length() < 1) {
            return null;
        }
        return new File(path);
    }
}
