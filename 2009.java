class Basket {
	
	private int basketCapacity;
	private int basketCount;
	private Farmer farmer;
	
	Basket(int basketCapacity, Farmer farmer) {
		this.basketCapacity = basketCapacity;
		this.farmer = farmer;
	}
	
	synchronized void pickApple() {
		while (basketCount == basketCapacity) { wait(); };
		basketCount++;
		if (basketCount == basketCapacity) {
		  wakeFarmer();
		}
		notifyAll();
	}
	
	synchronized void takeApples() {
		while (basketcount == 0) { wait(); };
		basketCount--;
		notifyAll();
	}
	
	synchronized boolean empty() {
		return basketCount == 0;
	}
	
	private void wakeFarmer() {
		farmer.interrupt();
	}
}

class Farmer implements Runnable {
	
	Basket basket;
	
	Farmer(Basket basket) {
		this.basket = basket;
	}
	
	public void run() {
	  while (true) {
		  try {
			  while (true) {
				  Thread.sleep(10);
			  }
		  } catch (InterruptedException e) {
			  while (!basket.empty()) {
			  	basket.takeApples();
			  }
		  }
	  }
	}
}

class Child implements Runnable {
	
	Basket basket;
	
	Child(Basket basket) {
		this.basket = basket;
	}
	
	public void run() {
		try {
			for (int i = 0; i < 500; i++) {
				basket.pickApple();
				Thread.sleep(30000);
			}
		} catch (InterruptedException e) {}
	}
	
	
}