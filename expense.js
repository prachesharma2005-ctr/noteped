const balance = document.getElementById('balance');
const income = document.getElementById('income');
const expense = document.getElementById('expense');
const transactionList = document.getElementById('transactionList');
const form = document.getElementById('form');
const text = document.getElementById('text');
const amount = document.getElementById('amount');

let transactions = JSON.parse(localStorage.getItem('transactions')) || [];

function addTransaction(e) {
  e.preventDefault();

  const newTransaction = {
    id: generateID(),
    text: text.value,
    amount: +amount.value
  };

  transactions.push(newTransaction);
  updateUI();
  updateLocalStorage();

  text.value = '';
  amount.value = '';
}
function generateID() {
  return Math.floor(Math.random() * 1000000);
}

function removeTransaction(id) {
  transactions = transactions.filter(tx => tx.id !== id);
  updateUI();
  updateLocalStorage();
}

function updateUI() {
  transactionList.innerHTML = '';

  transactions.forEach(tx => {
    const sign = tx.amount < 0 ? '-' : '+';
    const item = document.createElement('li');
    item.classList.add(tx.amount < 0 ? 'minus' : 'plus');

    item.innerHTML = `
      ${tx.text} <span>${sign}$${Math.abs(tx.amount)}</span>
      <button onclick="removeTransaction(${tx.id})">x</button>
    `;

    transactionList.appendChild(item);
  });

  updateSummary();
}

function updateSummary() {
  const amounts = transactions.map(tx => tx.amount);
  const total = amounts.reduce((acc, val) => acc + val, 0).toFixed(2);
  const incomeTotal = amounts.filter(v => v > 0).reduce((acc, val) => acc + val, 0).toFixed(2);
  const expenseTotal = amounts.filter(v => v < 0).reduce((acc, val) => acc + val, 0).toFixed(2);

  balance.innerText = `$${total}`;
  income.innerText = `+$${incomeTotal}`;
  expense.innerText = `-$${Math.abs(expenseTotal)}`;
}
function updateLocalStorage() {
  localStorage.setItem('transactions', JSON.stringify(transactions));
}

form.addEventListener('submit', addTransaction);
updateUI();
