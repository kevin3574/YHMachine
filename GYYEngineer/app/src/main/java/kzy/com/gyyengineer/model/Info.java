package kzy.com.gyyengineer.model;

/**
 * 项目名称：DoctorMhealth
 * 类描述：图片上传
 * 创建人：赵金祥
 * 修改人：Administrator
 * 修改备注：
 */
public class Info {

    /**
     * status : 1
     * data : 图片上传成功！
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
}
