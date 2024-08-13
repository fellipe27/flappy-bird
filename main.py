import pygame
import os
from panel import Panel
from events import Events
from bird import Bird
from ground import Ground
from pipe import Pipe
from audio import Audio
from database import Database

class Game:
    pygame.init()
    ICON_IMAGE = pygame.image.load(os.path.join('images', 'icon.png'))
    WIDTH = 288
    HEIGHT = 512

    def __init__(self):
        self.screen = pygame.display.set_mode((self.WIDTH, self.HEIGHT))
        pygame.display.set_caption('Flappy Bird')
        pygame.display.set_icon(self.ICON_IMAGE)
        self.clock = pygame.time.Clock()
        self.birds = [Bird()]
        self.ground = Ground(self)
        self.pipes = [Pipe(self.WIDTH + 100)]
        self.panel = Panel(self)
        self.events = Events(self)
        self.audio = Audio()
        self.score = 0
        self.database = Database()
        self.database.create_table()
        self.is_in_title = True
        self.is_playing = False
        self.is_game_over = False

    def restart_game(self):
        self.birds.append(Bird())
        self.pipes.append(Pipe(self.WIDTH + 100))
        self.score = 0

    def run(self):
        self.audio.SOUNDTRACK.play(loops=-1)

        while True:
            self.clock.tick(30)

            add_pipe = False
            remove_pipes = []

            if self.is_playing:
                for bird in self.birds:
                    if bird.y + bird.image.get_height() > self.ground.y or bird.y < 0:
                        self.birds.remove(bird)

                    bird.move()

                for pipe in self.pipes:
                    for i, bird in enumerate(self.birds):
                        if pipe.collide(bird):
                            self.birds.pop(i)

                        if not pipe.bird_passed and bird.x > pipe.x:
                            pipe.bird_passed = True
                            add_pipe = True
                            self.audio.SUCCESS.play()
                            self.score += 1

                    if pipe.x + pipe.TOP_PIPE_IMAGE.get_width() < 0:
                        remove_pipes.append(pipe)

                if add_pipe:
                    self.pipes.append(Pipe(self.WIDTH))

                for pipe in remove_pipes:
                    self.pipes.remove(pipe)

                if len(self.birds) == 0 and len(self.pipes) == 0:
                    self.is_playing = False
                    self.is_game_over = True
                    self.database.save_score(self.score)

            if self.is_playing or self.is_game_over:
                self.ground.move()

                for pipe in self.pipes:
                    pipe.move()

            self.events.check_events()
            self.panel.draw(self.birds, self.ground, self.pipes)

if __name__ == '__main__':
    Game().run()
