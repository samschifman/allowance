<%@page contentType="text/html" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Collections" %>
<%@ page import="me.samschifman.allowance.domain.Bear" %>
<%@ page import="me.samschifman.allowance.domain.Transaction" %>
<%@ page import="me.samschifman.allowance.domain.Role" %>
<%
    Bear bear = (Bear) request.getAttribute("bear");
    Bear cub = (Bear) request.getAttribute("cub");
%>
<html>
  <head>
    <title>Bears Allowance</title>
  </head>
  <body>
    <h1>Welcome <%=bear.getName()%></h1>
    <% if (bear.getRole() != Role.CUB) { %>
    <div>
      <span><a href="/?action=VIEW_SELF">My Allowance</a></span> &nbsp;|&nbsp; <span><a href="/?action=VIEW_BEARS">The Family</a></span> &nbsp;|&nbsp; <span><a href="/?action=EDIT_BEAR&cub_id=<%=bear.getId()%>">My Profile</a></span>
    </div>
    <% } %>
    
    <hr/>
    
    <h1>You are viewing: <%=cub.getName()%></h1>
    <p><%=cub.getName()%>'s current ballanace is: $<%=cub.getBallance()%></p>
    <br/>
    
    <form name="debit" action="/" method="get">
          <input type="hidden" name="action" value="DEBIT" />
          <input type="hidden" name="cub_id" value="<%=cub.getId()%>" />
      Payed out <%=cub.getName()%> $<input type="text" name="amount" value="-<%=cub.getAllowance()%>" /> because <br/>
          <textarea style="width: 500px; height: 10ex;" name="comments"></textarea>
          <input name="index" type="submit" value="Debit"/>
    </form>
    ---------------------------------------------------------<br/>
    <form name="credit" action="/" method="get">
          <input type="hidden" name="action" value="CREDIT" />
          <input type="hidden" name="cub_id" value="<%=cub.getId()%>" />
      Give <%=cub.getName()%> $<input type="text" name="amount" value="<%=cub.getAllowance()%>" /> because <br/>
          <textarea style="width: 500px; height: 10ex;" name="comments"></textarea>
          <input name="index" type="submit" value="Credit"/>
    </form>
    
          
    <br/>
    <p><%=cub.getName()%>'s Transaction History:</p>
    
    <table border="1">
      <tr><th>Type</th><th>When</th><th>How Much</th><th>Running Ballance</th><th>Comments</th></tr>
    <% 
        List<Transaction> transList = new ArrayList<Transaction>(cub.getTransactions()); 
        Collections.sort(transList); 
    %>
    <% for (Transaction trans : transList) { %>
      <%=trans.toTableRow()%>
    <% } %>
    </table>
    
  </body>
</html>