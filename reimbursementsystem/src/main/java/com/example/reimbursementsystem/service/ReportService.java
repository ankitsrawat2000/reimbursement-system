package com.example.reimbursementsystem.service;

import com.example.reimbursementsystem.entity.ClaimStatus;
import com.example.reimbursementsystem.entity.ExpenseClaim;
import com.example.reimbursementsystem.repository.ExpenseClaimRepository;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReportService {

    private final ExpenseClaimRepository claimRepo;

    public ReportService(ExpenseClaimRepository claimRepo) {
        this.claimRepo = claimRepo;
    }

    public byte[] generateReportPdf(LocalDate start, LocalDate end) {

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document doc = new Document();
            PdfWriter.getInstance(doc, out);

            doc.open();

            Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);

            doc.add(new Paragraph("Expense Claims Report", font));
            doc.add(new Paragraph("Date Range: " + start + " to " + end));
            doc.add(new Paragraph(" "));

            List<ExpenseClaim> claims =
                    claimRepo.findByDateSubmittedBetween(start, end);

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);

            table.addCell("Employee");
            table.addCell("Department");
            table.addCell("Description");
            table.addCell("Amount");
            table.addCell("Status");
            table.addCell("Date");

            BigDecimal totalApprovedAmount = BigDecimal.ZERO;

            for (ExpenseClaim c : claims) {

                table.addCell(c.getEmployee().getName());
                table.addCell(c.getEmployee().getDepartment());
                table.addCell(c.getDescription());
                table.addCell(c.getAmount().toString());
                table.addCell(c.getStatus().name());
                table.addCell(c.getDateSubmitted().toString());

                if (c.getStatus() == ClaimStatus.Approved) {
                    totalApprovedAmount =
                            totalApprovedAmount.add(c.getAmount());
                }
            }

            doc.add(table);

            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("Summary", font));
            doc.add(new Paragraph("Total Claims: " + claims.size()));
            doc.add(new Paragraph("Total Approved Amount: " + totalApprovedAmount));

            doc.close();

            return out.toByteArray();

        } catch (DocumentException e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }
}
