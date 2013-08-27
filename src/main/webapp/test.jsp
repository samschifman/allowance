<!DOCTYPE html>
<%
  String email = (String) request.getAttribute("user_email");
%>
<html>
  <head>
    <title>Testing</title>
  </head>
  <body>
    <h1>Welcome <%=email%></h1>
  </body>
</html>
