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
package org.geotools.swing.process;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.geotools.process.ProcessFactory;
import org.geotools.process.Processors;
import org.geotools.swing.wizard.JPage;
import org.opengis.feature.type.Name;
import org.opengis.util.InternationalString;

/**
 * This page is responsible for making a process selection widget that moves onto to the selected
 * process page.
 *
 * @author gdavis (Refractions)
 * @since 8.0
 * @version $Id$
 */
public class ProcessSelectionPage extends JPage {
    /**
     * This is an initial set of input parameters; which can be used to help find an initial
     * "matching" process.
     */
    Map<String, Object> input;

    /** List of available processes */
    JTree processList;

    /** This set of process factories avaialble (mostly an internal detail). */
    Set<ProcessFactory> processFactories;

    /** Description of the current process */
    JTextField descLabel;

    /** The currently selected factory responsible for describing a process */
    ProcessFactory selectedFactory;

    static final String defaultDesc = "Select a process to see its description";

    public ProcessSelectionPage() {
        this(null);
    }

    public ProcessSelectionPage(Map<String, Object> input) {
        super("process selection");
        this.input = input;
        processFactories = Processors.getProcessFactories();
        selectedFactory = null;
    }

    public String getBackPageIdentifier() {
        return null;
    }

    public String getNextPageIdentifier() {
        if (selectedFactory == null) {
            return null;
        }
        ProcessParameterPage inputPage = new ProcessParameterPage(selectedFactory);
        this.getJWizard().registerWizardPanel(inputPage);
        return inputPage.getPageIdentifier();
    }

    public void aboutToDisplayPanel() {
        JPanel page = getPanel();
        page.removeAll();
        GridBagLayout gridBag = new GridBagLayout();
        page.setLayout(gridBag);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipadx = 5;
        c.ipady = 5;

        JLabel title = new JLabel("Process Selection");
        title.setFont(new Font("Arial", Font.BOLD, 14));
        c.gridx = 0;
        c.gridy = 0;
        gridBag.setConstraints(title, c);
        page.add(title);

        JLabel description = new JLabel("Select a Process and click 'Next'");
        description.setFont(new Font("Arial", Font.PLAIN, 11));
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.ipady = 20;
        gridBag.setConstraints(description, c);
        page.add(description);

        JLabel label1 = new JLabel("Process:");
        label1.setFont(new Font("Arial", Font.BOLD, 12));
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.ipady = 5;
        gridBag.setConstraints(label1, c);
        page.add(label1);

        JLabel label2 = new JLabel("Process Description:");
        label2.setFont(new Font("Arial", Font.BOLD, 12));
        c.gridx = 1;
        c.gridy = 2;
        gridBag.setConstraints(label2, c);
        page.add(label2);

        TreeModel data = createFactoryTitleArray(processFactories);
        processList = new JTree(data);

        processList.setFont(new Font("Arial", Font.PLAIN, 12));
        processList.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        processList
                .getSelectionModel()
                .addTreeSelectionListener(
                        new TreeSelectionListener() {
                            @Override
                            public void valueChanged(TreeSelectionEvent e) {
                                TreePath path = e.getNewLeadSelectionPath();
                                if (path.getLastPathComponent() instanceof Name) {
                                    Name name = (Name) path.getLastPathComponent();
                                    ProcessFactory factory =
                                            (ProcessFactory)
                                                    path.getParentPath().getLastPathComponent();
                                    updateProcessDesc(factory, name);
                                }
                            }
                        });
        c.gridx = 0;
        c.gridy = 3;
        gridBag.setConstraints(processList, c);
        page.add(processList);

        descLabel = new JTextField(defaultDesc, 35);
        descLabel.setEditable(false);
        Border border = new LineBorder(this.getPanel().getBackground(), 0);
        descLabel.setBorder(border);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        c.gridx = 1;
        c.gridy = 3;
        gridBag.setConstraints(descLabel, c);
        page.add(descLabel);

        /*
         * ParamWidget widget; widget = new JField( parameter ); JComponent field =
         * widget.doLayout(); page.add(processList); fields.put( parameter.key, widget );
         */
    }

    /**
     * Populates an array of strings with the process factory titles based on the factory set
     *
     * @param factories the string array to populate
     */
    private TreeModel createFactoryTitleArray(Set<ProcessFactory> factories) {
        final List<ProcessFactory> root = new ArrayList<ProcessFactory>();
        final Map<ProcessFactory, List<Name>> branch = new HashMap<ProcessFactory, List<Name>>();

        root.addAll(factories);
        Collections.sort(
                root,
                new Comparator<ProcessFactory>() {

                    @Override
                    public int compare(ProcessFactory o1, ProcessFactory o2) {
                        String s1 = o1.getTitle().toString();
                        String s2 = o2.getTitle().toString();

                        return s1.compareTo(s2);
                    }
                });
        return new TreeModel() {
            @Override
            public Object getRoot() {
                return root;
            }

            List<Name> getChildren(ProcessFactory factory) {
                synchronized (factory) {
                    List<Name> list = branch.get(factory);
                    if (list == null) {
                        list = new ArrayList<Name>();
                        list.addAll(factory.getNames());
                        Collections.sort(
                                list,
                                new Comparator<Name>() {
                                    @Override
                                    public int compare(Name o1, Name o2) {
                                        String s1 = o1.toString();
                                        String s2 = o2.toString();
                                        return s1.compareTo(s2);
                                    }
                                });
                        branch.put(factory, list);
                    }
                    return list;
                }
            }

            @Override
            public Object getChild(Object parent, int index) {
                if (parent == root) {
                    return root.get(index);
                } else if (parent instanceof ProcessFactory) {
                    ProcessFactory factory = (ProcessFactory) parent;
                    return getChildren(factory).get(index);
                }
                return null;
            }

            @Override
            public int getChildCount(Object parent) {
                if (parent == root) {
                    return root.size();
                } else if (parent instanceof ProcessFactory) {
                    ProcessFactory factory = (ProcessFactory) parent;
                    return getChildren(factory).size();
                }
                return 0;
            }

            @Override
            public boolean isLeaf(Object node) {
                if (node == root) {
                    return false;
                } else if (node instanceof ProcessFactory) {
                    return false;
                } else if (node instanceof Name) {
                    return true;
                }
                return true;
            }

            @Override
            public void valueForPathChanged(TreePath path, Object newValue) {
                // our tree is not editable
            }

            @Override
            public int getIndexOfChild(Object parent, Object child) {
                if (parent == root) {
                    return root.indexOf(child);
                } else if (parent instanceof ProcessFactory) {
                    ProcessFactory factory = (ProcessFactory) parent;
                    return getChildren(factory).indexOf(child);
                }
                return 0;
            }

            @Override
            public void addTreeModelListener(TreeModelListener l) {
                // our tree is not editable
            }

            @Override
            public void removeTreeModelListener(TreeModelListener l) {
                // our tree is not editable
            }
        };
    }

    /** Update the process description based on the selected process */
    private void updateProcessDesc(ProcessFactory factory, Name name) {
        if (name == null || factory == null) {
            return;
        }
        InternationalString title = factory.getTitle(name);
        InternationalString description = factory.getDescription(name);
        descLabel.setText(defaultDesc);
        selectedFactory = factory;
        updateNavButtons();
        descLabel.setText("<html><b>" + title + "</b>" + description);
        updateNavButtons();
    }

    /**
     * Update the wizard nav buttons based on what process factory is selected and if the form is
     * validated to move to the next page or not
     */
    private void updateNavButtons() {
        this.getJWizard().getController().syncButtonsToPage();
    }
}
