package kzy.com.gyyengineer.model;

import java.util.List;

/**
 * 创建日期：2017/6/22 0022 on 9:45
 * 描述：当前报修数据bean
 * 作者：赵金祥  Administrator
 */

public class CurrentDataBean {


    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String cloudAddress) {
        this.status = cloudAddress;
    }

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * cloudAddress : 陕西省西安市雁塔区沣惠南路
         * cloudOrder : 5fa60c71
         * faultDescription : 快乐拉进来
         * marchineType : 工业机
         * orderStatus : 0
         * repairTime : 2017-06-27
         */

        private String cloudAddress;
        private String cloudOrder;
        private String machineBrand;
        private String faultDescription;
        private String marchineType;
        private String orderStatus;
        private String repairTime;

        public String getCloudAddress() {
            return cloudAddress;
        }

        public void setCloudAddress(String cloudAddress) {
            this.cloudAddress = cloudAddress;
        }

        public String getMachineBrand() {
            return machineBrand;
        }

        public void setMachineBrand(String machineBrand) {
            this.machineBrand = machineBrand;
        }

        public String getCloudOrder() {
            return cloudOrder;
        }

        public void setCloudOrder(String cloudOrder) {
            this.cloudOrder = cloudOrder;
        }

        public String getFaultDescription() {
            return faultDescription;
        }

        public void setFaultDescription(String faultDescription) {
            this.faultDescription = faultDescription;
        }

        public String getMarchineType() {
            return marchineType;
        }

        public void setMarchineType(String marchineType) {
            this.marchineType = marchineType;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getRepairTime() {
            return repairTime;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "cloudAddress='" + cloudAddress + '\'' +
                    ", cloudOrder='" + cloudOrder + '\'' +
                    ", faultDescription='" + faultDescription + '\'' +
                    ", marchineType='" + marchineType + '\'' +
                    ", orderStatus='" + orderStatus + '\'' +
                    ", repairTime='" + repairTime + '\'' +
                    ", machineBrand='" + machineBrand + '\'' +
                    '}';
        }

        public void setRepairTime(String repairTime) {

            this.repairTime = repairTime;
        }
    }

    @Override
    public String toString() {
        return "CurrentDataBean{" +
                "status='" + status + '\'' +
                ", data=" + data +
                '}';
    }
}
