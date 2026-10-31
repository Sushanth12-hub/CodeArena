# CodeArena diagrams

## Use cases

```text
Student -> register, log in, browse subjects, read notes, take quiz, see history, log out
Admin   -> log in, view results, add a question, log out
```

## ER diagram

```text
users 1 --- * attempts * --- 1 topics * --- 1 subjects
topics 1 --- * questions 1 --- * options
attempts 1 --- * student_answers * --- 1 questions
topics 1 --- * learning_material
```

## Request flow

```text
Browser -> Router -> Service -> DAO -> JDBC PreparedStatement -> H2 database
   ^                                                            |
   +-------------------------- HTML response -------------------+
```
