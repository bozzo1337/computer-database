<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap -->
<link href="static/css/bootstrap.min.css" rel="stylesheet"
	media="screen">
<link href="static/css/font-awesome.css" rel="stylesheet" media="screen">
<link href="static/css/main.css" rel="stylesheet" media="screen">
<link rel="stylesheet"
	href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/themes/smoothness/jquery-ui.css">
<script type="text/javascript"
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.19.1/jquery.validate.min.js"></script>
<script
	src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>
<script type="text/javascript" src="static/js/form.valid.js"></script>
</head>
<body>
	<header class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<a class="navbar-brand" href="dashboard?page=0"> Application - Computer
				Database </a>
		</div>
	</header>
	<section id="main">
		<div class="container">
			<div class="row">
				<div class="col-xs-8 col-xs-offset-2 box">
					<h1>Add Computer</h1>
					<form id="createForm" name="createForm" action="create"
						method="POST">
						<fieldset>
							<div class="form-group">
								<label for="computerName">Computer name *</label> <input
									type="text" class="form-control" id="computerNameInput"
									name="computerNameInput" placeholder="Computer name">
							</div>
							<div class="form-group">
								<label for="introduced">Introduced date (DD/MM/YYYY)</label> <input
									class="form-control" id="introduced"
									name="introduced" placeholder="Introduced date">
							</div>
							<div class="form-group">
								<label for="discontinued">Discontinued date (DD/MM/YYYY)</label>
								<input class="form-control" id="discontinued"
									name="discontinued" placeholder="Discontinued date">
							</div>
							<div class="form-group">
								<label for="companyId">Company</label> <select
									class="form-control" id="companyId" name="companyId">
									<option value="0" />
									<c:forEach var="company" items="${ listCompanies }">
										<option value="${ company.id }"><c:out
												value="${ company.name }" /></option>
									</c:forEach>

								</select>
							</div>
						</fieldset>
						<div class="actions pull-right">
							<input type="submit" value="Add" class="btn btn-primary">
							or <a href="dashboard?page=0" class="btn btn-default">Cancel</a>
						</div>
					</form>
				</div>
			</div>
		</div>

		<c:if test="${ not firstCallCreate }">
			<div class="container">
				<c:choose>
					<c:when test="${ creationOK }">
						<p class="alert-success">Creation successful</p>
					</c:when>
					<c:otherwise>
						<p class="alert-warning">
							Error creating :
							<c:out value="${ errMessage }" />
						</p>
					</c:otherwise>
				</c:choose>
			</div>
		</c:if>

	</section>
</body>
</html>