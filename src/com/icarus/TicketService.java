package com.icarus;

import com.icarus.flights.Offer;

import java.util.List;
import java.util.UUID;

public interface TicketService {
    List<Offer> searchForTickets(String origin, String destination);

    void confirmBooking(UUID id, String userAuthToken);
}
