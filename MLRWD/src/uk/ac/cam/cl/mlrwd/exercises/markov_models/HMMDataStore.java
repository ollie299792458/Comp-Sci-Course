package uk.ac.cam.cl.mlrwd.exercises.markov_models;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class HMMDataStore<T, U> {

	public List<T> observedSequence;
	public List<U> hiddenSequence;

	public HMMDataStore(List<T> observed, List<U> hidden) {
		this.observedSequence = observed;
		this.hiddenSequence = hidden;
	}

	/**
	 * Loads a file with a dice roll sequence.
	 * 
	 * @param sequenceFile
	 *            A file with the dice sequence.
	 * @return {@link HMMDataStore}<{@link DiceRoll}, {@link DiceType}> A
	 *         matched pair of observed and hidden sequences.
	 * @throws IOException
	 */
	public static HMMDataStore<DiceRoll, DiceType> loadDiceFile(Path sequenceFile) throws IOException {
		List<DiceRoll> rolls;
		List<DiceType> dice;
		try (BufferedReader reader = Files.newBufferedReader(sequenceFile)) {
			rolls = reader.readLine().chars().mapToObj(i -> DiceRoll.valueOf((char) i)).collect(Collectors.toList());
			dice = reader.readLine().chars().mapToObj(i -> DiceType.valueOf((char) i)).collect(Collectors.toList());
		} catch (IOException e) {
			throw new IOException("Can't load the data file.", e);
		}
		return new HMMDataStore<DiceRoll, DiceType>(rolls, dice);
	}

	/**
	 * Loads multiple files with dice sequences.
	 * 
	 * @param sequenceFiles
	 *            {@link Collection}<{@link Path}> Files with dice sequences.
	 * @return {@link List}<{@link HMMDataStore}<{@link DiceRoll},
	 *         {@link DiceType}>> A list of matched observed-hidden sequence
	 *         pairs.
	 * @throws IOException
	 */
	public static List<HMMDataStore<DiceRoll, DiceType>> loadDiceFiles(Collection<Path> sequenceFiles)
			throws IOException {
		List<HMMDataStore<DiceRoll, DiceType>> data = new ArrayList<HMMDataStore<DiceRoll, DiceType>>();
		for (Path p : sequenceFiles) {
			data.add(loadDiceFile(p));
		}
		return data;
	}
}
