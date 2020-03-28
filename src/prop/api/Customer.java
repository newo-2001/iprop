package prop.api;

/*
 * A customer represents a customer of Alibaba.
 * In this implementation it only contains a name,
 * but this could be extended to contain: email, postal codes,
 * login information, etc.
 */
public class Customer {
	private static int customer_count = 0;
	
	private String name;	// The customer's name
	private int id;	// The customer's unique identifier
	
	public Customer(String name) {
		this.name = name;
		id = customer_count;
		customer_count++;
	}
	
	// Returns this customer's id
	public int getId() {
		return id;
	}
	
	// Returns this customer's name
	public String getName() {
		return name;
	}
	
	@Override	// Allows printing the object to the console directly
	public String toString() {
		return name;
	}
	
	@Override	// Allows equality checks to only care about the id
	public boolean equals(Object o) {
		if (!(o instanceof Customer)) return false;
		return ((Customer) o).id == id;
	}
	
	@Override	// Makes the customer's hashcode equal to its id useful for hashmaps
	public int hashCode() {
		return id;
	}
}
