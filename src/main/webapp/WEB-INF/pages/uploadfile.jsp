<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page session="true"%>
<%@ taglib
    prefix="c"
    uri="http://java.sun.com/jsp/jstl/core" 
%> 
<html>
<head>
<title>Crunchify - Spring MVC Upload Multiple Files Example</title>

<style type="text/css">
body {
    background-image:
        url('http://cdn3.crunchify.com/wp-content/uploads/2013/03/Crunchify.bg_.300.png');
}
</style>
</head>
<body>
    <br>
    <br>
    <div align="center">
        <h1>Crunchify - Spring MVC Upload Multiple Files Example</h1>
 		<c:if test="${errorList != null}">
 			<c:forEach items="${errorList}" var="item">
	   					<li>${item}</li>
	   					</c:forEach>
 		</c:if>
 		 
        <form:form method="post" action="/SpringSecurity/dashboard/upload?${_csrf.parameterName}=${_csrf.token}"
            modelAttribute="uploadForm" enctype="multipart/form-data">
 
            <p>Select files to upload. Press Add button to add more file
                inputs.</p>
 
            <table id="fileTable">
                <tr>
                    <td><input name="crunchifyFiles" type="file" /></td>
                </tr>
            </table>
            <br />
            <input type="submit" value="Upload" />
            <input id="addFile" type="button" value="Add File" />

        </form:form>
 
        <br />
    </div>
</body>
</html>