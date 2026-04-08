const result = document.getElementById('result');
const copyBtn = document.getElementById('copy');
const lengthEl = document.getElementById('length');
const uppercaseEl = document.getElementById('uppercase');
const lowercaseEl = document.getElementById('lowercase');
const numbersEl = document.getElementById('numbers');
const symbolsEl = document.getElementById('symbols');
const generateBtn = document.getElementById('generate');

const randomFunc = {
  lower: () => String.fromCharCode(Math.floor(Math.random() * 26) + 97),
  upper: () => String.fromCharCode(Math.floor(Math.random() * 26) + 65),
  number: () => String.fromCharCode(Math.floor(Math.random() * 10) + 48),
  symbol: () => "!@#$%^&*()_+{}[]<>?~".charAt(Math.floor(Math.random() * 18)),
};

function generatePassword(length, upper, lower, number, symbol) {
  let generated = '';
  const typesArr = [
    upper && { func: randomFunc.upper },
    lower && { func: randomFunc.lower },
    number && { func: randomFunc.number },
    symbol && { func: randomFunc.symbol },
  ].filter(Boolean);

  if (typesArr.length === 0) return '';

  for (let i = 0; i < length; i++) {
    const randType = typesArr[Math.floor(Math.random() * typesArr.length)];
    generated += randType.func();
  }

  return generated;
}


generateBtn.addEventListener('click', () => {
  const length = +lengthEl.value;
  const hasUpper = uppercaseEl.checked;
  const hasLower = lowercaseEl.checked;
  const hasNumber = numbersEl.checked;
  const hasSymbol = symbolsEl.checked;

  const password = generatePassword(length, hasUpper, hasLower, hasNumber, hasSymbol);
  result.value = password;
});

copyBtn.addEventListener('click', () => {
  if (!result.value) return;

  navigator.clipboard.writeText(result.value)
    .then(() => alert('Password copied!'))
    .catch(() => alert('Failed to copy'));
});
