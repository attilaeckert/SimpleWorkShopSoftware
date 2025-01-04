package com.simpleworkshopsoftware.pdfGenerator;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.simpleworkshopsoftware.dto.PdfDTO;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
/**
 * WorkSheetPdfGenerator, extends the PdfGenerator class and is responsible for generating a PDF document
 * for internal use in the work shop, based on the provided PdfDTO object.
 *
 * @author Attila Eckert
 * @date 12/27/2024
 * @version 1.0
 */
public class WorkSheetPdfGenerator extends PdfGenerator{

    public WorkSheetPdfGenerator() {}

    @Override
    public void createPdf(PdfDTO dto) throws IOException {
        String fileName = dto.getFilePath().replace(".pdf", "_műhely_munkalap.pdf");
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, fos);
            writer.setPageEvent(new EndOfPageEvent());
            document.open();
            createDefaultCells();
            PdfPTable header = createHeaderCells("Műhely munkalap");
            document.add(header);

            PdfPTable informations = createInformationsHeader(dto, getStringFromDTO(dto));
            document.add(informations);

            addFirstSection(document);

            addSection(document, "Általános", new String[]{
                    "Szélvédő állapota",
                    "Ablaktörlő lapátok állapota",
                    "Világítás, fényszórók működése",
                    "Karosszéria sérülések, korrózió",
                    "Akkumulátor állapota"
            });

            addSection(document, "Motortér, és hajtáslánc", new String[]{
                    "Motor tömítettsége",
                    "Hűtőrendszer tömítettsége",
                    "Hűtőfolyadék szintje, fagyáspont",
                    "Hajtáslánc tömítettsége",
                    "Hajtáslánc állapota",
                    "Kipufogórendszer állapota",
                    "Kiegészítő szíjhajtás állapota"
            });

            addBrakeSection(document, new String[]{
                    "Első féktárcsák",
                    "Első fékbetétek",
                    "Hátsó féktárcsák/fékdobok",
                    "Hátsó fékbetétek/fékpofák",
                    "Fékfolyadék",
                    "Fékcsövek",
                    "Kézifék működése"
            });

            addSection(document, "Felfüggesztés állapota", new String[]{
                    "Kormánymű",
                    "Első felfüggesztés",
                    "Hátsó felfüggesztés",
                    "Lengéscsillapítók, rugók"
            });

            addSection(document, "Gumiabroncsok", new String[]{
                    "Első abroncsok állapota",
                    "Hátsó abroncsok állapota",
                    "Felnik állapota"
            });

            addRemarksAndSignature(document);

            document.close();
        } catch (Exception e) {
            throw new IOException("Nem sikerült létrehozni a PDF dokumentumot", e);
        }
    }

    private PdfPTable createInformationsHeader(
            PdfDTO dto, String issue) {
        PdfPTable informations = new PdfPTable(new float[]{4, 2, 4});
        informations.setWidthPercentage(100);

        PdfPCell custHeaderCell = new PdfPCell(defaultCell);
        custHeaderCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        custHeaderCell.setPhrase(new Phrase("Ügyfél", blackHeaderFont));
        custHeaderCell.setPaddingLeft(20);
        informations.addCell(custHeaderCell);
        informations.addCell(emptyCell);

        PdfPCell carHeaderCell = new PdfPCell(defaultCell);
        carHeaderCell.setPhrase(new Phrase(dto.getCarModel(), blackHeaderFont));
        carHeaderCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        carHeaderCell.setPaddingBottom(10);
        informations.addCell(carHeaderCell);

        PdfPCell custCell = new PdfPCell(defaultCell);
        custCell.setPhrase(new Phrase(dto.getCustomerInformation(), cellFont));
        custCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        custCell.setLeading(4f, 1f);
        custCell.setPaddingLeft(20);
        custCell.setPaddingBottom(10);
        informations.addCell(custCell);
        informations.addCell(emptyCell);

        PdfPCell carInformationCell = new PdfPCell(defaultCell);
        carInformationCell.setPhrase(new Phrase(dto.getCarInformation(), cellFont));
        carInformationCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        carInformationCell.setLeading(4f, 1f);
        carInformationCell.setPaddingBottom(10);
        informations.addCell(carInformationCell);

        lineCell.setPaddingBottom(10);
        informations.addCell(lineCell);

        PdfPCell noteCell = new PdfPCell(defaultCell);
        noteCell.setPhrase(new Phrase("Megjegyzés:\n" + dto.getNote(), cellFont));
        noteCell.setLeading(4f, 1f);
        noteCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        noteCell.setPaddingLeft(20);
        noteCell.setPaddingBottom(10);
        informations.addCell(noteCell);
        informations.addCell(emptyCell);

        PdfPCell repairCell = new PdfPCell(defaultCell);
        repairCell.setPhrase(new Phrase(issue, cellFont));
        repairCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        repairCell.setPaddingBottom(10);
        informations.addCell(repairCell);

        informations.addCell(lineCell);
        return informations;
    }

    private static String getStringFromDTO(PdfDTO dto) {
        String issue;
        if (!Objects.equals(dto.getIssueOrRepair(), null)) {
            issue = "Hiba leírása: " + dto.getIssueOrRepair();
        } else {
            issue = "Hiba leírása:";
        }
        return issue;
    }

    private PdfPTable createHeaderCells(String title) {
        PdfPTable header = new PdfPTable(1);
        PdfPCell headerCell = new PdfPCell(defaultCell);
        headerCell.setPhrase(new Phrase(title, titleFont));
        header.addCell(headerCell);
        header.setSpacingAfter(20);
        return header;
    }

    private void addSection (Document document, String sectionTitle, String[]items) throws DocumentException {
        Paragraph section = new Paragraph(sectionTitle, blackHeaderFont);
        section.setSpacingBefore(5);
        section.setSpacingAfter(5);
        document.add(section);

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{35, 2, 2, 2, 2, 57});

        for (String item : items) {
            PdfPCell descriptionCell = new PdfPCell(new Phrase(item, smallBlackFont));
            descriptionCell.setBorder(Rectangle.NO_BORDER);
            table.addCell(descriptionCell);

            PdfPCell checkboxCell = new PdfPCell(createCheckbox());
            checkboxCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            checkboxCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            checkboxCell.setBorder(Rectangle.NO_BORDER);
            table.addCell(checkboxCell);

            table.addCell(emptyCell);
            table.addCell(checkboxCell);
            table.addCell(emptyCell);

            PdfPCell lineCell = new PdfPCell(createLine());
            lineCell.setBorder(Rectangle.NO_BORDER);
            table.addCell(lineCell);
        }
        document.add(table);
    }

    private void addFirstSection (Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{29, 9, 12, 50});

            table.addCell(emptyCell);

            PdfPCell checkboxCellFirstTitle = new PdfPCell(defaultCell);
            checkboxCellFirstTitle.setPhrase(new Phrase("Megfelelő", tinyFont));
            checkboxCellFirstTitle.setHorizontalAlignment(Element.ALIGN_LEFT);
            checkboxCellFirstTitle.setVerticalAlignment(Element.ALIGN_MIDDLE);
            checkboxCellFirstTitle.setBorder(Rectangle.NO_BORDER);
            table.addCell(checkboxCellFirstTitle);


            PdfPCell checkboxCellSecondTitle = new PdfPCell(defaultCell);
            checkboxCellSecondTitle.setPhrase(new Phrase("Nem megfelelő", tinyFont));
            checkboxCellSecondTitle.setHorizontalAlignment(Element.ALIGN_LEFT);
            checkboxCellSecondTitle.setVerticalAlignment(Element.ALIGN_MIDDLE);
            checkboxCellSecondTitle.setBorder(Rectangle.NO_BORDER);
            table.addCell(checkboxCellSecondTitle);

            table.addCell(emptyCell);

        document.add(table);
    }

    private void addBrakeSection(Document document, String[] items) throws DocumentException {
        Paragraph section = new Paragraph("Fékrendszer állapota", blackHeaderFont);
        section.setSpacingBefore(5);
        section.setSpacingAfter(5);
        document.add(section);

        PdfPTable table = new PdfPTable(6);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{35, 2, 2, 2, 2, 57});

        for (int i = 0; i < items.length; i++) {
            boolean isForBrake = i < 4;

            PdfPCell descriptionCell = new PdfPCell(new Phrase(items[i], smallBlackFont));
            descriptionCell.setBorder(Rectangle.NO_BORDER);
            table.addCell(descriptionCell);

            PdfPCell checkboxCell = new PdfPCell(createCheckbox());
            checkboxCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            checkboxCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            checkboxCell.setBorder(Rectangle.NO_BORDER);
            if (isForBrake) {
                checkboxCell.setColspan(2);
                table.addCell(checkboxCell);
            } else {
                table.addCell(checkboxCell);
            }

            if (isForBrake) {
                table.addCell(createPercentageParagraph());
            } else {
                table.addCell(emptyCell);
                table.addCell(checkboxCell);
            }

            table.addCell(emptyCell);

            PdfPCell lineCell = new PdfPCell(createLine());
            lineCell.setBorder(Rectangle.NO_BORDER);
            table.addCell(lineCell);
        }

        document.add(table);
    }

    private Paragraph createPercentageParagraph () {
        return new Paragraph("%", smallBlackFont);
    }

    private PdfPTable createCheckbox () {
        PdfPTable checkboxTable = new PdfPTable(1);
        checkboxTable.setWidthPercentage(30);
        PdfPCell cell = new PdfPCell();
        cell.setFixedHeight(10);
        cell.setBorder(Rectangle.BOX);
        checkboxTable.addCell(cell);

        return checkboxTable;
    }

    private PdfPTable createLine () {
        PdfPTable lineTable = new PdfPTable(1);
        lineTable.setWidthPercentage(100);

        PdfPCell lineCell = new PdfPCell(new Phrase(" "));
        lineCell.setFixedHeight(10);
        lineCell.setBorder(Rectangle.BOTTOM);
        lineTable.addCell(lineCell);

        return lineTable;
    }

    private void addRemarksAndSignature(Document document) throws DocumentException {
        PdfPTable remarksAndSign = new PdfPTable(3);
        remarksAndSign.setSpacingBefore(20);
        PdfPCell remarksCell = new PdfPCell(defaultCell);
        remarksCell.setPhrase(new Phrase("Egyéb megjegyzés:", smallBlackFont));
        remarksAndSign.addCell(remarksCell);
        remarksAndSign.addCell(emptyCell);
        PdfPCell signCell = new PdfPCell(defaultCell);
        signCell.setPhrase(new Phrase("Szerelő aláírása:", smallBlackFont));
        remarksAndSign.addCell(signCell);
        document.add(remarksAndSign);
    }
}
