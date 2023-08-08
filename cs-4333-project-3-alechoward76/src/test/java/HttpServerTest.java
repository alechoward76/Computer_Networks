package test.java;

/**
 * DO NOT DISTRIBUTE.
 *
 * This code is intended to support the education of students associated with the Tandy School of
 * Computer Science at the University of Tulsa. It is not intended for distribution and should
 * remain within private repositories that belong to Tandy faculty, students, and/or alumni.
 */
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsArrayContaining.hasItemInArray;
import static org.hamcrest.junit.MatcherAssume.assumeThat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.util.regex.Pattern;
import main.java.HttpServer;
import org.junit.After;
import org.junit.AssumptionViolatedException;
import org.junit.Before;
import org.junit.Test;
import test.java.TUGrader.DisplayName;
import test.java.TUGrader.TestGroup;

public class HttpServerTest {

  private File htmlDir;
  private File tmpDir;
  private File tmpFile;

  @Before
  public void before() {
    TUGrader.silenceStdout();
    this.htmlDir = new File("public_html");
  }

  @After
  public void after() {
    TUGrader.resetStdIO();
    if (this.tmpFile != null) {
      this.tmpFile.delete();
      this.tmpFile = null;
    }
    if (this.tmpDir != null) {
      this.tmpDir.delete();
      this.tmpDir = null;
    }
  }

  @Test
  @TestGroup({"statusCode", "status-200"})
  @DisplayName(
      "getStatusCode(String, int, String) should return 200 for a valid GET/HEAD request for an"
          + " html file")
  public void testGetStatusCodeForGetOnHtmlFile() {
    assumeThat(this.htmlDir.exists(), is(true));
    try {
      this.tmpFile = File.createTempFile("__test_200__", ".html", this.htmlDir);
    } catch (IOException e) {
      throw new AssumptionViolatedException(e.getMessage());
    }
    assumeThat(this.tmpFile.exists(), is(true));
    String filename = this.tmpFile.getName();
    assertThat(
        "should accept Host: <domain> when port is 80",
        HttpServer.getStatusCode(
            "test.utulsa.edu",
            80,
            String.format("GET /%s HTTP/1.1\r\nHost: test.utulsa.edu\r\n\r\n", filename)),
        is(equalTo(200)));
    assertThat(
        "should accept Host: <domain>:<port>",
        HttpServer.getStatusCode(
            "test.utulsa.edu",
            4000,
            String.format("GET /%s HTTP/1.1\r\nHost: test.utulsa.edu:4000\r\n\r\n", filename)),
        is(equalTo(200)));
    assertThat(
        "should accept Host: localhost:<port>",
        HttpServer.getStatusCode(
            "test.utulsa.edu",
            4000,
            String.format("GET /%s HTTP/1.1\r\nHost: localhost:4000\r\n\r\n", filename)),
        is(equalTo(200)));
    assertThat(
        "should accept Host: 127.0.0.1:<port>",
        HttpServer.getStatusCode(
            "test.utulsa.edu",
            4000,
            String.format("GET /%s HTTP/1.1\r\nHost: 127.0.0.1:4000\r\n\r\n", filename)),
        is(equalTo(200)));
    assertThat(
        "should accept HEAD method",
        HttpServer.getStatusCode(
            "test.utulsa.edu",
            4000,
            String.format("HEAD /%s HTTP/1.1\r\nHost: test.utulsa.edu:4000\r\n\r\n", filename)),
        is(equalTo(200)));
  }

  @Test
  @TestGroup({"statusCode", "status-200"})
  @DisplayName(
      "getStatusCode(String, int, String) should return 200 for a valid GET/HEAD request for an htm"
          + " file")
  public void testGetStatusCodeForGetOnHtmFile() {
    assumeThat(this.htmlDir.exists(), is(true));
    try {
      this.tmpFile = File.createTempFile("__test_200__", ".htm", this.htmlDir);
    } catch (IOException e) {
      throw new AssumptionViolatedException(e.getMessage());
    }
    assumeThat(this.tmpFile.exists(), is(true));
    String filename = this.tmpFile.getName();
    assertThat(
        "should accept Host: <domain> when port is 80",
        HttpServer.getStatusCode(
            "test.utulsa.edu",
            80,
            String.format("GET /%s HTTP/1.1\r\nHost: test.utulsa.edu\r\n\r\n", filename)),
        is(equalTo(200)));
    assertThat(
        "should accept Host: <domain>:<port>",
        HttpServer.getStatusCode(
            "test.utulsa.edu",
            4000,
            String.format("GET /%s HTTP/1.1\r\nHost: test.utulsa.edu:4000\r\n\r\n", filename)),
        is(equalTo(200)));
    assertThat(
        "should accept Host: localhost:<port>",
        HttpServer.getStatusCode(
            "test.utulsa.edu",
            4000,
            String.format("GET /%s HTTP/1.1\r\nHost: localhost:4000\r\n\r\n", filename)),
        is(equalTo(200)));
    assertThat(
        "should accept Host: 127.0.0.1:<port>",
        HttpServer.getStatusCode(
            "test.utulsa.edu",
            4000,
            String.format("GET /%s HTTP/1.1\r\nHost: 127.0.0.1:4000\r\n\r\n", filename)),
        is(equalTo(200)));
    assertThat(
        "should accept HEAD method",
        HttpServer.getStatusCode(
            "test.utulsa.edu",
            4000,
            String.format("HEAD /%s HTTP/1.1\r\nHost: test.utulsa.edu:4000\r\n\r\n", filename)),
        is(equalTo(200)));
  }

  @Test
  @TestGroup({"statusCode", "status-200"})
  @DisplayName(
      "getStatusCode(String, int, String) should return 200 for a valid GET/HEAD request for an gif"
          + " file")
  public void testGetStatusCodeForGetOnGifFile() {
    assumeThat(this.htmlDir.exists(), is(true));
    try {
      this.tmpFile = File.createTempFile("__test_200__", ".gif", this.htmlDir);
    } catch (IOException e) {
      throw new AssumptionViolatedException(e.getMessage());
    }
    assumeThat(this.tmpFile.exists(), is(true));
    String filename = this.tmpFile.getName();
    assertThat(
        "should accept Host: <domain> when port is 80",
        HttpServer.getStatusCode(
            "test.utulsa.edu",
            80,
            String.format("GET /%s HTTP/1.1\r\nHost: test.utulsa.edu\r\n\r\n", filename)),
        is(equalTo(200)));
    assertThat(
        "should accept Host: <domain>:<port>",
        HttpServer.getStatusCode(
            "test.utulsa.edu",
            4000,
            String.format("GET /%s HTTP/1.1\r\nHost: test.utulsa.edu:4000\r\n\r\n", filename)),
        is(equalTo(200)));
    assertThat(
        "should accept Host: localhost:<port>",
        HttpServer.getStatusCode(
            "test.utulsa.edu",
            4000,
            String.format("GET /%s HTTP/1.1\r\nHost: localhost:4000\r\n\r\n", filename)),
        is(equalTo(200)));
    assertThat(
        "should accept Host: 127.0.0.1:<port>",
        HttpServer.getStatusCode(
            "test.utulsa.edu",
            4000,
            String.format("GET /%s HTTP/1.1\r\nHost: 127.0.0.1:4000\r\n\r\n", filename)),
        is(equalTo(200)));
    assertThat(
        "should accept HEAD method",
        HttpServer.getStatusCode(
            "test.utulsa.edu",
            4000,
            String.format("HEAD /%s HTTP/1.1\r\nHost: test.utulsa.edu:4000\r\n\r\n", filename)),
        is(equalTo(200)));
  }

  @Test
  @TestGroup({"statusCode", "status-200"})
  @DisplayName(
      "getStatusCode(String, int, String) should return 200 for a valid GET/HEAD request for an jpg"
          + " file")
  public void testGetStatusCodeForGetOnJpgFile() {
    assumeThat(this.htmlDir.exists(), is(true));
    try {
      this.tmpFile = File.createTempFile("__test_200__", ".jpg", this.htmlDir);
    } catch (IOException e) {
      throw new AssumptionViolatedException(e.getMessage());
    }
    assumeThat(this.tmpFile.exists(), is(true));
    String filename = this.tmpFile.getName();
    assertThat(
        "should accept Host: <domain> when port is 80",
        HttpServer.getStatusCode(
            "test.utulsa.edu",
            80,
            String.format("GET /%s HTTP/1.1\r\nHost: test.utulsa.edu\r\n\r\n", filename)),
        is(equalTo(200)));
    assertThat(
        "should accept Host: <domain>:<port>",
        HttpServer.getStatusCode(
            "test.utulsa.edu",
            4000,
            String.format("GET /%s HTTP/1.1\r\nHost: test.utulsa.edu:4000\r\n\r\n", filename)),
        is(equalTo(200)));
    assertThat(
        "should accept Host: localhost:<port>",
        HttpServer.getStatusCode(
            "test.utulsa.edu",
            4000,
            String.format("GET /%s HTTP/1.1\r\nHost: localhost:4000\r\n\r\n", filename)),
        is(equalTo(200)));
    assertThat(
        "should accept Host: 127.0.0.1:<port>",
        HttpServer.getStatusCode(
            "test.utulsa.edu",
            4000,
            String.format("GET /%s HTTP/1.1\r\nHost: 127.0.0.1:4000\r\n\r\n", filename)),
        is(equalTo(200)));
    assertThat(
        "should accept HEAD method",
        HttpServer.getStatusCode(
            "test.utulsa.edu",
            4000,
            String.format("HEAD /%s HTTP/1.1\r\nHost: test.utulsa.edu:4000\r\n\r\n", filename)),
        is(equalTo(200)));
  }

  @Test
  @TestGroup({"statusCode", "status-200"})
  @DisplayName(
      "getStatusCode(String, int, String) should return 200 for a valid GET/HEAD request for an"
          + " jpeg file")
  public void testGetStatusCodeForGetOnJpegFile() {
    assumeThat(this.htmlDir.exists(), is(true));
    try {
      this.tmpFile = File.createTempFile("__test_200__", ".jpeg", this.htmlDir);
    } catch (IOException e) {
      throw new AssumptionViolatedException(e.getMessage());
    }
    assumeThat(this.tmpFile.exists(), is(true));
    String filename = this.tmpFile.getName();
    assertThat(
        "should accept Host: <domain> when port is 80",
        HttpServer.getStatusCode(
            "test.utulsa.edu",
            80,
            String.format("GET /%s HTTP/1.1\r\nHost: test.utulsa.edu\r\n\r\n", filename)),
        is(equalTo(200)));
    assertThat(
        "should accept Host: <domain>:<port>",
        HttpServer.getStatusCode(
            "test.utulsa.edu",
            4000,
            String.format("GET /%s HTTP/1.1\r\nHost: test.utulsa.edu:4000\r\n\r\n", filename)),
        is(equalTo(200)));
    assertThat(
        "should accept Host: localhost:<port>",
        HttpServer.getStatusCode(
            "test.utulsa.edu",
            4000,
            String.format("GET /%s HTTP/1.1\r\nHost: localhost:4000\r\n\r\n", filename)),
        is(equalTo(200)));
    assertThat(
        "should accept Host: 127.0.0.1:<port>",
        HttpServer.getStatusCode(
            "test.utulsa.edu",
            4000,
            String.format("GET /%s HTTP/1.1\r\nHost: 127.0.0.1:4000\r\n\r\n", filename)),
        is(equalTo(200)));
    assertThat(
        "should accept HEAD method",
        HttpServer.getStatusCode(
            "test.utulsa.edu",
            4000,
            String.format("HEAD /%s HTTP/1.1\r\nHost: test.utulsa.edu:4000\r\n\r\n", filename)),
        is(equalTo(200)));
  }

  @Test
  @TestGroup({"statusCode", "status-200"})
  @DisplayName(
      "getStatusCode(String, int, String) should return 200 for a valid GET/HEAD request for a pdf"
          + " file")
  public void testGetStatusCodeForGetOnPdfFile() {
    assumeThat(this.htmlDir.exists(), is(true));
    try {
      this.tmpFile = File.createTempFile("__test_200__", ".pdf", this.htmlDir);
    } catch (IOException e) {
      throw new AssumptionViolatedException(e.getMessage());
    }
    assumeThat(this.tmpFile.exists(), is(true));
    String filename = this.tmpFile.getName();
    assertThat(
        "should accept Host: <domain> when port is 80",
        HttpServer.getStatusCode(
            "test.utulsa.edu",
            80,
            String.format("GET /%s HTTP/1.1\r\nHost: test.utulsa.edu\r\n\r\n", filename)),
        is(equalTo(200)));
    assertThat(
        "should accept Host: <domain>:<port>",
        HttpServer.getStatusCode(
            "test.utulsa.edu",
            4000,
            String.format("GET /%s HTTP/1.1\r\nHost: test.utulsa.edu:4000\r\n\r\n", filename)),
        is(equalTo(200)));
    assertThat(
        "should accept Host: localhost:<port>",
        HttpServer.getStatusCode(
            "test.utulsa.edu",
            4000,
            String.format("GET /%s HTTP/1.1\r\nHost: localhost:4000\r\n\r\n", filename)),
        is(equalTo(200)));
    assertThat(
        "should accept Host: 127.0.0.1:<port>",
        HttpServer.getStatusCode(
            "test.utulsa.edu",
            4000,
            String.format("GET /%s HTTP/1.1\r\nHost: 127.0.0.1:4000\r\n\r\n", filename)),
        is(equalTo(200)));
    assertThat(
        "should accept HEAD method",
        HttpServer.getStatusCode(
            "test.utulsa.edu",
            4000,
            String.format("HEAD /%s HTTP/1.1\r\nHost: test.utulsa.edu:4000\r\n\r\n", filename)),
        is(equalTo(200)));
  }

  @Test
  @TestGroup({"statusCode", "status-200"})
  @DisplayName(
      "getStatusCode(String, int, String) should return 200 for a request with a valid URI")
  public void testGetStatusCodeOnUri() {
    assumeThat(this.htmlDir.exists(), is(true));
    try {
      this.tmpDir = Files.createTempDirectory(this.htmlDir.toPath(), "__test_dir__").toFile();
      this.tmpFile = File.createTempFile("__test_200__", ".html", this.tmpDir);
    } catch (IOException e) {
      throw new AssumptionViolatedException(e.getMessage());
    }
    assertThat(
        "should accept full URIs with the / path separator",
        HttpServer.getStatusCode(
            "test.utulsa.edu",
            4000,
            String.format(
                "GET /%s/%s HTTP/1.1\r\nHost: 127.0.0.1:4000\r\n\r\n",
                this.tmpDir.getName(), this.tmpFile.getName())),
        is(equalTo(200)));
  }

  @Test
  @TestGroup({"statusCode", "status-400"})
  @DisplayName("getStatusCode(String, int, String) should return 400 for a bad request")
  public void testGetStatusCodeOnBadRequest() {
    assertThat(
        "should return 400 when method is missing",
        HttpServer.getStatusCode(
            "test.utulsa.edu", 4000, "/index.html HTTP/1.1\r\nHost: test.utulsa.edu:4000\r\n\r\n"),
        is(equalTo(400)));
    assertThat(
        "should return 400 when space between method and URI is missing",
        HttpServer.getStatusCode(
            "test.utulsa.edu",
            4000,
            "GET/index.html HTTP/1.1\r\nHost: test.utulsa.edu:4000\r\n\r\n"),
        is(equalTo(400)));
    assertThat(
        "should return 400 when URI is missing",
        HttpServer.getStatusCode(
            "test.utulsa.edu", 4000, "GET HTTP/1.1\r\nHost: test.utulsa.edu:4000\r\n\r\n"),
        is(equalTo(400)));
    assertThat(
        "should return 400 when space between URI and HTTP version is missing",
        HttpServer.getStatusCode(
            "test.utulsa.edu",
            4000,
            "GET /index.htmlHTTP/1.1\r\nHost: test.utulsa.edu:4000\r\n\r\n"),
        is(equalTo(400)));
    assertThat(
        "should return 400 when HTTP version is missing",
        HttpServer.getStatusCode(
            "test.utulsa.edu", 4000, "GET /index.html\r\nHost: test.utulsa.edu:4000\r\n\r\n"),
        is(equalTo(400)));
    assertThat(
        "should return 400 when HTTP is missing",
        HttpServer.getStatusCode(
            "test.utulsa.edu", 4000, "GET /index.html 1.1\r\nHost: test.utulsa.edu:4000\r\n\r\n"),
        is(equalTo(400)));
    assertThat(
        "should return 400 when version is missing",
        HttpServer.getStatusCode(
            "test.utulsa.edu", 4000, "GET /index.html HTTP\r\nHost: test.utulsa.edu:4000\r\n\r\n"),
        is(equalTo(400)));
    assertThat(
        "should return 400 when CRLF between HTTP version and Host is missing",
        HttpServer.getStatusCode(
            "test.utulsa.edu", 4000, "GET /index.html HTTP/1.1 Host: test.utulsa.edu:4000\r\n\r\n"),
        is(equalTo(400)));
    assertThat(
        "should return 400 when Host: <domain>:<port> is missing",
        HttpServer.getStatusCode("test.utulsa.edu", 4000, "GET /index.html HTTP/1.1\r\n\r\n\r\n"),
        is(equalTo(400)));
    assertThat(
        "should return 400 when Host: is missing the colon",
        HttpServer.getStatusCode(
            "test.utulsa.edu",
            4000,
            "GET /index.html HTTP/1.1\r\nHost test.utulsa.edu:4000\r\n\r\n"),
        is(equalTo(400)));
    assertThat(
        "should return 400 when <domain>:<port> is missing",
        HttpServer.getStatusCode(
            "test.utulsa.edu", 4000, "GET /index.html HTTP/1.1\r\nHost:\r\n\r\n"),
        is(equalTo(400)));
    assertThat(
        "should return 400 when <domain> is wrong",
        HttpServer.getStatusCode(
            "test.utulsa.edu",
            4000,
            "GET /index.html HTTP/1.1\r\nHost: wrong.domain.edu:4000\r\n\r\n"),
        is(equalTo(400)));
    assertThat(
        "should return 400 when <port> is missing",
        HttpServer.getStatusCode(
            "test.utulsa.edu", 4000, "GET /index.html HTTP/1.1\r\nHost: test.utulsa.edu\r\n\r\n"),
        is(equalTo(400)));
    assertThat(
        "should return 400 when <port> is wrong",
        HttpServer.getStatusCode(
            "test.utulsa.edu",
            4000,
            "GET /index.html HTTP/1.1\r\nHost: test.utulsa.edu:8080\r\n\r\n"),
        is(equalTo(400)));
    assertThat(
        "should return 400 when CRLF between Host and body is missing",
        HttpServer.getStatusCode(
            "test.utulsa.edu", 4000, "GET /index.html HTTP/1.1\r\nHost: test.utulsa.edu:4000\r\n"),
        is(equalTo(400)));
    assertThat(
        "should return 400 when final CRLF is missing",
        HttpServer.getStatusCode(
            "test.utulsa.edu", 4000, "GET /index.html HTTP/1.1\r\nHost: test.utulsa.edu:4000"),
        is(equalTo(400)));
  }

  @Test
  @TestGroup({"statusCode", "status-404"})
  @DisplayName(
      "getStatusCode(String, int, String) should return 404 if the requested file does not exist")
  public void testGetStatusCodeForFileNotFound() {
    assumeThat(new File("__test_404__.html").exists(), is(false));
    assertThat(
        "should return 404 if the requested html file doesn't exit",
        HttpServer.getStatusCode(
            "test.utulsa.edu",
            4000,
            "GET /__test_404__.html HTTP/1.1\r\nHost: test.utulsa.edu:4000\r\n\r\n"),
        is(equalTo(404)));
    assumeThat(new File("__test_404__.htm").exists(), is(false));
    assertThat(
        "should return 404 if the requested html file doesn't exit",
        HttpServer.getStatusCode(
            "test.utulsa.edu",
            4000,
            "GET /__test_404__.htm HTTP/1.1\r\nHost: test.utulsa.edu:4000\r\n\r\n"),
        is(equalTo(404)));
    assumeThat(new File("__test_404__.gif").exists(), is(false));
    assertThat(
        "should return 404 if the requested html file doesn't exit",
        HttpServer.getStatusCode(
            "test.utulsa.edu",
            4000,
            "GET /__test_404__.gif HTTP/1.1\r\nHost: test.utulsa.edu:4000\r\n\r\n"),
        is(equalTo(404)));
    assumeThat(new File("__test_404__.jpg").exists(), is(false));
    assertThat(
        "should return 404 if the requested html file doesn't exit",
        HttpServer.getStatusCode(
            "test.utulsa.edu",
            4000,
            "GET /__test_404__.jpg HTTP/1.1\r\nHost: test.utulsa.edu:4000\r\n\r\n"),
        is(equalTo(404)));
    assumeThat(new File("__test_404__.jpeg").exists(), is(false));
    assertThat(
        "should return 404 if the requested html file doesn't exit",
        HttpServer.getStatusCode(
            "test.utulsa.edu",
            4000,
            "GET /__test_404__.jpeg HTTP/1.1\r\nHost: test.utulsa.edu:4000\r\n\r\n"),
        is(equalTo(404)));
    assumeThat(new File("__test_404__.pdf").exists(), is(false));
    assertThat(
        "should return 404 if the requested html file doesn't exit",
        HttpServer.getStatusCode(
            "test.utulsa.edu",
            4000,
            "GET /__test_404__.pdf HTTP/1.1\r\nHost: test.utulsa.edu:4000\r\n\r\n"),
        is(equalTo(404)));
  }

  @Test
  @TestGroup({"statusCode", "status-404"})
  @DisplayName(
      "getStatusCode(String, int, String) should return 404 if the requested file is not in dir"
          + " public_html")
  public void testGetStatusCodeForFileOutsidePubHtml() {
    try {
      this.tmpFile = File.createTempFile("__test_404__", ".html");
    } catch (IOException e) {
      throw new AssumptionViolatedException(e.getMessage());
    }
    assumeThat(this.tmpFile.exists(), is(true));
    assertThat(
        "should return 404 if the requested file isn't in dir public_html",
        HttpServer.getStatusCode(
            "test.utulsa.edu",
            4000,
            "GET /__test_404__.html HTTP/1.1\r\nHost: test.utulsa.edu:4000\r\n\r\n"),
        is(equalTo(404)));
  }

  @Test
  @TestGroup({"statusCode", "status-501"})
  @DisplayName("getStatusCode(String, int, String) should return 501 if request is not supported")
  public void testGetStatusCodeForUnsupportedRequest() {
    assertThat(
        "should return 501 if method isn't supported",
        HttpServer.getStatusCode(
            "test.utulsa.edu",
            4000,
            "PUT /index.html HTTP/1.1\r\nHost: test.utulsa.edu:4000\r\n\r\n"),
        is(equalTo(501)));
    assertThat(
        "should return 501 if file type isn't supported",
        HttpServer.getStatusCode(
            "test.utulsa.edu",
            4000,
            "GET /__test_501__.md HTTP/1.1\r\nHost: test.utulsa.edu:4000\r\n\r\n"),
        is(equalTo(501)));
    assertThat(
        "should return 501 if HTTP version isn't supported",
        HttpServer.getStatusCode(
            "test.utulsa.edu",
            4000,
            "GET /index.html HTTP/3\r\nHost: test.utulsa.edu:4000\r\n\r\n"),
        is(equalTo(501)));
  }

  @Test
  @TestGroup("reasonPhrase")
  @DisplayName("getReasonPhrase(int) should return `OK` for status 200")
  public void testGetReasonPhraseForStatus200() {
    assertThat(HttpServer.getReasonPhrase(200), is(equalTo("OK")));
  }

  @Test
  @TestGroup("reasonPhrase")
  @DisplayName("getReasonPhrase(int) should return `Bad Request` for status 400")
  public void testGetReasonPhraseForStatus400() {
    assertThat(HttpServer.getReasonPhrase(400), is(equalTo("Bad Request")));
  }

  @Test
  @TestGroup("reasonPhrase")
  @DisplayName("getReasonPhrase(int) should return `Not Found` for status 404")
  public void testGetReasonPhraseForStatus404() {
    assertThat(HttpServer.getReasonPhrase(404), is(equalTo("Not Found")));
  }

  @Test
  @TestGroup("reasonPhrase")
  @DisplayName("getReasonPhrase(int) should return `Not Implemented` for status 501")
  public void testGetReasonPhraseForStatus501() {
    assertThat(HttpServer.getReasonPhrase(501), is(equalTo("Not Implemented")));
  }

  @Test
  @TestGroup("statusLine")
  @DisplayName("getStatusLine(int) should return the correct status line")
  public void testGetStatusLine() {
    assertThat(
        "incorrect status line for status code 200",
        HttpServer.getStatusLine(200),
        is(equalTo("HTTP/1.1 200 OK\r\n")));
    assertThat(
        "incorrect status line for status code 400",
        HttpServer.getStatusLine(400),
        is(equalTo("HTTP/1.1 400 Bad Request\r\n")));
    assertThat(
        "incorrect status line for status code 404",
        HttpServer.getStatusLine(404),
        is(equalTo("HTTP/1.1 404 Not Found\r\n")));
    assertThat(
        "incorrect status line for status code 501",
        HttpServer.getStatusLine(501),
        is(equalTo("HTTP/1.1 501 Not Implemented\r\n")));
  }

  @Test
  @TestGroup("contentType")
  @DisplayName("getContentType(String) should return `text/html` for an html file")
  public void testGetContentTypeReturnsContentTypeOfHtmlFiles() {
    assertThat(
        "should recognize html files",
        HttpServer.getContentType("index.html"),
        is(equalTo("text/html")));
    assertThat(
        "should recognize html files with dashes",
        HttpServer.getContentType("file-with-dashs.html"),
        is(equalTo("text/html")));
    assertThat(
        "should recognize html files with underscores",
        HttpServer.getContentType("file_with_underscores.html"),
        is(equalTo("text/html")));
    assertThat(
        "should recognize html URIs",
        HttpServer.getContentType("/path/to/file.html"),
        is(equalTo("text/html")));
  }

  @Test
  @TestGroup("contentType")
  @DisplayName("getContentType(String) should return `text/html` for an htm file")
  public void testGetContentTypeReturnsContentTypeOfHtmFiles() {
    assertThat(
        "should recognize htm files",
        HttpServer.getContentType("index.htm"),
        is(equalTo("text/html")));
    assertThat(
        "should recognize htm files with dashes",
        HttpServer.getContentType("file-with-dashs.htm"),
        is(equalTo("text/html")));
    assertThat(
        "should recognize htm files with underscores",
        HttpServer.getContentType("file_with_underscores.htm"),
        is(equalTo("text/html")));
    assertThat(
        "should recognize htm URIs",
        HttpServer.getContentType("/path/to/file.htm"),
        is(equalTo("text/html")));
  }

  @Test
  @TestGroup("contentType")
  @DisplayName("getContentType(String) should return `image/gif` for a gif file")
  public void testGetContentTypeReturnsContentTypeOfGifFiles() {
    assertThat(
        "should recognize htm files",
        HttpServer.getContentType("index.gif"),
        is(equalTo("image/gif")));
    assertThat(
        "should recognize html files with dashes",
        HttpServer.getContentType("file-with-dashs.gif"),
        is(equalTo("image/gif")));
    assertThat(
        "should recognize html files with underscores",
        HttpServer.getContentType("file_with_underscores.gif"),
        is(equalTo("image/gif")));
    assertThat(
        "should recognize gif URIs",
        HttpServer.getContentType("/path/to/file.gif"),
        is(equalTo("image/gif")));
  }

  @Test
  @TestGroup("contentType")
  @DisplayName("getContentType(String) should return `image/jpeg` for a jpg file")
  public void testGetContentTypeReturnsContentTypeOfJpgFiles() {
    assertThat(
        "should recognize jpg files",
        HttpServer.getContentType("index.jpg"),
        is(equalTo("image/jpeg")));
    assertThat(
        "should recognize jpg files with dashes",
        HttpServer.getContentType("file-with-dashs.jpg"),
        is(equalTo("image/jpeg")));
    assertThat(
        "should recognize jpg files with underscores",
        HttpServer.getContentType("file_with_underscores.jpg"),
        is(equalTo("image/jpeg")));
    assertThat(
        "should recognize jpg URIs",
        HttpServer.getContentType("/path/to/file.jpg"),
        is(equalTo("image/jpeg")));
  }

  @Test
  @TestGroup("contentType")
  @DisplayName("getContentType(String) should return `image/jpeg` for a jpeg file")
  public void testGetContentTypeReturnsContentTypeOfJpegFiles() {
    assertThat(
        "should recognize jpeg files",
        HttpServer.getContentType("index.jpeg"),
        is(equalTo("image/jpeg")));
    assertThat(
        "should recognize jpeg files with dashes",
        HttpServer.getContentType("file-with-dashs.jpeg"),
        is(equalTo("image/jpeg")));
    assertThat(
        "should recognize jpeg files with underscores",
        HttpServer.getContentType("file_with_underscores.jpeg"),
        is(equalTo("image/jpeg")));
    assertThat(
        "should recognize jpeg URIs",
        HttpServer.getContentType("/path/to/file.jpeg"),
        is(equalTo("image/jpeg")));
  }

  @Test
  @TestGroup("contentType")
  @DisplayName("getContentType(String) should return `application/pdf` for a pdf file")
  public void testGetContentTypeReturnsContentTypeOfPdfFiles() {
    assertThat(
        "should recognize pdf files",
        HttpServer.getContentType("index.pdf"),
        is(equalTo("application/pdf")));
    assertThat(
        "should recognize pdf files with dashes",
        HttpServer.getContentType("file-with-dashs.pdf"),
        is(equalTo("application/pdf")));
    assertThat(
        "should recognize pdf files with underscores",
        HttpServer.getContentType("file_with_underscores.pdf"),
        is(equalTo("application/pdf")));
    assertThat(
        "should recognize pdf URIs",
        HttpServer.getContentType("/path/to/file.pdf"),
        is(equalTo("application/pdf")));
  }

  @Test
  @TestGroup("contentType")
  @DisplayName("getContentType(String) should return null for unrecognized file types")
  public void testGetContentTypeReturnsNullForUnrecognizedFileTypes() {
    assertThat(HttpServer.getContentType("index.htmls"), is(nullValue()));
    assertThat(HttpServer.getContentType("index.xhtml"), is(nullValue()));
    assertThat(HttpServer.getContentType("index.htms"), is(nullValue()));
    assertThat(HttpServer.getContentType("index.xhtm"), is(nullValue()));
    assertThat(HttpServer.getContentType("index.gifs"), is(nullValue()));
    assertThat(HttpServer.getContentType("index.xgif"), is(nullValue()));
    assertThat(HttpServer.getContentType("index.jpgs"), is(nullValue()));
    assertThat(HttpServer.getContentType("index.xjpg"), is(nullValue()));
    assertThat(HttpServer.getContentType("index.jpegs"), is(nullValue()));
    assertThat(HttpServer.getContentType("index.xjpeg"), is(nullValue()));
    assertThat(HttpServer.getContentType("index.pdfs"), is(nullValue()));
    assertThat(HttpServer.getContentType("index.xpdf"), is(nullValue()));
    assertThat(HttpServer.getContentType("index.java"), is(nullValue()));
    assertThat(HttpServer.getContentType("index.md"), is(nullValue()));
  }

  @Test
  @TestGroup("entityHeaders")
  @DisplayName(
      "getEntityHeaders(String, String, long, String) should return 'Server', 'Content-Length', and"
          + " 'Content-Type' entity headers when the response contains file content.")
  public void testGetEntityHeadersForResponseWithContent() {
    assertThat(
        HttpServer.getEntityHeaders("test.utulsa.edu", "1.0.2", 2048, "text/html"),
        is(
            arrayContainingInAnyOrder(
                "Server: test.utulsa.edu/1.0.2",
                "Content-Length: 2048",
                "Content-Type: text/html")));
  }

  @Test
  @TestGroup("entityHeaders")
  @DisplayName(
      "getEntityHeaders(String, String, long, String) should return at least 'Server' entity header"
          + " when the response doesn't contain file content.")
  public void testGetEntityHeadersForResponseWithoutContent() {
    assertThat(
        HttpServer.getEntityHeaders("test.utulsa.edu", "1.0.2", 0, null),
        hasItemInArray("Server: test.utulsa.edu/1.0.2"));
  }

  @Test
  @TestGroup("responseHeader")
  @DisplayName(
      "getResponseHeader(String, String, int, String) should return correct response header for a"
          + " 200 response")
  public void testGetResponseHeaderForResponse200() {
    assumeThat(this.htmlDir.exists(), is(true));
    String content = "<p>Hello, World!</p>";
    try {
      this.tmpFile = File.createTempFile("__test_200_OK__", ".html", this.htmlDir);
      BufferedWriter writer = new BufferedWriter(new FileWriter(this.tmpFile));
      writer.write(content);
      writer.close();
    } catch (IOException e) {
      throw new AssumptionViolatedException(e.getMessage());
    }
    String statusLine = "HTTP/1.1 200 OK";
    String server = "Server: test.utulsa.edu/1.0.2";
    String contentLength = String.format("Content-Length: %d", content.length());
    String contentType = "Content-Type: text/html";
    assertThat(
        HttpServer.getResponseHeader("test.utulsa.edu", "1.0.2", 200, this.tmpFile.getName()),
        is(
            anyOf(
                equalTo(
                    String.join("\r\n", statusLine, server, contentLength, contentType, "", "")),
                equalTo(
                    String.join("\r\n", statusLine, server, contentType, contentLength, "", "")),
                equalTo(
                    String.join("\r\n", statusLine, contentLength, server, contentType, "", "")),
                equalTo(
                    String.join("\r\n", statusLine, contentLength, contentType, server, "", "")),
                equalTo(
                    String.join("\r\n", statusLine, contentType, server, contentLength, "", "")),
                equalTo(
                    String.join("\r\n", statusLine, contentType, contentLength, server, "", "")))));
  }

  @Test
  @TestGroup("responseHeader")
  @DisplayName(
      "getResponseHeader(String, String, int, String) should return correct response header for a"
          + " 400 response")
  @SuppressWarnings("unchecked")
  public void testGetResponseHeaderForResponse400() {
    assumeThat(this.htmlDir.exists(), is(true));
    String statusLine = "HTTP/1.1 400 Bad Request";
    String server = "Server: test.utulsa.edu/1.0.2";
    String contentLength =
        String.format("Content-Length: %d", new File(this.htmlDir, "index.html").length());
    String contentType = "Content-Type: text/html";
    assertThat(
        HttpServer.getResponseHeader("test.utulsa.edu", "1.0.2", 400, "index.html"),
        is(
            anyOf(
                equalTo(String.join("\r\n", statusLine, server, "", "")),
                equalTo(String.join("\r\n", statusLine, server, contentLength, "", "")),
                equalTo(String.join("\r\n", statusLine, server, contentType, "", "")),
                equalTo(
                    String.join("\r\n", statusLine, server, contentLength, contentType, "", "")),
                equalTo(
                    String.join("\r\n", statusLine, server, contentType, contentLength, "", "")),
                equalTo(
                    String.join("\r\n", statusLine, contentLength, server, contentType, "", "")),
                equalTo(
                    String.join("\r\n", statusLine, contentLength, contentType, server, "", "")),
                equalTo(
                    String.join("\r\n", statusLine, contentType, server, contentLength, "", "")),
                equalTo(
                    String.join("\r\n", statusLine, contentType, contentLength, server, "", "")))));
  }

  @Test
  @TestGroup("responseHeader")
  @DisplayName(
      "getResponseHeader(String, String, int, String) should return correct response header for a"
          + " 404 response")
  @SuppressWarnings("unchecked")
  public void testGetResponseHeaderForResponse404() {
    String statusLine = "HTTP/1.1 404 Not Found";
    String server = "Server: test.utulsa.edu/1.0.2";
    String contentLength = "Content-Length: 0";
    String contentType = "Content-Type: text/html";
    assertThat(
        HttpServer.getResponseHeader("test.utulsa.edu", "1.0.2", 404, "__test_404__.html"),
        is(
            anyOf(
                equalTo(String.join("\r\n", statusLine, server, "", "")),
                equalTo(String.join("\r\n", statusLine, server, contentLength, "", "")),
                equalTo(String.join("\r\n", statusLine, server, contentType, "", "")),
                equalTo(
                    String.join("\r\n", statusLine, server, contentLength, contentType, "", "")),
                equalTo(
                    String.join("\r\n", statusLine, server, contentType, contentLength, "", "")),
                equalTo(
                    String.join("\r\n", statusLine, contentLength, server, contentType, "", "")),
                equalTo(
                    String.join("\r\n", statusLine, contentLength, contentType, server, "", "")),
                equalTo(
                    String.join("\r\n", statusLine, contentType, server, contentLength, "", "")),
                equalTo(
                    String.join("\r\n", statusLine, contentType, contentLength, server, "", "")))));
  }

  @Test
  @TestGroup("responseHeader")
  @DisplayName(
      "getResponseHeader(String, String, int, String) should return correct response header for a"
          + " 501 response")
  @SuppressWarnings("unchecked")
  public void testGetResponseHeaderForResponse501() {
    assumeThat(this.htmlDir.exists(), is(true));
    String statusLine = "HTTP/1.1 501 Not Implemented";
    String server = "Server: test.utulsa.edu/1.0.2";
    String contentLength =
        String.format("Content-Length: %d", new File(this.htmlDir, "index.html").length());
    String contentType = "Content-Type: text/html";
    assertThat(
        HttpServer.getResponseHeader("test.utulsa.edu", "1.0.2", 501, "index.html"),
        is(
            anyOf(
                equalTo(String.join("\r\n", statusLine, server, "", "")),
                equalTo(String.join("\r\n", statusLine, server, contentLength, "", "")),
                equalTo(String.join("\r\n", statusLine, server, contentType, "", "")),
                equalTo(
                    String.join("\r\n", statusLine, server, contentLength, contentType, "", "")),
                equalTo(
                    String.join("\r\n", statusLine, server, contentType, contentLength, "", "")),
                equalTo(
                    String.join("\r\n", statusLine, contentLength, server, contentType, "", "")),
                equalTo(
                    String.join("\r\n", statusLine, contentLength, contentType, server, "", "")),
                equalTo(
                    String.join("\r\n", statusLine, contentType, server, contentLength, "", "")),
                equalTo(
                    String.join("\r\n", statusLine, contentType, contentLength, server, "", "")))));
  }

  @Test
  @TestGroup("response")
  @DisplayName("HttpServer should respond 400 on bad requests")
  public void testGetResponse400() {
    try {
      int port = 5050;
      String msg = String.format("HEAD HTTP/1.1\r\nHost: localhost:%d\r\n\r\n", port);
      String responsePattern = "^HTTP/1.1 400 Bad Request\r\nServer:(.+)\r\n(.*)\r\n";
      Thread thread =
          new Thread(
              new Runnable() {
                public void run() {
                  try {
                    HttpServer server = new HttpServer(port);
                    server.close();
                  } catch (IOException e) {
                    throw new AssumptionViolatedException(e.getMessage());
                  }
                }
              });
      thread.start();
      Thread.sleep(100);
      Socket client = new Socket("127.0.0.1", port);
      BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
      PrintWriter out = new PrintWriter(client.getOutputStream(), true);
      out.println(msg);
      while (!in.ready()) {}
      StringBuilder response = new StringBuilder();
      while (in.ready()) {
        response.append((char) in.read());
      }
      out.close();
      in.close();
      client.close();
      assertThat(
          String.format(
              "expected match on pattern '%s' with status code 400 but was '%s'",
              responsePattern, response.toString()),
          Pattern.compile(responsePattern).matcher(response.toString()).find(),
          is(true));
    } catch (IOException | InterruptedException e) {
      throw new AssumptionViolatedException(e.getMessage());
    }
  }

  @Test
  @TestGroup("response")
  @DisplayName("HttpServer should respond 501 on unsupported requests")
  public void testGetResponse501() {
    try {
      int port = 5051;
      String msg =
          String.format(
              "PUT /__test-get-response-501__.html HTTP/1.1\r\nHost: localhost:%d\r\n\r\n", port);
      String responsePattern = "^HTTP/1.1 501 Not Implemented\r\nServer:(.+)\r\n(.*)\r\n";
      Thread thread =
          new Thread(
              new Runnable() {
                public void run() {
                  try {
                    HttpServer server = new HttpServer(port);
                    server.close();
                  } catch (IOException e) {
                    throw new AssumptionViolatedException(e.getMessage());
                  }
                }
              });
      thread.start();
      Thread.sleep(100);
      Socket client = new Socket("127.0.0.1", port);
      BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
      PrintWriter out = new PrintWriter(client.getOutputStream(), true);
      out.println(msg);
      while (!in.ready()) {}
      StringBuilder response = new StringBuilder();
      while (in.ready()) {
        response.append((char) in.read());
      }
      out.close();
      in.close();
      client.close();
      assertThat(
          String.format(
              "expected match on pattern '%s' with status code 501 but was '%s'",
              responsePattern, response.toString()),
          Pattern.compile(responsePattern).matcher(response.toString()).find(),
          is(true));
    } catch (IOException | InterruptedException e) {
      throw new AssumptionViolatedException(e.getMessage());
    }
  }

  @Test
  @TestGroup("response")
  @DisplayName("HttpServer should respond 404 on files not found")
  public void testGetResponse404() {
    try {
      int port = 5052;
      String msg =
          String.format(
              "HEAD /__test-get-response-404__.html HTTP/1.1\r\nHost: localhost:%d\r\n\r\n", port);
      String responsePattern = "^HTTP/1.1 404 Not Found\r\nServer:(.+)\r\n(.*)\r\n";
      Thread thread =
          new Thread(
              new Runnable() {
                public void run() {
                  try {
                    HttpServer server = new HttpServer(port);
                    server.close();
                  } catch (IOException e) {
                    throw new AssumptionViolatedException(e.getMessage());
                  }
                }
              });
      thread.start();
      Thread.sleep(100);
      Socket client = new Socket("127.0.0.1", port);
      BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
      PrintWriter out = new PrintWriter(client.getOutputStream(), true);
      out.println(msg);
      while (!in.ready()) {}
      StringBuilder response = new StringBuilder();
      while (in.ready()) {
        response.append((char) in.read());
      }
      out.close();
      in.close();
      client.close();
      assertThat(
          String.format(
              "expected match on pattern '%s' with status code 404 but was '%s'",
              responsePattern, response.toString()),
          Pattern.compile(responsePattern).matcher(response.toString()).find(),
          is(true));
    } catch (IOException | InterruptedException e) {
      throw new AssumptionViolatedException(e.getMessage());
    }
  }

  @Test
  @TestGroup("response")
  @DisplayName("HttpServer should respond 200 on good requests")
  public void testGetResponse200() {
    assumeThat(this.htmlDir.exists(), is(true));
    try {
      this.tmpFile = File.createTempFile("__test-get-response-200__", ".html", this.htmlDir);
    } catch (IOException e) {
      throw new AssumptionViolatedException(e.getMessage());
    }
    assumeThat(this.tmpFile.exists(), is(true));
    String filename = this.tmpFile.getName();
    try {
      int port = 5053;
      String msg = String.format("HEAD /%s HTTP/1.1\r\nHost: localhost:%d\r\n\r\n", filename, port);
      String responsePattern =
          "^HTTP/1.1 200 OK\r\n"
              + "Server:(.+)\r\n"
              + "Content-Length:[\\s]*0[\\s]*\r\n"
              + "Content-Type:[\\s]*text/html[\\s]*\r\n\r\n";
      Thread thread =
          new Thread(
              new Runnable() {
                public void run() {
                  try {
                    HttpServer server = new HttpServer(port);
                    server.close();
                  } catch (IOException e) {
                    throw new AssumptionViolatedException(e.getMessage());
                  }
                }
              });
      thread.start();
      Thread.sleep(100);
      Socket client = new Socket("127.0.0.1", port);
      BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
      PrintWriter out = new PrintWriter(client.getOutputStream(), true);
      out.println(msg);
      while (!in.ready()) {}
      StringBuilder response = new StringBuilder();
      while (in.ready()) {
        response.append((char) in.read());
      }
      out.close();
      in.close();
      client.close();
      assertThat(
          String.format(
              "expected match on pattern '%s' with status code 200 but was '%s'",
              responsePattern, response.toString()),
          Pattern.compile(responsePattern).matcher(response.toString()).find(),
          is(true));
    } catch (IOException | InterruptedException e) {
      throw new AssumptionViolatedException(e.getMessage());
    }
  }

  @Test
  @TestGroup("response")
  @DisplayName("HttpServer should return file content on 200 with GET")
  public void testGetFile() {
    assumeThat(this.htmlDir.exists(), is(true));
    try {
      this.tmpFile = File.createTempFile("__test-get-file__", ".html", this.htmlDir);
    } catch (IOException e) {
      throw new AssumptionViolatedException(e.getMessage());
    }
    assumeThat(this.tmpFile.exists(), is(true));
    String filename = this.tmpFile.getName();
    String content = "greetings from outer space!";
    BufferedWriter writer = null;
    try {
      writer = new BufferedWriter(new FileWriter(this.tmpFile));
      writer.write(content);
      writer.flush();
    } catch (IOException e) {
      throw new AssumptionViolatedException(e.getMessage());
    } finally {
      try {
        writer.close();
      } catch (IOException e) {
        throw new AssumptionViolatedException(e.getMessage());
      }
    }
    try {
      int port = 5054;
      String msg = String.format("GET /%s HTTP/1.1\r\nHost: localhost:%d\r\n\r\n", filename, port);
      String responsePattern =
          String.format(
              "^HTTP/1.1 200 OK\r\n"
                  + "Server:(.+)\r\n"
                  + "Content-Length:[\\s]*%d[\\s]*\r\n"
                  + "Content-Type:[\\s]*text/html[\\s]*\r\n\r\n"
                  + "%s",
              content.length(), content);
      Thread thread =
          new Thread(
              new Runnable() {
                public void run() {
                  try {
                    HttpServer server = new HttpServer(port);
                    server.close();
                  } catch (IOException e) {
                    throw new AssumptionViolatedException(e.getMessage());
                  }
                }
              });
      thread.start();
      Thread.sleep(100);
      Socket client = new Socket("127.0.0.1", port);
      BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
      PrintWriter out = new PrintWriter(client.getOutputStream(), true);
      out.println(msg);
      while (!in.ready()) {}
      StringBuilder response = new StringBuilder();
      while (in.ready()) {
        response.append((char) in.read());
      }
      out.close();
      in.close();
      client.close();
      assertThat(
          String.format(
              "expected match on pattern '%s' with status code 200 but was '%s'",
              responsePattern, response.toString()),
          Pattern.compile(responsePattern).matcher(response.toString()).find(),
          is(true));
    } catch (IOException | InterruptedException e) {
      throw new AssumptionViolatedException(e.getMessage());
    }
  }
}