package kzy.com.gyyengineer.model;

/**
 * 创建日期：2017/7/8 0008 on 10:21
 * 描述：
 * 作者：赵金祥  Administrator
 */

public class MarchineDataBean {

    /**
     * dischargePressure : 45℃
     * environment : 23℃
     * voltage : 1V21V4V
     * emptyPressure : 9
     * loadTime : 6Hrs
     * oilDivPressure : 6
     * loadPressure : 34bar
     * dischargeTemper : 15℃
     * electricity : 9bar9V
     * runTime : 300Hrs
     * oilPressure : 7
     * miniPressure : 21bar
     */

    private String dischargePressure;
    private String environment;
    private String voltage;
    private String emptyPressure;
    private String loadTime;
    private String oilDivPressure;
    private String loadPressure;
    private String dischargeTemper;
    private String electricity;
    private String runTime;
    private String oilPressure;
    private String miniPressure;

    public String getDischargePressure() {
        return dischargePressure;
    }

    public void setDischargePressure(String dischargePressure) {
        this.dischargePressure = dischargePressure;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getVoltage() {
        return voltage;
    }

    public void setVoltage(String voltage) {
        this.voltage = voltage;
    }

    public String getEmptyPressure() {
        return emptyPressure;
    }

    public void setEmptyPressure(String emptyPressure) {
        this.emptyPressure = emptyPressure;
    }

    public String getLoadTime() {
        return loadTime;
    }

    public void setLoadTime(String loadTime) {
        this.loadTime = loadTime;
    }

    public String getOilDivPressure() {
        return oilDivPressure;
    }

    public void setOilDivPressure(String oilDivPressure) {
        this.oilDivPressure = oilDivPressure;
    }

    public String getLoadPressure() {
        return loadPressure;
    }

    public void setLoadPressure(String loadPressure) {
        this.loadPressure = loadPressure;
    }

    public String getDischargeTemper() {
        return dischargeTemper;
    }

    public void setDischargeTemper(String dischargeTemper) {
        this.dischargeTemper = dischargeTemper;
    }

    public String getElectricity() {
        return electricity;
    }

    public void setElectricity(String electricity) {
        this.electricity = electricity;
    }

    public String getRunTime() {
        return runTime;
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    public String getOilPressure() {
        return oilPressure;
    }

    public void setOilPressure(String oilPressure) {
        this.oilPressure = oilPressure;
    }

    public String getMiniPressure() {
        return miniPressure;
    }

    public void setMiniPressure(String miniPressure) {
        this.miniPressure = miniPressure;
    }

    @Override
    public String toString() {
        return "MarchineDataBean{" +
                "dischargePressure='" + dischargePressure + '\'' +
                ", environment='" + environment + '\'' +
                ", voltage='" + voltage + '\'' +
                ", emptyPressure='" + emptyPressure + '\'' +
                ", loadTime='" + loadTime + '\'' +
                ", oilDivPressure='" + oilDivPressure + '\'' +
                ", loadPressure='" + loadPressure + '\'' +
                ", dischargeTemper='" + dischargeTemper + '\'' +
                ", electricity='" + electricity + '\'' +
                ", runTime='" + runTime + '\'' +
                ", oilPressure='" + oilPressure + '\'' +
                ", miniPressure='" + miniPressure + '\'' +
                '}';
    }
}
