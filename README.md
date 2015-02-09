## Summary
This Java library provides a parser for the GC logs produced by Oracle JDK 1.7 and 1.8. It includes a command line utility to generate CSV files from the logs. Your can load them in your favorite tool for analysis then. See the following inspiring article as an example - [Using R to analyze Java G1 garbage collector log files](https://blogs.oracle.com/taylor22/entry/using_r_to_analyze_g1gc).

## Requirements
The library requires Java 1.8.

## Documentation
#### Supported JVM Options
The library is able to parse the logs produced by using the key GC logging options:
* -XX:+PrintGC (or -verbose:gc)
* -XX:+PrintGCDetails
* -XX:+PrintGCTimeStamps
* -XX:+PrintGCDateStamps

The following Oracle JVM's garbage collectors are supported:

<table>
<thead>
<tr>
  <td><b>Generation</b></td>
  <td><b>Collector<b></td>
  <td><b>JVM Option</b></td>
</tr>
</thead>
<tbody>
  <tr><td rowspan="4">New</td><td>Copy</td><td>-XX:+UseSerialGC</td></tr>
  <tr><td>PS Scavenge</td><td>-XX:+UseParallelGC</td></tr>
  <tr><td>ParNew</td><td>-XX:+UseParNewGC</td></tr>
  <tr><td>G1 Young</td><td>-XX:+UseG1GC</td></tr>
  <tr><td rowspan="4">Old</td><td>MarkSweepCompact</td><td>-XX:+UseSerialGC</td></tr>
  <tr><td>PS MarkSweep</td><td>-XX:+UseParallelGC<br/>-XX:+UseParallelOldGC</td></tr>
  <tr><td>ConcurrentMarkSweep<br/>(except iCMS)</td><td>-XX:+UseConcMarkSweepGC</td></tr>
  <tr><td>G1 Mixed</td><td>-XX:+UseG1GC</td></tr>
</tbody>
</table>

#### Provided Fields
The parser tries to extract as much information as possible from the log files. All possible fields that may present in the result CSV files are described in the table below. Exact subset of these fields depends on the GC options being used.

<table>
<thead>
  <tr><th>Field</th><th>Description</th></tr>
</thead>
<tbody>
  <tr>
    <td>Type</td>
    <td>Type of the GC pause event. Possible values are:
      <ul>
        <li>N - new (young) generation collection</li>
        <li>O - old (tenured) generation collection (stop-the-world phase of the concurrent collector)</li>
        <li>F - full collection</li>
      </ul>
    </td>
  </tr>
  <tr><td>Date</td><td>Date/time of the GC event</td></tr>
  <tr><td>Time</td><td>Time passed since JVM start (in seconds)</td></tr>
  <tr><td>PauseTime</td><td>GC pause time (in seconds)</td></tr>
  <tr><td>UserPauseTime</td><td>CPU time spent in user space (in seconds)</td></tr>
  <tr><td>SysPauseTime</td><td>CPU time spent in kernel space (in seconds)</td></tr>
  <tr><td>RealPauseTime</td><td>Real time (in seconds) spent by GC (rounded value of PauseTime)</td></tr>
  <tr><td>NewGenPauseTime</td><td>Time spent to collect young generation (in seconds)</td></tr>
  <tr><td>OldGenPauseTime</td><td>Time spent to collect old generation (in seconds)</td></tr>
  <tr><td>MemBefore</td><td>Used heap memory before GC run</td></tr>
  <tr><td>MemAfter</td><td>Used heap memory after GC run</td></tr>
  <tr><td>MemTotal</td><td>Overall allocated heap memory</td></tr>
  <tr><td>NewMemBefore</td><td>Used young generation space before GC run</td></tr>
  <tr><td>NewMemAfter</td><td>Used young generation space after GC run</td></tr>
  <tr><td>NewMemTotal</td><td>Overall allocated young generation space</td></tr>
  <tr><td>EdenMemBefore</td><td>Used Eden space before GC run</td></tr>
  <tr><td>EdenMemAfter</td><td>Used Eden space after GC run</td></tr>
  <tr><td>EdenMemTotal</td><td>Overall allocated Eden space</td></tr>
  <tr><td>SurvivorMemBefore</td><td>Used Survivor space before GC run</td></tr>
  <tr><td>SurvivorMemAfter</td><td>Used Survivor space after GC run</td></tr>
  <tr><td>OldMemBefore</td><td>Used old generation space before GC run</td></tr>
  <tr><td>OldMemAfter</td><td>Used old generation space after GC run</td></tr>
  <tr><td>OldMemTotal</td><td>Overall allocated old generation space</td></tr>
  <tr><td>PermMemBefore</td><td>Used permanent generation space before GC run</td></tr>
  <tr><td>PermMemAfter</td><td>Used permanent generation space after GC run</td></tr>
  <tr><td>PermMemTotal</td><td>Overall allocated permanent space</td></tr>
  <tr><td>MetaspaceMemBefore</td><td>Used metaspace before GC run (since Java 8)</td></tr>
  <tr><td>MetaspaceMemAfter</td><td>Used metaspace after GC run (since Java 8)</td></tr>
  <tr><td>MetaspaceMemTotal</td><td>Overall allocated metaspace (since Java 8)</td></tr>
  <tr><td>ConcurrentPhase</td><td>Stop-the-world phase of the concurrent old collector</td></tr>
</tbody>
</table>

Memory-related fields are extracted as is and measured either in Kylobytes or Megabytes.

## Installation
Run Maven command to build a JAR file:

`mvn clean install`

## License
Licensed under the Apache License 2.0.
