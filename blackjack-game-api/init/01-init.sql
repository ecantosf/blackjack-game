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

CREATE TABLE IF NOT EXISTS games (
    id VARCHAR(36) PRIMARY KEY,
    player_id VARCHAR(36) NOT NULL,
    bet INT NOT NULL,
    status VARCHAR(20) NOT NULL,
    winner VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    finished_at TIMESTAMP NULL,
    FOREIGN KEY (player_id) REFERENCES players(id) ON DELETE CASCADE,
    INDEX idx_player_id (player_id),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at DESC)
);

CREATE TABLE IF NOT EXISTS cards (
    id INT AUTO_INCREMENT PRIMARY KEY,
    game_id VARCHAR(36) NOT NULL,
    hand_type VARCHAR(10) NOT NULL,
    suit VARCHAR(10) NOT NULL,
    card_rank VARCHAR(5) NOT NULL,
    card_value INT NOT NULL,
    FOREIGN KEY (game_id) REFERENCES games(id) ON DELETE CASCADE,
    INDEX idx_game_id (game_id),
    INDEX idx_hand_type (hand_type)
);

CREATE USER IF NOT EXISTS 'blackjack_user'@'%' IDENTIFIED BY 'blackjack_pass';
GRANT ALL PRIVILEGES ON blackjack.* TO 'blackjack_user'@'%';
FLUSH PRIVILEGES;

CREATE TABLE IF NOT EXISTS ranking_cache (
    player_id VARCHAR(36) PRIMARY KEY,
    player_name VARCHAR(50) NOT NULL,
    total_games INT NOT NULL,
    games_won INT NOT NULL,
    total_points INT NOT NULL,
    win_rate DECIMAL(5,2) NOT NULL,
    calculated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_win_rate (win_rate DESC),
    INDEX idx_total_points (total_points DESC)
);