package cz.zcu.zswi2010.main;

import java.io.*;

/**
 * @author pavel.pour
 * @author ppolak
 * Trida Mapa uchovava vsechny topologické a logisticke informace o mestech.
 * Ziskava data ze souboru s popisem mapy, který je ulozen na disku a jehoz jmeno je specifikovano pri vytvareni tridy.
 * Dale trida dopocitava nektere delkove a casove hodnoty. Vypoctene hodnoty si uklada na disk pro pozdejsi vyuziti.
 */
public class Mapa {
	private class Mesto	// Udaje o jednotlivych mestech
	{
		String nazev;
		short velikost;
		short okenko;
	}
	Mesto mesta[];
	
	// Delkove udaje
	short spoje[][];	// Vzdalenosti mezi dvojicemi mest z mapy.
	short cesty[][];	// Nejkratsi cesty mezi dvojicemi mest - vypocteno (vcetne ruznych zkratek)
	short trasy[][];	// Matice sousednosti, z ktere lze rekonstruovat trasy popsane v matici cesty pouze koncovými body.
	// Casove udaje ex = expresni
	short casy[][];		// Doba, za kterou lze projet cesty mezi dvojicemi mest z mapy.
	short exCesty[][];	// Nejmensi doby, za ktere lze projet mezi dvojicemi mest - vypocteno (vcetne ruznych zkratek)
	short exTrasy[][];	// Matice sousednosti, z ktere lze rekonstruovat trasy popsane v exCesty cesty pouze koncovými body.
	File cache_file;  // Cache soubor s predpocitanymi daty

	int POCET_MEST;		// Pocet mest ulozenych v mape

	
	/**
	* Konstruktor.
	* Vytvori a inicializuje matice a nacte do nich data z mapy.
	* V zavisloti na tom, zda se jedna o studeny/teply start vypoccte nektere dalsi hodnoty, nebo je naxte z disku.
	*/
	public Mapa (String filename, String path)
	{
		BufferedReader bfr;
		int i, j;
		String radka;
		File txt_file = new File(path + filename + ".txt");	// Slozime si cely nazev souboru
		
		this.cache_file = new File(path + filename + ".csh");
		
		// TODO: Vyhodit to nacitani z konstruktoru!
		// Otevrit soubor mapy a data nacte do matic.
		try {	
			bfr = new BufferedReader(new FileReader(txt_file));
			
			// Na prvni radce souboru je ulozen pocet mest ulozenych v souboru.
			radka = bfr.readLine();		
			this.POCET_MEST = Integer.parseInt(radka);
		    
			// Inicializuj matice
			this.initMatice();
			
			// Naplneni matic daty ze souboru
			i = 0;
			while((radka = bfr.readLine()) != null){
				String tokeny[] = radka.split(" ");
				mesta[i] = new Mesto();
				mesta[i].nazev = tokeny[1];	
				mesta[i].velikost = Short.parseShort(tokeny[2]);	
				mesta[i].okenko = Short.parseShort(tokeny[3]);
				//System.out.format("%s %s %d %d \n", tokeny[0], mesta[i].nazev, mesta[i].velikost, mesta[i].okenko);
				
				for(j = 4; j < tokeny.length; j++){
					String spoj[] = tokeny[j].split(":");
					short sousedID = Short.parseShort(spoj[0]);
					spoje[i][sousedID] = Short.parseShort(spoj[1]);
					spoje[sousedID][i] = Short.parseShort(spoj[1]);
					cesty[i][sousedID] = Short.parseShort(spoj[1]);
					cesty[sousedID][i] = Short.parseShort(spoj[1]);
					casy[i][sousedID] = Short.parseShort(spoj[2]);
					casy[sousedID][i] = Short.parseShort(spoj[2]);
					exCesty[i][sousedID] = Short.parseShort(spoj[2]);
					exCesty[sousedID][i] = Short.parseShort(spoj[2]);
					trasy[i][sousedID] = sousedID;
					trasy[sousedID][i] = (short)i;
					exTrasy[i][sousedID] = sousedID;
					exTrasy[sousedID][i] = (short)i;

					//System.out.format(" -%s %d %d\n", spoj[0], cesty[i][Short.parseShort(spoj[0])], casy[i][Short.parseShort(spoj[0])]);
				}
				i++;
			}
			bfr.close();
		} catch(IOException e){
			System.out.println("Chyba pri cteni ze souboru " + txt_file);
			e.printStackTrace();
		}
		
		// Zkusime, zda mame ulozen soubor s kesovanymi daty matic. Otevreme jej a nacteme matice.
		// Tady se to musi udelat pres IF/ELSE, aby se pak dal overovat treba checksum, pokud soubor existuje ale zmenil se zdroj
		// TODO: Implementovat checksum pro zdrojovy soubor a overit jestli se zdroj nezmenil
		if (this.cache_file.exists()) { // Tady se navic overi && checksum je stale stejnej
			try {	
				bfr = new BufferedReader(new FileReader(cache_file));

				for(i = 0; i < POCET_MEST; i++){
					radka = bfr.readLine();
					String tokeny[] = radka.split(" ");
					for(j = 0; j < tokeny.length; j++){
						cesty[i][j] = Short.parseShort(tokeny[j]);
					}
				}				
				for(i=0; i < POCET_MEST; i++){
					radka = bfr.readLine();
					String tokeny[] = radka.split(" ");
					for(j = 0; j < tokeny.length; j++){
						trasy[i][j] = Short.parseShort(tokeny[j]);
					}
				}				
				for(i=0; i < POCET_MEST; i++){
					radka = bfr.readLine();
					String tokeny[] = radka.split(" ");
					for(j = 0; j < tokeny.length; j++){
						exCesty[i][j] = Short.parseShort(tokeny[j]);
					}
				}				
				for(i=0; i < POCET_MEST; i++){
					radka = bfr.readLine();
					String tokeny[] = radka.split(" ");
					for(j = 0; j < tokeny.length; j++){
						exTrasy[i][j] = Short.parseShort(tokeny[j]);
					}
				}				
				System.out.println("Nacteny matice");
				
			} catch(IOException e){	
				System.out.println("Chyba pri cteni ze souboru " + this.cache_file);
				e.printStackTrace();
			}				
		} else { //Soubor s kesovanymi maticemi neni (nebo neni aktualni), musime data vypocitat a ulozit.
			System.out.println("Pocitam vzdalenosti");
			spoctiVzdalenosti();
			System.out.println("Pocitam casy");
			spoctiExCasy();
			System.out.println("Ukladam matice");
			zapisMatice();
		}
	} // konec konstruktoru

	/**
	 * Vytvori matice a naplni je vychozimi hodnotami
	 */
	private void initMatice() {
		// Vytvoreni matic
		mesta = new Mesto[POCET_MEST];
		spoje = new short[POCET_MEST][POCET_MEST];
		cesty = new short[POCET_MEST][POCET_MEST];
		trasy = new short[POCET_MEST][POCET_MEST];
		casy = new short[POCET_MEST][POCET_MEST];
		exCesty = new short[POCET_MEST][POCET_MEST];
		exTrasy = new short[POCET_MEST][POCET_MEST];

		// Inicializace delkovych matic
		for(int i = 0; i < POCET_MEST; i++){
			for(int j = 0; j < POCET_MEST; j++){
				if (i == j) {
					cesty[i][j] = 0;
					exCesty[i][j] = 0;
				} else {
					cesty[i][j] = 999;
					exCesty[i][j] = 999;
				}
				trasy[i][j] = -1;
				exTrasy[i][j] = -1;
			}
		}	
	} // konec initMatice
	
	/**
	* Zapise vypoctene matice do souboru, pro dalsi pouziti pri dalsim startu programu.
	*/
	void zapisMatice()
	{
		FileWriter fw;
		try {
			fw = new FileWriter(cache_file);
		
			for(int i = 0; i < POCET_MEST; i++){
				for(int j = 0; j < POCET_MEST; j++){
					fw.write(cesty[i][j]+" ");
		
				}
				fw.write("\n");
			}
			for(int i = 0; i < POCET_MEST; i++){
				for(int j = 0; j < POCET_MEST; j++){
					fw.write(trasy[i][j]+" ");
		
				}
				fw.write("\n");
			}
			for(int i = 0; i < POCET_MEST; i++){
				for(int j = 0; j < POCET_MEST; j++){
					fw.write(exCesty[i][j]+" ");
		
				}
				fw.write("\n");
			}
			for(int i = 0; i < POCET_MEST; i++){
				for(int j = 0; j < POCET_MEST; j++){
					fw.write(exTrasy[i][j]+" ");
		
				}
				fw.write("\n");
			}
			fw.close();
		
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	* Vrati nazev mesta dle jeho ID.
	*/
	String getNazev(short mesto) {	return (mesta[mesto].nazev);	}
	
	/**
	* Vrati zacatek vykladaciho okenka mesta dle jeho ID.
	*/
	short getOkenko(short mesto) {	return (mesta[mesto].okenko); }

	/**
	* Vrati pocet odebiranych palet mesta dle jeho ID.
	*/
	short getPalet(short mesto) {	return (mesta[mesto].velikost); }
	
	/**
	* Vrati delku nejkratsi cesty mezi mesty dle jejich ID.
	*/
	short getDelku(short mesto1, short mesto2) { return (cesty[mesto1][mesto2]);	}
		
	/**
	* Vrati dobu projeti nejkratsi cesty mezi mesty dle jejich ID.
	*/
	short getDobu(short mesto, short mestodo)
	{
		short cas = 0;
		short uz;	// Mesto z ktereho merime usek
//			System.out.format("hledam cestu mezi %d a %d", mesto, mestodo);
	
		uz = mesto;	// Projde matici sousednosti a secte jednotlive doby jizdy mezi sousedy.
		while (mesto != mestodo){
			mesto = trasy[mesto][mestodo];
//			System.out.format(" %d ", mesto);
			cas += casy[uz][mesto];
			uz = mesto;
		}
		return(cas);
	}

	/**
	* Vrati delku nejrychlejsi cesty mezi mesty dle jejich ID.
	*/
	short getExDelku(short mesto, short mestodo)
	{
		short draha = 0;
		short uz;	// Mesto z ktereho merime usek
//			System.out.format("hledam cestu mezi %d a %d", mesto, mestodo);
	
		uz = mesto;
		while (mesto != mestodo){
			mesto = exTrasy[mesto][mestodo];
//			System.out.format(" %d ", mesto);
			draha += spoje[uz][mesto];
			uz = mesto;
		}
		return(draha);
	}

	/**
	* Vrati dobu projeti nejrychlejsi cesty mezi mesty dle jejich ID.
	*/
	short getExDobu(short mesto, short mestodo)
	{
		return(exCesty[mesto][mestodo]);
	}	
		
	/**
	* Vypise matici cest pro ladici ucely.
	*/
	public void vypisCesty()
	{
		System.out.format("\n    ");		
		for(int i=0; i < POCET_MEST; i++){
			System.out.format("%3d ", i);
		}
		System.out.format("\n\n");		
		for(int i=0; i < POCET_MEST; i++){
			System.out.format("%2d: ", i);
			for(int j=0; j < POCET_MEST; j++){
				System.out.format("%3d ", cesty[i][j]);
			}
			System.out.format("\n");		
		}
		
	}	
	
	/**
	* Vypise matici nejkratsich tras mezi mesty pro ladici ucely.
	*/
	public void vypisTrasy()
	{
		System.out.format("\n    ");		
		for(int i=0; i < POCET_MEST; i++){
			System.out.format("%3d ", i);
		}
		System.out.format("\n\n");		
		for(int i=0; i < POCET_MEST; i++){
			System.out.format("%2d: ", i);
			for(int j=0; j < POCET_MEST; j++){
				System.out.format("%3d ", trasy[i][j]);
			}
			System.out.format("\n");		
		}
	}
	
	/**
	* Vypise matici nejkratsich casu jizdy mezi mesty pro ladici ucely.
	*/
	public void vypisExCasy()
	{
		
		System.out.format("\n    ");		
		for(int i=0; i < POCET_MEST; i++){
			System.out.format("%3d ", i);
		}
		System.out.format("\n\n");		
		for(int i=0; i < POCET_MEST; i++){
			System.out.format("%2d: ", i);
			for(int j=0; j < POCET_MEST; j++){
				System.out.format("%3d ", exCesty[i][j]);
			}
			System.out.format("\n");		
		}
	}

	/**
	* Vypise matici nejrychlejsich tras mezi mesty pro ladici ucely.
	*/
	public void vypisExTrasy()
	{
		System.out.format("\n    ");		
		for(int i=0; i < POCET_MEST; i++){
			System.out.format("%3d ", i);
		}
		System.out.format("\n\n");		
		for(int i=0; i < POCET_MEST; i++){
			System.out.format("%2d: ", i);
			for(int j=0; j < POCET_MEST; j++){
				System.out.format("%3d ", exTrasy[i][j]);
			}
			System.out.format("\n");		
		}
	}


	/**
	* Spocte nejkradsi trasy mezi dvojicemi mest a naplni matici sousednosti, kde jsou trasy ulozeny.
	* Pro vypocet se pouziva Floyd-Warshalluv algoritmus.
	*/
	public void spoctiVzdalenosti()
	{
		int i, u, v;
		for(i=0; i< POCET_MEST; i++){
//System.out.format("%d\n", i);
			for(u=0; u< POCET_MEST; u++){
				for(v=0; v< POCET_MEST; v++){
					if(cesty[u][v] > cesty[u][i] + cesty[i][v]){
						cesty[u][v] = (short)(cesty[u][i] + cesty[i][v]);
						trasy[u][v] = trasy[u][i];
					}
				}
			}
		}
	}
	
	/**
	* Spocte nejrychlejsi trasy mezi dvojicemi mest a naplni matici sousednosti, kde jsou trasy ulozeny.
	* Pro vypocet se pouziva Floyd-Warshalluv algoritmus.
	*/
	public void spoctiExCasy()
	{
		int i, u, v;
		for(i=0; i< POCET_MEST; i++){
//System.out.format("%d\n", i);
			for(u=0; u< POCET_MEST; u++){
				for(v=0; v< POCET_MEST; v++){
					if(exCesty[u][v] > exCesty[u][i] + exCesty[i][v]){
						exCesty[u][v] = (short)(exCesty[u][i] + exCesty[i][v]);
						exTrasy[u][v] = exTrasy[u][i];
					}
				}
			}
		}
	}
}
