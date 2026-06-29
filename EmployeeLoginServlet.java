package Controllers;

import DAOs.EmployeeDAO;
import Models.Employee;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "EmployeeLoginServlet", urlPatterns = {"/EmployeeLogin"})
public class EmployeeLoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("EmployeeLoginView.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        HttpSession session = request.getSession();
        if (email == null || password == null || email.trim().isEmpty() || password.isEmpty()) {
            session.setAttribute("message", "Email and password are required.");
            response.sendRedirect(request.getContextPath() + "/EmployeeLogin");
            return;
        }

        String loginEmail = email.trim();
        EmployeeDAO employeeDAO = new EmployeeDAO();
        Employee employee = employeeDAO.employeeLogin(loginEmail, password);
        if (employee == null && loginEmail.toLowerCase().endsWith("@smarttick.vn")) {
            loginEmail = loginEmail.substring(0, loginEmail.indexOf('@')) + "@fwatch.vn";
            employee = employeeDAO.employeeLogin(loginEmail, password);
        }

        if (employee == null) {
            session.setAttribute("message", "Incorrect email or password.");
            response.sendRedirect(request.getContextPath() + "/EmployeeLogin");
            return;
        }
        if (employee.getStatus() != 1) {
            session.setAttribute("message", "This employee account is inactive.");
            response.sendRedirect(request.getContextPath() + "/EmployeeLogin");
            return;
        }

        session.setAttribute("employee", employee);
        Cookie cookie = new Cookie("smarttick_employee", employee.getEmail());
        cookie.setHttpOnly(true);
        cookie.setMaxAge(20 * 60);
        cookie.setPath(request.getContextPath().isEmpty() ? "/" : request.getContextPath());
        response.addCookie(cookie);

        if (employee.getRoleId() == 1 || employee.getRoleId() == 2) {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        } else if (employee.getRoleId() == 3) {
            response.sendRedirect(request.getContextPath() + "/ViewOrderListServlet");
        } else if (employee.getRoleId() == 4) {
            response.sendRedirect(request.getContextPath() + "/Warehouse");
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}
