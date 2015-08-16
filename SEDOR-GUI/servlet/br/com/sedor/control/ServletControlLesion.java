package br.com.sedor.control;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jess.JessException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import br.com.sedor.odonto.ExpertSystem;

/**
 * Servlet implementation class ServletControlLesion
 */
@WebServlet("/ServletControlLesion")
public class ServletControlLesion extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServletControlLesion() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		try {
			List<FileItem> items = new ServletFileUpload(
					new DiskFileItemFactory()).parseRequest(request);
			for (FileItem item : items) {
				if (item.isFormField()) {
					System.out.println("regular field");
					// Process regular form field (input
					// type="text|radio|checkbox|etc", select, etc).
					// String fieldname = item.getFieldName();
					// String fieldvalue = item.getString();
					// ... (do your job here)
				} else {
					System.out.println("file field");
					// Process form file field (input type="file").
					// String fieldname = item.getFieldName();
					String filename = FilenameUtils.getName(item.getName());
					InputStream filecontent = item.getInputStream();

					File resource = new File(getServletContext().getRealPath(
							"resource"));

					resource.mkdir();

					System.out.println(resource.getAbsolutePath());

					IOUtils.copy(filecontent, new FileOutputStream(resource
							+ File.separator + "lesions" + File.separator
							+ filename));
					// ... (do your job here)

					System.out.println("Arquivo salvo em: " + resource
							+ File.separator + "lesions" + File.separator
							+ filename);

					ExpertSystem es = ExpertSystem.getInstance(resource
							.getAbsolutePath());
					try {
						es.clean();
						es.ini();
					} catch (JessException e) {
						e.printStackTrace();
					}

					request.getSession().setAttribute("sucesso", "true");
				}
			}
		} catch (FileUploadException e) {
			throw new ServletException("Cannot parse multipart request.", e);
		}
		request.getRequestDispatcher("add_lesion.jsp").forward(request,
				response);
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
