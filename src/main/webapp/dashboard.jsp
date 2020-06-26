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
</head>
<body>
	<header class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<a class="navbar-brand" href="dashboard.html"> Application -
				Computer Database </a>
		</div>
	</header>

	<section id="main">
		<div class="container">
			<h1 id="homeTitle">${ compCount } computers found</h1>
			<div id="actions" class="form-horizontal">
				<div class="pull-left">
					<form id="searchForm" action="#" method="GET" class="form-inline">

						<input type="search" id="searchbox" name="search"
							class="form-control" placeholder="Search name" /> <input
							type="submit" id="searchsubmit" value="Filter by name"
							class="btn btn-primary" />
					</form>
				</div>
				<div class="pull-right">
					<a class="btn btn-success" id="addComputer" href="create">Add
						Computer</a> <a class="btn btn-default" id="editComputer" href="#"
						onclick="$.fn.toggleEditMode();">Edit</a>
				</div>
			</div>
		</div>

		<form id="deleteForm" action="#" method="POST">
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
						<th>Computer name</th>
						<th>Introduced date</th>
						<!-- Table header for Discontinued Date -->
						<th>Discontinued date</th>
						<!-- Table header for Company -->
						<th>Company</th>

					</tr>
				</thead>
				<!-- Browse attribute computers -->
				<tbody id="results">
					<c:forEach var="comp" items="${ listComp }">
						<tr>
							<td class="editMode"><input type="checkbox" name="cb"
								class="cb" value="0"></td>
							<td><a href="editComputer.html" onclick="">${ comp.name }</a>
							</td>
							<td>${ comp.introduced }</td>
							<td>${ comp.discontinued }</td>
							<td>${ comp.company.name }</td>
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
					<li><a href="dashboard?page=${ Math.max(0, currentPage - 1) }"
						aria-label="Previous"> <span aria-hidden="true">&laquo;</span>
					</a></li>
				</c:if>
				<c:forEach var="i" begin="${ Math.max(currentPage - 3, 0) }"
					end="${ Math.min(maxPage, currentPage + 3) }">
					<c:choose>
						<c:when test="${ i ne currentPage }">
							<li><a href="dashboard?page=${i}">${ i+1 }</a></li>
						</c:when>
						<c:otherwise>
							<li class="active"><a href="dashboard?page=${i}">${ i+1 }</a>
							</li>
						</c:otherwise>
					</c:choose>
				</c:forEach>
				<c:if test="${ currentPage < maxPage }">
					<li><a
						href="dashboard?page=${ Math.min(currentPage + 1, maxPage) }"
						aria-label="Next"> <span aria-hidden="true">&raquo;</span>
					</a></li>
				</c:if>
			</ul>
			<div class="btn-group btn-group-sm pull-right" role="group">
				<form action="dashboard"
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
	<script src="static/js/jquery.min.js"></script>
	<script src="static/js/bootstrap.min.js"></script>
	<script src="static/js/dashboard.js"></script>

</body>
</html>