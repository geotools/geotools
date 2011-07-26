/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.geotools.swing.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPopupMenu;

/**
 *
 * @author michael
 */
class MenuStatusBarItem extends StatusBarItem {

    public MenuStatusBarItem(String name, final ImageIcon icon, final JPopupMenu menu) {
        this(name, icon, new PopupMenuProvider() {
            @Override
            public JPopupMenu getMenu() {
                return menu;
            }
        });
    }

    public MenuStatusBarItem(String name, final ImageIcon icon, 
            final PopupMenuProvider menuProvider) {

        super(name, false);

        final JButton btn = new JButton(icon);
        btn.setBorderPainted(false);
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                menuProvider.getMenu().show(btn, 0, 0);
            }
        });

        add(btn);
    }

}
