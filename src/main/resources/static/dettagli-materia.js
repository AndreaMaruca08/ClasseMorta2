const API_BASE = "http://localhost:8080";

// Prende un parametro dalla query string
function getQueryParam(key) {
    const params = new URLSearchParams(window.location.search);
    return params.get(key);
}

// Prende l'id studente da sessionStorage oppure di default 15
function getStudenteId() {
    return sessionStorage.getItem("idStudente") || 15;
}

// Prende il valore del periodo (stringa backend)
function getPeriodoBackend() {
    const val = document.getElementById("periodo").value;
    switch (val) {
        case "primo":   return "TRIMESTRE";
        case "secondo": return "PENTAMESTRE";
        case "annuale": return "ANNO";
        default:        return "ANNO";
    }
}

// Quando la pagina è pronta, carica voti (e aggiorna a cambio periodo)
document.addEventListener('DOMContentLoaded', () => {
    aggiornaTitoloMateria();
    caricaVoti();

    document.getElementById("periodo").addEventListener("change", caricaVoti);
});

// Aggiorna l'H1 con il nome della materia
function aggiornaTitoloMateria() {
    const nomeMateria = decodeURIComponent(getQueryParam("materia") || "Materia");
    document.getElementById("titolo-materia").textContent = nomeMateria;
}

// --- CARICAMENTO VOTI
async function caricaVoti() {
    const idStudente = getStudenteId();
    const idMateria = getQueryParam("idMateria");
    const periodo = getPeriodoBackend();

    if (!idMateria) {
        alert("Manca idMateria, sistema la chiamata dal bottone dettaglio!");
        return;
    }

    // Chiamata backend - elenco voti
    const res = await fetch(`${API_BASE}/voti/VotiPerMateria?idMateria=${idMateria}&idStudente=${idStudente}&periodo=${periodo}`);
    const json = await res.json();

    const tbody = document.getElementById('elenco-voti');
    tbody.innerHTML = "";

    if (json.message && Array.isArray(json.message)) {
        json.message.forEach(voto => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${voto.data || "-"}</td>
                <td>${voto.voto}</td>
                <td>${voto.periodo || "-"}</td>
            `;
            tbody.appendChild(tr);
        });
    }
    caricaMedieIpotetiche(idMateria, idStudente, periodo, json.message || []);
}

// --- CALCOLO MEDIE IPOTETICHE
async function caricaMedieIpotetiche(idMateria, idStudente, periodo, votiAttuali) {
    const tbody = document.getElementById("medie-ipotetiche");
    tbody.innerHTML = "";

    // Loop da 0 a 10 incluso, step 0.5
    for (let voto = 0; voto <= 10.0; voto += 0.5) {
        // Richiesta al backend per media ipotetica
        const res = await fetch(`${API_BASE}/media/calcolaMedia`, {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({
                idMateria: parseInt(idMateria),
                idStudente: parseInt(idStudente),
                ipotetico: voto,
                periodoVoto: periodo
            })
        });
        const json = await res.json();

        const nuovaMedia = (typeof json.message === "number") ? json.message.toFixed(2) : "-";
        const tr = document.createElement("tr");
        tr.innerHTML = `
            <td>${voto.toFixed(1)}</td>
            <td>${nuovaMedia}</td>
        `;
        tbody.appendChild(tr);
    }
}