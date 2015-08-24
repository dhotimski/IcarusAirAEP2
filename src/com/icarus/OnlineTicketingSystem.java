package com.icarus;

import com.icarus.booking.Booking;
import com.icarus.booking.BookingSystem;
import com.icarus.flights.Offer;
import com.icarus.flights.Quote;
import com.icarus.flights.FlightDatabase;

import java.math.BigDecimal;
import java.util.*;

public class OnlineTicketingSystem implements TicketService {

    private static final long MAX_QUOTE_AGE_MILLIS = 20 * 60 * 1000;

    public static final BigDecimal STANDARD_PROCESSING_CHARGE = new BigDecimal(10);

    private Map<UUID, Quote> quotes = new HashMap<UUID, Quote>();

    @Override
    public List<Offer> searchForTickets(String origin, String destination) {

        List<Offer> searchResults = FlightDatabase.getInstance().searchFor(origin, destination);
        for (Offer offer : searchResults) {
            quotes.put(offer.id, new Quote(offer, System.currentTimeMillis()));
        }
        return searchResults;
    }

    @Override
    public void confirmBooking(UUID id, String userAuthToken) {

        if (!quotes.containsKey(id)) {
            throw new NoSuchElementException("Offer ID is invalid");
        }

        Quote quote = quotes.get(id);

        long timeNow = System.currentTimeMillis();

        if (timeNow - quote.timestamp > MAX_QUOTE_AGE_MILLIS) {
            throw new IllegalStateException("Quote expired, please get a new price");
        }

        Booking completeBooking = new Booking(quote.offer.price.add(STANDARD_PROCESSING_CHARGE), quote, timeNow, userAuthToken);

        BookingSystem.getInstance().process(completeBooking);
    }

}