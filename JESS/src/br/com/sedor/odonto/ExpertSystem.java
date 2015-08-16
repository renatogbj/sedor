package br.com.sedor.odonto;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import jess.Fact;
import jess.JessException;
import jess.RU;
import jess.Rete;
import jess.Value;
import jess.ValueVector;
import br.com.sedor.rules.Writer;
import br.com.sedor.spreadsheet.Reader;

/**
 * 
 * @author Renato Borges
 * 
 */
public class ExpertSystem {

	private String resource;

	/** Singleton. */
	private static ExpertSystem instance;

	/** List of all the lesion's names. */
	private String[] lesions;

	/** The frequency of each lesion */
	HashMap<String, Double> frequency;

	/** The pattern of all lesions. */
	private Lesion pattern;

	/** The JESS engine */
	private Rete engine;

	/**
	 * Initialize the engine. Create the templates based on a pattern
	 * spreadsheet and the rules based on the lesions spreadsheet then finally
	 * batch them to the engine.
	 */
	public ExpertSystem() {
		this("/");
	}

	/**
	 * Initialize the engine. Create the templates based on a pattern
	 * spreadsheet and the rules based on the lesions spreadsheet then finally
	 * batch them to the engine.
	 * 
	 * Uses the location of project to get the resource directories.
	 */
	public ExpertSystem(String resource) {
		engine = new Rete();
		this.resource = resource;
		ini();
	}

	public void ini() {
		frequency = new HashMap<String, Double>();

		// get the list of files present in the directory "lesions"
		File fileLesions = new File(resource + File.separator + "lesions");
		fileLesions.mkdir();
		String[] temp = fileLesions.list();
		ArrayList<String> lesionNameTemp = new ArrayList<String>();

		File fileRules = new File(resource + File.separator + "rules");
		String[] rules = fileRules.list();
		for (int i = 0; i < rules.length; i++) {
			File fr = new File(resource + File.separator + "rules"
					+ File.separator + rules[i]);
			fr.delete();
			System.out.println(fr.getAbsolutePath());
		}
		fileRules.mkdir();

		for (int i = 0; i < temp.length; i++) {
			// avoid processing another kind of file different than a .csv file
			if (temp[i].substring(temp[i].length() - 3).equals(
					new String("csv"))) {
				lesionNameTemp.add(temp[i]);
			}
		}

		lesions = new String[lesionNameTemp.size()];
		for (int i = 0; i < lesions.length; i++) {
			lesions[i] = (String) lesionNameTemp.get(i);
			lesions[i] = lesions[i].substring(0, lesions[i].length() - 4);
		}

		writeTemplates();
		writeRules();
		try {
			batch();
		} catch (JessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * The singleton pattern. Make sure this class is instantiated only once.
	 * 
	 * @return
	 */
	public static synchronized ExpertSystem getInstance() {
		if (instance == null) {
			instance = new ExpertSystem();
		}
		return instance;
	}

	public static synchronized ExpertSystem getInstance(String resource) {
		if (instance == null) {
			instance = new ExpertSystem(resource);
		}
		return instance;
	}

	/**
	 * Return the pattern containing all possible attributes and their values.
	 * 
	 * @return The pattern is returned as a Lesion object.
	 */
	public Lesion getPattern() {
		return pattern;
	}

	public String[] getLesions() {
		return this.lesions;
	}

	/**
	 * Create and write the defined templates to a .clp file.
	 */
	private void writeTemplates() {
		Writer.createTemplateDiagnosis(lesions, resource);
		pattern = Reader.readPattern(resource + File.separator + "pattern.csv");
		Writer.createTemplateAttributes(pattern, resource);
	}

	/**
	 * Create and write the rules to a .clp file.
	 */
	private void writeRules() {
		for (int i = 0; i < lesions.length; i++) {
			Lesion lesion = Reader.readLesion(resource + File.separator
					+ "lesions" + File.separator + lesions[i] + ".csv");
			frequency.put(lesions[i], lesion.getProb());
			Writer.createRules(lesion, pattern, resource);
		}
	}

	public void removeLesion(String name) {
		File l = new File(resource + File.separator + "lesions"
				+ File.separator + name + ".csv");
		l.delete();
		try {
			clean();
			ini();
		} catch (JessException e) {
			System.out.println("Can't restart engine");
			e.printStackTrace();
		}

	}

	/**
	 * Batch the templates and rules files to the JESS engine.
	 * 
	 * @throws JessException
	 */
	private void batch() throws JessException {
		engine.clear();
		engine.batch(resource + File.separator + "template_attributes.clp");
		engine.batch(resource + File.separator + "template_diagnosis.clp");
		String[] file = new File(resource + File.separator + "rules").list();
		for (int i = 0; i < file.length; i++) {
			if (file[i].substring(file[i].length() - 3).equals(
					new String("clp"))) {
				engine.batch(resource + File.separator + "rules"
						+ File.separator + file[i]);
			}
		}
		engine.reset();
	}

	/**
	 * Create the attribute class based on a mapping of attribute names to a
	 * list of supported values, then assert it to the engine.
	 * 
	 * @param values
	 *            A mapping of an attribute to a list of values.
	 * @throws JessException
	 */
	public void assertValues(HashMap<String, ArrayList<String>> values)
			throws JessException {
		Attributes attribute = new Attributes();
		Set<String> set = values.keySet();
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			String attName = it.next();
			attribute.addAtributo(attName, 1.0, pattern.getAttributes()
					.getType(attName));
			ArrayList<String> val = values.get(attName);
			for (int i = 0; i < val.size(); i++) {
				attribute.addValue(attName, val.get(i), 1.0);
			}
		}
		assertFactToEngine(attribute);
	}

	/**
	 * Assert these attributes to the engine.
	 * 
	 * @param attributes
	 */
	public void assertValues(Attributes attributes) {
		assertFactToEngine(attributes);
	}

	/**
	 * Convert an Attribute class to a Fact class and assert it to the JESS
	 * engine.
	 * 
	 * @param a
	 * @throws JessException
	 */
	private void assertFactToEngine(Attributes a) {
		try {
			Fact f = new Fact("Atributos", engine);

			String[] attributeName = a.getAttributes();

			for (int i = 0; i < attributeName.length; i++) {
				if (a.getType(attributeName[i]).equals("u")) {
					String[] value = a.getValues(attributeName[i]);
					if (value.length > 1) {
						System.out
								.println("Atribute eh univalorado e deve conter apenas um valor.");
						System.exit(1);
					}
					f.setSlotValue(attributeName[i], new Value(value[0],
							RU.SYMBOL));
				} else {
					String[] value = a.getValues(attributeName[i]);
					ValueVector vv = new ValueVector();
					for (int j = 0; j < value.length; j++) {
						vv.add(new Value(value[j], RU.SYMBOL));
					}
					f.setSlotValue(attributeName[i], new Value(vv, RU.LIST));
				}
			}
			engine.assertFact(f);
		} catch (JessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Run the diagnostic.
	 */
	public void diagnose() {
		try {
			engine.run();
		} catch (JessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Return a string containing the results found.
	 * 
	 * @return The lesions and their probability.
	 * @throws JessException
	 */
	@SuppressWarnings("unchecked")
	public String result() throws JessException {
		String result = "";
		Iterator<Fact> it = engine.listFacts();
		while (it.hasNext()) {
			Fact f = it.next();
			if (f.getName().equals("MAIN::Diagnostico")) {
				Value lesion = f.getSlotValue("doenca");
				Value prob = f.getSlotValue("cnf");
				DecimalFormat deci = new DecimalFormat("0.0");
				String tmp = deci.format(Double.valueOf(prob.stringValue(engine
						.getGlobalContext())) * 100);
				result += lesion.stringValue(engine.getGlobalContext());
				result += ": " + tmp + "%\n";
			}
		}
		return result;
	}

	/**
	 * Return a string containing the results found.
	 * 
	 * @return The lesions and their probability.
	 * @throws JessException
	 */
	@SuppressWarnings("unchecked")
	public String httpResult() throws JessException {
		String result = "";
		Iterator<Fact> it = engine.listFacts();
		while (it.hasNext()) {
			Fact f = it.next();
			if (f.getName().equals("MAIN::Diagnostico")) {
				Value lesion = f.getSlotValue("doenca");
				Value prob = f.getSlotValue("cnf");
				DecimalFormat deci = new DecimalFormat("0.0");
				String tmp = deci.format(Double.valueOf(prob.stringValue(engine
						.getGlobalContext())) * 100);
				result += lesion.stringValue(engine.getGlobalContext());
				result += ": " + tmp + "%<br/>";
			}
		}
		return result;
	}

	public String sortedResult() throws JessException {
		ArrayList<Lesion> les = new ArrayList<Lesion>();
		Iterator<Fact> it = engine.listFacts();
		int count = 0;
		
		while (it.hasNext()) {
			Fact f = it.next();
			if (f.getName().equals("MAIN::Diagnostico")) {

				Value lesion = f.getSlotValue("doenca");
				Value prob = f.getSlotValue("cnf");

				double dProb = Double.valueOf(prob.stringValue(engine
						.getGlobalContext())) * 100;

				les.add(new Lesion(null, lesion.stringValue(engine
						.getGlobalContext()), dProb));
			}
		}
		Object[] l = les.toArray();
		String result = "";

		if (l == null || l.length == 0) {
			result = "Nenhuma lesão para calcular.<br/>Por favor adicione alguma lesão antes!";
			System.out.println(result);
		} else {
			Arrays.sort(l);

			for (Object i : l) {
				count++;
				Lesion il = (Lesion) i;
				DecimalFormat deci = new DecimalFormat("0.0");
				String name = il.getName();
				result += count + " - " + engineToPrintable(name) + ": "
						+ deci.format(il.getProb()) + "%<br/>";
			}
		}
		return result;
	}

	/**
	 * Print only the lesion with the high probability.
	 * 
	 * @return
	 * @throws JessException
	 */
	@SuppressWarnings("unchecked")
	public String bestResult() throws JessException {
		Iterator<Fact> it = engine.listFacts();
		String bestLesion = "";
		double bestCnf = -1;
		while (it.hasNext()) {
			Fact fact = it.next();
			if (fact.getName().equals("MAIN::Diagnostico")) {
				double cnf = Double.valueOf(fact.getSlotValue("cnf")
						.stringValue(engine.getGlobalContext()));
				if (cnf > bestCnf) {
					bestLesion = fact.getSlotValue("doenca").stringValue(
							engine.getGlobalContext());
					bestCnf = cnf;
				}
			}
		}
		DecimalFormat deci = new DecimalFormat("0.0");
		return bestLesion + ": " + deci.format(bestCnf * 100) + "%";
	}

	public String topResult(int n) throws JessException {
		ArrayList<Lesion> les = new ArrayList<Lesion>();
		Iterator<Fact> it = engine.listFacts();
		int count = 0;
		while (it.hasNext()) {
			Fact f = it.next();
			if (f.getName().equals("MAIN::Diagnostico")) {

				Value lesion = f.getSlotValue("doenca");
				Value prob = f.getSlotValue("cnf");

				double dProb = Double.valueOf(prob.stringValue(engine
						.getGlobalContext())) * 100;

				les.add(new Lesion(null, lesion.stringValue(engine
						.getGlobalContext()), dProb));
			}
		}
		Object[] l = les.toArray();
		Arrays.sort(l);
		Lesion[] top = new Lesion[n];
		for (int i = 0; i < n; i++) {
			top[i] = (Lesion) l[i];
			top[i].setProb(top[i].getProb() * frequency.get(top[i].getName()));
		}
		String result = "";
		Arrays.sort(top);
		for (Lesion il : top) {
			count++;
			DecimalFormat deci = new DecimalFormat("0.0");
			String name = il.getName();
			name = engineToPrintable(name);
			char[] stringArray = name.toCharArray();
			stringArray[0] = Character.toUpperCase(stringArray[0]);
			name = new String(stringArray);
			result += count + " - " + name + ": "
					+ deci.format(il.getProb()) + "%<br/>";
		}
		return result;
	}

	public void clean() throws JessException {
		engine.eval("(facts)");
		engine.reset();
	}

	public void facts() throws JessException {
		engine.eval("(facts)");
	}

	public static String printableToEngine(String str) {
		String pp = str;
		pp = pp.toUpperCase().replace(' ', '_');
		pp = pp.replace('(', '_');
		pp = pp.replace(')', '_');
		return pp;
	}

	public static String engineToPrintable(String str) {
		String pp = str.toLowerCase();
		char[] stringArray = pp.toCharArray();
		stringArray[0] = Character.toUpperCase(stringArray[0]);
		pp = new String(stringArray);
		pp = pp.replaceFirst("__", " (");
		if (pp.endsWith("_"))
			pp = pp.substring(0, pp.length() - 1) + ")";
		pp = pp.replaceAll("_", " ");
		pp = pp.replaceAll(" s ", "(s)");
		return pp;
	}
}
