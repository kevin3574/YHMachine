package kzy.com.gyyengineer.model;

import java.util.List;

/**
 * 创建日期：2017/6/22 0022 on 11:01
 * 描述：历史维修bean
 * 作者：赵金祥  Administrator
 */

public class HistoryDataBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * cloudOrder : 773f1898
         * evaluateStatus : 1
         * marchineCode : 1000001
         * recordTime : 2017-02-16
         * repairDetails : 程序自行车
         */

        private String recordDetails;
        private String cloudOrder;
        private String evaluateStatus;
        private String marchineCode;
        private String recordTime;
        private String repairDetails;

        public String getRecordDetails() {
            return recordDetails;
        }

        public void setRecordDetails(String recordDetails) {
            this.recordDetails = recordDetails;
        }

        public String getCloudOrder() {
            return cloudOrder;
        }

        public void setCloudOrder(String cloudOrder) {
            this.cloudOrder = cloudOrder;
        }

        public String getEvaluateStatus() {
            return evaluateStatus;
        }

        public void setEvaluateStatus(String evaluateStatus) {
            this.evaluateStatus = evaluateStatus;
        }

        public String getMarchineCode() {
            return marchineCode;
        }

        public void setMarchineCode(String marchineCode) {
            this.marchineCode = marchineCode;
        }

        public String getRecordTime() {
            return recordTime;
        }

        public void setRecordTime(String recordTime) {
            this.recordTime = recordTime;
        }

        public String getRepairDetails() {
            return repairDetails;
        }

        public void setRepairDetails(String repairDetails) {
            this.repairDetails = repairDetails;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "cloudOrder='" + cloudOrder + '\'' +
                    ", evaluateStatus='" + evaluateStatus + '\'' +
                    ", marchineCode='" + marchineCode + '\'' +
                    ", recordTime='" + recordTime + '\'' +
                    ", repairDetails='" + repairDetails + '\'' +
                    ", recordDetails='" + recordDetails + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "HistoryDataBean{" +
                "data=" + data +
                '}';
    }
}
