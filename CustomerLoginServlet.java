package Controllers;

import DAOs.CustomerDAO;
import Models.Customer;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "CustomerLoginServlet", urlPatterns = {"/customerLogin"})
public class CustomerLoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("CustomerLoginView.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        HttpSession session = request.getSession();
        CustomerDAO dao = new CustomerDAO();

        if (email == null || password == null || email.trim().isEmpty() || password.isEmpty()) {
            session.setAttribute("message", "Email and password are required.");
            response.sendRedirect(request.getContextPath() + "/customerLogin");
            return;
        }

        Customer customer = dao.getCustomerLogin(email.trim(), password);
        if (customer == null) {
            session.setAttribute("message", "Incorrect email or password.");
            response.sendRedirect(request.getContextPath() + "/customerLogin");
            return;
        }
        if (customer.getIsDeleted() == 1) {
            session.setAttribute("message", "This account no longer exists.");
            response.sendRedirect(request.getContextPath() + "/customerLogin");
            return;
        }
        if (customer.getIsBlock() == 1) {
            session.setAttribute("message", "This account is locked.");
            response.sendRedirect(request.getContextPath() + "/customerLogin");
            return;
        }

        session.setAttribute("customer", customer);
        Cookie cookie = new Cookie("smarttick_customer", email.trim());
        cookie.setHttpOnly(true);
        cookie.setMaxAge(20 * 60);
        cookie.setPath(request.getContextPath().isEmpty() ? "/" : request.getContextPath());
        response.addCookie(cookie);
        response.sendRedirect(request.getContextPath() + "/customer/dashboard");
    }
}
