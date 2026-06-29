/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;
import java.sql.Date;


/**
 *
 * @author NguyenPVT-CE181835
 */

public class Employee {
    private int employeeId;
    private String fullname;
    private Date birthday;
    private String password;
    private String phoneNumber;
    private String email;
    private String gender;
    private Date createdDate;
    private int Status;
    private String avatar;
    private int roleId;


    public Employee(String fullname, Date birthday, String password, String phoneNumber, String email, String gender, Date createdDate, int Status, String avatar, int roleId) {
        this.fullname = fullname;
        this.birthday = birthday;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.gender = gender;
        this.createdDate = createdDate;
        this.Status = Status;
        this.avatar = avatar;
        this.roleId = roleId;
    }
    

    public Employee(int employeeId, String fullname, Date birthday, String password, String phoneNumber, String email, String gender, Date createdDate, int Status, String avatar, int roleId) {
        this.employeeId = employeeId;
        this.fullname = fullname;
        this.birthday = birthday;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.gender = gender;
        this.createdDate = createdDate;
        this.Status = Status;
        this.avatar = avatar;
        this.roleId = roleId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
    
}
