/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.HashMap;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import trongns.user.UserDAO;
import trongns.user.UserDTO;
import trongns.util.MyUtility;
import trongns.util.VerifyUtils;

/**
 *
 * @author TrongNS
 */
public class LoginServlet extends HttpServlet {

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

        boolean isSuccess = false;
        boolean valid = true;

        try {
            if (request.getParameterMap().isEmpty() || request.getParameterMap() == null) {
                return;
            }

            url = "login--page";

            String email = request.getParameter("txtEmail");
            String password = request.getParameter("txtPassword");
            String gRecaptchaResponse = request.getParameter("g-recaptcha-response");

            System.out.println("gRecaptchaResponse=" + gRecaptchaResponse);

            valid = VerifyUtils.verify(gRecaptchaResponse);
            if (!valid) {
                request.setAttribute("LOGIN_ERROR", "Captcha invalid");
            }
            
            if (valid) {
                String hashedPassword = MyUtility.getEncryptedPassword(password);

                UserDAO dao = new UserDAO();
                dao.checkLogin(email, hashedPassword);
                UserDTO dto = dao.getLoginUser();

                HttpSession session = request.getSession();
                if (dto != null) {
                    if ("Inactive".equals(dto.getStatus())) {
                        url = "verify-account-page";
                        request.setAttribute("LOGIN_ERROR", "Your account still hasn't been verified");
                        session.setAttribute("USER", dto.getEmail());
                    } else {
                        isSuccess = true;
                        url = "home";

                        session.setAttribute("USER", dto.getEmail());
                        session.setAttribute("FULLNAME", dto.getFullname());
                        session.setAttribute("ROLE", dto.getRole());
                        session.setAttribute("ADDRESS", dto.getAddress());
                        session.setAttribute("PHONE", dto.getPhone());
                        session.setAttribute("FIRST_TIME_ACCESS", "first");
                    }
                } else {
                    request.setAttribute("LOGIN_ERROR", "Invalid email or password. Please try again.");
                }
            }
        } catch (NoSuchAlgorithmException ex) {
            String msg = ex.getMessage();
            log("Error at LoginServlet _ NoSuchAlgorithmException : " + msg);
        } catch (NamingException ex) {
            String msg = ex.getMessage();
            log("Error at LoginServlet _ NamingException : " + msg);
        } catch (SQLException ex) {
            String msg = ex.getMessage();
            log("Error at LoginServlet _ SQLException : " + msg);
        } finally {
            if (isSuccess) {
                response.sendRedirect(url);
            } else {
                HashMap<String, String> mappingList = (HashMap<String, String>) request.getServletContext().getAttribute("MAPPING_LIST");
                if (mappingList != null) {
                    url = mappingList.get(url);
                }
                request.getRequestDispatcher(url).forward(request, response);
            }
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
