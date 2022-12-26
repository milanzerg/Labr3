package bsu.rfe.java.group6.lab3.Zhiv.varA7;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;


public class HornerTableCellRenderer implements TableCellRenderer {
    private final JPanel panel = new JPanel();
    private final JLabel label = new JLabel();
    private String needle = null;
    private final DecimalFormat formatter =
            (DecimalFormat) NumberFormat.getInstance();
    
    public HornerTableCellRenderer() {
        formatter.setMaximumFractionDigits(5);
        formatter.setGroupingUsed(false);
        DecimalFormatSymbols dottedDouble = formatter.getDecimalFormatSymbols();
        dottedDouble.setDecimalSeparator('.');
        formatter.setDecimalFormatSymbols(dottedDouble);

        panel.add(label);

        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
    }


    public Component getTableCellRendererComponent(JTable table,
                                                   Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        // Преобразовать double в строку с помощью форматировщика
        String formattedDouble = formatter.format(value);
        // Установить текст надписи равным строковому представлению числа
        label.setText(formattedDouble);
        if (col==1 && needle!=null && needle.equals(formattedDouble)) {
        // Номер столбца = 1 (т.е. второй столбец) + иголка не null
        // (значит что-то ищем) +
        // значение иголки совпадает со значением ячейки таблицы -
        // окрасить задний фон панели в красный цвет
        // окрасить задний фон панели в красный цвет
            panel.setBackground(Color.RED);
            return panel;
        }
        else {
            // Иначе - в обычный белый
            panel.setBackground(Color.WHITE);
        }
        if (col==0 && needle!=null && needle.equals(formattedDouble)) {
            panel.setBackground(Color.GREEN);
            return panel;
        }
        else {
            panel.setBackground(Color.WHITE);
            return panel;
        }
    }
    public void setNeedle(String needle) {
        this.needle = needle;
    }

}
