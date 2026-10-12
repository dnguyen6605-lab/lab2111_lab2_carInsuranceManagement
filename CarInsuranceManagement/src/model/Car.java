package model;

import java.io.Serializable;
import java.time.LocalDate;

public class Car implements Serializable{
    private static final long serialVersionUID = 1L;
    private String lisencePlate;
    private String owner;
    private String brand;
    private long value;
    private LocalDate registrationDate;
    private String registrationPlace;
    private int vehicleType;
    
    // contructor

    public Car() {
    }

    public Car(String lisencePlate, String owner, String brand, long value,
            LocalDate registrationDate, String registrationPlace, int vehicleType) {
        this.lisencePlate = lisencePlate;
        this.owner = owner;
        this.brand = brand;
        this.value = value;
        this.registrationDate = registrationDate;
        this.registrationPlace = registrationPlace;
        this.vehicleType = vehicleType;
    }

    public String getLisencePlate() {
        return lisencePlate;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getRegistrationPlace() {
        return registrationPlace;
    }

    public void setRegistrationPlace(String registrationPlace) {
        this.registrationPlace = registrationPlace;
    }

    public int getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(int vehicleType) {
        this.vehicleType = vehicleType;
    }

    private static final java.time.format.DateTimeFormatter DATE_FORMAT =
            java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy");

    @Override
    public String toString() {
        return String.format(
                "License plate: %s%nOwner: %s%nBrand: %s%nValue: $ %,d%nRegistration date: %s%nRegistration place: %s%nVehicle type: %d",
                lisencePlate,
                owner,
                brand,
                value,
                (registrationDate != null ? registrationDate.format(DATE_FORMAT) : "N/A"),
                registrationPlace,
                vehicleType);
    }
}
