package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }


        //TODO: Some tests are failing here. Need to check if this logic is correct
        double duration = checkForDiscount(ticket);

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }

    public double checkForDiscount(Ticket ticket) {
        long inHour = (ticket.getInTime().getTime() / 60 / 1000) ;
        long outHour = (ticket.getOutTime().getTime() / 60 / 1000);

        if (outHour - 60 < inHour) {
            return 0.75;
        }
        if (inHour + 30 > outHour) {
            return 0;
        } else {
            return ticket.getOutTime().getHours() - ticket.getInTime().getHours();
        }
    }
}