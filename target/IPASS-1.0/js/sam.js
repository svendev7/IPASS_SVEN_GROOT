function logout() {
    window.location.href = "index.html";
}

const logoutButton = document.querySelector(".logout-button a");
logoutButton.addEventListener("click", logout);

function roosterInzien() {
    window.location.href = "rooster.html";
}

const inzienButton = document.querySelector(".home-link-image1");
inzienButton.addEventListener("click", roosterInzien);

function roosterInvul() {
    var password = prompt("Enter the teamleider password:");
    if (password === "teamleider123") {
        window.location.href = "roosterinvul.html";
    } else {
        alert("Incorrect password. Access denied.");
    }
}

const roosterButton = document.querySelector(".home-link-image2");
roosterButton.addEventListener("click", roosterInvul);

// Fetch the nieuws data from the server
fetch('http://localhost:8080/sam/nieuws/nieuws')
    .then(response => response.json())
    .then(data => {
        // Update the nieuws data in the HTML
        const nieuwsElement = document.querySelector(".home-text07");
        nieuwsElement.innerHTML += "<br>" + data.datum + "<br>" + data.informatie + "<br>" + data.acties;
    })
    .catch(error => {
        console.error("Error fetching nieuws data:", error);
    });