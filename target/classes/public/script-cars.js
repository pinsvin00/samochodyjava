main();


async function deleteCar(car) {
    await (await fetch("delete", {
        method: "post",
        body: JSON.stringify(car)
    })).json();

    window.location.reload();
}



async function main(){
        const table = document.getElementById("table");
        const referenceRow = document.getElementById("reference-row");

        const response = await fetch("json", {
            method: "post"
        });
        const data = await response.json();

        let i = 1;
        console.log("lol");

        for(const dataRow of data) {
            const row = referenceRow.cloneNode(true);

            const airBagText = `kierowca:${dataRow.airbags[0].value} <br>
                                pasazer: ${dataRow.airbags[1].value} <br>
                                tylnie:  ${dataRow.airbags[2].value} <br>
                                boczne:  ${dataRow.airbags[3].value} <br>`

            row.innerHTML = row.innerHTML.replace("$index", i);
            row.innerHTML = row.innerHTML.replace("$name", dataRow.model);
            row.innerHTML = row.innerHTML.replace("$id", dataRow.uuid);
            row.innerHTML = row.innerHTML.replace("$poduszki", airBagText);
            row.innerHTML = row.innerHTML.replace("$year", dataRow.year);
            row.innerHTML = row.innerHTML.replace("$price", dataRow.price);
            row.innerHTML = row.innerHTML.replace("$vat", dataRow.vat);
            row.getElementsByClassName("color")[0].style = `background-color: ${dataRow.color}`

            row.getElementsByClassName("delete-button")[0].onclick = () => deleteCar(dataRow);
            row.getElementsByClassName("update-button")[0].onclick = () => showUpdateModal(dataRow);


            table.appendChild(row);

            i += 1;
        }

        referenceRow.remove();

}

async function showUpdateModal(car){
    let root = document.documentElement;
    root.style.setProperty('--overlay-display', "flex");

    let btnConfirm = document.getElementById("modal-confirm")
    let btnCancel = document.getElementById("modal-cancel");
    let yearSelect = document.getElementById('year-select');
    let modelInp = document.getElementById('model-input');

    modelInp.value = car.model;

    for(let year = 1998; year < 2020; year++){
        let option = document.createElement("option");

        if(year === car.year){
            option.selected = true
        }

        option.text = year.toString();
        option.value = year.toString();
        yearSelect.appendChild(option);

    }

    btnConfirm.onclick = async () => {
        car.model = modelInp.value
        car.year = yearSelect.value;

        const data = await (await fetch("update", {
            method: "post",
            body: JSON.stringify(car)
        })).json();

        alert(JSON.stringify(data));

        window.location.reload();

    };

    btnCancel.addEventListener('click', ()=>{
        root.style.setProperty('--overlay-display', "none")
    })

}