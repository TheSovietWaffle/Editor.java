import javax.swing.*;

import com.formdev.flatlaf.FlatLaf;

import java.awt.*;
import java.awt.event.*;
import java.util.Collections;
import java.util.Arrays;
import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;


public class Editor extends JFrame {
    public JPanel MainPanel;
    private JPanel MainOperations;
    private JButton insertRowButton;
    private JButton readDataButton;
    private JButton updateDataButton;
    private JButton buildQueryLabel;
    private JButton deleteButton;
    private JPanel InsertOperations;
    private JButton connectButton;
    private JButton createTableButton;
    private JTextArea consoleReadOut;
    private JTextField insertName;
    private JTextField insertCity;
    private JButton submitButton1;
    private JRadioButton nameRadioToggle1;
    private JRadioButton cityRadioToggle1;
    private JPanel Toggles;
    private JTextField inputUpdateOName;
    private JTextField inputUpdateNName;
    private JButton OKButton;
    private JPanel UpdateOperations;
    private JPanel QueryOperations;
    private JLabel columnLabel2;
    private JLabel criteriaLabel;
    private JLabel oldNameLabel;
    private JLabel newNameLabel;
    private JLabel nameLable1;
    private JLabel cityLabel1;
    private JButton submitButton3;
    private JPanel DeleteOperations;
    private JRadioButton idRadioToggle;
    private JRadioButton rangeIdToggle;
    private JButton deleteButton1;
    private JButton DropTable;
    private JPanel connectionPanel;
    private JLabel dbConnectionDataLabel;
    private JButton connectToDB;
    private JPanel MainMainPanel;
    private JLabel consoleReadOutLabel;
    private JTextField dbConnDataField;
    private JTextField dbNameField;
    private JTextField userNameField;
    private JPasswordField passField;
    private JLabel dbNameLabel;
    private JLabel userNameLabel;
    private JLabel passLabel;
    private JComboBox tableSelect;
    private JComboBox columnSelector;
    private JComboBox criteriaSelector;
    private JLabel parameterQueryLabel;
    private JTextField queryParamField;
    private JTextField specificIdsTextField;
    private JLabel fromRangeLabel;
    private JTextField fromIdTextField;
    private JLabel toRangeLabel;
    private JTextField toIdTextField;
    private JButton addUserBtt;
    private JPanel userAddPanel;
    private JButton addUserFbttn;
    private JTextField usernameField;
    private JTextField textField2;
    private JTextField textField3;
    private JLabel uUserNLabel;
    private JLabel userPassLabel;
    private JLabel clearUserLabel;
    private JButton basicMButton;
    private JList pastConnsL;
    private JList pastUNames;

    static DbFunctions db = new DbFunctions();
    private DbFunctions.DbFunctionsResult connResult;

    private DefaultListModel<String> pastConnsModel;

    public Editor() {
        criteriaSelector.addItem("<");
        criteriaSelector.addItem(">");
        criteriaSelector.addItem("=");

        pastConnsModel = new DefaultListModel<>();

        insertRowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (InsertOperations.isVisible()) {
                    insertRowButton.setBackground(new Color(252, 226, 226));
                    InsertOperations.setVisible(false);
                } else if (!InsertOperations.isVisible() && DbFunctions.getClearanceLevel() >= 4) {
                    InsertOperations.setVisible(true);
                    insertRowButton.setBackground(new Color(225, 250, 233));
                }
            }
        });

        /*readDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (UpdateOperations.isVisible()) {
                    readDataButton.setBackground(new Color(252, 226, 226));
                    UpdateOperations.setVisible(false);
                } else if (!UpdateOperations.isVisible()) {
                    UpdateOperations.setVisible(true);
                    readDataButton.setBackground(new Color(225, 250, 233));
                }
            }
        });*/
        buildQueryLabel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (QueryOperations.isVisible()) {
                    buildQueryLabel.setBackground(new Color(252, 226, 226));
                    QueryOperations.setVisible(false);
                } else if (!QueryOperations.isVisible()) {
                    QueryOperations.setVisible(true);
                    buildQueryLabel.setBackground(new Color(225, 250, 233));
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (DeleteOperations.isVisible()) {
                    deleteButton.setBackground(new Color(252, 226, 226));
                    DeleteOperations.setVisible(false);
                } else if (!DeleteOperations.isVisible() && DbFunctions.getClearanceLevel() >= 4) {
                    DeleteOperations.setVisible(true);
                    deleteButton.setBackground(new Color(225, 250, 233));
                }
            }
        });
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainPanel.setVisible(false);
                connectionPanel.setVisible(true);
                pastConnsModel = new DefaultListModel<>();
                readPastConnections();
                pastConnsL.setModel(pastConnsModel);
            }
        });


        connectToDB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                char[] userPass = passField.getPassword();
                String up = new String(userPass);


                db.setConsoleTextArea(consoleReadOut);
                //change later
                connResult = db.connect_to_db("gstation_elena", userNameField.getText(), up);

                if (connResult.isConnected()) {
                    String[] tableNames = db.getTableNames(connResult.getConnection());
                    for (String tableName : tableNames) {

                        tableSelect.addItem(tableName);

                    }
                    if (connResult != null && connResult.isConnected()) {

                        try (PrintWriter writer = new PrintWriter(new FileWriter("connlist.txt"))) {
                            //chage later
                            writer.println("Database Name: " + "gstation_elena");
                            System.out.println("Connection data saved successfully.");
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                    } else {
                        // Print a message indicating the connection is not successful
                        System.out.println("Connection is not successful. Unable to save connection data.");
                    }
                    readDataButton.setBackground(new Color(214, 228, 253, 255));
                    connectionPanel.setVisible(false);
                    MainPanel.setVisible(true);
                } else {
                    DbFunctions.printToConsole("Connection failed");
                    connectionPanel.setVisible(false);
                    MainPanel.setVisible(true);
                }
            }
        });


        readDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                db.read_data(connResult.getConnection(), tableSelect.getSelectedItem().toString());

            }
        });
        tableSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tableSelect.getSelectedItem().toString() != "users") {
                    String selectedTable = (String) tableSelect.getSelectedItem();
                    String[] columnNames = db.getColumnNames(connResult.getConnection(), selectedTable);

                    // Store the column names in a variable
                    updateColumnNames(columnNames);
                    columnSelector.removeAllItems();
                    for (String columnName : columnNames) {
                        boolean found = false;
                        // Check if the column name already exists in the columnSelector
                        for (int i = 0; i < columnSelector.getItemCount(); i++) {
                            if (columnName.equals(columnSelector.getItemAt(i))) {
                                found = true;
                                break;
                            }
                        }
                        // If the column name is not found, add it to the columnSelector
                        if (!found) {
                            columnSelector.addItem(columnName);
                        }
                    }
                } else if (tableSelect.getSelectedItem().toString() == "users" && DbFunctions.getClearanceLevel() < 5) {
                    consoleReadOut.append("Insufficient clearance!");
                    tableSelect.setSelectedIndex(0);

                }
            }
        });
        submitButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                db.search(
                        connResult.getConnection(),
                        tableSelect.getSelectedItem().toString(),
                        columnSelector.getSelectedItem().toString(),
                        criteriaSelector.getSelectedItem().toString(),
                        queryParamField.getText()
                );
            }
        });
        idRadioToggle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                if (idRadioToggle.isSelected()) {
                    fromRangeLabel.setVisible(false);
                    toRangeLabel.setVisible(false);
                    rangeIdToggle.setSelected(false);
                    fromIdTextField.setVisible(false);
                    toIdTextField.setVisible(false);
                    specificIdsTextField.setVisible(true);
                } else {
                    fromRangeLabel.setVisible(false);
                    toRangeLabel.setVisible(false);
                    specificIdsTextField.setVisible(false);
                }
            }
        });
        rangeIdToggle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (rangeIdToggle.isSelected()) {
                    fromRangeLabel.setVisible(true);
                    toRangeLabel.setVisible(true);
                    fromIdTextField.setVisible(true);
                    toIdTextField.setVisible(true);
                    specificIdsTextField.setVisible(false);
                    idRadioToggle.setSelected(false);
                } else {
                    fromRangeLabel.setVisible(false);
                    toRangeLabel.setVisible(false);
                    rangeIdToggle.setSelected(false);
                    fromIdTextField.setVisible(false);
                    toIdTextField.setVisible(false);
                }
            }
        });
        deleteButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (rangeIdToggle.isSelected()) {
                    for (int i = Integer.parseInt(fromIdTextField.getText()); i < Integer.parseInt(toIdTextField.getText()) + 1; i++) {
                        //db.delete_row_id(connResult.getConnection(), tableSelect.getSelectedItem().toString(), i);
                        consoleReadOut.append("Deleted Id " + i + "\n");
                    }
                } else if (idRadioToggle.isSelected()) {
                    String text = specificIdsTextField.getText();

                    // Split the text by comma
                    String[] items = text.split(",");

                    // Convert items to an array of integers
                    int[] intArray = new int[items.length];
                    for (int i = 0; i < items.length; i++) {
                        try {
                            intArray[i] = Integer.parseInt(items[i].trim());
                            consoleReadOut.append("Deleted Id " + intArray[i] + "\n");
                        } catch (NumberFormatException ex) {
                            // Handle the case where a non-integer value is encountered
                            System.out.println("Invalid input: " + items[i]);
                            return; // Exit the method if there's an invalid input
                        }
                    }
                }
            }
        });
        addUserBtt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (userAddPanel.isVisible()) {
                    addUserBtt.setBackground(new Color(252, 226, 226));
                    userAddPanel.setVisible(false);
                } else if (!userAddPanel.isVisible() && DbFunctions.getClearanceLevel() == 5) {
                    userAddPanel.setVisible(true);
                    addUserBtt.setBackground(new Color(225, 250, 233));
                }
            }
        });
        addUserFbttn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                db.addUser(connResult.getConnection(), userNameField.getText(), passField.getText(), Integer.parseInt((textField3.getText())));
            }
        });
        basicMButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Basic.main(new String[0]);
                //System.exit(0);
                Editor.this.dispose();
            }
        });

        pastConnsL.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) { // Check for single click
                    JList<String> list = (JList<String>) e.getSource();
                    int index = list.locationToIndex(e.getPoint()); // Get the index of the clicked item
                    if (index != -1) {
                        String selectedValue = list.getModel().getElementAt(index);
                        String[] parts = selectedValue.split(":");
                        if (parts.length > 1) {
                            String dbName = parts[1].trim(); // Extract the database name
                            dbNameField.setText(dbName); // Autofill the dbNameField
                        }
                    }
                }
            }

        });
    }


    private void readPastConnections() {
        try (BufferedReader reader = new BufferedReader(new FileReader("connlist.txt"))) {
            String line;
            StringBuilder section = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                System.out.println("Read line: " + line); // Print debug information
                if (!line.isEmpty()) {
                    System.out.println("Adding line to section: " + line);
                    section.append(line).append("\n");
                } else if (section.length() > 0) {
                    System.out.println("Adding section to model: " + section.toString().trim());
                    pastConnsModel.addElement(section.toString().trim());
                    section.setLength(0); // Clear the StringBuilder for the next section
                }

            }
            // Add the last section if not empty
            if (section.length() > 0) {
                pastConnsModel.addElement(section.toString().trim());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void addSection(List<String> sectionLines) {
        StringBuilder section = new StringBuilder();
        for (String line : sectionLines) {
            section.append(line).append("\n");
        }
        pastConnsModel.addElement(section.toString());
    }

    private void updateColumnNames(String[] columnNames) {
        // Use the 'columnNames' array as needed
        System.out.println("Column names for the selected table: " + Arrays.toString(columnNames));
    }


    public static void main(String[] args) {
        FlatLaf.setGlobalExtraDefaults(Collections.singletonMap("@accentColor", "#f00"));
        com.formdev.flatlaf.FlatDarculaLaf.setup();

        db = new DbFunctions();

        Editor editor = new Editor();
        // Set the model of pastConnsL
        editor.setContentPane(editor.MainMainPanel);
        editor.setTitle("DataBase Editor by RLS");
        editor.pack();
        editor.setMinimumSize(new Dimension(630, 400));
        editor.setVisible(true);
        editor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        editor.setExtendedState(JFrame.MAXIMIZED_BOTH);
        editor.InsertOperations.setVisible(false);
        editor.UpdateOperations.setVisible(false);
        editor.QueryOperations.setVisible(false);
        editor.DeleteOperations.setVisible(false);
        editor.specificIdsTextField.setVisible(false);
        editor.fromRangeLabel.setVisible(false);
        editor.fromIdTextField.setVisible(false);
        editor.toIdTextField.setVisible(false);
        editor.toRangeLabel.setVisible(false);
        editor.userAddPanel.setVisible(false);
    }

}



