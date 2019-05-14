import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class SimEnka {
	
	public static Scanner scan = new Scanner (System.in);
	
	public static List<String> konacanSkupUlaznihZnakova = new ArrayList<>(); //moze biti duplikata
	public static Set<String> konacanSkupStanja = new TreeSet<>();
	public static Set<String> skupPrihvatljivihStanja = new TreeSet<>();
	public static Set<String> skupSimbolaAbecede=new TreeSet<>();
	public static String pocetnoStanje;
	public static List<String> funkcije=new ArrayList<>(); //tu dolaze sve funkcije prijelaza kao cjeloviti niz
	
	
	public static String ulazniZnakovi[] = null;
	public static String svaStanja[] = null;
	public static String simboliAbecede[] = null;
	public static String prihvatljivaStanja[]=null;
	
	
	public static Set<String> trenutnaStanja = new TreeSet<>();
	public static Set<String> sljedecaStanja = new TreeSet<>();
	public static Set<String> epsilonStanja = new TreeSet<>();
	
	public static void main(String[] args) {
		
		String line1=scan.nextLine();
		ulazniZnakovi=line1.split("\\|");
		for(int i=0; i<ulazniZnakovi.length; i++) {
			konacanSkupUlaznihZnakova.add(ulazniZnakovi[i]);
		}
		
		String line2=scan.nextLine();
		svaStanja=line2.split(",");
		for(int i=0; i<svaStanja.length; i++) {
			konacanSkupStanja.add(svaStanja[i]);
		}
		String line3=scan.nextLine();
		simboliAbecede=line3.split(",");
		for(int i=0; i<simboliAbecede.length; i++) {
			skupSimbolaAbecede.add(simboliAbecede[i]);
		}
		String line4=scan.nextLine();
		prihvatljivaStanja=line4.split(",");
		for(int i=0; i<prihvatljivaStanja.length; i++) {
			skupPrihvatljivihStanja.add(prihvatljivaStanja[i]);
		}
		
		pocetnoStanje=scan.nextLine();
		
		while(scan.hasNextLine()) {
			String line=scan.nextLine();
			if(line.isEmpty()) {
				break;
			}
			funkcije.add(line);
		}
		scan.close();
		
		for(String niz : konacanSkupUlaznihZnakova) {
			funkcijaAutomata(niz);
		}	
		
	}
	
	private static void funkcijaAutomata(String niz) {
		trenutnaStanja.add(pocetnoStanje);
		
		String[] znakovi=niz.split(",");
		for(int i=0; i<znakovi.length; i++) {
			
			epsilonOkruzenje(); //pronalazi sva epsilon okruzenja trenutnog stanja i dodaje ih u set trenutnih stanja
			
			//ispis rezultata trenutnog stanja i njegovih epsilon okruzenja
			ispis1();

			prijelazUNovoStanje(znakovi[i]); //pronalazi sljedeca stanja od trenutnog i dodaje ih u set sljedecih stanja
		
			osvjeziSetove(); //osvjezava setove za novi prolaz petlje
			
		}
		
		//nakon sto smo prosli sve ulazne znakove, potrebno je jos pronaci epsilon okruzenje zadnjeg seta stanja
		epsilonOkruzenje();
		
		ispis2();

		//isprazniti set prije novog prolaza
		osvjeziSetove();
		
	}
	
	private static void epsilonOkruzenje() {
		boolean biloPromjena=true;
		while(true) {
			if(!biloPromjena) {
				break;
			}
			biloPromjena=false;
			epsilonStanja.removeAll(epsilonStanja);
			for(String stanje : trenutnaStanja) {
				for(String f : funkcije) {
					String[] tmp=f.split(",|->");
					if(stanje.equals(tmp[0]) && konacanSkupStanja.contains(tmp[0])) {
						if(tmp[1].equals("$")) {
							for(int j=2; j<tmp.length; j++) {
								if(!(trenutnaStanja.contains(tmp[j])) && konacanSkupStanja.contains(tmp[j])) {
									epsilonStanja.add(tmp[j]);
									biloPromjena=true;
								}
							}
						}
					}
				}
			}
			trenutnaStanja.addAll(epsilonStanja);
		}
	}
	
	private static void prijelazUNovoStanje(String znak) {
		for(String stanje : trenutnaStanja) {
			for(String f : funkcije) {
				String[] tmp= f.split(",|->");
				if(stanje.equals(tmp[0]) && konacanSkupStanja.contains(tmp[0])) {
					if(tmp[1].equals(znak)) {
						for(int j=2; j<tmp.length; j++) {
							if(konacanSkupStanja.contains(tmp[j])) {
								sljedecaStanja.add(tmp[j]);
							}
						}
					}
				}
			}
		}
		if(sljedecaStanja.isEmpty()) {
			sljedecaStanja.add("#");
		}
	}
	
	private static void ispis1() {
		for(int i=0; i<trenutnaStanja.size()-1; i++) {
			System.out.print(trenutnaStanja.toArray()[i].toString() + ",");
		}
		System.out.print(trenutnaStanja.toArray()[trenutnaStanja.size()-1].toString());
		System.out.print("|");
	}
	
	private static void ispis2() {
		for(int i=0; i<trenutnaStanja.size()-1; i++) {
			System.out.print(trenutnaStanja.toArray()[i].toString() + ",");
		}
		System.out.print(trenutnaStanja.toArray()[trenutnaStanja.size()-1].toString());
		System.out.println();
	}
	
	private static void osvjeziSetove() {
		trenutnaStanja.removeAll(trenutnaStanja);
		trenutnaStanja.addAll(sljedecaStanja);		//pronadjena nova stanja prebaci u set trenutnih stanja
		sljedecaStanja.removeAll(sljedecaStanja);
	}
		
}		

