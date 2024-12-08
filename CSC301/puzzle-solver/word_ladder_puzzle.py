from puzzle import Puzzle


class WordLadderPuzzle(Puzzle):
    """
    A word-ladder puzzle that may be solved, unsolved, or even unsolvable.
    """

    def __init__(self, from_word, to_word, ws):
        """
        Create a new word-ladder puzzle with the aim of stepping
        from from_word to to_word using words in ws, changing one
        character at each step.

        @type from_word: str
        @type to_word: str
        @type ws: set[str]
        @rtype: None
        """
        (self._from_word, self._to_word, self._word_set) = (from_word,
                                                            to_word, ws)
        # set of characters to use for 1-character changes
        self._chars = "abcdefghijklmnopqrstuvwxyz"

    def __eq__(self, other):
        """
        Return whether WordLadderPuzzle self is equivalent to other.

        @type self: WordLadderPuzzle
        @type other: WordLadderPuzzle | Any
        @rtype: bool

        >>> with open("words", "r") as words:
        ...     word_set = set(words.read().split())
        >>> w = WordLadderPuzzle("same", "cost", word_set)
        >>> q = WordLadderPuzzle("same", "cost", word_set)
        >>> w == q
        True
        >>> j = WordLadderPuzzle("magic", "cost", word_set)
        >>> j == w
        False
        """
        return self._from_word == other._from_word and \
            self._to_word == other._to_word and \
            self._word_set == other._word_set

    def __str__(self):
        """
        Return a human-readable string representation of WordLadderPuzzle self.

        >>> with open("words", "r") as words:
        ...     word_set = set(words.read().split())
        >>> w = WordLadderPuzzle("same", "cost", word_set)
        >>> print(w)
        from word: same , to word: cost
        """
        return "from word: {} , to word: {}" \
            .format(self._from_word, self._to_word)

    def extensions(self):
        """
        Return list of extensions of WordLadderPuzzle self.

        @type self: WordLadderPuzzle
        @rtype: list[WordLadderPuzzle]
        # legal extensions are WordLadderPuzzles that have a from_word that can
        # be reached from this one by changing a single letter to one of those
        # in self._chars

        >>> test_puzzle = WordLadderPuzzle('cost', 'lost', {'lost', 'cast',
        ...      'cost', 'happy', 'sappy', 'nappy', 'boss', 'floss'})
        >>> extension_1 = WordLadderPuzzle('cast', 'lost', {'lost', 'cast',
        ...      'cost', 'happy', 'sappy', 'nappy', 'boss', 'floss'})
        >>> extension_1 in test_puzzle.extensions()
        True
        """
        word_configurations, chars, word = \
            [], list(self._chars), list(self._from_word)

        # Get every possible word configuration that can be created
        # by replacing one letter
        index = -1
        for character in word:
            word = list(self._from_word)
            index += 1
            for letter in chars:
                word[index] = letter
                word_configurations.append(''.join(word))

        # Set intersection will give words which are common to
        # the word set and word_configurations (gets real words)
        possible_words = set(word_configurations) & self._word_set

        # Using possible words,create new WordLadderPuzzle extensions
        word_ladder_extensions = []
        for word in possible_words:
            if word != self._from_word:
                new_puzzle = \
                    WordLadderPuzzle(word, self._to_word, self._word_set)
                word_ladder_extensions.append(new_puzzle)
        return word_ladder_extensions

    def is_solved(self):
        """
        Return whether WordLadderPuzzle self is solved.

        @type self: WordLadderPuzzle
        @rtype: bool

        >>> with open("words", "r") as words:
        ...     word_set = set(words.read().split())
        >>> w = WordLadderPuzzle("same", "cost", word_set)
        >>> w.is_solved()
        False
        >>> j = WordLadderPuzzle("magic", "magic", word_set)
        >>> j.is_solved()
        True
        """
        return self._from_word == self._to_word


if __name__ == '__main__':
    import doctest
    doctest.testmod()
    from puzzle_tools import breadth_first_solve, depth_first_solve
    from time import time
    with open("words", "r") as words:
        word_set = set(words.read().split())
    w = WordLadderPuzzle("same", "cost", word_set)

    start = time()
    sol = breadth_first_solve(w)
    end = time()
    print("Solving word ladder from same->cost")
    print("...using breadth-first-search")
    print("Solutions: {} took {} seconds.".format(sol, end - start))
    start = time()
    sol = depth_first_solve(w)
    end = time()
    print("Solving word ladder from same->cost")
    print("...using depth-first-search")
    print("Solutions: {} took {} seconds.".format(sol, end - start))
