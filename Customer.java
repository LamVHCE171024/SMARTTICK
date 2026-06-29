/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author TuongMPCE180644
 */
public class Customer {

    private int id;
    private String fullName;
    private String password;
    private String birthday;
    private String gender;
    private String phoneNumber;
    private String email;
    private String createDate;
    private String googleId;
    private int isBlock;
    private int isDeleted;
    private String avatar;

    public Customer() {
    }

    public Customer(int id, String fullName, String password, String birthday, String gender, String phoneNumber, String email, String createDate, String googleId, int isBlock, int isDeleted, String avatar) {
        this.id = id;
        this.fullName = fullName;
        this.password = password;
        this.birthday = birthday;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.createDate = createDate;
        this.googleId = googleId;
        this.isBlock = isBlock;
        this.isDeleted = isDeleted;
        this.avatar = avatar;
    }

    public Customer(int id, String fullName, String password, String birthday, String gender, String phoneNumber, String email, String createDate, int isBlock, int isDeleted, String avatar) {
        this.id = id;
        this.fullName = fullName;
        this.password = password;
        this.birthday = birthday;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.createDate = createDate;
        this.isBlock = isBlock;
        this.isDeleted = isDeleted;
        this.avatar = avatar;
    }

    public Customer(int id, String fullName, String password, String birthday, String gender, String phoneNumber, String email, String createDate, int isBlock, String avatar) {
        this.id = id;
        this.fullName = fullName;
        this.password = password;
        this.birthday = birthday;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.createDate = createDate;
        this.isBlock = isBlock;
        this.avatar = avatar;
    }

    public Customer(int id, String fullName, String password, String birthday, String gender, String phoneNumber, String email, String createAt, String avatar) {
        this.id = id;
        this.fullName = fullName;
        this.password = password;
        this.birthday = birthday;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.createDate = createAt;
        this.avatar = avatar;
    }

    //xem danh sach khach hang
    public Customer(int id, String fullName, String email, String phoneNumber, int isBlock) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.isBlock = isBlock;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }
    

    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPassword() {
        return password;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getGender() {
        return gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getCreateAt() {
        return createDate;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getStatus() {
        if (isBlock == 1) {
            return "Blocked";
        }
        return "Activate";
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCreateAt(String createAt) {
        this.createDate = createAt;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public int getIsBlock() {
        return isBlock;
    }

    public void setIsBlock(int isBlock) {
        this.isBlock = isBlock;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

}
