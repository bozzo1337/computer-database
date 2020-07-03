<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">
<!-- Bootstrap -->
<link href="static/css/bootstrap.min.css" rel="stylesheet"
	media="screen">
<link href="static/css/font-awesome.css" rel="stylesheet" media="screen">
<link href="static/css/main.css" rel="stylesheet" media="screen">
<script src="static/js/jquery.min.js"></script>
<script src="static/js/bootstrap.min.js"></script>
<script src="static/js/dashboard.js"></script>
</head>
<body>
	<header class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<a class="navbar-brand" href="dashboard?page=0"> Application -
				Computer Database </a>
		</div>
	</header>

	<section id="main">
		<div class="container">
			<h1 id="homeTitle">
				<c:out value="${ compCount }" />
				computers found
			</h1>
			<div id="actions" class="form-horizontal">
				<div class="pull-left">
					<form id="searchForm" action="dashboard" method="GET"
						class="form-inline">

						<input type="search" id="searchbox" name="search"
							class="form-control" placeholder="Search name" /> <input
							type="submit" id="searchsubmit" value="Filter by name"
							class="btn btn-primary" />
					</form>
				</div>
				<div class="pull-right">
					<a class="btn btn-success" id="addComputer"
						href="create?firstCallCreate=true">Add Computer</a> <a
						class="btn btn-default" id="editComputer" href="#"
						onclick="$.fn.toggleEditMode();">Edit</a>
				</div>
			</div>
		</div>

		<form id="deleteForm"
			action="delete?search=${ search }&order=${ order }&page=${ currentPage }"
			method="POST">
			<input type="hidden" name="selection" value="">
		</form>

		<div class="container" style="margin-top: 10px;">
			<table class="table table-striped table-bordered">
				<thead>
					<tr>
						<!-- Variable declarations for passing labels as parameters -->
						<!-- Table header for Computer Name -->

						<th class="editMode" style="width: 60px; height: 22px;"><input
							type="checkbox" id="selectall" /> <span
							style="vertical-align: top;"> - <a href="#"
								id="deleteSelected" onclick="$.fn.deleteSelected();"> <i
									class="fa fa-trash-o fa-lg"></i>
							</a>
						</span></th>
						<th><c:choose>
								<c:when test="${ order == 'computer' }">
									<a href="dashboard?search=${ search }&order=computerdesc">Computer
										name</a>
								</c:when>
								<c:otherwise>
									<a href="dashboard?search=${ search }&order=computer">Computer
										name</a>
								</c:otherwise>
							</c:choose></th>
						<th><c:choose>
								<c:when test="${ order == 'introduced' }">
									<a href="dashboard?search=${ search }&order=introduceddesc">Introduced
										date (DD/MM/YYYY)</a>
								</c:when>
								<c:otherwise>
									<a href="dashboard?search=${ search }&order=introduced">Introduced
										date (DD/MM/YYYY)</a>
								</c:otherwise>
							</c:choose></th>
						<!-- Table header for Discontinued Date -->
						<th><c:choose>
								<c:when test="${ order == 'discontinued' }">
									<a href="dashboard?search=${ search }&order=discontinueddesc">Discontinued
										date (DD/MM/YYYY)</a>
								</c:when>
								<c:otherwise>
									<a href="dashboard?search=${ search }&order=discontinued">Discontinued
										date (DD/MM/YYYY)</a>
								</c:otherwise>
							</c:choose></th>
						<!-- Table header for Company -->
						<th><c:choose>
								<c:when test="${ order == 'company' }">
									<a href="dashboard?search=${ search }&order=companydesc">Company</a>
								</c:when>
								<c:otherwise>
									<a href="dashboard?search=${ search }&order=company">Company</a>
								</c:otherwise>
							</c:choose></th>
					</tr>
				</thead>
				<!-- Browse attribute computers -->
				<tbody id="results">
					<c:forEach var="comp" items="${ listComp }">
						<tr>
							<td class="editMode"><input type="checkbox" name="cb"
								class="cb" value="${ comp.id }"></td>
							<td><a href="edit?computerId=${ comp.id }&firstCall=true"
								onclick=""><c:out value="${ comp.name }" /></a></td>
							<td><c:out value="${ comp.introduced }" /></td>
							<td><c:out value="${ comp.discontinued }" /></td>
							<td><c:out value="${ comp.companyName }" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</section>

	<footer class="navbar-fixed-bottom">
		<div class="container text-center">
			<ul class="pagination">
				<c:if test="${ currentPage > 0 }">
					<li><a
						href="dashboard?search=${ search }&order=${ order }&page=${ currentPage - 1 }"
						aria-label="Previous" id="previousPage"> <span
							aria-hidden="true">&laquo;</span>
					</a></li>
				</c:if>
				<c:forEach var="i" begin="${ Math.max(currentPage - 3, 0) }"
					end="${ Math.min(maxPage, currentPage + 3) }">
					<li <c:if test="${ i eq currentPage }">class="active"</c:if>>
						<a href="dashboard?search=${ search }&order=${ order }&page=${i}"><c:out
								value="${ i+1 }" /></a>
					</li>

				</c:forEach>
				<c:if test="${ currentPage < maxPage }">
					<li><a
						href="dashboard?search=${ search }&order=${ order }&page=${ currentPage + 1 }"
						aria-label="Next" id="nextPage"> <span aria-hidden="true">&raquo;</span>
					</a></li>
				</c:if>
			</ul>
			<div class="btn-group btn-group-sm pull-right" role="group">
				<form action="dashboard?search=${ search }&order=${ order }"
					method="post">
					<c:choose>
						<c:when test="${ entitiesPerPage eq 10 }">
							<button type="submit" class="btn active" name="button10">10</button>
						</c:when>
						<c:otherwise>
							<button type="submit" class="btn btn-default" name="button10">10</button>
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${ entitiesPerPage eq 50 }">
							<button type="submit" class="btn active" name="button50">50</button>
						</c:when>
						<c:otherwise>
							<button type="submit" class="btn btn-default" name="button50">50</button>
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${ entitiesPerPage eq 100 }">
							<button type="submit" class="btn active" name="button100">100</button>
						</c:when>
						<c:otherwise>
							<button type="submit" class="btn btn-default" name="button100">100</button>
						</c:otherwise>
					</c:choose>
				</form>
			</div>
		</div>
	</footer>
</body>
</html>