package prop.cli;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import prop.api.Alibaba;
import prop.api.IncomingOrder;
import prop.api.Item;
import prop.api.OutgoingOrder;
import prop.api.Product;
import prop.api.Seller;

// The command line interface implementation of the Alibaba API
public class Main {
	private static Scanner scanner = new Scanner(System.in);
	
	public static void main(String[] args) {
		Alibaba.init();
		mainMenu();
	}
	
	// Render the main menu
	private static void mainMenu() {
		String header = "\n          Welcome to the Alibaba CLI!          \n" +
						"      Enter one of the options to start.       \n" +
						"-----------------------------------------------\n" +
						"[l] Get a list our registered sellers.\n" +
						"[o] Look through the outgoing orders.\n" +
						"[i] look through or ship incoming orders.\n" +
						"[w] Look through the stock in our warehouse.\n" +
						"[q] Exit the Alibaba CLI.\n";
		
		System.out.print(header);
		
		while (true) {
			System.out.print("> ");
			
			String line = scanner.nextLine().toLowerCase();
			if (line.length() == 0) continue;
			char input = line.charAt(0);
			
			if (input == 'l') {
				sellers();
				System.out.print(header);
			} else if (input == 'o') {
				outgoing_orders();
				System.out.print(header);
			} else if (input == 'i') {
				incoming_orders();
				System.out.print(header);
			} else if (input == 'q') {
				System.out.println("See you next time!");
				System.exit(0);
			} else if (input == 'w') {
				warehouse();
				System.out.print(header);
			}
		}
	}
	
	// Render a list of all the registered sellers
	private static void sellers() {
		String header = "\n   Here is a list of our registered sellers.   \n" +
						"        Enter one of the options below.        \n" +
						"-----------------------------------------------\n";
		
		Seller[] sellers = Alibaba.getSellers();
		for (int i = 1; i <= sellers.length; i++) {
			header += String.format("[%d] %s\n", i, sellers[i-1]);
		}
		header += "[q] Back\n";
		System.out.print(header);
		
		int n = 0;
		while (true) {
			System.out.print("> ");
			
			String line = scanner.nextLine().toLowerCase();
			if (line.length() == 0) continue;
			try {
				// Check if the entered token is a number
				n = Integer.parseUnsignedInt(line);
				if (n <= sellers.length && n > 0) seller(sellers[n-1]);
				System.out.print(header);
			} catch (NumberFormatException e) {
				if (line.charAt(0) == 'q') {
					return;
				}
			}
		}
	}
	
	// Render a list of all the products and stock for a particular seller
	private static void seller(Seller seller) {
		String header = String.format("\n%s's stock\n", seller) +
						"Enter a number to order that item or q to exit\n" +
						"-----------------------------------------------\n";
		
		Product[] products = seller.getProducts();
		for (int i = 1; i <= products.length; i++) {
			Product product = products[i-1];
			header += String.format("[%d] '%s' $%.2f (x%d) %d days shipping time.\n", i, product, product.getPrice(), seller.getStock(product), product.getDeliveryTime());
		}
		header += "[q] Back\n";
		System.out.print(header);
		
		while (true) {
			System.out.print("> ");
			String line = scanner.nextLine().toLowerCase();
			if (line.length() == 0) continue;
			
			try {
				// Check if the entered token is a number
				int n = Integer.parseUnsignedInt(line);
				if (n <= products.length && n > 0) {
					seller.order(products[n-1], 1);
				}
				
			} catch (NumberFormatException e) {
				if (line.charAt(0) == 'q') {
					return;
				}
			}
		}
	}
	
	// Render a list of the current stock in the warehouse
	private static void warehouse() {
		String header = "\n          Alibaba's warehouse's stock          \n" +
						"-----------------------------------------------\n";
		
		for (Item item : Alibaba.getItems()) {
			if (Alibaba.getStock(item) == 0) continue;
			header += String.format("'%s' (x%d)\n", item, Alibaba.getStock(item));
		}
		header += "Press enter to go back.";
		System.out.print(header);
		
		scanner.nextLine();
	}
	
	// Render a list of outgoing orders to sellers
	private static void outgoing_orders() {
		String header = "\n          Outgoing orders to sellers           \n" +
						"-----------------------------------------------\n";
		
		for (OutgoingOrder order : Alibaba.getOutgoingOrders()) {
			header += order.toString() + "\n";
		}
		header += "Press enter to go back.\n";
		System.out.print(header);
		
		scanner.nextLine();
	}
	
	// Render a list of incoming orders from customers
	private static void incoming_orders() {
		String header = "\n        Incoming orders from customers        \n" +
						" Enter a number to ship the order or q to exit \n" +
						"-----------------------------------------------\n";
		IncomingOrder[] orders = Alibaba.getIncomingOrders();
		for (int i = 1; i <= orders.length; i++) {
			header += String.format("[%d] %s\n", i, orders[i-1]);
		}
		header += "[q] Back\n";
		System.out.print(header);
		
		while (true) {
			System.out.print("> ");
			String line = scanner.nextLine().toLowerCase();
			if (line.length() == 0) continue;
			
			try {
				// Check if the entered token is a number
				int n = Integer.parseUnsignedInt(line);
				if (n <= orders.length && n > 0) {
					if (!Alibaba.shipOrder(orders[n-1])) {
						System.out.print("Would you like to do an automated search for this item? (y/n)\n");
						while (true) {
							System.out.print("> ");
							line = scanner.nextLine().toLowerCase();
							if (line.length() == 0) continue;
							char input = line.charAt(0);
							
							if (input == 'y') {
								IncomingOrder order = orders[n-1];
								// Automated search for the item
								List<Product> products = Alibaba.findProduct(order.getItem());
								List<Product> options = new ArrayList<Product>();
								
								// Remove all the options that did not have enough stock for the order
								for (int i = 0; i < products.size(); i++) {
									Product product = products.get(i);
									if (product.getSeller().getStock(product) >= order.getAmount() - Alibaba.getStock(product.getItem())) {
										options.add(product);
									}
								}
								
								// Find cheaper a option that is allowed to have a little extra shipping time
								Product option;
								if (options.size() == 0) option = null;
								else {
									option = options.get(0);
									options.sort(null);
									for (int i = 1; i < options.size(); i++) {
										Product product = options.get(i);
										if (product.getDeliveryTime() - option.getDeliveryTime() <= 2 && product.getPrice() < option.getPrice()) {
											option = product;
										} else if (product.getDeliveryTime() - option.getDeliveryTime() > 2) {
											break;
										}
									}
									
									if (option == null) {
										System.out.println("Automated lookup failed, no sellers currently sell that item.");
									} else {
										System.out.printf("We recommend %s with a price of $%.2f and shipping time of %d day%s.\n", option.getSeller(), option.getPrice(), option.getDeliveryTime(), option.getDeliveryTime() == 1 ? "" : "s");
										System.out.println("Would you like to order this item? (y/n)");
										
										// Offer to order the item automatically
										while (true) {
											System.out.print("> ");
											line = scanner.nextLine().toLowerCase();
											if (line.length() == 0) continue;
											if (line.charAt(0) == 'y') {
												option.getSeller().order(option, order.getAmount() - Alibaba.getStock(option.getItem()));
												System.out.println("Press enter to go back.");
												scanner.nextLine();
												System.out.print(header);
												break;
											} else if (line.charAt(0) == 'n') {
												System.out.print(header);
												break;
											}
										}
									}
								}
								break;
							} else if (input == 'n') {
								System.out.print(header);
								break;
							}
						}
					}
				}
			} catch (NumberFormatException e) {
				if (line.charAt(0) == 'q') {
					return;
				}
			}
		}
	}
}