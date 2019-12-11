package parqueacuatico;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Reloj implements Runnable {

	private int horaActual;
	private Object horario = new Object();
	private Lock lock = new ReentrantLock();
	private Condition dormir = lock.newCondition();
	public Reloj() {
		horaActual = 8;
	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(1000);
				lock.lock();
				if (horaActual == 23) {
					horaActual = 0;
				} else {
					horaActual++;
				}
				if(horaActual >= 9 && horaActual <= 17)
				{
					dormir.signalAll();
				}
				
				
			} catch (InterruptedException ex) {
			}finally {
				lock.unlock();
			}
			System.out.println("----------------HORA ACTUAL: " + horaActual);
		}

	}

	public int getHoraActual() {
		return horaActual;
	}
	
	public void esperarUnaHora()
	{
		try {
			lock.lock();
			dormir.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
	}
	
}
