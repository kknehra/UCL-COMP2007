// 3b
class SwimmingPool {
	
	public enum Person {
		ADULT;
		CHILD;
	}
	
	private int totalCapacity;
	private int totalCount;
	private int childCapacity;
	private int childCount;
	
	
	private ArrayList <Adult> adults = new ArrayList <Adult>(); // 3d
	private ArrayList <Child> children = new ArrayList <Children>(); // 3d
	
	SwimmingPool(int totalCapacity, int childCapacity) {
		this.totalCapacity = totalCapacity;
		this.childCapacity = childCapacity;
	}
	
	synchronized void enterPool(Person person) throws InterruptedException {
		while (totalCount == totalCapacity) {
			wait();
		}
		if (person == Person.CHILD) {
			while (childCount == childCapactiy) {
				wait();
			}
			childCount++;
			totalCount++;
			children.add(Thread.currentThread()); // 3d
			notifyAll();
		}
		else {
			totalCount++;
			adults.add(Thread.currentThread()); // 3d
			notifyAll();
		}
	}
	
	synchronized void leavePool(Person person) throws InterruptedException {
		while (totalCount == 0) {
			wait();
		}
		if (person == Person.CHILD) {
			childCount--;
			totalCount--;
			notifyAll();
		}
		else {
			totalCount--;
			notifyAll();
		}
	}
	
	// 3d
	synchronized void evacuate() {
		for (Adult a : adults) {
			a.interrupt();
		}
		for (Child c : children) {
			c.interrupt();
		} 
	}
}

// 3c
class Adult implements Runnable {
	
	SwimmingPool swimmingPool;
	
	Adult(SwimmingPool swimmingPool) {
		this.swimmingPool = swimmingPool;
	}
	
	public void run() {
		try {
			swimmingPool.enterPool(this);
			Thread.sleep(600000);
			swimmingPool.leavePool(this);
		} catch (InterruptedException e) {
			swimmingPool.leavePool(this);
		}
	}
}
