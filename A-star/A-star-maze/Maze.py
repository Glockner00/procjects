import random

class Cell:
    def __init__(self, x, y):
        self.x = x
        self.y = y
        self.visited = False
        self.walls = [True, True, True, True] #top, right, bottom, left
        self.start = False
        self.end = False


def generateMaze(w: int, h: int) -> list:
    maze = [[Cell(x,y) for y in range(h)] for x in range(w)] # a 2D array filled with cells
    currentCell = maze[random.randint(0, w-1)][random.randint(0, h-1)] # A random starting point
    currentCell.visited = True 
    visitedCells = [currentCell] # a stack with all visited cells
    
    while visitedCells:  # while there are unvisited cells
        currentCell = visitedCells.pop()  # Pop the last cell

        neighbors = getUnvisitedNeighbors(maze, currentCell) #check the neigbor cells
        if neighbors:
            neighbor = random.choice(neighbors)  # A random neighbor
            removeWall(currentCell, neighbor)  # remove the wall between the cells
            
            # The neibor cells is visited and added to the stack. 
            neighbor.visited = True 
            visitedCells.append(currentCell)
            visitedCells.append(neighbor)

    return maze

def removeWall(current: Cell, choice: Cell):
    if current.x == choice.x:
        if current.y>choice.y:
            current.walls[0] = False
            choice.walls[2] = False
        else:
            current.walls[2] = False
            choice.walls[0] = False
    else:
        if current.x > choice.x:
            current.walls[3] = False
            choice.walls[1] = False
        else:
            current.walls[1] = False
            choice.walls[3] = False

def getUnvisitedNeighbors(maze: list, cell: Cell) -> list:
    neighbors = []
    x, y = cell.x, cell.y
    
    if x>0:
        neighbors.append(maze[x-1][y])
    if x<len(maze) - 1:
        neighbors.append(maze[x+1][y])
    if y>0:
        neighbors.append(maze[x][y-1])
    if y<len(maze[0]) - 1:
        neighbors.append(maze[x][y+1])

    # return neigbors that ar not visited yet. 
    return [neighbor for neighbor in neighbors if not neighbor.visited]

