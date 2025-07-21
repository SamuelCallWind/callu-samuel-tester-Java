package com.parkit.parkingsystem;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.service.InteractiveShell;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InteractiveShellTest {

    @Mock
    InputReaderUtil inputReaderUtil = new InputReaderUtil();
    @Mock
    ParkingSpotDAO parkingSpotDAO = new ParkingSpotDAO();
    @Mock
    TicketDAO ticketDAO = new TicketDAO(new DataBaseConfig());



    @Test
    public void testLoadInterfaceIfTheInputIsCorrect() {
        when(inputReaderUtil.readSelection()).thenReturn(3);

        boolean result = InteractiveShell.loadInterface(inputReaderUtil, parkingSpotDAO, ticketDAO);

        assertFalse(result);
    }
    @Test
    public void testLoadInterface_programShouldStopAfterThreeWrongInput() {
        when(inputReaderUtil.readSelection()).thenReturn(5);

        boolean result = InteractiveShell.loadInterface(inputReaderUtil, parkingSpotDAO, ticketDAO);

        assertFalse(result);
    }
}
