package com.user.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.DAO.BookOrderImpl;
import com.DAO.CartDAOImpl;
import com.DB.DBConnect;
import com.entity.Book_Order;
import com.entity.Cart;
@WebServlet("/order")
public class OrderServlet extends HttpServlet{

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			int id=Integer.parseInt(req.getParameter("id"));
			String name=req.getParameter("username");
			String email=req.getParameter("email");
			String phno=req.getParameter("phno");
			String address=req.getParameter("address");
			String landmark=req.getParameter("landmark");
			String city=req.getParameter("city");
			String state=req.getParameter("state");
			String pincode=req.getParameter("pincode");
			String paymentType=req.getParameter("payment");
			String fullAdd=address+","+landmark+","+city+","+state+","+pincode;
			CartDAOImpl dao=new CartDAOImpl(DBConnect.getConn());
			List<Cart> blist=dao.getBookByUser(id);
			BookOrderImpl dao2=new BookOrderImpl(DBConnect.getConn());
			HttpSession session=req.getSession();
		    int i=dao2.getOrderNo();
			Book_Order o=new Book_Order();
			ArrayList<Book_Order> orderlist=new ArrayList<Book_Order>();
			for(Cart c:blist)
			{
				o.setOrderId("BOOK-ORD-00");
				o.setUserName(name);
				o.setEmail(email);
				o.setPhno(phno);
				o.setFulladd(fullAdd);
				o.setBookName(c.getBookName());
				o.setAuthor(c.getAuthor());
				o.setPrice(c.getPrice());
				o.setPaymentType(paymentType);
				orderlist.add(o);
				i++;
			}
			if("noselect".equals(paymentType))
			{
				session.setAttribute("failedMsg", "choose payment method");
				resp.sendRedirect("checkout.jsp");
			}else {
				boolean f=dao2.saveOrder(orderlist);
				if(f)
				{
					resp.sendRedirect("order_success.jsp");
				}else
				{
					session.setAttribute("failedMsg", "Order Failed");
					resp.sendRedirect("checkout.jsp");
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	
}
