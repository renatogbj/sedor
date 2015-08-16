<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@page import="br.com.sedor.odonto.ExpertSystem"%>
<%@page import="br.com.sedor.odonto.Attributes"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.io.File"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Sedor - Consulta</title>

<!-- Style -->
<link href="resource/css/main.css" rel="stylesheet" type="text/css">

<!-- Bootstrap -->
<link href="resource/css/jasny.bootstrap.css" rel="stylesheet"
	media="screen">

<!-- JQuery -->
<script type="text/javascript" src="resource/js/jquery-1.8.3.js"></script>
<script type="text/javascript" src="resource/js/bootstrap.min.js"></script>

<!-- Multiselect -->
<link rel="stylesheet" type="text/css"
	href="resource/css/jquery.multiselect.css" />
<link rel="stylesheet" type="text/css"
	href="http://ajax.googleapis.com/ajax/libs/jqueryui/1/themes/ui-lightness/jquery-ui.css" />
<script type="text/javascript" src="resource/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="resource/js/jquery.multiselect.js"></script>

<script type="text/javascript">
	$(function() {
		$("#field0").multiselect();
		$("#field1").multiselect();
		$("#field5").multiselect();
		$("#field4").multiselect();
	});

	$(".alert").alert();
	$().alert();

	function validate() {
		var qtd = document.getElementById("qtd").value;
		var i;
		for (i = 0; i < qtd; i++) {
			if (document.getElementById("field" + i).value == "") {
				$('html,body').animate({
					scrollTop : 0
				}, 'slow');
				if (document.getElementById("empty_field_alert").style.display == 'none') {
					document.getElementById("empty_field_alert").style.display = 'block';
				}
				return false;
			}
		}
	}
</script>
</head>
<body>
	<div id="page">
		<div class="row1"><%@ include file="header.jsp"%></div>

		<div class="row2 clear" style="padding-bottom: 60px;">
			<div class="span2" style="margin-left: 0px;"><%@ include
					file="menu.jsp"%>
			</div>
			<div class="span9">
				<div class="alert" style="display: none;" id="empty_field_alert">
					Por favor, preencha todos os valores! <a class="close"
						data-dismiss="alert" href="#">&times;</a>
				</div>
				<form class="bs-docs-example" action="diagnose" name="diagnose_form"
					method="POST" onsubmit="return validate();">

					<fieldset>
						<%
							String resource = new File(getServletContext().getRealPath(
									"resource")).getAbsolutePath();
							ExpertSystem es = ExpertSystem.getInstance(resource);
							Attributes attributes = es.getPattern().getAttributes();

							int n = 0;
							for (String att : attributes.getAttributes()) {
						%>
						<div class="span9">
							<legend style="display: block; margin-bottom: 5px;">
								<%
									char[] stringArray = ExpertSystem.engineToPrintable(att)
												.toCharArray();
										stringArray[0] = Character.toUpperCase(stringArray[0]);
										out.print(new String(stringArray));
								%>
							</legend>
							<%
								if (attributes.getType(att).equals("m")) {
							%>
							<select style="width: 350px;" multiple="multiple"
								id="field<%out.print(n);%>" name="<%out.print(att);%>">
								<%
									} else {
								%>
								<select name="<%out.print(att);%>" id="field<%out.print(n);%>">
									<%
										}
											String[] sortedValues = attributes.getValues(att);
											Arrays.sort(sortedValues);
											for (String val : sortedValues) {
									%>
									<option>
										<%
											stringArray = ExpertSystem.engineToPrintable(val)
															.toCharArray();
													stringArray[0] = Character.toUpperCase(stringArray[0]);
													out.print(new String(stringArray));
										%>
									</option>
									<%
										}
											n++;
									%>
							</select>
						</div>
						<br class="clear" /> <br />
						<%
							}
						%>
						<br class="clear" /> <br />
						<div class="span9">
							<button type="submit" class="btn btn-primary">Submit</button>
							<button type="reset" class="btn">Reset</button>
							<input type="hidden" id="qtd" value="<%out.print(n);%>" />
						</div>
					</fieldset>
				</form>
			</div>
			<div class="span2"></div>
			<div class="clear"></div>
		</div>

		<div class="row3 clear" id="footer"><%@ include
				file="footer.jsp"%></div>
	</div>
</body>
</html>