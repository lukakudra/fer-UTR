import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class SimTS {
	
	public static Set<String> skupStanja=new TreeSet<>();
	public static Set<String> skupUlaznihZnakova=new TreeSet<>();
	public static List<String> skupZnakovaTrake=new ArrayList<>();
	public static String praznaCelija;
	public static List<String> trakaStroja=new ArrayList<>();
	public static Set<String> skupPrihvatljivihStanja=new TreeSet<>();
	public static String pocetnoStanje;
	public static Integer pocetniPolozajGlave;
	public static Map<String, String> funkcije=new HashMap<>();
	
	public static void main(String[] args) {
		
		Scanner scan=new Scanner(System.in);
		
		//1.redak
		String line1=scan.nextLine();
		String[] tmp=line1.split(",");
		for(int i=0; i<tmp.length; i++) {
			skupStanja.add(tmp[i]);
		}
		
		//2.redak
		String line2=scan.nextLine();
		tmp=line2.split(",");
		for(int i=0; i<tmp.length; i++) {
			skupUlaznihZnakova.add(tmp[i]);
		}
		
		//3.redak
		String line3=scan.nextLine();
		tmp=line3.split(",");
		for(int i=0; i<tmp.length; i++) {
			skupZnakovaTrake.add(tmp[i]);
		}
		
		//4.redak
		praznaCelija=scan.nextLine();
		
		//5.redak
		String line5=scan.nextLine();
		tmp=line5.split("(?!^)");
		for(int i=0; i<tmp.length; i++) {
			trakaStroja.add(tmp[i]);
		}
		
		//6.redak
		String line6=scan.nextLine();
		tmp=line6.split(",");
		for(int i=0; i<tmp.length; i++) {
			skupPrihvatljivihStanja.add(tmp[i]);
		}
		
		//7.redak
		pocetnoStanje=scan.nextLine();
		
		//8.redak
		String line8=scan.nextLine();
		pocetniPolozajGlave=Integer.parseInt(line8);
		
		//9.redak i svi ostali
		while(scan.hasNextLine()) {
			String line=scan.nextLine();
			if(line.isEmpty()) {
				break;
			}
			String[] pom=line.split("->");
			funkcije.put(pom[0], pom[1]);
		}
		
		scan.close();
		
		/*
		//TESTIRANJE CITANJA PODATAKA
		System.out.println("SKUP STANJA:");
		for(String s : skupStanja) {
			System.out.println(s);
		}
		
		System.out.println("SKUP ULAZNIH ZNAKOVA:");
		for(String s : skupUlaznihZnakova) {
			System.out.println(s);
		}
		
		System.out.println("SKUP ZNAKOVA TRAKE:");
		for(String s : skupZnakovaTrake) {
			System.out.println(s);
		}
		
		System.out.println("PRAZNA CELIJA:");
		System.out.println(praznaCelija);
		
		System.out.println("TRAKA STROJA:");
		for(String s : trakaStroja) {
			System.out.println(s);
		}
		
		System.out.println("SKUP PRIHVATLJIVIH STANJA:");
		for(String s : skupPrihvatljivihStanja) {
			System.out.println(s);
		}
		
		System.out.println("POCETNO STANJE:");
		System.out.println(pocetnoStanje);
		
		System.out.println("POCETNI POLOZAJ GLAVE:");
		System.out.println(pocetniPolozajGlave);
		
		System.out.println("FUNKCIJE PRIJELAZA:");
		for(Map.Entry<String, String> f : funkcije.entrySet()) {
			System.out.println(f);
		}
		*/
		
		funkcijaAutomata();
	}
	
	
	private static void funkcijaAutomata() {
		String trenutnoStanje=pocetnoStanje;
		int polozajGlave=pocetniPolozajGlave;
		String trenutniZnak="";
		int jeLiPrihvatljiv=0;
		String prijelaz="";
		
		while(true) {
			trenutniZnak=trakaStroja.get(polozajGlave);
			prijelaz=funkcije.get(trenutnoStanje + "," + trenutniZnak);
			if(prijelaz==null) {
				break;
			}
			String[] tmp=prijelaz.split(",");
			trenutnoStanje=tmp[0];
			trakaStroja.set(polozajGlave, tmp[1]);
			if(tmp[2].equals("R")) {
				polozajGlave++;
				if(polozajGlave>69) {
					polozajGlave--;
					break;
				}
			} else if (tmp[2].equals("L")) {
				polozajGlave--;
				if(polozajGlave<0) {
					polozajGlave++;
					break;
				}
			}
				
		}
		
		if(skupPrihvatljivihStanja.contains(trenutnoStanje)) {
			jeLiPrihvatljiv=1;
		} else {
			jeLiPrihvatljiv=0;
		}
		ispis(trenutnoStanje, jeLiPrihvatljiv, polozajGlave);
	}
	
	private static void ispis(String trenutnoStanje, int jeLiPrihvatljiv, int polozajGlave) {
		String zaIspis="";
		for(String s : trakaStroja) {
			zaIspis+=s;
		}
		System.out.println(trenutnoStanje + "|" + polozajGlave + "|" + zaIspis + "|" + jeLiPrihvatljiv);
	}
}
