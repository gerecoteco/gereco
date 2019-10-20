package helpers;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PdfTableGeneratorTests {

    private static final String TITLE = "example";
    private static final String DESTINATION = "it_does_not_matter";
    private static final ObservableList<TreeItem<LeaderBoardView>> LEADER_BOARD_VIEWS = FXCollections.observableArrayList();

    @Mock
    Document document;

    @Mock
    private PdfWriter pdfWriter;

    @Test
    public void should_create_a_valid_pdf() throws IOException, DocumentException {
        PdfTableGenerator pdfTableGenerator = new TestablePdqTableGenerator();

        pdfTableGenerator.createPdf(TITLE, DESTINATION, LEADER_BOARD_VIEWS);

        verify(document).open();

        verify(document).add(any(Phrase.class));
        verify(document).add(any(PdfPTable.class));

        verify(document).close();
    }

    class TestablePdqTableGenerator extends PdfTableGenerator {

        @Override
        Document createDocument(String dest) throws DocumentException, FileNotFoundException {
            super.createDocument(dest);
            return document;
        }

        @Override
        PdfWriter initializePDFWriter(String dest, Document document) throws DocumentException, FileNotFoundException {
            return pdfWriter;
        }
    }
}