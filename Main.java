
public class Main {
	public int numCustomers = 17, numTables = 3, numSeats = 3, numtableEmployee = 2;	
	public static void main(String []args) {
		long time = System.currentTimeMillis();
		
		Pickup_Employee pickup1 = new Pickup_Employee(01);
		pickup1.start();
		
		Table_Employee server1 = new Table_Employee(01);
		Table_Employee server2 = new Table_Employee(02);
		server1.start();
		// server2.start();		
		
		for (int i = 0; i < 17; i++) {
			Customer cust = new Customer(i+1);
			cust.start();
		}
		
		
	}
}
