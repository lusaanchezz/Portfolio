package compilador;

import java.io.File;
import java.io.IOException;

public class GestorErrores {
	private File ficheroSalidaError;
	private Salida salida;

	public GestorErrores() throws IOException {
		ficheroSalidaError = new File("C:\\Users\\lucia\\OneDrive\\Escritorio\\UPM\\3º\\2º Cuatri\\PDL\\gestorErrores.txt");
		salida = new Salida(ficheroSalidaError);
	}

	public String error(String tipoError, int numLinea, String cadena) {

		String error = "";

		switch(tipoError) {
		case "lexico01":
			error = ("Error en linea " + numLinea + ". Numero fuera de rango. El mayor numero procesable es '32767'\n");
			salida.escribir(ficheroSalidaError, error);
			break;
		case "lexico02":
			error = ("Error en linea " + numLinea + ". El simbolo '" + cadena + "' no esta reconocido en el lenguaje\n");
			salida.escribir(ficheroSalidaError, error);
			break;
		case "lexico03":
			error = ("Error en linea " + numLinea + ". Cadena fuera de rango. Como maximo puede tener " + cadena + " caracteres\n");
			salida.escribir(ficheroSalidaError, error);
			break;
		case "sintactico01":
			error = ("Error en linea " + numLinea + cadena + "\n");
			//System.out.println(error);
			salida.escribir(ficheroSalidaError, error);
			break;
		case "sintactico02":
			error = ("Error en linea " + numLinea + ". " + cadena + '\n');
			//System.out.println(error);
			salida.escribir(ficheroSalidaError, error);
			break;
		case "semantico01":
			error = ("Error en linea " + numLinea + ". " + cadena + "\n");
			salida.escribir(ficheroSalidaError, error);
			break;
		case "semantico02":
			error = ("Error en linea " + numLinea + ". " + cadena + "\n");
			salida.escribir(ficheroSalidaError, error);
			break;
		case "semantico03":
			error = ("Error en linea " + numLinea + ". " + cadena + "\n");
			salida.escribir(ficheroSalidaError, error);
			break;
		case "semantico04":
			error = ("Error en linea " + numLinea + ". " + cadena + "\n");
			salida.escribir(ficheroSalidaError, error);
			break;
		case "semantico05":
			error = ("Error en linea " + numLinea + ". " + cadena + "\n");
			salida.escribir(ficheroSalidaError, error);
			break;
		case "semantico06":
			error = ("Error en linea " + numLinea + ". " + cadena + "\n");
			salida.escribir(ficheroSalidaError, error);
			break;
		case "semantico07":
			error = ("Error en linea " + numLinea + ". " + cadena + "\n");
			salida.escribir(ficheroSalidaError, error);
			break;
		case "semantico08":
			error = ("Error en linea " + numLinea + ". " + cadena + "\n");
			salida.escribir(ficheroSalidaError, error);
			break;
		case "semantico09":
			error = ("Error en linea " + numLinea + ". " + cadena + "\n");
			salida.escribir(ficheroSalidaError, error);
			break;
		case "semantico10":
			error = ("Error en linea " + numLinea + ". " + cadena + "\n");
			salida.escribir(ficheroSalidaError, error);
			break;
		case "semantico11":
			error = ("Error en linea " + numLinea + ". " + cadena + "\n");
			salida.escribir(ficheroSalidaError, error);
			break;
		case "semantico12":
			error = ("Error en linea " + numLinea + ". " + cadena + "\n");
			salida.escribir(ficheroSalidaError, error);
			break;
		case "semantico13":
			error = ("Error en linea " + numLinea + ". " + cadena + "\n");
			salida.escribir(ficheroSalidaError, error);
			break;
		case "semantico14":
			error = ("Error en linea " + numLinea + ". " + cadena + "\n");
			salida.escribir(ficheroSalidaError, error);
			break;
		case "semantico15":
			error = ("Error en linea " + numLinea + ". " + cadena + "\n");
			salida.escribir(ficheroSalidaError, error);
			break;
		case "semantico16":
			error = ("Error en linea " + numLinea + ". " + cadena + "\n");
			salida.escribir(ficheroSalidaError, error);
			break;
		case "semantico17":
			error = ("Error en linea " + numLinea + ". " + cadena + "\n");
			salida.escribir(ficheroSalidaError, error);
			break;
		case "semantico18":
			error = ("Error en linea " + numLinea + ". " + cadena + "\n");
			salida.escribir(ficheroSalidaError, error);
			break;
		case "semantico19":
			error = ("Error en linea " + numLinea + ". " + cadena + "\n");
			salida.escribir(ficheroSalidaError, error);
			break;
		case "semantico20":
			error = ("Error en linea " + numLinea + ". " + cadena + "\n");
			salida.escribir(ficheroSalidaError, error);
			break;
		case "semantico21":
			error = ("Error en linea " + numLinea + ". " + cadena + "\n");
			salida.escribir(ficheroSalidaError, error);
			break;
		}        

		return error;
	}
	public void cierre() {
		salida.cerrarDescriptor();
	}
}