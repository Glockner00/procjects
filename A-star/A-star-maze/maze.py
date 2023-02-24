import pygame
from random import choice

# Constants
WIDTH = 800
WIN = pygame.display.set_mode((WIDTH, WIDTH))
CLOCK = pygame.time.Clock()

# Colors
RED = (255, 0, 0)
GREEN = (0, 255, 0)
BLUE = (28, 134, 238)
YELLOW = (255, 255, 0)
WHITE = (255, 255, 255)
BLACK = (0, 0, 0)
PURPLE = (128, 0, 128)
GREY = (128, 128, 128)


class Tile:
    def __init__(self, x, y):
        self.x = x
        self.y = y
        self.current = False
        self.neighbors = []

    
    def draw(self):
        pass
    
    def update_neighbors(self):
        pass


def draw():
    pass


def a_star():
    pass


def h():
    pass


def reconstruct_path():
    pass


def generate_maze():
    pygame.init()
    WIN.fill(WHITE)
    
    run = True
    while run:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                run = False
        pygame.display.flip()
        CLOCK.tick(30)
