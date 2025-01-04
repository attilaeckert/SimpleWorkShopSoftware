package com.simpleworkshopsoftware.pdfGenerator;

import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.simpleworkshopsoftware.controller.PopupWindowsController;
import com.simpleworkshopsoftware.dto.PdfDTO;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
/**
 * PdfGenerator class is responsible for creating PDF documents using the OpenPDf library.
 * It generates various sections of the PDF including headers, tables for work done and parts used
 * to repair the vehicle, based on the child class.
 *
 * @author Attila Eckert
 * @date 12/27/2024
 * @version 1.0
 */
public class PdfGenerator {

    private static final Color ANTHRACITE = new Color(67, 72, 84);
    private static final Color RUST_RED = new Color(135, 60, 40);
    private static final int TITLE_FONT_SIZE = 13;
    private static final int HEADER_FONT_SIZE = 11;
    private static final int CELL_FONT_SIZE = 10;
    private static final int TINY_FONT_SIZE = 9;
    protected Font titleFont;
    protected Font whiteHeaderFont;
    protected Font blackHeaderFont;
    protected Font cellFont;
    protected Font smallWhiteFont;
    protected Font smallBlackFont;
    protected Font tinyFont;
    protected PdfPCell defaultCell;
    protected PdfPCell lineCell;
    protected PdfPCell emptyCell;

    public PdfGenerator() {
        try {
            BaseFont titleBaseFont = loadBaseFont("fonts/NotoSerif-SemiBold.ttf");
            this.titleFont = createFont(titleBaseFont, TITLE_FONT_SIZE, Font.BOLD);

            BaseFont headerBaseFont = loadBaseFont("fonts/NotoSerif-Medium.ttf");
            this.whiteHeaderFont = createFont(headerBaseFont, HEADER_FONT_SIZE, Font.BOLD, Color.WHITE);
            this.blackHeaderFont = createFont(headerBaseFont, HEADER_FONT_SIZE, Font.BOLD, Color.BLACK);

            BaseFont regularBaseFont = loadBaseFont("fonts/NotoSerif-Regular.ttf");
            this.cellFont = createFont(regularBaseFont, CELL_FONT_SIZE, Font.NORMAL);

            BaseFont smallBaseFont = loadBaseFont("fonts/NotoSerif-Light.ttf");
            this.smallWhiteFont = createFont(smallBaseFont, CELL_FONT_SIZE, Font.NORMAL, Color.WHITE);
            this.smallBlackFont = createFont(smallBaseFont, CELL_FONT_SIZE, Font.NORMAL);
            this.tinyFont = createFont(smallBaseFont, TINY_FONT_SIZE, Font.NORMAL);
        } catch (Exception e) {
            handleFontLoadingError(e);
        }
    }

    private BaseFont loadBaseFont(String fontPath) throws Exception {
        String resourcePath = Objects.requireNonNull(getClass().getResource("/" + fontPath)).toExternalForm();
        return BaseFont.createFont(resourcePath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    }

    private Font createFont(BaseFont baseFont, int size, int style) {
        return new Font(baseFont, size, style);
    }

    private Font createFont(BaseFont baseFont, int size, int style, Color color) {
        return new Font(baseFont, size, style, color);
    }

    // Error handling for font loading
    private void handleFontLoadingError(Exception e) {
        PopupWindowsController.showDialog(
                "Nem sikerűlt betölteni a betűtípust", PopupWindowsController.DialogType.WARNING, e);
    }

    public void createPdf(PdfDTO dto) throws IOException {}

    protected PdfPTable createHeaderCells(String title, String documentNumber) throws IOException {
        PdfPTable header = new PdfPTable(new float[]{1, 1, 1});
        header.setWidthPercentage(100);
        PdfPCell logoCell = new PdfPCell(defaultCell);
        logoCell.setFixedHeight(60);
        try {
            Image logo = Image.getInstance("config/logo.png");
            logoCell.setImage(logo);
        } catch (BadElementException e) {
            PopupWindowsController.showDialog(
                    "A beállított logo formátuma nem megfelelő", PopupWindowsController.DialogType.WARNING, e);
        } catch (IOException e) {
            PopupWindowsController.showDialog(
                    "Nem található a cég logo", PopupWindowsController.DialogType.WARNING, e);
        }
        header.addCell(logoCell);

        PdfPCell titleCell = new PdfPCell(defaultCell);
        titleCell.setPhrase(new Phrase(title, titleFont));
        titleCell.setFixedHeight(60);
        header.addCell(titleCell);

        PdfPCell ordinalNumberCell = new PdfPCell(defaultCell);
        ordinalNumberCell.setPhrase(new Phrase("Sorszám: " + documentNumber, smallBlackFont));
        ordinalNumberCell.setFixedHeight(60);
        header.addCell(ordinalNumberCell);
        header.setSpacingAfter(20);
        return header;
    }

    protected PdfPTable createInformationsHeader(
            PdfDTO dto, String dateCellTitle, String repairHeaderTitle, String nextRepairsHeaderTitle) {

        PdfPTable informations = new PdfPTable(new float[]{2, 1});
        informations.setWidthPercentage(100);

        PdfPCell compHeaderCell = new PdfPCell(defaultCell);
        compHeaderCell.setPhrase(new Phrase(dto.getCompName(), blackHeaderFont));
        compHeaderCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        compHeaderCell.setPaddingLeft(20);
        informations.addCell(compHeaderCell);

        PdfPCell custHeaderCell = new PdfPCell(defaultCell);
        custHeaderCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        custHeaderCell.setPaddingLeft(10);
        custHeaderCell.setPhrase(new Phrase("Ügyfél", blackHeaderFont));

        informations.addCell(custHeaderCell);

        PdfPCell compCell = new PdfPCell(defaultCell);
        compCell.setPhrase(new Phrase(dto.getCompInformation(), cellFont));
        compCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        compCell.setPaddingLeft(20);
        compCell.setLeading(4f,1f);
        compCell.setPaddingBottom(10);
        informations.addCell(compCell);

        PdfPCell custCell = new PdfPCell(defaultCell);
        custCell.setPhrase(new Phrase(dto.getCustomerInformation(), cellFont));
        custCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        custCell.setLeading(4f,1f);
        custCell.setPaddingLeft(10);
        custCell.setPaddingBottom(10);
        informations.addCell(custCell);

        lineCell.setPaddingBottom(10);
        informations.addCell(lineCell);

        PdfPCell dateCell = new PdfPCell(defaultCell);
        dateCell.setPhrase(new Phrase(dateCellTitle + dto.getExpectedDate(), blackHeaderFont));
        dateCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        dateCell.setPaddingLeft(20);
        dateCell.setPaddingBottom(10);
        informations.addCell(dateCell);

        PdfPCell carHeaderCell = new PdfPCell(defaultCell);
        carHeaderCell.setPhrase(new Phrase(dto.getCarModel(), blackHeaderFont));
        carHeaderCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        carHeaderCell.setPaddingBottom(10);
        carHeaderCell.setPaddingLeft(10);
        informations.addCell(carHeaderCell);

        PdfPCell noteCell = new PdfPCell(defaultCell);
        noteCell.setPhrase(new Phrase("Megjegyzés:\n" + dto.getNote(), smallBlackFont));
        noteCell.setLeading(4f,1f);
        noteCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        noteCell.setPaddingLeft(20);
        informations.addCell(noteCell);

        PdfPCell carInformationCell = new PdfPCell(defaultCell);
        carInformationCell.setPhrase(new Phrase(dto.getCarInformation(), cellFont));
        carInformationCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        carInformationCell.setLeading(4f,1f);
        carInformationCell.setPaddingLeft(10);
        carInformationCell.setPaddingBottom(10);
        informations.addCell(carInformationCell);
        lineCell.setPaddingBottom(10);
        informations.addCell(lineCell);

        PdfPCell repairHeader = new PdfPCell(defaultCell);
        repairHeader.setPhrase(new Phrase(repairHeaderTitle, cellFont));
        repairHeader.setHorizontalAlignment(Element.ALIGN_LEFT);
        repairHeader.setPaddingLeft(20);
        repairHeader.setPaddingBottom(5);
        informations.addCell(repairHeader);

        PdfPCell nextRepairsHeader = new PdfPCell(defaultCell);
        nextRepairsHeader.setPhrase(new Phrase(nextRepairsHeaderTitle, cellFont));
        nextRepairsHeader.setHorizontalAlignment(Element.ALIGN_LEFT);
        nextRepairsHeader.setPaddingLeft(10);
        nextRepairsHeader.setPaddingBottom(5);
        informations.addCell(nextRepairsHeader);

        PdfPCell repairCell = new PdfPCell(defaultCell);
        repairCell.setPhrase(new Phrase(dto.getIssueOrRepair(), cellFont));
        repairCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        repairCell.setPaddingBottom(10);
        repairCell.setPaddingLeft(20);
        informations.addCell(repairCell);

        PdfPCell nextRepairsCell = new PdfPCell(defaultCell);
        nextRepairsCell.setPhrase(new Phrase(dto.getExpectedOrNextRepairs(), cellFont));
        nextRepairsCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        nextRepairsCell.setPaddingLeft(10);
        nextRepairsCell.setPaddingBottom(10);
        informations.addCell(nextRepairsCell);
        informations.addCell(lineCell);

        informations.setSpacingAfter(10);
        return informations;
    }

    protected void createWorkTable(PdfDTO dto, String header, Document document) {
        PdfPTable workTable = new PdfPTable(4);
        workTable.setWidthPercentage(90);
        workTable.setWidths(new float[]{40, 20, 20, 20});
        workTable.setHeaderRows(1);

        if (!dto.getWorkTableData().isEmpty()) {
            String[] workHeaders = {header, "Mennyiség", "Egység", "Bruttó érték"};
            for (String headerTitle : workHeaders) {
                PdfPCell headerCell = new PdfPCell(defaultCell);
                headerCell.setPadding(7);
                headerCell.setPhrase(new Phrase(headerTitle, whiteHeaderFont));
                headerCell.setBackgroundColor(ANTHRACITE);
                workTable.addCell(headerCell);
            }

            fillTableData(dto.getWorkTableData(),  workTable);
        }

        PdfPCell workFooter = new PdfPCell(defaultCell);
        workFooter.setPhrase(new Phrase("Munkadíj bruttó: " + dto.getLaborCost(), whiteHeaderFont));
        workFooter.setColspan(4);
        workFooter.setPadding(6);
        workFooter.setBackgroundColor(ANTHRACITE);
        workFooter.setHorizontalAlignment(Element.ALIGN_RIGHT);
        workTable.addCell(workFooter);
        workTable.setSpacingAfter(10);
        document.add(workTable);
    }

    protected PdfPTable createPartsTable(PdfDTO dto, String partsHeadersTitle) {
        PdfPTable partsTable = new PdfPTable(4);
        partsTable.setWidthPercentage(90);
        partsTable.setWidths(new float[]{40,20, 20, 20});
        partsTable.setHeaderRows(1);

        String[] partsHeaders = {partsHeadersTitle, "Mennyiség", "Egység", "Bruttó érték"};
        for (String headerTitle : partsHeaders) {
            PdfPCell headerCell = new PdfPCell(defaultCell);
            headerCell.setPadding(7);
            headerCell.setPhrase(new Phrase(headerTitle, whiteHeaderFont));
            headerCell.setBackgroundColor(ANTHRACITE);
            partsTable.addCell(headerCell);
        }

        fillTableData(dto.getPartsTableData(),  partsTable);

        PdfPCell discountFooter = new PdfPCell(defaultCell);
        if (!Objects.equals(dto.getDiscountPercent(), String.valueOf(0))
                && !Objects.equals(dto.getDiscountPercent(), "")) {
            discountFooter.setPhrase(new Phrase("Alkatrész kedvezmény mértéke: "
                    + dto.getDiscountPercent() + "%", smallWhiteFont));
        }
        discountFooter.setColspan(2);
        discountFooter.setPadding(6);
        discountFooter.setBackgroundColor(ANTHRACITE);
        discountFooter.setHorizontalAlignment(Element.ALIGN_LEFT);
        partsTable.addCell(discountFooter);

        PdfPCell partsCostFooter = new PdfPCell(defaultCell);
        partsCostFooter.setPhrase(new Phrase( "Alkatrész bruttó: "
                + dto.getPartsCost() +" Ft", whiteHeaderFont));
        partsCostFooter.setColspan(2);
        partsCostFooter.setPadding(6);
        partsCostFooter.setBackgroundColor(ANTHRACITE);
        partsCostFooter.setHorizontalAlignment(Element.ALIGN_RIGHT);
        partsTable.addCell(partsCostFooter);

        PdfPCell overallCostWithoutTax = new PdfPCell(defaultCell);
        overallCostWithoutTax.setPhrase(new Phrase( "Összesen nettó: "
                + dto.getWithoutTax() +" Ft", whiteHeaderFont));
        overallCostWithoutTax.setColspan(2);
        overallCostWithoutTax.setPadding(6);
        overallCostWithoutTax.setBackgroundColor(ANTHRACITE);
        overallCostWithoutTax.setHorizontalAlignment(Element.ALIGN_LEFT);
        partsTable.addCell(overallCostWithoutTax);

        PdfPCell overallCostFooter = new PdfPCell(defaultCell);
        overallCostFooter.setPhrase(new Phrase( "bruttó: "
                + dto.getOverallCost() +" Ft", whiteHeaderFont));
        overallCostFooter.setColspan(2);
        overallCostFooter.setPadding(6);
        overallCostFooter.setBackgroundColor(ANTHRACITE);
        overallCostFooter.setHorizontalAlignment(Element.ALIGN_RIGHT);
        partsTable.addCell(overallCostFooter);
        return partsTable;
    }

    protected void createDefaultCells() {
        // the rest of the cells inherits this parameters
        defaultCell = new PdfPCell();
        defaultCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        defaultCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        defaultCell.setBorder(PdfPCell.NO_BORDER);

        emptyCell = new PdfPCell(defaultCell);
        emptyCell.setPhrase(new Phrase(""));

        // used for creating lines
        lineCell = new PdfPCell();
        lineCell.setColspan(5);
        lineCell.setBorderWidthTop(2.0f);
        lineCell.setBorderWidthBottom(0);
        lineCell.setBorderWidthLeft(0);
        lineCell.setBorderWidthRight(0);
        lineCell.setBorderColorTop(RUST_RED);
    }

    protected PdfPTable createTailTextTable(String footerNote) {
        PdfPTable tailTextTable = new PdfPTable(1);
        PdfPCell textCell = new PdfPCell(defaultCell);
        textCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        textCell.setPhrase(new Phrase(footerNote, tinyFont));
        tailTextTable.addCell(textCell);
        return tailTextTable;
    }

    protected void addTailTableToBottomOfPage(Document doc, PdfWriter writer, String footerNote, String date) throws DocumentException {
        PdfPTable tailTable = new PdfPTable(new float[]{1,1,1});
        PdfPCell compSignatureCell = new PdfPCell(defaultCell);
        compSignatureCell.setPhrase(new Phrase("__________________________", cellFont));
        tailTable.addCell(compSignatureCell);

        tailTable.addCell(emptyCell);

        PdfPCell custSignatureCell = new PdfPCell(defaultCell);
        custSignatureCell.setPhrase(new Phrase("__________________________", cellFont));
        tailTable.addCell(custSignatureCell);

        PdfPCell underCompSignatureCell = new PdfPCell(defaultCell);
        underCompSignatureCell.setPhrase(new Phrase("Átadó", cellFont));
        underCompSignatureCell.setPaddingBottom(10);
        tailTable.addCell(underCompSignatureCell);

        tailTable.addCell(emptyCell);

        PdfPCell underCustSignatureCell = new PdfPCell(defaultCell);
        underCustSignatureCell.setPhrase(new Phrase("Átvevő", cellFont));
        underCustSignatureCell.setPaddingBottom(10);
        tailTable.addCell(underCustSignatureCell);

        PdfPCell actualDateCell = new PdfPCell(defaultCell);
        actualDateCell.setPhrase(new Phrase("Dátum: " + date, cellFont));
        tailTable.addCell(actualDateCell);

        tailTable.addCell(emptyCell);

        PdfPCell footerNoteCell = new PdfPCell(defaultCell);
        footerNoteCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        footerNoteCell.setPhrase(new Phrase(footerNote, tinyFont));
        tailTable.addCell(footerNoteCell);

        tailTable.setTotalWidth(doc.getPageSize().getWidth() - doc.leftMargin() - doc.rightMargin());
        tailTable.setLockedWidth(true);
        PdfContentByte canvas = writer.getDirectContent();
        float customOffset = 10f;
        float tableHeight = tailTable.getTotalHeight();
        float bottomMargin = doc.bottomMargin();
        float currentY = writer.getVerticalPosition(true);

        if (currentY - tableHeight < bottomMargin) {
            doc.newPage();
        }

        tailTable.writeSelectedRows(0, -1,
                doc.leftMargin(),
                doc.bottomMargin() + customOffset + tableHeight,
                canvas);
    }

    protected void fillTableData(List<String[]> tableData,  PdfPTable table) {
        int counter = 1;
        for (String[] row : tableData) {
            Color rowColor = (counter % 2 == 0) ? new Color(238, 238, 238) : Color.WHITE;

            PdfPCell nameCell = new PdfPCell(new Phrase(row[0], cellFont));
            nameCell.setBackgroundColor(rowColor);
            nameCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            nameCell.setPadding(5);
            nameCell.setBorder(Rectangle.NO_BORDER);
            table.addCell(nameCell);

            PdfPCell unitCell = new PdfPCell(new Phrase(row[1], cellFont));
            unitCell.setBackgroundColor(rowColor);
            unitCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            unitCell.setPadding(5);
            unitCell.setBorder(Rectangle.NO_BORDER);
            table.addCell(unitCell);

            PdfPCell quantityCell = new PdfPCell(new Phrase(row[2], cellFont));
            quantityCell.setBackgroundColor(rowColor);
            quantityCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            quantityCell.setPadding(5);
            quantityCell.setBorder(Rectangle.NO_BORDER);
            table.addCell(quantityCell);

            PdfPCell unitPriceCell = new PdfPCell(new Phrase(PdfUtils.numberFormat(row[3]), cellFont));
            unitPriceCell.setBackgroundColor(rowColor);
            unitPriceCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            unitPriceCell.setPadding(5);
            unitPriceCell.setBorder(Rectangle.NO_BORDER);
            table.addCell(unitPriceCell);
            counter++;
        }
    }

     static class EndOfPageEvent extends PdfPageEventHelper {
         private PdfTemplate tpl;
         private BaseFont helv;

         public void onOpenDocument(PdfWriter writer, Document document) {
             try {
                 // Initialize the template and font
                 tpl = writer.getDirectContent().createTemplate(50, 50);
                 helv = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, false);
             } catch (Exception e) {
                 throw new RuntimeException(e);
             }
         }

         public void onEndPage(PdfWriter writer, Document document) {
             PdfContentByte cb = writer.getDirectContent();
             cb.saveState();
             String text = writer.getPageNumber() + " / ";
             float textSize = helv.getWidthPoint(text, 12);
             float x = (document.right() + document.left()) / 2; // Center horizontally
             float y = document.bottom() - 20; // Position at the bottom
             cb.beginText();
             cb.setFontAndSize(helv, 12);
             cb.setTextMatrix(x - textSize / 2, y);
             cb.showText(text);
             cb.endText();
             // Add the total page count template
             cb.addTemplate(tpl, x + textSize / 2, y);
             cb.restoreState();
         }

         public void onCloseDocument(PdfWriter writer, Document document) {
             tpl.beginText();
             tpl.setFontAndSize(helv, 12);
             tpl.setTextMatrix(0, 0);
             tpl.showText(String.valueOf(writer.getPageNumber() - 1));
             tpl.endText();
         }
     }
}
