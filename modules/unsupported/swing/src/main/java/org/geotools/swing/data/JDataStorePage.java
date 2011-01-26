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

package org.geotools.swing.data;

import java.awt.Font;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.Parameter;
import org.geotools.data.DataAccessFactory.Param;
import org.geotools.swing.wizard.JPage;
import org.geotools.swing.wizard.ParamField;

/**
 * A wizard page that will prompt the user for a file of the supplied format ask for any additional
 * information.
 * <p>
 * This page will allow the user to edit and modify the provided connectionParameters map
 * - but will only show parameters that match the indicated "level". If level is null it
 * assumed to be "user".
 *
 * @source $URL$
 */
public class JDataStorePage extends JPage {
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

    /** max line length of parameter description labels (chars) */
    private final int MAX_DESCRIPTION_WIDTH = 60;

    public JDataStorePage(DataStoreFactorySpi format) {
        this(format, null);
    }

    public JDataStorePage(DataStoreFactorySpi format, Map<String, Object> params) {
        this.format = format;
        if (params == null) {
            params = new HashMap<String, Object>();
            if( format != null ){
                for (Param param : format.getParametersInfo()) {
                    params.put(param.key, (Serializable) param.sample);
                }
            }
        }
        this.connectionParameters = params;
    }

    public void setLevel(String level) {
        this.level = level;
    }
    
    public void setFormat(DataStoreFactorySpi format) {
        if( this.format != format ){
            this.format = format;            
        }        
    }

    @Override
    public JPanel createPanel() {
        final JPanel page = super.createPanel();
        page.setLayout(new MigLayout());
        JLabel title = new JLabel(format.getDisplayName());
        Font titleFont = new Font("Arial", Font.BOLD, 14);
        title.setFont(titleFont);
        page.add(title, "span");
        JLabel description = new JLabel(format.getDescription());
        page.add(description, "grow, span");

        for (Param param : format.getParametersInfo()) {
            if( level != null ){
                String check = param.metadata == null ? "user" : (String) param.metadata.get(Parameter.LEVEL);
                
                if( check == null ){
                    check = "user";
                }
                if (level.equals( check )){
                    // we are good this is the one we want
                }
                else {
                    continue; // skip since it is not the one we want
                }
            }
            String txt = param.title.toString();
            if( param.required ){
                txt +="*";
            }
            JLabel label = new JLabel(txt);
            page.add(label);

            ParamField field = ParamField.create(param);
            JComponent component = field.doLayout();
            page.add(component, "span, wrap");

            fields.put(param, field);

            if (param.description != null) {
                JLabel info = new JLabel(formatDescription(param.description.toString()));
                page.add(info, "skip, span, wrap");
            }
        }
        return page;
    }

    @Override
    public void preDisplayPanel() {
        // populate panel from params map
        for (Entry<Param, ParamField> entry : fields.entrySet()) {
            Param param = entry.getKey();
            ParamField field = entry.getValue();
            Object value = null;
            try {
                value = param.lookUp(connectionParameters);
            } catch (IOException e) {
            }
            if( value == null && param.required ){
                value = param.sample;
            }
            field.setValue(value);
        }
        for (Entry<Param, ParamField> entry : fields.entrySet()) {
            ParamField field = entry.getValue();
            field.addListener(getJWizard().getController());
        }
    }

    @Override
    public void preClosePanel() {
        for (Entry<Param, ParamField> entry : fields.entrySet()) {
            Param param = entry.getKey();
            ParamField field = entry.getValue();

            Object value = field.getValue();
            connectionParameters.put(param.key, (Serializable) value);
            field.setValue(value);
        }
        for (Entry<Param, ParamField> entry : fields.entrySet()) {
            ParamField field = entry.getValue();
            field.removeListener(getJWizard().getController());
        }
    }

    @Override
    public boolean isValid() {
        // populate panel
        for (Entry<Param, ParamField> entry : fields.entrySet()) {
            if (!entry.getValue().validate()) {                
                return false; // not validate
            }
            if (entry.getKey().required && entry.getValue().getValue() == null) {

            }
        }
        return true;
    }


    private String formatDescription(String desc) {
        String prefix = "<html>";
        final int LEN = desc.length();

        if (LEN < MAX_DESCRIPTION_WIDTH) {
            return prefix + desc;
        } else {
            StringBuffer sb = new StringBuffer(prefix);
            StringTokenizer tokenizer = new StringTokenizer(desc);

            int n = 0;
            while (tokenizer.hasMoreTokens()) {
                String word = tokenizer.nextToken();
                if (n + word.length() + 1 > MAX_DESCRIPTION_WIDTH) {
                    sb.append("<br>");
                    n = 0;
                }
                sb.append(word);
                sb.append(' ');
                n += word.length() + 1;
            }

            return sb.toString();
        }
    }
}
