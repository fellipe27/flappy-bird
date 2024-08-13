import pygame
import os

class Bird:
    BIRD_IMAGES = [pygame.image.load(os.path.join('images', f'bird-{i}.png')) for i in range(3)]
    MAX_ROTATION = 25
    ROTATION_VELOCITY = 20
    ANIMATION_TIME = 5

    def __init__(self):
        self.x = 80
        self.y = 120
        self.image_count = 0
        self.image = self.BIRD_IMAGES[self.image_count]
        self.velocity = 0
        self.time = 0
        self.angle = 0
        self.height = self.y

    def jump(self):
        self.velocity = -10.5
        self.time = 0
        self.height = self.y

    def move(self):
        self.time += 1

        displacement = 1.5 * (self.time ** 2) + self.velocity * self.time

        if displacement > 16:
            displacement = 16
        elif displacement < 0:
            displacement -= 2

        self.y += displacement

        if displacement < 0 or self.y < self.height + 50:
            if self.angle < self.MAX_ROTATION:
                self.angle = self.MAX_ROTATION
        else:
            if self.angle > -90:
                self.angle -= self.ROTATION_VELOCITY

    def get_mask(self):
        return pygame.mask.from_surface(self.image)

    def draw(self, panel):
        self.image_count += 1

        if self.image_count < self.ANIMATION_TIME:
            self.image = self.BIRD_IMAGES[0]
        elif self.image_count < self.ANIMATION_TIME * 2:
            self.image = self.BIRD_IMAGES[1]
        elif self.image_count < self.ANIMATION_TIME * 3:
            self.image = self.BIRD_IMAGES[2]
        elif self.image_count < self.ANIMATION_TIME * 4:
            self.image = self.BIRD_IMAGES[1]
        elif self.image_count < self.ANIMATION_TIME * 5:
            self.image = self.BIRD_IMAGES[0]
            self.image_count = 0

        if self.angle < -80:
            self.image = self.BIRD_IMAGES[1]
            self.image_count = self.ANIMATION_TIME * 2

        rotated_image = pygame.transform.rotate(self.image, self.angle)
        image_center_pos = self.image.get_rect(topleft=(self.x, self.y)).center
        rectangle = rotated_image.get_rect(center=image_center_pos)

        panel.blit(rotated_image, rectangle.topleft)
