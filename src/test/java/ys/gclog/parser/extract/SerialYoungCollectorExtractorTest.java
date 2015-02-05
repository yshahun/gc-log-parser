package ys.gclog.parser.extract;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

public class SerialYoungCollectorExtractorTest {

  private SerialYoungCollectorExtractor extractor;

  @Before
  public void setUp() {
    extractor = new SerialYoungCollectorExtractor();
  }

  @Test
  public void testAccept_java7_serial_details_minor() {
    String s = "0.133: [GC0.133: [DefNew: 69440K->8640K(78080K), 0.0909376 secs] "
        + "69440K->46942K(251648K), 0.0910944 secs] [Times: user=0.08 sys=0.02, real=0.09 secs]";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java7_serial_details_full() {
    String s = "0.552: [GC0.552: [DefNew: 78080K->8640K(78080K), 0.1315698 secs]0.684: [Tenured: "
        + "207957K->165851K(209624K), 0.4194380 secs] 216597K->165851K(287704K), [Perm : "
        + "2665K->2665K(21248K)], 0.5513922 secs] [Times: user=0.56 sys=0.00, real=0.55 secs] ";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testExtract_java7_serial_details_minor() {
    String s = "0.133: [GC0.133: [DefNew: 69440K->8640K(78080K), 0.0909376 secs] "
        + "69440K->46942K(251648K), 0.0910944 secs] [Times: user=0.08 sys=0.02, real=0.09 secs]";

    Optional<Map<String, String>> option =
        extractor.extract(s, new ExtractContext(Collections.emptyList()));
    assertTrue(option.isPresent());

    Map<String, String> values =  option.get();
    assertEquals(4, values.size());
    assertEquals("69440", values.get(LogField.MEM_NEW_BEFORE.toString()));
    assertEquals("8640", values.get(LogField.MEM_NEW_AFTER.toString()));
    assertEquals("78080", values.get(LogField.MEM_NEW_TOTAL.toString()));
    assertEquals("0.0909376", values.get(LogField.GC_NEW_TIME.toString()));
  }

  @Test
  public void testExtract_java7_serial_details_full() {
    String s = "0.552: [GC0.552: [DefNew: 78080K->8640K(78080K), 0.1315698 secs]0.684: [Tenured: "
        + "207957K->165851K(209624K), 0.4194380 secs] 216597K->165851K(287704K), [Perm : "
        + "2665K->2665K(21248K)], 0.5513922 secs] [Times: user=0.56 sys=0.00, real=0.55 secs] ";

    Optional<Map<String, String>> option =
        extractor.extract(s, new ExtractContext(Collections.emptyList()));
    assertTrue(option.isPresent());

    Map<String, String> values =  option.get();
    assertEquals(4, values.size());
    assertEquals("78080", values.get(LogField.MEM_NEW_BEFORE.toString()));
    assertEquals("8640", values.get(LogField.MEM_NEW_AFTER.toString()));
    assertEquals("78080", values.get(LogField.MEM_NEW_TOTAL.toString()));
    assertEquals("0.1315698", values.get(LogField.GC_NEW_TIME.toString()));
  }
}
