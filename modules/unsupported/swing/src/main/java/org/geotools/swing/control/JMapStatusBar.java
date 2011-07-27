/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.swing.control;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;

import org.geotools.swing.MapPane;
import org.geotools.swing.dialog.AbstractModalDialog;
import org.geotools.swing.dialog.DialogUtils;
import org.geotools.util.logging.Logging;

/**
 * A status bar that works with a map pane.
 *
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
public class JMapStatusBar extends JPanel {
    private static final Logger LOGGER = Logging.getLogger("org.geotools.swing");

    private static final int INSET = 0;

    // Package-private constants for use by StatusBarItem classes
    static final Font DEFAULT_FONT = new Font("Courier", Font.PLAIN, 12);
    static final int DEFAULT_NUM_DECIMAL_DIGITS = 2;

    private int numDecimalDigits = DEFAULT_NUM_DECIMAL_DIGITS;

    private static class ItemInfo {
        final StatusBarItem item;
        final boolean configurable;
        final int componentIndex;
        boolean showing;

        public ItemInfo(StatusBarItem item,
                boolean configurable,
                int componentIndex,
                boolean showing) {

            this.item = item;
            this.configurable = configurable;
            this.componentIndex = componentIndex;
            this.showing = showing;
        }
    }

    private final Map<Integer, ItemInfo> itemInfo;
    private int minItemHeight;

    /**
     * Creates a new status bar, with the default set of items, linked to
     * the given map pane. This method can be called safely from any thread.
     * 
     * The default items are:
     * <ul>
     * <li>rendering activity item</li>
     * <li>cursor coordinate item</li>
     * <li>map extent item</li>
     * </ul>
     *
     * @param mapPane the map pane linked to the status bar
     * 
     * @return a new status bar
     *
     * @throws IllegalArgumentException if {@code mapPane} is {@code null}
     */
    public static JMapStatusBar createDefaultStatusBar(final MapPane mapPane) {
        final JMapStatusBar[] statusBar = new JMapStatusBar[1];

        if (SwingUtilities.isEventDispatchThread()) {
            statusBar[0] = doCreateDefaultStatusBar(mapPane);

        } else {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        statusBar[0] = doCreateDefaultStatusBar(mapPane);
                    }
                });
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        return statusBar[0];
    }

    /**
     * Helper method for {@linkplain #createDefaultStatusBar}.
     *
     * @param mapPane the map pane linked to the new status bar
     *
     * @return a new status bar
     *
     * @throws IllegalArgumentException if {@code mapPane} is {@code null}
     */
    private static JMapStatusBar doCreateDefaultStatusBar(MapPane mapPane) {
        JMapStatusBar statusBar = new JMapStatusBar();

        statusBar.addItem( new CoordsStatusBarItem(mapPane) );
        statusBar.addItem( new ExtentStatusBarItem(mapPane) );
        statusBar.addItem( new CRSStatusBarItem(mapPane) );

        return statusBar;
    }

    public JMapStatusBar() {
        this.itemInfo = new HashMap<Integer, ItemInfo>();

        setLayout(new MigLayout("insets " + INSET));
        setBackground(new Color(224, 224, 224));
        setFont(DEFAULT_FONT);

        URL url = this.getClass().getResource("icons/configure-3.png");
        ImageIcon icon = new ImageIcon(url);
        PopupMenuProvider menuProvider = new PopupMenuProvider() {
            @Override
            public JPopupMenu getMenu() {
                return createItemMenu();
            }
        };

        StatusBarItem item = new MenuStatusBarItem("", icon, menuProvider);
        addItem(item, false, true);
    }

    public boolean addItem(StatusBarItem item) {
        return addItem(item, true, true);
    }

    public boolean addItem(StatusBarItem item, boolean configurable, boolean showing) {
        if (!itemInfo.containsKey(item.getID())) {
            ItemInfo ie = new ItemInfo(item, configurable, getComponentCount(), showing);
            itemInfo.put(item.getID(), ie);

            if (showing) {
                add(item);
            }

            int h = item.getMinimumHeight();
            if (h > minItemHeight) {
                minItemHeight = h;
                setMinimumSize(new Dimension(-1, minItemHeight));
            }
            return true;
            
        } else {
            LOGGER.log(Level.WARNING, 
                    "Item label:{0} id:{1} is already in the status bar",
                    new Object[]{item.getName(), item.getID()});
            return false;
        }
    }

    private JPopupMenu createItemMenu() {
        JPopupMenu menu = new JPopupMenu();

        // Add menu items to toggle display of status bar elements
        for (Entry<Integer, ItemInfo> entry : itemInfo.entrySet()) {
            final ItemInfo el = entry.getValue();
            if (el.configurable) {
                JMenuItem menuItem = new JCheckBoxMenuItem(el.item.getName(), el.showing);
                menuItem.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        el.showing = !el.showing;
                        Rectangle r = el.item.getBounds();
                        if (el.showing) {
                            add(el.item, el.componentIndex);
                        } else {
                            remove(el.item);
                        }
                        revalidate();
                        repaint(r);
                    }
                });
                menu.add(menuItem);
            }
        }

        menu.addSeparator();
        JMenuItem menuItem = new JMenuItem("Set number decimal digits...");
        menuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setNumDecimals();
            }
        });
        menu.add(menuItem);

        return menu;
    }

    private void setNumDecimals() {
        DecimalDigitsDialog dialog = new DecimalDigitsDialog(numDecimalDigits);
        DialogUtils.showCentred(dialog);
        int n = dialog.getNumDigits();

        if (n >= 0) {
            numDecimalDigits = n;
            for (ItemInfo ie : itemInfo.values()) {
                ie.item.setNumDecimals(numDecimalDigits);
            }
        }
    }

    private static class DecimalDigitsDialog extends AbstractModalDialog {

        private JIntegerField digitsFld;
        private int numDigits;

        public DecimalDigitsDialog(int initialValue) {
            super("Status bar value format");
            numDigits = initialValue;
            initComponents();
        }

        @Override
        public JPanel createControlPanel() {
            JPanel panel = new JPanel(new MigLayout());

            panel.add(new JLabel("Decimal digits:"), "gap related");
            
            digitsFld = new JIntegerField(numDigits, false);
            panel.add(digitsFld, "w 40!");

            return panel;
        }

        public int getNumDigits() {
            return numDigits;
        }

        @Override
        public void onOK() {
            numDigits = digitsFld.getValue();
            closeDialog();
        }
    }
}
