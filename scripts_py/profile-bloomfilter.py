import os
from pathlib import Path

# to use java 17
java_command = '/usr/lib/jvm/java-17-openjdk-amd64/bin/java'
probabilities = []
for i in range(1, 9):
    probabilities.append(1/10**i)
bloomfilterTypes = ['longfastbloomfilter', 'guava', 'orestes', 'longfastbloomfilter-optimized']
threads = [1, 2, 6, 12, 24, 48, 96, 192]
numExecutions = 10
expectedElements = 13967372
hash_function = 'murmur_kirschmitz'


def writeConfig(path, p, bfType):
    optimize = False
    if bfType == 'longfastbloomfilter-optimized':
        optimize = True
        bfType = 'longfastbloomfilter'
    with open(path, 'w') as f:
        f.write(f'expected-elements = {expectedElements}\n')
        f.write(f'false-positive-probability = {p}\n')
        f.write(f'bloomfilter-type = {bfType}\n')
        f.write('serializer = io.github.vvcogo.longfastbloomfilter.framework.serialization.StringSerializer\n')
        f.write(f'hash-function = {hash_function}')
        if optimize:
            f.write(f'enable-speed-optimization = true')


for p in probabilities:
    for bfType in bloomfilterTypes:
        writeConfig('config.properties', p, bfType)
        for t in threads:
            for i in range(1, numExecutions + 1):
                recordingFilePath = os.path.join('recordings', f'p={p}', bfType, f'{t}t', f'{i}.jfr')
                printsFilePath = os.path.join('prints', f'p={p}', bfType, f'{t}t', f'{i}.txt')
                logsFilePath = os.path.join('logs', f'p={p}', bfType, f'{t}t', f'{i}.txt')

                Path(recordingFilePath).parent.mkdir(exist_ok=True, parents=True)
                Path(printsFilePath).parent.mkdir(exist_ok=True, parents=True)
                Path(logsFilePath).parent.mkdir(exist_ok=True, parents=True)
                os.system(f'{java_command} -Xmx100G -XX:StartFlightRecording:filename={recordingFilePath} -jar bloomfilter-test.jar inputs/input-80-{i}.txt queries/query-20-{i}.txt config.properties {t} "{logsFilePath}"')
                os.system(f'jfr print "{recordingFilePath}" > "{printsFilePath}"')
