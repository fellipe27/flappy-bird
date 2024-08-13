import pygame
import os

class Panel:
    pygame.font.init()
    BACKGROUND_IMAGE = pygame.image.load(os.path.join('images', 'background.png'))
    LOGO_IMAGE = pygame.image.load(os.path.join('images', 'logo.png'))
    RESTART_BUTTON_IMAGE = pygame.image.load(os.path.join('images', 'restart-button.png'))
    TITLE_FONT = pygame.font.SysFont('pixelart', 30)
    SCORE_FONT = pygame.font.SysFont('comicsansms', 20)
    GAME_OVER_FONT = pygame.font.SysFont('pixelart', 20)
    HIGH_SCORE_FONT = pygame.font.SysFont('comicsansms', 15)

    def __init__(self, game):
        self.game = game

    def draw(self, birds, ground, pipes):
        self.game.screen.blit(self.BACKGROUND_IMAGE, (0, 0))

        if self.game.is_in_title:
            self.game.screen.blit(self.LOGO_IMAGE, (self.game.WIDTH / 2 - self.LOGO_IMAGE.get_width() / 2, self.LOGO_IMAGE.get_height()))

            title_text = self.TITLE_FONT.render('PRESS SPACE TO PLAY', False, (255, 255, 255))
            self.game.screen.blit(title_text, (self.game.WIDTH / 2 - title_text.get_width() / 2, self.game.HEIGHT / 3))

        if self.game.is_playing:
            for pipe in pipes:
                pipe.draw(self.game.screen)
            for bird in birds:
                bird.draw(self.game.screen)

        if self.game.is_game_over:
            game_over_text = self.GAME_OVER_FONT.render('PRESS SPACE TO RESTART', True, (255, 255, 255))
            self.game.screen.blit(game_over_text, (self.game.WIDTH / 2 - game_over_text.get_width() / 2, self.game.HEIGHT / 2 - 50))

            high_score_text = self.HIGH_SCORE_FONT.render(f'HIGHSCORE: {self.game.database.get_highscore()}', True, (255, 255, 255))
            self.game.screen.blit(high_score_text, (self.game.WIDTH - 10 - high_score_text.get_width(), 40))

            self.game.screen.blit(self.RESTART_BUTTON_IMAGE, (self.game.WIDTH / 2 - self.RESTART_BUTTON_IMAGE.get_width() / 2, self.game.HEIGHT / 2 - self.RESTART_BUTTON_IMAGE.get_height() / 2))

        if self.game.is_playing or self.game.is_game_over:
            score_text = self.SCORE_FONT.render(f'SCORE: {self.game.score}', True, (255, 255, 255))
            self.game.screen.blit(score_text, (self.game.WIDTH - 10 - score_text.get_width(), 10))

        ground.draw(self.game.screen)

        pygame.display.update()
