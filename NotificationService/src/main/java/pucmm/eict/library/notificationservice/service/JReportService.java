package pucmm.eict.library.notificationservice.service;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import pucmm.eict.library.notificationservice.client.CatalogueClient;
import pucmm.eict.library.notificationservice.client.UserClient;
import pucmm.eict.library.notificationservice.dto.*;

import java.awt.print.Book;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JReportService {

    private final CatalogueClient catalogueClient;
    private final UserClient userClient;
    private final ResourceLoader resourceLoader;

    public JReportService(CatalogueClient catalogueClient, UserClient userClient, @Qualifier("webApplicationContext") ResourceLoader resourceLoader) {
        this.catalogueClient = catalogueClient;
        this.userClient = userClient;
        this.resourceLoader = resourceLoader;
    }

    public byte[] exportReport(OrderDTO orderDTO) throws IOException, JRException {
        try {
            Resource resource = resourceLoader.getResource("classpath:invoice.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(resource.getInputStream());

            UserDTO userDTO = userClient.getUserById(orderDTO.getUserId());

            List<CartItemReportDTO> cartItemReportDTOs = new ArrayList<>();

            orderDTO.getCartItems().forEach(
                    cartItemDTO -> {
                        CartItemReportDTO cartItemReportDTO = new CartItemReportDTO();
                        BookDTO bookDTO = catalogueClient.getBook(cartItemDTO.getBookId());

                        cartItemReportDTO.setTitle(bookDTO.getTitle());
                        cartItemReportDTO.setAuthor(bookDTO.getAuthor());
                        cartItemReportDTO.setPrice(bookDTO.getPrice());
                        cartItemReportDTO.setQuantity(cartItemDTO.getQuantity());
                        cartItemReportDTO.setTotalPrice(bookDTO.getPrice() * cartItemDTO.getQuantity());
                        cartItemReportDTOs.add(cartItemReportDTO);
                    }
            );

            System.out.println(cartItemReportDTOs);

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(cartItemReportDTOs);

            // Parameters for the report
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("orderId", orderDTO.getId());
            parameters.put("userId", orderDTO.getUserId());
            parameters.put("username", userDTO.getUsername());
            parameters.put("email", userDTO.getEmail());
            parameters.put("totalPrice", orderDTO.getTotalPrice());
            parameters.put("address", orderDTO.getAddress());
            parameters.put("paymentMethod", orderDTO.getPaymentMethod());
            parameters.put("billingAddress", orderDTO.getBillingAddress());
            parameters.put("deliveryMethod", orderDTO.getDeliveryMethod());
            parameters.put("status", orderDTO.getStatus());
            parameters.put("createDate", orderDTO.getCreateDate());

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            System.out.println("JasperPrint created successfully");

            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Error loading JRXML file", e);
        } catch (JRException e) {
            e.printStackTrace();
            throw new JRException("Error compiling or filling report", e);
        }
    }
}
