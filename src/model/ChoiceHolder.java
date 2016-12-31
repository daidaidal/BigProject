package model;

public class ChoiceHolder {
	private int choice = -1;
	public synchronized void set(int c){
		if (choice == -1){
			choice = c;
			notifyAll();
		}
	}
	public synchronized int get(){
		if (choice == -1){
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return choice;
	}
}
