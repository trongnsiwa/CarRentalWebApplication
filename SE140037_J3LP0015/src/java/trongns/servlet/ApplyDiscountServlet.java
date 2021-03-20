/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import trongns.cart.CartObj;
import trongns.discount.DiscountDAO;
import trongns.discount.DiscountDTO;

/**
 *
 * @author TrongNS
 */
public class ApplyDiscountServlet extends HttpServlet {

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

        String url = "cart";
        try {
            if (request.getParameterMap() == null || request.getParameterMap().isEmpty()) {
                return;
            }

            String discountCode = request.getParameter("txtDiscountCode");

            HttpSession session = request.getSession(false);
            if (session != null) {
                CartObj cart = (CartObj) session.getAttribute("CART");
                if (cart != null) {
                    if (cart.getItems() != null) {
                        if (cart.getItems().size() > 0) {
                            if (discountCode != null && !discountCode.trim().isEmpty()) {
                                DiscountDAO dao = new DiscountDAO();
                                dao.checkAvailableDiscount(discountCode);
                                DiscountDTO dto = dao.getDiscount();

                                if (dto != null) {
                                    cart.applyDiscount(dto);
                                    session.setAttribute("CART", cart);

                                    request.setAttribute("APPY_CODE_MESSAGE", "Apply discount successfully");
                                } else {
                                    cart.removeDiscount();
                                    request.setAttribute("APPY_CODE_MESSAGE", "Your code is invalid or expired");
                                }
                            } else {
                                if (cart.getDiscount() != null) {
                                    cart.removeDiscount();
                                }
                            }
                        } else {
                            if (cart.getDiscount() != null) {
                                cart.removeDiscount();
                            }
                        }
                    } else {
                        if (cart.getDiscount() != null) {
                            cart.removeDiscount();
                        }
                    }
                }

                if (session.getAttribute("ORDER_INFO") != null) {
                    session.removeAttribute("ORDER_INFO");
                }
            }

        } catch (ParseException ex) {
            String msg = ex.getMessage();
            log("Error at ApplyDiscountServlet _ ParseException : " + msg);
        } catch (NamingException ex) {
            String msg = ex.getMessage();
            log("Error at ApplyDiscountServlet _ NamingException : " + msg);
        } catch (SQLException ex) {
            String msg = ex.getMessage();
            log("Error at ApplyDiscountServlet _ SQLException : " + msg);
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
