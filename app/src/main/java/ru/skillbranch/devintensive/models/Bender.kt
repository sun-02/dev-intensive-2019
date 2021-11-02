package ru.skillbranch.devintensive.models

class Bender(var status: Status = Status.NORMAL, var question: Question = Question.NAME) {

    fun askQuestion(): String = when (question) {
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question
    }

    fun listenAnswer(answer: String): Pair<String, Triple<Int, Int, Int>> {
        if (!question.validate(answer)) {
            return "${question.notValidMsg}\n${question.question}" to status.color
        }

        if (question == Question.IDLE) {
            return question.question to status.color
        }

        if (question.answers.contains(answer)) {
            question = question.nextQuestion()
            return "Отлично - ты справился\n${question.question}" to status.color
        }

        status = status.nextStatus()
        if (status == Status.NORMAL) {
            question = Question.NAME
            return "Это неправильный ответ. Давай все по новой\n${question.question}" to status.color
        }

        return "Это неправильный ответ\n${question.question}" to status.color



    }

    enum class Status(val color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)) {
            override fun nextStatus() = WARNING
        },
        WARNING(Triple(255, 120, 0)) {
            override fun nextStatus() = DANGER
        },
        DANGER(Triple(255, 60, 60)) {
            override fun nextStatus() = CRITICAL
        },
        CRITICAL(Triple(255, 0, 0)) {
            override fun nextStatus() = NORMAL
        };

        abstract fun nextStatus(): Status
    }

    enum class Question(val question: String, val answers: List<String>, val notValidMsg: String) {
        NAME(
            "Как меня зовут?",
            listOf("Бендер", "Bender"),
            "Имя должно начинаться с заглавной буквы")  {
            override fun nextQuestion() = PROFESSION

            override fun validate(string: String): Boolean {
                return string[0].isUpperCase()
            }
        },
        PROFESSION(
            "Назови мою профессию?",
            listOf("сгибальщик", "bender"),
            "Профессия должна начинаться со строчной буквы"
        ) {
            override fun nextQuestion() = MATERIAL

            override fun validate(string: String): Boolean {
                return string[0].isLowerCase()
            }
        },
        MATERIAL(
            "Из чего я сделан?",
            listOf("металл", "дерево", "metal", "iron", "wood"),
            "Материал не должен содержать цифр"
        ) {
            override fun nextQuestion() = BDAY

            override fun validate(string: String): Boolean {
                return !string.contains("\\d".toRegex())
            }
        },
        BDAY(
            "Когда меня создали?",
            listOf("2993"),
            "Год моего рождения должен содержать только цифры"
        ) {
            override fun nextQuestion() = SERIAL

            override fun validate(string: String): Boolean {
                return !string.contains("\\D".toRegex())
            }
        },
        SERIAL(
            "Мой серийный номер?",
            listOf("2716057"),
            "Серийный номер содержит только цифры, и их 7"
        ) {
            override fun nextQuestion() = IDLE

            override fun validate(string: String): Boolean {
                return string.length == 7 && string.contains("\\d{7}".toRegex())
            }
        },
        IDLE(

            "На этом все, вопросов больше нет",
            listOf(),
            ""
        ) {
            override fun nextQuestion() = IDLE

            override fun validate(string: String): Boolean {
                return true
            }
        };

        abstract fun nextQuestion(): Question

        abstract fun validate(string: String): Boolean
    }
}