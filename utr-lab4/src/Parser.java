import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Parser {
	
	public static List<Character> ulazniZnakovi=new ArrayList<>();
	public static Character ulaz;
	public static int i;
	
	public static void main(String[] args) {
		
		Scanner scan=new Scanner(System.in);
		String line=scan.nextLine();
		ulazniZnakovi=line.chars().mapToObj(e->(char)e).collect(Collectors.toList());
		ulazniZnakovi.add('\n');
		scan.close();
		
		//TEST ISPISA LISTE ULAZNIH ZNAKOVA
		/*
		System.out.println("ULAZNI ZNAKOVI:");
		for(Character c : ulazniZnakovi) {
			System.out.print(c);
		}
		*/
		
		gramatika();
	}
	
	//moraju biti 4 potprograma jer imam 4 nezavrsna znaka gramatike
	
	//ovo je glavni program
	private static void gramatika() {
		i=0;
		S();
		ulaz=ulazniZnakovi.get(i);
		if(ulaz!='\n') {
			System.out.printf("\nNE\n");
		} else {
			System.out.printf("\nDA\n");
		}
		
	}
	
	//prvi potprogram
	private static void S() {
		System.out.print("S");
		
		if(i>=ulazniZnakovi.size()) {
			System.out.printf("\nNE\n");
			System.exit(0);
		}
		ulaz=ulazniZnakovi.get(i);
		i++;
		
		if(ulaz=='a') {
			A();
			B();
		} else if(ulaz=='b') {
			B();
			A();
		} else {
			System.out.printf("\nNE\n");
			System.exit(0);
		}
	}
	
	//drugi potprogram
	private static void A() {
		System.out.print("A");
		if(i>=ulazniZnakovi.size()) {
			System.out.printf("\nNE\n");
			System.exit(0);
		}
		ulaz=ulazniZnakovi.get(i);
		i++;
		
		if(ulaz=='b') {
			C();
		} else if(ulaz=='a') {
			return;
		} else {
			System.out.printf("\nNE\n");
			System.exit(0);
		}
		
	}
	
	//treci potprogram
	private static void B() {
		System.out.print("B");
		if(i>=ulazniZnakovi.size()) {
			System.out.printf("\nNE\n");
			System.exit(0);
		}
		
		ulaz=ulazniZnakovi.get(i);
		
		switch(ulaz) {
		case 'c':
			ulaz=ulazniZnakovi.get(i);
			i++;
			if(ulaz!='c') {
				System.out.printf("\nNE\n");
				System.exit(0);
			}
			ulaz=ulazniZnakovi.get(i);
			i++;
			if(ulaz!='c') {
				System.out.printf("\nNE\n");
				System.exit(0);
			}
			
			S();
			
			ulaz=ulazniZnakovi.get(i);
			i++;
			if(ulaz!='b') {
				System.out.printf("\nNE\n");
				System.exit(0);
			}
			
			ulaz=ulazniZnakovi.get(i);
			i++;
			if(ulaz!='c') {
				System.out.printf("\nNE\n");
				System.exit(0);
			}
			break;
		default:
			
			break;
		}
	}
	
	//cetvrti potprogram 
	private static void C() {
		System.out.print("C");
		A();
		A();
	}
}
