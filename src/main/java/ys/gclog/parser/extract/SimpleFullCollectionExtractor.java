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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleFullCollectionExtractor implements LogExtractor {

  private static final Pattern PATTERN = Pattern.compile(
      String.format("(?:%s: )?%s: \\[Full GC(?: \\(.+?\\) )? %s, %s secs]\\s*",
          REGEX_DATE, REGEX_DECIMAL, REGEX_MEMORY_FROM_TO_KB, REGEX_DECIMAL));

  @Override
  public boolean accept(String log) {
    return PATTERN.matcher(log).matches();
  }

  @Override
  public Optional<Map<String, String>> extract(String log, ExtractContext context) {
    Matcher matcher = PATTERN.matcher(log);
    if (!matcher.matches()) {
      return Optional.empty();
    }

    Map<String, String> fields = new LinkedHashMap<>();
    fields.put(LogField.EVENT.toString(), "F");
    LogExtractor.putIfNotNull(fields, LogField.EVENT_DATE.toString(), matcher.group(1));
    fields.put(LogField.EVENT_TIME.toString(), matcher.group(2));
    fields.put(LogField.MEM_BEFORE.toString(), matcher.group(3));
    fields.put(LogField.MEM_AFTER.toString(), matcher.group(4));
    fields.put(LogField.MEM_TOTAL.toString(), matcher.group(5));
    fields.put(LogField.GC_TIME.toString(), matcher.group(6));

    return Optional.of(fields);
  }

  public static void main(String[] args) {
    System.out.println(PATTERN);
  }
}
