/*
 *    uDig - User Friendly Desktop Internet GIS client
 *    http://udig.refractions.net
 *    (C) 2004, Refractions Research Inc.
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
 *
 */
package org.geotools.swt.control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.swt.utils.Messages;
import org.opengis.metadata.Identifier;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Creates a Control for choosing a Coordinate Reference System.
 *
 * @author jeichar
 * @since 0.6.0
 */
public class CRSChooser {

    private static final String WKT_ID = "WKT"; // $NON-NLS-1$
    private static final String ALIASES_ID = "ALIASES"; // $NON-NLS-1$
    private static final String LAST_ID = "LAST_ID"; // $NON-NLS-1$
    private static final String NAME_ID = "NAME_ID"; // $NON-NLS-1$
    private static final String CUSTOM_ID = "CRS.Custom.Services"; // $NON-NLS-1$
    private static final Controller DEFAULT =
            new Controller() {

                public void handleClose() {}

                public void handleOk() {}
            };

    ListViewer codesList;
    Text searchText;
    Text wktText;
    Text keywordsText;
    CoordinateReferenceSystem selectedCRS;
    Matcher matcher;
    private TabFolder folder;
    private Controller parentPage;
    private HashMap<String, String> crsCodeMap;
    private CoordinateReferenceSystem sourceCRS;

    public CRSChooser(Controller parentPage) {
        matcher = Pattern.compile(".*?\\(([^(]*)\\)$").matcher(""); // $NON-NLS-1$ //$NON-NLS-2$
        this.parentPage = parentPage;
    }

    public CRSChooser() {
        this(DEFAULT);
    }

    private Control createCustomCRSControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);

        GridLayout layout = new GridLayout(2, false);
        composite.setLayout(layout);

        GridData gridData = new GridData();
        Label keywordsLabel = new Label(composite, SWT.NONE);
        keywordsLabel.setText(Messages.getString("CRSChooser_keywordsLabel"));
        keywordsLabel.setLayoutData(gridData);
        keywordsLabel.setToolTipText(Messages.getString("CRSChooser_tooltip"));

        gridData = new GridData(SWT.FILL, SWT.NONE, true, false);
        keywordsText = new Text(composite, SWT.SINGLE | SWT.BORDER);
        keywordsText.setLayoutData(gridData);
        keywordsText.setToolTipText(Messages.getString("CRSChooser_tooltip"));

        gridData = new GridData(SWT.FILL, SWT.NONE, true, false);
        gridData.horizontalSpan = 2;
        Label editorLabel = new Label(composite, SWT.NONE);
        editorLabel.setText(Messages.getString("CRSChooser_label_crsWKT"));
        editorLabel.setLayoutData(gridData);

        gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        gridData.horizontalSpan = 2;
        wktText = new Text(composite, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        if (selectedCRS != null) wktText.setText(selectedCRS.toWKT());
        wktText.setLayoutData(gridData);
        wktText.addModifyListener(
                new ModifyListener() {

                    public void modifyText(ModifyEvent e) {
                        if (!keywordsText.isEnabled()) keywordsText.setEnabled(true);
                    }
                });

        searchText.setFocus();
        return composite;
    }

    private Control createStandardCRSControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        composite.setLayout(layout);

        GridData gridData = new GridData();
        Label codesLabel = new Label(composite, SWT.NONE);
        codesLabel.setText(Messages.getString("CRSChooser_label_crs"));
        codesLabel.setLayoutData(gridData);

        gridData = new GridData(SWT.FILL, SWT.FILL, false, false);
        searchText = new Text(composite, SWT.SINGLE | SWT.BORDER | SWT.SEARCH | SWT.CANCEL);
        searchText.setLayoutData(gridData);
        searchText.addModifyListener(
                new ModifyListener() {
                    public void modifyText(ModifyEvent e) {
                        fillCodesList();
                    }
                });
        searchText.addListener(
                SWT.KeyUp,
                new Listener() {
                    public void handleEvent(Event event) {
                        if (event.keyCode == SWT.ARROW_DOWN) {
                            codesList.getControl().setFocus();
                        }
                    }
                });
        gridData = new GridData(400, 300);
        codesList = new ListViewer(composite);
        codesList.setContentProvider(new ArrayContentProvider());
        codesList.setLabelProvider(new LabelProvider());
        codesList.addSelectionChangedListener(
                new ISelectionChangedListener() {

                    public void selectionChanged(SelectionChangedEvent event) {
                        selectedCRS = null;
                        String crsCode =
                                (String)
                                        ((IStructuredSelection) codesList.getSelection())
                                                .getFirstElement();
                        if (crsCode == null) return;
                        matcher.reset(crsCode);
                        if (matcher.matches()) {
                            selectedCRS = createCRS(matcher.group(1));
                            if (selectedCRS != null && wktText != null) {
                                wktText.setEditable(true);
                                String wkt = null;
                                try {
                                    wkt = selectedCRS.toWKT();
                                } catch (Exception e) {
                                    /*
                                     *  if unable to generate WKT, just return the
                                     *  string and make the text area non editable.
                                     */
                                    wkt = selectedCRS.toString();
                                    wktText.setEditable(false);
                                }
                                wktText.setText(wkt);
                                Preferences node = findNode(matcher.group(1));
                                if (node != null) {
                                    Preferences kn = node.node(ALIASES_ID);
                                    try {
                                        String[] keywords = kn.keys();
                                        if (keywords.length > 0) {
                                            StringBuffer buffer = new StringBuffer();
                                            for (String string : keywords) {
                                                buffer.append(", "); // $NON-NLS-1$
                                                buffer.append(string);
                                            }
                                            buffer.delete(0, 2);
                                            keywordsText.setText(buffer.toString());
                                        }
                                    } catch (BackingStoreException e) {
                                        ExceptionMonitor.show(wktText.getShell(), e);
                                    }
                                } else {
                                    keywordsText.setText(""); // $NON-NLS-1$
                                }
                            }
                        }
                    }
                });

        codesList.addDoubleClickListener(
                new IDoubleClickListener() {

                    public void doubleClick(DoubleClickEvent event) {
                        parentPage.handleOk();
                        parentPage.handleClose();
                    }
                });

        codesList.getControl().setLayoutData(gridData);
        /*
         * fillCodesList() by itself resizes the Preferences Page but in the paintlistener it
         * flickers the window
         */
        fillCodesList();

        searchText.setFocus();

        return composite;
    }

    public void setFocus() {
        searchText.setFocus();
    }

    /**
     * Creates the CRS PreferencePage root control with a CRS already selected
     *
     * @param parent PreferencePage for this chooser
     * @param crs current CRS for the associated map
     * @return control for the PreferencePage
     */
    public Control createControl(Composite parent, CoordinateReferenceSystem crs) {
        Control control = createControl(parent);
        selectedCRS = crs;
        gotoCRS(selectedCRS);
        return control;
    }

    public void clearSearch() {
        searchText.setText(""); // $NON-NLS-1$
    }

    /**
     * Takes in a CRS, finds it in the list and highlights it
     *
     */
    @SuppressWarnings("unchecked")
    public void gotoCRS(CoordinateReferenceSystem crs) {
        if (crs != null) {
            final List list = codesList.getList();
            Set<Identifier> identifiers = new HashSet<Identifier>(crs.getIdentifiers());

            final Set<Integer> candidates = new HashSet<Integer>();

            for (int i = 0; i < list.getItemCount(); i++) {
                for (Identifier identifier : identifiers) {
                    final String item = list.getItem(i);
                    if (sameEPSG(crs, identifier, item) || exactMatch(crs, identifier, item)) {
                        codesList.setSelection(new StructuredSelection(item), false);
                        list.setTopIndex(i);
                        return;
                    }
                    if (isMatch(crs, identifier, item)) {
                        candidates.add(i);
                    }
                }
            }
            if (candidates.isEmpty()) {
                java.util.List<String> input = (java.util.List<String>) codesList.getInput();
                String sourceCRSName = crs.getName().toString();
                sourceCRS = crs;
                input.add(0, sourceCRSName);
                codesList.setInput(input);
                codesList.setSelection(new StructuredSelection(sourceCRSName), false);
                list.setTopIndex(0);
                try {
                    String toWKT = crs.toWKT();
                    wktText.setText(toWKT);
                } catch (RuntimeException e) {
                    ExceptionMonitor.show(
                            wktText.getShell(),
                            e,
                            crs.toString() + " cannot be formatted as WKT"); // $NON-NLS-1$
                    wktText.setText(Messages.getString("CRSChooser_unknownWKT"));
                }
            } else {
                Integer next = candidates.iterator().next();
                codesList.setSelection(new StructuredSelection(list.getItem(next)), false);
                list.setTopIndex(next);
            }
        }
    }

    private boolean exactMatch(CoordinateReferenceSystem crs, Identifier identifier, String item) {
        return (crs == DefaultGeographicCRS.WGS84 && item.equals("WGS 84 (4326)"))
                || // $NON-NLS-1$
                item.equalsIgnoreCase(identifier.toString())
                || isInCodeMap(identifier, item);
    }

    private boolean isInCodeMap(Identifier identifier, String item) {

        String name = crsCodeMap.get(identifier.getCode());
        if (name == null) return false;
        else return name.equals(item);
    }

    private boolean sameEPSG(CoordinateReferenceSystem crs, Identifier identifier, String item) {
        String toString = identifier.toString();
        return toString.contains("EPSG:") && item.contains(toString); // $NON-NLS-1$
    }

    private boolean isMatch(CoordinateReferenceSystem crs, Identifier identifier, String item) {
        return (crs == DefaultGeographicCRS.WGS84 && item.contains("4326"))
                || item.contains(identifier.toString()); // $NON-NLS-1$
    }

    /**
     * Creates the CRS PreferencePage root control with no CRS selected
     *
     * @param parent PreferencePage for this chooser
     * @return control for the PreferencePage
     */
    public Control createControl(Composite parent) {
        GridData gridData = null;

        gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        folder = new TabFolder(parent, SWT.NONE);
        folder.setLayoutData(gridData);

        TabItem standard = new TabItem(folder, SWT.NONE);
        standard.setText(Messages.getString("CRSChooser_tab_standardCRS"));
        Control stdCRS = createStandardCRSControl(folder);
        standard.setControl(stdCRS);

        TabItem custom = new TabItem(folder, SWT.NONE);
        custom.setText(Messages.getString("CRSChooser_tab_customCRS"));
        Control cstCRS = createCustomCRSControl(folder);
        custom.setControl(cstCRS);

        return folder;
    }

    /**
     * checks if all keywords in filter array are in input
     *
     * @param input test string
     * @param filter array of keywords
     * @return true, if all keywords in filter are in the input, false otherwise
     */
    protected boolean matchesFilter(String input, String[] filter) {
        for (String match : filter) {
            if (!input.contains(match)) return false;
        }
        return true;
    }

    /**
     * filters all CRS Names from all available CRS authorities
     *
     * @param filter array of keywords
     * @return Set of CRS Names which contain all the filter keywords
     */
    protected Set<String> filterCRSNames(String[] filter) {
        crsCodeMap = new HashMap<String, String>();
        Set<String> descriptions = new TreeSet<String>();

        for (Object object : ReferencingFactoryFinder.getCRSAuthorityFactories(null)) {
            CRSAuthorityFactory factory = (CRSAuthorityFactory) object;
            try {
                Set<String> codes = factory.getAuthorityCodes(CoordinateReferenceSystem.class);
                for (Object codeObj : codes) {
                    String code = (String) codeObj;
                    String description;
                    try {
                        description = factory.getDescriptionText(code).toString();
                    } catch (Exception e1) {
                        description = Messages.getString("CRSChooser_unnamed");
                    }
                    description += " (" + code + ")"; // $NON-NLS-1$ //$NON-NLS-2$
                    crsCodeMap.put(code, description);
                    if (matchesFilter(description.toUpperCase(), filter)) {
                        descriptions.add(description);
                    }
                }
            } catch (FactoryException e) {
                ExceptionMonitor.show(wktText.getShell(), e, "CRS Authority:" + e.getMessage());
            }
        }
        return descriptions;
    }

    /** populates the codes list with a filtered list of CRS names */
    protected void fillCodesList() {
        String[] searchParms = searchText.getText().toUpperCase().split(" "); // $NON-NLS-1$
        Set<String> descriptions = filterCRSNames(searchParms);
        descriptions = filterCustomCRSs(descriptions, searchParms);
        java.util.List<String> list = new ArrayList<String>(descriptions);
        codesList.setInput(list);
        if (list != null && !list.isEmpty()) {
            codesList.setSelection(new StructuredSelection(list.get(0)));
        } else {
            codesList.setSelection(new StructuredSelection());
            // System.out.println( "skipped");
        }
    }

    private Set<String> filterCustomCRSs(Set<String> descriptions, String[] searchParms) {
        try {
            Preferences root = Preferences.userRoot();
            Preferences node = root.node(CUSTOM_ID);

            for (String id : node.childrenNames()) {
                Preferences child = node.node(id);
                String string = child.get(NAME_ID, null);
                if (string != null && matchesFilter(string.toUpperCase(), searchParms)) {
                    descriptions.add(string);
                    continue;
                }

                Preferences aliases = child.node(ALIASES_ID);
                for (String alias : aliases.keys()) {
                    if (matchesFilter(alias.toUpperCase(), searchParms)) {
                        descriptions.add(string);
                        continue;
                    }
                }
            }
        } catch (Exception e) {
            ExceptionMonitor.show(wktText.getShell(), e);
        }
        return descriptions;
    }

    /**
     * creates a CRS from a code when the appropriate CRSAuthorityFactory is unknown
     *
     * @param code CRS code
     * @return CRS object from appropriate authority, or null if the appropriate factory cannot be
     *     determined
     */
    protected CoordinateReferenceSystem createCRS(String code) {
        if (code == null) return null;
        for (Object object : ReferencingFactoryFinder.getCRSAuthorityFactories(null)) {
            CRSAuthorityFactory factory = (CRSAuthorityFactory) object;
            try {
                return (CoordinateReferenceSystem) factory.createObject(code);
            } catch (FactoryException e2) {
                // then we have the wrong factory
                // is there a better way to do this?
            } catch (Exception e) {
                ExceptionMonitor.show(
                        wktText.getShell(), e, "Error creating CRS object, trying more...");
            }
        }
        try {
            Preferences child = findNode(code);
            if (child != null) {
                String wkt = child.get(WKT_ID, null);
                if (wkt != null) {
                    try {
                        return ReferencingFactoryFinder.getCRSFactory(null).createFromWKT(wkt);
                    } catch (Exception e) {
                        ExceptionMonitor.show(wktText.getShell(), e);
                        child.removeNode();
                    }
                }
            }

        } catch (Exception e) {
            ExceptionMonitor.show(wktText.getShell(), e);
        }
        return null; // should throw an exception?
    }

    private Preferences findNode(String code) {
        try {
            Preferences root = Preferences.userRoot();
            Preferences node = root.node(CUSTOM_ID);

            if (node.nodeExists(code)) {
                return node.node(code);
            }

            for (String id : node.childrenNames()) {
                Preferences child = node.node(id);
                String name = child.get(NAME_ID, null);
                if (name != null && matchesFilter(name, new String[] {code})) {
                    return child;
                }
            }
            return null;
        } catch (BackingStoreException e) {
            ExceptionMonitor.show(wktText.getShell(), e);
            return null;
        }
    }

    /**
     * returns the selected CRS
     *
     * @return selected CRS
     */
    public CoordinateReferenceSystem getCRS() {
        if (folder == null) return selectedCRS;
        if (folder.getSelectionIndex() == 1) {
            try {
                String text = wktText.getText();
                CoordinateReferenceSystem createdCRS =
                        ReferencingFactoryFinder.getCRSFactory(null).createFromWKT(text);

                if (keywordsText.getText().trim().length() > 0) {
                    Preferences node = findNode(createdCRS.getName().getCode());
                    if (node != null) {
                        Preferences kn = node.node(ALIASES_ID);
                        String[] keywords = keywordsText.getText().split(","); // $NON-NLS-1$
                        kn.clear();
                        for (String string : keywords) {
                            string = string.trim().toUpperCase();
                            if (string.length() > 0) kn.put(string, string);
                        }
                        kn.flush();
                    } else {
                        CoordinateReferenceSystem found = createCRS(createdCRS.getName().getCode());
                        if (found != null
                                && CRS.findMathTransform(found, createdCRS, true).isIdentity()) {
                            saveKeywords(found);
                            return found;
                        }

                        Set<Identifier> identifiers =
                                new HashSet<Identifier>(createdCRS.getIdentifiers());
                        for (Identifier identifier : identifiers) {
                            found = createCRS(identifier.toString());
                            if (found != null
                                    && CRS.findMathTransform(found, createdCRS, true)
                                            .isIdentity()) {
                                saveKeywords(found);
                                return found;
                            }
                        }
                        return saveCustomizedCRS(text, true, createdCRS);
                    }
                }

                return createdCRS;
            } catch (Exception e) {
                ExceptionMonitor.show(wktText.getShell(), e);
            }
        }
        if (selectedCRS == null) {
            String crsCode =
                    (String) ((IStructuredSelection) codesList.getSelection()).getFirstElement();
            if (sourceCRS != null && crsCode.equals(sourceCRS.getName().toString())) {
                // System.out.println("source crs: " + sourceCRS.getName().toString());
                return sourceCRS;
            }
            return createCRS(searchText.getText());
        }
        return selectedCRS;
    }

    /**
     */
    private void saveKeywords(CoordinateReferenceSystem found)
            throws CoreException, IOException, BackingStoreException {
        String[] keywords = keywordsText.getText().split(","); // $NON-NLS-1$
        if (keywords.length > 0) {
            boolean legalKeyword = false;
            // determine whether there are any keywords that are not blank.
            for (int i = 0; i < keywords.length; i++) {
                String string = keywords[i];
                string = string.trim().toUpperCase();
                if (string.length() > 0) {
                    legalKeyword = true;
                    break;
                }
            }
            if (legalKeyword) {
                saveCustomizedCRS(found.toWKT(), false, found);
            }
        }
        keywordsText.setText(""); // $NON-NLS-1$
        wktText.setText(found.toWKT());
    }

    /**
     */
    private CoordinateReferenceSystem saveCustomizedCRS(
            String text, boolean processWKT, CoordinateReferenceSystem createdCRS)
            throws CoreException, IOException, BackingStoreException {
        Preferences root = Preferences.userRoot();
        Preferences node = root.node(CUSTOM_ID);
        int lastID;
        String code;
        String name;
        String newWKT;
        if (processWKT) {
            lastID = Integer.parseInt(node.get(LAST_ID, "0")); // $NON-NLS-1$
            code = "UDIG:" + lastID; // $NON-NLS-1$
            name = createdCRS.getName().toString() + "(" + code + ")"; // $NON-NLS-1$ //$NON-NLS-2$
            lastID++;
            node.putInt(LAST_ID, lastID);
            newWKT = processingWKT(text, lastID);
        } else {
            Set<ReferenceIdentifier> ids = createdCRS.getIdentifiers();
            if (!ids.isEmpty()) {
                Identifier id = ids.iterator().next();
                code = id.toString();
                name =
                        createdCRS.getName().getCode()
                                + " ("
                                + code
                                + ")"; // $NON-NLS-1$ //$NON-NLS-2$
            } else {
                name = code = createdCRS.getName().getCode();
            }

            newWKT = text;
        }

        Preferences child = node.node(code);
        child.put(NAME_ID, name);
        child.put(WKT_ID, newWKT);
        String[] keywords = keywordsText.getText().split(","); // $NON-NLS-1$
        if (keywords.length > 0) {
            Preferences keyworkNode = child.node(ALIASES_ID);
            for (String string : keywords) {
                string = string.trim().toUpperCase();
                keyworkNode.put(string, string);
            }
        }
        node.flush();

        return createdCRS;
    }

    /** Remove the last AUTHORITY if it exists and add a UDIG Authority */
    private String processingWKT(String text, int lastID) {
        String newWKT;
        String[] prep = text.split(","); // $NON-NLS-1$
        if (prep[prep.length - 2].toUpperCase().contains("AUTHORITY")) { // $NON-NLS-1$
            String substring = text.substring(0, text.lastIndexOf(','));
            newWKT =
                    substring.substring(0, substring.lastIndexOf(','))
                            + ", AUTHORITY[\"UDIG\",\""
                            + (lastID - 1)
                            + "\"]]"; // $NON-NLS-1$ //$NON-NLS-2$
        } else {
            newWKT =
                    text.substring(0, text.lastIndexOf(']'))
                            + ", AUTHORITY[\"UDIG\",\""
                            + (lastID - 1)
                            + "\"]]"; // $NON-NLS-1$ //$NON-NLS-2$
        }
        wktText.setText(newWKT);
        return newWKT;
    }

    public void setController(Controller controller) {
        parentPage = controller;
    }
}
