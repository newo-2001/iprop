package prop.api;

/* 
 * A product represents an item being sold by a seller.
 * Including seller specific information like price and shipping times.
 * Products are sorted by deliveryTime ascending by default
 */
public class Product implements Comparable<Product> {
	private Item item;			// The item associated with the product
	private Seller seller;		// The seller of this product
	private double price;		// The product price in euros
	private int deliveryTime;	// The delivery time in days
	
	public Product(Item item, double price, int deliveryTime, Seller seller) {
		this.item = item;
		this.price = price;
		this.deliveryTime = deliveryTime;
		this.seller = seller;
	}
	
	public Item getItem() {
		return item;
	}
	
	// Returns this product's name
	public String getName() {
		return item.getName();
	}
	
	// Returns this product's price
	public double getPrice() {
		return price;
	}
	
	// Returns this product's delivery time in days
	public int getDeliveryTime() {
		return deliveryTime;
	}
	
	// Returns the seller of this product
	public Seller getSeller() {
		return seller;
	}
	
	@Override	// Allows printing the object to the console directly
	public String toString() {
		return item.getName();
	}
	
	@Override	// Allows equality checks to only care about item the id
	public boolean equals(Object o) {
		if (o instanceof Product) {
			return ((Product) o).getItem().getId() == item.getId();
		} else if (o instanceof Item) {
			return ((Item) o).getId() == item.getId();
		}
		return false;
	}
	
	@Override	// Makes the product's hashcode equal to its id useful for hashmaps
	public int hashCode() {
		return item.getId();
	}

	@Override	// Allows sorting products by delivery time
	public int compareTo(Product product) {
		if (product.deliveryTime == deliveryTime) return 0;
		return deliveryTime - product.deliveryTime > 0 ? 1 : -1;
	}
}
