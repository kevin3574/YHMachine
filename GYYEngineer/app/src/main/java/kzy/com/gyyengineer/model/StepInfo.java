package kzy.com.gyyengineer.model;

/**
 * 项目名称：
 * 类描述：
 * 作者：赵金祥
 */

public class StepInfo {


    /**
     * status : 1
     * data : 用户图像上传成功
     */

    private String status;
    private String data;

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

    @Override
    public String toString() {
        return "StepInfo{" +
                "status='" + status + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
