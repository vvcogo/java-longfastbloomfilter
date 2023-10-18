import sys
import numpy as np

def read_args():
    argc = len(sys.argv)
    if argc < 12:
        print('Invalid arguments')
        sys.exit(1)
    categoryName = sys.argv[1]
    prob = sys.argv[2]
    return (categoryName, prob)

def read_file(path):
    with open(path, 'r') as file:
        next(file)
        return file.readlines()

def split_cols(lines_list):
    result = []
    for line in lines_list:
        result.append(line.split(',')[-1])
    return result
        
def get_max(list_y):
    max = list_y[0]
    for elem in list_y:
        if elem > max:
            max = elem
    return max

def main():
    (category, prob) = read_args()
    
    allMax = []
    for file in sys.argv[3:]:
        list_lines = read_file(file)
        colY = split_cols(list_lines)
        maxPerFile = get_max(colY)
        allMax.append(int(maxPerFile))
        
    avgMaxBytes = np.average(allMax) #bytes
    avgMaxGB = avgMaxBytes*pow(10, -9)
    print(f"{category}, {prob}, {str(avgMaxGB)}")

    
    

    
#--------------
main()
        


