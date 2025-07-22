package com.parkit.parkingsystem;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
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

        boolean result = ticketDAO.saveTicket(ticket);

        assertFalse(result);
        verify(dataBaseConfig).getConnection();
        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStatement).execute();
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
        ResultSet mockResultSet = mock(ResultSet.class);

        when(dataBaseConfig.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(1); // ParkingSpot id
        when(mockResultSet.getInt(2)).thenReturn(10); // Ticket ID
        when(mockResultSet.getDouble(3)).thenReturn(12.3);
        when(mockResultSet.getTimestamp(4)).thenReturn(new Timestamp(123123));
        when(mockResultSet.getTimestamp(5)).thenReturn(new Timestamp(456456));
        when(mockResultSet.getString(6)).thenReturn("CAR");
        
        TicketDAO ticketDAO = new TicketDAO(dataBaseConfig);

        Ticket result = ticketDAO.getTicket("ABC123");

        assertEquals("ABC123", result.getVehicleRegNumber());
    }


}
