package kaba4cow.examples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import kaba4cow.prefixtree.PrefixTree;

public class PrefixTreeExample {

	private final PrefixTree prefixTree;

	public PrefixTreeExample() throws IOException {
		prefixTree = new PrefixTree().ignoreCase(true);

		loadWords("kaba4cow/examples/lorem_ipsum");

		printWordsWithPrefix("l");
		printWordsWithPrefix("lo");
		printWordsWithPrefix("al");
	}

	private void loadWords(String path) throws IOException {
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(getClass().getClassLoader().getResourceAsStream(path)));
		String line;
		while ((line = reader.readLine()) != null)
			prefixTree.addWords(line.split(" "));
		reader.close();
	}

	public void printWordsWithPrefix(String prefix) {
		String[] words = prefixTree.matchPrefix(prefix);
		System.out.printf("%d words starting with \"%s\":\n", words.length, prefix);
		for (String word : words)
			System.out.printf("    [%s]%s\n", prefix, word.substring(prefix.length()));
		System.out.println();
	}

	public static void main(String[] args) throws IOException {
		new PrefixTreeExample();
	}

}
