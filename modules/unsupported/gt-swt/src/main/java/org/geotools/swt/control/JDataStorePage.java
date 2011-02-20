/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.geotools.data.DataAccessFactory.Param;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.Parameter;

/**
 * Data store wizard page for the {@link JDataStoreWizard data store wizard}.
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 */
public class JDataStorePage extends WizardPage {
    public static final String ID = "org.geotools.swt.data.DataStorePage";

    /**
     * Factory for which we are collection connection parameters
     */
    protected DataStoreFactorySpi format;

    /** Map of user interface ParamFields displayed to the user */
    private Map<Param, ParamField> fields = new HashMap<Param, ParamField>();

    /** Connection params for datastore */
    protected Map<String, Object> connectionParameters;

    /** level of parameters to display */
    private String level = null;

    public JDataStorePage( DataStoreFactorySpi format ) {
        this(format, null);
    }

    public JDataStorePage( DataStoreFactorySpi format, Map<String, Object> params ) {
        super(ID);
        setTitle(format.getDisplayName());
        setDescription(format.getDescription());

        this.format = format;
        if (params == null) {
            params = new HashMap<String, Object>();
            if (format != null) {
                for( Param param : format.getParametersInfo() ) {
                    params.put(param.key, (Serializable) param.sample);
                }
            }
        }
        this.connectionParameters = params;
    }

    public void setVisible( boolean visible ) {
        if (visible) {
            preDisplayPanel();
        } else {
            preClosePanel();
        }

        super.setVisible(visible);
    }

    public void createControl( Composite parent ) {

        Composite mainComposite = new Composite(parent, SWT.NONE);
        GridLayout gridLayout = new GridLayout(2, false);
        mainComposite.setLayout(gridLayout);

        for( Param param : format.getParametersInfo() ) {
            if (level != null) {
                String check = param.metadata == null ? "user" : (String) param.metadata.get(Parameter.LEVEL);

                if (check == null) {
                    check = "user";
                }
                if (level.equals(check)) {
                    // we are good this is the one we want
                } else {
                    continue; // skip since it is not the one we want
                }
            }
            String txt = param.title.toString();
            if (param.required) {
                txt += "*";
            }

            Label label = new Label(mainComposite, SWT.NONE);
            label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
            label.setText(txt);

            ParamField field = ParamField.create(mainComposite, param);
            field.doLayout();

            fields.put(param, field);

            // if (param.description != null) {
            // JLabel info = new JLabel(formatDescription(param.description.toString()));
            // page.add(info, "skip, span, wrap");
            // }
        }

        setControl(mainComposite);

    }

    private void preDisplayPanel() {
        // populate panel from params map
        for( Entry<Param, ParamField> entry : fields.entrySet() ) {
            Param param = entry.getKey();
            ParamField field = entry.getValue();
            Object value = null;
            try {
                value = param.lookUp(connectionParameters);
            } catch (IOException e) {
            }
            if (value == null && param.required) {
                value = param.sample;
            }
            field.setValue(value);
        }
        // for( Entry<Param, ParamField> entry : fields.entrySet() ) {
        // ParamField field = entry.getValue();
        // TODO field.addListener(getWizard().getController());
        // }
    }

    private void preClosePanel() {
        for( Entry<Param, ParamField> entry : fields.entrySet() ) {
            Param param = entry.getKey();
            ParamField field = entry.getValue();

            Object value = field.getValue();
            connectionParameters.put(param.key, (Serializable) value);
            field.setValue(value);
        }
        // for( Entry<Param, ParamField> entry : fields.entrySet() ) {
        // ParamField field = entry.getValue();
        // TODO field.removeListener(getJWizard().getController());
        // }
    }

    public void setLevel( String level ) {
        this.level = level;
    }

    public void setFormat( DataStoreFactorySpi format ) {
        if (this.format != format) {
            this.format = format;
        }
    }

    public boolean isValid() {
        // populate panel
        for( Entry<Param, ParamField> entry : fields.entrySet() ) {
            if (!entry.getValue().validate()) {
                return false; // not validate
            }
            if (entry.getKey().required && entry.getValue().getValue() == null) {

            }
        }
        return true;
    }

    // private String formatDescription( String desc ) {
    // String prefix = "<html>";
    // final int LEN = desc.length();
    //
    // if (LEN < MAX_DESCRIPTION_WIDTH) {
    // return prefix + desc;
    // } else {
    // StringBuffer sb = new StringBuffer(prefix);
    // StringTokenizer tokenizer = new StringTokenizer(desc);
    //
    // int n = 0;
    // while( tokenizer.hasMoreTokens() ) {
    // String word = tokenizer.nextToken();
    // if (n + word.length() + 1 > MAX_DESCRIPTION_WIDTH) {
    // sb.append("<br>");
    // n = 0;
    // }
    // sb.append(word);
    // sb.append(' ');
    // n += word.length() + 1;
    // }
    //
    // return sb.toString();
    // }
    // }
}
