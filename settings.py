import pygame
import os

pygame.font.init()
pygame.mixer.init()

SCREEN_WIDTH = 400
SCREEN_HEIGHT = 600

PIPE_IMAGE = pygame.transform.scale2x(pygame.image.load(os.path.join('imgs', 'pipe.png')))
GROUND_IMAGE = pygame.transform.scale2x(pygame.image.load(os.path.join('imgs', 'ground.png')))
BACKGROUND_IMAGE = pygame.transform.scale2x(pygame.image.load(os.path.join('imgs', 'background.png')))
BIRD_IMAGES = [
    pygame.transform.scale2x(pygame.image.load(os.path.join('imgs', 'bird-0.png'))),
    pygame.transform.scale2x(pygame.image.load(os.path.join('imgs', 'bird-1.png'))),
    pygame.transform.scale2x(pygame.image.load(os.path.join('imgs', 'bird-2.png')))

]

LOGO_IMAGE = pygame.transform.smoothscale(pygame.image.load(os.path.join('imgs', 'logo.png')), (250, 66))
RESTART_BUTTON_IMAGE = pygame.transform.smoothscale(pygame.image.load(os.path.join('imgs', 'restart-button.png')), (50, 50))
ICON_IMAGE = pygame.transform.smoothscale(pygame.image.load(os.path.join('imgs', 'icon.png')), (24, 24))

TITLE_FONT = pygame.font.SysFont('pixelart', 30)
POINTS_FONT = pygame.font.SysFont('comicsansms', 30)
HIGHSCORE_FONT = pygame.font.SysFont('comicsansms', 15)

SOUNDTRACK = pygame.mixer.Sound(os.path.join('sounds', 'soundtrack.mpeg'))
SUCCESS_SOUND = pygame.mixer.Sound(os.path.join('sounds', 'success.mpeg'))
SUCCESS_SOUND.set_volume(0.1)
