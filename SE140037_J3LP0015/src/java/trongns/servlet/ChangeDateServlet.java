/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import trongns.carmodel.CarModelDAO;
import trongns.carmodel.CarModelDTO;
import trongns.cart.CartObj;
import trongns.type.TypeDAO;

/**
 *
 * @author TrongNS
 */
public class ChangeDateServlet extends HttpServlet {

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
        boolean valid = true;

        try {
            if (request.getParameterMap() == null || request.getParameterMap().isEmpty()) {
                return;
            }

            String id = request.getParameter("id");
            String suserRentalDate = request.getParameter("txtUserRentalDate");
            String suserRentalTime = request.getParameter("txtUserRentalTime");
            String suserReturnDate = request.getParameter("txtUserReturnDate");
            String suserReturnTime = request.getParameter("txtUserReturnTime");

            if (id != null && !id.trim().isEmpty()) {
                if (id.matches("\\d+")) {
                    HttpSession session = request.getSession(false);
                    if (session != null) {
                        CartObj cart = (CartObj) session.getAttribute("CART");
                        if (cart != null) {
                            HashMap<CarModelDTO, Integer> items = cart.getItems();
                            if (items != null) {

                                Date userRentalDate = new SimpleDateFormat("dd/MM/yyyy").parse(suserRentalDate);
                                Date userReturnDate = new SimpleDateFormat("dd/MM/yyyy").parse(suserReturnDate);

                                Calendar calendar1 = Calendar.getInstance();
                                calendar1.setTime(userRentalDate);
                                if (suserRentalTime != null && !suserRentalTime.trim().isEmpty()) {
                                    if (suserRentalTime.matches("\\d+")) {
                                        calendar1.add(Calendar.MINUTE, Integer.parseInt(suserRentalTime));
                                    } else {
                                        valid = false;
                                    }
                                } else {
                                    valid = false;
                                }
                                userRentalDate = calendar1.getTime();

                                Calendar calendar2 = Calendar.getInstance();
                                calendar2.setTime(userReturnDate);
                                if (suserReturnTime != null && !suserReturnTime.trim().isEmpty()) {
                                    if (suserReturnTime.matches("\\d+")) {
                                        calendar2.add(Calendar.MINUTE, Integer.parseInt(suserReturnTime));
                                    } else {
                                        valid = false;
                                    }
                                } else {
                                    valid = false;
                                }
                                
                                userReturnDate = calendar2.getTime();

                                long diffInMillies = userReturnDate.getTime() - userRentalDate.getTime();

                                String carName = "";

                                if (diffInMillies > 0 && valid) {
                                    long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                                    CarModelDTO oldCar = null;
                                    CarModelDTO newCar = null;

                                    CarModelDAO carModelDAO = new CarModelDAO();
                                    TypeDAO typeDAO = new TypeDAO();

                                    for (CarModelDTO dto : items.keySet()) {
                                        if (dto.getCarModelId() == Integer.parseInt(id)) {
                                            boolean result = carModelDAO.checkNotAvailableCarBetweenDates(dto.getCarModelId(), userRentalDate, userReturnDate);

                                            if (result) {
                                                valid = false;
                                                carName = dto.getCarName();
                                                break;
                                            } else {
                                                oldCar = dto;

                                                typeDAO.getTypeNameById(dto.getTypeId());
                                                dto.setTypeName(typeDAO.getTypeName());

                                                dto.setUserRentalDate(suserRentalDate);
                                                dto.setUserRentalTime(Integer.parseInt(suserRentalTime));
                                                dto.setUserReturnDate(suserReturnDate);
                                                dto.setUserReturnTime(Integer.parseInt(suserReturnTime));

                                                int days = (int) diff;
                                                dto.setRentDays(days + 1);

                                                BigDecimal total = dto.getPrice().multiply(BigDecimal.valueOf(diff + 1));
                                                dto.setUserPrice(total);

                                                newCar = dto;
                                                break;
                                            }
                                        }
                                    }

                                    if (valid) {
                                        if (oldCar != null) {
                                            url = "view-detail"
                                                    + "?id=" + id
                                                    + "&goBackPage=cart"
                                                    + "&rentalDate=" + suserRentalDate
                                                    + "&rentalTime=" + suserRentalTime
                                                    + "&returnDate=" + suserReturnDate
                                                    + "&returnTime=" + suserReturnTime
                                                    + "&status=success";

                                            cart.updateCarInCart(oldCar, newCar);
                                            session.setAttribute("CART", cart);
                                        }
                                    } else {
                                        url = "view-detail"
                                                + "?id=" + id
                                                + "&carName=" + carName
                                                + "&goBackPage=cart"
                                                + "&rentalDate=" + suserRentalDate
                                                + "&rentalTime=" + suserRentalTime
                                                + "&returnDate=" + suserReturnDate
                                                + "&returnTime=" + suserReturnTime
                                                + "&status=fail";
                                    }
                                } else {
                                    url = "view-detail"
                                            + "?id=" + id
                                            + "&carName=" + carName
                                            + "&goBackPage=cart"
                                            + "&rentalDate=" + suserRentalDate
                                            + "&rentalTime=" + suserRentalTime
                                            + "&returnDate=" + suserReturnDate
                                            + "&returnTime=" + suserReturnTime
                                            + "&change-status=fail";
                                }
                            }
                        }
                    }
                }
            }

        } catch (ParseException ex) {
            String msg = ex.getMessage();
            log("Error at ChangeDateServlet _ ParseException : " + msg);
        } catch (NamingException ex) {
            String msg = ex.getMessage();
            log("Error at ChangeDateServlet _ NamingException : " + msg);
        } catch (SQLException ex) {
            String msg = ex.getMessage();
            log("Error at ChangeDateServlet _ SQLException : " + msg);
        } finally {
            response.sendRedirect(url);
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
