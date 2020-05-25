import {State} from "./interfaces";

export const INITIAL_STATE: State = {
    isLoading: false,
    isOptimizing: false,
    date: new Date(),
    maxCombinations: 10000000,
    salaryCap: 0,
    site: '',
    sport: '',
    contest: '',
    loadingText: '',
    searchText: '',
    sortValue: 'All',
    playerPool: [],
    filteredPool: [],
    whiteList: [],
    blackList: [],
    lineup: [],
    lineupPositions: [],
    displayMatrix: [],
    contests: [],
    playerStatuses: [],
    dfsData: [],
    injuries: {},
    lineupRestrictions: {},
    opponentRanks: {},
    projectionsData: {}
};
export const INJURY_ABBREVIATIONS: any = {
    'questionable': 'Q',
    'out': 'O',
    'day-to-day': 'DTD',
    'suspended': 'SUSP'
};
export const LINEUP_RULES: any = {
    'Fanduel': {
        'mlb': {
            'Classic': {
                'lineupPositions': ['P', 'C,1B', '2B', '3B', 'SS', 'OF', 'OF', 'OF', 'C,1B,2B,3B,SS,OF'],
                'displayMatrix': ['P', 'C/1B', '2B', '3B', 'SS', 'OF', 'OF', 'OF', 'Util'],
                'salaryCap': 35000,
                'lineupRestrictions': {
                    'distinctTeamsRequired': 3,
                    'maxPlayersPerTeam': 4,
                    'teamAgnosticPosition': 'P'
                }
            },
            'Single Game': {
                'lineupPositions': ['any', 'any', 'any', 'any', 'any'],
                'displayMatrix': ['MVP - 1.5X Points', 'AnyFLEX', 'AnyFLEX', 'AnyFLEX', 'AnyFLEX'],
                'salaryCap': 35000,
                'lineupRestrictions': {
                    'distinctTeamsRequired': 2,
                    'maxPlayersPerTeam': 4,
                    'teamAgnosticPosition': ''
                }
            }
        },
        'nfl': {
            'Classic': {
                'lineupPositions': ['QB', 'RB', 'RB', 'WR', 'WR', 'WR', 'TE', 'RB,WR,TE', 'D'],
                'displayMatrix': ['QB', 'RB', 'RB', 'WR', 'WR', 'WR', 'TE', 'FLEX', 'D/ST'],
                'salaryCap': 60000,
                'lineupRestrictions': {
                    'distinctTeamsRequired': 3,
                    'maxPlayersPerTeam': 4,
                    'teamAgnosticPosition': ''
                }
            },
            'Single Game': {
                'lineupPositions': ['any', 'any', 'any', 'any', 'any'],
                'displayMatrix': ['MVP - 1.5X Points', 'AnyFLEX', 'AnyFLEX', 'AnyFLEX', 'AnyFLEX'],
                'salaryCap': 60000,
                'lineupRestrictions': {
                    'distinctTeamsRequired': 2,
                    'maxPlayersPerTeam': 4,
                    'teamAgnosticPosition': ''
                }
            }
        },
        'nba': {
            'Classic': {
                'lineupPositions': ['PG', 'PG', 'SG', 'SG', 'SF', 'SF', 'PF', 'PF', 'C'],
                'displayMatrix': ['PG', 'PG', 'SG', 'SG', 'SF', 'SF', 'PF', 'PF', 'C'],
                'salaryCap': 60000,
                'lineupRestrictions': {
                    'distinctTeamsRequired': 3,
                    'maxPlayersPerTeam': 4,
                    'teamAgnosticPosition': ''
                }
            },
            'Single Game': {
                'lineupPositions': ['any', 'any', 'any', 'any', 'any'],
                'displayMatrix': ['MVP - 2x Points', 'STAR - 1.5x Points', 'PRO - 1.2x Points', 'UTIL', 'UTIL'],
                'salaryCap': 60000,
                'lineupRestrictions': {
                    'distinctTeamsRequired': 2,
                    'maxPlayersPerTeam': 4,
                    'teamAgnosticPosition': ''
                }
            }
        },
        'nhl': {
            'Classic': {
                'lineupPositions': ['C', 'C', 'W', 'W', 'W', 'W', 'D', 'D', 'G'],
                'displayMatrix': ['C', 'C', 'W', 'W', 'W', 'W', 'D', 'D', 'G'],
                'salaryCap': 55000,
                'lineupRestrictions': {
                    'distinctTeamsRequired': 3,
                    'maxPlayersPerTeam': 4,
                    'teamAgnosticPosition': ''
                }
            },
            'Single Game': {
                'lineupPositions': ['any', 'any', 'any', 'any', 'any', 'any'],
                'displayMatrix': ['Captain - 1.5x Points', 'UTIL', 'UTIL', 'UTIL', 'UTIL', 'UTIL'],
                'salaryCap': 55000,
                'lineupRestrictions': {
                    'distinctTeamsRequired': 2,
                    'maxPlayersPerTeam': 5,
                    'teamAgnosticPosition': ''
                }
            }
        }
    },
    'DraftKings': {
        'mlb': {
            'Classic': {
                'lineupPositions': ['P', 'P', 'C', '1B', '2B', '3B', 'SS', 'OF', 'OF', 'OF'],
                'displayMatrix': ['P', 'P', 'C', '1B', '2B', '3B', 'SS', 'OF', 'OF', 'OF'],
                'salaryCap': 50000,
                'lineupRestrictions': {
                    'distinctTeamsRequired': 3,
                    'maxPlayersPerTeam': 5,
                    'teamAgnosticPosition': 'P'
                }
            },
            'Single Game': {
                'lineupPositions': ['any', 'any', 'any', 'any', 'any', 'any'],
                'displayMatrix': ['Captain (1.5x Points)', 'FLEX', 'FLEX', 'FLEX', 'FLEX', 'FLEX'],
                'salaryCap': 50000,
                'lineupRestrictions': {
                    'distinctTeamsRequired': 2,
                    'maxPlayersPerTeam': 5,
                    'teamAgnosticPosition': ''
                }
            }
        },
        'nfl': {
            'Classic': {
                'lineupPositions': ['QB', 'RB', 'RB', 'WR', 'WR', 'WR', 'TE', 'RB,WR,TE', 'DST'],
                'displayMatrix': ['QB', 'RB', 'RB', 'WR', 'WR', 'WR', 'TE', 'FLEX', 'D/ST'],
                'salaryCap': 50000,
                'lineupRestrictions': {
                    'distinctTeamsRequired': 3,
                    'maxPlayersPerTeam': 8,
                    'teamAgnosticPosition': ''
                }
            },
            'Single Game': {
                'lineupPositions': ['any', 'any', 'any', 'any', 'any', 'any'],
                'displayMatrix': ['Captain (1.5x Points)', 'FLEX', 'FLEX', 'FLEX', 'FLEX', 'FLEX'],
                'salaryCap': 50000,
                'lineupRestrictions': {
                    'distinctTeamsRequired': 2,
                    'maxPlayersPerTeam': 5,
                    'teamAgnosticPosition': ''
                }
            }
        },
        'nba': {
            'Classic': {
                'lineupPositions': ['PG', 'SG', 'SF', 'PF', 'C', 'PG,SG', 'SF,PF', 'PG,SG,SF,PF,C'],
                'displayMatrix': ['PG', 'SG', 'SF', 'PF', 'C', 'G', 'F', 'Util'],
                'salaryCap': 50000,
                'lineupRestrictions': {
                    'distinctTeamsRequired': 3,
                    'maxPlayersPerTeam': 7,
                    'teamAgnosticPosition': ''
                }
            },
            'Single Game': {
                'lineupPositions': ['any', 'any', 'any', 'any', 'any', 'any'],
                'displayMatrix': ['Captain (1.5x Points)', 'FLEX', 'FLEX', 'FLEX', 'FLEX', 'FLEX'],
                'salaryCap': 50000,
                'lineupRestrictions': {
                    'distinctTeamsRequired': 2,
                    'maxPlayersPerTeam': 5,
                    'teamAgnosticPosition': ''
                }
            }
        },
        'nhl': {
            'Classic': {
                'lineupPositions': ['C', 'C', 'LW,RW', 'LW,RW', 'LW,RW', 'D', 'D', 'G', 'LW,RW,C,D'],
                'displayMatrix': ['C', 'C', 'W', 'W', 'W', 'D', 'D', 'G', 'Util'],
                'salaryCap': 50000,
                'lineupRestrictions': {
                    'distinctTeamsRequired': 3,
                    'maxPlayersPerTeam': 8,
                    'teamAgnosticPosition': 'G'
                }
            },
            'Single Game': {
                'lineupPositions': ['any', 'any', 'any', 'any', 'any', 'any'],
                'displayMatrix': ['Captain (1.5x Points)', 'FLEX', 'FLEX', 'FLEX', 'FLEX', 'FLEX'],
                'salaryCap': 50000,
                'lineupRestrictions': {
                    'distinctTeamsRequired': 2,
                    'maxPlayersPerTeam': 5,
                    'teamAgnosticPosition': ''
                }
            }
        }
    }
};
export const TEAM_ABBREVIATIONS: any = {
    'Ari': 'Arizona Cardinals',
    'Atl': 'Atlanta Falcons',
    'Bal': 'Baltimore Ravens',
    'Buf': 'Buffalo Bills',
    'Car': 'Carolina Panthers',
    'Chi': 'Chicago Bears',
    'Cin': 'Cincinnati Bengals',
    'Cle': 'Cleveland Browns',
    'Dal': 'Dallas Cowboys',
    'Den': 'Denver Broncos',
    'Det': 'Detroit Lions',
    'GB': 'Green Bay Packers',
    'Hou': 'Houston Texans',
    'Ind': 'Indianapolis Colts',
    'Jax': 'Jacksonville Jaguars',
    'KC': 'Kansas City Chiefs',
    'LAC': 'Los Angeles Chargers',
    'LAR': 'Los Angeles Rams',
    'Mia': 'Miami Dolphins',
    'Min': 'Minnesota Vikings',
    'NE': 'New England Patriots',
    'NO': 'New Orleans Saints',
    'NYG': 'New York Giants',
    'NYJ': 'New York Jets',
    'Oak': 'Oakland Raiders',
    'Phi': 'Philadelphia Eagles',
    'Pit': 'Pittsburgh Steelers',
    'SF': 'San Francisco 49ers',
    'Sea': 'Seattle Seahawks',
    'TB': 'Tampa Bay Buccaneers',
    'Ten': 'Tennessee Titans',
    'Was': 'Washington'
};
export const SUPPORTED_SPORTS = ['mlb', 'nfl', 'nba', 'nhl'];
export const KNAPSACK_PROBLEM_LINK = "https://en.wikipedia.org/wiki/Knapsack_problem";
export const isDevelopment = () => !process.env.NODE_ENV || process.env.NODE_ENV === 'development'
