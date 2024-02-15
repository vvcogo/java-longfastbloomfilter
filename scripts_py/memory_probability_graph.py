import matplotlib.pyplot as plt
import numpy as np
import os

bloomfilter_types = ['longfastbloomfilter', 'longfastbloomfilter-optimized']
num_threads = 48
probabilities = []
for i in range(1, 8):
    probabilities.append(1/10**i)

def bytes_to_mb(bytes):
    return bytes/10**6

def read_memory_by_probability(bf_type, probability):
    memory_bytes = []
    folder_path = os.path.join('logs', f'p={probability}', bf_type, f'{num_threads}t')
    for file in os.listdir(folder_path):
        with open(os.path.join(folder_path, file), 'r') as f:
            for line in f:
                if 'Used memory:' in line:
                    memory_bytes.append(int(line.split(' ')[-2]))
    return np.mean(memory_bytes)


graph_data = {}
for bf_type in bloomfilter_types:
    graph_data[bf_type] = {}
    for p in probabilities:
        graph_data[bf_type][p] = read_memory_by_probability(bf_type, p)


for bf_type, data in graph_data.items():
    plt.plot(list(data.keys()), list(data.values()), label=bf_type)
plt.xticks(probabilities)
plt.xscale('log')
plt.legend()
plt.show()
