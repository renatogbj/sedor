<%@page import="br.com.sedor.odonto.ExpertSystem"%>
<html>
<head>
<title>Sedor - Adicionar Lesão</title>

<!-- Style -->
<link href="resource/css/main.css" rel="stylesheet" type="text/css">

<!-- Bootstrap -->
<link href="resource/css/jasny.bootstrap.min.css" rel="stylesheet"
	media="screen">

<!-- JQuery -->
<script src="resource/js/jquery-1.8.3.js"></script>
<script src="resource/js/jasny.bootstrap.js"></script>

<script type="text/javascript">
	var qtd = document.getElementById("qtd").value;
	for (cont = 0; cont < qtd; cont++) {
		$('#example' + cont).tooltip();
	}
</script>
</head>
<body>
	<div id="page">
		<div class="row1"><%@ include file="header.jsp"%></div>

		<div class="row2 clear" style="padding-bottom: 60px;">
			<div class="span2" style="margin-left: 0px;"><%@ include
					file="menu.jsp"%></div>
			<div class="span9">

				<form action="add_lesion" method="POST"
					enctype="multipart/form-data">
					<legend>Adicionar Lesão</legend>
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
				<legend>Lesões</legend>
				<%
					ExpertSystem es = ExpertSystem.getInstance();
					String[] lesions = es.getLesions();
					int i = 0;
					for (i = 0; i < lesions.length; i++) {
				%>
				<p>
					<a href="#" id="example<%out.print(i);%>" data-placement="top"
						onmouseover="$('#example<%out.print(i);%>').tooltip();"
						rel="tooltip" data-original-title="A description of the lesion.">
						<%
							char[] stringArray = ExpertSystem.engineToPrintable(lesions[i])
										.toCharArray();
								stringArray[0] = Character.toUpperCase(stringArray[0]);
								out.print(new String(stringArray));
						%>
						<button class="btn right">Remover</button>
					</a>
				</p>
				<br />
				<%
					}
				%>
			</div>
			<div class="span2"></div>
			<div class="clear"></div>
			<input type="hidden" id="qtd" value="<%out.print(i);%>" />
		</div>

		<div class="row3" id="footer"><%@ include file="footer.jsp"%></div>
	</div>
</body>
</html>