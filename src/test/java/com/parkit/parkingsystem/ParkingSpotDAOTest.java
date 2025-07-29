package com.parkit.parkingsystem;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingSpotDAOTest {

    @Mock
    DataBaseConfig databaseConfig;
    @Mock
    Connection mockConnection;
    @Mock
    PreparedStatement preparedStatement;
    @Mock
    ResultSet resultSet;
    @Mock
    ParkingSpot parkingSpot;


    @Test
    public void getNextAvailableSlot_shouldReturnValueOfOne() throws SQLException, ClassNotFoundException {
        when(databaseConfig.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        ParkingSpotDAO parkingSpotDAO = new ParkingSpotDAO(databaseConfig);
        int result = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);

        assertEquals(-1, result);
    }

    @Test
    public void testUpdateParking_ShouldReturnTrueForCorrectInput() throws SQLException, ClassNotFoundException {
        // mocking parkingSpot Behavior
        Date mockDate = new Date();
        when(parkingSpot.getId()).thenReturn(2);
        when(parkingSpot.getInTime()).thenReturn(mockDate);
        when(parkingSpot.getRegistrationNumber()).thenReturn("ABCDEF2");

        when(databaseConfig.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(any())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        ParkingSpotDAO parkingSpotDAO = new ParkingSpotDAO(databaseConfig);
        boolean result = parkingSpotDAO.updateParking(parkingSpot);

        assertTrue(result);
    }

    @Test
    public void getParkingIsAvailable_ShouldReturnTrueIfSpotIsAvailable() throws SQLException, ClassNotFoundException {
        when(databaseConfig.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(any())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getBoolean(any())).thenReturn(true);

        ParkingSpotDAO parkingSpotDAO = new ParkingSpotDAO(databaseConfig);

        assertTrue(parkingSpotDAO.getParkingIsAvailable(96));
    }
    @Test
    public void getParkingIsAvailable_ShouldReturnFalseIfSpotIsUnavailable() throws SQLException, ClassNotFoundException {
        when(databaseConfig.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(any())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getBoolean(any())).thenReturn(false);

        ParkingSpotDAO parkingSpotDAO = new ParkingSpotDAO(databaseConfig);

        assertFalse(parkingSpotDAO.getParkingIsAvailable(2));
    }
}
