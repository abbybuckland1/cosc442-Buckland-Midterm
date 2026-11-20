# Agent Instructions

## Build and Test Environment
- JoshHack is a Java project using Maven and JUnit 5.
- Use the Maven Wrapper (.\mvnw.cmd) in the VS Code PowerShell terminal.
- Run .\mvnw.cmd test to execute the JUnit tests.

## Quality Checks
- Run the relevant JUnit 5 tests, PIT mutation testing, and SpotBugs before considering quality-related work complete.
- Use .\mvnw.cmd org.pitest:pitest-maven:mutationCoverage for PIT mutation testing.
- Use .\mvnw.cmd spotbugs:spotbugs for SpotBugs.
- Write meaningful tests with clear assertions rather than tests that only increase coverage.

## Code Changes
- Preserve existing functionality unless a change is explicitly requested.
- Never weaken, remove, or disable existing tests simply to make the build pass.
- Clearly identify production-code changes and obtain human approval before making them.
-Keep modifications limited to the requested task.

## Git and Human Review 
- Do not automatically commit, push, or merge changes.
- Summarize all modified files and quality-check results for human review before the work is considered complete.