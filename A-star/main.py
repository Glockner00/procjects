import pygame
import math
from queue import PriorityQueue


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


class Tile:
    def __init__(self, row, col, width, total_rows):
        self.row = row
        self.col = col
        self.width = width
        self.total_rows = total_rows
        self.x = row * width
        self.y = col * width
        self.color = WHITE
        self.neighbours = []

    def get_pos(self):
        return self.row, self.col

    def is_closed(self):
        return self.color == RED

    def is_opened(self):
        return self.color == GREEN

    def is_barrier(self):
        return self.color == BLACK

    def is_start(self):
        return self.color == YELLOW

    def is_end(self):
        return self.color == BLUE

    def reset(self):
        self.color = WHITE

    def make_start(self):
        self.color = YELLOW

    def make_closed(self):
        self.color = RED

    def make_open(self):
        self.color = GREEN

    def make_barrier(self):
        self.color = BLACK

    def make_end(self):
        self.color = BLUE

    def make_path(self):
        self.color = PURPLE

    def draw(self, win):
        pygame.draw.rect(win, self.color, (self.x, self.y,
                                           self.width, self.width))


def make_grid(rows, width):
    grid = []
    gap = width // rows
    for i in range(rows):
        grid.append([])
        for j in range(rows):
            tile = Tile(i, j, gap, rows)
            grid[i].append(tile)
    return grid


def draw_grid(win, rows, width):
    gap = width // rows
    for i in range(rows):
        pygame.draw.line(win, GREY, (0, i*gap), (width, i * gap))
        for j in range(rows):
            pygame.draw.line(win, GREY, (j * gap, 0), (j * gap, width))


def draw(win, grid, rows, width):
    win.fill(WHITE)
    for row in grid:
        for tile in row:
            tile.draw(win)

    draw_grid(win, rows, width)
    pygame.display.update()


def mouse_clicked_position(pos, row, width):
    gap = width // row
    y, x = pos
    row = y // gap
    col = x // gap

    return row, col


def main(win, width):
    ROWS = 50
    grid = make_grid(ROWS, width)

    start = None
    end = None

    run = True
    while run:
        draw(win, grid, ROWS, width)
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                run = False
            # LEFT CLICK - enter start, end and barriers
            if pygame.mouse.get_pressed()[0]:
                pos = pygame.mouse.get_pos()
                row, col = mouse_clicked_position(pos, ROWS, width)
                tile = grid[row][col]

                if not start and tile != end:
                    start = tile
                    start.make_start()

                if not end and tile != start:
                    end = tile
                    end.make_end()

                if tile != end and tile != start:
                    tile.make_barrier()

            # RIGHT CLICK - reset Tile
            if pygame.mouse.get_pressed()[2]:
                pos = pygame.mouse.get_pos()
                row, col = mouse_clicked_position(pos, ROWS, width)
                tile = grid[row][col]
                tile.reset()

                if tile == start:
                    start = None
                elif tile == end:
                    end = None

    pygame.QUIT


main(WIN, WIDTH)
