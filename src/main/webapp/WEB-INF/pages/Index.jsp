<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>  
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib
    prefix="c"
    uri="http://java.sun.com/jsp/jstl/core" 
%> 
  <%@ include file="header.jsp" %>
 
    	<div class="main">
    		<div class="post">
    		 
			
			
			
			
    	  <c:forEach items="${postList}" var="item">
		  <table cellspacing="10" class="post_table">
    		<tr>  
    		
    		<td width="15%">     
    		<a href="/SpringSecurity/profile/<c:out value="${item.authorID }" />"><c:out value="${item.authorName}" /></a><br>
    		<img  width="50px" src="resources/avatars/${item.authorAvatarURL}">
			<br>Posted:<br> 
			<p>${item.createdDate }</p>  
			 
			<table cellspacing="10" class="rate_table"><tr ><td><span class="glyphicon glyphicon-plus" aria-hidden="true"></td>
			<td><span class="glyphicon glyphicon-minus" aria-hidden="true"></td>
			
			</tr><tr><td><span class="badge"><c:out value="${item.plusCount}" /></span></td><td><span class="badge"><c:out value="${item.minusCount}" /></span></td></tr></table>
			  
			</td>
			<td >
			<a href="${contextPath}/post/${item.id}">#<c:out value="${item.id}" /></a>
			
			
			<hr class="specialhr"> 
			<c:out value="${item.message}" escapeXml="false" />
			</td>
			</tr>
			</table>
			<p></p>
			<div style="    position: relative;
    		left: 15%;">  
			<a href="${contextPath}/post/${item.id}/addComment" class="btn btn-primary btn-lg raised" role="button">Add Comment</a> <a href="#" class="btn btn-primary btn-lg raised" role="button">Report this post</a>
			</div> 
    		<hr> 
 
 
 
 
 
 		 </c:forEach>
    	
    	
    	</div>
    </div>
</body>
</html>