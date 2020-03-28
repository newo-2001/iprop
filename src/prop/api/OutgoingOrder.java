package prop.api;

import java.time.Duration;
import java.util.Date;

/*
 * An OutgoingOrder represents an order placed by Alibaba to another seller.
 * It contains information like what and how much was ordered and when it was ordered,
 */
public class OutgoingOrder {
	private static int order_count = 0;
	
	private Product product;	// The product that was ordered
	private Date date;	// The date at which the order was placed
	private int amount; // The amount that was ordered
	private int id;	// The unique identifier for this order
	
	public OutgoingOrder(Product product, int amount) {
		this.product = product;
		this.amount = amount;
		date = new Date();
		id = order_count;
		order_count++;
	}
	
	// Returns the product that was ordered
	public Product getProduct() {
		return product;
	}
	
	// Returns the amount of items that was ordered
	public int getAmount() {
		return amount;
	}
	
	// Returns the total price of the order
	public double getTotalPrice() {
		return product.getPrice() * amount;
	}
	
	// Get the date at which the order was placed
	public Date getOrderDate() {
		return date;
	}
	
	// Returns the time that is left until the order arrives at the warehouse
	public Duration timeUntilArrived() {
		return Duration.ofMillis(86400000l * product.getDeliveryTime() - ((new Date()).getTime() - date.getTime()));
	}
	
	// Returns the seller that sold the product
	public Seller getSeller() {
		return product.getSeller();
	}
	
	// Returns the id of the order
	public int getId() {
		return id;
	}
	
	@Override	// Allows printing the object to the console directly
	public String toString() {
		int days = (int) Math.floor(timeUntilArrived().getSeconds() / 86400);
		return String.format("%dx '%s' from %s, arrives in %s", amount, product, product.getSeller(), (days == 0 ? "< 1 day." : (days == 1 ? "1 day." : days + " days.")));
	}
	
	@Override	// Allows equality checks to only care about the id
	public boolean equals(Object o) {
		if (!(o instanceof OutgoingOrder)) return false;
		return ((OutgoingOrder) o).getId() == id;
	}
	
	@Override	// Makes the order's hashcode equal to its id useful for hashmaps
	public int hashCode() {
		return id;
	}
}
