<%@page contentType="text/html" %>
<%@ page import="me.samschifman.allowance.domain.Bear" %>
<%@ page import="me.samschifman.allowance.domain.Transaction" %>
<%@ page import="me.samschifman.allowance.domain.Role" %>
<%
    Bear bear = (Bear) request.getAttribute("bear");
%>
<html>
  <head>
    <title>Bears Allowance</title>
  </head>
  <body>
    <h1>Welcome <%=bear.getName()%></h1>
    <% if (bear.getRole() != Role.CUB) { %>
    <div>
      <span>My Allowance</span> &nbsp;|&nbsp; <span><a href="/?action=VIEW_BEARS">The Family</a></span> &nbsp;|&nbsp; <span><a href="/?action=EDIT_BEAR&cub_id=<%=bear.getId()%>">My Profile</a></span>
    </div> 
    <% } %>
    
    <hr/>
    
    <p>Your current ballanace is: $<%=bear.getBallance()%></p>
    
    <br/>
    <p>Your Transaction History:</p>
    
    <table b>
      <tr><th>Type</th><th>When</th><th>How Much</th><th>Running Ballance</th><th>Comments</th></tr>
    <% for (Transaction trans : bear.getTransactions()) { %>
      <tr><td><%=trans.getType()%></td><td><%=trans.getDate()%></td><td><%=trans.getAmount()%></td><td><%=trans.getRunningTotal()%></td><td><%=trans.getComments()%></td></tr>
    <% } %>
    </table>
    
  </body>
</html>