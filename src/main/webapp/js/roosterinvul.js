function logout() {
    window.location.href = "index.html";
}

const logoutButton = document.querySelector(".home-link");
logoutButton.addEventListener("click", logout);
function handleFormSubmit(event) {
    event.preventDefault();

    var selectedUser = document.getElementById('users-dropdown').value;
    var roosterData = {};

    var dayInputs = document.getElementsByClassName('day-input');
    for (var i = 0; i < dayInputs.length; i++) {
        var dayInput = dayInputs[i];
        var day = dayInput.getAttribute('data-day');
        var time = dayInput.value;
        roosterData[day] = time;
    }

    var data = {
        rooster: JSON.stringify(roosterData),
        username: selectedUser
    };

    fetch('sam/rooster/roosterinvul', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(function(response) {
            if (response.ok) {
                console.log('Rooster saved successfully!');
                alert('Rooster saved successfully!');
            } else {
                console.error('Error saving rooster:', response.statusText);
                alert('Error saving rooster.');
            }
        })
        .catch(function(error) {
            console.error('Error saving rooster:', error);
            alert('Error saving rooster.');
        });
}

function updateAvailabilityTable(selectedUser) {
    fetch('data/beschikbaarheden.json')
        .then(function(response) {
            return response.json();
        })
        .then(function(data) {
            var availabilityData = data.find(function(item) {
                return item.username === selectedUser;
            });

            var tableBody = document.querySelector('#availability-table tbody');
            tableBody.innerHTML = '';

            var beschikbaarheidRow = document.createElement('tr');
            var days = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];
            days.forEach(function(day) {
                var beschikbaarheidCell = document.createElement('td');
                beschikbaarheidCell.textContent = availabilityData[day.toLowerCase()] || '';
                beschikbaarheidRow.appendChild(beschikbaarheidCell);
            });
            tableBody.appendChild(beschikbaarheidRow);
        })
        .catch(function(error) {
            console.error('Error fetching users data:', error);
        });
}

function updateVrijaanvraagTable() {
    fetch('data/vrijaanvraag.json')
        .then(function(response) {
            return response.json();
        })
        .then(function(data) {
            var tableBody = document.querySelector('#vrijaanvraag-table tbody');
            tableBody.innerHTML = '';

            data.forEach(function(item) {
                var vrijaanvraagRow = document.createElement('tr');
                var vrijaanvraagCell = document.createElement('td');
                var usernameCell = document.createElement('td');

                vrijaanvraagCell.textContent = item.vrijaanvraag;
                usernameCell.textContent = item.username;

                vrijaanvraagRow.appendChild(vrijaanvraagCell);
                vrijaanvraagRow.appendChild(usernameCell);
                tableBody.appendChild(vrijaanvraagRow);
            });
        })
        .catch(function(error) {
            console.error('Error fetching vrijaanvraag data:', error);
        });
}

document.addEventListener('DOMContentLoaded', function() {
    var dropdown = document.getElementById('users-dropdown');

    dropdown.addEventListener('change', function() {
        var selectedUser = this.value;
        document.getElementById('availability-header').textContent = 'Beschikbaarheid van geselecteerde gebruiker: ' + selectedUser;
        updateAvailabilityTable(selectedUser);
    });

    fetch('data/beschikbaarheden.json')
        .then(function(response) {
            return response.json();
        })
        .then(function(data) {
            data.forEach(function(user) {
                var option = document.createElement('option');
                option.value = user.username;
                option.text = user.username;
                dropdown.appendChild(option);
            });

            var selectedUser = dropdown.value;
            document.getElementById('availability-header').textContent = 'Beschikbaarheid van geselecteerde gebruiker: ' + selectedUser;
            updateAvailabilityTable(selectedUser);
        })
        .catch(function(error) {
            console.error('Error fetching users data:', error);
        });

    updateVrijaanvraagTable();
});

var roosterForm = document.getElementById('rooster-form');
roosterForm.addEventListener('submit', handleFormSubmit);
