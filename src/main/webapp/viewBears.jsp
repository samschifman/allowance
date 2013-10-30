<%@page contentType="text/html" %>
<%@ page import="java.util.Collection" %>
<%@ page import="me.samschifman.allowance.domain.Bear" %>
<%@ page import="me.samschifman.allowance.domain.Transaction" %>
<%@ page import="me.samschifman.allowance.domain.Role" %>
<%
    Bear bear = (Bear) request.getAttribute("bear");
    Collection<Bear> bears = (Collection<Bear>) request.getAttribute("bears");
%>
<html>
  <head>
    <title>Bears Allowance</title>
  </head>
  <body>
    <h1>Welcome <%=bear.getName()%></h1>
    <% if (bear.getRole() != Role.CUB) { %>
    <div>
      <span><a href="/?action=VIEW_SELF">My Allowance</a></span> &nbsp;|&nbsp; <span>The Family</span> &nbsp;|&nbsp; <span><a href="/?action=EDIT_BEAR&cub_id=<%=bear.getId()%>">My Profile</a></span>
    </div>
    <% } %>
    
    <hr/>
    
    <table>
      <tr><th>Name</th><th>Balance</th><th>Actions</th></tr>
    <% for (Bear cub : bears) { %>
      <tr><td><%=cub.getName()%></td><td>$<%=cub.getBallance()%></td><td><a href="/?action=EDIT_BEAR&cub_id=<%=cub.getId()%>">Profile</a></td></tr>
    <% } %>
    </table>
    <a href="/?action=CREATE_BEAR">+ Add Cub</a>
  </body>
</html>