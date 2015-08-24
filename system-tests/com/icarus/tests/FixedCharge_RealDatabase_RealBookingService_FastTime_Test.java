package com.icarus.tests;

import com.icarus.BookingService;
import com.icarus.OnlineTicketingSystem;
import com.icarus.flights.Offer;
import org.junit.Before;
import org.junit.Test;
import org.junit.*;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.Duration;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;

/**
 * Created by Dmitri on 24.08.2015.
 */
public class FixedCharge_RealDatabase_RealBookingService_FastTime_Test {


    OnlineTicketingSystem ticketingSystem = new OnlineTicketingSystem();
    String userAuthToken = "tom@example.com";
    BigDecimal processingChargeExpected;
    BigDecimal totalPriceExpected;
    Date date;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));


    }

    @After
    public void cleanUpStreams() {
        System.setOut(null);
        System.setErr(null);
    }

    @Test
    public void try_to_confirm_tickets_for_nonExistent_flight() throws Exception {
        List<Offer> searchResults = ticketingSystem.searchForTickets("Moscow", "N");
        assertTrue(searchResults.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void confirm_tickets_after_timeout() throws Exception {

        List<Offer> searchResults = ticketingSystem.searchForTickets("", "");
         Offer  offer = searchResults.get(0);
        Duration duration = Duration.ofMinutes(21);
        Clock newClock = Clock.offset(Clock.systemDefaultZone(), duration);
        ticketingSystem.setConfirmTicketsTimer(Clock.fixed(newClock.instant(), ZoneId.systemDefault()));
        ticketingSystem.confirmBooking(offer.id, userAuthToken);
    }


    @Test
    public void confirm_tickets_within_time_limit() throws Exception {
        List<Offer> searchResults = ticketingSystem.searchForTickets("", "");
       Offer offer = searchResults.get(0);

        Duration duration = Duration.ofMinutes(10);
        Clock newClock = Clock.offset(Clock.systemDefaultZone(), duration);
        ticketingSystem.setConfirmTicketsTimer(Clock.fixed(newClock.instant(), ZoneId.systemDefault()));


        processingChargeExpected = OnlineTicketingSystem.STANDARD_PROCESSING_CHARGE;

        ticketingSystem.confirmBooking(offer.id, userAuthToken);
        totalPriceExpected = ticketingSystem.getTicketPriceBeforeSurcharge().add(processingChargeExpected).setScale(2, RoundingMode.HALF_UP);// how to get it?
        date = new Date(ticketingSystem.getTimeConfirmation());

        String msg = outContent.toString();
        assertThat(msg, allOf(containsString(ticketingSystem.getOrigin()),
                containsString(ticketingSystem.getDestination()),
                containsString(userAuthToken),
                containsString(String.valueOf(totalPriceExpected)),
                containsString(String.valueOf(date))));
    }


}
