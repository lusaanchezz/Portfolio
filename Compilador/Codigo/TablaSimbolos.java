package compilador;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TablaSimbolos {
	private File ficheroTabla;
	private Salida escritura;
	private ArrayList<String> lexemas;
	private ArrayList<String> identificadores;
	private int numero;
	private ArrayList<String>[] tablaSimbolosGlobal = new ArrayList[6];
	private ArrayList<String>[] tablaSimbolosLocal= new ArrayList[3];
	private int despG;
	private int despL;
	private int TSActual;

	//private ArrayList<String> lexemasNuevo;
	//private ArrayList<String> identificadoresNuevo;
	//private int desplazamientoNuevo;

	public TablaSimbolos() throws IOException {
		ficheroTabla = new File("C:\\Users\\lucia\\OneDrive\\Escritorio\\UPM\\3º\\2º Cuatri\\PDL\\tablaSimbolos.txt");
		escritura = new Salida(ficheroTabla);
	}

	//TSG
	public void crearTS() {
		TSActual = 0;
		tablaSimbolosGlobal[0] = new ArrayList<String>(); // Lexema
		tablaSimbolosGlobal[1] = new ArrayList<String>(); // Tipo
		tablaSimbolosGlobal[2] = new ArrayList<String>(); // Desplazamiento
		tablaSimbolosGlobal[3] = new ArrayList<String>(); // NumeroParametros
		tablaSimbolosGlobal[4] = new ArrayList<String>(); // TipoParametros
		tablaSimbolosGlobal[5] = new ArrayList<String>(); // TipoRetorno
		despG = 0;	
		lineaTabla();
	}

	//TSL
	public void crearTS(String nombreTabla) {
		TSActual = 1;
		tablaSimbolosLocal[0] = new ArrayList<String>(); // Lexema
		tablaSimbolosLocal[1] = new ArrayList<String>(); // Tipo
		tablaSimbolosLocal[2] = new ArrayList<String>(); // Desplazamiento
		despL = 0;	
		lineaTabla();
	}

	public void insertarDesplazamiento(int desplazamiento) {
		if (TSActual==0) {
			tablaSimbolosGlobal[2].add(tablaSimbolosGlobal[2].size(), "" + despG);
			despG = despG + desplazamiento;
		}
		else {
			tablaSimbolosLocal[2].add(tablaSimbolosLocal[2].size(), "" + despL);
			despL = despL + desplazamiento;
		}
	}

	public String buscarDesplazamiento(int posicion) {
		return tablaSimbolosGlobal[2].get(posicion);
	}

	public boolean insertarLexema(String lexema) {
		boolean esta = true;

		if (TSActual==0) {
			if (tablaSimbolosGlobal[0].isEmpty()) {
				esta = false;
				tablaSimbolosGlobal[0].add(0, lexema);
			}
			else {
				if (!tablaSimbolosGlobal[0].contains(lexema)) {
					esta = false;
					tablaSimbolosGlobal[0].add(tablaSimbolosGlobal[0].size(), lexema);
				}
			}
		}
		else {
			if (tablaSimbolosLocal[0].isEmpty()) {
				esta = false;
				tablaSimbolosLocal[0].add(0, lexema);
			}
			else {
				if (!tablaSimbolosLocal[0].contains(lexema)) {
					esta = false;
					tablaSimbolosLocal[0].add(tablaSimbolosLocal[0].size(), lexema);

				}
			}
		}
		return esta;
	}

	public void insertarTipo(String tipo) {
		if (TSActual == 0) {
			tablaSimbolosGlobal[1].add(tablaSimbolosGlobal[1].size(), tipo);
		}
		else {
			tablaSimbolosLocal[1].add(tablaSimbolosLocal[1].size(), tipo);
		}
	}

	public void insertarNumParam(int numParam) {
		tablaSimbolosGlobal[3].add(tablaSimbolosGlobal[3].size(), "" + numParam);
	}

	public int buscarNumeroParametros(int posicion) {
		return Integer.parseInt(tablaSimbolosGlobal[3].get(posicion));
	}

	public void insertarTipoParametros(String tipo) {
		tablaSimbolosGlobal[4].add(tablaSimbolosGlobal[4].size(), tipo);
	}

	public String buscarTipoParametros(int posicion) {
		return tablaSimbolosGlobal[4].get(posicion);
	}

	public void insertarTipoRet(String tipoRet) {
		tablaSimbolosGlobal[5].add(tablaSimbolosGlobal[5].size(), tipoRet);
	}
	//insertarnumparametros, buscarnumparametros, insertartiporetorno
	public String buscarTipoRetorno(int posicion) {
		return tablaSimbolosGlobal[5].get(posicion);
	}
	public void destruirTS() {
		tablaSimbolosGlobal[0].clear();
		tablaSimbolosGlobal[1].clear();
		tablaSimbolosGlobal[2].clear();
		tablaSimbolosGlobal[3].clear();
		tablaSimbolosGlobal[4].clear();
		tablaSimbolosGlobal[5].clear();
		despG = 0;
	}

	public void destruirTS(String nombreTabla) {
		tablaSimbolosLocal[0].clear();
		tablaSimbolosLocal[1].clear();
		tablaSimbolosLocal[2].clear();
		despL = 0;
		TSActual = 0;
	}

	public int buscaPosicion(String id) {
		int posicion = -1;

		if (TSActual==0) {
			posicion = tablaSimbolosGlobal[0].indexOf(id);
		}

		else {
			posicion = tablaSimbolosLocal[0].indexOf(id);
		}

		return posicion;
	}

	public String buscaTipo(int posicion) {
		String tipo;

		if (TSActual==0) {
			tipo = tablaSimbolosGlobal[1].get(posicion);
		}
		else {
			tipo = tablaSimbolosLocal[1].get(posicion);
		}

		return tipo;
	}

	public void lineaTabla () {
		escritura.escribir(ficheroTabla, "CONTENIDOS DE LA TABLA #" + numero + ": " + '\n');
		numero++;
	}
	
	public void lineaLexema(String lexema) {
		String resultado = "";
		
		resultado = "* LEXEMA : '" + lexema + "'\n  ATRIBUTOS :\n";
		escritura.escribir(ficheroTabla, resultado);	
	}

	public void lineaAtributos(int posicion) {
		if (TSActual == 0) {
			escritura.escribir(ficheroTabla, "+ tipo : " + buscaTipo(posicion) + '\n' + "+ desplazamiento : " +
					buscarDesplazamiento(posicion) + '\n' + "+ numeroParametros : " + buscarNumeroParametros(posicion) + 
					'\n' + "+ tipoParametros : " + buscarTipoParametros(posicion) + '\n' + "+ tipoRetorno : " + 
					buscarTipoRetorno(posicion) + '\n');
			escritura.escribir(ficheroTabla, "--------- ----------" + '\n');
		} else {
			escritura.escribir(ficheroTabla, "+ tipo : " + buscaTipo(posicion) + '\n' + "+ desplazamiento : " + despL + '\n');
			escritura.escribir(ficheroTabla, "--------- ----------" + '\n');
		}
	}

	public void cierre() {
		escritura.cerrarDescriptor();
	}
}