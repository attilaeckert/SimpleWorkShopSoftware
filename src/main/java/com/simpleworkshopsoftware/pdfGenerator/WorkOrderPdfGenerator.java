package com.simpleworkshopsoftware.pdfGenerator;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.simpleworkshopsoftware.dto.PdfDTO;

import java.io.FileOutputStream;
import java.io.IOException;
/**
 * WorkOrderPdfGenerator, extends the PdfGenerator class and is responsible for generating a PDF document
 * based on the provided PdfDTO object.
 *
 * @author Attila Eckert
 * @date 12/27/2024
 * @version 1.0
 */
public class WorkOrderPdfGenerator extends PdfGenerator {

    public WorkOrderPdfGenerator() {}

    @Override
    public void createPdf(PdfDTO dto) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(dto.getFilePath())) {
            Document document = new Document();
            document.setMargins(20, 20, 30, 30);
            PdfWriter writer = PdfWriter.getInstance(document, fos);
            writer.setPageEvent(new EndOfPageEvent());
            document.open();
            createDefaultCells();
            PdfPTable header = createHeaderCells("Munkalap", dto.getDocumentNumber());
            document.add(header);

            PdfPTable informations = createInformationsHeader(
                    dto,
                    "Teljesítés dátuma ",
                    "Elvégzett munka rövid leírása:",
                    "Várható javítások a jövőben:");
            document.add(informations);

            createWorkTable(dto, "Elvégzett munka", document);

            if (!dto.getPartsTableData().isEmpty()) {
                PdfPTable partsTable = createPartsTable(dto, "Beépített alkatrész");
                document.add(partsTable);
            }

            PdfPTable tailTextTable = createTailTextTable(dto.getFooterNote());
            tailTextTable.setSpacingAfter(30);
            document.add(tailTextTable);

            addTailTableToBottomOfPage(document, writer,
                    "A járművet a megrendelt szolgáltatások elvégzése után hiánytalanul, üzemképesen átvettem.", dto.getDate());
            document.close();
        } catch (Exception e) {
            throw new IOException("Nem sikerült létrehozni a PDF dokumentumot", e);
        }
    }
}
