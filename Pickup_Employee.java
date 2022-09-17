import java.util.ArrayList;
import java.util.Random;

public class Pickup_Employee extends Thread{

	public Pickup_Employee(int id){
		setName("Pickup Employee-" + id);
	}
	
	public void run() {
		sleepAtStart();
		while (true) {
			ArrayList<Customer> line = Customer.pickupLine;;
			if (line.size() > 0) {
				handleOrder(line);
				
				// wait for the customer to pay and leave before removing them from the line and helping next cust
				try {
					line.get(0).join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				// First person in line leaves
				line.remove(0);
				Customer.setPickupLine(line);
			}
			
			else {
				msg("there's no one in the pickup line");
				break; 
				// change break - if restaurant closed -> terminate
			}
			
		}
		
		// if restaurant is closed go home
	}
	public void handleOrder(ArrayList<Customer> line) {
		try {
			msg("is handling the order of " + line.get(0).getName());
			sleep(calcRandomSleep()); //sleep random
			line.get(0).interrupt();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void sleepAtStart()  {
		try {
			sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public int calcRandomSleep() {
		Random rand = new Random();
		int roll = rand.nextInt(500, 1500);
		return roll;
	}
	
	// -------------------------
	public static long time = System.currentTimeMillis();
	public void msg(String m) {
		System.out.println("[" + (System.currentTimeMillis()-time) + "]" + getName() + ": " + m);
	}
	// -------------------------
	
}
