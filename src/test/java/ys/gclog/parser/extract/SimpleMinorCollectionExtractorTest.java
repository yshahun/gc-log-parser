package ys.gclog.parser.extract;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

public class SimpleMinorCollectionExtractorTest {

  private SimpleMinorCollectionExtractor extractor;

  @Before
  public void setUp() {
    extractor = new SimpleMinorCollectionExtractor();
  }

  @Test
  public void testAccept_java7_serial_minor() {
    String s = "0.131: [GC 69440K->46942K(251648K), 0.0902211 secs]";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java7_serial_date_minor() {
    String s = "2015-01-15T12:30:38.059+0300: 0.138: [GC 69440K->46942K(251648K), 0.0858643 secs]";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java7_parallel_minor() {
    String s = "0.209: [GC 94161K->91267K(315392K), 0.0716301 secs]";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java7_parallel_date_minor() {
    String s = "2015-01-19T14:27:22.252+0300: 0.407: [GC 222363K->198474K(329728K), 0.1361051 secs]";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java8_serial_minor() {
    String s = "0.070: [GC (Allocation Failure)  1024K->689K(262016K), 0.0019299 secs]";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java8_serial_date_minor() {
    String s = "2015-01-23T16:26:45.866+0300: 0.073: [GC (Allocation Failure)  1024K->689K(262016K), 0.0019005 secs]";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java8_parallel_minor() {
    String s = "0.069: [GC (Allocation Failure)  504K->496K(261632K), 0.0009330 secs]";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java8_parallel_date_minor() {
    String s = "2015-01-23T19:02:16.760+0300: 0.070: [GC (Allocation Failure)  504K->504K(261632K), 0.0006433 secs]";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java8_cms_minor() {
    String s = "0.090: [GC (Allocation Failure)  1024K->842K(262016K), 0.0041663 secs]";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java8_cms_date_minor() {
    String s = "2015-01-23T20:40:32.548+0300: 0.069: [GC (Allocation Failure)  1024K->864K(262016K), 0.0038840 secs]";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testExtract_java7_serial_minor() {
    String s = "0.131: [GC 69440K->46942K(251648K), 0.0902211 secs]";

    Optional<Map<String, String>> option =
        extractor.extract(s, new ExtractContext(Collections.emptyList()));
    assertTrue(option.isPresent());

    Map<String, String> values =  option.get();
    assertEquals(6, values.size());
    assertEquals("N", values.get(LogField.EVENT.toString()));
    assertEquals("0.131", values.get(LogField.EVENT_TIME.toString()));
    assertEquals("69440", values.get(LogField.MEM_BEFORE.toString()));
    assertEquals("46942", values.get(LogField.MEM_AFTER.toString()));
    assertEquals("251648", values.get(LogField.MEM_TOTAL.toString()));
    assertEquals("0.0902211", values.get(LogField.GC_TIME.toString()));
  }

  @Test
  public void testExtract_java7_serial_date_minor() {
    String s = "2015-01-15T12:30:38.059+0300: 0.138: [GC 69440K->46942K(251648K), 0.0858643 secs]";

    Optional<Map<String, String>> option =
        extractor.extract(s, new ExtractContext(Collections.emptyList()));
    assertTrue(option.isPresent());

    Map<String, String> values =  option.get();
    assertEquals(7, values.size());
    assertEquals("N", values.get(LogField.EVENT.toString()));
    assertEquals("2015-01-15T12:30:38.059+0300", values.get(LogField.EVENT_DATE.toString()));
    assertEquals("0.138", values.get(LogField.EVENT_TIME.toString()));
    assertEquals("69440", values.get(LogField.MEM_BEFORE.toString()));
    assertEquals("46942", values.get(LogField.MEM_AFTER.toString()));
    assertEquals("251648", values.get(LogField.MEM_TOTAL.toString()));
    assertEquals("0.0858643", values.get(LogField.GC_TIME.toString()));
  }

  @Test
  public void testExtract_java8_serial_minor() {
    String s = "0.070: [GC (Allocation Failure)  1024K->689K(262016K), 0.0019299 secs]";

    Optional<Map<String, String>> option =
        extractor.extract(s, new ExtractContext(Collections.emptyList()));
    assertTrue(option.isPresent());

    Map<String, String> values =  option.get();
    assertEquals(6, values.size());
    assertEquals("N", values.get(LogField.EVENT.toString()));
    assertEquals("0.070", values.get(LogField.EVENT_TIME.toString()));
    assertEquals("1024", values.get(LogField.MEM_BEFORE.toString()));
    assertEquals("689", values.get(LogField.MEM_AFTER.toString()));
    assertEquals("262016", values.get(LogField.MEM_TOTAL.toString()));
    assertEquals("0.0019299", values.get(LogField.GC_TIME.toString()));
  }

  @Test
  public void testExtract_java8_serial_date_minor() {
    String s = "2015-01-23T16:26:45.866+0300: 0.073: [GC (Allocation Failure)  1024K->689K(262016K), 0.0019005 secs]";

    Optional<Map<String, String>> option =
        extractor.extract(s, new ExtractContext(Collections.emptyList()));
    assertTrue(option.isPresent());

    Map<String, String> values =  option.get();
    assertEquals(7, values.size());
    assertEquals("N", values.get(LogField.EVENT.toString()));
    assertEquals("2015-01-23T16:26:45.866+0300", values.get(LogField.EVENT_DATE.toString()));
    assertEquals("0.073", values.get(LogField.EVENT_TIME.toString()));
    assertEquals("1024", values.get(LogField.MEM_BEFORE.toString()));
    assertEquals("689", values.get(LogField.MEM_AFTER.toString()));
    assertEquals("262016", values.get(LogField.MEM_TOTAL.toString()));
    assertEquals("0.0019005", values.get(LogField.GC_TIME.toString()));
  }
}
