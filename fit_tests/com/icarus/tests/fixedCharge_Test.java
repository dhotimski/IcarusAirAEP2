package com.icarus.tests;

import com.icarus.OnlineTicketingSystem;

/**
 * Created by Dmitri on 24.08.2015.
 */
public class fixedCharge_Test extends OnlineTicketingSystem{
    public int processing_charge( String origin, String destination, long timeBeforeConfirmation){return 0;}
    public boolean doesOfferExist( String origin, String destination, long timeBeforeConfirmation){return true;}
    public boolean isQuoteExpired(long timeBeforeConfirmation){return false;}
}
