import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class MinDka {
	
	public static Scanner scan=new Scanner(System.in);
	
	public static Set<String> skupSvihStanja=new TreeSet<>();
	public static Set<String> skupSimbolaAbecede=new TreeSet<>();
	public static Set<String> skupPrihvatljivihStanja=new TreeSet<>();
	public static String pocetnoStanje;
	public static List<String> funkcijaPrijelaza=new ArrayList<>();
	
	public static String[] svaStanja=null;
	public static String[] abeceda=null;
	public static String[] prihvatljivaStanja=null;
	
	public static Set<String> dohvatljivaStanja=new TreeSet<>();
	public static List<String> noviPrijelazi=new ArrayList<>();
	
	public static Set<String> neoznaceni=new TreeSet<>();
	public static Set<String> oznaceni=new TreeSet<>();
	
	public static List<String> konacniPrijelazi=new ArrayList<>();
	public static Set<String> novaPrihvatljivaStanja=new TreeSet<>();
	
	public static void main(String[] args) {
		
		String line1=scan.nextLine();
		svaStanja=line1.split(",");
		for(int i=0; i<svaStanja.length; i++) {
			skupSvihStanja.add(svaStanja[i]);
		}
		String line2=scan.nextLine();
		abeceda=line2.split(",");
		for(int i=0; i<abeceda.length; i++) {
			skupSimbolaAbecede.add(abeceda[i]);
		}
		String line3=scan.nextLine();
		prihvatljivaStanja=line3.split(",");
		for(int i=0; i<prihvatljivaStanja.length; i++) {
			skupPrihvatljivihStanja.add(prihvatljivaStanja[i]);
		}
		pocetnoStanje=scan.nextLine();
		while(scan.hasNextLine()) {
			String line=scan.nextLine();
			if(line.isEmpty()) {
				break;
			}
			funkcijaPrijelaza.add(line);
		}
		
		scan.close();
		
		/*ova metoda nam daje 2 nova seta: 1) dohvatljivaStanja koja sadrze samo dohvatljiva stanja
		 * 								   2) noviPrijelazi koji sadrze samo prijelaze s dohvatljivim stanjima
		 */
		rijesiSeNedohvatljivih();
		
		/*ova metoda popunjava tablicu na nacin da oznacena stanja stavlja u set oznaceni, a neoznacena stanja u set neoznaceni*/
		rijesiSeIstovjetnih();
		
		/*ova metoda daje konacni izlaz*/
		ispisDKA();
	}
	
	private static void rijesiSeNedohvatljivih() {
		dohvatljivaStanja.add(pocetnoStanje);
		List<String> lista=new ArrayList<>(dohvatljivaStanja);
		for(int i=0; i<lista.size(); i++) {
			for(int j=0; j<funkcijaPrijelaza.size(); j++) {
				String[] tmp=funkcijaPrijelaza.get(j).split(",|->");
				for(String slovo : skupSimbolaAbecede) {
					if(tmp[0].equals(lista.get(i)) && tmp[1].equals(slovo)) {
						if(!lista.contains(tmp[2])) {
							lista.add(tmp[2]);
						}
					}
				}
				
			}
		}
		dohvatljivaStanja=new TreeSet<>(lista);
		
		
		for(String f : funkcijaPrijelaza) {
			String tmp[]=f.split(",|->");
			if(dohvatljivaStanja.contains(tmp[0])) {
				noviPrijelazi.add(f);
			}
		}
	}
	
	
	private static void rijesiSeIstovjetnih() {
		
		List<String> lista = new ArrayList<>(dohvatljivaStanja);
		
		//prolazimo sve kombinacije dohvatljivih stanja te popunjavamo setove oznaceni i neoznaceni 
		for(int i=0; i<lista.size()-1; i++) {
			for(int j=i+1; j<lista.size(); j++) {
				//ako je kombinacija stanja (i,j) istog stupnja prihvatljivosti, dodajemo ju u set neoznacenih
				if(!((skupPrihvatljivihStanja.contains(lista.toArray()[i].toString()) && !skupPrihvatljivihStanja.contains(lista.toArray()[j].toString())) || (!skupPrihvatljivihStanja.contains(lista.toArray()[i].toString()) && skupPrihvatljivihStanja.contains(lista.toArray()[j].toString())))) {
					String par = lista.toArray()[i].toString().concat("->").concat(lista.toArray()[j].toString());
					neoznaceni.add(par);
				}
				//ako je kombinacija stanja (i,j) razlicitog stupnja prihvatljivosti, dodajemo ju u set oznacenih
				if((skupPrihvatljivihStanja.contains(lista.toArray()[i].toString()) && !skupPrihvatljivihStanja.contains(lista.toArray()[j].toString())) || (!skupPrihvatljivihStanja.contains(lista.toArray()[i].toString()) && skupPrihvatljivihStanja.contains(lista.toArray()[j].toString()))) {
					String par=lista.toArray()[i].toString().concat("->").concat(lista.toArray()[j].toString());
					oznaceni.add(par);
				}
						
			}
		}
		
		//moramo provjeriti za sve ulazne znakove abecede gdje prelaze stanja (i,j)
		for(int i=0; i<noviPrijelazi.size()-1; i=i+skupSimbolaAbecede.size()) {
			for(int j=i+skupSimbolaAbecede.size(); j<noviPrijelazi.size(); j=j+skupSimbolaAbecede.size()) {
				String prijelaz_I = noviPrijelazi.get(i);
				String prijelaz_J=noviPrijelazi.get(j);
				String[] tmp_I=prijelaz_I.split(",|->");
				String[] tmp_J=prijelaz_J.split(",|->");
				//ako oba stanja (i,j) prelaze u dohvatljiva stanja, oznacavamo ih kao prvi par
				if(dohvatljivaStanja.contains(tmp_I[0]) && dohvatljivaStanja.contains(tmp_J[0])) {
					String prviPar=tmp_I[0].concat("->").concat(tmp_J[0]);
					String drugiPar="";
					//za svako slovo abecede odredi prijelaz za stanje (i,j) te ga poredaj uzlazno po abecedi i obiljezi kao drugi par
					for(int x=0; x<skupSimbolaAbecede.size(); x++) {
						String prijelaz1=noviPrijelazi.get(i+x);
						String prijelaz2=noviPrijelazi.get(j+x);
						String[] tmp1=prijelaz1.split(",|->");
						String[] tmp2=prijelaz2.split(",|->");
						drugiPar=poredajAbecedno(tmp1[2], tmp2[2]);
						
						//ako je drugi par presao u razlicita stanja (i,j) te ako je prvi par bio neoznacen, a ovaj drugi par je oznacen, oznaci i prvi par
						if(!(tmp1[2].compareTo(tmp2[2]) == 0)) {
							if(!(oznaceni.contains(prviPar)) && oznaceni.contains(drugiPar)) {
								oznaceni.add(prviPar);
								neoznaceni.remove(prviPar);
							}
						}
					}
				}
			}
		}
			
	}
	
	private static String poredajAbecedno (String niz1, String niz2) {
		int i;
		String povratniNiz;
		i=niz1.compareTo(niz2);
		if(i<0) {
			povratniNiz=niz1.concat("->").concat(niz2);
		} else {
			povratniNiz=niz2.concat("->").concat(niz1);
		}
		return povratniNiz;
	}
	
	private static void ispisDKA() {
		
		//najprije treba u dohvatljivim stanjima izbaciti istovjetna stanja, tj. ostaviti samo jedno, ono koje je abecedno manje
		//zatim treba iz skupa prihvatljivih stanja izbaciti sva istovjetna stanja
		//zatim ako je pocetno stanje istovjetno s nekim od neoznacenih, postaviti da je prihvatljivo stanje abecedno manje
		for(String par : neoznaceni) {
			String[] tmp=par.split("->");
			dohvatljivaStanja.remove(tmp[1]);
			if(skupPrihvatljivihStanja.contains(tmp[1])) {
				skupPrihvatljivihStanja.remove(tmp[1]);
			}
			if(pocetnoStanje.equals(tmp[1])) {
				pocetnoStanje=tmp[0];
			}
		}
		
		for(String stanje : skupPrihvatljivihStanja) {
			if(dohvatljivaStanja.contains(stanje)) {
				novaPrihvatljivaStanja.add(stanje);
			}
		}
		
		//iz skupa konacnih prijelaza izbaciti sva istovjetna stanja
		for(String f : noviPrijelazi) {
			String[] tmp=f.split(",|->");
			if(dohvatljivaStanja.contains(tmp[0])) {
				konacniPrijelazi.add(f);
			}
		}

		for(String s : neoznaceni) {
			for(int i=0; i<konacniPrijelazi.size(); i++) {
				String[] tmp1=konacniPrijelazi.get(i).split(",|->");
				String[] tmp2=s.split("->");
				if(tmp1[2].equals(tmp2[1])) {
					String n=konacniPrijelazi.get(i).replace(tmp1[2], tmp2[0]);
					konacniPrijelazi.set(i, n);
				}
			}
		}
		
		//ISPIS CIJELOG AUTOMATA ZA SPRUT
		
		//ispis dohvatljivih stanja --- 1.redak
		for(int i=0; i<dohvatljivaStanja.size()-1; i++) {
			System.out.print(dohvatljivaStanja.toArray()[i].toString() + ",");
		}
		System.out.print(dohvatljivaStanja.toArray()[dohvatljivaStanja.size()-1].toString());
		System.out.println();
		
		//ispis abecede --- 2.redak
		for(int i=0; i<skupSimbolaAbecede.size()-1; i++) {
			System.out.print(skupSimbolaAbecede.toArray()[i].toString() + ",");
		}
		System.out.print(skupSimbolaAbecede.toArray()[skupSimbolaAbecede.size()-1].toString());
		System.out.println();
		
		//ispis prihvatljivih stanja --- 3.redak
		if(novaPrihvatljivaStanja.size()==0) {
			System.out.println();
		} else {
			for(int i=0; i<novaPrihvatljivaStanja.size()-1; i++) {
				System.out.print(novaPrihvatljivaStanja.toArray()[i].toString() + ",");
			}
			System.out.print(novaPrihvatljivaStanja.toArray()[novaPrihvatljivaStanja.size()-1].toString());
			System.out.println();
		}
		
		//ispis pocetnog stanja --- 4.redak
		System.out.println(pocetnoStanje);
		
		//ispis konacnih prijelaza --- 5.redak pa nadalje
		for(String s : konacniPrijelazi) {
			System.out.println(s);
		}
	}
}
