/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import DAOs.CustomerDAO;
import Models.Customer;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author TuongMPCE180644
 */
@WebServlet(name = "ChangePasswordServlet", urlPatterns = { "/changeCustomerPassword" })
public class ChangeCustomerPasswordServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the
    // + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        HttpSession session = request.getSession();
        Customer cus = (Customer) session.getAttribute("customer");
        if (cus == null) {
            response.sendRedirect(request.getContextPath() + "/customerLogin");
            return;
        }
        // try (PrintWriter out = response.getWriter()) {

        // out.println(currentPassword);
        // out.println(newPassword);
        // out.println(confirmPassword);
        // }

        CustomerDAO cusDAO = new CustomerDAO();
        if (cusDAO.cofirmPassword(cus.getId(), currentPassword) > 0) {
            if (currentPassword.equals(newPassword)) {
                session.setAttribute("messageFail", "New password must be different from current password.");
                request.setAttribute("profilePage", "ChangeCustomerPasswordView.jsp");
                request.getRequestDispatcher("ProfileManagementView.jsp").forward(request, response);
                return;
            }

            if (confirmPassword == null || !newPassword.equals(confirmPassword)) {
                session.setAttribute("messageFail", "New password and confirmation do not match.");
                request.setAttribute("profilePage", "ChangeCustomerPasswordView.jsp");
                request.getRequestDispatcher("ProfileManagementView.jsp").forward(request, response);
                return;
            }

            if (cusDAO.changeCustomerPassword(cus.getId(), newPassword) > 0) {
                session.setAttribute("message", "Change Password Success!");
            } else {
                session.setAttribute("messageFail", "Change Password Fail!");
            }
        } else {
            session.setAttribute("messageFail", "Your Current Password Is Not Correct!");
        }

        request.setAttribute("profilePage", "ChangeCustomerPasswordView.jsp");
        request.getRequestDispatcher("ProfileManagementView.jsp").forward(request, response);
    }

    /**
     * Returns the servlet description.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "SMARTTICK servlet";
    }// </editor-fold>

}
