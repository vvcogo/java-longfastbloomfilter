import sys
import numpy

argc = len(sys.argv)

if argc < 3:
    print('Invalid arguments')
    sys.exit(1)

categoryName = sys.argv[1]

def read_file(path):
    with open(path, 'r') as file:
        return file.readlines()
    
def get_number_value(lines, lineNumber):
    line = lines[lineNumber]
    return float(line.split(' ')[-2])

def get_input_throughtput(lines):
    return get_number_value(lines, 18)

def get_query_throughtput(lines):
    return get_number_value(lines, 23)

def get_input_latency(lines):
    return get_number_value(lines, 19)

def get_query_latency(lines):
    return get_number_value(lines, 24)


input_throughtput = []
query_throughtput = []
input_latency = []
query_latency = []

for file in sys.argv[2:]:
    lines = read_file(file)
    input_throughtput.append(get_input_throughtput(lines))
    query_throughtput.append(get_query_throughtput(lines))
    input_latency.append(get_input_latency(lines))
    query_latency.append(get_query_latency(lines))

average_input_throughput = round(numpy.average(input_throughtput), 2)
average_quey_throughput = round(numpy.average(query_throughtput), 2)
average_input_latency = round(numpy.average(input_latency), 5)
average_query_latency = round(numpy.average(query_latency), 5)

print(f'{categoryName}, {average_input_throughput}, {average_input_latency}, {average_quey_throughput}, {average_query_latency}')

# print(categoryName)
# print(f" > Average input throughput: {average_input_throughput} e/s")
# print(f" > Average input latency: {average_input_latency} ms")
# print(f" > Average query throughput: {average_quey_throughput} e/s")
# print(f" > Average query latency: {average_query_latency} ms")
# print()

