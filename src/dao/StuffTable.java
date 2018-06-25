package dao;

import app.Employee;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;

public class StuffTable {

    private ArrayList<Employee> employees = new ArrayList<>();
    private ArrayList<String> departments = new ArrayList<>();
    private ArrayList<String> uniqueDepartments;

    public StuffTable() {

        String dbPath = "jdbc:ucanaccess://C:/BDOrderTyres.mdb";
        String sqlQuery = "SELECT EmpName,Department FROM Staff";

        try {
            Connection conn = DriverManager.getConnection(dbPath);
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery(sqlQuery);

            while (rs.next()) {
                String name = rs.getString("EmpName");
                String department = rs.getString("Department");
                this.departments.add(department);
                employees.add(new Employee(name, department));
            }

            conn.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        uniqueDepartments = new ArrayList<>(new HashSet<String>(this.departments));
    }

    public ArrayList<String> getUniqueDepartmentNames() {
        return uniqueDepartments;
    }

    public ArrayList<String> getDepartmentEmployees(String department) {

        ArrayList<String> result = new ArrayList<>();

        for(Employee emp : employees) {
            if (emp.getDepartment().equals(department)) {
                result.add(emp.getName());
            }
        }

        return result;
    }
}
