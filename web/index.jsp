<%--
    Document   : index
    Created on : Jul 27, 2015, 4:54:05 PM
    Author     : cheryl
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>My form</h1>
        <form method="POST" action="addTitle" enctype="multipart/form-data">
            <label for="file2upld">Select File</label><input type="file" name="file2upld">
            <label for="title">Enter title</label><input type="text" name="title">
            <label for="colors">Select a color</label><input type="color" name="colors">

            <input type="submit" value="Get image with title">
        </form>
    </body>
</html>
