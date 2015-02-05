/*
 * Copyright 2015 Yauheni Shahun
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ys.gclog.parser.extract;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CmsCollectorExtractor implements LogExtractor {

  private static final Logger logger = Logger.getLogger(CmsCollectorExtractor.class.getName());

  private static final String REGEX_CONCURRENT_TOKEN =
      "(?:CMS-concurrent-mark-start|CMS-concurrent-mark|CMS-concurrent-preclean-start|"
      + "CMS-concurrent-preclean|CMS-concurrent-abortable-preclean-start|"
      + "CMS-concurrent-abortable-preclean|CMS-concurrent-sweep-start|CMS-concurrent-sweep|"
      + "CMS-concurrent-reset-start|CMS-concurrent-reset)";

  private static final Pattern PATTERN_STOP_EVENT_SHORT = Pattern.compile(
      String.format("(?:%s: )?%s: \\[GC(?: \\(.+?\\) )? (\\d+)K\\((\\d+)K\\), %s secs]\\s*",
          REGEX_DATE, REGEX_TIME, REGEX_DECIMAL));

  private static final Pattern PATTERN_MARK_INITIAL = Pattern.compile(
      String.format("(?:%s: )?%s: \\[GC(?: \\(.+?\\))? \\[.+CMS-initial-mark: %s] %s, %s secs] %s\\s*",
          REGEX_DATE, REGEX_TIME, REGEX_MEMORY_AT, REGEX_MEMORY_AT, REGEX_TIME, REGEX_TIMES));

  private static final Pattern PATTERN_REMARK = Pattern.compile(
      String.format("(?:%s: )?%s: \\[GC(?: \\(.+?\\) )?\\[YG occupancy: (\\d+) K \\((\\d+) K\\)]"
          + "(?:(?:%s: )?%s: \\[.+?])+\\s?\\[1 CMS-remark: %s] %s, %s secs] %s\\s*",
          REGEX_DATE, REGEX_TIME, REGEX_DATE, REGEX_TIME, REGEX_MEMORY_AT, REGEX_MEMORY_AT,
          REGEX_TIME, REGEX_TIMES));

  private static final Pattern PATTERN_CONCURRENT_PHASE = Pattern.compile(
      String.format("(?:%s: )?%s: \\[%s.*]\\s*", REGEX_DATE, REGEX_TIME, REGEX_CONCURRENT_TOKEN));

  private static final Pattern PATTERN_FULL_GC = Pattern.compile(
      String.format("(?:%s: )?%s: \\[GC.+\\[CMS.+\\s+\\(concurrent mode failure\\): %s, %s secs] %s, \\[.+?], %s secs] %s\\s*",
          REGEX_DATE, REGEX_TIME, REGEX_MEMORY_FROM_TO_KB, REGEX_TIME, REGEX_MEMORY_FROM_TO_KB,
          REGEX_TIME, REGEX_TIMES));

  private static final Pattern PATTERN_ALL = Pattern.compile(String.format("%s|%s|%s|%s|%s",
      PATTERN_STOP_EVENT_SHORT, PATTERN_MARK_INITIAL, PATTERN_REMARK, PATTERN_CONCURRENT_PHASE,
      PATTERN_FULL_GC));

  @Override
  public boolean accept(String log) {
    return PATTERN_ALL.matcher(log).matches();
  }

  @Override
  public Optional<Map<String, String>> extract(String log, ExtractContext context) {
    Map<String, String> values = new LinkedHashMap<>();

    if (!extractShortStopEvent(log, values))
      if (!extractInitialMark(log, values))
        if (!extractRemark(log, values))
          if (!extractFullCollection(log, context, values));

    return (values.isEmpty() && !skipConcurrentPhase(log)) ? Optional.empty() : Optional.of(values);
  }

  private static boolean extractShortStopEvent(String log, Map<String, String> values) {
    Matcher matcher = PATTERN_STOP_EVENT_SHORT.matcher(log);
    if (!matcher.matches()) {
      return false;
    }

    values.put(LogField.EVENT.toString(), "O");
    LogExtractor.putIfNotNull(values, LogField.EVENT_DATE.toString(), matcher.group(1));
    values.put(LogField.EVENT_TIME.toString(), matcher.group(2));
    values.put(LogField.MEM_BEFORE.toString(), matcher.group(3));
    values.put(LogField.MEM_TOTAL.toString(), matcher.group(4));
    values.put(LogField.GC_TIME.toString(), matcher.group(5));

    return true;
  }

  private static boolean extractInitialMark(String log, Map<String, String> values) {
    Matcher matcher = PATTERN_MARK_INITIAL.matcher(log);
    if (!matcher.matches()) {
      return false;
    }

    values.put(LogField.EVENT.toString(), "O");
    values.put(LogField.CONCURRENT_PHASE.toString(), "initial-mark");
    LogExtractor.putIfNotNull(values, LogField.EVENT_DATE.toString(), matcher.group(1));
    values.put(LogField.EVENT_TIME.toString(), matcher.group(2));
    values.put(LogField.MEM_OLD_BEFORE.toString(), matcher.group(3));
    values.put(LogField.MEM_OLD_TOTAL.toString(), matcher.group(4));
    values.put(LogField.MEM_BEFORE.toString(), matcher.group(5));
    values.put(LogField.MEM_TOTAL.toString(), matcher.group(6));
    values.put(LogField.GC_TIME.toString(), matcher.group(7));
    values.put(LogField.GC_TIME_USER.toString(), matcher.group(8));
    values.put(LogField.GC_TIME_SYS.toString(), matcher.group(9));
    values.put(LogField.GC_TIME_REAL.toString(), matcher.group(10));

    return true;
  }

  private static boolean extractRemark(String log, Map<String, String> values) {
    Matcher matcher = PATTERN_REMARK.matcher(log);
    if (!matcher.matches()) {
      return false;
    }

    values.put(LogField.EVENT.toString(), "O");
    values.put(LogField.CONCURRENT_PHASE.toString(), "remark");
    LogExtractor.putIfNotNull(values, LogField.EVENT_DATE.toString(), matcher.group(1));
    values.put(LogField.EVENT_TIME.toString(), matcher.group(2));
    values.put(LogField.MEM_NEW_BEFORE.toString(), matcher.group(3));
    values.put(LogField.MEM_NEW_TOTAL.toString(), matcher.group(4));
    values.put(LogField.MEM_OLD_BEFORE.toString(), matcher.group(7));
    values.put(LogField.MEM_OLD_TOTAL.toString(), matcher.group(8));
    values.put(LogField.MEM_BEFORE.toString(), matcher.group(9));
    values.put(LogField.MEM_TOTAL.toString(), matcher.group(10));
    values.put(LogField.GC_TIME.toString(), matcher.group(11));
    values.put(LogField.GC_TIME_USER.toString(), matcher.group(12));
    values.put(LogField.GC_TIME_SYS.toString(), matcher.group(13));
    values.put(LogField.GC_TIME_REAL.toString(), matcher.group(14));

    return true;
  }

  private static boolean extractFullCollection(
      String log, ExtractContext context, Map<String, String> values) {
    Matcher matcher = PATTERN_FULL_GC.matcher(log);
    if (!matcher.matches()) {
      return false;
    }

    values.put(LogField.EVENT.toString(), "F");
    LogExtractor.putIfNotNull(values, LogField.EVENT_DATE.toString(), matcher.group(1));
    values.put(LogField.EVENT_TIME.toString(), matcher.group(2));
    values.put(LogField.MEM_OLD_BEFORE.toString(), matcher.group(3));
    values.put(LogField.MEM_OLD_AFTER.toString(), matcher.group(4));
    values.put(LogField.MEM_OLD_TOTAL.toString(), matcher.group(5));
    values.put(LogField.GC_OLD_TIME.toString(), matcher.group(6));
    values.put(LogField.MEM_BEFORE.toString(), matcher.group(7));
    values.put(LogField.MEM_AFTER.toString(), matcher.group(8));
    values.put(LogField.MEM_TOTAL.toString(), matcher.group(9));
    values.put(LogField.GC_TIME.toString(), matcher.group(10));
    values.put(LogField.GC_TIME_USER.toString(), matcher.group(11));
    values.put(LogField.GC_TIME_SYS.toString(), matcher.group(12));
    values.put(LogField.GC_TIME_REAL.toString(), matcher.group(13));

    subExtract(log, context, values);

    return true;
  }

  private static boolean skipConcurrentPhase(String log) {
    return PATTERN_CONCURRENT_PHASE.matcher(log).matches();
  }

  private static void subExtract(String line, ExtractContext context, Map<String, String> values) {
    if (!context.getExtractors().isEmpty()) {
      for (LogExtractor extractor : context.getExtractors()) {
        Optional<Map<String, String>> option = extractor.extract(line, context);
        if (option.isPresent()) {
          values.putAll(option.get());
        }
      }
      return;
    }

    for (LogExtractor extractor : context.getAvailableExtractors()) {
      Optional<Map<String, String>> option = extractor.extract(line, context);
      if (option.isPresent()) {
        logger.fine(() -> "Adding " + extractor);
        context.getExtractors().add(extractor);
        values.putAll(option.get());
      }
    }
  }

  public static void main(String[] args) {
    System.out.println(PATTERN_FULL_GC);
  }
}
