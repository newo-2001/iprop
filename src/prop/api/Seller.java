package prop.api;

import java.util.HashMap;
import java.util.Map;

/* 
 * A seller represent an independant company that sells products
 * to Alibaba. It also contains information about their stock.
 */
public class Seller {
	private String name;	// The name of the seller
	private Map<Product, Integer> stock = new HashMap<Product, Integer>();	// The seller's stock
	
	public Seller(String name) {
		this.name = name;
	}
	
	// Returns an array of all the products
	public Product[] getProducts() {
		return stock.keySet().toArray(new Product[stock.size()]);
	}
	
	// Returns the seller's stock for a particular product
	public int getStock(Product product) {
		return stock.get(product);
	}
	
	// Returns whether this seller sells a particular product 
	public boolean hasProduct(Product product) {
		return stock.containsKey(product);
	}
	
	// Returns wheter this seller sells a particular item
	@SuppressWarnings("unlikely-arg-type")
	public boolean hasItem(Item item) {
		return stock.containsKey(item);
	}
	
	// Returns a product for a particular item, null if not found
	@SuppressWarnings("unlikely-arg-type")
	public Product getProduct(Item item) {
		if (!hasItem(item)) return null;
		for (Product product : stock.keySet()) {
			if (product.equals(item)) return product;
		}
		return null;
	}
	
	//	Return this seller's name
	public String getName() {
		return name;
	}
	
	// Adds a product to this seller's list of products
	// Returns this seller for chaining
	public Seller addProduct(Product product, int stock) {
		if (hasProduct(product)) System.out.printf("[WARNING] attempted to register product %s for %s but it was already registered", product, this);
		else this.stock.put(product, stock);
		return this;
	}
	
	// Same as addProduct(product, stock) but the stock defaults to 0
	public Seller addProduct(Product product) {
		return addProduct(product, 0);
	}
	
	// Order a particular product from this seller
	// Returns whether the order was successful
	public boolean order(Product product, int count) {
		if (!hasProduct(product)) {
			System.out.printf("Order failed, %s does not sell %s", this, product);
		} else if (getStock(product) < count) {
			if (getStock(product) == 0) System.out.printf("Order failed, %s currently doesn't have any %s in stock\n", this, product);
			else System.out.printf("Order failed, %s currently only has %d/%d %s in stock\n", this, getStock(product), count, product);
		} else {
			stock.put(product, getStock(product) - count);
			Alibaba.placeOutgoingOrder(new OutgoingOrder(product, count));
			System.out.printf("Successfully ordered %d %s from %s\n", count, product, this);
			return true;
		}
		return false;
	}
	
	@Override	// Allows printing the object to the console directly
	public String toString() {
		return name;
	}
}
