package springboot.Pure.Goat.Farms;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// iText imports
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.Font.FontFamily;

@Controller
public class payment_con {
	  @Autowired
	    private payment_repo paymentRepo;

	    @GetMapping("/payment")
	    public String payment() {
	        return "payment";
	    }

	    @GetMapping("/payment_add")
	    public String showPaymentForm(Model model) {
	        payment_model payment = new payment_model();
	        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

	        payment.setOdate(LocalDate.now().format(dateFormatter));
	        payment.setOtime(LocalTime.now().format(timeFormatter));
	        payment.setDdate(LocalDate.now().plusDays(3).format(dateFormatter));

	        model.addAttribute("payment", payment);
	        return "payment_add";
	    }

	    @PostMapping("/payment")
	    public String insert_payment(
	            @RequestParam("name") String name,
	            @RequestParam("mobile") String mobile,
	            @RequestParam("email") String email,
	            @RequestParam("address") String address,
	            @RequestParam("purpose") String purpose,
	            @RequestParam("gender") String gender,
	            @RequestParam("qty") String qty,
	            @RequestParam("ctype") String ctype,
	            @RequestParam("otype") String otype,
	            @RequestParam("pmethod") String pmethod,
	            @RequestParam("odate") String odate,
	            @RequestParam("otime") String otime,
	            @RequestParam("ddate") String ddate,
	            @RequestParam("feedback") String feedback,
	            @RequestParam("total") String total,
	            Model m) {

	        try {
	            if (paymentRepo.findById(name).isPresent()) {
	                m.addAttribute("err", "Payment record already exists");
	                return "payment_add";
	            }

	            payment_model payment = new payment_model(name, mobile, email, address, purpose,
	                    gender, qty, ctype, otype, pmethod,
	                    odate, otime, ddate, feedback, total);
	            paymentRepo.save(payment);
	            m.addAttribute("msg", "Payment processed successfully");
	            return "redirect:/payment_add";
	        } catch (Exception e) {
	            m.addAttribute("err", "Payment failed: " + e.getMessage());
	            return "payment_add";
	        }
	    }

	    @PostMapping("/payment_add")
	    public String insertPayment(@Valid @ModelAttribute("payment") payment_model payment,
	                                BindingResult result, Model model) {
	        if (result.hasErrors()) {
	            return "payment_add";
	        }

	        if (paymentRepo.existsById(payment.getName())) {
	            model.addAttribute("err", "Payment with this name already exists.");
	            return "payment_add";
	        }

	        try {
	            if (payment.getTotal() == null || payment.getTotal().isEmpty()) {
	                try {
	                    int qty = Integer.parseInt(payment.getQty());
	                    payment.setTotal(String.valueOf(qty * 100));
	                } catch (NumberFormatException e) {
	                    payment.setTotal("0");
	                }
	            }
	            paymentRepo.save(payment);
	            model.addAttribute("msg", "Payment record added successfully!");
	            return "redirect:/payment_add";
	        } catch (Exception e) {
	            model.addAttribute("err", "Payment addition failed: " + e.getMessage());
	            return "payment_add";
	        }
	    }

	    @GetMapping({"/view_payments", "/payment_list"})
	    public String viewAllPayments(Model model) {
	        model.addAttribute("payments", paymentRepo.findAll());
	        return "payment_list";
	    }

	    @GetMapping("/payment_search")
	    public String showSearchPaymentPage(Model model) {
	        model.addAttribute("payments", paymentRepo.findAll());
	        return "payment_search";
	    }

	    @GetMapping("/payment_search1")
	    public String searchPayments(@RequestParam(value = "name", required = false) String name, Model model) {
	        List<payment_model> allPayments = paymentRepo.findAll();
	        List<payment_model> filtered = new ArrayList<>();

	        if (name != null && !name.trim().isEmpty()) {
	            String searchTerm = name.trim().toLowerCase();
	            for (payment_model p : allPayments) {
	                if (p.getName() != null && p.getName().toLowerCase().contains(searchTerm)) {
	                    filtered.add(p);
	                }
	            }
	        } else {
	            filtered = allPayments;
	        }

	        model.addAttribute("payments", filtered);
	        model.addAttribute("searchTerm", name);
	        return "payment_search";
	    }

	    private String deletePaymentLogic(String name, RedirectAttributes re) {
	        try {
	            String cleanName = name.split(",")[0].trim();
	            if (!paymentRepo.existsById(cleanName)) {
	                re.addFlashAttribute("err", "Payment not found with name: " + cleanName);
	            } else {
	                paymentRepo.deleteById(cleanName);
	                re.addFlashAttribute("msg", "Payment deleted successfully!");
	            }
	        } catch (Exception e) {
	            re.addFlashAttribute("err", "Unable to delete the payment: " + e.getMessage());
	        }
	        return "redirect:/view_payments";
	    }

	    @GetMapping("/deletePayment")
	    public String deletePaymentGet(@RequestParam("name") String name, RedirectAttributes re) {
	        return deletePaymentLogic(name, re);
	    }

	    @PostMapping("/deletePayment")
	    public String deletePaymentPost(@RequestParam("name") String name, RedirectAttributes re) {
	        return deletePaymentLogic(name, re);
	    }

	    @GetMapping("/payment_pdf")
	    public String payment_pdf() {
	        return "payment_pdf";
	    }

	    // ================== PDF GENERATION (All Fields) ==================
	    @GetMapping("/generate_pdf")
	    public void exportToPDF(@RequestParam("name") String name, 
	                            HttpServletResponse response) throws IOException, DocumentException {
	        
	        String cleanName = name.split(",")[0].trim();
	        payment_model payment = paymentRepo.findById(cleanName).orElse(null);

	        if (payment == null) {
	            response.sendError(HttpServletResponse.SC_NOT_FOUND, 
	                               "Payment with name: " + cleanName + " not found");
	            return;
	        }

	        response.setContentType("application/pdf");
	        response.setHeader("Content-Disposition", 
	            "attachment; filename=\"invoice_" + payment.getName().replaceAll("\\s+", "_") + ".pdf\"");

	        Document document = new Document(PageSize.A4);
	        PdfWriter.getInstance(document, response.getOutputStream());
	        document.open();

	        // -------- Company Header ----------
	        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, new BaseColor(46, 125, 50));
	        Paragraph company = new Paragraph("PURE GOAT FARM", headerFont);
	        company.setAlignment(Element.ALIGN_CENTER);
	        document.add(company);

	        Font addressFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.DARK_GRAY);
	       
	   

	        // Separator line
	        PdfPTable line = new PdfPTable(1);
	        line.setWidthPercentage(100);
	        PdfPCell lineCell = new PdfPCell(new Phrase(" "));
	        lineCell.setBorder(Rectangle.BOTTOM);
	        lineCell.setBorderColor(new BaseColor(46, 125, 50));
	        lineCell.setBorderWidth(2);
	        lineCell.setPaddingBottom(5);
	        line.addCell(lineCell);
	        document.add(line);

	        // -------- Invoice Title & Number ----------
	        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.DARK_GRAY);
	        Paragraph invoiceTitle = new Paragraph("TAX INVOICE", titleFont);
	        invoiceTitle.setAlignment(Element.ALIGN_CENTER);
	        invoiceTitle.setSpacingAfter(10);
	        document.add(invoiceTitle);

	        PdfPTable invoiceInfo = new PdfPTable(2);
	        invoiceInfo.setWidthPercentage(100);
	        invoiceInfo.setSpacingAfter(15);

	        Font infoFont = FontFactory.getFont(FontFactory.HELVETICA, 11);

	        PdfPCell leftCell = new PdfPCell(new Phrase("Invoice No: " + System.currentTimeMillis(), infoFont));
	        leftCell.setBorder(Rectangle.NO_BORDER);
	        leftCell.setHorizontalAlignment(Element.ALIGN_LEFT);
	        invoiceInfo.addCell(leftCell);

	        PdfPCell rightCell = new PdfPCell(new Phrase("Date: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), infoFont));
	        rightCell.setBorder(Rectangle.NO_BORDER);
	        rightCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        invoiceInfo.addCell(rightCell);

	        document.add(invoiceInfo);

	        // -------- Customer Details (All fields: Name, Mobile, Email, Address, Gender, Customer Type, Order Type) ----------
	        Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, new BaseColor(46, 125, 50));
	        Paragraph custTitle = new Paragraph("Customer Details", sectionFont);
	        custTitle.setSpacingBefore(5);
	        custTitle.setSpacingAfter(5);
	        document.add(custTitle);

	        PdfPTable custTable = new PdfPTable(2);
	        custTable.setWidthPercentage(100);
	        custTable.setSpacingAfter(15);
	        custTable.setWidths(new float[]{1, 2});

	        addInfoRow(custTable, "Name :", payment.getName(), false);
	        addInfoRow(custTable, "Mobile :", payment.getMobile(), true);
	        addInfoRow(custTable, "Email :", payment.getEmail(), false);
	        addInfoRow(custTable, "Address :", payment.getAddress(), true);
	        addInfoRow(custTable, "Customer Type :", payment.getCtype(), true);
	        addInfoRow(custTable, "Order Type :", payment.getOtype(), false);
	        document.add(custTable);

	        // -------- Order Items Table (Purpose, Qty, Unit Price, Total) ----------
	        Paragraph itemsTitle = new Paragraph("Order Details", sectionFont);
	        itemsTitle.setSpacingBefore(5);
	        itemsTitle.setSpacingAfter(5);
	        document.add(itemsTitle);

	        PdfPTable itemTable = new PdfPTable(4);
	        itemTable.setWidthPercentage(100);
	        itemTable.setSpacingAfter(15);
	        itemTable.setWidths(new float[]{3, 1, 1, 1}); // Description, Qty, Unit Price, Total

	        // Table Header
	        Font thFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
	        BaseColor thColor = new BaseColor(46, 125, 50);
	        addTableCell(itemTable, "Description", thFont, thColor);
	        addTableCell(itemTable, "Qty (Kg)", thFont, thColor);
	        addTableCell(itemTable, "Unit Price (₹)", thFont, thColor);
	        addTableCell(itemTable, "Total (₹)", thFont, thColor);

	        // Null-safe parsing for numeric fields
	        double qty = 0, total = 0;
	        try {
	            qty = payment.getQty() != null ? Double.parseDouble(payment.getQty()) : 0;
	            total = payment.getTotal() != null ? Double.parseDouble(payment.getTotal()) : 0;
	        } catch (NumberFormatException e) {
	            qty = 0; total = 0;
	        }
	        double unitPrice = qty > 0 ? total / qty : 0;

	        // Data row
	        Font tdFont = FontFactory.getFont(FontFactory.HELVETICA, 11);
	        BaseColor rowColor = new BaseColor(240, 240, 240);
	        String purpose = payment.getPurpose() != null ? payment.getPurpose() : "N/A";
	        addTableCell(itemTable, "Goat (" + purpose + ")", tdFont, rowColor);
	        addTableCell(itemTable, String.format("%.2f", qty), tdFont, rowColor);
	        addTableCell(itemTable, String.format("%.2f", unitPrice), tdFont, rowColor);
	        addTableCell(itemTable, String.format("%.2f", total), tdFont, rowColor);

	        document.add(itemTable);

	        // -------- Total Summary ----------
	        PdfPTable totalTable = new PdfPTable(2);
	        totalTable.setWidthPercentage(40);
	        totalTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        totalTable.setSpacingAfter(15);

	        Font totalLabelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
	        Font totalValueFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

	        PdfPCell totalLabelCell = new PdfPCell(new Phrase("Grand Total:", totalLabelFont));
	        totalLabelCell.setBorder(Rectangle.NO_BORDER);
	        totalLabelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        totalTable.addCell(totalLabelCell);

	        PdfPCell totalValueCell = new PdfPCell(new Phrase("₹ " + String.format("%.2f", total), totalValueFont));
	        totalValueCell.setBorder(Rectangle.NO_BORDER);
	        totalTable.addCell(totalValueCell);

	      

	        totalValueCell = new PdfPCell(new Phrase("₹ " + String.format("%.2f", total), 
	                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, new BaseColor(46, 125, 50))));
	        totalValueCell.setBorder(Rectangle.NO_BORDER);
	        totalTable.addCell(totalValueCell);

	        document.add(totalTable);

	        // -------- Payment & Delivery Info (Payment Method, Order Date, Delivery Date, Order Time, Feedback) ----------
	        PdfPTable infoTable = new PdfPTable(2);
	        infoTable.setWidthPercentage(100);
	        infoTable.setSpacingAfter(15);

	        addInfoRow(infoTable, "Payment Method :", payment.getPmethod(), true);
	        addInfoRow(infoTable, "Order Date :", payment.getOdate(), false);
	        addInfoRow(infoTable, "Delivery Date :", payment.getDdate(), true);
	        addInfoRow(infoTable, "Order Time :", payment.getOtime(), false);
	        if (payment.getFeedback() != null && !payment.getFeedback().isEmpty()) {
	            addInfoRow(infoTable, "Feedback :", payment.getFeedback(), true);
	        }

	        document.add(infoTable);

	        // -------- Footer ----------
	        PdfPTable footerTable = new PdfPTable(1);
	        footerTable.setWidthPercentage(100);
	        footerTable.setSpacingBefore(20);

	        PdfPCell footerCell = new PdfPCell(new Phrase("Thank you for your business!   Visit again..", 
	                FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 12, BaseColor.WHITE)));
	        footerCell.setBackgroundColor(new BaseColor(46, 125, 50));
	        footerCell.setPadding(10);
	        footerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        footerCell.setBorder(Rectangle.NO_BORDER);
	        footerTable.addCell(footerCell);
	        document.add(footerTable);

	        
	        document.close();
	    }

	    // Helper method for table cell with background
	    private void addTableCell(PdfPTable table, String text, Font font, BaseColor bgColor) {
	        PdfPCell cell = new PdfPCell(new Phrase(text, font));
	        cell.setPadding(8);
	        cell.setBackgroundColor(bgColor);
	        cell.setBorder(Rectangle.NO_BORDER);
	        table.addCell(cell);
	    }

	    // Helper method for two-column info rows with alternating background
	    private void addInfoRow(PdfPTable table, String field, String value, boolean isLightGray) {
	        BaseColor bgColor = isLightGray ? new BaseColor(240, 240, 240) : BaseColor.WHITE;
	        Font font = FontFactory.getFont(FontFactory.HELVETICA, 11);

	        PdfPCell cell1 = new PdfPCell(new Phrase(field, font));
	        cell1.setPadding(6);
	        cell1.setBackgroundColor(bgColor);
	        cell1.setBorder(Rectangle.NO_BORDER);
	        table.addCell(cell1);

	        PdfPCell cell2 = new PdfPCell(new Phrase(value != null ? value : "", font));
	        cell2.setPadding(6);
	        cell2.setBackgroundColor(bgColor);
	        cell2.setBorder(Rectangle.NO_BORDER);
	        table.addCell(cell2);
	    }
	}
