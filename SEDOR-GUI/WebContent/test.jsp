<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<%@page import="java.io.File"%>
<%@page import="br.com.sedor.test.SedorTest"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Sedor - Testing</title>

<!-- Style -->
<link href="resource/css/main.css" rel="stylesheet" type="text/css">

<!-- Bootstrap -->
<link href="resource/css/jasny.bootstrap.min.css" rel="stylesheet"
	media="screen">

<!-- JQuery -->
<script src="resource/js/jquery-1.8.3.js"></script>
<script src="resource/js/jasny.bootstrap.js"></script>

</head>
<body>
	<div id="page">
		<div class="row1"><%@ include file="header.jsp"%></div>

		<div class="row2 clear" style="padding-bottom: 60px;">
			<div class="span2" style="margin-left: 0px;"><%@ include
					file="menu.jsp"%></div>
			<div class="span9">

				<form action="test" method="POST" enctype="multipart/form-data">
					<legend>Adicionar Planilha de Teste</legend>
					<div class="fileupload fileupload-new left"
						data-provides="fileupload">
						<div class="input-append">
							<div class="uneditable-input span3">
								<i class="icon-file fileupload-exists"></i> <span
									class="fileupload-preview"></span>
							</div>
							<span class="btn btn-file"><span class="fileupload-new">Select
									file</span><span class="fileupload-exists">Change</span><input
								type="file" name="file" style="position: absolute;" /></span><a
								href="#" class="btn fileupload-exists" data-dismiss="fileupload">Remove</a>
						</div>
					</div>
					<button type="submit" class="left btn btn-primary"
						style="margin-left: 5px;">Submit</button>
				</form>
				<br class="clear" /> <br /> <br />
				<legend>Resultados</legend>
				<%
					String resource = new File(getServletContext().getRealPath(
							"resource")).getAbsolutePath();
					out.print(SedorTest.test(resource));
				%>
			</div>
			<div class="span2"></div>
			<div class="clear"></div>
		</div>
		<div class="row3" id="footer"><%@ include file="footer.jsp"%></div>
	</div>
</body>
</html>