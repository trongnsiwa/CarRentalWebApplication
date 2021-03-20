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
import java.sql.Timestamp;
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
import trongns.car.CarDAO;
import trongns.car.CarDTO;
import trongns.cart.CartDAO;
import trongns.cart.CartObj;
import trongns.discount.DiscountDAO;
import trongns.discount.DiscountDTO;
import trongns.order.OrderDAO;
import trongns.order.OrderDTO;
import trongns.ordercardetail.OrderCarDetailDAO;
import trongns.ordercardetail.OrderCarDetailDTO;
import trongns.orderdetail.OrderDetailDAO;
import trongns.orderdetail.OrderDetailDTO;

/**
 *
 * @author TrongNS
 */
public class CheckOutServlet extends HttpServlet {

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
        boolean complete = false;

        HttpSession session = request.getSession();
        try {
            String user = (String) session.getAttribute("USER");
            if (user != null) {
                CartObj cart = (CartObj) session.getAttribute("CART");
                if (cart != null) {
                    HashMap<CarModelDTO, Integer> items = cart.getItems();
                    if (items != null) {
                        String confirm = request.getParameter("confirm");

                        if (confirm == null) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date rentalDate = new Date();
                            rentalDate = dateFormat.parse(dateFormat.format(rentalDate));

                            String orderId = null;
                            String carName, imageLink;
                            int carModelId, quantity, rentingAmout;
                            BigDecimal price, subtotal;
                            BigDecimal totalPrice = BigDecimal.ZERO;
                            Date pickUpDate, dropOffDate;
                            int days;

                            ArrayList<OrderDetailDTO> orderList = new ArrayList<>();

                            boolean isFirst = true;
                            String warningOutOfStock = null;
                            String failureMessage = null;

                            OrderDAO orderDAO = new OrderDAO();
                            OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
                            CarModelDAO carModelDAO = new CarModelDAO();
                            CarDAO carDAO = new CarDAO();

                            SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");

                            for (CarModelDTO dto : items.keySet()) {
                                carModelId = dto.getCarModelId();
                                carName = dto.getCarName();
                                imageLink = dto.getSmallImageLink();
                                price = dto.getUserPrice();

                                Date _userRentalDate = df1.parse(dto.getUserRentalDate());
                                int userRentalTime = dto.getUserRentalTime();
                                Calendar cal1 = Calendar.getInstance();
                                cal1.setTime(_userRentalDate);
                                cal1.add(Calendar.MINUTE, userRentalTime);
                                pickUpDate = cal1.getTime();
                                
                                Date _userReturnDate = df1.parse(dto.getUserReturnDate());
                                int userReturnTime = dto.getUserReturnTime();
                                Calendar cal2 = Calendar.getInstance();
                                cal2.setTime(_userReturnDate);
                                cal2.add(Calendar.MINUTE, userReturnTime);
                                dropOffDate = cal2.getTime();

                                days = dto.getRentDays();

                                rentingAmout = items.get(dto);

                                carModelDAO.getQuantityOfCarModelById(carModelId);
                                quantity = carModelDAO.getQuantity();

                                orderDetailDAO.loadCarModelBetweenDates(pickUpDate, dropOffDate);
                                HashMap<Integer, Integer> carModelBetweenDatesList = orderDetailDAO.getListCarModelBetweenDates();

                                if (carModelBetweenDatesList != null) {
                                    if (carModelBetweenDatesList.containsKey(carModelId)) {
                                        int rentedAmount = carModelBetweenDatesList.get(carModelId);
                                        quantity -= rentedAmount;
                                    }
                                }

                                subtotal = price.multiply(new BigDecimal(rentingAmout));
                                totalPrice = totalPrice.add(subtotal);

                                if (rentingAmout > quantity) {
                                    if (isFirst) {
                                        warningOutOfStock = carName + " (" + quantity + " left)";
                                        isFirst = false;
                                    } else {
                                        warningOutOfStock += ", " + carName + " (" + quantity + " left)";
                                    }
                                }

                                if (warningOutOfStock == null) {
                                    if (orderId == null) {
                                        orderId = createOrderId(14);
                                        boolean duplicated = orderDAO.checkExistedOrderId(orderId);
                                        while (duplicated) {
                                            orderId = createOrderId(14);
                                            duplicated = orderDAO.checkExistedOrderId(orderId);
                                        }
                                    }

                                    carDAO.loadCarByCarModelId(carModelId, rentingAmout, pickUpDate, dropOffDate);
                                    ArrayList<CarDTO> carList = carDAO.getCarList();

                                    dropOffDate = new Timestamp(dropOffDate.getTime());
                                    pickUpDate = new Timestamp(pickUpDate.getTime());

                                    OrderDetailDTO detail = new OrderDetailDTO(orderId, carModelId, carName, imageLink, rentingAmout, price, pickUpDate, dropOffDate, days);
                                    
                                    if (carList != null) {
                                        if (carList.size() != rentingAmout) {
                                            failureMessage = "Sorry! There is a mistake, please comeback later!";
                                            break;
                                        }

                                        ArrayList<OrderCarDetailDTO> orderCarDetailList = new ArrayList<>();

                                        for (CarDTO carDetailDTO : carList) {
                                            OrderCarDetailDTO orderCarDetailDTO = new OrderCarDetailDTO(orderId, carModelId, carDetailDTO.getLicensePlates(), null);
                                            orderCarDetailList.add(orderCarDetailDTO);
                                        }

                                        detail.setOrderCarDetailList(orderCarDetailList);
                                    } else {
                                        failureMessage = "Sorry! There is a mistake, please comeback later!";
                                        break;
                                    }

                                    orderList.add(detail);
                                }
                            }

                            if (warningOutOfStock != null) {
                                valid = false;
                                request.setAttribute("CHECKOUT_FAIL", warningOutOfStock);
                                return;
                            }

                            String discountCode = null;
                            if (cart.getDiscount() != null) {
                                discountCode = cart.getDiscount().getCode();

                                DiscountDAO discountDAO = new DiscountDAO();
                                discountDAO.checkAvailableDiscount(discountCode);
                                DiscountDTO discount = discountDAO.getDiscount();

                                if (discount != null) {
                                    if (discount.getPercent() != cart.getDiscount().getPercent()) {
                                        cart.applyDiscount(discount);
                                    }

                                    totalPrice = totalPrice.subtract(totalPrice.multiply(BigDecimal.valueOf(cart.getDiscount().getPercent())).divide(BigDecimal.valueOf(100)));
                                } else {
                                    failureMessage = "Sorry! There is a mistake, please comeback later!";
                                }
                            }

                            if (failureMessage != null) {
                                valid = false;
                                request.setAttribute("CHECKOUT_FAIL", failureMessage);
                            }

                            if (valid) {
                                OrderDTO order = new OrderDTO(orderId, user, rentalDate, discountCode, totalPrice, true);
                                order.setRentingItems(orderList);
                                if (discountCode != null) {
                                    order.setDiscountPercent(cart.getDiscount().getPercent());
                                }
                                session.setAttribute("ORDER_INFO", order);
                            }

                        } else {
                            OrderDTO order = (OrderDTO) session.getAttribute("ORDER_INFO");
                            if (order != null) {
                                OrderDAO orderDAO = new OrderDAO();
                                boolean result = orderDAO.insertOrder(order);

                                if (result) {
                                    OrderDetailDAO detailDAO = new OrderDetailDAO();
                                    OrderCarDetailDAO rentCarDetailDAO = new OrderCarDetailDAO();

                                    for (OrderDetailDTO detail : order.getRentingItems()) {
                                        detail.setRenting(true);
                                        result = detailDAO.insertOrderDetail(detail);

                                        if (result) {
                                            for (OrderCarDetailDTO carDetail : detail.getOrderCarDetailList()) {
                                                result = rentCarDetailDAO.insertOrderCarDetail(carDetail);
                                            }
                                        }

                                        if (result) {
                                            complete = true;
                                            url = "thank-you-page";
                                            session.removeAttribute("CART");

                                            CartDAO cartDAO = new CartDAO();
                                            cartDAO.deleteCustomerCart(user);
                                        } else {
                                            valid = false;
                                            request.setAttribute("CHECKOUT_FAIL", "Sorry! There is a mistake, you cannot complete the order now!");
                                        }
                                    }
                                }

                            }
                        }
                    }
                }
            }
        } catch (ParseException ex) {
            String msg = ex.getMessage();
            log("Error at CheckOutServlet _ ParseException : " + msg);
        } catch (NamingException ex) {
            String msg = ex.getMessage();
            log("Error at CheckOutServlet _ NamingException : " + msg);
        } catch (SQLException ex) {
            String msg = ex.getMessage();
            log("Error at CheckOutServlet _ SQLException : " + msg);
        } finally {
            if (!valid) {
                if (session.getAttribute("ORDER_INFO") != null) {
                    session.removeAttribute("ORDER_INFO");
                }
            }
            if (!complete || !valid) {
                HashMap<String, String> mappingList = (HashMap<String, String>) request.getServletContext().getAttribute("MAPPING_LIST");
                if (mappingList != null) {
                    url = mappingList.get(url);
                }
                request.getRequestDispatcher(url).forward(request, response);
            } else {
                response.sendRedirect(url);
            }
            out.close();
        }
    }

    private String createOrderId(int size) {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
        String date = dateFormat.format(now);

        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder(size);

        for (int i = 0; i < size - 6; i++) {
            int index = (int) (letters.length() * Math.random());
            sb.append(letters.charAt(index));
        }

        return date + sb.toString();
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
