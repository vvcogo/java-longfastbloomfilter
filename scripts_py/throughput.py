import sys
import os
import numpy

argc = len(sys.argv)

if argc < 2:
    print('Invalid number of arguments')
    sys.exit(1)

p = float(sys.argv[1])

bloomfilter_types = ['longfastbloomfilter', 'guava']
threads = [1, 2, 6, 12, 24, 48, 96]
base_dir = 'logs'

def read_logs_in_folder(path):
    logs_data = {'insert_throughput': 0, 'insert_latency': 0, 'query_latency': 0, 'query_throughtput': 0}
    insert_throughput = []
    insert_latency = []
    query_throughtput = []
    query_latency = []
    for file_name in os.listdir(path):
        with open(os.path.join(path, file_name), 'r') as file:
            lines = file.readlines()
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
    logs_data['insert_throughput'] = numpy.mean(insert_throughput)
    logs_data['insert_latency'] = numpy.mean(insert_latency)
    logs_data['query_throughtput'] = numpy.mean(query_throughtput)
    logs_data['query_latency'] = numpy.mean(query_latency)
    return logs_data

logs_data = {}

for bf_type in bloomfilter_types:
    for num_threads in threads:
        path = os.path.join(base_dir, f'p={p}', bf_type, f'{num_threads}t')
        logs_data[f'{bf_type}-{num_threads}t'] = read_logs_in_folder(path)

print('Type, Average Insert Throughput (e/s), Average Insert Latency (ms), Average Query Throughtput (e/s), Avearage Query Latency (ms)')
for k, v in logs_data.items():
    print(k, v['insert_throughput'], v['insert_latency'], v['query_throughtput'], v['query_latency'], sep=', ')