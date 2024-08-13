import pygame
import os
import random

class Pipe:
    BASE_PIPE_IMAGE = pygame.image.load(os.path.join('images', 'pipe.png'))
    TOP_PIPE_IMAGE = pygame.transform.flip(BASE_PIPE_IMAGE, False, True)
    DISTANCE = 150
    VELOCITY = 5

    def __init__(self, x):
        self.x = x
        self.height = 0
        self.top_pos = 0
        self.base_pos = 0
        self.bird_passed = False
        self.define_height()

    def define_height(self):
        self.height = random.randrange(50, 210)
        self.top_pos = self.height - self.TOP_PIPE_IMAGE.get_height()
        self.base_pos = self.height + self.DISTANCE

    def move(self):
        self.x -= self.VELOCITY

    def collide(self, bird):
        bird_mask = bird.get_mask()
        top_mask = pygame.mask.from_surface(self.TOP_PIPE_IMAGE)
        base_mask = pygame.mask.from_surface(self.BASE_PIPE_IMAGE)

        top_distance = (self.x - bird.x, self.top_pos - round(bird.y))
        base_distance = (self.x - bird.x, self.base_pos - round(bird.y))

        top_point = bird_mask.overlap(top_mask, top_distance)
        base_point = bird_mask.overlap(base_mask, base_distance)

        return top_point or base_point

    def draw(self, panel):
        panel.blit(self.TOP_PIPE_IMAGE, (self.x, self.top_pos))
        panel.blit(self.BASE_PIPE_IMAGE, (self.x, self.base_pos))
