import os
import numpy
import matplotlib.pyplot as plt
from matplotlib.ticker import FormatStrFormatter
import json
import matplotlib

def calculateAverageBytes(path):
    if (not os.path.exists(path)):
        return None
    files = os.listdir(path)
    values = []
    for f in files:
        lines = []
        with open(path + '/' + f, 'r') as file:
            lines = file.readlines()
        memoryValues = []
        for line in lines[1::]:
            value = float(line.split(',')[1])
            if (value > 0):
                memoryValues.append(value)
        values.append(max(memoryValues))
    print(values)
    return numpy.average(values)


dirs = [d for d in os.listdir() if os.path.isdir(d)]

points = {}

for dir in dirs:
    p = float(dir.split("=")[1])
    points[p] = {}
    points[p]['longfastbloomfilter-st'] = calculateAverageBytes(dir + '/lf/1t')
    points[p]['longfastbloomfilter-mt'] = calculateAverageBytes(dir + '/lf/11t')
    points[p]['orestes-st'] = calculateAverageBytes(dir + '/o/1t')
    points[p]['orestes-mt'] = calculateAverageBytes(dir + '/o/11t')
    points[p]['guava-st'] = calculateAverageBytes(dir + '/g/1t')
    points[p]['guava-mt'] = calculateAverageBytes(dir + '/g/11t')

# print(points)

prettyJson = json.dumps(points, indent=4)
with open('points.json', 'w') as jsonFile:
    jsonFile.write(prettyJson)

x = [float(p) for p in points.keys()]
y = []
for p in x:
    value = points[p]['orestes-mt']
    y.append(value)

print(x)
print(y)

# plt.xticks(range(len(x)), x)
plt.xticks(x)
plt.plot(x, y, '-o')
plt.show()
    