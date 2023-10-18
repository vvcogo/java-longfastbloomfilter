import os
from pathlib import Path
import matplotlib.pyplot as plt
import numpy
import sys
      
def convertToB(num, type):
    if type == 'B':
        return num
    if type == "KB":
        return num+(10**3)
    elif type == "MB":
        return num*(10**6)
    elif type == "GB":
        return num*(10**9)
    
def calculate_average_memory_used_file_after_gc(filePath):
    memoryValues = []
    shouldCaptureMemory = False
    with open(filePath, 'r') as file:
        afterGC = False
        for line in file:
            if 'atomic.AtomicLongArray.<init>' in line or 'util.BitSet.<init>' in line:
                shouldCaptureMemory = True
            elif 'when = "After GC"' in line:
                afterGC = True
            elif 'when = "Before GC"' in line:
                afterGC = False
            elif 'heapUsed' in line and afterGC and shouldCaptureMemory:
                heapUsedValue = line.split(' = ')[1]
                num = float(heapUsedValue.split(' ')[0].replace(',', '.'))
                type = heapUsedValue.split(' ')[1][:-1]
                memory = convertToB(num, type)
                memoryValues.append(memory)
    return numpy.mean(memoryValues)
    
def calculate_max_memory_used_file_after_gc(filePath):
    maxMemory = 0
    with open(filePath, 'r') as file:
        afterGC = False
        for line in file:
            if 'when = "After GC"' in line:
                afterGC = True
            elif 'when = "Before GC"' in line:
                afterGC = False
            elif 'heapUsed' in line and afterGC:
                heapUsedValue = line.split(' = ')[1]
                num = float(heapUsedValue.split(' ')[0].replace(',', '.'))
                type = heapUsedValue.split(' ')[1][:-1]
                memory = convertToB(num, type)
                if memory > maxMemory:
                    maxMemory = memory
    return maxMemory

def calculate_max_memory_used(filePath):
    maxMemory = 0
    with open(filePath, 'r') as file:
        for line in file:
            if 'heapUsed' in line:
                heapUsedValue = line.split(' = ')[1]
                num = float(heapUsedValue.split(' ')[0].replace(',', '.'))
                type = heapUsedValue.split(' ')[1][:-1]
                memory = convertToB(num, type)
                if memory > maxMemory:
                    maxMemory = memory
    return maxMemory

def calculate_average_memory_used_file(filePath):
    memoryValues = []
    shouldCaptureMemory = False
    with open(filePath, 'r') as file:
        for line in file:
            if 'atomic.AtomicLongArray.<init>' in line or 'util.BitSet.<init>' in line:
                shouldCaptureMemory = True
            if 'heapUsed' in line and shouldCaptureMemory:
                heapUsedValue = line.split(' = ')[1]
                num = float(heapUsedValue.split(' ')[0].replace(',', '.'))
                type = heapUsedValue.split(' ')[1][:-1]
                memoryValues.append(convertToB(num, type))
    return numpy.mean(memoryValues)


def calculate_average_memory_used(folderPath):
    files = os.listdir(folderPath)
    memoryValues = []
    for file in files:
        filePath = os.path.join(folderPath, file)
        memory = calculate_max_memory_used(filePath)
        memoryValues.append(memory)
    return numpy.mean(memoryValues)

def get_average_bloomfilter_memory(bfType, p, numThreads):
    folderPath = os.path.join('prints', f'p={p}', bfType, f'{numThreads}t')
    return calculate_average_memory_used(folderPath)

probabilities = [0.1, 0.01, 0.001, 0.0001, 0.00001, 0.000001, 0.0000001]
bloomfilterTypes = ['guava', 'longfastbloomfilter', 'orestes']


def make_plot(numThreads):
    plt.figure()
    plt.title(f'{numThreads} threads')
    for bfType in bloomfilterTypes:
        print(f'Getting {bfType} {numThreads} thread(s)...')
        y = []
        for p in probabilities:
            memory = get_average_bloomfilter_memory(bfType, p, numThreads)
            y.append(memory)
        x = probabilities
        plt.xscale('log')
        plt.plot(x, y, '-o', label=bfType)
    plt.legend(loc='upper right')


make_plot(1)
make_plot(11)
plt.show()


