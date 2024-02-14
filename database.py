import sqlite3

class Database:
    def __init__(self):
        self.conn = sqlite3.connect('score.db')
        self.cursor = self.conn.cursor()
        self.create_highscore_table()

    def create_highscore_table(self):
        self.cursor.execute('SELECT name FROM sqlite_master WHERE type="table" AND name="highscore"')
        table_exists = self.cursor.fetchone()

        if not table_exists:
            self.cursor.execute('CREATE TABLE highscore(score INTEGER NOT NULL)')

    def insert_highscore(self, score):
        highscore = self.select_highscore()

        if score > highscore:
            self.cursor.execute(f'INSERT INTO highscore VALUES ({score})')
            self.conn.commit()

    def select_highscore(self):
        scores = self.cursor.execute('SELECT score FROM highscore')
        best_score = 0

        for row in scores.fetchall():
            if row[0] > best_score:
                best_score = row[0]

        return best_score

    def close_database(self):
        self.conn.close()
