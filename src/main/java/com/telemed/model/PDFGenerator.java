package com.telemed.model;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.http.HttpServletResponse;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class PDFGenerator {
    private List<Record> recordList;

    public void setRecordList(List<Record> recordList) {
        this.recordList = recordList;
    }
    public void generate(User user, HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);

        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        Font fontHeader = FontFactory.getFont(FontFactory.TIMES_BOLD);
        fontHeader.setSize(20);
        Paragraph headerParagraph = new Paragraph("Zapisi tlaka", fontHeader);
        headerParagraph.setAlignment(Paragraph.ALIGN_CENTER);

        Font fontParagraph = FontFactory.getFont(FontFactory.TIMES);
        fontParagraph.setSize(14);
        Paragraph paragraph = new Paragraph(user.getFname() + " " + user.getLname(), fontParagraph);
        paragraph.setAlignment(Paragraph.ALIGN_LEFT);

        document.add(headerParagraph);
        document.add(paragraph);

        // Creating a table of 5 columns
        PdfPTable table = new PdfPTable(5);

        // Setting width of table, its columns and spacing
        table.setWidthPercentage(100f);
        table.setWidths(new int[]{3, 3, 3, 3, 3});
        table.setSpacingBefore(5);

        // Create Table Cells for table header
        PdfPCell cell = new PdfPCell();

        // Setting the background color and padding for table
        cell.setBackgroundColor(CMYKColor.getHSBColor(158,234,249));
        cell.setPadding(5);

        // Creating font
        // Setting font style and size for table
        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font.setColor(CMYKColor.BLACK);

        // Adding headings in the created table cell/ header
        // Adding Cell to table
        cell.setPhrase(new Phrase("Datum", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Vrijeme", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Sistolicki", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Dijastolicki", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Biljeska", font));
        table.addCell(cell);

        // Iterating over the list of records
        for (Record record : this.recordList) {
            // Adding record id
            if(user.getId() == record.getUser().getId()) {
                table.addCell(record.getDate());
                table.addCell(record.getTime());
                table.addCell(String.valueOf(record.getSysPressure()));
                table.addCell(String.valueOf(record.getDiasPressure()));
                table.addCell(record.getNote());
            }
        }
        // Adding the created table to document
        document.add(table);

        document.close();
    }
}
