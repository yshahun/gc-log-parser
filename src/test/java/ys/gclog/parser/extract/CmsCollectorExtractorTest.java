package ys.gclog.parser.extract;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

public class CmsCollectorExtractorTest {

  private CmsCollectorExtractor extractor;

  @Before
  public void setUp() {
    extractor = new CmsCollectorExtractor();
  }

  @Test
  public void testAccept_java7_cms_major() {
    String s = "0.489: [GC 142860K(251648K), 0.0272534 secs]";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java7_cms_date_major() {
    String s = "2015-01-21T17:08:29.698+0300: 0.564: [GC 142860K(251648K), 0.0268580 secs]";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java7_cms_details_mark_initial() {
    String s = "45.854: [GC [1 CMS-initial-mark: 1885273K(3484096K)] 2007452K(4097536K), "
        + "0.1069750 secs] [Times: user=0.11 sys=0.00, real=0.11 secs] ";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java7_cms_details_date_mark_initial() {
    String s = "2015-01-22T11:58:40.204+0300: 0.498: [GC [1 CMS-initial-mark: 110183K(173568K)] "
        + "142860K(251648K), 0.0269829 secs] [Times: user=0.03 sys=0.00, real=0.02 secs] ";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java7_cms_details_concurrent() {
    assertTrue(extractor.accept("21.876: [CMS-concurrent-mark-start]"));
    assertTrue(extractor.accept("22.139: [CMS-concurrent-mark: 0.263/0.263 secs] "
        + "[Times: user=0.80 sys=0.00, real=0.27 secs] "));

    assertTrue(extractor.accept("22.139: [CMS-concurrent-preclean-start]"));
    assertTrue(extractor.accept("22.144: [CMS-concurrent-preclean: 0.005/0.005 secs] "
        + "[Times: user=0.00 sys=0.00, real=0.00 secs] "));

    assertTrue(extractor.accept("22.144: [CMS-concurrent-abortable-preclean-start]"));
    assertTrue(extractor.accept("22.952: [CMS-concurrent-abortable-preclean: 0.470/0.808 secs] "
        + "[Times: user=2.73 sys=0.00, real=0.81 secs] "));

    assertTrue(extractor.accept("23.148: [CMS-concurrent-sweep-start]"));
    assertTrue(extractor.accept("26.461: [CMS-concurrent-sweep: 2.213/3.313 secs] "
        + "[Times: user=12.00 sys=0.02, real=3.31 secs] "));

    assertTrue(extractor.accept("26.461: [CMS-concurrent-reset-start]"));
    assertTrue(extractor.accept("226.465: [CMS-concurrent-reset: 0.004/0.004 secs] "
        + "[Times: user=0.00 sys=0.00, real=0.00 secs] "));
  }

  @Test
  public void testAccept_java7_cms_details_date_concurrent() {
    assertTrue(extractor.accept("2015-01-22T11:59:13.441+0300: 33.738: [CMS-concurrent-mark-start]"));
    assertTrue(extractor.accept("2015-01-22T11:59:13.681+0300: 33.975: [CMS-concurrent-mark: "
        + "0.237/0.237 secs] [Times: user=0.70 sys=0.00, real=0.24 secs] "));

    assertTrue(extractor.accept("2015-01-22T11:59:13.681+0300: 33.975: [CMS-concurrent-preclean-start]"));
    assertTrue(extractor.accept("2015-01-22T11:59:13.681+0300: 33.981: [CMS-concurrent-preclean: "
        + "0.005/0.005 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] "));

    assertTrue(extractor.accept("2015-01-22T11:59:13.681+0300: 33.981: "
        + "[CMS-concurrent-abortable-preclean-start]"));
    assertTrue(extractor.accept("2015-01-22T11:59:14.493+0300: 34.786: "
        + "[CMS-concurrent-abortable-preclean: 0.467/0.805 secs] "
        + "[Times: user=2.42 sys=0.00, real=0.81 secs] "));

    assertTrue(extractor.accept("2015-01-22T11:59:14.693+0300: 34.986: [CMS-concurrent-sweep-start]"));
    assertTrue(extractor.accept("2015-01-22T11:59:18.962+0300: 39.257: [CMS-concurrent-sweep: "
        + "2.720/4.271 secs] [Times: user=15.79 sys=0.01, real=4.27 secs] "));

    assertTrue(extractor.accept("2015-01-22T11:59:18.962+0300: 39.258: [CMS-concurrent-reset-start]"));
    assertTrue(extractor.accept("2015-01-22T11:59:18.962+0300: 39.262: [CMS-concurrent-reset: "
        + "0.004/0.004 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] "));
  }

  @Test
  public void testAccept_java7_cms_details_remark() {
    String s = "22.957: [GC[YG occupancy: 228273 K (613440 K)]22.957: [Rescan (parallel) , "
        + "0.1903250 secs]23.147: [weak refs processing, 0.0000135 secs]23.147: "
        + "[scrub string table, 0.0001064 secs] [1 CMS-remark: 1869858K(3484096K)] "
        + "2098132K(4097536K), 0.1905419 secs] [Times: user=0.89 sys=0.00, real=0.19 secs] ";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java7_cms_details_date_remark() {
    String s = "2015-01-22T11:59:01.985+0300: 22.278: [GC[YG occupancy: 238637 K (613440 K)]"
        + "2015-01-22T11:59:01.985+0300: 22.278: [Rescan (parallel) , 0.1898640 secs]"
        + "2015-01-22T11:59:02.175+0300: 22.468: [weak refs processing, 0.0000182 secs]"
        + "2015-01-22T11:59:02.175+0300: 22.468: [scrub string table, 0.0001012 secs] "
        + "[1 CMS-remark: 1866997K(3484096K)] 2105634K(4097536K), 0.1901015 secs] "
        + "[Times: user=0.75 sys=0.00, real=0.19 secs]";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java7_cms_details_fullgc() {
    String s = "8.810: [GC8.810: [ParNew: 78080K->78080K(78080K), 0.0000182 secs]8.810: "
        + "[CMS10.776: [CMS-concurrent-mark: 4.232/10.160 secs] [Times: user=47.60 sys=0.81, "
        + "real=10.16 secs]\n (concurrent mode failure): 3443471K->190926K(3444224K), "
        + "3.3446142 secs] 3521551K->190926K(3522304K), [CMS Perm : 2671K->2670K(21248K)], "
        + "3.3459078 secs] [Times: user=5.27 sys=0.00, real=3.34 secs] ";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java7_cms_details_date_fullgc() {
    String s = "2015-01-22T11:58:48.374+0300: 8.668: [GC2015-01-22T11:58:48.374+0300: 8.668: "
        + "[ParNew: 78080K->78080K(78080K), 0.0000196 secs]2015-01-22T11:58:48.374+0300: 8.668: "
        + "[CMS2015-01-22T11:58:50.149+0300: 10.449: [CMS-concurrent-preclean: 3.540/8.127 secs] "
        + "[Times: user=33.45 sys=0.59, real=8.12 secs]\n (concurrent mode failure): "
        + "3443682K->190926K(3444416K), 3.0772196 secs] 3521762K->190926K(3522496K), [CMS Perm : "
        + "2671K->2670K(21248K)], 3.0785972 secs] [Times: user=3.06 sys=0.00, real=3.08 secs] ";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java8_cms_major() {
    assertTrue(extractor.accept("2.925: [GC (CMS Initial Mark)  417524K(610716K), 0.0455043 secs]"));
    assertTrue(extractor.accept("4.102: [GC (CMS Final Remark)  945284K(1060048K), 0.0914864 secs]"));
  }

  @Test
  public void testAccept_java8_cms_date_major() {
    assertTrue(extractor.accept("2015-01-23T20:40:54.741+0300: 22.262: [GC (CMS Initial Mark)  "
        + "2028530K(4097536K), 0.1091749 secs]"));
    assertTrue(extractor.accept("2015-01-23T20:40:55.955+0300: 23.476: [GC (CMS Final Remark)  "
        + "2212327K(4097536K), 0.0928672 secs]"));
  }

  @Test
  public void testAccept_java8_cms_details_mark_initial() {
    String s = "0.606: [GC (CMS Initial Mark) [1 CMS-initial-mark: 131390K(260864K)] "
        + "131557K(262016K), 0.0001334 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] ";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java8_cms_details_date_mark_initial() {
    String s = "2015-01-26T19:49:12.024+0300: 0.596: [GC (CMS Initial Mark) [1 CMS-initial-mark: "
        + "131597K(260864K)] 131745K(262016K), 0.0001236 secs] "
        + "[Times: user=0.00 sys=0.00, real=0.00 secs] ";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java8_cms_details_remark() {
    String s = "3.989: [GC (CMS Final Remark) [YG occupancy: 75138 K (189568 K)]3.989: [Rescan "
        + "(parallel) , 0.0830358 secs]4.072: [weak refs processing, 0.0000168 secs]4.072: [class "
        + "unloading, 0.0003112 secs]4.072: [scrub symbol table, 0.0003158 secs]4.073: [scrub "
        + "string table, 0.0001017 secs][1 CMS-remark: 870145K(870480K)] 945284K(1060048K), "
        + "0.0839077 secs] [Times: user=0.53 sys=0.00, real=0.08 secs] ";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java8_cms_details_date_remark() {
    String s = "2015-01-26T19:49:15.142+0300: 3.714: [GC (CMS Final Remark) [YG occupancy: 25259 K"
        + " (189568 K)]3.714: [Rescan (parallel) , 0.0640781 secs]3.778: [weak refs processing, "
        + "0.0000163 secs]3.778: [class unloading, 0.0003578 secs]3.778: [scrub symbol table, "
        + "0.0004376 secs]3.779: [scrub string table, 0.0001525 secs][1 CMS-remark: "
        + "723235K(723448K)] 748494K(913016K), 0.0652098 secs] [Times: user=0.50 sys=0.00, real=0.07 secs] ";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java8_cms_details_fullgc() {
    String s = "1.479: [GC (Allocation Failure) 1.479: [ParNew: 619K->127K(1152K), 0.0051739 secs]"
        + "1.484: [CMS1.873: [CMS-concurrent-mark: 0.510/1.266 secs] [Times: user=6.15 sys=0.19, "
        + "real=1.27 secs] \n (concurrent mode failure): 324150K->252686K(324608K), 1.0432362 secs]"
        + " 324045K->252686K(325760K), [Metaspace: 2625K->2625K(1056768K)], 1.0489746 secs] "
        + "[Times: user=1.30 sys=0.11, real=1.05 secs] ";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java8_cms_details_date_fullgc() {
    String s = "2015-01-26T19:49:12.922+0300: 1.495: [GC (Allocation Failure) 1.495: [ParNew: "
        + "580K->128K(1152K), 0.0065824 secs]1.501: [CMS2015-01-26T19:49:13.333+0300: 1.905: "
        + "[CMS-concurrent-mark: 0.518/1.309 secs] [Times: user=6.63 sys=0.09, real=1.31 secs] \n"
        + " (concurrent mode failure): 324342K->252686K(324800K), 1.0629986 secs] "
        + "324272K->252686K(325952K), [Metaspace: 2625K->2625K(1056768K)], 1.0700895 secs] "
        + "[Times: user=1.42 sys=0.03, real=1.07 secs] ";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testExtract_java7_cms_major() {
    String s = "0.489: [GC 142860K(251648K), 0.0272534 secs]";

    Optional<Map<String, String>> option =
        extractor.extract(s, new ExtractContext(Collections.emptyList()));
    assertTrue(option.isPresent());

    Map<String, String> values =  option.get();
    assertEquals(5, values.size());
    assertEquals("O", values.get(LogField.EVENT.toString()));
    assertEquals("0.489", values.get(LogField.EVENT_TIME.toString()));
    assertEquals("142860", values.get(LogField.MEM_BEFORE.toString()));
    assertEquals("251648", values.get(LogField.MEM_TOTAL.toString()));
    assertEquals("0.0272534", values.get(LogField.GC_TIME.toString()));
  }

  @Test
  public void testExtract_java7_cms_date_major() {
    String s = "2015-01-21T17:08:29.698+0300: 0.564: [GC 142860K(251648K), 0.0268580 secs]";

    Optional<Map<String, String>> option =
        extractor.extract(s, new ExtractContext(Collections.emptyList()));
    assertTrue(option.isPresent());

    Map<String, String> values =  option.get();
    assertEquals(6, values.size());
    assertEquals("O", values.get(LogField.EVENT.toString()));
    assertEquals("2015-01-21T17:08:29.698+0300", values.get(LogField.EVENT_DATE.toString()));
    assertEquals("0.564", values.get(LogField.EVENT_TIME.toString()));
    assertEquals("142860", values.get(LogField.MEM_BEFORE.toString()));
    assertEquals("251648", values.get(LogField.MEM_TOTAL.toString()));
    assertEquals("0.0268580", values.get(LogField.GC_TIME.toString()));
  }

  @Test
  public void testExtract_java7_cms_details_mark_initial() {
    String s = "45.854: [GC [1 CMS-initial-mark: 1885273K(3484096K)] 2007452K(4097536K), "
        + "0.1069750 secs] [Times: user=0.11 sys=0.00, real=0.11 secs] ";

    Optional<Map<String, String>> option =
        extractor.extract(s, new ExtractContext(Collections.emptyList()));
    assertTrue(option.isPresent());

    Map<String, String> values =  option.get();
    assertEquals(11, values.size());
    assertEquals("O", values.get(LogField.EVENT.toString()));
    assertEquals("initial-mark", values.get(LogField.CONCURRENT_PHASE.toString()));
    assertEquals("45.854", values.get(LogField.EVENT_TIME.toString()));
    assertEquals("1885273", values.get(LogField.MEM_OLD_BEFORE.toString()));
    assertEquals("3484096", values.get(LogField.MEM_OLD_TOTAL.toString()));
    assertEquals("2007452", values.get(LogField.MEM_BEFORE.toString()));
    assertEquals("4097536", values.get(LogField.MEM_TOTAL.toString()));
    assertEquals("0.1069750", values.get(LogField.GC_TIME.toString()));
    assertEquals("0.11", values.get(LogField.GC_TIME_USER.toString()));
    assertEquals("0.00", values.get(LogField.GC_TIME_SYS.toString()));
    assertEquals("0.11", values.get(LogField.GC_TIME_REAL.toString()));
  }

  @Test
  public void testExtract_java7_cms_details_date_mark_initial() {
    String s = "2015-01-22T11:58:40.204+0300: 0.498: [GC [1 CMS-initial-mark: 110183K(173568K)] "
        + "142860K(251648K), 0.0269829 secs] [Times: user=0.03 sys=0.00, real=0.02 secs] ";

    Optional<Map<String, String>> option =
        extractor.extract(s, new ExtractContext(Collections.emptyList()));
    assertTrue(option.isPresent());

    Map<String, String> values =  option.get();
    assertEquals(12, values.size());
    assertEquals("O", values.get(LogField.EVENT.toString()));
    assertEquals("initial-mark", values.get(LogField.CONCURRENT_PHASE.toString()));
    assertEquals("2015-01-22T11:58:40.204+0300", values.get(LogField.EVENT_DATE.toString()));
    assertEquals("0.498", values.get(LogField.EVENT_TIME.toString()));
    assertEquals("110183", values.get(LogField.MEM_OLD_BEFORE.toString()));
    assertEquals("173568", values.get(LogField.MEM_OLD_TOTAL.toString()));
    assertEquals("142860", values.get(LogField.MEM_BEFORE.toString()));
    assertEquals("251648", values.get(LogField.MEM_TOTAL.toString()));
    assertEquals("0.0269829", values.get(LogField.GC_TIME.toString()));
    assertEquals("0.03", values.get(LogField.GC_TIME_USER.toString()));
    assertEquals("0.00", values.get(LogField.GC_TIME_SYS.toString()));
    assertEquals("0.02", values.get(LogField.GC_TIME_REAL.toString()));
  }

  @Test
  public void testExtract_java7_cms_details_remark() {
    String s = "22.957: [GC[YG occupancy: 228273 K (613440 K)]22.957: [Rescan (parallel) , "
        + "0.1903250 secs]23.147: [weak refs processing, 0.0000135 secs]23.147: "
        + "[scrub string table, 0.0001064 secs] [1 CMS-remark: 1869858K(3484096K)] "
        + "2098132K(4097536K), 0.1905419 secs] [Times: user=0.89 sys=0.00, real=0.19 secs] ";

    Optional<Map<String, String>> option =
        extractor.extract(s, new ExtractContext(Collections.emptyList()));
    assertTrue(option.isPresent());

    Map<String, String> values =  option.get();
    assertEquals(13, values.size());
    assertEquals("O", values.get(LogField.EVENT.toString()));
    assertEquals("remark", values.get(LogField.CONCURRENT_PHASE.toString()));
    assertEquals("22.957", values.get(LogField.EVENT_TIME.toString()));
    assertEquals("228273", values.get(LogField.MEM_NEW_BEFORE.toString()));
    assertEquals("613440", values.get(LogField.MEM_NEW_TOTAL.toString()));
    assertEquals("1869858", values.get(LogField.MEM_OLD_BEFORE.toString()));
    assertEquals("3484096", values.get(LogField.MEM_OLD_TOTAL.toString()));
    assertEquals("2098132", values.get(LogField.MEM_BEFORE.toString()));
    assertEquals("4097536", values.get(LogField.MEM_TOTAL.toString()));
    assertEquals("0.1905419", values.get(LogField.GC_TIME.toString()));
    assertEquals("0.89", values.get(LogField.GC_TIME_USER.toString()));
    assertEquals("0.00", values.get(LogField.GC_TIME_SYS.toString()));
    assertEquals("0.19", values.get(LogField.GC_TIME_REAL.toString()));
  }

  @Test
  public void testExtract_java7_cms_details_date_remark() {
    String s = "2015-01-22T11:59:01.985+0300: 22.278: [GC[YG occupancy: 238637 K (613440 K)]"
        + "2015-01-22T11:59:01.985+0300: 22.278: [Rescan (parallel) , 0.1898640 secs]"
        + "2015-01-22T11:59:02.175+0300: 22.468: [weak refs processing, 0.0000182 secs]"
        + "2015-01-22T11:59:02.175+0300: 22.468: [scrub string table, 0.0001012 secs] "
        + "[1 CMS-remark: 1866997K(3484096K)] 2105634K(4097536K), 0.1901015 secs] "
        + "[Times: user=0.75 sys=0.00, real=0.19 secs]";

    Optional<Map<String, String>> option =
        extractor.extract(s, new ExtractContext(Collections.emptyList()));
    assertTrue(option.isPresent());

    Map<String, String> values =  option.get();
    assertEquals(14, values.size());
    assertEquals("O", values.get(LogField.EVENT.toString()));
    assertEquals("remark", values.get(LogField.CONCURRENT_PHASE.toString()));
    assertEquals("2015-01-22T11:59:01.985+0300", values.get(LogField.EVENT_DATE.toString()));
    assertEquals("22.278", values.get(LogField.EVENT_TIME.toString()));
    assertEquals("238637", values.get(LogField.MEM_NEW_BEFORE.toString()));
    assertEquals("613440", values.get(LogField.MEM_NEW_TOTAL.toString()));
    assertEquals("1866997", values.get(LogField.MEM_OLD_BEFORE.toString()));
    assertEquals("3484096", values.get(LogField.MEM_OLD_TOTAL.toString()));
    assertEquals("2105634", values.get(LogField.MEM_BEFORE.toString()));
    assertEquals("4097536", values.get(LogField.MEM_TOTAL.toString()));
    assertEquals("0.1901015", values.get(LogField.GC_TIME.toString()));
    assertEquals("0.75", values.get(LogField.GC_TIME_USER.toString()));
    assertEquals("0.00", values.get(LogField.GC_TIME_SYS.toString()));
    assertEquals("0.19", values.get(LogField.GC_TIME_REAL.toString()));
  }

  @Test
  public void testExtract_java7_cms_details_fullgc() {
    String s = "8.810: [GC8.810: [ParNew: 78080K->78080K(78080K), 0.0000182 secs]8.810: "
        + "[CMS10.776: [CMS-concurrent-mark: 4.232/10.160 secs] [Times: user=47.60 sys=0.81, "
        + "real=10.16 secs]\n (concurrent mode failure): 3443471K->190926K(3444224K), "
        + "3.3446142 secs] 3521551K->190926K(3522304K), [CMS Perm : 2671K->2670K(21248K)], "
        + "3.3459078 secs] [Times: user=5.27 sys=0.00, real=3.34 secs] ";

    Optional<Map<String, String>> option =
        extractor.extract(s, new ExtractContext(Extractors.secondaryExtractors()));
    assertTrue(option.isPresent());

    Map<String, String> values =  option.get();
    assertEquals(20, values.size());
    assertEquals("F", values.get(LogField.EVENT.toString()));
    assertEquals("8.810", values.get(LogField.EVENT_TIME.toString()));
    assertEquals("78080", values.get(LogField.MEM_NEW_BEFORE.toString()));
    assertEquals("78080", values.get(LogField.MEM_NEW_AFTER.toString()));
    assertEquals("78080", values.get(LogField.MEM_NEW_TOTAL.toString()));
    assertEquals("0.0000182", values.get(LogField.GC_NEW_TIME.toString()));
    assertEquals("3443471", values.get(LogField.MEM_OLD_BEFORE.toString()));
    assertEquals("190926", values.get(LogField.MEM_OLD_AFTER.toString()));
    assertEquals("3444224", values.get(LogField.MEM_OLD_TOTAL.toString()));
    assertEquals("3.3446142", values.get(LogField.GC_OLD_TIME.toString()));
    assertEquals("3521551", values.get(LogField.MEM_BEFORE.toString()));
    assertEquals("190926", values.get(LogField.MEM_AFTER.toString()));
    assertEquals("3522304", values.get(LogField.MEM_TOTAL.toString()));
    assertEquals("2671", values.get(LogField.MEM_PERM_BEFORE.toString()));
    assertEquals("2670", values.get(LogField.MEM_PERM_AFTER.toString()));
    assertEquals("21248", values.get(LogField.MEM_PERM_TOTAL.toString()));
    assertEquals("3.3459078", values.get(LogField.GC_TIME.toString()));
    assertEquals("5.27", values.get(LogField.GC_TIME_USER.toString()));
    assertEquals("0.00", values.get(LogField.GC_TIME_SYS.toString()));
    assertEquals("3.34", values.get(LogField.GC_TIME_REAL.toString()));
  }

  @Test
  public void testExtract_java7_cms_details_date_fullgc() {
    String s = "2015-01-22T11:58:48.374+0300: 8.668: [GC2015-01-22T11:58:48.374+0300: 8.668: "
        + "[ParNew: 78080K->78080K(78080K), 0.0000196 secs]2015-01-22T11:58:48.374+0300: 8.668: "
        + "[CMS2015-01-22T11:58:50.149+0300: 10.449: [CMS-concurrent-preclean: 3.540/8.127 secs] "
        + "[Times: user=33.45 sys=0.59, real=8.12 secs]\n (concurrent mode failure): "
        + "3443682K->190926K(3444416K), 3.0772196 secs] 3521762K->190926K(3522496K), [CMS Perm : "
        + "2671K->2670K(21248K)], 3.0785972 secs] [Times: user=3.06 sys=0.00, real=3.08 secs] ";

    Optional<Map<String, String>> option =
        extractor.extract(s, new ExtractContext(Extractors.secondaryExtractors()));
    assertTrue(option.isPresent());

    Map<String, String> values =  option.get();
    assertEquals(21, values.size());
    assertEquals("F", values.get(LogField.EVENT.toString()));
    assertEquals("2015-01-22T11:58:48.374+0300", values.get(LogField.EVENT_DATE.toString()));
    assertEquals("8.668", values.get(LogField.EVENT_TIME.toString()));
    assertEquals("78080", values.get(LogField.MEM_NEW_BEFORE.toString()));
    assertEquals("78080", values.get(LogField.MEM_NEW_AFTER.toString()));
    assertEquals("78080", values.get(LogField.MEM_NEW_TOTAL.toString()));
    assertEquals("0.0000196", values.get(LogField.GC_NEW_TIME.toString()));
    assertEquals("3443682", values.get(LogField.MEM_OLD_BEFORE.toString()));
    assertEquals("190926", values.get(LogField.MEM_OLD_AFTER.toString()));
    assertEquals("3444416", values.get(LogField.MEM_OLD_TOTAL.toString()));
    assertEquals("3.0772196", values.get(LogField.GC_OLD_TIME.toString()));
    assertEquals("3521762", values.get(LogField.MEM_BEFORE.toString()));
    assertEquals("190926", values.get(LogField.MEM_AFTER.toString()));
    assertEquals("3522496", values.get(LogField.MEM_TOTAL.toString()));
    assertEquals("2671", values.get(LogField.MEM_PERM_BEFORE.toString()));
    assertEquals("2670", values.get(LogField.MEM_PERM_AFTER.toString()));
    assertEquals("21248", values.get(LogField.MEM_PERM_TOTAL.toString()));
    assertEquals("3.0785972", values.get(LogField.GC_TIME.toString()));
    assertEquals("3.06", values.get(LogField.GC_TIME_USER.toString()));
    assertEquals("0.00", values.get(LogField.GC_TIME_SYS.toString()));
    assertEquals("3.08", values.get(LogField.GC_TIME_REAL.toString()));
  }

  @Test
  public void testExtract_java8_cms_details_mark_initial() {
    String s = "0.606: [GC (CMS Initial Mark) [1 CMS-initial-mark: 131390K(260864K)] "
        + "131557K(262016K), 0.0001334 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] ";

    Optional<Map<String, String>> option =
        extractor.extract(s, new ExtractContext(Collections.emptyList()));
    assertTrue(option.isPresent());

    Map<String, String> values =  option.get();
    assertEquals(11, values.size());
    assertEquals("O", values.get(LogField.EVENT.toString()));
    assertEquals("initial-mark", values.get(LogField.CONCURRENT_PHASE.toString()));
    assertEquals("0.606", values.get(LogField.EVENT_TIME.toString()));
    assertEquals("131390", values.get(LogField.MEM_OLD_BEFORE.toString()));
    assertEquals("260864", values.get(LogField.MEM_OLD_TOTAL.toString()));
    assertEquals("131557", values.get(LogField.MEM_BEFORE.toString()));
    assertEquals("262016", values.get(LogField.MEM_TOTAL.toString()));
    assertEquals("0.0001334", values.get(LogField.GC_TIME.toString()));
    assertEquals("0.00", values.get(LogField.GC_TIME_USER.toString()));
    assertEquals("0.00", values.get(LogField.GC_TIME_SYS.toString()));
    assertEquals("0.00", values.get(LogField.GC_TIME_REAL.toString()));
  }

  @Test
  public void testExtract_java8_cms_details_remark() {
    String s = "3.989: [GC (CMS Final Remark) [YG occupancy: 75138 K (189568 K)]3.989: [Rescan "
        + "(parallel) , 0.0830358 secs]4.072: [weak refs processing, 0.0000168 secs]4.072: [class "
        + "unloading, 0.0003112 secs]4.072: [scrub symbol table, 0.0003158 secs]4.073: [scrub "
        + "string table, 0.0001017 secs][1 CMS-remark: 870145K(870480K)] 945284K(1060048K), "
        + "0.0839077 secs] [Times: user=0.53 sys=0.00, real=0.08 secs] ";

    Optional<Map<String, String>> option =
        extractor.extract(s, new ExtractContext(Collections.emptyList()));
    assertTrue(option.isPresent());

    Map<String, String> values =  option.get();
    assertEquals(13, values.size());
    assertEquals("O", values.get(LogField.EVENT.toString()));
    assertEquals("remark", values.get(LogField.CONCURRENT_PHASE.toString()));
    assertEquals("3.989", values.get(LogField.EVENT_TIME.toString()));
    assertEquals("75138", values.get(LogField.MEM_NEW_BEFORE.toString()));
    assertEquals("189568", values.get(LogField.MEM_NEW_TOTAL.toString()));
    assertEquals("870145", values.get(LogField.MEM_OLD_BEFORE.toString()));
    assertEquals("870480", values.get(LogField.MEM_OLD_TOTAL.toString()));
    assertEquals("945284", values.get(LogField.MEM_BEFORE.toString()));
    assertEquals("1060048", values.get(LogField.MEM_TOTAL.toString()));
    assertEquals("0.0839077", values.get(LogField.GC_TIME.toString()));
    assertEquals("0.53", values.get(LogField.GC_TIME_USER.toString()));
    assertEquals("0.00", values.get(LogField.GC_TIME_SYS.toString()));
    assertEquals("0.08", values.get(LogField.GC_TIME_REAL.toString()));
  }

  @Test
  public void testExtract_java8_cms_details_fullgc() {
    String s = "1.479: [GC (Allocation Failure) 1.479: [ParNew: 619K->127K(1152K), 0.0051739 secs]"
        + "1.484: [CMS1.873: [CMS-concurrent-mark: 0.510/1.266 secs] [Times: user=6.15 sys=0.19, "
        + "real=1.27 secs] \n (concurrent mode failure): 324150K->252686K(324608K), 1.0432362 secs]"
        + " 324045K->252686K(325760K), [Metaspace: 2625K->2625K(1056768K)], 1.0489746 secs] "
        + "[Times: user=1.30 sys=0.11, real=1.05 secs] ";

    Optional<Map<String, String>> option =
        extractor.extract(s, new ExtractContext(Extractors.secondaryExtractors()));
    assertTrue(option.isPresent());

    Map<String, String> values =  option.get();
    assertEquals(20, values.size());
    assertEquals("F", values.get(LogField.EVENT.toString()));
    assertEquals("1.479", values.get(LogField.EVENT_TIME.toString()));
    assertEquals("619", values.get(LogField.MEM_NEW_BEFORE.toString()));
    assertEquals("127", values.get(LogField.MEM_NEW_AFTER.toString()));
    assertEquals("1152", values.get(LogField.MEM_NEW_TOTAL.toString()));
    assertEquals("0.0051739", values.get(LogField.GC_NEW_TIME.toString()));
    assertEquals("324150", values.get(LogField.MEM_OLD_BEFORE.toString()));
    assertEquals("252686", values.get(LogField.MEM_OLD_AFTER.toString()));
    assertEquals("324608", values.get(LogField.MEM_OLD_TOTAL.toString()));
    assertEquals("1.0432362", values.get(LogField.GC_OLD_TIME.toString()));
    assertEquals("324045", values.get(LogField.MEM_BEFORE.toString()));
    assertEquals("252686", values.get(LogField.MEM_AFTER.toString()));
    assertEquals("325760", values.get(LogField.MEM_TOTAL.toString()));
    assertEquals("2625", values.get(LogField.MEM_METASPACE_BEFORE.toString()));
    assertEquals("2625", values.get(LogField.MEM_METASPACE_AFTER.toString()));
    assertEquals("1056768", values.get(LogField.MEM_METASPACE_TOTAL.toString()));
    assertEquals("1.0489746", values.get(LogField.GC_TIME.toString()));
    assertEquals("1.30", values.get(LogField.GC_TIME_USER.toString()));
    assertEquals("0.11", values.get(LogField.GC_TIME_SYS.toString()));
    assertEquals("1.05", values.get(LogField.GC_TIME_REAL.toString()));
  }
}
