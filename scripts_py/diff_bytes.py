import os
from itertools import combinations

# def to_GB(num):
#     return round(num * (10**-9),2)

def read_logs_in_folder(path, pp):
    logs_data = {'bytes': 0, 'p': 0}
    logs_data['p'] = pp
    
    with open(os.path.join(path, '1.txt'), 'r') as file:
        all_lines = file.readlines()
        for i in range(len(all_lines)):
            if 'BitSet Size:' in all_lines[i]:
                logs_data['bytes'] = int(all_lines[i].split(' ')[-1])
                break
                       
    return logs_data


#----------------------------------------------------------------------

bloomfilter_types = ['longfastbloomfilter'] # same values for bf extensions like guava and orestes
p = ['0.1', '0.001', '0.0001', '1e-05','1e-06', '1e-07', '1e-08']
logs_data = {}

for bf_type in bloomfilter_types:
    for pp in p:
        path = os.path.join(f'p={pp}', bf_type, '48t')
        logs_data[f'{bf_type}-48t-{pp}'] = read_logs_in_folder(path, pp)


#readed from bitset size in logs-gc
diffs = {}
print('Type, Probability, BitSet Size diff (Bytes)')
for k, v in logs_data.items():
    dif = ((v['bytes'] / 64) * 8) * (10**-9)
    print('Any BF', v['p'], dif, sep=', ')
    diffs[v['p']] = dif

#-----------------combinations-----------------------------
print('\n' + '-'*10 + '[COMBINATIONS BITSET]' + '-'*10 + '\n' ) 
comb = combinations(diffs.items(), 2) 

print('Probability 1, Probability 2, Diff between 2 probabilities (GB)')
for c in list(comb):
    # print(c)
    print(c[0][0], c[1][0], round(abs(c[0][1] - c[1][1]),3), sep=', ')
