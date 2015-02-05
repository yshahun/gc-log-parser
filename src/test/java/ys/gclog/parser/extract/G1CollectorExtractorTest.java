package ys.gclog.parser.extract;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

public class G1CollectorExtractorTest {

  private G1CollectorExtractor extractor;

  @Before
  public void setUp() {
    extractor = new G1CollectorExtractor();
  }

  @Test
  public void testAccept_java7_g1_minor() {
    assertTrue(extractor.accept("0.110: [GC pause (young) 20M->18M(255M), 0.0720303 secs]"));
    assertTrue(extractor.accept("1.328: [GC pause (young) (initial-mark) 412M->419M(896M), 0.0875886 secs]"));
  }

  @Test
  public void testAccept_java7_g1_major() {
    assertTrue(extractor.accept("2.232: [GC remark, 0.0006918 secs]"));
    assertTrue(extractor.accept("2.233: [GC cleanup 677M->334M(960M), 0.0006634 secs]"));
  }

  @Test
  public void testAccept_java7_g1_concurrent() {
    assertTrue(extractor.accept("1.416: [GC concurrent-root-region-scan-start]"));
    assertTrue(extractor.accept("1.448: [GC concurrent-root-region-scan-end, 0.0322386 secs]"));
    assertTrue(extractor.accept("1.448: [GC concurrent-mark-start]"));
    assertTrue(extractor.accept("2.093: [GC concurrent-mark-end, 0.5879844 secs]"));
    assertTrue(extractor.accept("14.407: [GC concurrent-mark-abort]"));
    assertTrue(extractor.accept("2.234: [GC concurrent-cleanup-start]"));
    assertTrue(extractor.accept("2.234: [GC concurrent-cleanup-end, 0.0002412 secs]"));
  }

  @Test
  public void testAccept_java7_g1_full() {
    assertTrue(extractor.accept("13.725: [Full GC 888M->176M(588M), 0.6822729 secs]"));
  }

  @Test
  public void testAccept_java7_g1_date_minor() {
    assertTrue(extractor.accept("2015-01-22T17:13:16.502+0300: 0.115: [GC pause (young) 20M->18M(255M), 0.0746109 secs]"));
    assertTrue(extractor.accept("2015-01-22T17:13:17.742+0300: 1.349: [GC pause (young) (initial-mark) 410M->414M(896M), 0.0797546 secs]"));
  }

  @Test
  public void testAccept_java7_g1_date_major() {
    assertTrue(extractor.accept("2015-01-22T17:13:18.231+0300: 1.840: [GC remark, 0.0007809 secs]"));
    assertTrue(extractor.accept("2015-01-22T17:13:18.231+0300: 1.841: [GC cleanup 508M->165M(922M), 0.0006806 secs]"));
  }

  @Test
  public void testAccept_java7_g1_date_concurrent() {
    assertTrue(extractor.accept("2015-01-22T17:13:17.814+0300: 1.429: [GC concurrent-root-region-scan-start]"));
    assertTrue(extractor.accept("2015-01-22T17:13:17.824+0300: 1.433: [GC concurrent-root-region-scan-end, 0.0037810 secs]"));
    assertTrue(extractor.accept("2015-01-22T17:13:17.824+0300: 1.433: [GC concurrent-mark-start]"));
    assertTrue(extractor.accept("2015-01-22T17:13:18.231+0300: 1.840: [GC concurrent-mark-end, 0.4066947 secs]"));
    assertTrue(extractor.accept("2015-01-22T17:13:18.231+0300: 1.842: [GC concurrent-cleanup-start]"));
    assertTrue(extractor.accept("2015-01-22T17:13:18.231+0300: 1.842: [GC concurrent-cleanup-end, 0.0001768 secs]"));
  }

  @Test
  public void testAccept_java7_g1_date_full() {
    assertTrue(extractor.accept("2015-01-22T17:13:25.756+0300: 9.365: [Full GC 802M->117M(393M), 0.5380146 secs]"));
  }

  @Test
  public void testAccept_java7_g1_details_minor() {
    String s =
        "0.111: [GC pause (young), 0.0633706 secs]\n" +
        "   [Parallel Time: 63.0 ms, GC Workers: 8]\n" +
        "      [GC Worker Start (ms): Min: 111.1, Avg: 117.1, Max: 153.8, Diff: 42.7]\n" +
        "      [Ext Root Scanning (ms): Min: 0.0, Avg: 0.6, Max: 3.4, Diff: 3.4, Sum: 5.1]\n" +
        "      [Update RS (ms): Min: 0.0, Avg: 20.7, Max: 42.2, Diff: 42.2, Sum: 165.6]\n" +
        "         [Processed Buffers: Min: 0, Avg: 6.4, Max: 11, Diff: 11, Sum: 51]\n" +
        "      [Scan RS (ms): Min: 0.0, Avg: 1.3, Max: 3.0, Diff: 3.0, Sum: 10.7]\n" +
        "      [Object Copy (ms): Min: 12.2, Avg: 28.0, Max: 43.2, Diff: 31.0, Sum: 224.4]\n" +
        "      [Termination (ms): Min: 0.0, Avg: 6.1, Max: 8.0, Diff: 8.0, Sum: 48.6]\n" +
        "      [GC Worker Other (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.1]\n" +
        "      [GC Worker Total (ms): Min: 20.2, Avg: 56.8, Max: 62.8, Diff: 42.7, Sum: 454.5]\n" +
        "      [GC Worker End (ms): Min: 173.9, Avg: 173.9, Max: 174.0, Diff: 0.1]\n" +
        "   [Code Root Fixup: 0.0 ms]\n" +
        "   [Clear CT: 0.0 ms]\n" +
        "   [Other: 0.3 ms]\n" +
        "      [Choose CSet: 0.0 ms]\n" +
        "      [Ref Proc: 0.2 ms]\n" +
        "      [Ref Enq: 0.0 ms]\n" +
        "      [Free CSet: 0.0 ms]\n" +
        "   [Eden: 12.0M(12.0M)->0.0B(10.0M) Survivors: 0.0B->2048.0K Heap: 20.1M(255.0M)->18.8M(255.0M)]\n" +
        " [Times: user=0.25 sys=0.00, real=0.07 secs]";

    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java7_g1_details_major_remark() {
    String s = "1.870: [GC remark 1.870: [GC ref-proc, 0.0000550 secs], 0.0008780 secs]\n"
        + " [Times: user=0.00 sys=0.00, real=0.00 secs]";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java7_g1_details_major_cleanup() {
    String s = "1.871: [GC cleanup 560M->216M(922M), 0.0008481 secs]\n"
        + " [Times: user=0.00 sys=0.00, real=0.00 secs] ";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java7_g1_details_full() {
    String s =
        "6.881: [Full GC 808M->117M(393M), 0.5734608 secs]\n" +
        "   [Eden: 0.0B(49.0M)->0.0B(61.0M) Survivors: 7168.0K->0.0B Heap: 808.9M(1023.0M)->117.6M(393.0M)]\n" +
        " [Times: user=0.58 sys=0.05, real=0.57 secs] ";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java8_g1_minor() {
    assertTrue(extractor.accept("0.207: [GC pause (G1 Evacuation Pause) (young) 43M->41M(256M), 0.1056194 secs]"));
    assertTrue(extractor.accept("24.118: [GC pause (G1 Humongous Allocation) (young) (initial-mark) 537M->541M(1012M), 0.0898380 secs]"));
    assertTrue(extractor.accept("25.146: [GC pause (G1 Humongous Allocation) (young) 773M->777M(1017M), 0.1147535 secs]"));
  }

  @Test
  public void testAccept_java8_g1_full() {
    assertTrue(extractor.accept("8.583: [Full GC (Allocation Failure)  792M->117M(512M), 0.5768242 secs]"));
  }

  @Test
  public void testAccept_java8_g1_details_full() {
    String s =
        "7.779: [Full GC (Allocation Failure)  792M->117M(613M), 0.5609501 secs]\n"
        + "   [Eden: 0.0B(52.0M)->0.0B(59.0M) Survivors: 7168.0K->0.0B Heap: "
        + "792.9M(1024.0M)->117.7M(613.0M)], [Metaspace: 2643K->2643K(1056768K)]\n"
        + " [Times: user=0.56 sys=0.05, real=0.56 secs] ";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testExtract_java7_g1_minor() {
    String s = "0.110: [GC pause (young) 20M->18M(255M), 0.0720303 secs]";

    Optional<Map<String, String>> option =
        extractor.extract(s, new ExtractContext(Collections.emptyList()));
    assertTrue(option.isPresent());

    Map<String, String> values =  option.get();
    assertEquals(6, values.size());
    assertEquals("N", values.get(LogField.EVENT.toString()));
    assertEquals("0.110", values.get(LogField.EVENT_TIME.toString()));
    assertEquals("20", values.get(LogField.MEM_BEFORE.toString()));
    assertEquals("18", values.get(LogField.MEM_AFTER.toString()));
    assertEquals("255", values.get(LogField.MEM_TOTAL.toString()));
    assertEquals("0.0720303", values.get(LogField.GC_TIME.toString()));
  }

  @Test
  public void testExtract_java7_g1_major_remark() {
    String s = "2.232: [GC remark, 0.0006918 secs]";

    Optional<Map<String, String>> option =
        extractor.extract(s, new ExtractContext(Collections.emptyList()));
    assertTrue(option.isPresent());

    Map<String, String> values =  option.get();
    assertEquals(3, values.size());
    assertEquals("O", values.get(LogField.EVENT.toString()));
    assertEquals("2.232", values.get(LogField.EVENT_TIME.toString()));
    assertEquals("0.0006918", values.get(LogField.GC_TIME.toString()));
  }

  @Test
  public void testExtract_java7_g1_major_cleanup() {
    String s = "2.233: [GC cleanup 677M->334M(960M), 0.0006634 secs]";

    Optional<Map<String, String>> option =
        extractor.extract(s, new ExtractContext(Collections.emptyList()));
    assertTrue(option.isPresent());

    Map<String, String> values =  option.get();
    assertEquals(6, values.size());
    assertEquals("O", values.get(LogField.EVENT.toString()));
    assertEquals("2.233", values.get(LogField.EVENT_TIME.toString()));
    assertEquals("677", values.get(LogField.MEM_BEFORE.toString()));
    assertEquals("334", values.get(LogField.MEM_AFTER.toString()));
    assertEquals("960", values.get(LogField.MEM_TOTAL.toString()));
    assertEquals("0.0006634", values.get(LogField.GC_TIME.toString()));
  }

  @Test
  public void testExtract_java7_g1_full() {
    String s = "13.725: [Full GC 888M->176M(588M), 0.6822729 secs]";

    Optional<Map<String, String>> option =
        extractor.extract(s, new ExtractContext(Collections.emptyList()));
    assertTrue(option.isPresent());

    Map<String, String> values =  option.get();
    assertEquals(6, values.size());
    assertEquals("F", values.get(LogField.EVENT.toString()));
    assertEquals("13.725", values.get(LogField.EVENT_TIME.toString()));
    assertEquals("888", values.get(LogField.MEM_BEFORE.toString()));
    assertEquals("176", values.get(LogField.MEM_AFTER.toString()));
    assertEquals("588", values.get(LogField.MEM_TOTAL.toString()));
    assertEquals("0.6822729", values.get(LogField.GC_TIME.toString()));
  }

  @Test
  public void testExtract_java7_g1_details_minor() {
    String s =
        "0.111: [GC pause (young), 0.0633706 secs]\n" +
        "   [Parallel Time: 63.0 ms, GC Workers: 8]\n" +
        "      [GC Worker Start (ms): Min: 111.1, Avg: 117.1, Max: 153.8, Diff: 42.7]\n" +
        "      [Ext Root Scanning (ms): Min: 0.0, Avg: 0.6, Max: 3.4, Diff: 3.4, Sum: 5.1]\n" +
        "      [Update RS (ms): Min: 0.0, Avg: 20.7, Max: 42.2, Diff: 42.2, Sum: 165.6]\n" +
        "         [Processed Buffers: Min: 0, Avg: 6.4, Max: 11, Diff: 11, Sum: 51]\n" +
        "      [Scan RS (ms): Min: 0.0, Avg: 1.3, Max: 3.0, Diff: 3.0, Sum: 10.7]\n" +
        "      [Object Copy (ms): Min: 12.2, Avg: 28.0, Max: 43.2, Diff: 31.0, Sum: 224.4]\n" +
        "      [Termination (ms): Min: 0.0, Avg: 6.1, Max: 8.0, Diff: 8.0, Sum: 48.6]\n" +
        "      [GC Worker Other (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.1]\n" +
        "      [GC Worker Total (ms): Min: 20.2, Avg: 56.8, Max: 62.8, Diff: 42.7, Sum: 454.5]\n" +
        "      [GC Worker End (ms): Min: 173.9, Avg: 173.9, Max: 174.0, Diff: 0.1]\n" +
        "   [Code Root Fixup: 0.0 ms]\n" +
        "   [Clear CT: 0.0 ms]\n" +
        "   [Other: 0.3 ms]\n" +
        "      [Choose CSet: 0.0 ms]\n" +
        "      [Ref Proc: 0.2 ms]\n" +
        "      [Ref Enq: 0.0 ms]\n" +
        "      [Free CSet: 0.0 ms]\n" +
        "   [Eden: 12.0M(12.0M)->0.0B(10.0M) Survivors: 0.0B->2048.0K Heap: 20.1M(255.0M)->18.8M(255.0M)]\n" +
        " [Times: user=0.25 sys=0.00, real=0.07 secs]";

    Optional<Map<String, String>> option =
        extractor.extract(s, new ExtractContext(Collections.emptyList()));
    assertTrue(option.isPresent());

    Map<String, String> values =  option.get();
    assertEquals(14, values.size());
    assertEquals("N", values.get(LogField.EVENT.toString()));
    assertEquals("0.111", values.get(LogField.EVENT_TIME.toString()));
    assertEquals("0.0633706", values.get(LogField.GC_TIME.toString()));
    assertEquals("12.0", values.get(LogField.MEM_EDEN_BEFORE.toString()));
    assertEquals("0.0", values.get(LogField.MEM_EDEN_AFTER.toString()));
    assertEquals("10.0", values.get(LogField.MEM_EDEN_TOTAL.toString()));
    assertEquals("0.0", values.get(LogField.MEM_SURVIVOR_BEFORE.toString()));
    assertEquals("2048.0", values.get(LogField.MEM_SURVIVOR_AFTER.toString()));
    assertEquals("20.1", values.get(LogField.MEM_BEFORE.toString()));
    assertEquals("18.8", values.get(LogField.MEM_AFTER.toString()));
    assertEquals("255.0", values.get(LogField.MEM_TOTAL.toString()));
    assertEquals("0.25", values.get(LogField.GC_TIME_USER.toString()));
    assertEquals("0.00", values.get(LogField.GC_TIME_SYS.toString()));
    assertEquals("0.07", values.get(LogField.GC_TIME_REAL.toString()));
  }

  @Test
  public void testExtract_java7_g1_details_major_remark() {
    String s = "1.870: [GC remark 1.870: [GC ref-proc, 0.0000550 secs], 0.0008780 secs]\n"
        + " [Times: user=0.00 sys=0.00, real=0.00 secs]";

    Optional<Map<String, String>> option =
        extractor.extract(s, new ExtractContext(Collections.emptyList()));
    assertTrue(option.isPresent());

    Map<String, String> values =  option.get();
    assertEquals(6, values.size());
    assertEquals("O", values.get(LogField.EVENT.toString()));
    assertEquals("1.870", values.get(LogField.EVENT_TIME.toString()));
    assertEquals("0.0008780", values.get(LogField.GC_TIME.toString()));
    assertEquals("0.00", values.get(LogField.GC_TIME_USER.toString()));
    assertEquals("0.00", values.get(LogField.GC_TIME_SYS.toString()));
    assertEquals("0.00", values.get(LogField.GC_TIME_REAL.toString()));
  }

  @Test
  public void testExtract_java7_g1_details_major_cleanup() {
    String s = "1.871: [GC cleanup 560M->216M(922M), 0.0008481 secs]\n"
        + " [Times: user=0.00 sys=0.00, real=0.00 secs] ";

    Optional<Map<String, String>> option =
        extractor.extract(s, new ExtractContext(Collections.emptyList()));
    assertTrue(option.isPresent());

    Map<String, String> values =  option.get();
    assertEquals(9, values.size());
    assertEquals("O", values.get(LogField.EVENT.toString()));
    assertEquals("1.871", values.get(LogField.EVENT_TIME.toString()));
    assertEquals("560", values.get(LogField.MEM_BEFORE.toString()));
    assertEquals("216", values.get(LogField.MEM_AFTER.toString()));
    assertEquals("922", values.get(LogField.MEM_TOTAL.toString()));
    assertEquals("0.0008481", values.get(LogField.GC_TIME.toString()));
    assertEquals("0.00", values.get(LogField.GC_TIME_USER.toString()));
    assertEquals("0.00", values.get(LogField.GC_TIME_SYS.toString()));
    assertEquals("0.00", values.get(LogField.GC_TIME_REAL.toString()));
  }

  @Test
  public void testExtract_java7_g1_details_full() {
    String s =
        "6.881: [Full GC 808M->117M(393M), 0.5734608 secs]\n" +
        "   [Eden: 0.0B(49.0M)->0.0B(61.0M) Survivors: 7168.0K->0.0B Heap: 808.9M(1023.0M)->117.6M(393.0M)]\n" +
        " [Times: user=0.58 sys=0.05, real=0.57 secs] ";

    Optional<Map<String, String>> option =
        extractor.extract(s, new ExtractContext(Collections.emptyList()));
    assertTrue(option.isPresent());

    Map<String, String> values =  option.get();
    assertEquals(14, values.size());
    assertEquals("F", values.get(LogField.EVENT.toString()));
    assertEquals("6.881", values.get(LogField.EVENT_TIME.toString()));
    assertEquals("808", values.get(LogField.MEM_BEFORE.toString()));
    assertEquals("117", values.get(LogField.MEM_AFTER.toString()));
    assertEquals("393", values.get(LogField.MEM_TOTAL.toString()));
    assertEquals("0.5734608", values.get(LogField.GC_TIME.toString()));
    assertEquals("0.0", values.get(LogField.MEM_EDEN_BEFORE.toString()));
    assertEquals("0.0", values.get(LogField.MEM_EDEN_AFTER.toString()));
    assertEquals("61.0", values.get(LogField.MEM_EDEN_TOTAL.toString()));
    assertEquals("7168.0", values.get(LogField.MEM_SURVIVOR_BEFORE.toString()));
    assertEquals("0.0", values.get(LogField.MEM_SURVIVOR_AFTER.toString()));
    assertEquals("0.58", values.get(LogField.GC_TIME_USER.toString()));
    assertEquals("0.05", values.get(LogField.GC_TIME_SYS.toString()));
    assertEquals("0.57", values.get(LogField.GC_TIME_REAL.toString()));
  }

  @Test
  public void testExtract_java8_g1_minor() {
    String s = "0.207: [GC pause (G1 Evacuation Pause) (young) 43M->41M(256M), 0.1056194 secs]";

    Optional<Map<String, String>> option =
        extractor.extract(s, new ExtractContext(Collections.emptyList()));
    assertTrue(option.isPresent());

    Map<String, String> values =  option.get();
    assertEquals(6, values.size());
    assertEquals("N", values.get(LogField.EVENT.toString()));
    assertEquals("0.207", values.get(LogField.EVENT_TIME.toString()));
    assertEquals("43", values.get(LogField.MEM_BEFORE.toString()));
    assertEquals("41", values.get(LogField.MEM_AFTER.toString()));
    assertEquals("256", values.get(LogField.MEM_TOTAL.toString()));
    assertEquals("0.1056194", values.get(LogField.GC_TIME.toString()));
  }

  @Test
  public void testExtract_java8_g1_full() {
    String s = "8.583: [Full GC (Allocation Failure)  792M->117M(512M), 0.5768242 secs]";

    Optional<Map<String, String>> option =
        extractor.extract(s, new ExtractContext(Collections.emptyList()));
    assertTrue(option.isPresent());

    Map<String, String> values =  option.get();
    assertEquals(6, values.size());
    assertEquals("F", values.get(LogField.EVENT.toString()));
    assertEquals("8.583", values.get(LogField.EVENT_TIME.toString()));
    assertEquals("792", values.get(LogField.MEM_BEFORE.toString()));
    assertEquals("117", values.get(LogField.MEM_AFTER.toString()));
    assertEquals("512", values.get(LogField.MEM_TOTAL.toString()));
    assertEquals("0.5768242", values.get(LogField.GC_TIME.toString()));
  }

  @Test
  public void testExtract_java8_g1_details_full() {
    String s =
        "7.779: [Full GC (Allocation Failure)  792M->117M(613M), 0.5609501 secs]\n"
        + "   [Eden: 0.0B(52.0M)->0.0B(59.0M) Survivors: 7168.0K->0.0B Heap: "
        + "792.9M(1024.0M)->117.7M(613.0M)], [Metaspace: 2643K->2643K(1056768K)]\n"
        + " [Times: user=0.56 sys=0.05, real=0.56 secs] ";

    Optional<Map<String, String>> option =
        extractor.extract(s, new ExtractContext(Collections.emptyList()));
    assertTrue(option.isPresent());

    Map<String, String> values =  option.get();
    assertEquals(17, values.size());
    assertEquals("F", values.get(LogField.EVENT.toString()));
    assertEquals("7.779", values.get(LogField.EVENT_TIME.toString()));
    assertEquals("792", values.get(LogField.MEM_BEFORE.toString()));
    assertEquals("117", values.get(LogField.MEM_AFTER.toString()));
    assertEquals("613", values.get(LogField.MEM_TOTAL.toString()));
    assertEquals("0.5609501", values.get(LogField.GC_TIME.toString()));
    assertEquals("0.0", values.get(LogField.MEM_EDEN_BEFORE.toString()));
    assertEquals("0.0", values.get(LogField.MEM_EDEN_AFTER.toString()));
    assertEquals("59.0", values.get(LogField.MEM_EDEN_TOTAL.toString()));
    assertEquals("7168.0", values.get(LogField.MEM_SURVIVOR_BEFORE.toString()));
    assertEquals("0.0", values.get(LogField.MEM_SURVIVOR_AFTER.toString()));
    assertEquals("2643", values.get(LogField.MEM_METASPACE_BEFORE.toString()));
    assertEquals("2643", values.get(LogField.MEM_METASPACE_AFTER.toString()));
    assertEquals("1056768", values.get(LogField.MEM_METASPACE_TOTAL.toString()));
    assertEquals("0.56", values.get(LogField.GC_TIME_USER.toString()));
    assertEquals("0.05", values.get(LogField.GC_TIME_SYS.toString()));
    assertEquals("0.56", values.get(LogField.GC_TIME_REAL.toString()));
  }
}
