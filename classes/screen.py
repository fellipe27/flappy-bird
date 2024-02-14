from settings import *

class Screen:
    BACKGROUND_IMG = BACKGROUND_IMAGE
    LOGO_IMG = LOGO_IMAGE
    RESTART_BUTTON_IMG = RESTART_BUTTON_IMAGE

    def __init__(self, game, birds, pipes, ground, database):
        self.game = game
        self.birds = birds
        self.pipes = pipes
        self.ground = ground
        self.database = database

    def draw(self):
        self.game.screen.blit(self.BACKGROUND_IMG, (0, -300))

        if self.game.title_screen:
            title_text = TITLE_FONT.render('PRESS SPACE TO PLAY', False, (255, 255, 255))

            self.game.screen.blit(self.LOGO_IMG, (SCREEN_WIDTH / 2 - self.LOGO_IMG.get_width() / 2, 100))
            self.game.screen.blit(title_text, (SCREEN_WIDTH / 2 - title_text.get_width() / 2, 250))
        elif self.game.playing:
            for bird in self.birds:
                bird.draw(self.game.screen)
        elif self.game.restart_screen:
            restart_text = TITLE_FONT.render('PRESS SPACE TO RESTART', False, (255, 255, 255))
            highscore = HIGHSCORE_FONT.render(f'Highscore: {self.database.select_highscore()}', True, (255, 255, 255))

            self.game.screen.blit(restart_text, (SCREEN_WIDTH / 2 - restart_text.get_width() / 2, 150))
            self.game.screen.blit(self.RESTART_BUTTON_IMG, (SCREEN_WIDTH / 2 - self.RESTART_BUTTON_IMG.get_width() / 2, 200))
            self.game.screen.blit(highscore, (SCREEN_WIDTH - 10 - highscore.get_width(), 50))

        if self.game.playing or self.game.restart_screen:
            for pipe in self.pipes:
                pipe.draw(self.game.screen)

            score_text = POINTS_FONT.render(f'Score: {self.game.points}', True, (255, 255, 255))
            self.game.screen.blit(score_text, (SCREEN_WIDTH - 10 - score_text.get_width(), 10))

        self.ground.draw(self.game.screen)

        pygame.display.update()
