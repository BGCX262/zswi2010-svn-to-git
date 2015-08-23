package cz.zcu.zswi2010.main;
import cz.zcu.zswi2010.config.*;
/**
 * @author pavel.pour
 * Trida Zakazka predstavuje jednu objednavku od jednoho zakaznika. Obsahuje informace o sidle zakaznika,
 * jeho pozadavku a vykladacim okenku. Zakazky  jsou uzpusobeny pro uchovavani ve spojovem seznamu.
 * Priklad pro me: Zakazka je proste "3 krabice do Prahy", 1 jizda == 1 az X zakazek dohromady
 */
public class Zakazka implements Comparable<Zakazka>{
	//  TODO: Vymytit ten spojovej seznam...
	short mesto;
	public short zacatek_okenka;
	public short konec_okenka;
	short palet;
	//Zakazka next;
	
	public Zakazka(Mapa mapa, short mesto)
	{
		this.mesto = mesto;
		this.zacatek_okenka = mapa.getOkenko(mesto);
		this.konec_okenka = (short)(mapa.getOkenko(mesto) + Konfig.DELKA_OKENKA);
		this.palet = (short)(mapa.getPalet(mesto));
		//this.next = null;
	}
	
	/**
	 * Porovnavaci funkce pro zakazky
	 * Snad seradi seznam zakazek vzestupne od prvniho zacatku okenka nejnizsiho
	 * @param z Zakazka k porovnani
	 * @return vraci 1 pokud je tahle zakazka/zacatek okenka vetsi nez ta v parametru, ve zbylych pripadech dava -1
	 */
	public int compareTo(Zakazka z) {
		return ((this.zacatek_okenka > z.zacatek_okenka) ? 1 : -1);		
	}
}
