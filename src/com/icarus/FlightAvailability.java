package com.icarus;

import com.icarus.flights.Offer;

import java.util.List;

public interface FlightAvailability {
    List<Offer> searchFor(String origin, String destination);
}
