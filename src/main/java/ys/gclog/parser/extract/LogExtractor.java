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

import java.util.Map;
import java.util.Optional;

public interface LogExtractor {

  String REGEX_DECIMAL = "(\\d+\\.\\d+)";
  String REGEX_TIME = REGEX_DECIMAL;
  /**
   * Regular expression for the date field, e.g. {@code 2015-01-15T12:30:38.059+0300}.
   */
  String REGEX_DATE = "(\\d+-\\d+-\\d+T\\d+:\\d+:\\d+\\.\\d+\\+\\d+)";
  String REGEX_MEMORY_AT = "(\\d+)K\\((\\d+)K\\)";
  String REGEX_MEMORY_FROM_TO_KB = "(\\d+)K->(\\d+)K\\((\\d+)K\\)";
  String REGEX_MEMORY_FROM_TO_MB = "(\\d+)M->(\\d+)M\\((\\d+)M\\)";
  String REGEX_MEMORY_WITH_TOTAL_FROM_TO = String.format(
      "%s[BKM]\\(%s[BKM]\\)->%s[BKM]\\(%s[BKM]\\)",
      REGEX_DECIMAL, REGEX_DECIMAL, REGEX_DECIMAL, REGEX_DECIMAL);
  String REGEX_MEMORY_WITHOUT_TOTAL_FROM_TO =
      String.format("%s[BKM]->%s[BKM]", REGEX_DECIMAL, REGEX_DECIMAL);
  String REGEX_TIMES = String.format("\\[Times: user=%s sys=%s, real=%s secs]",
      REGEX_DECIMAL, REGEX_DECIMAL, REGEX_DECIMAL);

  boolean accept(String log);

  Optional<Map<String, String>> extract(String log, ExtractContext context);

  static void putIfNotNull(Map<String, String> map, String key, String value) {
    if (value != null) {
      map.put(key, value);
    }
  }
}
