package com.parkit.parkingsystem;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.sql.*;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TicketDAOTest {

    @Mock
    DataBaseConfig dataBaseConfig;
    @Mock
    Connection mockConnection;
    @Mock
    PreparedStatement mockPreparedStatement;
    @Mock
    ResultSet resultSet;

    Ticket ticket;

    @BeforeEach
    public void initiateVariable() {
        this.ticket = new Ticket();
        ticket.setVehicleRegNumber("ABCDEF2");
        ticket.setPrice(2.5);
        ticket.setInTime(new Date());
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
    }


    @Test
    public void saveTicketWhenInputIsCorrect() throws SQLException, ClassNotFoundException {

        when(dataBaseConfig.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.execute()).thenReturn(true);

        TicketDAO ticketDAO = new TicketDAO(dataBaseConfig);

        ticket.setTotalTimesParked(1);
        ticket.setVehicleRegNumber("abcd");
        ticket.setParkingType(ParkingType.CAR);
        ticket.setInTime(new Date());
        ticket.setParkingSpot(new ParkingSpot(2, ParkingType.CAR, false));
        ticket.setId(2);

        boolean result = ticketDAO.saveTicket(ticket);

        assertFalse(result);
    }
    @Test
    public void saveTicketButThrowsError() throws SQLException, ClassNotFoundException {

        when(dataBaseConfig.getConnection()).thenThrow(new RuntimeException("Unable to connect to the DB"));

        TicketDAO ticketDAO = new TicketDAO(dataBaseConfig);

        boolean result = ticketDAO.saveTicket(ticket);

        assertFalse(result);
    }

    @Test
    public void getTicket_ShouldReturnTicket_WhenRecordFound() throws Exception {

        when(dataBaseConfig.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1); // ParkingSpot id
        when(resultSet.getInt(2)).thenReturn(10); // Ticket ID
        when(resultSet.getDouble(3)).thenReturn(12.3);
        when(resultSet.getTimestamp(4)).thenReturn(new Timestamp(123123));
        when(resultSet.getTimestamp(5)).thenReturn(new Timestamp(456456));
        when(resultSet.getString(6)).thenReturn("CAR");

        TicketDAO ticketDAO = new TicketDAO(dataBaseConfig);

        Ticket result = ticketDAO.getTicket("ABC123");

        assertEquals("ABC123", result.getVehicleRegNumber());
    }

    @Test
    public void getTicket_ShouldReturnAnException() throws Exception {
        when(dataBaseConfig.getConnection()).thenThrow(new IllegalArgumentException("Unable to fetch the data"));

        TicketDAO ticketDAO = new TicketDAO(dataBaseConfig);
        try {
            ticketDAO.getTicket("ABCDEF1");
        } catch (IllegalArgumentException e) {
            assertEquals("Unable to fetch the data", e.getMessage());
        }
    }

    @Test
    public void updateTicket_shouldReturnTrueWhenSucceeding() throws SQLException, ClassNotFoundException {
        when(dataBaseConfig.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        Ticket ticket = new Ticket();
        ticket.setPrice(55.5);
        ticket.setOutTime(new Date());
        ticket.setId(60);

        TicketDAO ticketDAO = new TicketDAO(dataBaseConfig);
        boolean result = ticketDAO.updateTicket(ticket);

        assertTrue(result);
    }

    @Test
    public void updateTicket_shouldReturnFalseWithWrongConfiguration() throws SQLException, ClassNotFoundException {
        when(dataBaseConfig.getConnection()).thenReturn(mockConnection);

        TicketDAO ticketDAO = new TicketDAO(dataBaseConfig);
        boolean result = ticketDAO.updateTicket(ticket);

        assertFalse(result);
    }

    @Test
    public void getNbTicketTest_verifyTotalTimeParked() throws SQLException, ClassNotFoundException {
        when(dataBaseConfig.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(any())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);

        Ticket ticket = new Ticket();
        ticket.setVehicleRegNumber("ABCDEFGHIJKLMNOP1");

        TicketDAO ticketDAO = new TicketDAO(dataBaseConfig);

        assertEquals(1, ticketDAO.getNbTicket(ticket.getVehicleRegNumber()));
    }


}
