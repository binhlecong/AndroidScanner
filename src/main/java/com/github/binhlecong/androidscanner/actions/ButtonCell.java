package com.github.binhlecong.androidscanner.actions;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonCell extends AbstractCellEditor implements TableCellEditor {

    private JButton btn;
    public ButtonCell() {
        btn = new JButton();
        btn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("clicked");
            }
        });
    }
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if(value instanceof Icon){
            btn.setIcon((Icon) value);
        } else {
            btn.setIcon(null);
        }
        return btn;
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }
}
