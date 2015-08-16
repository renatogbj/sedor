package br.com.sedor.spreadsheet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import br.com.sedor.odonto.Attributes;
import br.com.sedor.odonto.ExpertSystem;
import br.com.sedor.odonto.Lesion;

public class Reader {

	public static Lesion readPattern(String file) {
		String nameLesion = "Modelo";
		Double probLesion = 0.0;
		Attributes attributes = null;
		Scanner read;
		try {
			attributes = new Attributes();
			read = new Scanner(new FileInputStream(file));

			// le o arquivo inteiro
			ArrayList<String> lines = new ArrayList<String>();
			while (read.hasNext()) {
				lines.add(read.nextLine());
			}

			String last = null;

			for (int i = 2; i < lines.size(); i++) {
				String[] column = lines.get(i).split(";");

				String nameAttributes = column[0];

				if (!nameAttributes.equals(last)) {

					Double probAttribute = Double.valueOf(column[2]);
					String typeAttribute = column[6];

					attributes.addAtributo(nameAttributes, probAttribute,
							typeAttribute);

					String nameValue = ExpertSystem.printableToEngine(column[3]);
					Double probValue = 0.0;

					attributes.addValue(nameAttributes, nameValue, probValue);

					last = "" + nameAttributes;

				} else {
					String nameValue = ExpertSystem.printableToEngine(column[3]);
					Double probValue = 0.0;

					attributes.addValue(nameAttributes, nameValue, probValue);
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return new Lesion(attributes, nameLesion, probLesion);
	}

	public static Lesion readLesion(String file) {
		Attributes attributes = null;
		String nameLesion = null;
		Double probLesion = null;
//		Double frequencyLesion = null;
		Scanner read;

		try {
			attributes = new Attributes();

			// primeiro le o modelo para determinar os atributos e valores
			read = new Scanner(new FileInputStream(file));

			// le o arquivo inteiro
			ArrayList<String> lines = new ArrayList<String>();
			while (read.hasNext()) {
				lines.add(read.nextLine());
			}

			// primeira linha: nome e probabilidade da lesao
			String[] column = lines.get(0).split(";");
			nameLesion = column[0];
			probLesion = Double.valueOf(column[1]);

			String last = null;

			for (int i = 2; i < lines.size(); i++) {
				column = lines.get(i).split(";");

				String nameAttribute = column[0];

				if (!nameAttribute.equals(last)) {

					String typeAttribute = "";
					Double probAttribute = Double.valueOf(column[2]);

					attributes.addAtributo(nameAttribute, probAttribute,
							typeAttribute);

					String nameValue = ExpertSystem.printableToEngine(column[3]);
					Double probValue = Double.valueOf(column[5]);

					attributes.addValue(nameAttribute, nameValue, probValue);

					last = "" + nameAttribute;

				} else {
					String nameValue = ExpertSystem.printableToEngine(column[3]);
					Double probValue = Double.valueOf(column[5]);

					attributes.addValue(nameAttribute, nameValue, probValue);
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return new Lesion(attributes, nameLesion, probLesion);
	}

	public static Lesion readTest(String file) {
		Attributes attributes = null;
		String nameLesion = null;
		Double probLesion = null;
		Scanner read;

		try {
			attributes = new Attributes();
			read = new Scanner(new FileInputStream(file));

			// le o arquivo inteiro
			ArrayList<String> lines = new ArrayList<String>();
			while (read.hasNext()) {
				lines.add(read.nextLine());
			}

			String[] column = lines.get(0).split(";");
			nameLesion = column[0];

			for (int i = 1; i < lines.size(); i++) {
				column = lines.get(i).split(";");
				String nameAttribute = column[0];
				
				if (column.length > 2)
					attributes.addAtributo(nameAttribute, 1.0, "m");
				else
					attributes.addAtributo(nameAttribute, 1.0, "u");
				
				for (int j = 1; j < column.length; j++) {
					String nameValue = ExpertSystem.printableToEngine(column[j]);
					attributes.addValue(nameAttribute, nameValue, 1.0);
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return new Lesion(attributes, nameLesion, probLesion);
	}
}
