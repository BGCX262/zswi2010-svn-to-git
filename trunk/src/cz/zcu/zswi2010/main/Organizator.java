package cz.zcu.zswi2010.main;

/**
 * @author pavel.pour
 * @author ppolak
 * Rekneme si to na rovinu, tahle trida je uplne k nicemu. :) Vsechno tady resene by mela pokryt trida Seznam_jizd
 * 
 * Trida Organizator predstavuje frontu automobilu a zakazek, cekajicich na odjezd k zakaznikovi.
 * Pred tím, nez je auto vypusteno, je co nejlepe nalozeno tak, aby se co nejvice minimalizovaly naklady na dopravu.
 * Naklady jsou vycislovany v penezich. Odjizdejici automobily jsou prevedeny do fronty simulujici provoz na silnicich.
 */
public class Organizator {
	private Seznam_jizd seznam_jizd = null; // seznam jizd
	private short sidlo; // sidlo firmy
	private short idJizdy = 0; // na co to vlastne je???
	private Mapa mapa;
	
	/**
	* Konstruktor.
	* Ulozi ukazatel na mapu a ID mesta, ktere predstavuje sidlo firmy.
	* @param mapa Mapa
	* @param sidlo ID mesta, ktere je sidlem firmy
	*/
	public Organizator(Mapa mapa, short sidlo)	{
		this.mapa = mapa;
		this.sidlo = sidlo;
	}

	/**
	* Vlozi jednu jizdu do fronty.
	* ppolak : FRONTA == SEZNAM_JIZD
	* Jizdy jsou do fronty razeny tak, aby byla fronta serazena podle doby odjezdu - na cele fronty jsou nejdrive odjizdejici vozy.
	* Jizdy se stejnou dobou odjezdu jsou pak dale razeny podle mnozství nalozenych palet - nejvíce nalozene dopredu.
	* @param cil Parametrem je ID mesta, kam se pojede.
	*/
	public void pridatJizdu(short cil)	{
		Jizda jizda_pro_pridani = new Jizda(mapa, sidlo, cil, idJizdy);	// Vytvorime novou jizdu k pridani.
		
		if(jizda_pro_pridani.idJizdy == -1){ // Jizdu neslo pridat - naklad nelze dorucit.
			idJizdy--;
			return;
		} else {
			idJizdy++;
			seznam_jizd.pridatJizdu(jizda_pro_pridani);
		}
		
		// Prochazime v cyklu frontu a vkladame jizdu na spravne misto.
		// ppolak: co je "spravny misto" ?
		// Jo, takze - razeni: 1. podle cas_start, nejmensi na zacatku, (pri shode cas_start) 2. podle volnych mist, nejplnejsi jdou na zacatek
		// Jee, aha, takze viz popis metody "optimalizovat"
		
		/*
		while(current != null){
			if( ((int)current.cas_start > (int)jizda_pro_pridani.cas_start) || 
			((int)current.cas_start == (int)jizda_pro_pridani.cas_start && (int)current.getVolnaMista() >= (int)jizda_pro_pridani.getVolnaMista()) ){
		*/
	}

	
	/**
	* Presune naklad z jedne jizdy na druhou.
	* Jizdy jsou zadany svymi ukazateli.
	* ppolak: Tomu vubec nerozumim, to je nejaka blbost...
	* 
	*/
	/*
	void presunout(Jizda kam, Jizda co)
	{
		Zakazka curr = kam.zakazky;
		while(curr.next != null)	// Najdeme si ukazatel na konec seznamu zakazek "nalozenych" na jizde kam.
		{
			curr = curr.next;
		}
			// Na konec seznamu pripojime zakazku co
		curr.next = co.zakazky;
		co.prev.next = co.next;
		co.next.prev = co.prev;
			// Zaroven musime upravit i celkove hodnoty v jizde
		// TODO: Tohle je na houby to set a get naraz, jeste predelat
		kam.setVolnaMista((short)(kam.getVolnaMista() - (Konfig.POCET_PALET - co.getVolnaMista())));
		kam.konecna = co.konecna;	// Nova konecna.
		kam.cas_konec = co.novyKonec;	// Novy konec jizdy (cas).
	}
	*/

	/**
	* Zoptimalizuje vytizeni jednotlivych jizd, ktere opousteji sidlo vyrobce.
	* Jizdy jsou do fronty razeny tak, aby byla fronta serazena podle doby odjezdu - na cele fronty jsou nejdrive odjizdejici vozy.
	* Jizdy se stejnou dobou odjezdu jsou pak dale razeny podle mnozství nalozenych palet - nejvíce nalozene dopredu.
	* Prochazi se od zacatku fronta jizd, a pro jizdy, jimz nadesel cas odjezdu se dohledava z ostatnich jizd vhodny
	* naklad na doplneni tak, aby byly jizdy co nejlepe vytizeny a najelo se co nejmene kilometru v co nejkratsim case.
	* ppolak: co je ten parametr cas????
	* @param cas short cotoje?
	*/
	/*
	void optimalizovat(short cas)
	{
		Jizda jizda_k_pridani;
		short delkaJizdy;
		short zbytek;		// Zbytek volneho mista na automobilu.
		short usporaDelky = 0;
		short usporaCasu = 0;
		short prostoj = 0;	// Cas mezi dobou, kdy auto dorazi do mesta a zacatkem vykladaciho okenka.
		
		Jizda curr_out = seznam_jizd;
		while(curr_out.next != null){	// Vnejsi cyklus - Prochazime v cyklu, dokud nenarazime na konec fronty.
			if(Konfig.START_PRAC_DOBY != curr_out.cas_start){	// Kdyz narazime na pozdeji odjizdejici jizdy cyklus prerusime.
				break;
//				curr_out = curr_out.next;
//				continue; 
			}
			short bonitaMem = -999;	// Inicializace - Posledni nejlepsi hodnota bonity zakazky.
			short zbytekMem = -999;	// Inicializace - Posledni nejlepsi hodnota zbytku.
			jizda_k_pridani = null;		// Inicializace - ukazatel na jizdu=zakazku s posledni nejlepsi bonitou.

			Jizda curr_in = curr_out.next;	// Vnitrni cyklus prohledava frontu od mista za jizdou zpracovavanou ve vnejsim syklu.
			while(curr_in.next != null){
//	System.out.format("xxxxxxxx%d %d %d %d %d %d\n", gear.nyni, curr_out.start, curr_out.zakazky.mesto, curr_in.zakazky.mesto, curr_out.volno, gear.POCET_PALET - curr_in.volno);
		
				zbytek = (short)(curr_out.getVolnaMista() - (Konfig.POCET_PALET - curr_in.getVolnaMista()));
					// Pomoci if odfiltrujeme jednoduse pripady kdy auto prelozime a beznadejne se casove mijejici.
				if(zbytek >= 0 && curr_out.cas_konec < curr_in.zakazky.konec_okenka){
					
					short dobaJizdy = mapa.getDobu(curr_out.konecna, curr_in.konecna);	// Doba jizdy z konecne do noveho mesta.
						// Pokud je doba jizdy prilis velka, abychom to stihli do dalsiho mesta, zkusime rychlejsi variantu.
					if(curr_out.cas_konec + dobaJizdy > curr_in.zakazky.konec_okenka){
						dobaJizdy = mapa.getExDobu(curr_out.konecna, curr_in.konecna);	// Doba jizdy expresni variantou
						if(curr_out.cas_konec + dobaJizdy > curr_in.zakazky.konec_okenka){
							curr_in = curr_in.next;		// Nevyjde to ani expresne, nastavime ukazatel na dalsi jizdu.
							continue;
						}else{
							delkaJizdy = mapa.getExDelku(curr_out.konecna, curr_in.konecna);
						}
					}else{
						delkaJizdy = mapa.getDelku(curr_out.konecna, curr_in.konecna);
						
					}

					prostoj = (short)(curr_in.zakazky.zacatek_okenka - curr_out.cas_konec + dobaJizdy); // prostoj - cekani na okenko
					if(prostoj < 0){
						prostoj = 0;
					}
					usporaCasu = (short)((2 * mapa.getDobu(sidlo, curr_in.konecna)) - (delkaJizdy + prostoj + mapa.getDobu(sidlo, curr_in.konecna)));
					usporaDelky = (short)((2 * mapa.getDelku(sidlo, curr_in.zakazky.mesto)) - (delkaJizdy + mapa.getDelku(sidlo, curr_in.zakazky.mesto)));

					short bonita = (short)( (zbytek * (-10/Konfig.POCET_PALET) * delkaJizdy) + (usporaDelky * 10) + (usporaCasu * 2));

					System.out.format("Hodnoceno %d k %d s bonitou %d zbytek: %d palet, usporaDelky %d km, usporaCasu %d min, prostoj %d min\n",curr_in.zakazky.mesto , curr_out.zakazky.mesto, bonita, zbytek, usporaDelky, usporaCasu, prostoj);
					if(bonita > 0 && bonita > bonitaMem){	// Pokud je bonita teto kombinace lepsi, nez posledni nejlepsi, zapmatujeme si ji.
						curr_in.novyKonec = (short)(curr_out.cas_konec + dobaJizdy + ((Konfig.POCET_PALET - curr_in.getVolnaMista()) * Konfig.DOBA_VYKLADKY));
						jizda_k_pridani = curr_in;
						bonitaMem = bonita;
						zbytekMem = zbytek;
					}

				}
				curr_in = curr_in.next;	// Nastavime ukazatel na dalsi jizdu ve vnitrnim cyklu.
			}
			if(jizda_k_pridani != null){	// Pokud jsme nasli optimalni kombinaci, prelozime naklad
				System.out.format("Pridat %d k %d s bonitou %d \n", jizda_k_pridani.zakazky.mesto, curr_out.zakazky.mesto, bonitaMem);
				this.presunout(curr_out, jizda_k_pridani);//pridat
				if(zbytekMem == 0){
					curr_out = curr_out.next;
					// Zde by melo auto odjet - byt prerazeno do jine fronty.
				}
			}else{
				curr_out = curr_out.next;
				// Zde by melo auto odjet - byt prerazeno do jine fronty.
			}
		}
		
	}
	*/
	
}
