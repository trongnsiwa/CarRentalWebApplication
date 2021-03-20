/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import trongns.orderdetail.OrderDetailDAO;
import trongns.orderdetail.OrderDetailDTO;
import trongns.type.TypeDAO;
import trongns.type.TypeDTO;

/**
 *
 * @author TrongNS
 */
public class LoadHomeServlet extends HttpServlet {

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

        String url = "home-page";

        try {

            HttpSession session = request.getSession(false);
            if (session != null) {
                if (session.getAttribute("ORDER_INFO") != null) {
                    session.removeAttribute("ORDER_INFO");
                }
            }

            OrderDetailDAO rentDetailDAO = new OrderDetailDAO();
            rentDetailDAO.loadDropOffCarList();
            ArrayList<OrderDetailDTO> dropOffCarList = rentDetailDAO.getListDropOffCar();

            if (dropOffCarList != null) {
                for (OrderDetailDTO detail : dropOffCarList) {
                    String orderId = detail.getOrderId();
                    int carModelId = detail.getCarModelId();
                    rentDetailDAO.updateRentingStatus(orderId, carModelId, false);
                }
            }
            
            TypeDAO typeDAO = new TypeDAO();
            typeDAO.loadTypeList();
            ArrayList<TypeDTO> carTypeList = typeDAO.getTypeList();
            request.setAttribute("CAR_TYPE_LIST", carTypeList);
        } catch (NamingException ex) {
            String msg = ex.getMessage();
            log("Error at LoadHomeServlet _ NamingException : " + msg);
        } catch (SQLException ex) {
            String msg = ex.getMessage();
            log("Error at LoadHomeServlet _ SQLException : " + msg);
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
