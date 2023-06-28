package com.project.order.service;

import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.project.order.Repository.OrderRepository;
import com.project.order.exception.OrderServiceCustomException;
import com.project.order.model.Order;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;



@Service
public  class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Override
    public long placeOrder(Order order) {
        System.out.println("Order is about to place");

        // Create order entity with status "CREATED"
        
        order.setOrderStatus("CREATED");
        order.setOrderDate(Instant.now());
        long p=order.getAmount();
       order.setAmount(p);
        order = orderRepository.save(order);

        String orderStatus;
        try {
            System.out.println(" Payment done Successfully. Changing the Order status to PLACED");
            orderStatus = "PLACED";
        } catch (Exception e) {
            System.out.println(" Error occurred in payment. Changing order status to PAYMENT_FAILED");
            orderStatus = "PAYMENT_FAILED";
        }

        order.setOrderStatus(orderStatus);
        orderRepository.save(order);

        System.out.println("Order Placed successfully with Order Id: " + order.getId());

        return order.getId();
    }

    @Override
    public Order getOrderDetails(long orderId) {
        System.out.println(" Get order details for Order Id: " + orderId);

        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (!orderOptional.isPresent()) {
            // Handle the case when the order is not found
            throw new OrderServiceCustomException("Order not found for the order Id:" + orderId,
                    "NOT_FOUND",
                    404);
        }
       Order order = orderOptional.get();
        return order;
    }
    
    @Override
    public Order updateOrder( Order order) {
    	 order.setOrderDate(Instant.now());
    	String orderStatus;
        try {
            System.out.println(" Order updated Successfully. Changing the Order status to UPDATED");
            orderStatus = "UPDATED";
        } catch (Exception e) {
            System.out.println(" Error occurred in payment. Changing order status to PAYMENT_FAILED");
            orderStatus = "PAYMENT_FAILED";
        }

        order.setOrderStatus(orderStatus);
    	return orderRepository.save(order);
    }
    
    @Override
    public void generateBill(Order order) {
        // Create a new document
        Document document = new Document();

        try {
            // Set the file path where the bill will be saved
//            String filePath = "path/to/save/bill.pdf";
        	String filePath = "D:\\SmartBuy\\OrderService4/bill.pdf";
            
            // Create a PDF writer to write the document to a file
            PdfWriter.getInstance(document, new FileOutputStream(filePath));

            // Open the document
            document.open();

            // Add content to the document
            document.add(new Paragraph("Order Details"));
            document.add(new Paragraph("Order ID: " + order.getId()));
            document.add(new Paragraph("Product ID: " + order.getProductId()));
            document.add(new Paragraph("Quantity: " + order.getQuantity()));
            document.add(new Paragraph("Order Date: " + order.getOrderDate()));
            document.add(new Paragraph("Order Status: " + order.getOrderStatus()));
            document.add(new Paragraph("Total Amount: " + order.getAmount()));

            // Close the document
            document.close();

            System.out.println("Bill generated successfully!");
        } catch (DocumentException e) {
            System.out.println("Error generating the bill: " + e.getMessage());
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }


}