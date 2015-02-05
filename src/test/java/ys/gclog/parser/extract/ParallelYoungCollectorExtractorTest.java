package ys.gclog.parser.extract;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

public class ParallelYoungCollectorExtractorTest {

  private ParallelYoungCollectorExtractor extractor;

  @Before
  public void setUp() {
    extractor = new ParallelYoungCollectorExtractor();
  }

  @Test
  public void testAccept_java7_parallel_details_minor() {
    String s = "0.123: [GC [PSYoungGen: 65536K->10752K(76288K)] 65536K->43219K(249856K), "
        + "0.0562888 secs] [Times: user=0.36 sys=0.00, real=0.06 secs] ";
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
  public void testExtract_java7_parallel_details_minor() {
    String s = "0.123: [GC [PSYoungGen: 65536K->10752K(76288K)] 65536K->43219K(249856K), "
        + "0.0562888 secs] [Times: user=0.36 sys=0.00, real=0.06 secs] ";

    Optional<Map<String, String>> option =
        extractor.extract(s, new ExtractContext(Collections.emptyList()));
    assertTrue(option.isPresent());

    Map<String, String> values =  option.get();
    assertEquals(3, values.size());
    assertEquals("65536", values.get(LogField.MEM_NEW_BEFORE.toString()));
    assertEquals("10752", values.get(LogField.MEM_NEW_AFTER.toString()));
    assertEquals("76288", values.get(LogField.MEM_NEW_TOTAL.toString()));
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
    assertEquals("10736", values.get(LogField.MEM_NEW_BEFORE.toString()));
    assertEquals("0", values.get(LogField.MEM_NEW_AFTER.toString()));
    assertEquals("141824", values.get(LogField.MEM_NEW_TOTAL.toString()));
  }
}
