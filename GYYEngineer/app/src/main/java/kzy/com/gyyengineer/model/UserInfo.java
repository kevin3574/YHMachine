package kzy.com.gyyengineer.model;

/**
 * 项目名称：DoctorMhealth
 * 类描述：用户登录
 * 创建人：赵金祥
 * 修改人：Administrator
 */
public class UserInfo {
    private String phone;
    private String pass;
    private String qiNumber;
    private String objectId;
    private String name;

    public String getOldpwd() {
        return oldpwd;
    }

    public void setOldpwd(String oldpwd) {
        this.oldpwd = oldpwd;
    }

    private String oldpwd;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getQiNumber() {
        return qiNumber;
    }

    public void setQiNumber(String qiNumber) {
        this.qiNumber = qiNumber;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
