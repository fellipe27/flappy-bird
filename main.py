from settings import *
from classes.screen import Screen
from classes.bird import Bird
from classes.pipe import Pipe
from classes.ground import Ground
from database import Database
from sys import exit

class Game:
    ICON_IMG = ICON_IMAGE

    def __init__(self):
        self.screen = pygame.display.set_mode((SCREEN_WIDTH, SCREEN_HEIGHT))
        pygame.display.set_caption('Flappy Bird')
        pygame.display.set_icon(self.ICON_IMG)
        self.clock = pygame.time.Clock()
        self.title_screen = True
        self.playing = False
        self.restart_screen = False
        self.birds = [Bird(100, 200)]
        self.pipes = [Pipe(700)]
        self.ground = Ground(500)
        self.points = 0
        self.database = Database()
        self.game_screen = Screen(self, self.birds, self.pipes, self.ground, self.database)

    def check_events(self):
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                self.database.close_database()
                pygame.quit()
                exit()
            if event.type == pygame.KEYDOWN:
                if event.key == pygame.K_SPACE:
                    if self.title_screen:
                        self.title_screen = False
                        self.playing = True
                    elif self.playing:
                        for bird in self.birds:
                            bird.jump()
                    elif self.restart_screen:
                        self.restart_screen = False
                        self.playing = True

                        self.points = 0
                        self.birds.append(Bird(100, 200))
                        self.pipes.append(Pipe(700))

    def run(self):
        SOUNDTRACK.play(loops=-1)

        while True:
            self.clock.tick(30)
            self.check_events()

            add_pipe = False
            remove_pipes = []

            if self.playing:
                self.ground.move()

                for bird in self.birds:
                    bird.move()

                for pipe in self.pipes:
                    for i, bird in enumerate(self.birds):
                        if pipe.collide(bird):
                            self.birds.pop(i)

                        if not pipe.bird_passed and bird.x > pipe.x:
                            pipe.bird_passed = True
                            add_pipe = True
                            SUCCESS_SOUND.play()

                    pipe.move()

                    if pipe.x + pipe.TOP_PIPE_IMAGE.get_width() < 0:
                        remove_pipes.append(pipe)

                if add_pipe:
                    self.points += 1
                    self.pipes.append((Pipe(500)))

                for pipe in remove_pipes:
                    self.pipes.remove(pipe)
                for i, bird in enumerate(self.birds):
                    if bird.y + bird.image.get_height() > self.ground.y or bird.y < 0:
                        self.birds.pop(i)

                if len(self.birds) == 0 and len(self.pipes) == 0:
                    self.playing = False
                    self.restart_screen = True
                    self.database.insert_highscore(self.points)
            elif self.restart_screen:
                for pipe in self.pipes:
                    pipe.move()
                self.ground.move()

            self.game_screen.draw()

if __name__ == '__main__':
    game = Game()
    game.run()
