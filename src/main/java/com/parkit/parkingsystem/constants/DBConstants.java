package com.parkit.parkingsystem.constants;

public class DBConstants {

    public static final String GET_NEXT_PARKING_SPOT = "select min(PARKING_NUMBER) from parking where AVAILABLE = 1 and TYPE = ?";
    public static final String UPDATE_PARKING_SPOT = "update parking set available = ?, VEHICLE_REG_NUMBER = ?, IN_TIME = ? where PARKING_NUMBER = ?";
    public static final String GET_PARKING_SPOT = "select AVAILABLE from parking where PARKING_NUMBER = ?";
    public static final String GET_TIMES_PARKED = "select TOTAL_TIMES_PARKED from user_stats where VEHICLE_REG_NUMBER = ?";
    public static final String UPDATE_TIMES_PARKED = "INSERT INTO user_stats (VEHICLE_REG_NUMBER, TOTAL_TIMES_PARKED) VALUES (?, 1) ON DUPLICATE KEY UPDATE TOTAL_TIMES_PARKED = TOTAL_TIMES_PARKED + 1";
    public static final String REMOVE_CAR_FROM_PARKING = "update parking set VEHICLE_REG_NUMBER = '', IN_TIME = null, OUT_TIME = null, AVAILABLE = 1 where VEHICLE_REG_NUMBER = ?";



    public static final String SAVE_TICKET = "insert into ticket(PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME, TYPE) values(?,?,?,?,?,?)";
    public static final String UPDATE_TICKET = "update ticket set PRICE=?, OUT_TIME=? where ID=?";
    public static final String GET_TICKET = "select t.PARKING_NUMBER, t.ID, t.PRICE, t.IN_TIME, t.OUT_TIME, p.TYPE from ticket t,parking p where p.parking_number = t.parking_number and t.VEHICLE_REG_NUMBER=? order by t.IN_TIME  limit 1";
    public static final String UPDATE_PRICE_TICKET = "update ticket set PRICE = ? where VEHICLE_REG_NUMBER = ?";

}
