import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

public class SimPa {
	
	public static List<String> skupUlaznihNizova=new ArrayList<>();
	public static Set<String> skupStanja=new TreeSet<>();
	public static Set<String> skupUlaznihZnakova=new TreeSet<>();
	public static Set<String> skupZnakovaStoga=new TreeSet<>();
	public static Set<String> skupPrihvatljivihStanja=new TreeSet<>();
	public static String pocetnoStanje="";
	public static Character pocetniZnakStoga;
	
	public static String trenutnoStanje="";
	public static Character trenutnoStog;
	public static Stack<Character> stog=new Stack<>();
	
	public static Map<String, String> funkcije = new HashMap<>();
	
	public static Set<String> prethodnaStanja=new TreeSet<>();
	
	public static void main(String[] args) {
		
		Scanner scan=new Scanner(System.in);
		
		
		//1.redak
		String line1=scan.nextLine();
		String[] tmp=line1.split("\\|");
		for(int i=0; i<tmp.length; i++) {
			skupUlaznihNizova.add(tmp[i]);
			
		}
		
		//2.redak
		String line2=scan.nextLine();
		String[] tmp2=line2.split(",");
		for(int i=0; i<tmp2.length; i++) {
			skupStanja.add(tmp2[i]);
		}
		
		//3.redak
		String line3=scan.nextLine();
		String[] tmp3=line3.split(",");
		for(int i=0; i<tmp3.length; i++) {
			skupUlaznihZnakova.add(tmp3[i]);
		}
		
		//4.redak
		String line4=scan.nextLine(); 
		String[] tmp4=line4.split(",");
		for(int i=0; i<tmp4.length; i++) {
			skupZnakovaStoga.add(tmp4[i]);
		}
		
		//5.redak
		String line5=scan.nextLine();
		String[] tmp5=line5.split(",");
		for(int i=0; i<tmp5.length; i++) {
			skupPrihvatljivihStanja.add(tmp5[i]);
		}
		
		//6.redak
		pocetnoStanje=scan.nextLine();
		
		//7.redak
		pocetniZnakStoga=scan.nextLine().toCharArray()[0];
		
		//8.redak i svi ostali
		while(scan.hasNextLine()) {
			String line=scan.nextLine();
			if(line.isEmpty()) {
				break;
			}
			String[] pom=line.split("->");
			funkcije.put(pom[0], pom[1]);
			prethodnaStanja.add(pom[0]);
		}
		
		scan.close();
		
		/*
		//PROVJERA UCITAVANJA
		//1.redak
		System.out.println("skupUlaznihNizova:");
		for(String s : skupUlaznihNizova) {
			System.out.println(s);
		}
		
		//2.redak
		System.out.println("skupStanja:");
		for(String s : skupStanja) {
			System.out.println(s);
		}
		
		//3.redak
		System.out.println("skupUlaznihZnakova:");
		for(String s : skupUlaznihZnakova) {
			System.out.println(s);
		}
		
		//4.redak
		System.out.println("skupZnakovaStoga:");
		for(String s : skupZnakovaStoga) {
			System.out.println(s);
		}
		
		//5.redak
		System.out.println("skupPrihvatljivihStanja:");
		for(String s : skupPrihvatljivihStanja) {
			System.out.println(s);
		}
		
		//6.redak
		System.out.println("pocetnoStanje:");
		System.out.println(pocetnoStanje);
		
		//7.redak
		System.out.println("pocetniZnakStoga:");
		System.out.println(pocetniZnakStoga);
		
		//8.redak
		System.out.println("PRIJELAZI:");
		for(Map.Entry<String, String> m : funkcije.entrySet()) {
			System.out.println(m);
		}
		*/
		
		for(String niz : skupUlaznihNizova) {
			funkcijaAutomata(niz);
		}
	}
	
	private static void funkcijaAutomata(String niz) {
		trenutnoStanje=pocetnoStanje;
		trenutnoStog=pocetniZnakStoga;
		System.out.print(trenutnoStanje + "#" + trenutnoStog);
		System.out.print("|");
		stog.add(pocetniZnakStoga);
		
		String[] znakovi=niz.split(",");
		int brojac=0;
		while(brojac<znakovi.length) {
			
			if(!(prethodnaStanja.contains(trenutnoStanje + "," + znakovi[brojac] + "," + trenutnoStog) || prethodnaStanja.contains(trenutnoStanje + "," + "$" + "," + trenutnoStog))) {
				trenutnoStanje="fail";
				System.out.print("fail|");
				break;
			} else if(prethodnaStanja.contains(trenutnoStanje + "," + znakovi[brojac] + "," + trenutnoStog)) {
				prijelaz(trenutnoStanje + "," + znakovi[brojac] + "," + trenutnoStog);
				brojac++;
			} else if(prethodnaStanja.contains(trenutnoStanje + "," + "$" + "," + trenutnoStog)) {
				prijelaz(trenutnoStanje + "," + "$" + "," + trenutnoStog);
			}
			
		}
		
		epsilonPrijelaz();
		provjeraPrihvatljivosti();
		stog.clear();

	}
	
	private static void prijelaz(String niz) {
		String novoStanje=funkcije.get(niz);
		String[] tmp=novoStanje.split(",");
		trenutnoStanje = tmp[0];
		promjenaStoga(new StringBuffer(tmp[1]).reverse().toString());
		trenutnoStog = stog.peek();
		ispis();
	}

	private static void epsilonPrijelaz() {
		boolean biloPromjena = true;
		
		if(skupPrihvatljivihStanja.contains(trenutnoStanje)) {
			biloPromjena=false;
		}
		
		while(true) {
			if(!biloPromjena) {
				break;
			}
			biloPromjena=false;
			if (prethodnaStanja.contains(trenutnoStanje + "," + "$" + "," + trenutnoStog)) {
				String novoStanje = funkcije.get(trenutnoStanje + "," + "$" + "," + trenutnoStog);
				String[] tmp=novoStanje.split(",");
				trenutnoStanje = tmp[0];
				promjenaStoga(new StringBuffer(tmp[1]).reverse().toString());
				trenutnoStog = stog.peek();
				ispis();
				biloPromjena = true;
			}
			if(skupPrihvatljivihStanja.contains(trenutnoStanje)) {
				biloPromjena=false;
			}
		}

	}

	private static void sadrzajNaStogu() {
		if (stog.isEmpty()) {
			System.out.print("$");
		} else { 
			for(int i=stog.size()-1; i>=0; i--) {
				System.out.print(stog.get(i));
				
			}
		}
	}

	private static void promjenaStoga(String niz) {
		
		if (!stog.isEmpty()) {
			stog.pop();
		}
		
		char[] znakovi=niz.toCharArray();
		for(int i=0; i<znakovi.length; i++) {
			stog.push(znakovi[i]);
		}
		
		if(niz.equals("$")){
			stog.pop();
		}
		if(stog.isEmpty()){
			stog.push('$');
		}
	}
	
	private static void provjeraPrihvatljivosti() {
		if(skupPrihvatljivihStanja.contains(trenutnoStanje)) {
			System.out.println("1");
		} else {
			System.out.println("0");
		}
	}
	
	private static void ispis() {
		System.out.print(trenutnoStanje);
		System.out.print("#");
		sadrzajNaStogu();
		System.out.print("|");
		
	}
}
