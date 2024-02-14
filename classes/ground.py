from settings import *

class Ground:
    VELOCITY = 5
    IMAGE = GROUND_IMAGE
    WIDTH = GROUND_IMAGE.get_width()

    def __init__(self, y):
        self.y = y
        self.x_0 = 0
        self.x_1 = self.WIDTH

    def move(self):
        self.x_0 -= self.VELOCITY
        self.x_1 -= self.VELOCITY

        if self.x_0 + self.WIDTH < 0:
            self.x_0 = self.x_1 + self.WIDTH
        if self.x_1 + self.WIDTH < 0:
            self.x_1 = self.x_0 + self.WIDTH

    def draw(self, screen):
        screen.blit(self.IMAGE, (self.x_0, self.y))
        screen.blit(self.IMAGE, (self.x_1, self.y))
