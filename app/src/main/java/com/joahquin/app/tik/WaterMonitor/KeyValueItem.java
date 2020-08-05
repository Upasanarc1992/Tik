package com.joahquin.app.tik.WaterMonitor;

public class KeyValueItem {
    String key;
    double value;
    boolean isSelected = false;

    KeyValueItem(String itemKey, double itemValue){
        setKey(itemKey);
        setValue(itemValue);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return "KeyValueItem{" +
                "key='" + key + '\'' +
                ", value=" + value +
                ", isSelected=" + isSelected +
                '}';
    }
}
