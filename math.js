let num1, num2, operator, correctAnswer;
let score = 0;

function generateQuestion() {
  const operators = ['+', '-', '*'];
  num1 = Math.floor(Math.random() * 10) + 1;
  num2 = Math.floor(Math.random() * 10) + 1;
  operator = operators[Math.floor(Math.random() * operators.length)];

  switch (operator) {
    case '+': correctAnswer = num1 + num2; break;
    case '-': correctAnswer = num1 - num2; break;
    case '*': correctAnswer = num1 * num2; break;
  }

  document.getElementById('question').textContent = `What is ${num1} ${operator} ${num2}?`;
  document.getElementById('answer').value = '';
  document.getElementById('result').textContent = '';
}

function checkAnswer() {
  const userAnswer = parseInt(document.getElementById('answer').value);
  if (userAnswer === correctAnswer) {
    document.getElementById('result').textContent = 'Correct!';
    score++;
  } else {
    document.getElementById('result').textContent = `Wrong! The correct answer was ${correctAnswer}.`;
  }
  document.getElementById('score').textContent = `Score: ${score}`;
  generateQuestion();
}

generateQuestion();
