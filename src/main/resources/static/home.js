const API_BASE = "http://localhost:8080";

// Ottieni idStudente da sessionStorage (o default 15)
function getStudenteId() {
    return sessionStorage.getItem("idStudente") || 6;
}
/**
 * Funzione per ottenere il PHPSESSID via POST
 */
async function ottieniPHPSESSID(codiceUtente, password) {
    try {
        const response = await fetch(`${API_BASE}/importa/getPHPSESSID`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                codiceUtente: codiceUtente,
                password: password
            })
        });

        if (!response.ok) {
            throw new Error("Impossibile ottenere il PHPSESSID");
        }

        return await response.json();
    } catch (error) {
        console.error(error);
        return { success: false, message: error.message };
    }
}

/**
 * Funzione di mapping selector periodo => stringa backend
 */
function getPeriodoBackend() {
    const val = document.getElementById("periodo").value;
    switch (val) {
        case "primo":   return "TRIMESTRE";
        case "secondo": return "PENTAMESTRE";
        case "annuale": return "ANNO";
        default:        return "ANNO";
    }
}

/**
 * Chiama il backend e stampa media totale e ogni materia con la media nella tabella
 */
async function aggiornaTutto() {
    const ID_STUDENTE = getStudenteId();

    // 1. Recupera le materie
    let materieRes = await fetch(`${API_BASE}/materie?idStudente=${ID_STUDENTE}`);
    let materieJson;
    try {
        materieJson = await materieRes.json();
    } catch { materieJson = {message: []}; }

    const tbody = document.getElementById("elenco-materie");
    tbody.innerHTML = '';
    const periodo = getPeriodoBackend();

    const promises = (materieJson.message || []).map(async (mat) => {
        try {
            const resp = await fetch(`${API_BASE}/media/calcolaMedia`, {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify({
                    idMateria: mat.id,
                    idStudente: ID_STUDENTE,
                    ipotetico: -1.0,
                    periodoVoto: periodo
                })
            });
            const mediaJson = await resp.json();
            return {
                nome: mat.nome,
                media: typeof mediaJson.message === "number" ? mediaJson.message : "-"
            };
        } catch {
            return { nome: mat.nome, media: "-" };
        }
    });

    const result = await Promise.all(promises);

    for(let i = 0; i < result.length; i++) {
        const r = result[i];
        const mat = materieJson.message[i];
        const tr = document.createElement('tr');
        tr.innerHTML = `<td>${r.nome}</td>
                        <td>${r.media}</td>
                        <td>
                            <button class="btn dettagli-btn" 
                                data-materia="${encodeURIComponent(r.nome)}" 
                                data-id="${mat.id}">
                                Dettagli
                            </button>
                        </td>`;
        tbody.appendChild(tr);
    }

    tbody.querySelectorAll(".dettagli-btn").forEach(btn => {
        btn.addEventListener("click", function() {
            const materia = this.getAttribute("data-materia");
            const idMateria = this.getAttribute("data-id");
            window.location.href = `materia.html?materia=${materia}&idMateria=${idMateria}`;
        });
    });

    // 3. Media totale (per periodo)
    try {
        const mediaTotaleRes = await fetch(`${API_BASE}/media/mediaAll`, {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({
                idMateria: -1,
                idStudente: ID_STUDENTE,
                ipotetico: -1.0,
                periodoVoto: periodo
            })
        });
        const mediaTotaleJson = await mediaTotaleRes.json();
        document.getElementById('media-totale').textContent =
            typeof mediaTotaleJson.message === "number" ? mediaTotaleJson.message : "-";
    } catch {
        document.getElementById('media-totale').textContent = "-";
    }
}

document.getElementById("btn-importa-voti").onclick = function() {
    document.getElementById("import-form-container").style.display = "";
    document.getElementById("import-status").textContent = "";
};

function chiudiFormImport() {
    document.getElementById("import-form-container").style.display = "none";
}


async function salvaVotiConMaterie(votiImportati, studenteId) {
    for (const voto of votiImportati) {
        // 1. Controlla se la materia esiste
        const materiaPayload = { nome: voto.nomeMateria, id: studenteId };
        const existsRes = await fetch(`${API_BASE}/materie/exists`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(materiaPayload)
        });
        const existsJson = await existsRes.json();
        if (!existsJson.data) {
            await fetch(`${API_BASE}/materie`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(materiaPayload)
            });
        }

        // 2. Salva il voto
        const votoPayload = {
            valore: voto.valore,
            nomeMateria: voto.nomeMateria,
            periodo: voto.periodo,
            idStudente: studenteId
        };
        await fetch(`${API_BASE}/voti`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(votoPayload)
        });
    }
}

// MODIFICA LA SUBMIT DEL FORM DI IMPORTAZIONE:
document.getElementById("import-form").onsubmit = async function(e) {
    e.preventDefault();
    console.log("submit partito");
    const codiceUtente = document.getElementById("codiceUtente").value;
    const password = document.getElementById("password").value;
    let studenteID = null;
    const studIDEl = document.getElementById("studenteID");
    if (studIDEl && studIDEl.value && studIDEl.value.trim() !== "") {
        studenteID = studIDEl.value.trim();
    } else {
        studenteID = getStudenteId();
    }

    const status = document.getElementById("import-status");
    status.textContent = "Recupero il PHPSESSID in corso...";

    const json = await ottieniPHPSESSID(codiceUtente, password);
    console.log("ottenuto json", json);

    if (!json.success) {
        status.textContent = "Errore: " + (json.message || "Impossibile ottenere PHPSESSID. Controlla credenziali.");
        return;
    }
    console.log("PHPSESSID", json.message);

    // Qui logga assolutamente!
    console.log("Chiamo sync-voti");
    const resp = await fetch(`${API_BASE}/importa/sync-voti?phpSessId=${encodeURIComponent(phpsessid)}&studenteID=${encodeURIComponent(studenteID)}`, {
        method: "POST"
    });
    console.log("sync-voti response", resp);

    const result = await resp.json();

    if (!result.success) {
        status.textContent = "Errore nell'importazione: " + (result.message || "Operazione fallita!");
        return;
    }

    status.textContent = "Importazione completata!";
    aggiornaTutto(); // aggiorna la tabella materie/media
};

/**
 * Aggiorna per cambio periodo
 */
document.getElementById("periodo").addEventListener("change", aggiornaTutto);

window.addEventListener("DOMContentLoaded", () => {
    aggiornaTutto();
});