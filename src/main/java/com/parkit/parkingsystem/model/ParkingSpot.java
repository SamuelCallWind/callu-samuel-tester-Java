package com.parkit.parkingsystem.model;

import com.parkit.parkingsystem.constants.ParkingType;

import java.util.Date;

public class ParkingSpot {
    private int number;
    private ParkingType parkingType;
    private boolean isAvailable;
    private String vehicleRegNumber;
    private Date inTime;

    public ParkingSpot(int number, ParkingType parkingType, boolean isAvailable) {
        this.number = number;
        this.parkingType = parkingType;
        this.isAvailable = isAvailable;
        this.inTime = inTime;
    }
    public ParkingSpot(int number, ParkingType parkingType, boolean isAvailable, Date inTime) {
        this.number = number;
        this.parkingType = parkingType;
        this.isAvailable = isAvailable;
        this.inTime = inTime;
    }

    public int getId() {
        return number;
    }

    public void setId(int number) {
        this.number = number;
    }

    public ParkingType getParkingType() {
        return parkingType;
    }

    public void setParkingType(ParkingType parkingType) {
        this.parkingType = parkingType;
    }

    public boolean isAvailable() {
        return isAvailable;
    }
    public String getRegistrationNumber() {
        return vehicleRegNumber;
    }
    public void setRegistrationNumber(String vehicleRegNumber) {
        this.vehicleRegNumber = vehicleRegNumber;
    }


    public Date getInTime() {
        return inTime;
    }
    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingSpot that = (ParkingSpot) o;
        return number == that.number;
    }

    @Override
    public int hashCode() {
        return number;
    }
}
