package kzy.com.gyyengineer.model;

/**
 * 项目名称：DoctorMhealth
 * 类描述：找回密码
 * 创建人：赵金祥
 * 修改备注：
 */
public class ForGetpass {

    /**
     * status : 1
     * data : 密码存在！
     * password : e10adc3949ba59abbe56e057f20f883e
     */

    private String status;
    private String data;
    private String password;

    public void setStatus(String status) {
        this.status = status;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public String getData() {
        return data;
    }

    public String getPassword() {
        return password;
    }
}
