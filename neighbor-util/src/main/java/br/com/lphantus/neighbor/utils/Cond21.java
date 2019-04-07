package br.com.lphantus.neighbor.utils;

import java.io.File;
import java.io.IOException;

import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;

public class Cond21 {

	public static void main(String[] args) throws IOException {
		Database db = DatabaseBuilder.open(new File("/home/elias/Trabalho/Condominio21/Cond21.mdb"));
		System.out.println(db.getDatabasePassword());
	}

}
