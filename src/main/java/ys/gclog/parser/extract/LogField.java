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

public enum LogField {
  EVENT("Type"),
  EVENT_DATE("Date"),
  EVENT_TIME("Time"), // in seconds
  GC_TIME("PauseTime"), // in seconds
  GC_TIME_USER("UserPauseTime"),
  GC_TIME_SYS("SysPauseTime"),
  GC_TIME_REAL("RealPauseTime"),
  GC_NEW_TIME("NewGenPauseTime"),
  GC_OLD_TIME("OldGenPauseTime"),
  MEM_BEFORE("MemBefore"),
  MEM_AFTER("MemAfter"),
  MEM_TOTAL("MemTotal"),
  MEM_NEW_BEFORE("NewMemBefore"),
  MEM_NEW_AFTER("NewMemAfter"),
  MEM_NEW_TOTAL("NewMemTotal"),
  MEM_EDEN_BEFORE("EdenMemBefore"),
  MEM_EDEN_AFTER("EdenMemAfter"),
  MEM_EDEN_TOTAL("EdenMemTotal"),
  MEM_SURVIVOR_BEFORE("SurvivorMemBefore"),
  MEM_SURVIVOR_AFTER("SurvivorMemAfter"),
  MEM_OLD_BEFORE("OldMemBefore"),
  MEM_OLD_AFTER("OldMemAfter"),
  MEM_OLD_TOTAL("OldMemTotal"),
  MEM_PERM_BEFORE("PermMemBefore"),
  MEM_PERM_AFTER("PermMemAfter"),
  MEM_PERM_TOTAL("PermMemTotal"),
  MEM_METASPACE_BEFORE("MetaspaceMemBefore"),
  MEM_METASPACE_AFTER("MetaspaceMemAfter"),
  MEM_METASPACE_TOTAL("MetaspaceMemTotal"),
  CONCURRENT_PHASE("ConcurrentPhase");

  private final String name;

  private LogField(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return name;
  }
}
