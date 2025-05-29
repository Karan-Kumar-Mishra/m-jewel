package jewellery;

/**
 *
 * @author AR-LABS For: Java Developer Internship
 *
 * If you look at the code, you will feel like it's written by a NOOB who was
 * learning java when he wrote this code but believe me it is not how i wanted
 * to write this code. Unfortunately, due to time limitations, I was not able to
 * write the best version of this. I even thought of refactoring this code but
 * realized that I was running out of time and that I had other very important
 * things to do. so please pardon this NOOBINESS. - A depressed fellow
 * programmer
 */
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.DatabaseMetaData;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class DBController {

    private static Connection dbConnection;
    private static ResultSet resultSet;
    private static Statement statement;
    private static PreparedStatement preparedStatement;
    private static DatabaseMetaData dbMetaData;

    private static boolean isDBConnected = false;

    public static boolean isDatabaseConnected() {
        return isDBConnected;
    }

    public static boolean connectToDatabase(String dbURL,
            String username, String password) {

        try {
            dbConnection = DBConnect.connect();

            if (dbConnection != null) {
                isDBConnected = true;
                Logger.getLogger(DBController.class.getName()).log(Level.INFO, "Connected with the database!");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }

        return isDatabaseConnected();
    }

    public static boolean closeDatabaseConnection() {
        if (isDBConnected) {
            try {
                dbConnection.close();
                isDBConnected = false;
                Logger.getLogger(DBController.class.getName()).log(Level.INFO, "Disconnected from the database!");
            } catch (SQLException ex) {
                Logger.getLogger(DBController.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }

        return isDatabaseConnected();
    }

    public static List<Object> getTableColumnNames(String tableName) {
        Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                "getTableColumnNames() method called!");

        List<Object> tableMetaData = new ArrayList<>();

        System.out.println("Table name is" + tableName);
        if (isDBConnected) {
            try {
                Connection con = DBConnect.connect();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("select * from " + tableName + "");
                ResultSetMetaData rsMetaData = rs.getMetaData();
                int count = rsMetaData.getColumnCount();
                for (int i = 1; i <= count; i++) {
                    tableMetaData.add(rsMetaData.getColumnName(i));
                }
//                dbMetaData = dbConnection.getMetaData();
//                resultSet = dbMetaData.getColumns(null, null, tableName, null);
//                tableMetaData.removeAll(tableMetaData);
//                while(resultSet.next()) {                    
//                    tableMetaData.add(resultSet.getString("COLUMN_NAME"));
//                }

                Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                        "getTableColumnNames() method finished with return value!");

                return tableMetaData;
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }

        }

        Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                "getTableColumnNames() method finished with \"NULL\"!");

        return null;
    }

    public static List<Object> getTableColumnNamescompany(String tableName) {
        Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                "getTableColumnNames() method called!");

        List<Object> tableMetaData = new ArrayList<>();

        System.out.println("Table name is" + tableName);

        try {
            Connection con = DBConnect.connectCopy();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from " + tableName + "");
            ResultSetMetaData rsMetaData = rs.getMetaData();
            int count = rsMetaData.getColumnCount();
            for (int i = 1; i <= count; i++) {
                tableMetaData.add(rsMetaData.getColumnName(i));
            }
//                dbMetaData = dbConnection.getMetaData();
//                resultSet = dbMetaData.getColumns(null, null, tableName, null);
//                tableMetaData.removeAll(tableMetaData);
//                while(resultSet.next()) {                    
//                    tableMetaData.add(resultSet.getString("COLUMN_NAME"));
//                }

            Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                    "getTableColumnNames() method finished with return value!");

            return tableMetaData;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }

        Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                "getTableColumnNames() method finished with \"NULL\"!");

        return null;
    }

    public static List<String> getDatabaseTablesNames() {
        List<String> tableNames = new ArrayList<>();

        Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                "getDatabaseTablesNames() method called!");

        if (isDBConnected) {
            try {
                dbMetaData = dbConnection.getMetaData();
                resultSet = dbMetaData
                        .getTables(dbConnection.getCatalog(), null, "%",
                                new String[]{"TABLE"});
                while (resultSet.next()) {
                    tableNames.add(resultSet.getString(3));
                }

                Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                        "getDatabaseTablesNames() method finished with table names!");

                return tableNames;
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }

        Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                "getDatabaseTablesNames() method finished with \"NULL\"!");

        return null;
    }

    public static List<Object> executeQuery(String query) {
        List<Object> queryResult = new ArrayList<>();

        Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                "executeQuery() method called!");

        if (isDBConnected) {
            try {

                statement = dbConnection.createStatement();
                resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    for (int idx = 1; idx <= resultSet.getMetaData().getColumnCount(); idx++) {
                        queryResult.add(resultSet.getObject(idx));
                    }
                }

            } catch (SQLException ex) {
                Logger.getLogger(DBController.class.getName()).log(Level.SEVERE, null, ex);

                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }

        Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                "executeQuery() method finished with resultSet!");

        return queryResult;
    }

    public static List<Object> executeQueryCompany(String query) {
        List<Object> queryResult = new ArrayList<>();

        Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                "executeQuery() method called!");

        try {
            dbConnection = DBConnect.connectCopy();
            statement = dbConnection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                for (int idx = 1; idx <= resultSet.getMetaData().getColumnCount(); idx++) {
                    queryResult.add(resultSet.getObject(idx));
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }

        Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                "executeQuery() method finished with resultSet!");

        return queryResult;
    }

    public static int executeQueryUpdate(String query) {
        Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                "executeUpdate() method called!");

        int rowsAffected = 0;

        if (isDBConnected) {
            try {
                statement = dbConnection.createStatement();
                rowsAffected = statement.executeUpdate(query);
            } catch (SQLException ex) {
                Logger.getLogger(DBController.class.getName()).log(Level.SEVERE, null, ex);
              //  JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }

        Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                "executeUpdate() method finished, rows affected: " + rowsAffected);

        return rowsAffected;
    }

    public static List<List<Object>> getDataFromTableforcompany(String query) {
        List<List<Object>> tableData = new ArrayList<>();

        Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                "getDataFromTable() method called!");
        dbConnection = DBConnect.connectCopy();

        try {
            statement = dbConnection.createStatement();
            resultSet = statement.executeQuery(query);

            int tableColumnsCount = resultSet.getMetaData().getColumnCount();

            while (resultSet.next()) {
                List<Object> tableRow = new ArrayList<>();

                for (int idx = 1; idx <= tableColumnsCount; idx++) {
                    tableRow.add(resultSet.getObject(idx));
                }

                tableData.add(tableRow);

            }
            Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                    "getDataFromTable() method finished with table data!");
//                dbConnection.close();
            return tableData;
        } catch (SQLException ex) {
            Logger.getLogger(DBController.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());

        }

        Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                "getDataFromTable() method finished with \"NULL\"!");

        return null;
    }

    public static List<List<Object>> getDataFromTable(String query) {
        List<List<Object>> tableData = new ArrayList<>();

        Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                "getDataFromTable() method called!");

        if (isDBConnected) {
            try {
                dbConnection = DBConnect.connect();
                statement = dbConnection.createStatement();
                resultSet = statement.executeQuery(query);

                int tableColumnsCount = resultSet.getMetaData().getColumnCount();

                while (resultSet.next()) {
                    List<Object> tableRow = new ArrayList<>();

                    for (int idx = 1; idx <= tableColumnsCount; idx++) {
                        tableRow.add(resultSet.getObject(idx));
                    }

                    tableData.add(tableRow);
                }

                Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                        "getDataFromTable() method finished with table data!");

                return tableData;
            } catch (SQLException ex) {
                Logger.getLogger(DBController.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }

        Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                "getDataFromTable() method finished with \"NULL\"!");

        return null;
    }

    public static List<List<Object>> getDataFromcompanyTable(String query) {
        List<List<Object>> tableData = new ArrayList<>();

        Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                "getDataFromTable() method called!");

        try {
            dbConnection = DBConnect.connectCopy();
            statement = dbConnection.createStatement();
            resultSet = statement.executeQuery(query);

            int tableColumnsCount = resultSet.getMetaData().getColumnCount();

            while (resultSet.next()) {
                List<Object> tableRow = new ArrayList<>();

                for (int idx = 1; idx <= tableColumnsCount; idx++) {
                    tableRow.add(resultSet.getObject(idx));
                }

                tableData.add(tableRow);
            }

            Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                    "getDataFromTable() method finished with table data!");

            return tableData;
        } catch (SQLException ex) {
            Logger.getLogger(DBController.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }

        Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                "getDataFromTable() method finished with \"NULL\"!");

        return null;
    }

    public static boolean insertDataIntoTable(String tableName,
            List<Object> columnNames, List<Object> data) {

        Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                "insertDataIntoTable() method called!");

        String query = "INSERT INTO " + tableName + "(";
        int rowInserted;

        for (int idx = 0; idx < columnNames.size(); idx++) {
            // If its the last element then we dont need comma and space
            if (idx == columnNames.size() - 1) {
                query += columnNames.get(idx).toString();
            } else {
                query += columnNames.get(idx).toString();
                query += ", ";
            }
        }

        query += ") values(";

        for (int idx = 0; idx < columnNames.size(); idx++) {
            // If its the last question mark then we don't need comma and space
            if (idx == columnNames.size() - 1) {
                query += "?)";
            } else {
                query += "?, ";
            }
        }
        Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                query);
        Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                "insertDataIntoTable() method, query compiled!");

        if (isDBConnected) {
            try {

                preparedStatement = dbConnection.prepareStatement(query);

                for (int idx = 1; idx <= data.size(); idx++) {
                    preparedStatement.setObject(idx, data.get(idx - 1));
                }

                Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                        preparedStatement.toString());

                rowInserted = preparedStatement.executeUpdate();

                if (rowInserted > 0) {
                    Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                            "insertDataIntoTable() method finished with true!");
                    return true;
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
                Logger.getLogger(CreateNewCompanyScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                "insertDataIntoTable() method finished with false!");

        return false;
    }

    public static boolean insertDataIntoCompanyTable(String tableName,
            List<Object> columnNames, List<Object> data) {
        JOptionPane.showMessageDialog(null, "table name " + tableName);
        Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                "insertDataIntoTable() method called!");

        String query = "INSERT INTO " + tableName + "(";
        int rowInserted;

        for (int idx = 0; idx < columnNames.size(); idx++) {
            // If its the last element then we dont need comma and space
            if (idx == columnNames.size() - 1) {
                query += columnNames.get(idx).toString();
            } else {
                query += columnNames.get(idx).toString();
                query += ", ";
            }
        }

        query += ") values(";

        for (int idx = 0; idx < columnNames.size(); idx++) {
            // If its the last question mark then we don't need comma and space
            if (idx == columnNames.size() - 1) {
                query += "?)";
            } else {
                query += "?, ";
            }
        }
        Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                query);
        Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                "insertDataIntoTable() method, query compiled!");

        try {
            dbConnection = DBConnect.connectCopy();
            preparedStatement = dbConnection.prepareStatement(query);

            for (int idx = 1; idx <= data.size(); idx++) {
                preparedStatement.setObject(idx, data.get(idx - 1));
            }

            Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                    preparedStatement.toString());

            rowInserted = preparedStatement.executeUpdate();

            if (rowInserted > 0) {
                Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                        "insertDataIntoTable() method finished with true!");
                return true;
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            Logger.getLogger(CreateNewCompanyScreen.class.getName()).log(Level.SEVERE, null, ex);
        }

        Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                "insertDataIntoTable() method finished with false!");

        return false;
    }

    public static boolean updateTableDataCompany(String tableName, List<Object> data,
            List<Object> columnNames, Object identifier, Object key) {

        Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                "updateTableData() method called!");

        String query = "UPDATE " + tableName + " SET ";
        int rowUpdated;

        for (int idx = 0; idx < data.size(); idx++) {
            if (idx == data.size() - 1) {
                query += columnNames.get(idx) + "=? ";
            } else {
                query += columnNames.get(idx) + "=?, ";
            }
        }

        query += "WHERE " + identifier.toString() + "=?";
        Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                query);

        Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                "updateTableData() method, query compiled!");

        try {
            dbConnection = DBConnect.connectCopy();
            preparedStatement = dbConnection.prepareStatement(query);

            for (int idx = 0; idx < data.size(); idx++) {
                preparedStatement.setObject(idx + 1, data.get(idx));
            }

            preparedStatement.setObject(data.size() + 1, key);

            rowUpdated = preparedStatement.executeUpdate();

            Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                    preparedStatement.toString());

            if (rowUpdated > 0) {
                Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                        "updateTableData() method finished with true!");
                return true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(DBController.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());

        }

        Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                "updateTableData() method finished with false!");

        return false;
    }

    public static boolean updateTableData(String tableName, List<Object> data,
            List<Object> columnNames, Object identifier, Object key) {

        Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                "updateTableData() method called!");

        String query = "UPDATE " + tableName + " SET ";
        int rowUpdated;

        for (int idx = 0; idx < data.size(); idx++) {
            if (idx == data.size() - 1) {
                query += columnNames.get(idx) + "=? ";
            } else {
                query += columnNames.get(idx) + "=?, ";
            }
        }

        query += "WHERE " + identifier.toString() + "=?";
        Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                query);

        Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                "updateTableData() method, query compiled!");

        try {

            preparedStatement = dbConnection.prepareStatement(query);

            for (int idx = 0; idx < data.size(); idx++) {
                preparedStatement.setObject(idx + 1, data.get(idx));
            }

            preparedStatement.setObject(data.size() + 1, key);

            rowUpdated = preparedStatement.executeUpdate();

            Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                    preparedStatement.toString());

            if (rowUpdated > 0) {
                Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                        "updateTableData() method finished with true!");
                return true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(DBController.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());

        }

        Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                "updateTableData() method finished with false!");

        return false;
    }

    public static boolean removeDataFromTable(String tableName, Object identifier, Object key) {

        Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                "removeDataFromTable() method called!");

        int rowDeleted;
        String query = "DELETE FROM " + tableName + " WHERE "
                + identifier.toString() + " = " + key.toString();

        try {

            preparedStatement = dbConnection.prepareStatement(query);

            rowDeleted = preparedStatement.executeUpdate();

            if (rowDeleted > 0) {
                Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                        "removeDataFromTable() method finished with true!");
                return true;
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }

        Logger.getLogger(DBController.class.getName()).log(Level.INFO,
                "removeDataFromTable() method finished with false!");

        return false;
    }

}
