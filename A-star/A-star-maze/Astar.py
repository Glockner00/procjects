import pygame
from Maze import generateMaze, Cell
import random

#CONSTANTS 
WHITE = (255, 255, 255)
BLACK = (0, 0, 0)
GREEN = (0, 255, 0)
PURPLE = (128, 0, 128)

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
            
            if cell.walls[1]:
                pygame.draw.line(WIN, BLACK, (cellX + CELL_SIZE, cellY), (cellX + CELL_SIZE, cellY + CELL_SIZE))
            
            if cell.walls[2]:
                pygame.draw.line(WIN, BLACK, (cellX, cellY + CELL_SIZE), (cellX + CELL_SIZE, cellY + CELL_SIZE))
            
            if cell.walls[3]:
                pygame.draw.line(WIN, BLACK, (cellX , cellY), (cellX, cellY + CELL_SIZE))

            if cell.start:
                pygame.draw.circle(WIN, GREEN, (cellX + CELL_SIZE//2, cellY + CELL_SIZE //2), CELL_SIZE//4)
            
            if cell.end:
                pygame.draw.circle(WIN, PURPLE, (cellX + CELL_SIZE//2, cellY + CELL_SIZE //2), CELL_SIZE//4) 

        pygame.display.update()

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
        CLOCK.tick(1)
    pygame.quit()

main()
