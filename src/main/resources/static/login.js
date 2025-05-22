document.addEventListener("DOMContentLoaded", () => {
    const loginForm = document.querySelector("#login-form");
    const messageDiv = document.getElementById("login-message");

    loginForm.addEventListener("submit", async (event) => {
        event.preventDefault();

        messageDiv.textContent = "";

        const email = document.querySelector("#email").value;
        const password = document.querySelector("#password").value;

        const requestData = {
            email: email,
            password: password // MANDIAMO UNA STRINGA, NON array!
        };

        try {
            const response = await fetch("http://localhost:8080/studenti/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(requestData)
            });

            const data = await response.json();

            if (response.ok) {
                if (data.successMessage) {
                    // Salvataggio email (per future richieste in home.js)
                    sessionStorage.setItem("email", email);

                    // Se vuoi potresti anche salvare un nomeUtente/username
                    // (dipende dalla risposta del backend; qui presunto data.nome)
                    if(data.nome) {
                        sessionStorage.setItem("nomeUtente", data.nome);
                    }

                    messageDiv.style.color = "green";
                    messageDiv.textContent = "Login riuscito! Bentornato.";
                    setTimeout(() => {
                        window.location.href = "home.html";
                    }, 1000);
                } else {
                    messageDiv.style.color = "#d90429";
                    messageDiv.textContent = data.message || "Errore sconosciuto.";
                }
            } else {
                messageDiv.style.color = "#d90429";
                messageDiv.textContent = data.message || "Credenziali errate o errore del server.";
            }
        } catch (error) {
            console.error("Errore durante il login", error);
            messageDiv.style.color = "#d90429";
            messageDiv.textContent = "Impossibile connettersi al server. Riprova più tardi.";
        }
    });
});