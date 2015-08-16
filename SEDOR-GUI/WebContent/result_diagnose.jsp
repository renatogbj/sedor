<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="br.com.sedor.odonto.ExpertSystem"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="resource/css/main.css" rel="stylesheet" type="text/css">
<!-- Bootstrap -->
<link href="resource/css/bootstrap.min.css" rel="stylesheet"
	media="screen">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Diagnose</title>
</head>
<body>
	<div id="page">
		<div class="row1"><%@ include file="header.jsp"%></div>

		<div class="row2 clear" style="padding-bottom: 60px;">
			<div class="span2" style="margin-left: 0px;"><%@ include
					file="menu.jsp"%></div>
			<div class="span9">
				<legend>Diagnóstico</legend>
				${ diagResponse } <br />
				<legend>Sintomas</legend>
				<%
					HashMap<String, ArrayList<String>> symp = (HashMap) request
							.getAttribute("attributes");
					Set<String> att = symp.keySet();
					Iterator<String> it = att.iterator();
					while (it.hasNext()) {
						String atName = it.next();
				%><h4>
					<%
						char[] stringArray = ExpertSystem.engineToPrintable(atName)
									.toCharArray();
							stringArray[0] = Character.toUpperCase(stringArray[0]);
							out.print(new String(stringArray));
					%>
				</h4>
				<ul>
					<%
						ArrayList<String> values = symp.get(atName);
							for (int i = 0; i < values.size(); i++) {
					%><li>
						<%
							stringArray = ExpertSystem.engineToPrintable(values.get(i))
											.toCharArray();
									stringArray[0] = Character.toUpperCase(stringArray[0]);
									out.print(new String(stringArray));
						%> </li><%
 	}
 %>
					</ul>
				<%
					}
				%>

			</div>
			<div class="span2"></div>
			<div class="clear"></div>
		</div>
		<div class="row3" id="footer"><%@ include file="footer.jsp"%></div>
	</div>
	<script src="http://code.jquery.com/jquery-latest.js"></script>
	<script src="resource/js/bootstrap.min.js"></script>
</body>
</html>