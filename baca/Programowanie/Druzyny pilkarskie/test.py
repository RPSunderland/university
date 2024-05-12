from parser import GameParser
from stats import TeamStats
import re

def read_report():
    """
    Wczytaj raport pomeczowy ze standardowego wejscia
    :return: wielo-lonijkowy string z raportem pomeczowym
    """
    with open('3.in') as f:
        lines = f.read().splitlines()
    return '\n'.join(lines)


if __name__ == "__main__":
    game_report = read_report()

    home_stats = TeamStats()
    away_stats = TeamStats()

    parser = GameParser(home_stats, away_stats)
    parser.parse(game_report)

    parser.print_summary()


