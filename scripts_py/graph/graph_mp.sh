#!/bin/bash
echo "type, prob, max avg (GB)"
python3 graph_pm.py longfast_st 0.000001 lf/1t/*.csv
python3 graph_pm.py guava_st 0.000001 g/1t/*.csv
python3 graph_pm.py orestes_st 0.000001 o/1t/*.csv
python3 graph_pm.py longfast_mt 0.000001 lf/11t/*.csv
python3 graph_pm.py guava_mt 0.000001 g/11t/*.csv
python3 graph_pm.py orestes_mt 0.000001 o/11t/*.csv