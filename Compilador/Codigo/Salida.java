package compilador;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Salida {	
	private FileWriter escribir;
	private BufferedWriter buffer;
	private PrintWriter out;
	
	public Salida(File ficheroSalida) throws IOException {
		escribir = new FileWriter(ficheroSalida);
		buffer = new BufferedWriter(escribir);
		out = new PrintWriter(buffer);
	}
	public void escribir(File ficheroSalida, String palabra) {
		out.append(palabra);
	}

	public void cerrarDescriptor() {
		out.close();
	}
}