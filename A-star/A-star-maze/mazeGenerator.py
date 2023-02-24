import random
import pygame

# Constants
WIDTH = 800
WIN = pygame.display.set_mode((WIDTH, WIDTH))
RED = (255, 0, 0)
GREEN = (0, 255, 0)
BLUE = (28, 134, 238)
YELLOW = (255, 255, 0)
WHITE = (255, 255, 255)
BLACK = (0, 0, 0)
PURPLE = (128, 0, 128)
GREY = (128, 128, 128)


# Defines each tile in the grid.
class Tile:
    def __init__(self, row, col, width):
        self.row = row
        self.col = col
        self.width = width
        self.x = row * width
        self.y = col * width
        self.color = PURPLE
        self.visited = False
        self.walls = [True, True, True, True]  # Up, Down, Right, Up
    
    # Check if the cell has any surrounding unvisited cells that are walkable
    def neighbours(self, grid: list) -> list:
        neighbours = []
        return neighbours 


    # Draw a single Tile
    def draw(self, win):
        pygame.draw.rect(win, self.color, (self.x, self.y, self.width, self.width))

 
# Makes a grid for the maze
def makeGrid(rows: int, width: int) -> list:
    grid = []
    gap = rows // width
    for i in range(rows):
        grid.append([])  # 2D - array
        for j in range(rows):
            tile = Tile(i, j, gap)
            grid[i].append(tile)
    return grid



# Drawing grey gridlines for the gird.
def draw_grid(win, rows: int, width: int):
    gap = width // rows  # width of each cube.
    for i in range(rows):

        # Vertical lines
        pygame.draw.line(win, GREY, (0, i*gap), (width, i * gap))
        for j in range(rows):

            # Horizontal lines
            pygame.draw.line(win, GREY, (j * gap, 0), (j * gap, width))

# Main draw function that draws the grid with help of the Tile class.
def draw(win, grid: list, rows: int, width: int):
    win.fill(WHITE)  # Fill entire window with WHITE
    for row in grid:
        for tile in row:
            tile.draw(win)

    # Draw gridlines on top.
    draw_grid(win, rows, width)
    pygame.display.update()

# Draw exisiting walls around a Tile
def drawWalls(grid: list, tmpGrid: list) -> list:
    return tmpGrid


# Remove the wall between two Tiles 
def removeWalls(current: Tile, choice: Tile):
    pass


# Draw a boarder around the maze
def drawBoarder(grid: list) -> list:
    return grid


# main loop to generate the maze 
def main():
    print(makeGrid(8, 8))
    ROWS = 20
    grid = makeGrid(ROWS, WIDTH)

    run = True

    while run:
        draw(WIN, grid, ROWS, WIDTH)
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                run = False


main()
