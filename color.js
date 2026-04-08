function getRandomColor() {
  const hex = Math.floor(Math.random() * 0xffffff).toString(16).padStart(6, '0');
  return `#${hex}`;
}

function generatePalette() {
  const palette = document.getElementById('palette');
  palette.innerHTML = ''; // Clear old colors

  for (let i = 0; i < 5; i++) {
    const color = getRandomColor();
    const colorBox = document.createElement('div');
    colorBox.className = 'color';
    colorBox.style.backgroundColor = color;
    colorBox.innerHTML = `<span>${color}</span>`;

    colorBox.addEventListener('click', () => {
      navigator.clipboard.writeText(color).then(() => {
        alert(`Copied ${color} to clipboard!`);
      });
    });

    palette.appendChild(colorBox);
  }
}
generatePalette();
