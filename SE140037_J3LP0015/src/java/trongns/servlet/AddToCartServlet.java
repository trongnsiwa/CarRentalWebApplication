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
public class AddToCartServlet extends HttpServlet {

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

        String url = "home";
        boolean valid = true;
        try {
            if (request.getParameterMap() == null || request.getParameterMap().isEmpty()) {
                return;
            }

            String keyword = request.getParameter("txtCarName");
            String carType = request.getParameter("txtCarType");
            String srentalDate = request.getParameter("txtRentalDate");
            String srentalTime = request.getParameter("txtRentalTime");
            String sreturnDate = request.getParameter("txtReturnDate");
            String sreturnTime = request.getParameter("txtReturnTime");
            String samount = request.getParameter("txtAmountOfCar");
            String spageSize = request.getParameter("pageSize");
            String spageNo = request.getParameter("pageNo");

            String suserRentalDate = request.getParameter("txtUserRentalDate");
            String suserRentalTime = request.getParameter("txtUserRentalTime");
            String suserReturnDate = request.getParameter("txtUserReturnDate");
            String suserReturnTime = request.getParameter("txtUserReturnTime");

            String goBackPage = request.getParameter("goBackPage");

            if ("search".equals(goBackPage)) {
                url = "search"
                        + "?txtCarName=" + keyword
                        + "&txtCarType=" + carType
                        + "&txtRentalDate=" + srentalDate
                        + "&txtRentalTime=" + srentalTime
                        + "&txtReturnDate=" + sreturnDate
                        + "&txtReturnTime=" + sreturnTime
                        + "&txtAmountOfCar=" + samount
                        + "&pageSize=" + spageSize
                        + "&pageNo=" + spageNo;
            }

            HttpSession session = request.getSession();
            String role = (String) session.getAttribute("ROLE");
            CartObj cart = (CartObj) session.getAttribute("CART");
            if (cart == null) {
                cart = new CartObj();
            }

            String id = request.getParameter("id");

            if (id != null && !id.trim().isEmpty()) {
                if (id.matches("\\d+")) {

                    Date userRentalDate = new SimpleDateFormat("dd/MM/yyyy").parse(suserRentalDate);
                    Date userReturnDate = new SimpleDateFormat("dd/MM/yyyy").parse(suserReturnDate);

                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.setTime(userRentalDate);
                    if (suserRentalTime != null && !suserRentalTime.trim().isEmpty()) {
                        if (suserRentalTime.matches("\\d+")) {
                            calendar1.add(Calendar.MINUTE, Integer.parseInt(suserRentalTime));
                        }
                    }
                    userRentalDate = calendar1.getTime();

                    Calendar calendar2 = Calendar.getInstance();
                    calendar2.setTime(userReturnDate);
                    if (suserReturnTime != null && !suserReturnTime.trim().isEmpty()) {
                        if (suserReturnTime.matches("\\d+")) {
                            calendar2.add(Calendar.MINUTE, Integer.parseInt(suserReturnTime));
                        }
                    }
                    userReturnDate = calendar2.getTime();

                    long diffInMillies = userReturnDate.getTime() - userRentalDate.getTime();

                    if (diffInMillies > 0) {
                        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                        CarModelDAO carModelDAO = new CarModelDAO();
                        carModelDAO.loadCarModelDetail(Integer.parseInt(id), role);
                        CarModelDTO dto = carModelDAO.getCarModelDetail();

                        if (dto != null) {
                            TypeDAO typeDAO = new TypeDAO();
                            typeDAO.getTypeNameById(dto.getTypeId());
                            dto.setTypeName(typeDAO.getTypeName());

                            dto.setUserRentalDate(suserRentalDate);
                            dto.setUserRentalTime(Integer.parseInt(suserRentalTime));
                            dto.setUserReturnDate(suserReturnDate);
                            dto.setUserReturnTime(Integer.parseInt(suserReturnTime));

                            int days = (int) diff;
                            days += 1;
                            dto.setRentDays(days);

                            BigDecimal total = dto.getPrice().multiply(BigDecimal.valueOf(days));
                            dto.setUserPrice(total);

                            CarModelDTO oldCar = cart.getCarInCartById(dto.getCarModelId());
                            if (oldCar != null) {
                                if (oldCar.getUserRentalDate().compareTo(suserRentalDate) != 0
                                        || oldCar.getUserReturnDate().compareTo(suserReturnDate) != 0) {
                                    cart.updateCarInCart(oldCar, dto);
                                }
                            }

                            cart.addCarToCart(dto);
                            session.setAttribute("CART", cart);
                        }
                    } else {
                        valid = false;
                        url = "view-detail?"
                                + "id=" + id
                                + "&goBackPage=search"
                                + "&txtCarName=" + keyword
                                + "&txtCarType=" + carType
                                + "&txtRentalDate=" + srentalDate
                                + "&txtRentalTime=" + srentalTime
                                + "&txtReturnDate=" + sreturnDate
                                + "&txtReturnTime=" + sreturnTime
                                + "&txtAmountOfCar=" + samount
                                + "&pageSize=" + spageSize
                                + "&pageNo=" + spageNo
                                + "&add-status=fail";
                    }
                }
            }

        } catch (ParseException ex) {
            String msg = ex.getMessage();
            log("Error at AddToCartServlet _ ParseException : " + msg);
        } catch (NamingException ex) {
            String msg = ex.getMessage();
            log("Error at AddToCartServlet _ NamingException : " + msg);
        } catch (SQLException ex) {
            String msg = ex.getMessage();
            log("Error at AddToCartServlet _ SQLException : " + msg);
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
