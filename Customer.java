import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class Customer extends Thread {
	
	public Customer(int id){
		setName("Customer-" + id);
		 // waitForSeat.set(false);
		 // needsToPay.set(false);
	}
	
	// PickupLine arraylist is the line the customers wait on to place and get their orders
	public static ArrayList<Customer> pickupLine = new ArrayList<Customer>();
	public static ArrayList<Customer> getPickupLine() {
		return pickupLine;
	}
	public static void setPickupLine(ArrayList<Customer> inputLine) {
		pickupLine = inputLine;
	}	
	
	// waitList holds the customers that wait to be seated to dine-in
	public static ArrayList<Customer> waitList = new ArrayList<Customer>();
	public static ArrayList<Customer> getWaitList() {
		return waitList;
	}
	public static void setWaitList(ArrayList<Customer> inputList) {
		waitList = inputList;
	}	
	
	// -------------------------
	public static long time = System.currentTimeMillis();
	public void msg(String m) {
		System.out.println("[" + (System.currentTimeMillis()-time) + "]" + getName() + ": " + m);
	}
	// -------------------------
	
	
	public AtomicBoolean waitForSeat = new AtomicBoolean(true); 
	public AtomicBoolean needsToPay = new AtomicBoolean(true); // = false;
	
	public boolean ordering = false;
	public boolean waitForFood = false;
	
	
	public void run() {
		msg("started running");;
		commuteToDiner();
		
		if (roll() < 3) { 
			// pickup order
			msg("decided to pickup");
			pickupLine.add(this);
			
			// If customer is not at position 0, he can't place an order -> busywait until position 0
			while (pickupLine.get(0) != this) {
				continue;				
			}
			
			//Customer needs to wait a long time and be interrupted when their order is ready
			customerWaitsForOrder(this);			

		}
		else {
			// dine-in
			msg("decided to dine in");
			waitList.add(this);
			
			// busywait to be seated
			while(waitForSeat.get()) {
				// waiting for seat until table employee changes the value to false
			}	
			
			orderingFood();
			waitingToBeServed(this);
			
			while(needsToPay.get()) {
				// busywaiting for server to notice customer wants to pay
			}
			
			// when customer leaves need to delete their entries from tables in table_employee
			
		}
		msg("stopped running");
	}
	
	// Decide whether dine-in or pickup
	public int roll() {
		Random rand = new Random();
		int roll = rand.nextInt(0, 11);
		return roll;
	}
	
	public int calcRandomSleep() {
		Random rand = new Random();
		int roll = rand.nextInt(500, 1500);
		return roll;
	}
	
	public void commuteToDiner() {
		try {
			sleep(calcRandomSleep());
			msg("arrived at the diner");
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.out.println(getName() + "problem commuting to Diner");
		}
	}
	
	public void customerPaysPickup() {
		try {
			sleep(calcRandomSleep());
			msg("paid for pickup order");
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.out.println(getName() + "problem paying for pickup order");
		}
	}
	
	public void customerWaitsForOrder(Customer c) {
		try {
			System.out.println(c.getName() + " is waiting for their order");
			sleep(30000); //sleep a long time until interrupt
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.out.println(getName() + "Pickup Employee interrupted "+ c.getName() + " - order is ready");
			customerPaysPickup();
		}
	}
	
	public void orderingFood() {
		try {
			// ordering = true;
			sleep(calcRandomSleep());
			msg("is ordering their food at the table");
			ordering = false;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void waitingToBeServed(Customer c) {
		try {
			msg(" is waiting for their food at the table");
			sleep(30000); //sleep a long time until interrupt
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.out.println("Server interrupted "+ c.getName() + " - Dinner is served");
			
			// Customers will take their time with the food
			try {
				msg("is eating");
				sleep(calcRandomSleep());  // sleep random
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			Thread.yield();
			Thread.yield();
			
			needsToPay.set(true); 			
			// add customerPaysServer
		}
	}
	
	public void waitingToBeServedV2(Customer c) {
		waitForFood = true;
		msg(" is waiting for their food");
		while(waitForFood) {
			// busywait until they receive food
		}
	}
		

}
