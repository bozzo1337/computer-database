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
				<a
					href="?lang=en&computerId=${ editAttributes.computer.id }&firstCall=${ editAttributes.firstCall }"><img src="static/images/flag_uk.webp" height="40px" width="40px"/></a>&nbsp;
				<a
					href="?lang=fr&computerId=${ editAttributes.computer.id }&firstCall=${ editAttributes.firstCall }"><img src="static/images/flag_fr.webp" height="40px" width="40px"/></a>
			</p>
		</div>
	</header>
	<section id="main">
		<div class="container">
			<div class="row">
				<div class="col-xs-8 col-xs-offset-2 box">
					<div class="label label-default pull-right">
						<label for="computerId" id="computerID"> id : <c:out
								value="${ editAttributes.computer.id }" />
						</label>
					</div>
					<h1>
						<spring:message code="edit" />
					</h1>

					<form action="edit?computerId=${ editAttributes.computer.id }"
						method="POST">
						<input type="hidden" value="${ editAttributes.computer.id }"
							id="id" />
						<!-- TODO: Change this value with the computer id -->
						<fieldset>
							<div class="form-group">
								<label for="computerName"><spring:message
										code="computerName" />*</label> <input type="text"
									class="form-control" id="computerNameInput"
									name="computerNameInput"
									value="${ editAttributes.computer.name }">
							</div>
							<div class="form-group">
								<label for="introduced"><spring:message
										code="computerIntroduced" /></label> <input type="date"
									class="form-control" id="introduced" name="introduced"
									value="${ editAttributes.computer.introduced }">
							</div>
							<div class="form-group">
								<label for="discontinued"><spring:message
										code="computerDiscontinued" /></label> <input type="date"
									class="form-control" id="discontinued" name="discontinued"
									value="${ editAttributes.computer.discontinued }">
							</div>
							<div class="form-group">
								<label for="companyId"><spring:message
										code="computerCompany" /></label> <select class="form-control"
									id="companyId" name="companyId">
									<option value="${ editAttributes.computer.companyId }"><c:out
											value="${ editAttributes.computer.companyName }" /></option>
									<option value="0" />
									<c:forEach var="company"
										items="${ editAttributes.listCompanies }">
										<option value="${ company.id }"><c:out
												value="${ company.name }" /></option>
									</c:forEach>
								</select>
							</div>
						</fieldset>
						<div class="actions pull-right">
							<input type="submit" value="<spring:message code="editButton"/>"
								class="btn btn-primary">
							<spring:message code="or" />
							<a href="/web-cdb/" class="btn btn-default"><spring:message
									code="cancel" /></a>
						</div>
					</form>
				</div>
			</div>
		</div>
		<div class="container">
			<c:if test="${ not editAttributes.firstCall }">
				<c:choose>
					<c:when test="${ editAttributes.updateOK }">
						<p class="alert-success">
							<spring:message code="updateSuccess" />
						</p>
					</c:when>
					<c:otherwise>
						<p class="alert-warning">
							<spring:message code="updateError" />
							:
							<c:out value="${ editAttributes.errMessage }" />
						</p>
					</c:otherwise>
				</c:choose>
			</c:if>
		</div>
	</section>
</body>
</html>