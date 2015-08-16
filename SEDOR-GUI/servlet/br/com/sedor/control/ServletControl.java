package br.com.sedor.control;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jess.JessException;
import br.com.sedor.odonto.Attributes;
import br.com.sedor.odonto.ExpertSystem;

/**
 * Servlet implementation class ServletControl
 */
@WebServlet("/ServletControl")
public class ServletControl extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public ServletControl() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// get the path to the resource of the project
		String resource = new File(getServletContext().getRealPath("resource"))
				.getAbsolutePath();

		// get the current instance of the Expert System
		ExpertSystem es = ExpertSystem.getInstance(resource);

		Attributes pattern = es.getPattern().getAttributes();
		try {
			es.facts();
		} catch (JessException e1) {
			e1.printStackTrace();
		}

		Enumeration<String> attName = request.getParameterNames();

		HashMap<String, ArrayList<String>> attributes = new HashMap<String, ArrayList<String>>();

		while (attName.hasMoreElements()) {
			String att = attName.nextElement();
			ArrayList<String> values = new ArrayList<String>();

			if (pattern.getType(att).equals("m")) {
				String[] val = request.getParameterValues(att);

				for (String s : val) {
					values.add(ExpertSystem.printableToEngine(s));
				}
			} else {
				String val = request.getParameter(att);
				values.add(ExpertSystem.printableToEngine(val));
			}

			attributes.put(att, values);
		}

		try {
			es.assertValues(attributes);
			es.diagnose();
//			String out = ExpertSystem.engineToPrintable(es.topResult(5));
			String out = es.topResult(5);
			String best = ExpertSystem.engineToPrintable(es.bestResult());
			request.setAttribute("diagResponse", out);
			request.setAttribute("bestDiagResponse", best);
			request.setAttribute("attributes", attributes);

			es.clean();

			request.getRequestDispatcher("result_diagnose.jsp").forward(
					request, response);
		} catch (JessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
