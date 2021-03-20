/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import trongns.user.UserDAO;

/**
 *
 * @author TrongNS
 */
public class VerifyAccountServlet extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String url = "login-page";

        try {
            if (request.getParameterMap().isEmpty() || request.getParameterMap() == null) {
                return;
            }
            
            url = "verify-account-page";

            HttpSession session = request.getSession(false);
            if (session != null) {
                String email = (String) session.getAttribute("USER");
                if (email != null) {
                    String code = request.getParameter("code");

                    UserDAO dao = new UserDAO();
                    boolean result = dao.checkVerifyCode(email, code);
                    if (result) {
                        result = dao.changeAccountStatus(email);
                        if (result) {
                            url = "login--page";
                            request.setAttribute("VERIFY_ACCOUNT_SUCCESS", "Verify successful, please login");
                        } else {
                            request.setAttribute("VERIFY_ACCOUNT_ERROR", "Verify failed");
                        }
                    } else {
                        request.setAttribute("VERIFY_ACCOUNT_ERROR", "Invalid code! Please input code we sended to your email");
                    }
                }
            }
        } catch (NamingException ex) {
            String msg = ex.getMessage();
            log("Error at VerifyAccountServlet _ NamingException : " + msg);
        } catch (SQLException ex) {
            String msg = ex.getMessage();
            log("Error at VerifyAccountServlet _ SQLException : " + msg);
        } finally {
            HashMap<String, String> mappingList = (HashMap<String, String>) request.getServletContext().getAttribute("MAPPING_LIST");
            if (mappingList != null) {
                url = mappingList.get(url);
            }
            request.getRequestDispatcher(url).forward(request, response);
            out.close();
        }
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
        processRequest(request, response);
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
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
