package com.bupt.heartarea.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuqing on 2017/3/24.
 */
public class MeasureData implements Serializable {
    private int heart_rate = -1;
    private int blood_oxygen = -1;
    private int blood_pressure_high = -1;
    private int blood_pressure_low = -1;
    private int current_status = -1;
    private List<Integer> rr_interval = new ArrayList<>();
    private List<Float> data = new ArrayList<>();

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

    public List<Integer> getRr_interval() {
        return rr_interval;
    }

    public void setRr_interval(List<Integer> rr_interval) {
        this.rr_interval = rr_interval;
    }

    public List<Float> getData() {
        return data;
    }

    public void setData(List<Float> data) {
        this.data = data;
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

    public int getCurrent_status() {
        return current_status;
    }

    public void setCurrent_status(int current_status) {
        this.current_status = current_status;
    }
}
