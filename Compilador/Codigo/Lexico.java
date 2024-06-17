package compilador;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Lexico {
	private Entrada lectura;
	private File ficheroSalida;
	private Salida escritura;
	private ArrayList<String> identificadores;
	private Sintactico sintactico;
	private boolean error;
	private String fallo = "";
	private GestorErrores gestor;
	private TablaSimbolos tablaSimbolos;

	public Lexico() throws IOException {
		lectura = new Entrada();
		ficheroSalida = new File("C:\\Users\\lucia\\OneDrive\\Escritorio\\UPM\\3º\\2º Cuatri\\PDL\\salida.txt");
		escritura = new Salida(ficheroSalida);
		identificadores = new ArrayList<String>();
		tablaSimbolos = new TablaSimbolos();
		sintactico = new Sintactico();
		sintactico.crearTSintantico(tablaSimbolos);
		gestor = new GestorErrores();
	}

	public void correspondiente() throws IOException {
		String palabra = "";
		Tokens tokens = new Tokens();
		String[] genera = tokens.getToken(); 
		String generado = "";

		while (lectura.getLongitud()>0 && !error) {
			palabra = lectura.leer();
			switch(palabra) {
			case "boolean" :
				generado = genera[0];
				break;
			case "for" :
				generado = genera[1];
				break;
			case "function" :
				generado = genera[2];
				break;
			case "if" :
				generado = genera[3];
				break;
			case "get" :
				generado = genera[4];
				break;
			case "int" :
				generado = genera[5];
				break;
			case "let" :
				generado = genera[6];
				break;
			case "put" :
				generado = genera[7];
				break;
			case "return" :
				generado = genera[8];
				break;
			case "string" :
				generado = genera[9];
				break;
			case "void" :
				generado = genera[10];
				break;
			case "--":
				generado = genera[11];
				break;
			case "=" :
				generado = genera[15];
				break;
			case "," :
				generado = genera[16];
				break;
			case ";" :
				generado = genera[17];
				break;
			case "(" :
				generado = genera[18];
				break;
			case ")" :
				generado = genera[19];
				break;
			case "{" :
				generado = genera[20];
				break;
			case "}" :
				generado = genera[21];
				break;
			case "-" :
				generado = genera[22];
				break;
			case "&&" :
				generado = genera[23];
				break;
			case ">" :
				generado = genera[24];
				break;			
			default :
				if (Character.isDigit(palabra.charAt(0))) {
					generado = genera[12];
					sintactico.setGestor(gestor);
					sintactico.setNumeroLinea(lectura.getNumLineas());
					if (!error) {
						sintactico.analizador(new Tuple<String, String>("ent", "ent"));
					}
				}
				else if(palabra.charAt(0)==39) {
					generado = genera[13];
					sintactico.setGestor(gestor);
					sintactico.setNumeroLinea(lectura.getNumLineas());
					if (!error) {
						sintactico.analizador(new Tuple<String, String>("cad", "cad"));
					}
				}
				else {
					if (Character.isAlphabetic(palabra.charAt(0)) || palabra.charAt(0)=='_') {
						generado = genera[14];
					}
					else {
						generado = "";
						fallo = gestor.error("lexico02", lectura.getNumLineas(), palabra);
						error = true;
					}
					sintactico.setGestor(gestor);
					sintactico.setNumeroLinea(lectura.getNumLineas());
				}
				break;
			}

			if (!generado.equals(genera[12]) && !generado.equals(genera[13]) && !generado.equals(genera[14])) {
				sintactico.setGestor(gestor);
				sintactico.setNumeroLinea(lectura.getNumLineas());
				if (lectura.existe(palabra.charAt(0))) {
					if (!error) {
						sintactico.analizador(new Tuple<String, String>(palabra, "-"));
					}
				}
			}
			if (generado.equals(genera[12])||generado.equals(genera[14])) {
				valor(generado, palabra);

			}
			else {
				identifica(generado, palabra);
			}

		}
		sintactico.setGestor(gestor);
		sintactico.setNumeroLinea(lectura.getNumLineas());
		if (!error) {
			sintactico.analizador(new Tuple<String, String>("$", "-"));
		}
		//tablaSimbolos.lineaAtributos();
		tablaSimbolos.cierre();
		gestor.cierre();
		escritura.cerrarDescriptor();
	}

	public Tuple<String, String> identifica(String generado, String palabra) {
		Tokens tokens = new Tokens();
		String[] genera = tokens.getToken();
		String cadena = "";
		Tuple<String, String> tupla;
		

		if (generado.equals(genera[13])) {
			if (palabra.length()>64) {
				fallo = gestor.error("lexico03", lectura.getNumLineas(), "64");
				error = true;
			}
			else {
				cadena = palabra;
				cadena = cadena.replace("\'", "\"");
			}
		}
		tupla = new Tuple<String, String>(generado, cadena);
		if (!error) {
			imprimirToken(tupla);
		}
		else {
			escritura.escribir(ficheroSalida, fallo);
		}
		return tupla;
	}

	public Tuple<String, Integer> valor(String generado, String palabra) {
		Tokens tokens = new Tokens();
		String[] genera = tokens.getToken();
		int i = 0;
		int numero = 0;
		int digito = 0;
		Tuple<String, Integer> tupla;

		if (generado.equals(genera[12])) {
			for (i = 0; i < palabra.length(); i++) {
				digito = palabra.charAt(i)-48;
				numero = (int) (numero + (digito*Math.pow(11, palabra.length()-(i+1))));
			}
			if (numero>32767) {
				fallo = gestor.error("lexico01", lectura.getNumLineas(), "32767");
				error = true;
			}
		}
		else if (generado.equals(genera[14])) {
			if (identificadores.isEmpty()) {
				identificadores.add(palabra);
				numero = 0;
			}
			else {
				numero = identificadores.indexOf(palabra);
				if (numero==-1) {
					numero = identificadores.size();
					identificadores.add(palabra);
				}
			}
			if (!error) {
				sintactico.analizador(new Tuple<String, String> ("id", palabra));
			}
		}

		tupla = new Tuple<String, Integer>(generado, numero);
		if (!error) {
			imprimirNumero(tupla);
		}
		else {
			escritura.escribir(ficheroSalida, fallo);
		}
		return tupla;
	}

	public void imprimirToken(Tuple<String, String> tupla) {
		escritura.escribir(ficheroSalida, tupla.toString());
		escritura.escribir(ficheroSalida, "" + '\n');
	}

	public void imprimirNumero(Tuple<String, Integer> tupla) {
		escritura.escribir(ficheroSalida, tupla.toString());
		escritura.escribir(ficheroSalida, "" + '\n');
	}

}