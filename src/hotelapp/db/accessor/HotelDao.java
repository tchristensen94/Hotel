/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelapp.db.accessor;

import java.util.List;

/**
 *
 * @author Timothy
 */
public class HotelDao {

    private DBAccessor db;

    public HotelDao() {
        db = new DB_MySql();
    }

    public List findHotels() throws Exception {
        openLocalConn();
        return db.getAllRecords("hotels", true);
    }

    public void deleteHotel(String table, String pk, int id) throws Exception {
        openLocalConn();
        db.deleteRecord(table, pk, id, true);
    }
    
    public void newHotel(String table, String[] keys, String[] values) throws Exception {
        openLocalConn();
        db.insertRecord(table, keys, values, true);
    }
    
    public void updateHotel(String table, String[] keys, String[] values, String pk, int id) throws Exception {
        openLocalConn();
        db.updateRecord(table, pk, id, keys, values, true);
    }

    public void setDb(DBAccessor db) {
        this.db = db;
    }

    public DBAccessor getDB() {
        return this.db;
    }

    private void openLocalConn() throws Exception {
        // Each time you perform a new query you must re-open the connection
        db.openConn(
                "com.mysql.jdbc.Driver",
                "jdbc:mysql://localhost:3306/hotel",
                "root", "school");

    }

}
