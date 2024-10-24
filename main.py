from panel import Panel
from events import Events
from bird import Bird
from ground import Ground
from pipe import Pipe
from audio import Audio
from database import Database
import pygame
import os
import neat

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
        Audio().SOUNDTRACK.play(loops=-1)
        self.running = True
        self.birds = []
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
        self.ai_playing = False
        self.is_game_over = False
        self.ai_generation = 0

    def restart_game(self):
        self.birds.append(Bird())
        self.pipes.append(Pipe(self.WIDTH + 100))
        self.score = 0

    def run(self, genomes, config):
        networks = []
        genome_list = []
        self.ai_generation += 1

        for _, genome in genomes:
            network = neat.nn.FeedForwardNetwork.create(genome, config)
            networks.append(network)

            genome.fitness = 0
            genome_list.append(genome)

            self.birds.append(Bird())

        if not self.running:
            self.running = True

        while self.running:
            self.clock.tick(30)

            add_pipe = False
            remove_pipes = []
            pipe_index = 0

            if self.is_playing:
                if self.ai_playing:
                    if len(self.birds) > 0:
                        if (
                                len(self.pipes) > 1
                                and self.birds[0].x > (self.pipes[0].x + self.pipes[0].TOP_PIPE_IMAGE.get_width())
                        ):
                            pipe_index = 1
                    else:
                        if len(self.pipes) == 0:
                            self.pipes.append(Pipe(self.WIDTH + 100))
                            self.score = 0
                            self.running = False

                            break

                for i, bird in enumerate(self.birds):
                    if bird.y + bird.image.get_height() > self.ground.y or bird.y < 0:
                        self.birds.remove(bird)

                        if self.ai_playing:
                            genome_list.pop(i)
                            networks.pop(i)

                        continue
                    if self.ai_playing:
                        genome_list[i].fitness += 0.1

                        output = networks[i].activate((
                            bird.y,
                            abs(bird.y - self.pipes[pipe_index].height),
                            abs(bird.y - self.pipes[pipe_index].base_pos)
                        ))

                        if output[0] > 0.5:
                            bird.jump()

                    bird.move()

                for pipe in self.pipes:
                    for i, bird in enumerate(self.birds):
                        if pipe.collide(bird):
                            self.birds.pop(i)

                            if self.ai_playing:
                                genome_list[i].fitness -= 1
                                genome_list.pop(i)
                                networks.pop(i)

                        if not pipe.bird_passed and bird.x > pipe.x:
                            pipe.bird_passed = True
                            add_pipe = True
                            self.audio.SUCCESS.play()

                    if pipe.x + pipe.TOP_PIPE_IMAGE.get_width() < 0:
                        remove_pipes.append(pipe)

                if add_pipe:
                    self.pipes.append(Pipe(self.WIDTH))
                    self.score += 1

                    if self.ai_playing:
                        for genome in genome_list:
                            genome.fitness += 5

                for pipe in remove_pipes:
                    self.pipes.remove(pipe)

                if len(self.birds) == 0 and len(self.pipes) == 0 and not self.ai_playing:
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
    path = os.path.dirname(__file__)
    config_path = os.path.join(path, 'config.txt')

    configurations = neat.config.Config(
        neat.DefaultGenome,
        neat.DefaultReproduction,
        neat.DefaultSpeciesSet,
        neat.DefaultStagnation,
        config_path
    )

    population = neat.Population(configurations)
    population.run(Game().run, 50)
