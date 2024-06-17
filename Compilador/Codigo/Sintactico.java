package compilador;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Sintactico {
	private String[] noTerminales = { "PZ", "P", "E", "EZ", "R", "RZ", "U", "UZ", 
			"B", "BZ", "C", "CZ", "S", "SZ", "L", 
			"Q", "X", "D", "T", "DZ", "F", "H", "G",
			"K", "I","Z", "W", "Y", "M" };

	private String[] terminales =  { "$", "&&", ">", "-", "--",
			"id", "(", ")", "ent", "cad", "=", ";",
			",", "put", "get", "return", "if",
			"let", "int", "boolean", "string", "for",
			"{", "}", "function", "void"};
	private String[][] tabla;
	private Stack<Tuple<String, String>> pilaSintactica; 
	private Stack<Tuple<String, String>> pilaSemantica;
	private GestorErrores gestorErrores; 
	private int numeroLinea;
	private boolean comprueba = false;
	private File sintactico;
	private Salida escritura;
	private Tuple<String,String> tupla;
	private TablaSimbolos tablaSimbolos;
	private List<String> terminal = new LinkedList<String>();
	private boolean zona_decl = false;
	private boolean retorno = false;
	private int numParam = 0;
	private String tipoParam = "";
	private String tipoReturn = "";
	private int numParamCreacion = 0;
	private String tipoParamCreacion = "";
	private boolean tieneRetorno = false;
	private int todosPop = 0;

	public Sintactico() throws IOException {
		inicializarTabla();
		inicializarPila();
		sintactico = new File("C:\\Users\\lucia\\OneDrive\\Escritorio\\UPM\\3º\\2º Cuatri\\PDL\\parse.txt");
		escritura = new Salida(sintactico);
		escritura.escribir(sintactico, "Des ");
	}

	public void crearTSintantico(TablaSimbolos TS) {
		tablaSimbolos = TS;
		tablaSimbolos.crearTS();
	}

	public int buscarNoTerminal(String noTerminal) {
		int i;
		boolean comprobar = false;
		int posicion = -1;

		for (i = 0; !comprobar && i < noTerminales.length; i++) {
			if (noTerminal.equals(noTerminales[i])) {
				posicion = i;
				comprobar = true;
			}
		}
		return posicion;
	}

	public int buscarTerminal(String terminal) {
		int i;
		boolean comprobar = false;
		int posicion = -1;

		for (i = 0; !comprobar && i < terminales.length; i++) {
			if (terminal.equals(terminales[i])) {
				posicion = i;
				comprobar = true;
			}
		}
		return posicion;
	}

	public String[][] inicializarTabla() {
		tabla = new String[noTerminales.length][terminales.length];

		tabla[buscarNoTerminal("PZ")][buscarTerminal("$")] = "0 PZ ~ 0.1 P 0.2 0.3";
		tabla[buscarNoTerminal("PZ")][buscarTerminal("id")] = "0 PZ ~ 0.1 P 0.2 0.3"; //
		tabla[buscarNoTerminal("PZ")][buscarTerminal("put")] = "0 PZ ~ 0.1 P 0.2 0.3"; //
		tabla[buscarNoTerminal("PZ")][buscarTerminal("get")] = "0 PZ ~ 0.1 P 0.2 0.3"; //
		tabla[buscarNoTerminal("PZ")][buscarTerminal("return")] = "0 PZ ~ 0.1 P 0.2 0.3"; //
		tabla[buscarNoTerminal("PZ")][buscarTerminal("if")] = "0 PZ ~ 0.1 P 0.2 0.3"; //
		tabla[buscarNoTerminal("PZ")][buscarTerminal("let")] = "0 PZ ~ 0.1 P 0.2 0.3"; //
		tabla[buscarNoTerminal("PZ")][buscarTerminal("for")] = "0 PZ ~ 0.1 P 0.2 0.3"; //
		tabla[buscarNoTerminal("PZ")][buscarTerminal("function")] = "0 PZ ~ 0.1 P 0.2 0.3"; //
		tabla[buscarNoTerminal("PZ")][buscarTerminal("--")] = "0 PZ ~ 0.1 P 0.2 0.3"; //
		//Fila  ()
		tabla[buscarNoTerminal("P")][buscarTerminal("$")] = "3 P ~ +"; //
		tabla[buscarNoTerminal("P")][buscarTerminal("id")] = "1 P ~ D 1.1 P 1.2";////
		tabla[buscarNoTerminal("P")][buscarTerminal("--")] = "1 P ~ D 1.1 P 1.2";////
		tabla[buscarNoTerminal("P")][buscarTerminal("put")] = "1 P ~ D 1.1 P 1.2";//
		tabla[buscarNoTerminal("P")][buscarTerminal("get")] = "1 P ~ D 1.1 P 1.2";//
		tabla[buscarNoTerminal("P")][buscarTerminal("return")] = "1 P ~ D 1.1 P 1.2";//
		tabla[buscarNoTerminal("P")][buscarTerminal("if")] = "1 P ~ D 1.1 P 1.2";//
		tabla[buscarNoTerminal("P")][buscarTerminal("let")] = "1 P ~ D 1.1 P 1.2";//
		tabla[buscarNoTerminal("P")][buscarTerminal("for")] = "1 P ~ D 1.1 P 1.2";//
		tabla[buscarNoTerminal("P")][buscarTerminal("function")] = "2 P ~ F P 2.1";//
		//Fila  (5)
		tabla[buscarNoTerminal("E")][buscarTerminal("id")] = "4 E ~ R 4.1 EZ 4.2 4.3"; //
		tabla[buscarNoTerminal("E")][buscarTerminal("(")] = "4 E ~ R 4.1 EZ 4.2 4.3";//
		tabla[buscarNoTerminal("E")][buscarTerminal("ent")] = "4 E ~ R 4.1 EZ 4.2 4.3";//
		tabla[buscarNoTerminal("E")][buscarTerminal("cad")] = "4 E ~ R 4.1 EZ 4.2 4.3";//
		//Fila  ()
		tabla[buscarNoTerminal("EZ")][buscarTerminal("&&")] = "5 EZ ~ && R EZ 5.1 5.2";//
		tabla[buscarNoTerminal("EZ")][buscarTerminal(")")] = "6 EZ ~ +";//
		tabla[buscarNoTerminal("EZ")][buscarTerminal(";")] = "6 EZ ~ +";//
		tabla[buscarNoTerminal("EZ")][buscarTerminal(",")] = "6 EZ ~ +";//
		//Fila  (5)
		tabla[buscarNoTerminal("R")][buscarTerminal("id")] = "7 R ~ U 7.1 RZ 7.2 7.3";//
		tabla[buscarNoTerminal("R")][buscarTerminal("(")] = "7 R ~ U 7.1 RZ 7.2 7.3";//
		tabla[buscarNoTerminal("R")][buscarTerminal("ent")] = "7 R ~ U 7.1 RZ 7.2 7.3";//
		tabla[buscarNoTerminal("R")][buscarTerminal("cad")] = "7 R ~ U 7.1 RZ 7.2 7.3";//
		//Fila 5 (5)
		tabla[buscarNoTerminal("RZ")][buscarTerminal("&&")] = "9 RZ ~ + 9.1";//
		tabla[buscarNoTerminal("RZ")][buscarTerminal(">")] = "8 RZ ~ > U RZ 8.1 8.2";//
		tabla[buscarNoTerminal("RZ")][buscarTerminal(")")] = "9 RZ ~ + 9.1";//
		tabla[buscarNoTerminal("RZ")][buscarTerminal(";")] = "9 RZ ~ + 9.1";//
		tabla[buscarNoTerminal("RZ")][buscarTerminal(",")] = "9 RZ ~ + 9.1";//
		//Fila 6 (5)
		tabla[buscarNoTerminal("U")][buscarTerminal("id")] = "10 U ~ C 10.1 UZ 10.2 10.3";//
		tabla[buscarNoTerminal("U")][buscarTerminal("(")] = "10 U ~ C 10.1 UZ 10.2 10.3";//
		tabla[buscarNoTerminal("U")][buscarTerminal("ent")] = "10 U ~ C 10.1 UZ 10.2 10.3";//
		tabla[buscarNoTerminal("U")][buscarTerminal("cad")] = "10 U ~ C 10.1 UZ 10.2 10.3";//
		//Fila 7 (6)
		tabla[buscarNoTerminal("UZ")][buscarTerminal("&&")] = "12 UZ ~ + 12.1";//
		tabla[buscarNoTerminal("UZ")][buscarTerminal(">")] = "12 UZ ~ + 12.1";//
		tabla[buscarNoTerminal("UZ")][buscarTerminal("-")] = "11 UZ ~ - C UZ 11.1 11.2";//
		tabla[buscarNoTerminal("UZ")][buscarTerminal(")")] = "12 UZ ~ + 12.1";//
		tabla[buscarNoTerminal("UZ")][buscarTerminal(";")] = "12 UZ ~ + 12.1";//
		tabla[buscarNoTerminal("UZ")][buscarTerminal(",")] = "12 UZ ~ + 12.1";//

		//Fila  ()
		tabla[buscarNoTerminal("C")][buscarTerminal("id")] = "13 C ~ id CZ 13.1 13.2"; //
		tabla[buscarNoTerminal("C")][buscarTerminal("(")] = "14 C ~ ( E ) 14.1 14.2";//
		tabla[buscarNoTerminal("C")][buscarTerminal("ent")] = "15 C ~ ent 15.1 15.2";//
		tabla[buscarNoTerminal("C")][buscarTerminal("cad")] = "16 C ~ cad 16.1 16.2";//

		//Fila 5 (0)
		tabla[buscarNoTerminal("CZ")][buscarTerminal("&&")] = "19 CZ ~ + 19.1";//
		tabla[buscarNoTerminal("CZ")][buscarTerminal(">")] = "19 CZ ~ + 19.1";//
		tabla[buscarNoTerminal("CZ")][buscarTerminal("-")] = "19 CZ ~ + 19.1";//
		tabla[buscarNoTerminal("CZ")][buscarTerminal(")")] = "19 CZ ~ + 19.1";//
		tabla[buscarNoTerminal("CZ")][buscarTerminal(";")] = "19 CZ ~ + 19.1";//
		tabla[buscarNoTerminal("CZ")][buscarTerminal(",")] = "19 CZ ~ + 19.1";//
		tabla[buscarNoTerminal("CZ")][buscarTerminal("(")] = "18 CZ ~ ( L ) 18.1 18.2";//
		tabla[buscarNoTerminal("CZ")][buscarTerminal("=")] = "17 CZ ~ = ent 17.1 17.2";//
		//Fila 6 ()
		tabla[buscarNoTerminal("S")][buscarTerminal("id")] = "20 S ~ id SZ 20.1 20.2";//
		tabla[buscarNoTerminal("S")][buscarTerminal("put")] = "29 S ~ put E ; 29.1 29.2";//
		tabla[buscarNoTerminal("S")][buscarTerminal("get")] = "30 S ~ get id ; 30.1 30.2";//
		tabla[buscarNoTerminal("S")][buscarTerminal("return")] = "31 S ~ return X ; 31.1 31.2";//
		tabla[buscarNoTerminal("S")][buscarTerminal("--")] = "21 S ~ Y 21.1 21.2";
		//Fila 7 ()
		tabla[buscarNoTerminal("SZ")][buscarTerminal("(")] = "23 SZ ~ ( L ) ; 23.1 23.2";//
		tabla[buscarNoTerminal("SZ")][buscarTerminal("=")] = "22 SZ ~ = E ; 22.1 22.2";//
		tabla[buscarNoTerminal("SZ")][buscarTerminal(";")] = "24 SZ ~ ; 24.1";//
		//Fila 8 (6)
		tabla[buscarNoTerminal("L")][buscarTerminal("id")] = "25 L ~ E Q 25.1 25.2"; //
		tabla[buscarNoTerminal("L")][buscarTerminal("(")] = "25 L ~ E Q 25.1 25.2";//
		tabla[buscarNoTerminal("L")][buscarTerminal(")")] = "26 L ~ + 26.1";//
		tabla[buscarNoTerminal("L")][buscarTerminal("ent")] = "25 L ~ E Q 25.1 25.2";//
		tabla[buscarNoTerminal("L")][buscarTerminal("cad")] = "25 L ~ E Q 25.1 25.2";//
		//Fila 9 ()
		tabla[buscarNoTerminal("Q")][buscarTerminal(",")] = "27 Q ~ , E Q 27.1 27.2";//
		tabla[buscarNoTerminal("Q")][buscarTerminal(")")] = "28 Q ~ + 28.1";//
		//Fila 0 (6)
		tabla[buscarNoTerminal("X")][buscarTerminal("id")] = "32 X ~ E 32.1 32.2";//
		tabla[buscarNoTerminal("X")][buscarTerminal("(")] = "32 X ~ E 32.1 32.2";//
		tabla[buscarNoTerminal("X")][buscarTerminal(";")] = "33 X ~ + 33.1";//
		tabla[buscarNoTerminal("X")][buscarTerminal("ent")] = "32 X ~ E 32.1 32.2";//
		tabla[buscarNoTerminal("X")][buscarTerminal("cad")] = "32 X ~ E 32.1 32.2";//
		//Fila  ()
		tabla[buscarNoTerminal("D")][buscarTerminal("id")] = "35 D ~ S 35.1 35.2";//
		tabla[buscarNoTerminal("D")][buscarTerminal("put")] = "35 D ~ S 35.1 35.2";//
		tabla[buscarNoTerminal("D")][buscarTerminal("get")] = "35 D ~ S 35.1 35.2";//
		tabla[buscarNoTerminal("D")][buscarTerminal("return")] = "35 D ~ S 35.1 35.2";//
		tabla[buscarNoTerminal("D")][buscarTerminal("--")] = "35 D ~ S 35.1 35.2";
		tabla[buscarNoTerminal("D")][buscarTerminal("if")] = "34 D ~ if ( E ) S 34.1 34.2";//
		tabla[buscarNoTerminal("D")][buscarTerminal("let")] = "36 D ~ let id T ; 36.1 36.2";//
		tabla[buscarNoTerminal("D")][buscarTerminal("for")] = "44 D ~ for ( Z ; E ; W ) { I } 44.1 44.2";//
		//Fila  ()
		tabla[buscarNoTerminal("T")][buscarTerminal("int")] = "37 T ~ int 37.1 37.2";//
		tabla[buscarNoTerminal("T")][buscarTerminal("boolean")] = "38 T ~ boolean 38.1 38.2";//
		tabla[buscarNoTerminal("T")][buscarTerminal("string")] = "39 T ~ string 39.1 39.2";//
		//Fila
		tabla[buscarNoTerminal("M")][buscarTerminal("int")] = "40 M ~ int 40.1 40.2";//
		tabla[buscarNoTerminal("M")][buscarTerminal("boolean")] = "41 M ~ boolean 41.1 41.2";//
		tabla[buscarNoTerminal("M")][buscarTerminal("string")] = "42 M ~ string 42.1 42.2";//
		tabla[buscarNoTerminal("M")][buscarTerminal("void")] = "43 M ~ void 43.1 43.2";//
		//Fila
		tabla[buscarNoTerminal("F")][buscarTerminal("function")] = "45 F ~ function id H 45.1 ( G ) 45.2 { I } 45.3 45.4";//
		//Fila 5 ()
		tabla[buscarNoTerminal("H")][buscarTerminal("int")] = "46 H ~ M 46.1 46.2"; //
		tabla[buscarNoTerminal("H")][buscarTerminal("boolean")] = "46 H ~ M 46.1 46.2";//
		tabla[buscarNoTerminal("H")][buscarTerminal("string")] = "46 H ~ M 46.1 46.2";//
		tabla[buscarNoTerminal("H")][buscarTerminal("void")] = "46 H ~ M 46.1 46.2";//
		//Fila 6 ()
		tabla[buscarNoTerminal("G")][buscarTerminal(")")] = "49 G ~ + 49.1";//
		tabla[buscarNoTerminal("G")][buscarTerminal("int")] = "48 G ~ T id K 48.1 48.2";//
		tabla[buscarNoTerminal("G")][buscarTerminal("boolean")] = "48 G ~ T id K 48.1 48.2";//
		tabla[buscarNoTerminal("G")][buscarTerminal("string")] = "48 G ~ T id K 48.1 48.2";//
		tabla[buscarNoTerminal("G")][buscarTerminal("void")] = "47 G ~ void 47.1 47.2";//
		//Fila 7 ()
		tabla[buscarNoTerminal("K")][buscarTerminal(")")] = "51 K ~ + 51.1";//
		tabla[buscarNoTerminal("K")][buscarTerminal(",")] = "50 K ~ , T id K 50.1 50.2"; //
		//Fila 8 ()
		tabla[buscarNoTerminal("I")][buscarTerminal("id")] = "52 I ~ D I 52.1 52.2"; //
		tabla[buscarNoTerminal("I")][buscarTerminal("put")] = "52 I ~ D I 52.1 52.2";//
		tabla[buscarNoTerminal("I")][buscarTerminal("get")] = "52 I ~ D I 52.1 52.2";//
		tabla[buscarNoTerminal("I")][buscarTerminal("return")] = "52 I ~ D I 52.1 52.2";//
		tabla[buscarNoTerminal("I")][buscarTerminal("if")] = "52 I ~ D I 52.1 52.2";//
		tabla[buscarNoTerminal("I")][buscarTerminal("let")] = "52 I ~ D I 52.1 52.2";//
		tabla[buscarNoTerminal("I")][buscarTerminal("for")] = "52 I ~ D I 52.1 52.2";//
		tabla[buscarNoTerminal("I")][buscarTerminal("--")] = "52 I ~ D I 52.1 52.2";
		tabla[buscarNoTerminal("I")][buscarTerminal("}")] = "53 I ~ + 53.1";//

		tabla[buscarNoTerminal("Z")][buscarTerminal("id")] = "54 Z ~ id = E 54.1 54.2";
		tabla[buscarNoTerminal("Z")][buscarTerminal(";")] = "55 Z ~ + 55.1";

		tabla[buscarNoTerminal("W")][buscarTerminal(")")] = "58 W ~ +";
		tabla[buscarNoTerminal("W")][buscarTerminal("--")] = "56 W ~ -- id 56.1 56.2";
		tabla[buscarNoTerminal("W")][buscarTerminal("id")] = "57 W ~ id = E 57.1 57.2";

		tabla[buscarNoTerminal("Y")][buscarTerminal("--")] = "59 Y ~ -- id 59.1 59.2";

		return tabla;
	}

	private void inicializarPila() {
		pilaSintactica = new Stack<Tuple<String, String>>();
		pilaSemantica = new Stack<Tuple<String, String>>();
		pilaSintactica.push(new Tuple<String, String>("$", "-"));
		pilaSintactica.push(new Tuple<String, String>("PZ", "-"));
	}

	public void parse(String regla) {
		int posicion = regla.indexOf(" ");
		String cadena = "";

		if (posicion!=-1) {
			cadena = regla.substring(0, posicion);
			escritura.escribir(sintactico, (cadena + " "));
		}
	}

	public void consecuente(String regla, String identificador) {
		int posicion = regla.indexOf('~');
		String cadena = "";

		if (posicion!=-1) {
			cadena = regla.substring(posicion+2);
		}
		meterNoTerminales(cadena, identificador);
	}

	public void analizador(Tuple<String, String> tupla) {
		Tuple<String, String> noTerminal = new Tuple<String, String>("", "");
		String regla = "";
		String correcto = "";

		//System.out.println("hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh\n" + pilaSintactica.toString());
		if (tupla.getX().equals("id")) {
		}
		while ((buscarNoTerminal(pilaSintactica.peek().getX())!=-1 || Character.isDigit(pilaSintactica.peek().getX().charAt(0))) && !comprueba) {
			System.out.println("1 En la cima de la pila sintactica hay " + pilaSintactica.peek());
			if (Character.isDigit(pilaSintactica.peek().getX().charAt(0))) {
				semantico(pilaSintactica.pop().getX());
			} 
			else {
				noTerminal = pilaSintactica.pop();
				System.out.println("2 "+ noTerminal + " se ha sacado de la sintactica");
				pilaSemantica.push(noTerminal);		
				System.out.println("3 Cima pila semantica ----> " + pilaSemantica.peek());
				regla = tabla[buscarNoTerminal(noTerminal.getX())][buscarTerminal(tupla.getX())];
				if (regla!=null) {
					
			System.out.println("33333333333333333333333333333333333" + regla);
					parse(regla);
					
					consecuente(regla, tupla.getY());
				}
				else {

					gestorErrores.error("sintactico01", numeroLinea, ". Revise que haya declarado de manera correcta la expresion " + terminal.get(terminal.size()-2) + ". No debería haber recicbido " + tupla.getX());
					comprueba = true;
				}
			}
		}
		if (!comprueba) {
			if (pilaSintactica.peek().getX().equals(tupla.getX())) {
				System.out.println("6 Pila Sintactica ----> " + pilaSintactica.peek().toString() + " tupla ----> " + tupla.toString());
				//tupla.setY(pilaSintactica.peek().getX());
				if (!pilaSintactica.peek().getX().equals("id")) {
					tupla.setY(pilaSintactica.peek().getY());
					
				}
				
				pilaSintactica.pop();
				pilaSemantica.push(tupla);
				if (tupla.getX().equals("$")) {
					escritura.cerrarDescriptor();
				}
			}
			else {
				gestorErrores.error("sintactico02", numeroLinea, "Se esperaba '" + pilaSintactica.peek().getX() + "' pero se recibio '" + tupla.getX() + "'");
				comprueba = true;
			}
		}
	}

	public void meterNoTerminales(String cadena, String identificador) {
		int i;
		String[] token = cadena.split(" ");

		/*for (int j = 0; j < token.length; j++) {
			System.out.println(token[j]);
		}*/
		for (i = token.length-1; i >= 0; i--) {

			if (buscarTerminal(token[i])!=-1) {
				if (terminal.isEmpty()) {
					terminal.add(0, token[i]);
				}
				else {
					terminal.add(terminal.size(), token[i]);
				}
			}
			if (!token[i].equals("+") && !token[i].equals("id")) {
				pilaSintactica.push(new Tuple<String, String>(token[i], "-"));
			}
			else {
				if (token[i].equals("id")) {
					System.out.println("5 identificador ----> " + identificador);
					pilaSintactica.push(new Tuple<String, String>("id", identificador));

				}
			}
		}

	}

	public void semantico(String valorSemantico) {
		switch(valorSemantico) {
		case "0.1":
			tablaSimbolos.crearTS();
			zona_decl = true;
			retorno = false;
			break;

		case "0.2":
			tablaSimbolos.destruirTS();
			break;

		case "0.3":
			int i;
			popN(1);
			for(i = 0; i < todosPop; i++) {
				pilaSemantica.pop();
			}
			break;

		case "1.1":
			//zona_decl = false;
			break;

		case "1.2":
			popN(2);
			break;

		case "2.1":
			popN(2);
			break;

		case "4.1":
			Tuple<String, String> R = new Tuple<String, String>("", "");
			Tuple<String, String> EZ = new Tuple<String, String>("", "");

			EZ = pilaSintactica.pop();
			R = pilaSemantica.pop();

			EZ.setY(R.getY());

			pilaSemantica.push(R);
			pilaSintactica.push(EZ);
			break;

		case "4.2":
			Tuple<String, String> E2 = new Tuple<String, String>("", "");
			Tuple<String, String> R2 = new Tuple<String, String>("", "");
			Tuple<String, String> EZ2 = new Tuple<String, String>("", "");

			EZ2 = pilaSemantica.pop();
			R2 = pilaSemantica.pop();
			E2 = pilaSemantica.pop();

			E2.setY(EZ2.getY());

			pilaSemantica.push(E2);
			pilaSintactica.push(R2);
			pilaSintactica.push(EZ2);
			break;

		case "4.3":
			popN(2);
			break;

		case "5.1":
			Tuple<String, String> EZ1_3 = new Tuple<String, String>("", "");
			Tuple<String, String> R3 = new Tuple<String, String>("", "");
			Tuple<String, String> and3 = new Tuple<String, String>("", "");
			Tuple<String, String> EZ3 = new Tuple<String, String>("", "");

			EZ1_3 = pilaSemantica.pop();
			R3 = pilaSemantica.pop();
			and3 = pilaSemantica.pop();
			EZ3 = pilaSemantica.pop();

			if (!EZ3.getY().equals("logico") || !R3.getY().equals("logico")) {
				EZ3.setY("tipo_error");
				gestorErrores.error("semantico01", numeroLinea, "La operacion logica && solo puede usarse con logicos");
				comprueba = true;
			}

			pilaSemantica.push(EZ3);
		//	pilaSemantica.push(and3);
			pilaSemantica.push(R3);
			pilaSemantica.push(EZ1_3);
			break;

		case "5.2":
			popN(2);
			break;

		case "7.1":

			Tuple<String, String> RZ4 = new Tuple<String, String>("", "");
			Tuple<String, String> U4 = new Tuple<String, String>("", "");

			RZ4 = pilaSintactica.pop();
			U4 = pilaSemantica.pop();

			RZ4.setY(U4.getY());

			pilaSintactica.push(RZ4);
			pilaSemantica.push(U4);
			break;

		case "7.2":
			Tuple<String, String> RZ5 = new Tuple<String, String>("", "");
			Tuple<String, String> U5 = new Tuple<String, String>("", "");
			Tuple<String, String> R5 = new Tuple<String, String>("", "");

			RZ5 = pilaSemantica.pop();
			U5 = pilaSemantica.pop();
			R5 = pilaSemantica.pop();

			R5.setY(RZ5.getY());

			pilaSemantica.push(R5);
			pilaSemantica.push(U5);
			//pilaSemantica.push(RZ5);
			break;

		case "7.3":
			popN(1);
			break;

		case "8.1":
			Tuple<String, String> RZ1_6 = new Tuple<String, String>("", "");
			Tuple<String, String> U6 = new Tuple<String, String>("", "");
			Tuple<String, String> mayor6 = new Tuple<String, String>("", "");
			Tuple<String, String> RZ6 = new Tuple<String, String>("", "");

			RZ1_6 = pilaSemantica.pop();
			U6 = pilaSemantica.pop();
			mayor6 = pilaSemantica.pop();
			RZ6 = pilaSemantica.pop();

			if (!RZ6.getY().equals("ent") || !U6.getY().equals("ent")) {
				RZ6.setY("tipo_error");
				gestorErrores.error("semantico02", numeroLinea, "El operador relacional > solo puede usarse con enteros");
				comprueba = true;
			}
			else {
				RZ6.setY("logico");
			}

			pilaSemantica.push(RZ6);
			//pilaSemantica.push(mayor6);
			pilaSemantica.push(U6);
			//pilaSemantica.push(RZ1_6);
			break;

		case "8.2":
			popN(1);
			break;

			case "9.1": 
			Tuple<String, String> RZ58 = new Tuple<String, String>("", "");

			RZ58 = pilaSemantica.pop();

			RZ58.setY("vacio");

			pilaSemantica.push(RZ58);
			break;
			
		case "10.1":
			Tuple<String, String> UZ7 = new Tuple<String, String>("", "");
			Tuple<String, String> C7 = new Tuple<String, String>("", "");

			UZ7 = pilaSintactica.pop();
			C7 = pilaSemantica.pop();

			UZ7.setY(C7.getY());

			pilaSintactica.push(UZ7);
			pilaSemantica.push(C7);
			break;

		case "10.2":
			Tuple<String, String> UZ8 = new Tuple<String, String>("", "");
			Tuple<String, String> C8 = new Tuple<String, String>("", "");
			Tuple<String, String> U8 = new Tuple<String, String>("", "");

			UZ8 = pilaSemantica.pop();
			C8 = pilaSemantica.pop();
			U8 = pilaSemantica.pop();

			U8.setY(UZ8.getY());

			pilaSemantica.push(U8);
			pilaSemantica.push(C8);
			//pilaSemantica.push(UZ8);
			break;

		case "10.3":
			popN(1);
			break;

		case "11.1":
			Tuple<String, String> UZ1_9 = new Tuple<String, String>("", "");
			Tuple<String, String> C9 = new Tuple<String, String>("", "");
			Tuple<String, String> menos9 = new Tuple<String, String>("", "");
			Tuple<String, String> UZ9 = new Tuple<String, String>("", "");

			System.out.println("22222222222222222222222222222222222" + pilaSemantica.toString());
			UZ1_9 = pilaSemantica.pop();
			C9 = pilaSemantica.pop();
			menos9 = pilaSemantica.pop();
			UZ9 = pilaSemantica.pop();

			if (!UZ9.getY().equals("ent") || !C9.getY().equals("ent")) {
				UZ9.setY("tipo_error");
				gestorErrores.error("semantico03", numeroLinea, "No se puede restar algo que no sean enteros");
				comprueba = true;
			}

			pilaSemantica.push(UZ9);
			//pilaSemantica.push(menos9);
			pilaSemantica.push(C9);
			pilaSemantica.push(UZ1_9);
			break;

		case "11.2":
			popN(1);
			break;
			
			case "12.1":
			Tuple<String, String> UZ57 = new Tuple<String, String>("", "");

			UZ57 = pilaSemantica.pop();

			UZ57.setY("vacio");

			pilaSemantica.push(UZ57);
			break;

		case "13.1":
			Tuple<String, String> CZ10 = new Tuple<String, String>("", "");
			Tuple<String, String> id10 = new Tuple<String, String>("", "");
			Tuple<String, String> C10 = new Tuple<String, String>("", "");

			//System.out.println("11111111111111111111111111111111111" + pilaSemantica.toString());
			
			System.out.println("22222222222222222222222222222222222" + pilaSemantica.toString());
			CZ10 = pilaSemantica.pop();
			System.out.println("8 CZ10 es " + CZ10);
			id10 = pilaSemantica.pop();
			C10 = pilaSemantica.pop();

			//Hay que saber el nombre del id con id.getX
			compruebaLexema(id10.getY());
			System.out.println("7 tipo de lo que hay despues del igual es " + CZ10.getY());
			if (!CZ10.getY().equals("vacio")) {
				if (tablaSimbolos.buscaTipo(tablaSimbolos.buscaPosicion(id10.getY())).equals(CZ10.getY())) {
					C10.setY(CZ10.getY());
				}
				else {
					C10.setY("tipo_error");
					gestorErrores.error("semantico04", numeroLinea, "El tipo de " + id10.getY() + " es incorrecto");
					comprueba = true;
				}
			}
			else {
				C10.setY(tablaSimbolos.buscaTipo(tablaSimbolos.buscaPosicion(id10.getY())));
			}

			pilaSemantica.push(C10);
			//pilaSemantica.push(id10);
			//pilaSemantica.push(CZ10);
			
		//	System.out.println("22222222222222222222222222222222222" + pilaSemantica.toString());
			break;

		case "13.2":
			popN(0);
			break;

		case "14.1":
			Tuple<String, String> pc11 = new Tuple<String, String>("", "");
			Tuple<String, String> E11 = new Tuple<String, String>("", "");
			Tuple<String, String> pa11 = new Tuple<String, String>("", "");
			Tuple<String, String> C11 = new Tuple<String, String>("", "");

			pc11 = pilaSemantica.pop();
			E11 = pilaSemantica.pop();
			pa11 = pilaSemantica.pop();
			C11 = pilaSemantica.pop();

			C11.setY(E11.getY());

			pilaSemantica.push(C11);
		//	pilaSemantica.push(pa11);
			pilaSemantica.push(E11);
			//pilaSemantica.push(pc11);
			break;

		case "14.2":
			popN(1);
			break;

		case "15.1":
			Tuple<String, String> ent12 = new Tuple<String, String>("", "");
			Tuple<String, String> C12 = new Tuple<String, String>("", "");

			ent12 = pilaSemantica.pop();
			C12 = pilaSemantica.pop();

			C12.setY("ent");

			pilaSemantica.push(C12);
			//pilaSemantica.push(ent12);
			break;

		case "15.2":
			popN(0);
			break;

		case "16.1":
			Tuple<String, String> cad13 = new Tuple<String, String>("", "");
			Tuple<String, String> C13 = new Tuple<String, String>("", "");

			cad13 = pilaSemantica.pop();
			C13 = pilaSemantica.pop();

			C13.setY("cad");

			pilaSemantica.push(C13);
			//pilaSemantica.push(cad13);
			break;

		case "16.2":
			popN(0);
			break;

		case "17.1":
			Tuple<String, String> ent14 = new Tuple<String, String>("", "");
			Tuple<String, String> igual14 = new Tuple<String, String>("", "");
			Tuple<String, String> CZ14 = new Tuple<String, String>("", "");

			ent14 = pilaSemantica.pop();
			igual14 = pilaSemantica.pop();
			CZ14 = pilaSemantica.pop();

			CZ14.setY("ent");

			pilaSemantica.push(CZ14);
		//	pilaSemantica.push(igual14);
		//	pilaSemantica.push(ent14);
			break;

		case "17.2":
			popN(0);
			break;

		case "18.1":
			Tuple<String, String> pc15 = new Tuple<String, String>("", "");
			Tuple<String, String> L15 = new Tuple<String, String>("", "");
			Tuple<String, String> pa15 = new Tuple<String, String>("", "");
			Tuple<String, String> CZ15 = new Tuple<String, String>("", "");

			pc15 = pilaSemantica.pop();
			L15 = pilaSemantica.pop();
			pa15 = pilaSemantica.pop();
			CZ15 = pilaSemantica.pop();

			CZ15.setY(L15.getY());

			pilaSemantica.push(CZ15);
		//	pilaSemantica.push(pa15);
			pilaSemantica.push(L15);
		//	pilaSemantica.push(pc15);
			break;

		case "18.2":
			popN(1);
			break;

			case "19.1":
			Tuple<String, String> CZ56 = new Tuple<String, String>("", "");

			CZ56 = pilaSemantica.pop();

			CZ56.setY("vacio");

			pilaSemantica.push(CZ56);
			break;

		case "20.1":
			Tuple<String, String> SZ16 = new Tuple<String, String>("", "");
			Tuple<String, String> id16 = new Tuple<String, String>("", "");
			Tuple<String, String> S16 = new Tuple<String, String>("", "");

			System.out.println("22222222222222222222222222222222222" + pilaSemantica.toString());
			SZ16 = pilaSemantica.pop();
			id16 = pilaSemantica.pop();
			S16 = pilaSemantica.pop();

			compruebaLexema(id16.getY());
			if (tablaSimbolos.buscaTipo(tablaSimbolos.buscaPosicion(id16.getY())).equals("funcion")) {
				if (numParam != tablaSimbolos.buscarNumeroParametros(tablaSimbolos.buscaPosicion(id16.getY()))) {
					S16.setY("tipo_error");
					gestorErrores.error("semantico05", numeroLinea, "El numero de parametros de " + id16.getY() + 
							" no concuerda. Se esperaban " + tablaSimbolos.buscarNumeroParametros(tablaSimbolos.buscaPosicion(id16.getY())) + 
							" parametros y se recibieron " + numParam);
					comprueba = true;
				}
				else if (tipoParam != tablaSimbolos.buscarTipoParametros(tablaSimbolos.buscaPosicion(id16.getY()))){
					S16.setY("tipo_error");
					gestorErrores.error("semantico06", numeroLinea, "El tipo de los parametros de " + id16.getY() + " no coincide");
					comprueba = true;
				}
				else {
					S16.setY("tipo_ok");
				}
			}
			else {
				if (tablaSimbolos.buscaTipo(tablaSimbolos.buscaPosicion(id16.getY())).equals(SZ16.getY())) {
					S16.setY("tipo_ok");
				}
				else {
					S16.setY("tipo_error");
					gestorErrores.error("semantico07", numeroLinea, "El tipo de ambos lados de la asignacion debe ser igual");
					comprueba = true;
				}
			}

			pilaSemantica.push(S16);	
			pilaSemantica.push(id16);
			pilaSemantica.push(SZ16);			
			break;

		case "20.2":
			popN(2);
			break;

		case "21.1":
			Tuple<String, String> Y17 = new Tuple<String, String>("", "");
			Tuple<String, String> S17 = new Tuple<String, String>("", "");

			Y17 = pilaSemantica.pop();
			S17 = pilaSemantica.pop();

			if (tablaSimbolos.buscaTipo(tablaSimbolos.buscaPosicion(Y17.getY())).equals("ent")) {
				S17.setY("tipo_ok");
			}
			else {
				S17.setY("tipo_error");
				gestorErrores.error("semantico08", numeroLinea, "La operacion -- solo puede hacerse con enteros");
				comprueba = true;
			}

			pilaSemantica.push(S17);
			pilaSemantica.push(Y17);
			break;

		case "21.2":
			popN(1);
			break;

		case "22.1":
			Tuple<String, String> puntoyc18 = new Tuple<String, String>("", "");
			Tuple<String, String> E18 = new Tuple<String, String>("", "");
			Tuple<String, String> igual18 = new Tuple<String, String>("", "");
			Tuple<String, String> SZ18 = new Tuple<String, String>("", "");

			//System.out.println("22222222222222222222222222222222222" + pilaSemantica.toString());
			puntoyc18 = pilaSemantica.pop();
			E18 = pilaSemantica.pop();
			igual18 = pilaSemantica.pop();
			SZ18 = pilaSemantica.pop();

			SZ18.setY(E18.getY());

			pilaSemantica.push(SZ18);
			//pilaSemantica.push(igual18);
			pilaSemantica.push(E18);
			//pilaSemantica.push(puntoyc18);
			break;

		case "22.2":
			popN(1);
			break;

		case "23.1":
			Tuple<String, String> puntoyc19 = new Tuple<String, String>("", "");
			Tuple<String, String> pc19 = new Tuple<String, String>("", "");
			Tuple<String, String> L19 = new Tuple<String, String>("", "");
			Tuple<String, String> pa19 = new Tuple<String, String>("", "");
			Tuple<String, String> SZ19 = new Tuple<String, String>("", "");

			puntoyc19 = pilaSemantica.pop();
			pc19 = pilaSemantica.pop();
			L19 = pilaSemantica.pop();
			pa19 = pilaSemantica.pop();
			SZ19 = pilaSemantica.pop();

			SZ19.setY("funcion");

			pilaSemantica.push(SZ19);
		//	pilaSemantica.push(pa19);
			pilaSemantica.push(L19);
		//	pilaSemantica.push(pc19);
		//	pilaSemantica.push(puntoyc19);
			break;

		case "23.2":
			popN(1);
			break;

		case "25.1":
			Tuple<String, String> Q20 = new Tuple<String, String>("", "");
			Tuple<String, String> E20 = new Tuple<String, String>("", "");
			Tuple<String, String> L20 = new Tuple<String, String>("", "");

			Q20 = pilaSemantica.pop();
			E20 = pilaSemantica.pop();
			L20 = pilaSemantica.pop();

			if (Q20.getY().equals("vacio")) {
				tipoParam = E20.getY();
				L20.setY(tipoParam);
				numParam = 1;
			}
			else {
				tipoParam = E20.getY() + ", " + tipoParam;
				L20.setY(tipoParam);
				numParam = numParam + 1;
			}

			pilaSemantica.push(L20);
			pilaSemantica.push(E20);
			pilaSemantica.push(Q20);
			break;

		case "25.2":
			popN(2);
			break;

		case "26.1":
			Tuple<String, String> L21 = new Tuple<String, String>("", "");

			L21 = pilaSemantica.pop();

			L21.setY("vacio");

			pilaSemantica.push(L21);
			break;

		case "27.1":
			Tuple<String, String> Q1_22 = new Tuple<String, String>("", "");
			Tuple<String, String> E22 = new Tuple<String, String>("", "");
			Tuple<String, String> coma22 = new Tuple<String, String>("", "");
			Tuple<String, String> Q22 = new Tuple<String, String>("", "");

			Q1_22 = pilaSemantica.pop();
			E22 = pilaSemantica.pop();
			coma22 = pilaSemantica.pop();
			Q22 = pilaSemantica.pop();

			if (Q22.getY().equals("vacio")) {
				tipoParam = E22.getY();
				numParam = 1;
				Q22.setY(tipoParam);
			} else {
				tipoParam = E22.getY() + ", " + Q1_22.getY();
				Q22.setY(tipoParam);
				Q1_22.setY(Q22.getY());
				numParam = numParam + 1;
			}

			pilaSemantica.push(Q22);
		//	pilaSemantica.push(coma22);
			pilaSemantica.push(E22);
			pilaSemantica.push(Q1_22);
			break;

		case "27.2":
			popN(2);
			break;

		case "28.1":
			Tuple<String, String> Q23 = new Tuple<String, String>("", "");

			Q23 = pilaSemantica.pop();

			Q23.setY("vacio");

			pilaSemantica.push(Q23);
			break;

		case "29.1":
			Tuple<String, String> puntoyc24 = new Tuple<String, String>("", "");
			Tuple<String, String> E24 = new Tuple<String, String>("", "");
			Tuple<String, String> put24 = new Tuple<String, String>("", "");
			Tuple<String, String> S24 = new Tuple<String, String>("", "");

			puntoyc24 = pilaSemantica.pop();
			E24 = pilaSemantica.pop();
			put24 = pilaSemantica.pop();
			S24 = pilaSemantica.pop();

			if(E24.getY().equals("ent") || E24.getY().equals("cad")) {
				S24.setY("tipo_ok");
			}
			else {
				S24.setY("tipo_error");
				gestorErrores.error("semantico09", numeroLinea, "put solo se puede usar con enteros o cadenas");
				comprueba = true;
			}

			pilaSemantica.push(S24);
			//pilaSemantica.push(put24);
			pilaSemantica.push(E24);
			//pilaSemantica.push(puntoyc24);
			break;

		case "29.2":
			popN(1);
			break;

		case "30.1":
			Tuple<String, String> puntoyc25 = new Tuple<String, String>("", "");
			Tuple<String, String> id25 = new Tuple<String, String>("", "");
			Tuple<String, String> get25 = new Tuple<String, String>("", "");
			Tuple<String, String> S25 = new Tuple<String, String>("", "");

			puntoyc25 = pilaSemantica.pop();
			id25 = pilaSemantica.pop();
			get25 = pilaSemantica.pop();
			S25 = pilaSemantica.pop();

			compruebaLexema(id25.getY());
			if(tablaSimbolos.buscaTipo(tablaSimbolos.buscaPosicion(id25.getY())).equals("ent") || 
					tablaSimbolos.buscaTipo(tablaSimbolos.buscaPosicion(id25.getY())).equals("cad")) {
				S25.setY("tipo_ok");
			}
			else {
				S25.setY("tipo_error");
				gestorErrores.error("semantico10", numeroLinea, "get solo se puede usar con enteros o cadenas");
				comprueba = true;
			}

			pilaSemantica.push(S25);
		//	pilaSemantica.push(get25);
			pilaSemantica.push(id25);
			//pilaSemantica.push(puntoyc25);
			break;

		case "30.2":
			popN(1);
			break;

		case "31.1":
			Tuple<String, String> puntoyc26 = new Tuple<String, String>("", "");
			Tuple<String, String> X26 = new Tuple<String, String>("", "");
			Tuple<String, String> return26 = new Tuple<String, String>("", "");
			Tuple<String, String> S26 = new Tuple<String, String>("", "");

			puntoyc26 = pilaSemantica.pop();
			X26 = pilaSemantica.pop();
			return26 = pilaSemantica.pop();
			S26 = pilaSemantica.pop();

			if (!retorno) {
				S26.setY("tipo_error");
				gestorErrores.error("semantico11", numeroLinea, "No puede haber return fuera de una funcion");
				comprueba = true;
			}
			else {
				S26.setY("tipo_ok");
				tipoReturn = X26.getY();
				tieneRetorno = true;
			}

			pilaSemantica.push(S26);
			//pilaSemantica.push(return26);
			pilaSemantica.push(X26);
			//pilaSemantica.push(puntoyc26);
			break;

		case "31.2":
			popN(1);
			break;

		case "32.1":
			Tuple<String, String> E27 = new Tuple<String, String>("", "");
			Tuple<String, String> X27 = new Tuple<String, String>("", "");

			E27 = pilaSemantica.pop();
			X27 = pilaSemantica.pop();

			X27.setY(E27.getY());

			pilaSemantica.push(X27);
			pilaSemantica.push(E27);
			break;

		case "32.2":
			popN(1);
			break;

		case "33.1":
			Tuple<String, String> X28 = new Tuple<String, String>("", "");

			X28 = pilaSemantica.pop();

			X28.setY("vacio");

			pilaSemantica.push(X28);
			break;

		case "34.1":
			Tuple<String, String> S29 = new Tuple<String, String>("", "");
			Tuple<String, String> pc29 = new Tuple<String, String>("", "");
			Tuple<String, String> E29 = new Tuple<String, String>("", "");
			Tuple<String, String> pa29 = new Tuple<String, String>("", "");
			Tuple<String, String> if29 = new Tuple<String, String>("", "");
			Tuple<String, String> D29 = new Tuple<String, String>("", "");

			S29 = pilaSemantica.pop();
			pc29 = pilaSemantica.pop();
			E29 = pilaSemantica.pop();
			pa29 = pilaSemantica.pop();
			if29 = pilaSemantica.pop();
			D29 = pilaSemantica.pop();

			if (E29.getY().equals("logico")) {
				D29.setY(S29.getY());
			}
			else {
				D29.setY("tipo_error");
				gestorErrores.error("semantico12", numeroLinea, "La condicion del if debe ser logica");
				comprueba = true;
			}

			pilaSemantica.push(D29);
		//	pilaSemantica.push(if29);
		//	pilaSemantica.push(pa29);
			pilaSemantica.push(E29);
		//	pilaSemantica.push(pc29);
			pilaSemantica.push(S29);
			break;

		case "34.2":
			popN(2);
			break;

		case "35.1":
			Tuple<String, String> S30 = new Tuple<String, String>("", "");
			Tuple<String, String> D30 = new Tuple<String, String>("", "");

			S30 = pilaSemantica.pop();
			D30 = pilaSemantica.pop();

			D30.setY(S30.getY());

			pilaSemantica.push(D30);
			pilaSemantica.push(S30);
			break;

		case "35.2":
			popN(1);
			break;

		case "36.1":
			Tuple<String, String> puntoyc31 = new Tuple<String, String>("", "");
			Tuple<String, String> T31 = new Tuple<String, String>("", "");
			Tuple<String, String> id31 = new Tuple<String, String>("", "");
			Tuple<String, String> let31 = new Tuple<String, String>("", "");
			Tuple<String, String> D31 = new Tuple<String, String>("", "");
			

			puntoyc31 = pilaSemantica.pop();
			T31 = pilaSemantica.pop();
			id31 = pilaSemantica.pop();
			let31 = pilaSemantica.pop();
			D31 = pilaSemantica.pop();

			if (!zona_decl) {
				D31.setY("tipo_error");
				gestorErrores.error("semantico13", numeroLinea, "Los atributos deben declararse al inicio del codigo o de las funciones");
				comprueba = true;
			} 
			else {
				if (T31.getY().equals("vacio")) {
					D31.setY("tipo_error");
					gestorErrores.error("semantico14", numeroLinea, "No se puede declarar un atributo como void");
					comprueba = true;
				}
				else {
					if (tablaSimbolos.buscaPosicion(id31.getY()) == -1) {
						tablaSimbolos.insertarLexema(id31.getY());
						tablaSimbolos.lineaLexema(id31.getY());
						tablaSimbolos.insertarTipo(T31.getY());
						if (T31.getY().equals("ent") || T31.getY().equals("boolean")) {
							tablaSimbolos.insertarDesplazamiento(1);
						}
						else {
							tablaSimbolos.insertarDesplazamiento(64);
						}
						tablaSimbolos.insertarNumParam(-1);
						tablaSimbolos.insertarTipoParametros("");
						tablaSimbolos.insertarTipoRet("");
						tablaSimbolos.lineaAtributos(tablaSimbolos.buscaPosicion(id31.getY()));
						D31.setY("tipo_ok");
					}
					else {
						D31.setY("tipo_error");
						gestorErrores.error("semantico18", numeroLinea, id31.getY() + " ya ha sido declarado");
						comprueba = true;
					}
				}
			}

			pilaSemantica.push(D31);
			//pilaSemantica.push(let31);
			pilaSemantica.push(id31);
			pilaSemantica.push(T31);
			//pilaSemantica.push(puntoyc31);
			break;

		case "36.2":
			popN(1);
			break;

		case "37.1":
			Tuple<String, String> int32 = new Tuple<String, String>("", "");
			Tuple<String, String> T32 = new Tuple<String, String>("", "");
			//System.out.println("1111111111111111111111111111111111111111111" + pilaSemantica.toString());
			
			int32 = pilaSemantica.pop();
			T32 = pilaSemantica.pop();
			

			T32.setY("ent");

			pilaSemantica.push(T32);
			//pilaSemantica.push(int32);
			
			//System.out.println("22222222222222222222222222222222222" + pilaSemantica.toString());
			break;

		case "37.2":
			popN(0);
			break;

		case "38.1":
			Tuple<String, String> boolean33 = new Tuple<String, String>("", "");
			Tuple<String, String> T33 = new Tuple<String, String>("", "");

			boolean33 = pilaSemantica.pop();
			T33 = pilaSemantica.pop();

			T33.setY("logico");

			pilaSemantica.push(T33);
			//pilaSemantica.push(boolean33);
			break;

		case "38.2":
			popN(0);
			break;

		case "39.1":
			Tuple<String, String> string34 = new Tuple<String, String>("", "");
			Tuple<String, String> T34 = new Tuple<String, String>("", "");

			string34 = pilaSemantica.pop();
			T34 = pilaSemantica.pop();

			T34.setY("cad");

			pilaSemantica.push(T34);
		//	pilaSemantica.push(string34);
			break;

		case "39.2":
			popN(0);
			break;

		case "40.1":
			Tuple<String, String> int35 = new Tuple<String, String>("", "");
			Tuple<String, String> M35 = new Tuple<String, String>("", "");

			int35 = pilaSemantica.pop();
			M35 = pilaSemantica.pop();

			M35.setY("ent");

			pilaSemantica.push(M35);
		//	pilaSemantica.push(int35);
			break;

		case "40.2":
			popN(0);
			break;

		case "41.1":
			Tuple<String, String> boolean36 = new Tuple<String, String>("", "");
			Tuple<String, String> M36 = new Tuple<String, String>("", "");

			boolean36 = pilaSemantica.pop();
			M36 = pilaSemantica.pop();

			M36.setY("logico");

			pilaSemantica.push(M36);
		//	pilaSemantica.push(boolean36);
			break;

		case "41.2":
			popN(0);
			break;

		case "42.1":
			Tuple<String, String> string37 = new Tuple<String, String>("", "");
			Tuple<String, String> M37 = new Tuple<String, String>("", "");

			string37 = pilaSemantica.pop();
			M37 = pilaSemantica.pop();

			M37.setY("cad");

			pilaSemantica.push(M37);
			//pilaSemantica.push(string37);
			break;

		case "42.2":
			popN(0);
			break;

		case "43.1":
			Tuple<String, String> void38 = new Tuple<String, String>("", "");
			Tuple<String, String> M38 = new Tuple<String, String>("", "");

			void38 = pilaSemantica.pop();
			M38 = pilaSemantica.pop();

			M38.setY("vacio");

			pilaSemantica.push(M38);
			//pilaSemantica.push(void38);
			break;

		case "43.2":
			popN(0);
			break;

		case "44.1":
			Tuple<String, String> lc39 = new Tuple<String, String>("", "");
			Tuple<String, String> I39 = new Tuple<String, String>("", "");
			Tuple<String, String> la39 = new Tuple<String, String>("", "");
			Tuple<String, String> pc39 = new Tuple<String, String>("", "");
			Tuple<String, String> W39 = new Tuple<String, String>("", "");
			Tuple<String, String> pyc39_1 = new Tuple<String, String>("", "");
			Tuple<String, String> E39 = new Tuple<String, String>("", "");
			Tuple<String, String> pyc39_2 = new Tuple<String, String>("", "");
			Tuple<String, String> Z39 = new Tuple<String, String>("", "");
			Tuple<String, String> pa39 = new Tuple<String, String>("", "");
			Tuple<String, String> for39 = new Tuple<String, String>("", "");
			Tuple<String, String> D39 = new Tuple<String, String>("", "");

			lc39 = pilaSemantica.pop();
			I39 = pilaSemantica.pop();
			la39 = pilaSemantica.pop();
			pc39 = pilaSemantica.pop();
			W39 = pilaSemantica.pop();
			pyc39_1 = pilaSemantica.pop();
			E39 = pilaSemantica.pop();
			pyc39_2 = pilaSemantica.pop();
			Z39 = pilaSemantica.pop();
			pa39 = pilaSemantica.pop();
			for39 = pilaSemantica.pop();
			D39 = pilaSemantica.pop();

			if(Z39.getY().equals("tipo_ok") && E39.getY().equals("logico") &&
					!W39.getY().equals("tipo_error") && !I39.getY().equals("tipo_error")) {
				D39.setY("tipo_ok");
			}
			else {
				D39.setY("tipo_error");
				if (!E39.getY().equals("logico")) {
					gestorErrores.error("semantico15", numeroLinea, "La condicion del bucle for debe ser logica");
					comprueba = true;
				}
			}

			pilaSemantica.push(D39);
			//pilaSemantica.push(for39);
			//pilaSemantica.push(pa39);
			pilaSemantica.push(Z39);
			//pilaSemantica.push(pyc39_2);
			pilaSemantica.push(E39);
			//pilaSemantica.push(pyc39_1);
			pilaSemantica.push(W39);
			//pilaSemantica.push(pc39);
			//pilaSemantica.push(la39);
			pilaSemantica.push(I39);
			//pilaSemantica.push(lc39);

			break;
			
		case "44.2":
			popN(5);
			break;

		case "45.1":
			Tuple<String, String> H40 = new Tuple<String, String>("", "");
			Tuple<String, String> id40 = new Tuple<String, String>("", "");

			H40 = pilaSemantica.pop();
			id40 = pilaSemantica.pop();

			zona_decl = true;
			tablaSimbolos.insertarLexema(id40.getY());
			tablaSimbolos.lineaLexema(id40.getY());
			tablaSimbolos.insertarTipo("funcion");
			tablaSimbolos.insertarTipoRet(H40.getY());
			tablaSimbolos.crearTS(id40.getY());

			tablaSimbolos.lineaTabla();
			tablaSimbolos.lineaAtributos(tablaSimbolos.buscaPosicion(id40.getY()));

			pilaSemantica.push(id40);
			pilaSemantica.push(H40);			
			break;

		case "45.2":
			Tuple<String, String> pc41 = new Tuple<String, String>("", "");
			Tuple<String, String> G41 = new Tuple<String, String>("", "");
			Tuple<String, String> pa41 = new Tuple<String, String>("", "");
			Tuple<String, String> H41 = new Tuple<String, String>("", "");
			Tuple<String, String> id41 = new Tuple<String, String>("", "");
			Tuple<String, String> function41 = new Tuple<String, String>("", "");
			Tuple<String, String> F41 = new Tuple<String, String>("", "");

			pc41 = pilaSemantica.pop();
			G41 = pilaSemantica.pop();
			pa41 = pilaSemantica.pop();
			H41 = pilaSemantica.pop();
			id41 = pilaSemantica.pop();
			function41 = pilaSemantica.pop();
			F41 = pilaSemantica.pop();

			tablaSimbolos.insertarNumParam(numParamCreacion);
			tablaSimbolos.insertarTipoParametros(G41.getY());
			if(G41.getY().equals("tipo_error")) {
				F41.setY("tipo_error");
			}

			pilaSemantica.push(F41);
			pilaSemantica.push(function41);
			pilaSemantica.push(id41);
			pilaSemantica.push(H41);
			pilaSemantica.push(pa41);
			pilaSemantica.push(G41);
			pilaSemantica.push(pc41);
			break;

		case "45.3":
			Tuple<String, String> lc42 = new Tuple<String, String>("", "");
			Tuple<String, String> I42 = new Tuple<String, String>("", "");
			Tuple<String, String> la42 = new Tuple<String, String>("", "");
			Tuple<String, String> pc42 = new Tuple<String, String>("", "");
			Tuple<String, String> G42 = new Tuple<String, String>("", "");
			Tuple<String, String> pa42 = new Tuple<String, String>("", "");
			Tuple<String, String> H42 = new Tuple<String, String>("", "");
			Tuple<String, String> id42 = new Tuple<String, String>("", "");
			Tuple<String, String> function42 = new Tuple<String, String>("", "");
			Tuple<String, String> F42 = new Tuple<String, String>("", "");

			lc42 = pilaSemantica.pop();
			I42 = pilaSemantica.pop();
			la42 = pilaSemantica.pop();
			pc42 = pilaSemantica.pop();
			G42 = pilaSemantica.pop();
			pa42 = pilaSemantica.pop();
			H42 = pilaSemantica.pop();
			id42 = pilaSemantica.pop();
			function42 = pilaSemantica.pop();
			F42 = pilaSemantica.pop();

			zona_decl = false;
			if (!H42.getY().equals("vacio") && !tieneRetorno) {
				F42.setY("tipo_error");
				gestorErrores.error("semantico16", numeroLinea, "La funcion " + id42.getY() + " debe tener retorno");
				comprueba = true;
			}
			else if (!tipoReturn.equals(H42.getY())) {
				F42.setY("tipo_error");
				gestorErrores.error("semantico17", numeroLinea, "El tipo de retorno no coincide con el de la funcion " + id42.getY());
				comprueba = true;
			}
			else {
				F42.setY("tipo_ok");
			}
			tieneRetorno = false;
			tablaSimbolos.destruirTS(id42.getY());

			pilaSemantica.push(F42);
			//pilaSemantica.push(function42);
			pilaSemantica.push(id42);
			pilaSemantica.push(H42);
			//pilaSemantica.push(pa42);
			pilaSemantica.push(G42);
			//pilaSemantica.push(pc42);
			//pilaSemantica.push(la42);
			pilaSemantica.push(I42);
			//pilaSemantica.push(lc42);
			break;

		case "45.4":
			popN(4);
			break;

		case "46.1":
			Tuple<String, String> M43 = new Tuple<String, String>("", "");
			Tuple<String, String> H43 = new Tuple<String, String>("", "");

			M43 = pilaSemantica.pop();
			H43 = pilaSemantica.pop();

			H43.setY(M43.getY());

			pilaSemantica.push(H43);
			pilaSemantica.push(M43);
			break;

		case "46.2":
			popN(1);
			break;

		case "47.1":
			Tuple<String, String> void44 = new Tuple<String, String>("", "");
			Tuple<String, String> G44 = new Tuple<String, String>("", "");

			void44 = pilaSemantica.pop();
			G44 = pilaSemantica.pop();

			G44.setY("vacio");

			pilaSemantica.push(G44);
			//pilaSemantica.push(void44);
			break;

		case "47.2":
			popN(1);
			break;

		case "48.1":
			Tuple<String, String> K45 = new Tuple<String, String>("", "");
			Tuple<String, String> id45 = new Tuple<String, String>("", "");
			Tuple<String, String> T45 = new Tuple<String, String>("", "");
			Tuple<String, String> G45 = new Tuple<String, String>("", "");

			K45 = pilaSemantica.pop();
			id45 = pilaSemantica.pop();
			T45 = pilaSemantica.pop();
			G45 = pilaSemantica.pop();

			if (tablaSimbolos.buscaPosicion(id45.getY()) == -1) {
				tablaSimbolos.insertarLexema(id45.getY());
				tablaSimbolos.lineaLexema(id45.getY());
				tablaSimbolos.insertarTipo(T45.getY());

				if (T45.getY().equals("ent") || T45.getY().equals("boolean")) {
					tablaSimbolos.insertarDesplazamiento(1);
				}
				else {
					tablaSimbolos.insertarDesplazamiento(64);
				}
				
				tablaSimbolos.lineaAtributos(tablaSimbolos.buscaPosicion(id45.getY()));
				
				if (K45.getY().equals("vacio")) {
					G45.setY(T45.getY());
					numParamCreacion = 1;
					tipoParamCreacion = T45.getY();				
				}
				else if (K45.getY().equals("tipo_error")){
					G45.setY("tipo_error");
					gestorErrores.error("semantico19", numeroLinea, id45.getY() + " ya ha sido pasado como parametro");
					comprueba = true;
				}
				else {
					G45.setY(T45.getY() + ", " + K45.getY());
					numParamCreacion++;
					tipoParamCreacion = G45.getY();
				}
			}
			else {
				G45.setY("tipo_error");
				gestorErrores.error("semantico19", numeroLinea, id45.getY() + " ya ha sido pasado como parametro");
				comprueba = true;
				numParamCreacion = -1;
				tipoParamCreacion = "";
			}

			pilaSemantica.push(G45);
			pilaSemantica.push(T45);
			pilaSemantica.push(id45);	
			pilaSemantica.push(K45);
			break;
			
		case "48.2":
			popN(3);
			break;
			
		case "49.1":
			Tuple<String, String> G46 = new Tuple<String, String>("", "");
			
			G46 = pilaSemantica.pop();
			
			G46.setY("vacio");
			
			pilaSemantica.push(G46);
			break;
			
		case "50.1":
			Tuple<String, String> K1_47 = new Tuple<String, String>("", "");
			Tuple<String, String> id47 = new Tuple<String, String>("", "");
			Tuple<String, String> T47 = new Tuple<String, String>("", "");
			Tuple<String, String> coma47 = new Tuple<String, String>("", "");
			Tuple<String, String> K47 = new Tuple<String, String>("", "");

			K1_47 = pilaSemantica.pop();
			id47 = pilaSemantica.pop();
			T47 = pilaSemantica.pop();
			coma47 = pilaSemantica.pop();
			K47 = pilaSemantica.pop();
			
			if (tablaSimbolos.buscaPosicion(id47.getY()) == -1) {
				tablaSimbolos.insertarLexema(id47.getY());
				tablaSimbolos.lineaLexema(id47.getY());
				tablaSimbolos.insertarTipo(T47.getY());

				if (T47.getY().equals("ent") || T47.getY().equals("boolean")) {
					tablaSimbolos.insertarDesplazamiento(1);
				}
				else {
					tablaSimbolos.insertarDesplazamiento(64);
				}

				tablaSimbolos.lineaAtributos(tablaSimbolos.buscaPosicion(id47.getY()));
				
				if (K1_47.getY().equals("vacio")) {
					K47.setY(T47.getY());
					numParamCreacion = 1;
				} 
				else {
					K47.setY(T47.getY() + ", " + K1_47.getY());
					numParamCreacion++;
				}
			}
			else {
				K47.setY("tipo_error");
				gestorErrores.error("semantico19", numeroLinea, id47.getY() + " ya ha sido pasado como parametro");
				comprueba = true;
				numParamCreacion = -1;
				tipoParamCreacion = "";
			}
			
			pilaSemantica.push(K47);
			//pilaSemantica.push(coma47);
			pilaSemantica.push(T47);
			pilaSemantica.push(id47);	
			pilaSemantica.push(K1_47);
			break;
			
		case "50.2":
			popN(3);
			break;
			
		case "51.1":
			Tuple<String, String> K48 = new Tuple<String, String>("", "");
			
			K48 = pilaSemantica.pop();
			
			K48.setY("vacio");
			
			pilaSemantica.push(K48);
			break;
			
		case "52.1":
			Tuple<String, String> I1_49 = new Tuple<String, String>("", "");
			Tuple<String, String> D49 = new Tuple<String, String>("", "");
			Tuple<String, String> I49 = new Tuple<String, String>("", "");
			
			I1_49 = pilaSemantica.pop();
			D49 = pilaSemantica.pop();
			I49 = pilaSemantica.pop();
			
			if(D49.getY().equals("tipo_error")) {
				I49.setY("tipo_error");
				comprueba = true;
			}
			else {
				if (I1_49.getY().equals("vacio")) {
					I49.setY("tipo_ok");
				}
				else {
					I49.setY(I1_49.getY());
				}
			}
			
			pilaSemantica.push(I49);
			pilaSemantica.push(D49);	
			pilaSemantica.push(I1_49);
			break;
			
		case "52.2":
			popN(2);
			break;
			
		case "53.1":
			Tuple<String, String> I50 = new Tuple<String, String>("", "");
			
			I50 = pilaSemantica.pop();
			
			I50.setY("vacio");
			
			pilaSemantica.push(I50);
			break;
			
		case "54.1":
			Tuple<String, String> E51 = new Tuple<String, String>("", "");
			Tuple<String, String> igual51 = new Tuple<String, String>("", "");
			Tuple<String, String> id51 = new Tuple<String, String>("", "");
			Tuple<String, String> Z51 = new Tuple<String, String>("", "");
			
			E51 = pilaSemantica.pop();
			igual51 = pilaSemantica.pop();
			id51 = pilaSemantica.pop();
			Z51 = pilaSemantica.pop();
			
			compruebaLexema(id51.getY());
			if (tablaSimbolos.buscaTipo(tablaSimbolos.buscaPosicion(id51.getY())).equals(E51.getY())) {
				Z51.setY("tipo_ok");
			}
			else {
				Z51.setY("tipo_error");
				gestorErrores.error("semantico20", numeroLinea, "Ambos de la asignacion deben ser del mismo tipo");
				comprueba = true;
			}
			
			pilaSemantica.push(Z51);
			pilaSemantica.push(id51);
			//pilaSemantica.push(igual51);
			pilaSemantica.push(E51);
			break;
			
		case "54.2":
			popN(2);
			break;
			
		case "55.1":
			Tuple<String, String> Z52 = new Tuple<String, String>("", "");
			
			Z52 = pilaSemantica.pop();
			
			Z52.setY("vacio");
			
			pilaSemantica.push(Z52);
			break;
			
		case "56.1":
			Tuple<String, String> id53 = new Tuple<String, String>("", "");
			Tuple<String, String> mm53 = new Tuple<String, String>("", "");
			Tuple<String, String> W53 = new Tuple<String, String>("", "");
			
			id53 = pilaSemantica.pop();
			mm53 = pilaSemantica.pop();
			W53 = pilaSemantica.pop();
			
			compruebaLexema(id53.getY());
			if (tablaSimbolos.buscaTipo(tablaSimbolos.buscaPosicion(id53.getY())).equals("ent")) {
				W53.setY("tipo_ok");
			}
			else {
				W53.setY("tipo_error");
				gestorErrores.error("semantico21", numeroLinea, "La operacion -- solo se puede realizar con enteros");
				comprueba = true;
			}

			pilaSemantica.push(W53);
			//pilaSemantica.push(mm53);
			pilaSemantica.push(id53);
			break;
			
		case "56.2":
			popN(1);
			break;
			
		case "57.1":
			Tuple<String, String> E54 = new Tuple<String, String>("", "");
			Tuple<String, String> igual54 = new Tuple<String, String>("", "");
			Tuple<String, String> id54 = new Tuple<String, String>("", "");
			Tuple<String, String> Q54 = new Tuple<String, String>("", "");
			
			E54 = pilaSemantica.pop();
			igual54 = pilaSemantica.pop();
			id54 = pilaSemantica.pop();
			Q54 = pilaSemantica.pop();
			
			compruebaLexema(id54.getY());
			if (tablaSimbolos.buscaTipo(tablaSimbolos.buscaPosicion(id54.getY())).equals(E54.getY())) {
				Q54.setY("tipo_ok");
			}
			else {
				Q54.setY("tipo_error");
				gestorErrores.error("semantico20", numeroLinea, "Ambos de la asignacion deben ser del mismo tipo");
				comprueba = true;
			}
			
			pilaSemantica.push(Q54);
			pilaSemantica.push(id54);
			//pilaSemantica.push(igual54);
			pilaSemantica.push(E54);
			break;
			
		case "57.2":
			popN(2);
			
		case "59.1":
			Tuple<String, String> id55 = new Tuple<String, String>("", "");
			Tuple<String, String> mm55 = new Tuple<String, String>("", "");
			Tuple<String, String> Y55 = new Tuple<String, String>("", "");
			
			id55 = pilaSemantica.pop();
			mm55 = pilaSemantica.pop();
			Y55 = pilaSemantica.pop();
			
			compruebaLexema(id55.getY());
			if (tablaSimbolos.buscaTipo(tablaSimbolos.buscaPosicion(id55.getY())).equals("ent")) {
				Y55.setY("tipo_ok");
			}
			else {
				Y55.setY("tipo_error");
				gestorErrores.error("semantico21", numeroLinea, "La operacion -- solo se puede realizar con enteros");
				comprueba = true;
			}

			pilaSemantica.push(Y55);
			//pilaSemantica.push(mm55);
			pilaSemantica.push(id55);
			break;
			
		case "59.2":
			popN(1);
			break;
		}

	}

	public void compruebaLexema (String id) {
		if (tablaSimbolos.buscaPosicion(id) == -1) {
			tablaSimbolos.insertarLexema(id);
			tablaSimbolos.lineaLexema(id);
			tablaSimbolos.insertarTipo("ent");
			tablaSimbolos.insertarDesplazamiento(1);
			tablaSimbolos.insertarNumParam(-1);
			tablaSimbolos.insertarTipoParametros("");
			tablaSimbolos.insertarTipoRet("");

			tablaSimbolos.lineaAtributos(tablaSimbolos.buscaPosicion(id));
		}
	}

	public void popN(int n) {
		int i;
		todosPop = todosPop + n;
		/*for(i = 0; i < n; i++) {
			pilaSemantica.pop();
		}*/
	}

	public void setNumeroLinea(int numero) {
		numeroLinea = numero;
	}

	public void setGestor(GestorErrores gestor) {
		gestorErrores = gestor;
	}

	public void setTablaSimbolos(TablaSimbolos tabla) {
		tablaSimbolos = tabla;	
	}
}