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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import trongns.carmodel.CarModelDAO;
import trongns.carmodel.CarModelDTO;
import trongns.cart.CartDAO;
import trongns.cart.CartDTO;
import trongns.cart.CartObj;
import trongns.type.TypeDAO;

/**
 *
 * @author TrongNS
 */
public class ViewCartServlet extends HttpServlet {

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

        String url = "cart-page";
        boolean valid = true;

        try {
            HttpSession session = request.getSession(false);
            String user = null;
            String role = null;
            CartObj cart = null;

            if (session != null) {
                user = (String) session.getAttribute("USER");
                role = (String) session.getAttribute("ROLE");
                cart = (CartObj) session.getAttribute("CART");
            }

            CartDAO cartDAO = new CartDAO();
            CarModelDAO carModelDAO = new CarModelDAO();
            TypeDAO typeDAO = new TypeDAO();

            String failureMessage = null;
            SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");

            if (cart != null) {
                HashMap<CarModelDTO, Integer> items = cart.getItems();
                ArrayList<CarModelDTO> listChangedCar = null;
                if (items != null) {
                    for (CarModelDTO dto : items.keySet()) {
                        carModelDAO.loadCarModelDetail(dto.getCarModelId(), role);
                        CarModelDTO carModel = carModelDAO.getCarModelDetail();

                        if (carModel != null) {
                            typeDAO.getTypeNameById(dto.getTypeId());
                            carModel.setTypeName(typeDAO.getTypeName());

                            carModel.setUserRentalDate(dto.getUserRentalDate());
                            carModel.setUserReturnDate(dto.getUserReturnDate());
                            carModel.setUserRentalTime(dto.getUserRentalTime());
                            carModel.setUserReturnTime(dto.getUserReturnTime());
                            carModel.setRentDays(dto.getRentDays());
                            carModel.setUserPrice(dto.getUserPrice());

                            if (!carModel.equals(dto)) {
                                if (listChangedCar == null) {
                                    listChangedCar = new ArrayList<>();
                                }

                                listChangedCar.add(carModel);
                            }
                        } else {
                            failureMessage = "Sorry! There is a mistake, please comeback later!";
                            valid = false;
                            break;
                        }
                    }

                    if (valid) {
                        if (listChangedCar != null && !listChangedCar.isEmpty()) {
                            for (CarModelDTO newCar : listChangedCar) {
                                CarModelDTO oldCar = cart.getCarInCartById(newCar.getCarModelId());
                                cart.updateCarInCart(oldCar, newCar);
                            }

                            session.setAttribute("CART", cart);
                        }
                    }
                }

                if (user != null) {
                    boolean existed = cartDAO.checkExistedCustomerCart(user);
                    if (existed && session.getAttribute("FIRST_TIME_ACCESS") != null) {
                        cartDAO.getCartByUserId(user);
                        ArrayList<CartDTO> customerCart = cartDAO.getCustomerCart();

                        for (CartDTO dto : customerCart) {
                            carModelDAO.loadCarModelDetail(dto.getCarModelId(), role);
                            CarModelDTO carModel = carModelDAO.getCarModelDetail();

                            if (carModel != null) {
                                typeDAO.getTypeNameById(carModel.getTypeId());
                                carModel.setTypeName(typeDAO.getTypeName());
                                carModel.setUserRentalDate(dto.getUserRentalDate());
                                carModel.setUserRentalTime(dto.getUserRentalTime());

                                Date _userRentalDate = df1.parse(dto.getUserRentalDate());
                                int userRentalTime = dto.getUserRentalTime();
                                Calendar cal1 = Calendar.getInstance();
                                cal1.setTime(_userRentalDate);
                                cal1.add(Calendar.MINUTE, userRentalTime);
                                Date pickUpDate = cal1.getTime();

                                if (pickUpDate.getTime() < new Date().getTime()) {
                                    Calendar cal = Calendar.getInstance();
                                    cal.setTime(new Date());

                                    userRentalTime = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE);

                                    cal.set(Calendar.HOUR_OF_DAY, 0);
                                    cal.set(Calendar.MINUTE, 0);
                                    cal.set(Calendar.SECOND, 0);
                                    cal.set(Calendar.MILLISECOND, 0);
                                    pickUpDate = cal.getTime();

                                    String userRentalDate = df1.format(pickUpDate);

                                    carModel.setUserRentalDate(userRentalDate);
                                    carModel.setUserRentalTime(userRentalTime);
                                }

                                carModel.setUserReturnDate(dto.getUserReturnDate());
                                carModel.setUserReturnTime(dto.getUserReturnTime());
                                carModel.setRentDays(dto.getRentDays());
                                carModel.setUserPrice(dto.getUserPrice());

                                cart.loadAmountCarToCart(carModel, dto.getAmount());
                            } else {
                                valid = false;
                                failureMessage = "Sorry! There is a mistake, please comeback later!";
                                break;
                            }
                        }
                    }

                    if (valid) {
                        cartDAO.deleteCustomerCart(user);

                        items = cart.getItems();
                        if (items != null) {
                            for (CarModelDTO dto : items.keySet()) {
                                cartDAO.insertCustomerCart(new CartDTO(user, dto.getCarModelId(), items.get(dto), dto.getUserRentalDate(), dto.getUserRentalTime(), dto.getUserReturnDate(), dto.getUserReturnTime(), dto.getRentDays(), dto.getUserPrice()));
                            }
                        }

                        session.setAttribute("CART", cart);
                    } else {
                        request.setAttribute("CHECKOUT_FAIL", failureMessage);
                    }

                    if (session.getAttribute("FIRST_TIME_ACCESS") != null) {
                        session.removeAttribute("FIRST_TIME_ACCESS");
                    }
                }
            } else {
                cart = new CartObj();
                if (user != null) {
                    boolean existed = cartDAO.checkExistedCustomerCart(user);
                    if (existed && session.getAttribute("FIRST_TIME_ACCESS") != null) {
                        cartDAO.getCartByUserId(user);
                        ArrayList<CartDTO> customerCart = cartDAO.getCustomerCart();

                        if (customerCart != null) {
                            for (CartDTO dto : customerCart) {
                                carModelDAO.loadCarModelDetail(dto.getCarModelId(), role);
                                CarModelDTO carModel = carModelDAO.getCarModelDetail();

                                if (carModel != null) {
                                    typeDAO.getTypeNameById(carModel.getTypeId());
                                    carModel.setTypeName(typeDAO.getTypeName());
                                    carModel.setUserRentalDate(dto.getUserRentalDate());
                                    carModel.setUserRentalTime(dto.getUserRentalTime());

                                    Date _userRentalDate = df1.parse(dto.getUserRentalDate());
                                    int userRentalTime = dto.getUserRentalTime();
                                    Calendar cal1 = Calendar.getInstance();
                                    cal1.setTime(_userRentalDate);
                                    cal1.add(Calendar.MINUTE, userRentalTime);
                                    Date pickUpDate = cal1.getTime();

                                    if (pickUpDate.getTime() < new Date().getTime()) {
                                        Calendar cal = Calendar.getInstance();
                                        cal.setTime(new Date());

                                        userRentalTime = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE);

                                        cal.set(Calendar.HOUR_OF_DAY, 0);
                                        cal.set(Calendar.MINUTE, 0);
                                        cal.set(Calendar.SECOND, 0);
                                        cal.set(Calendar.MILLISECOND, 0);
                                        pickUpDate = cal.getTime();

                                        String userRentalDate = df1.format(pickUpDate);

                                        carModel.setUserRentalDate(userRentalDate);
                                        carModel.setUserRentalTime(userRentalTime);
                                    }

                                    carModel.setUserReturnDate(dto.getUserReturnDate());
                                    carModel.setUserReturnTime(dto.getUserReturnTime());
                                    carModel.setRentDays(dto.getRentDays());
                                    carModel.setUserPrice(dto.getUserPrice());

                                    cart.loadAmountCarToCart(carModel, dto.getAmount());
                                } else {
                                    valid = false;
                                    break;
                                }
                            }
                        }

                        if (valid) {
                            session.setAttribute("CART", cart);
                        } else {
                            cartDAO.deleteCustomerCart(user);
                        }
                    }
                }
            }

            if (session != null) {
                if (session.getAttribute("RENTING_INFO") != null) {
                    session.removeAttribute("RENTING_INFO");
                }

                if (session.getAttribute("FIRST_TIME_ACCESS") != null) {
                    session.removeAttribute("FIRST_TIME_ACCESS");
                }
            }

        } catch (ParseException ex) {
            String msg = ex.getMessage();
            log("Error at ViewCartServlet _ ParseException : " + msg);
        } catch (NamingException ex) {
            String msg = ex.getMessage();
            log("Error at ViewCartServlet _ NamingException : " + msg);
        } catch (SQLException ex) {
            String msg = ex.getMessage();
            log("Error at ViewCartServlet _ SQLException : " + msg);
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
