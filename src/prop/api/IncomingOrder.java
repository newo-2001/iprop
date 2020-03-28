package prop.api;

import java.time.Duration;
import java.util.Date;

/*
 * An IncomingOrder represents an order placed by a customer of Alibaba.
 * It contains information like what, how much, when and by who it was ordered.
 */
public class IncomingOrder {
	private static int order_count = 0;
	
	private Item item;	// The item that was ordered
	private int amount; // The amount of items that was ordered
	private Customer customer; // The customer that ordered the item
	private Date date; // The date that the order was placed;
	private int id; // The unique identifier for this order
	
	public IncomingOrder(Item item, int amount, Customer customer) {
		this.item = item;
		this.amount = amount;
		this.customer = customer;
		date = new Date();
		id = order_count;
		order_count++;
	}
	
	// Get the item that is associated with this order
	public Item getItem() {
		return item;
	}
	
	// Get the amount of items that was ordered
	public int getAmount() {
		return amount;
	}
	
	// Get the id for this order
	public int getId() {
		return id;
	}
	
	// Get the customer that ordered this order
	public Customer getCustomer() {
		return customer;
	}
	
	// Get the date that this order was placed
	public Date getOrderDate() {
		return date;
	}
	
	// Get the time passed since the order was placed
	public Duration getOrderTime() {
		return Duration.ofMillis(new Date().getTime() - date.getTime());
	}
	
	@Override	// Allows printing the object to the console directly
	public String toString() {
		int days = (int) Math.floor(getOrderTime().getSeconds() / 86400);
		return String.format("(%s) %dx '%s', %s ago.", customer, amount, item, days == 0 ? "< 1 day" : (days == 1 ? "1 day" : days + " days"));
	}
	
	@Override	// Allows equality checks to only care about the id
	public boolean equals(Object o) {
		if (!(o instanceof IncomingOrder)) return false;
		return ((IncomingOrder) o).id == id;
	}
	
	@Override	// Makes the order's hashcode equal to its id useful for hashmaps
	public int hashCode() {
		return id;
	}
}
