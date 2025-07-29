package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.CharArrayReader;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ParkingServiceTest {

    private static ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;

    @BeforeEach
    public void setUpPerTest() {
        try {
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
            Ticket ticket = new Ticket();
            ticket.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
            when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);

            when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }
    }

    @Test
    public void processIncomingVehicleTest() {
        when(parkingSpotDAO.getNextAvailableSlot(any())).thenReturn(1);
        when(inputReaderUtil.readSelection()).thenReturn(1);

        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        assertEquals(0, parkingService.processIncomingVehicle());
    }

    @Test
    public void processExitingVehicleTestUnableUpdate() {
        when(ticketDAO.updateTicket(any())).thenReturn(false);

        parkingService.processExitingVehicle();

        verify(parkingSpotDAO, never()).updateParking(any());
    }
    @Test
    public void processExitingVehicleTestUpdate() {
        when(ticketDAO.updateTicket(any())).thenReturn(true);

        parkingService.processExitingVehicle();

        verify(parkingSpotDAO).updateParking(any());
    }

    @Test
    public void testGetNextParkingNumberIfAvailable() {
        ParkingService parkingServiceSpy = Mockito.spy(new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO));
        doReturn(ParkingType.CAR).when(parkingServiceSpy).getVehicleType();
        when(parkingSpotDAO.getNextAvailableSlot(any())).thenReturn(1);

        ParkingSpot spot = parkingServiceSpy.getNextParkingNumberIfAvailable();

        assertNotNull(spot);
        assertEquals(1, spot.getId());
    }

    @Test
    public void testGetNextParkingNumberIfAvailableParkingNumberNotFound() {
        ParkingService parkingServiceSpy = Mockito.spy(new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO));
        doReturn(ParkingType.CAR).when(parkingServiceSpy).getVehicleType();
        when(parkingSpotDAO.getNextAvailableSlot(any())).thenReturn(-1);

        ParkingSpot spot = parkingServiceSpy.getNextParkingNumberIfAvailable();

        assertNull(spot);
    }

    @Test
    public void testGetNextParkingNumberIfAvailableParkingNumberWrongArgument() {
        ParkingService parkingServiceSpy = Mockito.spy(new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO));
        doReturn(null).when(parkingServiceSpy).getVehicleType();
        when(parkingSpotDAO.getNextAvailableSlot(any())).thenReturn(-1);

        assertEquals(-1,parkingSpotDAO.getNextAvailableSlot(parkingServiceSpy.getVehicleType()));
    }
}
