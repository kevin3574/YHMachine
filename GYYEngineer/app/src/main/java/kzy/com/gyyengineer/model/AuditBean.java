package kzy.com.gyyengineer.model;

/**
 * 创建日期：2017/6/16 0016 on 13:31
 * 描述：工程师审核bean
 * 作者：赵金祥  Administrator
 */

public class AuditBean {
    private String telephone;
    private String password;
    private String userName;
    private String userID;
    private String image;

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "AuditBean{" +
                "telephone='" + telephone + '\'' +
                ", password='" + password + '\'' +
                ", userName='" + userName + '\'' +
                ", userID='" + userID + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
