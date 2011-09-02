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
package org.geotools.swing.process;

import java.awt.GridLayout;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.geotools.data.Parameter;
import org.geotools.process.Process;
import org.geotools.process.ProcessFactory;
import org.geotools.swing.ProgressWindow;
import org.geotools.swing.wizard.JDoubleField;
import org.geotools.swing.wizard.JField;
import org.geotools.swing.wizard.JGeometryField;
import org.geotools.swing.wizard.JPage;
import org.geotools.swing.wizard.ParamField;
import org.geotools.text.Text;
import org.opengis.feature.type.Name;
import org.opengis.util.ProgressListener;

import com.vividsolutions.jts.geom.Geometry;

/**
 * This page is responsible for actually executing the process with the given parameters and then
 * displaying the result.
 * 
 * @author gdavis
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
public class ProcessRunPage extends JPage {
    ProcessFactory factory;
    Name name;

    Map<String, Object> paramMap;

    public ProcessRunPage(ProcessFactory factory) {
        this(factory, null);
    }

    public ProcessRunPage(ProcessFactory factory, Map<String, Object> params) {
        super("Run Process");
        this.factory = factory;
        this.paramMap = params;
    }

    public String getBackPageIdentifier() {
        if (this.factory == null) {
            ProcessSelectionPage selectionPage = new ProcessSelectionPage();
            this.getJWizard().registerWizardPanel(selectionPage);
            return selectionPage.getPageIdentifier();
        }
        ProcessParameterPage inputPage = new ProcessParameterPage(this.factory);
        this.getJWizard().registerWizardPanel(inputPage);
        return inputPage.getPageIdentifier();
    }

    public String getNextPageIdentifier() {
        return FINISH;
    }

    public void aboutToDisplayPanel() {
        JPanel page = getPanel();
        page.removeAll();
        page.setLayout(new GridLayout(0, 2));

        Process process = this.factory.create(name);

        final ProgressListener progress = new ProgressWindow(this.getJWizard());
        Map<String, Object> resultMap = process.execute(paramMap, progress);

        // when we get here, the processing is over so show the result
        JLabel title = new JLabel(factory.getTitle().toString());
        page.add(title);
        JLabel description = new JLabel("Your process results are below:");
        page.add(description);
        for (Entry<String, Object> entry : resultMap.entrySet()) {
            Parameter<?> parameter = new Parameter(entry.getKey(), entry.getValue().getClass(),
                    Text.text("Result"), Text.text("Result of process"));
            JLabel label = new JLabel(entry.getKey());
            page.add(label);

            ParamField widget;
            if (Double.class.isAssignableFrom(parameter.type)) {
                widget = new JDoubleField(parameter);
            } else if (Geometry.class.isAssignableFrom(parameter.type)) {
                widget = new JGeometryField(parameter);
            } else {
                // We got nothing special, let's hope the converter api can deal
                widget = new JField(parameter);
            }
            JComponent field = widget.doLayout();
            widget.setValue(entry.getValue());
            page.add(field);

        }
    }
}