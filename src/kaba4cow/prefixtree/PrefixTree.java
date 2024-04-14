package kaba4cow.prefixtree;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * A PrefixTree (Trie) data structure for efficient storage and retrieval of
 * words.
 *
 * @version 1.0
 * @author Yaroslav
 */
public class PrefixTree {

	private final LinkedHashMap<Character, Integer> charset;
	private final Node root;

	private boolean ignoreCase;

	/**
	 * Constructs an empty PrefixTree.
	 */
	public PrefixTree() {
		charset = new LinkedHashMap<>();
		root = new Node();
		ignoreCase = true;
	}

	/**
	 * Checks if the PrefixTree contains a given word.
	 *
	 * @param word the word to check.
	 * @return {@code true} if the word is found, {@code false} otherwise.
	 */
	public boolean containsWord(String word) {
		if (word == null)
			return false;
		return root.containsWord(word);
	}

	/**
	 * Returns an array of words that match the given prefix.
	 *
	 * @param prefix the prefix to match.
	 * @return an array of matching words.
	 * @throws NullPointerException if the prefix is null.
	 */
	public String[] matchPrefix(String prefix) {
		if (prefix == null)
			throw new NullPointerException("Prefix cannot be null");
		ArrayList<String> list = new ArrayList<>();
		root.matchPrefix(list, prefix.toCharArray(), 0);
		return list.toArray(new String[list.size()]);
	}

	/**
	 * Adds multiple words to the PrefixTree.
	 *
	 * @param words the words to add.
	 * @return a reference to this object.
	 */
	public PrefixTree addWords(String... words) {
		for (int i = 0; i < words.length; i++)
			addWord(words[i]);
		return this;
	}

	/**
	 * Adds a word to the PrefixTree.
	 *
	 * @param word the word to add.
	 * @return a reference to this object.
	 */
	public PrefixTree addWord(String word) {
		if (word == null)
			return this;
		if (ignoreCase)
			word = word.toLowerCase();
		char[] characters = word.toCharArray();
		for (int j = 0; j < characters.length; j++)
			if (!charset.containsKey(characters[j]))
				charset.put(characters[j], charset.size());
		root.addWord(word, characters, 0);
		return this;
	}

	/**
	 * Clears the PrefixTree.
	 *
	 * @return a reference to this object.
	 */
	public PrefixTree clearTree() {
		charset.clear();
		root.children.clear();
		root.word = null;
		return this;
	}

	/**
	 * Sets whether the PrefixTree should convert the input words to lower case when
	 * adding them to the tree.
	 *
	 * @param ignoreCase {@code true} to ignore case, {@code false} otherwise.
	 * @return a reference to this object.
	 */
	public PrefixTree ignoreCase(boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
		return this;
	}

	/**
	 * Represents a node in the PrefixTree.
	 */
	private class Node {

		private final LinkedHashMap<Character, Node> children;
		private String word;

		/**
		 * Constructs an empty Node.
		 */
		public Node() {
			children = new LinkedHashMap<>();
			word = null;
		}

		/**
		 * Recursively matches words with the given prefix.
		 *
		 * @param words            the list of matching words.
		 * @param prefixCharacters the characters of the prefix.
		 * @param prefixIndex      the current index in the prefix.
		 */
		public void matchPrefix(ArrayList<String> words, char[] prefixCharacters, int prefixIndex) {
			if (prefixIndex >= prefixCharacters.length) {
				if (word != null)
					words.add(word);
				for (Character character : children.keySet())
					children.get(character).matchPrefix(words, prefixCharacters, prefixIndex);
			} else {
				Character character = prefixCharacters[prefixIndex];
				if (children.containsKey(character))
					children.get(character).matchPrefix(words, prefixCharacters, prefixIndex + 1);
			}
		}

		/**
		 * Recursively adds a word to the PrefixTree.
		 *
		 * @param original       the original word being added.
		 * @param wordCharacters the characters of the word.
		 * @param wordIndex      the current index in the word.
		 */
		public void addWord(String original, char[] wordCharacters, int wordIndex) {
			if (wordIndex >= wordCharacters.length) {
				if (word == null)
					word = original;
				return;
			}
			Character character = wordCharacters[wordIndex];
			Integer index = charset.get(character);
			if (index == null)
				addWord(original, wordCharacters, wordIndex + 1);
			Node child = children.get(character);
			if (!children.containsKey(character)) {
				child = new Node();
				children.put(character, child);
			}
			child.addWord(original, wordCharacters, wordIndex + 1);
		}

		/**
		 * Checks if the Node or its children contain a given word.
		 *
		 * @param word the word to check.
		 * @return {@code true} if the word is found, {@code false} otherwise.
		 */
		public boolean containsWord(String word) {
			if (word != null && word.equals(word))
				return true;
			for (Character character : children.keySet())
				if (children.get(character).containsWord(word))
					return true;
			return false;
		}

	}

}
