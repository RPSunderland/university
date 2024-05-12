#Rafal Plizga
import re


class GameParser:
    def __init__(self, home_team_stats, away_team_stats):
        self.is_parse = False
        self.home_team_stats = home_team_stats  # statystyki druzyny gospodarzy
        self.away_team_stats = away_team_stats  # statystyki druzyny gosci
        self.is_parse = False
        self.stadium_name = None
        self.match_day = None
        # dodatkowe struktury potrzebne do sparsowania raportu oraz wypisania go

    def parse(self, raport):
        self.is_parse = True
        self.home_team_stats.goals = []
        self.away_team_stats.goals = []
        self.home_team_stats.subs = []
        self.away_team_stats.subs = []
        self.home_team_stats.players = []
        self.away_team_stats.players = []
        self.home_team_stats.cards = []
        self.away_team_stats.cards = []
        self.home_team_stats.score = 0
        self.away_team_stats.score = 0
        lines_raport = raport.splitlines()  # podzielenie raportu na linie
        actual_line = 0
        temporary_team_name = None
        temporary_player_name = None
        temporary_action = None
        temporary_time = None
        head_line = None
        player_team_dict = {}  # slowniki jednoznacznie kojarzycze pilkarzy z druzynami

        head_line_regex = \
            re.compile(
                '^(.*?)(\s+)'  # nazwa druzyny gospodarzy
                '(vs|v\.)(\s+)'  # wyrazenie "kontra"
                '(.*?)(\s+)'  # nazwa druzyny gosci     
                '(@|at|venue)(\s+)'  # wyrazenie "gdzie"
                '(.*?)(\s+)'  # nazwa stadionu
                '(on)(\s+)'  # wyraznie "on"
                '(\d+\s+(JANUARY|FEBRUARY|MARCH|APRIL|MAY|JUNE|JULY|AUGUST|SEPTEMBER|OCTOBER|NOVEMBER|DECEMBER)\s+\d+)$', #data meczu
                re.IGNORECASE | re.VERBOSE
            )
        lineup_regex = re.compile('^(Lineup)(\s+)(.*)$', re.IGNORECASE)
        player_regex = re.compile('^(\d+)(\s+)(.*)$', re.IGNORECASE)
        game_regex = re.compile('^game$', re.IGNORECASE)
        sub_regex =\
            re.compile(
                '^(\d+|\d+\+\d+)(\'|m|min)(\s+)'    #minuta meczu
                '(sub|Substitution)(\s+)'           #wyrazenie "zmiana"
                '(out|off)(\s+)'                    #wyrazenie "off"
                '(.*?)(\s+)'                        #nazwa zawodnika schodzacego
                '(in|on)(\s+)'                      #wyrazenienie "in
                '(.*)$',                            #nazwa zawodnika wchodzacego
                re.IGNORECASE | re.VERBOSE
            )
        card_regex =\
            re.compile(
                '^(\d+|\d+\+\d+)(\'|m|min)(\s+)'  # minuta meczu
                '(card)(\s+)'  # wyrazenie "kartka"
                '(yellow|red|y|r)(\s+)'  # kolor kartki
                '(.*)'  # nazwa zawodnika 
                '$',
                re.IGNORECASE | re.VERBOSE
            )

        goal_regex = re.compile(        #do porpawy !!!!!!!!!
                '^(\d+|\d+\+\d+)(\'|m|min)(\s+)'  # minuta meczu
                '(g+o+a+l+)(\s+)'  # wyrazenie "goal"
                '(.*?)'  # nazwa zawodnika 
                '(\s*)(\(og\))?' #samoboj 
                '$',
                re.IGNORECASE | re.VERBOSE
            )

        # znalezienie naglowka
        while actual_line < len(lines_raport):
            if re.match(head_line_regex, lines_raport[actual_line]):
                head_line = re.search(head_line_regex, lines_raport[actual_line])
                self.home_team_stats.name = head_line.groups()[0]
                self.away_team_stats.name = head_line.groups()[4]
                self.stadium_name = head_line.groups()[8]
                self.match_day = head_line.groups()[12]
                break
            actual_line += 1
        actual_line += 1
        # wyznaczenie skladow druzyn
        while actual_line < len(lines_raport):
            if re.match(lineup_regex, lines_raport[actual_line]):
                temporary_team_name = re.search(lineup_regex, lines_raport[actual_line]).groups()[2]
                break
            actual_line += 1
        actual_line += 1
        if temporary_team_name == self.home_team_stats.name:
            # przypadek gdy pierwsza druzyna to druzyna gospodarzy
            for i in range(0, 11):
                temporary_player_name = re.search(player_regex, lines_raport[actual_line]).groups()[2]
                self.home_team_stats.players.append(temporary_player_name)
                player_team_dict[temporary_player_name] = self.home_team_stats.name
                actual_line += 1

            while actual_line < len(lines_raport):
                if re.match(lineup_regex, lines_raport[actual_line]):
                    break
                actual_line += 1
            actual_line += 1
            for i in range(0, 11):
                temporary_player_name = re.search(player_regex, lines_raport[actual_line]).groups()[2]
                self.away_team_stats.players.append(temporary_player_name)
                player_team_dict[temporary_player_name] = self.away_team_stats.name
                actual_line += 1
        else:
            # przypadek gdy pierwsza druzyna to druzyna gosci
            for i in range(0, 11):
                temporary_player_name = re.search(player_regex, lines_raport[actual_line]).groups()[2]
                self.away_team_stats.players.append(temporary_player_name)
                player_team_dict[temporary_player_name] = self.away_team_stats.name
                actual_line += 1

            while actual_line < len(lines_raport):
                if re.match(lineup_regex, lines_raport[actual_line]):
                    break
                actual_line += 1
            actual_line += 1
            for i in range(0, 11):
                temporary_player_name = re.search(player_regex, lines_raport[actual_line]).groups()[2]
                self.home_team_stats.players.append(temporary_player_name)
                player_team_dict[temporary_player_name] = self.home_team_stats.name
                actual_line += 1

        # wyznaczenie raportu minutowego
        while actual_line < len(lines_raport):
            if re.match(game_regex, lines_raport[actual_line]):
                break
            actual_line += 1
        actual_line += 1

        # odczyt raportu minutowego
        while actual_line < len(lines_raport):
            if re.match(sub_regex, lines_raport[actual_line]):
                temporary_action = re.search(sub_regex, lines_raport[actual_line]).groups()
                temporary_player_name = temporary_action[7]
                temporary_time = temporary_action[0]
                try:
                    player_team_dict[temporary_player_name]
                except:
                    actual_line += 1
                    continue
                if player_team_dict[temporary_player_name] == self.home_team_stats.name:
                    temporary_player_name = temporary_action[11]
                    player_team_dict[temporary_player_name] = self.home_team_stats.name
                    self.home_team_stats.subs.append(temporary_time + "' " + temporary_action[11] + " for " + temporary_action[7])
                else:
                    temporary_player_name = temporary_action[11]
                    player_team_dict[temporary_player_name] = self.away_team_stats.name
                    self.away_team_stats.subs.append(temporary_time + "' " + temporary_action[11] + " for " + temporary_action[7])
                actual_line += 1
                continue
            if re.match(card_regex, lines_raport[actual_line]):
                temporary_action = re.search(card_regex, lines_raport[actual_line]).groups()
                temporary_player_name = temporary_action[7]
                temporary_time = temporary_action[0]
                try:
                    player_team_dict[temporary_player_name]
                except:
                    actual_line += 1
                    continue
                if player_team_dict[temporary_player_name] == self.home_team_stats.name:
                    self.home_team_stats.cards.append(temporary_time + "' " + temporary_player_name + " " + temporary_action[5][0].capitalize())
                else:
                    self.away_team_stats.cards.append(temporary_time + "' " + temporary_player_name + " " + temporary_action[5][0].capitalize())
                actual_line += 1
                continue
            if re.match(goal_regex, lines_raport[actual_line]):
                temporary_action = re.search(goal_regex, lines_raport[actual_line]).groups()
                temporary_player_name = temporary_action[5]
                temporary_time = temporary_action[0]
                is_player_score = False
                try:
                    player_team_dict[temporary_player_name]
                except:
                    actual_line += 1
                    continue
                if player_team_dict[temporary_player_name] == self.home_team_stats.name:    #strzelec zawodnikiem druzyny gospodarzy
                    if temporary_action[7] is None:
                        self.home_team_stats.score += 1
                        for i in range(len(self.home_team_stats.goals)):
                            if " " + temporary_player_name == self.home_team_stats.goals[i][1]:
                                self.home_team_stats.goals[i][0] += ',' + (temporary_time + "'")
                                is_player_score = True
                                break
                        if not is_player_score:
                            self.home_team_stats.goals.append([temporary_time + "'", " " + temporary_player_name])
                    else:
                        self.away_team_stats.score += 1  # samoboj
                        for i in range(len(self.away_team_stats.goals)):
                            if " " + temporary_player_name == self.away_team_stats.goals[i][1]:
                                self.away_team_stats.goals[i][0] += ',' + (temporary_time + "'")
                                is_player_score = True
                                break
                        if not is_player_score:
                            self.away_team_stats.goals.append([temporary_time + "'", " " + temporary_player_name, " (OG)"])
                else:       #strzelec zawodnikiem druzyny gosci
                    if temporary_action[7] is None:
                        self.away_team_stats.score += 1
                        for i in range(len(self.away_team_stats.goals)):
                            if " " + temporary_player_name == self.away_team_stats.goals[i][1]:
                                self.away_team_stats.goals[i][0] += ',' + (temporary_time + "'")
                                is_player_score = True
                                break
                        if not is_player_score:
                            self.away_team_stats.goals.append([temporary_time + "'", " " + temporary_player_name])
                    else:
                        self.home_team_stats.score += 1     #samoboj
                        for i in range(len(self.home_team_stats.goals)):
                            if " " + temporary_player_name == self.home_team_stats.goals[i][1]:
                                self.home_team_stats.goals[i][0] += ',' + (temporary_time + "'")
                                is_player_score = True
                                break
                        if not is_player_score:
                            self.home_team_stats.goals.append([temporary_time + "'", " " + temporary_player_name, " (OG)"])
                actual_line += 1
                continue
            actual_line += 1
        return

    def print_summary(self):
        if self.is_parse:
            print "HOME:", self.home_team_stats.name
            print "AWAY:", self.away_team_stats.name
            print "SCORE: " + str(self.home_team_stats.get_score()) + ":" + str(self.away_team_stats.get_score())
            print "VENUE:", self.stadium_name
            print "DATE:", self.match_day
            print
            self.home_team_stats.print_summary()
            print
            self.away_team_stats.print_summary()
            return
        print "NO INFO AVAILABLE!"
        return