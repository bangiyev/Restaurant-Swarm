import java.util.ArrayList;

public class Table_Employee extends Thread{
	
	public Table_Employee(int id){
		setName("Server-" + id);
	}
	
	public static ArrayList<ArrayList<Customer>> servingTables = new ArrayList<ArrayList<Customer>>();
	public static ArrayList<Customer> seated = new ArrayList<Customer>();
	
	public static ArrayList<Customer> table1 = new ArrayList<Customer>();
	public static ArrayList<Customer> table2 = new ArrayList<Customer>();
	public static ArrayList<Customer> table3 = new ArrayList<Customer>();
	
	int customersServed = 0;
	
	public void run() {
		sleepAtStart();
		while (true) {
			while (Customer.waitList.size() > 2) {
				if (table1.size() == 0) {					
					for (int i = 0; i < 3; i++) {
						table1.add(Customer.waitList.get(i));	// seat customers in groups of 3 fcfs
						msg("seated "+ table1.get(i).getName() + " at table 1");
					}
					
					// add the table to this server's list of working tables
					servingTables.add(table1);									
					
					for (int i = 0; i < 3; i++) {
						msg(Customer.waitList.get(0).getName() + " is no longer in the waitlist");
						Customer.waitList.get(0).waitForSeat.set(false); // change flag so customer can exit busywait loop
						Customer.waitList.remove(0); // remove customers from the waitList in groups of 3 (they got seats)					
					}					
										
					while(table1.get(0).ordering || table1.get(1).ordering || table1.get(2).ordering) {
						// server busywaits until all 3 orders are placed						
					}
										
					customersServed += 3;
					checkIfNeedPay();
					// see if server can seat more customers
					
					// table1.get(0).interrupt();
					
					
					if (Customer.waitList.size() > 2 ) {
						break;
					}
					
				}
				
				else if (table2.size() == 0) {
					
									
					for (int i = 0; i < 3; i++) {
						table2.add(Customer.waitList.get(i));	// seat customers in groups of 3
						msg("seated "+ table2.get(i).getName() + " at table 2");
					}
					
					// add the table to this server's list of working tables
					servingTables.add(table2);
					
					
					for (int i = 0; i < 3; i++) {
						msg(Customer.waitList.get(0).getName() + " is no longer in the waitlist");
						Customer.waitList.get(0).waitForSeat.set(false); // change flag so customer can exit busywait loop
						Customer.waitList.remove(0); // remove customers from the waitList in groups of 3 (they got seats)					
					}										
					
					while(table2.get(0).ordering || table2.get(1).ordering || table2.get(2).ordering) {
						// server busywaits until all 3 orders are placed						
					}
					
					
					customersServed += 3;
					checkIfNeedPay();
					// see if server can seat more customers
				}
					
				
				else if (table3.size() == 0) {
					
					
					for (int i = 0; i < 3; i++) {
						table3.add(Customer.waitList.get(i));	// seat customers in groups of 3
						msg("seated "+ table3.get(i).getName() + " at table 3");
					}
					
					// add the table to this server's list of working tables
					servingTables.add(table3);									
					
					for (int i = 0; i < 3; i++) {
						msg(Customer.waitList.get(0).getName() + " is no longer in the waitlist");
						Customer.waitList.get(0).waitForSeat.set(false); // change flag so customer can exit busywait loop
						Customer.waitList.remove(0); // remove customers from the waitList in groups of 3 (they got seats)					
					}					
					
					
					while(table3.get(0).ordering || table3.get(1).ordering || table3.get(2).ordering) {
						// server busywaits until all 3 orders are placed						
					}
					
					
					customersServed += 3;
					checkIfNeedPay();
					// see if server can seat more customers
				}
				
				else {
					// no tables available for a new group to sit at. They keep waiting
				}				
				
			}
			
			if (Customer.waitList.size() > 0 && Customer.waitList.size() < 3 && customersServed > 9) {
				// serve the remainder who couldn't make a group of 3
			}
						
			
		}
	}
	
	
	public void checkIfNeedPay() {
		for (int i = 0; i < servingTables.size(); i++) {
			for (int j = 0; j < servingTables.get(i).size(); j++) {
				if (servingTables.get(i).get(j).needsToPay.get()) {
					try {
						servingTables.get(i).get(j).needsToPay.set(false);
						servingTables.get(i).get(j).join(); //wait for customer thread to die since they're paying and leaving
						servingTables.get(i).remove(j); //remove customer from the table array
						System.out.println(servingTables.get(i).get(j).getName() + " Paid and left");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	
	public void sleepAtStart()  {
		try {
			sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	// -------------------------
	public static long time = System.currentTimeMillis();
	public void msg(String m) {
		System.out.println("[" + (System.currentTimeMillis()-time) + "]" + getName() + ": " + m);
	}
	// -------------------------
	
}



