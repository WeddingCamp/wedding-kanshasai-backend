CREATE TABLE IF NOT EXISTS
  events (
    id BINARY(16) NOT NULL,
    name VARCHAR(100) NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAUlT false,
    created_at DATETIME NOT NULL DEFAUlT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAUlT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY(id)
  );

CREATE TABLE IF NOT EXISTS
  sessions (
    id BINARY(16) NOT NULL,
    event_id BINARY(16) NOT NULL,
    name VARCHAR(100) NOT NULL,
    state_id INTEGER NOT NULL DEFAULT 1,
    current_quiz_id BINARY(16) NULL,
    current_introduction_id INTEGER NOT NULL DEFAULT 1,
    current_quiz_result_id INTEGER NULL,

    result_state_id INTEGER NOT NULL DEFAULT 1,
    result_rank_state_id INTEGER NOT NULL DEFAULT 1,
    result_rank INTEGER NOT NULL DEFAULT 100,

    is_cover_visible BOOLEAN NOT NULL DEFAULT false,
    is_deleted BOOLEAN NOT NULL DEFAUlT false,
    created_at DATETIME NOT NULL DEFAUlT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAUlT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY(id)
  );

CREATE TABLE IF NOT EXISTS
  quizzes (
    id BINARY(16) NOT NULL,
    event_id BINARY(16) NOT NULL,
    body VARCHAR(100) NOT NULL,
    correct_answer TEXT NOT NULL,
    type INTEGER NOT NULL DEFAUlT 0,
    is_deleted BOOLEAN NOT NULL DEFAUlT false,
    created_at DATETIME NOT NULL DEFAUlT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAUlT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY(id)
  );

CREATE TABLE IF NOT EXISTS
  participants (
    id BINARY(16) NOT NULL,
    session_id BINARY(16) NOT NULL,
    name VARCHAR(100) NOT NULL,
    image_id BINARY(16) NULL,
    type INTEGER NOT NULL DEFAUlT 0,
    is_connected BOOLEAN NOT NULL DEFAUlT false,
    is_deleted BOOLEAN NOT NULL DEFAUlT false,
    created_at DATETIME NOT NULL DEFAUlT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAUlT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY(id)
  );

CREATE TABLE IF NOT EXISTS
  choices (
    id BINARY(16) NOT NULL,
    quiz_id BINARY(16) NOT NULL,
    body text NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAUlT false,
    created_at DATETIME NOT NULL DEFAUlT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAUlT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY(id)
  );

CREATE TABLE IF NOT EXISTS
  session_quizzes (
    session_id BINARY(16) NOT NULL,
    quiz_id BINARY(16) NOT NULL,
    is_completed BOOLEAN NOT NULL DEFAUlT false,
    started_at DATETIME NULL,
    correct_answer TEXT NULL,
    is_deleted BOOLEAN NOT NULL DEFAUlT false,
    created_at DATETIME NOT NULL DEFAUlT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAUlT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY(session_id, quiz_id)
  );

CREATE TABLE IF NOT EXISTS
  participant_answers (
    participant_id BINARY(16) NOT NULL,
    session_id BINARY(16) NOT NULL,
    quiz_id BINARY(16) NOT NULL,
    answer TEXT NOT NULL,
    time FLOAT NOT NULL,
    is_correct BOOLEAN NOT NULL DEFAUlT false,
    is_deleted BOOLEAN NOT NULL DEFAUlT false,
    created_at DATETIME NOT NULL DEFAUlT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAUlT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY(participant_id, session_id, quiz_id)
  );

ALTER TABLE sessions ADD FOREIGN KEY (event_id) REFERENCES events(id);
ALTER TABLE quizzes ADD FOREIGN KEY (event_id) REFERENCES events(id);
ALTER TABLE participants ADD FOREIGN KEY (session_id) REFERENCES sessions(id);
ALTER TABLE choices ADD FOREIGN KEY (quiz_id) REFERENCES quizzes(id);
ALTER TABLE session_quizzes ADD FOREIGN KEY (session_id) REFERENCES sessions(id);
ALTER TABLE session_quizzes ADD FOREIGN KEY (quiz_id) REFERENCES quizzes(id);
ALTER TABLE participant_answers ADD FOREIGN KEY (session_id, quiz_id) REFERENCES session_quizzes(session_id, quiz_id);
ALTER TABLE participant_answers ADD FOREIGN KEY (participant_id) REFERENCES participants(id);
