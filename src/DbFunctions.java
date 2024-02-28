import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JTextArea;
import java.util.ArrayList;
import java.util.List;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;


public class DbFunctions {
    public class DbFunctionsResult {
        private Connection connection;
        private boolean isConnected;

        public DbFunctionsResult(Connection connection, boolean isConnected) {
            this.connection = connection;
            this.isConnected = isConnected;
        }

        public Connection getConnection() {
            return connection;
        }

        public boolean isConnected() {
            return isConnected;
        }
    }

    private static JTextArea consoleTextArea;

    public static void setConsoleTextArea(JTextArea textArea) {
        if (consoleTextArea == null) {
            consoleTextArea = textArea;
        }
    }

    public DbFunctionsResult connect_to_db(String dbname, String user, String pass) {
        Connection conn = null;
        boolean isConnected = false;

        try {
            Class.forName("org.postgresql.Driver");

            // Extract username and password before '..'
            String dbUser = user.split("\\.\\.")[0];
            String dbPass = pass.split("\\.\\.")[0];

            // Connection attempt
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbname, dbUser, dbPass);

            if (conn != null) {
                printToConsole("Connected successfully");

                // Check user credentials in the 'users' table
                String loginQuery = "SELECT * FROM users WHERE uname = ? AND upass = ?";
                PreparedStatement loginStatement = conn.prepareStatement(loginQuery);
                loginStatement.setString(1, user.split("\\.\\.")[1]); // Extracting username after '..'
                loginStatement.setString(2, pass.split("\\.\\.")[1]); // Extracting password after '..'
                ResultSet loginResult = loginStatement.executeQuery();

                if (loginResult.next()) {
                    // Retrieve clearance level from the user's row
                    int clearanceLevel = loginResult.getInt("clearance");

                    // Set the clearance level in a variable for future use
                    setClearanceLevel(clearanceLevel);

                    isConnected = true;

                    printToConsole("Login successful - Welcome " + user.split("\\.\\.")[1] + ". Clearance level: " + clearanceLevel);
                } else {
                    printToConsole("Login failed");
                }
            } else {
                printToConsole("Connection failed");
            }
        } catch (Exception e) {
            printToConsole(e.toString());
        }

        return new DbFunctionsResult(conn, isConnected);
    }


    public String[] getTableNames(Connection conn) {
        Statement statement;
        ArrayList<String> tableNamesList = new ArrayList<>();

        try {
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet resultSet = metaData.getTables(null, null, "%", new String[]{"TABLE"});

            while (resultSet.next()) {
                tableNamesList.add(resultSet.getString("TABLE_NAME"));
            }

        } catch (Exception e) {
            printToConsole(e.toString());
        }

        return tableNamesList.toArray(new String[0]);
    }

    public void createTable(Connection conn, String table_name) {
        Statement statement;
        try {
            String query = "create table " + table_name + "(empid SERIAL, name varchar(200), address varchar(200), primary key(empid))";
            statement = conn.createStatement();
            statement.executeUpdate(query);
            printToConsole("Table Created");
        } catch (Exception e) {
            printToConsole(e.toString());
        }
    }

    public void insert_row(Connection conn, String table_name, String name, String address) {
        Statement statement;
        try {
            String query = String.format("insert into %s(name,address) values('%s','%s');", table_name, name, address);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            printToConsole("Row Inserted");
        } catch (Exception e) {
            printToConsole(e.toString());
        }
    }

    public void read_data(Connection conn, String table_name) {
        Statement statement;
        try {
            // Retrieve column names dynamically
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet columnsResultSet = metaData.getColumns(null, null, table_name, null);

            // Collect column names in a list
            List<String> columnNames = new ArrayList<>();
            while (columnsResultSet.next()) {
                columnNames.add(columnsResultSet.getString("COLUMN_NAME"));
            }

            // Construct the SELECT query dynamically
            StringBuilder queryBuilder = new StringBuilder("SELECT * FROM ").append(table_name);
            statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(queryBuilder.toString());

            // Display column names
            for (String columnName : columnNames) {
                printToConsole(columnName + ", ");
            }
            printToConsole("\n"); // New line after column names

            // Display data
            while (rs.next()) {
                for (String columnName : columnNames) {
                    printToConsole(rs.getString(columnName) + ", ");
                }
                printToConsole("\n"); // New line after each row
            }

        } catch (Exception e) {
            printToConsole(e.toString());
        }
    }


    public void update_name(Connection conn, String table_name, String old_name, String new_name) {
        Statement statement;
        try {
            String query = String.format("update %s set name='%s' where name = '%s'", table_name, new_name, old_name);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            printToConsole("Data Updated");
        } catch (Exception e) {
            printToConsole(e.toString());
        }
    }

    public void update_city(Connection conn, String table_name, String old_name, String address) {
        Statement statement;
        try {
            String query = String.format("update %s set address='%s' where name = '%s'", table_name, address, old_name);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Data Updated");
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public void search(Connection conn, String table_name, String column_name, String criteria, String param) {
        Statement statement;
        ResultSet rs = null;

        try {
            String query = String.format("select * from %s where %s %s %s", table_name, column_name, criteria, param);
            printToConsole(query);

            statement = conn.createStatement();
            rs = statement.executeQuery(query);

            // Display column names
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                printToConsole(metaData.getColumnName(i) + "\t");
            }
            printToConsole(""); // New line after column names

            // Display data
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    printToConsole(rs.getString(i) + "\t");
                }
                printToConsole(""); // New line after each row
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public void delete_row_name(Connection conn, String table_name, String name) {
        Statement statement;
        try {
            String query = String.format("delete from %s where name = '%s'", table_name, name);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Data deleted");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void delete_row_id(Connection conn, String table_name, int empid) {
        Statement statement;
        try {
            String query = String.format("delete from %s where empid = '%s'", table_name, empid);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Data deleted");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void delete_table(Connection conn, String table_name) {
        Statement statement;
        try {
            String query = String.format("drop table %s", table_name);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Table deleted");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String[] getColumnNames(Connection conn, String tableName) {
        try {
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet columnsResultSet = metaData.getColumns(null, null, tableName, null);

            List<String> columnNames = new ArrayList<>();
            while (columnsResultSet.next()) {
                columnNames.add(columnsResultSet.getString("COLUMN_NAME"));
            }

            return columnNames.toArray(new String[0]);
        } catch (Exception e) {
            printToConsole(e.toString());
            return new String[0];
        }
    }
    public void addUser(Connection conn, String uname, String upass, int clearance) {
        try {
            // Get the current maximum ID from the "users" table
            int maxId = getMaxUserId(conn);

            // Increment the ID for the new user
            int newUserId = maxId + 1;

            // Insert the new user into the "users" table
            String query = "INSERT INTO users (id, uname, upass, clearance) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, newUserId);
                statement.setString(2, uname);
                statement.setString(3, upass);
                statement.setInt(4, clearance);

                // Execute the query
                statement.executeUpdate();
            }

            // Print success message
            printToConsole("User added successfully. ID: " + newUserId);

        } catch (Exception e) {
            printToConsole("Error adding user: " + e.toString());
        }
    }

    private int getMaxUserId(Connection conn) {
        int maxId = 0;
        try {
            String query = "SELECT MAX(id) FROM users";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                var result = statement.executeQuery();
                if (result.next()) {
                    maxId = result.getInt(1);
                }
            }
        } catch (Exception e) {
            printToConsole("Error getting max user ID: " + e.toString());
        }
        return maxId;
    }


    public static void printToConsole(String message) {
        if (consoleTextArea != null) {
            consoleTextArea.append(message + "\n");
        } else {
            System.out.println(message);
        }
    }

    private static int clearanceLevel;

    public static void setClearanceLevel(int level) {
        clearanceLevel = level;
    }

    // Add a method to get clearance level
    public static int getClearanceLevel() {
        return clearanceLevel;
    }
}
