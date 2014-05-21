// 3bi
class ComputerRoom {
	
	private int computerCapacity;
	private int waitingCapacity;
	private int computersInUse;
	private int waitingInUse;
	
	ComputerRoom(int computerCapacity, int waitingCapacity) {
		this.computerCapacity = computerCapacity;
		this.waitingCpacity = waitingCapacity;
	}
	
	synchronized boolean enterRoom() throws InterruptedException {
		if (waitingInUse == waitingCapacity) {
			return false;
		}
		while (computersInUse == computerCapacity) {
			waitingInUse++;
			wait();
			waitingInUse--;
		}
			computersInUse++;
			notifyAll();
			return true;
	}
	
	synchronized void leaveRoom() throws InterruptedException {
		while (computesInUse == 0) {
			wait();
		}
		computersInUse--;
		notifyAll();
	}
}

// 3bii
class Student implements Runnable {
	
	ComputerRoom computerRoom;
	
	Student(ComputerRoom computerRoom) {
		this.computerRoom = computerRoom;
	}
	
	public void run() {
		try {
			
			double randomTime = Math.random() * 2 * 60000;
			Thread.sleep(randomTime);
			if (!computerRoom.enterRoom()) {
				System.out.println("Failed to wait inside room!");
				return;
			}
			Thread.sleep(1800000)
			computerRoom.leaveRoom();
		} catch (InterruptedException e) {}
	}
}

// 3biii
class CountingSemaphore {
	
	private int value;
	private int bound;
	
	CountingSemaphore(int initialValue) {
		this.value = initialValue;
	}
	
	/* For 3biv
	CountingSemaphore(int initialValue, int upperBound) {
		this.value = initialValue;
		this.bound = upperBound;
	}
	*/
	
	synchronized void increment() {
		value++;
		notify();
	}
	
	synchronized void decrement() throws InterruptedException {
		while (value == 0) {
			wait();
		}
		value--;
	}
}

// 3biv
/*
-- Add an upper bound to the semaphore (As shown in above code)
-- Refactor private variables into one semaphore object for the computer room, and another for the waiting queue.
-- Replace increments and decrements '++' expressions with semaphore object 'dot incrememnt' and 'dot decrement' methods in the ComputerRoom class such that entering the room appropriately calls 'semaphore dot increment/decrement' for the appropriate counters (Trivially given). In addition, it's important to note that the 'while loops' and 'wait' statements are no longer required/
-- Because the semaphore methods are also synchronized, nested locks can occur, leading to the student objects getting locks to the semaphore, while the semaphore is called, locks the current object itself, and they both end up waiting for each other, violating the liveness property of the system.
-- The deadlock can be overcome by removing the 'synchronized' keyword from the methods in ComputerRoom and simply syncing the block of statements AFTER the semaphore 'increment/decrement' has been called.
/*