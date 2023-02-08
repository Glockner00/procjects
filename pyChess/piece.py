import pygame
import os

b_bishop = pygame.image.load(os.path.join("img", "black_bishop.png"))
b_king = pygame.image.load(os.path.join("img", "black_king.png"))
b_knight = pygame.image.load(os.path.join("img", "black_knight.png"))
b_pawn = pygame.image.load(os.path.join("img", "black_pawn.png"))
b_queen = pygame.image.load(os.path.join("img", "black_queen.png"))
b_rook = pygame.image.load(os.path.join("img", "black_rook.png"))

w_bishop = pygame.image.load(os.path.join("img", "white_bishop.png"))
w_king = pygame.image.load(os.path.join("img", "white_king.png"))
w_knight = pygame.image.load(os.path.join("img", "white_knight.png"))
w_pawn = pygame.image.load(os.path.join("img", "white_pawn.png"))
w_queen = pygame.image.load(os.path.join("img", "white_queen.png"))
w_rook = pygame.image.load(os.path.join("img", "white_rook.png"))

b = [b_bishop, b_king, b_knight, b_pawn, b_queen, b_rook]
w = [w_bishop, w_king, w_knight, w_pawn, w_queen, w_rook]
B = []
W = []

for img in b:
    B.append(pygame.transform.scale(img, (70, 70)))

for img in w:
    W.append(pygame.transform.scale(img, (70, 70)))

class Piece:
    rect = (150, 150, 700, 700)
    img = -1
    startX = rect[0]
    startY = rect[1]

    def __init__(self, row, col, color):
        self.row = row
        self.col = col
        self.color = color
        self.selected = False
        
    def click(self):
        pass
    
    def isSelected(self):
        return self.isSelected
    
    def draw(self, win, board):
        if self.color == "w":
            drawThis = W[self.img]
        else:
            drawThis = B[self.img]
        moves = self.valid_moves(board)   
        
        if self.selected:
            for move in moves:
                x = 35 + round(self.startX + (move[0] * self.rect[2]/8))
                y = 35 + round(self.startY + (move[1] * self.rect[3]/8))
                pygame.draw.circle(win, (255, 0, 0), (x,y), 10)

        x = 5 + round(self.startX + (self.col * self.rect[2]/8))
        y = 5 + round(self.startY + (self.row * self.rect[3]/8))

        if self.selected:
            pygame.draw.rect(win, (255, 0, 0), (x, y, 70, 70), 2)
        
        win.blit(drawThis, (x, y))

class Bishop(Piece):
    img = 0
    def valid_moves(self, board):
        return[]

class King(Piece):
    img = 1
    def valid_moves(self, board):
        i = self.row
        j = self.col
        moves = []

        if i<7:
            #TOP LEFT 
            if j>0:
                p = board[i-1][j-1]
                if p!=0:   
                    if p.color != self.color:
                        moves.append((j-1, i-1))
                #TOP MIDDLE
                p = board[i-1][j]
                if p.color != self.color:
                    moves.append((j, i-1))
            #TOP RIGHT
            if j<7:
                p = board[i-1][j+1]
                if p !=0:
                    if p.color != self.color:
                        moves.append((j+1, i-1))
        if i<7:
            #BOTTOM LEFT
            if j>0:
                p = board[i+1][j-1]
                if p !=0:
                    if p.color != self.color:
                        moves.append((j-1, i+1))
            #BOTTOM MIDDLE
            p = board[i+1][j]
            if p !=0:
                if p.color != self.color:
                    moves.append((j, i+1))
            #BOTTOM RIGHT
            if i<7:
                p = board[i+1][j+1]
                if p !=0:
                    if p.color != self.color:
                        moves.append((j+1, i+1))
        #MIDDLE LEFT 
        if j>0:
            p = board[i][j-1]
            if p!=0:
                if p.color != self.color:
                    moves.append((j-1, i))
        #MIDDLE RIGHT
        if j<7:
           p = board[i-1][j-1]
           if p!=0:
            if p.color != self.color:
                    moves.append((j-1, i-1))
        return moves

class Knight(Piece):
    img = 2

    def valid_moves(self, board):
        i = self.row
        j = self.col
        moves = []
        
        # DOWN LEFT
        if i<6 and j>0:
            p = board[i+2][j-1]
            if p!=0:
                if p.color != self.color:
                    moves.append((j-1, i+2))
        #DOWN RIGH:
        if i<6 and j<7:
            p = board[i+2][j+1]
            if p != 0:
                if p.color != self.color:
                    moves.append((j+1, i+2))
        #UP LEFT
        if i>1 and j>0:
            p = board[i-2][j-1]
            if p !=0:
                if p.color != self.color:
                    moves.append((j-1, i-2))
        #UP RIGHT
        if i>1 and j<7:
            p = board[i-2][j+1] 
            if p!=0:
                if p.color != self.color:
                    moves.append((j+1, i-2))    
        return moves

class Pawn(Piece):
    img = 3
    
    def __init__(self, row, col, color):
        super().__init__(row, col, color)
        self.first = True
        self.queen = False
   
    def valid_moves(self, board):
        i = self.row
        j = self.col
        moves = []

        if self.color == "b":
            if self.first:
                if i < 6:
                    p  = board[i+2][j]
                    if p==0:
                        moves.append((j, i+2))
            if i < 7:
                p  = board[i+1][j]
                if p == 0:
                    moves.append((j, i+1))
                #DIAGONAL 
            if j<7:
                p = board[i+1][j+1]
                if p!=0:
                    if p.color != self.color:
                        moves.append((j+1, i+1))
            if j>0:
                p = board[i+1][j-1]
                if p!=0:
                    if p.color != self.color:
                        moves.append((j-1, i+1))
        else:
            if self.first:
                if i > 1:
                    p  = board[i-2][j]
                    if p == 0:
                        moves.append((j, i-2))
            if i > 0:
                p  = board[i-1][j]
                if p == 0:
                    moves.append((j, i-1))  
            if j<7:
                p = board[i-1][j+1]
                if p!=0:
                    if p.color != self.color:
                            moves.append((j+1, i-1))
            if j>0:
                p = board[i-1][j-1]
                if p!=0:
                    if p.color != self.color:
                        moves.append((j-1, i-1))
        return moves

class Queen(Piece):
    img = 4

    def valid_moves(self, board):
        return []

class Rook(Piece):
    img = 5
    def valid_moves(self, board):
        i = self.row
        j = self.col
        moves = []
        #UP 
        for x in range(i, -1, -1):
            p = board[i][j]
            if p ==0:
                moves.append((j,x))
                #if we find a zero we break, cant jump over.
                break
        #DOWN
        for x in range(i, 8, 1):
            p = board[i][j]
            if p ==0:
                moves.append((j,x))
                break
        #LEFT
        for x in range(j, -1, -1):
            p = board[i][j]
            if p ==0:
                moves.append((x, i))
                break
        #RIGHT
        for x in range(j, -8, 1):
            p = board[i][j]
            if p ==0:
                moves.append((x, i))
                break
        return moves