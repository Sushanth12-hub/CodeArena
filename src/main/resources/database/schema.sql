CREATE TABLE IF NOT EXISTS users (
  user_id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(80) NOT NULL,
  email VARCHAR(120) NOT NULL UNIQUE,
  password_hash VARCHAR(64) NOT NULL,
  role VARCHAR(10) NOT NULL CHECK (role IN ('STUDENT', 'ADMIN'))
);
CREATE TABLE IF NOT EXISTS subjects (
  subject_id INT AUTO_INCREMENT PRIMARY KEY,
  subject_name VARCHAR(80) NOT NULL UNIQUE,
  description VARCHAR(255) NOT NULL
);
CREATE TABLE IF NOT EXISTS topics (
  topic_id INT AUTO_INCREMENT PRIMARY KEY,
  subject_id INT NOT NULL,
  topic_name VARCHAR(100) NOT NULL,
  description VARCHAR(255) NOT NULL,
  FOREIGN KEY (subject_id) REFERENCES subjects(subject_id)
);
CREATE TABLE IF NOT EXISTS learning_material (
  material_id INT AUTO_INCREMENT PRIMARY KEY,
  topic_id INT NOT NULL UNIQUE,
  content CLOB NOT NULL,
  FOREIGN KEY (topic_id) REFERENCES topics(topic_id)
);
CREATE TABLE IF NOT EXISTS questions (
  question_id INT AUTO_INCREMENT PRIMARY KEY,
  topic_id INT NOT NULL,
  question_text VARCHAR(500) NOT NULL,
  question_type VARCHAR(20) NOT NULL DEFAULT 'MCQ',
  correct_answer VARCHAR(10) NOT NULL,
  marks INT NOT NULL DEFAULT 1,
  explanation VARCHAR(500),
  FOREIGN KEY (topic_id) REFERENCES topics(topic_id)
);
CREATE TABLE IF NOT EXISTS options (
  option_id INT AUTO_INCREMENT PRIMARY KEY,
  question_id INT NOT NULL,
  option_label CHAR(1) NOT NULL,
  option_text VARCHAR(300) NOT NULL,
  UNIQUE(question_id, option_label),
  FOREIGN KEY (question_id) REFERENCES questions(question_id)
);
CREATE TABLE IF NOT EXISTS attempts (
  attempt_id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  topic_id INT NOT NULL,
  score INT NOT NULL,
  total_marks INT NOT NULL,
  attempt_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(user_id),
  FOREIGN KEY (topic_id) REFERENCES topics(topic_id)
);
CREATE TABLE IF NOT EXISTS student_answers (
  answer_id INT AUTO_INCREMENT PRIMARY KEY,
  attempt_id INT NOT NULL,
  question_id INT NOT NULL,
  submitted_answer VARCHAR(20),
  is_correct BOOLEAN NOT NULL,
  FOREIGN KEY (attempt_id) REFERENCES attempts(attempt_id),
  FOREIGN KEY (question_id) REFERENCES questions(question_id)
);
