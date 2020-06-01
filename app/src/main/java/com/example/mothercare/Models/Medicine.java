package com.example.mothercare.Models;

public class Medicine {
    public String medicineName, medicineDose, pharmacyName, pharmacyID;
    public int price, stockQuantity, cartQuantity;
    public String medicineID;

    public Medicine() {
    }

    public Medicine(String medicineID, String medicineName, String pharmacyName, String medicineDose, int price, int stockQuantity, String pharmacyID) {
        this.medicineName = medicineName;
        this.medicineDose = medicineDose;
        this.price = price;
        this.medicineID = medicineID;
        this.pharmacyName = pharmacyName;
        this.stockQuantity = stockQuantity;
        this.pharmacyID = pharmacyID;
    }

    public void setCartQuantity(int cartQuantity) {
        this.cartQuantity = cartQuantity;
    }

    public void setMedicineID(String medicineID) {
        this.medicineID = medicineID;
    }

    public String getMedicineID() {
        return medicineID;
    }

    public int getCartQuantity() {
        return cartQuantity;
    }
}
