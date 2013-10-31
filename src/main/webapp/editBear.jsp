<%@page contentType="text/html" %>
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
      <span><a href="/?action=VIEW_SELF">My Allowance</a></span> &nbsp;|&nbsp; <span><a href="/?action=VIEW_BEARS">The Family</a></span> &nbsp;|&nbsp; <span>My Profile</span>
    </div>
    <% } %>
    
    <hr/>
    
    <div>
        <form name="edit" action="/" method="get">
          <input type="hidden" name="action" value="SAVE_BEAR" />
          
          <% if (cub != null) { %>
          <input type="hidden" name="cub_id" value="<%=cub.getId()%>" />
      
          Name: <input type="text" name="name" value="<%=(cub.getName() != null) ? cub.getName() : ""%>"  style="width: 225px;"/> <br/>
          Email: <input type="text" name="email" value="<%=cub.getEmail()%>" style="width: 225px;"/> <br/>
          Allowance: $<input type="text" name="allowance" value="<%=cub.getAllowance()%>" /> <br/>
      
          Role:
          <select name="role">
            <option <%=Role.CUB.equals(cub.getRole())? "selected" : ""%>>CUB</option>
            <option <%=Role.PARENT.equals(cub.getRole())? "selected" : ""%>>PARENT</option>
            <option <%=Role.ADMIN.equals(cub.getRole())? "selected" : ""%>>ADMIN</option>
          </select> 
          
          <% } else { %>
      
          Name: <input type="text" name="name" value=""  style="width: 225px;"/> <br/>
          Email: <input type="text" name="email" value="" style="width: 225px;"/> <br/>
          Allowance: $<input type="text" name="allowance" value="" /> <br/>
      
          Role:
          <select name="role">
            <option>CUB</option>
            <option>PARENT</option>
          </select> 
          <% } %> 
          
          
          <br/>
          <br/>
          
          
          <input name="index" type="submit" value="Save"/>
        </form>
    </div>
  </body>
</html>