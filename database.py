import sqlite3

class Database:
    def __init__(self):
        self.conn = sqlite3.connect('score.db')
        self.cursor = self.conn.cursor()

    def create_table(self):
        self.cursor.execute('CREATE TABLE IF NOT EXISTS scores (id INTEGER PRIMARY KEY AUTOINCREMENT, score INTEGER)')

    def save_score(self, score):
        self.cursor.execute('INSERT INTO scores (score) VALUES (?)', (score,))
        self.conn.commit()

    def get_highscore(self):
        self.cursor.execute('SELECT MAX(score) from scores')
        highscore = self.cursor.fetchone()[0]

        return highscore if highscore is not None else 0

    def close_database(self):
        self.conn.close()
