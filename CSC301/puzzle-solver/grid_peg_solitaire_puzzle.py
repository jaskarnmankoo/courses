from puzzle import Puzzle
from copy import deepcopy


class GridPegSolitairePuzzle(Puzzle):
    """
    Snapshot of peg solitaire on a rectangular grid. May be solved,
    unsolved, or even unsolvable.
    """

    def __init__(self, marker, marker_set):
        """
        Create a new GridPegSolitairePuzzle self with
        marker indicating pegs, spaces, and unused
        and marker_set indicating allowed markers.

        @type marker: list[list[str]]
        @type marker_set: set[str]
                          "#" for unused, "*" for peg, "." for empty
        """
        assert isinstance(marker, list)
        assert len(marker) > 0
        assert all([len(x) == len(marker[0]) for x in marker[1:]])
        assert all([all(x in marker_set for x in row) for row in marker])
        assert all([x == "*" or x == "." or x == "#" for x in marker_set])
        self._marker, self._marker_set = marker, marker_set

    def __eq__(self, other):
        """
        Return whether GridPegSolitairePuzzle self is equivalent to other.

        @type self: GridPegSolitairePuzzle
        @type other: GridPegSolitairePuzzle | Any
        @rtype: bool

        >>> grid = [["*", "*", "*", "*", "*"],
        ... ["*", "*", "*", "*", "*"],
        ... ["*", "*", "*", "*", "*"],
        ... ["*", "*", ".", "*", "*"],
        ... ["*", "*", "*", "*", "*"]]
        >>> another_grid = [["*", "*", "*", "*", "*"],
        ... ["*", "*", "*", "*", "*"],
        ... ["*", "*", ".", "*", "*"],
        ... ["*", "*", "*", "*", "*"],
        ... ["*", "*", "*", "*", "*"]]
        >>> p1 = GridPegSolitairePuzzle(grid, {"*", ".", "#"})
        >>> p2 = GridPegSolitairePuzzle(grid, {"*", ".", "#"})
        >>> p3 = GridPegSolitairePuzzle(another_grid, {"*", ".", "#"})
        >>> p1 == p2
        True
        >>> p1 == p3
        False
        """
        return self._marker == other._marker and \
            self._marker_set == other._marker_set

    def __str__(self):
        """
        Return a string representation of GridPegSolitairePuzzle object.

        >>> grid = [["*", "*", "*", "*", "*"],
        ... ["*", "*", "*", "*", "*"],
        ... ["*", "*", "*", "*", "*"],
        ... ["*", "*", ".", "*", "*"],
        ... ["*", "*", "*", "*", "*"]]
        >>> p1 = GridPegSolitairePuzzle(grid, {"*", ".", "#"})
        >>> print(p1)
        ['*', '*', '*', '*', '*']
        ['*', '*', '*', '*', '*']
        ['*', '*', '*', '*', '*']
        ['*', '*', '.', '*', '*']
        ['*', '*', '*', '*', '*']
        <BLANKLINE>
        """
        peg_picture = ''
        i = -1
        for peg_line in self._marker:
            i += 1
            if i < len(self._marker) - 1:
                peg_picture += str(peg_line) + '\n'
            else:
                peg_picture += str(peg_line)
        return peg_picture + "\n"

    def extensions(self):
        """
        Return list of extensions of GridPegSolitairePuzzle self.

        @type self: GridPegSolitairePuzzle
        @rtype: list[GridPegSolitairePuzzle]

        >>> grid = [["*", "*", "*", "*", "*"],
        ... ["*", "*", "*", "*", "*"],
        ... ["*", "*", "*", "*", "*"],
        ... ["*", "*", ".", "*", "*"],
        ... ["*", "*", "*", "*", "*"]]
        >>> p1 = GridPegSolitairePuzzle(grid, {"*", ".", "#"})
        >>> L1 = p1.extensions()
        >>> len(L1)
        3
        >>> grid[3] = [".", ".", "*", "*", "*"]
        >>> L2 = GridPegSolitairePuzzle(grid, {"*", ".", "#"})
        >>> L2 in L1
        True
        """
        left = self.get_jump_indexes('left')
        right = self.get_jump_indexes('right')
        down = self.get_jump_indexes('down')
        up = self.get_jump_indexes('up')
        list_of_puzzles = []

        for index in right:
            row, col = index[0], index[1]
            markers = deepcopy(self._marker)
            new_puzzle = GridPegSolitairePuzzle(markers, self._marker_set)
            new_puzzle._marker[row][col] = "."
            new_puzzle._marker[row][col + 1] = "."
            new_puzzle._marker[row][col + 2] = "*"
            list_of_puzzles.append(new_puzzle)

        for index in left:
            row, col = index[0], index[1]
            markers = deepcopy(self._marker)
            new_puzzle = GridPegSolitairePuzzle(markers, self._marker_set)
            new_puzzle._marker[row][col] = "."
            new_puzzle._marker[row][col - 1] = "."
            new_puzzle._marker[row][col - 2] = "*"
            list_of_puzzles.append(new_puzzle)

        for index in down:
            row, col = index[0], index[1]
            markers = deepcopy(self._marker)
            new_puzzle = GridPegSolitairePuzzle(markers, self._marker_set)
            new_puzzle._marker[row][col] = "."
            new_puzzle._marker[row + 1][col] = "."
            new_puzzle._marker[row + 2][col] = "*"
            list_of_puzzles.append(new_puzzle)

        for index in up:
            row, col = index[0], index[1]
            markers = deepcopy(self._marker)
            new_puzzle = GridPegSolitairePuzzle(markers, self._marker_set)
            new_puzzle._marker[row][col] = "."
            new_puzzle._marker[row - 1][col] = "."
            new_puzzle._marker[row - 2][col] = "*"
            list_of_puzzles.append(new_puzzle)

        return list_of_puzzles

    def get_jump_indexes(self, direction):
        """
        Helper method for getting jump indexes
        @return: Tuple ( , , , ,) containing 4 lists of jump indexes
        for each direction
        """
        jump_indexes = []
        row = -1
        for peg_line in self._marker:
            row += 1
            col = -1
            for peg in peg_line:
                col += 1
                if peg == '*':
                    if direction == 'left':
                        if (col - 2 > 0) and \
                            self._marker[row][col - 2] == '.' \
                                and self._marker[row][col - 1] == '*':
                            jump_indexes.append((row, col))

                    if direction == 'right':
                        if (col + 2) < len(peg_line) and \
                            self._marker[row][col + 2] == '.' and \
                                self._marker[row][col + 1] == '*':
                            jump_indexes.append((row, col))

                    if direction == 'down':
                        if (row + 2) < len(self._marker) and \
                            self._marker[row + 2][col] == '.' and \
                                self._marker[row + 1][col] == "*":
                            jump_indexes.append((row, col))

                    if direction == 'up':
                        if (row - 2) > 0 and \
                            self._marker[row - 2][col] == '.' \
                                and self._marker[row - 1][col] == '*':
                            jump_indexes.append((row, col))

        return jump_indexes

    def is_solved(self):
        """
        Return whether GridPegSolitairePuzzle self is solved.

        @type self: GridPegSolitairePuzzle
        @rtype: bool

        >>> grid = [["*", "*", "*", "*", "*"],
        ... ["*", "*", "*", "*", "*"],
        ... ["*", "*", "*", "*", "*"],
        ... ["*", "*", ".", "*", "*"],
        ... ["*", "*", "*", "*", "*"]]
        >>> p1 = GridPegSolitairePuzzle(grid, {"*", ".", "#"})
        >>> p1.is_solved()
        False
        >>> another_grid = [[".", ".", ".", ".", "."],
        ... [".", ".", ".", ".", "."],
        ... [".", ".", ".", ".", "."],
        ... [".", ".", ".", ".", "."],
        ... [".", ".", ".", ".", "."]]
        >>> p2 = GridPegSolitairePuzzle(grid, {"*", ".", "#"})
        """
        peg_count = 0
        for peg_line in self._marker:
            for peg in peg_line:
                if peg == "*":
                    peg_count += 1
        return peg_count == 1


if __name__ == "__main__":
    import doctest

    doctest.testmod()
    from puzzle_tools import depth_first_solve

    grid = [["*", "*", "*", "*", "*"],
            ["*", "*", "*", "*", "*"],
            ["*", "*", "*", "*", "*"],
            ["*", "*", ".", "*", "*"],
            ["*", "*", "*", "*", "*"]]
    gpsp = GridPegSolitairePuzzle(grid, {"*", ".", "#"})
    import time

    start = time.time()
    solution = depth_first_solve(gpsp)
    end = time.time()
    print("Solved 5x5 peg solitaire in {} seconds.".format(end - start))
    print("Using depth-first: \n{}".format(solution))
