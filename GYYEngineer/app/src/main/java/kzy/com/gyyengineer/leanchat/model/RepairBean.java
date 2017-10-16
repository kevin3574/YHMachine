package kzy.com.gyyengineer.leanchat.model;

/**
 * 创建日期：2017/6/29 0029 on 11:35
 * 描述：
 * 作者：赵金祥  Administrator
 */

public class RepairBean {

    /**
     * cloudOrder : 773f1898
     * status : 1
     * marchineExit : 0
     * repairOrder : 20147856
     * data : 相关报修信息提交成功
     */

    private String cloudOrder;
    private String status;
    private int marchineExit;
    private String repairOrder;
    private String data;

    public String getCloudOrder() {
        return cloudOrder;
    }

    public void setCloudOrder(String cloudOrder) {
        this.cloudOrder = cloudOrder;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getMarchineExit() {
        return marchineExit;
    }

    public void setMarchineExit(int marchineExit) {
        this.marchineExit = marchineExit;
    }

    public String getRepairOrder() {
        return repairOrder;
    }

    public void setRepairOrder(String repairOrder) {
        this.repairOrder = repairOrder;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RepairBean{" +
                "cloudOrder='" + cloudOrder + '\'' +
                ", status='" + status + '\'' +
                ", marchineExit=" + marchineExit +
                ", repairOrder='" + repairOrder + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
