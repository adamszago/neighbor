package br.com.lphantus.neighbor.utils;

import org.springframework.util.StringUtils;

//import com.sun.xml.internal.ws.util.StringUtils;  

public class Capitalize {

	// "Capitalização" ingÃªnua - só converte a primeira inicial para
	// maiÃºsculas.
	public static String capitalizePrimeira(final String str) {
		char[] chars = str.toCharArray();
		if (chars.length >= 1)
			chars[0] = Character.toUpperCase(chars[0]);
		return new String(chars);
	}

	// Esta é uma normalização mais completa, mas pode ter alguns problemas
	// também.
	// Um deles, como vocÃª deve perceber, é que ela não preserva os espaços.
	// Outra é que certas partículas nos nomes serão também passadas para
	// maiÃºsculas - não é esquisito "João Da Silva"?
	// Para tanto, vocÃª pode alterar "ret.append (capitalize (partes[i])" para
	// checar
	// se a partícula deve ou não ser capitalizada.
	/*
	 * public static String capitalize (final String str) { String[] partes =
	 * str.split ("\\s+"); StringBuilder ret = new StringBuilder (str.length());
	 * for (int i = 0; i < partes.length; ++i) { if (i != 0) ret.append (" ");
	 * ret.append (capitalize (partes[i])); } return ret.toString(); }
	 */

	public static String capitalizeAll(String s) {
		if (s != null) {
			String newString = s.toLowerCase();
			String[] array = newString.split(" ");
			newString = "";
			for (int i = 0; i < array.length; i++) {
				if (array[i].length() > 2) {
					newString += StringUtils.capitalize(array[i]) + " ";
				} else {
					newString += array[i] + " ";
				}
			}
			s = newString.trim();
		}
		return s;
	}

	public static void main(String[] args) {
		System.out.println(capitalizePrimeira("java"));
		System.out.println(capitalizeAll("leanDRO anjOs do nASCImento"));
	}
}