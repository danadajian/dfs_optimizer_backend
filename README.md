# DFS Optimizer Backend ![danadajian](https://circleci.com/gh/danadajian/dfs_optimizer_backend.svg?style=svg)

### Overview

My Daily Fantasy Sports (DFS) lineup optimizer aims to generate a lineup of players such that the lineup's total 
projected points are maximized, given the constraint that the lineup's total salary is within the salary cap. Each 
player has a position, a projection, and a salary, and a lineup must contain a certain number of each position. The 
optimization problem presented here is a type of knapsack problem, which involves picking items with weights and values 
optimally to fit inside a knapsack.

![dfs-optimizer-example](https://github.com/danadajian/danadajian.github.io/blob/master/src/images/dfs-optimizer-example.png)

### My Solution

The method I developed uses what I call "selective brute force", which reduces the problem efficiently to make checking 
all possibilities feasible. First, players are grouped by position and sorted by their salary-to-projection ratios. 
Then, players are removed from each position pool until the maximum number of lineup combinations is under a specified 
fixed threshold. Finally, a recursive algorithm efficiently iterates over possible lineups one by one, ensuring each 
new lineup it checks has the potential to be better (i.e. has a higher projected point total).

### Support

The app currently supports Fanduel and DraftKings contests, and scrapes data from multiple sources.
