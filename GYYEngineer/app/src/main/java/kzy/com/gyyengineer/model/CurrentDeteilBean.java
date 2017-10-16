package kzy.com.gyyengineer.model;

import java.util.List;

/**
 * 创建日期：2017/6/24 0024 on 11:27
 * 描述：首页当前维修维修单详情页面Bean
 * 作者：赵金祥  Administrator
 */

public class CurrentDeteilBean {


    private List<RecordDataBean> recordData;
    private List<RepairDataBean> repairData;

    public List<RecordDataBean> getRecordData() {
        return recordData;
    }

    public void setRecordData(List<RecordDataBean> recordData) {
        this.recordData = recordData;
    }

    public List<RepairDataBean> getRepairData() {
        return repairData;
    }

    public void setRepairData(List<RepairDataBean> repairData) {
        this.repairData = repairData;
    }

    public static class RecordDataBean {
        /**
         * engineerName : 崔文
         * recordOrder : f9416bef
         * recordTime : 2017-07-06
         * repairDetail : 建议更换油管
         */

        private String engineerName;
        private String recordOrder;
        private String recordTime;
        private String repairDetail;

        public String getEngineerName() {
            return engineerName;
        }

        public void setEngineerName(String engineerName) {
            this.engineerName = engineerName;
        }

        public String getRecordOrder() {
            return recordOrder;
        }

        public void setRecordOrder(String recordOrder) {
            this.recordOrder = recordOrder;
        }

        public String getRecordTime() {
            return recordTime;
        }

        public void setRecordTime(String recordTime) {
            this.recordTime = recordTime;
        }

        public String getRepairDetail() {
            return repairDetail;
        }

        public void setRepairDetail(String repairDetail) {
            this.repairDetail = repairDetail;
        }
    }

    public static class RepairDataBean {
        /**
         * cloudAddress : 陕西省西安市雁塔区科技六路靠近上海浦东发展银行(西安唐延路支行)
         * cloudOrder : fd6bf99e
         * faultDescription : 快了
         * latitude : 108.889188;
         * longitude : 34.211588
         * machineBrand : 寿力
         * marchineCode : 100078
         * marchineType : 工业机
         * picture : ["http://117.34.105.29:8818/IndustrialCloud/upload/2017060809273700.jpg"]
         * repairTime : 2017-06-08
         * telephone : 18709264359
         * userId : 595c9f69ac502e7589deaec1
         * userName : 薛磊
         */

        private String cloudAddress;
        private String cloudOrder;
        private String faultDescription;
        private String latitude;
        private String longitude;
        private String machineBrand;
        private String marchineCode;
        private String marchineType;
        private String repairTime;
        private String telephone;
        private String userId;
        private String userName;
        private List<String> picture;

        public String getCloudAddress() {
            return cloudAddress;
        }

        public void setCloudAddress(String cloudAddress) {
            this.cloudAddress = cloudAddress;
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

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getMachineBrand() {
            return machineBrand;
        }

        public void setMachineBrand(String machineBrand) {
            this.machineBrand = machineBrand;
        }

        public String getMarchineCode() {
            return marchineCode;
        }

        public void setMarchineCode(String marchineCode) {
            this.marchineCode = marchineCode;
        }

        public String getMarchineType() {
            return marchineType;
        }

        public void setMarchineType(String marchineType) {
            this.marchineType = marchineType;
        }

        public String getRepairTime() {
            return repairTime;
        }

        public void setRepairTime(String repairTime) {
            this.repairTime = repairTime;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public List<String> getPicture() {
            return picture;
        }

        public void setPicture(List<String> picture) {
            this.picture = picture;
        }
    }
}
