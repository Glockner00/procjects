from typing import List, Tuple

# Define the Maze class
class Maze:
    def __init__(self, maze: List[List[int]]):
        self.maze = maze
        self.num_rows = len(maze)
        self.num_cols = len(maze[0])

    def is_valid_location(self, row: int, col: int) -> bool:
        """Return True if the location is valid and not a wall."""
        return 0 <= row < self.num_rows and 0 <= col < self.num_cols and self.maze[row][col] != 1

    def get_neighbors(self, row: int, col: int) -> List[Tuple[int]]:
        """Return a list of valid neighboring locations."""
        neighbors = []
        if self.is_valid_location(row-1, col):
            neighbors.append((row-1, col))
        if self.is_valid_location(row+1, col):
            neighbors.append((row+1, col))
        if self.is_valid_location(row, col-1):
            neighbors.append((row, col-1))
        if self.is_valid_location(row, col+1):
            neighbors.append((row, col+1))
        return neighbors


# Define the SearchNode class
class SearchNode:
    def __init__(self, row: int, col: int, parent=None):
        self.row = row
        self.col = col
        self.parent = parent

    def get_path(self) -> List[Tuple[int]]:
        """Return the path from the start node to this node."""
        path = [(self.row, self.col)]
        while self.parent:
            path.append((self.parent.row, self.parent.col))
            self = self.parent
        return list(reversed(path))


# Define the search function
def dfs(maze: Maze, start: Tuple[int], goal: Tuple[int]) -> List[Tuple[int]]:
    """Return the path from start to goal using Depth First Search."""
    start_node = SearchNode(start[0], start[1])
    visited = set()
    stack = [start_node]

    while stack:
        node = stack.pop()
        if (node.row, node.col) == goal:
            return node.get_path()

        if (node.row, node.col) not in visited:
            visited.add((node.row, node.col))
            for neighbor in maze.get_neighbors(node.row, node.col):
                neighbor_node = SearchNode(neighbor[0], neighbor[1], parent=node)
                stack.append(neighbor_node)

    return []


# Define the main function to read input and call search function
def main():
    maze_input = input("Enter the maze as a list of lists, with 0 representing a clear path and 1 representing a wall:\n")
    maze = Maze(eval(maze_input))
    start_input = input("Enter the starting position as a tuple (row, col):\n")
    start = eval(start_input)
    goal_input = input("Enter the goal position as a tuple (row, col):\n")
    goal = eval(goal_input)
    path = dfs(maze, start, goal)
    print(f"The path from {start} to {goal} is: {path}")


if __name__ == '__main__':
    main()
