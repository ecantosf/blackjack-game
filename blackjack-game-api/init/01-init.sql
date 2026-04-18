CREATE DATABASE IF NOT EXISTS blackjack;
USE blackjack;

CREATE TABLE IF NOT EXISTS players (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    total_games INT NOT NULL DEFAULT 0,
    games_won INT NOT NULL DEFAULT 0,
    total_points INT NOT NULL DEFAULT 0,
    INDEX idx_name (name),
    INDEX idx_total_points (total_points DESC)
);

CREATE USER IF NOT EXISTS 'blackjack_user'@'%' IDENTIFIED BY 'blackjack_pass';
GRANT ALL PRIVILEGES ON blackjack.* TO 'blackjack_user'@'%';
FLUSH PRIVILEGES;