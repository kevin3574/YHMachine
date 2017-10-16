package kzy.com.gyyengineer.model;

/**
 * 项目名称：DoctorMhealth
 * 类描述：用户注册
 * 创建人：赵金祥
 * 修改备注：
 */
public class UserReg {

    private String status;
    private String userName;
    private String telephone;
    private String data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "UserReg{" +
                "status='" + status + '\'' +
                ", userName='" + userName + '\'' +
                ", telephone='" + telephone + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
