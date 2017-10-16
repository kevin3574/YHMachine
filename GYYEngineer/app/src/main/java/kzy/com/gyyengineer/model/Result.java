package kzy.com.gyyengineer.model;

/**
 * 项目名称：DoctorMhealth
 * 类描述：修改密码
 * 创建人：赵金祥
 * 修改备注：
 */
public class Result {

    /**
     * status : 1
     * data : 密码修改成功！
     */

    private String status;
    private String data;

    public void setStatus(String status) {
        this.status = status;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "status='" + status + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
