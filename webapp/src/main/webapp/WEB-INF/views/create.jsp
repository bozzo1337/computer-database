<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title><spring:message code="application" /></title>
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
			<a class="navbar-brand" href="/web-cdb/"><spring:message
					code="application" /></a>
		<p align="right">
			<a href="?lang=en&firstCallCreate=${ addAttributes.firstCallCreate }"><img src="static/images/flag_uk.webp" height="40px" width="40px"/></a>&nbsp;<a
				href="?lang=fr&firstCallCreate=${ addAttributes.firstCallCreate }"><img src="static/images/flag_fr.webp" height="40px" width="40px"/></a>
		</p>
		</div>
	</header>
	<section id="main">
		<div class="container">
			<div class="row">
				<div class="col-xs-8 col-xs-offset-2 box">
					<h1>
						<spring:message code="addComputer" />
					</h1>
					<form id="createForm" name="createForm" action="create"
						method="POST">
						<fieldset>
							<div class="form-group">
								<label for="computerName"><spring:message
										code="computerName" />*</label> <input type="text"
									class="form-control" id="computerNameInput"
									name="computerNameInput" placeholder="Computer name">
							</div>
							<div class="form-group">
								<label for="introduced"><spring:message
										code="computerIntroduced" /></label> <input class="form-control"
									id="introduced" name="introduced" placeholder="Introduced date">
							</div>
							<div class="form-group">
								<label for="discontinued"><spring:message
										code="computerDiscontinued" /></label> <input class="form-control"
									id="discontinued" name="discontinued"
									placeholder="Discontinued date">
							</div>
							<div class="form-group">
								<label for="companyId"><spring:message
										code="computerCompany" /></label> <select class="form-control"
									id="companyId" name="companyId">
									<option value="" />
									<c:forEach var="company"
										items="${ addAttributes.listCompanies }">
										<option value="${ company.id }"><c:out
												value="${ company.name }" /></option>
									</c:forEach>

								</select>
							</div>
						</fieldset>
						<div class="actions pull-right">
							<input type="submit" value="<spring:message code="add"/>"
								class="btn btn-primary">
							<spring:message code="or" />
							<a href="/web-cdb/" class="btn btn-default"><spring:message
									code="cancel" /></a>
						</div>
					</form>
				</div>
			</div>
		</div>

		<c:if test="${ not addAttributes.firstCallCreate }">
			<div class="container">
				<c:choose>
					<c:when test="${ addAttributes.creationOK }">
						<p class="alert-success">
							<spring:message code="createSuccess" />
						</p>
					</c:when>
					<c:otherwise>
						<p class="alert-warning">
							<spring:message code="createError" />
							:
							<c:out value="${ addAttributes.errMessage }" />
						</p>
					</c:otherwise>
				</c:choose>
			</div>
		</c:if>

	</section>
</body>
</html>