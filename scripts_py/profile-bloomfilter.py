import os
import sys
from pathlib import Path

probabilities = [0.000001]
bloomfilterTypes = ['longfastbloomfilter']
threads = [1, 12]
numExecutions = 1
expectedElements = 13967372

def writeConfig(path, p, bfType):
    with open(path, 'w') as f:
        f.write(f'expected-elements = {expectedElements}\n')
        f.write(f'false-positive-probability = {p}\n')
        f.write(f'bloomfilter-type = {bfType}\n')
        f.write('serializer = io.github.vvcogo.longfastbloomfilter.framework.serialization.StringSerializer\n')

for p in probabilities:
    for bfType in bloomfilterTypes:
        writeConfig('config.properties', p, bfType)
        for t in threads:
            for i in range(1, numExecutions + 1):
                print(f'Running {bfType} #{i} p={p} with {t} threads')
                recordingFilePath = os.path.join('recordings', f'p={p}', bfType, f'{t}t', f'{i}.jfr')
                printsFilePath = os.path.join('prints', f'p={p}', bfType, f'{t}t', f'{i}.txt')
                logsFilePath = os.path.join('logs', f'p={p}', bfType, f'{t}t', f'{i}.txt')

                Path(recordingFilePath).parent.mkdir(exist_ok=True, parents=True)
                Path(printsFilePath).parent.mkdir(exist_ok=True, parents=True)
                Path(logsFilePath).parent.mkdir(exist_ok=True, parents=True)

                os.system(f'java -Xmx10G -XX:StartFlightRecording:filename={recordingFilePath} -jar bloomfilter-test.jar inputs/input-80-{i}.txt queries/query-20-{i}.txt config.properties {t} > "{logsFilePath}"')
                os.system(f'jfr print "{recordingFilePath}" > "{printsFilePath}"')
                print('\tDone\n')