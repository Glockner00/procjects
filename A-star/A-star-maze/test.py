import random
import pygame
RED = (255, 0, 0)
GREEN = (0, 255, 0)
BLUE = (28, 134, 238)
YELLOW = (255, 255, 0)
WHITE = (255, 255, 255)
BLACK = (0, 0, 0)
PURPLE = (128, 0, 128)
GREY = (128, 128, 128)

class Cell:
    def __init__(self, x, y):
        self.x = x
        self.y = y
        self.visited = False
        self.walls = [True, True, True, True] #top, right, bottom, left
        self.start = False
        self.end = False
        self.color = WHITE

    def makeOpen(self):
        self.color = GREEN

    # Check if neighbours have barriers between them or not and returns the neighbors that have not.
    def update_neighbours(self, maze: list) -> list:
        neighbors = []
        
        #LEFT 
        if self.x>0 and not (maze[self.x-1][self.y].walls[1] and maze[self.x][self.y].walls[3]):
            neighbors.append(maze[self.x-1][self.y])

        #RIGHT
        if self.x<len(maze) - 1 and not (maze[self.x+1][self.y].walls[3] and maze[self.x][self.y].walls[1]):
            neighbors.append(maze[self.x+1][self.y])
        
        # DOWN
        if self.y>0 and not (maze[self.x][self.y-1].walls[0] and maze[self.x][self.y].walls[2]):
            neighbors.append(maze[self.x][self.y-1])

        # UP
        if self.y<len(maze[0]) - 1 and not (maze[self.x][self.y+1].walls[2] and maze[self.x][self.y].walls[1]):
            neighbors.append(maze[self.x][self.y+1])

        return neighbors
    
    def draw(self, win):
        cellX, cellY = self.x * CELL_SIZE, self.y * CELL_SIZE
        pygame.draw.circle(win, YELLOW, (cellX + CELL_SIZE//2, cellY + CELL_SIZE //2), CELL_SIZE//4) 
        pygame.display.update()


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


CELL_SIZE = 35
CLOCK = pygame.time.Clock()


def makeStartEnd(maze: list):
    run = True
    while run:
        startX, startY = random.randint(0, len(maze) -1), random.randint(0, len(maze) - 1)
        endX, endY = random.randint(0, len(maze) -1), random.randint(0, len(maze) - 1)
        if maze[startX][startY] != maze[endX][endY]:
            maze[startX][startY].start = True
            maze[endX][endY].end = True
            run = False


def drawMaze(WIN, maze: list):
    WIN.fill(WHITE)
    for x in range(len(maze)):
        for y in range(len(maze[x])):
            cell = maze[x][y]
           
            cellX, cellY = x * CELL_SIZE, y * CELL_SIZE
            
            #TOP
            if cell.walls[0]:
                pygame.draw.line(WIN, BLACK, (cellX, cellY), (cellX + CELL_SIZE, cellY))
            
            #RIGHT
            if cell.walls[1]:
                pygame.draw.line(WIN, BLACK, (cellX + CELL_SIZE, cellY), (cellX + CELL_SIZE, cellY + CELL_SIZE))
            
            #BOTTOM
            if cell.walls[2]:
                pygame.draw.line(WIN, BLACK, (cellX, cellY + CELL_SIZE), (cellX + CELL_SIZE, cellY + CELL_SIZE))
            
            #LEFT
            if cell.walls[3]:
                pygame.draw.line(WIN, BLACK, (cellX , cellY), (cellX, cellY + CELL_SIZE))

            if cell.start: 
                pygame.draw.circle(WIN, GREEN, (cellX + CELL_SIZE//2, cellY + CELL_SIZE //2), CELL_SIZE//4)
            
            if cell.end:
                pygame.draw.circle(WIN, PURPLE, (cellX + CELL_SIZE//2, cellY + CELL_SIZE //2), CELL_SIZE//4) 

        pygame.display.update()


def h():
    pass


def drawAlgo(maze: list, neighbors: list, win):
    for row in maze:
        for cell in row:
            if cell in neighbors:
                cell.makeOpen()
                cell.draw(win)
def main():
    print("MAZE DIMENSIONS")
    print("===============")
    x = int(input("x: "))
    y = int(input("y: "))

    HEIGHT = x * CELL_SIZE
    WIDTH = y * CELL_SIZE
    WIN = pygame.display.set_mode((WIDTH, HEIGHT))

    maze = generateMaze(x, y)
    makeStartEnd(maze)

    pygame.init()
    run = True
    while run: 
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                run = False


        drawMaze(WIN, maze)
            # TEST
        for x in range(len(maze)):
            for y in range(len(maze[x])):
                cell = maze[x][y]
                if cell.start:
                    neighbors = cell.update_neighbours(maze)
                    print(neighbors[0].x)
                    print(cell.x)
                    drawAlgo(maze, neighbors, WIN)   

        CLOCK.tick(1)
    pygame.quit()

main()
