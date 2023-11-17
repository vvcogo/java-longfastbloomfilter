import os
import statistics as st
from itertools import combinations

def to_GB(num):
    return round(num * (10**-9),2)

def read_logs_in_folder(path, pp):
    logs_data = {'bytes': 0, 'gb': 0, 'p': 0}
    memo_bytes = []
    
    for file_name in os.listdir(path):
        with open(os.path.join(path, file_name), 'r') as file:
            all_lines = file.readlines()
            for i in range(len(all_lines)):
                if 'Used memory:' in all_lines[i]:
                    memo_bytes.append(int(all_lines[i].split(' ')[-2]))
                       
    avg_bytes = st.mean(memo_bytes)
    logs_data['bytes'] = avg_bytes
    logs_data['gb'] = to_GB(avg_bytes)
    logs_data['p'] = pp

    return logs_data

#----------------------------------------------------------------------

bloomfilter_types = ['longfastbloomfilter', 'guava', 'orestes']
p = ['0.1', '0.001', '0.0001', '1e-05','1e-06', '1e-07', '1e-08']
logs_data = {}

for bf_type in bloomfilter_types:
    for pp in p:
        path = os.path.join(f'p={pp}', bf_type, '48t')
        logs_data[f'{bf_type}-48t-{pp}'] = read_logs_in_folder(path, pp)


#readed from memory used in logs-cg
diffs = {}
print('Type - Probability, Used memory (Bytes), Used memory (GB)')
for k, v in logs_data.items():
    print(k, v['bytes'], v['gb'], sep=', ')
    dif = ((v['bytes'] / 64) * 8) * (10**-9)
    diffs[v['p']] = dif
    

#---------------combinations-------------------------------
print('\n' + '-'*10 + '[COMBINATIONS MEMO]' + '-'*10 + '\n' ) 
comb = combinations(diffs.items(), 2) 

print('Probability 1, Probability 2, Diff between 2 probabilities (GB)')
for c in list(comb):
    print(c[0][0], c[1][0], round(abs(c[0][1] - c[1][1]),3), sep=', ')
    
