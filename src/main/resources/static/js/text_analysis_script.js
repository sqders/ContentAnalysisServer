async function submitTextAnalysis() {
    let analyzingText;
    analyzingText = {
        label: document.querySelector('input[name="analysisOption"]:checked').value,
        text: document.getElementById("inputText").value
    };

    const token =sessionStorage.getItem("accessToken")

    const response = await fetch('/analyze', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(analyzingText)
    });
    const analyzedText = await response.json()
    console.log(JSON.stringify(analyzedText))
    document.getElementById("output").innerText ="Оценка: " + (analyzedText.weight*100).toFixed(2) + " %"
}