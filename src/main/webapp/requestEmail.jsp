<%@page contentType="text/html" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="me.samschifman.allowance.service.AllowanceService" %>

<html>
  <head>
    <title>Bears Allowance</title>
  </head>
  <body>
    <% if (AllowanceService.getInstance().isTest()) { %>
    <h1>Enter your e-mail</h1>
    
    <form name="setEmail" action="/" method="get">
      <input type="hidden" name="action" value="SET_EMAIL"/>
      <input placeholder="Email" style="width:500px;" type="text" name="user_email" id="user_email" />
      <input type="submit" />
    </form>
    
    <% } else { %>
    <%
        UserService userService = UserServiceFactory.getUserService();
        String loginURL = userService.createLoginURL("/");
        String logoutURL = null;
        if (request.getUserPrincipal() != null) {
          logoutURL = userService.createLogoutURL("/");
        }
    %>
    <h1>Login with Google</h1>
    
    <p>To use this site you have to be a member of the family and log in with your Google account.</p>
    
    <a href="<%=loginURL%>">Login</a> <% if (logoutURL != null) { %> <a href="<%=logoutURL%>">Logout</a> <% } %>
    
    
    <% } %>
  </body>
</html>
