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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPopupMenu;

/**
 * A status bar item with an icon which displays a pop-up menu when clicked.
 *
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
class MenuStatusBarItem extends StatusBarItem {

    /**
     * Creates a new item to display the given menu;
     *
     * @param name the item name
     * @param icon the icon to display
     * @param toolTip tool tip text (may be {@code null}
     * @param menu the pop-up menu to launch when the item is clicked
     *
     * @throws IllegalArgumentException if {@code icon} or {@code menu} are {@code null}
     */
    public MenuStatusBarItem(String name, final ImageIcon icon, final String toolTip,
            final JPopupMenu menu) {
        this(name, icon, toolTip, new PopupMenuProvider() {
            {
                if (menu == null) {
                    throw new IllegalArgumentException("menu must not be null");
                }
            }

            @Override
            public JPopupMenu getMenu() {
                return menu;
            }
        });
    }

    /**
     * Creates a new item to display a menu which will be supplired by
     * {@code menuProvider}.
     *
     * @param name the item name
     * @param icon the icon to display
     * @param menuProvider an object to provide a (possibly dynamic) pop-up menu
     *
     * @throws IllegalArgumentException if {@code icon} or {@code menuPRovider}
     *     are {@code null}
     */
    public MenuStatusBarItem(String name, final ImageIcon icon, String toolTip,
            final PopupMenuProvider menuProvider) {

        super(name, false);

        if (icon == null) {
            throw new IllegalArgumentException("icon must not be null");
        }
        if (menuProvider == null) {
            throw new IllegalArgumentException("menuProvider must not be null");
        }

        final JButton btn = new JButton(icon);
        btn.setBorderPainted(false);
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                menuProvider.getMenu().show(btn, 0, 0);
            }
        });

        if (toolTip != null && toolTip.trim().length() > 0) {
            btn.setToolTipText(toolTip);
        }

        add(btn);
    }

}
