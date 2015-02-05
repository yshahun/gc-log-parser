package ys.gclog.parser.extract;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

public class ParallelNewYoungCollectorExtractorTest {

  private ParallelNewYoungCollectorExtractor extractor;

  @Before
  public void setUp() {
    extractor = new ParallelNewYoungCollectorExtractor();
  }

  @Test
  public void testAccept_java7_cms_details_minor() {
    String s = "0.652: [GC0.652: [ParNew: 65329K->8640K(78080K), 0.1309582 secs] "
        + "175513K->190933K(260864K), 0.1310795 secs] [Times: user=0.72 sys=0.03, real=0.13 secs] ";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testExtract_java7_cms_details_minor() {
    String s = "0.652: [GC0.652: [ParNew: 65329K->8640K(78080K), 0.1309582 secs] "
        + "175513K->190933K(260864K), 0.1310795 secs] [Times: user=0.72 sys=0.03, real=0.13 secs] ";

    Optional<Map<String, String>> option =
        extractor.extract(s, new ExtractContext(Collections.emptyList()));
    assertTrue(option.isPresent());

    Map<String, String> values =  option.get();
    assertEquals(4, values.size());
    assertEquals("65329", values.get(LogField.MEM_NEW_BEFORE.toString()));
    assertEquals("8640", values.get(LogField.MEM_NEW_AFTER.toString()));
    assertEquals("78080", values.get(LogField.MEM_NEW_TOTAL.toString()));
    assertEquals("0.1309582", values.get(LogField.GC_NEW_TIME.toString()));
  }
}
