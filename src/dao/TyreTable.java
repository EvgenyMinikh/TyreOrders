package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.util.ArrayList;
import app.Tyre;

public class TyreTable {

    private final int CODE_LENGTH = 7;

    private ArrayList<Tyre> tyres = new ArrayList<>();
    private ArrayList<String> codes = new ArrayList<>();
    private ArrayList<String> types = new ArrayList<>();
    private ArrayList<String> prices = new ArrayList<>();

    private ObservableList<String> obsListCodes;
    private ObservableList<String> obsListTypes;
    private ObservableList<String> obsListPrices;

    public TyreTable() {

        String dbPath = "jdbc:ucanaccess://C:/BDOrderTyres.mdb";
        String sqlQuery = "SELECT * FROM Price";

        try {
            Connection conn = DriverManager.getConnection(dbPath);
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery(sqlQuery);

            while (rs.next()) {
                String code = rs.getString("Code");
                this.codes.add(code);

                String type = rs.getString("Type");
                this.types.add(type);

                String price = rs.getString("Price");
                this.prices.add(price);

                this.tyres.add(new Tyre(code, type, price));
            }

            obsListCodes = FXCollections.observableArrayList(codes);
            obsListTypes = FXCollections.observableArrayList(types);
            obsListPrices = FXCollections.observableArrayList(prices);

            conn.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<String> getObsListCodes() {
        return this.obsListCodes;
    }

    public ObservableList<String> getObsListTypes() {
        return this.obsListTypes;
    }

//    public ObservableList<String> getObsListPrices() {
//        return this.obsListPrices;
//    }
//
//    public ArrayList<Tyre> getTyresFromPriceTable() {
//        return this.tyres;
//    }

    public String[] findPair(String obj) {

        String[] result = new String[2];
        Tyre tmp;
        int fieldNumber = 0;

        if (obj.trim().length() == CODE_LENGTH) {
            tmp = new Tyre(obj, "*", "*");
        } else {
            tmp = new Tyre("*", obj, "*");
            fieldNumber = 1;
        }

        for(Tyre tyre : tyres) {
            if (tyre.equals(tmp)) {
                result[0] = fieldNumber == 1 ? tyre.getTyreCode() : tyre.getTyreType();
                result[1] = tyre.getTyrePrice();
                //System.out.println("fieldNumber: " + fieldNumber + " code: " + tyre.getTyreCode() + " type: " + tyre.getTyreType());
                //System.out.println("RESULT: " + result);
            }
        }

        return result;
    }
}
