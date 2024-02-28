import com.formdev.flatlaf.FlatLaf;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;

public class Basic extends JFrame {
    JPanel BasicMainPanel = new JPanel();
    private JButton nomenMenuBtt;
    private JButton skladOpBtt;
    private JButton reportsBtt;
    private JPanel mainMenuPanel;
    private JButton adminMenuBtt;
    private JButton settingsBtt;
    private JPanel smenuPanel;
    private JButton sNomenBtt;
    private JList nomOps;
    private JButton sSkladOpBtt;
    private JList sSkladOps;
    private JPanel parentPanel;
    private JButton sRepBtt;
    private JList sRepList;
    private JButton sSettBtt;
    private JList sSettList;
    private JScrollPane tableHolder;
    private JTable table1; // Corrected the name of the variable
    private JPanel tablePanel;

    private JToolBar toolBar;

    public void createTable() {
        Object[][] data = {
                {"Movie1", 2004, 9, 424}, // Row 1
                {"Movie2", 2005, 9, 323}, // Row 2
                {"Movie3", 2006, 9, 222}  // Row 3
        };
        table1.setModel(new DefaultTableModel(
                data,
                new String[]{"Title", "Year", "Rating", "Num Votes"}
        ));
    } // Added a semicolon here

    public Basic() {

        parentPanel.add(tableHolder);
        createTable();
        // Set frame properties
        setTitle("DataBase Editor by RLS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set layout manager for the content pane
        getContentPane().setLayout(new BorderLayout());


        // Set layout manager for BasicMainPanel
        BasicMainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST; // Align components to top-left corner
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        BasicMainPanel.add(smenuPanel, gbc);

        // Create a sub-panel to center mainMenuPanel
        JPanel menuWrapperPanel = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        menuWrapperPanel.add(mainMenuPanel, gbc);

        // Add parentPanel to the center of BasicMainPanel
        gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridx = 1;
        gbc.gridy = 1;
        BasicMainPanel.add(menuWrapperPanel, gbc);

        // Add BasicMainPanel to the content pane
        getContentPane().add(BasicMainPanel, BorderLayout.NORTH);

        adminMenuBtt.addActionListener(e -> {
            Editor.main(new String[0]);
            Basic.this.dispose();
        });

        nomenMenuBtt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainMenuPanel.setVisible(false);
                smenuPanel.setVisible(true);
                nomOps.setVisible(true);
            }
        });

        skladOpBtt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainMenuPanel.setVisible(false);
                smenuPanel.setVisible(true);
                sSkladOps.setVisible(true);
            }
        });
        reportsBtt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainMenuPanel.setVisible(false);
                smenuPanel.setVisible(true);
                sRepList.setVisible(true);
            }
        });
        settingsBtt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainMenuPanel.setVisible(false);
                smenuPanel.setVisible(true);
                sSettList.setVisible(true);
            }
        });


        sNomenBtt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (nomOps.isVisible()) {
                    nomOps.setVisible(false);
                } else {
                    nomOps.setVisible(true);
                }
            }
        });

        // Add a mouse listener to nomOps to show mainTable when an item is clicked
        nomOps.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Create a DefaultTableModel with column names
                DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Column 1", "Column 2", "Column 3"}, 0);

                if (nomOps.getSelectedIndex() == 1) System.out.println("2");

                // Set the table model to the mainTable
                createTable(); // Call the createTable method to initialize the table
            }
        });
    }

    public static void main(String[] args) {

        // Set look and feel
        FlatLaf.setGlobalExtraDefaults(Collections.singletonMap("@accentColor", "#Ff0000"));
        com.formdev.flatlaf.FlatDarculaLaf.setup();

        // Create instance of Basic
        Basic basic = new Basic();

        // Set frame to fullscreen based on user's resolution
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        basic.setSize(screenSize);
        basic.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximizes the frame to fullscreen

        // Display the frame
        basic.setVisible(true);

    }
}
