package com.example.kjr_app;

public class DocInfo {
    private int doc_id;
    private String doc_name;
    private String nickName;
    private String phone;
    private String password;
    private String sex;//0男 1女，2未知
    private String email;
    private String speciality;
    private String update_time;
    private String create_time;
    private String del_flag;
    private String avatar;
    private String remark;
    private String login_date;
    private String status;

    public DocInfo(int doc_id, String doc_name, String nickName, String phone, String password, String sex, String email, String speciality, String update_time, String create_time, String del_flag, String avatar, String remark, String login_date, String status) {
        this.doc_id = doc_id;
        this.doc_name = doc_name;
        this.nickName = nickName;
        this.phone = phone;
        this.password = password;
        this.sex = sex;
        this.email = email;
        this.speciality = speciality;
        this.update_time = update_time;
        this.create_time = create_time;
        this.del_flag = del_flag;
        this.avatar = avatar;
        this.remark = remark;
        this.login_date = login_date;
        this.status = status;
    }

    public int getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(int doc_id) {
        this.doc_id = doc_id;
    }

    public String getDoc_name() {
        return doc_name;
    }

    public void setDoc_name(String doc_name) {
        this.doc_name = doc_name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getDel_flag() {
        return del_flag;
    }

    public void setDel_flag(String del_flag) {
        this.del_flag = del_flag;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLogin_date() {
        return login_date;
    }

    public void setLogin_date(String login_date) {
        this.login_date = login_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
