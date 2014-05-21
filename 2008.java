class Bridge {
	
	private int maximumWeight;
	private int maximumDifference;
	
	private int currentWeightA;
	private int currentWeightB;
	
	Bridge(int maximumWeight, int maximumDifference) {
		this.maximumWeight = maximumWeight;
		this.maximumDifference = maximumDifference;
	}
	
	synchronized void enterLaneA(Car car) throws InterruptedException {
		while (currentWeightA + currentWeightB + car.getWeight >= maximumWeight || Math.abs(currentWeightA - currentWeightB + car.getWeight) >= maximumDifference) {
			wait();
		}
		currentWeightA += newWeight;
		notifyAll();
	}
	
	synchronized void enterLaneB(Car car) throws InterruptedException {
		while (currentWeightA + currentWeightB + car.getWeight >= maximumWeight || Math.abs(currentWeightA - currentWeightB + car.getWeight) >= maximumDifference) {
			wait();
		}
		currentWeightB += car.getWeight;
		notifyAll();
	}
	
	synchronized void exitLaneA(Car car) throws InterruptedException {
		while (Math.abs(currentWeightA - currentWeightB) >= maximumDifference) {
			wait();
		}
		currentWeightA -= car.getWeight;
		notifyAll();
	}
	
	synchronized void exitLaneB(Car car) throws InterruptedException {
		while (Math.abs(currentWeightA - currentWeightB) >= maximumDifference) {
			wait();
		}
		currentWeightB -= car.getWeight;
		notifyAll();
	}
}

class Car implements Runnable {
	
	Bridge bridge;
	private int weight;
	
	Car(Bridge bridge, int weight) {
		this.bridge = bridge;
		this.weight = weight
	}
	
	public int getWeight() {
		return this.weight;
	}
	
	public void run() {
		try {
			double randomTime = Math.random(1, 2) * 60000; // Not officially Java, gets range
			enterLaneA(this);
			Thread.sleep(randomTime);
			exitLaneA(this);
		} catch (InterruptedException e) {}
	}
}