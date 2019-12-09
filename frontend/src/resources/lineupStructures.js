export const lineupStructures = {
  'fd': {
    'mlb': {
      'Classic': {
        'lineupMatrix': ['P', 'C 1B', '2B', '3B', 'SS', 'OF', 'OF', 'OF', 'C,1B,2B,3B,SS,OF'],
        'displayMatrix': ['P', 'C/1B', '2B', '3B', 'SS', 'OF', 'OF', 'OF', 'Util'],
        'salaryCap': 35000
      },
      'Single Game': {
        'lineupMatrix': ['any', 'any', 'any', 'any', 'any'],
        'displayMatrix': ['MVP - 1.5X Points', 'AnyFLEX', 'AnyFLEX', 'AnyFLEX', 'AnyFLEX'],
        'salaryCap': 35000
      }
    },
    'nfl': {
      'Classic': {
        'lineupMatrix': ['QB', 'RB', 'RB', 'WR', 'WR', 'WR', 'TE', 'RB,WR,TE', 'D'],
        'displayMatrix': ['QB', 'RB', 'RB', 'WR', 'WR', 'WR', 'TE', 'FLEX', 'D/ST'],
        'salaryCap': 60000
      },
      'Single Game': {
        'lineupMatrix': ['any', 'any', 'any', 'any', 'any'],
        'displayMatrix': ['MVP - 1.5X Points', 'AnyFLEX', 'AnyFLEX', 'AnyFLEX', 'AnyFLEX'],
        'salaryCap': 60000
      }
    },
    'nba': {
      'Classic': {
        'lineupMatrix': ['PG', 'PG', 'SG', 'SG', 'SF', 'SF', 'PF', 'PF', 'C'],
        'displayMatrix': ['PG', 'PG', 'SG', 'SG', 'SF', 'SF', 'PF', 'PF', 'C'],
        'salaryCap': 60000
      },
      'Single Game': {
        'lineupMatrix': ['any', 'any', 'any', 'any', 'any'],
        'displayMatrix': ['MVP - 2x Points', 'STAR - 1.5x Points', 'PRO (1.2x Points)', 'UTIL', 'UTIL'],
        'salaryCap': 60000
      }
    },
    'nhl': {
      'Classic': {
        'lineupMatrix': ['C', 'C', 'W', 'W', 'W', 'W', 'D', 'D', 'G'],
        'displayMatrix': ['C', 'C', 'W', 'W', 'W', 'W', 'D', 'D', 'G'],
        'salaryCap': 55000
      },
      'Single Game': {
        'lineupMatrix': ['any', 'any', 'any', 'any', 'any', 'any'],
        'displayMatrix': ['Captain - 1.5x Points', 'UTIL', 'UTIL', 'UTIL', 'UTIL', 'UTIL'],
        'salaryCap': 55000
      }
    }
  },
  'dk': {
    'mlb': {
      'Classic': {
        'lineupMatrix': ['P', 'P', 'C', '1B', '2B', '3B', 'SS', 'OF', 'OF', 'OF'],
        'displayMatrix': ['P', 'P', 'C', '1B', '2B', '3B', 'SS', 'OF', 'OF', 'OF'],
        'salaryCap': 50000
      },
      'Single Game': {
        'lineupMatrix': ['any', 'any', 'any', 'any', 'any', 'any'],
        'displayMatrix': ['Captain (1.5x Points)', 'FLEX', 'FLEX', 'FLEX', 'FLEX', 'FLEX'],
        'salaryCap': 50000
      }
    },
    'nfl': {
      'Classic': {
        'lineupMatrix': ['QB', 'RB', 'RB', 'WR', 'WR', 'WR', 'TE', 'RB,WR,TE', 'DST'],
        'displayMatrix': ['QB', 'RB', 'RB', 'WR', 'WR', 'WR', 'TE', 'FLEX', 'D/ST'],
        'salaryCap': 50000
      },
      'Single Game': {
        'lineupMatrix': ['any', 'any', 'any', 'any', 'any', 'any'],
        'displayMatrix': ['Captain (1.5x Points)', 'FLEX', 'FLEX', 'FLEX', 'FLEX', 'FLEX'],
        'salaryCap': 50000
      }
    },
    'nba': {
      'Classic': {
        'lineupMatrix': ['PG', 'SG', 'SF', 'PF', 'C', 'PG,SG', 'SF,PF', 'PG,SG,SF,PF,C'],
        'displayMatrix': ['PG', 'SG', 'SF', 'PF', 'C', 'G', 'F', 'Util'],
        'salaryCap': 50000
      },
      'Single Game': {
        'lineupMatrix': ['any', 'any', 'any', 'any', 'any', 'any'],
        'displayMatrix': ['Captain (1.5x Points)', 'FLEX', 'FLEX', 'FLEX', 'FLEX', 'FLEX'],
        'salaryCap': 50000
      }
    },
    'nhl': {
      'Classic': {
        'lineupMatrix': ['C', 'C', 'LW,RW', 'LW,RW', 'LW,RW', 'D', 'D', 'G', 'LW,RW,C,D'],
        'displayMatrix': ['C', 'C', 'W', 'W', 'W', 'D', 'D', 'G', 'Util'],
        'salaryCap': 50000
      },
      'Single Game': {
        'lineupMatrix': ['any', 'any', 'any', 'any', 'any', 'any'],
        'displayMatrix': ['Captain (1.5x Points)', 'FLEX', 'FLEX', 'FLEX', 'FLEX', 'FLEX'],
        'salaryCap': 50000
      }
    }
  }
};
