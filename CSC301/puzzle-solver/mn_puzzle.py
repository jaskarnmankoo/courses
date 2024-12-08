from puzzle import Puzzle
from copy import deepcopy


class MNPuzzle(Puzzle):
    """
    An nxm puzzle, like the 15-puzzle, which may be solved, unsolved,
    or even unsolvable.
    """

    def __init__(self, from_grid, to_grid):
        """
        MNPuzzle in state from_grid, working towards
        state to_grid

        @param MNPuzzle self: this MNPuzzle
        @param tuple[tuple[str]] from_grid: current configuration
        @param tuple[tuple[str]] to_grid: solution configuration
        @rtype: None
        """
        # represent grid symbols with letters or numerals
        # represent the empty space with a "*"
        assert len(from_grid) > 0
        assert all([len(r) == len(from_grid[0]) for r in from_grid])
        assert all([len(r) == len(to_grid[0]) for r in to_grid])
        self.n, self.m = len(from_grid), len(from_grid[0])
        self.from_grid, self.to_grid = from_grid, to_grid

    def __eq__(self, other):
        """
        Return whether MNPuzzle self is equivalent to other.

        @type self: MNPuzzle
        @type other: MNPuzzle | Any
        @rtype: bool

        >>> target_grid = (("1", "2", "3"), ("4", "5", "*"))
        >>> start_grid = (("*", "2", "3"), ("1", "4", "5"))
        >>> p1 = MNPuzzle(start_grid, target_grid)
        >>> p2 = MNPuzzle(start_grid, target_grid)
        >>> another_target_grid = (("1", "2", "3"), ("4", "*", "5"))
        >>> another_start_grid = (("2", "*", "3"), ("1", "4", "5"))
        >>> p3 = MNPuzzle(another_start_grid, another_target_grid)
        >>> p1 == p2
        True
        >>> p1 == p3
        False
        """

        return self.from_grid == other.from_grid and \
            self.to_grid == other.to_grid

    def __str__(self):
        """
        Return a human-readable string representation of MNPuzzle self.

        >>> target_grid = (("1", "2", "3"), ("4", "5", "*"))
        >>> start_grid = (("*", "2", "3"), ("1", "4", "5"))
        >>> p1 = MNPuzzle(start_grid, target_grid)
        >>> print(p1)
        * 2 3
        1 4 5
        <BLANKLINE>
        """

        mn_puzzle_picture = ''
        for line in self.from_grid:
            for number in line:
                mn_puzzle_picture += number + " "
            mn_puzzle_picture += '\n'
        return mn_puzzle_picture

    def extensions(self):
        """
        Return list of extensions of MNPuzzle self.
        legal extensions are configurations that can be reached by swapping one
        symbol to the left, right, above, or below "*" with "*"

        @type self: MNPuzzle
        @rtype: list[MNPuzzle]

        >>> target_grid = (("1", "2", "3"), ("4", "5", "*"))
        >>> start_grid = (("*", "2", "3"), ("1", "4", "5"))
        >>> puzzle = MNPuzzle(start_grid, target_grid)
        >>> L1 = puzzle.extensions()
        >>> ext_1 = MNPuzzle((("1", "2", "3"), ("*", "4", "5")), target_grid)
        >>> ext_2 = MNPuzzle((("2", "*", "3"), ("1", "4", "5")), target_grid)
        >>> ext_1 in L1 and ext_2 in L1
        True
        """

        # Get the index of the '*' in the MNPuzzle Grid
        grid_list = [list(i) for i in self.from_grid]

        a = -1
        for line in grid_list:
            a += 1
            i = -1
            for value in line:
                i += 1
                if value == "*":
                    empty_index = a, i
        row, col = empty_index

        # Collect all of the extension grids in list form
        list_of_grids = []

        # Appends extension for right switch if it's possible
        grid = deepcopy(grid_list)
        if col + 1 < len(grid[row]):
            grid[row][col], grid[row][col + 1] = \
                grid[row][col + 1], grid[row][col]
            list_of_grids.append(grid)

        # Appends extension for left switch if it's possible
        grid = deepcopy(grid_list)
        if col - 1 >= 0:
            grid[row][col], grid[row][col - 1] = \
                grid[row][col - 1], grid[row][col]
            list_of_grids.append(grid)

        # Appends extension for down switch if it's possible
        grid = deepcopy(grid_list)
        if row - 1 >= 0:
            grid[row][col], grid[row - 1][col] = \
                grid[row - 1][col], grid[row][col]
            list_of_grids.append(grid)

        # Appends extension for up switch if it's possible
        grid = deepcopy(grid_list)
        if row + 1 < len(grid):
            grid[row][col], grid[row + 1][col] = \
                grid[row + 1][col], grid[row][col]
            list_of_grids.append(grid)

        # Converts each list in list_of_grids to a tuple and uses that tuple
        # to create a new MNPuzzle for the list of extensions
        list_of_puzzles = []
        for grid in list_of_grids:
            grid_tuple = tuple(tuple(i) for i in grid)
            if grid != self.from_grid:
                list_of_puzzles.append(MNPuzzle(grid_tuple, self.to_grid))
        return list_of_puzzles

    def is_solved(self):
        """
        Return whether MNPuzzle self is solved.

        @type self: MNPuzzle
        @return: bool

        >>> target_grid = (("1", "2", "3"), ("4", "5", "*"))
        >>> start_grid = (("*", "2", "3"), ("1", "4", "5"))
        >>> p1 = MNPuzzle(start_grid, target_grid)
        >>> p1.is_solved()
        False
        >>> another_start_grid = (("1", "2", "3"), ("4", "5", "*"))
        >>> p2 = MNPuzzle(another_start_grid, target_grid)
        >>> p2.is_solved()
        True
        """

        return self.from_grid == self.to_grid


if __name__ == "__main__":
    import doctest
    doctest.testmod()
    target_grid = (("1", "2", "3"), ("4", "5", "*"))
    start_grid = (("*", "2", "3"), ("1", "4", "5"))
    from puzzle_tools import breadth_first_solve, depth_first_solve
    from time import time
    start = time()
    solution = breadth_first_solve(MNPuzzle(start_grid, target_grid))
    end = time()
    print("BFS solved: \n\n{} \n\nin {} seconds".format(
        solution, end - start))
    start = time()
    solution = depth_first_solve((MNPuzzle(start_grid, target_grid)))
    end = time()
    print("DFS solved: \n\n{} \n\nin {} seconds".format(
        solution, end - start))
