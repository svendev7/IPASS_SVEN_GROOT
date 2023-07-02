
    function getCurrentUser() {
    return fetch('data/currentuser.json')
    .then(response => response.json())
    .then(data => {
    if (data && data.length > 0) {
    const username = data[0].username;
    return Promise.resolve({ username: username });
} else {
    return Promise.reject('User data not found.');
}
})
    .catch(error => {
    console.error('Error:', error);
});
}

    getCurrentUser()
    .then(data => {
    if (data && data.username) {
    const username = data.username;
    const usernameElement = document.getElementById('username');
    usernameElement.textContent = username;
    updateRosterData(username);
}
})
    .catch(error => {
    console.error('Error:', error);
});

    function getRosterData(selectedUser) {
    return fetch('data/rooster.json')
    .then(response => response.json())
    .then(data => {
    const userData = data.filter(item => item.username === selectedUser);
    if (userData.length > 0) {
    const roosterData = JSON.parse(userData[0].rooster);
    return Promise.resolve(roosterData);
} else {
    return Promise.reject('Rooster data not found for the selected user.');
}
})
    .catch(error => {
    console.error('Error:', error);
    throw error; // Rethrow the error to be caught by the outer catch block
});
}

    function updateRosterData(selectedUser) {
    getRosterData(selectedUser)
        .then(roosterData => {
            document.getElementById('rosterMonday').textContent = roosterData.Monday || '';
            document.getElementById('rosterTuesday').textContent = roosterData.Tuesday || '';
            document.getElementById('rosterWednesday').textContent = roosterData.Wednesday || '';
            document.getElementById('rosterThursday').textContent = roosterData.Thursday || '';
            document.getElementById('rosterFriday').textContent = roosterData.Friday || '';
            document.getElementById('rosterSaturday').textContent = roosterData.Saturday || '';
            document.getElementById('rosterSunday').textContent = roosterData.Sunday || '';
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Geen rooster beschikbaar');
        });
}

    // Calculate the dates for each day of the current week
    const today = new Date();
    const currentDay = today.getDay();
    const monday = new Date(today.getFullYear(), today.getMonth(), today.getDate() - currentDay + 1);
    const dates = [];
    for (let i = 0; i < 7; i++) {
    const date = new Date(monday);
    date.setDate(date.getDate() + i);
    dates.push(date);
}

    // Update the dates for each day in the HTML
    document.getElementById('mondayDate').textContent = formatDate(dates[0]);
    document.getElementById('tuesdayDate').textContent = formatDate(dates[1]);
    document.getElementById('wednesdayDate').textContent = formatDate(dates[2]);
    document.getElementById('thursdayDate').textContent = formatDate(dates[3]);
    document.getElementById('fridayDate').textContent = formatDate(dates[4]);
    document.getElementById('saturdayDate').textContent = formatDate(dates[5]);
    document.getElementById('sundayDate').textContent = formatDate(dates[6]);
    document.getElementById('mondayDate2').textContent = formatDate(dates[0]);
    document.getElementById('tuesdayDate2').textContent = formatDate(dates[1]);
    document.getElementById('wednesdayDate2').textContent = formatDate(dates[2]);
    document.getElementById('thursdayDate2').textContent = formatDate(dates[3]);
    document.getElementById('fridayDate2').textContent = formatDate(dates[4]);
    document.getElementById('saturdayDate2').textContent = formatDate(dates[5]);
    document.getElementById('sundayDate2').textContent = formatDate(dates[6]);

    // Function to format the date as "YYYY-MM-DD"
    function formatDate(date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
}

    document.getElementById('availabilityForm').addEventListener('submit', saveAvailability);

    function saveAvailability(event) {
    event.preventDefault();

    const availabilityData = {
    username: document.getElementById('username').textContent,
    monday: document.getElementById('monday').value,
    tuesday: document.getElementById('tuesday').value,
    wednesday: document.getElementById('wednesday').value,
    thursday: document.getElementById('thursday').value,
    friday: document.getElementById('friday').value,
    saturday: document.getElementById('saturday').value,
    sunday: document.getElementById('sunday').value
}

    const jsonData = JSON.stringify(availabilityData);

    fetch('sam/savebeschikbaarheid/saveavailability', {
    method: 'POST',
    headers: {
    'Content-Type': 'application/json'
},
    body: jsonData
})
    .then(response => {
    if (response.ok) {
    document.getElementById('monday').value = '';
    document.getElementById('tuesday').value = '';
    document.getElementById('wednesday').value = '';
    document.getElementById('thursday').value = '';
    document.getElementById('friday').value = '';
    document.getElementById('saturday').value = '';
    document.getElementById('sunday').value = '';
    alert('Availability saved successfully.');
    console.log('Availability saved successfully.');
} else {
    alert('Error: ' + response.statusText);
    console.error('Error:', response.statusText);
}
})
    .catch(error => {
    console.error('Error:', error);
    alert('Failed to save availability.');
});
}
    function handleFormSubmit(event) {
    event.preventDefault();

    var selectedUser = document.getElementById('username').textContent;
    var startDate = document.getElementById('startDate').value;
    var endDate = document.getElementById('endDate').value;
    var date = startDate +", "+ endDate;

    var data = {
    username: selectedUser,
    vrijaanvraag: JSON.stringify(date)

};

    fetch('sam/vrij/vrijaanvraag', {
    method: 'POST',
    headers: {
    'Content-Type': 'application/json'
},
    body: JSON.stringify(data)
})
    .then(function (response) {
    if (response.ok) {
    console.log('Vrij aanvraag saved successfully!');
    alert('Vrij aanvraag saved successfully!');
} else {
    console.error('Error saving Vrij aanvraag:', response.statusText);
    alert('Error saving Vrij aanvraag.');
}
})
    .catch(function (error) {
    console.error('Error saving Vrij aanvraag:', error);
    alert('Error saving Vrij aanvraag.');
});
}

    var vrijAanvraagForm = document.getElementById('vrijAanvraagForm');
    vrijAanvraagForm.addEventListener('submit', handleFormSubmit);
