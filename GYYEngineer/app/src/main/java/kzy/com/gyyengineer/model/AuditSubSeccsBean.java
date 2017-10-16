package kzy.com.gyyengineer.model;

/**
 * 创建日期：2017/6/27 0027 on 10:09
 * 描述：
 * 作者：赵金祥  Administrator
 */

public class AuditSubSeccsBean {
    private String status;
    private String data;
    private String userName;
    private String telephone;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public String toString() {
        return "AuditSubSeccsBean{" +
                "status='" + status + '\'' +
                ", data='" + data + '\'' +
                ", userName='" + userName + '\'' +
                ", telephone='" + telephone + '\'' +
                '}';
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
