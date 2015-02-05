package ys.gclog.parser.extract;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

public class SimpleFullCollectionExtractorTest {

  private SimpleFullCollectionExtractor extractor;

  @Before
  public void setUp() {
    extractor = new SimpleFullCollectionExtractor();
  }

  @Test
  public void testAccept_java7_serial_full() {
    String s = "0.683: [Full GC 216597K->165851K(287704K), 0.4143117 secs]";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java7_serial_date_full() {
    String s = "2015-01-15T12:30:38.599+0300: 0.672: [Full GC 216597K->165851K(287704K), 0.3994760 secs]";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java7_parallel_full() {
    String s = "0.505: [Full GC 198466K->171397K(550912K), 3.3310004 secs]";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java7_parallel_date_full() {
    String s = "2015-01-19T14:27:22.388+0300: 0.543: [Full GC 198474K->171397K(550400K), 3.3222507 secs]";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java7_cms_full() {
    String s = "11.668: [Full GC 3939468K->252654K(3968152K), 1.6225255 secs]";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java7_cms_date_full() {
    String s = "2015-01-21T17:08:39.712+0300: 10.586: [Full GC 3521751K->190926K(3522496K), 1.2916910 secs]";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java8_serial_full() {
    String s = "0.733: [Full GC (Allocation Failure)  252395K->189781K(262016K), 0.4805541 secs]";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java8_serial_date_full() {
    // No date actually.
    String s = "0.806: [Full GC (Allocation Failure)  252395K->189781K(262016K), 0.4814904 secs]";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java8_parallel_full() {
    String s = "1.268: [Full GC (Ergonomics)  305330K->202116K(288256K), 3.5870453 secs]";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java8_parallel_date_full() {
    String s = "2015-01-23T19:02:18.483+0300: 1.792: [Full GC (Ergonomics)  318248K->3177K(87040K), 0.0414253 secs]";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java8_cms_full() {
    String s = "1.942: [Full GC (Allocation Failure)  324477K->252686K(325952K), 0.6488929 secs]";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java8_cms_date_full() {
    // No date actually.
    String s = "1.932: [Full GC (Allocation Failure)  324485K->252686K(325952K), 0.6447406 secs]";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testExtract_java7_serial_full() {
    String s = "0.683: [Full GC 216597K->165851K(287704K), 0.4143117 secs]";

    Optional<Map<String, String>> option =
        extractor.extract(s, new ExtractContext(Collections.emptyList()));
    assertTrue(option.isPresent());

    Map<String, String> values =  option.get();
    assertEquals(6, values.size());
    assertEquals("F", values.get(LogField.EVENT.toString()));
    assertEquals("0.683", values.get(LogField.EVENT_TIME.toString()));
    assertEquals("216597", values.get(LogField.MEM_BEFORE.toString()));
    assertEquals("165851", values.get(LogField.MEM_AFTER.toString()));
    assertEquals("287704", values.get(LogField.MEM_TOTAL.toString()));
    assertEquals("0.4143117", values.get(LogField.GC_TIME.toString()));
  }

  @Test
  public void testExtract_java8_serial_full() {
    String s = "0.733: [Full GC (Allocation Failure)  252395K->189781K(262016K), 0.4805541 secs]";

    Optional<Map<String, String>> option =
        extractor.extract(s, new ExtractContext(Collections.emptyList()));
    assertTrue(option.isPresent());

    Map<String, String> values =  option.get();
    assertEquals(6, values.size());
    assertEquals("F", values.get(LogField.EVENT.toString()));
    assertEquals("0.733", values.get(LogField.EVENT_TIME.toString()));
    assertEquals("252395", values.get(LogField.MEM_BEFORE.toString()));
    assertEquals("189781", values.get(LogField.MEM_AFTER.toString()));
    assertEquals("262016", values.get(LogField.MEM_TOTAL.toString()));
    assertEquals("0.4805541", values.get(LogField.GC_TIME.toString()));
  }
}
