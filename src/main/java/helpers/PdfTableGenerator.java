package helpers;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import java.io.FileOutputStream;
import java.io.IOException;

public class PdfTableGenerator {
    private BaseColor backgrounColor;

    public void createPdf(String title, String dest, ObservableList<TreeItem<LeaderBoardView>> leaderBoardViews)
            throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();

        PdfPTable table = new PdfPTable(7);
        table.setWidths(new int[]{3, 5, 2, 2, 2, 2, 4});
        backgrounColor = BaseColor.LIGHT_GRAY;
        table.addCell(createCell("Posição"));
        table.addCell(createCell("Nome"));
        table.addCell(createCell("Pontos"));
        table.addCell(createCell("PP"));
        table.addCell(createCell("PC"));
        table.addCell(createCell("Saldo"));
        table.addCell(createCell("Penalidades"));

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
