let yearSelect = document.getElementById('year')

for(let i = 1998; i < 2020; i++){
    let option = document.createElement("option");
    option.text = i.toString();
    option.value = i.toString();
    yearSelect.appendChild(option);
}


let submitBtn = document.getElementById("submit2")

submitBtn.addEventListener("click", async () => await addCar())

async function addCar(){
    const model = document.getElementById("modelInput").value
    const vat = document.getElementById("vat").value
    const price = document.getElementById("price").value
    const airbags = [
        {
            name: "dairbag",
            value: document.getElementById("dairbag").checked
        },
        {
            name: "pairbag",
            value: document.getElementById("pairbag").checked
        },
        {
            name: "bairbag",
            value: document.getElementById("bairbag").checked
        },
        {
            name: "sairbag",
            value: document.getElementById("sairbag").checked
        }
    ]
    console.log(airbags)
    const year = document.getElementById("year").value
    const color = document.getElementById("color").value

    const response = await fetch("add", {
        method: "post",
        body: JSON.stringify({
            model,
            airbags,
            price,
            vat,
            year,
            color
        })
    })

    const data = await response.json()
    //window.alert(JSON.stringify(data));
    // alert(JSON.stringify(data, null, 3))
}
