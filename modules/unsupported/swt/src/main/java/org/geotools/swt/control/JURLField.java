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
package org.geotools.swt.control;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;
import org.geotools.data.DataUtilities;
import org.geotools.data.FileDataStoreFactorySpi;
import org.geotools.data.Parameter;

/**
 * Widget for URL; provides a "Browse" button to open a file dialog.
 * 
 * @author Jody Garnett
 * @author Andrea Antonello (www.hydrologis.com)
 */
public class JURLField extends ParamField {

    private Text field;

    private Button browse;

    public JURLField( Composite parent, Parameter< ? > parameter ) {
        super(parent, parameter);
    }

    public Control doLayout() {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        GridLayout gridLayout = new GridLayout(2, false);
        gridLayout.marginWidth = 0;
        composite.setLayout(gridLayout);

        field = new Text(composite, SWT.SINGLE | SWT.LEAD | SWT.BORDER);
        field.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        browse = new Button(composite, SWT.PUSH);
        browse.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        browse.setText("...");
        browse.addSelectionListener(new SelectionAdapter(){
            public void widgetSelected( SelectionEvent e ) {
                browse();
            }
        });
        return composite;
    }

    protected void browse() {
        FileDialog dialog;
        Object format = this.parameter.metadata.get(Parameter.EXT);
        if (format instanceof FileDataStoreFactorySpi) {
            JFileDataStoreChooser tmpdialog = new JFileDataStoreChooser(parent.getShell(), SWT.NONE,
                    (FileDataStoreFactorySpi) format);
            dialog = tmpdialog.getFileDialog();
        } else if (format instanceof String) {
            JFileDataStoreChooser tmpdialog = new JFileDataStoreChooser(parent.getShell(), SWT.NONE, (String) format);
            dialog = tmpdialog.getFileDialog();
        } else if (format instanceof String[]) {
            JFileDataStoreChooser tmpdialog = new JFileDataStoreChooser(parent.getShell(), SWT.NONE, (String[]) format);
            dialog = tmpdialog.getFileDialog();
        } else {
            dialog = new FileDialog(parent.getShell(), SWT.NONE);
        }
        dialog.setFileName(getFile().getAbsolutePath());

        String returnVal = dialog.open();
        if (returnVal != null && returnVal.length() >= 1) {
            setValue(new File(returnVal));
        }
    }

    public URL getValue() {
        String txt = field.getText();
        if (txt == null || txt.equals("")) {
            return null;
        }
        try {
            File file = new File(txt);
            return file.toURI().toURL();
        } catch (Exception e) {
        }
        try {
            return new URL(txt);
        } catch (MalformedURLException e) {
        }
        return null;
    }

    public File getFile() {
        String txt = field.getText();
        if (txt == null || txt.equals("")) {
            return null;
        }
        try {
            File file = new File(txt);
            return file;
        } catch (Exception e) {
        }
        try {
            URL url = new URL(txt);
            return DataUtilities.urlToFile(url);
        } catch (MalformedURLException e) {
        }
        return null; // not a file
    }

    public void setValue( Object value ) {
        if (value instanceof File) {
            File file = (File) value;
            field.setText(file.toString());
        } else if (value instanceof URL) {
            URL url = (URL) value;
            field.setText(url.toExternalForm());
        } else if (value instanceof String) {
            field.setText((String) value);
        }
    }

    public boolean validate() {
        String txt = field.getText();
        if (txt == null || txt.equals("")) {
            return !parameter.required;
        }
        File file = getFile();
        if (file != null) {
            return file.exists();
        }
        URL url = getValue();

        return url != null;
    }

}
