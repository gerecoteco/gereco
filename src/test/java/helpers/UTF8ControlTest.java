package helpers;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.Locale;
import java.util.ResourceBundle;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UTF8ControlTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    private ClassLoader loader;
    private String basename = "";
    private Locale locale = Locale.ENGLISH;
    private String format = "";
    private UTF8Control utf8Control;
    private ResourceBundle resourceBundle;

    @Before
    public void setup() {
        loader = mock(ClassLoader.class);
    }

    @Test
    public void shouldCreateNoBundleIfResourceStreamCouldNotBeFound()
            throws IOException {
        givenUTF8Control();
        givenResourceStreamCouldNotBeFound();

        whenNewBundleCreationIsAttemptedWithReload(false);

        thenBundleCouldNotBeFound();
    }

    @Test
    public void shouldCreateBundleIfResourceStreamCouldBeFound()
            throws IOException {
        givenUTF8Control();
        givenResourceStreamCouldBeFound();

        whenNewBundleCreationIsAttemptedWithReload(false);

        theBundleShouldBeCreated();
    }

    @Test
    public void shouldFailToCreateBundleIfResourceStreamReadingFails()
            throws IOException {
        givenUTF8Control();
        givenResourceStreamFails();

        thenExecutionShoudlFail();

        whenNewBundleCreationIsAttemptedWithReload(false);

    }

    @Test
    public void shouldFailToCreateBundleIfResourceIsNotFound()
            throws IOException {
        givenUTF8Control();

        givenResourceIsNotFound();

        whenNewBundleCreationIsAttemptedWithReload(true);

        thenBundleCouldNotBeFound();
    }

    @Test
    public void shouldFailToCreateBundleIfUrlCannotOpenConnection()
            throws IOException {
        givenUTF8Control();
        givenCannotOpenConnection();

        whenNewBundleCreationIsAttemptedWithReload(true);

        thenBundleCouldNotBeFound();
    }

    @Test
    public void shouldCreateBundleIfUrlCanBeOpened()
            throws IOException {
        givenUTF8Control();
        givenResourceCanBeOpened();

        whenNewBundleCreationIsAttemptedWithReload(true);

        theBundleShouldBeCreated();
    }

    private void theBundleShouldBeCreated() {
        assertNotNull(resourceBundle);
    }

    private void givenResourceCanBeOpened()
            throws IOException {
        InputStream stream = new InputStream() {
            @Override
            public int read() {
                return -1;
            }
        };
        URLConnection urlConnection = mock(URLConnection.class);
        when(urlConnection.getInputStream()).thenReturn(stream);
        URLStreamHandler handler = new MyHandler(urlConnection);
        URL url = new URL("http", "dontcare", 80, "", handler);
        givenResourceReturns(url);
    }

    private void givenResourceIsNotFound() {
        givenResourceReturns(null);
    }

    private void givenResourceReturns(URL url) {
        when(loader.getResource(any())).thenReturn(url);
    }

    private void givenCannotOpenConnection()
            throws MalformedURLException {
        URLStreamHandler handler = new MyHandler(null);
        URL url = new URL("http", "dontcare", 80, "", handler);
        givenResourceReturns(url);
    }

    private void givenResourceStreamCouldBeFound() {
        InputStream stream = new InputStream() {
            @Override
            public int read() {
                return -1;
            }
        };
        givenResourceStream(stream);
    }

    private void thenExecutionShoudlFail() {
        exception.expect(IOException.class);
    }

    private void givenResourceStreamFails() {
        InputStream stream = new InputStream() {
            @Override
            public int read()
                    throws IOException {
                throw new IOException();
            }
        };
        givenResourceStream(stream);
    }


    private void givenResourceStreamCouldNotBeFound() {
        givenResourceStream(null);
    }

    private void givenResourceStream(InputStream stream) {
        when(loader.getResourceAsStream(any())).thenReturn(stream);
    }

    private void givenUTF8Control() {
        utf8Control = new UTF8Control();
    }

    private void thenBundleCouldNotBeFound() {
        assertNull(resourceBundle);
    }

    private void whenNewBundleCreationIsAttemptedWithReload(boolean reload)
            throws IOException {
        resourceBundle = utf8Control.newBundle(basename, locale, format, loader, reload);
    }

    private static class MyHandler
            extends URLStreamHandler {
        private URLConnection urlConnection;

        MyHandler(URLConnection urlConnection) {
            this.urlConnection = urlConnection;
        }

        @Override
        protected URLConnection openConnection(URL u) {
            return urlConnection;
        }
    }
}
