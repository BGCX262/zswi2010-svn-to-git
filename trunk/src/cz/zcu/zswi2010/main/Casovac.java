package cz.zcu.zswi2010.main;

import cz.zcu.zswi2010.config.Konfig;

/**
 * Trida ktera bude slouzit jako tikatko v samostatnem vlakne
 * Bude cyklicky volat posun seznamu aut v pravidelnem intervalu
 * Hlavni centrum LINEARNI simulace
 * @author ppolak
 *
 */
public class Casovac extends Thread {
	private short minuty;
	
	
	/**
	 * Asi by to chtelo mu predhodit aspon cil toho tikani - bud primo seznam a nebo nejakej director, co by hejbal treba i hodinama v gui
	 *  Konstruktor 
	 */
	public Casovac() {
	  this.minuty = Konfig.START_SIM;	
	  
	}
	
	/**
	 * Prepsana metoda run(), myslim ze to se pouziva pro vykonny kod 
	 */
	public void run() {
		
		// Neco delej
		this.minuty++; // to bude fofr, zatim co vterina realu to minuta
		
		try {
			Thread.sleep(Konfig.TIK_INTERVAL_MS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
