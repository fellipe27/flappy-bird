import pygame
import os

class Audio:
    pygame.mixer.init()
    SOUNDTRACK = pygame.mixer.Sound(os.path.join('sounds', 'soundtrack.wav'))
    SUCCESS = pygame.mixer.Sound(os.path.join('sounds', 'success.wav'))

    def __init__(self):
        self.SUCCESS.set_volume(0.1)
