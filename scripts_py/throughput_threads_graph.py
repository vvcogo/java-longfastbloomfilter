# GRAPH THROUGHPUT THREADS
import sys
import os
import numpy as np
import matplotlib.pyplot as plt

argc = len(sys.argv)

if argc < 2:
    print('Invalid number of arguments')
    sys.exit(1)
p = float(sys.argv[1])
bloomfilter_types = ['longfastbloomfilter', 'guava', 'orestes']
threads = [1, 2, 6, 12, 24, 48, 96, 192]
base_dir = 'logs'


def format_number(num):
    return format(round(num, 5), ',').replace(',', ' ')


def read_logs_in_folder(path):
    logs_data = {'insert_throughput': 0, 'insert_latency': 0, 'query_latency': 0, 'query_throughtput': 0}
    insert_throughput = []
    insert_latency = []
    query_throughtput = []
    query_latency = []
    for file_name in os.listdir(path):
        with open(os.path.join(path, file_name), 'r') as file:
            lines = file.readlines()
            in_th, in_lat, quer_th, quer_lat = data_avg_per_iteration(lines)
            insert_throughput.append(in_th)
            insert_latency.append(in_lat)
            query_throughtput.append(quer_th)
            query_latency.append(quer_lat)
    logs_data['insert_throughput'] = format_number(np.mean(insert_throughput))
    logs_data['insert_latency'] = format_number(np.mean(insert_latency))
    logs_data['query_throughtput'] = format_number(np.mean(query_throughtput))
    logs_data['query_latency'] = format_number(np.mean(query_latency))
    return logs_data


def data_avg_per_iteration(lines):
    insert_throughput = []
    insert_latency = []
    query_throughtput = []
    query_latency = []
    value = '-1'
    for i in range(len(lines)):
        if 'Throughput:' in lines[i] or 'Latency:' in lines[i]:
            value = float(lines[i].split(' ')[-2])
        if 'inserting' in lines[i-1]:
            insert_throughput.append(value)
        elif 'inserting' in lines[i-2]:
            insert_latency.append(value)
        elif 'querying' in lines[i-1]:
            query_throughtput.append(value)
        elif 'querying' in lines[i-2]:
            query_latency.append(value)
    return np.mean(insert_throughput), np.mean(insert_latency), np.mean(query_throughtput), np.mean(query_latency)


logs_data_last = {}
for bf_type in bloomfilter_types:
    for num_threads in threads:
        path = os.path.join(base_dir, f'p={p}', bf_type, f'{num_threads}t')
        logs_data_last[f'{bf_type}-{num_threads}t'] = read_logs_in_folder(path)

f = open("table_throughput_threads.csv", "w")
f.write('Type, Average Insert Throughput (e/s), Average Insert Latency (ms), Average Query Throughtput (e/s), Avearage Query Latency (ms)')
for k, v in logs_data_last.items():
    f.write(k, v['insert_throughput'])
f.close()


def make_plot(typeGraph):
    plt.figure()
    plt.title(f'{typeGraph}_throughput_thread')

    file_table = open('table_throughput_threads.csv', 'r')
    lines = file_table.readlines()

    for bfType in bloomfilter_types:
        y = []
        for l in lines:
            if bfType in l:
                if typeGraph is 'insert':
                    y.append(l.split(',')[1])
                else:
                    y.append(l.split(',')[3])
        x = threads
        plt.xscale('log')
        plt.plot(x, y, '-o', label=bfType)
    plt.legend(loc='upper right')


make_plot('insert')
make_plot('query')
