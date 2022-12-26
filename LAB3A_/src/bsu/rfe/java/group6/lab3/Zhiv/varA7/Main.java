package bsu.rfe.java.group6.lab3.Zhiv.varA7;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Main extends JFrame {
    // Константы с исходным размером окна приложения
    private static final int WIDTH = 700;
    private static final int HEIGHT = 500;
    // Массив коэффициентов многочлена
    private final Double[] coefficients;
    private JFileChooser fileChooser = null;
    private JLabel aboutNameTF;
    private final JMenuItem saveToTextMenuItem;
    private final JMenuItem saveToGraphicsMenuItem;
    private final JMenuItem searchValueMenuItem;
    private final JTextField textFieldFrom;
    private final JTextField textFieldTo;
    private final JTextField textFieldStep;
    private final Box hBoxResult;
    private final HornerTableCellRenderer renderer = new HornerTableCellRenderer();
    private HornerTableModel data;


    public Main(Double[] coefficients){
        // Обязательный вызов конструктора предка
        super("Табулирование функции на отрезке");
        // Установить размеры окна
        this.coefficients = coefficients;
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        // Отцентрировать окно приложения на экране
        setLocation((kit.getScreenSize().width - WIDTH)/2, (kit.getScreenSize().height - HEIGHT)/2);

            JMenuBar menuBar = new JMenuBar();
            setJMenuBar(menuBar);
            JMenu aboutMenu = new JMenu("Справка");
            menuBar.add(aboutMenu);
            JMenu fileMenu = new JMenu("Файл");
            menuBar.add(fileMenu);
            JMenu tableMenu = new JMenu("Таблица");
            menuBar.add(tableMenu);

            Action aboutAction = new AbstractAction("О программе") {
                public void actionPerformed(ActionEvent event) {
                    JDialog dialog = new JDialog(Main.this, "О программе", true);
                    dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                    dialog.setSize(360, 100);

                    aboutNameTF = new JLabel("Живоглод Алексей 6 группа");

                    Box box = Box.createVerticalBox();
                    box.add(aboutNameTF);

                    Box hbox = Box.createHorizontalBox();
                    hbox.add(Box.createHorizontalStrut(20));
                    hbox.add(box);

                    dialog.getContentPane().add(hbox);

                    dialog.setVisible(true);
                }
            };

            JMenuItem aboutMenuItem = aboutMenu.add(aboutAction);

            Action saveToTextAction = new AbstractAction("Сохранить в текстовый файл") {
                public void actionPerformed(ActionEvent event) {
                    if (fileChooser == null) {
                        fileChooser = new JFileChooser();
                        fileChooser.setCurrentDirectory(new File("."));
                    }
                    if (fileChooser.showSaveDialog(Main.this) == JFileChooser.APPROVE_OPTION)
                        saveToTextFile(fileChooser.getSelectedFile());
                }
            };
            saveToTextMenuItem = fileMenu.add(saveToTextAction);
            saveToTextMenuItem.setEnabled(false);
            Action saveToGraphics = new AbstractAction("Сохранить в Grafics") {

                public void actionPerformed(ActionEvent event) {
                    if (fileChooser == null) {
                        fileChooser = new JFileChooser();
                        fileChooser.setCurrentDirectory(new File("."));
                    }
                    if (fileChooser.showSaveDialog(Main.this) == JFileChooser.APPROVE_OPTION)
                        saveToGraphics(fileChooser.getSelectedFile());
                }
            };
        saveToGraphicsMenuItem = fileMenu.add(saveToGraphics);
        saveToGraphicsMenuItem.setEnabled(true);

            Action searchValueAction = new AbstractAction("Найти значение многочлена") {
                public void actionPerformed(ActionEvent event) {
                    // Запросить пользователя ввести искомую строку
                    String value = JOptionPane.showInputDialog(Main.this, "Введите значение для поиска",
                            "Поиск значения", JOptionPane.QUESTION_MESSAGE);
                    // Установить введенное значение в качестве иголки
                    renderer.setNeedle(value);
                    // Обновить таблицу
                    getContentPane().repaint();
                }
            };

            // Добавить действие в меню "Таблица"
            searchValueMenuItem = tableMenu.add(searchValueAction);
            // По умолчанию пункт меню является недоступным (данных ещѐ нет)
            searchValueMenuItem.setEnabled(false);



            JLabel labelForFrom = new JLabel("X изменяется на интервале от:");
            textFieldFrom = new JTextField("0.0", 10);
            textFieldFrom.setMaximumSize(textFieldFrom.getPreferredSize());
            JLabel labelForTo = new JLabel("до:");
            textFieldTo = new JTextField("1.0", 10);
            textFieldTo.setMaximumSize(textFieldTo.getPreferredSize());
            JLabel labelForStep = new JLabel("с шагом:");
            textFieldStep = new JTextField("0.1", 10);
            textFieldStep.setMaximumSize(textFieldStep.getPreferredSize());
            Box hboxRange = Box.createHorizontalBox();
            hboxRange.setBorder(BorderFactory.createBevelBorder(1));
            hboxRange.add(Box.createHorizontalGlue());
            hboxRange.add(labelForFrom);
            hboxRange.add(Box.createHorizontalStrut(10));
            hboxRange.add(textFieldFrom);
            hboxRange.add(Box.createHorizontalStrut(20));
            hboxRange.add(labelForTo);
            hboxRange.add(Box.createHorizontalStrut(10));
            hboxRange.add(textFieldTo);
            hboxRange.add(Box.createHorizontalStrut(20));
            hboxRange.add(labelForStep);
            hboxRange.add(Box.createHorizontalStrut(10));
            hboxRange.add(textFieldStep);
            hboxRange.add(Box.createHorizontalGlue());
            hboxRange.setPreferredSize(new Dimension((int) hboxRange.getMaximumSize().getWidth(), (int) (hboxRange.getMinimumSize().getHeight()) * 2));
            getContentPane().add(hboxRange, BorderLayout.NORTH);
            JButton buttonCalc = new JButton("Вычислить");
            buttonCalc.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    try {
                        Double from = Double.parseDouble(textFieldFrom.getText());
                        Double to = Double.parseDouble(textFieldTo.getText());
                        Double step = Double.parseDouble(textFieldStep.getText());
                        data = new HornerTableModel(from, to, step, Main.this.coefficients);
                        JTable table = new JTable(data);
                        table.setDefaultRenderer(Double.class, renderer);
                        table.setRowHeight(30);
                        hBoxResult.removeAll();
                        hBoxResult.add(new JScrollPane(table));
                        getContentPane().validate();
                        saveToTextMenuItem.setEnabled(true);
                        saveToGraphicsMenuItem.setEnabled(true);
                        searchValueMenuItem.setEnabled(true);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(Main.this, "Ошибка в формате записи числа с плавающей точкой", "Ошибочный формат числа", JOptionPane.WARNING_MESSAGE);
                    }
                }
            });
            JButton buttonReset = new JButton("Очистить поля");
            buttonReset.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    textFieldFrom.setText("0.0");
                    textFieldTo.setText("1.0");
                    textFieldStep.setText("0.1");
                    hBoxResult.removeAll();
                    hBoxResult.add(new JPanel());
                    saveToTextMenuItem.setEnabled(false);
                    saveToGraphicsMenuItem.setEnabled(false);
                    searchValueMenuItem.setEnabled(false);
                    getContentPane().validate();
                }
            });
            Box hboxButtons = Box.createHorizontalBox();
            hboxButtons.setBorder(BorderFactory.createBevelBorder(1));
            hboxButtons.add(Box.createHorizontalGlue());
            hboxButtons.add(buttonCalc);
            hboxButtons.add(Box.createHorizontalStrut(30));
            hboxButtons.add(buttonReset);
            hboxButtons.add(Box.createHorizontalGlue());
            hboxButtons.setPreferredSize(new Dimension((int) hboxButtons.getMaximumSize().getWidth(), (int) hboxButtons.getMinimumSize().getHeight() * 2));
            getContentPane().add(hboxButtons, BorderLayout.SOUTH);
            hBoxResult = Box.createHorizontalBox();
            hBoxResult.add(new JPanel());
            getContentPane().add(hBoxResult, BorderLayout.CENTER);
        }

        protected void saveToTextFile(File selectedFile) {
            try {
                PrintStream out = new PrintStream(selectedFile);
                out.println("Результаты табулирования многочлена по схеме Горнера");
                out.print("Многочлен: ");

                for (int i = coefficients.length - 1; i > 0; i--) {
                    out.print(coefficients[i].toString() + "*X^" + i + " ");
                }
                out.println(coefficients[0]);

                out.println();
                out.println("Интервал от " + data.getFrom() + " до " + data.getTo() + " с шагом " + data.getStep());
                out.println("====================================================");
                for (int i = 0; i < data.getRowCount(); i++) {
                    out.println("Значение в точке " + data.getValueAt(i, 0) + " равно " + data.getValueAt(i, 1));
                }
                out.close();
            } catch (FileNotFoundException ignored) {
            }
        }

    protected void saveToGraphics(File selectedFile) {
        try {
            // Создать новый байтовый поток вывода, направленный указанный файл
            DataOutputStream out = new DataOutputStream(new
                    FileOutputStream(selectedFile));
            // Записать в поток вывода попарно значение X в точке, значение многочлена в точке
            for (int i = 0; i<data.getRowCount(); i++) {
                out.writeDouble((Double)data.getValueAt(i,0));
                out.writeDouble((Double)data.getValueAt(i,1));
            }
            // Закрыть поток вывода
            out.close();
        } catch (Exception e) {
            // Исключительную ситуацию "ФайлНеНайден" в данном случае можно не обрабатывать,
// так как мы файл создаѐм, а не открываем для чтения
        }
    }

    public static void main(String[] args){ // Создать экземпляр главного окна, передав ему коэффициенты
        if (args.length==0) {
            System.out.println("Невозможно табулировать многочлен, для которого не задано ни одного коэффициента!");
            System.exit(-1);
        }

        Double[] coefficients = new Double[args.length];
        int i = 0;
        try {
// Перебрать аргументы, пытаясь преобразовать их в Double
            for (String arg: args) {
                coefficients[i++] = Double.parseDouble(arg);
            }
        }
        catch (NumberFormatException ex) {
            System.out.println("Ошибка преобразования строки '" +
                    args[i] + "' в число типа Double");
            System.exit(-2);
        }
        Main frame = new Main(coefficients);
        // Задать действие, выполняемое при закрытии окна
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}