package me.samschifman.allowance.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class AllowanceServlet extends HttpServlet {
  private static final String EMAIL_ATTR = "user_email";
  private static final String ACTION_ATTR = "action";
  
  enum Action {
    SET_EMAIL, DEBIT, CREDIT, VIEW;
  }
  
  private UserService userService = UserServiceFactory.getUserService();
  
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
    String view = null;
    Action action = loadAction(req);
    String email = (action == Action.SET_EMAIL) ? setEmail(req) : loadEmail(req);
    
    if (email == null) {
      view = "requestEmail.jsp";
    } else {
      req.setAttribute(EMAIL_ATTR, email);
      view = "test.jsp";
    }
     
    req.getRequestDispatcher(view).forward(req, resp);
  }
  
  private Action loadAction(HttpServletRequest req) {
    Action action = Action.VIEW;
    String selectedAction = req.getParameter(ACTION_ATTR);

    if (selectedAction != null) {
      action = Action.valueOf(selectedAction.toUpperCase());
    }
    
    return action;
  }
  
  private String loadEmail(HttpServletRequest req) {
    String email = null;
    HttpSession session = req.getSession(true);
    email = (String) session.getValue(EMAIL_ATTR);
    
    if (email == null) {
      User user = userService.getCurrentUser();
      email = (user != null) ? user.getEmail() : null;
    }
    
    session.setAttribute(EMAIL_ATTR, email);
    return email;
  }
  
  private String setEmail(HttpServletRequest req) {
    String email = null;
    HttpSession session = req.getSession(true);
    email = (String) req.getParameter(EMAIL_ATTR);
    session.setAttribute(EMAIL_ATTR, email);
    return email;
  }
}














     
  
