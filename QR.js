let qrcode;

function generateQRCode() {
  const text = document.getElementById("text").value;

  if (!text) {
    alert("Please enter text or URL");
    return;
  }

  if (qrcode) {
    qrcode.clear();
    qrcode.makeCode(text);
  } else {
    qrcode = new QRCode(document.getElementById("qrcode"), {
      text: text,
      width: 200,
      height: 200,
      colorDark: "#000000",
      colorLight: "#ffffff",
      correctLevel: QRCode.CorrectLevel.H
    });
  }
}
