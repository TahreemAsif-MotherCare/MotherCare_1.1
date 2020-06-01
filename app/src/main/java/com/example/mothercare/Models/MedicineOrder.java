package com.example.mothercare.Models;

import java.util.ArrayList;

public class MedicineOrder {
    public String orderID;
    public ArrayList<Medicine> medicine;
    public String orderDate;
    public int orderPrice;
    public Patient patient;

    public MedicineOrder() {
    }

    public MedicineOrder(String orderID, ArrayList<Medicine> medicine, String orderDate,int orderPrice, Patient patient) {
        this.orderID = orderID;
        this.medicine = medicine;
        this.orderDate = orderDate;
        this.orderPrice = orderPrice;
        this.patient = patient;
    }
}
