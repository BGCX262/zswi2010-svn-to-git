package cz.zcu.zswi2010.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * Trida pro seznam zakazek
 * @author ppolak
 *
 */
public class Seznam_zakazek {
	private ArrayList<Zakazka> sz;
	
	/**
	 * Konstruktor, pro prazdnej seznam
	 */
	public Seznam_zakazek() {
		this.sz = new ArrayList<Zakazka>();
	}
	
	/**
	 * Konstruktor, ktery rovnou vlozi jednu zakazku do seznamu
	 * @param z Zakazka pro vlozeni na zacatek seznamu
	 */
	public Seznam_zakazek(Zakazka z) {
		this.sz = new ArrayList<Zakazka>();
		this.sz.add(z);
	}
	
	/**
	 * Prida jednu zakazku na konec seznamu
	 * @param z Zakazka zakazka k pridani na konec seznamu
	 */
	public void pridejZakazku(Zakazka z) {
		this.sz.add(z);
		this.seraditSeznam();
	}
	
	
	/**
	 * Prida seznam zakazek na konec seznamu
	 * @param sz ArrayList<Zakazka> seznam zakazek k pridani na konec seznamu
	 */
	public void pridejZakazku(ArrayList<Zakazka> sz) {
		this.sz.addAll(sz);
		this.seraditSeznam();
	}
	
	/** 
	 * Tohle bude asi nutnost, pac sem vyhodil to Pavlovo razeni rovnou pri vytvareni
	 * Tam jak se to ve spojovym seznamu vkladalo primo na nejaky vybrany misto
	 * Naucit se a pouzit sort z Collection
	 */
	public void seraditSeznam() {
		Collections.sort(this.sz);
	}
	
	
	
	/**
	 * Ziska zacatek okenka prvni zakazky v seznamu
	 * Nahrazuje Pavlovo puvodni primy volani na prvnim ukazateli
	 * @return short zacatek prvniho okenka nebo -1 pokud je seznam prazdny
	 */
	public short getZacatekPrvnihoOkenka() {
		Iterator<Zakazka> it_z = this.sz.iterator();
		Zakazka z;
		if (it_z.hasNext()) { // mame aspon tu jednu zakazku
			z = it_z.next();	
			return z.zacatek_okenka;	
		} else {
			return -1;
		}
	}
	
	
	/**
	 * KOnec okenka prvni zakazky
	 * Optimalizator to nekde pouziva
	 * @return short - konec prvniho okenka v seznamu nebo -1 pokud je seznam prazdny
	 */
	public short getKonecPrvnihoOkenka() {
		Iterator<Zakazka> it_z = this.sz.iterator();
		Zakazka z;
		if (it_z.hasNext()) { // mame aspon tu jednu zakazku
			z = it_z.next();	
			return z.konec_okenka;	
		} else {
			return -1;
		}
	}

	
	/**
	 * Ziska ID prvniho mesta do kteryho se neco veze - kam se veze prvni zakazka
	 * @return short - ID prvniho mesta nebo -1 pokud je seznam prazdny
	 */
	public short getPrvniMesto() {
		Iterator<Zakazka> it_z = this.sz.iterator();
		Zakazka z;
		if (it_z.hasNext()) { // mame aspon tu jednu zakazku
			z = it_z.next();	
			return z.mesto;	
		} else {
			return -1;
		}
	}
	
	
	/**
	 * Getra
	 * @return vraci cely seznam zakazek, vhodne pro pripojovani
	 */
	public ArrayList<Zakazka> getCelySeznam() {
		return this.sz;
	}
	
	
	/**
	 * Vraci pocet zakazek v seznamu
	 * @return int - Pocet zakazek v seznamu
	 */
	public int getPocetZakazek() {
		return this.sz.size();
	}
}
