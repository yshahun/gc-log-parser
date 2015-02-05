package ys.gclog.parser.extract;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

public class DetailedFullCollectionExtractorTest {

  private DetailedFullCollectionExtractor extractor;

  @Before
  public void setUp() {
    extractor = new DetailedFullCollectionExtractor();
  }

  @Test
  public void testAccept_java7_serial_details_full() {
    String s = "0.552: [GC0.552: [DefNew: 78080K->8640K(78080K), 0.1315698 secs]0.684: "
        + "[Tenured: 207957K->165851K(209624K), 0.4194380 secs] 216597K->165851K(287704K), "
        + "[Perm : 2665K->2665K(21248K)], 0.5513922 secs] [Times: user=0.56 sys=0.00, real=0.55 secs] ";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java7_serial_details_date_full() {
    String s = "2015-01-11T15:21:43.075+0300: 0.542: [GC2015-01-11T15:21:43.075+0300: 0.542: "
        + "[DefNew: 78080K->8640K(78080K), 0.1287106 secs]2015-01-11T15:21:43.204+0300: 0.671: "
        + "[Tenured: 207957K->165851K(209624K), 0.4196717 secs] 216597K->165851K(287704K), "
        + "[Perm : 2665K->2665K(21248K)], 0.5487271 secs] [Times: user=0.55 sys=0.00, real=0.55 secs] ";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java7_parallel_details_full() {
    String s = "0.512: [Full GC [PSYoungGen: 10736K->0K(141824K)] [ParOldGen: "
        + "187722K->171397K(409088K)] 198458K->171397K(550912K) [PSPermGen: 2665K->2664K(21504K)], "
        + "3.3574584 secs] [Times: user=9.81 sys=0.00, real=3.36 secs] ";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java7_parallel_details_date_full() {
    String s = "2015-01-11T15:24:15.837+0300: 0.505: [Full GC [PSYoungGen: 10736K->0K(141824K)] "
        + "[ParOldGen: 187706K->171397K(409088K)] 198442K->171397K(550912K) [PSPermGen: "
        + "2665K->2664K(21504K)], 3.3356635 secs] [Times: user=9.78 sys=0.02, real=3.34 secs] ";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java8_serial_details_full() {
    String s = "0.742: [GC (Allocation Failure) 0.742: [DefNew: 579K->128K(1152K), 0.0015646 secs]"
        + "0.744: [Tenured: 252267K->189781K(260864K), 0.4807888 secs] 252413K->189781K(262016K), "
        + "[Metaspace: 2637K->2637K(1056768K)], 0.4827313 secs] [Times: user=0.48 sys=0.00, real=0.48 secs] ";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java8_serial_details_date_full() {
    String s = "2015-01-23T18:45:05.774+0300: 0.747: [GC (Allocation Failure) 0.747: [DefNew: "
        + "622K->128K(1152K), 0.0017550 secs]0.748: [Tenured: 252267K->189781K(260864K), "
        + "0.5053237 secs] 252405K->189781K(262016K), [Metaspace: 2637K->2637K(1056768K)], "
        + "0.5073954 secs] [Times: user=0.51 sys=0.00, real=0.51 secs] ";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java8_parallel_details_full() {
    String s = "1.343: [Full GC (Ergonomics) [PSYoungGen: 49824K->0K(58368K)] [ParOldGen: "
        + "255454K->202113K(229888K)] 305278K->202113K(288256K), [Metaspace: "
        + "2637K->2637K(1056768K)], 3.4285328 secs] [Times: user=8.95 sys=0.00, real=3.43 secs] ";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java8_parallel_details_date_full() {
    String s = "2015-01-23T20:09:03.127+0300: 1.294: [Full GC (Ergonomics) [PSYoungGen: "
        + "49792K->0K(58368K)] [ParOldGen: 255458K->202079K(229888K)] 305250K->202079K(288256K), "
        + "[Metaspace: 2637K->2637K(1056768K)], 3.4139222 secs] [Times: user=9.25 sys=0.02, real=3.41 secs] ";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testExtract_java7_serial_details_full() {
    String s = "0.552: [GC0.552: [DefNew: 78080K->8640K(78080K), 0.1315698 secs]0.684: "
        + "[Tenured: 207957K->165851K(209624K), 0.4194380 secs] 216597K->165851K(287704K), "
        + "[Perm : 2665K->2665K(21248K)], 0.5513922 secs] [Times: user=0.56 sys=0.00, real=0.55 secs] ";

    Optional<Map<String, String>> option =
        extractor.extract(s, new ExtractContext(Collections.emptyList()));
    assertTrue(option.isPresent());

    Map<String, String> values =  option.get();
    assertEquals(9, values.size());
    assertEquals("F", values.get(LogField.EVENT.toString()));
    assertEquals("0.552", values.get(LogField.EVENT_TIME.toString()));
    assertEquals("216597", values.get(LogField.MEM_BEFORE.toString()));
    assertEquals("165851", values.get(LogField.MEM_AFTER.toString()));
    assertEquals("287704", values.get(LogField.MEM_TOTAL.toString()));
    assertEquals("0.5513922", values.get(LogField.GC_TIME.toString()));
    assertEquals("0.56", values.get(LogField.GC_TIME_USER.toString()));
    assertEquals("0.00", values.get(LogField.GC_TIME_SYS.toString()));
    assertEquals("0.55", values.get(LogField.GC_TIME_REAL.toString()));
  }

  @Test
  public void testExtract_java7_serial_details_date_full() {
    String s = "2015-01-11T15:21:43.075+0300: 0.542: [GC2015-01-11T15:21:43.075+0300: 0.542: "
        + "[DefNew: 78080K->8640K(78080K), 0.1287106 secs]2015-01-11T15:21:43.204+0300: 0.671: "
        + "[Tenured: 207957K->165851K(209624K), 0.4196717 secs] 216597K->165851K(287704K), "
        + "[Perm : 2665K->2665K(21248K)], 0.5487271 secs] [Times: user=0.55 sys=0.00, real=0.55 secs] ";

    Optional<Map<String, String>> option =
        extractor.extract(s, new ExtractContext(Collections.emptyList()));
    assertTrue(option.isPresent());

    Map<String, String> values =  option.get();
    assertEquals(10, values.size());
    assertEquals("F", values.get(LogField.EVENT.toString()));
    assertEquals("2015-01-11T15:21:43.075+0300", values.get(LogField.EVENT_DATE.toString()));
    assertEquals("0.542", values.get(LogField.EVENT_TIME.toString()));
    assertEquals("216597", values.get(LogField.MEM_BEFORE.toString()));
    assertEquals("165851", values.get(LogField.MEM_AFTER.toString()));
    assertEquals("287704", values.get(LogField.MEM_TOTAL.toString()));
    assertEquals("0.5487271", values.get(LogField.GC_TIME.toString()));
    assertEquals("0.55", values.get(LogField.GC_TIME_USER.toString()));
    assertEquals("0.00", values.get(LogField.GC_TIME_SYS.toString()));
    assertEquals("0.55", values.get(LogField.GC_TIME_REAL.toString()));
  }

  @Test
  public void testExtract_java7_parallel_details_full() {
    String s = "0.512: [Full GC [PSYoungGen: 10736K->0K(141824K)] [ParOldGen: "
        + "187722K->171397K(409088K)] 198458K->171397K(550912K) [PSPermGen: 2665K->2664K(21504K)], "
        + "3.3574584 secs] [Times: user=9.81 sys=0.00, real=3.36 secs] ";

    Optional<Map<String, String>> option =
        extractor.extract(s, new ExtractContext(Collections.emptyList()));
    assertTrue(option.isPresent());

    Map<String, String> values =  option.get();
    assertEquals(9, values.size());
    assertEquals("F", values.get(LogField.EVENT.toString()));
    assertEquals("0.512", values.get(LogField.EVENT_TIME.toString()));
    assertEquals("198458", values.get(LogField.MEM_BEFORE.toString()));
    assertEquals("171397", values.get(LogField.MEM_AFTER.toString()));
    assertEquals("550912", values.get(LogField.MEM_TOTAL.toString()));
    assertEquals("3.3574584", values.get(LogField.GC_TIME.toString()));
    assertEquals("9.81", values.get(LogField.GC_TIME_USER.toString()));
    assertEquals("0.00", values.get(LogField.GC_TIME_SYS.toString()));
    assertEquals("3.36", values.get(LogField.GC_TIME_REAL.toString()));
  }

  @Test
  public void testExtract_java7_parallel_details_date_full() {
    String s = "2015-01-11T15:24:15.837+0300: 0.505: [Full GC [PSYoungGen: 10736K->0K(141824K)] "
        + "[ParOldGen: 187706K->171397K(409088K)] 198442K->171397K(550912K) [PSPermGen: "
        + "2665K->2664K(21504K)], 3.3356635 secs] [Times: user=9.78 sys=0.02, real=3.34 secs] ";

    Optional<Map<String, String>> option =
        extractor.extract(s, new ExtractContext(Collections.emptyList()));
    assertTrue(option.isPresent());

    Map<String, String> values =  option.get();
    assertEquals(10, values.size());
    assertEquals("F", values.get(LogField.EVENT.toString()));
    assertEquals("2015-01-11T15:24:15.837+0300", values.get(LogField.EVENT_DATE.toString()));
    assertEquals("0.505", values.get(LogField.EVENT_TIME.toString()));
    assertEquals("198442", values.get(LogField.MEM_BEFORE.toString()));
    assertEquals("171397", values.get(LogField.MEM_AFTER.toString()));
    assertEquals("550912", values.get(LogField.MEM_TOTAL.toString()));
    assertEquals("3.3356635", values.get(LogField.GC_TIME.toString()));
    assertEquals("9.78", values.get(LogField.GC_TIME_USER.toString()));
    assertEquals("0.02", values.get(LogField.GC_TIME_SYS.toString()));
    assertEquals("3.34", values.get(LogField.GC_TIME_REAL.toString()));
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
    assertEquals(9, values.size());
    assertEquals("F", values.get(LogField.EVENT.toString()));
    assertEquals("0.742", values.get(LogField.EVENT_TIME.toString()));
    assertEquals("252413", values.get(LogField.MEM_BEFORE.toString()));
    assertEquals("189781", values.get(LogField.MEM_AFTER.toString()));
    assertEquals("262016", values.get(LogField.MEM_TOTAL.toString()));
    assertEquals("0.4827313", values.get(LogField.GC_TIME.toString()));
    assertEquals("0.48", values.get(LogField.GC_TIME_USER.toString()));
    assertEquals("0.00", values.get(LogField.GC_TIME_SYS.toString()));
    assertEquals("0.48", values.get(LogField.GC_TIME_REAL.toString()));
  }
}
