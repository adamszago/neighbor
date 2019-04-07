package br.com.lphantus.neighbor.util;

import java.util.HashSet;
import java.util.Set;

public class TotemUtil {

	private static final Set<String> conjuntoSenhas = new HashSet<String>();
	public static final int MAX_SENHA = 10000;

	static {
		for(int i = 0; i < MAX_SENHA; ++i){
			conjuntoSenhas.add(String.format("%04d", i));
		}
	}
	
	public static Set<String> obterConjuntoSenhas(){
		final HashSet<String> retorno = new HashSet<String>();
		retorno.addAll(conjuntoSenhas);
		return retorno;
	}


}
