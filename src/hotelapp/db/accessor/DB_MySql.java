/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelapp.db.accessor;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author tim
 */
public class DB_MySql implements DBAccessor {
    
    private Connection conn;
    
    public DB_MySql() {
        
    }
    
    /**
     *
     * @param driver
     * @param url
     * @param user
     * @param pass
     */
    @Override
    public void openConn(String driver, String url, String user, String pass) throws SQLException, ClassNotFoundException {
        if(url == null) throw new IllegalArgumentException("Url must not be null");
        Class.forName(driver);
        conn = DriverManager.getConnection(url, user, pass);
    }
    
    @Override
    public void close() throws SQLException {
        conn.close();
    }
    
    @Override
    public List<Map<String, Object>> getAllRecords(String table, boolean keepAlive) throws Exception {
        List<Map<String, Object>> records = new ArrayList<>();
        Statement s = null;
        ResultSet rs = null;
        ResultSetMetaData metaData = null;
        String statement = "SELECT * FROM " + table;
        try {
            s = conn.createStatement();
            rs = s.executeQuery(statement);
            metaData = rs.getMetaData();
            int fields = metaData.getColumnCount();
            Map<String, Object> temp = new HashMap<>();
            while(rs.next()) {
                for(int i = 1; i <= fields; i++) {
                    temp.put(metaData.getColumnName(i), rs.getObject(i));
                }
                
                records.add(temp);
            }
        } catch(Exception e) {
            throw e;
        } finally {
            try {
                s.close();
                if(!keepAlive) conn.close();
            } catch(Exception e) {
                throw e;
            }
        }
        return records;
    }
    
    /**
     * Inserts a record of strings into specified table
     * @param table table to do the insert on
     * @param keys the keys
     * @param values the values
     * @param keepAlive whether or not to keep alive
     * @throws Exception 
     */
    @Override
    public void insertRecord(String table, String[] keys, String[] values, boolean keepAlive) throws Exception {
        PreparedStatement ps = null;
        String insertString = "INSERT INTO " + table + "(";
        
        for(int i = 1; i <= keys.length; i++) {
            if(i == keys.length) {
                insertString += keys[i];
            } else {
            insertString += keys[i] + ", ";
            }
        }
        insertString += ") VALUES (";
        for(int i = 1; i <= keys.length; i++) {
            if(i == keys.length) {
                insertString += "?)";
            } else {
            insertString += "?,";
            }
        }
        ps = conn.prepareStatement(insertString);
        for(int i = 0; i < keys.length; i++) {
            ps.setString(i, values[i]);
        }
        ps.executeUpdate();
        ps.close();
        if(!keepAlive) conn.close();
    }
    
    @Override
    public void updateRecord(String table, String pk, int id, String[] keys, String[] values, boolean keepAlive) throws Exception {
        PreparedStatement ps = null;
        String insertString = "UPDATE " + table + " SET (";
        
        for(int i = 1; i <= keys.length; i++) {
            if(i == keys.length) {
                insertString += keys[i];
            } else {
            insertString += keys[i] + ", ";
            }
        }
        insertString += ") VALUES (";
        for(int i = 0; i < keys.length; i++) {
            if(i == keys.length) {
                insertString += "?)";
            } else {
            insertString += "?,";
            }
        }
        insertString += " WHERE " + pk + " = " + id; 
        ps = conn.prepareStatement(insertString);
        for(int i = 1; i <= keys.length; i++) {
            ps.setString(i, values[i]);
        }
        ps.executeUpdate();
        ps.close();
        if(!keepAlive) conn.close();
    }
    
    @Override
    public void deleteRecord(String table, String pk, int id, boolean keepAlive) throws Exception{
        String sql = "DELETE " + table + " WHERE " + pk + " = ?";
        
        PreparedStatement ps = conn.prepareStatement(sql);
        
        ps.setInt(1, id);
        
        ps.executeUpdate();
        ps.close();
        if(!keepAlive) conn.close();
    }
    
    public static void main(String args[]) {
        DB_MySql db = new DB_MySql();
        /*db.openConn("com.mysql.jdbc.Driver", 
           "jdbc:mysql://localhost:3306/employeedb", 
           "root", "");
            
        List<Map<String, Object>> records = db.getAllRecords("employee", true);
        for(Map<String, Object> record : records) {
            System.out.println(record);
        }*/
        try {
        String[] a = {"x", "y", "z"};
        String[] b = {"a", "b", "c"};
        db.insertRecord("test", a, b, true);
        } catch(Exception e) {
            //Something
        }
    }
}
