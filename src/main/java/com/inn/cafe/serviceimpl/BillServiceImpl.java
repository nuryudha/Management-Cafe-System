package com.inn.cafe.serviceimpl;

import com.inn.cafe.constents.CafeConstant;
import com.inn.cafe.dao.BillDao;
import com.inn.cafe.jwt.JwtFilter;
import com.inn.cafe.pojo.Bill;
import com.inn.cafe.service.BillService;
import com.inn.cafe.utils.CafeUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.util.Map;

@Slf4j
@Service
public class BillServiceImpl implements BillService {

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    BillDao billDao;

    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
        log.info("Inside generateReport");
        try {
            String fileName;
            if (validateRequestMap(requestMap)) {
                if (requestMap.containsKey("isGenerate") && !(Boolean) requestMap.get("isGenerate")) {
                    fileName = (String) requestMap.get("uuid");
                } else {
                    fileName = CafeUtils.getUUID();
                    requestMap.put("uuid", fileName);
                    insertBill(requestMap);
                }
                String data = "Name : " + requestMap.get("name") +
                        "\n" + "Contact Number : " + requestMap.get("contactNumber") +
                        "\n" + "Email : " + requestMap.get("email") +
                        "\n" + "Payment Method : " + requestMap.get("paymentMethod");

                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(CafeConstant.STORE_LOCATION + "\\" + fileName + ".pdf"));

                document.open();
                setRectangleInPdf(document);
//                TODO : IN HERE
                Paragraph chunk = new Paragraph("Cafe");
            }
            return CafeUtils.getResponseEntity("Required data not found", HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void setRectangleInPdf(Document document) throws DocumentException {
        log.info("Inside setRectangleInPdf");
        Rectangle react = new Rectangle(577, 825,18,15);
        react.enableBorderSide(1);
        react.enableBorderSide(2);
        react.enableBorderSide(4);
        react.enableBorderSide(8);
        react.setBackgroundColor(BaseColor.BLACK);
        react.setBorderWidth(1);
        document.add(react);
    }

    private void insertBill(Map<String, Object> requestMap) {
        try {
            Bill bill = new Bill();
            bill.setUuid((String) requestMap.get("uuid"));
            bill.setName((String) requestMap.get("name"));
            bill.setEmail((String) requestMap.get("email"));
            bill.setContactNumber((String) requestMap.get("contactNumber"));
            bill.setPaymentMethod((String) requestMap.get("paymentMethod"));
            bill.setTotal(String.valueOf(Integer.parseInt((String) requestMap.get("totalAmount"))));
            bill.setProductDetails((String) requestMap.get("productDetails"));
            bill.setCreatedBy(jwtFilter.getCurrentUser());
            billDao.save(bill);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean validateRequestMap(Map<String, Object> requestMap) {
        return requestMap.containsKey("name") &&
                requestMap.containsKey("contactNumber") &&
                requestMap.containsKey("email") &&
                requestMap.containsKey("paymentMethod") &&
                requestMap.containsKey("productDetails") &&
                requestMap.containsKey("totalAmount");
    }
}
