import java.math.BigDecimal;
import java.util.List;

import com.icarus.OnlineTicketingSystem;
import com.icarus.TicketService;
import com.icarus.flights.Offer;


public class ExampleClient {
	 public static void main(String[] args) throws Exception {
	 TicketService ticketingSystem = new OnlineTicketingSystem();
	 String userAuthToken = "tom@example.com";
	 List<Offer> searchResults = ticketingSystem.searchForTickets("London", "New York");
	 if (searchResults.isEmpty()) {
	 System.out.println("No search results found");
	 } else {
	 Offer offer = searchResults.get(0);
	 // some time may pass...
	 Thread.sleep(5 * 1000);
	 if (priceAcceptable(offer.price)) {
	 ticketingSystem.confirmBooking(offer.id, userAuthToken);
	 }
	 }
	 }
	 private static boolean priceAcceptable(BigDecimal price) {
	 return true;
	 }
	}