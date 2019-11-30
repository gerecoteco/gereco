package helpers;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import models.GeneralMatch;
import models.Team;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

public class PdfTableGenerator {
    private BaseColor backgrounColor;
    private ResourceBundle strings = ResourceBundle.getBundle("bundles.lang", new UTF8Control());

    public void generateLeaderBoardPdf(
            String title, String dest, ObservableList<TreeItem<LeaderBoardModel>> leaderBoardViews)
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
        for (TreeItem<LeaderBoardModel> leaderBoardView : leaderBoardViews) {
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

    public void generateMatchTablePdf(
            String title, String dest, ObservableList<TreeItem<MatchTableModel>> matchTableItems)
            throws IOException, DocumentException {

        Document document = createDocument(dest);
        document.open();

        PdfPTable table = new PdfPTable(6);
        backgrounColor = BaseColor.LIGHT_GRAY;

        table.addCell(createCell(strings.getString("stage")));
        table.addCell(createCell(strings.getString("team")));
        table.addCell(createCell(strings.getString("score")));
        table.addCell(createCell("X"));
        table.addCell(createCell(strings.getString("score")));
        table.addCell(createCell(strings.getString("team")));

        backgrounColor = BaseColor.WHITE;
        for (TreeItem<MatchTableModel> matchTableItem : matchTableItems) {
            table.addCell(createCell(String.valueOf(matchTableItem.getValue().stageProperty().get())));
            table.addCell(createCell(matchTableItem.getValue().teamAProperty().get()));
            table.addCell(createCell(String.valueOf(matchTableItem.getValue().scoreAProperty().get())));
            table.addCell(createCell("X"));
            table.addCell(createCell(matchTableItem.getValue().teamBProperty().get()));
            table.addCell(createCell(String.valueOf(matchTableItem.getValue().scoreBProperty().get())));
        }

        document.add(new Phrase(title));
        document.add(table);
        document.close();
    }

    public void generateGeneralMatchTablePdf(
            String title, String dest, ObservableList<TreeItem<GeneralMatch>> matchTableItems)
            throws IOException, DocumentException {

        Document document = createDocument(dest);
        document.open();

        PdfPTable table = new PdfPTable(6);
        backgrounColor = BaseColor.LIGHT_GRAY;

        table.addCell(createCell(strings.getString("modality")));
        table.addCell(createCell(strings.getString("gender")));
        table.addCell(createCell(strings.getString("stage")));
        table.addCell(createCell(strings.getString("team")));
        table.addCell(createCell("X"));
        table.addCell(createCell(strings.getString("team")));

        backgrounColor = BaseColor.WHITE;
        for (TreeItem<GeneralMatch> matchTableItem : matchTableItems) {
            table.addCell(createCell(matchTableItem.getValue().getModality()));
            table.addCell(createCell(matchTableItem.getValue().getGender()));
            table.addCell(createCell(String.valueOf(matchTableItem.getValue().getStage())));
            table.addCell(createCell(String.valueOf(matchTableItem.getValue().getTeamA())));
            table.addCell(createCell("X"));
            table.addCell(createCell(String.valueOf(matchTableItem.getValue().getTeamB())));
        }

        document.add(new Phrase(title));
        document.add(table);
        document.close();
    }

    public void generateGroupTablesPdf(
            String title, String dest,  List<List<Team>> genderGroups)
            throws IOException, DocumentException {

        Document document = createDocument(dest);
        document.open();

        PdfPTable table;
        document.add(new Phrase(title));

        for (List<Team> group : genderGroups) {
            table = initializeGroupTableColumnsAndReturn();
            backgrounColor = BaseColor.WHITE;
            for (Team team : group) {
                table.addCell(createCell(team.getName()));
                table.addCell(createCell(String.valueOf(team.getScore().getPoints())));
                table.addCell(createCell(String.valueOf(team.getScore().getOwnPoints())));
                table.addCell(createCell(String.valueOf(team.getScore().getAgainstPoints())));
                table.addCell(createCell(String.valueOf(team.getScore().getBalance())));
                table.addCell(createCell(String.valueOf(team.getScore().getFouls())));
            }
            document.add(Chunk.NEWLINE);
            document.add(table);
        }

        document.close();
    }

    private PdfPTable initializeGroupTableColumnsAndReturn(){
        PdfPTable table = new PdfPTable(6);
        backgrounColor = BaseColor.LIGHT_GRAY;

        table.addCell(createCell(strings.getString("team")));
        table.addCell(createCell(strings.getString("points")));
        table.addCell(createCell(strings.getString("ownPoints")));
        table.addCell(createCell(strings.getString("againstPoints")));
        table.addCell(createCell(strings.getString("balance")));
        table.addCell(createCell(strings.getString("fouls")));

        return table;
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
