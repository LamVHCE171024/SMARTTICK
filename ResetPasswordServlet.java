/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import DAOs.CustomerDAO;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;

/**
 *
 * @author ThyLTKCE181577
 */
public class ResetPasswordServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("ResetPasswordView.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("resetEmail");

        // Kiểm tra confirm password
        if (newPassword == null || confirmPassword == null || !newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords do not match!");
            request.getRequestDispatcher("ResetPasswordView.jsp").forward(request, response);
            return;
        }

        // Check: ít nhất 8 kí tự
        if (newPassword.length() < 8) {
            request.setAttribute("error", "Password must be at least 8 characters long!");
            request.getRequestDispatcher("ResetPasswordView.jsp").forward(request, response);
            return;
        }

        // Check: chứa ít nhất 1 kí tự in hoa
        if (!newPassword.matches(".*[A-Z].*")) {
            request.setAttribute("error", "Password must contain at least one uppercase letter!");
            request.getRequestDispatcher("ResetPasswordView.jsp").forward(request, response);
            return;
        }

        // Check: chứa ít nhất 1 kí tự đặc biệt (không phải chữ và số)
        if (!newPassword.matches(".*[^a-zA-Z0-9].*")) {
            request.setAttribute("error", "Password must contain at least one special character!");
            request.getRequestDispatcher("ResetPasswordView.jsp").forward(request, response);
            return;
        }

        CustomerDAO userDAO = new CustomerDAO();
        // Cập nhật mật khẩu mới trong database
        boolean success = userDAO.updatePassword(email, newPassword);

        if (success) {
            session.removeAttribute("otp");
            session.removeAttribute("resetEmail");
            session.setAttribute("successMessage", "Password changed successfully!");
            response.sendRedirect(request.getContextPath() + "/customerLogin");
        } else {
            request.setAttribute("error", "An error occurred! Please try again.");
            request.getRequestDispatcher("ResetPasswordView.jsp").forward(request, response);
        }
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
