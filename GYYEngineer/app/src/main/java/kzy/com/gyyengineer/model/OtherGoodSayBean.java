package kzy.com.gyyengineer.model;

import java.util.List;

/**
 * 创建日期：2017/7/12 0012 on 13:22
 * 描述：
 * 作者：赵金祥  Administrator
 */

public class OtherGoodSayBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * totalNum : 2
         * rank : 1
         * rate : 50
         * engineer : 崔文
         * goodPraise : 1
         * telephone : 13335306262
         * engineerImage :
         */

        private String totalNum;
        private String rank;
        private String rate;
        private String engineer;
        private String goodPraise;
        private String telephone;
        private String engineerImage;

        public String getTotalNum() {
            return totalNum;
        }

        public void setTotalNum(String totalNum) {
            this.totalNum = totalNum;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getEngineer() {
            return engineer;
        }

        public void setEngineer(String engineer) {
            this.engineer = engineer;
        }

        public String getGoodPraise() {
            return goodPraise;
        }

        public void setGoodPraise(String goodPraise) {
            this.goodPraise = goodPraise;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getEngineerImage() {
            return engineerImage;
        }

        public void setEngineerImage(String engineerImage) {
            this.engineerImage = engineerImage;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "totalNum=" + totalNum +
                    ", rank=" + rank +
                    ", rate=" + rate +
                    ", engineer='" + engineer + '\'' +
                    ", goodPraise=" + goodPraise +
                    ", telephone='" + telephone + '\'' +
                    ", engineerImage='" + engineerImage + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "OtherGoodSayBean{" +
                "data=" + data +
                '}';
    }
}
