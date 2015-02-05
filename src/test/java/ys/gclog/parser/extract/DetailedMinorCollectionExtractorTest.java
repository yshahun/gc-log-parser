package ys.gclog.parser.extract;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

public class DetailedMinorCollectionExtractorTest {

  private DetailedMinorCollectionExtractor extractor;

  @Before
  public void setUp() {
    extractor = new DetailedMinorCollectionExtractor();
  }

  @Test
  public void testAccept_java7_serial_details_minor() {
    String s = "0.133: [GC0.133: [DefNew: 69440K->8640K(78080K), 0.0909376 secs] "
        + "69440K->46942K(251648K), 0.0910944 secs] [Times: user=0.08 sys=0.02, real=0.09 secs] ";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java7_serial_details_date_minor() {
    String s = "2015-01-11T15:21:42.669+0300: 0.135: [GC2015-01-11T15:21:42.669+0300: 0.135: "
        + "[DefNew: 69440K->8640K(78080K), 0.0867695 secs] 69440K->46942K(251648K), 0.0869281 secs] "
        + "[Times: user=0.08 sys=0.00, real=0.09 secs] ";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java7_parallel_details_minor() {
    String s = "0.123: [GC [PSYoungGen: 65536K->10752K(76288K)] 65536K->43219K(249856K), "
        + "0.0562888 secs] [Times: user=0.36 sys=0.00, real=0.06 secs] ";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java7_parallel_details_date_minor() {
    String s = "2015-01-11T15:24:15.454+0300: 0.122: [GC [PSYoungGen: 65536K->10752K(76288K)] "
        + "65536K->43211K(249856K), 0.0519373 secs] [Times: user=0.27 sys=0.00, real=0.05 secs] ";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java8_serial_details_minor() {
    String s = "0.068: [GC (Allocation Failure) 0.068: [DefNew: 1024K->128K(1152K), 0.0017759 secs]"
        + " 1024K->677K(262016K), 0.0018963 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] ";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java8_serial_details_date_minor() {
    String s = "2015-01-23T18:45:05.099+0300: 0.071: [GC (Allocation Failure) 0.071: [DefNew: "
        + "1024K->128K(1152K), 0.0017941 secs] 1024K->689K(262016K), 0.0019098 secs] "
        + "[Times: user=0.00 sys=0.00, real=0.00 secs] ";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java8_parallel_details_minor() {
    String s = "0.068: [GC (Allocation Failure) [PSYoungGen: 504K->496K(1024K)] "
        + "504K->512K(261632K), 0.0008290 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] ";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testAccept_java8_parallel_details_date_minor() {
    String s = "2015-01-23T20:09:01.899+0300: 0.067: [GC (Allocation Failure) [PSYoungGen: "
        + "504K->448K(1024K)] 504K->456K(261632K), 0.0007548 secs] "
        + "[Times: user=0.00 sys=0.00, real=0.00 secs] ";
    assertTrue(extractor.accept(s));
  }

  @Test
  public void testExtract_java7_serial_details_minor() {
    String s = "0.133: [GC0.133: [DefNew: 69440K->8640K(78080K), 0.0909376 secs] "
        + "69440K->46942K(251648K), 0.0910944 secs] [Times: user=0.08 sys=0.02, real=0.09 secs] ";

    Optional<Map<String, String>> option =
        extractor.extract(s, new ExtractContext(Collections.emptyList()));
    assertTrue(option.isPresent());

    Map<String, String> values =  option.get();
    assertEquals(9, values.size());
    assertEquals("N", values.get(LogField.EVENT.toString()));
    assertEquals("0.133", values.get(LogField.EVENT_TIME.toString()));
    assertEquals("69440", values.get(LogField.MEM_BEFORE.toString()));
    assertEquals("46942", values.get(LogField.MEM_AFTER.toString()));
    assertEquals("251648", values.get(LogField.MEM_TOTAL.toString()));
    assertEquals("0.0910944", values.get(LogField.GC_TIME.toString()));
    assertEquals("0.08", values.get(LogField.GC_TIME_USER.toString()));
    assertEquals("0.02", values.get(LogField.GC_TIME_SYS.toString()));
    assertEquals("0.09", values.get(LogField.GC_TIME_REAL.toString()));
  }

  @Test
  public void testExtract_java7_serial_details_date_minor() {
    String s = "2015-01-11T15:21:42.669+0300: 0.135: [GC2015-01-11T15:21:42.669+0300: 0.135: "
        + "[DefNew: 69440K->8640K(78080K), 0.0867695 secs] 69440K->46942K(251648K), 0.0869281 secs] "
        + "[Times: user=0.08 sys=0.00, real=0.09 secs] ";

    Optional<Map<String, String>> option =
        extractor.extract(s, new ExtractContext(Collections.emptyList()));
    assertTrue(option.isPresent());

    Map<String, String> values =  option.get();
    assertEquals(10, values.size());
    assertEquals("N", values.get(LogField.EVENT.toString()));
    assertEquals("2015-01-11T15:21:42.669+0300", values.get(LogField.EVENT_DATE.toString()));
    assertEquals("0.135", values.get(LogField.EVENT_TIME.toString()));
    assertEquals("69440", values.get(LogField.MEM_BEFORE.toString()));
    assertEquals("46942", values.get(LogField.MEM_AFTER.toString()));
    assertEquals("251648", values.get(LogField.MEM_TOTAL.toString()));
    assertEquals("0.0869281", values.get(LogField.GC_TIME.toString()));
    assertEquals("0.08", values.get(LogField.GC_TIME_USER.toString()));
    assertEquals("0.00", values.get(LogField.GC_TIME_SYS.toString()));
    assertEquals("0.09", values.get(LogField.GC_TIME_REAL.toString()));
  }

  @Test
  public void testExtract_java7_parallel_details_minor() {
    String s = "0.123: [GC [PSYoungGen: 65536K->10752K(76288K)] 65536K->43219K(249856K), "
        + "0.0562888 secs] [Times: user=0.36 sys=0.00, real=0.06 secs] ";

    Optional<Map<String, String>> option =
        extractor.extract(s, new ExtractContext(Collections.emptyList()));
    assertTrue(option.isPresent());

    Map<String, String> values =  option.get();
    assertEquals(9, values.size());
    assertEquals("N", values.get(LogField.EVENT.toString()));
    assertEquals("0.123", values.get(LogField.EVENT_TIME.toString()));
    assertEquals("65536", values.get(LogField.MEM_BEFORE.toString()));
    assertEquals("43219", values.get(LogField.MEM_AFTER.toString()));
    assertEquals("249856", values.get(LogField.MEM_TOTAL.toString()));
    assertEquals("0.0562888", values.get(LogField.GC_TIME.toString()));
    assertEquals("0.36", values.get(LogField.GC_TIME_USER.toString()));
    assertEquals("0.00", values.get(LogField.GC_TIME_SYS.toString()));
    assertEquals("0.06", values.get(LogField.GC_TIME_REAL.toString()));
  }

  @Test
  public void testExtract_java7_parallel_details_date_minor() {
    String s = "2015-01-11T15:24:15.454+0300: 0.122: [GC [PSYoungGen: 65536K->10752K(76288K)] "
        + "65536K->43211K(249856K), 0.0519373 secs] [Times: user=0.27 sys=0.00, real=0.05 secs] ";

    Optional<Map<String, String>> option =
        extractor.extract(s, new ExtractContext(Collections.emptyList()));
    assertTrue(option.isPresent());

    Map<String, String> values =  option.get();
    assertEquals(10, values.size());
    assertEquals("N", values.get(LogField.EVENT.toString()));
    assertEquals("2015-01-11T15:24:15.454+0300", values.get(LogField.EVENT_DATE.toString()));
    assertEquals("0.122", values.get(LogField.EVENT_TIME.toString()));
    assertEquals("65536", values.get(LogField.MEM_BEFORE.toString()));
    assertEquals("43211", values.get(LogField.MEM_AFTER.toString()));
    assertEquals("249856", values.get(LogField.MEM_TOTAL.toString()));
    assertEquals("0.0519373", values.get(LogField.GC_TIME.toString()));
    assertEquals("0.27", values.get(LogField.GC_TIME_USER.toString()));
    assertEquals("0.00", values.get(LogField.GC_TIME_SYS.toString()));
    assertEquals("0.05", values.get(LogField.GC_TIME_REAL.toString()));
  }

  @Test
  public void testExtract_java8_serial_details_minor() {
    String s = "0.068: [GC (Allocation Failure) 0.068: [DefNew: 1024K->128K(1152K), 0.0017759 secs]"
        + " 1024K->677K(262016K), 0.0018963 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] ";

    Optional<Map<String, String>> option =
        extractor.extract(s, new ExtractContext(Collections.emptyList()));
    assertTrue(option.isPresent());

    Map<String, String> values =  option.get();
    assertEquals(9, values.size());
    assertEquals("N", values.get(LogField.EVENT.toString()));
    assertEquals("0.068", values.get(LogField.EVENT_TIME.toString()));
    assertEquals("1024", values.get(LogField.MEM_BEFORE.toString()));
    assertEquals("677", values.get(LogField.MEM_AFTER.toString()));
    assertEquals("262016", values.get(LogField.MEM_TOTAL.toString()));
    assertEquals("0.0018963", values.get(LogField.GC_TIME.toString()));
    assertEquals("0.00", values.get(LogField.GC_TIME_USER.toString()));
    assertEquals("0.00", values.get(LogField.GC_TIME_SYS.toString()));
    assertEquals("0.00", values.get(LogField.GC_TIME_REAL.toString()));
  }

  @Test
  public void testExtract_java8_parallel_details_minor() {
    String s = "0.068: [GC (Allocation Failure) [PSYoungGen: 504K->496K(1024K)] "
        + "504K->512K(261632K), 0.0008290 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] ";

    Optional<Map<String, String>> option =
        extractor.extract(s, new ExtractContext(Collections.emptyList()));
    assertTrue(option.isPresent());

    Map<String, String> values =  option.get();
    assertEquals(9, values.size());
    assertEquals("N", values.get(LogField.EVENT.toString()));
    assertEquals("0.068", values.get(LogField.EVENT_TIME.toString()));
    assertEquals("504", values.get(LogField.MEM_BEFORE.toString()));
    assertEquals("512", values.get(LogField.MEM_AFTER.toString()));
    assertEquals("261632", values.get(LogField.MEM_TOTAL.toString()));
    assertEquals("0.0008290", values.get(LogField.GC_TIME.toString()));
    assertEquals("0.00", values.get(LogField.GC_TIME_USER.toString()));
    assertEquals("0.00", values.get(LogField.GC_TIME_SYS.toString()));
    assertEquals("0.00", values.get(LogField.GC_TIME_REAL.toString()));
  }
}
