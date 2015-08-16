package br.com.sedor.rules;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import br.com.sedor.odonto.Attributes;
import br.com.sedor.odonto.Lesion;

public class Writer {

	public static void createTemplateAttributes(Lesion pattern, String resource) {

		String templateAttributes = "(deftemplate Atributos";
		Attributes attributes = pattern.getAttributes();
		String[] listAttributes = attributes.getAttributes();

		for (int i = 0; i < listAttributes.length; i++) {

			String[] listValues = attributes.getValues(listAttributes[i]);
			String type = attributes.getType(listAttributes[i]);

			if (type.equals("u")) {
				templateAttributes += " (slot ";
			} else if (type.equals("m")) {
				templateAttributes += " (multislot ";
			}

			templateAttributes += listAttributes[i]
					+ " (type SYMBOL) (allowed-values";

			for (int j = 0; j < listValues.length; j++) {
				templateAttributes += " " + listValues[j];
			}
			templateAttributes += "))";
		}
		templateAttributes += ")";

		write(templateAttributes, resource + File.separator
				+ "template_attributes.clp");
	}

	public static void createTemplateDiagnosis(String[] lesions, String resource) {
		String template = "(deftemplate Diagnostico (slot doenca (type SYMBOL) (allowed-values";
		String deffact = "(deffacts inicia_diagnostico";
		for (int i = 0; i < lesions.length; i++) {
			template += " " + lesions[i];
			deffact += " (Diagnostico (doenca " + lesions[i] + ") (cnf 0.0))";
		}
		template += ")) (slot cnf (type FLOAT) (default 0.0)))\n\n";
		deffact += ")";
		write(template + deffact, resource + File.separator
				+ "template_diagnosis.clp");
	}

	public static void createRules(Lesion lesion, Lesion pattern,
			String resource) {
		int ruleNumber = 0;
		String rule = "";

		Attributes attributes = lesion.getAttributes();
		String[] nameAttributes = attributes.getAttributes();

		for (int i = 0; i < nameAttributes.length; i++) {
			String[] values = attributes.getValues(nameAttributes[i]);
			for (int j = 0; j < values.length; j++) {
				if (attributes.getProbValue(nameAttributes[i], values[j]) != 0) {
					if (pattern.getAttributes().getType(nameAttributes[i])
							.equals("u")) {
						String nameRule = lesion.getName() + ruleNumber;
						rule += "(defrule "
								+ nameRule
								+ "\n(Atributos ("
								+ nameAttributes[i]
								+ " "
								+ values[j]
								+ "))\n?diag <- (Diagnostico (doenca "
								+ lesion.getName()
								+ ") (cnf ?cnf))\n(not ("
								+ nameRule
								+ "))\n=>\n(bind ?novo_cnf (+ ?cnf "
								+ attributes.getProbValue(nameAttributes[i],
										values[j])
								+ "))\n(retract ?diag)\n(assert (Diagnostico (doenca "
								+ lesion.getName()
								+ ") (cnf ?novo_cnf)))\n(assert (" + nameRule
								+ ")))\n\n";
						ruleNumber++;
					} else {
						String nameRule = lesion.getName() + ruleNumber;
						rule += "(defrule "
								+ nameRule
								+ "\n(Atributos ("
								+ nameAttributes[i]
								+ " $? "
								+ values[j]
								+ " $?))\n?diag <- (Diagnostico (doenca "
								+ lesion.getName()
								+ ") (cnf ?cnf))\n(not ("
								+ nameRule
								+ "))\n=>\n(bind ?novo_cnf (+ ?cnf "
								+ attributes.getProbValue(nameAttributes[i],
										values[j])
								+ "))\n(retract ?diag)\n(assert (Diagnostico (doenca "
								+ lesion.getName()
								+ ") (cnf ?novo_cnf)))\n(assert (" + nameRule
								+ ")))\n\n";
						ruleNumber++;
					}
				}
			}
		}
		write(rule, resource + File.separator + "rules" + File.separator
				+ "rules_" + lesion.getName() + ".clp");
	}

	private static void write(String str, String file) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			out.write(str);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
