function login() {
    let requestData = {
        "username": document.querySelector("#username").value,
        "password": document.querySelector("#password").value
    };

    let fetchOptions = {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(requestData)
    };

    fetch("/sam/auth/login", fetchOptions)
        .then(response => {
            if (response.status === 200) {
                response.json().then(myJson => {
                    document.querySelector("#result").innerHTML = myJson["token"];
                    localStorage.setItem("myJWT", myJson["token"]);
                    window.location.href = "sam.html";
                });
            } else {
                document.querySelector("#result").innerHTML = "Login failed!";
            }
        });
}

function register() {
    let requestData = {
        "username": document.querySelector("#newUsername").value,
        "password": document.querySelector("#newPassword").value,
        "teamleiderPassword": document.querySelector("#teamleiderPassword").value
    };

    let fetchOptions = {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(requestData)
    };

    fetch("/sam/auth/register", fetchOptions)
        .then(response => {
            if (response.status === 200) {
                document.querySelector("#registerResult").innerHTML = "Registration successful!";
            } else {
                document.querySelector("#registerResult").innerHTML = "Registration failed!";
            }
        });
}

function changePassword() {
    let requestData = {
        "username": document.querySelector("#currentUsername").value,
        "currentPassword": document.querySelector("#currentPassword").value,
        "newPassword": document.querySelector("#newnewPassword").value
    };

    let fetchOptions = {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(requestData)
    };

    fetch("/sam/auth/changepassword", fetchOptions)
        .then(response => {
            if (response.status === 200) {
                document.querySelector("#changePasswordResult").innerHTML = "Password changed successfully!";
            } else {
                document.querySelector("#changePasswordResult").innerHTML = "Password change failed!";
            }
        });
}