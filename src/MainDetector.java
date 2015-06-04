/**
 * Copyright 2014 Vinicius Vielmo Cogo
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package long_bloomfilter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainDetector
{
	private final static int	S	= 30;
	private double				gId, gP, gOpt;
	private int					gTotalProc;
	private long				gN, gLinesClient, gTasks, gSensitiveN, gContainsN;
	private boolean				gWrite;
	private String				gServerFile, gClientFile, gAcid;
	private BufferedWriter		gSensitiveWriter, gNonSensitiveWriter;
	private ExecutorService		gExecutorService;
	private LongFastBloomFilter	gFilter;

	public MainDetector(double theP, double theOpt, String theServerFile,
			String theAcid, long theN, String theClientFile,
			long theLinesClient, long theTasks, int theThreads, boolean theWrite)
	{
		gId = System.currentTimeMillis();
		gP = theP;
		gOpt = theOpt;
		gServerFile = theServerFile;
		gAcid = theAcid;
		if (!gAcid.equals("ALL") && !gAcid.equals("BP") && !gAcid.equals("AA")) {
			printUsage();
		}
		gN = theN;
		gClientFile = theClientFile;
		gLinesClient = theLinesClient;
		gTasks = theTasks;
		gTotalProc = theThreads;
		if (theThreads == 1) {
			gExecutorService = Executors.newSingleThreadExecutor();
		} else {
			gExecutorService = Executors.newFixedThreadPool(theThreads);
		}
		gWrite = theWrite;
		gSensitiveN = 0;
		gContainsN = 0;
		if (gWrite) {
			try {
				File aSensitiveFile = new File(gClientFile + ".pri");
				File aNonSensitiveFile = new File(gClientFile + ".pub");
				gSensitiveWriter = new BufferedWriter(new FileWriter(
						aSensitiveFile));
				gNonSensitiveWriter = new BufferedWriter(new FileWriter(
						aNonSensitiveFile));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		gFilter = LongFastBloomFilter.getFilter(gN, gP, gOpt);
		runTest();
		if (gWrite) {
			try {
				gSensitiveWriter.close();
				gNonSensitiveWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void runTest()
	{
		double aAddT0 = 0, aAddT1 = 0;
		long aAddN = 0;
		double aContainT0 = 0, aContainT1 = 0;
		aAddT0 = System.currentTimeMillis();
		aAddN = loadBlackList();
		aAddT1 = System.currentTimeMillis();
		if (aAddN > 0) {
			long aLinesPerTask = gLinesClient / gTasks;
			aContainT0 = System.currentTimeMillis();
			for (int i = 0; i < gTasks - 1; i++) {
				gExecutorService.execute(new RunnableDnaBlock(i * aLinesPerTask
						* (S + 1), aLinesPerTask));
			}
			if (aLinesPerTask % gTasks == 0) {
				gExecutorService.execute(new RunnableDnaBlock((gTasks - 1)
						* aLinesPerTask * (S + 1), aLinesPerTask));
			} else {
				gExecutorService.execute(new RunnableDnaBlock(
						(gTasks - 1)* aLinesPerTask * (S + 1), 
						gLinesClient - (aLinesPerTask * (gTasks - 1))));
			}
			gExecutorService.shutdown();
			while (!gExecutorService.isTerminated()) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			aContainT1 = System.currentTimeMillis();
			//TS|DS|TA|N|P|K|M|Iti|Iop|Ith|G|PT|Cti|Cop|Cth|SS|PS
			System.out.println(gId + "|"
							+ gServerFile + "|"
							+ gAcid + "|"
							+ gN + "|"
							+ gP + "|"
							+ gFilter.getNumHashFunctions() + "|"
							+ gFilter.getBitSetSize() + "|"
							+ (aAddT1 - aAddT0) / 1000.0 + "|"
							+ aAddN + "|"
							+ (aAddN / ((aAddT1 - aAddT0) / 1000.0)) + "|"
							+ (aContainT1 - aContainT0) / 1000.0 + "|"
							+ gClientFile + "|"
							+ gTotalProc + "|"
							+ (aContainT1 - aContainT0)/ 1000.0 + "|"
							+ gContainsN + "|"
							+ (gContainsN / ((aContainT1 - aContainT0) / 1000.0)) + "|"
							+ gSensitiveN + "|"
							+ new Float(new Float(gSensitiveN)/ new Float(gContainsN)));
		}
	}

	public long loadBlackList()
	{
		long aAddN = 0;
		if (!gServerFile.equals("")) {
			try {
				String aLine;
				BufferedReader aFile = new BufferedReader(new FileReader(gServerFile));
				while ((aLine = aFile.readLine()) != null) {
					aAddN++;
					gFilter.add(aLine.getBytes());
				}
				aFile.close();
				return aAddN;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return aAddN;
	}

	public synchronized void addSensitive(long theSensitiveN)
	{
		gSensitiveN += theSensitiveN;
	}

	public synchronized void addContains(long theContainsN)
	{
		gContainsN += theContainsN;
	}

	public synchronized void writeSensitive(String theSequence)
	{
		try {
			gSensitiveWriter.write(theSequence);
			gSensitiveWriter.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void writeNonSensitive(String theSequence)
	{
		try {
			gNonSensitiveWriter.write(theSequence);
			gNonSensitiveWriter.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public class RunnableDnaBlock implements Runnable
	{

		private long		mOffset, mLines, mContainN, mSensitiveN;
		private MurmurHash	murmurHash;
		private long		mHash1, mHash2;
		private byte[]		mBytes;

		RunnableDnaBlock(long theOffset, long theLines)
		{
			mOffset = theOffset;
			mLines = theLines;
			mContainN = 0;
			mSensitiveN = 0;
			murmurHash = new MurmurHash();
			mHash1 = 0;
			mHash2 = 0;
		}

		public void run()
		{
			String aLine;
			if (!gClientFile.equals("")) {
				try {
					OptimizedRandomAccessFile aFile = new OptimizedRandomAccessFile(
							gClientFile, "r");
					aFile.seek(mOffset);
					if (gAcid.equals("ALL")) {
						while (mContainN < mLines) {
							if ((aLine = aFile.readLine()) != null) {
								processAll(aLine);
								mContainN++;
							} else {
								break;
							}
						}
					} else if (gAcid.equals("BP")) {
						while (mContainN < mLines) {
							if ((aLine = aFile.readLine()) != null) {
								processBp(aLine);
								mContainN++;
							} else {
								break;
							}
						}
					} else if (gAcid.equals("AA")) {
						while (mContainN < mLines) {
							if ((aLine = aFile.readLine()) != null) {
								processAa(aLine);
								mContainN++;
							} else {
								break;
							}
						}
					}
					aFile.close();
					addSensitive(mSensitiveN);
					addContains(mContainN);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		public void processAll(String theSequence)
		{
			mBytes = theSequence.getBytes();
			mHash1 = murmurHash.hash(mBytes, mBytes.length, 0);
			mHash2 = murmurHash.hash(mBytes, mBytes.length, mHash1);
			if (gFilter.contains(mBytes, mHash1, mHash2)) {
				mSensitiveN++;
				if (gWrite) {
					writeSensitive(theSequence);
				}
			} else {
				mBytes = translate(theSequence).getBytes();
				mHash1 = murmurHash.hash(mBytes, mBytes.length, 0);
				mHash2 = murmurHash.hash(mBytes, mBytes.length, mHash1);
				if (gFilter.contains(mBytes, mHash1, mHash2)) {
					mSensitiveN++;
					if (gWrite) {
						writeSensitive(theSequence);
					}
				} else {
					if (gWrite) {
						writeNonSensitive(theSequence);
					}
				}
			}
		}

		public void processBp(String theSequence)
		{
			mBytes = theSequence.getBytes();
			mHash1 = murmurHash.hash(mBytes, mBytes.length, 0);
			mHash2 = murmurHash.hash(mBytes, mBytes.length, mHash1);
			if (gFilter.contains(mBytes, mHash1, mHash2)) {
				mSensitiveN++;
				if (gWrite) {
					writeSensitive(theSequence);
				}
			} else {
				if (gWrite) {
					writeNonSensitive(theSequence);
				}
			}
		}

		public void processAa(String theSequence)
		{
			mBytes = translate(theSequence).getBytes();
			mHash1 = murmurHash.hash(mBytes, mBytes.length, 0);
			mHash2 = murmurHash.hash(mBytes, mBytes.length, mHash1);
			if (gFilter.contains(mBytes, mHash1, mHash2)) {
				mSensitiveN++;
				if (gWrite) {
					writeSensitive(theSequence);
				}
			} else {
				if (gWrite) {
					writeNonSensitive(theSequence);
				}
			}
		}
	}

	private String translate(String theNucleotideSeq)
	{
		String aReturn = "";
		theNucleotideSeq = theNucleotideSeq.toUpperCase();
		int aSize = theNucleotideSeq.length();
		for (int i = 0; i < aSize; i += 3) {
			aReturn = aReturn
					+ (convert(theNucleotideSeq.substring(i,
							Math.min(i + 3, aSize))));
		}
		return aReturn;
	}

	private String convert(String theTriplet)
	{
		if (theTriplet.length() == 3) {
			switch (theTriplet.charAt(0))
			{
				case 'A':
					switch (theTriplet.charAt(1))
					{
						case 'A':
							switch (theTriplet.charAt(2))
							{
								case 'C':
								case 'T':
									return "N";
								case 'A':
								case 'G':
									return "K";
							}
							break;
						case 'C':
							return "T";
						case 'G':
							switch (theTriplet.charAt(2))
							{
								case 'C':
								case 'T':
									return "S";
								case 'A':
								case 'G':
									return "R";
							}
							break;
						case 'T':
							switch (theTriplet.charAt(2))
							{
								case 'A':
								case 'C':
								case 'T':
									return "I";
								case 'G':
									return "M";
							}
							break;
					}
				case 'C':
					switch (theTriplet.charAt(1))
					{
						case 'A':
							switch (theTriplet.charAt(2))
							{
								case 'C':
								case 'T':
									return "H";
								case 'A':
								case 'G':
									return "Q";
							}
							break;
						case 'C':
							return "P";
						case 'G':
							return "R";
						case 'T':
							return "L";
					}
					break;
				case 'G':
					switch (theTriplet.charAt(1))
					{
						case 'A':
							switch (theTriplet.charAt(2))
							{
								case 'C':
								case 'T':
									return "D";
								case 'A':
								case 'G':
									return "E";
							}
							break;
						case 'C':
							return "A";
						case 'G':
							return "G";
						case 'T':
							return "V";
					}
					break;
				case 'T':
					switch (theTriplet.charAt(1))
					{
						case 'A':
							switch (theTriplet.charAt(2))
							{
								case 'C':
								case 'T':
									return "Y";
								case 'A':
								case 'G':
									return "*";
							}
							break;
						case 'C':
							return "S";
						case 'G':
							switch (theTriplet.charAt(2))
							{
								case 'C':
								case 'T':
									return "C";
								case 'A':
									return "*";
								case 'G':
									return "W";
							}
							break;
						case 'T':
							switch (theTriplet.charAt(2))
							{
								case 'C':
								case 'T':
									return "F";
								case 'A':
								case 'G':
									return "L";
							}
							break;
					}
					break;
			}
		}
		return "";
	}

	private static void printUsage()
	{
		System.out
				.println("usage: \n\tjava -cp \".:long_bloomfilter.jar\" long_bloomfilter.MainDetector <p> <performance_optimization> <input_server_file> <acid_type> <n> <input_client_file> <lines_client> <tasks> <threads> <write>");
		System.out.println("options:");
		System.out.println("\tp: the false positive probability");
		System.out
				.println("\tperformance_optimization: the limits of performance optimization");
		System.out
				.println("\tinput_server_file: the input file for filling the BloomFilter");
		System.out.println("\tacid_type: the acid type (BP, AA or ALL)");
		System.out
				.println("\tn: the expected number of elements in server file");
		System.out.println("\tinput_client_file: the input file for testing");
		System.out
				.println("\tlines_client: the number of lines in the client file");
		System.out.println("\ttasks: the number of tasks to be created");
		System.out.println("\tthreads: the number of threads running the test");
		System.out
				.println("\twrite: true for writing the output in the respective files");
		System.exit(1);
	}
	

	public static void main(String[] args)
	{
		if (args.length == 10) {
			MainDetector aMain = new MainDetector(Double.parseDouble(args[0]),
					Double.parseDouble(args[1]), args[2], args[3],
					Long.parseLong(args[4]), args[5], Long.parseLong(args[6]),
					Long.parseLong(args[7]), Integer.parseInt(args[8]),
					Boolean.parseBoolean(args[9]));
		} else {
			printUsage();
		}
	}
}
