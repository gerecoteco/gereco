package helpers;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ResourceBundle;

public class PdfTableGenerator {
    private BaseColor backgrounColor;
    private ResourceBundle strings = ResourceBundle.getBundle("bundles.lang", new UTF8Control());

    public void createPdf(String title, String dest, ObservableList<TreeItem<LeaderBoardView>> leaderBoardViews)
            throws IOException, DocumentException {

        Document document = createDocument(dest);
        document.open();

        PdfPTable table = new PdfPTable(7);
        backgrounColor = BaseColor.LIGHT_GRAY;

        table.addCell(createCell(strings.getString("position")));
        table.addCell(createCell(strings.getString("team")));
        table.addCell(createCell(strings.getString("points")));
        table.addCell(createCell(strings.getString("ownPoints")));
        table.addCell(createCell(strings.getString("againstPoints")));
        table.addCell(createCell(strings.getString("balance")));
        table.addCell(createCell(strings.getString("fouls")));

        backgrounColor = BaseColor.WHITE;
        for (TreeItem<LeaderBoardView> leaderBoardView : leaderBoardViews) {
            table.addCell(createCell(String.valueOf(leaderBoardView.getValue().positionProperty().get())));
            table.addCell(createCell(leaderBoardView.getValue().nameProperty().get()));
            table.addCell(createCell(String.valueOf(leaderBoardView.getValue().pointsProperty().get())));
            table.addCell(createCell(String.valueOf(leaderBoardView.getValue().ownPointsProperty().get())));
            table.addCell(createCell(String.valueOf(leaderBoardView.getValue().againstPointsProperty().get())));
            table.addCell(createCell(String.valueOf(leaderBoardView.getValue().balanceProperty().get())));
            table.addCell(createCell(String.valueOf(leaderBoardView.getValue().foulsProperty().get())));
        }

        document.add(new Phrase(title));
        document.add(table);
        document.close();
    }

    Document createDocument(String dest) throws DocumentException, FileNotFoundException {
        Document document = new Document();
        initializePDFWriter(dest, document);
        return document;
    }

    PdfWriter initializePDFWriter(String dest, Document document) throws DocumentException, FileNotFoundException {
        return PdfWriter.getInstance(document,new FileOutputStream(dest));
    }

    private PdfPCell createCell(String content) {
        PdfPCell cell = new PdfPCell(new Phrase(content));
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidth(1);
        cell.setColspan(1);
        cell.setMinimumHeight(20);
        cell.setBackgroundColor(backgrounColor);
        return cell;
    }
}
