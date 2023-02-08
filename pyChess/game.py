import os
from piece import Bishop
from board import Board
import pygame

board = pygame.transform.scale(pygame.image.load(os.path.join("img","board_alt.png")), (1000, 1000))
# chessbg = pygame.image.load(os.path.join("img", "chessbg.png"))
rect = (150, 150, 700, 700)

def redraw_gameWindow():
    global win, bo
    win.blit(board, (0, 0))
    bo.draw(win, bo.board)
    pygame.display.update()

#return pos (x,y) in range 0-7, 0-7
def click(pos):
    x = pos[0]
    y = pos[1]

    if rect[0] < x < rect[0] + rect[2]:
        if rect[1] < y < rect[1] + rect[3]:
            divX = x - rect[0]
            divY = y - rect[0]
            i = int(divX/(rect[2]/8))
            j = int(divY/(rect[3]/8))
            return i, j

def main():
    global bo
    bo = Board(8, 8)

    run = True
    clock = pygame.time.Clock()
    while run:
        clock.tick(10)
        redraw_gameWindow()

        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                run = False
                quit()
            if event.type == pygame.MOUSEMOTION: 
                pass
            if event.type == pygame.MOUSEBUTTONDOWN:
                pos = pygame.mouse.get_pos() 
                i, j = click(pos)
                bo.select(i,j)

WIDTH = 1000
HEIGHT = 1000
win = pygame.display.set_mode((WIDTH, HEIGHT))
pygame.display.set_caption(("pyChess"))
main()