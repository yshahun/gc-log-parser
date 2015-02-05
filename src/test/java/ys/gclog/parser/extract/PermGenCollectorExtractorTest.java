package ys.gclog.parser.extract;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

public class PermGenCollectorExtractorTest {

  private PermGenCollectorExtractor extractor;

  @Before
  public void setUp() {
    extractor = new PermGenCollectorExtractor();
  }

  @Test
  public void testAccept_java7_serial_details_full() {
    String s = "0.552: [GC0.552: [DefNew: 78080K->8640K(78080K), 0.1315698 secs]0.684: "
        + "[Tenured: 207957K->165851K(209624K), 0.4194380 secs] 216597K->165851K(287704K), "
        + "[Perm : 2665K->2665K(21248K)], 0.5513922 secs] [Times: user=0.56 sys=0.00, real=0.55 secs] ";
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
  public void testAccept_java7_cms_details_full() {
    String s = "8.810: [GC8.810: [ParNew: 78080K->78080K(78080K), 0.0000182 secs]8.810: "
        + "[CMS10.776: [CMS-concurrent-mark: 4.232/10.160 secs] [Times: user=47.60 sys=0.81, "
        + "real=10.16 secs]\n (concurrent mode failure): 3443471K->190926K(3444224K), "
        + "3.3446142 secs] 3521551K->190926K(3522304K), [CMS Perm : 2671K->2670K(21248K)], "
        + "3.3459078 secs] [Times: user=5.27 sys=0.00, real=3.34 secs] ";
    assertTrue(extractor.accept(s));
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
    assertEquals(3, values.size());
    assertEquals("2665", values.get(LogField.MEM_PERM_BEFORE.toString()));
    assertEquals("2665", values.get(LogField.MEM_PERM_AFTER.toString()));
    assertEquals("21248", values.get(LogField.MEM_PERM_TOTAL.toString()));
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
    assertEquals(3, values.size());
    assertEquals("2665", values.get(LogField.MEM_PERM_BEFORE.toString()));
    assertEquals("2664", values.get(LogField.MEM_PERM_AFTER.toString()));
    assertEquals("21504", values.get(LogField.MEM_PERM_TOTAL.toString()));
  }

  @Test
  public void testExtract_java7_cms_details_full() {
    String s = "8.810: [GC8.810: [ParNew: 78080K->78080K(78080K), 0.0000182 secs]8.810: "
        + "[CMS10.776: [CMS-concurrent-mark: 4.232/10.160 secs] [Times: user=47.60 sys=0.81, "
        + "real=10.16 secs]\n (concurrent mode failure): 3443471K->190926K(3444224K), "
        + "3.3446142 secs] 3521551K->190926K(3522304K), [CMS Perm : 2671K->2670K(21248K)], "
        + "3.3459078 secs] [Times: user=5.27 sys=0.00, real=3.34 secs] ";

    Optional<Map<String, String>> option =
        extractor.extract(s, new ExtractContext(Collections.emptyList()));
    assertTrue(option.isPresent());

    Map<String, String> values =  option.get();
    assertEquals(3, values.size());
    assertEquals("2671", values.get(LogField.MEM_PERM_BEFORE.toString()));
    assertEquals("2670", values.get(LogField.MEM_PERM_AFTER.toString()));
    assertEquals("21248", values.get(LogField.MEM_PERM_TOTAL.toString()));
  }
}
