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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketDAOTest {

    @Mock
    DataBaseConfig dataBaseConfig;
    @Mock
    Connection mockConnection;
    @Mock
    PreparedStatement mockPreparedStatement;


    @Test
    public void saveTicketWhenInputIsCorrect() throws SQLException, ClassNotFoundException {

        when(dataBaseConfig.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.execute()).thenReturn(true);

        TicketDAO ticketDAO = new TicketDAO(dataBaseConfig);

        Ticket ticket = new Ticket();
        ticket.setVehicleRegNumber("ABCDEF2");
        ticket.setPrice(2.5);
        ticket.setInTime(new Date());
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));

        boolean result = ticketDAO.saveTicket(ticket);

        assertFalse(result);
        verify(dataBaseConfig).getConnection();
        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStatement).execute();
    }

}
