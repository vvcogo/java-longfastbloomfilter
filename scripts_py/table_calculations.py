import math
import sys
from bitarray import bitarray

# m = -(n * ln(p)) / ln(2)^2
def calculateMinBitArraySize(n, p):
    return int(-n * math.log(p) / math.log(2)**2)


# n = -(m * ln(2)^2) / ln(p)
def calculateMaxNumberOfElements(p, mMax):
    return int(-mMax * math.log(2)**2 / math.log(p))

# k = ln(2) * m/n
def calculateOptimalNumberOfHashFunctions(m, n):
    return int(math.log(2) * m/n)


PROBABILITIES = [0.1, 0.01, 0.001, 0.0001]

def printStats(libName, mMax):
    print(f"\n===================[ {libName} ]===================")
    print(f"max m = {mMax:_}")
    for p in PROBABILITIES:
        print(f"> p = {p}")
        n = calculateMaxNumberOfElements(p, mMax)
        k = calculateOptimalNumberOfHashFunctions(mMax, n)
        print(f"\t n = {n:_}")
        print(f"\t k = {k}")
    print("======================================")

INTEGER_MAX_VALUE = 2**31 - 1
UINTEGER_MAX_VALUE = 2**32

LONG_MAX_VALUE = 2**63 - 1
ULONG_MAX_VALUE = 2**64

SHORT_MAX_VALUE = 2**15 - 1
USHORT_MAX_VALUE = 2**16

BYTE_MAX_VALUE = 2**7 - 1
UBYTE_MAX_VALUE = 2**8


# Nosso antigo - Long array (indexada por ints, logo INTEGER_MAX_VALUE longs) e cada long tem 64 bits

# Guava - usa também uma long array
# https://github.com/google/guava/blob/master/guava/src/com/google/common/hash/BloomFilter.java
printStats("Nosso antigo / Guava", INTEGER_MAX_VALUE * 64)

# Orestes - utiliza bitset do java, indexado por int logo INTEGER_MAX_VALUE
# https://github.com/Baqend/Orestes-Bloomfilter

# MagnusS/Java-BloomFilter - utiliza tambem bitset do java
# https://github.com/MagnusS/Java-BloomFilter

# wangxu0/bloomfilter - Utiliza BitSet do java ou BitSet do Jedis, ambos indexados por int
# https://github.com/wangxu0/bloomfilter/tree/master
printStats("Orestes / MagnusS / wangxu0", INTEGER_MAX_VALUE)

printStats("LONG FULL", LONG_MAX_VALUE)

printStats("ULONG FULL", ULONG_MAX_VALUE)

printStats("UINT FULL", UINTEGER_MAX_VALUE)

printStats("SHORT FULL", SHORT_MAX_VALUE)

printStats("USHORT FULL", USHORT_MAX_VALUE)

printStats("BYTE FULL", BYTE_MAX_VALUE)

printStats("UBYTE FULL", UBYTE_MAX_VALUE)


# TODO:
# https://github.com/jaybaird/python-bloomfilter/tree/master