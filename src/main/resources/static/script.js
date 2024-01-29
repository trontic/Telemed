const today = new Date().toISOString().split('T')[0];
document.getElementById('date').setAttribute('max', today);

document.addEventListener('DOMContentLoaded', function () {

    var adviceSwitch = document.getElementById('adviceSwitch');
    var adviceText = document.querySelector('.advice-text');

    adviceSwitch.addEventListener('change', function () {
        adviceText.style.display = this.checked ? 'block' : 'none';
    });
});


function toggleEmergencyCheck() {
    var emergencyCheck = document.getElementById("emergencyCheck");
    var toggleCheckbox = document.getElementById("toggleCheckbox");

    // Toggle the state of toggleCheckbox based on emergencyCheck
    toggleCheckbox.checked = !emergencyCheck.checked;

    // Update other checkboxes based on the state of toggleCheckbox
    updateTargetCheckboxes(toggleCheckbox.checked);

    var checkbox = document.getElementById("emergencyCheck");
    var regularTherapy = document.querySelector(".regular-therapy");

        // Check if the checkbox is checked
        if (checkbox.checked) {
            // If not checked, hide the therapy menu
            regularTherapy.style.display = "none";
        } else {
            // If checked, show the therapy menu
            regularTherapy.style.display = "block";
        }


}

function toggleTargetCheckboxes() {
    // Update other checkboxes based on the state of toggleCheckbox
    var toggleCheckbox = document.getElementById("toggleCheckbox");
    updateTargetCheckboxes(toggleCheckbox.checked);
}

function updateTargetCheckboxes(state) {
    var targetCheckboxes = document.getElementsByClassName("targetCheckbox");
    for (var i = 0; i < targetCheckboxes.length; i++) {
        targetCheckboxes[i].checked = state;
    }
}

/*
function toggleTherapyMenu() {
    var therapyMenu = document.querySelector('.therapy-menu');
    var nameInput = document.getElementById('name');
    var quantityInput = document.getElementById('quantity');

    // Check if the checkbox is checked
    if (document.getElementById('iregularCheck').checked) {
        therapyMenu.style.display = 'block'; // Show the therapy menu
        nameInput.disabled = false;          // Enable name input
        quantityInput.disabled = false;      // Enable quantity input

    } else {
        therapyMenu.style.display = 'none';  // Hide the therapy menu
        nameInput.disabled = true;           // Disable name input
        quantityInput.disabled = true;       // Disable quantity input

    }
}
*/

function toggleTherapyMenu() {
    var checkbox = document.getElementById("iregularCheck");
    var nameInput = document.getElementById("name");
    var quantityInput = document.getElementById("quantity");
    var therapyMenu = document.querySelector(".therapy-menu");

    // Toggle display of the therapy menu
    therapyMenu.style.display = checkbox.checked ? "block" : "none";

    // Enable or disable inputs based on checkbox state
    nameInput.disabled = !checkbox.checked;
    quantityInput.disabled = !checkbox.checked;

    // Set or remove 'required' attribute based on checkbox state
    if (checkbox.checked) {
        nameInput.setAttribute("required", "required");
        quantityInput.setAttribute("required", "required");
    } else {
        nameInput.removeAttribute("required");
        quantityInput.removeAttribute("required");
    }
}