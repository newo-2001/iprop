package prop.api;

/*
 * An item represents an object that can be purchased.
 * The same item can be sold by different vendors for different prices.
 */
public class Item {
	private static int item_count = 0;
	
	private int id; // This item's unique identifier
	private String name; // This item's name
	
	public Item(String name) {
		id = item_count;
		this.name = name;
		item_count++;
	}
	
	// Returns this item's name
	public String getName() {
		return name;
	}
	
	// Returns this item's id
	public int getId() {
		return id;
	}
	
	@Override	// Allows printing the object to the console directly
	public String toString() {
		return name;
	}
	
	@Override	// Allows equality checks to only care about the id
	public boolean equals(Object o) {
		if (o instanceof Product) {
			return ((Product) o).getItem().getId() == id;
		} else if (o instanceof Item) {
			return ((Item) o).id == id;
		}
		return false;
	}
	
	@Override	// Makes the item's hashcode equal to its id useful for hashmaps
	public int hashCode() {
		return id;
	}
}
