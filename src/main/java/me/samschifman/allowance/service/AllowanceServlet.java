package me.samschifman.allowance.service;

import org.apache.commons.lang3.StringUtils;

import me.samschifman.allowance.domain.Bear;
import me.samschifman.allowance.domain.Role;

import java.io.IOException;
import java.util.Collection;

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
  private static final String BEAR_ATTR = "bear";
  private static final String BEARS_ATTR = "bears";
  private static final String CUB_ID_ATTR = "cub_id";
  private static final String CUB_ATTR = "cub";
  
  
  enum Action {
    SET_EMAIL, DEBIT, CREDIT, SAVE_BEAR, VIEW_SELF, VIEW_BEAR, VIEW_BEARS, CREATE_BEAR, EDIT_BEAR;
  }
  
  private UserService userService = UserServiceFactory.getUserService();
  private AllowanceService allowanceService;
  
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
    String view = null;
    Action action = loadAction(req);
    String email = (action == Action.SET_EMAIL) ? setEmail(req) : loadEmail(req);
    
    if (email == null) {
      view = "requestEmail.jsp";
    } else {
      Bear bear = getAllowanceService().getBear(email);
      if (bear == null) {
        view = "requestEmail.jsp";
      } else {
        view = handleAction(req, bear, action);
      }
    }
     
    req.getRequestDispatcher(view).forward(req, resp);
  }
  
  private String  handleAction(HttpServletRequest req, Bear bear, Action action) {
    String view = null;
    switch (action) {
      case SAVE_BEAR :
        Long id = saveCub(req);
        addBear(req, bear);
        addCub(id, req);
        view = "editBear.jsp";
        break;
      case EDIT_BEAR :
        addBear(req, bear);
        addCub(req);
        view = "editBear.jsp";
        break;
      case CREATE_BEAR :
        addBear(req, bear);
        view = "editBear.jsp";
        break;
      case DEBIT :
        debit(req);
        addBear(req, bear);
        addCub(req);
        view = "transBear.jsp";
        break;
      case CREDIT :
        credit(req);
        addBear(req, bear);
        addCub(req);
        view = "transBear.jsp";
        break;
      case VIEW_BEAR :
        addBear(req, bear);
        addCub(req);
        view = "transBear.jsp";
        break;
      case VIEW_BEARS :
        addBear(req, bear);
        addBears(req);
        view = "viewBears.jsp";
        break;
      case VIEW_SELF :
      default :
        addBear(req, bear);
        view = "viewMySelf.jsp";
        break;
    };
    return view;
  }
  
  private void addBear(HttpServletRequest req, Bear bear) {
      req.setAttribute(BEAR_ATTR, bear);
  }
  
  private void addBears(HttpServletRequest req) {
      Collection<Bear> bears = getAllowanceService().getBears();
      req.setAttribute(BEARS_ATTR, bears);
  }
  
  private void addCub(HttpServletRequest req) {
      Long id = null;
      String idAttr = req.getParameter(CUB_ID_ATTR);
      id = Long.valueOf(idAttr);
      
      addCub(id, req);
  }
  
  private void addCub(Long id, HttpServletRequest req) {
      Bear cub = getAllowanceService().getCub(id);
      req.setAttribute(CUB_ATTR, cub);
  }
  
  private void debit(HttpServletRequest req) {
      String idAttr = req.getParameter(CUB_ID_ATTR);
      Long id = Long.valueOf(idAttr);
      Bear cub = getAllowanceService().getCub(id);
      
      String amountStr = req.getParameter("amount");
      double amount = Double.valueOf(amountStr);

      String comments = req.getParameter("comments");    
      
      getAllowanceService().debit(cub, amount, comments);
  }
  
  private void credit(HttpServletRequest req) {
      String idAttr = req.getParameter(CUB_ID_ATTR);
      Long id = Long.valueOf(idAttr);
      Bear cub = getAllowanceService().getCub(id);
      
      String amountStr = req.getParameter("amount");
      double amount = Double.valueOf(amountStr);

      String comments = req.getParameter("comments");    
      
      getAllowanceService().credit(cub, amount, comments);
    
  }
  
  private Long saveCub(HttpServletRequest req) {
      Long id = null;
      Bear cub = null;
      
      String idAttr = req.getParameter(CUB_ID_ATTR);
      
      if (!StringUtils.isEmpty(idAttr)) {
        id = Long.valueOf(idAttr);
        cub = getAllowanceService().getCub(id);
      } else {
        cub = new Bear();
      }
      
      String name = req.getParameter("name");
      cub.setName(name);
      String email = req.getParameter("email");
      cub.setEmail(email);
      String allowance = req.getParameter("allowance");
      cub.setAllowance(Double.valueOf(allowance));
      String role = req.getParameter("role");
      role = role.toUpperCase();
      cub.setRole(Role.valueOf(role));
      
      
      getAllowanceService().saveBear(cub);
      
      return cub.getId();
  }
  
  private Action loadAction(HttpServletRequest req) {
    Action action = Action.VIEW_SELF;
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
  
  private AllowanceService getAllowanceService() {
    if (allowanceService == null) {allowanceService = AllowanceService.getInstance(); }
    return allowanceService;
  }
}














     
  
