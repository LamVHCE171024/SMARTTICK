/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import DB.DBContext;
import Models.Employee;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author NguyenPVT-CE181835
 */
public class EmployeeDAO {

    DBContext db = new DBContext();
    Connection connector = db.getConnection();

    public String getMD5(String input) {

        try {
            // Tạo instance của MessageDigest với thuật toán MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Băm chuỗi đầu vào và trả về kết quả dạng byte[]
            byte[] hashBytes = md.digest(input.getBytes());

            // Chuyển đổi byte[] thành chuỗi hexadecimal
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0'); // Thêm '0' vào đầu nếu cần
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found!", e);
        }
    }

    public ArrayList<Employee> getAllEmployees() {
        ArrayList<Employee> listEmployee = new ArrayList<>();
        String sql = "SELECT * \n"
                + "FROM Employees \n"
                + "WHERE RoleID <> 1;";
        try {
            PreparedStatement pr = connector.prepareStatement(sql);
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                listEmployee.add(new Employee(rs.getInt("EmployeeID"),
                        rs.getString("FullName"),
                        rs.getDate("Birthday"),
                        "",
                        rs.getString("PhoneNumber"),
                        rs.getString("Email"),
                        rs.getString("Gender"),
                        rs.getDate("CreatedDate"),
                        rs.getInt("Status"),
                        rs.getString("Avatar"),
                        rs.getInt("RoleID")
                ));
            }
            return listEmployee;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return listEmployee;
    }

    public Employee getEmployeeById(String id) {
        int empId = Integer.parseInt(id);
        Employee emp = null;
        String sql = "SELECT * FROM Employees WHERE EmployeeID = ?";

        try {
            PreparedStatement pr = connector.prepareStatement(sql);
            pr.setInt(1, empId);
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                emp = new Employee(rs.getInt("EmployeeID"),
                        rs.getString("FullName"),
                        rs.getDate("Birthday"),
                        rs.getString("Password"),
                        rs.getString("PhoneNumber"),
                        rs.getString("Email"),
                        rs.getString("Gender"),
                        rs.getDate("CreatedDate"),
                        rs.getInt("Status"),
                        rs.getString("Avatar"),
                        rs.getInt("RoleID")
                );
            }
            return emp;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return emp;
    }

    public int UpdateEmployee(Employee employee) {
        int effectRows = 0;
        String sql = "Update Employees SET FullName = ?, Birthday = ?, Password = ?, PhoneNumber = ?, Email = ?, Gender = ?, CreatedDate = ?, Status = ?, Avatar = ?, RoleID = ? WHERE EmployeeID = ?";
        try {
            PreparedStatement pr = connector.prepareStatement(sql);
            pr.setString(1, employee.getFullname());
            pr.setDate(2, employee.getBirthday());
            pr.setString(3, employee.getPassword());
            pr.setString(4, employee.getPhoneNumber());
            pr.setString(5, employee.getEmail());
            pr.setString(6, employee.getGender());
            pr.setDate(7, employee.getCreatedDate());
            pr.setInt(8, employee.getStatus());
            pr.setString(9, employee.getAvatar());
            pr.setInt(10, employee.getRoleId());
            pr.setInt(11, employee.getEmployeeId());
            effectRows = pr.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return effectRows;
    }

    public int AddEmployee(Employee employee) {
        int effectRows = 0;
        String sql = "INSERT INTO Employees (FullName, Birthday, [Password], PhoneNumber, Email, Gender, CreatedDate, Status, Avatar, RoleID) VALUES "
                + "(?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pr = connector.prepareStatement(sql);
            pr.setString(1, employee.getFullname());
            pr.setDate(2, employee.getBirthday());
            pr.setString(3, getMD5(employee.getPassword()));
            pr.setString(4, employee.getPhoneNumber());
            pr.setString(5, employee.getEmail());
            pr.setString(6, employee.getGender());
            pr.setDate(7, employee.getCreatedDate());
            pr.setInt(8, employee.getStatus());
            pr.setString(9, employee.getAvatar());
            pr.setInt(10, employee.getRoleId());
            effectRows = pr.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return effectRows;
    }

    public Employee employeeLogin(String email, String password) {
        Employee emp = null;
        try {
            PreparedStatement pr = connector.prepareStatement("SELECT * FROM Employees WHERE Email = ? AND Password = ?");

            pr.setString(1, email);
            pr.setString(2, getMD5(password));

            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                emp = new Employee(rs.getInt("EmployeeID"),
                        rs.getString("FullName"),
                        rs.getDate("Birthday"),
                        "",
                        rs.getString("PhoneNumber"),
                        rs.getString("Email"),
                        rs.getString("Gender"),
                        rs.getDate("CreatedDate"),
                        rs.getInt("Status"),
                        rs.getString("Avatar"),
                        rs.getInt("RoleID")
                );
            }
            return emp;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return emp;
    }

    public int blockEmployee(int id) {
        int effectRows = 0;
        String sql = "Update Employees SET Status = 0 WHERE EmployeeID = ?;";
        try {
            PreparedStatement pr = connector.prepareStatement(sql);
            pr.setInt(1, id);
            effectRows = pr.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return effectRows;
    }

    public int checkEmailActive(String email) {
        String sql = "SELECT * FROM Employees WHERE Email = ? AND Status = 1";
        try {
            PreparedStatement pr = connector.prepareStatement(sql);
            pr.setString(1, email);
            ResultSet rs = pr.executeQuery();
            if (rs.next()) {
                return rs.getInt("EmployeeID");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return 0;
    }

    public int updateEmployeeProfile(Employee employee) {
        int effectRow = 0;
        String sql = "Update Employees SET FullName = ?, Birthday = ?, PhoneNumber = ?, Gender = ?, Avatar = ? WHERE EmployeeID = ?";
        try {
            PreparedStatement pr = connector.prepareStatement(sql);
            pr.setString(1, employee.getFullname());
            pr.setDate(2, employee.getBirthday());
            pr.setString(3, employee.getPhoneNumber());
            pr.setString(4, employee.getGender());
            pr.setString(5, employee.getAvatar());
            pr.setInt(6, employee.getEmployeeId());
            effectRow = pr.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
            return 0;
        }
        return effectRow;
    }

    public ArrayList<Employee> searchEmployeesByName(String employeeName) {
        ArrayList<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM Employees WHERE fullname LIKE ? AND RoleID <> 1";
        try {
            PreparedStatement pr = connector.prepareStatement(sql);
            pr.setString(1, "%" + employeeName + "%"); // Tìm kiếm gần đúng
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                employees.add(new Employee(
                        rs.getInt("EmployeeID"),
                        rs.getString("FullName"),
                        rs.getDate("Birthday"),
                        "",
                        rs.getString("PhoneNumber"),
                        rs.getString("Email"),
                        rs.getString("Gender"),
                        rs.getDate("CreatedDate"),
                        rs.getInt("Status"),
                        rs.getString("Avatar"),
                        rs.getInt("RoleID")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return employees;
    }

    public boolean isEmailExists(String email) {
        String sql = "SELECT COUNT(*) FROM Employees WHERE Email = ?";
        try {
            PreparedStatement pr = connector.prepareStatement(sql);
            pr.setString(1, email);
            ResultSet rs = pr.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }

    public int checkPassword(int id, String password) {
        try {
            PreparedStatement pr = connector.prepareStatement("SELECT * FROM Employees WHERE EmployeeID = ? AND Password = ?");

            pr.setInt(1, id);
            pr.setString(2, getMD5(password));

            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                return 1;
            }
        } catch (SQLException e) {
            System.out.println(e);
            return 0;
        }
        return 0;
    }

    public int changePassword(int id, String password) {
        int effectRow = 0;
        String sql = "Update Employees SET Password = ? WHERE EmployeeID = ?";
        try {
            PreparedStatement pr = connector.prepareStatement(sql);
            pr.setInt(2, id);
            pr.setString(1, getMD5(password));
            effectRow = pr.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
            return 0;
        }
        return effectRow;
    }

    public static void main(String[] args) {
        EmployeeDAO emDAO = new EmployeeDAO();
        int i = emDAO.blockEmployee(1);
        System.out.println(i);
    }
}
