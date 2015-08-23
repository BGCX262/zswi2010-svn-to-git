package cz.zcu.zswi2010.main;

/**
 * 
 * @author pavel.pour
 *
 */
public class Gear {
	public static void main(String[] args)
	{
		Mapa mapa = new Mapa("plna", "data/");

//		mapa.vypisCesty();
//		mapa.vypisTrasy();
//		mapa.vypisExCasy();
//		mapa.vypisExTrasy();
		
		
//		Organizator organizator = new Organizator(mapa, (short)63);
//		short zakazky[] = {0, 1, 2, 5, 17, 27, 37, 44, 49, 52, 57};

		//Organizator organizator = new Organizator(mapa, (short)1000);
		Seznam_jizd seznam_jizd = new Seznam_jizd(mapa, (short)1000);
		//short zakazky[] = {0, 1, 2, 5, 17, 27, 37, 44, 49, 52, 57, 999, 1001};
			
//		for(int i = 0; i < zakazky.length; i++){
			
//			organizator.addZakazku(zakazky[i]);
			
//		}
		
		// Tohle asi udela to, ze vytvori 500 zakazek do mest 0-499
		for(short i = 0; i < 500; i++){
			seznam_jizd.pridatJizdu(new Jizda(mapa, (short) 1000, i, (short) 0));
		}
		
		seznam_jizd.vypisSeznamJizd();

		seznam_jizd.optimalizovatSeznam((short)480);

		seznam_jizd.vypisSeznamJizd();
		
	}
}
