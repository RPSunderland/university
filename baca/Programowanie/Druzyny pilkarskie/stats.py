# Rafal Plizga

class TeamStats:
    def __init__(self):
        self.name = None  # nazwa druzyny
        self.players = None  # informacje o zawodnikach, nazwa oraz numer koszulki
        self.goals = None  # informacje o golach (trudne)
        self.cards = None  # informacje o kartkach (trudne)
        self.subs = None  # informacje o zmiaaach
        self.score = None  # informacja o liczbie bramek
        return

    def get_players(self):
        if self.players is None:
            return "NO INFO AVAILABLE!"
        res = "PLAYERS:"
        for i in range(len(self.players)):
            res += "\n" + self.players[i]
        return res

    def get_goals(self):
        if self.goals is None:
            return "NO INFO AVAILABLE!"
        res = "GOALS:"
        for i in range(len(self.goals)):
            res += "\n" + str.join('', self.goals[i])
        return res

    def get_cards(self):
        if self.cards is None:
            return "NO INFO AVAILABLE!"
        res = "CARDS:"
        for i in range(len(self.cards)):
            res += "\n" + self.cards[i]
        return res

    def get_subs(self):
        if self.subs is None:
            return "NO INFO AVAILABLE!"
        res = "SUBS:"
        for i in range(len(self.subs)):
            res += "\n" + self.subs[i]
        return res

    def get_score(self):
        if self.score is None:
            return "NO INFO AVAILABLE!"
        return self.score

    def print_summary(self):
        if self.players is None or self.goals is None or self.cards is None or self.subs is None or self.score is None:
            print "NO INFO AVAILABLE!"
            return
        print "TEAM", self.name
        print self.get_players()
        print self.get_goals()
        print self.get_cards()
        print self.get_subs()
        return
