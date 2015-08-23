package cz.zcu.zswi2010.main;

import java.util.ArrayList;
import java.util.Iterator;

import cz.zcu.zswi2010.config.Konfig;

public class Seznam_aut {
  private ArrayList<Jizda> sa = new ArrayList<Jizda>();
  
  /** 
   * Konstruktor
   */
  public Seznam_aut() {
	    
  }
  
  /**
   * Prida seznam aut do seznamu aut :)
   * Jakoby odstartuje auto ze zakladny do sveta
   * @param seznam Seznam aut k pridani/odstartovani
   */
  public void pridatAuta(ArrayList<Jizda> seznam) {
	  Iterator<Jizda> it = seznam.iterator(); 
	  Jizda j;
	  
	  while (it.hasNext()) { // nastavit vsechny v davce na stav "na ceste"
		 j = it.next();
		 j.setStav(Konfig.STAV_NACESTE);
	  }
	  this.sa.addAll(seznam); // pridat vsechny do seznamu
  }
	
  
  /**
   * POsune vsechny auta v seznamu o jeden krok
   * POuzije se pro nejaky tick hodin zvenci, bude se volat dokola
   */
  public void posunAuta() {
	  Iterator<Jizda> it = this.sa.iterator();
	  Jizda j;
	  
	  while (it.hasNext()) { // posunout vsema jizdama
		 j = it.next();
		 //j.posunJizdu();
	  }
	  
  }
	
}
