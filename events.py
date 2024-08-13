import pygame
import sys

class Events:
    def __init__(self, game):
        self.game = game

    def check_events(self):
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                self.game.database.close_database()
                pygame.quit()
                sys.exit()

            if event.type == pygame.KEYDOWN:
                if event.key == pygame.K_SPACE:
                    if self.game.is_in_title:
                        self.game.is_in_title = False
                        self.game.is_playing = True

                    if self.game.is_playing:
                        for bird in self.game.birds:
                            bird.jump()

                    if self.game.is_game_over:
                        self.game.is_game_over = False
                        self.game.is_playing = True
                        self.game.restart_game()
