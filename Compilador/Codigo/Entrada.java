package compilador;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Entrada {
	private File ficheroEntrada;
	private FileReader lectura;
	private long longitud;
	private int n = 0;
	private int numLineas = 1;

	public Entrada() throws IOException {
		ficheroEntrada = new File("C:\\Users\\lucia\\OneDrive\\Escritorio\\UPM\\3º\\2º Cuatri\\PDL\\entrada.txt");
		lectura = new FileReader(ficheroEntrada);
		longitud = ficheroEntrada.length();
		//gestorErrores = new GestorErrores();
	}

	int caracter;
	String cadena;

	public boolean caracteresSeparados(int caracter) {
		boolean comprueba = false;

		if (caracter=='('||caracter==')'||caracter=='{'||
				caracter=='}'||caracter==';'||caracter=='='||
				caracter==','||caracter=='-'||
				caracter=='/'||caracter=='>') {
			comprueba = true;
		}
		return comprueba;
	}
	
	public String leer() throws IOException {
		boolean comentario = false;

		if (n==0) {
			caracter = lectura.read();
			n++;
		}
		cadena = "";

		while (caracter != ' ' && caracter != '\n' && caracter!='\t' && caracter != 13 && !caracteresSeparados(caracter) 
				&& caracter!=-1 && !cadena.equals("&&") && caracter != 39) {
			if (!caracteresSeparados(caracter)) {
				cadena = cadena + (char)caracter;
				caracter = lectura.read();
				setLongitud(1);
			}
			else {

			}
			//setLongitud(1);
		}

		if (caracteresSeparados(caracter)) {

			if (cadena.equals("")) {
				cadena = cadena + (char)caracter;
				caracter = lectura.read();
				setLongitud(1);
			}
			if (cadena.equals("-")&&caracter=='-') {
				cadena = cadena + (char)caracter;
				caracter = lectura.read();
				setLongitud(1);
			}
			else if (cadena.equals("/")) {
				//setLongitud(1);
				if (caracter=='*') {
					cadena = "";
					comentario = true;
					while(comentario) {
						caracter = lectura.read();
						setLongitud(1);
						if(caracter=='*') {
							caracter = lectura.read();

							setLongitud(1);
							if (caracter=='/') {
								caracter = lectura.read();			
								setLongitud(1);
								comentario = false;
						
							}
						}
					} 

				}
			}
		}

		while (caracter == ' ' || caracter == '\n' || caracter == '\t' || caracter == 13) {
			caracter = lectura.read();

			setLongitud(1);
			if (cadena.length()==0 && caracter!=' ' && caracter!='\n' && caracter!='\t' && caracter != 13) {
					cadena = cadena + (char) caracter;
					caracter = lectura.read();
					setLongitud(1);
					while (caracter != ' ' && caracter != '\n' && caracter != '\t' && caracter != 13 && !caracteresSeparados(caracter) 
							&& caracter!=-1 && !cadena.equals("&&") && caracter != 39) {
						if (!caracteresSeparados(caracter)) {
							cadena = cadena + (char)caracter;
							caracter = lectura.read();
							setLongitud(1);
						}
					}
				}
			
			if (caracter == '\n') {
				numLineas++;
			}
		}

		if (caracter==39 && cadena.length()==0) {
			cadena = "'";
			caracter = lectura.read();
			setLongitud(1);
		
			while(caracter!=39) {
				cadena = cadena + (char) caracter;
				caracter = lectura.read();
				setLongitud(1);
			}
			cadena = cadena + "'";
			caracter = lectura.read();
			setLongitud(1);
		}


		if (!existe(caracter) && cadena.length()==0) {
			cadena = cadena + (char) caracter;
			caracter = lectura.read();
			setLongitud(1);
		}

		return cadena;
	}

	public boolean existe(int caracter) {
		boolean resultado = true;
		if (!Character.isLetterOrDigit(caracter) && !caracteresSeparados(caracter) && caracter != 39 && 
				caracter != ' ' && caracter != '\n' && caracter != 13 && caracter != '&' && caracter!=-1)  {
			resultado = false;
		}		
		return resultado;
	}

	public long getLongitud() {
		return this.longitud;
	}

	public void setLongitud(int tamano) {
		this.longitud = longitud - tamano;
	}

	public int getNumLineas() {
		return numLineas;
	}

	/*public GestorErrores getGestor() {
		return gestorErrores;
	}*/
}