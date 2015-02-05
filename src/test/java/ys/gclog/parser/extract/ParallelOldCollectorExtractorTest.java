package ys.gclog.parser.extract;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

public class ParallelOldCollectorExtractorTest {

  private ParallelOldCollectorExtractor extractor;

  @Before
  public void setUp() {
    extractor = new ParallelOldCollectorExtractor();
  }

  @Test
  public void testAccept_java7_parallel_details_full() {
    String s = "0.512: [Full GC [PSYoungGen: 10736K->0K(141824K)] [ParOldGen: "
        + "187722K->171397K(409088K)] 198458K->171397K(550912K) [PSPermGen: 2665K->2664K(21504K)], "
        + "3.3574584 secs] [Times: user=9.81 sys=0.00, real=3.36 secs] ";
    assertTrue(extractor.accept(s));
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
    assertEquals("187722", values.get(LogField.MEM_OLD_BEFORE.toString()));
    assertEquals("171397", values.get(LogField.MEM_OLD_AFTER.toString()));
    assertEquals("409088", values.get(LogField.MEM_OLD_TOTAL.toString()));
  }
}
