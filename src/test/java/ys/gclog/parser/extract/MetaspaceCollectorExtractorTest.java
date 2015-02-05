package ys.gclog.parser.extract;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

public class MetaspaceCollectorExtractorTest {

  private MetaspaceCollectorExtractor extractor;

  @Before
  public void setUp() {
    extractor = new MetaspaceCollectorExtractor();
  }

  @Test
  public void testAccept_java8_serial_details_full() {
    String s = "0.742: [GC (Allocation Failure) 0.742: [DefNew: 579K->128K(1152K), 0.0015646 secs]"
        + "0.744: [Tenured: 252267K->189781K(260864K), 0.4807888 secs] 252413K->189781K(262016K), "
        + "[Metaspace: 2637K->2637K(1056768K)], 0.4827313 secs] [Times: user=0.48 sys=0.00, real=0.48 secs] ";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java8_cms_details_full() {
    String s = "1.479: [GC (Allocation Failure) 1.479: [ParNew: 619K->127K(1152K), 0.0051739 secs]"
        + "1.484: [CMS1.873: [CMS-concurrent-mark: 0.510/1.266 secs] [Times: user=6.15 sys=0.19, "
        + "real=1.27 secs] \n (concurrent mode failure): 324150K->252686K(324608K), 1.0432362 secs]"
        + " 324045K->252686K(325760K), [Metaspace: 2625K->2625K(1056768K)], 1.0489746 secs] "
        + "[Times: user=1.30 sys=0.11, real=1.05 secs] ";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testExtract_java8_serial_details_full() {
    String s = "0.742: [GC (Allocation Failure) 0.742: [DefNew: 579K->128K(1152K), 0.0015646 secs]"
        + "0.744: [Tenured: 252267K->189781K(260864K), 0.4807888 secs] 252413K->189781K(262016K), "
        + "[Metaspace: 2637K->2637K(1056768K)], 0.4827313 secs] [Times: user=0.48 sys=0.00, real=0.48 secs] ";

    Optional<Map<String, String>> option =
        extractor.extract(s, new ExtractContext(Collections.emptyList()));
    assertTrue(option.isPresent());

    Map<String, String> values =  option.get();
    assertEquals(3, values.size());
    assertEquals("2637", values.get(LogField.MEM_METASPACE_BEFORE.toString()));
    assertEquals("2637", values.get(LogField.MEM_METASPACE_AFTER.toString()));
    assertEquals("1056768", values.get(LogField.MEM_METASPACE_TOTAL.toString()));
  }

  @Test
  public void testExtract_java8_cms_details_full() {
    String s = "1.479: [GC (Allocation Failure) 1.479: [ParNew: 619K->127K(1152K), 0.0051739 secs]"
        + "1.484: [CMS1.873: [CMS-concurrent-mark: 0.510/1.266 secs] [Times: user=6.15 sys=0.19, "
        + "real=1.27 secs] \n (concurrent mode failure): 324150K->252686K(324608K), 1.0432362 secs]"
        + " 324045K->252686K(325760K), [Metaspace: 2625K->2625K(1056768K)], 1.0489746 secs] "
        + "[Times: user=1.30 sys=0.11, real=1.05 secs] ";

    Optional<Map<String, String>> option =
        extractor.extract(s, new ExtractContext(Collections.emptyList()));
    assertTrue(option.isPresent());

    Map<String, String> values =  option.get();
    assertEquals(3, values.size());
    assertEquals("2625", values.get(LogField.MEM_METASPACE_BEFORE.toString()));
    assertEquals("2625", values.get(LogField.MEM_METASPACE_AFTER.toString()));
    assertEquals("1056768", values.get(LogField.MEM_METASPACE_TOTAL.toString()));
  }
}
