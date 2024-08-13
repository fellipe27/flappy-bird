import pygame
import os

class Ground:
    GROUND_IMAGE = pygame.image.load(os.path.join('images', 'ground.png'))
    WIDTH = GROUND_IMAGE.get_width()
    VELOCITY = 5

    def __init__(self, game):
        self.game = game
        self.x_0 = 0
        self.x_1 = self.WIDTH
        self.y = self.game.HEIGHT - self.GROUND_IMAGE.get_height()

    def move(self):
        self.x_0 -= self.VELOCITY
        self.x_1 -= self.VELOCITY

        if self.x_0 + self.WIDTH < 0:
            self.x_0 = self.x_1 + self.WIDTH
        if self.x_1 + self.WIDTH < 0:
            self.x_1 = self.x_0 + self.WIDTH

    def draw(self, panel):
        panel.blit(self.GROUND_IMAGE, (self.x_0, self.y))
        panel.blit(self.GROUND_IMAGE, (self.x_1, self.y))
