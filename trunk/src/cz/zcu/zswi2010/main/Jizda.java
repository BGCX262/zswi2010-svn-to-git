package cz.zcu.zswi2010.main;

import cz.zcu.zswi2010.config.*;

/**
 * @author pavel.pour
 * Trida Jizda predstavuje jednu jizdu jednoho nakladniho automobilu. Protoze jedna jizda muze vezt vice zakazek,
 * promene tridy obsahuji informace o celkovem prepravovanem mnozstvi atd. Jizda v sobe uchovava jednotlive zakazky,
 * ktere rozvazi. Jizdy jsou uzpusobeny pro uchovavani ve spojovem seznamu.
 * 
 * ppolak: Podle me se to mohlo jmenovat Auto nebo Ridic 
 */
public class Jizda implements Comparable<Jizda> {
	short idJizdy; // ID
	short cas_start; // startovni cas, asi minuty od pulnoci
	short cas_konec; // cas konce
	private short volna_mista; // zbyvajici volna mista
	private short obsazena_mista; // obsazena mista = pocet krabic
	short konecna; // id koncoveho mesta 
	short novyKonec; // wth - co to je? - je to v "optimalizovat", ale porad presne nechapu vyznam, proc nepouzit cas_konec
	Seznam_zakazek zakazky; // nova verze seznamu zakazek v aute/jizde
	short stav; // na zakladne, na ceste, vyklada, ceka, dokoncena (bacha ma se to vracet zpatky na zakladnu!) to by melo stacit
	// headingToCity
	// remainingKmToCity
	// remainingKmToDestination
	
	/**
	* Konstruktor.
	* Vytvori jizdu. Jeste pred tim zkontroluje (v konstruktoru jizdy), zda je mozne zakazku vubec dorucit do stanoveneho casu.
	* Nemoznost zakazku dorucit signalizuje nastavenim hodnoty idJizdy na -1.
	* TODO: Lip popsat, je to celkem dulezity - udelat param komentare
	*/
	public Jizda(Mapa mapa, short sidlo, short mesto, short idJizdy)
	{
		short opstart; // Optimalni doba startu - prijezd na zacatek okenka.
		short dobacesty;
		
		dobacesty = mapa.getDobu(sidlo, mesto);	// Doba nejkratsi cesty
		opstart = (short)(mapa.getOkenko(mesto) - dobacesty);
		
		if(Konfig.START_PRAC_DOBY > (short)(opstart + Konfig.DELKA_OKENKA)){	// Nestihneme prijet do konce okenka nejkratsi cestou.
			dobacesty = mapa.getExDobu(sidlo, mesto);	// Zkusime nejrychlejsi cestu.
			opstart = (short)(mapa.getOkenko(mesto) - dobacesty);
			if(Konfig.START_PRAC_DOBY > (short)(opstart + Konfig.DELKA_OKENKA)){	// Nestihneme to ani nejrychlejsi cestou.
				// Oznamit neprijeti objednavky - nelze dorucit
				System.out.format("objednavku do mesta %d %s nelze dorucit \n", mesto, mapa.getNazev(mesto));
				this.idJizdy = -1;	// Signalizujeme nemoznost prijmout zakazku.
				// TODO: Nejak lip vyresit tu nemoznost vytvoreni
				return;
			}
			
		}
		
		if(opstart < Konfig.START_PRAC_DOBY){	// Nastavime cas odjezdu
			this.cas_start = Konfig.START_PRAC_DOBY;
		}else{
			this.cas_start = opstart;
		}
			// Inicializujeme promene.
		this.idJizdy = idJizdy;
		this.setVolnaMista((short)(Konfig.POCET_PALET - mapa.getPalet(mesto)));
		this.konecna = mesto;
		this.cas_konec = (short)(this.cas_start + dobacesty + (mapa.getPalet(mesto) * Konfig.DOBA_VYKLADKY));
		this.zakazky = new Seznam_zakazek(new Zakazka(mapa, mesto)); // Vytvorime seznam a hned "nalozime" na auto novou zakazku.
		this.novyKonec = 0;
	}
	
	/**
	* Konstruktor.
	* Vytvori prazdnou jizdu. Tato jizda slouzi pouze k ukonceni fronty, kvuli jejimu lepsimu zpracovani.
	* 
	* TODO: Blbost, nepouzivat, zrusit
	*/
	public Jizda()
	{
		this.cas_start = Short.MAX_VALUE;
		this.zakazky = null;
		this.setVolnaMista(Konfig.POCET_PALET);
		this.novyKonec = 0;
		//this.prev = null;
		//this.next = null;
	}
	
	
	//?????????????????????
	/**
	 * Hadam, ze by to melo vratit zacatek okenka nejprvnejsi zakazky na aute
	 * Nevim sice k cemu to zatim je, ale casem to zkusim opravit
	 * Hlavne je to nadbytecny pre-volani
	 * TODO: Podle me to nebude mozna fungovat, overit!
	 */
	public short getPrvniOkenko()
	{	
		return(this.zakazky.getZacatekPrvnihoOkenka());	
	}
	
	
	/**
	 * Porovnavaci funkce, implementace interfacu Comparable
	 * Radit nejdriv podle doby odjezdu (napred ty nejvic brzo)
	 * Pak radit podle nalozenosti, nejvic nalozene dopredu
	 */
	public int compareTo(Jizda j) {
		if (this.cas_start != j.cas_start) {
			return (this.cas_start > j.cas_start) ? 1 : -1;
		} else { // odjezdovy cas je stejny, radit podle nalozenosti
			return (this.obsazena_mista <= j.obsazena_mista) ? 1 : -1;
		}
	}
	
	// Getry & Setry
	public short getVolnaMista() { return this.volna_mista;	}
	public short getObsazenaMista() { return this.obsazena_mista ; }
	
	public void setVolnaMista(short pocet) { 
		this.volna_mista = pocet;
		this.obsazena_mista = (short) (Konfig.POCET_PALET - pocet);
	}
	
	public void setStav(short stav) {  this.stav = stav; }
	
	
	
}
