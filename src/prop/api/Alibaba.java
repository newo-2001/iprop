package prop.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Alibaba represents the company Alibaba,
 * it stitches all the API calls together,
 * and acts as an entrypoint for other implementations.
 */
public class Alibaba {
	private static List<Seller> sellers = new ArrayList<Seller>();	// A list of all the registered sellers
	private static Map<Item, Integer> stock = new HashMap<Item, Integer>(); // A list of the warehouse's stock
	private static List<OutgoingOrder> outgoing_orders = new ArrayList<OutgoingOrder>();	// A list of outgoing orders to sellers
	private static List<IncomingOrder> incoming_orders = new ArrayList<IncomingOrder>();	// A list of incoming orders from customers
	
	// Initialize the list of sellers, items and orders
	// Should be called on application load
	public static void init() {
		// Create all the items
		Item duct_tape = addItem(new Item("Unreasonably strong duct tape"), 1);
		Item vga_hdmi_adapter = addItem(new Item("VGA to HDMI adapter"));
		Item resistors = addItem(new Item("Resistor x100"), 8);
		Item camellia_feelin_sky = addItem(new Item("Camellia - Feelin sky"));
		Item cz_75 = addItem(new Item("CZ-75 Machine pistol"), 2);
		Item playing_cards = addItem(new Item("Playing cards"));
		Item zenzenzense = addItem(new Item("Radwimps - zenzenzense"), 1);
		Item fire_and_flames = addItem(new Item("DragonForce - Through the fire and flames"));
		Item hitchhikers_guide = addItem(new Item("Douglas Adams - Hitchiker's guide to the galaxy"));
		Item stronger = addItem(new Item("Stonebank - Stronger"));
		Item fifth_science = addItem(new Item("Exurb1a - The fifth science"));
		Item m4a4 = addItem(new Item("m4a4 assault rifle"), 1);
		Item rusty_screw = addItem(new Item("A rusty screw"), 5);
		Item ammo = addItem(new Item("30x 5.56x45mm NATO ammo"));
		Item silent_voice = addItem(new Item("Yoshitoki Oima - A silent voice"));
		Item oxford_dictionary = addItem(new Item("Oxford english dictionary"));
		Item project_management = addItem(new Item("Remco Meisner - Projectmanagement: IT management voor het HBO"));
		
		
		// Create all the sellers and their products
		Seller DIY = new Seller("DIY shop");
		DIY.addProduct(new Product(duct_tape, 1.75, 6, DIY), 45).addProduct(new Product(vga_hdmi_adapter, 10.99, 7, DIY), 20);
		DIY.addProduct(new Product(resistors, 0.95, 6, DIY), 138).addProduct(new Product(playing_cards, 1.45, 4, DIY), 15);
		DIY.addProduct(new Product(rusty_screw, 0.14, 3, DIY), 89);
		sellers.add(DIY);
		
		Seller tomsmusic = new Seller("Tom's music");
		tomsmusic.addProduct(new Product(camellia_feelin_sky, 3.45, 4, tomsmusic), 3).addProduct(new Product(playing_cards, 1.8, 1, tomsmusic), 3);
		tomsmusic.addProduct(new Product(zenzenzense, 4.5, 4, tomsmusic), 13).addProduct(new Product(fire_and_flames, 8.99, 5, tomsmusic), 1);
		tomsmusic.addProduct(new Product(stronger, 3.25, 4, tomsmusic));
		sellers.add(tomsmusic);
		
		Seller runandgun = new Seller("Run and gun");
		runandgun.addProduct(new Product(cz_75, 289.99, 12, runandgun), 1).addProduct(new Product(playing_cards, 0.49, 6, runandgun), 8);
		runandgun.addProduct(new Product(m4a4, 312, 14, runandgun), 3).addProduct(new Product(rusty_screw, 0.11, 6, runandgun), 57);
		runandgun.addProduct(new Product(ammo, 8.99, 8, runandgun), 128);
		sellers.add(runandgun);
		
		Seller book_depot = new Seller("Book depot");
		book_depot.addProduct(new Product(hitchhikers_guide, 12.45, 5, book_depot), 8).addProduct(new Product(fifth_science, 15.99, 9, book_depot));
		book_depot.addProduct(new Product(silent_voice, 45.45, 7, book_depot), 2).addProduct(new Product(oxford_dictionary, 22.95, 4, book_depot), 15);
		book_depot.addProduct(new Product(project_management, 32.5, 9, book_depot));
		sellers.add(book_depot);
		
		// Create all the customers and their orders
		Customer ninja = new Customer("Ninja Fortnite");
		Customer casper = new Customer("Casper Kauffmann");
		Customer owen = new Customer("Owen Elderbroek");
		
		placeIncomingOrder(new IncomingOrder(cz_75, 1, ninja));
		placeIncomingOrder(new IncomingOrder(playing_cards, 2, ninja));
		placeIncomingOrder(new IncomingOrder(project_management, 1, casper));
		placeIncomingOrder(new IncomingOrder(vga_hdmi_adapter, 2, casper));
		placeIncomingOrder(new IncomingOrder(fire_and_flames, 1, owen));
		placeIncomingOrder(new IncomingOrder(zenzenzense, 2, owen));
		placeIncomingOrder(new IncomingOrder(silent_voice, 1, owen));
	}
	
	// Returns a list of all the registered sellers
	public static Seller[] getSellers() {
		return sellers.toArray(new Seller[sellers.size()]);
	}
	
	// Get a specific seller by name, returns null if not found
	public static Seller getSeller(String name) {
		for (Seller seller : sellers) {
			if (seller.getName() == name) return seller;
		}
		System.out.printf("Seller lookup failed, %s is not a registered seller", name);
		return null;
	}
	
	// Get a list of all the items in the warehouse
	public static Item[] getItems() {
		return stock.keySet().toArray(new Item[stock.size()]);
	}
	
	// Get the stock for a particular item
	public static int getStock(Item item) {
		return stock.get(item);
	}
	
	// Returns a list of outgoing orders
	public static OutgoingOrder[] getOutgoingOrders() {
		return outgoing_orders.toArray(new OutgoingOrder[outgoing_orders.size()]);
	}
	
	// Returns a list of incoming orders
	public static IncomingOrder[] getIncomingOrders() {
		return incoming_orders.toArray(new IncomingOrder[incoming_orders.size()]);
	}
	
	// Places an outgoing order
	public static void placeOutgoingOrder(OutgoingOrder order) {
		outgoing_orders.add(order);
	}
	
	// Places an incoming order
	private static void placeIncomingOrder(IncomingOrder order) {
		incoming_orders.add(order);
	}
	
	// Attempts to ship an incoming order, returns whether it was successful
	public static boolean shipOrder(IncomingOrder order) {
		if (!incoming_orders.contains(order)) {
			System.out.println("Shipping failed, Attempted to ship an order that was listed!");
		} else if (!stock.containsKey(order.getItem())) {
			System.out.println("Shipping failed, The item that was requested is not present in our warehouse!");
		} else if (stock.get(order.getItem()) < order.getAmount()) {
			System.out.printf("Shipping failed, Not enough of the requested item is present in our warehouse! (%d/%d)\n", stock.get(order.getItem()), order.getAmount());
		} else {
			incoming_orders.remove(order);
			stock.put(order.getItem(), stock.get(order.getItem()) - order.getAmount());
			System.out.printf("Successfully shipped %dx '%s' to %s\n", order.getAmount(), order.getItem(), order.getCustomer());
			return true;
		}
		return false;
	}
	
	// Add an item to the warehouse with an initial stock
	// Returns itself for chaining
	private static Item addItem(Item item, int stock) {
		Alibaba.stock.put(item, stock);
		return item;
	}
	
	// Same as addItem(item, stock) but the stock defaults to 0
	private static Item addItem(Item item) {
		return addItem(item, 0);
	}
	
	// Add items to the warehouse stock
	public static void addStock(Item item, int amount) {
		stock.put(item, stock.get(item) + amount);
	}
	
	// Returns a list of all products that match the specified item
	public static List<Product> findProduct(Item item) {
		List<Product> options = new ArrayList<Product>();
		for (Seller seller : sellers) {
			if (seller.hasItem(item)) {
				options.add(seller.getProduct(item));
			}
		}
		return options;
	}
}
