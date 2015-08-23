package cz.zcu.zswi2010.config;

/**
 * Trida pro konfiguraci, zejmena globalni konstanty volane staticky
 * @author ppolak
 *
 */
public class Konfig {

	public static final short POCET_PALET = 6; // pocet palet kolik se vejde na 1 auto
	public static final short DOBA_VYKLADKY = 30; // v minutach
	public static final short DELKA_OKENKA = 90; // v minutach
	public static final short START_PRAC_DOBY = 8 * 60;	// Aktualni cas pocet minut od 00:00. Zaciname v 6 hod. rano. (ja myslim ze 8 :)
	
	public static final short STAV_PRIPRAVENA = 1;
	public static final short STAV_NACESTE = 2;
	public static final short STAV_CEKA = 3;
	public static final short STAV_VYKLADA = 4;
	public static final short STAV_NAVRAT = 5;
	public static final short STAV_DOKONCENA = 6;
	
	public static final short START_SIM = 8 * 55; // Start simulatoru bude v 7:20
	public static final short TIK_INTERVAL_MS = 1000; // Jeden tik hodin v ms
}
