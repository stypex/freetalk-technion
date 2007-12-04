/**
 * 
 */
package client.listeners;

/**
 * This thread has a doStop() methofd which will cause it to exit.
 * Any inheriting thread must always check for the value of isStopped
 * and exit if it becomes true;
 * @author lenka
 *
 */
public class StoppableThread extends Thread {

	protected boolean isStopped;

	public StoppableThread() {
		super();
		this.isStopped = false;
	}
	
	public void doStop() {
		isStopped = true;
	}
}
