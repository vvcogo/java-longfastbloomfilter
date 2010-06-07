import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ReusuableObjectPool<T extends Factory<T>> {

	private LinkedBlockingQueue<T> pool;
	private T factoryObject;
	private AtomicInteger numObjects = new AtomicInteger(1);
	private final int maxObjects;

	public ReusuableObjectPool(int maxObjects, T factoryObject) {
		pool = new LinkedBlockingQueue<T>(maxObjects);
		pool.add(factoryObject);
		this.factoryObject = factoryObject;
		this.maxObjects = maxObjects;
	}

	private T create() {
		numObjects.getAndIncrement();
		return factoryObject.create();
	}

	private void destroy(T o) {
		o.destroy();
		numObjects.getAndDecrement();
	}

	public T checkOut() {
		if (pool.isEmpty() && numObjects.get() < maxObjects) {
			return create();
		}

		try {
			return pool.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return create();
	}

	public void checkIn(T o) {
		if (!pool.offer(o)) {
			destroy(o);
		}
	}

	public void optimize(int predictedObjectsInUse) {
		int numToReduceBy = numObjects.get() - predictedObjectsInUse;
		while (numToReduceBy > 0) {
			T o = pool.poll();
			if (o == null) {
				break;
			} else {
				destroy(o);
				numToReduceBy--;
			}
		}
	}
}


