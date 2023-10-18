#!/bin/bash
echo "type, average insert throughput (e/s), average insert latency (ms), average query throughtput (e/s), avearage query latency (ms)"
python3 calculate.py guava-st guava-st-*.log
python3 calculate.py longfastbloomfilter-st longfastbloomfilter-st-*.log
python3 calculate.py orestes-st orestes-st-*.log
python3 calculate.py guava-mt guava-mt-*.log
python3 calculate.py longfastbloomfilter-mt longfastbloomfilter-mt-*.log
python3 calculate.py orestes-mt orestes-mt-*.log