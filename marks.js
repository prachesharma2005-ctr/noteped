
const calculateFormEl = document.getElementById("CalculateForm");
const resultEl = document.getElementById("result");

calculateFormEl.addEventListener("submit", (event) => {
  event.preventDefault(); 

  const maxMarks = 400;
  const formData = new FormData(calculateFormEl);
  const data = {};

  formData.forEach((value, key) => {
    data[key] = Number(value);
  });


  const totalMarks = data.maths + data.english + data.hindi + data.science;
  const percentage = Math.round((totalMarks / maxMarks) * 100);

  
  resultEl.innerText = `You have got ${totalMarks} marks out of ${maxMarks} and your percentage is ${percentage}%`;
});
