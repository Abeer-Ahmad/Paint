package paint.main;
import java.awt.EventQueue;

import gui.Paint;

public class Main {
	
	static Paint p;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 EventQueue.invokeLater(new Runnable() {
	            public void run() {
	                try {
	                    p = new Paint();
	                } catch  (Exception e) {
	                    e.printStackTrace();
	                }
	            }
	        });
	}

}