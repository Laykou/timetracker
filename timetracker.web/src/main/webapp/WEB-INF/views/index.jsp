<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<t:layout title="Time tracker administration">
	<h1>Welcome to JBoss!</h1>

	<div>
		<p>You have successfully deployed a Spring MVC web application.</p>
		<h3>Your application can run on:</h3>
		<img src="<c:url value="/static/resources/gfx/dualbrand_as7eap.png"/>" />
	</div>

	<h2>Category: ${category.label}</h2>

	<h2>Cool members</h2>
	<c:choose>
		<c:when test="${members.size()==0}">
			<em>No registered members.</em>
		</c:when>
		<c:otherwise>
			<table class="simpletablestyle">
				<thead>
					<tr>
						<th>Id</th>
						<th>Name</th>
						<th>Email</th>
						<th>Phone #</th>
						<th>REST URL</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${members}" var="member">
						<tr>
							<td>${member.id}</td>
							<td>${member.name}</td>
							<td>${member.email}</td>
							<td>${member.phoneNumber}</td>
							<td><a href="<c:url value="/rest/members/${member.id}"/>">/rest/members/${member.id}</a></td>
					</c:forEach>
				</tbody>
			</table>
			<table class="simpletablestyle">
				<tr>
					<td>REST URL for all members: <a
						href="<c:url value="/rest/members"/>">/rest/members</a>
					</td>
				</tr>
			</table>
		</c:otherwise>
	</c:choose>
</t:layout>