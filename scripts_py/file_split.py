import sys
import random

def writeToFile(filename, lines):
    with open(filename, "w") as file:
        for line in lines:
            file.write(line + "\n")

def appendLines(output, start, end, lines):
    for i in range(start, end):
        output.append(lines[i])

argc = len(sys.argv)
if argc != 4:
    print("Usage ./" + sys.argv[0] + " <file> <80 file output> <20 file output>")
    sys.exit(1)

filename = sys.argv[1]
outputFilename80 = sys.argv[2]
outputFilename20 = sys.argv[3]

lines = []
with open(filename, "r") as file:
    lines = [line.rstrip() for line in file]

random.shuffle(lines)
size = len(lines)
file80Size = int(size * 0.8)
file80Output = []
file20Output = []

appendLines(file80Output, 0, file80Size, lines)
appendLines(file20Output, file80Size, size, lines)

writeToFile(outputFilename80, file80Output)
writeToFile(outputFilename20, file20Output)