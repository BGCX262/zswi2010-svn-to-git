package cz.zcu.zswi2010.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import cz.zcu.zswi2010.config.Konfig;

/**
 * Trida pro seznam aut a praci s nima
 * Int_poznamka: Stejnej srac jako pouzivam v BP pro seznam letadel - tikem s nima posouvat po mape/celym seznamem naraz
 * Mohlo by to vracet i pocet aut (aktualne v provozu), pamatovat si spicky (kdy, kolik) a tak
 * @author ppolak
 *
 */
public class Seznam_jizd {
	private ArrayList<Jizda> sj;
	private Mapa mapa;
	private short sidlo;
	
	/**
	 * Konstruktor, vytvori prazdny seznam jizd
	 * @param m - Mapa, je to Ta Mapa
	 * @param s - short sidlo spolecnosti
 	 */
	public Seznam_jizd(Mapa m, short s) {
		this.sj = new ArrayList<Jizda>();
		this.mapa = m;
		this.sidlo = s;
	}
	
	
	/**
	 * prida jednu jizdu na konec seznamu
	 * @param j Jizda - jizda k pridani
	 */
	public void pridatJizdu(Jizda j) {
		if (j.idJizdy > -1) { // -1 signalizuje, ze jizdu neslo vytvorit
			this.sj.add(j);
			this.seraditSeznam();
		}
	}
	
	
	/**
	* Zoptimalizuje vytizeni jednotlivych jizd, ktere opousteji sidlo vyrobce.
	* Jizdy jsou do fronty razeny tak, aby byla fronta serazena podle doby odjezdu - na cele fronty jsou nejdrive odjizdejici vozy.
	* Jizdy se stejnou dobou odjezdu jsou pak dale razeny podle mnozství nalozenych palet - nejvíce nalozene dopredu.
	* Prochazi se od zacatku fronta jizd, a pro jizdy, jimz nadesel cas odjezdu se dohledava z ostatnich jizd vhodny
	* naklad na doplneni tak, aby byly jizdy co nejlepe vytizeny a najelo se co nejmene kilometru v co nejkratsim case.
	* ppolak: co je ten parametr cas????
	* @param cas short cotoje?
	*/
	void optimalizovatSeznam(short cas)
	{
		Jizda jizda_k_pridani;
		short delkaJizdy;
		short zbyla_volna_mista;		// Zbytek volneho mista na automobilu.
		short usporaDelky = 0;
		short usporaCasu = 0;
		short prostoj = 0;	// Cas mezi dobou, kdy auto dorazi do mesta a zacatkem vykladaciho okenka.
		Iterator<Jizda> it_j, it_j2; // to popsani j a j2 se mi vubec nelibi, ale zatim nevim jak to spravit
		Jizda j, j2;
		
		it_j = this.sj.iterator();
		//Jizda curr_out = seznam_jizd;
		//while(curr_out.next != null){	// Vnejsi cyklus - Prochazime v cyklu, dokud nenarazime na konec fronty.
		int pom_pocitadlo = -1;
		while(it_j.hasNext()){	// Vnejsi cyklus - Prochazime v cyklu, dokud nenarazime na konec fronty.
			j = it_j.next();
			pom_pocitadlo++;
			if (j.getVolnaMista() == 0) { // pokud v aute nejsou volny mista, tak neni co resit
				continue;
			}
			
			// ppolak: vubec nechapu proc/co to je tahle podminka...
			// aha... jakmile auto odjede az po startu pracovni doby, tak uz ho neresime. Tuta funkce se spusti jakoze asi jeste pred zacatkem pracovni doby
			// a zbytek se pak bude resit nejak operativne
			if(Konfig.START_PRAC_DOBY != j.cas_start){	// Kdyz narazime na pozdeji odjizdejici jizdy cyklus prerusime.
				break;
//				curr_out = curr_out.next;
//				continue; 
			}
			short bonitaMem = -999;	// Inicializace - Posledni nejlepsi hodnota bonity zakazky.
			// Nechapu k cemu to zbytekMem slouzi... viz vyhozena podminka na konci
			short zbytekMem = -999;	// Inicializace - Posledni nejlepsi hodnota zbytku.
			jizda_k_pridani = null;		// Inicializace - ukazatel na jizdu=zakazku s posledni nejlepsi bonitou.

			it_j2 = this.sj.listIterator(pom_pocitadlo);
			
			//it_j2 = it_j;
			
			//Jizda curr_in = curr_out.next;	// Vnitrni cyklus prohledava frontu od mista za jizdou zpracovavanou ve vnejsim syklu.
			while (it_j2.hasNext()) {
				//	System.out.format("xxxxxxxx%d %d %d %d %d %d\n", gear.nyni, curr_out.start, curr_out.zakazky.mesto, curr_in.zakazky.mesto, curr_out.volno, gear.POCET_PALET - curr_in.volno);
				j2 = it_j2.next();
				zbyla_volna_mista = (short)(j.getVolnaMista() - j2.getObsazenaMista());
				// Pomoci if odfiltrujeme jednoduse pripady kdy auto prelozime a beznadejne se casove mijejici.
				// Pokud prelozenim j2 na j nepretizime j (zbytek >=0)
				// && konec vykladani j nenastane pred koncem okenka prvni zakazky j2 (tzn ma smysl se tim jeste zabyvat), pak dal
				if (zbyla_volna_mista >= 0 && j.cas_konec < j2.zakazky.getKonecPrvnihoOkenka()){
					// ppolak : Ta j2.konecna je extremne divna.. to se mi fakt nezda (je to tu vickrat pouzity!)
					short dobaJizdy = mapa.getDobu(j.konecna, j2.konecna);	// Doba jizdy z konecne do noveho mesta.
					// Pokud je doba jizdy prilis velka, abychom to stihli do dalsiho mesta, zkusime rychlejsi variantu.
					if(j.cas_konec + dobaJizdy > j2.zakazky.getKonecPrvnihoOkenka()){
						dobaJizdy = mapa.getExDobu(j.konecna, j2.konecna);	// Doba jizdy expresni variantou
						if(j.cas_konec + dobaJizdy > j.zakazky.getKonecPrvnihoOkenka()){
							// Nevyjde to ani expresne, nastavime ukazatel na dalsi jizdu.
							continue;
						} else {
							delkaJizdy = mapa.getExDelku(j.konecna, j2.konecna);
						}
					} else {
						delkaJizdy = mapa.getDelku(j.konecna, j2.konecna);
					}
					
					// ppolak : Tady uz se prestavam chytat a zacinam to jen tupe prepisovat
					prostoj = (short)(j2.zakazky.getZacatekPrvnihoOkenka() - j.cas_konec + dobaJizdy); // prostoj - cekani na okenko
					if(prostoj < 0){
						prostoj = 0;
					}
					usporaCasu = (short)((2 * mapa.getDobu(sidlo, j2.konecna)) - (delkaJizdy + prostoj + mapa.getDobu(sidlo, j2.konecna)));
					usporaDelky = (short)((2 * mapa.getDelku(sidlo, j2.zakazky.getPrvniMesto())) - (delkaJizdy + mapa.getDelku(sidlo, j2.zakazky.getPrvniMesto())));

					short bonita = (short)( (zbyla_volna_mista * (-10/Konfig.POCET_PALET) * delkaJizdy) + (usporaDelky * 10) + (usporaCasu * 2));

					System.out.format("Hodnoceno %d k %d s bonitou %d zbytek: %d palet, usporaDelky %d km, usporaCasu %d min, prostoj %d min\n",j2.zakazky.getPrvniMesto() , j.zakazky.getPrvniMesto(), bonita, zbyla_volna_mista, usporaDelky, usporaCasu, prostoj);
					if ((bonita > 0) && (bonita > bonitaMem)){	// Pokud je bonita teto kombinace lepsi, nez posledni nejlepsi, zapmatujeme si ji.
						j2.novyKonec = (short)(j.cas_konec + dobaJizdy + j2.getObsazenaMista() * Konfig.DOBA_VYKLADKY);
						jizda_k_pridani = j2;
						bonitaMem = bonita;
						zbytekMem = zbyla_volna_mista;
					}

				}
				// Nastavime ukazatel na dalsi jizdu ve vnitrnim cyklu.
			}  // prosli jsme vsechny jizdy az do konce
			
			if (jizda_k_pridani != null) {	// Pokud jsme nasli optimalni kombinaci, prelozime naklad
				System.out.format("Pridat %d k %d s bonitou %d \n", jizda_k_pridani.zakazky.getPrvniMesto(), j.zakazky.getPrvniMesto(), bonitaMem);
				this.pripojJizdu(j, jizda_k_pridani); // pripojit obsah jizdy na jizdu
				// podminku nechapu
				/*
				if(zbytekMem == 0) {
					j = curr_out.next;
					// Zde by melo auto odjet - byt prerazeno do jine fronty.
				}*/
				// Zde by melo auto odjet - byt prerazeno do jine fronty.
			}
		} // prosli jsme vsechny jizdy, jakoby tou prvni vlnou (j)
		
	}
	
	
	/**
	* Presune/pripoji naklad z jedne jizdy na druhou.
	* @param kam Jizda kam se ma pridat to co je nalozeno na Jizde "co"
	* @param co Jizda obsah, ktery se prelozi na Jizdu "kam"
	*/
	void pripojJizdu(Jizda kam, Jizda co)
	{
		kam.zakazky.pridejZakazku(co.zakazky.getCelySeznam()); // pripoji cely seznam z co na konec kam
		// Uprava hodnot - prasarna (nejsou setry)
		kam.konecna = co.konecna;	// Nova konecna.
		kam.cas_konec = co.novyKonec;	// Novy konec jizdy (cas).
		// TOhle by slo nahradit nejakym samospasnym Jizda.spocitejVolnaMista, coz by je spocetlo ze seznamu zakazek
		kam.setVolnaMista((short)(kam.getVolnaMista() - co.getObsazenaMista()));
		// Vyhodit ze seznamu "co" -- nevim jestli to bude fungovat
		//this.sj.remove(co);
		// TODO: otestovat funkcnost remove
	}
	
	
	/**
	 * Seradi seznam jizd
	 */
	public void seraditSeznam() {
		Collections.sort(this.sj);
	}
	
	
	/**
	 * Pro zadany minuty (od pulnoci) vrati vsechny jizdy v seznamu, ktere maji byt na ceste a vymaze je ze seznamu
	 * Predpoklada, ze je ten seznam serazeny !!!
	 * @param minuty HOdnota casu, minuty od pulnoci, vsechno <= tomuhle se vrati
	 * @return ArrayList<Jizda> Seznam vsech jizd, ktere se vyndavaji ze seznamu a vysilaji se do sveta
	 */
	public ArrayList<Jizda> popJizdy(short minuty) {
		Iterator<Jizda> it = this.sj.iterator();
		Jizda j;
		ArrayList<Jizda> jizdy_ven = new ArrayList<Jizda>();
		
		while (it.hasNext()) {
			j = it.next();
			if (j.cas_start > minuty) { // pokud uz jsme presli tu hranici casovou, tak ukonci cyklus
				break;
			} else {
				jizdy_ven.add(j); // pridej jizdu do lokalniho seznamku
				it.remove(); // odeber z hlavniho seznamu
			}
		}
		
		return jizdy_ven;
	}
	
	
	/**
	 * Vrati pocet jizd v seznamu
	 * @return int - pocet jizd v seznamu
	 */
	public int getPocetJizd() {
		return this.sj.size();
	}
	
	
	/**
	* Vypise frontu jizd pro ladici ucely.
	*/
	public void vypisSeznamJizd()
	{
		Iterator<Jizda> it_j = this.sj.iterator();
		Jizda j;
		
		System.out.println("Seznam jizd (Fronta)");
		while (it_j.hasNext()){
			j = it_j.next();
			System.out.format("Vyjezd: %d:%d (%d) Volno: %d Mesto: %d\n", j.cas_start/60, j.cas_start%60, j.cas_start, j.getVolnaMista(), j.zakazky.getPrvniMesto());
		}
	}
	
}
