package br.com.sedor.test;

import java.io.File;

import jess.JessException;
import br.com.sedor.odonto.ExpertSystem;
import br.com.sedor.odonto.Lesion;
import br.com.sedor.spreadsheet.Reader;

public class SedorTest {

	public static String test(String resource) throws JessException {
		// set up directory "test"
		String path = resource + File.separator + "test";
		File test = new File(path);
		test.mkdir();

		// get the list of files in the directory "test"
		String[] files = new File(path).list();

		if (files == null || files.length == 0) {
			System.out.println("Couldn't find any file to test!");
			System.out.println("Oh god!");
			return null;
		}

		ExpertSystem es = ExpertSystem.getInstance(resource);

		String out = "";
		int total = files.length;
		int correct = 0;

		for (String f : files) {
			Lesion lesion = Reader.readTest(resource + File.separator + "test"
					+ File.separator + f);

			es.assertValues(lesion.getAttributes());
			es.diagnose();

			String result = "";
			String waited = "";

			try {
//				result = ExpertSystem.engineToPrintable(es.topResult(5));
				result = es.topResult(5);
				waited = ExpertSystem.engineToPrintable(lesion.getName());
				System.out.println("Esperado: " + waited);
				System.out.println("Diagnosticado: " + result);
				System.out.println();
			} catch (ArrayIndexOutOfBoundsException e) {
				result = ExpertSystem.engineToPrintable(es.sortedResult());
				waited = ExpertSystem.engineToPrintable(lesion.getName());
				System.out.println("Esperado: " + waited);
				System.out.println("Diagnosticado: " + result);
				System.out.println();
			}

			es.clean();

			if (result.contains(waited)) {
				correct++;
			}

			out += "Esperado: " + waited + "<br/>";
			out += "Diagnosticado:<br/>" + result + "<br/><br/>";
		}
		return out + "<br/>Taxa de acerto: " + (correct * 100 / total) + "% ("
				+ correct + "/" + total + ")";
	}
}
