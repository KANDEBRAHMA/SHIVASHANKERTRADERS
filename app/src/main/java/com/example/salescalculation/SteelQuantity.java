package com.example.salescalculation;

import java.util.Date;

public class SteelQuantity {
    private int length8mm;
    private int length10mm;
    private int length12mm;
    private int length16mm;
    private int length20mm;
    private double billAmount;
    private String billDate;
    private int billNum;

    public int getBillNum() {
        return billNum;
    }

    public void setBillNum(int billNum) {
        this.billNum = billNum;
    }

    public SteelQuantity() {
    }

    public int getLength8mm() {
        return length8mm;
    }

    public void setLength8mm(int length8mm) {
        this.length8mm = length8mm;
    }

    public int getLength10mm() {
        return length10mm;
    }

    public void setLength10mm(int length10mm) {
        this.length10mm = length10mm;
    }

    public int getLength12mm() {
        return length12mm;
    }

    public void setLength12mm(int length12mm) {
        this.length12mm = length12mm;
    }

    public int getLength16mm() {
        return length16mm;
    }

    public void setLength16mm(int length16mm) {
        this.length16mm = length16mm;
    }

    public int getLength20mm() {
        return length20mm;
    }

    public void setLength20mm(int length20mm) {
        this.length20mm = length20mm;
    }

    public double getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(double billAmount) {
        this.billAmount = billAmount;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }
}
