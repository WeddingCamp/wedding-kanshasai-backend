package wedding.kanshasai.backend.domain.constant

enum class Table(val tableName: String) {
    EVENT("Event"),
    QUIZ("Quiz"),
    SESSION("Session"),
    SESSION_QUIZ("SessionQuiz"),
    PARTICIPANT("Participant"),
    PARTICIPANT_ANSWER("ParticipantAnswer"),
    CHOICE("ParticipantAnswer")
}