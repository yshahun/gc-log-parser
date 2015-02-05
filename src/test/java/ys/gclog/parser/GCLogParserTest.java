package ys.gclog.parser;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import ys.gclog.LogAware;

public class GCLogParserTest {

  private GCLogParser parser;

  @Before
  public void setUp() {
    parser = new GCLogParser();
  }

  /*
   * Serial collector, Java 7.
   */

  @Test
  public void testParse_java7_serial() throws Exception {
    try (InputStream input = LogAware.getLog("gc-7-serial-01.log")) {
      assertEquals(43, parser.parse(input).size());
    }
  }

  @Test
  public void testParse_java7_serial_date() throws Exception {
    try (InputStream input = LogAware.getLog("gc-7-serial-02-date.log")) {
      assertEquals(63, parser.parse(input).size());
    }
  }

  @Test
  public void testParse_java7_serial_details() throws Exception {
    try (InputStream input = LogAware.getLog("gc-7-serial-03-details.log")) {
      assertEquals(46, parser.parse(input).size());
    }
  }

  @Test
  public void testParse_java7_serial_details_date() throws Exception {
    try (InputStream input = LogAware.getLog("gc-7-serial-04-details-date.log")) {
      assertEquals(47, parser.parse(input).size());
    }
  }

  /*
   * Serial collector, Java 8.
   */

  @Test
  public void testParse_java8_serial() throws Exception {
    try (InputStream input = LogAware.getLog("gc-8-serial-01.log")) {
      assertEquals(189, parser.parse(input).size());
    }
  }

  @Test
  public void testParse_java8_serial_date() throws Exception {
    try (InputStream input = LogAware.getLog("gc-8-serial-02-date.log")) {
      assertEquals(189, parser.parse(input).size());
    }
  }

  @Test
  public void testParse_java8_serial_details() throws Exception {
    try (InputStream input = LogAware.getLog("gc-8-serial-03-details.log")) {
      assertEquals(176, parser.parse(input).size());
    }
  }

  @Test
  public void testParse_java8_serial_details_date() throws Exception {
    try (InputStream input = LogAware.getLog("gc-8-serial-04-details-date.log")) {
      assertEquals(180, parser.parse(input).size());
    }
  }

  /*
   * Parallel collector, Java 7.
   */

  @Test
  public void testParse_java7_parallel() throws Exception {
    try (InputStream input = LogAware.getLog("gc-7-parallel-01.log")) {
      assertEquals(49, parser.parse(input).size());
    }
  }

  @Test
  public void testParse_java7_parallel_date() throws Exception {
    try (InputStream input = LogAware.getLog("gc-7-parallel-02-date.log")) {
      assertEquals(53, parser.parse(input).size());
    }
  }

  @Test
  public void testParse_java7_parallel_details() throws Exception {
    try (InputStream input = LogAware.getLog("gc-7-parallel-03-details.log")) {
      assertEquals(57, parser.parse(input).size());
    }
  }

  @Test
  public void testParse_java7_parallel_details_date() throws Exception {
    try (InputStream input = LogAware.getLog("gc-7-parallel-04-details-date.log")) {
      assertEquals(73, parser.parse(input).size());
    }
  }

  /*
   * Parallel collector, Java 8.
   */

  @Test
  public void testParse_java8_parallel() throws Exception {
    try (InputStream input = LogAware.getLog("gc-8-parallel-01.log")) {
      assertEquals(61, parser.parse(input).size());
    }
  }

  @Test
  public void testParse_java8_parallel_date() throws Exception {
    try (InputStream input = LogAware.getLog("gc-8-parallel-02-date.log")) {
      assertEquals(46, parser.parse(input).size());
    }
  }

  @Test
  public void testParse_java8_parallel_details() throws Exception {
    try (InputStream input = LogAware.getLog("gc-8-parallel-03-details.log")) {
      assertEquals(62, parser.parse(input).size());
    }
  }

  @Test
  public void testParse_java8_parallel_details_date() throws Exception {
    try (InputStream input = LogAware.getLog("gc-8-parallel-04-details-date.log")) {
      assertEquals(42, parser.parse(input).size());
    }
  }

  /*
   * CMS collector, Java 7.
   */

  @Test
  public void testParse_java7_cms() throws Exception {
    try (InputStream input = LogAware.getLog("gc-7-cms-01.log")) {
      assertEquals(91, parser.parse(input).size());
    }
  }

  @Test
  public void testParse_java7_cms_date() throws Exception {
    try (InputStream input = LogAware.getLog("gc-7-cms-02-date.log")) {
      assertEquals(125, parser.parse(input).size());
    }
  }

  @Test
  public void testParse_java7_cms_details() throws Exception {
    try (InputStream input = LogAware.getLog("gc-7-cms-03-details.log")) {
      assertEquals(300, parser.parse(input).size());
    }
  }

  @Test
  public void testParse_java7_cms_details_date() throws Exception {
    try (InputStream input = LogAware.getLog("gc-7-cms-04-details-date.log")) {
      assertEquals(119, parser.parse(input).size());
    }
  }

  /*
   * CMS collector, Java 8.
   */

  @Test
  public void testParse_java8_cms() throws Exception {
    try (InputStream input = LogAware.getLog("gc-8-cms-01.log")) {
      assertEquals(184, parser.parse(input).size());
    }
  }

  @Test
  public void testParse_java8_cms_date() throws Exception {
    try (InputStream input = LogAware.getLog("gc-8-cms-02-date.log")) {
      assertEquals(205, parser.parse(input).size());
    }
  }

  @Test
  public void testParse_java8_cms_details() throws Exception {
    try (InputStream input = LogAware.getLog("gc-8-cms-03-details.log")) {
      assertEquals(220, parser.parse(input).size());
    }
  }

  @Test
  public void testParse_java8_cms_details_date() throws Exception {
    try (InputStream input = LogAware.getLog("gc-8-cms-04-details-date.log")) {
      assertEquals(182, parser.parse(input).size());
    }
  }

  /*
   * G1 collector, Java 7.
   */

  @Test
  public void testParse_java7_g1() throws Exception {
    try (InputStream input = LogAware.getLog("gc-7-g1-01.log")) {
      assertEquals(77, parser.parse(input).size());
    }
  }

  @Test
  public void testParse_java7_g1_date() throws Exception {
    try (InputStream input = LogAware.getLog("gc-7-g1-02-date.log")) {
      assertEquals(113, parser.parse(input).size());
    }
  }

  @Test
  public void testParse_java7_g1_details() throws Exception {
    try (InputStream input = LogAware.getLog("gc-7-g1-03-details.log")) {
      assertEquals(158, parser.parse(input).size());
    }
  }

  @Test
  public void testParse_java7_g1_details_date() throws Exception {
    try (InputStream input = LogAware.getLog("gc-7-g1-04-details-date.log")) {
      assertEquals(122, parser.parse(input).size());
    }
  }

  /*
   * G1 collector, Java 8.
   */

  @Test
  public void testParse_java8_g1() throws Exception {
    try (InputStream input = LogAware.getLog("gc-8-g1-01.log")) {
      assertEquals(109, parser.parse(input).size());
    }
  }

  @Test
  public void testParse_java8_g1_date() throws Exception {
    try (InputStream input = LogAware.getLog("gc-8-g1-02-date.log")) {
      assertEquals(82, parser.parse(input).size());
    }
  }

  @Test
  public void testParse_java8_g1_details() throws Exception {
    try (InputStream input = LogAware.getLog("gc-8-g1-03-details.log")) {
      assertEquals(76, parser.parse(input).size());
    }
  }

  @Test
  public void testParse_java8_g1_details_date() throws Exception {
    try (InputStream input = LogAware.getLog("gc-8-g1-04-details-date.log")) {
      assertEquals(84, parser.parse(input).size());
    }
  }
}
