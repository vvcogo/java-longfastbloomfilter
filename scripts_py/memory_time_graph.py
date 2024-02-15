import matplotlib.pyplot as plt
import os
import time

# TODO add marker showing timestamps of different events (gc, warmup, inserts start/end, queries start/end)

bloomfilter_types = ['longfastbloomfilter', 'guava', 'orestes', 'longfastbloomfilter-optimized']
num_threads = 48
probability = 1/10**6

def plot_bloomfilter(bf_type):
    plt.figure(bf_type)
    x = []
    y = []
    jfr_file = os.path.join('prints', f'p={probability}', bf_type, f'{num_threads}t', '1.txt')
    heap_event = False
    with open(jfr_file, 'r') as f:
        for line in f:
            if 'jdk.GCHeapSummary' in line:
                heap_event = True
            if heap_event:
                if 'startTime' in line:
                    start_time_str = line.split('startTime = ')[1]
                    # 23:03:05.221 (2023-11-17)
                    start_time = time.strptime(start_time_str.strip(), '%H:%M:%S.%f (%Y-%m-%d)')
                    x.append(time.mktime(start_time))
                if 'heapUsed' in line:
                    heap_used = line.split(' ')[-2]
                    y.append(float(heap_used))
                    heap_event = False
    x = [value - x[0] for value in x]
    plt.plot(x, y)

for bf_type in bloomfilter_types:
    plot_bloomfilter(bf_type)
plt.show()
