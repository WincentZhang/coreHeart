package com.bupt.heartarea.bean;

/**
 * Created by yuqing on 2017/4/4.
 */
public class HistoryDataItemBean {
    private String date;
    private String time;
    private int pressure;
    private int heart_rate;
    private int blood_oxygen;
    private int blood_pressure_high;
    private int blood_pressure_low;
    // 房颤风险
    private double af;

    public HistoryDataItemBean() {
    }

    public HistoryDataItemBean(String date, String time, int pressure, int heart_rate, int blood_oxygen, int blood_pressure_high, int blood_pressure_low) {
        this.date = date;
        this.time = time;
        this.pressure = pressure;
        this.heart_rate = heart_rate;
        this.blood_oxygen = blood_oxygen;
        this.blood_pressure_high = blood_pressure_high;
        this.blood_pressure_low = blood_pressure_low;
    }

    public HistoryDataItemBean(String date, String time, int pressure, int heart_rate, int blood_oxygen) {
        this.date = date;
        this.time = time;
        this.pressure = pressure;
        this.heart_rate = heart_rate;
        this.blood_oxygen = blood_oxygen;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getHeart_rate() {
        return heart_rate;
    }

    public void setHeart_rate(int heart_rate) {
        this.heart_rate = heart_rate;
    }

    public int getBlood_oxygen() {
        return blood_oxygen;
    }

    public void setBlood_oxygen(int blood_oxygen) {
        this.blood_oxygen = blood_oxygen;
    }

    public int getBlood_pressure_high() {
        return blood_pressure_high;
    }

    public void setBlood_pressure_high(int blood_pressure_high) {
        this.blood_pressure_high = blood_pressure_high;
    }

    public int getBlood_pressure_low() {
        return blood_pressure_low;
    }

    public void setBlood_pressure_low(int blood_pressure_low) {
        this.blood_pressure_low = blood_pressure_low;
    }

    public double getAf() {
        return af;
    }

    public void setAf(double af) {
        this.af = af;
    }

    @Override
    public String toString() {
        return "HistoryDataItemBean{" +
                "date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", pressure=" + pressure +
                ", heart_rate=" + heart_rate +
                ", blood_oxygen=" + blood_oxygen +
                '}';
    }
}
